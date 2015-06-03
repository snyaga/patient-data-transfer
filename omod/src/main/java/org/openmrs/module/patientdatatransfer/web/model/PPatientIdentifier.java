package org.openmrs.module.patientdatatransfer.web.model;

/**
 * Created by Muthoni on 03/06/15.
 */
public class PPatientIdentifier extends PBaseData {
    public String identifier = "";

    public Integer identifierTypeId = null;

    public Integer locationId = null;

    public Boolean preferred = false;
}
