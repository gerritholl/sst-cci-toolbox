package org.esa.cci.sst.assessment;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Properties;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class CarTemplatePoiTest {

    private File dataDir;
    private PoiWordDocument document;
    private Properties properties;

    @Before
    public void setUp() throws Exception {
        document = new PoiWordDocument(CarTemplateTest.class.getResource("car-template.docx"));
        properties = new Properties();
        properties.load(CarTemplateTest.class.getResourceAsStream("car-template.properties"));

        dataDir = new File(System.getProperty("user.home"), "scratch/car/figures");
    }

    @After
    public void tearDown() throws Exception {
        final File targetFile = new File("car.docx");
        document.save(targetFile);

        if (targetFile.isFile()) {
            if (!targetFile.delete()) {
                fail("Unable to delete test file");
            }
        }
    }

    @Test
    public void testReplaceComments() throws Exception {
        String comment = properties.getProperty("comment.Figure_2");
        assertTrue(document.containsVariable("${comment.Figure_2}"));
        document.replaceParagraphText("${comment.Figure_2}", comment);
        assertFalse(document.containsVariable("${comment.Figure_2}"));

        comment = properties.getProperty("comment.Figure_3");
        assertTrue(document.containsVariable("${comment.Figure_3}"));
        document.replaceParagraphText("${comment.Figure_3}", comment);
        assertFalse(document.containsVariable("${comment.Figure_3}"));
    }

    @Test
    public void testReplaceParagraphs() throws Exception {
        // @todo 2 tb/tb the same functionality is already covered by the test above. Iterate with NR and GC about the
        // variable names. Concequently all keys that start with paragraph replace a paragraph text. The comment.* keys then either
        // are removed or should have another functionality. tb 2015-07-10

        String paragraph = properties.getProperty("paragraph.summary_text");
        assertTrue(document.containsVariable("${paragraph.summary_text}"));
        document.replaceParagraphText("${paragraph.summary_text}", paragraph);
        assertFalse(document.containsVariable("${paragraph.summary_text}"));
    }
}
