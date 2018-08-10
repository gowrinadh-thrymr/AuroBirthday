package com.auro.service;

import java.util.LinkedList;

import org.json.JSONObject;
import org.w3c.dom.Element;

import com.ibm.portal.um.User;

public interface EventService {

	LinkedList<JSONObject> loadEventsBasesOnUser(User user);

	LinkedList<JSONObject> getCommunityEvents(String communityUuid);

	JSONObject retrieveEvent(String eventUuid);

	String getValue(String tag, Element element);
	
	
}
