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
		<f:script src="../resource/thirdparty/autocomplate/jquery.autocomplete.js"/>
		<f:css href="../resource/thirdparty/fileuploader/fileuploader.css" />
		<f:script src="../resource/thirdparty/bootstrap-editable/js/bootstrap-editable.js"/>
		<f:css href="../resource/thirdparty/bootstrap-editable/css/bootstrap-editable.css" />
	</head>
	<body class="vmt">
		<jsp:include page="banner.jsp"/>
		<div class="container main help">
			<h2 class="login-title upgrade">升级到付费方案</h2>
			<div class="upgrade">
				<div class="subHint">
				<c:choose>
				<c:when test="${isUpgrade }">
					<p>您现在已经使用的是组织通讯录付费方案。</p>   
					<p>有效时间：<fmt:formatDate value="${from }" pattern="yyyy年MM月dd日"/>-<fmt:formatDate value="${to }" pattern="yyyy年MM月dd日"/></p>
					
				</c:when>
					<c:otherwise>
					<p>当前是免费方案，仅支持用户姓名与邮件地址的共享。</p>
					<p>升级到付费方案，您将可以管理并共享更多用户通讯方式，以及今后要扩展的各项功能服务。</p>
					</c:otherwise>
				</c:choose>
				
				</div>
				<h4>付费方案增值功能</h4>
				<ul class="org">
					<li>支持更多的用户通讯方式管理与共享，包括：办公地点、办公电话、手机号码等</li>
				</ul>
				<!-- <ul class="scheme">
					<li>
						<p>年付</p>
						<p class="price">￥2000</p>
						<p><a class="btn btn-large">购买</a></p>
					</li>
					<li>
						<p>两年付</p>
						<p class="price">￥3600</p>
						<p><a class="btn btn-large">购买</a></p>
					</li>
					<li>
						<p>三年付</p>
						<p class="price">￥4800</p>
						<p><a class="btn btn-large">购买</a></p>
					</li>
				</ul> -->
				
				<p>请联系销售人员直接购买。</p>
				<p>电话：010-58812312</p>
				<p>邮件：vlab@cnic.cn</p>
				<p>QQ：1072762843</p>
			</div>
		</div>
		<jsp:include page="bottom.jsp"></jsp:include>
	</body>
</html>