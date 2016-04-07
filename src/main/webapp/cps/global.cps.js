//capsule examples by raynmune at gmail dot com is licensed under a Creative Commons Attribution-ShareAlike 3.0 Unported License.

(function($){$.capsule([

	{
		type:'CapsuleCore',
		info:{
			author:'raynmune at gmail dot com',
			license:'Creative Commons',
			version:'1.0'
		},
		
		name:'com.ferremundo.cps.SexyRowT',
		
		defaults:{
			quantity:NaN,
			unit:'n/s',
			description:'n/s',
			code:'n/s',
			mark:'n/s',
			unitPrice:NaN,
			total:NaN,
			quantityWidth:'8%',
			unitWidth:'8%',
			descriptionWidth:'10%',//'35%',
			codeWidth:'10%',
			markWidth:'10%',
			unitPriceWidth:'8%',
			totalWidth:'8%',
			classes:'wbreak ',//fleft pad1 wbreak',
			gclasses:'fleft ',
			fclasses:'ralign pad1',//'fleft ralign pad1 wbreak',
			id:function(){
				return "SexyRowTID"+$.capsule.randomString(1,1,15,15,'aA0');
			}
		},
		html:function(){
			var gd=this.get('com.ferremundo.cps.GenericTC');
			
			var ret= "<table id='$(id)' class='$(gclasses)' style='width:100%;'><tr>"+
			gd.gen({dclass:'$(fclasses)', width:'$(quantityWidth)',content:'$(quantity)'})+
			gd.gen({dclass:'$(classes)', width:'$(unitWidth)',content:'$(unit)'})+
			gd.gen({dclass:'$(classes)', width:'$(descriptionWidth)',content:'$(description)'})+
			gd.gen({dclass:'$(classes)', width:'$(codeWidth)',content:'$(code)'})+
			gd.gen({dclass:'$(classes)', width:'$(markWidth)',content:'$(mark)'})+
			gd.gen({dclass:'$(fclasses)', width:'$(unitPriceWidth)',content:'$(unitPrice)'})+
			gd.gen({dclass:'$(fclasses)', width:'$(totalWidth)',content:'$(total)'})+

			"</tr></table>";
			//console.log(ret);
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
		},
		css:function(){
			return {
				'.pad1' : {'padding' : '0px 2px 0px 2px'},
				'.box' : {
					'border-radius' : '10px',
					'box-shadow' : '1px 1px 1px #555',
					'width' : '100%'
				},
				'.fleft' : {'float': 'left'},
				'.ralign' : {'text-align' : 'right'},
				'.wbreak' : {'word-break':'break-all'}
			};
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
			author:'raynmune at gmail dot com',
			license:'Creative Commons',
			version:'1.0'
		},
		
		name:'com.ferremundo.cps.SexyRow',
		
		defaults:{
			quantity:NaN,
			unit:'n/s',
			description:'n/s',
			code:'n/s',
			mark:'n/s',
			unitPrice:NaN,
			total:NaN,
			quantityWidth:'8%',
			unitWidth:'8%',
			descriptionWidth:'35%',
			codeWidth:'10%',
			markWidth:'10%',
			unitPriceWidth:'8%',
			totalWidth:'8%',
			classes:'fleft pad1 wbreak',
			gclasses:'fleft wbreak',
			fclasses:'fleft ralign pad1 wbreak',
			id:function(){
				return "SexyRowID"+$.capsule.randomString(1,1,15,15,'aA0');
			}
		},
		html:function(){
			var gd=this.get('com.ferremundo.cps.GenericDiv');
			var ret= "<div id='$(id)' class='$(gclasses)' style='width:100%;'>"+
			gd.gen({dclass:'$(fclasses)', width:'$(quantityWidth)',content:'$(quantity)'})+
			gd.gen({dclass:'$(classes)', width:'$(unitWidth)',content:'$(unit)'})+
			gd.gen({dclass:'$(classes)', width:'$(descriptionWidth)',content:'$(description)'})+
			gd.gen({dclass:'$(classes)', width:'$(codeWidth)',content:'$(code)'})+
			gd.gen({dclass:'$(classes)', width:'$(markWidth)',content:'$(mark)'})+
			gd.gen({dclass:'$(fclasses)', width:'$(unitPriceWidth)',content:'$(unitPrice)'})+
			gd.gen({dclass:'$(fclasses)', width:'$(totalWidth)',content:'$(total)'})+

			"<div style='width:100%; height:0px' class='$(gclasses)'></div></div>";
			//console.log(ret);
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
		},
		css:function(){
			return {
				'.pad1' : {'padding' : '0% 1% 0% 1%'},
				'.box' : {
					'border-radius' : '10px',
					'box-shadow' : '0% 0% 1% 1% #555',
					'width' : '100%'
				},
				'.fleft' : {'float': 'left'},
				'.ralign' : {'text-align' : 'right'},
				//'.wbreak' : {'word-break':'break-all'}
			};
		},
		after:function(dobs){
			resize=function(){
				var height=$(dobs).height();
				$(dobs).children().each(function(){
					$(this).css('height',height);
					console.log('resizing to:'+height);
				}).last().css('height',0);
			};
			resize();
			$(dobs).resize(resize);
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
			dclass:'',
			width:'',
			content:'',
			id:function(){
				return "GenericDivID"+$.capsule.randomString(1,1,15,15,'aA0');
			}
		},
		html:function(){
			return "<div id=$(id) class='$(dclass)' style='width:$(width)'>$(content)</div>";
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

		name:'com.ferremundo.cps.GenericTC',

		defaults:{
			dclass:'',
			width:'',
			content:'',
			id:function(){
				return "com.ferremundo.cps.GenericTC"+$.capsule.randomString(1,1,15,15,'aA0');
			}
		},
		html:function(){
			return "<td id='$(id)' style='width:$(width)'><p class='$(dclass)'>$(content)</p></td>";
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
