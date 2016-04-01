<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="WEB-INF/tld/vmt.tld" prefix="vmt"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<!DOCTYPE html>
<html>
	<head>
		<title>组织通讯录-同步</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<jsp:include page="../header.jsp"/>
		<f:script src="../resource/thirdparty/bootstrap-editable/js/bootstrap-editable.js"/>
		<f:css href="../resource/thirdparty/bootstrap-editable/css/bootstrap-editable.css"/>
	</head>
	<body class="vmt">
		<jsp:include page="../banner.jsp"/>
			<div class="navbar settingNav container">
		  	<div class="navbar-inner">
		    	<ul class="nav">
			      	<li  id="manager"><a href="gm">同步设置</a></li>
			      	<li id="ipfilter"><a href="gm?func=ipList">IP设置</a></li>
			      	<li  id="upgrade"><a href="gm?func=upgrade">升级</a></li>
			      	<li class="active" id="domainMappings"><a href="gm?func=domainMapping">域名映射</a></li>
			      	<li   id="smsManage"><a href="gm?func=smsManage">短信管理</a></li>
			    </ul>
		  	</div>
			</div>
			<div  class="container main setting">
			<table class="table  table-bordered setttingTable">
				<thead>
					<form action="gm?func=domainMapping" method="post">
					<tr>
						
							<td><input type="text" name="name" value="<c:out value="${condition.name }"/>" placeholder="请输入组织名称"/></td>
							<td><input type="text" name="domain" value="<c:out value="${condition.domain }"/>" placeholder="请输入组织域名"/></td>
							<td>是否是院内用户:
							<select  name="cas">
								<option value="">--</option>
								<option value="true" <c:if test="${condition.cas=='true' }">selected="selected"</c:if> >是</option>
								<option value="false" <c:if test="${condition.cas=='false' }">selected="selected"</c:if> >否</option>
							</select> 
							是否是邮箱用户:
							<select  name="coreMail">
								<option value="">--</option>
								<option value="true" <c:if test="${condition.coreMail=='true' }">selected="selected"</c:if> >是</option>
								<option value="false" <c:if test="${condition.coreMail=='false' }">selected="selected"</c:if>>否</option>
							</select>
								<input type="submit" value="查询"/>
							</td>
					</tr>
					</form>
				</thead>
				<tbody>
				<c:forEach items="${orgs}" var="org">
					<tr>
						<td>
						<c:choose>
										<c:when test="${node.isGroup() }"><i class="icon-bullhorn"></i></c:when>
										<c:otherwise><i class="icon-home"></i></c:otherwise>
									</c:choose>
						<span class="teamName" data-pk='${org.dn }'>${org.name }</span>
						</td>
						<td>
							<ul class="selected" >
								<c:forEach items="${org.domains }" var="domain">
									<li>
										<a data-domain='${domain }' data-dn="<vmt:encode value="${org.dn }"/>" data-symbol='${org.symbol }' class="icon-remove removeDomain"></a>
										<c:out value="${domain }"/>
									</li>
								</c:forEach>
							</ul>
							<input maxlength="50" type="text" data-symbol="${org.symbol }" data-dn="<vmt:encode value="${org.dn }"/>" class="insertDomain" name="keyword" placeholder="请输入域名并按回车添加">	
						</td>
						<td data-dn="<vmt:encode value="${org.dn }"/>">
							<input type="checkbox" <c:if test="${org.isCas }">checked="checked"</c:if> class="isCas"/>是否是院内用户
							<input type="checkbox" <c:if test="${org.isCoreMail }">checked="checked"</c:if> class="isCoreMail"/>是否是邮箱用户
						</td>
					</tr>
					
					</c:forEach>
				</tbody>
			</table>
			
		</div>
		<jsp:include page="../bottom.jsp"></jsp:include>
	</body>
	<script type="text/javascript">
		var thisItem = null;
		var thisName = null;
		$(document).ready(function(){
			//editable
			$(".teamName").editable({
				type:'text',
				url:'common/rename',
				params: function(params) {
				    var data = {
				    		dn:params.pk,
				    		name:params.value,
				    		type:'folder'
				    };
				    return data;
				},
        		mode:"popup",
        		validate: function(value) {
        			if($.trim(value) == '') {
				        return '不允许为空';
				    }
				   var isGroup=$(this).data("isGroup");
				   if(isGroup){
					   if(!groupNameOK($.trim(value))){
						   return "请不要输入特殊字符";
					   }
				   }else{
					   if(!nodeIdOK($.trim(value))){
						   return "请不要输入特殊字符";
					   }
        		}
        		}
        	});
			//change check
			$('.isCas').on('click',function(){
				var isCas=$(this).prop('checked');
				var dn=$(this).parent().data('dn');
				$.post('gm/changeIsCas',{'isCas':isCas,'dn':dn}).done(function(flag){
					
				});
			});
			
			//change check
			$('.isCoreMail').on('click',function(){
				var isCas=$(this).prop('checked');
				var dn=$(this).parent().data('dn');
				$.post('gm/changeIsCoreMail',{'isCoreMail':isCas,'dn':dn}).done(function(flag){
					
				});
			});
			//remove domain
			$('.removeDomain').live('click',function(){
				var $data=$(this).data();
				var $self=$(this);
				$.post('gm/removeDomain',$data).done(function(flag){
					if(flag){
						$self.parent().remove();
					}
				});
			});
			
			//insert domain
			$('.insertDomain').enter(function(){
				var ulTarget=$(this).prev();
				var domain=$(this).val();
				if($.trim(domain)==''){
					alert('域名不允许为空');
					return;
				}
				var data=$(this).data();
				var $self=$(this);
				var param={'domain':domain,'dn':data.dn,'symbol':data.symbol}
				$.post('gm/addDomain',param).done(function(flag){
					if(!flag){
						alert('域名重复，请更改');
					}else{
						ulTarget.append($("#domainTmpl").tmpl(param));
						$self.val("");
					}
				});
			});
		});
	</script>
<script id="domainTmpl"  type="text/x-jquery-tmpl">
<li>
	<a data-domain='{{= domain }}' data-dn="{{= dn }}"  data-symbol='{{= symbol }}' class="icon-remove removeDomain"></a>
	{{= domain }}
</li>
</script>
</html>
