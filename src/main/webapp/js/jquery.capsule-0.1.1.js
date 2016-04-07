(function($) {

	function Capsule(core,options){

		Capsule.prototype.gen=function(v){
			var result="";
			if(typeOf(v)=="array"){
				for(var i=0;i<v.length;i++)
					result+=this.make(v[i]);
			}
			else if(typeOf(v)=="object"){
				result+=this.make(v);
			}
			//this.style();

			return result;
		};
		
		Capsule.prototype.make=function(v){
			var html=this.html();
			for (var key in this.defaults)
				v.hasOwnProperty(key)?
						html=this.replaceAll(html,"$("+key+")",v[key]):
							html=replaceAll(html,"$("+key+")",this.defaults[key]);
			for (var key in v)
				html=this.replaceAll(html,"$("+key+")",v[key]);
			return html;
		};
		Capsule.prototype.style=function(){
			if(!this.hasOwnProperty('css')){
				////console.log('no css defined for -> '+this.name);
				return;
			}
			var css=this.css();
			var styles="";
			if(typeOf(css)=='string'){
				styles=css;
			}
			else if(typeOf(css)=='object'){
				for(var keyi in css){
					styles+=keyi+'{\n';
					for(var keyj in css[keyi]){
						styles+=keyj+':'+css[keyi][keyj]+';\n';
					}
					styles+='}\n';
				}
			}
			else if(typeOf(css)=='array'){
				for(var i=0;i<css.length;i++){
					for(var keyi in css[i]){
						styles+=keyi+'{\n';
						for(var keyj in css[i][keyi]){
							styles+=keyi+':'+css[i][keyi][keyj]+';\n';
						}
						styles+='}\n';
					}
				}
			}
			////console.log(this.name+'.style -> '+styles);
			var dcss = document.createElement('style');
			dcss.type = 'text/css';

			if (dcss.styleSheet) dcss.styleSheet.cssText = styles;
			else dcss.appendChild(document.createTextNode(styles));
			document.getElementsByTagName("head")[0].appendChild(dcss);
		};
		
		Capsule.prototype.replaceAll=function(source,stringToFind,stringToReplace){
			var temp = source;
			var index = temp.indexOf(stringToFind);
			while(index != -1){
				temp = temp.replace(stringToFind,stringToReplace);
				index = temp.indexOf(stringToFind);
			}
			return temp;
		};
		Capsule.prototype.compatiblility=function(data){
			var dataMatches=0,dataDismatches=0;
			var optMatches=0,optDismatches=0;
			var matchesAll=true;
			for(var key in data){
				if(!this.defaults.hasOwnProperty(key)){
					matchesAll=false;
					dataDismatches++;
				}
				else dataMatches++;
			}
			for(var key in this.defaults){
				if(!data.hasOwnProperty(key)){
					optDismatches++;
				}
				else optMatches++;
			}
			var result={id:this.id, name:this.name, dataMatches:dataMatches, dataDismatches:dataDismatches,
					optDismatches:optDismatches,matchesAll:matchesAll};
			////console.log($.toJSON(result)+' for '+$.toJSON(data)+" -> "+$.toJSON(this.defaults));
			return result;
		};
		Capsule.prototype.construct=function(data){
			var v;
			if(typeOf(data)=="array"){
				v=[];
				for(var i=0;i<data.length;i++){
					for(var key in this.defaults){
						if(data[i][key])v.push(data[i][key]);
						else v.push(this.defaults[key]);
					}
				}
			}
			else {
				v={};
				for(var key in this.defaults){
					v[key]=data.hasOwnProperty(key)?data[key]:this.defaults[key];
				}
			}
			return v;
		};
		
		Capsule.prototype.get=function(name){
			return $.capsule.get(name);
		};
		
		
		if(core==null)return;
		this.id=$.capsule.count++;
		if(typeOf(core)=="string"){
			this.html=function(){return core;};
			setOptions=function(this_, core){
				var vars=core.match(/\$\(\w+\)/g);
				////console.log('variables from '+core+' are '+$.toJSON(vars));
				this_.defaults={};
				if(vars!=null){
					for(var i=0;i<vars.length;i++){
						vars[i]=vars[i].replace('$(','').replace(')','');
						this_.defaults[vars[i]]='';
						////console.log(options+' adding key '+vars[i]);
					}
				}
			};
			if(typeOf(options)=="string"){
				this.name=options?options:'unnamed'+this.id;
				setOptions(this, core);
			}
			else if(typeOf(options) == "object"){
				this.name=options.name?options.name:'unnamed'+this.id;
				this.defaults=options.defaults;
			}
			else if(options==null){
				setOptions(this, core);
			}
			
		}
		else{
			if(!core.hasOwnProperty('name'))this.name='unnamed'+this.id;
			for(var key in core){
				this[key]=core[key];
				////console.log(core.name+' adding key '+key);
			}
		}
		if(this.hasOwnProperty('css'))this.style();
	};
	
	
	$.capsule=function(cores,options){
		var capsules;
		if(typeOf(cores)=="string"){
			////console.log('adding core '+cores);
			capsules=new Capsule(cores,options);
			$.capsule.capsules.push(capsules);
		}
		else if(typeOf(cores)=="array"){
			capsules=[];
			for(var i=0;i<cores.length;i++){
				////console.log('adding core '+cores[i].name);
				capsule=new Capsule(cores[i]);
				capsules.push(capsule);
				$.capsule.capsules.push(capsule);
			}
		}
		else {
			////console.log('adding core '+cores.name);
			capsule=new Capsule(cores);
			$.capsule.capsules.push(capsule);
		}
		return capsules;
	};
	
	$.capsule.getMoreAccurate=function(data,name){
		var capsules=[];
		if(name!=null){
			////console.log('processing '+name+': '+$.toJSON(data));
			for(var i=0;i<$.capsule.capsules.length;i++){
				if($.capsule.capsules[i].name==name)capsules.push($.capsule.capsules[i]);
			}
		}
		else capsules=$.capsule.capsules;
		////console.log('SHIT IS: '+$.toJSON(data));
		var capsule;
		var compatibility=[];
		var matchesAll=[];
		for(var i=0;i<capsules.length;i++){
			var cmpt=capsules[i].compatiblility(data);
			compatibility.push(cmpt);
			if(cmpt.matchesAll)matchesAll.push(cmpt);
		}
		if(matchesAll.length>0){
			////console.log('SOMETHING IS SHITTING HERE -> '+$.toJSON(matchesAll));
			if(matchesAll.length==1)capsule=$.capsule.get(matchesAll[0].id);
			else{
				var minor=Infinity;
				var index=-1;
				for(var i=0;i<matchesAll.length;i++){
					if(matchesAll[i].optDismatches<minor){
						index=i;
						minor=matchesAll[i].optDismatches;
					}
				}
				capsule=$.capsule.get(matchesAll[index].id);
			}
		}
		else{
			////console.log('LOOKING FOR BETTER MATCH -> '+$.toJSON(compatibility));
			var major=-1;
			var cmpt=null;
			for(var i=0;i<compatibility.length;i++){
				if(compatibility[i].dataMatches>=major){
					if(cmpt==null){
						cmpt=compatibility[i];
						////console.log('=null GOING TO WIN ->'+$.toJSON(cmpt));
					}
					else{
						if(compatibility[i].dataMatches==major){
							if(compatibility[i].dataDismatches==cmpt.dataDismatches){
								if(compatibility[i].optDismatches<cmpt.optDismatches){
									cmpt=compatibility[i];
									////console.log('<optdis GOING TO WIN ->'+$.toJSON(cmpt));
								}
							}
							else if(compatibility[i].dataDismatches<cmpt.dataDismatches){
								cmpt=compatibility[i];
								////console.log('<datadis GOING TO WIN ->'+$.toJSON(cmpt));
							}
						}
						else if(compatibility[i].dataMatches>major){
							cmpt=compatibility[i];
							////console.log(compatibility[i].dataMatches+'>'+major);
							////console.log('>datamatch GOING TO WIN ->'+$.toJSON(cmpt));
						}
					}
				}
				major=cmpt.dataMatches;
			}
			if(cmpt.dataMatches==0)capsule=null;
			else capsule=$.capsule.get(cmpt.id);
			
		}
		////console.log('returning capsule:'+$.toJSON(capsule));
		return capsule;
	};
	$.capsule.count=0;
	$.capsule.capsules=[];
	$.capsule.get=function(name){
		var capsule;
		if($.capsule.isNumber(name))capsule=$.capsule.capsules[name];
		else 
			for(var i=0;i<$.capsule.capsules.length;i++)
				if($.capsule.capsules[i].name==name){
					capsule=$.capsule.capsules[i];
					break;
				}
		return capsule;
	};
	$.capsule.isNumber=function(n) {
		  return !isNaN(parseFloat(n)) && isFinite(n);
	};

	jQuery.fn.capsule = function(data,name) {
		var this_=this;
		return this.each(function(){
			var chl=jQuery.fn.capsule.tool(this_,data,'append',name);
		});
	};
	jQuery.fn.precapsule = function(data,name) {
		var this_=this;
		return this.each(function(){
			jQuery.fn.capsule.tool(this_,data,'prepend',name);
		});
	};
	jQuery.fn.incapsule = function(data,name) {
		var this_=this;
		var children=[];
		this.each(function(){
			var chl=jQuery.fn.capsule.tool(this_,data,'append',name);
			for(var i=0;i<chl.length;i++)children.push(chl[i]);
		});
		return $(children);
	};
	jQuery.fn.preincapsule = function(data,name) {
		var this_=this;
		var children=[];
		this.each(function(){
			var chl=jQuery.fn.capsule.tool(this_,data,'prepend',name);
			for(var i=0;i<chl.length;i++)children.push(chl[i]);
		});
		return $(children);
	};
	
	jQuery.fn.capsule.tool=function(this_, data, method,name){
		////console.log('data is: '+$.toJSON(data));
		var children=[];
		var afterProcess=[];
		if(typeOf(data)=="array"){
			for(var i=0;i<data.length;i++){
				var capsule;
				if(name)
					capsule=$.capsule.getMoreAccurate(data[i],name);
				else{
					capsule=$.capsule.getMoreAccurate(data[i]);
					if(capsule==null){
						////console.log('no matches and ommited, error is happening for entry -> '+$.toJSON(data[i]));
					}
					////console.log('choosing -> '+capsule.name);
				}
				var html=capsule.gen(data[i]);
				var htmlObjects=$(html);
				var length=htmlObjects.length;
				var afterChildren=[];
				for(var j=0;j<length;j++){
						children.push(htmlObjects[j]);
						capsule.hasOwnProperty('after')?afterChildren.push(htmlObjects[j]):'';
				}
				capsule.hasOwnProperty('after')?afterProcess.push({capsule:capsule, children:afterChildren}):'';
			}
		}
		else{
			var capsule;

			if(name)
				capsule=$.capsule.getMoreAccurate(data,name);
			else{
				capsule=$.capsule.getMoreAccurate(data);
				if(capsule==null){
					//console.log('no matches and ommited, error is happening for entry -> '+$.toJSON(data));
				}
				//console.log('choosing -> '+capsule.name);
			}
			var html=capsule.gen(data);
			var htmlObjects=$(html);
			var length=htmlObjects.length;
			for(var j=0;j<length;j++){
				children.push(htmlObjects[j]);
				capsule.hasOwnProperty('after')?afterChildren.push(htmlObjects[j]):'';
			}
			capsule.hasOwnProperty('after')?afterProcess.push({capsule:capsule, children:afterChildren}):'';
		}
		this_.each(function(){
			if(method=='append')
				$(this_).append(children);
			else if(method=='prepend')
				$(this_).prepend(children);
		});
		for(var i=0;i<afterProcess.length;i++){
			afterProcess[i].capsule.after(afterProcess[i].children);
		}
		return $(children);
	};
	jQuery.fn.capsule.capsules=[];
	
	function typeOf(value) {
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
	}
	
})(jQuery);