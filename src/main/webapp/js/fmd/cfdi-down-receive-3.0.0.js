javascript:(function() {
/* filesaver*/
var saveAs=saveAs||"undefined"!=typeof navigator&&navigator.msSaveOrOpenBlob&&navigator.msSaveOrOpenBlob.bind(navigator)||function(e){"use strict";if("undefined"==typeof navigator||!/MSIE [1-9]\./.test(navigator.userAgent)){var t=e.document,n=function(){return e.URL||e.webkitURL||e},o=t.createElementNS("http://www.w3.org/1999/xhtml","a"),r="download"in o,i=function(n){var o=t.createEvent("MouseEvents");o.initMouseEvent("click",!0,!1,e,0,0,0,0,0,!1,!1,!1,!1,0,null),n.dispatchEvent(o)},a=e.webkitRequestFileSystem,c=e.requestFileSystem||a||e.mozRequestFileSystem,s=function(t){(e.setImmediate||e.setTimeout)(function(){throw t},0)},u="application/octet-stream",f=0,d=500,l=function(t){var o=function(){"string"==typeof t?n().revokeObjectURL(t):t.remove()};e.chrome?o():setTimeout(o,d)},v=function(e,t,n){t=[].concat(t);for(var o=t.length;o--;){var r=e["on"+t[o]];if("function"==typeof r)try{r.call(e,n||e)}catch(i){s(i)}}},p=function(t,s){var d,p,w,y=this,m=t.type,S=!1,h=function(){v(y,"writestart progress write writeend".split(" "))},O=function(){if((S||!d)&&(d=n().createObjectURL(t)),p)p.location.href=d;else{var o=e.open(d,"_blank");void 0==o&&"undefined"!=typeof safari&&(e.location.href=d)}y.readyState=y.DONE,h(),l(d)},b=function(e){return function(){return y.readyState!==y.DONE?e.apply(this,arguments):void 0}},g={create:!0,exclusive:!1};return y.readyState=y.INIT,s||(s="download"),r?(d=n().createObjectURL(t),o.href=d,o.download=s,i(o),y.readyState=y.DONE,h(),void l(d)):(/^\s*(?:text\/(?:plain|xml)|application\/xml|\S*\/\S*\+xml)\s*;.*charset\s*=\s*utf-8/i.test(t.type)&&(t=new Blob(["﻿",t],{type:t.type})),e.chrome&&m&&m!==u&&(w=t.slice||t.webkitSlice,t=w.call(t,0,t.size,u),S=!0),a&&"download"!==s&&(s+=".download"),(m===u||a)&&(p=e),c?(f+=t.size,void c(e.TEMPORARY,f,b(function(e){e.root.getDirectory("saved",g,b(function(e){var n=function(){e.getFile(s,g,b(function(e){e.createWriter(b(function(n){n.onwriteend=function(t){p.location.href=e.toURL(),y.readyState=y.DONE,v(y,"writeend",t),l(e)},n.onerror=function(){var e=n.error;e.code!==e.ABORT_ERR&&O()},"writestart progress write abort".split(" ").forEach(function(e){n["on"+e]=y["on"+e]}),n.write(t),y.abort=function(){n.abort(),y.readyState=y.DONE},y.readyState=y.WRITING}),O)}),O)};e.getFile(s,{create:!1},b(function(e){e.remove(),n()}),b(function(e){e.code===e.NOT_FOUND_ERR?n():O()}))}),O)}),O)):void O())},w=p.prototype,y=function(e,t){return new p(e,t)};return w.abort=function(){var e=this;e.readyState=e.DONE,v(e,"abort")},w.readyState=w.INIT=0,w.WRITING=1,w.DONE=2,w.error=w.onwritestart=w.onprogress=w.onwrite=w.onabort=w.onerror=w.onwriteend=null,y}}("undefined"!=typeof self&&self||"undefined"!=typeof window&&window||this.content);"undefined"!=typeof module&&module.exports?module.exports.saveAs=saveAs:"undefined"!=typeof define&&null!==define&&null!=define.amd&&define([],function(){return saveAs});
/* /filesaver*/

var N=0;
var interval_=null;
search_interval_=null;
var date2=null;
var theNow=new Date().format("yyyy.MM.dd-HH:mm:ss");
var theLog="";
var theLogFile="sat-"+theNow+".log";
var csvData="";
var csvDataFile="sat-"+theNow+".csv";
var LENGTH=0;
function daysInMonth(month,year) {
    return new Date(year, month, 0).getDate();
};

(function ($) {
    $.fn.inlineStyle = function (prop) {
        return this.prop("style")[$.camelCase(prop)];
    };
}(jQuery));

function waitForMoreInvoices(millis,month,year){
	var length=$(".pgActual>table>tbody>tr>td,.pgOculta>table>tbody>tr>td").length/13;
	if(length>0){
		LENGTH+=length;
		nextInvoice(month,year,0);
		return;
	}
	else{
		if($("#ctl00_MainContent_PnlNoResultados").inlineStyle("display")!="none"){
			var lg=new Date().format("yyyy.MM.dd-HH:mm:ss.ff")+" - It seems this month is empty "+month+". Next...\n";
			console.log(lg);
			theLog+=lg;
			nextInvoice(month+1,year,-1);
		}
		else{
			var lg=new Date().format("yyyy.MM.dd-HH:mm:ss.ff")+" - waitForMoreInvoices ("+millis+")\n";
			console.log(lg);
			theLog+=lg;
			window.setTimeout(function(){
				waitForMoreInvoices(millis,month,year);
			},millis);
		}
	}
};

nextInvoice=function(month,year,n){
	var lg=new Date().format("yyyy.MM.dd-HH:mm:ss.ff")+" - nextInvoice ("+month+","+year+","+n+")\n";
	console.log(lg);
	theLog+=lg;
	var month_=month;
	var year_=year;
	if(month_>12){
		month_=1;
		year_+=1;
	};
	var date1=new Date(year_,month_,1);

	if(date1.getTime()>date2.getTime()){
		var lg=new Date().format("yyyy.MM.dd-HH:mm:ss.ff")+" Writing Log and csv files to disk "+theLogFile+".\n"+LENGTH+" files downloaded... END\n";
		console.log(lg);
		theLog+=lg;
		var blob = new Blob([theLog], {type: "text/plain;charset=utf-8"});
		saveAs(blob, theLogFile);
		blob = new Blob([csvData], {type: "text/plain;charset=utf-8"});
		saveAs(blob, csvDataFile);
		return;
	};

	var length=$(".pgActual>table>tbody>tr>td,.pgOculta>table>tbody>tr>td").length/13;
	if(n<0){
		var days_=daysInMonth(month_,year_);
		$("#ctl00_MainContent_CldFecha_DdlMes").val(month_);
		$("#sbSelector_"+$("#ctl00_MainContent_CldFecha_DdlMes").attr("sb")).html(month_);
		$('[name="ctl00$MainContent$CldFecha$DdlAnio"]').val(year_);
		$("#sbSelector_"+$('[name="ctl00$MainContent$CldFecha$DdlAnio"]').attr("sb")).html(year_);
		ocultaResultados();
		$(".pgActual>table,.pgOculta>table").empty();
		var wf=new WebForm_PostBackOptions("ctl00$MainContent$BtnBusqueda", "", true, "Fechas", "", false, false);
		__doPostBack(wf.eventTarget, wf.eventArgument);
		waitForMoreInvoices(
			search_interval_,month_,year_
		);
		return;
	}
	if(length>0){
		if(n>=length){
			var lg=new Date().format("yyyy.MM.dd-HH:mm:ss.ff")+" Go to next month if possible...\n";
			console.log(lg);
			theLog+=lg;
			window.setTimeout(function(){nextInvoice(month_+1,year_,-1);},interval_);
			return;
		}
		else{
			++N;
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

			var fileToSave=date_cert+"_"+UUID+"_"+rfc_emit+"_"+rfc_recieve+"_"+status+".xml";
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

				var blob = new Blob([text_to_xml], {type: "text/plain;charset=utf-8"});
				saveAs(blob, fileToSave);
				/*console.log((n+1)+"/"+length+" CANCEL : "+fileToSave);*/
				window.setTimeout(function(){nextInvoice(month_,year_,n+1);},interval_);
				return;
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
						

						var blob = new Blob([xml], {type: "text/plain;charset=utf-8"});

						saveAs(blob,fileToSave);
						lg=new Date().format("yyyy.MM.dd-HH:mm:ss.ff")+" SUCCESS INVOICE "+csv_tmp+"\n";
						console.log(lg);
						csvData+=csv_tmp+"\n";
						theLog+=lg;

						window.setTimeout(function(){nextInvoice(month_,year_,n+1);},interval_);
						return;
						}
						else{
							var lg=new Date().format("yyyy.MM.dd-HH:mm:ss.ff")+" WARNING retrieving "+fileToSave+"... FIXXING";
							console.warn(lg);
							theLog+=lg;
							window.setTimeout(function(){nextInvoice(month_,year_,n);},interval_);
							return;
						}
					},
					error : function(){
							var lg=new Date().format("yyyy.MM.dd-HH:mm:ss.ff")+" WARNING AJAX-ERROR retrieving "+fileToSave+"... FIXXING\n";
							console.warn(lg);
							theLog+=lg;
							window.setTimeout(function(){nextInvoice(month_,year_,n);},interval_);
							return;
					}
			});

		}
	}
	else{
		var lg=new Date().format("yyyy.MM.dd-HH:mm:ss.ff")+" Go to next month if possible...\n";
			console.log(lg);
			theLog+=lg;
		window.setTimeout(function(){nextInvoice(month_+1,year_,-1);},interval_);
		return;
	}
	
};

interval_=300;
search_interval_=1000;
date2=new Date(2015,8,1);
startYear=2015;
startMonth=7;
var lg=new Date().format("yyyy.MM.dd-HH:mm:ss.ff")+" - START LOGGING\n";
theLog+=lg;
console.log(lg);
lg=new Date().format("yyyy.MM.dd-HH:mm:ss.ff")+" - Going to download from date "+startYear+"."+startMonth+".1\n";
theLog+=lg;
console.log(lg);
nextInvoice(startMonth,startYear,-1);

})();


