/** COMMAND LINE FUNCTION*/
	commandLine=function(){
		//resetClient();
		console.log(productsLog);
		//TODO quita la dependencia de este popup
		
		$('#commands').autocomplete('close');
		this.value=$('#commands').val().replace(/^\s+|\s+$/g, '')//.replace(/^\s\s*/, '').replace(/\s\s*$/, '');		//value
		//clean
		//$('#commands').SetBubblePopupOptions({innerHtml:"comandos"});
		//alert("'"+this.value+"'");
		
		var splited=this.value.split(" ");
		this.command=splited[0];
		//if(command!='@s')$('#resultset').empty();
		this.kind=false;
		this.args=[];								//args
		this.pargs=[];
		this.argssize=0;
		this.getFromDB=false;
		if(this.value==""){
			return;
		}
		this.args=this.value.match(/[^\s"']+|"([^"]*)"|'([^']*)'/g).splice(1);
		this.argssize=this.args.length;
		
		if(isNumber(this.command)){
			this.kind ='product';
			this.quantity=this.command;
			for(var i=1;i<splited.length;i++)if(splited[i]!=""){
				this.args[i-1]=splited[i];
				this.argssize++;
			}
			if(this.argssize>=1)this.getFromDB=true;
			this.pargs=this.value.match(/[^\s"']+|"([^"]*)"|'([^']*)'/g);
			this.getFromDB=true;
			$('#commands').HideBubblePopup();
		}
		else if(this.command=='c'){
			this.kind ='client';
			for(var i=1;i<splited.length;i++)if(splited[i]!=""){
				this.args[i-1]=splited[i];
				this.argssize++;
			}
			if(this.argssize>=1)this.getFromDB=true;
			this.pargs=this.value.match(/[^\s"']+|"([^"]*)"|'([^']*)'/g);
			this.pargs.splice(0,1);
			this.getFromDB=true;
			$('#commands').HideBubblePopup();
		}
		else if(this.command=='a'){
			this.kind ='agent';
			/*for(var i=1;i<splited.length;i++)if(splited[i]!=""){
				this.args[i-1]=splited[i];
				this.argssize++;
			}
			if(this.argssize>=1)this.getFromDB=true;
			this.pargs=this.value.match(/[^\s"']+|"([^"]*)"|'([^']*)'/g);
			this.pargs.splice(0,1);
			*/
			this.getFromDB=true;
			$('#commands').HideBubblePopup();
		}
		else if(this.command=='ha'){
			this.kind ='agentstatus';
			this.getFromDB=true;
			$('#commands').HideBubblePopup();
		}
		else if(this.command=='hc'){
			this.kind ='clientstatus';
			this.getFromDB=true;
			$('#commands').HideBubblePopup();
		}
		else if(this.command=='@ic'||this.command=='@ia'||
				this.command=='@oc'||this.command=='@oa'||
				this.command=='$fc'||this.command=='$fa'||
				this.command=='$oc'||this.command=='$oa'){
			var liq=true;
			var msg="liquidando documento";
			if(this.argssize>0){
				liq=false;
				if(isNumber(this.args[0])){
					msg="con abono de <b>$"+this.args[0]+"</b>";
				}
				else{
					msg="ERROR, cantidad invalida <b>$"+this.args[0]+"</b>";
					this.kind='undefined';
					$('#commands').ShowBubblePopup( {innerHtml: msg} );
					$('#commands').val($('#commands').val().replace(this.args[0],""));
					return;
				}
				
			}
			if(this.command=='@ic')
				$('#commands').ShowBubblePopup( {innerHtml: 'cotizar a cliente'} );
			else if(this.command=='@ia')
				$('#commands').ShowBubblePopup( {innerHtml: 'cotizar a agente'} );
			else if(this.command=='$oc')
				$('#commands').ShowBubblePopup( {innerHtml: 'hacer pedido a cliente '+msg} );
			else if(this.command=='$oa')
				$('#commands').ShowBubblePopup( {innerHtml: 'hacer pedido a agente '+msg} );
			else if(this.command=='@oc')
				$('#commands').ShowBubblePopup( {innerHtml: 'dar credito a cliente'} );
			else if(this.command=='@oa')
				$('#commands').ShowBubblePopup( {innerHtml: 'dar credito a agente'} );
			else if(this.command=='$fc')
				$('#commands').ShowBubblePopup( {innerHtml: 'facturar a cliente '+msg} );
			else if(this.command=='$fa')
				$('#commands').ShowBubblePopup( {innerHtml: 'facturar a agente '+msg} );
			this.kind= 'sample';
		}
		else if(this.command=='@p'){
			$('#commands').ShowBubblePopup( {innerHtml: 'agregar producto'} );
			this.kind= 'editproduct';
		}
		else if(this.command=='@c'){
			$('#commands').ShowBubblePopup( {innerHtml: 'agregar cliente'} );
			this.kind= 'editclient';
		}
		else if(this.command=='@a'){
			$('#commands').ShowBubblePopup( {innerHtml: 'agregar agente'} );
			this.kind= 'editagent';
		}
		else if(this.command=='+f'){
			$('#commands').ShowBubblePopup( {innerHtml: 'facturar documento <b>'+this.args[0]+'</b>'} );
			this.kind="facture";
		}
		else if(this.command=='_inventario'){
			$('#commands').ShowBubblePopup( {innerHtml: 'se inventaríara la lista con los costos<b>'+this.args[0]+'</b>'} );
			this.kind="facture";
		}
		else if(this.command=='@r'||this.command=='@rl'){
			if(this.command=='@r'){
				$('#commands').ShowBubblePopup( {innerHtml: 'traer documento <b>'+this.args[0]+'</b>'} );
			}
			else{
				$('#commands').ShowBubblePopup( {innerHtml: 'mostrar estatus de documento <b>'+this.args[0]+'</b>'} );
			}
			this.kind="getinvoice";
		}
		else if(this.command=='%rc'){
			$('#commands').ShowBubblePopup( {innerHtml: 'escribe tu recordatorio'} );
			this.kind="makerecord";
		}
		else if(this.command=='%rr'){
			$('#commands').ShowBubblePopup( {innerHtml: '<enter> mostrar recordatorios. <espacio> n mostrar ultimos n recordatorios'} );
			this.kind="returnrecords";
		}
		else if(this.command=='%rb'){
			$('#commands').ShowBubblePopup( {innerHtml: 'marcar recordatorio como hecho'} );
			this.kind="deactivaterecord";
		}
		else if(this.command=='$a'||this.command=='$d'){
			if(this.command=='$a')
				$('#commands').ShowBubblePopup( {innerHtml: 'liquidar agente para <b>'+this.args[0]+'</b>'} );
			else if(this.command=='$d'){
				
				if(this.args[1]){
					if(isNumber(this.args[1]))
						$('#commands').ShowBubblePopup( {innerHtml: 'abonar a documento <b>'+this.args[0]+' $'+this.args[1]+'</b>'} );
					else
						$('#commands').ShowBubblePopup( {innerHtml: 'ERROR: cantidad invalida'} );
				}
				else if(this.args[0]){
					$('#commands').ShowBubblePopup( {innerHtml: 'liquidar documento <b>'+this.args[0]+'</b>'} );
				}
				else $('#commands').ShowBubblePopup( {innerHtml: 'liquidar/abonar documento'} );
			}
			this.kind="invoicepayment";
		}
		//TODO implement justprint
		else if(this.command=='@unimplemented'){
			this.kind="justprint";
		}
		else if(this.command=='$ag'){
			this.kind="incrementagentearning";
		}
		else if(this.command=='@d'){
			this.kind="discount";
		}
		else if(this.command=='@cd'){
			$('#commands').ShowBubblePopup( {innerHtml: 'cancelar documento <b>'+this.args[0]+'</b>'} );
			this.kind="canceldocument";
		}
		else if(this.command=='@s'){
			this.kind="searchinvoices";
		}
		else if(this.command=='@actualizap'){
			this.kind="updateproducts";
		}
		else if(this.command=='@pruebap'){
			this.kind="testproducts";
		}
		else if(this.command=='@j'){
			$('#commands').ShowBubblePopup( {innerHtml: 'consultar caja'} );
			this.kind="consultthebox";
		}
		else if(this.command=='%ip'){
			$('#commands').ShowBubblePopup( {innerHtml: 'inventarear productos'} );
			this.kind="productinventoryadd";
		}
		else if(this.command=='%updateproducts'){
			$('#commands').ShowBubblePopup( {innerHtml: 'update products'} );
			this.kind="updateproducts";
		}
		else if(this.command=='%adduser'){
			$('#commands').ShowBubblePopup( {innerHtml: 'add user'} );
			this.kind="adduser";
		}
		else if(this.command=='@mp'){
			$('#commands').ShowBubblePopup( {innerHtml: 'método de pago'} );
			this.kind="paymentmethod";
		}
		else {
			$('#commands').HideBubblePopup();
			if(this.command.indexOf("@")==0||
					this.command.indexOf("%")==0||
					this.command.indexOf("+")==0||
					this.command.indexOf("$")==0){
				console.log("matches");
				return;
				
			}
			console.log("matches!");
			this.quantity=1;
			this.kind= "retrieve";
			this.args=this.value.match(/[^\s"']+|"([^"]*)"|'([^']*)'/g);
			this.argssize=this.args.length;
			this.getFromDB=true;
		}
	};
