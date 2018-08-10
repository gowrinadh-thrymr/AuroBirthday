package com.auro.service;

import java.util.LinkedList;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface CommunityService {

	LinkedList<JSONObject> getAllCommunitiesJson(Document doc);

	LinkedList<JSONObject> getCommunityMembers(String communityUuid);

	Boolean checkCommunityMemberOrNot(String communityUuid, String userName);

	String getValue(String tag, Element element);
}
