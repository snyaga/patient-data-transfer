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

import org.openmrs.api.context.Context;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.regex.Pattern;

/**
 * The main controller.
 */
@Controller
public class  NewClinicPageController {

	// Shows the New Clinic form. Doesn't require any state, so the function is blank.
	@RequestMapping(value="/module/patientdatatransfer/newclinic", method=RequestMethod.GET)
	public void showGetNewClinicForm(ModelMap model) {}

	// Handles the POST data from the New Clinic form submission. Creates the new clinic.
	@RequestMapping(value="/module/patientdatatransfer/newclinic", method=RequestMethod.POST)
	public ModelAndView showPostNewClinicForm(			
			@RequestParam(value = "clinicName") String name,
			@RequestParam(value = "clinicDesc") String description,
			@RequestParam(value = "clinicIpAddress") String ipAddress,
			@RequestParam(value = "clinicPort") String port,
			@RequestParam(value = "clinicUrlPrefix") String urlPrefix,
			@RequestParam(value = "clinicLatitude") String latitude,
			@RequestParam(value = "clinicLongitude") String longitude) {
		
		

		return new ModelAndView("redirect:/module/patientdatatransfer/settings.form");
	}
}