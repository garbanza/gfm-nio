<%@page import="com.ferremundo.OnlineClient"%>
<%@page import="com.ferremundo.OnlineClients"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="shortcut icon" href="${pageContext.request.contextPath}/favicon.ico" />
<title>auth</title>
<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="js/jquery.idle.min.js"></script>
<script type="text/javascript" src="js/jquery.capsule.js"></script>
<script type="text/javascript" src="js/fmd/auth.js"></script>
<script type="text/javascript">
/*

OnlineClients clients= OnlineClients.instance();
String ipAddres=request.getRemoteAddr();
out.println("\tvar IP_ADDRESS='"+ipAddres+"';");
int clientReference=clients.add(ipAddres,request.getSession().getId());
out.println("\tvar CLIENT_REFERENCE="+clientReference+";");
OnlineClient onlineClient=clients.get(clientReference);
out.println("\tvar TOKEN='"+onlineClient.getToken()+"';");
out.println("\tvar SHOPMAN='';");
out.println("\tvar SID='"+request.getSession().getId()+"';");
request.getSession().setMaxInactiveInterval(60*60*8);
*/
//var ipAddres="${pageContext.request.getRemoteAddr()}";
var AUTHORIZED=false;
var TOKEN="${token}";
var CLIENT_REFERENCE="${clientReference}";
var BACK="${back}";
var SHOPMAN='';
var CONTEXT_PATH="${pageContext.request.contextPath}";
//{pageContext.request.getSession().setMaxInactiveInterval(60*60*8);};

$(document).ready(function(){
	/*var passinid='passid'+$.capsule.randomString(1,15,'aA0');
	passin({
		id:passinid,
		success:function(data){
			AUTHORIZED=true;
			alert("success " +data.authenticated+". #"+passinid+" to be removed")
			$('#'+passinid).remove();
		},
		message:"desbloquear"
	});*/

	//alert("start");
	var authid='authid'+$.capsule.randomString(1,15,'aA0');
	authin({
		id:authid, 
		success:function(data){
			//alert("success " +data.authenticated+". #"+authid+" to be removed");
			var url = BACK;
			$('<form action="' + url + '" method="post">' +
  				'<input type="text" name="clientReference" value="' + CLIENT_REFERENCE + '" />' +
  				'<input type="text" name="token" value="' + TOKEN + '" />' +
  				'<input type="text" name="shopman" value="{name:"' + data.shopman.name + '", login:"'+data.shopman.login+'""} />' +
  				'</form>').appendTo($(document.body)) //it has to be added somewhere into the <body>
  		        .submit();
			/*
			$('#'+authid).remove();
			AUTHORIZED=true;
			//$.idleTimer(1000);
			//$( document ).idleTimer( 1000 );
			//$(document).bind("idle.idleTimer", function(){
			$( document ).idle({
				idle:1000*60*5,
				onIdle: function(){
					var doIdle=document.isIdle?false:true;
					if(doIdle)document.isIdle=true;
					else return;
					lockin();
					var passinid='passid'+$.capsule.randomString(1,15,'aA0');
					passin({
						id:passinid,
						success:function(data){
							AUTHORIZED=true;
							//alert("success " +data.authenticated+". #"+passinid+" to be removed")
							$('#'+passinid).remove();
							document.isIdle=false;
						},
						message:"desbloquear "+SHOPMAN
					});
				}
			});*/
			
		},
		message:"autenticarse",
		error:function(jqXHR, textStatus, errorThrown){
			alert(jqXHR.responseText+" - "+textStatus+" - "+errorThrown);
		}
	});
	/*confirmIn({
		content:'seguro que eres un hijo de la chingada?', 
		ok:function(){alert("ok");},
		cancel:function(){alert("cancel");},
		okValue:"si",
		cancelValue:"no"
		});
	*/
});
</script>
</head>
<body>

</body>
</html>