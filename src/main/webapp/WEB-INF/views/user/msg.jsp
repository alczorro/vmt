<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="WEB-INF/tld/vmt.tld" prefix="vmt"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<%
	String msg=request.getParameter("msg");
	if(msg!=null){
		request.setAttribute("msg", msg);
	}
 %>
<!DOCTYPE html>
<html>
	<head>
		<title>组织通讯录</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<jsp:include page="../header.jsp"/>
		<f:script src="../resource/thirdparty/autocomplate/jquery.autocomplete.js"/>
		<f:css href="../resource/thirdparty/fileuploader/fileuploader.css"/>
	</head>
	<body class="vmt">
		<jsp:include page="../banner.jsp"/>
		<div class="container main">
				<p class="msg">${msg }<br/><a href="${context }/user/index">前往团队</a></p>
		</div>
	</body>
	<script>
	
	</script>

</html>