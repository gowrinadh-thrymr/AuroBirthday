package com.auro.bean;

public class UserAttributes {

	private String uid;
	
	private String mail;
	
	private String unit;
	
	private String division;
	
	private String entity;
	
	@Override
	public String toString() {
		return "UserAttributes [uid=" + uid + ", mail=" + mail + ", unit=" + unit + ", division=" + division
				+ ", entity=" + entity + "]";
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}
	
	
}
