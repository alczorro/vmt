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
			<a data-target=".nav-collapse" data-toggle="collapse" class="btn btn-navbar">          
				<span class="icon-bar"></span>          
				<span class="icon-bar"></span>          
				<span class="icon-bar"></span>        
			</a>      
			<div class="nav-collapse top-nav">         
				<ul id="vmt-nav" class="nav">   
				   <li class="dropdown">
                      <a data-toggle="dropdown" class="dropdown-toggle nav-logo active"><span class="brand vmt-logo"></span> <b class="caret"></b></a>
						<div class="clear"></div>
                   </li>
				   <li id="indexB"><a href="${empty context?'/':context }" >首页</a></li>
				   <c:if test="${!empty loginUser }">
					   <li><a tmplid="createTeam"  class="vmt-dialog">创建</a></li>
					   <li id="searchGroupB"><a href="${context }/user/search/group">加入群组</a></li>
					   <li id="profileB"><a href="${context }/user/profile">个性化</a></li>
					   <li id="settingB"><a href="${context }/user/setting">管理</a></li>
					    <c:if test="${loginUser.isSuperAdmin }">
					   		<li><a href="${context }/user/gm">系统管理</a></li>
					   </c:if>
					   <li id="needRmbB">
					   <div class="dropdown">
					   <a class="dropdown" class="dropdown-toggle sr-only" id="dropdownMenu1" data-toggle="dropdown">
						    增值服务
						</a>
						<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
						    <li role="presentation"><a role="menuitem" tabindex="-1" href="${context }/user/enhanced/sms">短信群发</a></li>
						    <li role="presentation"><a role="menuitem" tabindex="-1" href="${context }/user/enhanced/email">邮件群发</a></li>
						  </ul>
						  </div>
						</li>
						
				   </c:if>
				   
				   <li id="helpB"><a href="${context}/help">帮助</a></li>
				   <c:if test="${!empty dn }">
				  	 <li><a href="${context}/user/upgrade?dn=<vmt:encode value="${dn }"/>">升级</a></li>       
				   </c:if>
				   <li id="messageB" style="display:none"><a href="${context }/user/message"  >消息
					  	 	<span class="red" id="msgCount"></span>
					  	 </a>
					</li>
				</ul>
				<c:choose>
					<c:when test="${!empty loginUser }">
						<ul class="nav pull-right">  
						      <li><a target="_blank" href='<vmt:umtBase/>'>${loginUser.userInfo.trueName }</a></li> 	
						      <li><a href="${context }/logout">退出</a></li>
						</ul> 
					</c:when>
					<c:otherwise>
						<ul class="nav pull-right">  
						      <li><a href="${context }/login">登录</a></li> 	
						      <li><a target="_blank"  href="<vmt:umtBase/>/regist.jsp">注册</a></li>
						</ul> 
					</c:otherwise>
				</c:choose>
			</div>  
		</div>
	</div>
</div>
<script>
$(document).ready(function(){
	$.extend(window.vmt,{
		isUpgraded:'${isUpgraded}'=="true",
		isAdmin:'${isAdmin}'=='true',
		isGroup:'${isGroup}'=='true',
		context:'${context}',
		canEditCoreMail:function(email){
			return false;
		}
	});
	<c:if test="${!empty currOrg}">
		var domains=[];
		<c:forEach items="${currOrg.domains}" var="domain">
			domains.push('${domain}');
		</c:forEach>
		window.vmt.domains=domains;
		/**
		*验证该邮箱是否和当前组织机构所包含的域名匹配
		*/
		window.vmt.canEditCoreMail=function(email){
			if(vmt.domains.length==0){
				return false;
			}
			if(!vmt.currTeam.isCoreMail){
				return false;
			}
			var dUserDomain=email.split('@')[1];
			return vmt.domains[0]==dUserDomain;
		}
	</c:if>
	<c:if test="${!empty loginUser}">
		window.vmt.currTeam=${!empty currTeamJson?currTeamJson:'null'};
		$.get("${context}/user/message/getCount").done(function(data){
			if(data.flag&&data.data>0){
				$('#msgCount').html(data.data).parent().addClass("sup").parent().fadeIn(1000);
			}
		});
	</c:if>
	
});

	
	
</script>
