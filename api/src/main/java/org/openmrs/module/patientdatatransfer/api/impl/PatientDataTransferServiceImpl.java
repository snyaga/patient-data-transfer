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
package org.openmrs.module.patientdatatransfer.api.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.activelist.Allergy;
import org.openmrs.activelist.Problem;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.patientdatatransfer.PDTClinic;
import org.openmrs.module.patientdatatransfer.PDTSetting;
import org.openmrs.module.patientdatatransfer.PatientDataRequest;
import org.openmrs.module.patientdatatransfer.api.DirectoryService;
import org.openmrs.module.patientdatatransfer.api.PatientDataTransferService;
import org.openmrs.module.patientdatatransfer.api.db.PatientDataTransferDAO;

import javax.net.ssl.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.List;


/**
 * It is a default implementation of {@link PatientDataTransferService}.
 */
public class PatientDataTransferServiceImpl extends BaseOpenmrsService implements PatientDataTransferService {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private PatientDataTransferDAO dao;
	
	/**
     * @param dao the dao to set
     */
    public void setDao(PatientDataTransferDAO dao) {
	    this.dao = dao;
    }
    
    /**
     * @return the dao
     */
    public PatientDataTransferDAO getDao() {
	    return dao;
    }
    
    public List<PatientDataRequest> getRequests() {
    	return dao.getRequests();
    }
    
    public PatientDataRequest getRequestById(int id)
    {
    	return dao.getRequestById(id);
    }
    
    public List<PatientDataRequest> getRequestsByUser(int userId)
    {
    	return dao.getRequestsByUser(userId);
    }
    
    public void saveRequest(PatientDataRequest request) {
    	dao.saveRequest(request);
    }


	@Override
	public List<PatientDataRequest> getRequestsByStatus(int status) {
		return dao.getRequestsByStatus(status);
	}

	@Override
	public void updateRequest(PatientDataRequest request) {
		dao.updateRequest(request);	
	}

	@Override
	public void cleanPatient(Patient patient) {
		dao.cleanPatient(patient);
	}

	@Override
	public void cleanEncounter(Encounter encounter, Patient patient) {
		dao.cleanEncounter(encounter, patient);
	}
	
	@Override
	public void cleanProblem(Problem problem, Patient patient) {
		dao.cleanProblem(problem, patient);
	}
	
	@Override
	public void cleanAllergy(Allergy allergy, Patient patient) {
		dao.cleanAllergy(allergy, patient);
	}
	
	@Override
	public boolean isDNSValid(String hostname) {
		/* checks whether DNS resolution and secure directory cache provide the same answer */
		
		DirectoryService dirService = Context.getService(DirectoryService.class);
		PDTClinic originalClinic = dirService.getClinicByDomainName(hostname);

		if (originalClinic == null) {
			//clinic doesn't exist in directory
			log.error("Clinic doesn't exist in SDC");
			return false;
		}
		
		try {
			if (!InetAddress.getByName(hostname).getHostAddress().equals(originalClinic.getIpAddress())) {
				//mismatch occured; possible malicious activity
				log.error("DNS resolution mismatch: [" + InetAddress.getByName(hostname).getHostAddress() + "] vs. [" + originalClinic.getIpAddress()+ "] ");
				return false;
			}
		} catch (UnknownHostException e) {
			//DNS couldnt resolve hostname to IP
			log.error("DNS resolution failed for: " + hostname);
			return false;
		}
		
		return true;
	}
	
	@Override
	public void setupSSLforKeyStore() {

		Log log = LogFactory.getLog("org.openmrs.module.patientdatatransfer.web.controller.OutgoingRequestsPageController");
		DirectoryService dirService = Context.getService(DirectoryService.class);
		PDTSetting localClinicName 	= dirService.getSettingByName("clinicDomainName");
		String storeType = "JKS";
		char SEP = File.separatorChar;
		String privKeystoreLocation  = System.getProperty("catalina.base") + SEP + localClinicName.getValue() + ".keystore";
		String pubTruststoreLocation = System.getProperty("java.home") + SEP + "lib" + SEP + "security" + SEP + "omrsca.truststore";
		log.info("Private Keystore: " + privKeystoreLocation);
		log.info("Public Truststore: " + pubTruststoreLocation);
		
		KeyStore keystore = null;
		KeyStore truststore = null;
	
		KeyManager[] keymanagers = null;
		TrustManager[] trustmanagers = null;
		
		try {
			
			/* setup keystore (for client auth; local pub+priv pair) */
			InputStream keystoreLocation = new FileInputStream(privKeystoreLocation);
			//char [] keystorePassword = "changeit".toCharArray(); //definitely right
			char [] keystorePassword = null;
		    //char [] keyPassword = "openmrs".toCharArray();
		    char [] keyPassword = "openmrs".toCharArray();


		    keystore = KeyStore.getInstance(storeType);
			keystore.load(keystoreLocation, keystorePassword);
			log.info("Using the '" + KeyManagerFactory.getDefaultAlgorithm() + "' algorithm for keystore");
		    KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		    kmfactory.init(keystore, keyPassword);
		    keymanagers = kmfactory.getKeyManagers();
		    log.info("Keystore initialized successfully!");

		    
		    /* setup truststore (CA certs) */
		    InputStream truststoreLocation = new FileInputStream(pubTruststoreLocation);
		    char [] truststorePassword = null;
	
		    truststore = KeyStore.getInstance(storeType);
		    truststore.load(truststoreLocation, truststorePassword);
		    log.info("Using the '" + TrustManagerFactory.getDefaultAlgorithm() + "' algorithm for truststore");
		    TrustManagerFactory tmfactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		    tmfactory.init(truststore);
		    trustmanagers =  tmfactory.getTrustManagers();
		    log.info("Truststore initialized successfully!");
		  
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		    
		try {
		    /* setup default SSL connection */
			SSLContext sslContext = SSLContext.getInstance("TLS");
			//sslContext.
			
		    /* use pkix b/c sslcontext doesnt support crl's */
			/*
			X509SSLContextFactory sslContextFactory = new X509SSLContextFactory(keystore, "openmrs", truststore);
			log.info("PKIX Initialized Successfully!");
			*/
		    /* Load CRL from database cache */
			/*
			PDTSetting crl = dirService.getSettingByName("crlcache");
			String crldata = crl.getValue();
			if (crl.getValue() == null) {
				log.warn("No cached CRL list");
			} else {
				log.info("Initializing CRL list from cache");
				sslContextFactory.addCrl(new ByteArrayInputStream(crldata.getBytes()));	
			}
			
			SSLContext sslContext = sslContextFactory.buildSSLContext();
			*/
			

			
			sslContext.init(keymanagers , trustmanagers , new SecureRandom());
			SSLContext.setDefault(sslContext);
		    log.info("SSL Context Initialized!");
		} catch (Exception e) {
			log.error(e.getMessage());
			log.error(e.getStackTrace().toString());
		}

	}

	@Override
	public void replaceRequestAtIdWithObject(int id, PatientDataRequest request) {
		dao.replaceRequestAtIdWithObject(id, request);
	}
	
	
}