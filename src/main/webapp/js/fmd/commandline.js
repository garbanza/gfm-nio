/** COMMAND LINE FUNCTION*/
	commandLine = function(input){
		//resetClient();
		console.log(productsLog);
		//TODO quita la dependencia de este popup
		
		$(input).autocomplete('close');
		this.value=$(input).val().replace(/^\s+|\s+$/g, '')//.replace(/^\s\s*/, '').replace(/\s\s*$/, '');		//value
		//clean
		//$(input).SetBubblePopupOptions({innerHtml:"comandos"});
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
		var argsRegex=/[^\s"']+|"([^"]*)"|'([^']*)'/g;
		do {
		    //Each call to exec returns the next regex match as an array
		    var matches = argsRegex.exec(this.value);
		    if (matches != null)
		    {
		        //Index 1 in the array is the captured group if it exists
		        //Index 0 is the matched text, which we use if no captured group exists
		    	this.args.push(matches[1] ? matches[1] : matches[0]);
		    }
		} while (matches != null);
		this.args = this.args.splice(1);
		//this.args=this.value.match(/[^\s"']+|"([^"]*)"|'([^']*)'/g).splice(1);
		this.argssize=this.args.length;
		
		if(isNumber(this.command)){
			this.kind ='product';
			this.quantity=this.command;
			for(var i=1;i<splited.length;i++)if(splited[i]!=""){
				this.args[i-1]=splited[i];
				this.argssize++;
			}
			if(this.argssize > 0){
				this.getFromDB=true;
				this.kind ='product';
				this.quantity=this.command;
			}
			else {
				this.kind ='undefined';
			}
			//this.pargs=this.value.match(/[^\s"']+|"([^"]*)"|'([^']*)'/g);
			//this.getFromDB=true;
			//$(input).HideBubblePopup();
		}
		else if(this.command=='c'){
			this.kind ='client';
			for(var i=1;i<splited.length;i++)if(splited[i]!=""){
				this.args[i-1]=splited[i];
				this.argssize++;
			}
			if(this.argssize>=1)this.getFromDB=true;
			else this.kind = undefined;
			this.pargs=this.value.match(/[^\s"']+|"([^"]*)"|'([^']*)'/g);
			this.pargs.splice(0,1);
			this.getFromDB=true;
			$(input).HideBubblePopup();
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
			$(input).HideBubblePopup();
		}
		else if(this.command=='ha'){
			this.kind ='agentstatus';
			this.getFromDB=true;
			$(input).HideBubblePopup();
		}
		else if(this.command=='hc'){
			this.kind ='clientstatus';
			this.getFromDB=true;
			$(input).HideBubblePopup();
		}
		/*else if(//this.command=='@ic'||this.command=='@ia'||
				//this.command=='@oc'||this.command=='@oa'||
				//this.command=='$fc'||//this.command=='$fa'||
				//this.command=='$oc'||//this.command=='$oa'||
				this.command=='@ecot'||this.command=='@pcot'||
				this.command=='$ef'){
			this.kind= 'sample';
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
					$(input).ShowBubblePopup( {innerHtml: msg} );
					$(input).val($(input).val().replace(this.args[0],""));
					return;
				}
				
			}
		}*/
		else if(this.command=='$fc'||this.command=='$ef'){
			if(this.args[0]){
				if(isNumber(this.args[0]) && this.args[0]*1 >= 0){
					this.printCopies=this.args[0];
					this.kind = "emitinvoice";
					if(this.command=='$ef')this.printCopies=0;
				}
				else this.kind="undefinedcommand"
			}
			else {
				this.printCopies=-1;
				if(this.command=='$ef')this.printCopies=0;
				this.kind = "emitinvoice";
			}
		}
		else if(this.command=='$tfc'){
			if(this.args[0]){
				if(isNumber(this.args[0]) && this.args[0]*1 >= 0){
					this.printCopies=this.args[0];
					this.kind = "emitinvoiceticket";
				}
				else this.kind="undefinedcommand"
			}
			else {
				this.printCopies=-1;
				this.kind = "emitinvoiceticket";
			}
		}
		else if(this.command=='$oc'){
			if(this.args[0]){
				if(isNumber(this.args[0]) && this.args[0]*1 >= 0){
					this.printCopies=this.args[0];
					this.kind = "emitorder";
				}
				else this.kind="undefinedcommand"
			}
			else {
				this.printCopies=-1;
				this.kind = "emitorder";
			}
		}
		else if(this.command=='$toc'){
			if(this.args[0]){
				if(isNumber(this.args[0]) && this.args[0]*1 >= 0){
					this.printCopies=this.args[0];
					this.kind = "emitorderticket";
				}
				else this.kind="undefinedcommand"
			}
			else {
				this.printCopies=-1;
				this.kind = "emitorderticket";
			}
		}
		else if(this.command=='@ecot'||this.command=='@pcot'){
			if(this.args[0]){
				if(isNumber(this.args[0]) && this.args[0]*1 >= 0){
					this.printCopies=this.args[0];
					this.kind = "emitsample";
					if(this.command=='@ecot')this.printCopies=0;
				}
				else this.kind="undefinedcommand"
			}
			else {
				this.printCopies=-1;
				if(this.command=='@ecot')this.printCopies=0;
				this.kind = "emitsample";
			}
		}
		else if(this.command=='@tpcot'){
			if(this.args[0]){
				if(isNumber(this.args[0]) && this.args[0]*1 >= 0){
					this.printCopies=this.args[0];
					this.kind = "emitsampleticket";
				}
				else this.kind="undefinedcommand"
			}
			else {
				this.printCopies=-1;
				this.kind = "emitsampleticket";
			}
		}
		else if(this.command=='@print'){
			if(this.args[0]){
				if(isNumber(this.args[1]) && this.args[1]*1 >= 0){
					this.printCopies=this.args[1];
					this.documentReference = this.args[0];
					this.kind = "print";
				}
				else this.kind="undefinedcommand"
			}
			else {
				this.kind="undefinedcommand"
			}
		}
		else if(this.command=='@tprint'){
			if(this.args[0]){
				if(isNumber(this.args[1]) && this.args[1]*1 >= 0){
					this.printCopies=this.args[1];
					this.documentReference = this.args[0];
					this.kind = "tprint";
				}
				else this.kind="undefinedcommand"
			}
			else {
				this.kind="undefinedcommand"
			}
		}
		else if(this.command=='@mail'){
			if(this.args[0]){
				if(isNumber(this.args[0])){
					this.documentReference = this.args[0];
					this.kind = "mail";
					this.recipients = "";
					for(var i = 1; i < this.args.length; i++){
						this.recipients += this.args[i]+(i==(this.args.length-1)?"":" ");
					}
				}
				else this.kind="undefinedcommand"
			}
			else {
				this.kind="undefinedcommand"
			}
		}
		else if(this.command=='@h'){
			this.kind = "help";
		}
		else if(this.command=='@p'){
			$(input).ShowBubblePopup( {innerHtml: 'agregar producto'} );
			this.kind= 'editproduct';
		}
		else if(this.command=='@c'){
			$(input).ShowBubblePopup( {innerHtml: 'agregar cliente'} );
			this.kind= 'editclient';
		}
		else if(this.command=='@a'){
			$(input).ShowBubblePopup( {innerHtml: 'agregar agente'} );
			this.kind= 'editagent';
		}
		else if(this.command=='+f'){
			$(input).ShowBubblePopup( {innerHtml: 'facturar documento <b>'+this.args[0]+'</b>'} );
			this.kind="facture";
		}
		else if(this.command=='_inventario'){
			$(input).ShowBubblePopup( {innerHtml: 'se inventaríara la lista con los costos<b>'+this.args[0]+'</b>'} );
			this.kind="facture";
		}
		else if(this.command=='@r'||this.command=='@rl'){
			if(this.command=='@r'){
				$(input).ShowBubblePopup( {innerHtml: 'traer documento <b>'+this.args[0]+'</b>'} );
			}
			else{
				$(input).ShowBubblePopup( {innerHtml: 'mostrar estatus de documento <b>'+this.args[0]+'</b>'} );
			}
			this.kind="getinvoice";
		}
		else if(this.command=='%rc'){
			if(this.args.length > 0){
				this.kind="makerecord";
				this.message = this.args.join(" ");
			}
			else this.kind="undefined";
		}
		else if(this.command=='%rr'){
			$(input).ShowBubblePopup( {innerHtml: '<enter> mostrar recordatorios. <espacio> n mostrar ultimos n recordatorios'} );
			this.kind="returnrecords";
		}
		else if(this.command=='%rb'){
			$(input).ShowBubblePopup( {innerHtml: 'marcar recordatorio como hecho'} );
			this.kind="deactivaterecord";
		}
		else if(this.command=='$a'||this.command=='$d'){
			if(this.command=='$a')
				$(input).ShowBubblePopup( {innerHtml: 'liquidar agente para <b>'+this.args[0]+'</b>'} );
			else if(this.command=='$d'){
				
				if(this.args[1]){
					if(isNumber(this.args[1]))
						$(input).ShowBubblePopup( {innerHtml: 'abonar a documento <b>'+this.args[0]+' $'+this.args[1]+'</b>'} );
					else
						$(input).ShowBubblePopup( {innerHtml: 'ERROR: cantidad invalida'} );
				}
				else if(this.args[0]){
					$(input).ShowBubblePopup( {innerHtml: 'liquidar documento <b>'+this.args[0]+'</b>'} );
				}
				else $(input).ShowBubblePopup( {innerHtml: 'liquidar/abonar documento'} );
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
			if(this.args[0]){
				if(isNumber(this.args[0])){
					this.documentReference = this.args[0];
					this.kind = "canceldocument";
				}
				else this.kind="undefinedcommand"
			}
			else {
				this.kind="undefinedcommand"
			}
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
			$(input).ShowBubblePopup( {innerHtml: 'consultar caja'} );
			this.kind="consultthebox";
		}
		else if(this.command=='%ip'){
			$(input).ShowBubblePopup( {innerHtml: 'inventarear productos'} );
			this.kind="productinventoryadd";
		}
		else if(this.command=='%updateproducts'){
			$(input).ShowBubblePopup( {innerHtml: 'update products'} );
			this.kind="updateproducts";
		}
		else if(this.command=='%adduser'){
			$(input).ShowBubblePopup( {innerHtml: 'add user'} );
			this.kind="adduser";
		}
		else if(this.command=='@fp'){
			$(input).ShowBubblePopup( {innerHtml: 'forma de pago'} );
			this.kind="paymentway";
		}
		else if(this.command=='@mp'){
			$(input).ShowBubblePopup( {innerHtml: 'forma de pago'} );
			this.kind="paymentmethod";
		}
		else if(this.command=='@tc'){
			$(input).ShowBubblePopup( {innerHtml: 'tipo de documento'} );
			this.kind="documenttype";
		}
		else if(this.command=='@dest'){
			$(input).ShowBubblePopup( {innerHtml: 'destino/obra'} );
			this.kind="destiny";
		}
		else if(this.command=='@uc'){
			$(input).ShowBubblePopup( {innerHtml: 'uso de cfdi'} );
			this.kind="cfdiuse";
		}
		else if(this.command=='@ad'){
			$(input).ShowBubblePopup( {innerHtml: 'absolutediscount'} );
			this.kind="absolutediscount";
		}
		else if(this.command=='@pago'){
			this.kind="invoicepayment";
		}
		else if(this.command=='%fixdb'){
			if(this.args[0]){
				if(isNumber(this.args[0])){
					this.fixNumber = this.args[0];
					this.kind = "fixdb";
				}
				else this.kind="undefinedcommand"
			}
			else {
				this.kind="undefinedcommand"
			}
		}
		else if(this.command.indexOf("@")==0&&isNumber(this.command.replace("@",""))){
			this.kind="edititem";
			this.itemNumber=this.command.replace("@","")*1;
		}
		else {
			$(input).HideBubblePopup();
			if(this.command.indexOf("@")==0||
					this.command.indexOf("%")==0||
					this.command.indexOf("+")==0||
					this.command.indexOf("$")==0){
				console.log("matches");
				return;
				
			}
			console.log("matches!");
			console.log(this);
			this.quantity=1;
			this.kind= "retrieve";
			this.args=this.value.match(/[^\s"']+|"([^"]*)"|'([^']*)'/g);
			this.argssize=this.args.length;
			this.getFromDB=true;
		}
	};
