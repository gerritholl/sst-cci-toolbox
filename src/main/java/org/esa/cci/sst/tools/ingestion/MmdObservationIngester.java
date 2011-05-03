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

package org.esa.cci.sst.tools.ingestion;

import org.esa.cci.sst.data.Coincidence;
import org.esa.cci.sst.data.Matchup;
import org.esa.cci.sst.data.Observation;
import org.esa.cci.sst.data.Timed;
import org.esa.cci.sst.reader.MmdIOHandler;
import org.esa.cci.sst.tools.ToolException;
import org.esa.cci.sst.util.TimeUtil;

import javax.persistence.Query;
import java.io.IOException;
import java.text.MessageFormat;

/**
 * Responsible for re-ingesting observations and coincidences from an mmd file into the database.
 *
 * @author Thomas Storm
 */
class MmdObservationIngester {

    private final MmdIngester ingester;

    static final String GET_MATCHUP = "SELECT m " +
                                      "FROM Matchup m " +
                                      "WHERE m.id = %d";

    MmdObservationIngester(final MmdIngester ingester) {
        this.ingester = ingester;
    }

    void ingestObservations() {
        final MmdIOHandler ioHandler = (MmdIOHandler) ingester.getIoHandler();
        final int numRecords = ioHandler.getNumRecords();
        for (int i = 0; i < numRecords; i++) {
            ingester.getLogger().info(String.format("ingestion of record '%d/%d\'", (i + 1), numRecords));
            persistObservation(ioHandler, i);
        }
    }

    private void persistObservation(final MmdIOHandler ioHandler, int recordNo) {
        try {
            final Observation observation = ioHandler.readObservation(recordNo);
            final IngestionTool delegate = ingester.getDelegate();
            final boolean hasPersisted = delegate.persistObservation(observation, recordNo);
            if (hasPersisted) {
                persistCoincidence(ioHandler, recordNo, observation);
            }
        } catch (Exception e) {
            final String message = MessageFormat.format("Error persisting observation ''{0}''.", recordNo + 1);
            throw new ToolException(message, e, ToolException.TOOL_ERROR);
        }
    }

    private void persistCoincidence(final MmdIOHandler ioHandler, final int recordNo,
                                    final Observation observation) throws  IOException {
        final int matchupId = ioHandler.getMatchupId(recordNo);
        final Matchup matchup = (Matchup) getDatabaseObjectById(GET_MATCHUP, matchupId);
        final Coincidence coincidence = createCoincidence(matchup, observation);
        ingester.getPersistenceManager().persist(coincidence);
    }

    private Coincidence createCoincidence(final Matchup matchup, final Observation observation) {
        final Coincidence coincidence = new Coincidence();
        coincidence.setMatchup(matchup);
        coincidence.setObservation(observation);
        setCoincidenceTimeDelta(matchup, observation, coincidence);
        return coincidence;
    }

    private void setCoincidenceTimeDelta(final Matchup matchup, final Observation observation,
                                         final Coincidence coincidence) {
        if (observation instanceof Timed) {
            final double timeDelta = TimeUtil.computeTimeDelta(matchup, (Timed) observation);
            coincidence.setTimeDifference(timeDelta);
        }
    }

    private Object getDatabaseObjectById(final String baseQuery, final int id) {
        final String queryString = String.format(baseQuery, id);
        final Query query = ingester.getPersistenceManager().createQuery(queryString);
        return query.getSingleResult();
    }

}
