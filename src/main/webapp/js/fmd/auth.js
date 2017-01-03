$.capsule([
           {
        		name:"authin",
        		
        		defaults:{
        			id:function(){
            			return "authinID"+$.capsule.randomString(1,15,'aA0');
            		},
        			blockdiv:"blockdiv",
        			message:"authenticate"
        		},
        		html:function(){
        			return "<div class=$(blockdiv) id='$(id)'>"+
        			"$(message)<br>"+
        			  "login:<input type='text' id='login' autocomplete='off'><br>"+
        			  "password: <input type='password' id='password'><br>"+
        			"</div>";
        		},
        		css:function(){
        			return {
        				'.blockdiv' : { 'position': 'fixed', 'top': '0', 'left': '0', 'right': '0', 'bottom': '0', 'background-color': '#fff', 'opacity': '0.9' }
        			};
        		}
	       },
           {
	       		name:"passin",
	       		
	       		defaults:{
	       			id:function(){
		       			return "passinID"+$.capsule.randomString(1,15,'aA0');
		       		},
	       			blockdiv:"blockdiv",
	       			message:"authenticate"
	       		},
	       		html:function(){
	       			return '<div class="$(blockdiv)" id="$(id)" data-role="fieldcontain">'+
	       				'<label for="password">$(message)</label>'+
	       				'<input name="password" type="password" id="password">'+
	       			'</div>';
	       		},
	       		css:function(){
	       			return {
	       				'.blockdiv' : { 'position': 'absolute', 'top': '0', 'left': '0', 'right': '0', 'bottom': '0', 'background-color': '#fff', 'opacity': '0.9' }
	       			};
	       		}
	       },
	       {
	    	   name:"registerShopmanIn",
	    	   defaults:{
	       			id:function(){
		       			return "registerShopmanInID"+$.capsule.randomString(1,15,'aA0');
		       		},
	       			blockdiv:"blockdiv",
	       			message:"register"
	       		},
	    	   html:function(){
	    		   return "<div id='$(id)' class='$(blockdiv)'> $(message)"+
	    		   "<br>nombre completo:"+
			        "<input type='text' name='newUserName' id='newUserName'/>"+
			        "<br>login:"+
			        "<input type='text' name='newUserLogin' id='newUserLogin'/>"+
			        "<br>password:"+
			        "<input type='password'  id='newUserPassword'/>"+
			    	"<br>re password:"+
			        "<input type='password'  id='reNewUserPassword'/>"+
			        "</div>";
	    	   },
	    	   css:function(){
	       			return {
	       				'.blockdiv' : { 'position': 'absolute', 'top': '0', 'left': '0', 'right': '0', 'bottom': '0', 'background-color': '#fff', 'opacity': '0.9' }
	       			};
	       		}
	    		   
	       },
	       {
	    	   name:"addConsummerIn",
	    	   defaults:{
	       			id:function(){
		       			return "addConsummerInID"+$.capsule.randomString(1,15,'aA0');
		       		},
	       			blockdiv:"blockdiv",
	       			message:"addConsummer"
	       		},
	    	   html:function(){
	    		   return "<div id='$(id)' class=$(blockdiv)>"+"$(message)"+
	    		   "<br>Nombre:		<input id='consummer'/>"+
		   			"<br>Tipo:			<input id='consummerType' value='1'/>"+
		   			"<br>Calle:			<input id='address'/>"+
		   			"<br>Num Int:		<input id='interiorNumber'/>"+
		   			"<br>Num Ext:		<input id='exteriorNumber'/>"+
		   			"<br>Colonia:		<input id='suburb'/>"+
		   			"<br>Localidad:		<input id='locality' value='MORELIA'/>"+
		       		"<br>Municipio:		<input id='city' value='MORELIA'/>"+
		       		"<br>Estado:		<input id='state'/>"+
		       		"<br>País:			<input id='country' value='MEXICO'/>"+
		       		"<br>Codigo Postal:	<input id='cp'/>"+
		       		"<br>R.F.C.:		<input id='rfc' value='XAXX010101000'/>"+
		       		"<br>Tel(s):		<input id='tel'/>"+
		       		"<br>EMail:			<input id='email' value=''/>"+
		       		"<br>Crédito:		<input id='payment' value='0'/>"+
		         	"</div>";
	   			
	    	   },
	    	   css:function(){
	       			return {
	       				'.blockdiv' : { 'position': 'absolute', 'top': '0', 'left': '0', 'right': '0', 'bottom': '0', 'background-color': '#fff', 'opacity': '0.9' }
	       			};
	       		}
	    		   
	       },
	       {
	    	   name:"blockIn",
	    	   defaults:{
	       			id:function(){
		       			return "blockInID"+$.capsule.randomString(1,15,'aA0');
		       		},
	       			blockdiv:"blockdiv",
	       			content:"content"
	       		},
	    	   html:function(){
	    		   return "<div id='$(id)' class='$(blockdiv)'>"+
	    		   "$(content)"+
			        "</div>";
	    	   },
	    	   css:function(){
	       			return {
	       				'.blockdiv' : { 'position': 'absolute', 'top': '0', 'left': '0', 'right': '0', 'bottom': '0', 'background-color': '#fff', 'opacity': '0.9' }
	       			};
	       		}
	    		   
	       },
	       {
	    	   name:"confirmIn",
	    	   defaults:{
	       			id:function(){
		       			return "confirmInID"+$.capsule.randomString(1,15,'aA0');
		       		},
	       			content:"content"
	       		},
	    	   html:function(data){
	    		   return $.capsule.get("blockIn").gen({
	    			   content:data.content+'<br><button id="ok">ok</button> <button id="cancel">cancel</button>'
	    				   });
	    	   },
	    	   css:function(){
	       			return {
	       				'.blockdiv' : { 'position': 'absolute', 'top': '0', 'left': '0', 'right': '0', 'bottom': '0', 'background-color': '#fff', 'opacity': '0.9' }
	       			};
	       		}
	    		   
	       }
]);
authin=function(dats){
	var authid=dats.id;
	var $form=$('body').inAuthin({id:authid, success:dats.success, message: dats.message});
	var $login=$form.find('#login');
	var $password=$form.find('#password');
	$login.focus();
	$password.bind('keypress',function(event){
		if(event.which!=13)return;
		var blockid="blockID"+$.capsule.randomString(1,15,'aA0');
		var block=$('body').inBlockIn({content:'<img src="img/waitmini.gif" />', id:blockid});
		$.ajax({
			url: CONTEXT_PATH+"/clientauthenticate",
			dataType: "json",
			type: 'POST',
			data: {
				login: $login.val(),
				password: $password.val(),
				token: TOKEN,
				clientReference: CLIENT_REFERENCE
			},success: function(data) {
				
				dats.success(data);
			},
			error: function(jqXHR, textStatus, errorThrown){
				dats.error(jqXHR, textStatus, errorThrown);
				block.remove();
			}
		});
	});
};
passin=function(dats){
	var passid=dats.id;
	var $form=$('body').inPassin({id:passid, success:dats.success, message: dats.message});
	var $password=$form.find('#password');
	$password.focus();
	$password.bind('keydown',function(event){
		if(event.keyCode==27&dats.escape){
			$form.remove();
			return;
		}
		if(event.which!=13)return;
		$.ajax({
			url: CONTEXT_PATH+"/clientauthenticate",
			dataType: "json",
			type: 'POST',
			data: {
				password: $password.val(),
				token: TOKEN,
				clientReference: CLIENT_REFERENCE
			},success: function(data) {
				dats.success(data);
			},
			error: function(jqXHR, textStatus, errorThrown){
				alert(jqXHR.responseText+" - "+textStatus+" - "+errorThrown);
			}
		});
	});
};
passinTest=function(dats){
	var passid=dats.id;
	var $form=$('body').inPassin({id:passid, success:dats.success, message: dats.message});
	var $password=$form.find('#password');
	$password.focus();
	$password.bind('keyup',function(event){
		dats.success();
	});
	return passid;
};
lockin=function(){
	AUTHORIZED=false;
	$.ajax({
		url: CONTEXT_PATH+"/clientauthenticate",
		dataType: "json",
		type:'POST',
		//async:false,
		data: {
				lock: true,
				token:TOKEN,
				clientReference: CLIENT_REFERENCE
		}
	});
};
verifyin=function(success,escape){
	var passinid='verifyID'+$.capsule.randomString(1,15,'aA0');
	passin({
		id:passinid,
		success:function(data){
			success();
			$('#'+passinid).remove();
		},
		message:"verificar password "+SHOPMAN,
		escape:escape?escape:false
	});
	return passinid;
};
registerShopmanIn=function(){
	var regid='registerShopmanID'+$.capsule.randomString(1,15,'aA0');
	var $reg=$('body').inRegisterShopmanIn({id:regid, message:"registrar usuario"});
	$reg.find('#newUserName').focus();
	$reg.find('input').bind('keydown',function(event){
		if(event.keyCode==27){
			$reg.remove();
			return;
		}
		if(event.which!=13)return;
		$.ajax({
			url: CONTEXT_PATH+"/clientauthenticate",
			dataType: "json",
			type:'POST',
			data: {
				newUserName: $reg.find('#newUserName').val(),
				newUserLogin: $reg.find('#newUserLogin').val(),
				newUserPassword: $reg.find('#newUserPassword').val(),
				reNewUserPassword: $reg.find('#reNewUserPassword').val(),
				token : TOKEN,
				clientReference: CLIENT_REFERENCE
			},
			success: function(data) {
				alert("creado :"+ $('#newUserName').val());
				$reg.find('#newUserName').val('');
				$reg.find('#newUserLogin').val('');
				$reg.find('#newUserPassword').val('');
				$reg.find('#reNewUserPassword').val('');
				$reg.remove();
				$('#commands').empty().focus();
			},
			error: function(jqXHR, textStatus, errorThrown){
				alert(jqXHR.status+" "+textStatus+" : "+jqXHR.responseText);
				//alert('no creado : verifica password/re password');
				//$( "#unauthorizedAlert" ).dialog('open');
				$('#newUserName').focus();
				//$($( "#unauthorizedAlert" ).dialog("option", "buttons")['Ok']).focus();
				//
			}
		});
	});
};
addConsummerIn=function(where){
	var addid='addConsummerInID'+$.capsule.randomString(1,15,'aA0');
	var $add=$('body').inAddConsummerIn({id:addid, message:where});
	$('#commands').val('');
	$add.find('#consummer').focus();
	$add.find('input').bind('keydown',function(event){
		if(event.keyCode==27){
			var stay=confirm("abandonar formulario?");
			if(!stay){
				//$('#commands').focus();
				return;
			}
			$('#commands').focus();
			$add.remove();
			return;
		}
		if(event.which!=13)return;
		var consummer=$add.find('#consummer').val();
		var consummerType=$add.find('#consummerType').val();
		var address=$add.find('#address').val();
		var interiorNumber=$add.find('#interiorNumber').val();
		var exteriorNumber=$add.find('#exteriorNumber').val();
		var suburb=$add.find('#suburb').val();
		var locality=$add.find('#locality').val();
		var city=$add.find('#city').val();
		var state=$add.find('#state').val();
		var country=$add.find('#country').val();
		var cp=$add.find('#cp').val();
		var rfc=$add.find('#rfc').val();
		var tel=$add.find('#tel').val();
		var email=$add.find('#email').val();
		var payment=$add.find('#payment').val();
		var somethingWrong=false;
		if(consummer==null||consummer=='')somethingWrong=true;
		if(!(isNumber(consummerType)&&isNumber(payment)&&validateRfc(rfc))){
			somethingWrong=true;
		}
		if(email!=null){
			var emails=email.split(" ");
			for(var i=0;i<emails.length;i++){
				if(!validateEmail(emails[i]))somethingWrong=true;
			}
		}
		else somethingWrong=true;
		if(somethingWrong){
			var msg="error:";
			if(consummer==null||consummer=='')msg+="\nNombre indefinido.";
			msg+=isNumber(consummerType)?"":"\nTipo de cliente no es numerico.\n";
			msg+=isNumber(payment)?"":"\nCredito no es numerico.\n";
			msg+=validateRfc(rfc)?"":"\nRFC no valido.";
			if(email!=null){
				var emails=email.split(" ");
				for(var i=0;i<emails.length;i++){
					if(!validateEmail(emails[i]))msg+="\nEmail '"+emails[i]+"' no valido.";
				}
			}
			else msg+="\nemail(s) no definido(s).";
			alert(msg);
			return;
		}
		var ca;
		if(where=='clients'){
			ca=new Client_(null, consummer, consummerType,
				address, interiorNumber, exteriorNumber,
				suburb, locality, city, country,
				state, email, cp, rfc, tel,
				payment, null, null);
			client=ca;
		}
		else {
			ca=new Agent_(null, consummer, consummerType,
				address, interiorNumber, exteriorNumber,
				suburb, locality, city, country,
				state, email, cp, rfc, tel,
				payment, null, null);
			agent=ca
		}
		$.ajax({
			url: CONTEXT_PATH+"/welcome",
			type:'POST',
			data: {
				client : encodeURIComponent($.toJSON(ca)),
				where:where,
				token : TOKEN,
				clientReference: CLIENT_REFERENCE
			},
			success: function(data){
				//console.log(client.consummer +" creado");
				console.log("SUCCESS");
				console.log(data);
				if(where=='clients'){
				client=new setClient_(data);
				var code=data.code;
				for(var j=0;j<productsLog.length;j++){
					var jsonsrt="["+$.toJSON(productsLog[j])+"]";
					$.ajax({
						index : j,
						url: CONTEXT_PATH+"/getthis",
						type:'POST',
						data: {
							list:encodeURIComponent(jsonsrt),
							code:code,
							token : TOKEN,
	    					clientReference: CLIENT_REFERENCE
						},
						dataType: "json",
						error: function(jqXHR, textStatus, errorThrown){
							alert("el sistema dice: "+textStatus+" - "+errorThrown+" - "+jqXHR.responseText);
						},
						success: function(data) {
							//alert(productsLog[0].quantity+" ->"+this.index);
							var j=this.index;
							if(productsLog[j].id!="-1"){
								//alert(jsonsrt);
								//$('.quantity').eq(j).text(data[j].quantity);
								$(".tableingrow").unbind('DOMSubtreeModified');
								$('.unit').eq(j).html(data[0].unit);
								$('.description').eq(j).html(data[0].description);
								$('.code').eq(j).html(data[0].code);
								$('.mark').eq(j).html(data[0].mark);
								$('.unitPrice').eq(j).html(data[0].unitPrice);
								var quantity=productsLog[j].quantity;
								if(productsLog[j].disabled){
									productsLog[j]=data[0];
									productsLog[j].disabled=true;
								}
								else productsLog[j]=data[0];
								productsLog[j].quantity=quantity;
								onLogChange();
								//productsLog[j].quantity=$('.quantity').eq(j).val();
							}									
								//else{alert("code");}
							
						}
					});
				}
				}
				//$('#editclient').dialog('close');
				$add.remove();
				$('#commands').focus();
			},
			error: function(jqXHR, textStatus, errorThrown){
				alert("el sistema dice: "+textStatus+" - "+errorThrown+" - "+jqXHR.responseText);
			},
			dataType:"json"
		});
		
	});
};
confirmIn=function(dats){
	var confid="confirmInID"+$.capsule.randomString(1,15,'aA0');
	var confirm=$('body').inConfirmIn({content:dats.content, id:confid});
	confirm.find("#ok").focus().click(function(){dats.ok();confirm.remove();}).html(dats.okValue?dats.okValue:"ok");
	confirm.find("#cancel").click(function(){dats.cancel();confirm.remove();}).html(dats.cancelValue?dats.cancelValue:"cancel");
};