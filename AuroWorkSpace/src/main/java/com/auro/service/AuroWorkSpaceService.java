package com.auro.service;

import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONObject;

import com.auro.bean.AuroWorkSpaceBean;
import com.ibm.workplace.wcm.api.SiteArea;
import com.ibm.workplace.wcm.api.Workspace;

public interface AuroWorkSpaceService {

	public Workspace getWorkSpace();

	public List<SiteArea> getEntitySiteArea(Workspace contentWorkSpace);
	
	public List<AuroWorkSpaceBean> getAuroWorkSpacebyUserLogin(Workspace contentWorkSpace,List<SiteArea> siteAreas,String entity,String division,String unit);
	
	public List<AuroWorkSpaceBean> getAllContent(Workspace contentWorkSpace, SiteArea siteArea);

	public LinkedList<JSONObject> getContent(String name, Workspace contentWorkSpace, List<SiteArea> siteAreas);
}
