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
		<f:script  src="${context }/resource/js/jquery.validate.min.js"/>
		<f:script  src="${context }/resource/thirdparty/bootstrap/js/bootstrap.min.js"/>
		<f:script  src="${context }/resource/thirdparty/jquerymobile/jquery.mobile-1.3.2.min.js"/>
		<f:script  src="${context }/resource/js/vmt-util.js"/>
		<f:script  src="${context }/resource/js/vmt-validate-ext.js"/>
	</head>
	<body data-dom-cache=“false” class="vmt">
	<form method="post"  class="userContainer" id="createTeamForm" >
		<div data-role="header" class="ui-bar-b">
		    <a data-transition="slide" data-direction="reverse" href="${context }/user/mobile/detail?dn=<vmt:encode value="${prev }"/>" data-icon="arrow-l">取消</a>
		    <h1>创建组织机构</h1>
		</div>
		<div class="container mobile userDetail create">
				<div data-role="fieldcontain">
			        <label class="title">请选择团队类型：</label>
			        <fieldset style="width:100%" data-role="controlgroup" class="ui-corner-all ui-controlgroup ui-controlgroup-vertical" aria-disabled="false" data-disabled="false" data-shadow="false" data-corners="true" data-exclude-invisible="true" data-type="vertical" data-mini="false" data-init-selector=":jqmData(role='controlgroup')">
						<div class="ui-radio">
							<label class="radio">
								<input id="orgRadio" name="type" value="org" checked="checked" type="radio" style="display:none"/>
								组织机构
								<span class="mobileHint">组织机构是您在工作中的机构设置，您可以在组织机构内创建部门，添加成员。</span>
							</label> 
						</div>
						<div class="ui-radio">
							<label class="radio">
								<input id="groupRadio" name="type"  value="group" type="radio" style="display:none"/>
								群组
								<span class="mobileHint">基于兴趣、讨论需要而创建的组。</span>
							</label>  
						</div>  
					</fieldset>
			    </div>
			    <label class="title">请输入团队名称：</label>
			    <div data-role="fieldcontain">
		     		<input id="teamName" type="text" name="teamName"/> 
			    </div>
			    <span id="teamName_error" class="error help-inline"></span>
			    
			    <label class="title">请输入团队代号：</label>
			    <div data-role="fieldcontain">
			        <input id="teamSymbol" type="text" name="teamSymbol"/> 
			    </div>
				<p class="mobileHint sole">团队代号唯一，提交后不可更改，不作为显示使用。</p>
				<span id="teamSymbol_error" class="error help-inline"></span>
				
				
			    <div data-role="fieldcontain">
			        <label class="title">团队权限：</label>
			        <fieldset style="width:100%" data-role="controlgroup" class="ui-corner-all ui-controlgroup ui-controlgroup-vertical" aria-disabled="false" data-disabled="false" data-shadow="false" data-corners="true" data-exclude-invisible="true" data-type="vertical" data-mini="false" data-init-selector=":jqmData(role='controlgroup')">
						<div class="ui-radio">
							<label class="radio">
					        	<span class="option">
					        		<input checked="checked" value="privateNotAllow" name="privilege"  type="radio" style="display:none"/>私密
					        	</span>
					        </label> 
						</div>
						<div class="ui-radio">
							<label class="radio">
								<span class="option">
									<input name="privilege" type="radio" value="openRequired" style="display:none"/>公开需审核
								</span>
							</label> 
						</div>  
						<div class="ui-radio">
							<label class="radio"> 
								<span class="option">
									<input name="privilege" type="radio" value="allOpen"  style="display:none"/>完全公开
								</span>
							</label> 
						</div>  
					</fieldset>
			    </div>
		     	<a data-theme="b" data-role="button" id="submitBtn" style="text-decoration:none">保存</a>
			</div>
		</form>
	</body>
	<script>
	$(document).ready(function(){
		
		$("#teamName").off().on('blur',function(){
			var destValue=$('#teamSymbol').val();
			if($.trim(destValue)==''){
				var $teamName=$(this).val();
				$.post('../jq/getPinyin',{'word':$teamName}).done(function(data){
					$('#teamSymbol').val(data);
				});
				$('#teamSymbol').val($(this).val());
			}
		});
		$('#teamName,#teamSymbol').enter(function(event){
				$("#teamName").blur();
				$("#createTeamForm").submit();
				
		});
		$('#submitBtn').off('click').on('click',function(){
			var $form=$('#createTeamForm');
			if($form.valid()){
				var val=$("input[name=type]:checked").val(); 
				$form.attr("action","../"+val+"/create");
				$form[0].submit();
			}
		});
		$("#createTeamForm").validate({
			rules:{
				teamName:{
					required:true,
					doMySelf:{
						call:function(value){
							switch($('input[name=type]:checked').val()){
								case 'org':return nodeIdOK(value);
								case "group":return groupNameOK(value);
								default:return false;
							}
							return false;
						}
					}
					},
				teamSymbol:{
						required:true,
						remote:{
							 type: "POST",
							 url: '../jq/canTeamSymbolUse',
							 data:{ 
									 'type':function(){return $("input[name=type]:checked").val();}
						  		}
							 },
							 specialLetter:true,
							 noZh:true
					}
				
			},
			messages:{
				teamName:{required:'请输入团队名称',doMySelf:'请不要输入特殊字符'},
				teamSymbol:{required:'请输入团队代号',remote:'团队代号已被使用'}
			},
			errorPlacement: function(error, element){
				var sub="_error";
				var errorPlaceId="#"+$(element).attr("name")+sub;
				$(errorPlaceId).html("");
				error.appendTo($(errorPlaceId));
			}
		});
		vmt.doHash=function(hash){
			if(hash=='createGroup'){
				$('#groupRadio').click();
			}
		};
	});
	</script>
	<f:script  src="${context }/resource/js/vmt-hash-resolver.js"/>
</html>