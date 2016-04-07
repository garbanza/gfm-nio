<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">



<%@page import="com.ferremundo.OnlineClients"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>


<script type="text/javascript" src="js/jquery.sexytable-1.1.js"></script>
<script type="text/javascript" src="js/jquery.editable.js"></script>
<!-- 
<script type="text/javascript" src="js/jquery.ui.core.js"></script>
<script type="text/javascript" src="js/jquery.ui.widget.js"></script>
<script type="text/javascript" src="js/jquery.ui.position.js"></script>
<script type="text/javascript" src="js/jquery.effects.core.js"></script>
<script type="text/javascript" src="js/jquery.ui.autocomplete.js"></script>
<script type="text/javascript" src="js/jquery.ui.accordion.js"></script>
 -->
<script type="text/javascript" src="js/jquery-ui-1.8.21.custom.min.js"></script>
<script type="text/javascript" src="js/jquery.json-2.2.js"></script>
<script type="text/javascript" src="js/jquery.editable.js"></script>
<script type="text/javascript" src="js/jquery.caret.1.02.min.js"></script>
<script type="text/javascript" src="js/URLEncode.js"></script>
<script type="text/javascript" src="js/jquery.capsule-0.7.1.js"></script>
<script type="text/javascript" src="cps/global.cps.js"></script>

<script type="text/javascript" src="js/util.js"></script>
<link rel="stylesheet" type="text/css" href="css/layout.css">
<link rel="stylesheet" type="text/css" href="css/jquery-ui-1.8.4.cupertino.css" />

<script id="consummerTemplate" type="text/x-jquery-tmpl">
    <div>${consummer}</div>
</script>

<script type="text/javascript">
<%
OnlineClients clients= OnlineClients.instance();
String ipAddres=request.getRemoteAddr();
out.println("var IP_ADDRESS='"+ipAddres+"';");
out.println("var CLIENT_REFERENCE="+clients.add(ipAddres,"")+";");
%>

var REQUEST_NUMBER=0;



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

newRow=function(quantity,code,unit,mark,description,unitPrice,total){
	return [
		{content:quantity,width:5},
		{content:unit,width:10},
		{content:description,width:40},
		{content:code,width:10},
		{content:mark,width:10},
		{content:unitPrice,width:10},
		{content:total,width:10}
		];
};
function Item(quantity,code,unit,mark,description,unitPrice,total){
	this.quantity=quantity;
	this.unit=unit;
	this.description=description;
	this.code=code;
	this.mark=mark;
	this.unitPrice=unitPrice;
	this.total=total;
};

function replaceAll(Source,stringToFind,stringToReplace){
	var temp = Source;
	var index = temp.indexOf(stringToFind);
	while(index != -1){
		temp = temp.replace(stringToFind,stringToReplace);
		index = temp.indexOf(stringToFind);
	}
	return temp;
}
toCompile=function(template,vars){
	var html=template;
	for (var key in vars) {
		html=replaceAll(html,"$("+key+")",vars[key]);
	}
	return html;
}
var itemTemplate="<div id='$(randid)' style='width:100%'>"+
	"<div class='fleft' style='width:5%'>$(quantity)</div>"+
	"<div class='fleft' style='width:30%'>$(description)</div>"+
	"<div class='fleft' style='width:10%'>$(code)</div>"+
	"<div class='fleft' style='width:5%'>$(mark)</div>"+
	"<div class='fleft' style='width:5%'>$(unitPrice)</div>"+
	"<div class='fleft' style='width:5%'>$(total)</div>"+
	"</div><br>";
var consummerTemplate="<div id='$(randid)'>$(content)</div><br>";
$(document).ready(function(){

	//INIT
	$('#commands').focus();

	$.capsule(capsules);

	var inputValue;
	var commandline;
	var emptyCommand=false;
	var key_down = $.Event("keydown.autocomplete"); key_down.keyCode =  $.ui.keyCode.DOWN;
	var keyup = $.Event("keydown.autocomplete"); keyup.keyCode =  $.ui.keyCode.UP;
	var keyenter = $.Event("keydown.autocomplete"); keyenter.keyCode =  $.ui.keyCode.ENTER;

	$('#commands').keyup(function(event) {
		if(event.which==190)//the dot "."
			commandline=commandLine();
		  
	});
	function commandLine(){
		this.value=$('#commands').val().replace(/ +(?= )/g,''); 			//value
		//clean
		
		for(var i=0;i<this.value.length;i++){
			if(this.value.charAt(i)==' '){this.value=this.value.replace(" ","");i--;}
			else break;
		}
		//alert("'"+this.value+"'");
		splited=replaceAll(this.value,".","").split(" ");
		this.command=splited[0];
		this.kind=false;
		this.args=[];								//args
		this.argssize=0;
		this.getFromDB=false;
		if(isNumber(command)){
			this.kind ='product';
			this.quantity=command;
			for(var i=1;i<splited.length;i++)if(splited[i]!=""){
				this.args[i-1]=splited[i];
				this.argssize++;
			}
			if(this.argssize>=1)this.getFromDB=true;
		}
		else if(this.command=='c'){
			this.kind ='client';
			for(var i=1;i<splited.length;i++)if(splited[i]!=""){
				this.args[i-1]=splited[i];
				this.argssize++;
			}
			if(this.argssize>=1)this.getFromDB=true;
		}
		else if(this.command=='@d'){
			this.kind="discount";
			for(var i=1;i<splited.length;i++)if(splited[i]!=""){
				this.args[i-1]=splited[i];
				this.argssize++;
			}
			this.getFromDB=false;
		}
		else {
			this.quantity=1;
			for(var i=0;i<splited.length;i++)if(splited[i]!=""){
				this.args[i]=splited[i];
				this.argssize++;
			}
			if(this.argssize>=1&&this.value.charAt(this.value.length-1)=="."){
				$("#resultset").prepend("<img src=img/wait.gif width=70px height=70px/>");
				$.ajax({
					url: 'searchinvoices',
					data: {
						paths : url.encode(this.value)
					},
					success: function(data){
						$('#resultset').empty();
						//alert("hora de empezar");
						for(var i=0;i<data.invoices.length;i++){
							var gtotal=0;
							
							for(var j=0;j<data.invoices[i].items.length;j++){
								
								
								var item=data.invoices[i].items[j];
								var quantity=item.quantity;
								var unitPrice=item.unitPrice;
								var total=Math.round(quantity*unitPrice*100)/100;
								item.total=total;
								data.invoices[i].items[j].total=total;
								var contains=false;
								for(var field in item){
									for(var k=0;k<splited.length;k++){
										if(!isNumber(item[field])){
											item[field]=item[field].replace(splited[k].toUpperCase(),"<b>"+splited[k].toUpperCase()+"</b>");
											if(item[field].indexOf(splited[k].toUpperCase())!=-1)contains=true;	
										}
									}
								}
								gtotal+=quantity*unitPrice;
								if(contains)data.invoices[i].items[j].gclasses='highlight';
							}
							//$('#resultset').capsule(data.invoices[i].items).addClass('box');
							$("<table></table>").appendTo('#resultset')
							.capsule(data.invoices[i].items,'com.ferremundo.cps.GenericTableRow');
							
							var consummer=data.invoices[i].client.consummer;
							var consummerType=data.invoices[i].client.consummerType;
							var address=data.invoices[i].client.address;
							var city=data.invoices[i].client.city;
							var state=data.invoices[i].client.state;
							var country=data.invoices[i].client.country;
							var email=data.invoices[i].client.email;
							var cp=data.invoices[i].client.cp;
							var rfc=data.invoices[i].client.rfc;
							var payment=data.invoices[i].client.payment;
							var reference=data.invoices[i].reference;
							var consummerContent=reference+" "+(Math.round(gtotal*100)/100)+"<br>"+
								consummer+" "+
								"tipo:"+consummerType+" credito:"+payment+"<br>"+
								address+" "+city+" "+state+" "+cp+"<br>"+
								rfc+"<br>"+email;
							var consummerObj={content:consummerContent};
							$('#resultset').capsule(consummerObj,'com.ferremundo.cps.GenericDiv');
							/*
							var contains=false;
							for(var k=0;k<splited.length;k++){
								consummerObj.content=consummerObj.content.replace(splited[k].toUpperCase(),"<b>"+splited[k].toUpperCase()+"</b>");
								if(consummerObj.content.indexOf(splited[k].toUpperCase())!=-1)contains=true;	
							}
							if(contains)$(toCompile(consummerTemplate,consummerObj)).appendTo('#resultset').addClass('rowmatch');
							else $(toCompile(consummerTemplate,consummerObj)).appendTo('#resultset');
							*/
							//$( "#consummerTemplate" ).tmpl( consummerObj).appendTo( "#resultset" );
							//$( "#itemsTemplate" ).tmpl( data.invoices[i].items ).appendTo( "#resultset" );
							/*$('#resultset').sexytable({
								row:[
										{content:head,width:100},
										
										],animate:0,index:'last'
							});*/
						}
					},
					error : function(jqXHR, textStatus, errorThrown){
						alert("maldito error-> status :"+textStatus+". thrown : "+errorThrown);
					},
					dataType:"json"
				});
			}
		}

		

		return this;
	}

	$(function() {
		$("#accordion").accordion({
			collapsible: true,
			active: false,
			header:'p',
			autoHeight: false/*,
			change: function(event, ui) {
				var H=0;
				$('.accordion-e').each(function(){
					H+=$(this).height();
				});
				$('#accordion').animate({height:H+"px"},300);
			}*/
		
		});
	});

	function isNumber(n) {
		  return !isNaN(parseFloat(n)) && isFinite(n);
	}
});

</script>




<title>Buscar - Ferremundo</title>

</style>

</head>
<body>

<div class="ui-widget">
	<label for="comandos">buscar:</label>
	<input id="commands" style="width:50%"/>
</div>

<div id="resultset" style="position: relative;height: auto;width:60%">

</div>

</body>

</html>
