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
 
package org.openmrs.module.patientdatatransfer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.activelist.Allergy;
import org.openmrs.activelist.Problem;
import org.openmrs.api.EncounterService;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientdatatransfer.api.DirectoryService;
import org.openmrs.module.patientdatatransfer.api.PatientDataTransferService;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/* So, this crazy thing is a non component which wraps the methods
 * for POSTs/PUTs and fires them off in a new thread using Spring's 
 * TaskExecutor and runnable stuff.
 * */
public class PatientDataTransferAsyncHttp {
	private Log log = LogFactory.getLog(this.getClass());
	private TaskExecutor taskExecutor;

	public PatientDataTransferAsyncHttp(TaskExecutor te) {
		taskExecutor = te;
	}

	public void sendPutRequest(final String[] requests, final boolean approve) {
		taskExecutor.execute(new Runnable() {
			public void run() {
				Context.openSession();
				Context.setUserContext(Context.getUserContext());
				_sendPutRequest(requests, approve);
				Context.closeSession();
			}
		});
	}


	/**
	 * This method takes the array of string ID's representing requests, and then
	 * takes action based on whether they were approved or denied. In both cases,
	 * the request with an updated status is returned to the requesting clinic via
	 * the HTTP PUT verb. In the approve case, the Patient Data is augmented to the
	 * request before sending.
	 *
	 * @param requests - String array of id's of requests which were approved/denied remotely
	 * @param approve  - whether the requests were approved (true) or denied (false)
	 */
	private void _sendPutRequest(String[] requests, boolean approve) {
		PatientDataTransferService pdtService = Context.getService(PatientDataTransferService.class);
		DirectoryService dirService = Context.getService(DirectoryService.class);
		for (String str_id : requests) {
			// parse ID in string form to int, then use it to get the PDT request from DB
			Integer int_id = Integer.parseInt(str_id);
			PatientDataRequest pdr = pdtService.getRequestById(int_id);


			// use the domain name of "requesting clinic" to look up its IP address from directory service
			String requestingClinicDomainName = pdr.getRequestingClinic();
			PDTClinic requestingClinic = dirService.getClinicByDomainName(requestingClinicDomainName);


		}
	}
}