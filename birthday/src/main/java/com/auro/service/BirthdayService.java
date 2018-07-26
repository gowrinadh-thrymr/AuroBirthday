package com.auro.service;

import java.util.List;

import javax.naming.ldap.InitialLdapContext;

import com.auro.bean.BirthdayBean;

public interface BirthdayService {

	List<BirthdayBean> getUsers(InitialLdapContext ctx);
}
