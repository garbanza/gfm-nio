<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="com.ferremundo.stt.GSettings" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>sat</title>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery-2.1.1.min.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	console.log('${GSettings.get("MAIN_CERTIFICATE_AUTHORITY_USER")}');
	$("[name='Ecom_Password']").val('${GSettings.get("MAIN_CERTIFICATE_AUTHORITY_USER")}');
	$("[name='Ecom_User_ID']").val('${GSettings.get("MAIN_CERTIFICATE_AUTHORITY_PASSWORD")}');
	//$("[name='Ecom_User_ID']").val("COSB760826KV8");
	$(document.IDPLogin.submit).click();
});

</script>
</head>
<body>
<form name="IDPLogin" enctype="application/x-www-form-urlencoded" method="POST" action="https://cfdiau.sat.gob.mx/nidp/app/login?sid=1&sid=1" style="width: 840px; position: absolute; visibility: hidden">
			<input type="hidden" name="option" value="credential">

		<br>
		<table border="0" align="center">	
			<tr>
				<td width="auto"></td>
				<td width="auto" align="center">
					<div class="encabezado">
						<br>
						<p align="right" style="margin-right: 100px;"></p>
					</div>
					<div class="encabezadoFoot">Portal de Contribuyentes</div>

				</td>
				<td width="auto"></td>
			</tr>
		</table>
		<div id="xacerror">
		<a class="b-close">x</a>
		<table border="0">
			<tr>
				<td><img src="/nidp/xac/images/satLogoChico.jpg" /></td>
					<td>&nbsp;</td>
					<td><p id="msgError" class="normalFont"></p></td>
			</tr>			
		</table>
		</div>	
		<br>
		<div style="width:840px; padding-top:10px"align="center">		
		<table width="500px" cellpadding="0" cellspacing="3" border="0">
		
							<tr>							
								<td style="font-size:17px "height="33" colspan="2" align="center">
									<div style="font-size: 17px">
										<p>
											<B>Acceso a los servicios electr&oacute;nicos </B>
										</p>
									</div>
								</td>
							</tr>
							<tr>
								<td width="26%" height="47" align="left">
									<label class="normalFont">RFC</label>
								</td>
								<td width="74%" align="left">
									<input type="text" name="Ecom_User_ID" size="25" value="" class="cajatexto">
								</td>
							</tr>
							<tr>
								<td align=left>
									<label class="normalFont">Contrase&ntilde;a</label>						
								</td>							
								<td align=left>
									<input type="password" name="Ecom_Password" size="25" class="cajatexto">
								</td>							
							</tr>
							<tr>
                            <td colspan="2" align="center">&nbsp;</td>
                        	</tr>
							<tr>
								<td height="33" colspan="2" align="center">
									<input type="submit" value="Enviar" id="submit" name="submit" />
								</td>							
							</tr>
							<tr>
                      <td  height="45" colspan="2" align="center">
									<table class="contrasena" width="100%" border="0">
									<tr>
											<td width="37%"><div align="center"></div></td>
											<td width="18%"><div align="right">
													Contrase&ntilde;a
												</div></td>
											<td width="3%">|</td>
											<td width="42%"><div align="left"><a href="/nidp/app/login?id=SATx509Custom&sid=1&option=credential&sid=1">Fiel</a></div></td>

										</tr>
									</table>
								</td>
							</tr>
	      	</table>
		</div>
   	</form>
</body>
</html>