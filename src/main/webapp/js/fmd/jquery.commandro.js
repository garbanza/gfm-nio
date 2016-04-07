
(function ( $ ) {
    $.fn.commandro=function(options){
    	if(options==null){
    		var _reg=$.fn.commandro._reg;
    		for(var i=0;i<_reg.length;i++){
    			if(_reg[i].id===this[0])return _reg[i].settings;
    		}
    		return null;
    	}
    	return this.each(function(){
    		var settings = $.extend(true,{
    	       	data:[],
            	render:function(data){
            		var inp=$(this.input);
        			var pos=inp.position();
        			var height=inp[0].offsetHeight;
        			var width=inp[0].offsetWidth;
        			this.renderTo.empty();
    				ret="";
            		for(var i=0;i<data.length;i++){
            			ret+="<li id='menu"+i+"'>";
               			ret+=data[i];
            			ret+="</li>";
            		}
            		ret==""?$(this.renderTo).empty():$(this.renderTo).empty().append(ret)
            		.css({'background-color':'#fff','border':'1px','border-style':'outset',
            			//**TODO fix this height thing 
            			top:pos.top+height,left:pos.left,
        				position:'absolute',width:'auto',padding: 0, margin: 0,
        				'min-width':width});
            	},
            	handleHtmlMenuElements:function(){
            		hme=this.htmlMenuElements();
            		var this_=this;
            		var i=0;
            		hme.each(function(){
            			var j=i;
            			$(this).hover(function(){
            				this_.moveTo(j);
            				$(this)
            				.css({'cursor':'pointer'});
            				
            			})
            			.click(function(){
            				this_.fire['clickmenu'](this_);
            			});
            			//.addClass('ui-bar ui-bar-a ui-corner-all');
            			i++;
            		});
            	},
            	command:function(event){
            		var val=$(this.input).val();
            		if(event.which==13){
            			this.fire['enter'](this);
            			return;
            		}
            		else if(event.which==27||event.keyCode==27){
            			this.fire['escape'](this,val);
            			return;
            		}
            		else if(event.which==1){
            			this.fire['click'](this);
            			return;
            		}
            		if(val==this.inputValue)
            			return;
            		this.inputValue=val;
            		if(val==""){
            			this.renderTo.empty();
            			this.hidde();
            			return;
            		}
            		this.source(val);
            	},
            	source:function(path){
            		var ret=new Array();
            		var data=this.data;
            		for(var i=0;i<data.length;i++){
            			if(data[i].match(RegExp(path))){
       						ret.push(data[i]);
               			}
            		}
            		this.handleData(ret);
            	},
            	input:null,
            	fire:{
            		enter:function(this_){
            			var data=this_.menu[this_.selectedIndex];
            			this_.handleData([data]);
            			this_.inputValue=data;
            			this_.hidde();
            			$(this_.input).val(data);
            		},
            		escape:function(this_){
            			this_.hidde();
            			this_.moveTo(-1);
            		},
            		clickmenu:function(this_){
            			this_.fire['enter'](this_);
            			$(this_.input).focus();
            		},
            		click:function(this_){
            			this_.handleData(this_.data);
            		}
            	},
            	menu:null,
            	handleData:function(ret){
            		this.menu=ret;
            		this.render(ret);
            		this.handleHtmlMenuElements();
            		this.moveTo(0);
            		this.show();
            	},
            	htmlMenuElements:function(){return $(this.renderTo).children();},
            	highlight:function(init,end){
					var els=this.htmlMenuElements();
            		els.eq(init).css(//'background-color','#fff');
            				{'border':'0px','border-style':'outset'});
            		els.eq(end).css(//'background-color','#bad0f7');
            				{'border':'2px','border-style':'outset'});
            	},
            	hidde:function(){this.renderTo.css('visibility','hidden');},
            	show:function(){this.renderTo.css('visibility','visible');},
            	selectedIndex:0,
            	moveTo:function(e){
            		console.log(e);
            		var idx1=this.selectedIndex;
            		if(this._typeOf(e)=='number'){
            			this.selectedIndex=e;
            		}
            		else{
            			if(e.which==40){
                    		this.selectedIndex=
        						(this.selectedIndex+1)>=this.menu.length?
        								0:this.selectedIndex+1;
        				}
        				else if(e.which==38){
        					this.selectedIndex=
        						(this.selectedIndex-1)<0?
        								this.menu.length-1:
        									this.selectedIndex-1;
        				}
            		}
            		//console.log('gong from '+idx1+" to "+this.selectedIndex);
            		this.highlight(idx1,this.selectedIndex);
            	},
            	_typeOf:function(value) {
            	    var s = typeof value;
            	    if (s === 'object') {
            	        if (value) {
            	            if (value instanceof Array) {
            	                s = 'array';
            	            }
            	        } else {
            	            s = 'null';
            	        }
            	    }
            	    return s;
            	},
            	renderTo:$('<ul></ul>')
       				.appendTo('body')
       				.css({'visibility':'hidden'}),
       			_isDown:false,
       			clickable:false

            }, options );

    		settings._repeatKeyDown=function(condition,execute,i1,i2){
        		condition?(
        				execute()&
        				(settings._timeout=window.setTimeout(function(){settings._timer=window.setInterval(execute, i1);},i2))
        		):
        			window.clearTimeout(settings._timeout)&window.clearInterval(settings._timer)&(execute?execute():null);
	       	};
	       	
	       	//REGISTER settings for future $(element).commandro() calls
	       	$.fn.commandro._reg.push({id:this, settings:settings});
	       	settings.input=this;
	       	if(settings.clickable){
	       		$(this).click(function(event){
	       			settings.command(event);
	       		})
	       		.attr('tabindex','0')
	       		.css({'cursor':'pointer'});
	       	}
    		$(this).keydown(function(event){
        		if(event.which==40||event.which==38){
        			if(settings._isDown)return;
        			settings._isDown=true;
        			event.preventDefault();
        			//console.log('event.down');
        			settings.show();
        			settings._repeatKeyDown(true,function(){
        				settings.moveTo(event);
        				//console.log('down');
        			},50,300);
        			return;
        		};
        	})
        	.keyup(function(event){
        		if(event.which==40||event.which==38){
        			settings._isDown=false;
        			event.preventDefault();
        			settings._repeatKeyDown(false);
        			return;
        		}
        		
				settings.command(event);
        		
        	})
        	.blur(function(event){
        		settings._repeatKeyDown(false);
        		//TODO fix this. timeout added to let on-menu-click to actually happen
        		window.setTimeout(function(){settings.hidde();},100);
        	});
    	});
    };
    $.fn.commandro._reg=[];
}( jQuery ));