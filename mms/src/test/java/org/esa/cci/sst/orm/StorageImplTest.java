package org.esa.cci.sst.orm;

import org.esa.cci.sst.data.*;
import org.esa.cci.sst.util.TimeUtil;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.Query;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;


public class StorageImplTest {

    private static final int ID = 1234;
    private static final String GET_OBSERVATION_SQL = "select o from Observation o where o.id = ?1";

    private PersistenceManager persistenceManager;
    private StorageImpl storageImpl;

    @Before
    public void setUp() {
        persistenceManager = mock(PersistenceManager.class);
        storageImpl = new StorageImpl(persistenceManager);
    }

    @Test
    public void testGetDataFile() {
        final String sql = "select f from DataFile f where f.path = ?1";
        final String path = "/over/the/rainbow";
        final DataFile dataFile = createDataFile(path);

        when(persistenceManager.pick(sql, path)).thenReturn(dataFile);

        final DataFile toolStorageDatafile = storageImpl.getDatafile(path);
        assertNotNull(toolStorageDatafile);
        assertEquals(path, toolStorageDatafile.getPath());

        verify(persistenceManager, times(1)).pick(sql, path);
        verifyNoMoreInteractions(persistenceManager);
    }

    @Test
    public void testStoreDataFile() {
        final String sql = "select f from DataFile f where f.path = ?1";
        final String path = "/left/of/rome";
        final DataFile dataFile = createDataFile(path);

        when(persistenceManager.pick(sql, path)).thenReturn(dataFile);

        final int storedId = storageImpl.store(dataFile);
        assertEquals(ID, storedId);

        verify(persistenceManager, times(1)).persist(dataFile);
        verify(persistenceManager, times(1)).pick(sql, path);
        verifyNoMoreInteractions(persistenceManager);
    }

    @Test
    public void testGetObservation() {
        final int id = 8876;
        final String name = "TestObservation";
        final Observation observation = new Observation();
        observation.setName(name);

        when(persistenceManager.pick(GET_OBSERVATION_SQL, id)).thenReturn(observation);

        final Observation toolStorageObservation = storageImpl.getObservation(id);
        assertNotNull(toolStorageObservation);
        assertEquals(name, toolStorageObservation.getName());

        verify(persistenceManager, times(1)).pick(GET_OBSERVATION_SQL, id);
        verifyNoMoreInteractions(persistenceManager);
    }

    @Test
    public void testGetRelatedObservation() {
        final int id = 98843;
        final String name = "related";
        final RelatedObservation observation = new RelatedObservation();
        observation.setName(name);

        when(persistenceManager.pick(GET_OBSERVATION_SQL, id)).thenReturn(observation);

        final RelatedObservation toolStorageObservation = storageImpl.getRelatedObservation(id);
        assertNotNull(toolStorageObservation);
        assertEquals(name, toolStorageObservation.getName());

        verify(persistenceManager, times(1)).pick(GET_OBSERVATION_SQL, id);
        verifyNoMoreInteractions(persistenceManager);
    }

    @Test
    public void testGetRelatedObservations() throws ParseException {
        final Date startDate = TimeUtil.parseCcsdsUtcFormat("2010-01-01T13:00:00Z");
        final Date stoptDate = TimeUtil.parseCcsdsUtcFormat("2010-01-05T17:00:00Z");
        final String sensorName = "thermometer";
        final String sql ="select o.id from mm_observation o where o.sensor = ?1 and o.time >= timestamp '2010-01-01T13:00:00Z' and o.time < timestamp '2010-01-05T17:00:00Z' order by o.time, o.id";

        final ArrayList<RelatedObservation> observations = new ArrayList<>();
        final RelatedObservation observation = new RelatedObservation();
        observation.setName("tested thing");
        observations.add(observation);

        final Query query = mock(Query.class);
        when(query.getResultList()).thenReturn(observations);
        when(persistenceManager.createNativeQuery(sql, RelatedObservation.class)).thenReturn(query);

        final List<RelatedObservation> storedObservations = storageImpl.getRelatedObservations(sensorName, startDate, stoptDate);
        assertNotNull(storedObservations);
        assertEquals(1, storedObservations.size());

        verify(persistenceManager, times(1)).createNativeQuery(sql, RelatedObservation.class);
        verifyNoMoreInteractions(persistenceManager);

        verify(query, times(1)).setParameter(1, sensorName);
        verify(query, times(1)).getResultList();
        verifyNoMoreInteractions(query);
    }

    @Test
    public void testGetReferenceObservation() {
        final String sql = "select o from ReferenceObservation o where o.id = ?1";
        final int id = 2286;
        final String name = "refer_to_me";
        final ReferenceObservation referenceObservation = new ReferenceObservation();
        referenceObservation.setName(name);

        when(persistenceManager.pick(sql, id)).thenReturn(referenceObservation);

        final ReferenceObservation toolStorageReferenceObservation = storageImpl.getReferenceObservation(id);
        assertNotNull(toolStorageReferenceObservation);
        assertEquals(name, toolStorageReferenceObservation.getName());

        verify(persistenceManager, times(1)).pick(sql, id);
        verifyNoMoreInteractions(persistenceManager);
    }

    @Test
    public void testGetSensor() {
        final String sql = "select s from Sensor s where s.name = ?1";
        final String sensorName = "blabla";
        final Sensor sensor = new SensorBuilder().name(sensorName).build();

        when(persistenceManager.pick(sql, sensorName)).thenReturn(sensor);

        final Sensor toolStorageSensor = storageImpl.getSensor(sensorName);
        assertNotNull(toolStorageSensor);
        assertEquals(sensorName, toolStorageSensor.getName());

        verify(persistenceManager, times(1)).pick(sql, sensorName);
        verifyNoMoreInteractions(persistenceManager);
    }

    @SuppressWarnings("deprecation")
    private DataFile createDataFile(String path) {
        final DataFile dataFile = new DataFile(path, new SensorBuilder().name("cloud").build());
        dataFile.setId(ID);
        return dataFile;
    }
}
