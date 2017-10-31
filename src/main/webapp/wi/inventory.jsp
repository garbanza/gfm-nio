<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!-- PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"-->
<html>
<head>
<title>FERREMUNDO ADMIN</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<script type="text/javascript">
var SEARCH_REQUEST_ID=0;

var TOKEN="${token}";
var CLIENT_REFERENCE="${clientReference}";
var SHOPMAN=${shopman};
var CONTEXT_PATH="${pageContext.request.contextPath}";
var AUTHORIZED=true;
var THEME='c';
</script>
<link rel="shortcut icon"
	href="${pageContext.request.contextPath}/favicon.ico" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/mb/jquery.mobile.custom.structure.min.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/mb/jquery.mobile.custom.theme.min.css" />

<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery-2.1.1.min.js"></script>

<script type="text/javascript"
	src="${pageContext.request.contextPath}/mb/jquery.mobile.custom.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/fmd/util.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.capsule.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/fmd/jquery.commandro.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/fmd/commandline-1.0.0.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/fmd/jquery.mutant.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/fmd/jquery.validateForm.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/fmd/jquery.mudata-0.9.0.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/fmd/auth.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/URLEncode.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.idle.min.js"></script>
<!-- script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.caret.1.02.min.js"></script-->

<!-- jsqrcode scripts -->
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jsqrcode/grid.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jsqrcode/version.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jsqrcode/detector.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jsqrcode/formatinf.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jsqrcode/errorlevel.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jsqrcode/bitmat.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jsqrcode/datablock.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jsqrcode/bmparser.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jsqrcode/datamask.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jsqrcode/rsdecoder.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jsqrcode/gf256poly.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jsqrcode/gf256.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jsqrcode/decoder.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jsqrcode/qrcode.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jsqrcode/findpat.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jsqrcode/alignpat.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/html5-qrcode/html5-qrcode.min.js"></script>
<!-- end of jsqrcode scripts -->
<script type="text/javascript" src="${pageContext.request.contextPath}/js/instascan-1.0.0.min.js"></script>

<script type="text/javascript">

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
		   ret+="<tbody>";
		   for(var i=0;i<data.body.length;i++){
			   ret+='<tr id="'+i+'">';
			   for(var j in data.body[i]){
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
},
        {
        	   name:"aTable",
        	   doc:"form a table from data. data[0] is the head row",
        	   defaults:{
        		   id:function(){
				   return "aTableID"+$.capsule.randomString(1,15,'aA0');
           		   }
        	   },
        	   html:function(data){
        		   var ret="<table><tr>";
        		   for(var i in data[0]){
        			   ret+='<th>'+data[0][i]+'</th>';
        		   }
        		   ret+='</tr>';
        		   for(var l=1;l<data.length;l++){
        			   ret+='<tr id="'+(l-1)+'">';
        			   for(var i in data[l]){
            			   ret+='<td>'+data[l][i]+'</td>';
            		   }
        			   ret+='</tr>';
        		   }
        		   ret+='</table>';
        		   return ret;
        	   }
           },
           {
        	   name:"aUl",
        	   doc:"form a unsorted list from data.",
        	   defaults:{
        		   id:function(){
				   return "aTableID"+$.capsule.randomString(1,15,'aA0');
           		   }
        	   },
        	   html:function(data){
        		   var ret="<ul id='$(id)'>";
        		   for(var i=o;i<data.length;i++){
        			   ret+='<li id="'+i+'">';
        			   for(var i in data[l]){
            			   ret+='<>'+data[l][i]+'</td>';
            		   }
        			   ret+='</tr>';
        		   }
        		   ret+='</table>';
        		   return ret;
        	   }
           }
]);

function registering(){
	
}
$(document).ready(function(){
	//$('#shopmanSession').html(':'+SHOPMAN.name+":"+SHOPMAN.login);
	//$('[data-role="page"]').css("overflow","hidden");
	$('.login').html(" - "+SHOPMAN.login+" - ");
	//IDLE LOCK
	$( document ).idle({
		idle:1000*60*5,
		onIdle: function(){
			var doIdle=document.isIdle?false:true;
			if(doIdle)document.isIdle=true;
			else return;
			lockin();
			$('[data-role="header"]').each(function(){
				$(this).find('img').attr('src',CONTEXT_PATH+'/mb/images/icons-png/lock-black.png');
			});
			var passinid='passid'+$.capsule.uniqueId();
			passin({
				id:passinid,
				success:function(data){
					AUTHORIZED=true;
					//alert("success " +data.authenticated+". #"+passinid+" to be removed")
					$('#'+passinid).remove();
					document.isIdle=false;
					$('[data-role="header"]').each(function(){
						$(this).find('img').attr('src',CONTEXT_PATH+'/mb/images/icons-png/lock-white.png');
					});
					//$('#commands').focus();
				},
				message:"desbloquear "+SHOPMAN.name+"-"+SHOPMAN.login
			});
		}
	});
	//LOCK
	$('[data-role="header"]').click(function(){
		var doIdle=document.isIdle?false:true;
		if(doIdle)document.isIdle=true;
		else return;
		lockin();
		$(this).find('img').attr('src',CONTEXT_PATH+'/mb/images/icons-png/lock-black.png');
		var this_=this;
		var passinid='passid'+$.capsule.uniqueId();
		passin({
			id:passinid,
			success:function(data){
				AUTHORIZED=true;
				//alert("success " +data.authenticated+". #"+passinid+" to be removed")
				$('#'+passinid).remove();
				document.isIdle=false;
				$(this_).find('img').attr('src',CONTEXT_PATH+'/mb/images/icons-png/lock-white.png');
				//$('#commands').focus();
			},
			message:"desbloquear "+SHOPMAN.name+"-"+SHOPMAN.login
		});
	}).css('cursor','pointer');

	// commandroSearchTemplate expects {clicable, enter, appendTo, command}
	// and works with data of the form {result:[{},{}...], ...}
	commandroSearchTemplate=function(options){
		return {
			clickable:options.clickable?true:false,
			renderTo:$('<div></div>').appendTo(options.appendTo),//'#registerProduct'),
			render:function(data){
	    		var inp=$(this.input);
				var pos=inp.position();
				var height=inp[0].offsetHeight;
				var width=inp[0].offsetWidth;
				this.renderTo.empty()
				.css({'background-color':'#fff'/*,top:pos.top+height,left:pos.left,
					position:'absolute'*/,width:'auto',margin: 0,
					'min-width':width
				})
				.inTABLE(data).css({'min-width':width});
	    	},
	    	hide:function(){this.renderTo.toggle(false);},
	    	show:function(){this.renderTo.toggle(true);},
	    	htmlMenuElements:function(){return $(this.renderTo).find('tbody>tr');},
	    	highlight:function(init,end){
	    		var els=this.htmlMenuElements();
	    		els.eq(init).css({'background-color':'#fff'});
	    		els.eq(end).css({'background-color':'#efefef'});
	    	},
			fire:{
				enter:function(this_){options.enter(this_);}/*
					var rg=$('#registerProduct');
					var i=this_.selectedIndex;
					var me=this_.menu;
					rg.find('#description').val(me[i]['description']);
					rg.find('#code').val(me[i]['code']);
					rg.find('#mark').val(me[i]['mark']);
					rg.find('#unit').val(me[i]['unit']);
					this_.menu=[me[i]];
					this_.render(me[i]);
					this_.hide();
					//console.log(me[i]);
					this_.selectedIndex=0;
				}*/
			},
			source:function(path){
				var commandl=commandLine($(this.input));
				console.log("commandl");
				console.log(commandl);
				var args=path.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
				var this_=this;
				$.ajax({
					  type: "POST",
					  url: CONTEXT_PATH+"/dbport",
					  data: {
						  command: options.command,//"SEARCH_PRODUCTS",
						  search: args,
						  searchRequestId:++SEARCH_REQUEST_ID,
						  token:TOKEN,
						  clientReference:CLIENT_REFERENCE
						  }
				})
				.done(function( data ) {
					this_.handleData(data);
				 });
			},
			handleData:function(data){
				this.data=data;
				//console.log(data);
	    		this.menu=data.result;
	    		this.render({body:data.result});
	    		this.handleHtmlMenuElements();
	    		this.moveTo(0);
	    		this.show();
	    	},
		}
	};
	
	$('#registerProduct').find('#code').commandro(commandroSearchTemplate({
		//input:$('#registerProduct').find('#code'),
		clickable:true,
		appendTo:'#registerProductFormResultset',
		enter:function(this_){
			var rg=$('#registerProduct');
				//$(this_.appendTo)
			var i=this_.selectedIndex;
			var me=this_.menu;
			if(me.length<1)return;
			rg.find('#description').val(me[i]['description']);
			rg.find('#code').val(me[i]['code']);
			rg.find('#mark').val(me[i]['mark']);
			rg.find('#unit').val(me[i]['unit']);
			this_.menu=[me[i]];
			this_.data={'result':[me[i]]};
			this_.render({body:[me[i]]});
			this_.hide();
			this_.selectedIndex=0;
			
			this_.toUpdate=me[i];
			rg.find('#update').removeAttr('checked');
			rg.find('#toUpdate').css({'visibility':'visible', 'opacity':'.5'});
			rg.find('#updateLabel').empty()
			.append("<p>Actualizar la definicion existente de:</p>")
			.inTABLE({body:[me[i]]})
			.css({'background-color':'#fff','border':'1px','border-style':'outset'});
		},
		command: "SEARCH_PRODUCTS"
		
	}));
	$('#registerProduct').find('#toUpdate').click(function(){
		if($(this[0]).is(':checked')){
			$(this[0]).css({'opacity':'1'});
		}
		else $(this[0]).css({'opacity':'.5'});
	})
	.end()
	.find("[app-id~='form-input']")
	.each(function(){
    			
    			//var id=$(this).attr(settings.idAttribute);
    			//var this_=this;
    			//console.log(id);
    			$(this).keyup(function(){
    				
    			});
	});
	//$('#registerProduct').find('input').keyup(function(){this.each(function(){this.trigger('change');})});
	$('#registerProduct').find('#provider').commandro(commandroSearchTemplate({
		clickable:true,
		appendTo:'#registerProductFormResultset',
		enter:function(this_){
			var rg=$(this_.appendTo);
			var i=this_.selectedIndex;
			var me=this_.menu;
			rg.find('#provider').val(me[i]['fullName']);
			this_.menu=[me[i]];
			this_.data={'result':[me[i]]};
			this_.render({body:[me[i]]});
			this_.hide();
			console.log('ENTER');
			this_.selectedIndex=0;
		},
		command: "SEARCH_PROVIDERS"
		
	}));
	/*
	$('#registerProduct').find('#code').commandro({
		clickable:true,
		renderTo:$('<div></div>').appendTo('#registerProduct'),
		render:function(data){
    		var inp=$(this.input);
			var pos=inp.position();
			var height=inp[0].offsetHeight;
			var width=inp[0].offsetWidth;
			this.renderTo.empty()
			.css({'background-color':'#fff',top:pos.top+height,left:pos.left,
				position:'absolute',width:'auto',margin: 0,
				'min-width':width
			})
			.inTABLE(data).css({'min-width':width});
    	},
    	htmlMenuElements:function(){return $(this.renderTo).find('tbody>tr');},
    	highlight:function(init,end){
    		var els=this.htmlMenuElements();
    		els.eq(init).css({'background-color':'#fff'});
    		els.eq(end).css({'background-color':'#efefef'});
    	},
		fire:{
			enter:function(this_){
				var rg=$('#registerProduct');
				var i=this_.selectedIndex;
				var me=this_.menu;
				rg.find('#description').val(me[i]['description']);
				rg.find('#code').val(me[i]['code']);
				rg.find('#mark').val(me[i]['mark']);
				rg.find('#unit').val(me[i]['unit']);
				this_.menu=[me[i]];
				this_.render(me[i]);
				this_.hide();
				//console.log(me[i]);
				this_.selectedIndex=0;
			}
		},
		source:function(path){
			var args=path.replace(/^\s\sASTERISCO/, '').replace(/\s\s*$/, '');
			var this_=this;
			$.ajax({
				  type: "POST",
				  url: CONTEXT_PATH+"/dbport",
				  data: {
					  command: "SEARCH_PRODUCTS",
					  search: args,
					  searchRequestId:++SEARCH_REQUEST_ID,
					  token:TOKEN,
					  clientReference:CLIENT_REFERENCE
					  }
			})
			.done(function( data ) {
				this_.handleData(data);
			 });
		},
		handleData:function(data){
			this.data=data;
			//console.log(data);
    		this.menu=data.products;
    		this.render({body:data.products});
    		this.handleHtmlMenuElements();
    		this.moveTo(0);
    		this.show();
    	},
	});
	*/
	
	$('#registerProduct').find('#registerProductSubmit').click(function(){
		var rg=$('#registerProduct');
		var code=rg.find('#code').val();
		rg.find('#code').val();
		rg.find('#mark').val();
		rg.find('#unit').val();
	});
	//$('#registerProductForm').mutant({});
	$('#registerProductForm').validateForm({
		submit:'#registerProductSubmit',
		todo:'#todo',
		submitMessage:'[ OK ] Continúa con el Registro',
		hyphened:function(val){
			var match=val.match(/\b[a-z0-9]+[-][a-z0-9]+\b/i);
			return match?match[0]==val:false;
		},
		input:{
			'quantity':{test:'numeric',msg:'Cantidad invalida'},
			'code':{test:'hyphened',msg:'El codigo debe ser de la forma "[codigo de la marca]-[codigo del producto]"'},
			'mark':{test:'notEmpty',msg:'Marca invalida'},
			'description':{test:'notEmpty',msg:'Descripción invalida'},
			'unit':{test:'notEmpty',msg:'Unidad invalida'},
			'providerPrice':{test:'numeric',msg:'Precio invalido'},
			'percentageGain':{test:'numeric',msg:'Porcentage invalido'},
			'provider':{test:'notEmpty',msg:'Proveedor invalido'},
			'reference':{test:'notEmpty',msg:'Referencia invalida'}
		}
	});
	$("#submit").css("visibility","hidden");
	
	//if you have another AudioContext class use that one, as some browsers have a limit
	var audioCtx = new (window.AudioContext || window.webkitAudioContext || window.audioContext);

	//All arguments are optional:

	//duration of the tone in milliseconds. Default is 500
	//frequency of the tone in hertz. default is 440
	//volume of the tone. Default is 1, off is 0.
	//type of tone. Possible values are sine, square, sawtooth, triangle, and custom. Default is sine.
	//callback to use on end of tone
	function beep(duration, frequency, volume, type, callback) {
	    var oscillator = audioCtx.createOscillator();
	    var gainNode = audioCtx.createGain();

	    oscillator.connect(gainNode);
	    gainNode.connect(audioCtx.destination);

	    if (volume){gainNode.gain.value = volume;};
	    if (frequency){oscillator.frequency.value = frequency;}
	    if (type){oscillator.type = type;}
	    if (callback){oscillator.onended = callback;}

	    oscillator.start();
	    setTimeout(function(){oscillator.stop()}, (duration ? duration : 500));
	};
	
	$("#upInventory").click(function(){
		let scanner = new Instascan.Scanner(
				{
					video: document.getElementById('qrcodereader'),
					mirror:false,
					refractoryPeriod: 200});
				
	      scanner.addListener('scan', function (content, image) {
	    	  scanner.stop();
	    	  beep(100,440,1,"sine");
	        	console.log(content);
	        	
	        	
	      });

	      Instascan.Camera.getCameras().then(function (cameras) {
	        if (cameras.length > 0) {
	          scanner.start(cameras[0]);
	          console.log("starting camera 0");
	        }
	      });
	      
		/*$("#qrcodereader").html5_qrcode(
				function (data){console.log(data)},
				function (data){console.log("errer")},
				function (data){console.log("video errer")});*/
		
	});
	
});



</script>
</head>
<body>
	<!-- Start of first page -->
	<div data-role="page" id="inventory" data-theme="a">

		<div data-role="header">
			<h1>
				INVENTORY<br>
				<span class='login'></span><img
					src="${pageContext.request.contextPath}/mb/images/icons-png/lock-white.png">
			</h1>
		</div>
		<!-- /header -->

		<div role="main" class="ui-content">
			<p>
				<button id="upInventory" data-role="button">Registrar factura por
					inventario</button>
			</p>
			<p>
				<button id="submit" data-role="button">Submit</button>
			</p>
			<!--div id="qrcodereader" style="width:600px;height:500px">
				<h4>Ferremundo Inc.</h4>
			</div-->
			<video id="qrcodereader">
				<h4>Ferremundo Inc.</h4>
			</video>
		</div>
		<!-- /content -->

		<div data-role="footer">
			<h4>Ferremundo Inc.</h4>
		</div>
		<!-- /footer -->
	</div>
	<!-- /page -->

</body>
</html>