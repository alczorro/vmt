<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="WEB-INF/tld/vmt.tld" prefix="vmt"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<!DOCTYPE html>
<html>
	<head>
		<title>组织通讯录-短信群发</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<jsp:include page="../header.jsp"/>
	</head>
	<body class="vmt">
		<jsp:include page="../banner.jsp"/>
			<div   class="container main setting">
			<table class="table  table-bordered setttingTable" id="ipfilter-c">
				<tbody id="ipList">
					<tr  style="border:none;">
					 <td   style="border:none;"><h3>短信群发</h3>
					 </td>
					</tr>
					<tr  style="height:500px">
						<td style="width:20%;border: none;border-top:1px solid #ddd;">
							<ul class="left-ul">
								<li class="active"><a class="left-item" href="${context }/user/enhanced/sms/${currGroup.id }">发送短信</a></li>
								<li><a class="left-item" href="${context }/user/enhanced/sms/${currGroup.id }?func=log">发送记录</a></li>
								<li><a class="left-item" href="${context }/user/enhanced/sms/${currGroup.id }?func=manage">统计管理</a></li>
							</ul>
						</td>
						<td id="addrecipient">
						<a id="addMember">从通讯录选择</a>
						<a id="typeMobile" style="margin-left:2em">输入手机号码</a>
						<div id="addMemberMsg" class="alert alert-warning" style="display:none"></div>
						<ul id="finalPeopleSelectedUl">
						</ul>
						</td>
						<td>
							<form id="sendSmsForm">
								短信内容:<span style="color:gray"> (每条短信不超过70个字)</span>
								<textarea id="smsContent" name="smsContent" rows="3" ></textarea>
								<button type="submit" style="display:block" class="btn btn-primary">批量发送</button>
								<span style="color:gray">直接从发送记录里查看发送情况</span>
							</form>
						</td> 
					</tr>
				 
				</tbody>
			</table>
		</div>
		<jsp:include page="../bottom.jsp"></jsp:include>
<div id="typeMobileDialog" class="modal hide fade" tabindex="-1" role="dialog" data-backdrop='true' aria-hidden="true">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
			<h4 id="title">输入手机号码</h4>
		</div>
		<div class="modal-body">
			<p id="content">
				<textarea id="multiMobiles" rows="10" style="width:80%" placeholder="请输入手机号码,用回车分隔"></textarea>
				<br>
				<span>一行一个手机号码</span>
			</p>
		</div>
		<div class="modal-footer">
			<a class="btn btn-primary" id="submitMultiMobileBtn">确认</a>
			<a class="btn" data-dismiss="modal" aria-hidden="true">取消</a>
		</div>
	</div>
<div id="addMemberDialog" class="modal hide fade" style="width:920px; left:40%" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div id="am_sub">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
			<h3 id="myModalLabel">添加收信人</h3>
		</div>
		<div class="modal-body" style="padding:0;">
			<div class="sub-main">
				<div class="row-fluid">
					<div class="span6 source-choose">
						<div id="team-c">
							<div class="form-search search-staff" style="margin-bottom:0px;">
								<select id="myTeams" style="width:50%"> 
								</select>
								<input style="width:45%;" type="text" id="searchMemberBox" class="autoCompleteSearch" name="keyword" placeholder="请输入关键词搜索">
								<a style="position: relative;top: 7px" id="addAll">添加所有</a>
							</div>
							<div id="am_result_view1">
								<ul id="am_select_result_1" unscelctable="on" onselectstart="javascript:return false;" class="selected-staff" style="overflow:auto;-moz-user-select:none;height:350px;" >
								</ul>
							</div>
						</div>
					</div>
					<div class="span6 staff-batch">
						<div id="selectedPeople" style="display:none">
							<div class="bookmark popup">
								<h3 class="popModal">
									可接收短信成员
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
				redrawSelected:function(){
					$('#am_selected').html($('#am-select-result-template').tmpl(smsContext.selectedPeople.asArray()));
					this.ControlSelected(); 
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
					if(ele){
						$(ele).attr('class','icon-plus').closest('li').removeClass('choice');
					}
					$('#msgSpan').show().html(msg).delay(2000).fadeOut();
				},
				appendToFinalSelected:function(peoples){
					function isMobileEquals(mobile){
						var result=false;
						$.each(smsContext.finalSelectPeople.asArray(),function(i,n){
							if(n.mobile==mobile){
								result=true;
								return false;
							}
						});
						return result; 
					}
					var needAdd=[];
					$.each(peoples,function(i,n){
						var isMobileContain=isMobileEquals(n.mobile);
						if(!n.umtId&&!isMobileContain){
							smsContext.finalSelectPeople.put("m-"+n.mobile,n);
							needAdd.push(n);
						}else if(!smsContext.finalSelectPeople.isExists(n.umtId)&&!isMobileContain){
							smsContext.finalSelectPeople.put(n.umtId,n);
							needAdd.push(n);
						}
					});
					$('#finalPeopleSelectedUl').append($('#final-people-select-li-tmpl').tmpl(needAdd));
					$('#addMemberMsg').html("已过滤重复").show().delay(2000).fadeOut(500);
				}
			};
			$('#searchMemberBox').keyup(function(e){
				if($.trim($(this).val())==''){
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
				if(!n.mobile){
					return false;
				}
				if(smsContext.selectedPeople.isExists(n.umtId)){
					return false;
				}
				smsContext.selectedPeople.put(n.umtId,n);
				return true;
			}
			$('#typeMobile').on('click',function(){
				$('#typeMobileDialog').modal('show');
				$('#multiMobiles').val('');
			});
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
					}
					var mobile=[];
					var cstnetIds=[];
					var name=[];
					var umtId=[];
					$.each(smsContext.finalSelectPeople.asArray(),function(i,n){
						mobile.push(n.mobile);
						cstnetIds.push(n.cstnetId);
						name.push(n.truename);
						umtId.push(n.umtId);
					});
					$.post('${context}/user/enhanced/sms/${currGroup.id}/sendSms',{'content':$('#smsContent').val(),'mobile':mobile,'cstnetId':cstnetIds,'name':name,'umtId':umtId})
					.done(function(data){
						if(data.flag){
							window.location.href="?func=log";
						}else{
							alert(data.desc);
						}
					}); 
				},
				rules:{
					smsContent:{required:true,maxlength:70}
				},
				messages:{
					smsContent:{required:'请输入短信内容',maxlength:"短信内容不能超过70个字"}
				}
			});
			$('#finalPeopleSelectedUl span.close').live('click',function(){
				$(this).closest('li').fadeOut(500);
				smsContext.finalSelectPeople.remove($(this).data('umtId'));
			});
			$('#addSmsMember').on('click',function(){
				smsUIControl.appendToFinalSelected(smsContext.selectedPeople.asArray());
			});
 			$('#am_removeAllSelected').on('click',function(){
 				smsContext.selectedPeople.removeAll();
 				smsContext.selectedDept.removeAll();
 				smsUIControl.ControlSelected();
 				$('#am_select_result_1 li').removeClass('choice').find("i.icon-ok").attr('class','icon-plus');
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
							smsUIControl.showMessageAndUnChecked($self, "此部门下没有可接收短信的人员,或者已被选择");
						}
						$('#am_selected').append($('#am-select-result-template').tmpl(addPeople));
						smsUIControl.ControlSelected();
						smsContext.selectedDept.put(data.dn,result);
					});
				}else{
					var n={dn:data.dn,umtId:data.umtId,cstnetId:data.email,truename:data.name,mobile:data.mobile};
					if(!addSelectPeople(n)){
						smsUIControl.showMessageAndUnChecked(this, "该成员不可接收短信");
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
				$(this).closest('li').fadeOut(500,function(){$(this).remove();smsUIControl.ControlSelected();});
			});
			$('#addMember').on('click',function(){
				$('#addMemberDialog').modal('show');
			});
			$('#addMemberDialog').on('show',function(){  
				if($('#myTeams option').size()==0){
					$.post("${context}/user/enhanced/sms/${currGroup.id}/getMyTeam").done(function(result){
						if(result.flag){
							$('#myTeams').html($('#optionTmpl').tmpl(result.data)).trigger('change');
						}else if(!result.data){
							alert("您不属于任何一个团队,无法群发短信");
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
			$('#needRmbB').addClass('active');
			//添加所有
			$('#addAll').on('click',function(){
				//搜索过
				if($('#searchMemberBox').val()!=''){
					var people=[];
					$('.searchResult').each(function(i,n){
						var data=$(n).data('tmplItem').data;
						if(addSelectPeople(data)){
							people.push(data);
						}
					});
					if(people.length==0){
						smsUIControl.showMessageAndUnChecked(null, "此部门下没有可接收短信的人员,或者已被选择");
						return;
					}
					$('#am_selected').append($('#am-select-result-template').tmpl(people));
					smsUIControl.ControlSelected();
				}
				//点击去后台迭代查询
				else{
					var dn=$('.breadCatchShit:last').data('dn');
					$.post('${context}/user/search/dn/tree',{dn:dn,from:'addMember',q:''}).done(function(result){
						var addPeople=[];
						$.each(result,function(i,n){
							if(!addSelectPeople(n)){
								return;
							}
							addPeople.push(n);
						});
						if(addPeople.length==0){
							smsUIControl.showMessageAndUnChecked(null, "此部门下没有可接收短信的人员,或者已被选择");
						}
						$('#am_selected').append($('#am-select-result-template').tmpl(addPeople));
						smsUIControl.ControlSelected();
					});
				}
			});
		});
		</script>
		<script id="optionTmpl" type="text/x-jquery-tmpl">
			<option value="{{= dn}}">{{= name}}</option>
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
		<h4>{{= truename }}</h4> <span style="color:#999">- {{= mobile }}</span>
		<i style="float:right" data-umt-id="{{= umtId}}" class="icon-remove"></i> 
	</li> 
	</script>
	<script id="final-people-select-li-tmpl" type="text/x-jquery-tmpl">
	<li class="finalReciver">{{= truename }}-{{= mobile }}<span data-umt-id="{{= umtId}}" class="close"style="margin-top:0px;">×</span></li>
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