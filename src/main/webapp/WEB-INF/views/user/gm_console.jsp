<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="WEB-INF/tld/vmt.tld" prefix="vmt"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<!DOCTYPE html>
<html>
<head>
<style>
div.error{
	color:red;
	margin:15px
}
div.line{
	margin:15px
}
</style>
<title>控制台</title>
<%request.setAttribute("context",request.getContextPath()); %>
<f:script  src="${context }/resource/js/jquery-1.7.2.min.js"/>
<f:script  src="${context }/resource/js/vmt-util.js"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body style="background:black;color:gray">
<h2>
Hello GM Console (Beta)
</h2>
<p id="console">
</p>
<p id="loading" style="display:none">
	<jsp:include page="../loading.jsp"/>
</p>
<label >[vmt-gm-console]:&nbsp;&nbsp;</label>
<input style="margin-top:20px;background:black;color:white;border:0;outline:none;height:50px;font-size:larger;width:80%" id="command"/>
</body>
<script>
$(document).ready(function(){
	var flag=false; 
	var $console=$('#console');
	var $body=$('body');
	var $command=$('#command');
	var getLogTask;
	var history=[];
	var historyIndex=0;
	function push(cmd){
		history.push(cmd);
		historyIndex++;
	}
	function pop(cmd){
		history.pop();
		historyIndex--;
	}
	$command[0].focus();
	$(document).on('click',function(){
		$command[0].focus();
	});
	
	function loading(f){
		flag=f;
		var $loading=$('.vmt-load');
		if(f){
			$loading.addClass('loading');
		}else{
			$loading.removeClass('loading');
		}
	}
	function getMsg(url){
		if(flag){
			alert('有任务正在执行,请稍后');
			return;
		}
		loading(true);
		try{
			$.get(url);
		}catch(e){
			var errorMsg=url+" may be timeout,that's ok";
			if(console||console.log){
				console.log(errorMsg);
			}else{
				alert(errorMsg);
			}
		}
		getLogTask=setInterval(function(){
			$.get('${context}/user/console/getLog').done(function(str){
				$(str).each(function(i,n){
					if(n.indexOf('~~~Over~~~')>-1){
						clearInterval(getLogTask);
						loading(false);
						$console.append('<div class="error">已完成</span><br>');   
					}else{
						$console.append('<div class="line">'+n+"</div>");
					}
					
				});
				if (navigator.userAgent.indexOf('Firefox') >= 0){
				    document.documentElement.scrollTop=$body.height();
				}else{
					$body.scrollTop($body.height());
				}
			});
		},500);
	}
	$command.enter(function(){
		var cmd=encodeURIComponent($.trim($(this).val()));
		if(cmd=='cls'){
			window.location.reload();
		}else if(cmd=='return'){
			window.location.href="${context}/";
		}else{
			getMsg("${context}/user/console/cmd?func="+cmd);
		}
		$(this).val('');
	});
});
</script>
</html>