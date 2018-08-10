<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>


<html>
<head>
<!-- <link rel="stylesheet" href="/wps/contenthandler/dav/fs-type1/themes/Aurobindo-dashboard-theme/css/jquery-ui.css" type="text/css">
<link rel="stylesheet" href="/wps/contenthandler/dav/fs-type1/themes/Aurobindo-dashboard-theme/css/jquery.dataTables.min.css" type="text/css">

<script type="text/javascript" src="/wps/contenthandler/dav/fs-type1/themes/Aurobindo-dashboard-theme/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="/wps/contenthandler/dav/fs-type1/themes/Aurobindo-dashboard-theme/js/dataTables.jqueryui.min.js"></script>


 -->
</head>

<script type="text/javascript">
var isloggedin=${isloggedin};
$(document).ready(function(){
	if(isloggedin==false){
		window.location.href = "/wps/portal/aurobindo-login";
	}
});
 
</script>
<body>

<c:if test="${isloggedin==true}">
 <div class="table-responsive table_custom">   
	<table  class="table table-bordered">
		<thead>
		<tr><th>APPLICATION NAME</th></tr>
		</thead>
		<tbody>
			<c:forEach var="workSpace" items="${workSpaceResult}">
				<tr>
					<td><a href="${workSpace.getUrl()}" target="_blank"><c:out value="${workSpace.getTitle()}" /></a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	</div>
	</c:if>
</body>


</html>