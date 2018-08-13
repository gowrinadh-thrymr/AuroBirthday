package com.auro.serviceimpl;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;

import org.springframework.stereotype.Service;

import com.auro.bean.BirthdayBean;
import com.auro.bean.EmployeeBean;
import com.auro.service.BirthdayService;

@Service
public class BirthdayServiceImpl implements BirthdayService {

	@Override
	public List<BirthdayBean> getUsers(InitialLdapContext ctx) {
		// JSONObject userAttributes = new JSONObject();
		List<BirthdayBean> employees = new LinkedList<>();
		List<EmployeeBean> birthdays=new LinkedList<>();
		List<EmployeeBean> serviceAnniversaries=new LinkedList<>();
		List<EmployeeBean> marriageAnniversaries=new LinkedList<>();
		try {
			// System.out.println("Searching.....");
			SearchControls constraints = new SearchControls();
			 String searchFilter = "(&(objectClass=inetOrgPerson)(cn=*))";
			constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
			String[] attrIDs = { "uid","cn", "employeeDateOfBirth", "employeeDateOfMarraige", "employeeDateOfJoining",
					"displayName","jpegPhoto"};
			constraints.setReturningAttributes(attrIDs);
			String searchBase = "CN=hydhoconn,DC=corp,DC=aurobindo,DC=com";

			// perform search on directory
			NamingEnumeration answer = ctx.search(searchBase, searchFilter, constraints);
			while (answer.hasMoreElements()) {
					
					Attributes attrs = ((SearchResult) answer.next()).getAttributes();
					if(attrs.get("employeeDateOfBirth")!=null) {
						if((String) attrs.get("employeeDateOfBirth").get()!="") {
							try {
								if(verifyDate(convertStringToDate((String) attrs.get("employeeDateOfBirth").get()))){
									EmployeeBean bean = new EmployeeBean();
									if(attrs.get("displayName")!=null)
										bean.setName((String) attrs.get("displayName").get());
									if(attrs.get("employeeDateOfBirth")!=null) {
										bean.setBirthDate(convertDateToStringPassword(convertStringToDate((String) attrs.get("employeeDateOfBirth").get())));
										
										bean.setBod(convertStringToDate((String) attrs.get("employeeDateOfBirth").get()));
									}
									if(attrs.get("jpegPhoto;binary")!=null) {
										bean.setProfilePic(getBase64Image((byte[])attrs.get("jpegPhoto;binary").get()));
									}
									birthdays.add(bean);
								}
							} catch (Exception e) {
								System.out.println("Unrecognizable Date format employeeDateOfBirth : "+(String) attrs.get("uid").get());
								
							}
						}
					}
					if(attrs.get("employeeDateOfMarraige")!=null) {
						if((String) attrs.get("employeeDateOfMarraige").get()!="") {
							try {
								if(verifyDate(convertStringToDate((String) attrs.get("employeeDateOfMarraige").get()))) {
									EmployeeBean bean = new EmployeeBean();
									if(attrs.get("displayName")!=null)
										bean.setName((String) attrs.get("displayName").get());
									if(attrs.get("employeeDateOfMarraige")!=null) {
										
										bean.setBirthDate(convertDateToStringPassword(convertStringToDate((String) attrs.get("employeeDateOfMarraige").get())));
										
										bean.setBod(convertStringToDate((String) attrs.get("employeeDateOfMarraige").get()));
									}
									if(attrs.get("jpegPhoto;binary")!=null) {
										bean.setProfilePic(getBase64Image((byte[])attrs.get("jpegPhoto;binary").get()));
									}
									marriageAnniversaries.add(bean);
								}
							} catch (Exception e) {
								System.out.println("Unrecognizable Date format employeeDateOfMarraige : "+(String) attrs.get("uid").get());
							}
						}
					}
					if(attrs.get("employeeDateOfJoining")!=null) {
						if((String) attrs.get("employeeDateOfJoining").get()!="") {
							try {
								if(verifyDate(convertStringToDate((String) attrs.get("employeeDateOfJoining").get()))) {
									EmployeeBean bean = new EmployeeBean();
									if(attrs.get("displayName")!=null)
										bean.setName((String) attrs.get("displayName").get());
									
									if(attrs.get("employeeDateOfJoining")!=null) {
										bean.setBirthDate(convertDateToStringPassword(convertStringToDate((String) attrs.get("employeeDateOfJoining").get())));
										bean.setBod(convertStringToDate((String) attrs.get("employeeDateOfJoining").get()));
									}
									if(attrs.get("jpegPhoto;binary")!=null) {
										bean.setProfilePic(getBase64Image((byte[])attrs.get("jpegPhoto;binary").get()));
									}
									
									serviceAnniversaries.add(bean);
								}
							} catch (Exception e) {
								e.printStackTrace();
								System.out.println("Unrecognizable Date format employeeDateOfJoining : "+(String) attrs.get("uid").get());
							}
						}
					}
					
					
					
					
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Exception@@@@@@@  " + ex);
		}
		
		BirthdayBean bdayBean=new BirthdayBean();
		bdayBean.setBirthdays(birthdays);
		bdayBean.setMarriageAnniversaries(marriageAnniversaries);
		bdayBean.setServiceAnniversaries(serviceAnniversaries);
		employees.add(bdayBean);
		return employees;
	}

	public String getBase64Image(byte[] blob) throws SQLException, IOException {
		String base64Image = org.apache.commons.codec.binary.Base64.encodeBase64String(blob);
		return base64Image;
	}
	public Date convertStringToDate(String input) {
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
	public Boolean verifyDate(Date input) {
		Boolean result = Boolean.FALSE;
		try {
			Calendar cal1 = Calendar.getInstance();
			Calendar cal2 = Calendar.getInstance();
			Calendar cal3 = Calendar.getInstance();
			cal1.setTime(input);
			cal2.setTime(new Date());
			cal3.setTime(new Date());
			cal2.add(Calendar.DAY_OF_MONTH,30);
			if (cal3.get(Calendar.DAY_OF_YEAR) <= cal1.get(Calendar.DAY_OF_YEAR)) {
				if (cal1.get(Calendar.DAY_OF_YEAR) <= cal2.get(Calendar.DAY_OF_YEAR)) {
					result = Boolean.TRUE;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = Boolean.FALSE;
		}
		return result;
	}
	
	public static String convertDateToStringPassword(Date date) throws ParseException {
		String password = "";
		String months[] = { "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		password = day +" "+ months[month];
		return password;
	}
	
/*	public static void main(String[] args) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(new Date());
		cal2.setTime(new Date());
		cal2.add(Calendar.DAY_OF_MONTH, -80);
		
		System.out.println(cal1.getTimeInMillis());
		System.out.println(cal2.getTimeInMillis());
//		System.out.println(cal2.get(Calendar.LONG_STANDALONE));
	}*/
}
