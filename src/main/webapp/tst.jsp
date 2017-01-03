<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>op</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">


<link rel="stylesheet" href="${pageContext.request.contextPath}/mb/jquery.mobile.custom.structure.min.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/mb/jquery.mobile.custom.theme.min.css" />

<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-2.1.1.min.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/mb/jquery.mobile.custom.min.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.capsule.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/fmd/jquery.commandro.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/fmd/jquery.mudata-0.9.0.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/fmd/jquery.datam.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/fmd/auth.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.idle.min.js"></script>
<!--script type="text/javascript" src="${pageContext.request.contextPath}/dwr/engine.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/dwr/interface/OnlineClients.js"></script-->

<script type="text/javascript">
var SHOPMAN={name:'shopman',login:'login'};
var CONTEXT_PATH="${pageContext.request.contextPath}";
var AUTHORIZED=true;

$.capsule([
           {
        	   name:"TABLE",
        	   doc:"form a table from data. Asumptions data.head,data.body",
        	   defaults:{
        		   id:$.capsule.uniqueId("TABLE"),
        		   tdclass:'tdclass'
        	   },
        	   html:function(data){
        		   var ret="<table id='$(id)'>";
          		   if(data.head){
          			   ret+="<thead><tr>";
            		   for(var i=0;i<data.head.length;i++){
            			   ret+='<th class="$(tdclass)">'+data.head[i]+'</th>';
            		   }
            		   ret+='</tr></thead>';
          		   }
          		   ret+="<tbody>"
        		   for(var i=0;i<data.body.length;i++){
        			   ret+='<tr id="'+i+'">';
        			   for(var j=0;j<data.body[i].length;j++){
            			   ret+='<td class="$(tdclass)">'+data.body[i][j]+'</td>';
            		   }
        			   ret+='</tr>';
        		   }
        		   ret+='</tbody></table>';
        		   return ret;
        	   },
        	   css:function(){
        		   return {
        			   '.tdclass':{'padding' : '11.2px 2px'}
        		   };
        	   }
           }
]);


var THEME='c';

$(document).ready(function(){
	$('.login').html('userlogin');//':'+SHOPMAN.name+":"+SHOPMAN.login);
	$( document ).idle({
		idle:1000*60*5,
		onIdle: function(){
			var doIdle=document.isIdle?false:true;
			if(doIdle)document.isIdle=true;
			else return;
			//lockin();
			$('[data-role="header"]').each(function(){
				$(this).find('img').attr('src','mb/images/icons-png/lock-black.png');
			});
			var passinid='passid'+$.capsule.randomString(1,15,'aA0');
			var id=passinTest({
				id:passinid,
				success:function(data){
					AUTHORIZED=true;
					//alert("success " +data.authenticated+". #"+passinid+" to be removed")
					$('#'+passinid).remove();
					document.isIdle=false;
					//$('#commands').focus();
					$('[data-role="header"]').each(function(){
						$(this).find('img').attr('src','mb/images/icons-png/lock-white.png');
					});
				},
				message:"desbloquear "+SHOPMAN.name+"-"+SHOPMAN.login
			});
			$('#'+id).css({width:$(document).width(),height:$(document).height()})
			;//.enhanceWithin();
		}
	});
	$('[data-role="header"]').each(function(){$(this).click(function(){
		var doIdle=document.isIdle?false:true;
		if(doIdle)document.isIdle=true;
		else return;
		//lockin();
		$(this).find('img').attr('src','mb/images/icons-png/lock-black.png');
		var this_=this;
		var passinid='passid'+$.capsule.randomString(1,15,'aA0');
		var id=passinTest({
			id:passinid,
			success:function(data){
				$(this_).find('img').attr('src','mb/images/icons-png/lock-white.png');
				AUTHORIZED=true;
				//alert("success " +data.authenticated+". #"+passinid+" to be removed")
				$('#'+passinid).remove();
				document.isIdle=false;
				//$('#commands').focus();
			},
			message:"desbloquear "+SHOPMAN.name+"-"+SHOPMAN.login
		});
		$('#'+id).css({width:$(document).width(),height:$(document).height()})
		.enhanceWithin();
	});}).css('cursor','pointer');
	var data={body:[]};
	for(var i=0;i<100;i++){
		data.body.push([$.capsule.randomString(1,1,3,7,'a'),
		                $.capsule.randomString(4,8,3,7,'a'),
		                $.capsule.randomString(1,1,3,8,'a'),
		                $.capsule.randomString(1,1,3,5,'a'),
		                $.capsule.randomString(1,1,1,7,'0'),
		                $.capsule.randomString(1,1,1,5,'0')]);
	}
	$('#productCode').commandro({
		data:data,/*{body:[
			"@registrar-producto",
			"@inventarear-producto",
			"@editar-producto",
			"@editar-operador",
			]},*/
		clickable:true,
		renderTo:$('<div></div>').appendTo('#admin'),
		render:function(data){
    		var inp=$(this.input);
			var pos=inp.position();
			var height=inp[0].offsetHeight;
			var width=inp[0].offsetWidth;
			this.renderTo.empty()
			.css({//'background-color':'#fff','border':'1px','border-style':'outset',
    			//**TODO fix this height thing 
    			'background-color':'#fff',top:pos.top+height,left:pos.left,
				position:'absolute',width:'auto',padding: '11px 2px', margin: 0,
				'min-width':width
			})
			.inTABLE(data).css({'min-width':width})
			.attr({'data-role':'table', 'class':'ui-responsive'})
			;//.enhanceWithin();
			/*
			ret="";
    		for(var i=0;i<data.length;i++){
    			ret+='<li>';
       			ret+=data[i];
    			ret+='</li>';
    		}
    		ret==""?$(this.renderTo).empty():$(this.renderTo).empty().append(ret)
    		.css({//'background-color':'#fff','border':'1px','border-style':'outset',
    			//**TODO fix this height thing 
    			top:pos.top+height,left:pos.left,
				position:'absolute',//width:'auto',padding: 0, margin: 0,
				'min-width':width
			})
			.listview().listview("refresh").trigger('create');
    		*/
    	},
    	htmlMenuElements:function(){return $(this.renderTo).find('tbody>tr');},
    	highlight:function(init,end){
    		var els=this.htmlMenuElements();
    		els.eq(init).css({'background-color':'#fff'});
    		els.eq(end).css({'background-color':'#efefef'});
    	},
		fire:{
			enter:function(this_,data){
				console.log(data[0]);
				$(this_.input).val(data[0]);
				this_.inputValue=data[0];
				this_.hidde();
    			$(this_.renderTo).empty();
    			this_.menu=[data];
    			this_.selectedIndex=0;
    			//$(this_.input).animate({'color':'#00ff00'},50).animate({'color':'#000'},500);
			}
		}
		/*,
		render:function(data){
			var inp=$(this.input);
			var pos=inp.position();
			var height=inp.height();
			this.renderTo.empty()
			.css({//**TODO fix this height thing 
				top:pos.top+1.5*height,left:pos.left,
				position:'absolute',width:'auto',padding: 0, margin: 0})
			.aTable({body:data}).css({'background-color':'#fff','border':'1px','border-style':'outset'});
			this.show();
			this.moveTo(0);
		},
		highlight:function(init,end){
    		$(this.renderTo).find('table > tbody > tr').eq(init).css('background-color','#fff');
    		$(this.renderTo).find('table > tbody > tr').eq(end).css('background-color','#bad0f7');
    	}*/
	}).focus();
	$('html').mudata({name:'data-t',input:'[+]atrib1'})
	//$('html').mudata({name:'t-t-0',input:'[+]'});
	//$('html').mudata('$test',"test").datatest({'+t1':true}).datatest({':t1':true});
});

(function ( $ ) {
	defaults:{
		a:1
	};
	$.fn.factoryX=function(options){
		var settings = $.extend(true,defaults,options);
		$.fn[options.product]=function(newOptions){
			//Optinal var newSettings = $.extend(true,settigs,newOptions);
			var S=settigs;
		};
		return this;
		
	}
}(jQuery))
</script>
</head>
<body>
<!-- Start of first page -->
<div data-role="page" id="admin" data-theme="a">

	<div data-role="header">
		<h1>Admin<br><span class='login'></span><img src="mb/images/icons-png/lock-white.png"> </h1>
	</div><!-- /header -->

	<div role="main" class="ui-content">
		
		<p>Escoge una tarea admistrativa a continuación:</p>
		<p><a href="#registerProduct" data-role="button">Dar de alta nuevo producto</a></p>
		<p><a href="#buyProduct" data-role="button">Registrar entrada de producto</a></p>
		<p><a href="#upInventory" data-role="button">Subir producto por inventario</a></p>
		
	</div><!-- /content -->

	<div data-role="footer">
		<h4>Ferremundo Inc.</h4>
	</div><!-- /footer -->
</div><!-- /page -->

<!-- Start of second page -->
<div data-role="page" id="registerProduct">

	<div data-role="header">
		<h1>Dar de alta nuevo producto<br><span class='login'></span><img src="mb/images/icons-png/lock-white.png"> </h1>
	</div><!-- /header -->

	<div role="main" class="ui-content">
		<div class="ui-content">
		<p>Cantidad:</p>
		<input id='quantity'>
		<p>Codigo del producto:</p>
		<input id='productCode'>
		<p>Marca:</p>
		<input id='mark'>
		<p>Descripción:</p>
		<input id='description'>
		<p>Unidad:</p>
		<input id='unit'>
		<p>Precio de compra:</p>
		<input id='unitPrice'>
		<p>Porcentage de ganancia:</p>
		<input id='gain'>
		<p>Proveedor:</p>
		<input id='provider'>
		<p>Referencia de la nota o factura:</p>
		<input id='reference'>
		<input id='registerProductSubmit' type="button" e-type="" value="Registrar"/>
	</div>

		
		<a href="#admin" data-role="button">OP</a>
	</div><!-- /content -->

	<div data-role="footer">
		<h4>Ferremundo Inc.</h4>
	</div><!-- /footer -->
</div><!-- /page -->

<!-- Start of third page -->
<div data-role="page" id="buyProduct">

	<div data-role="header">
		<h1>Registrar entrada de producto<br><span class='login'></span><img src="mb/images/icons-png/lock-white.png"> </h1>
	</div><!-- /header -->

	<div role="main" class="ui-content">
		<a href="#admin" data-role="button">OP</a>
	</div><!-- /content -->

	<div data-role="footer">
		<h4>Ferremundo Inc.</h4>
	</div><!-- /footer -->
</div><!-- /page -->

<!-- Start of fourth page -->
<div data-role="page" id="upInventory">

	<div data-role="header">
		<h1>Subir producto por inventario<br><span class='login'></span><img src="mb/images/icons-png/lock-white.png"> </h1>
	</div><!-- /header -->

	<div role="main" class="ui-content">
		<a href="#admin" data-role="button">OP</a>
	</div><!-- /content -->

	<div data-role="footer">
		<h4>Ferremundo Inc.</h4>
	</div><!-- /footer -->
</div><!-- /page -->

</body>
</html>