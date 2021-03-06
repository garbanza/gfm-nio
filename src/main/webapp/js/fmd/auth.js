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
	       				'.blockdiv' : { 'z-index':'2000','position': 'fixed', 'top': '0', 'left': '0', 'height': '100%','width': '100%', 'bottom': '0', 'background-color': '#fff', 'opacity': '0.9' }
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
	       				'.blockdiv' : { 'position': 'fixed', 'top': '0', 'left': '0', 'right': '0', 'bottom': '0', 'background-color': '#fff', 'opacity': '0.9' }
	       			};
	       		}
	    		   
	       },
	       {
	    	   name:"editProduct",
	    	   defaults:{
	       			id:function(){
		       			return "editProductID"+$.capsule.randomString(1,15,'aA0');
		       		},
	       			blockdiv:"blockdiv",
	       			message:"editProduct"
	       		},
	    	   html:function(){
	    		   return "<div id='$(id)' class='$(blockdiv)'> $(message)"+
	    		   "<br>cantidad:		<input id='quantity' value='1'/>"+
	    		   "<br>unidad:		<input id='unit' value='pza'/>"+
	    		   "<br>descripcion:		<input id='description' value='articulo'/>"+
	    		   "<br>codigo:		<input id='code' value='art-gen'/>"+
	    		   "<br>marca:		<input id='mark' value='generica'/>"+
	    		   "<br>unidad sat:		<input id='unitCode' value='h87'/>"+
	    		   "<br>clave sat:		<input id='prodservCode' value='01010101'/>"+
	    		   "<br>$ unitario:		<input id='unitPrice' value='0'/>"+
			        "</div>";
	    	   },
	    	   css:function(){
	       			return {
	       				'.blockdiv' : { 'position': 'fixed', 'top': '0', 'left': '0', 'right': '0', 'bottom': '0', 'background-color': '#fff', 'opacity': '0.9' }
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
 	    		   				"<br>Nombre:		<input id='consummer'/>"
								+ "<br>R.F.C.:		<input id='rfc' value='XAXX010101000'/>"
								+ "<br>uso CFDI:		<select id='cfdiUse' style='width:10%'>"
								+ "<option ></option>"
								+ "<option value='G01'>G01 Adquisición de mercancias</option>"
								+ "<option value='G02'>G02 Devoluciones, descuentos o bonificaciones</option>"
								+ "<option value='G03'>G03 Gastos en general</option>"
								+ "<option value='I01'>I01 Construcciones</option>"
								+ "<option value='I02'>I02 Mobilario y equipo de oficina por inversiones</option>"
								+ "<option value='I03'>I03 Equipo de transporte</option>"
								+ "<option value='I04'>I04 Equipo de computo y accesorios</option>"
								+ "<option value='I05'>I05 Dados, troqueles, moldes, matrices y herramental</option>"
								+ "<option value='I06'>I06 Comunicaciones telefónicas</option>"
								+ "<option value='I07'>I07 Comunicaciones satelitales</option>"
								+ "<option value='I08'>I08 Otra maquinaria y equipo</option>"
								+ "<option value='D01'>D01 Honorarios médicos, dentales y gastos hospitalarios.</option>"
								+ "<option value='D02'>D02 Gastos médicos por incapacidad o discapacidad</option>"
								+ "<option value='D03'>D03 Gastos funerales.</option>"
								+ "<option value='D04'>D04 Donativos.</option>"
								+ "<option value='D05'>D05 Intereses reales efectivamente pagados por créditos hipotecarios (casa habitación).</option>"
								+ "<option value='D06'>D06 Aportaciones voluntarias al SAR.</option>"
								+ "<option value='D07'>D07 Primas por seguros de gastos médicos.</option>"
								+ "<option value='D08'>D08 Gastos de transportación escolar obligatoria.</option>"
								+ "<option value='D09'>D09 Depósitos en cuentas para el ahorro, primas que tengan como base planes de pensiones.</option>"
								+ "<option value='D10'>D10 Pagos por servicios educativos (colegiaturas)</option>"
								+ "<option value='P01'>P01 Por definir</option>"
								+ "</select>"
						       	+"<br>País:			<input id='country' value='MEXICO'/>"
						       	+ "<br>EMail:			<input id='email' value=''/>"
								+ "<br>Tel(s):		<input id='tel'/>"
						       	+ "<br>Tipo:			<input id='consummerType' value='1'/>"
								+ "<br>Calle:			<input id='address'/>"
								+ "<br>Num Int:		<input id='interiorNumber'/>"
								+ "<br>Num Ext:		<input id='exteriorNumber'/>"
								+ "<br>Colonia:		<input id='suburb'/>"
								+ "<br>Localidad:		<input id='locality' value='MORELIA'/>"
								+ "<br>Municipio:		<input id='city' value='MORELIA'/>"
								+ "<br>Estado:		<input id='state'/>"
								+ "<br>Codigo Postal:	<input id='cp'/>"
								+ "<br>Crédito:		<input id='payment' value='0'/>"
								+ "</div>";
	   			
	    	   },
	    	   css:function(){
	       			return {
	       				'.blockdiv' : { 'position': 'fixed', 'top': '0', 'left': '0', 'right': '0', 'bottom': '0', 'background-color': '#fff', 'opacity': '0.9' }
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
	       				'.blockdiv' : { 'position': 'fixed', 'top': '0', 'left': '0', 'right': '0', 'bottom': '0', 'background-color': '#fff', 'opacity': '0.9' }
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
	       				'.blockdiv' : { 'position': 'fixed', 'top': '0', 'left': '0', 'right': '0', 'bottom': '0', 'background-color': '#fff', 'opacity': '0.9' }
	       			};
	       		}
	    		   
	       },
	       {
	    	   name:"invoicePaymentForm",
	    	   defaults:{
	       			id:function(){
		       			return "invoicePaymentFormID"+$.capsule.randomString(1,15,'aA0');
		       		},
	       			blockdiv:"blockdiv",
	       			message:"invoicePaymentForm"
	       		},
	    	   html:function(){
	    		   return '<div id="$(id)" class="$(blockdiv)"> $(message)\
	    		   <table><tr><td><input id="amount" placeholder="Cantidad Transferida" type="number" step="0.01"/></td><td>Monto</td></tr>\
	    		   <tr><td><input id="datetime" value="'+new Date().format("yyyy-mm-ddFIXHH:MM:ss").replace("FIX","T")+'"/></td><td>Fecha(AAAA-MM-DDTHH:MM:SS)</td></tr>\
	    		   <tr><td><select id="paymentWay">\
	    		   <option disabled selected value>Forma de Pago</option>\
	    		   <option value="01">01 Efectivo</option>\
	    		   <option value="02">02 Cheque nominativo</option>\
	    		   <option value="03">03 Transferencia electrónica de fondos</option>\
	    		   <option value="04">04 Tarjeta de crédito</option>\
	    		   <option value="05">05 Monedero electrónico</option>\
	    		   <option value="06">06 Dinero electrónico</option>\
	    		   <option value="08">08 Vales de despensa</option>\
	    		   <option value="12">12 Dación en pago</option>\
	    		   <option value="13">13 Pago por subrogación</option>\
	    		   <option value="14">14 Pago por consignación</option>\
	    		   <option value="15">15 Condonación</option>\
	    		   <option value="17">17 Compensación</option>\
	    		   <option value="23">23 Novación</option>\
	    		   <option value="24">24 Confusión</option>\
	    		   <option value="25">25 Remisión de deuda</option>\
	    		   <option value="26">26 Prescripción o caducidad</option>\
	    		   <option value="27">27 A satisfacción del acreedor</option>\
	    		   <option value="28">28 Tarjeta de débito</option>\
	    		   <option value="29">29 Tarjeta de servicios</option>\
	    		   <option value="30">30 Aplicación de anticipos</option>\
	    		   <td>FormaPago</td></tr>\
	    		   <tr><td><input id="operationNumber" placeholder="Num Operación"/></td><td>(opcional) SPEI, Nº Cheque, Clave, Linea captura, Nº Referencia o identificación, etc...</td></tr>\
	    		   </select></td></tr></table>\
	    		   Refs:<div id="refs"><input name="ref" placeholder="Referencia"><input name="amount" placeholder="Monto a pagar" type="number" step="0.01"></div>\
	    		   <input id="morerefs" type="button" value="+ agregar otra" /><br>\
	    		   <input id="submit" type="button" value="Hacer Pago">\
	    		   </div>';
	    	   },
	    	   css:function(){
	       			return {
	       				'.blockdiv' : { 'position': 'fixed', 'top': '0', 'left': '0', 'right': '0', 'bottom': '0', 'background-color': '#fff', 'opacity': '0.9' }
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
editProduct=function(commander){
	var addid='editProductID'+$.capsule.randomString(1,15,'aA0');
	var $add=$('body').inEditProduct({id:addid, message:"editar producto"});
	commander.reset();
	$add.find('#quantity').focus();
	$add.find('input').bind('keydown',function(event){
		if(event.keyCode==27){
			var stay=confirm("abandonar formulario?");
			if(!stay){
				//$('#commands').focus();
				return;
			}
			commander.input.focus();
			$add.remove();
			return;
		}
		if(event.which!=13)return;
		var quantity=$add.find('#quantity').val();
		var unit=$add.find('#unit').val().toUpperCase();
		var description=$add.find('#description').val().toUpperCase();
		var code=$add.find('#code').val().toUpperCase();
		var mark=$add.find('#mark').val().toUpperCase();
		var unitCode=$add.find('#unitCode').val().toUpperCase();
		var prodservCode=$add.find('#prodservCode').val().toUpperCase();
		var unitPrice=$add.find('#unitPrice').val();
		dolog(quantity?quantity:1,unit?unit:"PZA",
				description?description:"ARTICULO",code?code:"ART-GEN",
				mark?mark:"GEN",unitPrice?unitPrice:0,
				prodservCode?prodservCode:"01010101",unitCode?unitCode:"H87");
		var json = "{"+ '"quantity":"' + quantity + '",' + '"unit":"'+ unit	+ '",' + '"description":"' + description + '",'	+ '"code":"' + code
					+ '",'	+ '"mark":"' + mark	+ '",'	+ '"productPriceKind":"-1",' + '"unitPrice":"'	+ unitPrice + '",'	+ '"id":"-1", "edited" : true }';
		productsLog.unshift($.parseJSON(json));
		onLogChange();
		commander.input.focus();
		$add.remove();
		$('.tableingrow').sexytable({
			'editedRow' : true,
			'index' : $('.tableingrow').get(0)
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
		var cfdiUse=$add.find('#cfdiUse').val();
		var somethingWrong=false;
		if(consummer==null||consummer=='')somethingWrong=true;
		if(!(/*isNumber(consummerType)&&isNumber(payment)&&*/validateRfc(rfc))){
			somethingWrong=true;
		}
		if(email!=null){
			var emails=email.split(" ");
			for(var i=0;i<emails.length;i++){
				if(emails[i]!=""&!validateEmail(emails[i]))somethingWrong=true;
			}
		}
		else somethingWrong=true;
		if(somethingWrong){
			var msg="error:";
			if(consummer==null||consummer=='')msg+="\nNombre indefinido.";
			//msg+=isNumber(consummerType)?"":"\nTipo de cliente no es numerico.\n";
			//msg+=isNumber(payment)?"":"\nCredito no es numerico.\n";
			msg+=validateRfc(rfc)?"":"\nRFC no valido.";
			if(email!=null){
				var emails=email.split(" ");
				for(var i=0;i<emails.length;i++){
					if(emails[i]!=""&!validateEmail(emails[i]))msg+="\nEmail '"+emails[i]+"' no valido.";
				}
			}
			//else msg+="\nemail(s) no definido(s).";
			alert(msg);
			return;
		}
		var ca;
		if(where=='clients'){
			ca=new Client_(null, consummer, consummerType,
				address, interiorNumber, exteriorNumber,
				suburb, locality, city, country,
				state, email, cp, rfc, tel,
				payment, null, null,cfdiUse);
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
				/*for(var j=0;j<productsLog.length;j++){
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
				}*/
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
addConsummerIn2=function(commander){
	var where;
	if(commander.commandline.kind == "editclient")where = "clients";
	else if(commander.commandline.kind == "editagent")where = "agents";
	var addid='addConsummerInID'+$.capsule.randomString(1,15,'aA0');
	var $add=$('body').inAddConsummerIn({id:addid, message:where});
	$add.find('#consummer').focus();
	$add.find('input').bind('keydown',function(event){
		if(event.keyCode==27){
			var stay=confirm("abandonar formulario?");
			if(!stay){
				//$('#commands').focus();
				return;
			}
			$add.remove();
			$(commander.input).focus();
			commander.reset();
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
		var cfdiUse=$add.find('#cfdiUse').val();
		var somethingWrong=false;
		if(consummer==null||consummer=='')somethingWrong=true;
		if(!(/*isNumber(consummerType)&&isNumber(payment)&&*/validateRfc(rfc))){
			somethingWrong=true;
		}
		if(email!=null){
			var emails=email.split(" ");
			for(var i=0;i<emails.length;i++){
				if(emails[i]!=""&!validateEmail(emails[i]))somethingWrong=true;
			}
		}
		else somethingWrong=true;
		if(somethingWrong){
			var msg="error:";
			if(consummer==null||consummer=='')msg+="\nNombre indefinido.";
			//msg+=isNumber(consummerType)?"":"\nTipo de cliente no es numerico.\n";
			//msg+=isNumber(payment)?"":"\nCredito no es numerico.\n";
			msg+=validateRfc(rfc)?"":"\nRFC no valido.";
			if(email!=null){
				var emails=email.split(" ");
				for(var i=0;i<emails.length;i++){
					if(emails[i]!=""&!validateEmail(emails[i]))msg+="\nEmail '"+emails[i]+"' no valido.";
				}
			}
			//else msg+="\nemail(s) no definido(s).";
			alert(msg);
			return;
		}
		var ca;
		if(where=='clients'){
			ca=new Client_(null, consummer, consummerType,
				address, interiorNumber, exteriorNumber,
				suburb, locality, city, country,
				state, email, cp, rfc, tel,
				payment, null, null,cfdiUse);
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
				console.log("SUCCESS");
				console.log(data);
				if(where=='clients'){
					client=new setClient_(data);
					var code=data.code;
				}
				$add.remove();
				$(commander.input).focus();
				commander.reset();
			},
			error: function(jqXHR, textStatus, errorThrown){
				alert("el sistema dice: "+textStatus+" - "+errorThrown+" - "+jqXHR.responseText);
			},
			dataType:"json"
		});
		
	});
};

invoicePaymentForm=function(commander){

	var addid='invoicePaymentFormID'+$.capsule.randomString(1,15,'aA0');
	var $add=$('body').inInvoicePaymentForm({id:addid});
	$add.find('#amount').focus();
	$add.bind('keydown',function(event){
		if(event.keyCode==27){
			var stay=confirm("abandonar formulario?");
			if(!stay){
				return;
			}
			$add.remove();
			$(commander.input).focus();
			commander.reset();
			return;
		}
	});
	$add.find("#morerefs").click(function(event){
		var id = "refID"+$.capsule.randomString(1,15,'aA0')
		$add.find("#refs").append('<div id="'+id+'"><input id= "tofocus'+id+'"name="ref" placeholder="Referencia">\
				<input name="amount" placeholder="Monto a pagar" type="number" step="0.01">\
				<input id="delete'+id+'" type="button" value="- Quitar">\
				</div>');
		$("#delete"+id).click(function(){$("#"+id).remove()});
		$("#tofocus"+id).focus();
	});
	$add.find('#submit').click(function(event){

		var amount=$add.find('#amount').val(),
			datetime=$add.find('#datetime').val(),
			paymentWay=$add.find('#paymentWay').val(),
			operationNumber = $add.find('#operationNumber').val(),
			refs = [], amounts = [],
			refsNodes = $add.find('[name="ref"]'),
			amountsNodes = $add.find('[name="amount"]'),
			amountsSum = 0;
		for(var i = 0; i < refsNodes.length; i++){
			refs[i] = refsNodes[i].value;
			amounts[i] = amountsNodes[i].value*1;
			amountsSum += amounts[i];
			if(refs[i] == '' || amounts[i] == ''){
				alert("Error: llenar referencias y montos");
				return;
			}
		}
		if(amountsSum != amount){
			alert("Error: La suma de los Montos a pagar debe ser igual al Monto "+amount+'!='+amountsSum);
			return;
		}
		if(amount == '' || amount <= 0 || datetime == '' || paymentWay == '' || refs.legth == 0){
			alert("Error: llenar todo correctamente");
			return;
		}
		if(!isNumber(amount)){alert("Error: Monto no numèrico"); return;}
		if(!datetime.match("^[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}$")){alert("Error: Fecha debe ser ^[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}$"); return;}
		
		var block1=$.blockUI({
			content : '<h1><img src="img/wait.gif" /> esperar...</h1>',
			appendTo : "#"+addid
			});
		
		$.ajax({
			url: CONTEXT_PATH+"/dbport",
			type:'POST',
			data: {
				command : commander.commandline.kind,
				amount : amount,
				paymentWay : paymentWay,
				datetime : datetime,
				operationNumber : operationNumber,
				refs : refs.join(" "),
				amounts : amounts.join(" "),
				token : TOKEN,
				clientReference: CLIENT_REFERENCE
			},
			success: function(data){
				invoiceInfoLog(data.invoice);
				console.log("SUCCESS");
				console.log(data);
				
				$add.remove();
				$(commander.input).focus();
				commander.reset();
				$.blockUI({
					content : '<h1>'+data.successResponse+'</h1>',
					unblockOnAnyKey:true
				});
			},
			error: function(jqXHR, textStatus, errorThrown){
				$.blockUI({
					node:block1,
					content : textStatus+" - "+errorThrown+" - "+ jqXHR.responseText,
					changeContent:true,
					unblockOnAnyKey:true
				});
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