<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
	<div id="cosDialog" class="modal hide fade" tabindex="-1" role="dialog" data-backdrop='true' aria-hidden="true">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
			<h4 id="title"></h4>
		</div>
		<div class="modal-body">
			<p id="content"></p>
		</div>
		<div class="modal-footer">
			<a class="btn btn-primary"  id="yes"></a>
			<a class="btn" id="no"></a>
			<a class="btn" id="hideThis" style="display:none">确认</a>
		</div>
	</div>

<script>
$(document).ready(function(){
	vmt.confirm=function(option){
		$('#cosDialog').modal('show');
		var defaultOption={
			'afterHide':true,
			'yes':'确认',
			'no':'取消',
			'hideThis':'取消',
			'title':'来自组织通讯录的消息',
			'content':'Are you Sure?',
			'callback':function(flag){
				alert('you click '+flag+',please override the callback function!');
			}
		};
		
		$('#yes').unbind().bind('click',function(){
			defaultOption.callback(true);
			if(defaultOption.afterHide){
				$('#cosDialog').modal('hide');
			}
		});
		$('#no').show().unbind().bind('click',function(){
			defaultOption.callback(false);
			if(defaultOption.afterHide){
				$('#cosDialog').modal('hide');
			}
		}).show();
		$('#hideThis').hide();
		$.extend(defaultOption,option);
		if(!defaultOption.yes){
			$('#yes').hide();
		}else{
			$('#yes').show();
		}
		for(id in defaultOption){
			if(typeof defaultOption[id]=='string'){
				$('#'+id).html(defaultOption[id]);
			}
		}
	};
});

</script>
