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
import org.openmrs.api.context.Context;

import org.openmrs.module.patientdatatransfer.PatientDataRequest;
;
import org.openmrs.module.patientdatatransfer.api.PatientDataTransferService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * The main controller.
 */
@Controller
public class NewRequestPageController {
	
	private Log log = LogFactory.getLog(this.getClass());

	
	// Show the form, containing all existing requests.
	@RequestMapping(value="/module/patientdatatransfer/currentRequests", method=RequestMethod.GET)
	//public ModelMap showExistingRequestsForm(HttpServletRequest request, HttpServletResponse response) {
	public ModelAndView showExistingRequestsForm(ModelMap model) {
		// quick check to prevent a common NullPointerException
		// that will be thrown below if user is browsing our page without being logged in
		if (Context.getAuthenticatedUser() == null)
			return CommonController.redirectToErrorPage(null, "You must log in before accessing PDT pages.");
		

			// add these requests to the model, for display on the JSP page

		return new ModelAndView("/module/patientdatatransfer/currentRequests", model);
	}
	
	// Show the New Request form.
	@RequestMapping(value="/module/patientdatatransfer/newRequest", method=RequestMethod.GET)
	public ModelAndView showNewRequestForm(
			HttpServletRequest servletRequest,
			@RequestParam(value="id", required=false) String id) {
		


		// construct model and pass to view
		ModelMap model = new ModelMap();

		return new ModelAndView("/module/patientdatatransfer/newRequest", model);
	}
	


}
