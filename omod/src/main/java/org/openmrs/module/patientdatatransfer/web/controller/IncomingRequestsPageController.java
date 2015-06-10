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

import org.openmrs.module.patientdatatransfer.api.PatientDataTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.Locale;

/**
 * The main controller.
 */
@Controller
public class  IncomingRequestsPageController {
	
	private Log log = LogFactory.getLog(this.getClass());

	

	
    @Autowired  
    private MessageSource messageSource;
	
	@RequestMapping(value="/module/patientdatatransfer/incomingRequests", method=RequestMethod.GET)
	public void showForm(ModelMap model) {
		PatientDataTransferService pdtService = Context.getService(PatientDataTransferService.class);

	}
	
	@RequestMapping(value="/module/patientdatatransfer/incomingRequests", method=RequestMethod.POST)
	public ModelAndView handleForm(			
			@RequestParam("action") String action,
			@RequestParam(value = "incomingRequestIdHidden") String[] allRequests,
			@RequestParam(value = "incomingRequestId", required=false) String[] requests,
			@RequestParam(value = "denyReason") String[] denyReasons, Locale locale) {
		
		// determine whether to deny or approve based on the 'submit' button that was pressed 
		boolean approve;
		if (action.equals(messageSource.getMessage("patientdatatransfer.incomingRequests.approve", null, locale)))
			approve  = true;
		else if (action.equals(messageSource.getMessage("patientdatatransfer.incomingRequests.deny", null, locale)))
			approve = false;
		else {
			return CommonController.redirectToErrorPage(null, "Unknown action was posted");
		}
		// input validation on deny reasons
		for (String denyReason : denyReasons) {
			ModelAndView validationResult = CommonController.validateDenyReason(denyReason);
			if (validationResult != null)
				return validationResult;
		}

		
		// Redirect user to the Home page of the module after their form data is processed
		return new ModelAndView("redirect:/module/patientdatatransfer/incomingRequests.form");
	}
	

		}