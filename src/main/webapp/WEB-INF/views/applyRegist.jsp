<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="WEB-INF/tld/vmt.tld" prefix="vmt"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<!DOCTYPE html>
<html>
<head>
<title>组织通讯录-电子邮箱账号申请</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="header.jsp" />
<style>
	label {display:inline-block; margin-left:10px;}
</style>
</head>
<body class="vmt">
	<jsp:include page="banner.jsp" />
	<div class="container main  help">
		<h2 class="login-title">电子邮箱账号申请</h2>
		<div class="searchGroup" style="padding-top: 1%">
		<div id="formDiv"> 
			<form id="applyUserForm" action="submit" class="form-horizontal">
				<div class="control-group">              
					<label class="control-label">邮箱地址：</label>              
					<div class="controls">
						<input type="text" name="email" maxlength="255" />@${domain }
						<input type="hidden" name="domain" value="${domain }">
						<p class="shint">该电子邮箱地址可作为中国科技网通行证账号使用。</p>
					</div>         
				</div>
				<div class="control-group">              
					<label class="control-label">姓名：</label>              
					<div class="controls">
						<input type="text" name="trueName" maxlength="255" />
					</div>         
				</div>
				<div class="control-group">              
					<label class="control-label">员工号：</label>              
					<div class="controls">
						<input type="text" name="custom1" maxlength="255" />
					</div>         
				</div>
				<div class="control-group">              
					<label class="control-label">ARP系统用户名：</label>              
					<div class="controls">
						<input type="text" name="custom2" maxlength="255" />
					</div>         
				</div>
				<div class="control-group">              
					<label class="control-label">联系电话：</label>              
					<div class="controls">
						<input type="text" maxlength="255" name="phone" />
					</div>         
				</div>
				<div class="control-group">              
					<label class="control-label">联系邮箱：</label>              
					<div class="controls">
						<input type="text" maxlength="255" name="contractEmail" />
						<p class="shint">邮箱创建成功后，通过此邮箱通知您。</p>
					</div>         
				</div>
				<div class="control-group">              
					<label class="control-label">其他：</label>              
					<div class="controls">
						<textarea name="other" maxlength="255" style="resize:none; width:20em; height:6em;"></textarea>
					</div>         
				</div>
				<div class="control-group">              
					<label class="control-label"></label>              
					<div class="controls">
						<input type="text" name="validateCode" maxlength="255" placeholder="验证码"/>
						<img id="validateCodeImg" data-src="${context }/validcode.jpg" style="width:100px;"/>
					</div>         
				</div>
				<div class="control-group">              
					<label class="control-label"></label>              
					<div class="controls">
						<input type="submit" class="btn btn-primary" value="保存" />
					</div>         
				</div>
			</form>
			</div>
			<div id="hintDiv" style="display:none;text-align:center">
				<h1 style="color:#0a0"><span id="emailHint"></span>恭喜你，申请成功！请耐心等待。</h1>
				<a class="btn btn-primary btn-large"href="${context }/">返回首页</a>
			</div>
		</div>
	</div>
	<jsp:include page="bottom.jsp"></jsp:include>
	<jsp:include page="confirm.jsp" />
</body>
<script type="text/javascript">
	$(document).ready(function() {
		$('#validateCodeImg').on('click', function() {
			$(this).attr('src', $(this).data('src') + "?_=" + Math.random());
		}).click();

		$('#applyUserForm').validate({
			submitHandler:function(form){
				$.post('submit',$(form).serialize()).done(function(result){
					if(result.flag){
						$('#formDiv').toggle();
						$('#hintDiv').toggle(); 
						$('#emailHint').html($('input[name=email]').val()+'@'+$('input[name=domain]').val());
					}
				});
			},
			rules : {
				email : {
					required : true,
					numOrLetter:true,
					remote : {
						type : "POST",
						url : '${context}/jq/isUserNameExist?domain=${domain}',
					}
				},
				trueName : {
					required : true
				},
				phone : {
					required : true
				},
				contractEmail : {
					required : true,
					email:true
				},
				validateCode : {
					required : true,
					remote:{
						 type: "POST",
						 url: '${context}/jq/validateCode',
					 }
				}
			},
			messages : {
				email : {
					required : '邮箱地址不能为空',
					remote:'用户名已存在'
				},
				trueName : {
					required : '姓名不能为空'
				},
				phone : {
					required : '联系电话不能为空'
				},
				contractEmail : {
					required : '联系邮箱不能为空',
					email:'不是标准的邮箱格式'
				},
				validateCode : {
					required : '验证码不能为空',
					remote:'验证码错误'
				}
			}
		});
	});
</script>

</html>