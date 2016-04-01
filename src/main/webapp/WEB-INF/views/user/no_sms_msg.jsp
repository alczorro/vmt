<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="WEB-INF/tld/vmt.tld" prefix="vmt"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
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
				<p class="msg" style="font-size:1.5em">您还没有开通短信群发功能,请与我们联系。<br/>
				电话：010-58812312<br>
				邮件：<a href="mailto:vlab@cnic.cn">vlab@cnic.cn</a><br>
				QQ：1072762843<br> 
				<a href="${context }/user/index">前往首页</a></p>
		</div>
	</body>
	<script>
	
	</script>

</html>