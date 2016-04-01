<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="WEB-INF/tld/vmt.tld" prefix="vmt"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
    <%
    request.setAttribute("context", request.getContextPath());
    %>
<meta name="viewport" content="width=device-width, initial-scale=1" charset="UTF-8" />

<f:css  href="${context }/resource/thirdparty/hColumns-master/css/reset.css" />
<f:css  href="${context }/resource/thirdparty/hColumns-master/css/hcolumns.css" />
<f:css  href="${context }/resource/thirdparty/bootstrap/css/bootstrap.min.css" />
<f:css  href="${context }/resource/thirdparty/bootstrap/css/bootstrap-responsive.min.css" />
<f:css  href="${context }/resource/css/slidernav.css" />
<f:css  href="${context }/resource/css/tokenInput.css" />
<f:css  href="${context }/resource/css/vmt.css" />
<f:css  href="${context }/resource/thirdparty/autocomplate/jquery.autocomplete.css" />
<f:css href="${context }/resource/thirdparty/datepicker/sample-css/page.css" />
<f:css href="${context }/resource/thirdparty/datepicker/css/dp.css" />
	
<f:script  src="${context }/resource/js/jquery-1.7.2.min.js"/>
<f:script  src="${context }/resource/js/jquery.tmpl.min.js"/>
<f:script  src="${context }/resource/js/jquery.validate.min.js"/>
<f:script  src="${context }/resource/thirdparty/autocomplate/jquery.autocomplete.js"/>

<f:script  src="${context }/resource/thirdparty/bootstrap/js/bootstrap.min.js"/>
<f:script  src="${context }/resource/js/vmt-Dialog.js"/>
<f:script  src="${context }/resource/js/slidernav.js"/>
<f:script  src="${context }/resource/js/toker-jQuery-forVmt.js"/>
<f:script  src="${context }/resource/thirdparty/hColumns-master/js/jquery.hcolumns.js"/>
<f:script  src="${context }/resource/js/vmt-validate-ext.js"/>
<f:script  src="${context }/resource/js/vmt-search.js"/>
<f:script  src="${context }/resource/js/vmt-dn.js"/>
<f:script  src="${context }/resource/js/vmt-util.js"/>
<f:script  src="${context }/resource/js/vmt-multi-press.js"/>

<f:script src="${context }/resource/thirdparty/datepicker/src/Plugins/datepicker_lang_HK.js" />
<f:script src="${context }/resource/thirdparty/datepicker/src/Plugins/jquery.datepicker.js" />

<link href="https://www.escience.cn/dface/images/favicon.ico" rel="shortcut icon" type="image/x-icon" />

<link href="https://www.escience.cn/dface/css/dface.banner.css" rel="stylesheet" type="text/css"/>
<script src="https://www.escience.cn/dface/js/dface.banner.js"  type="text/javascript"></script>

<link href="https://www.escience.cn/dface/css/dface.simple.footer.css" rel="stylesheet" type="text/css"/>
<script src="https://www.escience.cn/dface/js/dface.simple.footer.js"  type="text/javascript" ></script>
<script type="text/javascript">
$(document).ready(function(){
	var dialog=new VmtDialog("");
	$(document).data("dialog",dialog);
});
</script>
