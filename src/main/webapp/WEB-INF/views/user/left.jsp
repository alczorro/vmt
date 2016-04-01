<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="WEB-INF/tld/vmt.tld" prefix="vmt"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<div class="span3 team-list">
	<p class="title">
		<span style="border:0px;color:#2f973f;" class="count">${count }</span>  
		<span  class="left-item">全部</span> <!--href="index?dn=all" -->  
	</p> 
	<div class="leftMenu">
		<p class="sub-title">
			<a tmplId="createTeam" role="button" class="count add vmt-dialog" >添加</a>我的组织机构  
		</p>
		<ul class="sub-team" id="orgList">
			<c:forEach items="${orgs }" var="org" >
				<li <c:if test="${dn==org.dn }">class="active"</c:if> >
					<i class="icon-home"></i>
					<a href="${context }/user/index?dn=<vmt:encode value="${org.dn }"/>"><span class="count">${org.count }</span></a>
					<a href="${context }/user/index2?dn=<vmt:encode value="${org.dn }"/>" class="left-item" dn="<vmt:encode value="${org.dn }"/>"><c:out value="${org.name }"/></a>
				</li>
			</c:forEach>
		</ul>
		<p class="sub-title">
			<a init="$('input[name=type][value=group]').attr('checked','checked');" id="createGroupBtn" tmplId="createTeam" role="button" class="count add vmt-dialog" >添加</a>我的群组
		</p>
		<ul class="sub-team" id="groupList">
			<c:forEach items="${groups }" var="group" >
				<li <c:if test="${dn==group.dn }">class="active"</c:if>>
					<i class="icon-bullhorn"></i>
					<a href="${context }/user/index?dn=<vmt:encode value="${group.dn }"/>"><span class="count">${group.count }</span></a>
					<a href="${context }/user/index?dn=<vmt:encode value="${group.dn }"/>" class="left-item" dn="<vmt:encode value="${group.dn }"/>"><c:out value="${group.name }"/></a>
				</li>
			</c:forEach>
		</ul>
	</div>
</div>
<script>
	$(document).ready(function(){
		if($(".leftMenu ul li").hasClass("active")){
			$('.left-item,p.sub-title').each(function(i,n){
				if($(this).parent().hasClass("active")){
					$(".leftMenu").scrollTop($(this).offset().top-110);
					return false;
				}
			});
		}
		if('${empty orgs}'=='true'){
			$('#orgList').html("<p class='leftNoTeam'>没有组织机构</p>");
		}
		if('${empty groups}'=='true'){
			$('#groupList').html("<p class='leftNoTeam'>没有群组</p>");
		}
		var ids=[];
		$('#navspybar a').each(function(i,n){
			var id=$(n).attr('href');
			ids.push(id);
		});
		for(var i=0;i<ids.length;i++){
			var id=ids[i];
			ids[i]={'id':id,'start':$('#'+id).offset().top};
			if(i<ids.length-1){
				ids[i].end=$('#'+ids[i+1]).offset().top;
			}else{
				ids[i].end=1000000;
			}
		}
		function judiceWhereAndActive(scroll){
			$(ids).each(function(i,n){
				if(scroll>=n.start&&scroll<=end){
					$('a[href="#'+n.id+'"]').addClass('active');
				}
			});
		}
	});
</script>