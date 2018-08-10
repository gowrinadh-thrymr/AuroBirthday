package com.auro.constant;

public class ApiConstants {

//	public static final String base_Url = "http://hydhqdevcon.corp.aurobindo.com:9081";  UAT
	
	public static final String base_Url = "http://hydhoconnection.corp.aurobindo.com:9081";

	// calendarUuid
	public static final String retrieve_community_events = "/communities/calendar/atom/calendar/event";

	// communityUuid
	public static final String retrieve_community = "/communities/service/atom/community/instance";

	// communityUuid,memberid
	public static final String retrieve_community_member = "/communities/service/atom2/acl";

	// communityUuid
	public static final String retrieve_community_members = "/communities/service/atom/community/members";

	// eventUuid
	public static final String retrieve_event = "/communities/calendar/atom/calendar/event";

	// all communities
	public static final String retrieve_all_communities = "/communities/service/atom/communities/all";
}
