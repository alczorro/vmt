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
			      	<li id="apply"><a href="setting?view=ldap" >LDAP访问</a></li>
			      	<li  class="active" id="team"><a  href="setting?view=prev" >团队权限</a></li>
			      	<li id="share"><a href="setting?view=share" >成员信息共享设置</a></li>
			      	<c:if test="${!empty iamAdminResult }">
			      		<li id="batchUpdate"><a href="setting?view=batch">批量管理</a></li>
			      	</c:if>
			      	<li id="application"><a href="setting?view=appmanage">应用管理</a></li>
			    </ul>
		  	</div>
			</div>
			<div  class="container main setting">
			<p class="alert alert-success" id="pv_result" style="display:none">保存成功!</p>
			<table class="table table-bordered setttingTable" id="team-c">
				<thead>
					<tr>
						<th class="mini">团队名称</th>
						<th>权限</th>
					</tr>
				</thead>
				<tbody>
				
				<c:forEach items="${nodes}" var="node">
					<tr>
						<td class="mini"><span class="teamName">
							<c:choose>
								<c:when test="${node.isGroup() }"><i class="icon-bullhorn"></i></c:when>
								<c:otherwise><i class="icon-home"></i></c:otherwise>
							</c:choose>
						<c:out value='${node.name }'/></span></td>
						<td dn="${node.dn }">
							<input name="pv_${node.oid }" <c:if test="${node.privilege=='allOpen'}">checked="checked"</c:if> class="privilege" value="allOpen" type="radio" />完全公开
							<input name="pv_${node.oid }" <c:if test="${node.privilege=='openRequired'}">checked="checked"</c:if> class="privilege" value="openRequired" type="radio" />公开需审核
							<input name="pv_${node.oid }" <c:if test="${node.privilege=='privateAllowAdd'||node.privilege=='privateNotAllow'}">checked="checked"</c:if> class="privilege" value="private" type="radio" />私密
							<!-- 
							<span <c:if test="${node.privilege!='privateAllowAdd'&&node.privilege!='privateNotAllow'}">style="display:none"</c:if>><input name="private" type="checkbox" <c:if test="${node.privilege=='privateAllowAdd'}">checked="checked"</c:if> />允许普通成员添加新成员</span>
						 -->
						 <span style="display:none"><input name="private" type="checkbox"  />允许普通成员添加新成员</span>
						</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
		</div>
			</c:otherwise>
		</c:choose>
		<jsp:include page="../bottom.jsp"></jsp:include>
	</body>
	<script type="text/javascript">
		var thisItem = null; 
		var thisName = null;
		$(document).ready(function(){
			$('#settingB').addClass('active');
			$(".privilege").on('change',function(){
				var value=$(this).val();
				var $checkBox=$(this).parent().children("span");
				if(value=='private'){
					ajaxSetPrivilege($(this).parent().attr("dn"),'privateNotAllow');
					$checkBox.find("input[type=checkbox]").removeAttr("checked");
					//$checkBox.show();
				}else{
					ajaxSetPrivilege($(this).parent().attr("dn"),value);
					$checkBox.hide();
				}
			});
			function ajaxSetPrivilege(dn,value){
				$.post("setting/privilege/vmt-privilege/"+value,{dn:encodeURIComponent(dn)}).done(function(data){
					showAndHide("pv_result");
				});
			};
			
			function showAndHide(id){
				$('#'+id).show();
				$('#'+id).hide(3000);
			}
		});
	</script>
</html>