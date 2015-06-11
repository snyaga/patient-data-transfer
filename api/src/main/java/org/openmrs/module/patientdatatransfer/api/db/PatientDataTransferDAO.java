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
package org.openmrs.module.patientdatatransfer.api.db;

import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.activelist.Allergy;
import org.openmrs.activelist.Problem;
import org.openmrs.module.patientdatatransfer.PDTClinic;
import org.openmrs.module.patientdatatransfer.PDTSetting;
import org.openmrs.module.patientdatatransfer.PatientDataRequest;
import org.openmrs.module.patientdatatransfer.api.PatientDataTransferService;

import java.util.List;

/**
 *  Database methods for {@link PatientDataTransferService}.
 */
public interface PatientDataTransferDAO {

	/* PatientDataTransferService methods*/
	public List<PatientDataRequest> getRequests();
	public void saveRequest(PatientDataRequest request);
	public List<PatientDataRequest> getRequestsByUser(int userId);
	public List<PatientDataRequest> getRequestsByStatus(int status);
	public PatientDataRequest getRequestById(int id);
	public void updateRequest(PatientDataRequest request);
	public void replaceRequestAtIdWithObject(int id, PatientDataRequest request);

	/* DirectoryService methods */
	public List<PDTClinic> getClinics();
	public void saveClinic(PDTClinic clinic);
	public void updateClinic(PDTClinic clinic);
	public void deleteClinicById(Integer id);
	public List<PDTSetting> getSettings();
	public PDTSetting getSettingByName(String name);
	public PDTClinic getClinicByDomainName(String domainName);
	public void saveSetting(PDTSetting setting);
	public void updateSetting(PDTSetting setting);
	public void deleteSettingById(Integer id);

	/* methods for cleaning up deserialized data */
	public void cleanPatient(Patient patient);
	public void cleanEncounter(Encounter encounter, Patient patient);
	public void cleanProblem(Problem problem, Patient patient);
	public void cleanAllergy(Allergy allergy, Patient patient);
}