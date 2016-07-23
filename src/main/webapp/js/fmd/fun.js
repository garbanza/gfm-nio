
function Item(quantity,code,unit,mark,description,unitPrice,total){
	this.quantity=quantity;
	this.unit=unit;
	this.description=description;
	this.code=code;
	this.mark=mark;
	this.unitPrice=unitPrice;
	this.total=total;
};
randomString=function(minWords,maxWords, minLength, maxLength, kind){
    var text = '';
    var possible='';
    var low = 'abcdefghijklmnopqrstuvwxyz';
    var upp = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
    var num = '0123456789';
    if(kind.search('a')>=0)possible+=low;
    if(kind.search('A')>=0)possible+=upp;
    if(kind.search('0')>=0)possible+=num;
    var w=minWords+Math.floor(Math.random() * (maxWords-minWords));
    for( var i=0; i < w; i++ ){
    	var l=minLength+Math.floor(Math.random() * (maxLength-minLength));
    	text+=i>0?' ':'';
    	for( var j=0; j < l; j++ )
    		text += possible.charAt(Math.floor(Math.random() * possible.length));
    }
    return text;
};
setClient_=function(o){
	return new Client_(o.code, o.consummer, o.consummerType,
			o.address, o.interiorNumber, o.exteriorNumber,
			o.suburb, o.locality, o.city, o.country,
			o.state, o.email, o.cp, o.rfc, o.tel,
			o.payment, o.reference, o.aditionalReference);
	//return o;
};
Client_=function(code, consummer, consummerType,
		address, interiorNumber, exteriorNumber,
		suburb, locality, city, country,
		state, email, cp, rfc, tel,
		payment, reference, aditionalReference) {
	this.code = code?code:"";
	this.consummer = consummer?consummer:"";
	this.consummerType = consummerType?consummerType:1;
	this.address = address?address:"";
	this.interiorNumber = interiorNumber?interiorNumber:"";
	this.exteriorNumber = exteriorNumber?exteriorNumber:"";
	this.suburb = suburb?suburb:"";
	this.locality = locality?locality:"";
	this.city = city?city:"";
	this.country = country?country:"";
	this.state = state?state:"";
	this.email = email?email:"";
	this.cp = cp?cp:"";
	this.rfc = rfc?rfc:"";
	this.tel = tel?tel:"";
	this.payment = payment?payment:0;
	this.reference = reference?reference:"";
	this.aditionalReference = aditionalReference?aditionalReference:"";
	$('#client').html("[ tipo:"+this.consummerType+" ] [ credito:"+this.payment+"dias ] "+this.consummer+ " [ rfc:"+this.rfc+" ]");
	$('#client-address').html(this.consummer+" "+this.address+" "+this.interiorNumber+" "+this.exteriorNumber+" "+this.suburb);
	$('#client-city').html(this.locality+" "+this.city);
	$('#client-state').html(this.state);
	$('#client-cp').html(this.cp);
	$('#client-rfc').html(this.rfc);
	$('#client-tel').html(this.tel);
	$('#client-email').html(this.email);
	$('#client-country').html(this.country);
	document.title=this.consummer+" "+this.consummerType;
};
Agent_=function(code, consummer, consummerType,
		address, interiorNumber, exteriorNumber,
		suburb, locality, city, country,
		state, email, cp, rfc, tel,
		payment, reference, aditionalReference) {
	this.code = code?code:"";
	this.consummer = consummer?consummer:"";
	this.consummerType = consummerType?consummerType:1;
	this.address = address?address:"";
	this.interiorNumber = interiorNumber?interiorNumber:"";
	this.exteriorNumber = exteriorNumber?exteriorNumber:"";
	this.suburb = suburb?suburb:"";
	this.locality = locality?locality:"";
	this.city = city?city:"";
	this.country = country?country:"";
	this.state = state?state:"";
	this.email = email?email:"";
	this.cp = cp?cp:"";
	this.rfc = rfc?rfc:"";
	this.tel = tel?tel:"";
	this.payment = payment?payment:0;
	this.reference = reference?reference:"";
	this.aditionalReference = aditionalReference?aditionalReference:"";
	$('#agent').html("[ tipo:"+this.consummerType+" ] [ credito:"+this.payment+"dias ] "+this.consummer+ " [ rfc:"+this.rfc+" ]");
	$('#agent-address').html(this.consummer+" "+this.address+" "+this.interiorNumber+" "+this.exteriorNumber+" "+this.suburb);
	$('#agent-city').html(this.locality+" "+this.city);
	$('#agent-state').html(this.state);
	$('#agent-cp').html(this.cp);
	$('#agent-rfc').html(this.rfc);
	$('#agent-tel').html(this.tel);
	$('#agent-email').html(this.email);
	$('#agent-country').html(this.country);
	document.title=this.consummer+" "+this.consummerType;
};
setAgent_=function(o){
	new Agent_(o.code, o.consummer, o.consummerType,
			o.address, o.interiorNumber, o.exteriorNumber,
			o.suburb, o.locality, o.city, o.country,
			o.state, o.email, o.cp, o.rfc, o.tel,
			o.payment, o.reference, o.aditionalReference);
	return o;
};
	

setClient=function(code,consummer,consummerType,address,city,country,state,email,cp,rfc,tel,payment,email){
	this.id=-1;
	this.code=code;
	this.consummer=consummer;
	this.consummerType=consummerType;
	this.address=address;
	this.city=city;
	this.country=country;
	this.state=state;
	this.email=email;
	this.cp=cp;
	this.rfc=rfc;
	this.tel=tel;
	this.payment=payment;
	this.email=email;
	$('#client').html("[ "+consummerType+" ] [ "+payment+" ] "+consummer+ " [ "+rfc+" ]");
	$('#client-address').html(consummer+" "+address);
	$('#client-city').html(city);
	$('#client-state').html(state);
	$('#client-cp').html(cp);
	$('#client-rfc').html(rfc);
	$('#client-tel').html(tel);
	$('#client-email').html(email);
	$('#client-country').html(country);
	document.title=this.consummer+" "+this.consummerType;
	return this;
};

setAgent=function(code,consummer,consummerType,address,city,country,state,email,cp,rfc,tel,payment,email){
	this.id=-1;
	this.code=code;
	this.consummer=consummer;
	this.consummerType=consummerType;
	this.address=address;
	this.city=city;
	this.country=country;
	this.state=state;
	this.email=email;
	this.cp=cp;
	this.rfc=rfc;
	this.tel=tel;
	this.payment=payment;
	this.email=email;
	$('#agent').html("[ "+consummerType+" ] [ "+payment+" ] "+consummer+ " [ "+rfc+" ]");
	$('#agent-address').html(consummer+" "+address);
	$('#agent-city').html(city);
	$('#agent-state').html(state);
	$('#agent-cp').html(cp);
	$('#agent-rfc').html(rfc);
	$('#agent-tel').html(tel);
	$('#agent-email').html(email);
	$('#agent-country').html(country);
	
	return this;
};
setShopman=function(login){
	this.id=-1;
	this.login=login;
	return this;
};
setMetadata=function(invoicetype){
	this.invoicetype=invoicetype;
	return this;
};
setRequester=function(id,name,consummer){
	this.id=id;
	this.name=name;
	this.consummer=consummer;
	return this;
};
setSeller=function(id,code){
	this.id=id;
	this.code=code;
	return this;
};
setDestiny=function(id,address){
	this.id=id;
	this.address=address;
	return this;
};



(function ($) {
    $.fn.getCursorPosition = function() {
        var el = $(this).get(0);
        var pos = 0;
        if('selectionStart' in el) {
            pos = el.selectionStart;
        } else if('selection' in document) {
            el.focus();
            var Sel = document.selection.createRange();
            var SelLength = document.selection.createRange().text.length;
            Sel.moveStart('character', -el.value.length);
            pos = Sel.text.length - SelLength;
        }
        return pos;
    };
})(jQuery);

function setSelectionRange(input, selectionStart, selectionEnd) {
	if (input.setSelectionRange) {
	    input.focus();
	    input.setSelectionRange(selectionStart, selectionEnd);
	}
	else if (input.createTextRange) {
		var range = input.createTextRange();
		range.collapse(true);
		range.moveEnd('character', selectionEnd);
		range.moveStart('character', selectionStart);
		range.select();
	  }
};

function setCaretToPos (input, pos) {
	setSelectionRange(input, pos, pos);
};


invoiceInfoLog=function(invoice,node){
	var i=invoice;
	console.log(invoice);
	var agentPayed=false;
	var factured=false;
	var canceled=false;
	var cloded=false;
	var theLogs=" - | ";
	i.logs?"":i.logs=[];
	for(var j=0;j<i.logs.length;j++){
		log=i.logs[j];
		console.log('log');
		console.log(log);
		var thedate = new Date(log.date);
		var mm = thedate.getMonth() + 1;
	    var dd = thedate.getDate();
	    var yyyy = thedate.getFullYear();
	    var date = yyyy+'.'+mm + '.' + dd;
		if(log.kind==="AGENT_PAYMENT"){
			agentPayed=true;
			theLogs+="LIQUIDO AGENTE "+log.login+" " +date+" | ";
		}
		else if(log.kind==="FACTURE"){
			factured=true;
			theLogs+="FACTURADO "+log.login+" " +date+" | ";
		}
		else if(log.kind==="CANCEL"){
			canceled=true;
			theLogs+="CANCELADO "+log.login+" " +date+" | ";
		}
		else if(log.kind==="CLOSE"){
			closed=true;
			theLogs+="CERRADO "+log.login+" " +date+" | ";
		}
		else if(log.kind==="CREATED"){
			closed=true;
			theLogs+="CREADO "+log.login+" " +date+" | ";
		}
		else if(log.kind==="AGENT_INCREMENT_EARNINGS"){
			closed=true;
			theLogs+="MAS A AGENTE "+log.login+" $" +log.value+" "+date+" | ";
		}
		else if(log.kind==="PAYMENT"){
			closed=true;
			theLogs+="ENTRADA "+log.login+" $" +log.value+" "+date+" | ";
		}
	}
	theLogs+="-";
	var pastDue=i.creationTime+i.printedTo.payment*1000*60*60*24;
	var pdd = new Date(pastDue);
    var mm = pdd.getMonth() + 1;
    var dd = pdd.getDate();
    var yyyy = pdd.getFullYear();
    var date = yyyy+'.'+mm + '.' + dd;
	var pastDued=new Date().getTime()>=pastDue;
	
	var result="TIPO : "+(i.invoiceType==0?'FACTURA':(i.invoiceType==1?'PEDIDO':'COTIZACION'))+" | "+
		(canceled?"(CANCELADA)":"")+
		(closed?"(CERRADA)":"")+
		"referencia : "+i.reference+" | "+
		"valor neto : "+i.totalValue+" | "+
		"total : "+i.total+" | "+
		"agente : "+i.agent.consummer+" | "+
		"$agente : $"+i.agentPayment+""+(agentPayed?"(LIQUIDADO)":"(NO LIQUIDADO)")+" | "+
		"cliente : "+i.client.consummer+" | "+
		"facturado? : "+(factured?"SI":"NO")+" | "+
		"adeudo : $"+i.debt+" | "+
		"vence : "+date+""+(pastDued?"(VENCIDO)":"")+" | "+
		"atendio : "+i.shopman.login+" | "+
		theLogs;
	
	var consummerObj={content:result};
	var defaultNode='#logResultset';
	if(node!=null)defaultNode=node;
	var pos=$(node).children().length%2==0;
	var bgcolor=pos?"#ffffff":'#d9d9d9';
	if(!agentPayed)bgcolor=pos?"#fbff8d":'#dcdf7c';
	if(i.debt>0)bgcolor=pos?"#FFC2C2":'#d5a2a2';
	if(agentPayed)bgcolor=pos?"#95ff9a":'#79cf7d';
	if(i.invoiceType==2)bgcolor=pos?"#ffffff":'#d9d9d9';//COTIZACION
	if(canceled)bgcolor="#616161";
	$(defaultNode).preInCapsule(consummerObj,'com.ferremundo.cps.GenericDiv').addClass('box fleft').css('background-color',bgcolor);

};
nodeLog=function(src,node,classes){
	$(node).preInCapsule({content:src},'com.ferremundo.cps.GenericDiv').addClass(classes);
};
	
clientauthenticateLP=function () {
	var ret=false; 
	$.ajax({
		url: "clientauthenticate",
		dataType: "json",
		type: 'POST',
		async: false,
		data: {
			login: $('#login').val(),
			password: $('#password').val(),
			token: TOKEN,
			clientReference: CLIENT_REFERENCE
		},
		success: function(data) {
			//console.log(data.authenticated);
			shopman=data.shopman;
			AUTHORIZED=true;
			$('#login').val('');
			$('#password').val('');
			$('#login-form').dialog('close');
			$('#commands').focus();
			$('#lockbutton').html('lock : '+shopman.name);
			$.idleTimer(1000*60*5);
			$(document).bind("idle.idleTimer", function(){
				lock();
			});
			$( "#lock-form" ).dialog('option','title','Locked - '+shopman.name);
			if(onlineClientHasAccess('SHOPMAN_CREATE')){
				addRegisterButton();
			}
			else console.log('no access to SHOPMAN_CREATE');
			ret=true;
		},
		error: function(jqXHR, textStatus, errorThrown){
			alert(errorThrown);
			//$( "#unauthorizedAlert" ).dialog('open');
			$('#login').focus();
			//$(this).closest('.ui-dialog').find('.ui-dialog-buttonpane button:has(Ok)').focus();
			//$( "#unauthorizedAlert" ).focus();
		}
	});
	return ret;
};
clientauthenticateP=function () {
	var ret=false;
	$.ajax({
		url: "clientauthenticate",
		dataType: "json",
		type:'POST',
		async:false,
		data: {
			password: $('#unlockpassword').val(),
			token : TOKEN,
			clientReference: CLIENT_REFERENCE
		},
		success: function(data) {
			AUTHORIZED=true;
			$('#unlockpassword').val('');
			$('#lock-form').dialog('close');
			$('#commands').focus();
			ret=true;
		},
		error: function(jqXHR, textStatus, errorThrown){
			alert(jqXHR.status+" "+textStatus+" : "+jqXHR.responseText);
			//$( "#unauthorizedAlert" ).dialog('open');
			$('#unlockpassword').focus();
			//$($( "#unauthorizedAlert" ).dialog("option", "buttons")['Ok']).focus();
			//
		}
	});
	return ret;
};

addRegisterButton=function(){
	$('body').append("<button type='button' id='registerButton'>registrar usuario</button>");
	$('#registerButton').bind('click',function(){
		
		if(onlineClientHasAccess('SHOPMAN_CREATE')){
			verifyin(
					function(){
						registerShopmanIn();
					},
					true
			);/*
			var passinid='passid'+$.capsule.randomString(1,15,'aA0');
			passin({
				id:passinid,
				success:function(data){
					$('#'+passinid).remove();
					registerShopmanIn();
					
					//alert("success " +data.authenticated+". #"+passinid+" to be removed")
					
				},
				message:"verificar "+SHOPMAN,
				escape:true
			});
			$('#verify-form').dialog("open");
			$('#verify-').hide();
			$('#verifyPassword').hide();
			*/
		}
	});
};

onlineClientHasAccess=function(permission){
	var ret=false;
	$.ajax({
		url: "clienthasaccess",
		dataType: "json",
		type:'POST',
		async:false,
		data: {
			permission: permission,
			token: TOKEN,
			clientReference: CLIENT_REFERENCE
		},
		success: function(data) {
			ret=data.hasAccess;
		}
	});
	return ret;
};

clientauthenticateR=function () {
	var ret=false;
	$.ajax({
		url: "clientauthenticate",
		dataType: "json",
		type:'POST',
		async:false,
		data: {
			password: $('#verifypassword').val(),
			token : TOKEN,
			clientReference: CLIENT_REFERENCE
		},
		success: function(data) {
			AUTHORIZED=true;
			$('#verifypassword').val('');
			$('#verify-form').dialog('close');
			$('#commands').focus();
			ret=true;
		},
		error: function(jqXHR, textStatus, errorThrown){
			alert(jqXHR.status+" "+textStatus+" : "+jqXHR.responseText);
			
			//$( "#unauthorizedAlert" ).dialog('open');
			$('#unlockpassword').focus();
			//$($( "#unauthorizedAlert" ).dialog("option", "buttons")['Ok']).focus();
			//
		}
	});
	return ret;
};

authenticate_=function (pwd) {
	var ret=false;
	$.ajax({
		url: "clientauthenticate",
		dataType: "json",
		type:'POST',
		async:false,
		data: {
			password: pwd,
			token : TOKEN,
			clientReference: CLIENT_REFERENCE
		},
		success: function(data) {
			AUTHORIZED=true;
			ret=true;
		},
		error: function(jqXHR, textStatus, errorThrown){
			alert(jqXHR.status+" "+textStatus+" : "+jqXHR.responseText);
			
			//$( "#unauthorizedAlert" ).dialog('open');
			//$('#unlockpassword').focus();
			//$($( "#unauthorizedAlert" ).dialog("option", "buttons")['Ok']).focus();
			//
		}
	});
	return ret;
};


registerShopman=function () {
	var ret=false;
	$.ajax({
		url: "clientauthenticate",
		dataType: "json",
		type:'POST',
		async:false,
		data: {
			newUserName: $('#newUserName').val(),
			newUserLogin: $('#newUserLogin').val(),
			newUserPassword: $('#newUserPassword').val(),
			reNewUserPassword: $('#reNewUserPassword').val(),
			token : TOKEN,
			clientReference: CLIENT_REFERENCE
		},
		success: function(data) {
			//alert("creado :"+ $('#newUserName').val())
			$('#newUserName').val('');
			$('#newUserLogin').val('');
			$('#newUserPassword').val('');
			$('#reNewUserPassword').val('');
			$('#commands').focus();
			ret=true;
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
	return ret;
};

function authenticate(){
	$( "#login-form" ).dialog('open');
	$('#login').val('');
	$('#password').val('');
};


function lock(){
	AUTHORIZED=false;
	$.ajax({
		url: "clientauthenticate",
		dataType: "json",
		type:'POST',
		token : TOKEN,
		data: {
				lock: true,
				token:TOKEN,
				clientReference: CLIENT_REFERENCE
		}
	});
	$( "#lock-form" ).dialog('open');
	

};

function dolog(quantity,unit,description,code,mark,unitPrice){
	if(!quantity)quantity=1;
	if(!unitPrice)unitPrice=0;
	$("#log").sexytable({
		row:[
				{content: "<div class='quantity'>"+quantity+"</div>", width:5},
				{content: "<div class='unit'>"+unit+"</div>", width:10},
				{content: "<div class='description'>"+description+"</div>", width:40},
				{content: "<div class='code'>"+code+"</div>", width:10},
				{content: "<div class='mark'>"+mark+"</div>", width:10},
				{content: "<div class='unitPrice'>"+unitPrice+"</div>", width:10},
				{content: "<div class='total'>"+(quantity*unitPrice)+"</div>", width:10}
				],
		animate:0,
		class_:"tableingrow"
	});
	var row=$('.tableingrow').get(0);
	$('.quantity, .unitPrice, .description, .unit, .code, .mark',row)
	.editable({closeOnEnter : true, event : 'click', callback : function() {onLogChange();}});
	/*$('.unitPrice',row).editable();
	$('.description',row).editable();
	$('.unit',row).editable();
	$('.code',row).editable();
	$('.mark',row).editable(); */
	
	$('*',row).bind("contextmenu",function(e) {
    	e.preventDefault();
	});
	/*$(".unitPrice, .description, .unit, .code, .mark",row).bind('DOMSubtreeModified',function(e){
		var thisrow=$(e.target).parent().parent().get(0);
		var index=$('.tableingrow').index(thisrow);
		alert("index="+index);
		productsLog[index].key=-1;
		productsLog[index].code=productsLog[index].code.replace(/_/gi,"");+"_";
		$('#log').sexytable({'editedRow':true,'index':thisrow});
		onLogChange();
	});
	$(".quantity",row).bind('DOMSubtreeModified',function(e){
		//var row=$(e).parent().parent();
		var index=$('.tableingrow').index(row);
		productsLog[index].quantity=parseFloat($($('.quantity',row).get(0)).html());
		onLogChange();
	});
	*/
};

function rendLog(){
	
};

function onLogChange(){
	var q=[];
	var up=[];
	var i=0;
	
	$('.quantity').each(function(){
		q[i]=parseFloat($(this).text());
		i++;
	});
	i=0;
	$('.unitPrice').each(function(){
		up[i]=parseFloat($(this).text());
		i++;
	});
	i=0;
	var total=0;
	$('.total').each(function(){
		//$(this).html((q[i]*up[i]).toFixed(2));
		$(this).html((q[i]*up[i]).toFixed(2));
		total+=q[i]*up[i];
		i++;
	});
	$('.g-total').html("$ "+total.toFixed(2));
	$(".tableingrow").unbind('mousedown').mousedown(function(e){
		var index=$('.tableingrow').index(this);
		if(e.which==2){
			productsLog.splice(index,1);
			$('#log').sexytable({'removeRow':true,'index':this});
			onLogChange();
		}
		else if(e.which==3){
			if(productsLog[index].disabled){
				productsLog[index].disabled=false;
				$('#log').sexytable({'enabledRow':true,'index':this});
			}
			else {
				productsLog[index].disabled=true;
				$('#log').sexytable({'disabledRow':true,'index':this});
			}
			onLogChange();
		}
	});

	$(".tableingrow").unbind('DOMSubtreeModified').bind('DOMSubtreeModified',function(e){
		//alert("clicked "+ event.target);
		var index=$('.tableingrow').index(this);
		if($('.unitPrice, .description, .unit, .code, .mark',this).is(e.target)){
			productsLog[index].id=-1;
			productsLog[index].unitPrice=$($('.unitPrice',this).get(0)).html();
			productsLog[index].description=$($('.description',this).get(0)).html();
			productsLog[index].unit=$($('.unit',this).get(0)).html();
			productsLog[index].code=$($('.code',this).get(0)).html().replace(".","")+".";
			productsLog[index].mark=$($('.mark',this).get(0)).html();
			productsLog[index].edited=true;
			$('#log').sexytable({'editedRow':true,'index':this});
			//onLogChange();
			//alert("edited");
			/*if($('.unitPrice',this).is(e.target)){
				productsLog[index].unitPrice=$($('.unitPrice',this).get(0)).html();
				//alert("unitPrice "+e.target.nodeName);
			}*/
		}
		if($('.quantity',this).is(e.target)){
			productsLog[index].quantity=parseFloat($($('.quantity',this).get(0)).html());
			//alert("quantity "+e.target.nodeName+"\n"+$($('.quantity',this).get(0)).html());
		}
		//onLogChange();
		
	});
	total=0;
	for(var i=0;i<productsLog.length;i++)total+=productsLog[i].disabled?0:parseFloat(productsLog[i].quantity)*parseFloat(productsLog[i].unitPrice);
	$('.g-total2').html("$ "+total.toFixed(2));
	var length_=$(".tableingrow").length;
	$('.g-area-to-print').html(length_+" -> "+Math.ceil(length_/17)+" hojas");
	
	
};
logToJson=function(){
	var json="{";
	p=productsLog;
	for(var i=0;i<p.length;i++){
		json+="\""+p[i].id+"\"";
	}
};
function isNumber(n) {
	  return !isNaN(parseFloat(n)) && isFinite(n);
};

var sample=function(){
	return $('#sample').is(':checked');
};
resetClient=function(){
	productsLog=[];
	new setAgent_("-1","agente indefinido",".",".",".",".",".",".",".",".",".",".",".");
	new setClient_("-1","cliente indefinido",".",".",".",".",".",".",".",".",".",".",".");
	agent=null;
	//client=null;
	$('#log').empty();
	$('#paymentMethod').val('');
	onLogChange();
	$("#commands").val('');
	$("#commands").focus();
	$("#records").empty();
	$.ajax({
		url: CONTEXT_PATH+'/dbport',
		type:'POST',
		data: {
			command:"returnrecords",
			args: "",
			requestNumber: REQUEST_NUMBER,
			consummerType: client?client.consummerType:1,
			token : TOKEN,
			clientReference: CLIENT_REFERENCE
		},
		success: function(data){
			//alert(data.message);
			console.log(data);
			var rs=data.records;
			for(var i=0;i<rs.length;i++){
				var r=rs[i];
				console.log(r);
				var pdd = new Date(r.creationTime);
			    var mm = pdd.getMonth() + 1;
			    var dd = pdd.getDate();
			    var yyyy = pdd.getFullYear();
			    var date = yyyy+'.'+mm + '.' + dd;
				var src="<div id=record-"+r.id+">"+r.id+" | "+date+" | "+r.text+"</div>";
				//$('#records').empty();
				nodeLog(src,"#records","box fleft");
			}
			
		},
		error : function(jqXHR, textStatus, errorThrown){
			alert("el sistema dice:( "+textStatus+" - "+errorThrown+" - "+jqXHR.responseText);
		}
	});
	
};


/*
 * Date Format 1.2.3
 * (c) 2007-2009 Steven Levithan <stevenlevithan.com>
 * MIT license
 *
 * Includes enhancements by Scott Trenda <scott.trenda.net>
 * and Kris Kowal <cixar.com/~kris.kowal/>
 *
 * Accepts a date, a mask, or a date and a mask.
 * Returns a formatted version of the given date.
 * The date defaults to the current date/time.
 * The mask defaults to dateFormat.masks.default.
 */

dateFormat = function () {
	var	token = /d{1,4}|m{1,4}|yy(?:yy)?|([HhMsTt])\1?|[LloSZ]|"[^"]*"|'[^']*'/g,
		timezone = /\b(?:[PMCEA][SDP]T|(?:Pacific|Mountain|Central|Eastern|Atlantic) (?:Standard|Daylight|Prevailing) Time|(?:GMT|UTC)(?:[-+]\d{4})?)\b/g,
		timezoneClip = /[^-+\dA-Z]/g,
		pad = function (val, len) {
			val = String(val);
			len = len || 2;
			while (val.length < len) val = "0" + val;
			return val;
		};

	// Regexes and supporting functions are cached through closure
	return function (date, mask, utc) {
		var dF = dateFormat;

		// You can't provide utc if you skip other args (use the "UTC:" mask prefix)
		if (arguments.length == 1 && Object.prototype.toString.call(date) == "[object String]" && !/\d/.test(date)) {
			mask = date;
			date = undefined;
		}

		// Passing date through Date applies Date.parse, if necessary
		date = date ? new Date(date) : new Date;
		if (isNaN(date)) throw SyntaxError("invalid date");

		mask = String(dF.masks[mask] || mask || dF.masks["default"]);

		// Allow setting the utc argument via the mask
		if (mask.slice(0, 4) == "UTC:") {
			mask = mask.slice(4);
			utc = true;
		}

		var	_ = utc ? "getUTC" : "get",
			d = date[_ + "Date"](),
			D = date[_ + "Day"](),
			m = date[_ + "Month"](),
			y = date[_ + "FullYear"](),
			H = date[_ + "Hours"](),
			M = date[_ + "Minutes"](),
			s = date[_ + "Seconds"](),
			L = date[_ + "Milliseconds"](),
			o = utc ? 0 : date.getTimezoneOffset(),
			flags = {
				d:    d,
				dd:   pad(d),
				ddd:  dF.i18n.dayNames[D],
				dddd: dF.i18n.dayNames[D + 7],
				m:    m + 1,
				mm:   pad(m + 1),
				mmm:  dF.i18n.monthNames[m],
				mmmm: dF.i18n.monthNames[m + 12],
				yy:   String(y).slice(2),
				yyyy: y,
				h:    H % 12 || 12,
				hh:   pad(H % 12 || 12),
				H:    H,
				HH:   pad(H),
				M:    M,
				MM:   pad(M),
				s:    s,
				ss:   pad(s),
				l:    pad(L, 3),
				L:    pad(L > 99 ? Math.round(L / 10) : L),
				t:    H < 12 ? "a"  : "p",
				tt:   H < 12 ? "am" : "pm",
				T:    H < 12 ? "A"  : "P",
				TT:   H < 12 ? "AM" : "PM",
				Z:    utc ? "UTC" : (String(date).match(timezone) || [""]).pop().replace(timezoneClip, ""),
				o:    (o > 0 ? "-" : "+") + pad(Math.floor(Math.abs(o) / 60) * 100 + Math.abs(o) % 60, 4),
				S:    ["th", "st", "nd", "rd"][d % 10 > 3 ? 0 : (d % 100 - d % 10 != 10) * d % 10]
			};

		return mask.replace(token, function ($0) {
			return $0 in flags ? flags[$0] : $0.slice(1, $0.length - 1);
		});
	};
}();

// Some common format strings
dateFormat.masks = {
	"default":      "ddd mmm dd yyyy HH:MM:ss",
	shortDate:      "m/d/yy",
	mediumDate:     "mmm d, yyyy",
	longDate:       "mmmm d, yyyy",
	fullDate:       "dddd, mmmm d, yyyy",
	shortTime:      "h:MM TT",
	mediumTime:     "h:MM:ss TT",
	longTime:       "h:MM:ss TT Z",
	isoDate:        "yyyy-mm-dd",
	isoTime:        "HH:MM:ss",
	isoDateTime:    "yyyy-mm-dd'T'HH:MM:ss",
	isoUtcDateTime: "UTC:yyyy-mm-dd'T'HH:MM:ss'Z'"
};

// Internationalization strings
dateFormat.i18n = {
	dayNames: [
		//"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat",
		"dom", "Lun", "Mar", "Mie", "Jue", "Vie", "Sab",
		//"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
		"Domingo", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado"
	],
	monthNames: [
		//"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
		"Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic",
		//"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
		"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
	]
};

// For convenience...
Date.prototype.format = function (mask, utc) {
	return dateFormat(this, mask, utc);
};

function typeOf(value) {
    var s = typeof value;
    if (s === 'object') {
        if (value) {
            if (value instanceof Array) {
                s = 'array';
            }
        } else {
            s = 'null';
        }
    }
    return s;
};

function applyDiscount(){
	if(''==$('#consummerDiscount').val())$('#consummerDiscount').val(0)
	for(var j=0;j<productsLog.length;j++){
		var jsonsrt="["+$.toJSON(productsLog[j])+"]";
		$.ajax({
			index : j,
			url: "getthis",
			data: {
				list:encodeURIComponent(jsonsrt),
				code:client.code,
				token : TOKEN,
				clientReference: CLIENT_REFERENCE,
				consummerDiscount : $('#consummerDiscount').val()
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
