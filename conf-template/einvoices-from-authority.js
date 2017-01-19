
var theLog="",
	csvData="",
	BLOB=[],
	TASK_DONE=false;
function subDivideIntoHoursOfTheSameDay(start,end,hours){
	var divs=[];/*, init=getDateFromFormat(start,"yyyyMMddHHmmss"),
		endOfTheDay=new Date(getDateFromFormat(start.subString(0,8)+"235959","yyyyMMddHHmmss")+1000*60*60*24),
		fin=getDateFromFormat(end,"yyyyMMddHHmmss");*/
	areMore=function(init,fin){if(init.getTime()<fin.getTime())return true;return false;}
	nextDivision=function(init,fin,hours){
		var div;
		var y=init.getFullYear(),
			m=init.getMonth()+1,
			d=init.getDate(),
			lastSecondOfTheDay=Date.parse(y+"/"+m+"/"+d)+1000*60*60*24-1000,
			initSecond=init.getTime(),
			finSecond=fin.getTime();
		if(initSecond>finSecond)return {success:false};
		else if((initSecond+1000*60*60*hours)>lastSecondOfTheDay){
			return {start:init, end: new Date(lastSecondOfTheDay), success: true, nextSecond: new Date(lastSecondOfTheDay+1000)}
		}
		else if((initSecond+1000*60*60*hours)>finSecond){
			return {start:init, end: new Date(finSecond), success: true, nextSecond: new Date(finSecond+1000)};
		}
		return {start:init,end:new Date(initSecond+1000*60*60*hours-1000),success:true, nextSecond: new Date(initSecond+1000*60*60*hours)};
		
	}
	for(var i=0,continue_=true,init=start,fin=end;continue_;i++){
		if(areMore(init,fin)){
			var nextDiv=nextDivision(init,fin,hours);
			divs.push(nextDiv);
			init=nextDiv.nextSecond;
			//console.log(nextDiv);
		}
		else continue_=false;
	}
	return divs;
}

function waitForMoreInvoices(divs,n,millis,callback){
	var length=$(".pgActual>table>tbody>tr>td,.pgOculta>table>tbody>tr>td").length/13;
	if(length>0){
		//LENGTH+=length;
		//callback(divs,n);
		return callback(divs,n);
	}
	else{
		if($("#ctl00_MainContent_PnlNoResultados").css("display")!="none"){
			var lg=new Date().format("yyyy.MM.dd-HH:mm:ss.ff")+" - It seems this period is empty "+divs[n]+". Next...\n";
			console.log(lg);
			theLog+=lg;
			return callback(divs,n+1);
		}
		else{
			var lg=new Date().format("yyyy.MM.dd-HH:mm:ss.ff")+" - waitForMoreInvoices ("+millis+")\n";
			console.log(lg);
			theLog+=lg;
			return window.setTimeout(function(){
				waitForMoreInvoices(divs,n,millis,callback);
			},millis);
		}
	}
};

function getNextInvoice(n,onFinish){
	console.log("TRYING to get invoice #"+n);
	var length=$(".pgActual>table>tbody>tr>td,.pgOculta>table>tbody>tr>td").length/13;
	console.log("TESTING n & length ["+n+" & "+length+"]");
	if(n>=length||length==0){
		console.log("CALLING onfinish");
		//onFinish();
		return onFinish();
	}
	var list=$(".pgActual>table>tbody>tr>td,.pgOculta>table>tbody>tr>td");
	var UUID=		$(list[(n)*13+(2-1)]).find("span").html();
	var rfc_emit=		$(list[(n)*13+(3-1)]).find("span").html();
	var name_emit=		$(list[(n)*13+(4-1)]).find("span").html();
	var rfc_recieve=	$(list[(n)*13+(5-1)]).find("span").html();
	var name_recieve=	$(list[(n)*13+(6-1)]).find("span").html();
	var date_emit=		$(list[(n)*13+(7-1)]).find("span").html();
	var date_cert=		$(list[(n)*13+(8-1)]).find("span").html();
	var pac=		$(list[(n)*13+(9-1)]).find("span").html();
	var amount=		$(list[(n)*13+(10-1)]).find("span").html();
	var efect=		$(list[(n)*13+(11-1)]).find("span").html();
	var status=		$(list[(n)*13+(12-1)]).find("span").html();
	var date_cancel=	$(list[(n)*13+(13-1)]).find("span").html();
	
	var fileToSave=date_emit+"_"+UUID+"_"+rfc_emit+"_"+rfc_recieve+"_"+status+".xml";
	if(date_cancel){
		console.log((n+1)+"/"+length+" CANCELADA "+$(list[(n)*13+(2-1)]).find("span").html());
		var text_to_xml='<?xml version="1.0" encoding="utf-8"?>\n<data>\n';
		text_to_xml+='\t<uuid>'+UUID+'</uuid>\n';
		text_to_xml+='\t<rfcemit>'+rfc_emit+'</rfcemit>\n';
		text_to_xml+='\t<nameemit>'+name_emit+'</nameemit>\n';
		text_to_xml+='\t<rfcrecieve>'+rfc_recieve+'</rfcrecieve>\n';
		text_to_xml+='\t<namerecieve>'+name_recieve+'</namerecieve>\n';
		text_to_xml+='\t<dateemit>'+date_emit+'</dateemit>\n';
		text_to_xml+='\t<datecert>'+date_cert+'</datecert>\n';
		text_to_xml+='\t<pac>'+pac+'</pac>\n';
		text_to_xml+='\t<amount>'+amount+'</amount>\n';
		text_to_xml+='\t<efect>'+efect+'</efect>\n';
		text_to_xml+='\t<status>'+status+'</status>\n';
		text_to_xml+='\t<datecancel>'+date_cancel+'</datecancel>\n';
		text_to_xml+='</data>\n';
		var csv_tmp=UUID+"\t"+rfc_emit+"\t"+name_emit+"\t"+rfc_recieve+"\t"+name_recieve+"\t"+date_emit+"\t"+date_cert+"\t"+pac+"\t"+amount+"\t"+efect+"\t"+status+"\t"+date_cancel;
		var lg=new Date().format("yyyy.MM.dd-HH:mm:ss.ff")+" CALCELED INVOICE "+csv_tmp+"\n";
		console.log(lg);
		csvData+=csv_tmp+"\n";
		theLog+=lg;

		console.log("INVOICE CANCELED "+fileToSave+"====================================");
		console.log(text_to_xml);
		console.log("/INVOICE CANCELED "+fileToSave+"====================================");
		//blob = new Blob([text_to_xml], {type: "text/plain;charset=utf-8"});
		//BLOB.push({name:fileToSave,data:text_to_xml});
		console.log(JSON.stringify({name:fileToSave,data:text_to_xml,type:"invoice"}));
		//saveAs(blob, fileToSave);
		/*console.log((n+1)+"/"+length+" CANCEL : "+fileToSave);*/
		return window.setTimeout(function(){getNextInvoice(n+1,onFinish);},1000);
		//return;
	}
	var toDown=$(list[(n)*13+(1-1)]).find(".BtnDescarga").attr("onclick").replace("return AccionCfdi('","").replace("','Recuperacion');","");
	jQuery.ajax({
			type :"GET",
			url : toDown,
			success : function(xml){
				if(xml.match("TimbreFiscalDigital")){ 

				var csv_tmp=UUID+"\t"+rfc_emit+"\t"+name_emit+"\t"+rfc_recieve+"\t"+name_recieve+"\t"+date_emit+"\t"+date_cert+"\t"+pac+"\t"+amount+"\t"+efect+"\t"+status+"\t"+date_cancel;
				var lg=new Date().format("yyyy.MM.dd-HH:mm:ss.ff")+" Writing file to disk "+fileToSave+"\n";
				console.log(lg);
				theLog+=lg;
				
				console.log("INVOICE "+fileToSave+"====================================");
				console.log(xml);
				console.log("/INVOICE "+fileToSave+"====================================");
				
				//var blob = new Blob([xml], {type: "text/plain;charset=utf-8"});
				//BLOB.push({name:fileToSave,data:xml});
				console.log(JSON.stringify({name:fileToSave,data:xml,type:"invoice"}));
				//saveAs(blob,fileToSave);
				lg=new Date().format("yyyy.MM.dd-HH:mm:ss.ff")+" SUCCESS INVOICE "+csv_tmp+"\n";
				console.log(lg);
				csvData+=csv_tmp+"\n";
				theLog+=lg;

				return window.setTimeout(function(){getNextInvoice(n+1,onFinish);},1000);
				//return;
				}
				else{
					var lg=new Date().format("yyyy.MM.dd-HH:mm:ss.ff")+" WARNING retrieving "+fileToSave+"... FIXXING";
					console.warn(lg);
					theLog+=lg;
					return window.setTimeout(function(){getNextInvoice(n,onFinish);},10000);
					//return;
				}
			},
			error : function(){
					var lg=new Date().format("yyyy.MM.dd-HH:mm:ss.ff")+" WARNING AJAX-ERROR retrieving "+fileToSave+"... FIXXING\n";
					console.warn(lg);
					theLog+=lg;
					return window.setTimeout(function(){getNextInvoice(n,onFinish);},4000);
					//return;
			}
	});
	
}
function pad(num, size) {
    var s = num+"";
    while (s.length < size) s = "0" + s;
    return s;
}

function getNextInvoicesPeriod(divs,n){
	if(n>=divs.length){
		$('body').append("<h id='finishedTask'>FINISHED</h>");
		TASK_DONE=true;
		return BLOB;
	}
	/*var sYear	=divs[n].start.getFullYear(),
		sMonth	=divs[n].start.getMonth()+1,
		sDay	=divs[n].start.getDate(),
		sHour	=divs[n].start.getHours(),
		sMinute	=divs[n].start.getMinutes(),
		sSecond	=divs[n].start.getSeconds(),
		eYear	=divs[n].end.getFullYear(),
		eMonth	=divs[n].end.getMonth()+1,
		eDay	=divs[n].end.getDate(),
		eHour	=divs[n].end.getHours(),
		eMinute	=divs[n].end.getMinutes(),
		eSecond	=divs[n].end.getSeconds();
		*/
	var
	sYear	=pad(divs[n].start.getFullYear(),4),
	sMonth	=pad(divs[n].start.getMonth()+1,2),
	sDay	=pad(divs[n].start.getDate(),2),
	sHour	=pad(divs[n].start.getHours(),2),
	sMinute	=pad(divs[n].start.getMinutes(),2),
	sSecond	=pad(divs[n].start.getSeconds(),2),
	eYear	=pad(divs[n].end.getFullYear(),4),
	eMonth	=pad(divs[n].end.getMonth()+1,2),
	eDay	=pad(divs[n].end.getDate(),2),
	eHour	=pad(divs[n].end.getHours(),2),
	eMinute	=pad(divs[n].end.getMinutes(),2),
	eSecond	=pad(divs[n].end.getSeconds(),2);
	
	$('[name="ctl00$MainContent$CldFecha$DdlAnio"]').val(sYear);
	$("#ctl00_MainContent_CldFecha_DdlMes").val(sMonth);
	$("#sbSelector_"+$("#ctl00_MainContent_CldFecha_DdlMes").attr("sb")).html(sMonth);
	$('[name="ctl00$MainContent$CldFecha$DdlDia"]').val(sDay);
	$("#sbSelector_"+$('[name="ctl00$MainContent$CldFecha$DdlDia"]').attr("sb")).html(sDay);
	$('[name="ctl00$MainContent$CldFecha$DdlHora"]').val(sHour);
	$("#sbSelector_"+$('[name="ctl00$MainContent$CldFecha$DdlHora"]').attr("sb")).html(sHour);
	$('[name="ctl00$MainContent$CldFecha$DdlMinuto"]').val(sMinute);
	$("#sbSelector_"+$('[name="ctl00$MainContent$CldFecha$DdlMinuto"]').attr("sb")).html(sMinute);
	$('[name="ctl00$MainContent$CldFecha$DdlSegundo"]').val(sSecond);
	$("#sbSelector_"+$('[name="ctl00$MainContent$CldFecha$DdlSegundo"]').attr("sb")).html(sSecond);
	$('[name="ctl00$MainContent$CldFecha$DdlHoraFin"]').val(eHour);
	$("#sbSelector_"+$('[name="ctl00$MainContent$CldFecha$DdlHoraFin"]').attr("sb")).html(eHour);
	$('[name="ctl00$MainContent$CldFecha$DdlMinutoFin"]').val(eMinute);
	$("#sbSelector_"+$('[name="ctl00$MainContent$CldFecha$DdlMinutoFin"]').attr("sb")).html(eMinute);
	$('[name="ctl00$MainContent$CldFecha$DdlSegundoFin"]').val(eSecond);
	$("#sbSelector_"+$('[name="ctl00$MainContent$CldFecha$DdlSegundoFin"]').attr("sb")).html(eSecond);
	console.log("CAPTURING "+""+divs[n].start+"-"+divs[n].end);
	console.log(JSON.stringify({type:"capture",name:""+divs[n].start+"-"+divs[n].end}));
	if(sDay!=$('[name="ctl00$MainContent$CldFecha$DdlDia"]').val()){
		console.log("DIFFERENT VALUES FOR THE DAY!! "+sDay+"!="+$('[name="ctl00$MainContent$CldFecha$DdlDia"]').val());
		return window.setTimeout(function(){getNextInvoicesPeriod(divs,n);},1000);
	}
	ocultaResultados();
	$(".pgActual>table,.pgOculta>table").empty();
	$('[name="ctl00$MainContent$BtnBusqueda"]').click();
	/*var wf=new WebForm_PostBackOptions("ctl00$MainContent$BtnBusqueda", "", true, "Fechas", "", false, false);
	__doPostBack(wf.eventTarget, wf.eventArgument);
	*/
	return waitForMoreInvoices(
		divs,n,2000,
		function(){
				getNextInvoice(0,function(){getNextInvoicesPeriod(divs,n+1);});
			}
	);
	
}

function parseDate_yyyyMMddHHmmss(s){
	var d=new Date();
	d.setYear(s.substring(0,4));
	d.setMonth(s.substring(4,6)-1);
	d.setDate(s.substring(6,8));
	d.setHours(s.substring(8,10));
	d.setMinutes(s.substring(10,12));
	d.setSeconds(s.substring(12,14));
	return d;
}

function getSince(start,end,byHours){
	//$("#ctl00_MainContent_RdoFechas").click();
	console.log("getSince-----");
	console.log("start/end/hours "+start+"/"+end+"/"+byHours);
	var 
		init=parseDate_yyyyMMddHHmmss(start),
		fin=parseDate_yyyyMMddHHmmss(end),
		divs=subDivideIntoHoursOfTheSameDay(init,fin,byHours);
	console.log("DIVS=--------\n"+JSON.stringify([divs[0],divs[divs.length-1]])+"\n----------");
	return getNextInvoicesPeriod(divs,0);
}

//getSince("20170101000000","20170111000000", 24*12);