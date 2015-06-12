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

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientdatatransfer.PDTClinic;
import org.openmrs.module.patientdatatransfer.PDTSetting;
import org.openmrs.module.patientdatatransfer.PatientDataRequest;
import org.openmrs.module.patientdatatransfer.api.DirectoryService;
import org.openmrs.module.patientdatatransfer.api.LogService;
import org.openmrs.module.patientdatatransfer.api.PatientDataTransferService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;



/**
 * The main controller.
 */
@Controller
public class NewRequestPageController {

	private Log log = LogFactory.getLog(this.getClass());
	private Log auditLog = LogFactory.getLog(LogService.class);

	// Show the form, containing all existing requests.
	@RequestMapping(value="/module/patientdatatransfer/currentRequests", method=RequestMethod.GET)
	//public ModelMap showExistingRequestsForm(HttpServletRequest request, HttpServletResponse response) {
	public ModelAndView showExistingRequestsForm(ModelMap model) {
		// quick check to prevent a common NullPointerException
		// that will be thrown below if user is browsing our page without being logged in
		if (Context.getAuthenticatedUser() == null)
			return CommonController.redirectToErrorPage(null, "You must log in before accessing PDT pages.");

		// get all the requests for the currently logged in user
		PatientDataTransferService pdtService = Context.getService(PatientDataTransferService.class);
		Collection<PatientDataRequest> requests = pdtService.getRequestsByUser(Context.getAuthenticatedUser().getId());


		// add these requests to the model, for display on the JSP page
		model.addAttribute("requests", requests);
		return new ModelAndView("/module/patientdatatransfer/currentRequests", model);
	}

	// Show the New Request form.
	@RequestMapping(value="/module/patientdatatransfer/newRequest", method=RequestMethod.GET)
	public ModelAndView showNewRequestForm(
			HttpServletRequest servletRequest,
			@RequestParam(value="id", required=false) String id) {

		// get list of clinics, which will go in drop-down box
		DirectoryService dirService = Context.getService(DirectoryService.class);
		Collection<PDTClinic> clinics = dirService.getClinics();

		// if an ID was provided (for edit functionality), pass along that request to the view
		PatientDataRequest pdr = null;
		if (id != null && id.length() > 0) {
			PatientDataTransferService pdtService = Context.getService(PatientDataTransferService.class);
			try {
				log.info("Editing request ID#" + id);
				pdr = pdtService.getRequestById(Integer.parseInt(id));
			} catch (NumberFormatException ex) {
				// Should be a rare error, since the ID is not coming from user.
				// But it is a GET param that could be easily fuzzed, so we're checking just in case.
				return CommonController.redirectToErrorPage(ex, "Could not convert request's ID to number");
			}
		}

		// construct model and pass to view
		ModelMap model = new ModelMap();
		model.addAttribute("clinics", clinics);
		model.addAttribute("pdr", pdr);
		// add the base URL for showing the consent form link
		model.addAttribute("consentFormBaseURL", servletRequest.getContextPath() + "/images/pdt");
		return new ModelAndView("/module/patientdatatransfer/newRequest", model);
	}

	// Handle the New Request form.
	@RequestMapping(value="/module/patientdatatransfer/newRequest", method=RequestMethod.POST)
	public ModelAndView handleNewRequestForm(HttpServletRequest servletRequest,
											 @RequestParam(value="id", required=false) String id,
											 @RequestParam("patientIdentifier") String patientIdentifier,
											 @RequestParam("patientGivenName") String givenName,
											 @RequestParam(value = "patientMiddleName", required=false) String middleName,
											 @RequestParam("patientFamilyName") String familyName,
											 @RequestParam("birthDate") String birthDate,
											 @RequestParam("gender") Character gender,
											 @RequestParam("phoneNumber") String phoneNumber,
											 @RequestParam("originalClinic") String originalClinic,
											 @RequestParam(value = "consentFile", required=false) MultipartFile consentFile,
											 @RequestParam(value = "agreedToConsent", required=false) Boolean agreedToConsent)
	{

		PatientDataTransferService pdtService = Context.getService(PatientDataTransferService.class);

		int idNum = -1;
		if (id != null) {
			// Audit Log entry for attempt to edit a request.
			// If successfully edited, another entry will be added below.
			auditLog.info("User " + Context.getAuthenticatedUser().getUsername() +
					"(" + Context.getAuthenticatedUser().getUserId() +
					") has attempted to edit request#" + id + " for " + givenName +
					" " + middleName + " " + familyName + "'s patient data");

			try {
				idNum = Integer.parseInt(id);
			} catch (NumberFormatException ex) {
				// Should be a rare error, since the ID is not coming from user.
				// But it is a POST param that could be easily fuzzed, so we're checking just in case.
				return CommonController.redirectToErrorPage(ex, "Could not convert request's ID to number");
			}

		} else {
			// Audit Log entry for attempt to create a request.
			// If successfully created, another entry will be added below.
			auditLog.info("User " + Context.getAuthenticatedUser().getUsername() +
					"(" + Context.getAuthenticatedUser().getUserId() +
					") has attempted to create a new request for " + givenName +
					" " + middleName + " " + familyName + "'s patient data");
		}

		// When user does not check box for Agreed to Consent, then the Boolean object will be null.
		if (agreedToConsent == null) {
			agreedToConsent = false;
			// handle no consent agreement -- send user to some error form?
			return CommonController.redirectToErrorPage(null, "You must agree to the consent agreement!!");
		}

		// If a consent form file was provided, save it to the local hard disk.
		File destinationFile = null;
		if (!consentFile.isEmpty()) {


			// Get URL for OpenMRS images directory, where we will store these uploaded files
			String uploadURL = servletRequest.getSession().getServletContext().getRealPath("/images/");
			uploadURL += File.separator + "pdt";
			log.info("consent file upload url is: " + uploadURL);

			// check if directory exists, and if not create it
			File pdtImagesFolder = new File(uploadURL);
			if (!pdtImagesFolder.exists()) {
				log.info("Directory doesn't exist, so creating it...");
				if (!pdtImagesFolder.mkdir())
					return CommonController.redirectToErrorPage(null, "Failed to create upload directory for consent form");
			}

			String[] splitFileName = consentFile.getOriginalFilename().split("\\.");
			if (splitFileName.length >= 2) {
				String fileExtension = splitFileName[splitFileName.length-1];

				// Construct file path for the consent form
				destinationFile = new File(pdtImagesFolder + File.separator + UUID.randomUUID() + "." + fileExtension);
				try {
					// performs the actual saving of data
					consentFile.transferTo(destinationFile);
				} catch (IllegalStateException e) {
					return CommonController.redirectToErrorPage(e, "Illegal State Exception when attempting to save file");
				} catch (IOException e) {
					return CommonController.redirectToErrorPage(e, "IOException when attempting to save file");
				}
				log.info("Saved file to: " + destinationFile.getAbsolutePath());
			} else {
				CommonController.redirectToErrorPage(null, "Filename for consent form does not have extension.");
			}
		}

		// Construct a PatientDataRequest object and start filling out what the user provided
		PatientDataRequest request = new PatientDataRequest();
		request.setDateRequested(new Date());

		// if file was uploaded above, link it to consent form column
		if (destinationFile != null)
			request.setConsentForm(destinationFile.getName());

			// If a consent form was not provided, there is still one other situation we need to handle.
			// If the user is editing a request and doesn't provide a new form, use the old one.
		else if (id != null) { // means editing mode
			PatientDataRequest oldRequest = pdtService.getRequestById(idNum);
			request.setConsentForm(oldRequest.getConsentForm());
		}

		// add birth date to request by parsing it from correct format
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date date;
		try {
			date = (Date)formatter.parse(birthDate);
		} catch (ParseException e) {
			return CommonController.redirectToErrorPage(e, "Error parsing date - should be in DD/MM/YYYY format!");
		}
		request.setBirthDate(date);

		// pass along the patient info that was provided
		if(familyName == null)
			return CommonController.redirectToErrorPage(null, "You must provide a family name.");
		if(familyName.length()>32)
			return CommonController.redirectToErrorPage(null, "The family name must be less than 32 characters and may only contain letters, hyphens, and apostrophes.");
		if(!(Pattern.matches("[a-zA-Z[-']]*",familyName)))
			return CommonController.redirectToErrorPage(null, "The family name may only contain letters, hyphens, and apostrophes.");
		request.setFamilyName(familyName);
		if (middleName != null)
		{
			if(middleName.length()>32)
				return CommonController.redirectToErrorPage(null, "The middle name must be less than 32 characters and may only contain letters, hyphens, and apostrophes.");
			if(!(Pattern.matches("[a-zA-Z[-']]*",middleName)))
				return CommonController.redirectToErrorPage(null, "The middle name may only contain letters, hyphens, and apostrophes.");
			request.setMiddleName(middleName);
		}
		if(givenName == null)
			return CommonController.redirectToErrorPage(null, "You must provide a given name.");
		if(givenName.length()>32)
			return CommonController.redirectToErrorPage(null, "The given name must be less than 32 characters and may only contain letters, hyphens, and apostrophes.");
		if(!(Pattern.matches("[a-zA-Z[-']]*",givenName)))
			return CommonController.redirectToErrorPage(null, "The given name may only contain letters, hyphens, and apostrophes.");
		request.setGivenName(givenName);
		request.setGender(gender);
		if (patientIdentifier != null)
			request.setPatientIdentifier(patientIdentifier);
		if (phoneNumber != null)
		{
			if(true) //was planning on making an input validation method here but it was scoped out (many different forms of phone numbers which we cant expect to know)
				request.setPhoneNumber(phoneNumber);
		}

		// setup clinic information, both for local and remote
		request.setOriginalClinic(originalClinic);
		request.setRequestedBy(Context.getAuthenticatedUser().getId());
		request.setRequesterName(Context.getAuthenticatedUser().getUsername());
		if (request.getRequesterName() == null)
			return CommonController.redirectToErrorPage(null, "The current user has no username! Please fix before creating requests.");

		// add metadata
		request.setStatus(PatientDataTransferService.PENDINGLOCALAPPROVAL);
		request.setErrorRetries(0);

		// lookup local clinic info from DB and put in request
		DirectoryService dirService = Context.getService(DirectoryService.class);
		PDTSetting localClinicName = dirService.getSettingByName(DirectoryService.localClinicSetting);
		if (localClinicName == null)
			return CommonController.redirectToErrorPage(null, "The Patient Data Transfer setting to Clinic Domain Name has not yet been set. Please fix this before continuing.");
		request.setRequestingClinic(localClinicName.getValue());

		// setup temporal info
		request.setRequestingClinicId(-1);

		if (id != null) {
			// Replace the old request with a new request.
			// This basically invalidates the old request, then makes a new one.
			pdtService.replaceRequestAtIdWithObject(idNum, request);

			// Audit Log entry for successful creation of a request.
			auditLog.info("User " + Context.getAuthenticatedUser().getUsername() +
					"(" + Context.getAuthenticatedUser().getUserId() +
					") has successfully edited request for " + givenName +
					" " + middleName + " " + familyName + "'s patient data");
		} else {
			// If we are not editing, simply save the new request to the DB
			pdtService.saveRequest(request);

			// Audit Log entry for successful creation of a request.
			auditLog.info("User " + Context.getAuthenticatedUser().getUsername() +
					"(" + Context.getAuthenticatedUser().getUserId() +
					") has successfully created request for " + givenName +
					" " + middleName + " " + familyName + "'s patient data");
		}

		// Redirect user to current requests page.
		return new ModelAndView("redirect:/module/patientdatatransfer/currentRequests.form");
	}
}
