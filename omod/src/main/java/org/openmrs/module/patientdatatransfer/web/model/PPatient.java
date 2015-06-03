package org.openmrs.module.patientdatatransfer.web.model;

/**
 * Created by Muthoni on 03/06/15.
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PPatient extends PBaseData {
    public List<PPatientAddress> addresses = new ArrayList<PPatientAddress>();;

    @DeserializingHint(fieldName = "addresses")
    public PPatientAddress preferredAddress() {
        for (PPatientAddress adr : addresses) {
            if (adr.preferred) {
                return adr;
            }
        }
        if (!addresses.isEmpty()) {
            return addresses.get(0);
        }
        PPatientAddress adr = new PPatientAddress();
        adr.preferred = true;
        addresses.add(adr);
        return adr;
    }

    public List<PPatientName> names = new ArrayList<PPatientName>();

    @DeserializingHint(fieldName = "names")
    public PPatientName preferredName() {
        for (PPatientName name : names) {
            if (name.preferred) {
                return name;
            }
        }
        if (!names.isEmpty()) {
            return names.get(0);
        }
        PPatientName name = new PPatientName();
        name.preferred = true;
        names.add(name);
        return name;
    }


    public String gender = "";

    public Date birthdate = null;

    public Boolean birthdateEstimated = false;

    public String birthdateWithEstimation() {
        return formatDate(birthdate) + (birthdateEstimated ? " (Est.)" : "");
    }

    public void birthdateWithEstimation(String date) {
        if (isNotEmpty(date) && date.endsWith(" (Est.)")) {
            birthdateEstimated = true;
            birthdate = parseDate(date.substring(0, date.length() - " (Est.)".length()));
        } else if (isNotEmpty(date)) {
            birthdate = parseDate(date);
            birthdateEstimated = false;
        }
    }

    public Boolean dead = false;

    public Date deathDate = null;

    public Integer causeOfDeathConceptId = null;

    public List<PPatientIdentifier> identifiers = new ArrayList<PPatientIdentifier>();

    @DeserializingHint(fieldName = "identifiers")
    public PPatientIdentifier identifier(@DeserializingHint(fieldName = "identifierTypeId") Integer identifierTypeId) {
        for (PPatientIdentifier pi : identifiers) {
            if (pi.identifierTypeId.equals(identifierTypeId)) {
                // todo, find how how to get most recent number across all locations
                return pi;
            }
        }
        PPatientIdentifier pi = new PPatientIdentifier();
        pi.identifierTypeId = identifierTypeId;
        identifiers.add(pi);
        return pi;
    }

    public String identifiersAsString() {
        String result = "";
        for (PPatientIdentifier pi : identifiers) {
            result += pi.identifier + ", " + pi.locationId + ", " + pi.identifierTypeId + ", " + pi.uuid + "|";
        }
        return (result.lastIndexOf("|") > 0 ? result.substring(0, result.length() - 1) : result);
    }

    public List<PEncounter> encounters = new ArrayList<PEncounter>();

    @DeserializingHint(fieldName = "encounters")
    public PEncounter encounter(@DeserializingHint(fieldName = "encounterTypeId") Integer encounterTypeId) {
        for (PEncounter e : encounters) {
            if (e.encounterTypeId.equals(encounterTypeId)) {
                // todo, figure out what do to if there are more than 1 encounters of this type
                return e;
            }
        }
        PEncounter e = new PEncounter();
        e.encounterTypeId = encounterTypeId;
        encounters.add(e);
        return e;
    }

    @DeserializingHint(fieldName = "encounters")
    public List<PEncounter> encounters(@DeserializingHint(fieldName = "encounterTypeId") Integer encounterTypeId) {
        List<PEncounter> es = new ArrayList<PEncounter>();
        for (PEncounter e : encounters) {
            if (e.encounterTypeId.equals(encounterTypeId)) {
                es.add(e);
            }
        }
        return es;
    }

    public List<PPatientProgram> patientPrograms = new ArrayList<PPatientProgram>();
}
