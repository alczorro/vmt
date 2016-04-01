<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="WEB-INF/tld/vmt.tld" prefix="vmt"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<!DOCTYPE html>
<html>
	<head>
		<title>组织通讯录-加入群组</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<jsp:include page="../header.jsp"/>
	</head>
	<body class="vmt">
		<jsp:include page="../banner.jsp"/>
			<div class="container main  help">
				<h2 class="login-title">加入群组 </h2>
				<div class="searchGroup" style="padding-top: 1%">
					<h5>请输入群组名进行搜索</h5>
					<input  name='keyword' id="keyword" type="text" style="margin-bottom:0"/>
					<a id="search" class="btn btn-primary">搜索</a>
					<ul id='searchResult' class="searchResult">
					</ul>  
				</div>
			</div>
		<jsp:include page="../bottom.jsp"></jsp:include>
		<jsp:include page="../confirm.jsp"/>
	</body>
	<script type="text/javascript">
		$(document).ready(function(){
			$('#searchGroupB').addClass("active"); 
			//绑定回车事件
			$('#keyword').enter(function(){
				$('#search').click();
			});
			//绑定点击搜索按钮
			$('#search').click(function(){
				var keyword=$('#keyword').val();
				$.post('group',{'keyword':keyword}).done(function(data){
					$('#searchResult').html('');
					if(!data||!data.length){  
						$('#searchResult').html('<li>无查询结果</li>');
						return;
					}
					$('#result').tmpl(data).appendTo('#searchResult');
				});
			});
			//绑定，加入事件
			$('.add').live('click',function(){
				var data=$(this).parent().data('tmplItem').data;
				$.post('group?func=add',{'dn':data.dn,'privilege':data.privilege}).done(function(result){
					var content;
					var yes=false;
					if(result.flag){
						if(data.privilege=='allOpen'){
							yes="查看";
						}
					}
					content=result.desc;
					vmt.confirm({
						'yes':yes,
						'no':'关闭',
						'title':'消息',
						'content':content,
						'callback':function(flag){
							if(flag){
								window.location.href="${context}/user/index?dn="+encodeURIComponent(data.dn);
							}
						}
					});
				});
			});
		});
	</script>
	<script id="result" type="text/x-jquery-tmpl">
		<li>
			{{= name }}
			<a class="add btn btn-link btn-small">
			{{if $data.privilege=='allOpen'}}
				+加入群组
			{{else}}
				+申请加入群组
			{{/if}}
			</a>
		</li>
	</script>
</html>