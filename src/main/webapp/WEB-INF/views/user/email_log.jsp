<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="WEB-INF/tld/vmt.tld" prefix="vmt"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<!DOCTYPE html>
<html>
	<head>
		<title>组织通讯录-发送记录</title>
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
						<td style="width:20%;border: none;border-top:1px solid #ddd;">
							<ul class="left-ul">
								<li><a class="left-item" href="${context }/user/enhanced/email">写邮件</a></li>
								<li class="active"><a class="left-item" href="${context }/user/enhanced/email?func=log">发送记录</a></li>
								<li><a class="left-item" href="${context }/user/enhanced/email?func=getters">历史收件人组</a></li>
								<c:if test="${isAdmin }">
									<li><a class="left-item" href="${context }/user/enhanced/email?func=manage">管理</a></li>
								</c:if>
							</ul>
						</td>
						<td id="smssendedsended">
						 <a style="margin:15px;position: absolute;">按邮件主题</a>
						<ul id="smsDetail">
						<c:forEach items="${emails }" var="e">
							<li data-email-id="${e.id }" class="sendedSms">
							<span><c:out value="${e.sendTimeDisplay }"/> </span>
								<div class="SendedSmscontent"> 
									<c:out value="${e.title }"/>
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
				var emailId=$(this).data('emailId');
				var $li=$(this);
				$.get("email/getSendLogDetail",{emailId:emailId}).done(function(data){
					if(data.flag){
						$('#logDetailTd').html($('#logDetailTmpl').tmpl(data.data));
						$('#SmsSendedContent').html(data.data.email.content);
						$('li.sendedSms').removeClass('active');
						$li.addClass('active');
					}else{
						alert(jr.desc);
					}
				});
			});
			var $smsList=$('li.sendedSms');
			if($smsList.size()>0){
				$smsList[0].click();
			}
		});
	</script>
	<script id="logDetailTmpl" type="text/x-jquery-tmpl">
		<div>
			<span id="theSmsSender">{{= email.sendTimeDisplay}}</span>
			<span style="float:right;font-size:10px;">
				<a href="mailto:{{= email.senderCstnetId}}">{{= email.senderName}}</a>发送
			</span>
			<div>
			<span class="fontbold">主题：</span>{{= email.title}}
			<br>
			<span class="fontbold">正文内容：</span><div id="SmsSendedContent"></div>
			</div>
			{{if files&&files.length>0}}
			<div> 
				附件：
				<ul class="re-emailFiles">
					{{each files}}
						<li><a href="email/download/file/{{= $value.clbId}}">{{= $value.fileName}}</a></li>
<!--
						<a class="removeClass" style="margin-left:15px;" >下载</a>-->
					{{/each}}
				</ul>
			</div>
			{{/if}}
	      <span class="fontbold">收件人：</span>
<ul id="memberList">
              
			{{each getters}}
							<li id="textmessagesender" class="member textmessagesender inlinestyle">
								{{= getterName}}
									<br>
								<a style="margin-top:0px;float: inherit;" href="mailto:{{= getterCstnetId}}">
								{{= getterCstnetId}}
								</a>
								
							</li>
			{{/each}}
						</ul>


		</div>
	</script>
		
</html>