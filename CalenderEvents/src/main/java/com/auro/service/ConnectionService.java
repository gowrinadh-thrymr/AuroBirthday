package com.auro.service;

import javax.naming.ldap.InitialLdapContext;

public interface ConnectionService {

	InitialLdapContext setUpConnection();
	
}
