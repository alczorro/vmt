<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="WEB-INF/tld/vmt.tld" prefix="vmt"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<!DOCTYPE html>
<html>
	<head>
		<title>组织通讯录-邮件群发</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<jsp:include page="../header.jsp"/>
		<f:script src="../../resource/thirdparty/fileuploader/fileuploader.js" />
		<f:css href="../../resource/thirdparty/fileuploader/fileuploader.css" />
		<f:script src="${context }/resource/thirdparty/UEditor/ueditor.all.js"/>
<f:script src="${context }/resource/thirdparty/UEditor/ueditor.simple.config.js"/>
	</head> 
	<body class="vmt">
		<jsp:include page="../banner.jsp"/>
			<div   class="container main setting">
			<table class="table  table-bordered setttingTable" id="ipfilter-c">
				<tbody id="ipList">
					<tr  style="border:none;">
					 <td   style="border:none;"><h3>邮件群发</h3>
					 </td>
					</tr>
					<tr  style="height:500px">
						<td style="width:20%;border: none;border-top:1px solid #ddd;">
							<ul class="left-ul">
								<li class="active"><a class="left-item" href="${context }/user/enhanced/email">写邮件</a></li>
								<li><a class="left-item" href="${context }/user/enhanced/email?func=log">发送记录</a></li>
								<li><a class="left-item" href="${context }/user/enhanced/email?func=getters">历史收件人组</a></li>
								<c:if test="${isAdmin }">
									<li><a class="left-item" href="${context }/user/enhanced/email?func=manage">管理</a></li>
								</c:if>
							</ul>
						</td>
						<td id="addrecipient" style="width:25%;">
						<a id="addMember">从通讯录选择</a>
						<a id="selectFromHistory" style="margin-left:1em">从历史收件人选取</a>
						<div id="addMemberMsg" class="alert alert-warning" style="display:none"></div>
						<ul id="finalPeopleSelectedUl">
						</ul>
						</td>
						<td style=" width: 55%;">
							<form id="sendSmsForm">
								主题:
								<input type="text" name="emailTitle" maxlength="300" id="emailTitle" style="width: 495px;"/>  
								<br>
								<div>
									<div style="margin-top:10px;">
									附件:
									</div>
									<ul id="files" style="margin-bottom:-15px;">
									</ul> 
									<div id="fileUploader" class="d-large-btn maintain" style="padding-left: 47px;">
										<div class="qq-uploader"  >
											<div class="qq-upload-button">
												<input type="file" multiple="multiple" name="files" style="cursor:pointer;">
											</div>
											<ul class="qq-upload-list fileList"></ul>
										</div>
									</div>
								</div>
								
								
								
								  
								<textarea style="width:560px;height:200px;margin-top:-10px;" id="emailContent" name="emailContent"  ></textarea>
								
								<label>
									<input type="checkbox" id="sendToDchat" name="sendToDchat"/>同时发送到科信通知
								</label>
								<label style="display:inline;">
									<input type="checkbox" name="saveAsGroup" id="saveAsGroup"/>将收件人保存为历史收件人组：
									</label>
									<input type="text" id="groupName" name="groupName" maxlength="200"/>
								
								<button type="submit" style="display:block" class="btn btn-primary">批量发送</button>
							</form>
						</td> 
					</tr>
				</tbody>
			</table>
		</div>
		<jsp:include page="../bottom.jsp"></jsp:include>
<div id="selectFromHistoryModal" class="modal hide fade" style="width:920px; left:40%" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div id="am_sub">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
			<h3 id="myModalLabel">历史收件人组</h3>
		</div>
		<div class="modal-body" style="padding:0;">
			<div class="sub-main">
				<div class="row-fluid">
					<div class="span6 source-choose" style="width:50%;">
						<div id="team-c">
							<div id="am_result_view_history">
								<ul id="am_history" unscelctable="on" onselectstart="javascript:return false;" class="selected-staff" style="overflow:auto;-moz-user-select:none;height:350px;" >
								</ul>
							</div>
						</div>
					</div>
					<div class="span6 staff-batch" style="width:50%;margin-left:0px;">
						<div id="selectedHistoryPeople" style="display:none">
							<div class="bookmark popup">
								<h3 class="popModal">
									已选收件人
									<a class="btn btn-mini" id="am_removeAllSelected_history" >全部移除</a> 
									<span class="staff-num"><span id="am_selected_count_history">0</span>个成员</span>
								</h3>
							</div>
							<ul id="selected_history" class="selected-staff"  style="height:335px; overflow:auto;">
							</ul>
						</div>
						<div id="hasNoSelectedPeople_history">
							<p class="msg">您还没有添加成员</p>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="modal-footer">
		<button class="btn  btn-primary" data-dismiss="modal" aria-hidden="true" id="addSmsMemberFromHistory">确认</button> 
		<button class="btn" data-dismiss="modal" aria-hidden="true" >取消</button>
	</div>
	</div>
	</div>
<div id="addMemberDialog" class="modal hide fade" style="width:920px; left:40%" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div id="am_sub">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
			<h3 id="myModalLabel">添加收件人</h3>
		</div>
		<div class="modal-body" style="padding:0;">
			<div class="sub-main">
				<div class="row-fluid">
					<div class="span6 source-choose"  style="width:50%;">
						<div id="team-c">
							<div class="form-search search-staff" style="margin-bottom:0px;">
								<select id="myTeams" style="width:50%"> 
								</select>
								<input style="width:45%;" type="text" id="searchMemberBox" class="autoCompleteSearch" name="keyword" placeholder="请输入关键词搜索">
								<br/>
								<a style="position: relative;top: 7px" id="addAll">添加所有</a>
							</div> 
							
							<div id="am_result_view1">
								<ul id="am_select_result_1" unscelctable="on" onselectstart="javascript:return false;" class="selected-staff" style="overflow:auto;-moz-user-select:none;height:350px;" >
								</ul>
							</div>
						</div>
					</div>
					<div class="span6 staff-batch" style="width:50%;margin-left:0px;">
						<div id="selectedPeople" style="display:none">
							<div class="bookmark popup">
								<h3 class="popModal">
									已选收件人
									<a class="btn btn-mini" id="am_removeAllSelected" >全部移除</a> 
									<span class="staff-num"><span id="am_selected_count">0</span>个成员</span>
								</h3>
							</div>
							<ul id="am_selected" class="selected-staff"  style="height:335px; overflow:auto;">
								
							</ul>
						</div>
						<div id="hasNoSelectedPeople">
							<p class="msg">您还没有添加成员</p>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="modal-footer">
		<span class="root"><strong>位置：</strong><span id="breadCatch">/</span></span>
		<span class="alert alert-warning" id="msgSpan" style="display:none"></span> 
		<button class="btn  btn-primary" data-dismiss="modal" aria-hidden="true" id="addSmsMember">确认</button> 
		<button class="btn" data-dismiss="modal" aria-hidden="true" >取消</button>
	</div>
	</div>
	</div>
	</body>
	<script type="text/javascript">
		var smsContext;
		$(document).ready(function(){
			smsContext={
				selectedDept:new Map(),
				selectedHistoryPeople:new Map(),
				selectedPeople:new Map(),
				ajaxCache:new Map(),
				finalSelectPeople:new Map()
			};
			var smsUIControl={
				ControlSelected:function(){
					if(smsContext.selectedPeople.count>0){
						$('#selectedPeople').show();
						$('#hasNoSelectedPeople').hide();
					}else{  
						$('#selectedPeople').hide();
						$('#hasNoSelectedPeople').show();
						$('#am_selected').empty();
						$('#am_select_result_1 li').removeClass('choice').find("i.icon-ok").attr('class','icon-plus');
					}
					$('#am_selected_count').html(smsContext.selectedPeople.count);
				},
				ControlHistorySelected:function(){
					if(smsContext.selectedHistoryPeople.count>0){
						$('#selectedHistoryPeople').show();
						$('#hasNoSelectedPeople_history').hide();
					}else{  
						$('#selectedHistoryPeople').hide();
						$('#hasNoSelectedPeople_history').show();
						$('#selected_history').empty();
					}
					$('#am_selected_count_history').html(smsContext.selectedHistoryPeople.count);
				},
				redrawSelected:function(){
					$('#am_selected').html($('#am-select-result-template').tmpl(smsContext.selectedPeople.asArray()));
					this.ControlSelected(); 
				},
				redrawHistorySelected:function(){
					$('#selected_history').html($('#am-select-result-template').tmpl(smsContext.selectedHistoryPeople.asArray()));
					this.ControlHistorySelected(); 
				},
				redrawBreadCatchByData:function(currentDisplay,inDn){
					var display=currentDisplay.split(",");
					var dn=new DN(inDn);
					var breads=[];
					function startTo(split,index){
						var result='';
						for(var i=0;i<index;i++){
							result+=split[i]+",";
						}
						return result.substr(0,result.length-1);
					}
					$.each(display,function(i,n){
						breads.push({'dn':dn.dept(i+2),'name':display[i],'currentDisplay':startTo(display,i+1)});
					}); 
					$('#breadCatch').html($('#bread-catch-tmpl').tmpl(breads));
				},
				redrawBreadCatchByElement:function(deptElement){
					var data=$(deptElement).data('tmplItem').data;
					this.redrawBreadCatchByData(data.currentDisplay, data.dn);
				},
				renderSearchItem:function(data){
					 $('#am_select_result_1').html($('#am-search-result-template').tmpl(data,{
						 	isSelected:function(data){
						 		if(data.type=='folder'){
						 			return smsContext.selectedDept.isExists(data.dn);
						 		}else{
						 			return smsContext.selectedPeople.isExists(data.umtId);
						 		}
						 	}
						 }));
				},
				showMessageAndUnChecked:function(ele,msg){
					$(ele).attr('class','icon-plus').closest('li').removeClass('choice');
					$('#msgSpan').show().html(msg).delay(2000).fadeOut();
				},
				appendToFinalSelected:function(peoples){
					function isEmailEquals(email){
						var result=false;
						$.each(smsContext.finalSelectPeople.asArray(),function(i,n){
							if(n.cstnetId==email){
								result=true;
								return false;
							}
						});
						return result; 
					}
					var needAdd=[];
					$.each(peoples,function(i,n){
						var isEmailEq=isEmailEquals(n.email);
						if(!n.umtId&&!isEmailEq){
							smsContext.finalSelectPeople.put("m-"+n.mobile,n);
							needAdd.push(n);
						}else if(!smsContext.finalSelectPeople.isExists(n.umtId)&&!isEmailEq){
							smsContext.finalSelectPeople.put(n.umtId,n);
							needAdd.push(n);
						}
					});
					$('#finalPeopleSelectedUl').append($('#final-people-select-li-tmpl').tmpl(needAdd));
					$('#addMemberMsg').html("已过滤重复").show().delay(2000).fadeOut(500);
				}
			};
			//添加所有
			$('#addAll').on('click',function(){
				//搜索过
				if($('#searchMemberBox').val()!=''){
					var people=[];
					$('.searchResult').each(function(i,n){
						var data=$(n).data('tmplItem').data;
						people.push(data);
						addSelectPeople(data);
					});
					$('#am_selected').append($('#am-select-result-template').tmpl(people));
					smsUIControl.ControlSelected();
				}
				//点击去后台迭代查询
				else{
					var dn=$('.breadCatchShit:last').data('dn');
					$.post('${context}/user/search/dn/tree',{dn:dn,from:'addMember',q:''}).done(function(result){
						$.each(result,function(i,n){
							n.name=n.truename;
							n.email=n.cstnetId;
							n.type='link';
							addSelectPeople(n);
						});
						$('#am_selected').append($('#am-select-result-template').tmpl(result));
						smsUIControl.ControlSelected();
					});
				}
			});
			$('#selectFromHistory').on('click',function(){
				$('#selectFromHistoryModal').modal('show');
				smsContext.selectedHistoryPeople.removeAll();
				smsUIControl.redrawHistorySelected();
				$.get('${context}/user/enhanced/email/getHistoryGroup').done(function(jdo){
					if(!jdo.flag){
						alert(jdo.desc); 
					}else{
						$('#am_history').html($('#historyGroupTmpl').tmpl(jdo.data));
					}
				});
			});
			$('.searchHistoryResult').live('click',function(e){
				$.get('${context}/user/enhanced/email/getGroupMember?groupId='+$(this).data('groupId')).done(function(jdo){
					if(jdo.flag){
						$.each(jdo.data,function(i,n){
							n.truename=n.getterName;
							n.cstnetId=n.getterCstnetId;
							n.umtId=n.getterUmtId;
							addHistorySelectPeople(n);
						});				
						smsUIControl.redrawHistorySelected();
					}else{
						alert(jdo.desc);
					}
				});
			});
			$('#searchMemberBox').keyup(function(e){
				if($.trim($(this).val())==''&&e.keyCode==8){
					changeSelection(e,$('#myTeams').val()); 
				} 
			}).enter(function(event){
				$.post('${context}/user/search/dn/tree',{dn:$('#myTeams').val(),from:'addMember',q:$.trim($(this).val())}).done(function(result){
					$.each(result,function(i,n){
						n.name=n.truename;
						n.email=n.cstnetId;
						n.type='link';
					});
					 smsUIControl.renderSearchItem(result);
				}); 
			});
			function addSelectPeople(n){
				if(smsContext.selectedPeople.isExists(n.umtId)){
					return false;
				}
				smsContext.selectedPeople.put(n.umtId,n);
				return true;
			}
			function addHistorySelectPeople(n){
				if(smsContext.selectedHistoryPeople.isExists(n.umtId)){
					return false;
				}
				smsContext.selectedHistoryPeople.put(n.umtId,n);
				return true;
			}
			$('#submitMultiMobileBtn').on('click',function(){
				var multiMobiles=$('#multiMobiles').val();
				if($.trim(multiMobiles)!=''){
					var mobiles=multiMobiles.split('\n');
					var data=[];
					$.each(mobiles,function(i,n){
						n=$.trim(n);
						if(n&&n.length<15){ 
							data.push({mobile:n,truename:n,cstnetId:0,umtId:0});
						}
					});
					if(data.length>0){
						smsUIControl.appendToFinalSelected(data);
					}
				}
				$('#typeMobileDialog').modal('hide');
			});
			$('#sendSmsForm').validate({
				submitHandler:function(form){ 
					if(smsContext.finalSelectPeople.count==0){
						$('#addMemberMsg').html("请选择收件人").show().delay(2000).fadeOut(1000);
						return;
					}
					var cstnetIds=[];
					var name=[];
					var umtId=[];
					$.each(smsContext.finalSelectPeople.asArray(),function(i,n){
						cstnetIds.push(n.cstnetId);
						name.push(n.truename);
						umtId.push(n.umtId);
					});
					var clbId=[];
					var fileName=[];
					$('#files li:visible').each(function(i,n){
						clbId.push($(n).data('clbId'));
						fileName.push($(n).data('fileName'));
					});
					if(clbId.length==0){
						clbId.push(-1);
						fileName.push("");
					}
					$.post('${context}/user/enhanced/email/send',
							{'emailTitle':$('#emailTitle').val(),
								'emailContent':editor.getContent(),
								'txtContent':editor.getContentTxt(),
								'clbId':clbId,
								'cstnetId':cstnetIds,
								'name':name,
								'umtId':umtId,
								'fileName':fileName,
								'sendToDchat':$('#sendToDchat').is(":checked"),
								'saveAsGroup':$('#saveAsGroup').is(':checked'),
								'groupName':$('#groupName').val()
							})
					.done(function(data){
						if(data.flag){
							window.location.href="email?func=log";
						}else{
							alert(data.desc);
						}
					}); 
					return false;
				},
				rules:{
					emailTitle:{required:true,maxlength:200},
					emailContent:{required:true,maxlength:10000}
				},
				messages:{
					emailTitle:{required:'请输入邮件主题',maxlength:'邮件主题不能超过200个字符'},
					emailContent:{required:'请输入邮件内容',maxlength:"邮件内容不能超过10000个字"}
				}
			});
			$('#finalPeopleSelectedUl span.close').live('click',function(){
				$(this).closest('li').fadeOut(500);
				smsContext.finalSelectPeople.remove($(this).data('umtId'));
			});
			$('#addSmsMember').on('click',function(){
				smsUIControl.appendToFinalSelected(smsContext.selectedPeople.asArray());
			});
			$('#addSmsMemberFromHistory').on('click',function(){
				smsUIControl.appendToFinalSelected(smsContext.selectedHistoryPeople.asArray());
			});
 			$('#am_removeAllSelected').on('click',function(){
 				smsContext.selectedPeople.removeAll();
 				smsContext.selectedDept.removeAll();
 				smsUIControl.ControlSelected();
 				$('#am_select_result_1 li').removeClass('choice').find("i.icon-ok").attr('class','icon-plus');
 			});
 			$('#am_removeAllSelected_history').on('click',function(){
 				smsContext.selectedHistoryPeople.removeAll();
 				smsUIControl.ControlHistorySelected();
 			});
 			$('.breadCatchShit').live('click',function(e){
 				var data=$(this).data();
 				smsUIControl.renderSearchItem(smsContext.ajaxCache.get(data.dn));
 				smsUIControl.redrawBreadCatchByData(data.currentDisplay, data.dn);
 			});
			$('.cancleSelect').live('click',function(e){
				var $li=$(this).closest('li').removeClass('choice');
				$li.find("i.icon-ok").attr("class","icon-plus");
				var data=$li.data('tmplItem').data;
				if(data.type=='folder'){
					$.each(smsContext.selectedDept.get(data.dn),function(i,n){
						smsContext.selectedPeople.remove(n.umtId);
					});
					smsContext.selectedDept.remove(data.dn);
				}else{
					smsContext.selectedPeople.remove(data.umtId);
				}
				smsUIControl.redrawSelected();
				if (e.stopPropagation) {e.stopPropagation();}     
			    e.cancelBubble = true;   
			});
			$('li.searchResult.folder').live('click',function(e){
				var data=$(this).data('tmplItem').data;
				changeSelection(e,data.dn);
				
			});
			$('i.icon-plus').live('click',function(e){
				$(this).attr('class',"icon-ok");
				var $self=$(this);
				var data=$(this).closest('li').addClass('choice').data('tmplItem').data;
				if(data.type=='folder'){ 
					$.post('${context}/user/search/dn/tree',{dn:data.dn,from:'addMember',q:''}).done(function(result){
						var addPeople=[];
						$.each(result,function(i,n){
							if(!addSelectPeople(n)){
								return;
							}
							addPeople.push(n);
						});
						if(addPeople.length==0){
							smsUIControl.showMessageAndUnChecked($self, "此部门下没有可接收邮件的人员,或者已被选择");
						}
						$('#am_selected').append($('#am-select-result-template').tmpl(addPeople));
						smsUIControl.ControlSelected();
						smsContext.selectedDept.put(data.dn,result);
					});
				}else{
					var n={dn:data.dn,umtId:data.umtId,cstnetId:data.email,truename:data.name,mobile:data.mobile};
					if(!addSelectPeople(n)){
						smsUIControl.showMessageAndUnChecked(this, "该成员不可接收邮件");
						return;
					}
					$('#am_selected').append($('#am-select-result-template').tmpl(n));
					smsUIControl.ControlSelected();
				}
				if (e.stopPropagation) {e.stopPropagation();}     
			    e.cancelBubble = true;   
			});
			$('.searchResult .icon-remove').live('click',function(){
				smsContext.selectedPeople.remove($(this).data('umtId'));
				smsContext.selectedHistoryPeople.remove($(this).data('umtId'));
				$(this).closest('li').fadeOut(500,function(){$(this).remove();smsUIControl.ControlSelected();});
			});
			$('#addMember').on('click',function(){
				$('#addMemberDialog').modal('show');
			});
			$('#addMemberDialog').on('show',function(){  
				if($('#myTeams option').size()==0){
					$.post("${context}/user/enhanced/email/getMyTeam").done(function(result){
						if(result.flag){
							$('#myTeams').html($('#optionTmpl').tmpl(result.data)).trigger('change');
						}else if(!result.data){
							alert("您不属于任何一个团队,无法群发邮件");
						}else{
							alert(result.desc);
						}
					});
				}else{
					smsContext.selectedPeople.removeAll();
					smsContext.selectedDept.removeAll();
					$('#myTeams').trigger('change');
					smsUIControl.ControlSelected();
				}
			});
			$('#myTeams').on('change',changeSelection);
			function changeSelection(e,dn,data){
				if(e.type=='change'||e.type=='keyup'){ 
					smsUIControl.redrawBreadCatchByElement($('#myTeams').find(":selected"));
				}else{
					smsUIControl.redrawBreadCatchByElement(e.currentTarget); 
				}
				var value=(dn?dn:$(this).val());
				var cache=smsContext.ajaxCache.get(value);
				if(!cache){
					$.post("${context}/user/search/dn/list", {dn:value})
					.done(function(data){
						 smsContext.ajaxCache.put(value,data); 
						 smsUIControl.renderSearchItem(data);
					});
				}else{
					smsUIControl.renderSearchItem(cache);
				}
				$('#searchMemberBox').val('');
			}
			//上传附件
			new qq.FileUploader({
				element : document.getElementById('fileUploader'),
				action : 'email/upload',
				params:{
					'type':''//function(){return $('#uploadImgType').val();}
				},
				sizeLimit : 50*1024 * 1024,
				onSubmit:function(id,fileName){
					$('#files').append($('#file-tmpl').tmpl({id:id,fileName:fileName}));
				},
				//allowedExtensions:['png'],
				onComplete : function(id, fileName, data) {
					if(data.clbId==-2){
						alert('附件名称过长！');
						return false;
					}else if(data.clbId<0){
						alert('上传附件失败！');
						return false;
					} 
					$('#file_pro_'+id).html("100%");    
					$('#file_'+id).data('clbId',data.clbId);
					$('#file_dw_'+id).attr('href','${context}/user/enhanced/email/download/'+data.clbId);
				},
				messages:{
		        	typeError:"请上传png",
		        	emptyError:"请不要上传空文件",
		        	sizeError:"大小超过50M限制"
		        },
		        showMessage: function(message){
		        	alert(message);
		        },
		        onProgress: function(id, fileName, loaded, total){
		        	$('#file_pro_'+id).html((loaded*100/total).toFixed(2)+"%");    
		        },
		        multiple:true
			});
			$('#needRmbB').addClass('active');
			$('.removeFile').live('click',function(){
				$(this).closest('li').fadeOut(500);
			});
			//百度编辑器
			var editor = new UE.ui.Editor();
			editor.render("emailContent");
			//qqfile
			$('#uploadText').html('添加附件');
		});
		</script>
		<script id="historyGroupTmpl" type="text/x-jquery-tmpl">
		<li class="searchHistoryResult folder " data-group-id="{{= id}}">
		<i class="icon-hdd"></i>
		<h4>{{= groupName}}</h4> 
	</li> 	
		</script>
		<script id="optionTmpl" type="text/x-jquery-tmpl">
			<option value="{{= dn}}">{{= name}}</option>
		</script>
		<script id="file-tmpl" type="text/x-jquery-tmpl">
			<li id="file_{{= id}}" class="emailFile attFile" data-file-name="{{= fileName}}" data-clb-id="{{= clbId}}">
				<a href="#" id="file_dw_{{= id}}">{{= fileName}}</a> 
				(<span id="file_pro_{{= id}}"></span>)
				<span style="margin-right:5px"  class="close removeFile">×</span>
			</li> 
		</script>
		<script id="am-search-result-template" type="text/x-jquery-tmpl">
	
		{{if $data.type=='folder'}}
			<li class="searchResult folder {{if $item.isSelected($data)}} choice{{/if}}">
				<i class="icon-hdd"></i>
				<h4>{{= name}}</h4>
				<i  class="icon-chevron-right"></i>
				<i  class="{{if $item.isSelected($data)}}icon-ok{{else}}icon-plus{{/if}}"></i>
				<span class="cancleSelect">取消选择</span> 
			</li> 	
		{{else}}
			<li class="searchResult link {{if $item.isSelected($data)}} choice{{/if}}">
				<i class="icon-user"></i> 
				<h4>{{= name}}</h4> <span style="color:#999">- {{= email }}{{if $data.mobile!=null }} - {{= mobile}}{{/if}}</span>
				<i  class="{{if $item.isSelected($data)}}icon-ok{{else}}icon-plus{{/if}}"></i>
				<span class="cancleSelect">取消选择</span> 
			</li> 
		{{/if}} 
		
	</script>
	<script id="bread-catch-tmpl" type="text/x-jquery-tmpl">
		/<a data-dn={{= dn }} data-current-display="{{= currentDisplay }}" class="breadCatchShit">{{= name}}</a>
	</script>   
	<script id="am-select-result-template" type="text/x-jquery-tmpl">
	<li class="searchResult">
		<i class="icon-user"></i> 
		<h4>{{= truename }}</h4> <span style="color:#999">- {{= cstnetId }}</span>
		<i style="float:right" data-umt-id="{{= umtId}}" class="icon-remove"></i> 
	</li> 
	</script>
	<script id="am-select-result-template" type="text/x-jquery-tmpl">
	<li class="searchResult">
		<i class="icon-user"></i> 
		<h4>{{= truename }}</h4> <span style="color:#999">- {{= cstnetId }}</span>
		<i style="float:right" data-umt-id="{{= umtId}}" class="icon-remove"></i> 
	</li> 
	</script>
	<script id="final-people-select-li-tmpl" type="text/x-jquery-tmpl">
	<li class="finalReciver"><span class="licontainName">{{= truename }}-{{= cstnetId }}</span><span data-umt-id="{{= umtId}}" class="close">×</span></li>
	</script>
		<style>
		li.searchResult h4{
			display:inline
		}
		i.icon-plus{
			display:none
		}
		li.searchResult:hover i.icon-plus{
			display:block
		}
		li.searchResult i.icon-plus{
			float:right;
		}
		li.searchResult.link i.icon-plus{
			margin-right:25px
		}
		li.searchResult i.icon-chevron-right{
			float:right;
		}
		li.searchResult i.icon-ok{
			float:right;
		}
		li.searchResult.link i.icon-ok{
			margin-right:25px
		}
		
		li i{
			cursor: pointer; 
			margin-right:5px;
		}
		li.searchResult.choice  span.cancleSelect{
			display: inline; 
			float:right;
			color:white;
			cursor:pointer; 
			margin-right:5px;
		} 
		li.searchResult  span.cancleSelect{
			display:none;
		}
		
	</style>
	
		
</html>