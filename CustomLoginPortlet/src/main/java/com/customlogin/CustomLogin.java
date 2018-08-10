package com.customlogin;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.ibm.portal.auth.exceptions.AuthenticationException;
import com.ibm.portal.auth.exceptions.AuthenticationFailedException;
import com.ibm.portal.auth.exceptions.PasswordInvalidException;
import com.ibm.portal.auth.exceptions.PortletLoginDisabledException;
import com.ibm.portal.auth.exceptions.SessionTimeOutException;
import com.ibm.portal.auth.exceptions.SystemLoginException;
import com.ibm.portal.auth.exceptions.UserAlreadyLoggedInException;
import com.ibm.portal.auth.exceptions.UserIDInvalidException;
import com.ibm.portal.portlet.service.PortletServiceHome;
import com.ibm.portal.portlet.service.PortletServiceUnavailableException;
import com.ibm.portal.portlet.service.login.LoginHome;
import com.ibm.portal.portlet.service.login.LoginService;
import com.ibm.websphere.security.WSSecurityException;

@Controller
@RequestMapping(value = "VIEW")
public class CustomLogin {

	private LoginHome loginHome;
	
	@SuppressWarnings("restriction")
	@PostConstruct
	public void init() throws PortletServiceUnavailableException {
//		System.out.println("Entering CustomLoginPortlet.init()");
		try {
			InitialContext ctx = new InitialContext();
			PortletServiceHome psh = (PortletServiceHome) ctx.lookup(LoginHome.JNDI_NAME);
			loginHome = (LoginHome) psh.getPortletService(LoginHome.class);
			// loginHome =(LoginHome) ctx.lookup(LoginHome.JNDI_NAME);
		} catch (NamingException e) {
			e.printStackTrace();
		}
//		System.out.println("Exiting CustomLoginPortlet.init()");
	}
	@RenderMapping
	public String doView(ModelMap map) throws PortletException, IOException {
//		System.out.println("Loginpage");
		return "login";
	}

	@ActionMapping(params = "action=logindetails")
	public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {
		try {
//			System.out.println("Entering CustomLoginPortlet.processAction()");
			String userId = request.getParameter("USER_NAME");
//			System.out.println("User Id " + userId);
			String password = request.getParameter("PASSWORD");
//			System.out.println("Password " + password);
			LoginService loginService = loginHome.getLoginService(request, response);
//			System.out.println("Login Service " + loginService);
			Map contextMap = new HashMap();
			contextMap.put(LoginService.DO_RESUME_SESSION_KEY, new Boolean(false));
			loginService.login(userId, password.toCharArray(), contextMap, null);
//			System.out.println("Exiting CustomLoginPortlet.processAction()");
			response.sendRedirect("/wps/portal/dashboard/");
		} catch (PasswordInvalidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UserIDInvalidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AuthenticationFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SessionTimeOutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PortletLoginDisabledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UserAlreadyLoggedInException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SystemLoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (javax.security.auth.login.LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WSSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (com.ibm.portal.auth.exceptions.LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
