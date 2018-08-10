package com.auro.service;

import java.util.Map;

import com.ibm.portal.um.User;
import com.ibm.portal.um.exceptions.PumaAttributeException;
import com.ibm.portal.um.exceptions.PumaException;
import com.ibm.portal.um.exceptions.PumaMissingAccessRightsException;
import com.ibm.portal.um.exceptions.PumaModelException;
import com.ibm.portal.um.exceptions.PumaSystemException;

public interface AppUserService {

	public void init();
	
	public User getCurrentUser() throws PumaException;
	
	public Map<String, Object> getUserAttributes(User user)
			throws PumaAttributeException, PumaSystemException, PumaModelException, PumaMissingAccessRightsException;
}
