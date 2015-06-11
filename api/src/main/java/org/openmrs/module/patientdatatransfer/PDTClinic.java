package org.openmrs.module.patientdatatransfer;

import org.openmrs.BaseOpenmrsObject;

import java.io.Serializable;

public class PDTClinic extends BaseOpenmrsObject implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String name;
	private String description;
	private String ipAddress;
	private Integer port;
	private String urlPrefix;
	private Float latitude;
	private Float longitude;
	private Boolean isRevoked;
	
	// No-arg constructor for Hibernate
	public PDTClinic() {
		
	}
	
	public PDTClinic(String name, String ipAddress) {
		super();
		this.name = name;
		this.ipAddress = ipAddress;
	}
	
	public PDTClinic(String name, String ipAddress, String description, Integer port, String urlPrefix, float lat, float lng) {
		super();
		this.name = name;
		this.ipAddress = ipAddress;
		this.description = description;
		this.port = port;
		this.urlPrefix = urlPrefix;
		this.latitude = lat;
		this.longitude = lng;
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

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getUrlPrefix() {
		return urlPrefix;
	}

	public void setUrlPrefix(String urlPrefix) {
		this.urlPrefix = urlPrefix;
	}

	public Float getLatitude() {
		return latitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	public Float getLongitude() {
		return longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	public Boolean getIsRevoked() {
		return isRevoked;
	}
	
	public void setDescription(String desc) {
		this.description = desc;
	}
	
	public String getDescription() {
		return this.description;
	}

	public void setIsRevoked(Boolean isRevoked) {
		this.isRevoked = isRevoked;
	}

	public String toString() {
		String outstr = "";
		outstr += "Name = >" + this.name + "<\n";
		outstr += "Description = >" + this.description + "<\n";
		outstr += "IP Address = >" + this.ipAddress + "<\n";
		outstr += "Port = >" + this.port + "<\n";
		outstr += "Instance = >" + this.urlPrefix + "<\n";
		outstr += "Longitude = >" + this.longitude + "<\n";
		outstr += "Latitude = >" + this.latitude + "<\n";
		return outstr;
	}
}
