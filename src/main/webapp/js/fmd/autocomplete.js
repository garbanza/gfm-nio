/** AUTOCOMPLETE BEGINS*/
autocomplete=function(input){
	$(input).autocomplete({
		html:true,

		
/** AUTOCOMPLETE SOURCE*/
		source: function(request, response) {
			var event=this.options.keydownEvent;
			if(event.which==13)console.log('ENTER');
			console.log('this');
			console.log(this);
			console.log(event);
			//console.log(this.event);
			//console.log(this.event.which);
			commandline=new commandLine();
			console.log('commandline');
			console.log(commandline);
			//alert("kind:"+c.kind+"\nquantity:"+c.quantity+"\nargs:"+c.args+"\nargssize:"+c.argssize+"\ngetFRomBD:"+c.getFromDB+"\ncommand:"+c.command);
			var args="";
			for(var i=0;i<commandline.args.length;i++)args+=commandline.args[i]+(i==(commandline.args.length-1)?"":" ");
			if(commandline.getFromDB){
				if(commandline.kind=='client'||commandline.kind=='agent'||commandline.kind=='product'||commandline.kind=='agentstatus'||commandline.kind=='clientstatus'){
					if(commandline.args.length<1)return;
				}
				//alert(commandline.command);
				REQUEST_NUMBER=REQUEST_NUMBER+1;
				$.ajax({
					url: "port",
					dataType: "json",
					type:'POST',
					data: {
						search: encodeURIComponent(args),
						requestNumber: REQUEST_NUMBER,
						clientReference: CLIENT_REFERENCE,
						commandkind: commandline.kind,
						consummerType: client?client.consummerType:1,
						token : TOKEN
					},
					success: function(data) {
						if(REQUEST_NUMBER!=data.requestNumber){
							//console.log("distintos id's");
							return;
						}
						inputValue=$(input).val();
						var cpos=$(input).getCursorPosition();
						//console.log(cpos);
						//console.log(this);
						if(commandline.kind=='product'||commandline.kind=='retrieve'){
							products=data.products;
							response($.map(data.products, function(item) {
								return {
									label: item.code+" "+item.description,
									value: inputValue
								};
							}));
						}
						else if(commandline.kind=='client'||commandline.kind=='agent'||commandline.kind=='agentstatus'||commandline.kind=='clientstatus'){
							clients=data.clients;
							response($.map(data.clients, function(item) {
								return {
									label: item.consummer+" "+item.rfc,
									value: inputValue
								};
							}));
						}
						setCaretToPos($(input)[0],cpos);
						//$(input).trigger(key_down);
					}
				});
			}
		},
		minLength: 1,
/** AUTOCOMPLETE SELECT*/
		select: function(event, ui) {
			console.log("commandline "+commandline.kind);
			var i=$('#ui-active-menuitem').parent().index('.ui-menu-item');
			if(commandline.kind=='product'||commandline.kind=='retrieve'){
				var quantity=0;
				quantity=commandline.quantity?commandline.quantity:1;
				p=products;
			
				dolog(quantity,p[i].unit,p[i].description,p[i].code,p[i].mark,p[i].unitPrice);
				productsLog.unshift(p[i]);
				productsLog[0].quantity=quantity;
				onLogChange();
				
				//alert(productsLog[i].key);
			}
			else if(commandline.kind=='agentstatus'||commandline.kind=='clientstatus'){
				var where="";
				if(commandline.kind=='agentstatus')where='agents';
				else if(commandline.kind=='clientstatus')where='clients';
				$("#resultset").prepend("<img src=img/wait.gif width=70px height=70px/>");
				$.ajax({
					index : j,
					type:'POST',
					url: "clienthistory",
					data: {
						where:where,
						hash:clients[i].code,
						token : TOKEN,
    					clientReference: CLIENT_REFERENCE
					},
					dataType: "json",
					error: function(jqXHR, textStatus, errorThrown){
						alert(textStatus);
					},
					success: function(data) {
						$('#resultset').empty();
						console.log('data');
						console.log(data);
						for(var index=0;index<data.invoices.length;index++){
							var invoice=data.invoices[index];
							
							invoiceInfoLog(invoice,'#resultset');
						}
						$("#resultset").append("-- FIN DE BUSQUEDA --");
					}
				});
			}
			else if(commandline.kind=='client'||commandline.kind=='agent'){
				//TODO deactivate agent here
				if(commandline.kind=='agent'){
					agent=new setAgent_(clients[i]);

					emptyCommand=true;
					return;
				}
				if(event.which==2){
					if(confirm("quitar cliente de lista?:"+$.toJSON(clients[i]))){
						$(input).val("").focus();
						$.ajax({
							index : j,
							type:'POST',
							url: "changeclientstatus",
							data: {
								active:false,
								id:clients[i].id,
								token : TOKEN,
		    					clientReference: CLIENT_REFERENCE
							},
							dataType: "json",
							error: function(jqXHR, textStatus, errorThrown){
								alert(textStatus);
							},
							success: function(data) {
							
							}
						});
					}
				}
				//TODO chambea esto de setear el acordion de esta forma piñata
				
				else if(confirm("cambiar el cliente puede modificar los datos")){
					console.log(clients[i].agentCode);
					if(clients[i].agentCode!=null){
						$.ajax({
							index : j,
							url: "getclientbycode",
							data: {
								hash:clients[i].agentCode,
								token : TOKEN,
								where : 'agents',
		    					clientReference: CLIENT_REFERENCE
							},
							type: 'POST',
							dataType: "json",
							error: function(jqXHR, textStatus, errorThrown){
								alert(textStatus);
							},
							success: function(data) {
								agent=new setAgent_(data);
							}
						});
					}
					client=new setClient_(clients[i]);
					console.log('client');
					console.log(client);
					//$('#vips option').eq(client.consummerType-1).attr('selected', 'selected');
					var code=client.code;
					//alert($.URLEncode(jsonsrt));
					//alert($.toJSON(productsLog));
					for(var j=0;j<productsLog.length;j++){
						var jsonsrt="["+$.toJSON(productsLog[j])+"]";
						$.ajax({
							index : j,
							url: "getthis",
							data: {
								list:encodeURIComponent(jsonsrt),
								code:code,
								token : TOKEN,
		    					clientReference: CLIENT_REFERENCE
							},
							dataType: "json",
							error: function(jqXHR, textStatus, errorThrown){
								alert(textStatus);
							},
							type:'POST',
							success: function(data) {
								console.log("data");
								console.log(data);
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

				//
			}


			$(input).val('');
			emptyCommand=true;
			//log(ui.item ? ("Selected: " + ui.item.label) : "Nothing selected, input was " + this.value);
		},
/** AUTOCOMPLETE OPEN*/
		open: function() {
			$('.ui-autocomplete').css({width:'80%',height:'auto'});
			console.log('commandline.kind='+commandline.kind);
			if(commandline.kind=='product'||commandline.kind=='retrieve'){
				var i=0;
				$('li>a.ui-corner-all').each(function(){
					var p=products;
					var description=p[i].description;
					var code=p[i].code;
					var mark=p[i].mark;
					var args=commandline.args;
					//alert("args:"+commandline.args+" commandline.argssize:"+commandline.argssize);
					for(var j=0;j<commandline.args.length;j++){
						description=description.replace(args[j].toUpperCase().replace(/\"/g,""),"<b>"+args[j].toUpperCase().replace(/\"/g,"")+"</b>");
						code=code.replace(args[j].toUpperCase().replace(/\"/g,""),"<b>"+args[j].toUpperCase().replace(/\"/g,"")+"</b>");
						mark=mark.replace(args[j].toUpperCase().replace(/\"/g,""),"<b>"+args[j].toUpperCase().replace(/\"/g,"")+"</b>");
					}
					$(this).empty().append("<div style='position: relative;width: 100%; height:auto;'>"+
							"<div class='product-attr' style='width:10%; position:absolute; top:0%; left:0%;word-wrap: break-word;'>"+code+"</div>"+
							"<div class='product-attr' style='width:60%; position:absolute; top:0%; left:10%;word-wrap: break-word;'>"+description+"</div>"+
							"<div class='product-attr' style='width:10%; position:absolute; top:0%; left:70%;word-wrap: break-word;'>"+mark+"</div>"+
							"<div class='product-attr' style='width:10%; position:absolute; top:0%; left:80%;word-wrap: break-word;'>"+p[i].unit+"</div>"+
							"<div class='product-attr' style='width:10%; position:absolute; top:0%; left:90%;word-wrap: break-word;'>"+p[i].unitPrice+"</div></div>"
					).addClass(i%2==0?"even":"odd");
					var H=0;
					$('.product-attr',this).each(function(){H<$(this).height()?H=$(this).height():0;});
					$(this).css({height:H+'px'});
					$(this).parent().css({height:H+'px'});
					i++;
				});
			}
			else if(commandline.kind=='client'||commandline.kind=='agent'||commandline.kind=='agentstatus'||commandline.kind=='clientstatus'){
				var i=0;
				$('li>a.ui-corner-all').each(function(){
					var c=clients;
					var consummer=c[i].consummer;
					var rfc=c[i].rfc;
					var code=c[i].code;
					var address=c[i].address;
					var args=commandline.args;
					var consummerType=c[i].consummerType;
					var payment=c[i].payment;
					var email=c[i].email;
					//alert("args:"+commandline.args+" commandline.argssize:"+commandline.argssize);
					for(var j=0;j<commandline.args.length;j++){
						consummer=consummer.replace(args[j].toUpperCase().replace(/\"/g,""),"<b>"+args[j].toUpperCase().replace(/\"/g,"")+"</b>");
						code=code.replace(args[j].toUpperCase().replace(/\"/g,""),"<b>"+args[j].toUpperCase().replace(/\"/g,"")+"</b>");
						rfc=rfc.replace(args[j].toUpperCase().replace(/\"/g,""),"<b>"+args[j].toUpperCase().replace(/\"/g,"")+"</b>");
						address=address.replace(args[j].toUpperCase().replace(/\"/g,""),"<b>"+args[j].toUpperCase().replace(/\"/g,"")+"</b>");
					}
					$(this).empty().append("<div style='position: relative;width: 100%; height:auto;'>"+
							"<div class='client-attr' style='width:5%; position:absolute; top:0%; left:0%;word-wrap: break-word;'>[ "+consummerType+" ]</div>"+
							"<div class='client-attr' style='width:5%; position:absolute; top:0%; left:5%;word-wrap: break-word;'>[ "+payment+" ]</div>"+
							"<div class='client-attr' style='width:30%; position:absolute; top:0%; left:10%;word-wrap: break-word;'>"+consummer+"</div>"+
							"<div class='client-attr' style='width:20%; position:absolute; top:0%; left:40%;word-wrap: break-word;'>"+rfc+"</div>"+
							"<div class='client-attr' style='width:40%; position:absolute; top:0%; left:60%;word-wrap: break-word;'>"+address+" "+email+"</div></div>"
					).addClass(i%2==0?"even":"odd");
					var H=0;
					$('.client-attr',this).each(function(){H<$(this).height()?H=$(this).height():0;});
					$(this).css({height:H+'px'});
					$(this).parent().css({height:H+'px'});
					i++;
				});
			}
			$(this).removeClass("ui-corner-all").addClass("ui-corner-top");
			//alert('down:');
			$(input).trigger(key_down);
		},
		close: function() {
			$(this).removeClass("ui-corner-top").addClass("ui-corner-all");
			if(emptyCommand)$(input).val("");
			emptyCommand=false;
		},
		delay: 0
		
	});
};
/** AUTOCOMPLETE ENDS*/
