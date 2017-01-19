
var casper = require('casper').create({
    verbose: true,
    logLevel: 'debug',
    pageSettings: {
        loadImages:  false,        // The WebPage instance used by Casper will
        loadPlugins: false         // use these settings
    },
    clientScripts: ['/opt/fm/einvoices-from-authority.js']
});

casper.userAgent('Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36');

var fs = require('fs');
var system = require('system');
var args=function(a){
	var as={};for(var i=0;i<a.length;i++){var splited=a[i].split("="); as[splited[0]]=splited[1];console.log(as[splited[0]])};
	return as;
}(system.args);

function isJsonString(str) {
    try {
        JSON.parse(str);
    } catch (e) {
        return false;
    }
    return true;
}
var captureCount=-1;
function pad(num, size) {
    var s = num+"";
    while (s.length < size) s = "0" + s;
    return s;
}
function nextCaptureCount(){
	captureCount+=1;
	return pad(captureCount,5);
}
casper.on('remote.message', function(msg) {
	if(isJsonString(msg)){
		var json=JSON.parse(msg);
		if (json.type=="invoice"){
			this.echo("WRITING "+args.output+"/"+json.name);
			fs.write(args.output+"/"+json.name,json.data,"w");
		}
		else if (json.type=="capture"){
			this.echo("CAPTURING /opt/fm/trash/"+nextCaptureCount()+".png");
			this.capture("/opt/fm/trash/"+nextCaptureCount()+".png");
			COUNT++;
		}
	}
	else this.echo('remote message caught: ' + msg);
});

casper.start('https://cfdiau.sat.gob.mx/nidp/app/login?id=SATUPCFDiCon&sid=0&option=credential&sid=0', function() {
	
});
casper.waitForSelector("#submit",
		function(){
			this.echo("selector submit found");
		},
		function(){
			this.echo("selector submit not found. Time out").exit();
		},
		60*1000
);
casper.then(function start(){
    this.echo("start");
    this.evaluate(function(user,pass){
    	console.log("pass:"+pass);
    	console.log("name:"+user);
    	$("[name='Ecom_Password']").val(pass);
    	$("[name='Ecom_User_ID']").val(user);
    },args.user,args.password);
    casper.capture("/opt/fm/trash/"+nextCaptureCount()+".png");
    this.evaluate(function(){
    	$("[name='submit']").click();
    });
});
casper.waitForText("Recuperar descargas de CFDI",
		function(){
			this.echo("selector found");
			this.capture("/opt/fm/trash/"+nextCaptureCount()+".png");
		},
		function(){
			this.echo("selector not found. Time out");
		}
);
casper.thenOpen('https://portalcfdi.facturaelectronica.sat.gob.mx/ConsultaReceptor.aspx');

casper.waitForSelector("#ctl00_MainContent_BtnBusqueda",
	function(){
		this.echo("selector ctl00_MainContent_BtnBusqueda found");
		this.capture("/opt/fm/trash/"+nextCaptureCount()+".png");
		//require('utils').dump(JSON.parse(this.getPageContent()));
		
	},
	function(){
		this.echo("selector submit not found. Time out").exit();
	},
	60*1000);

var resultSet,__VIEWSTATEVAL;

casper.then(function(){
	this.evaluate(function(){
		__VIEWSTATEVAL=$("#__VIEWSTATE").val();
		console.log("__VIEWSTATEVAL is "+__VIEWSTATEVAL.substring(0,20));
	});
});
casper.then(function click(){
	this.evaluate(function(){
		$("#ctl00_MainContent_RdoFechas").click();
	});
	this.capture("/opt/fm/trash/"+nextCaptureCount()+".png");
});
casper.waitFor(function check(){
	return this.evaluate(function(){
		console.log("EVALUATING");
		console.log($("#__VIEWSTATE").val().substring(0,20));
		if (__VIEWSTATEVAL!=$("#__VIEWSTATE").val())return true;
		console.log("__VIEWSTATEVAL not changing "+__VIEWSTATEVAL.substring(0,20));
		return false;
	});
}, function then(){
	this.echo("SINCE : "+ args.start+" "+args.end+" "+args.hours);
	this.evaluate(
			function(start,end,hours){
				window.setTimeout(function(){getSince(start,end,hours);},2000);
				},
				args.start,args.end,args.hours);
}, function onTimeout(){
	this.echo("TIMEOUT. exiting").exit();
},
	1000*10);

casper.waitFor(function checkDone(){
	
	return this.evaluate(function(){
		//console.log("EVALUATING");
		return TASK_DONE;
	});
}, function then(){
	this.capture("/opt/fm/trash/"+nextCaptureCount()+".png");
	/*resultSet=this.evaluate(function(){
		return BLOB;
	});
	for(var i=0;i<resultSet.length;i++){
		this.echo("WRITING "+args.output+"/"+resultSet[i].name);
		fs.write(args.output+"/"+resultSet[i].name,resultSet[i].data,"w");
	}*/
}, function onTimeout(){
	this.echo("TIMEOUT. exiting").exit();
},
	24*60*60*1000);

require('utils').dump(casper.steps.map(function(step) {
    return step.toString();
}));

casper.run(function(){
	this.echo("DONE CASPER").exit();
});

