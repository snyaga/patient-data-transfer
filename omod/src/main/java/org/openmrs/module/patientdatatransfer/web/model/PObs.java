package org.openmrs.module.patientdatatransfer.web.model;

/**
 * Created by Muthoni on 03/06/15.
 */

import java.util.Date;

public class PObs extends PBaseData {

    public Integer obsId = null;

    public Integer conceptId = null;

    public Date obsDatetime = null;

    public String valueAsString = null;

    public Integer valueCodedConceptId = null;

    public String valueCodedConceptName = null;

    public Date valueDatetime = null;

    public Double valueNumeric = null;

    public String valueModifier = null;

    public String valueText = null;

    public String valueComplex = null;

    public PPatient person = null;

    public Integer locationId = null;

    public Date dateStarted = null;

    public Date dateStopped = null;
}

