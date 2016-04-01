<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="WEB-INF/tld/vmt.tld" prefix="vmt"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<!DOCTYPE html>
<html>
	<head>
		<title>组织通讯录-成员管理</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<jsp:include page="../header.jsp"/>
	</head>
	<body class="vmt">
		<jsp:include page="../banner.jsp"/>
			<div   class="container main setting">
			<table class="table  table-bordered setttingTable" id="ipfilter-c">
				<tbody id="ipList">
				<tr  style="border:none;">
					 <td   style="border:none;"><h3>邮件群发</h3>
					 </td>
					<tr  style="height:500px">
						<td style="width:195px;border: none;border-top:1px solid #ddd;">
							<ul class="left-ul">
								<li><a class="left-item" href="${context }/user/enhanced/email">写邮件</a></li>
								<li><a class="left-item" href="${context }/user/enhanced/email?func=log">发送记录</a></li>
								<li><a class="left-item" href="${context }/user/enhanced/email?func=getters">历史收件人组</a></li>
								<li class="active"><a class="left-item" href="${context }/user/enhanced/email?func=manage">管理</a></li>
							</ul>
						</td>
						<td id="smssendedsended" style="width:843px;">
						 <div> 
						 		<div>
						    			<div class="linetitlestyle fontbold"><span >可发送邮件的人员</span></div> 
						    			<input type="text" name="emailTitle" data-dn="${mainOrg.dn }"  class="autoSearch" maxlength="200" id="emailTitle" style="margin-bottom:15px;width: 260px;" placeholder="请输入完整邮箱地址"/> 
						         </div>
						         <ul id="memberList" class="littleeftmarginul"> 
								         <c:forEach items="${admins }" var="a">
											<li class="member textmessagesender"  data-is-admin="true" data-user-name="<c:out value="${a.userName }"/>" data-cstnet-id="${a.cstnetId }" class="member">
												<span class="application-tittle">
												<c:out value="${a.userName }"/>(
												<a style="float:inherit" href="mailto:${a.cstnetId }">${a.cstnetId }</a>
												)</span>
							    				<span class="application-tittle">管理员</span>
											</li>
										</c:forEach>
										<c:forEach items="${members }" var="m">
											<li class="member textmessagesender"  data-umt-id="${m.umtId }" data-is-admin="true" data-user-name="<c:out value="${m.userName }"/>" data-cstnet-id="${m.cstnetId }" class="member">
												<span class="application-tittle">
												<c:out value="${m.userName }"/>(
												<a style="float:inherit" href="mailto:${m.cstnetId }">${m.cstnetId }</a>
												)</span>
							    				<span class="application-tittle">普通成员</span>
							    				<a class="removeMember" >删除</a> 
											</li>
										</c:forEach>
						  		  </ul>
						  </div>
						</td>
						
					</tr>
				
				</tbody>
			</table>
		</div>
		<jsp:include page="../bottom.jsp"></jsp:include>
	</body>
	<script type="text/javascript">
		$(document).ready(function(){
			$('#needRmbB').addClass('active');
			$('.removeMember').live('click',function(){
				var data=$(this).closest('li').data();
				var $li=$(this).closest('li');
				if(confirm("您确定删除此成员吗？ 删除不可恢复")){
					$.get('email/removeMember?umtId='+data.umtId).done(function(data){
						if(data.flag){
							$li.remove();
						}else{
							alert(data.desc);
						}
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
			$('input.autoSearch').each(function(i,n){ 
				$(this).autocomplete('${context}/user/search/dn/tree?dn='+encodeURIComponent($(this).data("dn"))+"&from=addAdmin",
			            {
					  		width:300,
							parse:function(data){
									return $.map(data, function(item) {
										return {
											data : item,
											result : item.cstnetId,
											value:item.truename
										};
									});
							},
							formatItem:function(row, i, max) {
			    				return  row.truename + " [" + row.cstnetId+"]";
			 				},
							formatMatch:function(row, i, max) {
			    				return row.truename + " " + row.cstnetId;
			 				},
							formatResult: function(row) {
			    				return row.truename;
			 				}
						});
			});
			$("input.autoSearch").result(function(event, data, formatted){
				var cstnetId=data.cstnetId;
				var flag=true;
				$('li.member').each(function(i,n){
					if(cstnetId==$(n).data('cstnetId')){
							flag=false;
							return false;
					}
				});
				var $input=$(this);
				if(!flag){
					$input.val('');
					alert('该成员已存在');
					return;
				}
				
				$.post("${context}/user/enhanced/email/addMember",data).done(function(jdo){
					if(!jdo.flag){
						alert(jdo.desc);
					}else{
						$input.val('');
						console.log($('#addMemberTmpl').tmpl(data));
						$('#addMemberTmpl').tmpl(data).appendTo("#memberList");
					}
				});
			});
		});
	</script>
	<script id="addMemberTmpl" type="text/x-jquery-tmpl">
	<li class="member textmessagesender" data-umt-id="{{= umtId }}"  data-is-admin="false" data-user-name="{{= truename}}" data-cstnet-id="{{= cstnetId }}" class="member">
												<span class="application-tittle">
												{{= truename}}(
												<a style="float:inherit" href="mailto:{{= cstnetId }}">{{= cstnetId }}</a>
												)</span>
							    				<span class="application-tittle">普通成员</span>
							    				<a class="removeMember" >删除</a> 
											</li>

	</script>

		
</html>
