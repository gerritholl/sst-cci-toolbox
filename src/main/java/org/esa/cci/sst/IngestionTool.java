package org.esa.cci.sst;

import org.esa.cci.sst.data.Coincidence;
import org.esa.cci.sst.data.DataFile;
import org.esa.cci.sst.data.DataSchema;
import org.esa.cci.sst.data.Observation;
import org.esa.cci.sst.orm.PersistenceManager;
import org.esa.cci.sst.util.TimeUtil;
import org.postgis.Geometry;
import org.postgis.PGgeometry;
import org.postgis.Point;
import ucar.ma2.Array;
import ucar.ma2.ArrayChar;
import ucar.ma2.ArrayDouble;
import ucar.ma2.ArrayFloat;
import ucar.nc2.Dimension;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Tool to ingest new MD files into the MMS database.
 */
public class IngestionTool {

    static final String PERSISTENCE_UNIT_NAME = "matchupdb";

    public void ingest(String matchupFilePath, String schemaName, String sensorType) throws Exception {
        NetcdfFile matchupFile = null;
        PersistenceManager persistenceManager = null;
        try {
            // open match-up file
            matchupFile = NetcdfFile.open(matchupFilePath);
            final int numberOfRecords = matchupFile.findDimension("match_up").getLength();

            // open database
            persistenceManager = new PersistenceManager(PERSISTENCE_UNIT_NAME);
            persistenceManager.transaction();

            // lookup or create data schema and data file entry
            DataSchema dataSchema = (DataSchema) persistenceManager.pick("select s from DataSchema s where s.name = ?1", schemaName);
            if (dataSchema == null) {
                dataSchema = new DataSchema();
                dataSchema.setName(schemaName);
                dataSchema.setSensorType(sensorType);
                persistenceManager.persist(dataSchema);
            }

            DataFile dataFile = new DataFile();
            dataFile.setPath(matchupFilePath);
            dataFile.setDataSchema(dataSchema);
            persistenceManager.persist(dataFile);

            // (maybe create observation variable)

            // read in-situ variables
            final NetcdfMatchupReader insituReader = new NetcdfMatchupReader(matchupFile, schemaName, "insitu");
            final NetcdfMatchupReader satelliteReader = new NetcdfMatchupReader(matchupFile, schemaName, "satellite");
            final NetcdfMatchupReader matchupReader = new NetcdfMatchupReader(matchupFile, schemaName, "matchup");

            insituReader.read();
            satelliteReader.read();
            matchupReader.read();

            // loop over records
            for (int recordNo = 0; recordNo < Math.min(3, numberOfRecords); ++recordNo) {

                Observation insituObservation = createObservation(insituReader, dataFile, recordNo);
                persistenceManager.persist(insituObservation);

                final Observation satelliteObservation = createObservation(satelliteReader, dataFile, recordNo);
                persistenceManager.persist(satelliteObservation);

                final Coincidence insituCoincidence = createSelfCoincidence(insituObservation);
                persistenceManager.persist(insituCoincidence);

                final Coincidence satelliteCoincidence = createCoincidence(matchupReader, insituObservation, satelliteObservation, recordNo);
                persistenceManager.persist(satelliteCoincidence);
            }

            // make changes in database
            persistenceManager.commit();

        } catch (Exception e) {

            // do not make any change in case of errors
            if (persistenceManager != null) {
                persistenceManager.rollback();
            }
            throw e;

        } finally {

            // close match-up file
            if (matchupFile != null) {
                matchupFile.close();
            }
        }

    }

    private Observation createObservation(NetcdfMatchupReader reader, DataFile dataFile, int recordNo) {

        final Observation observation = new Observation();
        observation.setName(reader.getString("name", recordNo));
        observation.setSensor(reader.getString("sensor", recordNo));
        observation.setLocation(new PGgeometry(new Point(reader.getFloat("longitude", recordNo),
                                                         reader.getFloat("latitude", recordNo))));
        observation.setTime(TimeUtil.dateOfJulianDate(reader.getDouble("time", recordNo)));
        observation.setDatafile(dataFile);
        observation.setRecordNo(recordNo);
        return observation;
    }

    private Coincidence createSelfCoincidence(Observation observation) {

        final Coincidence coincidence = new Coincidence();
        coincidence.setRefObs(observation);
        coincidence.setObservation(observation);
        return coincidence;
    }

    private Coincidence createCoincidence(NetcdfMatchupReader reader, Observation insituObservation, Observation satelliteObservation, int recordNo) {

        final Coincidence coincidence = new Coincidence();
        coincidence.setRefObs(insituObservation);
        coincidence.setObservation(satelliteObservation);
        coincidence.setDistance(reader.getFloat("distance", recordNo));
        coincidence.setTimeDifference(reader.getDouble("timedifference", recordNo));
        return coincidence;
    }
}
