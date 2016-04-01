<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="WEB-INF/tld/vmt.tld" prefix="vmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>   
 <%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<%request.setAttribute("context", request.getContextPath()); %>
<div class="navbar navbar-inverse navbar-fixed-top">
	<div class="navbar-inner">      
		<div class="container">        
			<div class="nav-collapse top-nav">         
				<ul id="vmt-nav" class="nav">   
				   <li class="dropdown" style="float:left">
                      <a data-toggle="dropdown" class="dropdown-toggle nav-logo" style="padding-right:5px;"><span class="brand vmt-logo"></span> </a>
                   </li>
                   <li style="float:left">
                   	<a data-transition="slide" data-ajax="false" href="${context }/user/mobile/createTeam">创建</a>
                   </li>
				</ul>
				<ul class="nav pull-right">  
				    <li><a data-ajax="false" href="${context }/logout">退出</a></li>
				</ul> 
			</div>  
		</div>
	</div>
</div>
<script type="text/javascript">
        $(function() {
            $.mobile.page.prototype.options.domCache = true;
        });
</script>
