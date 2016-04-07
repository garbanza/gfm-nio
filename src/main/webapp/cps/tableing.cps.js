//capsule examples by raynmune at gmail dot com is licensed under a Creative Commons Attribution-ShareAlike 3.0 Unported License.

(function($){$.capsule([

	{
		coreVersion:'1.0',
		
		info:{
			author				:'raynmune at gmail dot com',
			license				:'Creative Commons'			,
			version				:'1.0'
		},
		
		name:'com.ferremundo.cps.TB',
		
		defaults:{
			quantity			:NaN		,
			unit				:'n/s'		,
			description			:'n/s'		,
			code				:'n/s'		,
			mark				:'n/s'		,
			unitPrice			:NaN		,
			total				:NaN		,
			quantityWidth		:'8%'		,
			unitWidth			:'8%'		,
			descriptionWidth	:'35%'		,
			codeWidth			:'10%'		,
			markWidth			:'10%'		,
			unitPriceWidth		:'8%'		,
			totalWidth			:'8%'		,
			classes				:'wbreak'	,
			gclasses			:'fleft'	,
			fclasses			:'ralign pad1',
			id					:function(){
									return "com.ferremundo.cps.TB"+$.capsule.randomString(1,1,15,15,'aA0');
								}
		},
		
		css:function(){
			return {
				'.pad1'		:{	'padding'		: '0px 2px 0px 2px'	},
				'.box' 		:{
								'border-radius' :'10px',
								'box-shadow' 	:'1px 1px 1px #555'	,
								'width' 		:'100%'
							},
				'.fleft' 	:{	'float'			:'left'				},
				'.ralign' 	:{	'text-align'	:'right'			},
				'.wbreak' 	:{	'word-break'	:'break-all'		}
			};
		},
		
		html:function(){
			var tc=this.get('com.ferremundo.cps.GenericTC');
			console.log("com.ferremundo.cps.GenericTC");
			console.log(tc);
			
			TC=function(d,w,c){return{dclass:d,width:w,content:c};};
			var ret=
				"<table id='$(id)' class='$(gclasses)' style='width:100%;'><[><tr>"+
				tc.gen([
				         TC('$(fclasses)','$(quantityWidth)','$(quantity)'),
				         TC('$(classes)','$(unitWidth)','$(unit)'),
				         TC('$(classes)','$(descriptionWidth)','$(description)'),
				         TC('$(classes)','$(codeWidth)','$(code)'),
				         TC('$(classes)','$(markWidth)','$(mark)'),
				         TC('$(classes)','$(unitPriceWidth)','$(unitPrice)'),
				         TC('$(classes)','$(totalWidth)','$(total)')
				])+
				"</tr></[></table>";
			console.log(ret);
			return ret;
		},
		
		construct:function(quantity,code,unit,mark,description,unitPrice,total){
			var r={};
			r.quantity=quantity;
			r.unit=unit;
			r.description=description;
			r.code=code;
			r.mark=mark;
			r.unitPrice=unitPrice;
			r.total=total;
			return r;
		}
		
		/*after:function(dobs){
			//var height=$(dobs).height();
			var i=0;
			$(dobs).find('tr').each(function(){
				i%2==0?$(this).addClass('odd'):$(this).addClass('even');//({'background-color':'#f0f', 'valign':'top'});
				//console.log('resizing to:'+height);
			});

			$(dobs).resize(resize);
		}*/
	}
	
	,
	
	{
		type:'CapsuleCore',
		info:{
			author				:'raynmune at gmail dot com',
			license				:'Creative Commons',
			version				:'1.0'
		},

		name:'com.ferremundo.cps.GenericTC',

		defaults:{
			dclass				:'',
			width				:'',
			content				:'',
			id					:function(){
									return "com.ferremundo.cps.GenericTC"+$.capsule.randomString(1,1,15,15,'aA0');
								}
		},
		html:function(){
			return 	"<array>" +
						"<td id='$(id)' style='width:$(width)'>" +
							"<p class='$(dclass)'>$(content)</p>" +
						"</td>" +
					"</array>";
		},
		construct:function(dclass,width,content){
			var v;
			v.dclass=dclass;
			v.width=width;
			v.content=content;
			return v;
		}
	}
	
	,
	
	{
		type:'CapsuleCore',
		info:{
			author:'raynmune at gmail dot com',
			license:'Creative Commons',
			version:'1.0'
		},

		name:'com.ferremundo.cps.GenericDiv',

		defaults:{
			dclass				:'',
			width				:'',
			content				:'',
			id					:function(){
									return "GenericDivID"+$.capsule.randomString(1,1,15,15,'aA0');
								}
		},
		html:function(){
			return	"<div id=$(id) class='$(dclass)' style='width:$(width)'>" +
						"$(content)" +
					"</div>";
		},
		construct:function(dclass,width,content){
			var v;
			v.dclass=dclass;
			v.width=width;
			v.content=content;
			return v;
		}
	}
	
]);})(jQuery);