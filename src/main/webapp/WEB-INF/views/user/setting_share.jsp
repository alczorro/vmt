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
			      	<li id="team"><a  href="setting?view=prev" >团队权限</a></li>
			      	<li class="active" id="share"><a href="setting?view=share" >成员信息共享设置</a></li>
			      	<c:if test="${!empty iamAdminResult }">
			      		<li id="batchUpdate"><a href="setting?view=batch">批量管理</a></li>
			      	</c:if>
			      	<li id="application"><a href="setting?view=appmanage">应用管理</a></li>
			    </ul>
		  	</div>
			</div>
			<div  class="container main setting">
			<p class="alert alert-success" id="share_result" style="display:none">保存成功!</p>
			<table class="table table-bordered setttingTable" id="share-c" >
				<thead>
					<tr>
						<th class="mini">团队名称</th>
						<th>共享范围</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${nodes }" var="node">
						<tr>
							<td class="mini">
							<span>
							<c:choose>
								<c:when test="${node.isGroup() }"><i class="icon-bullhorn"></i></c:when>
								<c:otherwise><i class="icon-home"></i></c:otherwise>
							</c:choose>
							
							</span>
							<span class="teamName"><c:out value='${node.name }'/></span></td>
							<td dn="${node.dn }">
								<input name="memberVisible" <c:if test="${node.memberVisible}">checked="checked"</c:if> type="checkbox" />成员信息团队内可见
								<span style="display:${node.memberVisible?'inline':'none' }" >
								<input  type="checkbox" <c:if test="${node.data.hideMobile}">checked="checked"</c:if> name="hideMobile" />隐藏手机号码
								</span>
								<c:if test="${node.memberVisible }">
								</c:if>
								<br/>
								<input name="unconfirmVisible" <c:if test="${node.unconfirmVisible}">checked="checked"</c:if> type="checkbox" />待确认成员信息被团队其他成员可见
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
			$("input[name=private]").on('change',function(){
				var value='';
				if($(this).attr("checked")){
					value='privateAllowAdd';
				}else{
					value='privateNotAllow';
				}
				ajaxSetPrivilege($(this).parent().parent().attr("dn"),value);
				
			});
			function ajaxSetPrivilege(dn,value){
				$.post("setting/privilege/vmt-privilege/"+value,{dn:encodeURIComponent(dn)}).done(function(data){
					showAndHide("pv_result");
				});
			};
			//
			$("input[name=memberVisible]").on('change',function(){
				$(this).next().toggle();
				$.post("setting/privilege/vmt-member-visible/"+($(this).attr("checked")=='checked'),{dn:encodeURIComponent($(this).parent().attr("dn"))}).done(function(data){
					showAndHide("share_result");
				});
			});
			$("input[name=unconfirmVisible]").on('change',function(){
				$.post("setting/privilege/vmt-unconfirm-visible/"+($(this).attr("checked")=='checked'),{dn:encodeURIComponent($(this).parent().attr("dn"))}).done(function(data){
					showAndHide("share_result");
				});
			});
			$('input[name=hideMobile]').on('change',function(){
				$.post("setting/privilege/vmt-hide-mobile/"+($(this).attr("checked")=='checked'),{dn:encodeURIComponent($(this).closest('td').attr("dn"))}).done(function(data){
					showAndHide("share_result");
				});
			});
			function showAndHide(id){
				$('#'+id).show();
				$('#'+id).fadeOut(3000);
			}
		});
	</script>
</html>