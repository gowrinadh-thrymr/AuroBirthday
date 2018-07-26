package com.auro.serviceimpl;

import java.sql.SQLException;
import java.util.Hashtable;

import javax.naming.ldap.InitialLdapContext;

import org.springframework.stereotype.Service;

import com.auro.service.ConnectionService;

@Service
public class ConnectionServiceImpl implements ConnectionService {

	protected static InitialLdapContext connection;

	public static InitialLdapContext getConnection() {
		if (connection == null) {
			connection = getConnection();
		}
		return connection;
	}

	@Override
	public InitialLdapContext getInstance() throws SQLException {
		// Set up values for LDAP Context Initialization
		// System.out.println("IN NEW CONNECTION");
		InitialLdapContext ctx = null;
		Hashtable<String, String> hashtable = null;
		// Set up default parameters for LDAP connection
		final String urlparam = "java.naming.provider.url";
		final String usernameparam = "java.naming.security.principal";
		final String passwordparam = "java.naming.security.credentials";
		// final String versionparam = "java.naming.ldap.version";
		final String factory_initialparam = "java.naming.factory.initial";
		final String authenticationparam = "java.naming.security.authentication";
		// final String protocolparam = "java.naming.security.protocol";
		final String referralparam = "java.naming.referral";
		try {
			// Set up LDAP configuration settings
			hashtable = new Hashtable<String, String>();
			hashtable.put(factory_initialparam, "com.sun.jndi.ldap.LdapCtxFactory");
			hashtable.put(authenticationparam, "simple");
			hashtable.put(usernameparam, "cn=root");
			hashtable.put(passwordparam, "Welcome@123");
			hashtable.put(referralparam, "follow");
			// hashtable.put(urlparam, "ldap://192.168.2.174:389");
			hashtable.put(urlparam, "ldap://10.0.45.108:1389");
			// Make LDAP connection
			ctx = new InitialLdapContext(hashtable, null);
			System.out.println("TDS Connection Started...");
		} catch (Exception ex) {
			ex.getMessage();
			System.out.println("Connection Failed " + ex.getMessage());
		}
		return ctx;
	}

}
