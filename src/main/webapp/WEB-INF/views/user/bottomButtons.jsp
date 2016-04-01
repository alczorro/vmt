<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="WEB-INF/tld/vmt.tld" prefix="vmt"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<div class="oper-area row-fluid">
	<div class="span3 first">
		<a tmplId="createTeam" role="button" class="btn btn-small vmt-dialog" >创建</a>
	</div>
	<div class="span6">
		<c:if test="${canAdd }">
			<c:if test="${!isGroup }">
				<a style="display:none" tmplId="createSection" role="button" class="btn btn-small vmt-dialog">新建部门</a>
			</c:if>
			<a style="display:none" tmplId="addMember" role="button" class="btn btn-small vmt-dialog">添加成员</a>
			<c:if test="${!isGroup&&isCurrAdmin &&currTeam.getIsCoreMail()&&!empty currTeam.domains}">
				<a style="display:none" tmplId="registCoreMail" role="button" class="btn btn-small vmt-dialog">新增邮箱账号</a>
			</c:if>
		</c:if>
	</div>
	
	<div class="span3" >
		<c:if test="${isAdmin }">
			<a style="display:none" default='false' tmplId="removeTeam" role="button" class="btn btn-small vmt-dialog">移除</a>
			<c:if test="${!isGroup }">
				<a style="display:none"  tmplId="moveTo" role="button" class="btn btn-small vmt-dialog">移动到...</a>
			</c:if>
		</c:if>
		<c:if test="${isCurrAdmin }">
			<a style="display:none" tmplId="batchUpdate" role="button" class="btn btn-small vmt-dialog">编辑</a>
		</c:if>
	</div>
</div>