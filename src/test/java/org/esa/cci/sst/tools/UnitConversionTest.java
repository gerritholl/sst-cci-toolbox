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

package org.esa.cci.sst.tools;

import org.esa.cci.sst.ColumnRegistry;
import org.esa.cci.sst.data.ColumnBuilder;
import org.esa.cci.sst.data.Item;
import org.esa.cci.sst.rules.Converter;
import org.esa.cci.sst.rules.Rule;
import org.esa.cci.sst.rules.RuleException;
import org.esa.cci.sst.rules.RuleFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ucar.ma2.Array;
import ucar.ma2.DataType;

import static org.junit.Assert.*;

/**
 * Illustrates how a unit conversion can be carried out.
 *
 * @author Ralf Quast
 */
public class UnitConversionTest {

    private static final String TIME_VARIABLE_NAME = "time";
    private static final DataType TIME_VARIABLE_TYPE = DataType.DOUBLE;
    private static final double JULIAN_DATE_OF_EPOCH_1978 = 2443509.5;

    private ColumnRegistry registry = new ColumnRegistry();

    @Test
    public void testColumnConversion() {
        final Item targetColumn = registry.getColumn(TIME_VARIABLE_NAME);

        assertEquals(TIME_VARIABLE_NAME, targetColumn.getName());
        assertEquals(TIME_VARIABLE_TYPE.name(), targetColumn.getType());
        assertEquals("seconds since 1978-01-01 00:00:00", targetColumn.getUnit());

        final Item sourceColumn = registry.getSourceColumn(targetColumn);

        assertNotNull(sourceColumn);
        assertEquals(TIME_VARIABLE_NAME, sourceColumn.getName());
        assertEquals(TIME_VARIABLE_TYPE.name(), sourceColumn.getType());
        assertEquals("Julian Date", sourceColumn.getUnit());
    }

    @Test
    public void testNumericConversion() throws RuleException {
        final Item targetColumn = registry.getColumn(TIME_VARIABLE_NAME);
        final Item sourceColumn = registry.getSourceColumn(targetColumn);
        final Converter converter = registry.getConverter(targetColumn);

        assertNotNull(targetColumn);
        assertNotNull(sourceColumn);
        assertNotSame(targetColumn, sourceColumn);
        assertNotNull(converter);

        final Array sourceArray = Array.factory(DataType.valueOf(sourceColumn.getType()), new int[]{2});
        sourceArray.setDouble(0, JULIAN_DATE_OF_EPOCH_1978);
        sourceArray.setDouble(1, JULIAN_DATE_OF_EPOCH_1978 + 1.0);
        final Array targetArray = converter.apply(sourceArray);

        assertEquals(0.0, targetArray.getDouble(0), 0.0);
        assertEquals(86400.0, targetArray.getDouble(1), 0.0);
    }

    @Before
    public void setUp() throws Exception {
        final Rule rule = RuleFactory.getInstance().getRule("JulianDateToSeconds");
        final ColumnBuilder builder = new ColumnBuilder();
        builder.name(TIME_VARIABLE_NAME);
        builder.type(TIME_VARIABLE_TYPE);
        builder.unit("Julian Date");

        registry.register(rule, builder.build());
    }

    @After
    public void clearRegistry() {
        registry.clear();
    }
}
