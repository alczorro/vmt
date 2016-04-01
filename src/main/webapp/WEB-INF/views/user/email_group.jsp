<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="WEB-INF/tld/vmt.tld" prefix="vmt"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<!DOCTYPE html>
<html>
	<head>
		<title>组织通讯录-历史收件人组</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<jsp:include page="../header.jsp"/>
		<f:script src="${context }/resource/thirdparty/bootstrap-editable/js/bootstrap-editable.js"/>
		<f:css href="${context }/resource/thirdparty/bootstrap-editable/css/bootstrap-editable.css" />
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
						<td style="width:20%;border: none;border-top:1px solid #ddd;">
							<ul class="left-ul">
								<li><a class="left-item" href="${context }/user/enhanced/email">写邮件</a></li>
								<li ><a class="left-item" href="${context }/user/enhanced/email?func=log">发送记录</a></li>
								<li class="active"><a class="left-item" href="${context }/user/enhanced/email?func=getters">历史收件人组</a></li>
								<c:if test="${isAdmin }">
									<li><a class="left-item" href="${context }/user/enhanced/email?func=manage">管理</a></li>
								</c:if>
							</ul>
						</td>
						<td id="smssendedsended">
						<ul id="smsDetail" style="margin-top:0px">
						<c:forEach items="${groups }" var="g">
							<li data-group-id="${g.id }" class="sendedSms" data-group-name="<c:out value="${g.groupName }"/>">
								<div class="SendedSmscontent"> 
									<c:out value="${g.groupName }"/>
								</div>
							</li>
						</c:forEach>
						</ul>
						</td>
						<td id="logDetailTd">
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
			$('li.sendedSms').on('click',function(){
				var groupId=$(this).data('groupId');
				var groupName=$(this).data('groupName');
				var $li=$(this);
				$.get("email/getGroupMember",{groupId:groupId}).done(function(data){
					if(data.flag){
						var groupData={
								groupId:groupId,
								groupName:groupName,
								data:data.data
						};
						$('#logDetailTd').html($('#logDetailTmpl').tmpl(groupData));
						$('li.sendedSms').removeClass('active');
						$li.addClass('active');
						$('#currentGroupName').editable({
							type:'text',
							url:'email/renameGroup',
							params: function(params) {
							    var data = {
							    		groupId:params.pk,
							    		name:params.value
							    };
							    return data;
							},
							success:function(data){
								$('li.active.sendedSms').data("groupName",data.data).find('div.SendedSmscontent').html(data.data);
							},
			        		mode:"popup",
			        		validate: function(value) {
							    if($.trim(value) == '') {
							        return '不允许为空';
							    }
							  if(value.length>200){
								  return '字符串过长';
							  }
			        		}
			        	});
					}else{
						alert(jr.desc);
					}
				});
			});
			
			$('.removeClass').live('click',function(){
				var groupId=$(this).data('groupId');
				if(confirm("确认删除该历史收件人组吗？ 删除不可恢复")){
					$.get("email/removeGroup?groupId="+groupId).done(function(){
						$('li.active.sendedSms').remove();
						if($('li.sendedSms').size()==0){
							$('#logDetailTd').empty();
						}else{
							$('li.sendedSms:first').trigger('click');
						}
					});
				};
			});
			var $smsList=$('li.sendedSms');
			if($smsList.size()>0){
				$smsList[0].click();
			}

		});
	</script>
	<script id="logDetailTmpl" type="text/x-jquery-tmpl">
			<div class="linetitlestyle"><span class="editable" data-pk='{{= groupId}}' id="currentGroupName" style="cursor:pointer;">{{= groupName}}</span>
			<a class="removeClass" data-group-id="{{= groupId}}">删除</a></div>
			<ul id="memberList"> 
					{{each data}}
							<li id="textmessagesender" class="member textmessagesender inlinestyle">
								{{= getterName}}
									<br>
								<a style="margin-top:0px;float: inherit;" href="mailto:{{= getterCstnetId}}">
								{{= getterCstnetId}}
								</a>
								
							</li>
					{{/each}}
			</ul>
	</script>
		
</html>