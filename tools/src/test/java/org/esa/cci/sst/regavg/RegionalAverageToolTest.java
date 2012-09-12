package org.esa.cci.sst.regavg;

import org.esa.cci.sst.common.ProcessingLevel;
import org.esa.cci.sst.util.ProductType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Norman Fomferra
 */
public class RegionalAverageToolTest {

    @Test
    public void testOutputName() throws Exception {
        RegionalAverageTool regionalAverageTool = new RegionalAverageTool();
        regionalAverageTool.setProductType(ProductType.CCI_L3U);
        String filename = regionalAverageTool.getOutputFilename("20000101", "20101231", "Global", ProcessingLevel.L3U, "SSTskin", "PS", "DM");
        assertEquals("20000101-20101231-Global_average-ESACCI-L3U_GHRSST-SSTskin-PS-DM-v1.2-fv1.1.nc", filename);
    }

}