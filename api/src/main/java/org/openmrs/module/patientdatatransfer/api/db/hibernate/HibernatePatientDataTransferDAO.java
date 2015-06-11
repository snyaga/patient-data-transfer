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
package org.openmrs.module.patientdatatransfer.api.db.hibernate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.metadata.ClassMetadata;
import org.openmrs.*;
import org.openmrs.activelist.Allergy;
import org.openmrs.activelist.Problem;
import org.openmrs.module.patientdatatransfer.PDTClinic;
import org.openmrs.module.patientdatatransfer.PDTSetting;
import org.openmrs.module.patientdatatransfer.PatientDataRequest;
import org.openmrs.module.patientdatatransfer.api.db.PatientDataTransferDAO;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * It is a default implementation of  {@link PatientDataTransferDAO}.
 */
@SuppressWarnings("unchecked")
public class HibernatePatientDataTransferDAO implements PatientDataTransferDAO {
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private final Date INFINITY_DATE = new Date(253402214400000L); // used to represent Infinity as a time in the temporal DB table
	private SessionFactory sessionFactory;
	
	/**
     * @param sessionFactory the sessionFactory to set
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
	    this.sessionFactory = sessionFactory;
    }
    
	/**
     * @return the sessionFactory
     */
    public SessionFactory getSessionFactory() {
	    return sessionFactory;
    }
    
    //
    // REQUESTS
    //
    public PatientDataRequest getRequestById(int id) {
    	Session session = sessionFactory.openSession();
		session.beginTransaction();
		Property pId = Property.forName("id");
		Property pValidTo = Property.forName("validTo");
		
		PatientDataRequest requests =
				(PatientDataRequest) session.createCriteria(PatientDataRequest.class).add(pValidTo.eq(INFINITY_DATE)).add(pId.eq(new Integer(id))).uniqueResult();
		session.getTransaction().commit();
		session.close();
		return requests;
    }

	@Override
	public List<PatientDataRequest> getRequests() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Property pValidTo = Property.forName("validTo");
		// basically doing a SELECT * FROM requests ORDER BY requestedDate DESC
		List<PatientDataRequest> requests =
				session.createCriteria(PatientDataRequest.class)
				.add(pValidTo.eq(INFINITY_DATE))
				.addOrder(Order.desc("dateRequested"))
				.list();		
		session.getTransaction().commit();
		session.close();
		return requests;
	}
	
	@Override
	public List<PatientDataRequest> getRequestsByUser(int userId)
	{
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Property requestedBy = Property.forName("requestedBy");
		Property pValidTo = Property.forName("validTo");
		// basically doing a SELECT * FROM requests WHERE requestedBy = userId
		List<PatientDataRequest> requests =
				session.createCriteria(PatientDataRequest.class)
				.add(requestedBy.eq(new Integer(userId)))
				.add(pValidTo.eq(INFINITY_DATE))
				.addOrder(Order.desc("dateRequested"))
				.list();
		session.getTransaction().commit();
		session.close();
		return requests;
	}

	@Override
	public void saveRequest(PatientDataRequest request) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		// set validTo to infinity if this is a new request
		// but don't overwrite a value that's already there, if there is one
		if (request.getValidTo() == null)
			request.setValidTo(INFINITY_DATE);
		session.save(request);
		session.getTransaction().commit();
		session.close();
	}
	
	@Override
	public List<PatientDataRequest> getRequestsByStatus(int status) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Property status_property = Property.forName("status");
		Property pValidTo = Property.forName("validTo");
		// basically doing a SELECT * FROM requests WHERE requestedBy = userId
		List<PatientDataRequest> requests =
				session.createCriteria(PatientDataRequest.class)
				.add(pValidTo.eq(INFINITY_DATE))
				.add(status_property.eq(new Integer(status)))
				.addOrder(Order.desc("dateRequested"))
				.list();
		session.getTransaction().commit();
		session.close();
		return requests;
	}

	@Override
	public void updateRequest(PatientDataRequest request) {
		// get existing request with this ID
		PatientDataRequest existingRequest = getRequestById(request.getId());
		// set its validTo to the current date
		existingRequest.setValidTo(new Date());
		// set its ID to null so it will make a new row
		existingRequest.setId(null);
		// persist the changes
		saveRequest(existingRequest);

		// persist the changes of the actual request
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.update(request);
		session.getTransaction().commit();
		session.close();
	}
	
	/**
	 * This method performs a rather unique operation. It takes the ID
	 * of an existing request and invalidates it. It then saves a new request.
	 * This is intended to be used to atomically invalidate one request, replacing
	 * it with another. This is used by the Edit Request functionality of the system.
	 */
	@Override
	public void replaceRequestAtIdWithObject(int id, PatientDataRequest request) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		// get existing request with this ID
		PatientDataRequest existingRequest = getRequestById(id);
		// set its validTo to the current date, effectively invalidating it
		existingRequest.setValidTo(new Date());
		// persist the changes for the update
		session.update(existingRequest);
		// Now save the new request.
		// set validTo to infinity if this is a new request
		// but don't overwrite a value that's already there, if there is one
		if (request.getValidTo() == null)
			request.setValidTo(INFINITY_DATE);
		session.save(request);
		session.getTransaction().commit();
		session.close();
	}
	
	//
	// CLINICS
	//

	@Override
	public List<PDTClinic> getClinics() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		List<PDTClinic> clinics =
				session.createCriteria(PDTClinic.class).list();		
		session.getTransaction().commit();
		session.close();
		
		// TODO: get revocation status from real CRL
		Random random = new Random();
		for (PDTClinic clinic : clinics) {
			clinic.setIsRevoked(random.nextBoolean());
		}
		
		return clinics;
	}
	
	@Override
	public void saveClinic(PDTClinic clinic) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(clinic);
		session.getTransaction().commit();
		session.close();
	}
	
	@Override
	public void updateClinic(PDTClinic clinic) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.update(clinic);
		session.getTransaction().commit();
		session.close();		
	}
	
	@Override
	public PDTClinic getClinicByDomainName(String domainName) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Property pName = Property.forName("name");
		PDTClinic clinic =
				(PDTClinic) session.createCriteria(PDTClinic.class).add(pName.eq(domainName)).uniqueResult();		
		session.getTransaction().commit();
		session.close();
		return clinic;
	}
	
	@Override
	public void deleteClinicById(Integer id) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		PDTClinic clinic = null;
		try {
			clinic = (PDTClinic) session.load(PDTClinic.class, id);
			session.delete(clinic);
		} catch (Exception e) {
			log.error("Something went very wrong deleting a clinic");
			log.error("Problem=" + e.getLocalizedMessage());
		} finally {
			session.getTransaction().commit();
			session.close();
		}
	}

	//
	// SETTINGS
	//
	
	@Override
	public PDTSetting getSettingByName(String name) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Property pName = Property.forName("name");
		PDTSetting setting =
				(PDTSetting) session.createCriteria(PDTSetting.class).add(pName.eq(name)).uniqueResult();		
		session.getTransaction().commit();
		session.close();
		return setting;
	}
	
	@Override
	public List<PDTSetting> getSettings() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		List<PDTSetting> settings =
				session.createCriteria(PDTSetting.class).list();		
		session.getTransaction().commit();
		session.close();
		return settings;
	}

	@Override
	public void saveSetting(PDTSetting setting) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(setting);
		session.getTransaction().commit();
		session.close();
	}
	
	@Override
	public void updateSetting(PDTSetting setting) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.update(setting);
		session.getTransaction().commit();
		session.close();		
	}

	@Override
	public void deleteSettingById(Integer id) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		PDTSetting setting = null;
		try {
			setting = (PDTSetting) session.load(PDTSetting.class, id);
			session.delete(setting);
		} catch (Exception e) {
			log.error("Something went very wrong deleting a setting");
			log.error(e.getStackTrace());
		} finally {
			session.getTransaction().commit();
			session.close();
		}
	}

	@Override
	public void cleanPatient(Patient patient) {
		ClassMetadata md = sessionFactory.getClassMetadata(Patient.class);
		
		try {
			// Destroy the Person/Patient ID
			Object[] arrWithNull = {null};
			Method m = new PropertyDescriptor(md.getIdentifierPropertyName(), Patient.class).getWriteMethod();
			log.info("invoking setter: " + m.getName());
			m.invoke(patient, arrWithNull);

			// Destroy the Person/Patient UUID
			m = new PropertyDescriptor("uuid", Patient.class).getWriteMethod();
			log.info("invoking setter: " + m.getName());
			m.invoke(patient, arrWithNull);
			
			
			for (PersonAddress addr : patient.getAddresses()) {
				md = sessionFactory.getClassMetadata(PersonAddress.class);
				
				// Destroy the Address(es) IDs
				m = new PropertyDescriptor(md.getIdentifierPropertyName(), PersonAddress.class).getWriteMethod();
				log.info("invoking setter: " + m.getName());
				m.invoke(addr, arrWithNull);
				
				// Destroy the Address(es) UUIDs
				m = new PropertyDescriptor("uuid", PersonAddress.class).getWriteMethod();
				log.info("invoking setter: " + m.getName());
				m.invoke(addr, arrWithNull);
			}
			
			for (PersonName name : patient.getNames()) {
				md = sessionFactory.getClassMetadata(PersonName.class);
				
				// Destroy the Name(s) IDs
				m = new PropertyDescriptor(md.getIdentifierPropertyName(), PersonName.class).getWriteMethod();
				log.info("invoking setter: " + m.getName());
				m.invoke(name, arrWithNull);
				
				// Destroy the Name(s) UUIDs
				m = new PropertyDescriptor("uuid", PersonName.class).getWriteMethod();
				log.info("invoking setter: " + m.getName());
				m.invoke(name, arrWithNull);
			}
			
			for (PatientIdentifier identifier : patient.getIdentifiers()) {
				md = sessionFactory.getClassMetadata(PatientIdentifier.class);
				
				// Destroy the Identifier(s) IDs
				m = new PropertyDescriptor(md.getIdentifierPropertyName(), PatientIdentifier.class).getWriteMethod();
				log.info("invoking setter: " + m.getName());
				m.invoke(identifier, arrWithNull);
				
				// Destroy the Identifier(s) UUIDs
				m = new PropertyDescriptor("uuid", PatientIdentifier.class).getWriteMethod();
				log.info("invoking setter: " + m.getName());
				m.invoke(identifier, arrWithNull);
			}
		} catch (IntrospectionException e) {
			log.error("IntrospectionException while in cleanPatient", e);
		} catch (IllegalArgumentException e) {
			log.error("IllegalArgumentException while in cleanPatient", e);
		} catch (IllegalAccessException e) {
			log.error("IllegalAccessException while in cleanPatient", e);
		} catch (InvocationTargetException e) {
			log.error("InvocationTargetException while in cleanPatient", e);
		}
	}

	@Override
	public void cleanEncounter(Encounter encounter, Patient patient) {
		ClassMetadata md = sessionFactory.getClassMetadata(Encounter.class);
		
		try {
			// Destroy the Encounter ID
			Object[] arrWithNull = {null};
			Method m = new PropertyDescriptor(md.getIdentifierPropertyName(), Encounter.class).getWriteMethod();
			log.info("invoking setter: " + m.getName());
			m.invoke(encounter, arrWithNull);
			
			// Destroy the Encounter UUID
			m = new PropertyDescriptor("uuid", Encounter.class).getWriteMethod();
			log.info("invoking setter: " + m.getName());
			m.invoke(encounter, arrWithNull);
			
			// add the newly created Patient's ID to the Encounter object for correct foreign key constraints
			encounter.setPatientId(patient.getId());
			encounter.setPatient(patient);
			
			for (Obs obs : encounter.getObs()) {
				md = sessionFactory.getClassMetadata(Obs.class);
				
				// Destroy the Observation(s) IDs
				m = new PropertyDescriptor(md.getIdentifierPropertyName(), Obs.class).getWriteMethod();
				log.info("invoking setter: " + m.getName());
				m.invoke(obs, arrWithNull);
				
				// add the newly created Patient into this observation
				obs.setPerson(patient);
				
				// Obliterate the Observation(s) UUIDs
				m = new PropertyDescriptor("uuid", Obs.class).getWriteMethod();
				log.info("invoking setter: " + m.getName());
				m.invoke(obs, arrWithNull);
			}
			
		} catch (IntrospectionException e) {
			log.error("IntrospectionException while in cleanEncounter", e);
		} catch (IllegalArgumentException e) {
			log.error("IllegalArgumentException while in cleanEncounter", e);
		} catch (IllegalAccessException e) {
			log.error("IllegalAccessException while in cleanEncounter", e);
		} catch (InvocationTargetException e) {
			log.error("InvocationTargetException while in cleanEncounter", e);
		}
	}

	@Override
	public void cleanProblem(Problem problem, Patient patient) {
		ClassMetadata md = sessionFactory.getClassMetadata(Problem.class);
		
		try {
			// Destroy the Problem ID
			Object[] arrWithNull = {null};
			Method m = new PropertyDescriptor(md.getIdentifierPropertyName(), Problem.class).getWriteMethod();
			log.info("invoking setter: " + m.getName());
			m.invoke(problem, arrWithNull);
			
			// Destroy the Problem UUID
			m = new PropertyDescriptor("uuid", Problem.class).getWriteMethod();
			log.info("invoking setter: " + m.getName());
			m.invoke(problem, arrWithNull);
			
			// add the newly created Patient to the Problem object for correct foreign key constraints
			problem.setPerson(patient);
			
		} catch (IntrospectionException e) {
			log.error("IntrospectionException while in cleanProblem", e);
		} catch (IllegalArgumentException e) {
			log.error("IllegalArgumentException while in cleanProblem", e);
		} catch (IllegalAccessException e) {
			log.error("IllegalAccessException while in cleanProblem", e);
		} catch (InvocationTargetException e) {
			log.error("InvocationTargetException while in cleanProblem", e);
		}
	}

	@Override
	public void cleanAllergy(Allergy allergy, Patient patient) {
		ClassMetadata md = sessionFactory.getClassMetadata(Allergy.class);
		
		try {
			// Destroy the Allergy ID
			Object[] arrWithNull = {null};
			Method m = new PropertyDescriptor(md.getIdentifierPropertyName(), Allergy.class).getWriteMethod();
			log.info("invoking setter: " + m.getName());
			m.invoke(allergy, arrWithNull);
			
			// Destroy the Allergy UUID
			m = new PropertyDescriptor("uuid", Allergy.class).getWriteMethod();
			log.info("invoking setter: " + m.getName());
			m.invoke(allergy, arrWithNull);
			
			// add the newly created Patient to the Allergy object for correct foreign key constraints
			allergy.setPerson(patient);
			
		} catch (IntrospectionException e) {
			log.error("IntrospectionException while in cleanAllergy", e);
		} catch (IllegalArgumentException e) {
			log.error("IllegalArgumentException while in cleanAllergy", e);
		} catch (IllegalAccessException e) {
			log.error("IllegalAccessException while in cleanAllergy", e);
		} catch (InvocationTargetException e) {
			log.error("InvocationTargetException while in cleanAllergy", e);
		}
	}
}