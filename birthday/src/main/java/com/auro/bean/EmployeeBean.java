package com.auro.bean;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

public class EmployeeBean {

	private String uid;

	private String name;

	private String marriageAnniversaryDate;

	private String serviceAnniversaryDate;

	private String birthDate;

	private Date bod;

	private String profilePic;

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public Date getBod() {
		return bod;
	}

	public void setBod(Date bod) {
		this.bod = bod;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMarriageAnniversaryDate() {
		return marriageAnniversaryDate;
	}

	public void setMarriageAnniversaryDate(String marriageAnniversaryDate) {
		this.marriageAnniversaryDate = marriageAnniversaryDate;
	}

	public String getServiceAnniversaryDate() {
		return serviceAnniversaryDate;
	}

	public void setServiceAnniversaryDate(String serviceAnniversaryDate) {
		this.serviceAnniversaryDate = serviceAnniversaryDate;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	@Override
	public String toString() {
		return "EmployeeBean [uid=" + uid + ", name=" + name + ", marriageAnniversaryDate=" + marriageAnniversaryDate
				+ ", serviceAnniversaryDate=" + serviceAnniversaryDate + ", birthDate=" + birthDate + "]";
	}

	public static Date convertStringToDate(String input) {
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		Date startDate = null;
		try {
			startDate = df.parse(input.trim());
		} catch (ParseException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return startDate;
	}

	/* Comparator for sorting the list by Date */
	public static Comparator<EmployeeBean> employeeBean = new Comparator<EmployeeBean>() {
		public int compare(EmployeeBean s1, EmployeeBean s2) {
			Calendar cal1 = Calendar.getInstance();
			Calendar cal2 = Calendar.getInstance();
			cal1.setTime(s1.getBod());
			cal2.setTime(s2.getBod());
			int month1 = cal1.get(Calendar.MONTH);
			int month2 = cal2.get(Calendar.MONTH);
			if (month1 < month2)
				return -1;
			else if (month1 == month2)
				return cal1.get(Calendar.DAY_OF_MONTH) - cal2.get(Calendar.DAY_OF_MONTH);
			else
				return 1;
		}
	};

}
