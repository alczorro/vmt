<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="WEB-INF/tld/vmt.tld" prefix="vmt"%>
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  
 <%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
 <%
    request.setAttribute("context", request.getContextPath());
    %>
<!DOCTYPE html>
<html>
	<head>
		<title>组织通讯录</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1" charset="UTF-8" />
		<f:css href="${context }/resource/thirdparty/bootstrap/css/bootstrap.min.css" />
		<f:css href="${context }/resource/thirdparty/bootstrap/css/bootstrap-responsive.min.css" />
		<f:css href="${context }/resource/thirdparty/jquerymobile/jquery.mobile-1.3.2.min.css" />
		<f:css href="${context }/resource/css/jquery.mobile.vera.css"/>
		
		<f:script type="text/javascript" src="${context }/resource/js/jquery-1.7.2.min.js"/>
		<f:script type="text/javascript" src="${context }/resource/thirdparty/bootstrap/js/bootstrap.min.js"/>
		<f:script type="text/javascript" src="${context }/resource/thirdparty/jquerymobile/jquery.mobile-1.3.2.min.js"/>
	</head>
	<body data-dom-cache="true" class="vmt">
		<%-- <jsp:include page="mobileBanner.jsp"/> --%>
			<div data-role="header" class="ui-bar-b">
			    <a data-direction="reverse" data-transition="slide" href="${context }/user/mobile/detail?dn=<vmt:encode value="${prev }"/>" data-icon="arrow-l" class="ui-bar-b">${parentDisplay }</a>
			    <h1>${currentDisplay }</h1>
			    <a data-transition="fade" href="javascript:window.location.reload()" data-icon="refresh" class="ui-bar-b">刷新</a>
			</div>
			<div class="container mobile userDetail">
			<form class="userContainer">
				<div data-role="fieldcontain">
			        <label class="title">姓名：</label>
			     	<label>${user.name }</label>
			    </div>
			    <div data-role="fieldcontain">
			        <label class="title">邮箱地址：</label>
			     	<label><a href="Mailto:${user.cstnetId }">${user.cstnetId }</a></label>
			    </div>
			    <div data-role="fieldcontain">
			        <label class="title">性别：</label>
			        <label>
			        <c:choose>
			        	<c:when test="${user.sex=='female' }">
			        		女
			        	</c:when>
			        	<c:when test="${user.sex=='male' }">
			        		男
			        	</c:when>
			        	<c:otherwise>
			        		未知
			        	</c:otherwise>
			        </c:choose>
			        </label>
			    </div>
			    <div data-role="fieldcontain">
			        <label class="title">所属${isGroup?'群组':'部门'}：</label>
			        <label>${fn:replace(user.currentDisplay,',','&nbsp;/&nbsp;') }</label>
			    </div>
			    
			    <div data-role="fieldcontain">
			        <label class="title">办公室：</label>
			        <label>${empty user.office?'无':user.office }</label>
			    </div>  
			    
			     <div data-role="fieldcontain">
			        <label class="title">办公室电话：</label>
			        <label>
			        <c:choose>
			        	<c:when test="${empty user.officePhone}">
			        	无
			        	</c:when>
			        	<c:otherwise>
			        		<a href="tel:${ user.officePhone }">${ user.officePhone}</a>
			        	</c:otherwise>
			        </c:choose>
			        </label>
			    </div>
			    
			     <div data-role="fieldcontain">
			        <label class="title">手机号码：</label>
			        <label>
			        <c:choose>
			        	<c:when test="${empty user.telephone}">
			        	无
			        	</c:when>
			        	<c:otherwise>
			        		<a href="tel:${ user.telephone }">${ user.telephone}</a>
			        	</c:otherwise>
			        </c:choose>
			        </label>
			    </div>
			    <div data-role="fieldcontain">
			        <label class="title">职称/职务：</label>
			        <label>
			       		${empty user.title?'无':user.title }
			        </label>
			    </div>
			    
			    
			</form>
			
		</div>
	</body>
</html>