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

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.patientdatatransfer.PDTClinic;
import org.openmrs.module.patientdatatransfer.PDTSetting;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * This service exposes module's core functionality. It is a Spring managed bean which is configured in moduleApplicationContext.xml.
 * <p>
 * It can be accessed only via Context:<br>
 * <code>
 * Context.getService(DirectoryService.class).someMethod();
 * </code>
 * 
 * @see org.openmrs.api.context.Context
 */
@Transactional
public interface DirectoryService extends OpenmrsService {
	public static final String localClinicSetting = "clinicDomainName";
	public static final String RestEndPoint = "/rest/patientdatatransfer/request";
	public static final String URLEndPoint = "/ws" + RestEndPoint;
    
	/* Directory methods */
	public List<PDTClinic> getClinics();
	public PDTClinic getClinicByDomainName(String domainName);
	public void saveClinic(PDTClinic clinic);
	public void updateClinic(PDTClinic clinic);
	public void deleteClinicById(Integer id);
	public void updateDirectory();
	
    /* Settings methods */
	public PDTSetting getSettingByName(String name);
	public List<PDTSetting> getSettings();
	public void saveSetting(PDTSetting setting);
	public void updateSetting(PDTSetting setting);
	public void deleteSettingById(Integer id);

}