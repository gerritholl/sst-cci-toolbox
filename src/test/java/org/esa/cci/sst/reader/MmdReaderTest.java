/*
 * Copyright (C) 2010 Brockmann Consult GmbH (info@brockmann-consult.de)
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

import org.esa.cci.sst.Constants;
import org.esa.cci.sst.data.DataFile;
import org.esa.cci.sst.data.Observation;
import org.esa.cci.sst.data.ReferenceObservation;
import org.esa.cci.sst.data.RelatedObservation;
import org.esa.cci.sst.data.VariableDescriptor;
import org.esa.cci.sst.orm.PersistenceManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.postgis.Geometry;
import org.postgis.Point;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import static org.junit.Assert.*;

/**
 * @author Thomas Storm
 */
public class MmdReaderTest {

    static final String TEST_WITH_ACTUAL_DATA = "test_with_actual_data.nc";

    private MmdReader mmdReader;

    @Before
    public void setUp() throws Exception {
        final Properties config = new Properties();
        config.setProperty("openjpa.ConnectionDriverName", "org.postgresql.Driver");
        config.setProperty("openjpa.ConnectionURL", "jdbc:postgresql://10.3.0.35:5432/mygisdb");
        config.setProperty("openjpa.ConnectionUserName", "mms");
        config.setProperty("openjpa.ConnectionPassword", "mms");
        config.setProperty("openjpa.Log", "DefaultLevel=INFO,SQL=INFO");
        config.setProperty("openjpa.jdbc.SynchronizeMappings", "buildSchema");
        final PersistenceManager persistenceManager = new PersistenceManager(Constants.PERSISTENCE_UNIT_NAME, config);
        mmdReader = new MmdReader(persistenceManager);
    }

    @After
    public void tearDown() throws Exception {
        try {
            mmdReader.close();
        } catch (IllegalStateException ignore) {
            // ok
        }
        mmdReader = null;
    }

    @Test(expected = IOException.class)
    public void testFailingInit() throws Exception {
        final DataFile dataFile = new DataFile();
        dataFile.setPath("pom.xml");

        mmdReader.init(dataFile);
    }

    @Test(expected = IllegalStateException.class)
    public void testFailingClose() throws Exception {
        mmdReader.close();
    }

    @Test
    public void testInit() throws Exception {
        initMmdReader(TEST_WITH_ACTUAL_DATA);
        final Field mmd = mmdReader.getClass().getDeclaredField("mmd");
        mmd.setAccessible(true);
        final NetcdfFile mmdObj = (NetcdfFile) mmd.get(mmdReader);

        assertNotNull(mmdObj);
        final String location = mmdObj.getLocation();
        assertEquals(TEST_WITH_ACTUAL_DATA, location.substring(location.lastIndexOf("/") + 1, location.length()));
    }

    @Test
    public void testGetNumRecords() throws Exception {
        initMmdReader(TEST_WITH_ACTUAL_DATA);
        assertEquals(10, mmdReader.getNumRecords());
    }

    @Test
    public void testSetObservationLocationToFirstMatchup() throws Exception {
        initMmdReader(TEST_WITH_ACTUAL_DATA);
        final RelatedObservation observation = new RelatedObservation();
        mmdReader.setObservationLocation(observation, 0);

        final Point expectedFirstPoint = new Point(-48.65955, 3.351261);
        final Geometry geometry = observation.getLocation().getGeometry();
        final Point actualFirstPoint = geometry.getFirstPoint();
        final Point actualLastPoint = geometry.getLastPoint();
        System.out.println("actualLastPoint = " + actualLastPoint);

        assertEquals(expectedFirstPoint.x, actualFirstPoint.x, 0.0001);
        assertEquals(expectedFirstPoint.y, actualFirstPoint.y, 0.0001);
    }

    @Test
    public void testSetObservationLocationToLastMatchup() throws Exception {
        initMmdReader(TEST_WITH_ACTUAL_DATA);
        final RelatedObservation observation = new RelatedObservation();
        mmdReader.setObservationLocation(observation, 9);

        final Point expectedLastPoint = new Point(-53.55205, 14.84941);
        final Point actualLastPoint = observation.getLocation().getGeometry().getLastPoint();

        assertEquals(expectedLastPoint.x, actualLastPoint.x, 0.0001);
        assertEquals(expectedLastPoint.y, actualLastPoint.y, 0.0001);
    }

    @Test
    public void testGetEndOrigin() throws Exception {
        initMmdReader(TEST_WITH_ACTUAL_DATA);
        final int[] endOrigin = mmdReader.getEndOrigin(5);
        assertArrayEquals(new int[]{5, 100, 100}, endOrigin);
    }

    @Test
    public void testReadObservation() throws Exception {
        initMmdReader(TEST_WITH_ACTUAL_DATA);
        final Observation firstObservation = mmdReader.readObservation(0);
        final Observation secondObservation = mmdReader.readObservation(1);

        assertTrue(firstObservation.getDatafile().getPath().endsWith(TEST_WITH_ACTUAL_DATA));
        assertEquals("mmd_observation_0", firstObservation.getName());
        assertEquals(55229, firstObservation.getRecordNo());
        assertEquals(55230, secondObservation.getRecordNo());
        assertEquals("ARC", firstObservation.getSensor());
    }

    @Test(expected = IOException.class)
    public void testReadObservationFails() throws Exception {
        initMmdReader(TEST_WITH_ACTUAL_DATA);
        mmdReader.readObservation(10);
    }

    @Test
    public void testGetCreationDate() throws Exception {
        initMmdReader(TEST_WITH_ACTUAL_DATA);
        final ReferenceObservation observation = new ReferenceObservation();
        mmdReader.setObservationLocation(observation, 0);
        final Date actualDate = mmdReader.getCreationDate(8368401, observation);
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        final Date expectedDate = sdf.parse("2010-06-01 08-18-29");
        assertEquals(expectedDate.getTime(), actualDate.getTime());
    }

    @Test
    public void testGetSSTVariable() throws Exception {
        initMmdReader(TEST_WITH_ACTUAL_DATA);
        final Variable sstVariable = mmdReader.getSSTVariable();
        assertNotNull(sstVariable);
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteThrowsException() throws Exception {
        mmdReader.write(null, null, "", "", 0, null, null);
    }

    @Test
    public void testGetVariableDescriptors() throws Exception {
        initMmdReader(TEST_WITH_ACTUAL_DATA);
        final VariableDescriptor[] variableDescriptors = mmdReader.getVariableDescriptors();

        assertEquals(8, variableDescriptors.length);

        for (VariableDescriptor variableDescriptor : variableDescriptors) {
            assertTrue(variableDescriptor.getName().startsWith("ARC3."));
            assertEquals("ARC", variableDescriptor.getDataSchema().getSensorType());
            assertEquals("mmd", variableDescriptor.getDataSchema().getName());
        }

    }

    private void initMmdReader(final String filename) throws IOException {
        final DataFile dataFile = new DataFile();
        final File file = new File(getClass().getResource(filename).getFile());
        dataFile.setPath(file.getPath());
        mmdReader.init(dataFile);
    }

}
