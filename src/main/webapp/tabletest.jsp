<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="es">
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
<script type="text/javascript" src="js/jquery.capsule.js"></script>
<script type="text/javascript" src="js/jquery.copyCSS.js"></script>
<!--script type="text/javascript" src="js/hyphenate.js"></script-->

<!-- script type="text/javascript" src="cps/tableing.cps.js"></script-->

<script type="text/javascript" src="js/jquery.hypher.js"></script>
<!--script type="text/javascript" src="js/es.js"></script-->


<script type="text/javascript" src="js/util.js"></script>
<link rel="stylesheet" type="text/css" href="css/layout.css">
<link rel="stylesheet" type="text/css" href="css/normalize.css">
<link rel="stylesheet" type="text/css" href="css/jquery-ui-1.8.4.cupertino.css">

<title>tabletest</title>


<script type="text/javascript">
$.capsule.host="http://localhost:8080/com.ferremundo.nio/";
$.capsule("http://localhost:8080/com.ferremundo.nio/js/cps/SysAuth.js");

$.capsule("http://localhost:8080/com.ferremundo.nio/cps/tableing.cps.js");
$.capsule("http://localhost:8080/com.ferremundo.nio/cps/global.cps.js");

function replaceAll(Source,stringToFind,stringToReplace){
	var temp = Source;
	var index = temp.indexOf(stringToFind);
	while(index != -1){
		temp = temp.replace(stringToFind,stringToReplace);
		index = temp.indexOf(stringToFind);
	}
	return temp;
}
function Item(quantity,code,unit,mark,description,unitPrice,total){
	this.quantity=quantity;
	this.unit=unit;
	this.description=description;
	this.code=code;
	this.mark=mark;
	this.unitPrice=unitPrice;
	this.total=quantity*unitPrice;

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
toCompile=function(template,vars){
	var html=template.$;
	for (var key in vars) {
		html=replaceAll(html,"$("+key+")",vars[key]);
	}
	return html;
};

$(document).ready(function(){

	//$.capsule(capsules);
	//$.capsule('cores');
	$('#thebutton').click(function(){
		$('body').feedCpsSysAuth("input",{
			failed:"falló",
			cancel:"cancelar?",
			destroy:true,
			todo:function(a){if(a=="ready")return true;return false;}
		});	
	});
	//$.capsule("<div>chingon soy $(person), tu eres $(name)</div>");
	//$.capsule("<div>chingon soy tu, tu eres $(name)</div>");
	var item= new Item(1,
			'code code code code code code code code code code code code code code code code',
			'unit unit unit unit unit unit unit unit unit unit unit unit unit unit unit unit unit unit unit ',
			'mark mark mark mark mark mark mark mark mark mark mark mark mark mark mark mark mark mark mark mark ',
			'description description description description description description description description description description description description description description '
			,1231,1231);
	var item2= new Item('.',
			'.',
			'.',
			'.',
			'.'
			,'.',1231);
	//$('#resultset').precapsule(item);
	//$('#resultset').precapsule({name:'ray',person:'el verga ray'});
	//$('#resultset').capsule({name:'la verga'});
	//$('#resultset').incapsule(item).addClass('box');//.addClass('odd').removeClass('boxie').addClass('boxie').css('background-color', '#dff');
	
	rs=randomString;
	for(var i=0;i<1;i++){
		var items=[];
		for(var j=0;j<1+Math.floor(Math.random() * 10);j++){
			var item= new Item(rs(1,1,1,2,'0'),
					rs(1,1,1,10,'A0'),
					rs(1,1,1,10,'A0'),
					rs(1,1,1,10,'A0'),
					rs(3,6,2,10,'A'),
					rs(1,1,1,3,'0'));
			items.push(item);
		}
		$('#resultset').inComFerremundoCpsGenericDiv({dclass:'box fleft', width:'100%',content:''})
		.comFerremundoCpsTB(items).addClass(i%2!=0?'odd':'even');
		$('#resultset').inCapsule({dclass:'box fleft', width:'100%',content:''},'com.ferremundo.cps.GenericDiv')
		.capsule(items,'com.ferremundo.cps.TB').addClass(i%2!=0?'odd':'even');

	}
	$.capsule('<div>$(content)</div>');//,"com.ferremundo.cps.Mydiv");
	$('#resultset').capsule({content:"the content"});
	//$('#resultset').capsule({person:"the person"});
	//$('#randid').feedSysAuth("input",{});
	//alert($('<p>asfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdf<p>').appendTo("document")[0].width);

/*	(function(a){
		function Fun(){
			Fun.prototype.say=function(s){
				console.log(s);
				Fun.log.push({say:s});
			};
			Fun.prototype.getlog=function(){
				return	Fun.log;
			};
		}
		Fun.log=[];
		a.fun=new Fun();
		
	})(a);*/
});
</script>
</head>
<body class=''>
<div id="resultset" style="width: 80%;" class=""></div>
<button id="thebutton">auth</button>
</body>
</html>