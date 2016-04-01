<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="WEB-INF/tld/vmt.tld" prefix="vmt"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<!DOCTYPE html>
<html>
	<head>
		<title>组织通讯录-管理</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<jsp:include page="../header.jsp"/>
		<f:script type="text/javascript" src="../resource/thirdparty/bootstrap-editable/js/bootstrap-editable.js"/>
		<f:script src="../resource/thirdparty/fileuploader/fileuploader.js" type="text/javascript"/>
		<f:css  href="../resource/thirdparty/bootstrap-editable/css/bootstrap-editable.css" />
		<f:css href="../resource/thirdparty/fileuploader/fileuploader.css"/>
	</head>
	<body class="vmt">
		<jsp:include page="../banner.jsp"/>
			<div class="navbar settingNav container">
		  	<!--  <div class="navbar-inner">
		    	<ul class="nav">
			      	<li class="active" id="manager"><a href="#">团队设置</a></li>
			      	<li id="apply"><a href="#">LDAP访问</a></li>
			      	<li id="team"><a href="#">团队权限</a></li>
			      	<li id="share"><a href="#">成员信息共享设置</a></li>
			      	<li id="batchUpdate"><a href="#">批量更新</a></li>
			    </ul>
		  	</div>
		  	 -->
			</div>
			<div  class="container main setting">
			</div>
		<jsp:include page="../bottom.jsp"></jsp:include>
	</body>
	<script type="text/javascript">
		$(document).ready(function(){
			
		});
	</script>
</html>