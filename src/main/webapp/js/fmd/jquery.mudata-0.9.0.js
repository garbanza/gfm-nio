(function ( $ ) {
	
	var error1=function(){
		throw new Error('mudata must receive string of the form "[$|-|+|:]attribute[.property]"');
	};
	var typeOf = function(value) {
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
	if (typeOf(String.prototype.startsWith) != 'function') {
		String.prototype.startsWith = function (str){
			return this.indexOf(str) == 0;
		};
	};
	//TODO make this more prototyped
	var oSlice=function(o){
		var ret={};
		var i=0;
		for(var key in o){
			i==0?'':ret[key]=o[key];
			i++;
		}
		return ret;
	};
	//TODO make this more prototyped
	var oFirst=function(o){
		var ret={};
		var i=0;
		for(var key in o){
			ret.name=key;
			ret.value=o[key];
			break;
		}
		return ret;
	};
	//TODO make this more prototyped
	var oSize=function(o){
		var i=0;
		for(var key in o){i++;}
		return i;
	};
	
	//TODO fuck this
	/*function Commies(o){
		var i=0;
		for(var key in o){
			this[i]=new function(key,o[key]){};
			i++;
		}
		Object.defineProperty(Commies.prototype, 'next', {
		    value: function () {
		    },
		    enumerable: false
		});
		//Commies.prototype.next=function(){
			
		//}
	}*/
	var plus=function(this_,attr,value){
		var oldAttrs=$(this_).attr(attr);
		var splited=oldAttrs.split(" ");
		var hasnt=true;
		for(var i=0;i<splited.length;i++){
			if(splited[i]==value){
				hasnt=false;
				break;
			}
		}
		if(hasnt)$(this_).attr(attr,oldAttrs+' '+value);
	};
	var minus=function(this_,attr,value){
		var oldAttrs=$(this_).attr(attr);
		var splited=oldAttrs.split(" ");
		for(var i=0;i<splited.length;i++){
			if(splited[i]==value){
				splited.splice(i--,1);
			}
		}
		$(this_).attr(attr,splited.join(" "));
	};
	
	var is=function(this_,attr,value){
		var attrs=$(this_).attr(attr);
		var splited=attrs.split(" ");
		for(var i=0;i<splited.length;i++){
			if(splited[i]==value){
				return true;
			}
		}
		return false;;
	};
	
	var DATA_RWX='datarwx';
	
	var ns=function(this_,dataRWXNS){
		this_[DATA_RWX] ?
				(this_[DATA_RWX][dataRWXNS] ?
					(null) :
					(this_[DATA_RWX][dataRWXNS]={})) :
				(this_[DATA_RWX]={},this_[DATA_RWX][dataRWXNS]={});
		return this_[DATA_RWX][dataRWXNS];
	}
	var commander={
			plus:function(jQ,attr,value,dataRWXNS,dataName){
				return jQ.each(function(){
		    		var nspace=ns(this,dataRWXNS);
		    		
					if(!this.hasAttribute(dataName)){
						this.setAttribute(dataName,attr);
					}
					else{
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
					nspace[dataName]={};
					if(attr!=""){
						nspace[dataName][attr]={};
						nspace[dataName][attr].exec=(typeOf(value)=='function')?true:false;
						nspace[dataName][attr].value=value;
						nspace[dataName][attr].htme=this;
					}
				});
			},
			less:function(jQ,attr,value,dataRWXNS,dataName){
				return jQ.each(function(){
					var nspace=ns(this,dataRWXNS);
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
			get:function(jQ,attr,value,dataRWXNS,dataName){
				var this_=jQ[0];
				var nspace=ns(this_,dataRWXNS);
	    		return nspace[dataName] ?
	    				(nspace[dataName][attr] ?
	    						(nspace[dataName][attr].value) :
	    						(undefined)) :
	    				(undefined);
			},
			exec:function(jQ,attr,value,dataRWXNS,dataName){
				if(attr==""){
					console.warn('".noMethod" Execution method not especified for "'+dataName+'". Nothing to do.');
					return jQ;
				}
				return jQ.each(function(){
					var nspace=ns(this,dataRWXNS);
					if(! this.hasAttribute(dataName)){
						return;
					}
					else{
						nspace[dataName][attr].exec ?
								(nspace[dataName][attr].value(value)) :
								(null);
					}
				});
			},
			equals:function(jQ,attr,value,dataRWXNS,dataName){
				var jQRet=$();
				jQ.each(function(){
					var nspace=ns(this,dataRWXNS);
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
			different:function(jQ,attr,value,dataRWXNS,dataName){
				var jQRet=$();
				jQ.each(function(){
					var nspace=ns(this,dataRWXNS);
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
			find:function(jQ,attr,value,dataRWXNS,dataName){
				if(attr==""){
					return jQ.find('['+dataName+']');
				}
				else return jQ.find('['+dataName+'~="'+attr+'"]');
			}
	};
	
	var commandMap={
			'+':'plus',
			'-':'less',
			':':'get',
			'*':'find',
			'=':'equals',
			'.':'exec',
			'!':'different'
	};
	
    $.fn.mudata=function(attr,value,dataRWXNS,dataName){
    	//console.log(attr,value,dataRWXNS,dataName);
    	/*if(typeOf(attr)!='string'){
			error1();
    		return null;
		}*/
    	
    	if(typeOf(attr)=='string'){
    		//TODO improve this filter
    		if(attr.match(/^\$\w+$/)){//of the form '$aA09_'
        		var name="data"+attr.match(/\w+$/)[0];
        		var dataName="data-"+attr.match(/\w+$/)[0];
        		var value_=value;
        		$.fn[name]=function(attr,value){
        			var dataRWXNS_=value_;
        			

        			return this.mudata(attr,value,dataRWXNS_,dataName);
        		};
        		return this;
         	}
    	}
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
    			if(key_.match(/^[+-:*=.!]+/)==null){
    				throw new Error('argument must matcn regex /^[+-:*=.!]+/');
    			}
    			val=thisAttr[key_];
    		}
    		else if(typeOf(thisAttr)=='string'){
    			if(thisAttr.match(/^[+-:*=.!]+/)==null){
    				throw new Error('argument must matcn regex /^[+-:*=.!]+/');
    			}
    			key_=thisAttr;
    			val=undefined;
    		}
    		else{
    			throw new Error('use the form "[...,a,...,{b:c}...]|{a,b}|a,b|a"');
    		}
    		commands=key_.match(/^[+-:*=.!]+/);
    		if(commands.length<1){
    			console.warn('Uncommanded query found. Nothing to do ',key_);
    			return this;
    		}
    		key=key_.replace(/^[+-:*=.!]+/,'');
    		instructive=commands[0].split('');
    		var jQ=this;
    		for(var i =0; i<instructive.length;i++){
    			tasks[i]=commandMap[instructive[i]];
    			jQ=commander[tasks[i]](jQ,key,val,dataRWXNS,dataName);
    			if(tasks[i]=='get'){
    				if(i!=(instructive.length-1)){
    					for(var j =i+1; j<instructive.length;j++){
    						tasks[j]=commandMap[instructive[j]];
    						console.warn('task "'+tasks[j]+' ('+instructive[j]+')" trunked by "get (:)"');
    					}
    				}
    				if(nextAttr.length>0){
    					console.warn('next chain trunked by "get (:)"',nextAttr);
    				}
    				return jQ;
    			}
    			//jQ,attr,value,dataRWXNS,dataName
    		}
    		return jQ.mudata(nextAttr,undefined,dataRWXNS,dataName);
    	}
    	else if(typeOf(attr)=='object'){
    		return this.mudata([attr],undefined,dataRWXNS,dataName);
    	}
    	else if(typeOf(attr)=='string'){
    		var att={};
    		att[attr]=value;
    		return this.mudata([att],undefined,dataRWXNS,dataName);
    	}
    	if(typeOf(attr)=='object'){
    		if(oSize(attr)==0){
    			return this;
    		}
    		nextAttr=oSlice(attr);
    		thisAttr=oFirst(attr);
    		var commands=thisAttr.name.match(/^[+-:*=.!]+/);
    		var prop=thisAttr.name.replace(/^[+-:*=.!]+/,'');
    		var value=thisAttr.value;
    		if(commands.length<1){
    			console.warn('nothing to do ',thisAttr);
    			return this;
    		}
    		else{
    			var instructive=commands[0].split('');
    			var jQ=this;
    			for(var i =0; i<instructive.length;i++){
    				tasks[i]=commandMap[instructive[i]];
    				jQ=commander[tasks[i]](jQ,prop,value,dataRWXNS,dataName);
    				if(tasks[i]=='get'){
    					if(i!=(instructive.length-1)){
    						for(var j =i+1; j<instructive.length;j++){
    							tasks[j]=commandMap[instructive[j]];
    							console.warn('task "'+tasks[j]+' ('+instructive[j]+')" trunked by "get (:)"');
    						}
    					}
    					if(oSize(nextAttr)>0){
    						console.warn('next chain trunked by "get (:)"',nextAttr);
    					}
    					return jQ;
    				}
    				//jQ,attr,value,dataRWXNS,dataName
    			}
    			return jQ.mudata(nextAttr,undefined,dataRWXNS,dataName);
    		}
    	}
    	return this;
    	var ns=dataRWXNS;
    	var ret=[];
    	
    	var command=attr.replace(/\s/g, '');//clean white garbage
		var unlabeled=command.replace(/^[\+|\-|\:]/,'');
		var core=unlabeled.split(".");
		if(core.length<1||core.length>2){
			error1();
    		return null;
		}
		var att=core[0];
		var prp=(core.length==2)?core[1]:null;
		
		// (":a[.b]",null)
		if(command.startsWith(':')){
			var this_=this[0];
			this_.dataRWX ?
    				(this_.dataRWX[ns] ?
    					(null) :
    					(this_.dataRWX[ns]={})) :
    				(this_.dataRWX={},this_.dataRWX[ns]={});
    		var nspace=this_.dataRWX[ns];
			// (":a",null)
			if(core.length==1){
				return nspace[att];
			}
			// (":a.b",null)
			else if(core.length==2){
				return nspace[att][prp];
			}
		}
		
    	this.each(function(){
    		
    		this.dataRWX ?
    				(this.dataRWX[ns] ?
    					(null) :
    					(this.dataRWX[ns]={})) :
    				(this.dataRWX={},this.dataRWX[ns]={});
    		var nspace=this.dataRWX[ns];
    		// ("+a[.b]",null)
			if(command.startsWith('+')){
				// ("+a",null)
				if(core.length==1){
					this.setAttribute(att,'');
					if(typeOf(value)!="undefined")
						nspace[att]=value;
					ret.push(this);
				}
				// ("+a.b",null)
				else if(core.length==2){
					plus(this,att,prp);
					if(typeOf(value)!="undefined"){
						if(typeOf(nspace[att])=="undefined"){
							nspace[att]={};
						}
						nspace[att][prp]=value;
					}
					ret.push(this);
				}
			}
			// ("-a[.b]",null)
			else if(command.startsWith('-')){
				// ("-a",null)
				if(core.length==1){
					this.removeAttribute(att);
					if(typeOf(nspace[att])!="undefined")
						if(!(delete nspace[att]))
							nspace[att]=undefined;
					ret.push(this);
				}
				// ("-a.b",null)
				else if(core.length==2){
					minus(this,att,prp);
					if(typeOf(nspace[att])!="undefined"
							&&typeOf(nspace[att][prp])!="undefined")
						if(!(delete nspace[att][prp]))
							nspace[att][prp]=undefined;
					ret.push(this);
				}
			}
			// ("a[.b]",null)
			else {
				// ("a",null)
				if(core.length==1){
					if(this.hasAttribute(att)){
						ret.push(this);
					}
				}
				// ("a.b",null)
				else if(core.length==2){
					if(this.hasAttribute(att)){
						if(is(this,att,prp))
							ret.push(this);
					}
				}
			}
    	});
    	return ret;
    };
}(jQuery));