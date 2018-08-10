package com.auro.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.auro.bean.HolidayBean;
import com.auro.bean.UserAttributes;
import com.auro.service.AppUserService;
import com.auro.service.CalendarService;
import com.auro.service.CommunityService;
import com.auro.service.ConnectionService;
import com.auro.service.EventService;
import com.auro.serviceimpl.ConnectionServiceImpl;
import com.ibm.portal.um.exceptions.PumaException;
import com.ibm.workplace.wcm.api.SiteArea;
import com.ibm.workplace.wcm.api.Workspace;

@Controller
@RequestMapping(value = "VIEW")
public class CalendarController {
	
	@Autowired
	ConnectionService connService;
	
	@Autowired
	CalendarService calendarService;


	@Autowired
	CommunityService communityService;

	@Autowired
	EventService eventService;

	
	@Autowired
	AppUserService appUserService;

	
	@RenderMapping
	public String defaultView(ModelMap map) {
		List<HolidayBean> json = null;
		LinkedList<org.json.JSONObject> eventsJson = null;
		String userName = "";
		Map<String, Object> getUser = null;
		InitialLdapContext ctx = connService.setUpConnection();
		if(ctx!=null) {
			try {
				getUser = appUserService.getUserAttributes(appUserService.getCurrentUser());
				for (Entry<String, Object> entry : getUser.entrySet()) {
					if (entry.getKey().equalsIgnoreCase("uid")) {
						userName = entry.getValue().toString();
					}
				}
				if (userName.equalsIgnoreCase("anonymous portal user")) {
					
				} else {
					UserAttributes userAttributes = getUserBasicAttributes(userName, ctx);
					// System.out.println("USER ATTRIBUTES "+userAttributes.toString());
					Workspace contentWorkSpace = calendarService.getWorkSpace();
					if (contentWorkSpace != null) {
						List<SiteArea> siteAreas = calendarService.getHolidaysSiteArea(contentWorkSpace);
						json = calendarService.getHolidaysByUserLogin(contentWorkSpace, siteAreas,
								userAttributes.getEntity(), userAttributes.getDivision(), userAttributes.getUnit());
						 System.out.println("CALENDER"+ json);
					}
					eventsJson = eventService.loadEventsBasesOnUser(appUserService.getCurrentUser());
				}
			} catch (PumaException e1) {
				e1.printStackTrace();
			}finally {
				try {
					ctx.close();
				} catch (NamingException e) {
					e.printStackTrace();
				}
			}
		}else {
			System.out.println("InitialLdapContext NULL");
		}
		// System.out.println("MERGED LIST---------" + merge(json, eventsJson));
		map.addAttribute("event_json", merge(json, eventsJson));
		return "calendarEvents";
	}

	private UserAttributes getUserBasicAttributes(String username, LdapContext ctx) {
		UserAttributes userAttributes = new UserAttributes();
		try {
			SearchControls constraints = new SearchControls();
			String filter = "(uid=" + username + ")";
			constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
			String[] attrIDs = { "uid", "cn", "mail", "Unit", "Division", "Entity" };
			constraints.setReturningAttributes(attrIDs);
			String searchBase = "CN=hydhoconn,DC=corp,DC=aurobindo,DC=com";
			NamingEnumeration answer = ctx.search(searchBase, filter, constraints);
			if (answer != null && answer.hasMore()) {
				Attributes attrs = ((SearchResult) answer.next()).getAttributes();

				if (attrs.get("mail").get() != null)
					userAttributes.setMail((String) attrs.get("mail").get());

				if (attrs.get("Unit").get() != null)
					userAttributes.setUnit((String) attrs.get("Unit").get());

				if (attrs.get("Division").get() != null)
					userAttributes.setDivision((String) attrs.get("Division").get());

				if (attrs.get("Entity").get() != null)
					userAttributes.setEntity((String) attrs.get("Entity").get());

				userAttributes.setUid((String) attrs.get("uid").get());

			} else {
				System.out.println("--------------");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Exception@@@@@@@  " + ex.getMessage());
		}
		return userAttributes;
	}
	public JSONObject merge(List<HolidayBean> list1, LinkedList<JSONObject> list2) {
		List<JSONObject> list = new LinkedList<JSONObject>();
		LinkedList<JSONObject> l = new LinkedList<>();
		if (list1 != null) {
			for (int i = 0; i < list1.size(); i++) {
				JSONObject obj = new JSONObject();
				obj.put("day", list1.get(i).getDay());
				obj.put("month", list1.get(i).getMonth());
				obj.put("year", list1.get(i).getYear());
				obj.put("occasion", list1.get(i).getHolidayName());
				l.add(obj);
			}
			list.addAll(l);
		}

		if (list2 != null)
			list.addAll(list2);
		JSONObject result = new JSONObject();
		result.put("events", list);
		return result;
	}
	
	

}
