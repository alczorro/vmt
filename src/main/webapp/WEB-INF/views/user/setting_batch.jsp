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
			      	<li id="share"><a href="setting?view=share" >成员信息共享设置</a></li>
			      	<c:if test="${!empty iamAdminResult }">
			      		<li class="active" id="batchUpdate"><a href="setting?view=batch">批量管理</a></li>
			      	</c:if>
			      	<li id="application"><a href="setting?view=appmanage">应用管理</a></li>
			    </ul>
		  	</div>
			</div>
			<div  class="container main setting">
			
			<table class="table table-bordered setttingTable" id="batchUpdate-c" >
				<thead>
					<tr>
						<th class="mini">团队名称</th>
						<th>操作</th>
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
									<span title="${node.symbol }" class="teamName" data-pk='${node.dn }'><c:out value='${node.name }'/></span>
								</td>
								<td>
										<c:choose>
											<c:when test="${node.isUpgraded() }">
												<a class="label label-info batchUpdate" href="batch/show?func=update&dn=<vmt:encode value="${node.dn}"/>" >
												批量更新
												</a>
												<a class="label label-success batchInsert" href="batch/show?func=insert&dn=<vmt:encode value="${node.dn}"/>" >
												批量导入
												</a>
												<a class="label label-warn batchInsert" href="batch/export?dn=<vmt:encode value="${node.dn }"/>&func=update&name=<vmt:encode value="${node.name }"/>" >
												批量导出
												</a>
												<c:if test="${!node.isGroup()&&node.data.isCoreMail&&!empty node.data.domains }">
													<a class="label label-warning batchRemoveEmail" data-dn="<vmt:encode value="${node.dn}"/>">
													批量删除邮箱
													</a>
												</c:if>
											</c:when>
											<c:otherwise>
												<a class="btn btn-small btn-link" href="upgrade?dn=<vmt:encode value="${node.dn}"/>">升级可支持批量更新</a>
											</c:otherwise>
										</c:choose>
								</td>
							</tr>
						</c:forEach>
				</tbody>
			</table>
			
			<div id="batchRemoveEmailDia" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			  <div class="modal-header">
			    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
			    <h3 id="myModalLabel">批量删除邮箱账号</h3>
			  </div>
			  <div class="modal-body">
			  	<div id="beforeSubmit">
				    <p>组织机构名称:<span id="orgName"></span></p>
				    <p>域名:<span id="orgDomains"></span></p>
				    <p>
				    <textarea placeholder="请用回车分隔" style="width:98%" rows="10" id="needDeleted"></textarea>
				    <span id="errorDeleteMsg" class="error"></span>
				    </p>
			    </div>
			    <div id="afterSubmit" style="display:none">
			    	<p id="content">
						<div class="progress progress-striped active">
						  <div id="progress-bar" class="bar" style="width: 0%;"></div>
						</div>
						<ul id="taskList" style="height:200px;overflow:auto">
						</ul>
					</p>
			    </div>
			  </div>
			  <div class="modal-footer">
			    <button class="btn" data-dismiss="modal" aria-hidden="true">取消</button>
			    <button class="btn btn-primary"  id="submitRemoveDeleteEmail">确定</button>
			  </div>
			</div>
		</div>
			</c:otherwise>
		</c:choose>
		<jsp:include page="../bottom.jsp"></jsp:include>
		
		
	</body>
	<script type="text/javascript">
		$(document).ready(function(){
			$('#settingB').addClass('active');
			//显示批量删除对话框的时候
			$('#batchRemoveEmailDia').on('show',function(){
				$('#beforeSubmit').show();
				$('#afterSubmit').hide();
				$('#needDeleted').val('');
				$('#taskList').empty();
				$('#submitRemoveDeleteEmail').show().prev().html('取消');
				$('#progress-bar').css('width','0%');
			});
			//批量删除邮箱账号
			$('.batchRemoveEmail').on('click',function(){
				var dn=$(this).data('dn');
				$.post('${context}/user/org/getDetail',{dn:dn}).done(function(data){
					if(data.flag){
						$('#orgName').html(data.data.name);
						$('#orgDomains').html(data.data.domains.join(','));
						$('#batchRemoveEmailDia').data('dn',dn).modal('show');
					}else{
						alert('获取组织信息失败');
					}
				});
			});
			//提交批量删除
			$('#submitRemoveDeleteEmail').on('click',function(){
				$(this).hide().prev().html('关闭');
				var emailListTxt=$('#needDeleted').val();
				var emailList=[];
				$.each(emailListTxt.split('\n'),function(i,n){
					if($.trim(n)!=''){
						emailList.push(n);
					}
				});
				if(emailList.length==0){
					alert('请至少输入一个邮箱');
					return;
				}
				
				doTask(0,emailList,$('#batchRemoveEmailDia').data('dn'));
				$('#beforeSubmit').toggle();
				$('#afterSubmit').toggle();
			});
			function doTask(index,params,dn){
				if(index>=params.length){
					$('#progress-bar').css('width',"100%").html('['+(index)+'/'+index+']任务完成!');
					return;
				}
				$('#progress-bar').css('width',((index+1)*100/(params.length))+"%").html('['+(index+1)+'/'+params.length+']');
				$.post('${context}/user/batch/doTask?func=delete',{
					dn:dn,
					cstnetId:params[index]
				}).done(function(result){
					var content='<li class="taskItem" id="task_'+index+'">'+(index+1)+
					'.'+params[index]+",";
					if(result.flag){
						content+="<span class='success'>删除成功!</span>";
					}else{
						content+="<span class='error'>" + result.desc + "</span>";
					}
					var $taskList=$('#taskList');
					$taskList.append(content+'</li>');
					$taskList.scrollTop(100000);
					doTask(++index,params,dn);
				});
			}
		});
	</script>
</html>