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

import org.esa.beam.framework.datamodel.GeoPos;
import org.esa.beam.framework.datamodel.PixelGeoCoding;
import org.esa.beam.framework.datamodel.PixelPos;
import org.esa.cci.sst.util.PixelFinder;
import org.esa.cci.sst.util.QuadTreePixelFinder;
import org.esa.cci.sst.util.RasterDataNodeSampleSource;

/**
 * A pixel geo-coding, which uses a different quad-tree algorithm as fallback
 * when a pixel position has not been found. (BEAM-1240)
 *
 * @author Ralf Quast
 */
public class PixelGeoCodingWithFallback extends ForwardingGeoCoding {

    private final PixelFinder pixelFinder;

    public PixelGeoCodingWithFallback(PixelGeoCoding pixelGeoCoding) {
        super(pixelGeoCoding);
        this.pixelFinder = new QuadTreePixelFinder(
                new RasterDataNodeSampleSource(pixelGeoCoding.getLonBand()),
                new RasterDataNodeSampleSource(pixelGeoCoding.getLatBand()));
    }

    public PixelGeoCodingWithFallback(PixelGeoCoding pixelGeoCoding, PixelFinder pixelFinder) {
        super(pixelGeoCoding);
        this.pixelFinder = pixelFinder;
    }

    @Override
    public PixelPos getPixelPos(GeoPos geoPos, PixelPos pixelPos) {
        super.getPixelPos(geoPos, pixelPos);
        if (geoPos.isValid()) {
            if (!pixelPos.isValid()) {
                pixelFinder.findPixel(geoPos.getLon(), geoPos.getLat(), pixelPos);
            }
        }
        return pixelPos;
    }
}
