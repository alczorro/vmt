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
		<f:script  src="../resource/thirdparty/bootstrap-editable/js/bootstrap-editable.js"/>
		<f:script src="../resource/thirdparty/fileuploader/fileuploader.js" />
		<f:css  href="../resource/thirdparty/bootstrap-editable/css/bootstrap-editable.css" />
		<f:css href="../resource/thirdparty/fileuploader/fileuploader.css" />
	</head>
	<body class="vmt">
		<jsp:include page="../banner.jsp"/>
		<c:choose>
			<c:when test="${empty nodes }">
				<div class="container main">
					<p class="noTeam">您没有可管理的团队</p>
				</div>
			</c:when>
			<c:otherwise>
			<div class="navbar settingNav container">
		  	<div class="navbar-inner">
		    	<ul class="nav">
			      	<li id="manager"><a href="setting" >团队设置</a></li>
			      	<li class="active"  id="apply"><a href="setting?view=ldap" >LDAP访问</a></li>
			      	<li id="team"><a href="setting?view=prev"  >团队权限</a></li>
			      	<li id="share" ><a href="setting?view=share"  >成员信息共享设置</a></li>
			      	<c:if test="${!empty iamAdminResult }">
			      		<li id="batchUpdate"><a href="setting?view=batch">批量管理</a></li>
			      	</c:if>
			      	<li id="application"><a href="setting?view=appmanage">应用管理</a></li>
			    </ul>
		  	</div>
			</div>
			<div  class="container main setting">
			
			<table class="table table-bordered setttingTable" id="apply-c">
				<thead>
					<tr>
						<th class="mini">团队名称</th>
						<th>DN</th>
						<th>密码</th>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${nodes}" var="node">
					<tr>
						<td class="mini">
						<span>
						<c:choose>
							<c:when test="${node.isGroup() }"><i class="icon-bullhorn"></i></c:when>
							<c:otherwise><i class="icon-home"></i></c:otherwise>
						</c:choose>
						
						</span>
						<span class="teamName"><c:out value='${node.name }'/></span>
						</td>
						<td>${node.dn },<vmt:config key="ldap.base.dn"/></td>
						<td>${node.password }</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
		</div>
			</c:otherwise>
		</c:choose>
		<jsp:include page="../bottom.jsp"></jsp:include>
	</body>
	<script>
	$(document).ready(function(){
		$('#settingB').addClass('active');
	});
	</script>
</html>