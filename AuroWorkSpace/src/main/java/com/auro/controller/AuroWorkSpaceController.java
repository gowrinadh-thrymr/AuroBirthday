package com.auro.controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.auro.bean.AuroWorkSpaceBean;
import com.auro.bean.UserAttributes;
import com.auro.service.AuroWorkSpaceService;
import com.auro.service.ConnectionService;
import com.ibm.portal.um.PumaHome;
import com.ibm.portal.um.PumaProfile;
import com.ibm.portal.um.User;
import com.ibm.portal.um.exceptions.PumaAttributeException;
import com.ibm.portal.um.exceptions.PumaException;
import com.ibm.portal.um.exceptions.PumaMissingAccessRightsException;
import com.ibm.portal.um.exceptions.PumaModelException;
import com.ibm.portal.um.exceptions.PumaSystemException;
import com.ibm.workplace.wcm.api.SiteArea;
import com.ibm.workplace.wcm.api.Workspace;

@Controller
@RequestMapping(value = "VIEW")
public class AuroWorkSpaceController {

	private PumaHome pumaHome;

	@Autowired
	ConnectionService connService;
	
	@Autowired
	AuroWorkSpaceService auroWorkSpaceService;
	
	@PostConstruct
	public void init() {
		javax.naming.Context ctx;
		try {
			ctx = new javax.naming.InitialContext();
			pumaHome = (PumaHome) ctx.lookup(PumaHome.JNDI_NAME);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public User getCurrentUser() throws PumaException {
		// To retrieve current User
		PumaProfile profile = pumaHome.getProfile();
		return profile.getCurrentUser();
	}

	public Map<String, Object> getUserAttributes(User user)
			throws PumaAttributeException, PumaSystemException, PumaModelException, PumaMissingAccessRightsException {
		List<String> returnAttributes = new ArrayList<String>();
		returnAttributes.add("uid");
		returnAttributes.add("cn");
		PumaProfile profile = pumaHome.getProfile();
		Map<String, Object> values = profile.getAttributes(user, returnAttributes);
		return values;
	}
	
	@RenderMapping
	public String defaultView(ModelMap map) {
		String userName = "";
		String redirectionPage = "";
		List<AuroWorkSpaceBean> workSpaceResult=new LinkedList<>();
		InitialLdapContext ctx = null;
		try {
			 ctx = connService.setUpConnection();
			Map<String, Object> getUser = getUserAttributes(getCurrentUser());
			for (Entry<String, Object> entry : getUser.entrySet()) {
				if (entry.getKey().equalsIgnoreCase("uid")) {
					userName = entry.getValue().toString();
				}
			}
		
		if (userName.equalsIgnoreCase("anonymous portal user")) {
		
		} else {
			UserAttributes userAttributes=getUserBasicAttributes(userName, ctx);
			redirectionPage = "AuroWorkSpace";
			Workspace workspace = auroWorkSpaceService.getWorkSpace();
			if (workspace != null) {
				List<SiteArea> siteAreas = auroWorkSpaceService.getEntitySiteArea(workspace);
				if (siteAreas != null) {
					workSpaceResult= auroWorkSpaceService.getAuroWorkSpacebyUserLogin(workspace, siteAreas,userAttributes.getEntity(),userAttributes.getDivision(),userAttributes.getUnit());   //DO DYNAMIC
				}
			} else {

			}
		}
			System.out.println(workSpaceResult.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("EXCEPTION @@@@@@@@@@ " + ex);
		}finally {
			if(ctx!=null) {
				try {
					ctx.close();
				} catch (NamingException e) {
					e.printStackTrace();
				}
			}
		}
		map.addAttribute("workSpaceResult", workSpaceResult);
		return redirectionPage;

	}
	
	private UserAttributes getUserBasicAttributes(String username, LdapContext ctx) {
		UserAttributes userAttributes=new UserAttributes();
		try {
			SearchControls constraints = new SearchControls();
			String filter = "(uid=" + username + ")";
			constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
			String[] attrIDs = { "uid" , "cn", "mail", "Unit", "Division" ,"Entity" };
			constraints.setReturningAttributes(attrIDs);
			String searchBase = "CN=hydhoconn,DC=corp,DC=aurobindo,DC=com";
			NamingEnumeration answer = ctx.search(searchBase, filter, constraints);
			if (answer != null && answer.hasMore()) {
				Attributes attrs = ((SearchResult) answer.next()).getAttributes();
				if(attrs.get("mail").get()!=null)
				userAttributes.setMail((String) attrs.get("mail").get());
				
				if(attrs.get("Unit").get()!=null)
				userAttributes.setUnit((String) attrs.get("Unit").get());
				
				if(attrs.get("Division").get()!=null)
				userAttributes.setDivision((String) attrs.get("Division").get());
				
				if(attrs.get("Entity").get()!=null)
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
}
