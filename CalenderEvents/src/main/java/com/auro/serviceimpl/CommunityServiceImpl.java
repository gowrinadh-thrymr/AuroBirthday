package com.auro.serviceimpl;

import java.io.IOException;
import java.util.LinkedList;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.auro.constant.ApiConstants;
import com.auro.service.CommunityService;
import com.auro.utility.Utility;

@Service
public class CommunityServiceImpl implements CommunityService {

	@Override
	public LinkedList<JSONObject> getAllCommunitiesJson(Document doc) {
		LinkedList<JSONObject> jsonObjects = new LinkedList<JSONObject>();
		JSONObject obj = null;
		NodeList nodes;
		try {
			nodes = doc.getElementsByTagName("entry");
			for (int i = 0; i < nodes.getLength(); i++) {
				obj = new JSONObject();
				Node node = nodes.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					obj.put("communityUuid", getValue("snx:communityUuid", element));
					obj.put("communityName", getValue("title", element));
					jsonObjects.add(obj);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonObjects;
	}

	@Override
	public LinkedList<JSONObject> getCommunityMembers(String communityUuid) {
		String membersApi = ApiConstants.base_Url + ApiConstants.retrieve_community_members + "?communityUuid="
				+ communityUuid;
		LinkedList<JSONObject> jsonObjects = new LinkedList<JSONObject>();
		JSONObject obj = null;
		Document doc;
		try {
			doc = Utility.getConnectionFeed(membersApi);
			NodeList nodes = doc.getElementsByTagName("entry");
			for (int i = 0; i < nodes.getLength(); i++) {
				obj = new JSONObject();
				Node node = nodes.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					obj.put("member", getValue("title", element));
					jsonObjects.add(obj);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
//			System.out.println(e.getMessage());
		}
		return jsonObjects;
	}

	@Override
	public Boolean checkCommunityMemberOrNot(String communityUuid, String userName) {
		Boolean result = Boolean.FALSE;
		LinkedList<JSONObject> membersList = null;
		try {
			if (communityUuid != null && !communityUuid.trim().isEmpty())
				// feed = Utility.getConnectionFeed(membersApi);
				membersList = getCommunityMembers(communityUuid.trim());
			for (JSONObject member : membersList) {
				if (userName.equalsIgnoreCase(member.get("member").toString())) {
					result = Boolean.TRUE;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
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
//			System.out.println(tag + " Not Found");
		}
		return node.getNodeValue();
	}

}
