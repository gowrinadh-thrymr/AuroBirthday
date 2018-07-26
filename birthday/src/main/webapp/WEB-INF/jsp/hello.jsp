<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
</head>
<body>


 <div class="row">
            <div class="col-md-4 birthdays-blocks">
            	<div class="horizontalTab">
                        <ul class="resp-tabs-list">
                            <li>Birthday</li>
                            <li>Marriage</li>
                            <li>Services</li>
                        </ul>
                        <div class="resp-tabs-container">
                            <div class="tab-1">
                                <div class="inner-blocks">
                                    <ul class="users-list">
								<c:forEach var="employee" items="${birthday}">
								        <li>
                                          <img src="images/user1.jpg" alt="User Image">
                                          <a class="users-list-name" href="#"><c:out value="${employee.name}"/></a>
                                          <span class="users-list-date"><c:out value="${employee.birthDate}"/></span>
                                        </li>
								</c:forEach>
                             
                                      </ul>
                                </div>
                                <span><a href="#">View All Users</a></span>
                            </div>
                            <div class="tab-2">
                                <div class="inner-blocks">
                                   <ul class="users-list">
                                       <c:forEach var="employee" items="${marriageAnniversary}">
                                        <li>
                                          <img src="images/user3.jpg" alt="User Image">
                                          <a class="users-list-name" href="#"><c:out value="${employee.name}"/></a>
                                          <span class="users-list-date"><c:out value="${employee.birthDate}"/></span>
                                        </li>
                                       </c:forEach>
                                      </ul>
                                </div>
                                <span><a href="#">View All Users</a></span>
                            </div>
                            <div class="tab-3">
                                <div class="inner-blocks">
                                   <ul class="users-list">
                                   <c:forEach var="employee" items="${serviceAnniversary}">
                                        <li>
                                          <img src="images/user1.jpg" alt="User Image">
                                          <a class="users-list-name" href="#"><c:out value="${employee.name}"/></a>
                                          <span class="users-list-date"><c:out value="${employee.birthDate}"/></span>
                                        </li>
                                    </c:forEach>
                                      </ul>
                                </div>
                                <span><a href="#">View All Users</a></span>
                            </div>
                        </div>
                    </div>
            </div>
            </div>

</body>
</html>