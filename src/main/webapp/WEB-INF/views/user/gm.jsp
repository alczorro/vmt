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
			        <li class="active" id="manager"><a href="gm">同步设置</a></li>
			      	<li id="ipfilter"><a href="gm?func=ipList">IP设置</a></li>
			      	<li  id="upgrade"><a href="gm?func=upgrade">升级</a></li>
			      	<li id="domainMappings"><a href="gm?func=domainMapping">域名映射</a></li>
			      	<li   id="smsManage"><a href="gm?func=smsManage">短信管理</a></li>
			    </ul>
		  	</div>
			</div>
			<div  class="container main setting">
			<table class="table table-bordered setttingTable" id="manager-c" style="">
				<thead>
					<tr>
						<td colspan="4">
							<c:if test="${loginUser.isSuperAdmin}">
								<a class="btn btn-small btn-primary" id="coreMailRootOnly">导入CoreMail组织结构</a>
								<a class="btn btn-small btn-primary" id="coreMail">导入CoreMail用户</a>
								<a class="btn btn-small btn-primary" id="ddlRootOnly">导入ddl团队</a>
								<a class="btn btn-small btn-primary" id="ddl">导入ddl用户</a>
								<a class="btn btn-small btn-success" id="sendMsgAllOrg">发送更新全部组织消息</a>
								<a class="btn btn-small btn-success" id="sendMsgAllGroup">发送更新全部群组消息</a>
								<a class="btn btn-small btn-warning" id="buildIndex">重建索引</a>
							</c:if>
						</td>
					</tr>
					<tr>
						<th class="mini">团队名称</th>
						<th>同步</th>
						<th>科信</th>
						<th>域名</th>
					</tr>
				</thead>
				<tbody >
						<c:forEach items="${nodes}" var="node">
							<tr>
								<td class="mini">
									<a class="icon-remove removeNode" href="#"></a>
									<c:choose>
										<c:when test="${node.isGroup() }"><i class="icon-bullhorn"></i></c:when>
										<c:otherwise><i class="icon-home"></i></c:otherwise>
									</c:choose>
									<span class="teamName" data-is-group="${node.isGroup() }" data-pk='${node.dn }'><c:out value='${node.name }'/></span>
								</td>
								<td>
								<c:choose>
									<c:when test="${node.from =='coreMail'}">
										<a class="sync-coreMail label label-success" data-symbol="${node.symbol }">同步CoreMail</a>
									</c:when>
									<c:when test="${node.from=='ddl' }">
										<a class="sync-ddl label label-info" data-symbol="${node.symbol}">同步DDL</a>
									</c:when>
									</c:choose>
									<a class="send-msg label label-info" data-dn="${node.dn}">发送同步消息</a>
									<a class="build-index label label-info" data-dn="${node.dn}">重建索引</a>
								</td>
								<td>
									<c:choose>
										<c:when test="${node.data.openDchat}">
											<span>已开通科信</span>  <a data-dn="${node.dn }" class="refuseDchat">关闭</a>
										</c:when>
										<c:otherwise>
											<span>未开通科信</span>  <a data-dn="${node.dn }" class="acceptDchat">开通</a>
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${node.data.applyOpenDchat=='applyDchat' }">
											(已申请)
										</c:when>
										<c:otherwise> 
											(未申请 )
										</c:otherwise>
									</c:choose>
								</td>
								<td>
								
								<c:if test="${!node.group }">
									<c:forEach items="${node.data.domains }" var="domain" >
								    	[ ${domain} ]
									</c:forEach>
									</c:if>
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
			function openDchat(isOpen,$self){
				var opDesc=isOpen?'开通':'关闭';
				$.post('${context}/user/common/openDchat',{'dn':$self.data('dn'),'isOpen':isOpen}).done(function(data){
					if(data){
						if(isOpen){
							$self.prev('span').html('已开通科信');
							$self.attr('class','refuseDchat');
							$self.html('关闭');
						}else{
							$self.prev('span').html('未开通科信');
							$self.attr('class','acceptDchat');
							$self.html('开通');
						}
					}else{
						alert(opDesc+'失败！');
					}
					
				});
			}
			//开通科信
			$('.acceptDchat').live('click',function(){
				openDchat(true,$(this));
			});
			//关闭科信
			$('.refuseDchat').live('click',function(){
				openDchat(false,$(this));
			});
			$('.send-msg').on('click',function(){
				var dn=$(this).data("dn");
				$.post('gm/sendMsg',{'dn':dn}).done(function(data){
					if(data){
						alert('发送成功');
					}else{
						alert('发送失败');
					}
				});
			});
			$('#sendMsgAllOrg').on('click',function(){
				$.get('gm/sendMsg/org/all').done(function(data){
					if(data){
						alert('发送成功');
					}else{
						alert('发送失败');
					}
				});
			});
			$('#sendMsgAllGroup').on('click',function(){
				$.get('gm/sendMsg/group/all').done(function(data){
					if(data){
						alert('发送成功');
					}else{
						alert('发送失败');
					}
				});
			});
			
			
			$('#coreMail').on('click',function(){
				syncMsg('sync/coreMail/all','此操作会将中科院邮件系统的对应组织机构直接覆盖本系统组织机构，您确定要同步吗？');
			});
			$('#coreMailRootOnly').on('click',function(){
				syncMsg('sync/coreMail/allRootOnly','此操作会将中科院邮件系统的对应组织机导入到本系统组织机构，不同步人和部门，您确定要同步吗？');
			});
			$('#ddl').on('click',function(){
				syncMsg('sync/ddl/all','此操作将会导入文档库对应团队新增的成员，您确定要同步吗？');
			});
			$('#ddlRootOnly').on('click',function(){
				syncMsg('sync/ddl/allRootOnly','此操作将会导入文档库对应团队新增团队，您确定要同步吗？');
			});
			$('.sync-coreMail').on('click',function(){
				syncMsg('sync/coreMail/'+$(this).data('symbol')+'/','此操作会将中科院邮件系统的对应组织机构直接覆盖本系统组织机构，您确定要同步吗？');
			});
			$('.sync-ddl').on('click',function(){
				syncMsg('sync/ddl/'+$(this).data('symbol')+'/','此操作将会导入文档库对应团队新增的成员，您确定要同步吗？');
			});
			$('#buildIndex').on('click',function(){
				syncMsg('gm/buildIndex','此操作会重新建立针对Ldap的Jdbc索引,过程中访问,可能导致看不到所在团队,您确定重建吗？');
			});
			$('.build-index').on('click',function(){
				syncMsg('gm/buildAIndex','此操作会重新建立针对此团队的Jdbc索引,过程中访问,可能导致看不到所在团队,您确定重建吗？',{dn:$(this).data('dn')});
			});
			function syncMsg(url,msg,param){
				var flag=confirm(msg);
				if(flag){
					$.post(url,param).done(function(data){
						alert('操作成功!');
					});
				}
			}
			$('.removeNode').on('click',function(){
				var dn=$(this).next().data("pk");
				var flag=confirm("确定要删除["+$(this).next().html()+"]吗？删除后不可恢复！");
				if(flag){
					$.get('common/unbind',{'dn':dn,'type':'other'}).done(function(data){
						window.location.reload();
					});
				}
			});
			
			
			//editable
			$("#manager-c .teamName").editable({
				type:'text',
				url:'common/rename',
				params: function(params) {
				    var data = {
				    		dn:params.pk,
				    		name:params.value,
				    		type:'folder'
				    };
				    return data;
				},
        		mode:"popup",
        		validate: function(value) {
    				    if($.trim(value) == '') {
    				        return '不允许为空';
    				    }
    				   var isGroup=$(this).data("isGroup");
    				   if(isGroup){
    					   if(!groupNameOK($.trim(value))){
    						   return "请不要输入特殊字符";
    					   }
    				   }else{
    					   if(!nodeIdOK($.trim(value))){
    						   return "请不要输入特殊字符";
    					   }
            		}
        		}
        	});
		});
	</script>
</html>