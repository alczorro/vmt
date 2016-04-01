<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="WEB-INF/tld/vmt.tld" prefix="vmt"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<!DOCTYPE html>
<html>
	<head>
		<title>组织通讯录-短信账号管理</title>
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
			      	<li id="upgrade"><a href="gm?func=upgrade">升级</a></li>
			      	<li id="domainMappings"><a href="gm?func=domainMapping">域名映射</a></li>
			      	<li  class="active" id="smsManage"><a href="gm?func=smsManage">短信管理</a></li>
			    </ul>
		  	</div>
			</div>
			<div  class="container main setting">
			<table class="table  table-bordered setttingTable" id="ipfilter-c">
				<thead>
					<tr>
					<tr>
						<th colspan="5"><input class="btn btn-primary" type="button" id="addSmsGroup" value="添加短信账号"/></th>
					</tr> 
					<tr>
						<th class="mini"  style="width:15%">账号</th>
						<th style="width:20%">单位</th>
						<th style="width:30%">短信条数</th>
						<th style="width:25%">管理员</th>
						<th style="width:10%">操作</th>
					</tr>
				</thead>
				<tbody id="ipList">
				<c:forEach items="${groups}" var="g">
					<tr>
						<td>
						<div id="smsManageaccount">${g.account }</div>
						</td>
						<td>
						<div id="smsManagenumber">${g.groupName }</div>
						</td>
						<td>
						共充值${g.smsCount }条,已使用${g.smsUsed }条
						<a class="recharge label label-success" data-gid="${g.id }">充值</a>
						<a class="rechargeHistory label label-info" data-gid="${g.id }">历史</a>
						</td>
						<td>
							<ul class="selected" >
								<c:forEach items="${g.members }" var ="m">
									<c:if test="${m.admin }">
										<li data-m-id="${m.id }" data-m-group-id="${m.groupId }" data-m-user-name="${m.userName }">
											<a role="button" class="icon-remove" data-toggle="modal" ></a>
											<span><c:out value="${m.userName }"/></span>
										</li>
									</c:if>
								</c:forEach>
							</ul>
							<input style="width:150px;" type="text" data-group-id="${g.id}"  class="autoCompleteSearch" name="keyword" placeholder="请输入关键词搜索">	
						</td>
						<td>
						<a class="removeSmsGroup label label-info" data-gid="${g.id }">删除</a>
						</td>
					</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<jsp:include page="../bottom.jsp"></jsp:include>
		<div id="addSmsGroupModal" class="modal hide fade" tabindex="-1" role="dialog" data-backdrop='true' aria-hidden="true">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
			<h4 id="title">添加短信账号</h4>
		</div>
		<form class="form-horizontal" id="addSmsGroupForm" style="margin-bottom:0px;">
		<div class="modal-body control-group">
			
			<label class="control-label" for="account">账号：</label>
			<div class="controls">
				<input type="text"  style="margin-bottom:10px;" placeholder="请填写账号" name="account" id="account"/ >
				<span id="account_error"></span>
			</div>
			
			<label class="control-label" for="groupName">单位：</label>
			<div class="controls">
				<input type="text"   style="margin-bottom:10px;" placeholder="请填写用户所属单位" name="groupName" id="groupName"/>
				<span id="groupName_error"></span>
			</div>
			
			<label class="control-label" for="smsCount">短信条数：</label>
			<div class="controls">
				<input type="text"  style="margin-bottom:10px;"  placeholder="请填写初始化短信数量" value="1000" name="smsCount" id="smsCount"/>
				<span id="smsCount_error"></span>
			</div>
			
		</div>
		<div class="modal-footer">
			<span id="submit_error_place"></span>
			<input type="submit" class="btn btn-primary"  id="saveAddSmsGroup" value="保存"/>
			<a class="btn" id="no" data-dismiss="modal" aria-hidden="true">取消</a>
		</div>
		</form>
	</div>
	<div id="rechargeDialog" class="modal hide fade" tabindex="-1" role="dialog" data-backdrop='true' aria-hidden="true">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
			<h4 id="title">短信充值</h4>
		</div>
		<form id="rechargeForm">
		<div class="modal-body">
			<p id="content">
			<label for="rechargeNum">请输入要充值的短信数量</label>
			<input placeholder="请输入要充值的短信数量" type="text" name="rechargeNum" id="rechargeNum" value="1000"/>
			<span id="rechargeNum_error"></span>
			</p>
		</div>
		<div class="modal-footer">
			<input type="submit" class="btn btn-primary"  id="rechargeBtn" value="充值"/>
			<a class="btn"  data-dismiss="modal" aria-hidden="true">取消</a>
		</div>
		</form>
	</div>
	<div id="rechargeHistoryDialog" class="modal hide fade" tabindex="-1" role="dialog" data-backdrop='true' aria-hidden="true">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
			<h4 id="title">充值记录</h4>
		</div>
		<form id="rechargeForm">
		<div class="modal-body">
			<p id="content">
				<ul id="rechargeContent">
				</ul>
		</div>
		<div class="modal-footer">
			<a class="btn"  data-dismiss="modal" aria-hidden="true">取消</a>
		</div>
		</form>
	</div>
	</body>
	<script type="text/javascript">
		$(document).ready(function(){
			$('.removeSmsGroup').on('click',function(){
				var $self=$(this); 
				if(confirm("确认删除该账号吗?删除无法恢复")){
					$.get('${context}/user/sms/'+$(this).data('gid')+"/removeGroup").done(function(result){
						if(result.flag){
							$self.closest('tr').fadeOut(1000,function(){
								$(this).remove();
							});
						}
					});
				}
			});
			$('.rechargeHistory').on('click',function(){
				$('#rechargeHistoryDialog').modal('show');
				$.get('${context}/user/sms/'+$(this).data('gid')+"/getLog").done(function(result){
					$('#rechargeContent').html($('#smsLogTmpl').tmpl(result.data,{
						getChargedTime:function(c){
							var now = new Date(c); 
							var nowStr = now.formatVmtDate("yyyy-MM-dd hh:mm:ss"); 
							return nowStr;
						}
					}));
				});
			});
			$('#rechargeForm').validate({
				submitHandler:function(form){
					var gid=$(form).data('gid'); 
					$.post('${context}/user/sms/'+gid+'/recharge',{plus:$('#rechargeNum').val()}).done(function(result){
						if(result.flag){
							$('#rechargeDialog').modal('hide').delay(1000,function(){
								window.location.reload();
							});
						}else{
							alert(result.desc);
						}
					});
				},
				rules:{
					rechargeNum:{required:true,digits:true,number:true,range:[1,5000000]}
				},
				messages:{
					rechargeNum:{required:'请输入新增短信条数',digits:'请输入正整数',number:'请输入数字',range:'允许范围为1到5000000'}
				},
				errorPlacement: function(error, element){
					var sub="_error";
					var errorPlaceId="#"+$(element).attr("name")+sub;
					$(errorPlaceId).html("");
					error.appendTo($(errorPlaceId));
				}
			});
			$('.recharge').on('click',function(){
				$('#rechargeDialog').modal('show');
				$('#rechargeForm').data('gid',$(this).data('gid')).get(0).reset();
				
			});
			$('input.autoCompleteSearch').autocomplete('${context}/user/sms/searchMember',
		       {
		  		width:300,
				parse:function(data){
						return $.map(data, function(item) {
							return {
								data : item,
								result : item.userCstnetId,
								value:item.userName
							};
						});
				},
				formatItem:function(row, i, max) {
    				return  row.userName + " [" + row.userCstnetId+"]";
 				},
				formatMatch:function(row, i, max) {
    				return row.userName + " " + row.userCstnetId;
 				},
				formatResult: function(row) {
    				return row.userName;
 				}
			}).result(function(event, data, formatted){
				var gid=$(this).data('groupId');
				var $ul=$(this).prev();
				$.post("${context}/user/sms/"+gid+"/addSmsAdmin",{indexId:data.id} ).done(function(result){
					if(result.flag){
						$('#adminTmpl').tmpl(result.data).appendTo($ul);
					}else{
						alert(result.desc);
					}
				});
				$(this).val("");
				
			});;
			$('.icon-remove').live('click',function(){
				var data=$(this).closest('li').data();
				var $self=$(this);
				if(confirm("确定删除管理员["+data.mUserName+"]?,删除后不可恢复")){
					$.post("${context}/user/sms/removeAdmin/"+data.mId,{gid:data.mGroupId}).done(function(flag){
						if(!flag.flag){
							alert(flag.desc)
						}else{
							$self.parent().remove();
						}
					});
				}
			});
			$('#addSmsGroup').on('click',function(){
				$('#addSmsGroupModal').modal('show');
				$('#addSmsGroupForm').get(0).reset();
				$('span[id$=error]').empty();
			});
			
			$('#addSmsGroupForm').validate({
				submitHandler:function(){
					var $form=$('#addSmsGroupForm');
					$.post('${context}/user/sms/addSmsGroup',$form.serialize()).done(function(result){
						if(result.flag){
							$("#addSmsGroupModal").modal('hide');
							window.location.reload();
						}else{
							alert(result.desc);
						}
					});
				},
				rules:{
					account:{required:true,
						remote:{
						 type: "POST",
							 url: '${context}/user/sms/isAccountUsed'
						 }
					},
					groupName:{required:true},
					smsCount:{required:true,digits:true,number:true,range:[1,10000000]}
				},
				messages:{
					account:{required:"请填写账号",remote:"账号已存在"},
					groupName:{required:'请填写用户所属单位'},
					smsCount:{required:'请输入初始化短信条数',digits:'请输入正整数',number:'请输入数字',range:'短信条数超出范围'}
				},
				errorPlacement: function(error, element){
					var sub="_error";
					var errorPlaceId="#"+$(element).attr("name")+sub;
					$(errorPlaceId).html("");
					error.appendTo($(errorPlaceId));
				}
			});

		});
		</script>
		<script id="adminTmpl" type="text/x-jquery-tmpl">
		<li data-m-id="{{= id }}" data-m-group-id="{{= groupId }}" data-m-user-name="{{= userName }}">
			<a role="button" class="icon-remove" data-toggle="modal" ></a>
			<span>{{= userName }}</span>
		</li>
		</script>
		<script id="smsLogTmpl" type="text/x-jquery-tmpl">
			<li >管理员[<b>{{= whoCharged}}</b>],于<b>{{= $item.getChargedTime(chargedTime) }}</b>充值<b>{{= plus}}</b>条短信,剩余<b>{{= last}}</b>条</li>
			<li>{{= email}}</li>
		</script>
</html>