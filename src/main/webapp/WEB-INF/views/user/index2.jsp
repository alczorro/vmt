<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="WEB-INF/tld/vmt.tld" prefix="vmt"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<!DOCTYPE html>
<html>
	<head>
		<title>组织通讯录</title>
		<jsp:include page="../header.jsp"/>
		<f:css href="../resource/thirdparty/fileuploader/fileuploader.css"/>
		<f:script src="../resource/thirdparty/bootstrap-editable/js/bootstrap-editable.js"/>
		<f:css href="../resource/thirdparty/bootstrap-editable/css/bootstrap-editable.css" />
		<f:script src="../resource/thirdparty/respond.src.js"/>
	</head>
	<body class="vmt">
		<jsp:include page="../banner.jsp"/>
		<div class="container main">
			<p class="alert alert-success" id="msg_success" style="display:none">保存成功!</p>
			<div class="row-fluid">
			  	<jsp:include page="left.jsp"></jsp:include>
			  	<div class="span6 staff">
		  			<div class="form-search search-staff">
			  			<input type="text" id="keyword" placeholder="按姓名、邮箱、职称搜索">
			  			<a class="btn btn-primary" id="search_button">搜索</a>
			  			<c:if test="${!isGroup}">
					  			<a class="btn userView list active" title="层级视图" alt="层级视图"  href="index2?dn=<vmt:encode value="${dn }"/>"></a>
					  			<a class="btn userView " title="列表视图" alt="列表视图" href="index?dn=<vmt:encode value="${dn }"/>"></a>
				  		</c:if>
			  		</div>
					<div id="slider"  style="display:none;-moz-user-select:none;" unscelctable="on" onselectstart="javascript:return false;">
						<p class="memberTitleAbc" >
							<span class="name">姓名</span>
							<span class="mail">邮箱</span>
							<span class="depart">部门</span>
							<span class="status">状态</span>
							<span class="choice">选择
								<c:if test="${dn!='all'}">
									<a class="helpMe"></a>
								</c:if>
							</span>
						</p>
						<div class="popover top helpMeContent" style="display:none;">
							<div class="arrow"></div>
							<h3 class="popover-title">提示 <i class="icon-remove"></i></h3>
							<div class="popover-content" style="word-break:break-all">按键盘上的“ctrl”键或者“shift”键，可以进行多选。</div>
						</div>
						<div class="slider-content">
							<ul class="memberListAbc">
								<li id="search">
									<ul id="search_result">
									</ul>
								</li>
							</ul>
						</div>
					</div>
					<p id="searchMsg" style="display:none" class="msg">没有找到您要找的成员</p>
			  		<div id="columnsFrame"  style="-moz-user-select:none;" unscelctable="on" onselectstart="javascript:return false;"></div>
				</div>
				
			  	<div class="span3 staff-detail form-horizontal" id="userDetailDiv" >
			  	<div class="bookmark">
						<i class="${isGroup?'icon-bullhorn':'icon-home' }"></i>
						<h2 >${teamInfo.name }</h2> 
					</div>
					<div class="control-group">
			           <label class="control-label">人数：</label>
			           <div class="controls">共${teamInfo.count }人</div>
			     	</div>
			     	<div class="control-group">
			           <label class="control-label">管理员：
			           <c:if test="${isAdmin }">
			           	<a target="_blank" class="management" href="${context }/user/setting">管理</a>
			           </c:if>
			           </label>
			            <c:forEach items="${teamInfo.admins }" var="admin" varStatus="varStatus"> 
			          	 <div class="controls"><a href="Mailto:${admin.email}">${varStatus.index!=0?',':'' }${admin.name}</a></div>
			           </c:forEach>
			     	</div>
			     	<div class="control-group">
			           <label class="control-label">当前应用: </label>
			           <div class="controls" > 
			            <a href="http://dchat.escience.cn" target="blank">-科信</a>
			          （
			           <c:choose>
			           	<c:when test="${currTeam.openDchat }">
			           		<span id="dchatStatus">已开通</span>
			           		<c:if test="${isAdmin }">
			           			|<span id="openDchatHint"><a id="closeDchat" data-dn="${currTeam.dn }">关闭</a></span>
			           		</c:if>
			           	</c:when>
			           	<c:otherwise>
			           		<span id="dchatStatus">未开通</span>
			           		<c:if test="${isAdmin }">
			           		|
			           		<c:choose>
			           		<c:when test="${!isGroup&&currTeam.isCoreMail }">
				           		<span id="openDchatHint">
				           		<c:choose>
				           			<c:when test="${currTeam.applyOpenDchat=='applyDchat' }">
				           				已提交申请
				           			</c:when>
				           			<c:otherwise>
				           				<a id="applyDchat" >申请开通</a>
				           			</c:otherwise>
				           		</c:choose>
				           		</span>
				           		
			           		</c:when>
			           		<c:otherwise>
			           			<span id="openDchatHint"><a id="openDchat" data-dn="${currTeam.dn }">开通</a></span>
			           		</c:otherwise>
			           		</c:choose>
			           		</c:if>
			           		</c:otherwise>
			           	</c:choose>
			           	）
			           </div>
			     	</div>
			     	<c:if test="${!isGroup}">
			     	<div class="control-group">
			          	 <div class="controls">
			          	 <a href="https://mail.cstnet.cn" target="_blank">-中科院邮件系统</a>
			          	 （<c:choose>
			          	 	<c:when test="${currTeam.isCoreMail }">已开通</c:when>
			          	 	<c:otherwise>未开通</c:otherwise>
			          	 </c:choose>）</div>
			     	</div>
			     	</c:if>
			     	<c:if test="${isGroup }">

				     	<div class="control-group">
				          	 <div class="controls">
				          	 <a href="<vmt:config key="ddl.base"/>/${currTeam.symbol }/list" target="_blank">-团队文档库</a>
				          	 （ 已开通 ）
				          	 </div> 
				     	</div>
			     	</c:if>
			     	
			     	<c:if test="${!empty currTeam.getApps()&&(isAdmin||currTeam.appsOpen ) }">
				     	<div class="control-group">
				           <label class="control-label">推荐应用：
				           	<c:if test="${isAdmin }">
				          	 <a target="_blank"  class="management" href="${context }/user/setting?view=appmanage">管理</a>
				            </c:if>
				           </label>
				          	 <div class="controls">
					          	 <c:forEach items="${currTeam.getApps() }" var="app">
					          	 	<a href="${app.appClientUrl }" target="_blank">
					          	 		<img class="application1" src='<vmt:appLogo  app="${app }"/>'>
					          	 	</a>
					          	 </c:forEach>
					          	 <p class="remark">推荐应用将会在科信等应用显示</p>
				          	 </div>
				     	</div>
			     	</c:if>
			     	
			     	 <c:if test="${isAdmin }">
				     	<div class="control-group">
				           <label class="control-label">成员信息共享：
				           <c:if test="${isAdmin }">
				           			<a class="management" target="_blank" href="${context }/user/setting?view=share">管理</a>
				        	</c:if>
				           </label>
				     	</div>
			     	</c:if>
			     	
			     	<c:if test="${isAdmin }">
				     	<div class="control-group">
				          	 <div class="controls">
								<input class="setMemberVisible" style="margin:2px 5px 5px 0" data-dn="${currTeam.dn }" <c:if test="${currTeam.memberVisible}">checked="checked"</c:if> type="checkbox" />成员信息团队内可见
							</div>
				    	 </div>
			    	 </c:if>
				</div>
				
				
				
				
				<div class="span3 staff-batch" id="seletedItemsDiv" style="display:none">
					<div class="bookmark">
						<h3>
							已选成员
							<a class="btn btn-mini" id="deleteAll">全部移除</a> 
							<span class="staff-num"><span id="selectedCount"></span>个成员</span>
						</h3>
					</div>
					<ul class="selected-staff" id="selectedItemsResult">
					</ul>
				</div>
			</div>
			<jsp:include page="bottomButtons.jsp"></jsp:include>
			
			<div id="sureRemove" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			  <div class="modal-header">
			    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
			    <h3 id="myModalLabel">确定移除</h3>
			  </div>
			  <div class="modal-body">
			    <p>您确定移除<span id="name"></span>？</p>
			    <p class="warning" id="removeNodePS"></p>
			  </div>
			  <div class="modal-footer">
			    <button class="btn" data-dismiss="modal" aria-hidden="true">取消</button>
			    <button class="btn btn-primary" id="makeSure">确定</button>
			  </div>
			</div>
		</div>
		<jsp:include page="../bottom.jsp"></jsp:include>
		<jsp:include page="../confirm.jsp"/>
	</body>
	<script type="text/javascript">
			var scopeDic;
			var thisItem = null;
			var thisName = null;
			var thisDn=null; 
			$(document).ready(function(){
				$('#indexB').addClass('active');
				function openDchat(isOpen,$self){
					var opDesc=isOpen?'开通':'关闭';
					$.post('${context}/user/common/openDchat',{'dn':$self.data('dn'),'isOpen':isOpen}).done(function(data){
						if(data){
							if(isOpen){
								$('#dchatStatus').html('已开通');
								$self.html('关闭');
								$self.attr('id','closeDchat');
							}else{
								
								$('#dchatStatus').html('未开通');
								if('${!isGroup&&currTeam.isCoreMail}'=='true'){
									$self.html('申请开通');
									$self.attr('id','applyDchat');
								}else{
									$self.html('开通');
									$self.attr('id','openDchat');
								}
							}
						}else{
							alert(opDesc+'失败！');
						}
						
					});
				}
				//开通科信
				$('#openDchat').live('click',function(){
					openDchat(true,$(this));
				});
				//关闭科信
				$('#closeDchat').live('click',function(){
					var self=$(this);
					vmt.confirm({
						callback:function(data){
							if(data){
								openDchat(false,self);
							}
						},
						content:'请谨慎操作，关闭后当前团队成员将不能通过科信进行即时交流。'
					});
					
				});
				//成员可见
				$(".setMemberVisible").on('change',function(){
					$.post("setting/privilege/vmt-member-visible/"+($(this).is(":checked")),{dn:encodeURIComponent($(this).data("dn"))});
				});
				//申请开通科信
				$("#applyDchat").live('click',function(){
					$.post('${context}/user/common/applyDchat',{dn:'${currTeam.dn}'}).done(function(data){
						if(data){
							alert('申请提交成功，请您耐心等待');
							
							$('#openDchatHint').html('已提交申请');
						}else{
							alert('提交失败');
						}
					});
					
				});
				respond.update();
				var selectedItem=new Map();
				vmt.selectedItem=selectedItem;
				vmt.from='index2';
				//选择问号
				$(".memberTitleAbc span.choice a.helpMe").live("click",function(e){
					$(".helpMeContent").toggle();
					e.stopPropagation();
				});
				//绑定enter事件
				$('#keyword').on('keyup', function(event){
					 if (event.keyCode=="13"){
						 $("#search_button").click();
					 }
					 if($(this).val()==''){
						 $('#slider').hide();
						 $('#columnsFrame').show();
						 $('#searchMsg').hide();
						 return;
					 };
				});
				//绑定searchKeyword按钮
				$('#search_button').on('click',function(){
					var keyword=$('#keyword').val();
					$.post("search/local",{
							 'keyword':keyword,
							 'scope':'${dn}'
						 }).done(function(data){
							 var $dest=$('#search_result');
							 $dest.html("");
							 if(data&&data.length){
								 $('#slider').show();
								 $('#searchMsg').hide();
								 $('#columnsFrame').hide();
							 }else{
								 $('#searchMsg').show();
								 $('#slider').hide();
								 $('#columnsFrame').hide();
							 }
							 $("#user_li").tmpl(data,{
									getHighestName: function(){
										var display=(this.data.currentDisplay).split(',');
										return ('${dn}'.indexOf(",")>-1)?display[0]:display[display.length-1];
									},
									encode:function(str){
										return encodeURIComponent(str);
									},
									getChoice:function(){
										if(selectedItem.isExists(this.data.oid)){
											return "icon-ok";
										}
										return "icon-plus0";
									},
									getStatus:function(){
										switch(this.data.status){
										case 'true':return '已确认';
										case 'false':return '未确认';
										case 'refuse':return '已拒绝';
										case 'apply':return '申请加入';
										}
									}
								}).appendTo($dest);
								$('#search_result li:even').addClass("even");
						 });
				});
				//多选配置
				var multiPressConfig={
						selector:".search_item",
						isSelected:function(li){
							return $(li).find("i").attr("class")=="icon-ok";
						},
						addClass:function(li){
							$(li).find("i").attr("class","icon-ok");
							var data=($(li).data().tmplItem.data);
							var item={
									label:data.name,
									name:data.name,
									email:data.cstnetId,
									oid:data.oid,
									umtId:data.umtId,
									dn:data.dn,
									currentDisplay:data.currentDisplay,
									status:data.status,
									type:'link'
							};
							$(document).data("selectedDepart",null);
							$(li).find("span.choice").append("<span class='cancle'>取消选择</span>");
							selectedItem.put(item.oid,item);
						},
						removeClass:function(li){
							$(li).find("i.icon-ok").attr("class","icon-plus0");
							$(li).find("span.choice .cancle").remove();
							selectedItem.remove($(li).attr("id"));
						},
						getCount:function(){
							return selectedItem.count;
						},
						complete:function(){
							if(selectedItem.count==1){
								drawDetail();
							}else{
								redrawSelected();							
							}
						},
						multiable:'${dn!="all"}'=='true'  
				};
				var keyControl=new KeyMultiPress(multiPressConfig);
				if('${count}'=='0'){
					$('#columnsFrame').html("<p class='noTeam'>没有团队，现在<a tmplId='createTeam' role='button' class='count add vmt-dialog' >创建</a></p>");
					return;
				}
				
				//init dialog
				
				$(document).data('domain','${domain}');
				$(document).data("currentDN",'${dn}');
				//删除
				$("#columnsFrame .column ul li i.icon-trash").live("click",function(){
					thisItem = $(this).parent().attr("id");
					thisName = $(this).parent().text();
					thisDn=$(this).parent().data('nodeData').dn;
					$("#sureRemove").modal("show");
				});
				
				$("#sureRemove").on("show",function(){
					$("#sureRemove span#name").html('“' + thisName + '”');
					$.post('search/dn/list',{dn:thisDn}).done(function(data){
						if(data.length>0){
							$("#removeNodePS").html("注意：此部门包含下级部门或者团队成员，此操作将会一并移除，且操作不可恢复。");
						}
					});
					
				});
				
				$("#sureRemove").on("hidden",function(){
					$("#removeNodePS").html("");
				});
				function clickParent(liId){
					var $a=$("#" + liId);
					var $data=$a.data("nodeData");
					var columns=$("#columnsFrame div.column");
					if($a.data("nodeType")=='link'){
						if(columns.size()<2){
							window.location.reload();   
							return
						}
						$("#columnsFrame .column:last ").prev().find("ul>li.active").click();
					}else{
						if(columns.size()<3){
							window.location.reload();
							return
						}
						$("#columnsFrame .column:last ").prev().prev().find("ul>li.active").click();
					}
					selectedItem.remove($data.id);
					redrawSelected();
				}
				
				$("#makeSure").live("click",function(){ 
					var $a=$(".column ul li i.icon-trash").parent("#" + thisItem);
					$.post("common/unbind",{'dn':$a.data("nodeData").dn,'type':$a.data("nodeData").type}).done(function(data){
						if(data>=0){
							clickParent(thisItem);
							var $count=$(".sub-team li.active a.left-item").prev().children('span');
							$count.html(parseInt($count.html())-data);
							$("#sureRemove").modal("hide");
						}else if(data==-1){
							alert('请不要移除自己');
						}else if(data==-2){
							alert('成员内包含邮箱账户，请先移动');
						}
					});
				});
				var searchScope='';
				scopeDic=new Map();
				$(".sub-team li a").each(function(i,n){
					 var dn=$(this).attr("dn");
					 if(dn!=null){
					 	searchScope+=((i==0?"":',')+dn);
					 	scopeDic.put(dn,$(this).html());
					 }
				});
				$(document).data("selected",selectedItem);
				var leftItemDN=encodeURIComponent('${dn}');
				var isAjaxing=false;
				var columnConfig={
						customNodeTypeHandler:{link:function(hColumn, node, data){
						}},
						nodeSource:function(node_id,callback){
								var data=$('#'+node_id).data('node-data');
						    	showButtonList();
						    	var queryDN=(data==null)?leftItemDN:data.dn;
						    	if(queryDN=='all'){
						    		return;
						    	}
						    	if(isAjaxing){
						    		return;
						    	}
						    	isAjaxing=true;
						    	$.post('search/dn/list',{dn:queryDN}).done(function(data){
						    		isAjaxing=false;
						    		callback(null,$.map(data,function(item){
						    			return {id:item.oid,label:item.name,type:item.type,dn:item.dn,email:item.email,currentDisplay:item.currentDisplay,status:item.status,visible:item.visible};
						    		}));
						    	});
						},
						labelText_maxLength:500,
						editable:{
							isAdmin:'${isAdmin}'=='true',  
							type:'text',
							url:'common/rename',
							params: function(params) {
							    var data = {
							    		dn:params.pk,
							    		name:params.value,
							    		type:$(this).parent().data("nodeType")
							    };
							    return data;
							},
							success :function(){
								clickParent($(this).parent().attr('id'));
								
							}, 
							validate: function(value) {
								if($.trim(value) == '') {
							        return '不允许为空';
							    }
							    
							    var $nodeData=$(this).parent().data("nodeData");
							    var ajaxResult='';
							    if($nodeData.type=='link'){
							    	return;
							    }
							    if(!nodeIdOK(value)){
							    	return '请不要输入特殊字符';
							    }
							    $.ajax({
									 url: 'jq/canDepartNameUse',
									 async:false,
									 type:'post',
									 data:{dn:$nodeData.dn,departName:$.trim(value)},
									 success:function(data){
											if(!data){ 
												ajaxResult= "部门名字冲突";
											}
										}
								});
							    if(ajaxResult!=''){
							    	return ajaxResult;
							    }
							},
							toggle:"manual",
	                		mode:"inline"
	                	}
				};
				function buildItem(data){
					return {
						name:data.label,
						email:data.email,
						oid:data.id,
						umtId:'',
						dn:data.dn,
						currentDisplay:data.currentDisplay,
						status:data.satatus,
						visible:data.visible,
						type:data.type
					};
				}
				new hColumns($("#columnsFrame"),columnConfig);
				$(".hcolumnLi i.icon-plus:visible").live('click',function(event){
					var data=($(this).parent().data("node-data"));
					selectedItem.put(data.id,{
							name:data.label,
							email:data.email,
							oid:data.id,
							umtId:'',
							dn:data.dn,
							currentDisplay:data.currentDisplay,
							status:data.satatus,
							visible:data.visible
					});
					if(selectedItem.count>1){
						$(document).data('selectedDepart',null);
					}
					redrawSelected();
					event.stopPropagation();
				});
				var shiftPressed=false;
				var shiftFirstEmail=-1;
				var shiftSecondEmail=-1;
				$("#columnsFrame .hcolumnLi").live('click',function(){
					var data=$(this).data("node-data");
					var item=buildItem(data);
					if(selectedItem.count<2&&data.type=='link'){
						selectedItem.removeAll();
						selectedItem.put(data.id,item);
						drawDetail(data);
					}
					if(data.type=='folder'){
						$(document).data("currentDN",encodeURIComponent(data.dn));
						if(selectedItem.count<2){
							$(document).data("selectedDepart",data);
						}
						if(selectedItem.count<2){
							drawDetail(data);
							return;
						};
					}else{
						$(document).data("selectedDepart",null);
						var dn=new DN(data.dn);
						$(document).data("currentDN",encodeURIComponent(dn.parent()));
					}
					if(shiftPressed){
						if(shiftFirstEmail==-1){
							shiftFirstEmail=$(this).attr("id");
						}else{
							shiftSecondEmail=$(this).attr("id");
							var index=0;
							var $ul=$(this).parent();
							//clearAll();
							if($ul.children("li#"+shiftFirstEmail).length==0||$ul.children("li#"+shiftSecondEmail).length==0){
								return;
							}								
							$ul.children("li").each(function(i,n){
								if(shiftFirstEmail==shiftSecondEmail){
									return false;
								}
								if(shiftFirstEmail==$(n).attr("id")||shiftSecondEmail==$(n).attr("id")){
									index++;
								}
								if(index>0){
									var currentData=$(this).data("node-data");
									if(currentData.type=='link'){
										selectedItem.put(currentData.id,buildItem(currentData));
									}
								}
								if(index==2){
									return false;
								}
							});
							shiftFirstEmail=-1;
							redrawSelected(); 
						}
					}else{
						shiftFirstEmail=$(this).attr("id");
					}
				});
				$(document).keydown(function(event){
					switch(event.keyCode){
						//ctrl
						case 17:{
							ctrlPressed=true;
							break;
						}
						//shift
						case 16:{
							shiftPressed=true;
							break;
						}
					}
				});
				$(document).keyup(function(event){
					switch(event.keyCode){
						//ctrl
						case 17:{ 
							ctrlPressed=false;break;
						}
						//shift
						case 16:{
							shiftPressed=false;
							break;
						}
					}
				});
				$("#selectedItemsResult a.closeMe").live("click",function(){
					var oid=$(this).attr("oid");
					selectedItem.remove(oid);
					multiPressConfig.removeClass($('#'+oid));
					redrawSelected();  
				});
				//取消选择
				$('.search_item span.choice span.cancle').live("click",function(event){
					multiPressConfig.removeClass($(this).parent().parent().parent());
					multiPressConfig.complete();
					event.stopPropagation();
				});
				//全部移除
				$('#deleteAll').on("click",function(){
					selectedItem.removeAll();
					redrawSelected();
					keyControl.clearAll();
				});
				function judiceBatchUpdateBtn(){
					if(selectedItem.count!=0||$(document).data("selectedDepart")){
						$("a[tmplid=moveTo]").show();
						$("a[tmplid=removeTeam]").show();
					}else{
						$("a[tmplid=moveTo]").hide();
						$("a[tmplid=removeTeam]").hide();
					}
					if(selectedItem.count>1){
						$("a[tmplid=batchUpdate]").show();
					}else{
						$("a[tmplid=batchUpdate]").hide();
					}
					
				}
				function drawDetailCallback(data){
					
					$('#userDetailDiv').html($('#detail_user').tmpl(data,{getValues:function(){
							var currentDisplay=this.data.currentDisplay;
							return currentDisplay.replace(/,/gm,"&nbsp;/&nbsp;");
						},
						getStatus:function(){  
							switch(this.data.status){
							case 'true':return '已确认';
							case 'false':return '待确认';
							case 'refuse':return '已拒绝';
							case 'apply':return '申请加入';
							};
						},
						getVisible:function(){
							return this.data.visible?'是':'否';
						},
						getAccountStatus:function(){
							var result='';
							switch(this.data.accountStatus){
								case 'normal':{
									if(this.data.expireTime&&this.data.expireTime<getTodayStr()){
										result= '锁定';
									}else{
										result='正常';
									}
									break;
								}
								case 'locked':{
									result= '锁定';
									break;
								}
								case 'stop':{
									result= '停用';
									break;								
								}
							}
							if(this.data.expireTime){
								result+='('+this.data.expireTime+')';
							}
							return result;
						},
						canEdit:function(){
							return vmt.canEditCoreMail(this.data.cstnetId);
						},
						getSex:function(){
							switch(this.data.sex){
							case 'male':return '男';
							case 'female':return '女';
							default:return '未知';
							};
						},
						isUpgraded:function(){
							return vmt.isUpgraded;
						},
						showResend:function(){
							var status=this.data.status;
							return (status=='false'||status=='refuse');
						}}));
					
						var $editableDest=$('#userDetailDiv h2');
						var dn=new DN($editableDest.data("pk"));
						var umtId=dn.getValue(dn.prefix(0));
						var oid=data.oid;
						var newValue='';
						if(('${loginUser.userInfo.umtId}'==umtId||'${canAdd}'=='true')){
							$editableDest.editable({
								type:'text',
								url:'common/rename',
								params: function(params) {
									newValue=params.value;
								    return {
								    		dn:params.pk,
								    		name:params.value,
								    		type:data.type
								    };
								},
								  success :function(){
										var hColumnsSpan=$('#'+oid+' a.hcolumnLiSpan');
										hColumnsSpan.html(newValue);
										hColumnsSpan.attr("title",data);
								  }
								  ,validate: function(value) {
									  if($.trim(value) == '') {
									        return '不允许为空';
									    }
									  var type=$(this).data('entryType');
									  if(type=='folder'&&!nodeIdOK(value)){
											 return "请不要输入特殊字符";
									  }
									  var dn=$(this).data('pk');
									  var ajaxResult='';
									  $.ajax({
											 url: 'jq/canDepartNameUse',
											 async:false,
											 type:'post',
											 data:{'dn':dn,departName:$.trim(value)},
											 success:function(data){
													if(!data){ 
														ajaxResult= "部门名字冲突";
													}
												}
										});
									 if(ajaxResult){
										 return ajaxResult;
									 }
								}
							});
						}
						judiceBatchUpdateBtn();
				}
				function drawDetail(jsObj){
					$('#seletedItemsDiv').hide();
					$('#userDetailDiv').show();
					$('#userDetailDiv').html("");
					jsObj=(jsObj==null?selectedItem.asArray()[0]:jsObj);
					if(jsObj.type=='link'){
						$.post('show',{"dn":jsObj.dn}).done(function(data){
							data.type=jsObj.type;
							drawDetailCallback(data);
						});
					}else if(jsObj.type=='folder'){
						$.post('depart/show',{"destDn":jsObj.dn}).done(function(data){
							data.type=jsObj.type;
							drawDetailCallback(data);
						});
					}
					judiceBatchUpdateBtn();
				}
				function redrawSelected(){
					if(selectedItem.count==0){
						$('#seletedItemsDiv').hide();
						$('#userDetailDiv').hide();
						judiceBatchUpdateBtn();
						return;
					}
					$("a[tmplid=batchUpdate]").show();
					$('#userDetailDiv').hide();
					$('#selectedCount').html(selectedItem.count);
					$('#seletedItemsDiv').show();
					$('#selectedItemsResult').html("");
					$('#select_li').tmpl(selectedItem.asArray()).appendTo($('#selectedItemsResult'));
					$('#selectedItemsResult li:even').attr("class","even");
					judiceBatchUpdateBtn();
				}
				function showButtonList(){
					$("a[tmplid=createSection]").show();
					$("a[tmplid=addMember]").show();
					$("a[tmplid=registCoreMail]").show();
				}
				function hideButtonList(){
					$("a[tmplid=createSection]").hide();
					$("a[tmplid=addMember]").hide();
					$("a[tmplid=removeTeam]").hide();
					$("a[tmplid=registCoreMail]").hide();
					$("a[tmplid=moveTo]").hide();
				}
				if('${dn}'=='all'){
					hideButtonList();
				}
				$(".resend").live('click',function(){
					var dn=($(this).attr("dn"));
					$.post('resend',{'dn':dn}).done(function(data){
						$('#msg_success').html("重新发送激活邮件成功！");
						$('#msg_success').show();
						$('#msg_success').hide(3000);
					});
				});
				$('a[tmplId=removeTeam]').click(function(){
					var selectedDepart=$(document).data("selectedDepart");
					if(selectedDepart!=null){
						$(this).attr("default","false");
						$('#'+selectedDepart.id).find("i.icon-trash").click();;
					}else{
						$(this).attr("default","true");
					}
				});
				$('.removeCoreMailUser').live('click',function(){
					if(confirm("您确定删除邮箱账号吗？ 此操作不可恢复")){
						$.post('removeCoreMail',$(this).data()).done(function(){
							var $activies=$('li.hcolumnLi.active');
							if($activies.size()==1){
								$activies.hide();
							}else{
								$activies.eq(-2).click();
							}
							$('li.search_item i.icon-ok').parent().parent().parent().hide();
							selectedItem.removeAll();
							redrawSelected();
						});
					};
				});
			});
			$('.upgrade').live('click',function(){
				var dn=$(this).data('dn');
				window.location.href="upgrade?dn="+encodeURIComponent(dn);
			});
		</script>
		<script id="select_li" type="text/x-jquery-tmpl">
		<li class="{{= $item.getEvenClass()}}">
			<a class="closeMe" oid={{= oid}}><i class="icon-remove"></i></a>
			<h4>{{= name}}</h4>
			<p>{{= email}}</p>
		</li>
		</script>
		<script id="detail_user" type="text/x-jquery-tmpl">
			{{if $data.type=='folder'}}	
				<div class="bookmark">
					<i class="icon-hdd"></i>
					<h2 data-entry-type="folder" oid="{{= id}}" data-pk="{{= dn}}">{{= name}}</h2> 
					{{if ${isAdmin} }}
						<a tmplId="updateDept" role="button" class="btn btn-small btn-link vmt-dialog">编辑</a>
					{{/if}}
				</div>
				
				<div class="control-group">
		           <label class="control-label">部门人数：</label>
		           <div class="controls">{{= count }}人</div>
		     	</div>
				<div class="control-group">
		           <label class="control-label">部门位置：</label>
		           <div class="controls">{{= $item.getValues() }}</div>
		     	</div>
				{{if ${isAdmin} }}
				<div class="control-group">
		           <label class="control-label">权重：</label>
		           <div class="controls">{{= listRank }}</div>
		     	</div>
				<div class="control-group">
		           <label class="control-label">是否在组织中显示：</label>
		           <div class="controls">{{= visible?'是':'否' }}</div>
		     	</div>
				{{/if}}
			{{else}}
				<div class="bookmark">
					<i class="icon-user"></i>
					<h2 oid="{{= oid}}" data-entry-type="link" data-pk="{{= dn}}">{{= name}}</h2>
					{{if ${isCurrAdmin} }}					
						<div class="dropdown oper">
							<a class="dropdown-toggle" id="drop4" role="button" data-toggle="dropdown" href="#">操作<b class="caret"></b></a>
               				<ul id="menu1" class="dropdown-menu" role="menu" aria-labelledby="drop4">
								<li><a tmplId="updateUser" role="button" class="btn btn-small btn-link vmt-dialog">编辑</a></li>
								{{if ${!isGroup}&& $item.canEdit() }}
									<li><a data-dn={{= dn }} data-cstnet-id="{{= cstnetId}}" class="btn btn-small btn-link removeCoreMailUser">删除邮箱账号</a></li>
									<li><a tmplId="changePassword" class="btn btn-small btn-link changePassword vmt-dialog">重置邮箱密码</a></li>
								{{/if}}
							</ul>
						</div>
					 {{/if}}
				</div>
				<div class="userDetail">
				<div class="control-group">
		    	    <label class="control-label">电子邮件：</label>
		       	 <div class="controls"><a href="mailto:{{= cstnetId}}">{{= cstnetId}}</a></div>
		    	</div>
 			<div class="control-group">
		           <label class="control-label">性别：</label>
		           <div class="controls">{{= $item.getSex() }}</div>
		     </div>
		   	 <div class="control-group">
		           <label class="control-label">所在部门：</label>
		           <div class="controls">{{= $item.getValues() }}</div>
		     </div>
			{{if ${isUpgraded}}}
			<div class="control-group">
		           <label class="control-label">办公室：</label>
		           <div class="controls">{{=  office?office:'无' }}</div>
		   	 </div>
			<div class="control-group">
		           <label class="control-label">办公室电话：</label>
		           <div class="controls">{{= officePhone?officePhone:'无' }}</div>
		     </div>
			<div class="control-group">
		           <label class="control-label">手机号码：</label>
		           <div class="controls">{{= telephone ?telephone:'无' }}</div>
		     </div>
			<div class="control-group">
		           <label class="control-label">职称/职务：</label>
		           <div class="controls">{{= title  ?title:'无'}}</div>
		     </div>
			{{/if}}
			{{if ${isAdmin} }}
				
				<div class="control-group">
		           <label class="control-label">是否在${isGroup?'群组':'组织'}中显示：</label>
		           <div class="controls">{{= $item.getVisible() }}</div>
				</div>
		     
				<div class="control-group">
		           <label class="control-label">权重：</label>
		           <div class="controls">{{= listRank }}</div>
		    	</div>
			
			 	<div class="control-group">
		           <label class="control-label">状态：</label>
		           <div class="controls">{{= $item.getStatus()}}
						{{if $item.showResend()}}	
							(<a href="#" dn="{{= dn}}" class="resend">重新发送激活邮件</a>)
						{{/if}}
					</div>
				</div>
				{{if $data.userFrom=='coreMail'||$item.canEdit()}}
					<div class="control-group">
		     	      <label class="control-label nopadding">邮箱账号状态：</label>
		   	   		  <div class="controls">{{= $item.getAccountStatus() }}</div>
		 		   	</div>
				{{/if}}
				{{if !${isUpgraded}}}
					<p class="update"><a class="btn btn-small btn-link upgrade" data-dn="{{= dn}}">升级可支持更多用户信息</a></p>
				{{/if}}
				<div class="control-group">
		           <label class="control-label">是否禁用科信：</label>
		           <div class="controls">{{= disableDchat?'是':'否' }}</div>
		     	</div>
			{{/if}}
				
			{{/if}}
		</div>
		</script>
		<script id="user_li" type="text/x-jquery-tmpl">
		<li id="{{= oid}}" dn="{{= $item.encode(dn)}}" umtId="{{= umtId}}" class="search_item">
			<a>
				<span class="name" title="{{= name}}">{{= name}} </span>
				<span class="mail" title="{{= cstnetId }}">{{= cstnetId }}</span>
				<span class="depart" title="{{= $item.getHighestName() }}">{{= $item.getHighestName() }}</span>
				<span class="status" title="">{{= $item.getStatus()}}</span>
				<span class="choice"><i class="{{= $item.getChoice()}}"></i></span>
			</a>	
		</li>
		</script>
</html>