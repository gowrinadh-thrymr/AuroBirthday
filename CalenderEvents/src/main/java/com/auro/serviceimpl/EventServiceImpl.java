package com.auro.serviceimpl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.auro.constant.ApiConstants;
import com.auro.controller.CalendarController;
import com.auro.service.AppUserService;
import com.auro.service.CommunityService;
import com.auro.service.EventService;
import com.auro.utility.Utility;
import com.ibm.portal.um.User;
import com.ibm.portal.um.exceptions.PumaAttributeException;
import com.ibm.portal.um.exceptions.PumaMissingAccessRightsException;
import com.ibm.portal.um.exceptions.PumaModelException;
import com.ibm.portal.um.exceptions.PumaSystemException;

@Service
public class EventServiceImpl implements EventService {

	@Autowired
	CommunityService communityService;

	
	  @Autowired
	  AppUserService appUserService;
	 

	@Override
	public LinkedList<JSONObject> loadEventsBasesOnUser(User user) {
		Map<String, Object> userMap = null;
		if (user != null) {
			try {
				userMap = appUserService.getUserAttributes(user);
			} catch (PumaAttributeException | PumaSystemException | PumaModelException
					| PumaMissingAccessRightsException e1) {
				e1.printStackTrace();
			}
		}
		String allCommunities = ApiConstants.base_Url + ApiConstants.retrieve_all_communities;

		// All Communities
		LinkedList<JSONObject> communities = null;

		// Filtered Communities
		LinkedList<JSONObject> result = null;

		// Events in community
		LinkedList<JSONObject> communityEvents = null;

		// Total Events
		LinkedList<JSONObject> events = null;

		if (user != null) {
			try {
				Document doc = Utility.getConnectionFeed(allCommunities);
				// Getting All Communities
				communities = communityService.getAllCommunitiesJson(doc);
				if (communities != null && userMap != null) {
					result = new LinkedList<JSONObject>();
					for (JSONObject community : communities) {
						// Filtering the communities based on community member
						// or not
						if (communityService.checkCommunityMemberOrNot(community.get("communityUuid").toString(),
								userMap.get("uid").toString())) {
							result.add(community);
						}
					}
				}
				// Filtered communities that he is a member
				events = new LinkedList<JSONObject>();
				if (result.size() > 0) {
					for (JSONObject community : result) {
						// Getting Events in community
						communityEvents = null;
						communityEvents = getCommunityEvents(community.get("communityUuid").toString().trim());
						for (JSONObject event : communityEvents) {
							events.add(event);
						}

					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return events;
	}

	@Override
	public LinkedList<JSONObject> getCommunityEvents(String communityUuid) {
		JSONObject obj = null;
		LinkedList<JSONObject> jsonObjects = new LinkedList<JSONObject>();
		if (communityUuid != null && !communityUuid.trim().isEmpty()) {
			try {
				String eventsApi = ApiConstants.base_Url + ApiConstants.retrieve_community_events + "?calendarUuid="
						+ communityUuid+ "&type=event";
				Document doc = Utility.getConnectionFeed(eventsApi);
				if(doc!=null) {	
					System.out.println("eventsApi============ "+eventsApi);
					NodeList nodes = doc.getElementsByTagName("entry");
					for (int i = 0; i < nodes.getLength(); i++) {
						obj = new JSONObject();
						Node node = nodes.item(i);
						if (node.getNodeType() == Node.ELEMENT_NODE) {
							Element element = (Element) node;
							obj.put("occasion", getValue("title", element));
							obj.put("eventUuid", getValue("snx:eventUuid", element));
							// obj.put("updated", getValue("updated", element));
							obj.put("event_date", getValue("snx:startDate", element));
							Calendar cal = Calendar.getInstance();
							Date date1 = null;
							try {
								date1 = new SimpleDateFormat("yyyy-MM-dd").parse(getValue("snx:startDate", element));
								cal.setTime(date1);
								int year = cal.get(Calendar.YEAR);
								int month = cal.get(Calendar.MONTH);
								int day = cal.get(Calendar.DAY_OF_MONTH);
								obj.put("year", year);
								obj.put("month", month + 1);
								obj.put("day", day);
							} catch (ParseException e) {
								e.printStackTrace();
							}
						
							// obj.put("location", getValue("snx:location",
							// element));
							// obj.put("event_link", getValue("link", element));
							// obj.put("summary", getValue("summary", element));
							// obj.put("communityUuid",getValue("snx:communityUuid",
							// element));
							jsonObjects.add(obj);
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
		}
		return jsonObjects;
	}

	@Override
	public JSONObject retrieveEvent(String eventUuid) {
		JSONObject obj = null;
		String eventApi = ApiConstants.base_Url + ApiConstants.retrieve_event + "?eventUuid=" + eventUuid.trim();
		Document doc;
		try {
			doc = Utility.getConnectionFeed(eventApi);
			NodeList nodes = doc.getElementsByTagName("entry");
			for (int i = 0; i < nodes.getLength(); i++) {
				obj = new JSONObject();
				Node node = nodes.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					obj.put("eventTitle", getValue("title", element));
					obj.put("eventUuid", getValue("snx:eventUuid", element));
					obj.put("updated", getValue("updated", element));
					obj.put("startDate", getValue("snx:startDate", element));
					obj.put("location", getValue("snx:location", element));
					obj.put("endDate", getValue("snx:endDate", element));
					// obj.put("summary", getValue("summary", element));
					obj.put("communityUuid", getValue("snx:communityUuid", element));

				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}

	@Override
	public String getValue(String tag, Element element) {
		Node node = null;
		try {
			NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
			node = (Node) nodes.item(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(tag + " Not Found");
		}
		return node.getNodeValue();
	}


}
