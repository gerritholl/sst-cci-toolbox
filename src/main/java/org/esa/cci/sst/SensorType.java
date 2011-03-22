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

package org.esa.cci.sst;

/**
 * Enumeration of sensor types.
 *
 * @author Ralf Quast
 * @author Thomas Storm
 */
public enum SensorType {

    ATSR_MD(0x01, "atsr_md"),
    METOP(0x02, "metop"),
    SEVIRI(0x04, "seviri"),
    AVHRR(0x08, "avhrr_m01", "avhrr_m02", "avhrr_m03", "avhrr_10", "avhrr_11", "avhrr_12", "avhrr_14", "avhrr_15",
          "avhrr_16", "avhrr_17", "avhrr_18", "avhrr_19"),
    AMSRE(0x10, "amsre"),
    TMI(0x20, "tmi"),
    ATSR(0x40, "atsr1", "atsr2", "aatsr"),
    AAI(0x80, "aai"),
    SEAICE(0x0100, "seaice"),
    INSITU(0x0200, "insitu");

    private final long pattern;
    private final String[] sensors;

    private SensorType(long pattern, String... sensors) {
        this.pattern = pattern;
        this.sensors = sensors;
    }

    public long getPattern() {
        return pattern;
    }

    public String getSensor() {
        return sensors[0];
    }

    public String[] getSensors() {
        return sensors.clone();
    }

    public String nameLowerCase() {
        // todo - eliminate usages of this method (rq-20110322)
        return getSensor();
    }

    public static boolean isSensorType(String name) {
        final SensorType[] values = SensorType.values();
        for (final SensorType sensorType : values) {
            if (sensorType.name().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public static SensorType valueOfIgnoreCase(String name) {
        return valueOf(name.toUpperCase());
    }
}
