<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="WEB-INF/tld/vmt.tld" prefix="vmt"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<!DOCTYPE html>
<html>
	<head>
		<title>组织通讯录</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<jsp:include page="header.jsp"/>
		<script type="text/javascript">
		$(document).ready(function(){
			$.ajax({
				  url: '<vmt:umtBase/>/js/isLogin.do',
				  dataType: "script",
				  async:false,
				  success: function(){
					  if(data.result){
						  window.location.href+='/login';
					  }else{
						  $('body').show();   
					  }
				  }
				});
		});
		</script>
	</head>
	<body style="display:none;" class="vmt login">
		<jsp:include page="banner.jsp"/>
		<div class="container main">
			<div class="row-fluid">
				<div class="span8 intro">
					<h2>轻松管理、简单协作</h2>
					<p>帮您轻松管理组织机构、群组关系，协作很简单！</p>
				</div>
	            <div class="span4 regist">
					<a class="btn btn-primary btn-large" href="${context }/login" id="loginVmt">使用中国科技网通行证登录</a>
					<p class="notHave"><span>或者</span></p>
					<a target="_blank" class="btn btn-warning btn-large" href="<vmt:umtBase/>/regist.jsp" id="registVmt">立即注册</a>
				</div>
			</div>
		</div>	
	</body>
</html>