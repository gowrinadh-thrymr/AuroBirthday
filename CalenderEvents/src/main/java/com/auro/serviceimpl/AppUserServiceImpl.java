package com.auro.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.naming.NamingException;

import org.springframework.stereotype.Service;

import com.auro.service.AppUserService;
import com.ibm.portal.um.PumaHome;
import com.ibm.portal.um.PumaProfile;
import com.ibm.portal.um.User;
import com.ibm.portal.um.exceptions.PumaAttributeException;
import com.ibm.portal.um.exceptions.PumaException;
import com.ibm.portal.um.exceptions.PumaMissingAccessRightsException;
import com.ibm.portal.um.exceptions.PumaModelException;
import com.ibm.portal.um.exceptions.PumaSystemException;

@Service
public class AppUserServiceImpl implements AppUserService{

	private PumaHome pumaHome;
	
	@Override
	@PostConstruct
	public void init() {
		javax.naming.Context ctx;
		try {
			ctx = new javax.naming.InitialContext();
			pumaHome = (PumaHome) ctx.lookup(PumaHome.JNDI_NAME);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public User getCurrentUser() throws PumaException {
		// To retrieve current User
		PumaProfile profile = pumaHome.getProfile();
		return profile.getCurrentUser();
	}

	@Override
	public Map<String, Object> getUserAttributes(User user)
			throws PumaAttributeException, PumaSystemException, PumaModelException, PumaMissingAccessRightsException {
		List<String> returnAttributes = new ArrayList<String>();
		returnAttributes.add("uid");
		returnAttributes.add("cn");
		PumaProfile profile = pumaHome.getProfile();
		Map<String, Object> values = profile.getAttributes(user, returnAttributes);
		return values;
	}
}
