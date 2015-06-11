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
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.patientdatatransfer.PDTClinic;
import org.openmrs.module.patientdatatransfer.PDTSetting;
import org.openmrs.module.patientdatatransfer.XMLDirectoryHandler;
import org.openmrs.module.patientdatatransfer.api.DirectoryService;
import org.openmrs.module.patientdatatransfer.api.db.PatientDataTransferDAO;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * It is a default implementation of {@link DirectoryServiceImpl}.
 */
public class DirectoryServiceImpl extends BaseOpenmrsService implements DirectoryService {
	
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

	@Override
	public List<PDTClinic> getClinics() {
		return dao.getClinics();
	}

	@Override
	public PDTSetting getSettingByName(String name) {
		return dao.getSettingByName(name);
	}

	@Override
	public PDTClinic getClinicByDomainName(String domainName) {
		return dao.getClinicByDomainName(domainName);
	}

	
	@Override
	public List<PDTSetting> getSettings() {
		return dao.getSettings();
	}
	
	@Override
	public void updateClinic(PDTClinic clinic) {
		dao.updateClinic(clinic);
	}
	@Override
	public void deleteClinicById(Integer id) {
		dao.deleteClinicById(id);
	}
	@Override
	public void saveClinic(PDTClinic clinic) {
		dao.saveClinic(clinic);
	}
	@Override
	public void updateSetting(PDTSetting setting) {
		dao.updateSetting(setting);
	}
	@Override
	public void deleteSettingById(Integer id) {
		dao.deleteSettingById(id);
	}
	
	@Override
	public void saveSetting(PDTSetting setting) {
		dao.saveSetting(setting);
	}

	@Override
	public void updateDirectory() {
		DirectoryService dirService = Context.getService(DirectoryService.class);
		PDTSetting DirectoryServer = dirService.getSettingByName("DirectoryServer");	
		PDTSetting DUpdateServer = dirService.getSettingByName("DUpdateServer");		
		PDTSetting omrsport = dirService.getSettingByName("omrsport");
		PDTSetting omrsinstance = dirService.getSettingByName("omrsinstance");

		/* issue update to directory server */
		try {
			URL updateurl = new URL(DUpdateServer.getValue() + "?omrsport=" + omrsport.getValue() + "&omrsinstance=" + omrsinstance.getValue());
			HttpsURLConnection httpscn = (HttpsURLConnection)updateurl.openConnection();
			log.info("Update response: " + httpscn.getResponseMessage());
		} catch (MalformedURLException e) {
			log.error("Invalid URL; could not connect to update service");
		} catch (IOException e) {
			log.error("Connection error: Could not run directory update");
		}
		
		
		/* download xml directory and parse clinics and crl */
		log.info("Connecting to directory service");
		ContentHandler omrsdirectoryparse;
		try {
			XMLReader sdreader = XMLReaderFactory.createXMLReader();
			omrsdirectoryparse = new XMLDirectoryHandler();
			sdreader.setContentHandler(omrsdirectoryparse);
			sdreader.parse(new InputSource(new URL(DirectoryServer.getValue()).openStream()));
			//for(PDTClinic zx : ((XMLDirectoryHandler) omrsdirectoryparse).getClinics())
			//	log.info("Loaded clinic: " + zx.getName());

		} catch (SAXException e) {
			log.error("Error parsing XML directory; cache update will not be performed");
			return;
		} catch (MalformedURLException e) {
			log.error("Error opening directory server URL; cache update will not be performed");
			return;
		} catch (IOException e) {
			log.error("Error opening directory server URL; cache update will not be performed");
			return;
		}		
		
		/* wipe all cached clinics (table truncation)*/
		/* this is commented out to allow custom clinics to be added locally without membership in DNS */
		//for(PDTClinic clinic : dirService.getClinics())
		//	dirService.deleteClinicById(clinic.getId());
		
		for(PDTClinic dclinic : ((XMLDirectoryHandler) omrsdirectoryparse).getClinics()) {
			/* search for each directory-present clinic in the clinics table, update if found, insert if not.
			 * the idea here is to not delete existing clinic entries, in case they were added manually.
			 * this may be disabled later by uncommenting the annotated section above, denoting table truncation 
			 */
			
			//log.info("Loaded clinic: " + zx.getName());
			//searches clinics table for clinic that matches FQDN of directory-present clinic and deletes if present
			if (dirService.getClinicByDomainName(dclinic.getName()) != null) {
				log.info("Deleting clinic: " + dclinic.getName() + " (" + dirService.getClinicByDomainName(dclinic.getName()).getDescription() + ")");
				dirService.deleteClinicById(dirService.getClinicByDomainName(dclinic.getName()).getId());
			}
			dirService.saveClinic(dclinic);
			log.info("Saved clinic: " + dclinic.getName() + " (" + dclinic.getDescription() + ")");
		}
		log.info("Clinics loaded successfully!");
		
		/* write base64'd CRL into database cache */
		String newcrl = ((XMLDirectoryHandler) omrsdirectoryparse).getCRLBase64();
		PDTSetting crldata = new PDTSetting("crldata", newcrl);
		if (dirService.getSettingByName("crldata")!=null)
			dirService.deleteSettingById(dirService.getSettingByName("crldata").getId());
		dirService.saveSetting(crldata);
		log.info("CRL loaded successfully!");
	}
	
}