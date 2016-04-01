<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="WEB-INF/tld/vmt.tld" prefix="vmt"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<!DOCTYPE html>
<html>
	<head>
		<title>组织通讯录-同步</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<jsp:include page="../header.jsp"/>
		<f:script src="../resource/thirdparty/bootstrap-editable/js/bootstrap-editable.js"/>
		<f:css href="../resource/thirdparty/bootstrap-editable/css/bootstrap-editable.css"/>
	</head>
	<body class="vmt">
		<jsp:include page="../banner.jsp"/>
			<div class="navbar settingNav container">
		  	<div class="navbar-inner">
		    	<ul class="nav">
			      	<li  id="manager"><a href="gm">同步设置</a></li>
			      	<li class="active" id="ipfilter"><a href="gm?func=ipList">IP设置</a></li>
			      	<li id="upgrade"><a href="gm?func=upgrade">升级</a></li>
			      	<li id="domainMappings"><a href="gm?func=domainMapping">域名映射</a></li>
			      	<li   id="smsManage"><a href="gm?func=smsManage">短信管理</a></li>
			    </ul>
		  	</div>
			</div>
			<div  class="container main setting">
			<table class="table  table-bordered setttingTable" id="ipfilter-c">
				<thead>
					<tr>
					<tr>
						<th><input class="btn btn-primary" type="button" id="addIp" value="增加ip"/></th>
					</tr>
					<tr>
						<th class="mini">IP列表</th>
					</tr>
				</thead>
				<tbody id="ipList">
				<c:forEach items="${ips}" var="ip">
					<tr>
						<td>
						${ip.ip }<i class="icon-remove removeIp" data-ip="${ip.id }"></i>
						</td>
					</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<jsp:include page="../bottom.jsp"></jsp:include>
	</body>
	<script type="text/javascript">
		var thisItem = null;
		var thisName = null;
		$(document).ready(function(){
			$('#addIp').on('click',function(){
				  var str=prompt("请输入允许访问API的IP","127.0.0.1");
				  if(str){
					 $.post('gm/addIp',{ip:str}).done(function(data){
						$("#ipList").append("<tr><td>"+str+"<i class='icon-remove removeIp' data-ip="+data+"/></td></tr>");
					});
				  }
			});
			$('.removeIp').live('click',function(){
				var self=this;
				$.post('gm/removeIp',{ipId:$(this).data('ip')}).done(function(){
					$(self).parent().parent().remove();
				});
			});
		});
		</script>
</html>