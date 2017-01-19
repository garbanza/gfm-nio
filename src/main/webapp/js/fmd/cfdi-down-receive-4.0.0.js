// ==UserScript==
// @name        Facturas Recibidas
// @namespace   https://portalcfdi.facturaelectronica.sat.gob.mx/
// @description Facturas Recibidas
// @include     https://portalcfdi.facturaelectronica.sat.gob.mx/ConsultaReceptor.aspx
// @version     1
// @grant       none
// ==/UserScript==
$( document ).ready(function() {
/* filesaver*/
var saveAs=saveAs||"undefined"!=typeof navigator&&navigator.msSaveOrOpenBlob&&navigator.msSaveOrOpenBlob.bind(navigator)||function(e){"use strict";if("undefined"==typeof navigator||!/MSIE [1-9]\./.test(navigator.userAgent)){var t=e.document,n=function(){return e.URL||e.webkitURL||e},o=t.createElementNS("http://www.w3.org/1999/xhtml","a"),r="download"in o,i=function(n){var o=t.createEvent("MouseEvents");o.initMouseEvent("click",!0,!1,e,0,0,0,0,0,!1,!1,!1,!1,0,null),n.dispatchEvent(o)},a=e.webkitRequestFileSystem,c=e.requestFileSystem||a||e.mozRequestFileSystem,s=function(t){(e.setImmediate||e.setTimeout)(function(){throw t},0)},u="application/octet-stream",f=0,d=500,l=function(t){var o=function(){"string"==typeof t?n().revokeObjectURL(t):t.remove()};e.chrome?o():setTimeout(o,d)},v=function(e,t,n){t=[].concat(t);for(var o=t.length;o--;){var r=e["on"+t[o]];if("function"==typeof r)try{r.call(e,n||e)}catch(i){s(i)}}},p=function(t,s){var d,p,w,y=this,m=t.type,S=!1,h=function(){v(y,"writestart progress write writeend".split(" "))},O=function(){if((S||!d)&&(d=n().createObjectURL(t)),p)p.location.href=d;else{var o=e.open(d,"_blank");void 0==o&&"undefined"!=typeof safari&&(e.location.href=d)}y.readyState=y.DONE,h(),l(d)},b=function(e){return function(){return y.readyState!==y.DONE?e.apply(this,arguments):void 0}},g={create:!0,exclusive:!1};return y.readyState=y.INIT,s||(s="download"),r?(d=n().createObjectURL(t),o.href=d,o.download=s,i(o),y.readyState=y.DONE,h(),void l(d)):(/^\s*(?:text\/(?:plain|xml)|application\/xml|\S*\/\S*\+xml)\s*;.*charset\s*=\s*utf-8/i.test(t.type)&&(t=new Blob(["﻿",t],{type:t.type})),e.chrome&&m&&m!==u&&(w=t.slice||t.webkitSlice,t=w.call(t,0,t.size,u),S=!0),a&&"download"!==s&&(s+=".download"),(m===u||a)&&(p=e),c?(f+=t.size,void c(e.TEMPORARY,f,b(function(e){e.root.getDirectory("saved",g,b(function(e){var n=function(){e.getFile(s,g,b(function(e){e.createWriter(b(function(n){n.onwriteend=function(t){p.location.href=e.toURL(),y.readyState=y.DONE,v(y,"writeend",t),l(e)},n.onerror=function(){var e=n.error;e.code!==e.ABORT_ERR&&O()},"writestart progress write abort".split(" ").forEach(function(e){n["on"+e]=y["on"+e]}),n.write(t),y.abort=function(){n.abort(),y.readyState=y.DONE},y.readyState=y.WRITING}),O)}),O)};e.getFile(s,{create:!1},b(function(e){e.remove(),n()}),b(function(e){e.code===e.NOT_FOUND_ERR?n():O()}))}),O)}),O)):void O())},w=p.prototype,y=function(e,t){return new p(e,t)};return w.abort=function(){var e=this;e.readyState=e.DONE,v(e,"abort")},w.readyState=w.INIT=0,w.WRITING=1,w.DONE=2,w.error=w.onwritestart=w.onprogress=w.onwrite=w.onabort=w.onerror=w.onwriteend=null,y}}("undefined"!=typeof self&&self||"undefined"!=typeof window&&window||this.content);"undefined"!=typeof module&&module.exports?module.exports.saveAs=saveAs:"undefined"!=typeof define&&null!==define&&null!=define.amd&&define([],function(){return saveAs});
/* /filesaver*/
/*! jquery-dateFormat 18-05-2015 */
var DateFormat={};!function(a){var b=["Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"],c=["Sun","Mon","Tue","Wed","Thu","Fri","Sat"],d=["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"],e=["January","February","March","April","May","June","July","August","September","October","November","December"],f={Jan:"01",Feb:"02",Mar:"03",Apr:"04",May:"05",Jun:"06",Jul:"07",Aug:"08",Sep:"09",Oct:"10",Nov:"11",Dec:"12"},g=/\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}\.?\d{0,3}[Z\-+]?(\d{2}:?\d{2})?/;a.format=function(){function a(a){return b[parseInt(a,10)]||a}function h(a){return c[parseInt(a,10)]||a}function i(a){var b=parseInt(a,10)-1;return d[b]||a}function j(a){var b=parseInt(a,10)-1;return e[b]||a}function k(a){return f[a]||a}function l(a){var b,c,d,e,f,g=a,h="";return-1!==g.indexOf(".")&&(e=g.split("."),g=e[0],h=e[e.length-1]),f=g.split(":"),3===f.length?(b=f[0],c=f[1],d=f[2].replace(/\s.+/,"").replace(/[a-z]/gi,""),g=g.replace(/\s.+/,"").replace(/[a-z]/gi,""),{time:g,hour:b,minute:c,second:d,millis:h}):{time:"",hour:"",minute:"",second:"",millis:""}}function m(a,b){for(var c=b-String(a).length,d=0;c>d;d++)a="0"+a;return a}return{parseDate:function(a){var b,c,d={date:null,year:null,month:null,dayOfMonth:null,dayOfWeek:null,time:null};if("number"==typeof a)return this.parseDate(new Date(a));if("function"==typeof a.getFullYear)d.year=String(a.getFullYear()),d.month=String(a.getMonth()+1),d.dayOfMonth=String(a.getDate()),d.time=l(a.toTimeString()+"."+a.getMilliseconds());else if(-1!=a.search(g))b=a.split(/[T\+-]/),d.year=b[0],d.month=b[1],d.dayOfMonth=b[2],d.time=l(b[3].split(".")[0]);else switch(b=a.split(" "),6===b.length&&isNaN(b[5])&&(b[b.length]="()"),b.length){case 6:d.year=b[5],d.month=k(b[1]),d.dayOfMonth=b[2],d.time=l(b[3]);break;case 2:c=b[0].split("-"),d.year=c[0],d.month=c[1],d.dayOfMonth=c[2],d.time=l(b[1]);break;case 7:case 9:case 10:d.year=b[3],d.month=k(b[1]),d.dayOfMonth=b[2],d.time=l(b[4]);break;case 1:c=b[0].split(""),d.year=c[0]+c[1]+c[2]+c[3],d.month=c[5]+c[6],d.dayOfMonth=c[8]+c[9],d.time=l(c[13]+c[14]+c[15]+c[16]+c[17]+c[18]+c[19]+c[20]);break;default:return null}return d.date=d.time?new Date(d.year,d.month-1,d.dayOfMonth,d.time.hour,d.time.minute,d.time.second,d.time.millis):new Date(d.year,d.month-1,d.dayOfMonth),d.dayOfWeek=String(d.date.getDay()),d},date:function(b,c){try{var d=this.parseDate(b);if(null===d)return b;for(var e,f=d.year,g=d.month,k=d.dayOfMonth,l=d.dayOfWeek,n=d.time,o="",p="",q="",r=!1,s=0;s<c.length;s++){var t=c.charAt(s),u=c.charAt(s+1);if(r)"'"==t?(p+=""===o?"'":o,o="",r=!1):o+=t;else switch(o+=t,q="",o){case"ddd":p+=a(l),o="";break;case"dd":if("d"===u)break;p+=m(k,2),o="";break;case"d":if("d"===u)break;p+=parseInt(k,10),o="";break;case"D":k=1==k||21==k||31==k?parseInt(k,10)+"st":2==k||22==k?parseInt(k,10)+"nd":3==k||23==k?parseInt(k,10)+"rd":parseInt(k,10)+"th",p+=k,o="";break;case"MMMM":p+=j(g),o="";break;case"MMM":if("M"===u)break;p+=i(g),o="";break;case"MM":if("M"===u)break;p+=m(g,2),o="";break;case"M":if("M"===u)break;p+=parseInt(g,10),o="";break;case"y":case"yyy":if("y"===u)break;p+=o,o="";break;case"yy":if("y"===u)break;p+=String(f).slice(-2),o="";break;case"yyyy":p+=f,o="";break;case"HH":p+=m(n.hour,2),o="";break;case"H":if("H"===u)break;p+=parseInt(n.hour,10),o="";break;case"hh":e=0===parseInt(n.hour,10)?12:n.hour<13?n.hour:n.hour-12,p+=m(e,2),o="";break;case"h":if("h"===u)break;e=0===parseInt(n.hour,10)?12:n.hour<13?n.hour:n.hour-12,p+=parseInt(e,10),o="";break;case"mm":p+=m(n.minute,2),o="";break;case"m":if("m"===u)break;p+=n.minute,o="";break;case"ss":p+=m(n.second.substring(0,2),2),o="";break;case"s":if("s"===u)break;p+=n.second,o="";break;case"S":case"SS":if("S"===u)break;p+=o,o="";break;case"SSS":var v="000"+n.millis.substring(0,3);p+=v.substring(v.length-3),o="";break;case"a":p+=n.hour>=12?"PM":"AM",o="";break;case"p":p+=n.hour>=12?"p.m.":"a.m.",o="";break;case"E":p+=h(l),o="";break;case"'":o="",r=!0;break;default:p+=t,o=""}}return p+=q}catch(w){return console&&console.log&&console.log(w),b}},prettyDate:function(a){var b,c,d;return("string"==typeof a||"number"==typeof a)&&(b=new Date(a)),"object"==typeof a&&(b=new Date(a.toString())),c=((new Date).getTime()-b.getTime())/1e3,d=Math.floor(c/86400),isNaN(d)||0>d?void 0:60>c?"just now":120>c?"1 minute ago":3600>c?Math.floor(c/60)+" minutes ago":7200>c?"1 hour ago":86400>c?Math.floor(c/3600)+" hours ago":1===d?"Yesterday":7>d?d+" days ago":31>d?Math.ceil(d/7)+" weeks ago":d>=31?"more than 5 weeks ago":void 0},toBrowserTimeZone:function(a,b){return this.date(new Date(a),b||"MM/dd/yyyy HH:mm:ss")}}}()}(DateFormat),function(a){a.format=DateFormat.format}(jQuery);

var N=0;
var interval_=null;
search_interval_=null;
var date2=null;
var theNow=$.format.date(new Date(),"yyyy.MM.dd-HH:mm:ss");
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
			var lg=$.format.date(new Date(),"yyyy.MM.dd-HH:mm:ss.ff")+" - It seems this month is empty "+month+". Next...\n";
			console.log(lg);
			theLog+=lg;
			nextInvoice(month+1,year,-1);
		}
		else{
			var lg=$.format.date(new Date(),"yyyy.MM.dd-HH:mm:ss.ff")+" - waitForMoreInvoices ("+millis+")\n";
			console.log(lg);
			theLog+=lg;
			window.setTimeout(function(){
				waitForMoreInvoices(millis,month,year);
			},millis);
		}
	}
};

nextInvoice=function(month,year,n){
	var lg=$.format.date(new Date(),"yyyy.MM.dd-HH:mm:ss.ff")+" - nextInvoice ("+month+","+year+","+n+")\n";
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
		var lg=$.format.date(new Date(),"yyyy.MM.dd-HH:mm:ss.ff")+" Writing Log and csv files to disk "+theLogFile+".\n"+LENGTH+" files downloaded... END\n";
		console.log(lg);
		theLog+=lg;
		var blob = new Blob([theLog], {type: "text/plain;charset=utf-8"});
		saveAs(blob, theLogFile);
		blob = new Blob([csvData], {type: "text/plain;charset=utf-8"});
		saveAs(blob, csvDataFile);
		$("#getthefacts")[0].disabled=false;
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
			var lg=$.format.date(new Date(),"yyyy.MM.dd-HH:mm:ss.ff")+" Go to next month if possible...\n";
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
				var lg=$.format.date(new Date(),"yyyy.MM.dd-HH:mm:ss.ff")+" CALCELED INVOICE "+csv_tmp+"\n";
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
						var lg=$.format.date(new Date(),"yyyy.MM.dd-HH:mm:ss.ff")+" Writing file to disk "+fileToSave+"\n";
						console.log(lg);
						theLog+=lg;
						

						var blob = new Blob([xml], {type: "text/plain;charset=utf-8"});

						saveAs(blob,fileToSave);
						lg=$.format.date(new Date(),"yyyy.MM.dd-HH:mm:ss.ff")+" SUCCESS INVOICE "+csv_tmp+"\n";
						console.log(lg);
						csvData+=csv_tmp+"\n";
						theLog+=lg;

						window.setTimeout(function(){nextInvoice(month_,year_,n+1);},interval_);
						return;
						}
						else{
							var lg=$.format.date(new Date(),"yyyy.MM.dd-HH:mm:ss.ff")+" WARNING retrieving "+fileToSave+"... FIXXING";
							console.warn(lg);
							theLog+=lg;
							window.setTimeout(function(){nextInvoice(month_,year_,n);},interval_);
							return;
						}
					},
					error : function(){
							var lg=$.format.date(new Date(),"yyyy.MM.dd-HH:mm:ss.ff")+" WARNING AJAX-ERROR retrieving "+fileToSave+"... FIXXING\n";
							console.warn(lg);
							theLog+=lg;
							window.setTimeout(function(){nextInvoice(month_,year_,n);},interval_);
							return;
					}
			});

		}
	}
	else{
		var lg=$.format.date(new Date(),"yyyy.MM.dd-HH:mm:ss.ff")+" Go to next month if possible...\n";
			console.log(lg);
			theLog+=lg;
		window.setTimeout(function(){nextInvoice(month_+1,year_,-1);},interval_);
		return;
	}
	
};

interval_=300;
search_interval_=1000;

$("#ctl00_MainContent_RdoFechas").click();
$("body").prepend("get facts from(MMYY)<input id='fromdate_'>to(MMYY)<input id='todate_'><button type='button' id='getthefacts'>Get!</button> ");
$("#getthefacts").click(function(){
	$("#getthefacts")[0].disabled=true;
	var startMonth=$("#fromdate_").val().substring(0,2)*1;
	var startYear=("20"+$("#fromdate_").val().substring(2,4))*1;
	var endMonth=$("#todate_").val().substring(0,2)*1;
	var endYear=("20"+$("#todate_").val().substring(2,4))*1;
	date2=new Date(endYear,endMonth,1);
	var lg=$.format.date(new Date(),"yyyy.MM.dd-HH:mm:ss.ff")+" - START LOGGING\n";
	theLog=lg;
	console.log(lg);
	lg=$.format.date(new Date(),"yyyy.MM.dd-HH:mm:ss.ff")+" - Going to download from date "+startYear+"."+startMonth+".1\n";
	theLog+=lg;
	console.log(lg);
	nextInvoice(startMonth,startYear,-1);
	
});

});
