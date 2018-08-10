package com.auro.service;

import java.util.LinkedList;
import java.util.List;

import com.auro.bean.HolidayBean;
import com.ibm.workplace.wcm.api.SiteArea;
import com.ibm.workplace.wcm.api.Workspace;

public interface CalendarService {

	Workspace getWorkSpace();

	List<SiteArea> getHolidaysSiteArea(Workspace contentWorkSpace);

	LinkedList<HolidayBean> getContent(Workspace contentWorkSpace, SiteArea siteArea);
	
	public List<HolidayBean> getHolidaysByUserLogin(Workspace contentWorkSpace,List<SiteArea> siteAreas,String entity,String division,String unit);
}
