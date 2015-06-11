package org.openmrs.module.patientdatatransfer;

import org.openmrs.BaseOpenmrsObject;

import java.io.Serializable;

public class PDTSetting extends BaseOpenmrsObject implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String name;
	private String value;
	
	// No-arg constructor for Hibernate
	public PDTSetting() {
		
	}
	
	public PDTSetting(String name, String value) {
		super();
		this.setName(name);
		this.setValue(value);
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	
}
