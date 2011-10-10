package org.esa.cci.sst.regavg;

import org.esa.cci.sst.tool.Tool;
import org.esa.cci.sst.util.FileTree;
import org.esa.cci.sst.util.UTC;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * A product store.
 *
 * @author Norman Fomferra
 */
public class ProductStore {

    private static final Logger LOGGER = Tool.LOGGER;

    private ProductType productType;
    private String[] inputPaths;
    private FileTree fileTree;

    public static ProductStore create(ProductType productType, String filenameRegex, String... inputPaths) {
        return new ProductStore(productType, inputPaths, scanFiles(productType, new InputFileFilter(filenameRegex), inputPaths));
    }

    private ProductStore(ProductType productType, String[] inputPaths, FileTree fileTree) {
        this.productType = productType;
        this.inputPaths = inputPaths;
        this.fileTree = fileTree;
    }

    public ProductType getProductType() {
        return productType;
    }

    public String[] getInputPaths() {
        return inputPaths;
    }

    public List<File> getFiles(Date date1, Date date2) {
        Calendar calendar = UTC.createCalendar(date1);
        ArrayList<File> files = new ArrayList<File>();
        while (calendar.getTime().before(date2)) {
            files.addAll(fileTree.get(calendar.getTime()));
            calendar.add(Calendar.DATE, 1);
        }
        return files;
    }

    private static FileTree scanFiles(ProductType productType, FileFilter fileFilter, String... inputPaths) {

        FileTree fileTree = new FileTree();

        for (String inputPath : inputPaths) {
            scanFiles(productType, fileFilter, new File(inputPath), fileTree);
        }

        return fileTree;
    }

    private static void scanFiles(ProductType productType, FileFilter fileFilter, File entry, FileTree fileTree) {
        if (entry.isDirectory()) {
            File[] files = entry.listFiles(fileFilter);
            if (files != null) {
                for (File file : files) {
                    scanFiles(productType, fileFilter, file, fileTree);
                }
            }
        } else if (entry.isFile()) {
            try {
                Date date = productType.getDate(entry);
                if (date != null) {
                    fileTree.add(date, entry);
                } else {
                    LOGGER.warning("Ignoring input file with unknown naming convention: " + entry.getPath());
                }
            } catch (IOException e) {
                LOGGER.warning("Ignoring input file because date can't be retrieved: " + entry.getPath());
            }
        }
    }

    private static class InputFileFilter implements FileFilter {
        private final Pattern filenamePattern;

        private InputFileFilter(String filenameRegex) {
            this.filenamePattern = Pattern.compile(filenameRegex);
        }

        @Override
        public boolean accept(File file) {
            return file.isDirectory()
                    || file.isFile() && filenamePattern.matcher(file.getName()).matches();
        }
    }
}
