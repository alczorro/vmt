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
		<f:css  href="${context }/resource/thirdparty/bootstrap/css/bootstrap.min.css" />
		<f:css  href="${context }/resource/thirdparty/bootstrap/css/bootstrap-responsive.min.css" />
		<f:css  href="${context }/resource/thirdparty/jquerymobile/jquery.mobile-1.3.2.min.css" />
		<f:css  href="${context }/resource/css/jquery.mobile.vera.css"/>
		
		<f:script  src="${context }/resource/js/jquery-1.7.2.min.js"/>
		<f:script  src="${context }/resource/thirdparty/bootstrap/js/bootstrap.min.js"/>
		<f:script  src="${context }/resource/thirdparty/jquerymobile/jquery.mobile-1.3.2.min.js"/>
	</head>
	<body data-dom-cache="true" class="vmt">
		<%-- <jsp:include page="mobileBanner.jsp"/> --%>
		<div data-role="header" class="ui-bar-b">
		    <a data-transition="slide" data-direction="reverse" href="${context }/user/mobile/detail?dn=<vmt:encode value="${prev }"/>" data-icon="arrow-l">${ parentDisplay}</a>
		    <h1>${currentDisplay }</h1>
		    <a data-transition="slide" href="javascript:window.location.reload()" data-icon="refresh">刷新</a>
		</div>
		<div class="container mobile detail">
			<ul data-role="listview" data-split-icon="gear" data-split-theme="d" data-inset="true" class="listView detail">
				<c:forEach items="${views }" var="view">
					
			    		<c:if test="${view.type=='folder' }">
				    		<li>
				    			<a data-transition="slide" href="${context }/user/mobile/detail?dn=<vmt:encode value="${view.dn }"/>" class="ui-link-inherit">
				    		 		<h2><i class="icon-hdd"></i> ${view.name }</h2>
				    		 	</a>  
				    		</li>
			    		</c:if>
			    		<c:if test="${view.type=='link' }">
		    				<li data-icon="false">
		    					<a data-transition="slide"  href="${context }/user/mobile/detailUser?dn=<vmt:encode value="${view.dn }"/>">
		    		 				<h2><i class="icon-user"></i> ${view.name }</h2>
		    		 			</a>  
		    		 		</li>   
			    		</c:if>
			    		
				</c:forEach>
			</ul>
		</div>
	</body>
	
</html>