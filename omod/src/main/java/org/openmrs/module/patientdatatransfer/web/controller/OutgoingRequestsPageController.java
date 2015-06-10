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

import org.openmrs.module.patientdatatransfer.api.PatientDataTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

/**
 * The main controller.
 */
@Controller
public class OutgoingRequestsPageController {
	
	private Log log = LogFactory.getLog(this.getClass());


	
	@Autowired
    private MessageSource messageSource;
	
	@RequestMapping(value="/module/patientdatatransfer/outgoingRequests", method=RequestMethod.GET)
	public ModelAndView showGetOutgoingRequestsForm(HttpServletRequest servletRequest) {
		// quick check to prevent a common NullPointerException
		// that will be thrown below if user is browsing our page without being logged in
		if (Context.getAuthenticatedUser() == null)
			return CommonController.redirectToErrorPage(null, "You must log in before accessing PDT pages.");
		
		PatientDataTransferService pdtService = Context.getService(PatientDataTransferService.class);
		//Outgoing Requests should only see requests with status 1 -- the first stage, OR status 12 (failed to send)


		// create Model
		ModelMap model = new ModelMap();

		return new ModelAndView("/module/patientdatatransfer/outgoingRequests", model);
	}

	


}
