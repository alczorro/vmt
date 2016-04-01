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
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<jsp:include page="../header.jsp"/>
		<f:script src="../resource/thirdparty/autocomplate/jquery.autocomplete.js"/>
		<f:css href="../resource/thirdparty/fileuploader/fileuploader.css" />
		<f:script  src="../resource/thirdparty/bootstrap-editable/js/bootstrap-editable.js"/>
		<f:css  href="../resource/thirdparty/bootstrap-editable/css/bootstrap-editable.css" />
		<f:script src="../resource/thirdparty/respond.src.js"/>
	</head>
	<body class="vmt">
		<jsp:include page="../banner.jsp"/>
		<div class="container main">
			<p class="alert alert-success" id="msg_success" style="display:none">保存成功!</p>
			<div class="row-fluid">
			  	<jsp:include page="left.jsp"></jsp:include>
			  	<div class="span6 staff">
			  		<div class="stafflist">
			  			<div class="form-search search-staff">
				  			<input type="text" id="keyword" placeholder="按姓名、邮箱搜索">
				  			<a class="btn btn-primary" id="search_button">搜索</a>
				  			<c:if test="${!isGroup}">
					  			<a class="btn userView list " title="层级视图" alt="层级视图"  href="index2?dn=<vmt:encode value="${dn }"/>"></a>
					  			<a class="btn userView active" title="列表视图" alt="列表视图" href="index?dn=<vmt:encode value="${dn }"/>"></a>
				  			</c:if>
				  		</div>
						<div id="slider" style="-moz-user-select:none;" unscelctable="on" onselectstart="javascript:return false;">
							<p class="memberTitleAbc" >
								<span class="name">姓名</span>
								<span class="mail">邮箱</span>
								<span class="depart">${isGroup?'群组':'部门'}</span>
								<span class="status">状态</span>
								<c:if test="${!isGroup }">
									<span class="status">邮箱状态</span>
								</c:if>
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
									<%for(int i=0;i<26;i++){ 
										char lowLetter=(char)(i+97);
										char upperLetter=(char)(i+65);
									%>
									<li style="display:none" id="<%=lowLetter%>">
										<a name="<%=lowLetter%>" class="title"><%=upperLetter %></a>
										<ul id="<%=lowLetter%>_result">
										</ul>
									</li>
									<%}%>
									<li style="display:none" id="other">
										<a name="other" class="title">#</a>
										<ul id="other_result">
										</ul>
									</li>
									<li style="display:none" id="search">
										<ul id="search_result">
										</ul>
									</li>
								</ul>
							</div>
						</div>
						<p class="msg" style="display:none" id="noResultMsg">没有找到您要找的成员</p>
				  	</div>
				</div>
			  	<div class="span3 staff-detail form-horizontal"  id="selected_detail">
				  	<div class="bookmark">
						<i class="${isGroup?'icon-bullhorn':'icon-home' }"></i>
						<h2 >${teamInfo.name }</h2> 
					</div>
					<c:if test="${isGroup }">
						<div class="control-group">
				           <label class="control-label">群组ID：</label>
				           <div class="controls">${currTeam.symbol }</div>
				     	</div>
					</c:if>
					<div class="control-group">
			           <label class="control-label">人数：</label>
			           <div class="controls">共${teamInfo.count }人</div>
			     	</div>
			     	<div class="control-group">
			           <label class="control-label">管理员：
			           <c:if test="${isAdmin }">
			           <a class="management" target="_blank" href="${context }/user/setting">管理</a>
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
				          	 <div class="controls">
				          	 
				          	 </div>
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
				<div style="display:none" class="span3 staff-batch" id="selected_item">
					<div class="bookmark">
						<h3>
							已选成员
							<a class="btn btn-mini" id="removeSelected">全部移除</a> 
							<span class="staff-num"><span id="selected_count"></span>个成员</span>
						</h3>
					</div>
					<ul class="selected-staff">
					</ul>
				</div>
			</div>
			<jsp:include page="bottomButtons.jsp"></jsp:include>
		</div>
		<jsp:include page="../bottom.jsp"></jsp:include>
		<jsp:include page="../confirm.jsp"/>
	</body>
	<script type="text/javascript">
			var scopeDic;
			vmt.from='index';
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
				$(document).data('lastDepart','${dn}');
				$(document).data('domain','${domain}');
				//旁边的导航自适应高度
				var setHeight = setTimeout(function(){
					var slideWidth = $(".slider-content").outerWidth()-20;
					$("p.memberTitleAbc,ul.memberListAbc,ul.memberListAbc ul").css({"width":slideWidth});
				},100);
				var setDynamicHeight = setInterval(function(){
					var staffHeight = window.screen.availHeight * 0.58;
					$(".column ul").css({"height":staffHeight});
				},500);
				
				//初始化联系人列表
				$('#slider').sliderNav();
				var searchScope='';
				if('${dn}'!='all'){
					searchScope='<vmt:encode value="${dn}"/>';
					$("a[tmplid=createSection]").show();
					$("a[tmplid=addMember]").show();
					$("a[tmplid=registCoreMail]").show();
				}
				scopeDic=new Map();
				$(".sub-team li a").each(function(i,n){
					 var dn=$(this).attr("dn");
					 if(dn!=null){
						 if('${dn}'=='all'){
					 		searchScope+=((i==0?"":',')+dn);
						 }
					 	scopeDic.put(dn,$(this).html());
					 }
				});
			
				var selectedItem=new Map();
				vmt.selectedItem=selectedItem;
				$(document).data("selected",selectedItem);
				var allConfig={scope:"${empty dn?'all':dn}",count:'${currentCount}',selected:selectedItem};
				if('${count}'=='0'){
					$('.stafflist').html("<p class='noTeam'><p class='noTeam'>没有团队，<a tmplId='createTeam' role='button' class='count add vmt-dialog' >现在创建</a></p></p>");
				}else{
					var loader=new Loader(allConfig);
					loader.init();
				}
				
				
				//bind left items click
				$(document).data("currentDN",'${dn}');
				//bind search item click 
				$("#selected_item .icon-remove").live("click",function(){
					var $li=$(this).parent().parent();
					$li.remove();
					var id=$li.attr("oid");
					keyControl.remove(id);
					$("#selected_count").html(selectedItem.count);
					if(selectedItem.count==0){
						$('#selected_item').hide();
					}
				});
				
				var multiPressConfig={
						selector:".search_item",
						isSelected:function(li){
							return $(li).find("i").attr("class")=="icon-ok";
						},
						addClass:function(li){
							$(li).find("i").attr("class","icon-ok");
							var data=($(li).data().tmplItem.data);
							var item={
									name:data.name,
									email:data.cstnetId,
									oid:data.oid,
									umtId:data.umtId,
									dn:data.dn,
									currentDisplay:data.currentDisplay,
									status:data.status,
									visible:data.visible
							};
							$(li).find("span.choice").append("<span class='cancle'>取消选择</span>");
							selectedItem.put(item.oid,item);
						},
						removeClass:function(li){
							$(li).find("i").attr("class","icon-plus0");
							$(li).find("span.choice .cancle").remove();
							selectedItem.remove($(li).attr("id"));
						},
						getCount:function(){
							return selectedItem.count;
						},
						complete:function(){
							if(selectedItem.count==1){
								showDetail();
							}else{
								drawSelected();							}
						},
						multiable:'${dn!="all"}'=='true'  
				};
				function judiceBtnDisplay(){
					if(selectedItem.count!=0){
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
				var keyControl=new KeyMultiPress(multiPressConfig);
				$('.search_item span.choice span.cancle').live("click",function(event){
					multiPressConfig.removeClass($(this).parent().parent().parent());
					multiPressConfig.complete();
					event.stopPropagation();
				});
				//全部清除
				$("#removeSelected").click(function(){
					$(this).parent().next("ul.selected-staff").children().remove();
					selectedItem.removeAll();
					$('#selected_item').hide();
					$("#selected_count").html(selectedItem.count);
					keyControl.clearAll();
					judiceBtnDisplay();
				});
				function showDetailCallback(data){
					var $dest=$("#selected_detail");
					$("#detail_user").tmpl(data,{getValues:function(){
						var displayName=decodeURIComponent(this.data.currentDisplay);
						return displayName=displayName.replace(/,/gm,"&nbsp;/&nbsp;");
					},
					canEdit:function(){
						return vmt.canEditCoreMail(this.data.cstnetId);
					},
					getStatus:function(){
						switch(this.data.status){
						case 'true':return '已确认';
						case 'false':return '待确认';
						case 'refuse':return '已拒绝';
						case 'apply':return '申请加入';
						}
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
					getSex:function(){
						switch(this.data.sex){
						case 'male':return '男';
						case 'female':return '女';
						default:return '未知';
						};
					},
					getVisible:function(){
						return this.data.visible?'是':'否';
					},
					showResend:function(){
						var status=this.data.status;
						return (status=='false'||status=='refuse');
					}
					}).appendTo($dest);
					var $editableDest=$('#selected_detail h2');
					var dn=new DN($editableDest.attr("data-pk"));
					var umtId=dn.getValue(dn.prefix(0));
					var oid=$editableDest.attr("oid");
					var newValue='';
					if('${loginUser.userInfo.umtId}'==umtId||'${canAdd}'=='true'){
						$editableDest.editable({
							type:'text',
							url:'rename',
							params: function(params) {
							    var data = {
							    		dn:params.pk,
							    		name:params.value
							    };
							    newValue=params.value;
							    return data;
							  },
							  success :function(){
									var hColumnsSpan=$('#'+oid);
									hColumnsSpan.find('span.name').html(newValue);
							  },
							  validate: function(value) {
								    if($.trim(value) == '') {
								        return '不允许为空';
								    }
								    var type=$(this).data('entryType');
									  if(type=='folder'&&!nodeIdOK(value)){
											 return "请不要输入特殊字符";
									}
						}
						});
					}
					judiceBtnDisplay();
				}
				function showDetail(){
					$('#selected_item').hide();
					var $dest=$("#selected_detail");
					$dest.show();
					$dest.html("");
					var dn=selectedItem.asArray()[0].dn;
					$.post('show',{"dn":dn}).done(showDetailCallback);  
					
				}
				function drawSelected(){
					var $dest=$('#selected_item');
					var $detail=$('#selected_detail');
					if(selectedItem.count==0){
						$dest.hide();
						$detail.hide();
						judiceBtnDisplay();
						return;
					}
					$("a[tmplid=moveTo]").show();
					$("a[tmplid=removeTeam]").show();
					$dest.show();
					$detail.hide();
					
					var $renderDest=$dest.children(".selected-staff");
					$renderDest.html("");
					var index=0;
					$("#select_li").tmpl(selectedItem.asArray(),{getEvenClass:function(){
						index++;
						if(index%2!=0){
							return "even";
						}
						return "";
					}}).appendTo($dest.children(".selected-staff"));
					$('#selected_count').html(selectedItem.count);
					judiceBtnDisplay();
				}
				$("a[tmplid=removeTeam]").removeAttr("default");
				$("a[tmplid=createSection]").hide();
				$(".resend").live('click',function(){
					var dn=($(this).attr("dn"));
					$.post('resend',{'dn':dn}).done(function(data){
						$('#msg_success').html("重新发送确认邮件成功！");
						$('#msg_success').show();
						$('#msg_success').hide(3000);
					});
				});
				$('.upgrade').live('click',function(){
					var dn=$(this).data('dn');
					window.location.href="upgrade?dn="+encodeURIComponent(dn);
				});
				$('.removeCoreMailUser').live('click',function(){
					if(confirm("您确定删除邮箱账号吗？ 此操作不可恢复")){
						$.post('removeCoreMail',$(this).data()).done(function(){
							$('li.search_item i.icon-ok').parent().parent().parent().remove();
							selectedItem.removeAll();
							drawSelected();
						});
					};
				});
			});
		</script>
		<script id="user_li" type="text/x-jquery-tmpl">
		<li id="{{= oid}}" dn="{{= $item.encode(dn)}}" umtId="{{= umtId}}" class="search_item">
			<a>
				<span class="name" title="{{= name}}">{{= name}}</span>
				<span class="mail" title="{{= cstnetId }}">{{= cstnetId }}</span>
				<span class="depart" title="{{= $item.getHighestName() }}">{{= $item.getHighestName() }}</span>
				<span class="status" title="">{{= $item.getStatus()}}</span>
				{{if ${!isGroup} }}  
					<span class="status" title="">{{= $item.getCoreMailStatus() }}</span>
				{{/if}}
				<span class="choice"><i class="{{= $item.getChoice()}}"></i></span>
			</a>	
		</li>
		</script>
		<script id="select_li" type="text/x-jquery-tmpl">
		<li oid="{{= oid}}" class="{{= $item.getEvenClass()}}">
			<a class="closeMe"><i class="icon-remove"></i></a>
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
		           <div class="controls">{{= count }}</div>
		     	</div>
				<div class="control-group">
		           <label class="control-label">部门位置：</label>
		           <div class="controls">{{= $item.getValues() }}</div>
		     	</div>
				<div class="control-group">
		           <label class="control-label">权重：</label>
		           <div class="controls">{{= listRank }}</div>
		     	</div>
			{{else}}
				<div class="bookmark">
					<i class="icon-user"></i>
					<h2 data-entry-type="link" oid="{{= oid}}" data-pk="{{= dn}}">{{= name}}</h2>
					{{if ${isCurrAdmin} }}					
						<div class="dropdown oper">
							<a class="dropdown-toggle" id="drop4" role="button" data-toggle="dropdown" href="#">操作<b class="caret"></b></a>
               				<ul id="menu1" class="dropdown-menu" role="menu" aria-labelledby="drop4">
								<li><a tmplId="updateUser" role="button" class="btn btn-small btn-link vmt-dialog">编辑</a></li>
								{{if ${!isGroup}  && $item.canEdit() }}
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
		           <label class="control-label">所在${isGroup?'群组':'部门'}：</label>
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
		     	      <label class="control-label">邮箱账号状态：</label>
		   	   		  <div class="controls">{{= $item.getAccountStatus() }}</div>
		 		   	</div>
				{{/if}}
				<div class="control-group">
		           <label class="control-label">是否禁用科信：</label>
		           <div class="controls">{{= disableDchat?'是':'否' }}</div>
		     	</div>
				{{if !${isUpgraded} }}
					<p class="update"><a class="btn btn-small btn-link upgrade" data-dn="{{= dn}}">升级可支持更多用户信息</a></p>
				{{/if}}
			{{/if}}
			{{/if}}
			</div>
		</script>
		<script>
			$(".memberTitleAbc span.choice a.helpMe").live("click",function(e){
				$(".helpMeContent").toggle();
				e.stopPropagation();
			});
			$("*:not(.memberTitleAbc span.choice a.helpMe)").live("click",function(){
				$(".helpMeContent").hide();
			});
		</script>
</html>