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

package org.esa.cci.sst.tools.ingestion;

import org.esa.cci.sst.data.DataFile;
import org.esa.cci.sst.data.Sensor;
import org.esa.cci.sst.orm.Storage;
import org.esa.cci.sst.reader.GunzipDecorator;
import org.esa.cci.sst.reader.MmdReader;
import org.esa.cci.sst.tool.Configuration;
import org.esa.cci.sst.tool.ToolException;
import org.esa.cci.sst.tools.BasicTool;
import org.esa.cci.sst.util.StopWatch;

import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

/**
 * Tool responsible for re-ingesting mmd files.
 *
 * @author Thomas Storm
 */
public class MmdIngestionTool extends BasicTool {

    public static void main(String[] args) {
        final MmdIngestionTool tool = new MmdIngestionTool();
        try {
            final boolean ok = tool.setCommandLineArgs(args);
            if (!ok) {
                tool.printHelp();
                return;
            }
            tool.initialize();
            tool.ingest();
        } catch (ToolException e) {
            tool.getErrorHandler().terminate(e);
        } catch (Exception e) {
            tool.getErrorHandler().terminate(new ToolException(e.getMessage(), e, ToolException.UNKNOWN_ERROR));
        } finally {
            tool.getPersistenceManager().close();
        }
    }


    private MmdReader reader;
    private Ingester ingester;

    private MmdIngestionTool() {
        super("reingestion-tool.sh", "1.0");
    }

    void ingest() {
        ingester = new Ingester(this);
        final Configuration config = getConfig();

        final String sensorName = config.getStringValue(Configuration.KEY_MMS_REINGESTION_SENSOR);
        final long pattern = config.getPatternValue(Configuration.KEY_MMS_REINGESTION_PATTERN);
        final boolean located = config.getBooleanValue(Configuration.KEY_MMS_REINGESTION_LOCATED);
        final boolean overwrite = config.getBooleanValue(Configuration.KEY_MMS_REINGESTION_OVERWRITE);
        final String archiveRootPath = config.getStringValue(Configuration.KEY_MMS_ARCHIVE_ROOT);
        final File archiveRoot = new File(archiveRootPath);
        final String mmdFileLocation = config.getStringValue(Configuration.KEY_MMS_REINGESTION_SOURCE);

        final File mmdFile = new File(mmdFileLocation);
        if (!mmdFile.exists()) {
            logger.warning("missing source file: " + mmdFile);
            logger.warning("reingestion skipped");
            return;
        }

        verifyCorrectMmdFile(archiveRootPath, mmdFile);

        final String mmdFileRelativePath = mmdFileLocation.substring(archiveRootPath.length() + 1);
        final Storage storage = getStorage();

        DataFile datafile = storage.getDatafile(mmdFileRelativePath);
        final boolean datafilePersisted = datafile != null;
        if (!datafilePersisted) {
            Sensor sensor = storage.getSensor(sensorName);
            if (sensor == null) {
                sensor = ingester.createSensor(sensorName, located ? "RelatedObservation" : "Observation", pattern);
                getPersistenceManager().transaction();
                try {
                    getPersistenceManager().persist(sensor);
                } catch (PersistenceException e) {
                    logger.severe(e.getMessage());
                    getPersistenceManager().rollback();
                }
                getPersistenceManager().commit();
                sensor = storage.getSensor(sensorName);
            }
            datafile = createDataFile(sensor, mmdFileRelativePath);
        } else {
            if (overwrite) {
                getPersistenceManager().transaction();
                deleteObservationsAndCoincidences(datafile);
                getPersistenceManager().commit();
            }
        }

        initReader(datafile, archiveRoot);
        getPersistenceManager().transaction();
        try {
            persistColumns(sensorName);
        } catch (PersistenceException ignored) {
            getPersistenceManager().rollback();
        }
        getPersistenceManager().commit();

        getPersistenceManager().transaction();
        try {
            if (!datafilePersisted) {
                storeDataFile(datafile, storage);
            }
            ingestObservations(pattern);
        } catch (PersistenceException e) {
            try {
                getPersistenceManager().rollback();
            } catch (PersistenceException ignored) {
            }
            throw new ToolException(e.getMessage(), e, ToolException.TOOL_ERROR);
        }
        getPersistenceManager().commit();
    }

    // @todo 3 tb/tb can be static, add tests 2015-09-17
    private void verifyCorrectMmdFile(String archiveRootPath, File mmdFile) {
        if (!mmdFile.isAbsolute()) {
            final String message = MessageFormat.format("Property key {0}: absolute path expected.", Configuration.KEY_MMS_REINGESTION_SOURCE);
            throw new ToolException(message, ToolException.TOOL_CONFIGURATION_ERROR);
        }

        if (!mmdFile.getAbsolutePath().startsWith(archiveRootPath)) {
            final String message = MessageFormat.format("Property key {0}: absolute path within archive root {1} expected.",  Configuration.KEY_MMS_REINGESTION_SOURCE,  archiveRootPath);
            throw new ToolException(message, ToolException.TOOL_CONFIGURATION_ERROR);
        }

        if (!mmdFile.isFile()) {
            final String message = MessageFormat.format("Property key {0}: absolute path to file expected.", Configuration.KEY_MMS_REINGESTION_SOURCE);
            throw new ToolException(message, ToolException.TOOL_CONFIGURATION_ERROR);
        }
    }

    private void deleteObservationsAndCoincidences(DataFile dataFile) {
        Query query = getPersistenceManager().createNativeQuery("delete from mm_coincidence c " +
                "where exists ( select f.id from mm_datafile f, mm_observation o " +
                "where f.path = ?1 and o.datafile_id = f.id and c.observation_id = o.id )");
        query.setParameter(1, dataFile.getPath());

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        query.executeUpdate();

        stopWatch.stop();
        logger.info(MessageFormat.format("{0} coincidences dropped in {1} ms.", dataFile.getPath(), stopWatch.getElapsedMillis()));

        query = getPersistenceManager().createNativeQuery("delete from mm_observation o " +
                "where exists ( select f.id from mm_datafile f " +
                "where f.path = ?1 and o.datafile_id = f.id )");
        query.setParameter(1, dataFile.getPath());

        stopWatch.start();

        query.executeUpdate();

        stopWatch.stop();
        logger.info(MessageFormat.format("{0} observations dropped in {1} ms.", dataFile.getPath(), stopWatch.getElapsedMillis()));
    }

    private void ingestObservations(long pattern) {
        final MmdObservationIngester observationIngester = new MmdObservationIngester(this, ingester, reader, pattern);
        observationIngester.ingestObservations();
    }

    private void persistColumns(String sensorName) {
        try {
            ingester.persistColumns(sensorName, reader);
        } catch (IOException e) {
            final String message = MessageFormat.format("Unable to persist columns for sensor ''{0}''.", sensorName);
            throw new ToolException(message, e, ToolException.TOOL_ERROR);
        }
    }

    private DataFile createDataFile(Sensor sensor, String path) {
        return new DataFile(new File(path), sensor);
    }

    // package access for testing only tb 2014-03-05
    static void storeDataFile(DataFile dataFile, Storage storage) {
        final DataFile persistedDatafile = storage.getDatafile(dataFile.getPath());
        if (persistedDatafile == null) {
            storage.store(dataFile);
        } else {
            throw new IllegalStateException("Trying to ingest duplicate datafile.");
        }
    }

    private void initReader(final DataFile dataFile, File archiveRoot) {
        reader = new MmdReader(dataFile.getSensor().getName());
        final GunzipDecorator decorator = new GunzipDecorator(reader);
        reader.setConfiguration(getConfig());
        try {
            decorator.open(dataFile, archiveRoot);
        } catch (IOException e) {
            final File filePath = new File(archiveRoot, dataFile.getPath());
            throw new ToolException("Error initializing Reader for MMD file '" + filePath + "'.", e,
                    ToolException.TOOL_ERROR);
        }
    }

}
