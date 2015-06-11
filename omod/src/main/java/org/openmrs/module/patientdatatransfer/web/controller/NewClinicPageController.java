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

import java.util.regex.Pattern;

import org.openmrs.api.context.Context;
import org.openmrs.module.patientdatatransfer.PDTClinic;
import org.openmrs.module.patientdatatransfer.api.DirectoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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


		DirectoryService dirService = Context.getService(DirectoryService.class);

		// create a new PDTClinic object and populate with sanitized request parameters
		PDTClinic clinic = new PDTClinic();

		// sanitize and add name
		if(name.length()>32 || !(Pattern.matches("[a-zA-Z[0123456789]\\.\\-]+",name)))
		{
			return CommonController.redirectToErrorPage(null, "The name should only contain letters, numbers, dashes, and periods.");
		}
		clinic.setName(name);

		// sanitize and add description
		if (description == null || description.trim().length() <= 0) {
			clinic.setDescription(null);
		}
		else if(description.length()>32 || !(Pattern.matches("[a-zA-Z[0123456789]\\. ]+",description))) {
			return CommonController.redirectToErrorPage(null, "The description should only contain spaces, letters, numbers, and periods.");
		} else {
			clinic.setDescription(description);
		}

		// sanitize and add ip address
		String [] parts = ipAddress.split ("\\.");
		for (String s : parts)
		{
			int i = Integer.parseInt (s);
			if (i < 0 || i > 255)
			{
				return CommonController.redirectToErrorPage(null, "The IP address entered does not conform to IPv4 standards.");
			}
		}
		clinic.setIpAddress(ipAddress);

		// sanitize and add port
		try {
			clinic.setPort(Integer.parseInt(port));
		} catch (Exception e) {
			return CommonController.redirectToErrorPage(e, "Something went wrong converting or setting the port number.");
		}

		// sanitize and add URL Prefix
		if(urlPrefix.length()>32 || !(Pattern.matches("[a-zA-Z[0123456789:/-]]+",urlPrefix)))
		{
			return CommonController.redirectToErrorPage(null, "The URL Prefix should only contain letters, numbers, hyphens, colons, and slashes.");
		}
		clinic.setUrlPrefix(urlPrefix);

		// sanitize and add latitude
		try {
			clinic.setLatitude(Float.parseFloat(latitude));
		} catch (Exception e) {
			return CommonController.redirectToErrorPage(e, "Something went wrong converting or setting the latitude. Is it a valid floating point number?");
		}

		// sanitize and add longitude
		try {
			clinic.setLongitude(Float.parseFloat(longitude));
		} catch (Exception e) {
			return CommonController.redirectToErrorPage(e, "Something went wrong converting or setting the longitude. Is it a valid floating point number?");
		}

		// now save the new clinic to the DB and redirect to settings page
		dirService.saveClinic(clinic);
		return new ModelAndView("redirect:/module/patientdatatransfer/settings.form");
	}
}