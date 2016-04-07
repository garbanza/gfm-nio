<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html><!-- PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"-->
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>datajet test</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/qunit-1.14.0.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-2.1.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/qunit-1.14.0.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/fmd/jquery.datajet.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.json-2.2.js"></script>

<script>

htmlNodesHere=function(){
	var list= new Array();
	$('*').each(function(){
		//console.log(Object.getPrototypeOf(this));
		if(!(list.indexOf(Object.getPrototypeOf(this).constructor.name)>=0))		
			list.push(Object.getPrototypeOf(this).constructor.name);
	});
	return list;
};
isHtmlNodeFromHere=function(obj){
	var list=htmlNodesHere();
	return list.indexOf(Object.getPrototypeOf(obj).constructor.name)>=0;	
};

jsonNonCyclicNonHtmlNode=function(obj){
	var seen = [];
	return JSON.stringify(obj, function(key, val) {
		if(typeof val == "function")return val.toString();
		if (typeof val == "object") {
			if(! isHtmlNodeFromHere(val)){
				if (seen.indexOf(val) >= 0){
	        		//$('#log').append('seen '+ val+"<br>");	
					return;
	        	}
	        	seen.push(val);
	        	//$('#log').append('push '+ val+"<br>");
	        	return val;
	    	}
			else{
				//$('#log').append('html '+ val+"<br>");
				return;
			}
	   }
	   //$('#log').append('!obj '+ val+"<br>");
	   return val;
	});
};

nameFilter=/^[a-z]*\w+(?:-(?![0-9])\w+)+$/;
methodFilter=/\[[\W|\w]+?\]/g;
getCameled=function(n){
	var dataName=n;
	var toCamel=n.match(/-[a-z]/g);
	if(toCamel!=null){
		var toUp=[];
		for(var i=0;i<toCamel.length;i++){
			var unSlashedUp=toCamel[i].replace("-",'').toUpperCase();
			dataName=dataName.replace(toCamel[i],unSlashedUp);
		}
	}
	return dataName;
};




$(document).ready(function(){
	//$('#log').append('seena,p.p,ñp ,ñp ,. p,ñp .,ñp ., <br>');

	var tstError=null;
	QUnit.assert.crashes=function(block,call, message){
		var tstError=null;
		try {
			//f(ars);
			//console.log('will fuck!!');
			call(block);
		}
		catch(err) {
		    //console.log(err.message);
		    tstError=err;
		}
		if(tstError!=null){
			var msg=message||'[ it crashed :) ] '+tstError.message;
			QUnit.push( true, [block,call], tstError, msg );
		}
		else{
			var msg='[ didnt crash :( ]';
			QUnit.push( false, [block,call], Error, msg );
		}
		
	};
	QUnit.assert.notCrashes=function(block,call,message){
		var tstError=null;
		try {
			//f(ars);
			//console.log('will fuck!!');
			call(block);
		}
		catch(err) {
		    //console.log(err.message);
		    tstError=err;
		}
		if(tstError!=null){
			var msg='[ it crashed :( ] '+tstError.message;
			QUnit.push( false, [block,call], tstError, msg );
		}
		else{
			var msg=message||'[ didnt crash :) ]';
			QUnit.push( true, [block,call], Error, msg );
		}
		
	};
	
	testMap={
			'=':'equal',
			'!=':'notEqual',
			'==':'deepEqual',
			'!==':'notDeepEqual',
			'ok':'ok',
			'#':'propEqual',
			'!#':'notPropEqual',
			'===':'strictEqual',
			'!===':'notStrictEqual',
			'*':'crashes',
			'!*':'notCrashes',
			
	}
	
	QUnit.test("datajet full test", function( assert ) {
		T={};
		for(var key in testMap){
			var key_=key;
			T[key_]=function(){
				
				
				assert[testMap[key_]](arguments);
				return T;
			};
			//$('#log').append(key+" " +testMap[key]+" " + arguments+'<br>');
			$('#log').append(jsonNonCyclicNonHtmlNode(T[key])+' DONE<br>');
		}
		$('#log').append(jsonNonCyclicNonHtmlNode(T));
		T['='](1,1,'1=1');
		
		var wn1='wrong-name1-';
		var wn2='wrong-2name';
		var goodName='g00d-n4m3';
		var goodNameCameled=getCameled(goodName);
		var dataset='dataset-test';
		var datarwx='datarwx';
		
		
		
		assert.crashes($().datajet,function(f){f({name:wn1,dataset:dataset});});
		assert.crashes($().datajet,function(f){f({name:wn2,dataset:dataset});});
		assert.notCrashes($().datajet,function(f){f({name:goodName,dataset:dataset,datarwx:datarwx});},'accepted name '+goodName);
		assert.notEqual($().datajet({name:goodName,dataset:dataset}),null,"factorying "+goodNameCameled);
		assert.notEqual($()[goodNameCameled],undefined,'$()['+goodNameCameled+'] exists');
		assert.notEqual($().g00dN4m3,undefined,'$().g00dN4m3 exists and accesed by dot as we want');
		var html=$('html');
		assert.ok(html.g00dN4m3('[+]'),'plus "'+goodName+'" - '+ html[0].outerHTML.match(/^<(?:.(?!>))+.>/)+" [&data] "+jsonNonCyclicNonHtmlNode(html[0][datarwx][dataset]));
		assert.ok(html.g00dN4m3('[+]prop-0'),'plus "prop-0" - '+ html[0].outerHTML.match(/^<(?:.(?!>))+.>/)+" [&data] "+jsonNonCyclicNonHtmlNode(html[0][datarwx][dataset]));
		assert.ok(html.g00dN4m3('[+]prop-1'),'plus "prop-1" - '+ html[0].outerHTML.match(/^<(?:.(?!>))+.>/)+" [&data] "+jsonNonCyclicNonHtmlNode(html[0][datarwx][dataset]));
		assert.ok(html.g00dN4m3('[-]prop-1'),'less "prop-1" - '+ html[0].outerHTML.match(/^<(?:.(?!>))+.>/)+" [&data] "+jsonNonCyclicNonHtmlNode(html[0][datarwx][dataset]));
		assert.ok(html.g00dN4m3('[+]prop-1',true),'plus "prop-1" & value - '+ html[0].outerHTML.match(/^<(?:.(?!>))+.>/)+" [&data] "+jsonNonCyclicNonHtmlNode(html[0][datarwx][dataset]));
		assert.equal(html.g00dN4m3('[-][+][:]prop-1',3.1416),3.1416,'less plus get "prop-1" & value - '+ html[0].outerHTML.match(/^<(?:.(?!>))+.>/)+" [&data] "+jsonNonCyclicNonHtmlNode(html[0][datarwx][dataset]));
		assert.ok(html.g00dN4m3('[+]prop-1',function(ops){QUnit.push( true, null, null, ops.msg );}),'plus "prop-1" & function - '+ html[0].outerHTML.match(/^<(?:.(?!>))+.>/)+" [&data] "+jsonNonCyclicNonHtmlNode(html[0][datarwx][dataset]));
		assert.ok(html.g00dN4m3('[.]prop-1',{msg:'fake test ganerated by the execution of the test bellow'}),'exec "prop-1" & options - '+ html[0].outerHTML.match(/^<(?:.(?!>))+.>/)+" [&data] "+jsonNonCyclicNonHtmlNode(html[0][datarwx][dataset]));
		assert.equal(html.g00dN4m3(['[-]prop-1',{'[+]prop-2':false},{'[+]':3.1416},"[:]"]),3.1416,'less prop-1 plus prop-2 selfPlus get'+html[0].outerHTML.match(/^<(?:.(?!>))+.>/)+" [&data] "+jsonNonCyclicNonHtmlNode(html[0][datarwx][dataset]));
		var body=$('body');
		assert.ok(body.g00dN4m3('[+]prop-1',true),'plus "prop-1" & value - '+ body[0].outerHTML.match(/^<(?:.(?!>))+.>/)+" [&data] "+jsonNonCyclicNonHtmlNode(body[0][datarwx][dataset]));
		assert.equal(html.g00dN4m3('[*][=]')[0],body[0]);
		assert.equal(html.g00dN4m3('[=][*]')[0],body[0]);
		assert.equal(html.g00dN4m3('[*][=][!]').length,0);
		assert.equal(html.g00dN4m3('[+]prop-4',true).g00dN4m3('[:]prop-4'),true,"chainable ok");
		//assert.ok(html[0].hasAttribute(goodName),'attr added "'+goodName+'" to "'+html[0].outerHTML.match(/^<(?:.(?!>))+.>/));////.substring(0,50)+'...');
		//assert.ok(html.g00dN4m3('[+]property1'),html,'adding property "property1" returns the same jQ-Object');
		
		
		//assert.equal(
		//$('html').dataTestHere-0()
	    /*
	    assert.equal(prettyDate(now, "2008/01/28 22:24:30"), "just now");
	    assert.equal(prettyDate(now, "2008/01/28 22:23:30"), "1 minute ago");
	    assert.equal(prettyDate(now, "2008/01/28 21:23:30"), "1 hour ago");
	    assert.equal(prettyDate(now, "2008/01/27 22:23:30"), "Yesterday");
	    assert.equal(prettyDate(now, "2008/01/26 22:23:30"), "2 days ago");
	    assert.equal(prettyDate(now, "2007/01/26 22:23:30"), undefined);
	    */
	});
});
</script>

</head>
<body>
	<div id="qunit"></div>
	<div id="log"></div>
</body>
</html>