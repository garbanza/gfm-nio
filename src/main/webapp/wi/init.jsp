
<%@page import="com.ferremundo.stt.GSettings"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="js/URLEncode.js"></script>
<script type="text/javascript" src="js/jquery.capsule.js"></script>

<script type="text/javascript" src="js/jquery.sexytable-1.1.js"></script>

<script type="text/javascript" src="js/jquery-ui-1.9.0.custom.min.js"></script>
<script type="text/javascript" src="js/jquery-ui-1.8.4.custom.min.js"></script>
<script type="text/javascript" src="js/jquery.ui.autocomplete.event.js"></script>

<script type="text/javascript" src="js/jquery.json-2.2.js"></script>
<!--script type="text/javascript" src="js/jquery.editable.js"></script-->
<script type="text/javascript" src="js/jquery.victorjonsson.editable.js"></script>
<script type="text/javascript" src="js/jquery.caret.1.02.min.js"></script>

<script type="text/javascript" src="js/cps/SysAuth.js"></script>
<script type="text/javascript" src="cps/tableing.cps.js"></script>
<script type="text/javascript" src="cps/global.cps.js"></script>

<script type="text/javascript" src="js/jquery.idle.min.js"></script>
<script type="text/javascript" src="js/jquery-bubble-popup-v3.min.js"></script>

<script type="text/javascript" src="js/fmd/util.js"></script>
<script type="text/javascript" src="js/fmd/commandline.js"></script>
<script type="text/javascript" src="js/fmd/autocomplete.js"></script>
<script type="text/javascript" src="js/fmd/fun.js"></script>
<script type="text/javascript" src="js/fmd/auth.js"></script>

<script type="text/javascript" src="js/jspdf/jspdf.js"></script>
<script type="text/javascript"
	src="js/jspdf/jspdf.plugin.standard_fonts_metrics.js"></script>
<script type="text/javascript"
	src="js/jspdf/jspdf.plugin.split_text_to_size.js"></script>
<script type="text/javascript" src="js/jspdf/jspdf.plugin.from_html.js"></script>

<script type="text/javascript" src="js/fmd/jquery.block-ui.js"></script>

<link rel="stylesheet" type="text/css" href="css/layout.css">
<link rel="stylesheet" type="text/css"
	href="css/jquery-ui-1.8.4.cupertino.css" />
<link rel="stylesheet" type="text/css"
	href="css/jquery-bubble-popup-v3.css" />
<link rel="stylesheet" type="text/css" href="css/jquery.msg.css">

<script type="text/javascript">
	var REQUEST_NUMBER = 0;

	var TOKEN = "${token}";
	var CLIENT_REFERENCE = "${clientReference}";
	var SHOPMAN = ${shopman};
	var CONTEXT_PATH = "${pageContext.request.contextPath}";
	var AUTHORIZED = true;
	var TAXES_VALUE = ${GSettings.get("TAXES_VALUE")} * 1;
	
	var shopman = SHOPMAN;
	var inputValue;
	var products;
	var productsLog = [];
	var clients;
	var CLIENT = null;
	var AGENT = null;
	//var shopman=new setShopman("unauthorized");
	var metadata = new setMetadata(1);// INVOICE_TYPE_ORDER
	var requester = new setRequester(-1, "publico", "publico");
	var seller = new setSeller(-1, "none");
	var destiny = new setDestiny(-1, "mostrador");
	var commandline;
	var emptyCommand = false;
	var key_down = $.Event("keydown.autocomplete");
	key_down.keyCode = $.ui.keyCode.DOWN;
	var keyup = $.Event("keydown.autocomplete");
	keyup.keyCode = $.ui.keyCode.UP;
	var keyenter = $.Event("keydown.autocomplete");
	keyenter.keyCode = $.ui.keyCode.ENTER;
	var keyenterUp = $.Event("keyup.autocomplete");
	keyenterUp.keyCode = $.ui.keyCode.ENTER;
	CLIENT=new Client_(
			"-1",
			'${GSettings.get("CONSUMER_NAME")}',
			1,
			'',
			'',
			'',
			'',
			'',
			'',
			'${GSettings.get("CONSUMER_FISCAL_RESIDENCY")}',
			'',
			'',
			'',
			'${GSettings.get("CONSUMER_TAX_CODE")}',
			"",
			0,
			"",
			"",
			'${GSettings.get("CONSUMER_CFDI_USE")}');
	AGENT=new Agent_(
			"-1",
			'${GSettings.get("INVOICE_SENDER_SOCIAL_REASON")}',
			1,
			'${GSettings.get("INVOICE_SENDER_ADDRESS")}',
			'${GSettings.get("INVOICE_SENDER_INTERIOR_NUMBER")}',
			'${GSettings.get("INVOICE_SENDER_EXTERIOR_NUMBER")}',
			'${GSettings.get("INVOICE_SENDER_SUBURB")}',
			'${GSettings.get("INVOICE_SENDER_LOCALITY")}',
			'${GSettings.get("INVOICE_SENDER_CITY")}',
			'${GSettings.get("INVOICE_SENDER_COUNTRY")}',
			'${GSettings.get("INVOICE_SENDER_STATE")}',
			'${GSettings.get("INVOICE_SENDER_EMAIL")}',
			'${GSettings.get("INVOICE_SENDER_CP")}',
			'${GSettings.get("INVOICE_SENDER_TAX_CODE")}',
			"",
			0,
			"",
			"",
			"");
	//console.log(client);

	$(document)
			.ready(
					function() {

						resetClient();
						
						$('#shopmanSession').html(
								':' + SHOPMAN.name + ":" + SHOPMAN.login);
						autocomplete('#commands');
						$('#commands').focus();
						$(document).idle(
								{
									idle : 1000 * 60 * 5,
									onIdle : function() {
										var doIdle = document.isIdle ? false
												: true;
										if (doIdle)
											document.isIdle = true;
										else
											return;
										lockin();
										var passinid = 'passid'
												+ $.capsule.randomString(1, 15,
														'aA0');
										passin({
											id : passinid,
											success : function(data) {
												AUTHORIZED = true;
												//alert("success " +data.authenticated+". #"+passinid+" to be removed")
												$('#' + passinid).remove();
												document.isIdle = false;
												$('#commands').focus();
											},
											message : "desbloquear "
													+ SHOPMAN.name + "-"
													+ SHOPMAN.login
										});
									}
								});
						$('#editproduct-6')
								.keyup(
										function(event) {
											if (event.keyCode == $.ui.keyCode.ENTER) {
												var e1 = $('#editproduct-1')
														.val();
												var e2 = $('#editproduct-2')
														.val();
												var e3 = $('#editproduct-3')
														.val();
												var e4 = $('#editproduct-4')
														.val();
												var e5 = $('#editproduct-5')
														.val();
												var e6 = $('#editproduct-6')
														.val();
												var quantity = e1;
												var unit = e2 != '' ? e2
														: 's/u';
												var description = e3 != '' ? e3
														: 's/d';
												var code = e4 != '' ? e4
														: 's/c';
												var mark = e5 != '' ? e5
														: 's/m';
												var unitPrice = e6;
												if (unitPrice == ''
														|| !parseFloat(e6)
														|| quantity == ''
														|| !parseFloat(quantity)) {
													alert("precio unitario invalido y/o cantidad");
													return;
												}
												var json = "{"
														+ '"quantity":"'
														+ quantity
														+ '",'
														+ '"unit":"'
														+ unit
														+ '",'
														+ '"description":"'
														+ description
														+ '",'
														+ '"code":"'
														+ code
														+ '",'
														+ '"mark":"'
														+ mark
														+ '",'
														+ '"productPriceKind":"-1",'
														+ '"unitPrice":"'
														+ unitPrice + '",'
														+ '"id":"-1" }';
												productsLog.unshift($
														.parseJSON(json));
												$('#editproduct').hide('slow');
												$('#commands').focus().val('');
												dolog(quantity, unit,
														description, code,
														mark, unitPrice);
												$('.tableingrow')
														.sexytable(
																{
																	'editedRow' : true,
																	'index' : $(
																			'.tableingrow')
																			.get(
																					0)
																});
												onLogChange();
											}
										});
						$('#lockbutton')
								.click(
										function() {
											var doIdle = document.isIdle ? false
													: true;
											if (doIdle)
												document.isIdle = true;
											else
												return;
											lockin();
											var passinid = 'passid'
													+ $.capsule.randomString(1,
															15, 'aA0');
											passin({
												id : passinid,
												success : function(data) {
													AUTHORIZED = true;
													//alert("success " +data.authenticated+". #"+passinid+" to be removed")
													$('#' + passinid).remove();
													document.isIdle = false;
													$('#commands').focus();
												},
												message : "desbloquear "
														+ SHOPMAN.name + "-"
														+ SHOPMAN.login
											});
										});

						//authenticate();
						//resetClient();
						///INIT

						
						$("#commands").bind(
								'autocompleteopen',
								function(event, ui) {
									$(this).autocomplete("option", 'isOpen',
											true);
								});

						$("#commands").bind(
								'autocompleteclose',
								function(event, ui) {
									$(this).autocomplete("option", 'isOpen',
											false);
								});

						$("#commands")
								.bind(
										'keypress',
										function(event) {
											//console.log(event.which);
											//commandline=commandLine();
											if (event.which != 13) {
												console.log('key != enter');
												return;
											}
											//console.log('key = enter');
											//alert(event.which);
											//$('#commands').SetBubblePopupOptions({innerHtml:"comandos"});
											$('#commands').HideBubblePopup();
											if (commandline.kind == 'edititem'){
												if(commandline.argssize%2!=0){
													alert("los argumentos no son pares");
													return;
												}
												var args = commandline.args;
												for(var i = 0; i < commandline.argssize; i += 2){
													if(!isNumber(args[i])){
														alert("los indices deben ser numéricos: -> "+args[i]);
														return;
													}
													if((args[i]*1)<0||(args[i]*1)>11){
														alert("los indices numéricos deben ser entre 0-11: -> "+args[i]);
														return;
													}
												}
												var rowIndex = commandline.itemNumber;
												for(var i = 0; i < commandline.argssize; i += 2){
													var row=$('.tableingrow').get(rowIndex);
													if(args[i]=="0"){
														if(args[i+1]==".")$($(row).find(".control1")).click();
														else return;
													}
													else if(args[i]=="11"){
														if(args[i+1]==".")$($(row).find(".control2")).click();
														else return;
													}
													else{
														$($(row).find("div>div").get(args[i])).html(args[i+1].toUpperCase());
													}
													//$(row).get(args[i]).html(args[i+1].toUpperCase());
												}
												onLogChange();
												$('#commands').val("");
											}
											else if (commandline.kind == 'fixdb'){
												$.ajax({
													url : CONTEXT_PATH+'/dbport',
													type : 'POST',
													data : {
														command : commandline.kind,
														fixNumber : commandline.args[0],//$("#absolutediscount").val(),
														token : TOKEN,
														list : $.toJSON(productsLog),
														clientReference : CLIENT_REFERENCE
													},
													success : function(data) {
														alert(data.result);
														console.log(data.result)
													},
													error : function(
															jqXHR,
															textStatus,
															errorThrown) {
														alert("el sistema dice: "
																+ textStatus
																+ " - "
																+ errorThrown
																+ " - "
																+ jqXHR.responseText);
													}
												});
											}
											else if (commandline.kind == 'absolutediscount'){
												$.ajax({
													url : CONTEXT_PATH+'/dbport',
													type : 'POST',
													data : {
														command : commandline.kind,
														absoluteDiscount : commandline.args[0],//$("#absolutediscount").val(),
														token : TOKEN,
														list : $.toJSON(productsLog),
														clientReference : CLIENT_REFERENCE
													},
													success : function(data) {
														$("#consummerDiscount").val(data.result);
														$("#consummerDiscount").trigger("onchange");
														$("#commands").val("");
														console.log(data.result)
													},
													error : function(
															jqXHR,
															textStatus,
															errorThrown) {
														alert("el sistema dice: "
																+ textStatus
																+ " - "
																+ errorThrown
																+ " - "
																+ jqXHR.responseText);
													}
												});
											}
													
											//console.log(commandline.kind);
											else if (commandline.kind == 'product'
													|| commandline.kind == 'retrieve') {
												if (!$(this).autocomplete(
														"option", 'isOpen')
														&& commandline.args.length == 1) {

													REQUEST_NUMBER = REQUEST_NUMBER + 1;
													$
															.ajax({
																url : "getproductbycode",
																dataType : "json",
																type : 'POST',
																data : {
																	code : commandline.args[0],
																	requestNumber : REQUEST_NUMBER,
																	consummerType : client ? client.consummerType
																			: 1,
																	token : TOKEN,
																	clientReference : CLIENT_REFERENCE,
																	consummerDiscount : $('#consummerDiscount').val()
																},
																success : function(
																		data) {
																	//console.log(data);
																	var p = data.product;
																	var quantity = commandline.quantity;

																	dolog(
																			quantity,
																			p.unit,
																			p.description,
																			p.code,
																			p.mark,
																			p.unitPrice,
																			p.prodservCode,
																			p.unitCode);
																	productsLog
																			.unshift(p);
																	productsLog[0].quantity = quantity;
																	onLogChange();
																	$(
																			"#commands")
																			.val(
																					'');
																}
															});
												}
											}
											if (commandline.kind == 'incrementagentearning') {
												if (commandline.args.length < 2) {
													alert("argumentos incompletos");
													return;
												} else if (commandline.args.length == 2) {
													if (!isNumber(commandline.args[1])) {
														alert("ERROR: cantidad invalida "
																+ commandline.args[1]);
														$("#commands")
																.val(
																		$(
																				"#commands")
																				.val()
																				.replace(
																						commandline.args[1],
																						""));
														return;
													}

													$
															.ajax({
																url : "invoiceport",
																dataType : "json",
																type : 'POST',
																data : {
																	command : commandline.command,
																	args : commandline.args
																			.join(" "),
																	requestNumber : REQUEST_NUMBER,
																	consummerType : client ? client.consummerType
																			: 1,
																	token : TOKEN,
																	clientReference : CLIENT_REFERENCE
																},
																success : function(
																		data) {

																	invoiceInfoLog(data.invoice);
																	$(
																			"#commands")
																			.val(
																					'');
																	alert(data.message);
																},
																error : function(
																		jqXHR,
																		textStatus,
																		errorThrown) {
																	console
																			.log(jqXHR);
																	console
																			.log(textStatus);
																	console
																			.log(errorThrown);
																	alert("el sistema dice: "
																			+ textStatus
																			+ " - "
																			+ errorThrown
																			+ " - "
																			+ jqXHR.responseText);
																}

															});
												}
											} else if (commandline.kind == 'editproduct') {
												$('#editproduct').show('slow');
												$('#editproduct-1').val('');
												$('#editproduct-2').val('');
												$('#editproduct-3').val('');
												$('#editproduct-4').val('');
												$('#editproduct-5').val('');
												$('#editproduct-6').val('');
												$('#editproduct-1').focus();
												$('#commands').val('');
											} else if (commandline.kind == 'editclient') {
												addConsummerIn('clients');
												//$('#commands').empty().focus();
											} else if (commandline.kind == 'editagent') {
												addConsummerIn('agents');
												$('#commands').empty().focus();
											} else if (commandline.kind == 'emitinvoice') {
												if (agent == null
														|| client == null) {
													alert("error: Cliente y/o Agente Indefinido(s).");
													$('#commands').val('');
													return;
												}
												/*if ($('#paymentMethod').val() == '' || $('#paymentWay').val() == '') {
													alert("metodo de pago y/o forma de pago indefinido.");
													$('#commands').val('');
													return;
												}*/
												//alert(" kind "+commandline.kind+" argsL "+commandline.args.length);
												if (commandline.args.length >= 0
														&& productsLog.length > 0) {
													//CONFIRM
													var block1=$.blockUI({content : '<h1><img src="img/wait.gif" /> esperar...</h1>'});
								
													/*$
															.blockUI({
																message : '<h1><img src="img/wait.gif" /> esperar...</h1>'
															});*/
													$.ajax({
																type : 'POST',
																url : CONTEXT_PATH+'/dbport',
																data : {
																	client : encodeURIComponent($
																			.toJSON(client)),
																	list : encodeURIComponent($
																			.toJSON(productsLog)),
																	shopman : encodeURIComponent($
																			.toJSON(shopman)),
																	metadata : encodeURIComponent($
																			.toJSON(metadata)),
																	requester : encodeURIComponent($
																			.toJSON(requester)),
																	seller : encodeURIComponent($
																			.toJSON(seller)),
																	agent : encodeURIComponent($
																			.toJSON(agent)),
																	destiny : encodeURIComponent("{\"address\" : \""+$('#destiny').val()+"\"}"),
																	args : encodeURIComponent(commandline.args
																			.join(" ")),
																	token : TOKEN,
																	command : commandline.kind,
																	clientReference : CLIENT_REFERENCE,
																	paymentMethod: $('#paymentMethod').val(),
																	paymentWay: $('#paymentWay').val(),
																	documentType: $('#documentType').val(),
																	cfdiUse:$('#cfdiUse').val(),
																	coin:$('#coin').val(),
																	printCopies : commandline.printCopies
																},
																success : function(data) {
																	resetClient();
																	$('#commands').val('');
																	invoiceInfoLog(data.invoice);
																	//alert(data.successResponse);
																	$.blockUI({
																		node:block1,
																		content : '<h1>'+data.successResponse+'</h1>',
																		changeContent:true,
																		unblockOnAnyKey:true
																	});
																},
																error : function(
																		jqXHR,
																		textStatus,
																		errorThrown) {
																	console
																			.log(jqXHR);
																	console
																			.log(textStatus);
																	console
																			.log(errorThrown);
																	/*alert("el sistema dice: "
																			+ textStatus
																			+ " - "
																			+ errorThrown
																			+ " - "
																			+ jqXHR.responseText);*/

																	$.blockUI({
																		node:block1,
																		content : textStatus+" - "+errorThrown+" - "+ jqXHR.responseText,
																		changeContent:true,
																		unblockOnAnyKey:true
																	});
																	
																},
																dataType : "json"
															});
												} else {
													alert('ningun item en lista');
													$('#commands').val('');
												}
											}
											else if (commandline.kind == 'emitorder') {
												if (agent == null
														|| client == null) {
													alert("error: Cliente y/o Agente Indefinido(s).");
													$('#commands').val('');
													return;
												}
												/*if ($('#paymentMethod').val() == '' || $('#paymentWay').val() == '') {
													alert("metodo de pago y/o forma de pago indefinido.");
													$('#commands').val('');
													return;
												}*/
												//alert(" kind "+commandline.kind+" argsL "+commandline.args.length);
												if (commandline.args.length >= 0
														&& productsLog.length > 0) {
													//CONFIRM
													var block1=$.blockUI({content : '<h1><img src="img/wait.gif" /> esperar...</h1>'});
								
													/*$
															.blockUI({
																message : '<h1><img src="img/wait.gif" /> esperar...</h1>'
															});*/
													$.ajax({
																type : 'POST',
																url : CONTEXT_PATH+'/dbport',
																data : {
																	client : encodeURIComponent($
																			.toJSON(client)),
																	list : encodeURIComponent($
																			.toJSON(productsLog)),
																	shopman : encodeURIComponent($
																			.toJSON(shopman)),
																	metadata : encodeURIComponent($
																			.toJSON(metadata)),
																	requester : encodeURIComponent($
																			.toJSON(requester)),
																	seller : encodeURIComponent($
																			.toJSON(seller)),
																	agent : encodeURIComponent($
																			.toJSON(agent)),
																	destiny : encodeURIComponent("{\"address\" : \""+$('#destiny').val()+"\"}"),
																	args : encodeURIComponent(commandline.args
																			.join(" ")),
																	token : TOKEN,
																	command : commandline.kind,
																	clientReference : CLIENT_REFERENCE,
																	paymentMethod: $('#paymentMethod').val(),
																	paymentWay: $('#paymentWay').val(),
																	documentType: "ORDER",
																	cfdiUse:$('#cfdiUse').val(),
																	coin:$('#coin').val(),
																	printCopies : commandline.printCopies
																},
																success : function(data) {
																	resetClient();
																	$('#commands').val('');
																	invoiceInfoLog(data.invoice);
																	//alert(data.successResponse);
																	$.blockUI({
																		node:block1,
																		content : '<h1>'+data.successResponse+'</h1>',
																		changeContent:true,
																		unblockOnAnyKey:true
																	});
																},
																error : function(
																		jqXHR,
																		textStatus,
																		errorThrown) {
																	console
																			.log(jqXHR);
																	console
																			.log(textStatus);
																	console
																			.log(errorThrown);
																	/*alert("el sistema dice: "
																			+ textStatus
																			+ " - "
																			+ errorThrown
																			+ " - "
																			+ jqXHR.responseText);*/

																	$.blockUI({
																		node:block1,
																		content : textStatus+" - "+errorThrown+" - "+ jqXHR.responseText,
																		changeContent:true,
																		unblockOnAnyKey:true
																	});
																	
																},
																dataType : "json"
															});
												} else {
													alert('ningun item en lista');
													$('#commands').val('');
												}
											}
											else if (commandline.kind == 'emitsample') {
												if (agent == null
														|| client == null) {
													alert("error: Cliente y/o Agente Indefinido(s).");
													$('#commands').val('');
													return;
												}
												/*if ($('#paymentMethod').val() == '' || $('#paymentWay').val() == '') {
													alert("metodo de pago y/o forma de pago indefinido.");
													$('#commands').val('');
													return;
												}*/
												//alert(" kind "+commandline.kind+" argsL "+commandline.args.length);
												if (commandline.args.length >= 0
														&& productsLog.length > 0) {
													//CONFIRM
													var block1=$.blockUI({content : '<h1><img src="img/wait.gif" /> esperar...</h1>'});
								
													/*$
															.blockUI({
																message : '<h1><img src="img/wait.gif" /> esperar...</h1>'
															});*/
													$.ajax({
																type : 'POST',
																url : CONTEXT_PATH+'/dbport',
																data : {
																	client : encodeURIComponent($
																			.toJSON(client)),
																	list : encodeURIComponent($
																			.toJSON(productsLog)),
																	shopman : encodeURIComponent($
																			.toJSON(shopman)),
																	metadata : encodeURIComponent($
																			.toJSON(metadata)),
																	requester : encodeURIComponent($
																			.toJSON(requester)),
																	seller : encodeURIComponent($
																			.toJSON(seller)),
																	agent : encodeURIComponent($
																			.toJSON(agent)),
																	destiny : encodeURIComponent("{\"address\" : \""+$('#destiny').val()+"\"}"),
																	args : encodeURIComponent(commandline.args
																			.join(" ")),
																	token : TOKEN,
																	command : commandline.kind,
																	clientReference : CLIENT_REFERENCE,
																	paymentMethod: $('#paymentMethod').val(),
																	paymentWay: $('#paymentWay').val(),
																	documentType: "SAMPLE",
																	cfdiUse:$('#cfdiUse').val(),
																	coin:$('#coin').val(),
																	printCopies : commandline.printCopies
																},
																success : function(data) {
																	resetClient();
																	$('#commands').val('');
																	invoiceInfoLog(data.invoice);
																	//alert(data.successResponse);
																	$.blockUI({
																		node:block1,
																		content : '<h1>'+data.successResponse+'</h1>',
																		changeContent:true,
																		unblockOnAnyKey:true
																	});
																},
																error : function(
																		jqXHR,
																		textStatus,
																		errorThrown) {
																	console
																			.log(jqXHR);
																	console
																			.log(textStatus);
																	console
																			.log(errorThrown);
																	/*alert("el sistema dice: "
																			+ textStatus
																			+ " - "
																			+ errorThrown
																			+ " - "
																			+ jqXHR.responseText);*/

																	$.blockUI({
																		node:block1,
																		content : textStatus+" - "+errorThrown+" - "+ jqXHR.responseText,
																		changeContent:true,
																		unblockOnAnyKey:true
																	});
																	
																},
																dataType : "json"
															});
												} else {
													alert('ningun item en lista');
													$('#commands').val('');
												}
											}
											else if (commandline.kind == 'sample') {
												if (agent == null
														|| client == null) {
													alert("error: Cliente y/o Agente Indefinido(s).");
													$('#commands').val('');
													return;
												}
												/*if ($('#paymentMethod').val() == '' || $('#paymentWay').val() == '') {
													alert("metodo de pago y/o forma de pago indefinido.");
													$('#commands').val('');
													return;
												}*/
												//alert(" kind "+commandline.kind+" argsL "+commandline.args.length);
												if (commandline.args.length >= 0
														&& productsLog.length > 0) {
													//CONFIRM
													var block1=$.blockUI({content : '<h1><img src="img/wait.gif" /> esperar...</h1>'});
								
													/*$
															.blockUI({
																message : '<h1><img src="img/wait.gif" /> esperar...</h1>'
															});*/
													$.ajax({
																type : 'POST',
																url : 'wishing',
																data : {
																	client : encodeURIComponent($
																			.toJSON(client)),
																	list : encodeURIComponent($
																			.toJSON(productsLog)),
																	shopman : encodeURIComponent($
																			.toJSON(shopman)),
																	metadata : encodeURIComponent($
																			.toJSON(metadata)),
																	requester : encodeURIComponent($
																			.toJSON(requester)),
																	seller : encodeURIComponent($
																			.toJSON(seller)),
																	agent : encodeURIComponent($
																			.toJSON(agent)),
																	destiny : encodeURIComponent("{\"address\" : \""+$('#destiny').val()+"\"}"),
																	args : encodeURIComponent(commandline.args
																			.join(" ")),
																	token : TOKEN,
																	command : commandline.command,
																	clientReference : CLIENT_REFERENCE,
																	paymentMethod: $('#paymentMethod').val(),
																	paymentWay: $('#paymentWay').val(),
																	documentType: $('#documentType').val(),
																	documentType: $('#documentType').val(),
																	cfdiUse:$('#cfdiUse').val()
																	
																},
																success : function(data) {
																	resetClient();
																	$('#commands').val('');
																	invoiceInfoLog(data.invoice);
																	//alert(data.successResponse);
																	$.blockUI({
																		node:block1,
																		content : '<h1>'+data.successResponse+'</h1>',
																		changeContent:true,
																		unblockOnAnyKey:true
																	});
																},
																error : function(
																		jqXHR,
																		textStatus,
																		errorThrown) {
																	console
																			.log(jqXHR);
																	console
																			.log(textStatus);
																	console
																			.log(errorThrown);
																	/*alert("el sistema dice: "
																			+ textStatus
																			+ " - "
																			+ errorThrown
																			+ " - "
																			+ jqXHR.responseText);*/

																	$.blockUI({
																		node:block1,
																		content : textStatus+" - "+errorThrown+" - "+ jqXHR.responseText,
																		changeContent:true,
																		unblockOnAnyKey:true
																	});
																	
																},
																dataType : "json"
															});
												} else {
													alert('ningun item en lista');
													$('#commands').val('');
												}
											}
											else if (commandline.kind == 'consultthebox') {
												$
														.ajax({
															url : 'consultthebox',
															data : {
																command : encodeURIComponent(commandline.command),
																token : TOKEN,
																clientReference : CLIENT_REFERENCE
															},
															type : 'POST',
															success : function(
																	data) {
																//alert('$'+ data.cash);
																$.blockUI({
																	content : "$"+data.cash,
																	unblockOnAnyKey:true
																});
																$('#commands')
																		.val('');
																//window.location="init6.jsp";

															},
															error : function(
																	jqXHR,
																	textStatus,
																	errorThrown) {
																console
																		.log(jqXHR);
																console
																		.log(textStatus);
																console
																		.log(errorThrown);
																/*alert("el sistema dice: "
																		+ textStatus
																		+ " - "
																		+ errorThrown
																		+ " - "
																		+ jqXHR.responseText);
																*/
																$.blockUI({
																	content : textStatus+" - "+errorThrown+" - "+ jqXHR.responseText,
																	unblockOnAnyKey:true
																});

															}/*,
																			dataType:"json",
																			statusCode: {
																			    400: function(jqXHR, textStatus, errorThrown) {
																			      alert("el sistema dice: "+textStatus+" - "+errorThrown);
																			    }
																			  }*/
														});
											}

											else if (commandline.kind == 'invoicepayment') {
												/***TODO mostrar inf del agente y cliente primero, para confirmar*/

												if (commandline.args.length >= 1) {
													if (!confirm("realizar la siguiente operacion? : "
															+ (commandline.command == '$d' ? 'pagar/abonar '
																	+ commandline.args[0]
																			.toUpperCase()
																	: 'pagar agente para '
																			+ commandline.args[0]
																					.toUpperCase()))) {
														$('#commands').val('');
														return;
													}
													$
															.ajax({
																url : 'invoicepayment',
																data : {
																	command : encodeURIComponent(commandline.command),
																	args : commandline.args
																			.join(" "),
																	client : encodeURIComponent($
																			.toJSON(client)),
																	token : TOKEN,
																	clientReference : CLIENT_REFERENCE
																},
																type : 'POST',
																success : function(
																		data) {
																	alert(data.successResponse);
																	$(
																			'#commands')
																			.val(
																					'');
																	//window.location="init6.jsp";

																},
																error : function(
																		jqXHR,
																		textStatus,
																		errorThrown) {
																	console
																			.log(jqXHR);
																	console
																			.log(textStatus);
																	console
																			.log(errorThrown);
																	alert("el sistema dice: "
																			+ textStatus
																			+ " - "
																			+ errorThrown
																			+ " - "
																			+ jqXHR.responseText);
																}/*,
																					dataType:"json",
																					statusCode: {
																					    400: function(jqXHR, textStatus, errorThrown) {
																					      alert("el sistema dice: "+textStatus+" - "+errorThrown);
																					    }
																					  }*/
															});
												}
											}

											else if (commandline.kind == 'canceldocument') {

												if (commandline.args.length >= 1) {

													if (!confirm("cancelar documento "
															+ commandline.args[0]
															+ " ? "))
														return;
													var block2=$.blockUI({
																content : '<h1><img src="img/wait.gif" /> cancelando...</h1>'
															});
													$
															.ajax({
																url : 'invoicecancelling',
																data : {
																	command : commandline.command,
																	args : commandline.args
																			.join(" "),
																	client : encodeURIComponent($
																			.toJSON(client)),
																	token : TOKEN,
																	clientReference : CLIENT_REFERENCE
																},
																success : function(
																		data) {
																	//alert(data.message);
																	$.blockUI({
																		node:block2,
																		content : '<h1>'+data.message+'</h1>',
																		changeContent:true,
																		unblockOnAnyKey:true
																	});
																},
																error : function(
																		jqXHR,
																		textStatus,
																		errorThrown) {
																	/*alert("el sistema dice: "
																			+ textStatus
																			+ " - "
																			+ errorThrown
																			+ " - "
																			+ jqXHR.responseText);*/
																	$.blockUI({
																		node:block2,
																		content : textStatus+" - "+errorThrown+" - "+ jqXHR.responseText,
																		changeContent:true,
																		unblockOnAnyKey:true
																	});
																},
																dataType : "json",
																type : 'POST'
															});
													$('#commands').val('');
												}
											} else if (commandline.kind == 'facture') {
												if (client == null) {
													alert("cliente indefinido");
													$('#commands').val('');
													return;
												}
												if (!validateRfc(client.rfc)) {
													$('#commands').val('');
													alert('cliente no facturable RFC invalido');
													return;
												}
												if (commandline.args.length >= 1) {

													if (!confirm("sera facturado "
															+ commandline.args[0]
																	.toUpperCase()
															+ " a "
															+ client.consummer
															+ " ? "))
														return;
													$
															.ajax({
																url : 'facture',
																data : {
																	command : commandline.command,
																	args : commandline.args
																			.join(" "),
																	client : encodeURIComponent($
																			.toJSON(client)),
																	token : TOKEN,
																	clientReference : CLIENT_REFERENCE
																},
																success : function(
																		data) {
																	if (commandline.command == '+f') {
																		$(
																				'#commands')
																				.val(
																						'');
																		//window.location="init6.jsp";
																	} else if (commandline.command == '@-') {

																	}
																},
																error : function(
																		jqXHR,
																		textStatus,
																		errorThrown) {
																	alert(textStatus
																			+ " : "
																			+ errorThrown);
																},
																dataType : "json",
																type : 'POST'
															});
												}
											} else if (commandline.kind == 'discount') {
												/*if (commandline.args.length >= 1
														&& commandline.args[0]
																.charAt(commandline.args[0].length - 1) == ',') {
													alert("discounting "
															+ (parseFloat(commandline.args[0])));
													for (var j = 0; j < productsLog.length; j++) {
														//$(".tableingrow").unbind('DOMSubtreeModified');

														var newPrice = Math
																.round(productsLog[j].unitPrice
																		* (100 - parseFloat(commandline.args[0]))) / 100;
														$('.unitPrice').eq(j)
																.html(newPrice);
														productsLog[j].unitPrice = newPrice;

														onLogChange();
														//productsLog[j].quantity=$('.quantity').eq(j).val();
													}
													$('#commands').val('');
												}*/
												$('#consummerDiscount').val(parseFloat(commandline.args[0]));
												$('#commands').val('');
												applyDiscount();

											} else if (commandline.kind == 'getinvoice') {
												if (commandline.args.length >= 1) {
													var block3=$
															.blockUI({
																content : '<h1><img src="img/wait.gif" /></h1>'
															});
													$
															.ajax({
																url : 'getinvoice',
																type : 'POST',
																data : {
																	command : commandline.command,
																	reference : commandline.args
																			.join(" "),
																	token : TOKEN,
																	clientReference : CLIENT_REFERENCE
																},
																success : function(
																		data) {
																	$.unblockUI(block3);
																	if (commandline.command == '@r') {
																		var itms = data.items;
																		for (var i = itms.length - 1; i >= 0; i--) {
																			//itms[i].quantity=parseFloat(itms[i].quantity);
																			//itms[i].product.unitPrice=parseFloat(itms[i].product.unitPrice);
																			console
																					.log(itms[i]);
																			dolog(
																					parseFloat(itms[i].quantity),
																					itms[i].unit,
																					itms[i].description,
																					itms[i].code,
																					itms[i].mark,
																					parseFloat(itms[i].unitPrice),
																					itms[i].prodservCode,
																					itms[i].unitCode);
																			//productsLog.unshift(itms[i].product);
																			itms[i].quantity = parseFloat(itms[i].quantity);
																			itms[i].unitPrice = parseFloat(itms[i].unitPrice);
																			productsLog
																					.unshift(itms[i]);
																			productsLog[0].disabled = itms[i].disabled;

																			if (itms[i].id == -1) {
																				$(
																						'#log')
																						.sexytable(
																								{
																									'editedRow' : true,
																									'index' : 0
																								});
																			}
																			if (itms[i].disabled) {
																				$(
																						'#log')
																						.sexytable(
																								{
																									'disabledRow' : true,
																									'index' : 0
																								});
																			}
																			//productsLog[0].quantity=parseFloat(itms[i].quantity);

																		}
																		onLogChange();
																		$(
																				'#commands')
																				.val(
																						'');
																	}
																	if (commandline.command == '@rl') {
																		console
																				.log("DATA:");
																		console
																				.log(data);
																		invoiceInfoLog(data.invoice);
																		$(
																				'#commands')
																				.val(
																						'');
																	}
																},
																dataType : "json"
															});
												}
											} else if (commandline.kind == 'searchinvoices') {
												//console.log('going search: '+commandline.args.join(" "));
												if (commandline.args.length >= 1) {
													commandline.args[commandline.args.length - 1] = commandline.args[commandline.args.length - 1]
															.replace(/\./g, '');
													$("#resultset")
															.prepend(
																	"<img src=img/wait.gif width=70px height=70px/>");
													$
															.ajax({
																url : 'searchinvoices',
																type : 'POST',
																data : {
																	paths : encodeURIComponent(commandline.args
																			.join(" ")),
																	token : TOKEN,
																	clientReference : CLIENT_REFERENCE
																},
																success : function(
																		data) {
																	$(
																			'#resultset')
																			.empty();
																	//alert("hora de empezar");
																	console
																			.log("DATA");
																	console
																			.log(data);
																	for (var i = 0; i < data.invoices.length; i++) {
																		var gtotal = 0;
																		for (var j = 0; j < data.invoices[i].items.length; j++) {
																			var item = data.invoices[i].items[j];
																			var quantity = item.quantity;
																			var unitPrice = item.unitPrice;
																			data.invoices[i].items[j].total = Math
																					.round(quantity
																							* unitPrice
																							* 100) / 100;
																			gtotal += quantity
																					* unitPrice;
																			for ( var field in item) {
																				for (var k = 0; k < commandline.args.length; k++) {
																					if (typeOf(item[field]) == "string") {
																						item[field] = item[field]
																								.replace(
																										commandline.args[k]
																												.toUpperCase()
																												.replace(
																														/\"/g,
																														""),
																										"<i style='background-color:#fbff8d'><b>"
																												+ commandline.args[k]
																														.toUpperCase()
																														.replace(
																																/\"/g,
																																"")
																												+ "</b></i>");
																						//if(item[field].indexOf(commandline.args[k].toUpperCase())!=-1)contains=true;	
																					}
																				}
																			}
																		}
																		var consummer = data.invoices[i].client.consummer;
																		var consummerType = data.invoices[i].client.consummerType;
																		var address = data.invoices[i].client.address;
																		var city = data.invoices[i].client.city;
																		var state = data.invoices[i].client.state;
																		var country = data.invoices[i].client.country;
																		var email = data.invoices[i].client.email;
																		var cp = data.invoices[i].client.cp;
																		var rfc = data.invoices[i].client.rfc;
																		var payment = data.invoices[i].client.payment;
																		var reference = data.invoices[i].reference;
																		var date = null;
																		//**TODO fix the db removing metadata */
																		if (data.invoices[i].metaData) date = new Date(
																				new Number(
																						data.invoices[i].metaData.date))
																				.format(
																						'dd.mmm.yyyy')
																				.toUpperCase();
																		else date = new Date(
																				new Number(
																						data.invoices[i].logs[0].date))
																				.format(
																						'dd.mmm.yyyy')
																				.toUpperCase();
																		var shopmanName = null;
																		var shopmanLogin = null;
																		var agentName = null;
																		var agentAddress = null;
																		var agentRfc = null;
																		var agentType = null;
																		if (data.invoices[i].shopman != null) {
																			shopmanName = data.invoices[i].shopman.name;
																			shopmanLogin = data.invoices[i].shopman.login;
																		}
																		if (data.invoices[i].agent != null) {
																			agentName = data.invoices[i].agent.consummer;
																			agentAddress = data.invoices[i].agent.address;
																			agentRfc = data.invoices[i].agent.rfc;
																			agentType = data.invoices[i].agent.consummerType;
																		}
																		var invoices = data.invoices;
																		var consummerContent = "<b>fecha:</b>"
																				+ date
																				+ " | <b>ref:</b>"
																				+ reference
																				+ (invoices[i].totalValue ? " | <b>totalValue:</b>"
																						+ invoices[i].totalValue
																						: "")
																				+ (invoices[i].agentPayment ? " | <b>totalValue:</b>"
																						+ invoices[i].agentPayment
																						: "")
																				+ " | <b>total:</b>"
																				+ (Math
																						.round(gtotal * 100) / 100)
																				+ "<br>"
																				+ "<b>cliente:</b>"
																				+ consummer
																				+ " | <b>tipo:</b>"
																				+ consummerType
																				+ " | <b>credito:</b>"
																				+ payment
																				+ " | <b>dir:</b>"
																				+ address
																				+ " | <b>ciudad:</b>"
																				+ city
																				+ " | <b>estado:</b>"
																				+ state
																				+ " | <b>cp:</b>"
																				+ cp
																				+ " | <b>rfc:</b>"
																				+ rfc
																				+ "<br>"
																				+ "<b>agente nombre:</b>"
																				+ (agentName ? agentName
																						: '')
																				+ " | <b>agente dir:</b>"
																				+ (agentAddress ? agentAddress
																						: '')
																				+ " | <b>agente rfc:</b>"
																				+ (agentRfc ? agentRfc
																						: '')
																				+ " | <b>agente tipo:</b>"
																				+ (agentType ? agentType
																						: '')
																				+ "<br>"
																				+ "<b>despachó nombre:</b>"
																				+ (shopmanName ? shopmanName
																						: '')
																				+ " | <b>despachó login:</b>"
																				+ (shopmanLogin ? shopmanLogin
																						: '');
																		for (var k = 0; k < commandline.args.length; k++) {
																			if (!isNumber(consummer[field])) {
																				consummerContent = consummerContent
																						.replace(
																								commandline.args[k]
																										.toUpperCase()
																										.replace(
																												/\"/g,
																												""),
																								"<i style='background-color:#fbff8d'><b>"
																										+ commandline.args[k]
																												.toUpperCase()
																												.replace(
																														/\"/g,
																														"")
																										+ "</b></i>");
																				//if(item[field].indexOf(commandline.args[k].toUpperCase())!=-1)contains=true;	
																			}
																		}

																		var consummerObj = {
																			content : consummerContent
																		};
																		/*
																		$('#resultset').inCapsule({dclass:'box fleft', width:'100%',content:''},'com.ferremundo.cps.GenericDiv')
																		.capsule(data.invoices[i].items,'com.ferremundo.cps.TB').addClass(i%2!=0?'odd':'even')
																		.preCapsule(consummerObj,'com.ferremundo.cps.GenericDiv');
																		 */
																		console
																				.log("data.invoices["
																						+ i
																						+ "].items");
																		console
																				.log(data.invoices[i].items);
																		$(
																				'#resultset')
																				.inComFerremundoCpsGenericDiv(
																						{
																							dclass : 'box fleft',
																							width : '100%',
																							content : ''
																						})
																				.comFerremundoCpsTB(
																						data.invoices[i].items)
																				.addClass(
																						i % 2 != 0 ? 'odd'
																								: 'even')
																				.preComFerremundoCpsGenericDiv(
																						consummerObj);
																	}
																	$(
																			"#resultset")
																			.append(
																					"-- FIN DE BUSQUEDA --");

																},
																dataType : "json",
																error : function() {
																	$(
																			'#resultset')
																			.empty();
																}
															});
												}
											} else if (commandline.kind == 'makerecord') {
												if (commandline.args.length >= 1) {
													commandline.args[commandline.args.length - 1] = commandline.args[commandline.args.length - 1]
															.replace(/\./g, '');

													$
															.ajax({
																url : CONTEXT_PATH+'/dbport',
																type : 'POST',
																data : {
																	command : commandline.kind,
																	args : encodeURIComponent(commandline.args
																			.join(" ")),
																	requestNumber : REQUEST_NUMBER,
																	consummerType : client ? client.consummerType
																			: 1,
																	token : TOKEN,
																	clientReference : CLIENT_REFERENCE
																},
																success : function(
																		data) {
																	//alert(data.message);
																	var r = data.record;
																	var pdd = new Date(
																			r.creationTime);
																	var mm = pdd
																			.getMonth() + 1;
																	var dd = pdd
																			.getDate();
																	var yyyy = pdd
																			.getFullYear();
																	var date = yyyy
																			+ '.'
																			+ mm
																			+ '.'
																			+ dd;
																	var src = "<div id=record-"+r.id+">"
																			+ r.id
																			+ " | "
																			+ date
																			+ " | "
																			+ r.text
																			+ "</div>";
																	nodeLog(
																			src,
																			"#records",
																			"box fleft");

																},
																error : function(
																		jqXHR,
																		textStatus,
																		errorThrown) {
																	alert("el sistema dice: "
																			+ textStatus
																			+ " - "
																			+ errorThrown
																			+ " - "
																			+ jqXHR.responseText);
																}
															});
													$('#commands').val('');
												} else
													alert("escribe algo para recordar");
											} else if (commandline.kind == 'deactivaterecord') {
												if (commandline.args.length >= 1) {
													commandline.args[commandline.args.length - 1] = commandline.args[commandline.args.length - 1]
															.replace(/\./g, '');

													$
															.ajax({
																url : CONTEXT_PATH+'/dbport',
																type : 'POST',
																data : {
																	command : commandline.kind,
																	args : commandline.args
																			.join(" "),
																	requestNumber : REQUEST_NUMBER,
																	consummerType : client ? client.consummerType
																			: 1,
																	token : TOKEN,
																	clientReference : CLIENT_REFERENCE
																},
																success : function(
																		data) {
																	alert("HECHO :"
																			+ data.record.text);
																	$(
																			"#record-"
																					+ data.record.id)
																			.remove();
																},
																error : function(
																		jqXHR,
																		textStatus,
																		errorThrown) {
																	alert("el sistema dice: "
																			+ textStatus
																			+ " - "
																			+ errorThrown
																			+ " - "
																			+ jqXHR.responseText);
																}
															});
													$('#commands').val('');
												} else
													alert("noda por hacer");
											} else if (commandline.kind == 'productinventoryadd') {
												$('body')
														.preinSysAuth({
															id : 'sysauth'
														})
														.feedSysAuth(
																"input",
																{
																	destroyAfter : true,
																	fun : function(
																			pass) {

																		var ret = false;
																		if (authenticate_(pass)) {
																			ret = true;

																			$
																					.ajax({
																						url : CONTEXT_PATH+'/dbport',
																						type : 'POST',
																						data : {
																							command : commandline.kind,
																							args : commandline.args
																									.join(" "),
																							requestNumber : REQUEST_NUMBER,
																							consummerType : client ? client.consummerType
																									: 1,
																							token : TOKEN,
																							items : encodeURIComponent($
																									.toJSON(productsLog)),
																							clientReference : CLIENT_REFERENCE
																						},
																						success : function(
																								data) {
																							alert("HECHO :"
																									+ data.record.text);
																							$(
																									"#record-"
																											+ data.record.id)
																									.remove();
																						},
																						error : function(
																								jqXHR,
																								textStatus,
																								errorThrown) {
																							alert("el sistema dice: "
																									+ textStatus
																									+ " - "
																									+ errorThrown
																									+ " - "
																									+ jqXHR.responseText);
																						}
																					});
																		}
																		return ret;
																	}
																});

												$('#commands').val('');
											} else if (commandline.kind == 'updateproducts') {
												$("#resultset")
														.prepend(
																"<img src=img/wait.gif width=70px height=70px/>");
												$
														.ajax({
															url : CONTEXT_PATH+'/dbport',
															type : 'POST',
															data : {
																command : commandline.kind,
																token : TOKEN,
																clientReference : CLIENT_REFERENCE
															},
															success : function(
																	data) {
																$("#resultset")
																		.empty()
																		.append(
																				data);
															},
															error : function(
																	jqXHR,
																	textStatus,
																	errorThrown) {
																console
																		.error("el sistema dice: "
																				+ textStatus
																				+ " - "
																				+ errorThrown
																				+ " - "
																				+ jqXHR.responseText);
																$("#resultset")
																		.empty()
																		.append(
																				"el sistema dice: "
																						+ textStatus
																						+ " - "
																						+ errorThrown
																						+ " - "
																						+ jqXHR.responseText);
															}
														});
												$('#commands').val('');
											} else if (commandline.kind == 'adduser') {
												$('#commands').val('');
												var passinid = 'passid'
														+ $.capsule
																.randomString(
																		1, 15,
																		'aA0');
												passin({
													id : passinid,
													success : function(data) {
														AUTHORIZED = true;
														//alert("success " +data.authenticated+". #"+passinid+" to be removed")
														$('#' + passinid)
																.remove();
														document.isIdle = false;
														registerShopmanIn();
														//$('#commands').focus();
													},
													message : "password "
															+ SHOPMAN.name
															+ "-"
															+ SHOPMAN.login
												});
											}
											else if (commandline.kind == 'paymentway') {
												$('#commands').val('');
												$("#paymentWay").val(commandline.args[0].toUpperCase());
											}
											else if (commandline.kind == 'paymentmethod') {
												$('#commands').val('');
												$("#paymentMethod").val(commandline.args[0].toUpperCase());
											}
											else if (commandline.kind == 'documenttype') {
												$('#commands').val('');
												$("#documentType").val(commandline.args[0].toUpperCase());
											}
											else if (commandline.kind == 'destiny') {
												$('#commands').val('');
												$("#destiny").val(commandline.args.join(" ").toUpperCase());
											}
											else if (commandline.kind == 'cfdiuse') {
												$('#commands').val('');
												$("#cfdiUse").val(commandline.args[0].toUpperCase());
											}
										});

						$(function() {
							$("#client-accordion").accordion({
								collapsible : true,
								active : false,
								header : 'p',
								autoHeight : false
							/*,
										change: function(event, ui) {
											var H=0;
											$('.accordion-e').each(function(){
												H+=$(this).height();
											});
											$('#accordion').animate({height:H+"px"},300);
										}*/

							});
							$("#agent-accordion").accordion({
								collapsible : true,
								active : false,
								header : 'p',
								autoHeight : false
							/*,
										change: function(event, ui) {
											var H=0;
											$('.accordion-e').each(function(){
												H+=$(this).height();
											});
											$('#accordion').animate({height:H+"px"},300);
										}*/

							});
						});

						//$('#editclient').hide();
						//$('#editagent').hide();
						$('#editproduct').hide();
					});
</script>


<title>Ferremundo - pedidos</title>
</head>
<body>
	


	<div class="ui-widget">
		<input id="commands" style="width:80%" /><a id="shopmanSession"></a>
		<script type="text/javascript">
			fillCommandLine = function(text) {
				$('#commands').val(text).focus();
				commandline = new commandLine();
				//console.log(commandline);
			};
		</script>
		<button type="button" id="lockbutton">lock</button>
		<button type="button" onclick="new fillCommandLine('@pcot');">imprimir cotizacion</button>
		<!--button type="button" onclick="new fillCommandLine('@ia');">cotizar
			a agente</button-->
		<button type="button" onclick="new fillCommandLine('@ecot');">enviar cotizacion</button>
		<!--button type="button" onclick="new fillCommandLine('@oa');">credito
			a agente</button-->
		<button type="button" onclick="new fillCommandLine('$oc');">pedido
			a cliente+abono</button>
		<!--button type="button" onclick="new fillCommandLine('$oa');">pedido
			a agente+abono</button-->
		<button type="button" onclick="new fillCommandLine('$fc');">factura
			a cliente+abono</button>
		<button type="button" onclick="new fillCommandLine('$ef');">enviar factura</button>
		<!--button type="button" onclick="new fillCommandLine('$fa');">factura
			a agente+abono</button-->
		<!--Metodo de pago -->
		
		<select id="paymentMethod" style="width:10%">
			<option ></option>
			<option value="PUE">PUE Pago en una sola exhibición</option>
			<option value="PPD">PPD Pago parcialidades o diferido</option>
		</select>
		<!-- Forma de pago -->
		<select id="paymentWay" style="width:10%">
  			<option ></option>
  			<option value="01">01 Efectivo</option>
			<option value="02">02 Cheque nominativo</option>
			<option value="03">03 Transferencia electrónica de fondos</option>
			<option value="04">04 Tarjeta de crédito</option>
			<option value="05">05 Monedero electrónico</option>
			<option value="06">06 Dinero electrónico</option>
			<option value="08">08 Vales de despensa</option>
			<option value="12">12 Dación en pago</option>
			<option value="13">13 Pago por subrogación</option>
			<option value="14">14 Pago por consignación</option>
			<option value="15">15 Condonación</option>
			<option value="17">17 Compensación</option>
			<option value="23">23 Novación</option>
			<option value="24">24 Confusión</option>
			<option value="25">25 Remisión de deuda</option>
			<option value="26">26 Prescripción o caducidad</option>
			<option value="27">27 A satisfacción del acreedor</option>
			<option value="28">28 Tarjeta de débito</option>
			<option value="29">29 Tarjeta de servicios</option>
			<option value="30">30 Aplicación de anticipos</option>
			<option value="99">99 Por definir</option>
		</select>
		<select id="documentType" style="width:10%">
			<option value="I">I Ingreso</option>
			<option value="E">E Egreso</option>
			<option value="T">T Traslado</option>
			<option value="N">N Nomina</option>
			<option value="P">P Pago</option>
		</select>
		<select id="cfdiUse" style="width:10%">
			<option ></option>
			<option value="G01">G01 Adquisición de mercancias</option>
			<option value="G02">G02 Devoluciones, descuentos o
				bonificaciones</option>
			<option value="G03">G03 Gastos en general</option>
			<option value="I01">I01 Construcciones</option>
			<option value="I02">I02 Mobilario y equipo de oficina por
				inversiones</option>
			<option value="I03">I03 Equipo de transporte</option>
			<option value="I04">I04 Equipo de computo y accesorios</option>
			<option value="I05">I05 Dados, troqueles, moldes, matrices y
				herramental</option>
			<option value="I06">I06 Comunicaciones telefónicas</option>
			<option value="I07">I07 Comunicaciones satelitales</option>
			<option value="I08">I08 Otra maquinaria y equipo</option>
			<option value="D01">D01 Honorarios médicos, dentales y
				gastos hospitalarios.</option>
			<option value="D02">D02 Gastos médicos por incapacidad o
				discapacidad</option>
			<option value="D03">D03 Gastos funerales.</option>
			<option value="D04">D04 Donativos.</option>
			<option value="D05">D05 Intereses reales efectivamente
				pagados por créditos hipotecarios (casa habitación).</option>
			<option value="D06">D06 Aportaciones voluntarias al SAR.</option>
			<option value="D07">D07 Primas por seguros de gastos
				médicos.</option>
			<option value="D08">D08 Gastos de transportación escolar
				obligatoria.</option>
			<option value="D09">D09 Depósitos en cuentas para el ahorro,
				primas que tengan como base planes de pensiones.</option>
			<option value="D10">D10 Pagos por servicios educativos
				(colegiaturas)</option>
			<option value="P01">P01 Por definir</option>
		</select>
		<input id="destiny" value="Mostrador" onfocus="(function(t){t.select()})(this)"/>
		desc<input id="consummerDiscount" value="0" onfocus="(function(t){t.select()})(this)" onchange="(function(){applyDiscount(this)})()"/>
		
		<!--abs<input id="absoluteDiscount" value="0" onfocus="(function(t){t.select()})(this)" onchange="(function(){applyDiscount(this)})()"/>-->
		<select id="coin" style="width:10%">
			<option value = "MXN">MXN</option>
		</select>
		<br><a class="g-total2">$0</a> [ <a class="g-area-to-print">0 - 0 hojas</a> ]
		<div id="editproduct">
			<input id="editproduct-1" /> <input id="editproduct-2" /> <input
				id="editproduct-3" /> <input id="editproduct-4"/> <input
				id="editproduct-5" /> <input id="editproduct-6"/>
		</div>

	</div>
    <table><tr><td>
	<div id="client-accordion" style="height: auto;">
		<p>
			<a href="#" id="client" class="accordion-e"></a>
		</p>
		<div>
			<div id="client-address" class="accordion-e"></div>
			<div id="client-city" class="accordion-e"></div>
			<div id="client-state" class="accordion-e"></div>
			<div id="client-cp" class="accordion-e"></div>
			<div id="client-rfc" class="accordion-e"></div>
			<div id="client-tel" class="accordion-e"></div>
			<div id="client-email" class="accordion-e"></div>
			<div id="client-country" class="accordion-e"></div>

		</div>
	</div>
	</td><td>
	<div id="agent-accordion" style="height: auto;">
		<p>
			<a href="#" id="agent" class="accordion-e"></a>
		</p>
		<div>
			<div id="agent-address" class="accordion-e"></div>
			<div id="agent-city" class="accordion-e"></div>
			<div id="agent-state" class="accordion-e"></div>
			<div id="agent-cp" class="accordion-e"></div>
			<div id="agent-rfc" class="accordion-e"></div>
			<div id="agent-tel" class="accordion-e"></div>
			<div id="agent-email" class="accordion-e"></div>
			<div id="agent-country" class="accordion-e"></div>

		</div>
	</div>
	</td></tr></table>
	<div style="position: relative; width: 100%">
		<div id="log" style="height: 500px; width: 100%; overflow: auto;"
			class="ui-widget-content"></div>
	</div>

	<div id="resultset" style="width: 100%;" class=""></div>
	<div id="logResultset" style="width: 100%;" class=""></div>
	<div id='records'>records</div>
	<h6 id="clientReference"></h6>
</body>

</html>
