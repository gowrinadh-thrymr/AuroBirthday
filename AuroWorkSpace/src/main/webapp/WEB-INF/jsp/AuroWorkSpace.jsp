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


<body>

	<table>
		<thead>
		<tr><th>APPLICATION NAME</th><th>URL</th></tr>
		</thead>
		<tbody>
			<c:forEach var="workSpace" items="${workSpaceResult}">
				<tr>
					<td><c:out value="${workSpace.getTitle()}" /></td><td><c:out value="${workSpace.getUrl()}" /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>

</html>