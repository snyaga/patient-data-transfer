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

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientdatatransfer.PatientDataRequest;
import org.openmrs.module.patientdatatransfer.PatientDataTransferAsyncHttp;
import org.openmrs.module.patientdatatransfer.api.LogService;
import org.openmrs.module.patientdatatransfer.api.PatientDataTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
public class  OutgoingRequestsPageController {

	private Log log = LogFactory.getLog(this.getClass());
	private Log auditLog = LogFactory.getLog(LogService.class);

	@Autowired
	private PatientDataTransferAsyncHttp asyncsender;

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
		Collection<PatientDataRequest> requests = pdtService.getRequestsByStatus(PatientDataTransferService.PENDINGLOCALAPPROVAL);
		Collection<PatientDataRequest> failedRequests = pdtService.getRequestsByStatus(PatientDataTransferService.FAILEDTOSEND);
		Collection<PatientDataRequest> pendingRequests = pdtService.getRequestsByStatus(PatientDataTransferService.SENDING);
		// combine the two request collections
		requests.addAll(pendingRequests);
		requests.addAll(failedRequests);
		// Remove any requests which were requested by this user, to separate roles.
		Iterator<PatientDataRequest> it = requests.iterator();
		while (it.hasNext())
			if (it.next().getRequestedBy() == Context.getAuthenticatedUser().getId())
				it.remove();
		// create Model
		ModelMap model = new ModelMap();
		// add the base URL for showing the consent form link
		model.addAttribute("consentFormBaseURL", servletRequest.getContextPath() + "/images/pdt");
		// Add objects to model
		model.addAttribute("outgoingRequestList", requests);
		return new ModelAndView("/module/patientdatatransfer/outgoingRequests", model);
	}

	//How to make sure the posted outgoingRequestId is safe ? Authorized
	@RequestMapping(value="/module/patientdatatransfer/outgoingRequests", method=RequestMethod.POST)
	public ModelAndView showPostOutgoingRequestsForm(
			@RequestParam("action") String action,
			@RequestParam(value = "outgoingRequestIdHidden") String[] allRequests,
			@RequestParam(value = "outgoingRequestId", required=false) String[] requests,
			@RequestParam(value = "denyReason") String[] denyReasons, Locale locale) {

		// determine whether to deny or approve based on the 'submit' button that was pressed
		boolean approve;
		if (action.equals(messageSource.getMessage("patientdatatransfer.outgoingRequests.approve", null, locale)))
			approve  = true;
		else if (action.equals(messageSource.getMessage("patientdatatransfer.outgoingRequests.deny", null, locale)))
			approve = false;
		else {
			return CommonController.redirectToErrorPage(null, "Unknown action was POSTed");
		}
		// input validation on deny reasons
		for (String denyReason : denyReasons) {
			ModelAndView validationResult = CommonController.validateDenyReason(denyReason);
			if (validationResult != null)
				return validationResult;
		}


		try{
			// actually approve/deny the selected requests
			requestorSign(requests, approve, denyReasons, allRequests);

			// if requests are being approved, send them to remote clinic
			if (approve) {
				// set intermediate status for the requests
				CommonController.applyStatusToAllRequests(requests, PatientDataTransferService.SENDING);

				// kick off the async send
			}

		}
		catch(Exception ex)
		{
			return CommonController.redirectToErrorPage(ex, "Problem occurred during signing and beginning to send the selected Outgoing requests.");
		}

		// Redirect user to home page after processing form data
		return new ModelAndView("redirect:/module/patientdatatransfer/outgoingRequests.form");
	}

	/**
	 * "Sign" the requests by approving or denying them. This is a local application step,
	 * nothing to do with SSL.
	 *
	 * @param requests - array of String id's for requests that have been signed
	 * @param approve - whether we are approving (true) or denying (false) the requests 
	 * @param denyReasons
	 * @param allRequests
	 */
	private void requestorSign(String[] requests, boolean approve, String[] denyReasons, String[] allRequests) {
		PatientDataTransferService pdtService = Context.getService(PatientDataTransferService.class);
		PatientDataRequest pdr = null;
		Integer int_id = null;
		if (requests == null) {
			log.info("Nothing To Approve");
			return;
		}

		log.info("The following patient requests were approved for request:");
		for (int i =0; i < requests.length; i++) {
			// get the existing request from the database
			int_id = Integer.parseInt(requests[i]);
			log.info(int_id);
			pdr = pdtService.getRequestById(int_id);
			// embed the signerName and signedBy fields into the request at this point
			pdr.setSignerName(Context.getAuthenticatedUser().getUsername());
			pdr.setSignedBy(Context.getAuthenticatedUser().getId());
			// if denied, change the status and also embed the deny message
			if (!approve) {
				pdr.setStatus(PatientDataTransferService.REJECTEDBYLOCALUSER); // denied by local user
				// find the right deny message, and embed
				for (int j = 0; j < allRequests.length; j++) {
					if (requests[i].equals(allRequests[j])) {
						pdr.setSignerMessage(denyReasons[j]);
						break;
					}
				}
				// Audit Log entry for a signer denying a request.
				auditLog.info("User " + Context.getAuthenticatedUser().getUsername() +
						"(" + Context.getAuthenticatedUser().getUserId() +
						") has denied the outgoing request #" + pdr.getId() + " for " + pdr.getGivenName() +
						" " + pdr.getMiddleName() + " " + pdr.getFamilyName() + "'s patient data");
			} else {
				// Audit Log entry for a signer approving a request.
				auditLog.info("User " + Context.getAuthenticatedUser().getUsername() +
						"(" + Context.getAuthenticatedUser().getUserId() +
						") has approved the outgoing request #" + pdr.getId() + " for " + pdr.getGivenName() +
						" " + pdr.getMiddleName() + " " + pdr.getFamilyName() + "'s patient data");
			}
			pdtService.updateRequest(pdr);
		}
	}

}
