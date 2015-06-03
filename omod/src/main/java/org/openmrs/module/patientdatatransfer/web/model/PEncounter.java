package org.openmrs.module.patientdatatransfer.web.model;

/**
 * Created by Muthoni on 03/06/15.
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class PEncounter extends PBaseData {

    public Date encounterDatetime = null;

    public Integer locationId = null;

    public Integer encounterTypeId = null;

    public Integer providerNameId = null;

    public List<PObs> obses = new ArrayList<PObs>();

    @DeserializingHint(fieldName = "obses")
    public PObs obs(@DeserializingHint(fieldName = "conceptId") Integer conceptId) {
        for (PObs obs : obses) {
            if (obs.conceptId.equals(conceptId)) {
                return obs;

            }
        }
        PObs obs = new PObs();
        obs.conceptId = conceptId;
        obses.add(obs);
        return obs;
    }

    public String obsesAsString(Integer conceptId) {
        String result = "";
        for (PObs obs : obses) {
            if (obs.conceptId.equals(conceptId)) {
                result += obs.valueAsString + "|";
            }
        }
        return (result.lastIndexOf("|") > 0 ? result.substring(0, result.length() - 1) : result);
    }

    public String obsesAsValueCodedList(Integer conceptId) {
        String result = "";
        for (PObs obs : obses) {
            if (obs.conceptId.equals(conceptId)) {
                result += obs.valueCodedConceptId + "|";
            }
        }
        return (result.lastIndexOf("|") > 0 ? result.substring(0, result.length() - 1) : result);
    }

    public void obsesAsValueCodedList(Integer conceptId, String valueCodedObses) {
        StringTokenizer st = new StringTokenizer(valueCodedObses, "|");
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            PObs obs = new PObs();
            obs.conceptId = conceptId;
            obs.valueCodedConceptId = Integer.parseInt(token);
            obses.add(obs);
        }
    }
}
