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
								<li class="active"><a class="left-item" href="${context }/user/enhanced/sms/${currGroup.id }?func=log">发送记录</a></li>
								<li><a class="left-item" href="${context }/user/enhanced/sms/${currGroup.id }?func=manage">统计管理</a></li>
							</ul>
						</td>
						<td id="smssendedsended">
						 <a style="margin:15px;position: absolute;">按短信内容</a>
						<ul id="smsDetail">
							
							 
						<c:forEach items="${smss }" var="s">
							<li data-sms-id="${s.id }" class="sendedSms">
							                              
							<span><c:out value="${s.sendTimeDisplay }"/> </span>
								<div class="SendedSmscontent"> 
									<c:out value="${s.content }"/>
									<c:if test="${!s.isSuccess() }">
										<span class="error">
											<c:out value="${s.getStatusDisplay()}"/>
										</span>
									</c:if>
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
				$.get("${currGroup.id}/getSendLogDetail",{smsId:$(this).data('smsId')}).done(function(data){
					$('#logDetailTd').html($('#logDetailTmpl').tmpl(data.data,{
						getStatClass:function(stat){
							if(stat=='r:xxx'){
								return 'success';
							}else if(stat=='r:000'){
								return 'waited';
							}else{
								return 'error';
							}
						}
						
					}));
				});
			});
			$('.resendSms').live('click',function(){
				$.get('${currGroup.id}/resendSms/'+$(this).data('smsId')).done(function(jr){
					if(jr.flag){
						$(".sendedSms.active").trigger('click');
					}else{
						alert(jr.desc);
					}
				});
			});
			$('#resendSmsAll').live('click',function(){
				$.get('${currGroup.id}/resendSmsAll',{smsId:$(this).data('smsId')}).done(function(jr){
					if(jr.flag){
						$(".sendedSms.active").trigger('click');
					}else{
						alert(jr.desc);
					}
					
				});
			});
			$(".sendedSms").on('click',function(){
				$('.sendedSms').removeClass("active");
				$(this).addClass('active');
			});
			var $smsList=$('li.sendedSms');
			if($smsList.size()>0){
				$smsList[0].click();
			}
		});
	</script>
	<script id="logDetailTmpl" type="text/x-jquery-tmpl">
		<div>
			<span id="theSmsSender">
			{{= sendTimeDisplay}},
			由<a href="mailto:{{= senderCstnetId}}">{{= senderName}}</a>发送</span>
			<div id="SmsSendedContent">{{= content}}</div>
			接收人列表(共发送{{= getter.length}}条,成功{{= successSendCount }}条,失败{{= errorCount }}条,待发送{{= delayCount}}条)
			<a id="resendSmsAll" data-sms-id="{{= id }}">重发所有失败</a>
			<table id="recipientslist">
					<tr>
						<td>序号</td>
						<td>姓名</td>
						<td>手机号</td>
						<td>发送状态</td>
					</tr>
				{{each getter}}
					<tr>
						<td>{{= $index+1}}</td>
						<td>{{= $value.trueName}}</td>
						<td>{{= $value.mobile}}</td>
						<td class="{{= $item.getStatClass($value.stat) }}">{{= $value.statDesc}}
							{{if $value.stat=='r:004'}}
								(<a data-sms-id="{{= $value.id}}" class="resendSms">重发</a>)
							{{/if}}
						</td>
					</tr>
				{{/each}}
			</table>
		</div>
	</script>
		
</html>