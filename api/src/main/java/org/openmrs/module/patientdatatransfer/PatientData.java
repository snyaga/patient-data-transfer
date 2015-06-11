package org.openmrs.module.patientdatatransfer;


public class PatientData {

	private String patientXMLString;
	private String encountersXMLString;
	private String problemsXMLString;
	private String allergiesXMLString;
	
	// no-arg constructor to appease JSON serializer
	public PatientData() {
		patientXMLString = "";
		encountersXMLString = "";
		setProblemsXMLString("");
		setAllergiesXMLString("");
	}
	
	public PatientData(String patientXMLString, String encountersXMLString,
					   String problemsXMLString, String allergiesXMLString) {
		setPatientXMLString(patientXMLString);
		setEncountersXMLString(encountersXMLString);
		setProblemsXMLString(problemsXMLString);
		setAllergiesXMLString(allergiesXMLString);
	}
	
	public String getPatientXMLString() {
		return patientXMLString;
	}
	
	public void setPatientXMLString(String patientXMLString) {
		this.patientXMLString = patientXMLString;
	}

	public String getEncountersXMLString() {
		return encountersXMLString;
	}

	public void setEncountersXMLString(String encountersXMLString) {
		this.encountersXMLString = encountersXMLString;
	}

	public String getProblemsXMLString() {
		return problemsXMLString;
	}

	public void setProblemsXMLString(String problemsXMLString) {
		this.problemsXMLString = problemsXMLString;
	}

	public String getAllergiesXMLString() {
		return allergiesXMLString;
	}

	public void setAllergiesXMLString(String allergiesXMLString) {
		this.allergiesXMLString = allergiesXMLString;
	}

}