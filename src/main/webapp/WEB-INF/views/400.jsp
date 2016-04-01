<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>页面未找到</title>
	<style>
		.container{width:50%; margin:5% auto; background:#f5f5f5; padding:2em; border-radius:4px; border:1px solid #ccc; text-align:center;}
	</style>
</head>
<body class="dHome-body gray">
	<div class="container">
		<h1>无效的地址，请重新输入</h1>
	</div>
</body>
<script>
	setTimeout(function(){
		window.history.back();			
	},2000);

</script>
</html>