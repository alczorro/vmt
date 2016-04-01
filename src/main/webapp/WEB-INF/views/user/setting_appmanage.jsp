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
		<c:choose>
			<c:when test="${empty nodes }">
				<div class="container main">
					<p class="noTeam">您没有可管理的团队</p>
				</div>
			</c:when>
			<c:otherwise>
			<div class="navbar settingNav container">
		  	<div class="navbar-inner">
		    	<ul class="nav">
			      	<li id="manager"><a href="setting">团队设置</a></li>
			      	<li id="apply"><a href="setting?view=ldap" >LDAP访问</a></li>
			      	<li id="team"><a href="setting?view=prev"  >团队权限</a></li>
			      	<li id="share"><a href="setting?view=share"  >成员信息共享设置</a></li>
			      	<c:if test="${!empty iamAdminResult }">
			      		<li id="batchUpdate"><a href="setting?view=batch">批量管理</a></li>
			      	</c:if>
			      	<li class="active" id="application"><a href="setting?view=appmanage">应用管理</a></li>
			    </ul>
		  	</div>
			</div>
			<div  class="container main setting">
			<table class="table table-bordered setttingTable">
				<thead>
					<tr>
						<th class="mini">团队名称</th>
						<th style="width: 180em;">应用推荐</th>
						<th style="width: 2em;">开通与关闭</th>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${nodes}" var="node">
							<tr>
								<td class="mini">
									<span>
									<c:choose>
										<c:when test="${node.isGroup() }"><i class="icon-bullhorn"></i></c:when>
										<c:otherwise><i class="icon-home"></i></c:otherwise>
									</c:choose>
									
									</span>
									<span title="${node.symbol }" class="teamName" ><c:out value='${node.name }'/></span>
								</td>
								<td data-dn="${node.dn }" data-team-dn='${node.dn }' data-team-symbol="${node.symbol }">
									<c:forEach items="${node.data.getApps() }" var="app">
									<div class="application-t" data-app-id="${app.id }" data-dn="${node.dn }">
											<img class="application1"  style="margin-right:5px;" src="<vmt:appLogo app="${app }"/>">
											<span class="application-tittle">
												<a href="${app.appClientUrl}" target="_blank"><c:out value="${app.appName }"/></a>
											</span>
											<c:if test="${app.appType!='oauth'  }">
												<a class="application-edi updateLocalApp">修改</a> 
											</c:if>
											<a class="application-edi removeApp">删除</a>
									</div>
									</c:forEach>
									
									<a  class="label label-info batchUpdate showAppModal"  style="margin-top:15px;">新增</a>				
								</td>
								<td>
									<c:choose>
										<c:when test="${node.data.appsOpen}">
											<span>已开通</span>  <a data-dn="${node.dn }" class="closeApp">关闭</a>
										</c:when>
										<c:otherwise> 
											<span>未开通</span>  <a data-dn="${node.dn }" class="openApp">开通</a>
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
						</c:forEach>
				</tbody>
			</table>
			<div id="addappModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
				<div class="modal-header">
			   		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
					<h3 id="myModalLabel">新增应用</h3>
			  	</div>
			  	<div class="modal-body sub-main" style="padding:0">
			    	<div class="source-choose" style="height:300px;">
						<ul class="source-list">
							<li class="active" id="addexisting"><a >选择已有通行证应用</a></li>
							<li id="addcustom"><a >自定义应用</a></li>
						</ul>
						<div id="addexisting-c"class="add-application">
							<div class="modal-search">
								<input name="searchOauth" id="searchOauth" type="text" maxlength="20" />
								<button id="searchOauthBtn" class="btn" style="margin-bottom:10px;">搜索</button>
								<span class="error" id="addOauthError"></span>
							</div>
							<div class="modal-app" >
								<div id="oauthAppContent">
								</div>
								<a class="inbatch" id="changeOauthApp">换一批</a>
							</div>
						</div>
						<div id="addcustom-c" class="add-application" style="display:none">
						<form id="batchCreateUser" style="margin:0">
							<p style="margin:10px 0px 0px 30px"><span>应用名称：</span>
							<input name="appName" id="appName" type="text" maxlength="20" />
							<span class="error" id="appName_error_place"></span></p>
							<p style="margin-left:30px;"><span>应用链接：</span>
							<input name="appClientUrl" id="appClientUrl" type="text"/>
							<span class="error" id="appClientUrl_error_place"></span></p>
							<p style="margin: -10px 0px 10px 2px;"><span>应用手机链接：</span>
							<input name="appMobileUrl" id="appMobileUrl" type="text"/>
							<span class="error" id="appMobileUrl_error_place"></span></p>
							
							<input name="logo100Url" id="logo100Url" type="hidden"/>
							<input name="appType" id="appType" type="hidden" value="local"/>
								<span style="margin:-15px 0px 0px 30px"><span>图标上传：</span>
								<img id="uploadImage" src="../resource/images/defaultApp.png">
								<a href="#" id="uploadImageBtn">上传</a> 
								</span>
						</form>
						</div>
					</div>
				</div>
			  	<div class="modal-footer">
			    	<button class="btn" data-dismiss="modal" aria-hidden="true">取消</button>
			    	<button class="btn btn-primary" id="saveAppForm">确定并保存</button>
			  	</div>
			</div>
			
			
		</div>
			</c:otherwise>
		</c:choose>
		
<jsp:include page="../bottom.jsp"></jsp:include>
<jsp:include page="../confirm.jsp"></jsp:include>
	</body>
	<script type="text/javascript">
		$(document).ready(function(){
			$('#settingB').addClass('active'); 
			//点击source-list显示对应的div
			$("ul.source-list li").die('click').live("click",function(){
				var name = $(this).attr("id");
				$(this).parent().parent("div.source-choose").children("div").hide();
				$('#' + name + '-c').show();
				$("ul.source-list li.active").removeClass("active");
				$(this).addClass("active");
				if($(this).attr('id')=='addcustom'){
					$(this).trigger('shown');
				}
			});
			//更换应用状态
			$('.closeApp').live('click',function(){
				switchApp($(this),{'isOpen':false,'toggleClass':'openApp','toggleA':'开通','toggleSpan':'未开通'});
			});
			$('.openApp').live('click',function(){
				switchApp($(this),{'isOpen':true,'toggleClass':'closeApp','toggleA':'关闭','toggleSpan':'已开通'});
			});
			function switchApp($this,msg){
				var dn=$this.data('dn');
				$.get('app/switch',{'dn':dn,'isOpen':msg.isOpen}).done(function(data){
					$this.attr('class',msg.toggleClass);
					$this.html(msg.toggleA);
					$this.prev().html(msg.toggleSpan);
				});
			}
			var AppManage={ 
				context:{
					offset:0,
					size:9,
					selectedData:{},
					isAdd:true
				}
			};
			$('.showAppModal').on('click',function(){
				AppManage.context.isAdd=true; 
				initModal('app/add',$(this));
				searchOauth();
				$('#addappModal').modal('show');
			});
			var initedFileUploader=false;
			$('#addcustom').on('shown',function(){
				if(!initedFileUploader){
					new qq.FileUploader({
						element : document.getElementById('uploadImageBtn'),
						action : '../logo/upload',
				        template: '<div class="qq-uploader">' + 
		                '<div class="qq-upload-drop-area"><span>Drop files here to upload</span></div>' +
		                '<div id="upload-qq-btn" class="qq-upload-button"><div id="uploadText">上传</div></div><br/>'+
		                '<ul style="display:none" class="qq-upload-list"></ul>' + 
		                '</div>',
						params:{
							'type':function(){return $('#uploadImgType').val();}
						},
						sizeLimit : 2*1024 * 1024,
						allowedExtensions:['png','gif','jpg'],
						onComplete : function(id, fileName, data) {
							if(data.success){
								$('#uploadImage').attr('src',"../logo/"+data.clbId);
								$('#logo100Url').val(data.clbId);
							}else{
								alert('上传失败');
							}
						},
						messages:{
				        	typeError:"请上传png,gif,jpg",
				        	emptyError:"请不要上传空文件",
				        	sizeError:"大小超过2M限制"
				        },
				        showMessage: function(message){
				        	alert(message);
				        },
				        onProgress: function(id, fileName, loaded, total){
				        	//$('#uploadText').html("<fmt:message key='configPhoto.uploadFile'/>("+Math.round((loaded/total)*100)+"%)");
				        },
				        multiple:false
					});
					initedFileUploader=true;
				}
				
				
			});
			//更新app
			$('.updateLocalApp').on('click',function(){
				var data=$(this).closest('div').data();
				$.get('app/'+data.appId,data).done(function(result){
					if(result.flag){
						$('form #appName').val(result.data.appName);
						$('form #appClientUrl').val(result.data.appClientUrl);
						$('form #logo100Url').val(result.data.logo100Url);
						$('form #appMobileUrl').val(result.data.appMobileUrl);
						$('form #uploadImage').attr('src','../logo/'+(result.data.logo100Url||'0'));
						
					}
				});
				AppManage.context.isAdd=false;
				initModal('app/update/'+data.appId,$(this));
				$('#addappModal').modal('show');
			});
			function initModal(url,$this){
				AppManage.context.localUrl=url;
				AppManage.context.selectedData=$this.closest('td').data();
				if(AppManage.context.isAdd){
					$('#myModalLabel').html("新增应用");
					$('#addexisting').show().click();
				}else{
					$('#myModalLabel').html("更新应用");
					$('#addexisting').hide();
					$('#addcustom').click();
				}
				$('.error').empty();
				$('#searchOauth').val('');
				$('#oauthAppContent').empty();
				$('#uploadImage').attr('src','../resource/images/defaultApp.png');
				$('#batchCreateUser')[0].reset();
			}
			//删除某个应用
			$('.removeApp').on('click',function(){
				var $compenent=$(this).closest('div');
				var data=$compenent.data();
				$.get("app/delete",{appId:data.appId,dn:data.dn}).done(function(data){
					if(data.flag){
						$compenent.remove();
					}
				});
			});
			//
			$('#appMobileUrl').on('focus',function(){
				if($(this).val()==''){
					$(this).val($('#appClientUrl').val());
				}
			});
			//验证自定义应用
			$('#batchCreateUser').validate({
				 submitHandler:function(form){
					return false;
				 },
				 rules: {
					appName:{
						 required:true,
						 maxlength:200
				 	 },
				 	appClientUrl:{
						 required:true,
						 url:true
					},
					appMobileUrl:{
						 required:true,
						 url:true
					}
				 },
				 messages: {
					 appName: {
						 required:'请填写应用名称',
						 maxlength:"应用名称过长"
					 },
					 appClientUrl:{
						 required:'请填写应用链接',
						 url:'非法的URL'
					 },
					 appMobileUrl:{
						 required:'请填写应用手机链接',
						 url:'非法的URL'
					}
				 },
				 errorPlacement: function(error, element){
					 var sub="_error_place";
					 var errorPlaceId="#"+$(element).attr("name")+sub;
					 	$(errorPlaceId).html("");
					 	error.appendTo($(errorPlaceId));
				}
			});
			//保存应用信息
			$('#saveAppForm').on('click',function(){
				//添加oauth
				if($('#addexisting').hasClass('active')){
					var $selectedOauth=$('.modal-app-t.active');
					if($selectedOauth.size()==0){
						$('#addOauthError').html('请选择一个应用');
						return;
					}
					$.extend(AppManage.context.selectedData,$selectedOauth.data());
					AppManage.context.selectedData.tmplItem=null;
					$.post('app/add',AppManage.context.selectedData).done(function(flag){
						if(flag.flag){
							$('#addappModal').modal('hide');
							window.location.reload();
						}else{
							$('#addOauthError').html(flag.desc);
						}
					});
				}
				//添加自定义
				else{
					var $form=$('#batchCreateUser');
					if($form.valid()){
						AppManage.context.selectedData.tmplItem=null;
						$.extend(AppManage.context.selectedData,$form.formToJson());
						$.post(AppManage.context.localUrl,AppManage.context.selectedData).done(function(flag){
							if(flag.flag){
								$('#addappModal').modal('hide');
								window.location.reload();
							}else{
								alert(flag.desc);
							}
						});
					}
				}
				
			});
			//点击搜索的Oauth
			$('.modal-app-t').live('click',function(){
				$('.modal-app-t').removeClass('active');
				$(this).addClass('active');
			});
			//搜索oauth应用
			$('#searchOauthBtn').on('click',function(){
				AppManage.context.offset=0;
				searchOauth();
			});
			$('#searchOauth').enter(function(){
				AppManage.context.offset=0;
				searchOauth();
			});
			$('#changeOauthApp').on('click',function(){
				AppManage.context.offset+=AppManage.context.size;
				searchOauth('没有更多了');
			});
			function searchOauth(msg){
				var key=$('#searchOauth').val();
				$.get("${context}/user/app/search",
						{
							'keyword':key,
							'offset':AppManage.context.offset,
							'size':AppManage.context.size
						},
						function(data){
							if(!data.data||data.data.length==0){
								$('#oauthAppContent').html(msg?msg:"未找到关键字为\""+key+"\"的查询结果");
							}else{
								$('#oauthAppContent').html($('#oauthAppTmpl').tmpl(data.data,{
									getLogoUrl:function(logoUrl){
										if(logoUrl){
											return '<vmt:config key="duckling.umt.site"/>'+logoUrl;
										}else{
											return '../logo/0';
										}
									}
								}));
							}
						}
				);
			}
		});
	</script>
	<script id="oauthAppTmpl" type="text/x-jquery-tmpl">
		<div class="modal-app-t" data-logo100-url="{{= logo100Url }}" data-logo64-url="{{= logo64Url }}" data-logo32-url="{{= logo32Url }}" data-logo16-url="{{= logo16Url }}" data-app-name="{{= clientName}}" data-app-type="oauth" data-app-client-id="{{= clientId}}" data-app-client-url="{{= clientUrl}}">
			
				<img style="height:30px;width:30px" src="{{= $item.getLogoUrl(logo32Url)}}">
			
				<span class="modal-app-tittle">
				<a target="_blank" href="{{= clientUrl}}">{{= clientName }}</a>
			</span>
		</div>
	</script>
</html>