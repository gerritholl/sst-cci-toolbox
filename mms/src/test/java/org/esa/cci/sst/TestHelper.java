package org.esa.cci.sst;


import org.esa.cci.sst.tools.samplepoint.TimeRange;
import org.esa.cci.sst.util.SamplingPoint;

import java.net.URL;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class TestHelper {

    public static void assertPointsInTimeRange(Date startDate, Date stopDate, List<SamplingPoint> inSituPoints) {
        final TimeRange timeRange = new TimeRange(startDate, stopDate);
        for (SamplingPoint next : inSituPoints) {
            assertTrue(timeRange.includes(new Date(next.getTime())));
        }
    }

    public static void assertPointsHaveReference(int id, List<SamplingPoint> inSituPoints) {
        for (SamplingPoint next : inSituPoints) {
            assertEquals(id, next.getReference());
        }
    }

    public static String getResourcePath(Class<?> clazz, String name) {
        final URL resource = clazz.getResource(name);
        assertNotNull(resource);
        return resource.getFile();
    }
}
