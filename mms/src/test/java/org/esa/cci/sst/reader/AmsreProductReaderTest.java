/*
 * Copyright (C) 2011 Brockmann Consult GmbH (info@brockmann-consult.de)
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option)
 * any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, see http://www.gnu.org/licenses/
 */

package org.esa.cci.sst.reader;

import org.esa.beam.framework.datamodel.GeoCoding;
import org.esa.beam.framework.datamodel.GeoPos;
import org.esa.beam.framework.datamodel.PixelPos;
import org.esa.cci.sst.TestHelper;
import org.esa.cci.sst.data.*;
import org.esa.cci.sst.util.PgUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.postgis.Geometry;
import org.postgis.Point;
import ucar.ma2.InvalidRangeException;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

//@RunWith(IoTestRunner.class)
// reading zipped test product blocks execution here - tests disabled tb 2014-02-12
@Ignore
public class AmsreProductReaderTest {

    private static final String AMSR_RESOURCE_NAME = "20100601-AMSRE-REMSS-L2P-amsr_l2b_v05_r42970.dat-v01.nc.gz";

    private static DataFile dataFile;
    private static GunzipDecorator handler;
    private static AbstractProductReader productReader;

    @BeforeClass
    public static void init() throws IOException, URISyntaxException {
        final String filePath = TestHelper.getResourcePath(AmsreProductReaderTest.class, AMSR_RESOURCE_NAME);
        final File file = new File(filePath);
        assertTrue(file.isFile());

        productReader = new ProductReader("amsre");
        dataFile = new DataFile();
        dataFile.setPath(file.getPath());
        final Sensor sensor = new SensorBuilder().
                name(productReader.getSensorName()).
                observationType(GlobalObservation.class).
                build();
        dataFile.setSensor(sensor);
        handler = new GunzipDecorator(productReader);
        handler.init(dataFile, null);
    }

    @AfterClass
    public static void clean() {
        if (handler != null) {
            handler.close();
        }
    }

    @Test
    public void testGeoCoding() throws URISyntaxException, IOException {
        final GeoCoding geoCoding = productReader.getProduct().getGeoCoding();
        final float wantedLat = -47.9727f;
        final float wantedLon = 18.7432f;
        final GeoPos g = new GeoPos(wantedLat, wantedLon);
        final PixelPos p = new PixelPos();

        geoCoding.getPixelPos(g, p);

        assertTrue(p.isValid());

        geoCoding.getGeoPos(p, g);

        final float actualLat = g.getLat();
        final float actualLon = g.getLon();
        final float actualDelta = delta(wantedLat, wantedLon, actualLat, actualLon);

        // check that delta for pixel position found is the least
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                p.setLocation(p.x + j, p.y + i);
                geoCoding.getGeoPos(p, g);

                assertTrue(actualDelta <= delta(wantedLat, wantedLon, g.lat, g.lon));
            }
        }

        // check that pixel position is exact when geo-position coincides with a pixel center
        geoCoding.getGeoPos(new PixelPos(0.5f, 0.5f), g);
        geoCoding.getPixelPos(g, p);

        assertTrue(p.isValid());
        assertEquals(0.5, p.getX(), 0.0);
        assertEquals(0.5, p.getY(), 0.0);
    }

    @Test
    public void testGetNumRecords() throws URISyntaxException, IOException {
        assertEquals(1, handler.getNumRecords());
    }

    @Test
    public void testReadObservation() throws IOException, InvalidRangeException, URISyntaxException {
        final Observation observation = handler.readObservation(0);

        assertSame(dataFile, observation.getDatafile());
        assertTrue(observation instanceof RelatedObservation);

        final RelatedObservation relatedObservation = (RelatedObservation) observation;
        assertNotNull(relatedObservation.getLocation());

        final Geometry geometry = relatedObservation.getLocation().getGeometry();

        assertTrue(geometry.checkConsistency());
        assertFalse(PgUtil.isClockwise(getPoints(geometry)));
    }

    @Test
    public void testGetColumns() throws URISyntaxException, IOException {
        final Item[] columns = handler.getColumns();

        assertEquals(12, columns.length);
    }

    private static float delta(float wantedLat, float wantedLon, float lat, float lon) {
        return (lat - wantedLat) * (lat - wantedLat) + (lon - wantedLon) * (lon - wantedLon);
    }

    private static List<Point> getPoints(Geometry geometry) {
        final List<Point> pointList = new ArrayList<>(geometry.numPoints());
        for (int i = 0; i < geometry.numPoints(); i++) {
            pointList.add(geometry.getPoint(i));
        }
        return pointList;
    }
}
