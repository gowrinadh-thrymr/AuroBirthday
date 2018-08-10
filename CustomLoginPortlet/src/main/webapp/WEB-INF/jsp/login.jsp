<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ page import="javax.portlet.*"%>
<%@ page session="false"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<portlet:defineObjects />   
<link rel="stylesheet" href="/wps/contenthandler/dav/fs-type1/themes/Aurobindo_LoginTheme/css/login.css" type="text/css">
<portlet:actionURL var="login_details">
	<portlet:param name="action" value="logindetails" />
</portlet:actionURL>
<%-- <div class="col-md-3">
	<div class="login_form">
        
        <form action="${login_details}" method="post">
    	<h4>Login</h4>
        <label>Username</label>
        <input type="text" class="form-control" placeholder="Username" name="USER_NAME">
        <label>Password</label>
        <input type="password" class="form-control" placeholder="Password" name="PASSWORD"> 
        <input class="btn login pull-right" type="submit" value="Login" />      
        </form>
    </div>
</div> --%>


<section class="main-body">
  <div class="form-wrap"> 
  	<div class="logo">
    	<img src="/wps/contenthandler/dav/fs-type1/themes/Aurobindo_LoginTheme/images/logo.png">
    </div>
    <div class="form-body">
      <form action="${login_details}" method="post">
      	<div class="group-input">
            <span class="icons">
                <i class="fa fa-user"></i>
            </span>
            <div class="group">
              <input type="text" required name="USER_NAME">
              <span class="highlight"></span> <span class="bar"></span>
              <label>Username</label>
            </div>
        </div>
        <div class="group-input">
            <span class="icons">
                <i class="fa fa-lock"></i>
            </span>
            <div class="group">
              <input type="password" required name="PASSWORD">
              <span class="highlight"></span> <span class="bar"></span>
              <label>Password</label>
            </div>
        </div>
        <div class="button-wrap">
        	<button class="btn btn-default login-btn form-control" type="submit">LOGIN</button>
        </div>
        <span class="forget-pass">
        	<a href="#" class="login-btn"> Forget Password?</a>
        </span>
      </form>
    </div>
  </div>
</section>

<script>
/* $( document ).ready(function() {
	$(".main-body").css({"height":$(window).height()});
});
 */
$(document).ready(myfunction);
$(window).on('resize',myfunction);

function myfunction() {
	$(".main-body").css({"height":$(window).height()});
}
</script>