package com.auro.controller;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.naming.ldap.InitialLdapContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import com.auro.bean.BirthdayBean;
import com.auro.bean.EmployeeBean;
import com.auro.service.BirthdayService;
import com.auro.service.ConnectionService;

@Controller
@RequestMapping(value = "VIEW")
public class BirthdayPortlet {

	@Autowired
	ConnectionService connService;

	@Autowired
	BirthdayService birthdayService;

	@RenderMapping
	public String defaultView(ModelMap map) {
		InitialLdapContext ctx = null;
		try {
			 ctx = connService.getInstance();
			if (ctx != null) {
				List<BirthdayBean> employees = birthdayService.getUsers(ctx);
				Collections.sort(employees.get(0).getBirthdays(), EmployeeBean.employeeBean);
				map.addAttribute("birthday",employees.get(0).getBirthdays());
				Collections.sort(employees.get(0).getMarriageAnniversaries(), EmployeeBean.employeeBean);
				map.addAttribute("marriageAnniversary",employees.get(0).getMarriageAnniversaries());
				Collections.sort(employees.get(0).getServiceAnniversaries(), EmployeeBean.employeeBean);
				map.addAttribute("serviceAnniversary",employees.get(0).getServiceAnniversaries());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			if (ctx != null) {
				try {
					ctx.close();
					System.out.println("TDS Connection closed");
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
					ex.getMessage();
				}
			}
		}
		return "hello";
	}


}
