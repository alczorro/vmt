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
		<f:script src= "../../resource/thirdparty/fileuploader/fileuploader.js" />
		<f:css href="../../resource/thirdparty/fileuploader/fileuploader.css"/>
	</head>
	<body class="vmt">
		<jsp:include page="../banner.jsp"/>
		<div class="container main">
			<h3 class="batch">
				批量更新：${node.name}
				<a style="font-size:10px" href="${context }/user/setting?view=batch">返回</a>
				<a href="export?dn=<vmt:encode value="${node.dn }"/>&func=update&name=<vmt:encode value="${node.name }"/>" class="btn btn-small btn-success">批量导出</a>
			</h3>
			<div class="batch">
				<p style="color:gray">
				请直接使用批量导出的Excel修改后进行批量更新
				</p>
				<div id="fileUploader" style="display:inline" class="d-large-btn maintain">
					<div class="qq-uploader">
						<div class="qq-upload-button">
							<input type="file" multiple="multiple" name="files" style="cursor:pointer;">
						</div>
						<ul class="qq-upload-list fileList"></ul>
					</div>
				</div>
				
				
				<p class="filter">
					<a id="all" class="btn btn-small"><i></i><span>全部</span></a>
					<a id="add" class="btn btn-small"><i class="icon-batchAdd"></i><span>新增数据</span></a>
					<a id="update" class="btn btn-small"><i class="icon-batchUpdate"></i><span>有变更的数据</span></a>
					<a id="undo" class="btn btn-small"><i class="icon-batchNo"></i><span>无变更的数据</span></a>
					<a id="exception" class="btn btn-small"><i class="icon-batchError"></i><span>异常数据</span></a>
				</p>
				<span id="batch_desc">
					<div>模板字段说明:</div>
					<img width=500 height=800 src="${context }/resource/images/desc_update.png"/>
				</span>
			</div>
			
			<div class="cont">
				<table style="display:none;white-space: nowrap;" class="table table-bordered setttingTable" id="dataTable" >
					<thead>
						<tr>
							<th class="check"><input type="checkbox" id="checkAll"/></th>
							<th class="result">结果</th>
							<th class="email">邮箱</th>
							<th class="name">姓名</th>
							<th class="org">部门</th>
							<th class="sex">性别</th>
							<th class="office">办公室</th>
							<th class="tel">办公室电话</th>
							<th class="phone">手机号码</th>
							<th class="title">职称</th>
							<th class="weight">权重</th>
							<th class="visible">是否可见</th>
							<th class="visible">是否禁用科信</th>
							<th class="">邮箱账号状态</th>
							<th>邮箱过期时间</th>
							<th>自定义1</th>
							<th>自定义2</th>
						</tr>
					</thead>
					<tbody id="resolveResult">
					</tbody>
				</table>
			</div>
			<p style="padding:20px; text-align:center;">
				<input style="display:none;" type="button" id="saveBatch" class="btn btn-large btn-primary" value="批量更新"/>
				<br>
				<span id="hasAdd" style="display:none"><b>提示</b>:该操作仅针对系统内已有成员进行批量修改。以上标记为<i class="icon-batchAdd"></i>的用户为新用户，您可以直接【批量添加】这些成员。
				</span>
			</p>
		</div>
		<div id="process" class="modal hide fade" tabindex="-1" role="dialog" data-backdrop='true' aria-hidden="true">
		<div class="modal-header">
			<!-- <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button> -->
			<h4 id="title">执行进度</h4>
		</div>
		<div class="modal-body">
			<p id="content">
				<div class="progress progress-striped active">
				  <div id="progress-bar" class="bar" style="width: 0%;"></div>
				</div>
				<ul id="taskList" style="height:200px;overflow:auto">
				</ul>
			</p>
		</div>
		<div class="modal-footer">
			<a class="btn btn-primary" id="start">开始</a>
			<a class="btn" data-dismiss="modal" aria-hidden="true" id="no">关闭</a>
		</div>
	</div>
	<jsp:include page="../bottom.jsp"></jsp:include>
	</body>
	<script type="text/javascript">  
		$(document).ready(function(){
			vmt.checkAllBox('checkAll','checkItem');
			var params=[];
			$('#all').on('click',function(){
				$('#dataTable tbody tr').show();
			});
			$('#exception').on('click',function(){
				$('#dataTable tbody tr').hide();
				$('#dataTable tbody tr i.icon-batchError').parent().parent().show();
			});
			$('#undo').on('click',function(){
				$('#dataTable tbody tr').hide();
				$('#dataTable tbody tr i.icon-batchNo').parent().parent().show();
			});
			$('#update').on('click',function(){
				$('#dataTable tbody tr').hide();
				$('#dataTable tbody tr i.icon-batchUpdate').parent().parent().show();
			});
			$('#add').on('click',function(){
				$('#dataTable tbody tr').hide();
				$('#dataTable tbody tr i.icon-batchAdd').parent().parent().show();
			});
			$('#start').on('click',function(){
				$(this).hide();
				$('#saveBatch').hide();
				doTask(0,params);
			});
			$('#saveBatch').on('click',function(){
				var $item=$('#resolveResult>tr>td>.checkItem:checked');
				if($item.length==0){
					alert('请选择数据');
					return
				}
				var index=0;
				params=[];
				$item.each(function(i,n){
					if($(n).attr("checked")){
						++index;
						var data=$(n).parents('tr').data('tmplItem').data;
						var param={};
						param.index=i+1;
						param.dn=data.dn;
						if(data.needAdd){
							param.type='insert'; 
						}else if(data.changed){  
							param.type='update';
						}
						param.teamDn='${node.dn}';
						for(var i=0;i<data.cells.length;i++){
							var cell=data.cells[i];
							param[cell.key]=cell.after;
							param['before'+cell.key]=cell.before;
						}
						params.push(param);
					}
				});
				$('#process').modal('show');
				$('#progress-bar').empty().css('width','0%');
				$('#taskList').empty();
				
			});
			function doTask(index,params){
				if(params.length<=index){
					$('#progress-bar').html("任务完成["+params.length+'/'+params.length+"]").css('width',"100%");
					return;
				}
				$('#progress-bar').css('width',(index*100/(params.length-1))+"%").html('['+(index+1)+'/'+params.length+']');
				var param=params[index++];
				param.index=index;
				param.all=params.length;
				$.ajax({
					 url : 'doTask?func=update',
					 type : "POST",
					 data:param,
					 success : function(flag){
						if(flag){
							var $taskList=$('#taskList');
							$taskList.append('<li class="taskItem icon-batchOk" id="task_'+index+'">'+index+'.更新用户：'+param.cstnetId+'</li>')
							.scrollTop(100000);;
							doTask(index,params);
						}
					}
				});
			}
			new qq.FileUploader({
				element : document.getElementById('fileUploader'),
				action : 'import?dn=<vmt:encode value="${node.dn}"/>&func=update',				sizeLimit : 1*1024 * 1024,
				allowedExtensions:['xls', 'xlsx'],
				onComplete : function(id, fileName, data) {
					if(data.success){
						$('#batch_desc').hide();
						$('#checkAll').attr("checked","checked");
						$('#resolveResult').html($('#batchRowsTmpl').tmpl(data.rows,{
							getErrorMsg:function(status){
								switch(status){
									case 'required':return '该字段不允许为空';
									case 'formatError':return '格式有误';
									case 'fatalError':return '此邮箱不可用';
									case null:return '';
								}
								return status;
							},
							getResult:function(){  
								with(this.data){
									if(needAdd&&canDo){
										return 'icon-batchAdd';
									}else if(!canDo){
										return 'icon-batchError';
									}else if(changed){
										return 'icon-batchUpdate';
									}else{
										return 'icon-batchNo';
									}
								}
							}
						}));
						if($('#resolveResult').find("i.icon-batchAdd").length>1){
							$('#hasAdd').show();
						}
						$('#saveBatch').show();
						$('#start').show();
						$('#dataTable').show();
						
					}else{
						alert('解析失败');
					}
					
				},
				messages:{
		        	typeError:"请上传xls,xlsx格式",
		        	emptyError:"请不要上传空文件",
		        	sizeError:"大小超过1M限制"
		        },
		        showMessage: function(message){
		        	alert(message);
		        },
		        onProgress: function(id, fileName, loaded, total){
		        	//$('#uploadText').html("<fmt:message key='configPhoto.uploadFile'/>("+Math.round((loaded/total)*100)+"%)");
		        },
		        multiple:false,
		        dragable:false
			});
		});
	</script>
	<script id="batchRowsTmpl" type="text/x-jquery-tmpl">
		<tr>
			<td >
				{{if canDo&&changed&&!needAdd}}
					<input type="checkbox" checked="checked" class="checkItem"/>
				{{/if}}
			</td>
			<td>
				<i class="{{= $item.getResult()}}"/>
			</td>
			{{each cells}}
				{{if $value.changed&&!needAdd}}
					<td >
						<b style="color:red">{{= $item.getErrorMsg($value.status)}}</b>
						<b>
							{{= $value.before }} => {{= $value.after }}
						</b>
					</td>
				{{else}}  
					<td>
						<b style="color:red">{{= $item.getErrorMsg($value.status)}}</b>
						{{= $value.after }}
					</td>
				{{/if}}
			{{/each}}
		</tr>
</script>
</html>
