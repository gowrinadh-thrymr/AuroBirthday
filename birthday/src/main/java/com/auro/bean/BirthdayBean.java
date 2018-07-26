package com.auro.bean;

import java.util.ArrayList;
import java.util.List;

public class BirthdayBean {
/*
	private static  BirthdayBean beanInstance;
	
	private BirthdayBean() {
		
	}
	
	public static BirthdayBean getInstance() {
		if (beanInstance == null) {
			beanInstance = new BirthdayBean();
		}
		return beanInstance;
	}
	*/
	List<EmployeeBean> birthdays=new ArrayList<>();
	
	List<EmployeeBean> serviceAnniversaries=new ArrayList<>();
	
	List<EmployeeBean> marriageAnniversaries=new ArrayList<>();

	
	public List<EmployeeBean> getBirthdays() {
		return birthdays;
	}

	public void setBirthdays(List<EmployeeBean> birthdays) {
		this.birthdays = birthdays;
	}

	public List<EmployeeBean> getServiceAnniversaries() {
		return serviceAnniversaries;
	}

	public void setServiceAnniversaries(List<EmployeeBean> serviceAnniversaries) {
		this.serviceAnniversaries = serviceAnniversaries;
	}

	public List<EmployeeBean> getMarriageAnniversaries() {
		return marriageAnniversaries;
	}

	public void setMarriageAnniversaries(List<EmployeeBean> marriageAnniversaries) {
		this.marriageAnniversaries = marriageAnniversaries;
	}
	
	
}
