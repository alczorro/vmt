<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="WEB-INF/tld/vmt.tld" prefix="vmt"%>
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
		<f:css href="${context }/resource/css/jquery.mobile.vera.css" />
		
		<f:script  src="${context }/resource/js/jquery-1.7.2.min.js"/>
		<f:script  src="${context }/resource/thirdparty/bootstrap/js/bootstrap.min.js"/>
		<f:script  src="${context }/resource/thirdparty/jquerymobile/jquery.mobile-1.3.2.min.js"/>
		
	</head>
	<body class="vmt"  data-dom-cache="true">
		<jsp:include page="mobileBanner.jsp"/>
		<div class="container mobile index">
			<ul data-role="listview" data-divider-theme="d" data-count-theme="c" data-inset="true"  >
				<c:if test="${!empty orgs }">
				<li data-role="list-divider">组织机构</li>
				<c:forEach items="${orgs }" var="org">
					 <li><a data-transition="slide" href="${context }/user/mobile/detail?dn=<vmt:encode value="${org.dn }"/>"><i class="icon-home"></i><c:out value="${org.name }"/> <span class="ui-li-count">${ org.count}</span></a></li>
				</c:forEach>
				</c:if>   
				<c:if test="${!empty groups }">
				<li data-role="list-divider">群组</li>
				<c:forEach items="${groups }" var="group">
					<li><a data-transition="slide" href="${context }/user/mobile/detail?dn=<vmt:encode value="${group.dn }"/>"><i class="icon-bullhorn"></i> <c:out value="${group.name }"/> <span class="ui-li-count">${group.count }</span></a></li>
				</c:forEach>
				</c:if>
			</ul>
		</div>
	</body>
</html>