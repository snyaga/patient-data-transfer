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
package org.openmrs.module.patientdatatransfer.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.EncounterService;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientdatatransfer.ImportReq;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import  javax.servlet.http.HttpSession;

import java.util.Date;
import java.util.List;

/**
 * The main controller.
 */
@Controller
public class  PatientDataTransferManageController {

	protected final Log log = LogFactory.getLog(getClass());

	@RequestMapping(value = "/module/patientdatatransfer/manage.form", method = RequestMethod.GET)
	public void manage(ModelMap model) {
		model.addAttribute("user", Context.getAuthenticatedUser());
	}


	@RequestMapping(value = "/module/patientdatatransfer/display.form", method = RequestMethod.POST)
	 @ResponseBody public ImportReq requestForm(ModelMap model, @RequestParam(value ="gender", required = false) String gender,
										  @RequestParam(value="county", required = false) String county,
										  @RequestParam(value = "dob", required = false) Date dob,
										  @RequestParam(value="ID", required = false) String ID,
										  @RequestParam(value = "clinicNo", required = false) String clinicNo
	) {
				model.addAttribute("user", Context.getAuthenticatedUser());

				ImportReq request = new ImportReq();

				request.setGender(gender);
				request.setCounty(county);
				request.setDob(dob);
				if(ID!=null){
					request.setNationalID(ID);
					request.setClinicNo(null);
				}
				else {
					request.setNationalID(null);
					request.setClinicNo(clinicNo);
				}

			return request;
		}


	@RequestMapping(value = "/module/patientdatatransfer/basicInfo.form", method = RequestMethod.GET)
	public void requestForm(ModelMap model){
		model.addAttribute("user", Context.getAuthenticatedUser());

	}
	/*Export Patient Data to JSON file*/

/*	@RequestMapping(value = "module/patientdatatransfer/display.form", method = RequestMethod.GET)
	@ResponseBody public Patient display(@RequestParam(value="ID", required = false) String ID,
										 @RequestParam(value = "clinicNo", required = false) String clinicNo) {

		PatientService patientService = Context.getPatientService();
		Patient patient;

		if(ID !=null) {
			patient = patientService.getPatientByUuid(ID);
		}
		else {
			patient = patientService.getPatientByUuid(clinicNo);
		}
		return patient;
	}*/

	/* Import Patient Data from JSON and add patient to Database */

}