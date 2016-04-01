<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="WEB-INF/tld/vmt.tld" prefix="vmt"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<!DOCTYPE html>
<html>
	<head>
		<title>组织通讯录-管理</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<jsp:include page="../header.jsp"/>
		<f:script  src="../resource/thirdparty/bootstrap-editable/js/bootstrap-editable.js"/>
		<f:script src="../resource/thirdparty/fileuploader/fileuploader.js" />
		<f:css  href="../resource/thirdparty/bootstrap-editable/css/bootstrap-editable.css" />
		<f:css href="../resource/thirdparty/fileuploader/fileuploader.css" />
	</head>
	<body class="vmt">
		<jsp:include page="../banner.jsp"/>
			<div class="navbar settingNav container">
		  	<div class="navbar-inner">
		    	<ul class="nav">
			      	<li class="active" id="appmanage"><a href="${context }/user/profile">应用</a></li>
			    </ul>
		  	</div>
			</div>
			
			<div><!-- Application management -->
				
					<div  class="container main setting">	
					<c:choose>
					<c:when test="${empty apps }">
						<p class="noTeam">暂无推荐应用</p>
					</c:when>
					<c:otherwise>
					
					<div class="profile-app-img">
					<c:forEach items="${apps}" var="app">
						<span data-app-type="${app.appType }" data-logo-url="<vmt:appLogo app="${app }"/>" data-app-client-url="${app.appClientUrl}" data-app-name="<c:out value="${app.appName }"/>" data-app-client-id="${app.appClientId }" data-app-id="${app.id }">
							<img id="profile-con-app-img" class="showDetail add-on preparePopover" data-html='true' data-animation='false' data-trigger="hover" data-content="<c:out value="${app.appName }"/>" data-placement="top"  src="<vmt:appLogo app="${app }"/>"/>
						</span>
					</c:forEach>
					</div>
					<div class="profile-app-introduce" >
					<div  id="profileContent" >
						<p id="content">
							<b>应用名称</b>：<span id="appName"></span><br>
							<b>应用首页</b>：<span id="appClientUrl"></span><br>
							<b>所属团队</b>：<span id="teamName"></span><br>
							<b>是否显示</b>：<input type="radio" value="true" name="isDisplay" id="diaplay_1"/><label for="diaplay_1">是</label> 
											<input type="radio" value="false" name="isDisplay" id="diaplay_2"/><label  for="diaplay_2">否</label>
							
							<br><div id="saveSuccessMsg" style="display:none" class="alert alert-success">保存成功!</div>
						</p>
					</div>
					</div>
					</c:otherwise>
				</c:choose>
					</div>
					
				
				
			
			</div><!-- end Application management -->
			
			<jsp:include page="../bottom.jsp"></jsp:include>
			
	</body>
	<script>
	$(document).ready(function(){
		$('#profileB').addClass('active');
		$('input[name=isDisplay]').on('click',function(){
			if(!AppProfile.selectedItem){
				alert('请选择一个应用');
				$(this).removeAttr("checked");
			}else{
				$.post('${context}/user/profile/change',{isDisplay:$(this).val(),appKey:AppProfile.selectedItem.appKey,appType:AppProfile.selectedItem.appType}).done(function(){
					$('#saveSuccessMsg').fadeIn(1000,function(){
						$(this).fadeOut(1000);
					});
					
				});
			}
		});
		$('.preparePopover').popover();
		var AppProfile={
			selectedItem:null,
		};
		$('.showDetail').on('click',function(){
			$('#profileContent').show();
			var data=$(this).closest('span').data();
			var _data={appType:data.appType};
			if(data.appType=='local'){
				_data.appKey=data.appId;
			}else{
				_data.appKey=data.appClientId;
			}

			$.get("${context}/user/profile/getAppInfo",{appKey:_data.appKey,appType:data.appType}).done(function(flag){
				if(flag.flag){
					AppProfile.selectedItem=_data;
					$('#appName').html(data.appName);
					$('#appClientUrl').html(data.appClientUrl);
					$('#teamName').html($('#teamNameTmpl').tmpl(flag.data));
					$('input[name=isDisplay][value='+flag.data.status+']').attr('checked','checked');
				}
			});
		});
	});
	</script>
	<script id="teamNameTmpl"  type="text/x-jquery-tmpl">
		{{each teamName}}
			<div>{{= $value }}<div>
		{{/each}}
	</script>
</html>