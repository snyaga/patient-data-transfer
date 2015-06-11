/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.patientdatatransfer.api;

import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.activelist.Allergy;
import org.openmrs.activelist.Problem;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.patientdatatransfer.PatientDataRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * This service exposes module's core functionality. It is a Spring managed bean which is configured in moduleApplicationContext.xml.
 * <p>
 * It can be accessed only via Context:<br>
 * <code>
 * Context.getService(PatientDataTransferService.class).someMethod();
 * </code>
 * 
 * @see org.openmrs.api.context.Context
 */
@Transactional
public interface PatientDataTransferService extends OpenmrsService {
    public static final int PENDINGLOCALAPPROVAL =1;
    public static final int SENTTOREMOTECLINIC=2;
    public static final int RECEIVEDBYREMOTECLINIC =3;
    public static final int APPROVEDBYREMOTECLINIC =4;
    public static final int RETURNEDTOREQUESTINGCLINIC = 5;
    public static final int TRANSFERCOMPLETE =6;
    public static final int SENTDENYTOREQUESTINGCLINIC = 7;
    
    public static final int REJECTEDBYLOCALUSER = 11;
    public static final int FAILEDTOSEND =12;
    public static final int REJECTEDBYREMOTEUSER =13;
    public static final int FAILEDTOAUGMENTDATA = 14;
    public static final int FAILEDTORETURNDATA = 15;
    
    public static final int SENDING = 22;
    public static final int RETURNING = 25;
    
	/* Request methods */
	public List<PatientDataRequest> getRequests();
	
	public PatientDataRequest getRequestById(int id);
	
	public List<PatientDataRequest> getRequestsByUser(int userId);
 
	public List<PatientDataRequest> getRequestsByStatus(int status);
	
    public void saveRequest(PatientDataRequest request);
    
    public void updateRequest(PatientDataRequest request);
    
    public void cleanPatient(Patient patient);
	
    public void cleanEncounter(Encounter encounter, Patient patient);
    
	public void cleanProblem(Problem problem, Patient patient);

	public void cleanAllergy(Allergy allergy, Patient patient);
	
	public void setupSSLforKeyStore();
    
	public boolean isDNSValid(String hostname);

	public void replaceRequestAtIdWithObject(int id, PatientDataRequest request);
}