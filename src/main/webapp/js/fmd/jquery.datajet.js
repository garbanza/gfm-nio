(function ( $ ) {
	
	var typeOf=function(value) {
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
	};
	var thr=function(m){throw new Error(m);};
	
	if (typeOf(String.prototype.startsWith) != 'function') {
		String.prototype.startsWith = function (str){
			return this.indexOf(str) == 0;
		};
	};
	//TODO make this more prototyped
	var oSize=function(o){
		var i=0;
		for(var key in o){i++;}
		return i;
	};
	
	var dic='❤♎☀★☂♞☯☭☢€☎⚑❄♫✂';
		//'ºª\\!|"@·#$~%¦&¬/½(⅛)^=`\'?¯¿˘[`+]*´{¨ç}ÇñÑ.:,;-_<>«»¶¥ŧđ¢ħ£æø€åœð®þŋß¤×µ';
    function randomString(size){
		var i=0;
		text='';
		var length=dic.length-1;
		while(i<size){
			var nth=Math.round(Math.random()*length);
			var ch=dic.charAt(nth);
			text+=ch;
			i++;
		}	    
	    return text;
	};
	
	var defaults={
			commander:{
					plus:function(jQ,attr,value,S){
						var dataName=S.dataName;
						return jQ.each(function(){
				    		var nspace=S.ns(this,S.dataset);
				    		
							if(!this.hasAttribute(dataName)){
								this.setAttribute(dataName,attr);
							}
							else{
								if(attr!=""){
									var oldAttrs=this.getAttribute(dataName);
									var splited=oldAttrs.split(" ");
									var hasnt=true;
									for(var i=0;i<splited.length;i++){
										if(splited[i]==attr){
											hasnt=false;
											break;
										}
									}
									if(hasnt){
										var newAttrs=(oldAttrs+' '+attr).replace(/^\s+|\s+$/g, '').replace(/[\s]+/g, ' ');
										this.setAttribute(dataName,newAttrs);
									}
								}
							}
							nspace[dataName]?undefined:(nspace[dataName]={});
							if(attr!=""){
								nspace[dataName][attr]={};
								//nspace[dataName][attr].exec=(typeOf(value)=='function')?true:false;
								nspace[dataName][attr].value=value;
								nspace[dataName][attr].htme=this;
							}
							else{
								nspace[dataName]['['+dataName+']']={}
								//nspace[dataName]['['+dataName+']'].exec=(typeOf(value)=='function')?true:false;
								nspace[dataName]['['+dataName+']'].value=value;
								nspace[dataName]['['+dataName+']'].htme=this;
							}
						});
					},
					
					less:function(jQ,attr,value,S){
						var dataName=S.dataName;
						return jQ.each(function(){
				    		var nspace=S.ns(this,S.dataset);
							if(!this.hasAttribute(dataName)){
								return;
							}
							else{
								if(attr!=""){
									nspace[dataName][attr]=undefined;
									var oldAttrs=this.getAttribute(dataName);
									var splited=oldAttrs.split(" ");
									for(var i=0;i<splited.length;i++){
										if(splited[i]==attr){
											splited.splice(i--,1);
										}
									}
									this.setAttribute(dataName,splited.join(" "));
								}
								else{
									this.removeAttribute(dataName);
									nspace[dataName]=undefined;
								}
							}
						});
					},
				
					get:function(jQ,attr,value,S){
						var dataName=S.dataName;
						var this_=jQ[0];
						var nspace=S.ns(this_,S.dataset);
						if(attr!=""){
							return nspace[dataName] ?
				    				(nspace[dataName][attr] ?
				    						(nspace[dataName][attr].value) :
				    						(undefined)) :
				    				(undefined);	
						}
						else{
							return nspace[dataName] ?
				    				(nspace[dataName]['['+dataName+']'] ?
				    						(nspace[dataName]['['+dataName+']'].value) :
				    						(undefined)) :
				    				(undefined);	
						}
					},

					exec:function(jQ,attr,value,S){
						if(attr==""){
							console.warn('".noMethod" Execution method not especified for "'+dataName+'". Nothing to do.');
							return jQ;
						}
						var dataName=S.dataName;
						return jQ.each(function(){
							var nspace=S.ns(this,S.dataset);
							if(! this.hasAttribute(dataName)){
								return;
							}
							else{
								if(typeOf(nspace[dataName][attr].value)=='function')
									nspace[dataName][attr].value(value);

							}
						});
					},
					
					equals:function(jQ,attr,value,S){
						var jQRet=$();
						var dataName=S.dataName;
						jQ.each(function(){
							var nspace=S.ns(this,S.dataset);
				    		if(this.hasAttribute(dataName)){
				    			if(attr==""){
				    				jQRet=jQRet.add(this);
								}
				    			else{
				    				nspace[dataName][attr] ?
				    						(jQRet=jQRet.add(this)) :
				    						(null);
					    		}
				    		}
				    		
						});
						return jQRet;
					},
					
					different:function(jQ,attr,value,S){
						var jQRet=$();
						var dataName=S.dataName;
						jQ.each(function(){
							var nspace=S.ns(this,S.dataset);
				    		if(this.hasAttribute(dataName)){
				    			if(attr==""){
				    				//jQRet=jQRet.add(this);
								}
				    			else{
				    				nspace[dataName][attr] ?
				    						(null) :
				    						(jQRet=jQRet.add(this));
					    		}
				    		}
				    		else{
				    			jQRet=jQRet.add(this);
				    		}
				    		
						});
						return jQRet;
					},

					find:function(jQ,attr,value,S){
						var dataName=S.dataName;
						if(attr==""){
							return jQ.find('['+dataName+']');
						}
						else return jQ.find('['+dataName+'~="'+attr+'"]');
					}
			},
			datarwx:'data'+randomString(3),
			ns:function(this_,datarwxNS){
				var datarwx=this.datarwx;
				this_[datarwx] ?
						(this_[datarwx][datarwxNS] ?
							(null) :
							(this_[datarwx][datarwxNS]={})) :
						(this_[datarwx]={},this_[datarwx][datarwxNS]={});
				return this_[datarwx][datarwxNS];
			},
			commandMap:{
					'[+]':'plus',
					'[-]':'less',
					'[:]':'get',
					'[*]':'find',
					'[=]':'equals',
					'[.]':'exec',
					'[!]':'different',
					
					'[plus]':'plus',
					'[less]':'less',
					'[get]':'get',
					'[find]':'find',
					'[equals]':'equals',
					'[exec]':'exec',
					'[diff]':'different'
			},
			error1:function(){
				throw new Error('mudata must receive string of the form "[$|-|+|:]attribute[.property]"');
			},
			nameFilter:/^[a-z]*\w+(?:-(?![0-9])\w+)+$/,///^[a-z]+[a-z0-9_\-]*$/,
			methodFilter:/\[[\W|\w]+?\]/g,
			getCameled:function(n){
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
			}
			
	};

    $.fn.datajet=function(options){//attr,value,dataRWXNS,dataName,options){
    	var settings = $.extend(true,defaults,options);
    	var S=settings;
    	var plugName,dataset,dataName;
    	S.name ?
    			(S.name.match(S.nameFilter) ?
    					(dataName=S.name) :
    					(thr('unvalid name provided "'+S.name+'". Must match "'+S.nameFilter+'"'))) :
    			(thr('no name provided'));
    	S.plugName=S.getCameled(S.name);
    	plugName=S.plugName;
    	S.dataName=S.name;
    	dataName=S.dataName;
    	S.dataset?undefined:(S.dataset=uniqueId(randomString(8)));
    	
    	$.fn[plugName]=function(attr,value){
    		var tasks=[];
        	var nextAttr=[];
        	if(typeOf(attr)=='array'){
        		if(attr.length==0)return this;
        		thisAttr=attr[0];
        		var nextAttr=attr.slice(1);
        		var key,val,commands,instructive,key_,tasks=[];
        		if(typeOf(thisAttr)=='object'){
        			if(oSize(thisAttr)!=1){
        				throw new Error('use the form "[...,a,...,{b:c}...]|{a,b}|a,b|a"');
        			}
        			key_=Object.keys(thisAttr)[0];
        			if(key_.match(S.methodFilter)==null){
        				throw new Error('argument must match '+methodFilter);
        			}
        			val=thisAttr[key_];
        		}
        		else if(typeOf(thisAttr)=='string'){
        			if(thisAttr.match(S.methodFilter)==null){
        				throw new Error('argument must match '+methodFilter);
        			}
        			key_=thisAttr;
        			val=undefined;
        		}
        		else{
        			throw new Error('use the form "[...,a,...,{b:c}...]|{a,b}|a,b|a"');
        		}
        		commands=key_.match(S.methodFilter);
        		if(commands.length<1){
        			console.warn('Uncommanded query found. Nothing to do ',key_);
        			return this;
        		}
        		key=key_.replace(S.methodFilter,'');
        		instructive=commands;//[0].split('');
        		var jQ=this;
        		for(var i =0; i<instructive.length;i++){
        			if(S.commandMap[instructive[i]])
        				tasks[i]=S.commandMap[instructive[i]];
        			else thr('No such method '+instructive[i]);
        			if(S.commander[tasks[i]])
        				jQ=S.commander[tasks[i]](jQ,key,val,S);
        			else thr('Method no defined '+tasks[i]+'. No map to '+instructive[i]);
        			if(tasks[i]=='get'){
        				if(i!=(instructive.length-1)){
        					for(var j =i+1; j<instructive.length;j++){
        						tasks[j]=S.commandMap[instructive[j]];
        						console.warn('task "'+tasks[j]+' ('+instructive[j]+')" trunked by "get (:)"');
        					}
        				}
        				if(nextAttr.length>0){
        					console.warn('next chain trunked by "get (:)"',nextAttr);
        				}
        				return jQ;
        			}
        		}
        		return jQ[plugName](nextAttr,undefined);
        	}
        	else if(typeOf(attr)=='object'){
        		return this[plugName]([attr],undefined);
        	}
        	else if(typeOf(attr)=='string'){
        		var att={};
        		att[attr]=value;
        		return this[plugName]([att],undefined);
        	}
    	};
    	if(S.input)	return this[plugName]([S.input],undefined);
    	else 		return this;
    };
    var id_=0;
    var uniqueId=function(prefix){
    	return prefix ?
    			(prefix+((++id_).toString(36))) :
    			((++id_).toString(36));
    };
    
	
}(jQuery));