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
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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


/* >>>Not using this for now<<<<

	@RequestMapping(value = "/module/patientdatatransfer/manage", method = RequestMethod.POST)
	public void manage(ModelMap model,
					   @RequestParam(value = "patientId", required = true) String patientId) {

		PatientService patientService = Context.getPatientService();
		Patient patient = patientService.getPatientByUuid(patientId);
		//List<Patient> patient = Context.getPatientService().getPatientByUuid(patientId);
		model.addAttribute("patient", patient);

		//return "redirect:display.form";

	}*/

	@RequestMapping(value = "/module/patientdatatransfer/basicInfo.form", method = RequestMethod.GET)
	public void requestForm(ModelMap model) {
		model.addAttribute("user", Context.getAuthenticatedUser());
	}
}

