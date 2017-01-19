<%@page import="com.ferremundo.stt.GSettings"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="com.ferremundo.stt.GSettings" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery-2.1.1.min.js"></script>
<!--script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.crossorigin.js"></script-->
<script type="text/javascript">
var CONTEXT_PATH="${pageContext.request.contextPath}"
$(document).ready(function(){
	console.log("setts "+ '${GSettings.get("MAIN_CERTIFICATE_AUTHORITY_USER")}');
	$("#authsat").click(function(){
		$("#satmodule").append("<iframe src='"+ CONTEXT_PATH+"/sat' id=sat style='visibility:hidden'></iframe>");
		$("#sat").change(function(){
			// qrcode to https://portalcfdi.facturaelectronica.sat.gob.mx/ConsultaReceptor.aspx
				console.log("changed");
		});
	});
});

</script>
<title>Insert title here</title>
</head>
<body>
<button id="authsat">auth sat</button>
<div id="satmodule"></div>
</body>
</html>