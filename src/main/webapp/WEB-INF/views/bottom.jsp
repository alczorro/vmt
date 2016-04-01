<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<f:script  src="${context }/resource/js/vmt-hash-resolver.js"/>
<script type="text/javascript">
$(document).ready(function(){
	$(".dface.footer p span#app-version").html("(VMT <f:version/>)");
});
</script>