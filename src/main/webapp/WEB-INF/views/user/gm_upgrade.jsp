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
			      	<li id="ipfilter"><a href="gm?func=ipList">IP设置</a></li>
			      	<li class="active" id="upgrade"><a href="gm?func=upgrade">升级</a></li>
			      	<li id="domainMappings"><a href="gm?func=domainMapping">域名映射</a></li>
			      	<li   id="smsManage"><a href="gm?func=smsManage">短信管理</a></li>
			    </ul>
		  	</div>
			</div>
			<div  class="container main setting">
			
			
			
			<table class="table  table-bordered setttingTable" id="upgrade-c" >
				<thead>
					<tr>
					</tr>
					<tr>
						<th class="mini">团队名称</th>
						<th class="mini">起始时间</th>
						<th class="mini">截止时间</th>
						<th class="mini">操作</th>
					</tr>
				</thead>
				<tbody id="ipList">
				<c:forEach items="${nodes}" var="node">
					<tr>
						<td>
						<c:out value="${node.name }"/>
						</td>
						<td>
						<input class="fromDate datepickable" value="<fmt:formatDate value="${node.fromDate }" pattern="yyyy-MM-dd"/>" />
						</td>
						<td>
						<input class="toDate datepickable" value="<fmt:formatDate value="${node.toDate }" pattern="yyyy-MM-dd"/>" />
						</td>
						<td>
						<a class="saveUpgrade label label-success" data-symbol="${node.dn }">保存</a>
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
			$('.saveUpgrade').on('click',function(){
				var $tr=$(this).parent().parent();
				$.post('gm/upgrade/save',{
					dn:$(this).data('symbol'),
					fromDate:$tr.find('input.fromDate').val(),
					toDate:$tr.find('input.toDate').val()
				}).done(function(data){
					alert('保存成功');
				});
			});
		});
	</script>
</html>