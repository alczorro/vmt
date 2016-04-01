<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="WEB-INF/tld/vmt.tld" prefix="vmt"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<!DOCTYPE html>
<html>
	<head>
		<title>组织通讯录-短信群发</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<jsp:include page="../header.jsp"/>
	</head>
	<body class="vmt">
		<jsp:include page="../banner.jsp"/>
			<div   class="container main setting">
			<table class="table  table-bordered setttingTable" id="ipfilter-c">
				<tbody id="ipList">
				<tr  style="border:none;">
					 <td   style="border:none;"><h3>短信群发</h3>
					 </td>
					<tr  style="height:500px">
						<td style="width:20%;border: none;border-top:1px solid #ddd;">
							<ul class="left-ul">
								<li><a class="left-item" href="${context }/user/enhanced/sms/${currGroup.id }">发送短信</a></li>
								<li><a class="left-item" href="${context }/user/enhanced/sms/${currGroup.id }?func=log">发送记录</a></li>
								<li class="active"><a class="left-item" href="${context }/user/enhanced/sms/${currGroup.id }?func=manage">统计管理</a></li>
							</ul>
						</td>
						<td>
						<h2 class="linewire">可发送短信的人员</h2>
						
						<c:if test="${me.admin }">
						<form id="addMember">
							<input type="text" placeholder="请输入完整邮箱" name="cstnetId" id="cstnetId"/>
							<span id="memberCstnetId_error"></span>
						</form>
						</c:if>
						<ul id="memberList">
						<c:forEach items="${members }" var="m">
							<li data-is-admin="${m.admin }" data-user-name="<c:out value="${m.userName }"/>" data-cstnet-id="${m.cstnetId }" data-m-id="${m.id }" class="member textmessagesender">
								<c:out value="${m.userName }"/>(${m.cstnetId })<p>${m.admin?'管理员':'成员' }</p>
								<c:if test="${me.admin }">
									<a class="editMember" >编辑</a> 
								</c:if>
							</li>
						</c:forEach>
						</ul>
						<h2 class="linewire">统计</h2>
						
						<span style="margin-left:10px;">总共可发${currGroup.smsCount }条,已发送${currGroup.smsUsed }条，明细如下：</span>
						<table id="recipientslist" style="margin: 10px;width: 600px;">
							<thead>
								<th>序号</th>
								<th>发送人姓名</th>
								<th>发送人邮箱</th>
								<th>已发送</th>
							</thead>
							<tbody>
								<c:forEach items="${smsLog }" var="log" varStatus="index">
								<tr>
								<td>${index.index+1 }</td>
								<td><c:out value="${log.senderName}"/></td>
								<td>${log.senderCstnetId}</td> 
								<td>${log.sendCount}条</td>
								</tr>
								</c:forEach>
							</tbody>
						</table>
						
						</td>
					</tr>
				
				</tbody>
			</table>
		</div>
		<jsp:include page="../bottom.jsp"></jsp:include>
		<div id="editMember" class="modal hide fade" tabindex="-1" role="dialog" data-backdrop='true' aria-hidden="true">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
			<h4 id="title">编辑用户</h4>
		</div>
		<div class="modal-body">
			<p id="content">
				<p style="padding-left:14px;">
				用户姓名：<span id="userName" style="padding-left:14px;"></span>
				<p style="padding-left:14px;">
				用户邮箱：<span id="userCstnetId" style="padding-left:14px;"></span>
				<p>
				是否管理员：<label style="display:-webkit-inline-box;"><input type='radio' value="true" name="isAdmin">是</label><label style="display:-webkit-inline-box;"><input type='radio' name="isAdmin" value="false">否</label>
			</p>
		</div>
		<div class="modal-footer">
			<span class="root"><a id="deleteUser">移除此用户</a></span>
			<a class="btn btn-primary"  id="save">保存</a>
			<a class="btn" data-dismiss="modal" aria-hidden="true" id="cancel">取消</a>
		</div>
	</div>
	</body>
	<script type="text/javascript">
		$(document).ready(function(){
			$('#needRmbB').addClass('active');
			$('#deleteUser').on('click',function(){
				if(confirm("是否移除此用户?删除不可恢复")){
					var data=$('#editMember').data();
					$.get('${currGroup.id}/removeMember?mId='+data.mId).done(function(jr){
						$('#editMember').modal('hide');	
						window.location.reload();
					});
				}
			});   
			$('.editMember').live('click',function(){
				var data=$(this).closest('li').data();
				$('#userName').html(data.userName);
				$('#userCstnetId').html(data.cstnetId);
				$('input[name="isAdmin"][value="'+data.isAdmin+'"]').trigger('click');
				$('#editMember').data('mId',data.mId).modal('show'); 
			});
			$('#save').on('click',function(){
				var data=$('#editMember').data();
				$.get('${currGroup.id}/changeAdmin?mId='+data.mId+"&isAdmin="+$('input[name="isAdmin"]:checked').val()).done(function(jr){
					if(jr.flag){
						$('#editMember').modal('hide');	
						window.location.reload();
					}else{
						alert(jr.desc);
					}
				});
			});
			
			$('#addMember').validate({
				submitHandler:function(form){
					$.post('${currGroup.id}/addMember',{cstnetId:$('#cstnetId').val()}).done(function(jr){
						if(jr.flag){
							$('#memberList').append($('#addMemberTmpl').tmpl(jr.data,{
								roleDisplay:function(admin){
									if(admin){
										return "管理员";
									}else{
										return "成员";
									}
								}
							}));
							$('#cstnetId').val('');
						}else{
							alert(jr.desc);
						}
					});
				},
				rules:{
					cstnetId:{
						required:true,
						email:true,
						remote:{
							 type: "GET",
							 url: '${currGroup.id}/checkMemberUseable'
						 }}
				},
				messages:{
					cstnetId:{required:"请输入邮箱地址",email:"邮箱格式不合法",remote:"无法找到该用户"}
				}
			});
		});
	</script>
	<script id="addMemberTmpl" type="text/x-jquery-tmpl">
		<li id="textmessagesender" data-m-id="{{= id}}" data-is-admin="{{= admin}}" data-user-name="{{= userName }}" data-cstnet-id="{{= cstnetId }}" class="member">
				{{= userName }} ({{= cstnetId}})<p>{{= $item.roleDisplay(admin) }}</p>
				<a class="editMember" >编辑</a>
		</li>

	</script>
</html>