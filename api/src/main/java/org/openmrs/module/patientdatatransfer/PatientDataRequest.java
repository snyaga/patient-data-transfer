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

import org.openmrs.BaseOpenmrsMetadata;
import org.openmrs.BaseOpenmrsObject;
import org.openmrs.module.patientdatatransfer.api.PatientDataTransferService;

import java.io.Serializable;
import java.util.Date;

/**
 * It is a model class. It should extend either {@link BaseOpenmrsObject} or {@link BaseOpenmrsMetadata}.
 */
public class PatientDataRequest extends BaseOpenmrsObject implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	private Integer requestingClinicId; // stores the ID (primary key) of the request, as exists in the requesting clinic
	
	private Date dateRequested; // date when the request was created
	
	private Date dateTransferred; // date when the request resulted in a finished, successful transfer of data
	
	private String givenName;
	
	private String middleName;
	
	private String familyName;
	
	private Date birthDate;
		
	private char gender;
	
	private String patientIdentifier; // any country-specific patient identifier that can help ID the patient
	
	private String phoneNumber; // used to send SMS to patient if desired
	
	private String requestingClinic; // domain name (from Directory Service) of requesting clinic
	
	private String originalClinic; // domain name (from Directory Service) of remote clinic (where original patient data resides)
	
	private String requesterName; // user that created the request
	
	private int requestedBy; // references ID column of openmrs_users table, for requesting clinic
	
	private String signerName; // user that signed off on request
	
	private int signedBy; // references ID column of openmrs_users table, for requesting clinic
	
	private String signerMessage; // message from local signer (if any)
	
	private String remoteUserName; // user that approved/denied request on other end
	
	private int remoteUserId; // references ID column of openmrs_users table, for remote clinic
	
	private String remoteMessage; // message from remote user (if any)
	
	private String consentForm; // full path to the uploaded file
	
//	private PatientData data; // contains the actual patient data, in a TBD format. Not stored in DB, only augmented when sent, then on receipt is handled.
	
	private int status;
	
	@SuppressWarnings("unused")
	private String printStatus;
	
	private int errorRetries; // if status is an error status, this indicates how many times the activity was retried. This will be 0 if no error occurred.
	
	private Date validTo; // to provide temporal auditing to DB
	
	// No-arg constructor to appease Hibernate
	public PatientDataRequest() {
		super();
	}
	
	@Override
	public Integer getId() {
		return id;
	}
	
	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRequestingClinicId() {
		return requestingClinicId;
	}

	public void setRequestingClinicId(Integer requestingClinicId) {
		this.requestingClinicId = requestingClinicId;
	}

	public Date getDateRequested() {
		return dateRequested;
	}

	public void setDateRequested(Date dateRequested) {
		
		this.dateRequested = dateRequested;
	}

	public Date getDateTransferred() {
		return dateTransferred;
	}

	public void setDateTransferred(Date dateTransferred) {
		this.dateTransferred = dateTransferred;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public char getGender() {
		return gender;
	}

	public void setGender(char gender) {
		this.gender = gender;
	}

	public String getPatientIdentifier() {
		return patientIdentifier;
	}

	public void setPatientIdentifier(String patientIdentifier) {
		this.patientIdentifier = patientIdentifier;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getRequestingClinic() {
		return requestingClinic;
	}

	public void setRequestingClinic(String requestingClinic) {
		this.requestingClinic = requestingClinic;
	}

	public String getOriginalClinic() {
		return originalClinic;
	}

	public void setOriginalClinic(String originalClinic) {
		this.originalClinic = originalClinic;
	}

	public String getRequesterName() {
		return requesterName;
	}

	public void setRequesterName(String requesterName) {
		this.requesterName = requesterName;
	}

	public int getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(int requestedBy) {
		this.requestedBy = requestedBy;
	}

	public String getSignerName() {
		return signerName;
	}

	public void setSignerName(String signerName) {
		this.signerName = signerName;
	}

	public int getSignedBy() {
		return signedBy;
	}

	public void setSignedBy(int signedBy) {
		this.signedBy = signedBy;
	}

	public String getSignerMessage() {
		return signerMessage;
	}

	public void setSignerMessage(String signerMessage) {
		this.signerMessage = signerMessage;
	}

	public String getRemoteUserName() {
		return remoteUserName;
	}

	public void setRemoteUserName(String remoteUserName) {
		this.remoteUserName = remoteUserName;
	}

	public int getRemoteUserId() {
		return remoteUserId;
	}

	public void setRemoteUserId(int remoteUserId) {
		this.remoteUserId = remoteUserId;
	}

	public String getRemoteMessage() {
		return remoteMessage;
	}

	public void setRemoteMessage(String remoteMessage) {
		this.remoteMessage = remoteMessage;
	}

	public String getConsentForm() {
		return consentForm;
	}

	public void setConsentForm(String consentForm) {
		this.consentForm = consentForm;
	}

//	public PatientData getData() {
//		return data;
//	}
//
//	public void setData(PatientData data) {
//		this.data = data;
//	}

/*	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
		this.printStatus = getPrintStatus();
	}

	public int getErrorRetries() {
		return errorRetries;
	}

	public void setErrorRetries(int errorRetries) {
		this.errorRetries = errorRetries;
	}

	public Date getValidTo() {
		return validTo;
	}

	public void setValidTo(Date validTo) {
		this.validTo = validTo;
	}
	
	/*public String getPrintStatus()
	{
		switch(status)
		{
		case PatientDataTransferService.PENDINGLOCALAPPROVAL: return "Pending Local approval";
		case PatientDataTransferService.SENTTOREMOTECLINIC: return "Sent to remote clinic";
		case PatientDataTransferService.RECEIVEDBYREMOTECLINIC: return "Received by remote clinic";
		case PatientDataTransferService.APPROVEDBYREMOTECLINIC: return "Approved by remote clinic";
		case PatientDataTransferService.RETURNEDTOREQUESTINGCLINIC: return "Returned to requesting clinic";
		case PatientDataTransferService.TRANSFERCOMPLETE: return "Transfer complete";
		case PatientDataTransferService.SENTDENYTOREQUESTINGCLINIC: return "Sent Deny to Requesting Clinic";
		
		case PatientDataTransferService.REJECTEDBYLOCALUSER: return "Rejected by local user";
		case PatientDataTransferService.FAILEDTOSEND: return "Failed to send";
		case PatientDataTransferService.REJECTEDBYREMOTEUSER: return "Rejected by remote user";
		case PatientDataTransferService.FAILEDTOAUGMENTDATA: return "Failed to augment data";
		case PatientDataTransferService.FAILEDTORETURNDATA: return "Failed to return data";
		
		case PatientDataTransferService.SENDING: return "Sending...";
		case PatientDataTransferService.RETURNING: return "Returning...";
		default: return "Status should not exist";
				
		}
				
	}*/
	
	public void setPrintStatus(String status) {
		this.printStatus = status;
	}

}