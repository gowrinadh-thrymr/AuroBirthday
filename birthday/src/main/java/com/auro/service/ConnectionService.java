package com.auro.service;

import java.sql.SQLException;

import javax.naming.ldap.InitialLdapContext;

public interface ConnectionService {

	InitialLdapContext getInstance() throws SQLException;
}
