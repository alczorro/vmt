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
			      	<li class="active" id="manager"><a href="setting" >团队设置</a></li>
			      	<li id="apply"><a href="setting?view=ldap" >LDAP访问</a></li>
			      	<li id="team"><a  href="setting?view=prev" >团队权限</a></li>
			      	<li id="share"><a href="setting?view=share" >成员信息共享设置</a></li>
			      	<c:if test="${!empty iamAdminResult }">
			      		<li id="batchUpdate"><a href="setting?view=batch">批量管理</a></li>
			      	</c:if>
			      	<li id="application"><a href="setting?view=appmanage">应用管理</a></li>
			    </ul>
		  	</div>
			</div>
			<div  class="container main setting">
			
			<table class="table table-bordered setttingTable" id="manager-c" style="">
				<thead>
					<tr>
						<th class="mini">团队名称</th>
						<th class="miniL">团队LOGO</th>
						<th>管理员</th>
					</tr>
				</thead>
				<tbody>
						<c:forEach items="${nodes}" var="node">
							<tr>
								<td class="mini">
									<a class="icon-remove removeNode" href="#"></a>
									<span>
									<c:choose>
										<c:when test="${node.isGroup() }"><i class="icon-bullhorn"></i></c:when>
										<c:otherwise><i class="icon-home"></i></c:otherwise>
									</c:choose>
									
									</span>
									<span title="${node.symbol }" class="teamName" data-is-group="${node.isGroup() }"  data-pk='${node.dn }'><c:out value='${node.name }'/></span>
								</td>
								<td>
									<span class="uploadLogoSpan">
										<a class="btn btn-small btn-link uploadLogo" data-pk="${node.dn}">
											<c:choose>
												<c:when test="${node.logoId==0 }">
													<img src="../resource/images/teamLogo.png" height="50" width="50" alt="上传LOGO" title="上传LOGO"/>
												</c:when>
												<c:otherwise> 
													<img src="../logo/${node.logoId }?size=small" height="50" width="50" alt="更新LOGO" title="更新LOGO"/>
												</c:otherwise>
											</c:choose>
										</a>
										<br>通用
									</span>
									
									<span class="uploadLogoSpan">
										<a class="btn btn-small btn-link uploadMobileLogo" data-pk="${node.dn}">
											<c:choose>
												<c:when test="${node.mobileLogo==0 }">
													<img src="../resource/images/teamLogo.png" height="50" width="50" alt="上传LOGO" title="上传LOGO"/>
												</c:when>
												<c:otherwise>
													<img src="../logo/${node.mobileLogo }?size=small" height="50" width="50" alt="更新LOGO" title="更新LOGO"/>
												</c:otherwise>
											</c:choose>
										</a>
										<br>科信移动端
									</span>
									
									<span class="uploadLogoSpan">
										<a class="btn btn-small btn-link uploadPcLogo" data-pk="${node.dn}">
											<c:choose>
												<c:when test="${node.pcLogo==0 }">
													<img src="../resource/images/teamLogo.png" height="50" width="50" alt="上传LOGO" title="上传LOGO"/>
												</c:when>
												<c:otherwise>
													<img src="../logo/${node.pcLogo }?size=small" height="50" width="50" alt="更新LOGO" title="更新LOGO"/>
												</c:otherwise>
											</c:choose>
										</a>
										<br>科信PC端
									</span>
								</td>
								
								<td>
									<ul class="selected" >
										<c:forEach items="${node.admins }"  var="admin">
											<li>
												<a href="#sureRemove" role="button" class="icon-remove" data-toggle="modal"  name="${admin.name }"  umtId="${admin.umtId }" id="${node.oid }-${admin.umtId}"></a>
												<span><c:out value="${admin.name }"/></span>
											</li>
										</c:forEach>
									</ul>
									<input type="text" dn="<vmt:encode value="${node.dn }"/>" class="autoCompleteSearch" name="keyword" placeholder="请输入关键词搜索">	
								</td>
							</tr>
						</c:forEach>
				</tbody>
			</table>
			
			
			
			<div id="sureRemove" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			  <div class="modal-header">
			    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
			    <h3 id="myModalLabel">确定删除</h3>
			  </div>
			  <div class="modal-body">
			    <p>您确定删除<span id="name"></span>？</p>
			  </div>
			  <div class="modal-footer">
			    <button class="btn" data-dismiss="modal" aria-hidden="true">取消</button>
			    <button class="btn btn-primary" id="makeSure">确定</button>
			  </div>
			</div>
		</div>
			</c:otherwise>
		</c:choose>
<div id="uploadLogo" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> 
	<div class="modal-header"> 
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button> 
		<h3 id="myModalLabel">上传<span id="logoType"></span>LOGO</h3> 
	</div> 
	<form id="saveLogo" action="../logo/save" method="post" style="margin-bottom:0;">
	<div class="modal-body"> 
		<input type="hidden" id="logoTeamDN" name="logoTeamDN" value=""/>
		<input type="hidden" id="logoId" name="logoId" value=""/>
		<input type="hidden" id="uploadImgType" name="uploadImgType" value=""/>
		<div id="fileUploader" class="d-large-btn maintain">
			<div class="qq-uploader">
				<div class="qq-upload-button">
					<input type="file" multiple="multiple" name="files" style="cursor:pointer;">
				</div>
				<ul class="qq-upload-list fileList"></ul>
			</div>
		</div>
		<img align="middle"  id="destImg" style="display:none">
		<div id="hint" class="hint"></div>
	</div> 
	<div class="modal-footer"> 
		<button class="btn" data-dismiss="modal" aria-hidden="true">取消</button> 
		<input type="button" id="saveImg" class="btn btn-primary" value="保存"/> 
	</div> 
	</form>
</div>
		<jsp:include page="../bottom.jsp"></jsp:include>
		
		
	</body>
	<script type="text/javascript">
		var thisItem = null; 
		var thisName = null;
		$(document).ready(function(){
			$('#settingB').addClass('active');
			function doUploadBefore(pk,title,type,hint){
				$('#uploadLogo').modal('show');
				$('#logoTeamDN').val(pk);
				$('#logoType').html(title);
				$('#uploadImgType').val(type);
				$('#hint').html(hint);
				$('#destImg').hide();
			}
			$('.uploadLogo').on('click',function(){
				doUploadBefore($(this).data('pk'),'通用','common','请上传PNG格式的图片');
			});
			$('.uploadMobileLogo').on('click',function(){
				doUploadBefore($(this).data('pk'),'科信移动客户端','mobile','请上传194*194大小的PNG格式图片');
			});
			$('.uploadPcLogo').on('click',function(){
				doUploadBefore($(this).data('pk'),'科信PC客户端','pc','请上传145*156大小的PNG格式图片');
			});
			$('#saveImg').on('click',function(){
				if($('#logoId').val()!=''){
					$(this).closest('form').submit();
				}else{
					alert('请先上传图片');
				}
			});
			new qq.FileUploader({
				element : document.getElementById('fileUploader'),
				action : '../logo/upload',
				params:{
					'type':function(){return $('#uploadImgType').val();}
				},
				sizeLimit : 5*1024 * 1024,
				allowedExtensions:['png'],
				onComplete : function(id, fileName, data) {
					$('#destImg').hide();
					$('#logoId').val(data.clbId);
					var uploadType=$('#uploadImgType').val();
					if(data.success){
						var $destImg=$('#destImg');
						$destImg.attr('src','../logo/'+data.clbId);
						switch(uploadType){
						case'common':{
							$destImg.css('width',100);
							$destImg.css('height',100);
							break;
						}
						case'mobile':{
							$destImg.css('width',194);
							$destImg.css('height',194);
							break;
						}
						case 'pc':{
							$destImg.css('width',145);
							$destImg.css('height',156);
							break;
						}
						}
						$('#destImg').fadeIn('slow');
					}else{
						if(data.message){
							alert(data.message);
						}else{
							alert('图片格式有误');						
						}
					}
					
				},
				messages:{
		        	typeError:"请上传png",
		        	emptyError:"请不要上传空文件",
		        	sizeError:"大小超过1M限制"
		        },
		        showMessage: function(message){
		        	alert(message);
		        },
		        onProgress: function(id, fileName, loaded, total){
		        	//$('#uploadText').html("<fmt:message key='configPhoto.uploadFile'/>("+Math.round((loaded/total)*100)+"%)");
		        },
		        multiple:false
			});
			
			$('.removeNode').on('click',function(){
				var dn=$(this).next().next().data("pk");
				var flag=confirm("确定要删除["+$(this).next().next().html()+"]吗？删除后不可恢复！");
				if(flag){
					$.post('common/unbind',{'dn':dn,'type':'other'}).done(function(data){
						window.location.reload();
					});
				}
			});
			
			//autocomplete
			$('input.autoCompleteSearch').each(function(i,n){
				$(this).autocomplete('search/dn/tree?dn='+$(this).attr("dn")+"&from=addAdmin",
			            {
					  		width:300,
							parse:function(data){
									return $.map(data, function(item) {
										return {
											data : item,
											result : item.cstnetId,
											value:item.truename
										};
									});
							},
							formatItem:function(row, i, max) {
			    				return  row.truename + "\" [" + row.cstnetId+"]";
			 				},
							formatMatch:function(row, i, max) {
			    				return row.truename + " " + row.cstnetId;
			 				},
							formatResult: function(row) {
			    				return row.truename;
			 				}
						});
			});
			$("input.autoCompleteSearch").result(function(event, data, formatted){
				//ajaxt 
				var ajaxresult=true;
				$.ajax({
					  url: "setting/admin/add/"+data.umtId,
					  data:{dn:$(this).attr("dn")},
					  async:false,
					  success: function(data){
						  ajaxresult=data;
					  } 
					});
				if(!ajaxresult){
					$(this).val("");
					alert("该用户已存在，请勿重复添加");
					return;
				}
				$(this).data('selected',data);
				var newLi = $('<li><span>' + data.truename + '</span><a  role="button" class="icon-remove" umtId="'+data.umtId+'" id="' + data.oid+"-"+data.umtId + '" name="' + data.truename + '"></a></li>');
				var appendParent = $(this).parent().children("ul.selected");
				newLi.appendTo(appendParent);
				$(this).val("");
				
			});
			$("ul.selected a.icon-remove").live("click",function(){
				thisItem = $(this).attr("id");
				thisName = $(this).attr("name");
				$("#sureRemove").modal("show")
			});
			$("#sureRemove").on("show",function(){
				$("#sureRemove span#name").html(thisName);
			});
			//删除
			$("#makeSure").live("click",function(){
				var $a=$("ul.selected li a#" + thisItem);
				var ajaxresult=true;
				$.ajax({
					  url: "setting/admin/delete/"+$a.attr("umtId"),
					  data:{dn:$a.parent().parent().next().attr("dn")},
					  async:false,
					  success: function(data){
						  ajaxresult=data;
					  }
					});
				if(!ajaxresult){
					alert("请勿删除自己");
					return;
				}
				$a.parent().remove();
				$("#sureRemove").modal('hide');
				$(".modal-backdrop").remove();
			});
			
			//editable
			$("#manager-c .teamName").editable({
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
		});
	</script>
</html>