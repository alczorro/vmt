<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="WEB-INF/tld/vmt.tld" prefix="vmt"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<!DOCTYPE html>
<html>
<head>
<title>组织通讯录</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="../header.jsp" />
</head>
<body class="vmt">
	<jsp:include page="../banner.jsp" />
	<script>
	$(document).ready(function(){
		var $msgList=$('#msgList');
		$.get('${context}/user/message/detail').done(function(data){
			if(data.flag&&data.data.length>0){
				$msgList.fadeIn(1000);
				$("#messageTmpl").tmpl(data.data,{
					getValue:getValue
				}).appendTo($msgList);
			}else{
				$('#noMsg').fadeIn(1000);
			}
		});
		function getValue(columns,key){
			for(var i in columns){
				if(columns[i].columnName==key){
					return columns[i].columnValue;
				}
			}
			return "";
		}
		//查看详情
		$('.detailRegistInfo').live('click',function(){
			var $list=$(this).closest('ul').data('tmplItem').data.list;
			var data={};
			var msgId=$(this).parent().data("msgId");
			for(var i in $list){
				if($list[i].id==msgId){ 
					vmt.confirm({
						title:'处理注册电子邮件申请',
						yes:'为其创建新邮箱',
						no:'删除',
						afterHide:false,
						content:'',
						callback:function(flag){
							if(flag){
								$.post('${context}/user/message/applyRegist',{msgId:msgId}).done(function(data){
									if(data.flag){
										$('#content').html("<h2>用户创建成功! 用户名为:"+data.data.email+",密码为:"+data.data.password+",请您牢记</h2>");
										$('#no').hide();
										$('#yes').hide();
										$('#hideThis').show().html("关闭").off('click').on('click',function(){
											$('#cosDialog').modal('hide');
											window.location.reload();
										});
									}else{
										alert(data.desc);
										$('#cosDialog').modal('hide');
									}
								});
							}else{
								$.post('${context}/user/message/refuseRegist',{msgId:msgId}).done(function(data){
									$('#cosDialog').modal('hide');
									window.location.reload();
								});
							}
						}
					});
					$('#detailUserTmpl').tmpl($list[i],{
						getValue:getValue
					}).appendTo('#content');
					break;
				}
			}
		});
	});
	</script>
	<div id="content_body" class="container main message">
				<p class="msg" id="noMsg" style="display:none">
				您暂时还没有消息
				<br>
				<a href="${context }/user/index" >前往团队</a> 
				</p>
				
				<div id="msgList" style="display:none">
				<h3 class="messageTitle">消息</h3>
				</div>
	</div>
	<jsp:include page="../confirm.jsp"></jsp:include>
	<jsp:include page="../bottom.jsp"></jsp:include>
</body>
<script type="text/javascript">
	$(document).ready(function() {
		$('#messageB').addClass("active");
		//高度
		var staffHeight = window.screen.availHeight * 0.6;
		$(".setting").css({
			"min-height" : staffHeight
		});
		//bind click
		$('.accept').live('click', function() {
			$.post("message/accept", {
				dn : $(this).closest('ul').data("dn"),
				msgId:$(this).parent().data("msgId")
			}).done(function(data) {
				if (data) {
					window.location.reload();
				}
			});
		});
		//绑定拒绝
		$('.refuse').live('click', function() {
			$.post("message/refuse", {
				dn : $(this).closest('ul').data("dn"),
				msgId:$(this).parent().data("msgId")
			}).done(function(data) {
				if (data) {
					window.location.reload();
				}
			});
		});
		//绑定接受申请
		$('.refuseApply').live('click', function() {
			$.post("message/refuseApply", {
				dn : $(this).parent().data("userDn"),
				msgId:$(this).parent().data("msgId")
			}).done(function(data) {
				if (data) {
					window.location.reload();
				}
			});
		});
		//绑定接受申请
		$('.acceptApply').live('click', function() {
			$.post("message/acceptApply", {
				dn : $(this).parent().data("userDn"),
				msgId:$(this).parent().data("msgId")
			}).done(function(data) {
				if (data) {
					window.location.reload();
				}
			});
		});
		
	});
</script>
<script id="detailUserTmpl"  type="text/x-jquery-tmpl">
<table>
	<tr>
		<td>邮箱:</td>
		<td>{{= $item.getValue(columns,'email') }}</td>
	</tr>
	<tr>
		<td>姓名:</td>
		<td>{{= $item.getValue(columns,'trueName') }}</td>
	</tr>
	<tr>
		<td>员工号:</td>
		<td>{{= $item.getValue(columns,'custom1') }}</td>
	</tr>
	<tr>
		<td>ARP系统用户名:</td>
		<td>{{= $item.getValue(columns,'custom2') }}</td>
	</tr>
	<tr>
		<td>联系电话:</td>
		<td>{{= $item.getValue(columns,'phone') }}</td>
	</tr>
	<tr>
		<td>联系邮箱:</td>
		<td>{{= $item.getValue(columns,'contractEmail') }}</td>
	</tr>
	<tr>
		<td>其他:</td>
		<td>{{= $item.getValue(columns,'other') }}</td>
	</tr>
<table>
</script>
<script id="messageTmpl"  type="text/x-jquery-tmpl">
<ul class="messageList" data-dn="{{= teamDN}}">
<h2>{{= teamName}}</h2>
{{each list}}
{{if $value.msgType=='addUserTemp'}}
	<li data-msg-id="{{= $value.id}}">
		您有来自 <span class="fromTeam">{{= teamName}}</span>的邀请， 
		<a class="accept">接受</a> 还是 
		<a class="refuse">拒绝</a>
	</li>
{{/if}}
{{if $value.msgType=='userApplyGroupTemp'}}
	<li data-msg-id="{{= $value.id}}" data-user-dn="{{= $item.getValue($value.columns,'applyDN') }}">{{= $item.getValue($value.columns,'applyName')}}申请加入
		<a class="acceptApply">接受</a>
		<a class="refuseApply">拒绝</a>
	</li>  
{{/if}}
{{if $value.msgType=='userRegistApply'}}
	<li data-msg-id="{{= $value.id}}">
		{{= $item.getValue($value.columns,'trueName')}}[{{= $item.getValue($value.columns,'email')}}]申请注册电子邮件账号  
		<a class="detailRegistInfo">处理</a> 
	</li>
{{/if}}
{{/each}}
</ul>
</script>
</html>