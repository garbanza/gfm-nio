(function($) {

	function Capsule(core,options){
		
		for(var key in Capsule){
			this[key]=Capsule[key];
		}
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
							html=this.replaceAll(html,"$("+key+")",this.defaults[key]);
			for (var key in v)
				html=this.replaceAll(html,"$("+key+")",v[key]);
			return html;
		};
		Capsule.prototype.style=function(){
			if(!this.hasOwnProperty('css')){
				//console.log('no css defined for -> '+this.name);
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
			//console.log(this.name+'.style -> '+styles);
			var dcss = document.createElement('style');
			dcss.type = 'text/css';

			if (dcss.styleSheet) dcss.styleSheet.cssText = styles;
			else dcss.appendChild(document.createTextNode(styles));
			document.getElementsByTagName("head")[0].appendChild(dcss);
		};
		
		Capsule.prototype.compatibility=function(data){
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
			//console.log($.toJSON(result)+' for '+$.toJSON(data)+" -> "+$.toJSON(this.defaults));
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
		
		if(core==null)return;
		
		if(typeOf(core)=="string"){
			if(core.trim().charAt(0)=='<'){
				var coreObj={};
				coreObj.html=function(){return core;};
				setOptions=function(this_, core){
					var vars=core.match(/\$\(\w+\)/g);
					//console.log('variables from '+core+' are '+$.toJSON(vars));
					this_.defaults={};
					if(vars!=null){
						for(var i=0;i<vars.length;i++){
							vars[i]=vars[i].replace('$(','').replace(')','');
							this_.defaults[vars[i]]='';
							//console.log(options+' adding key '+vars[i]);
						}
					}
				};
				if(typeOf(options)=="string"){
					//coreObj.name=options?options:'unnamed'+this.id;
					setOptions(coreObj, core);
				}
				else if(typeOf(options) == "object"){
					//coreObj.name=options.name?options.name:'unnamed'+this.id;
					coreObj.defaults=options.defaults;
				}
				else if(options==null){
					setOptions(coreObj, core);
				}
				var capsule=new Capsule(coreObj);
				if(capsule.hasOwnProperty('css'))capsule.style();
				return capsule;
			}
			else{
				var coresfs=coresfromServer(core);
				if(coresfs==null)return null;
				capsules=[];
				for(var i=0;i<coresfs.length;i++){
					//console.log('adding core '+cores[i].name);
					coreObj=new Capsule(coresfs[i]);
					capsules.push(coreObj);
				}
				return capsules;
			}
		}
		else if(typeOf(core)=="array"){
			capsules=[];
			for(var i=0;i<core.length;i++){
				//console.log('adding core '+cores[i].name);
				coreObj=new Capsule(core[i]);
				capsules.push(coreObj);
			}
			return capsules;
		}
		else{
			this.id=Capsule.count++;
			if(!core.hasOwnProperty('name'))this.name='unnamed'+this.id;
			for(var key in core){
				this[key]=core[key];
				//console.log(core.name+' adding key '+key);
			}
			
			Capsule.capsules.push(this);
			if(this.hasOwnProperty('css'))this.style();
			return this;
		}
		
	};
	
	function coresfromServer(str){
		var cpc;
		if(str.match(/.cpc$/)==".cpc")cpc=str;
		else cpc=str+".cpc";
		url="http://"+location.host+"/cpc/"+cpc;
		var cores;
		console.log(url);
		$.ajax({
			  url: url,
			  async:false
		}).done(function(data){var crs="cores="+data;eval(crs);}).fail(function(){cores=null;})
		return cores;
	};
	
	Capsule.getMoreAccurate=function(data,name){
		var capsules=[];
		if(name!=null){
			//console.log('processing '+name+': '+$.toJSON(data));
			for(var i=0;i<Capsule.capsules.length;i++){
				if(Capsule.capsules[i].name==name)capsules.push(Capsule.capsules[i]);
			}
		}
		else capsules=Capsule.capsules;
		//console.log('data is: '+$.toJSON(data));
		var capsule;
		var compatibility=[];
		var matchesAll=[];
		for(var i=0;i<capsules.length;i++){
			var cmpt=capsules[i].compatibility(data);
			compatibility.push(cmpt);
			if(cmpt.matchesAll)matchesAll.push(cmpt);
		}
		if(matchesAll.length>0){
			if(matchesAll.length==1)capsule=Capsule.get(matchesAll[0].id);
			else{
				var minor=Infinity;
				var index=-1;
				for(var i=0;i<matchesAll.length;i++){
					if(matchesAll[i].optDismatches<minor){
						index=i;
						minor=matchesAll[i].optDismatches;
					}
				}
				capsule=Capsule.get(matchesAll[index].id);
			}
		}
		else{
			var major=-1;
			var cmpt=null;
			for(var i=0;i<compatibility.length;i++){
				if(compatibility[i].dataMatches>=major){
					if(cmpt==null)cmpt=compatibility[i];
					else{
						if(compatibility[i].dataMatches==major){
							if(compatibility[i].dataDismatches==cmpt.dataDismatches){
								if(compatibility[i].optDismatches<cmpt.optDismatches)
									cmpt=compatibility[i];
							}
							else if(compatibility[i].dataDismatches<cmpt.dataDismatches)
								cmpt=compatibility[i];
						}
						else if(compatibility[i].dataMatches>major){
							cmpt=compatibility[i];
						}
					}
				}
				major=cmpt.dataMatches;
			}
			if(cmpt.dataMatches==0)capsule=null;
			else capsule=Capsule.get(cmpt.id);
			
		}
		//console.log('returning capsule:'+$.toJSON(capsule));
		return capsule;
	};
	Capsule.count=0;
	Capsule.capsules=[];
	
	Capsule.randomString=function(minWords,maxWords, minLength, maxLength, kind){
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
	
	Capsule.get=function(name){
		var capsule;
		if(Capsule.isNumber(name))capsule=Capsule.capsules[name];
		else 
			for(var i=0;i<Capsule.capsules.length;i++)
				if(Capsule.capsules[i].name==name){
					capsule=Capsule.capsules[i];
					break;
				}
		return capsule;
	};
	Capsule.isNumber=function(n) {
		  return !isNaN(parseFloat(n)) && isFinite(n);
	};

	Capsule.replaceAll=function(source,stringToFind,stringToReplace){
		var temp = source;
		var index = temp.indexOf(stringToFind);
		while(index != -1){
			temp = temp.replace(stringToFind,stringToReplace);
			index = temp.indexOf(stringToFind);
		}
		return temp;
	};
	
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
	
	$.capsule=function(cores,options){
		new Capsule(cores,options);
	};
	for(var key in Capsule){
		$.capsule[key]=Capsule[key];
	}
	
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
		//console.log('data is: '+$.toJSON(data));
		var children=[];
		var afterProcess=[];
		if(typeOf(data)=="array"){
			for(var i=0;i<data.length;i++){
				var capsule;
				if(name)
					capsule=Capsule.getMoreAccurate(data[i],name);
				else{
					capsule=Capsule.getMoreAccurate(data[i]);
					if(capsule==null){
						//console.log('no matches and ommited, error is happening for entry -> '+$.toJSON(data[i]));
					}
					//console.log('choosing -> '+capsule.name);
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
				capsule=Capsule.getMoreAccurate(data,name);
			else{
				capsule=Capsule.getMoreAccurate(data);
				if(capsule==null){
					//console.log('no matches and ommited, error is happening for entry -> '+$.toJSON(data));
				}
				//console.log('choosing -> '+capsule.name);
			}
			var html=capsule.gen(data);
			var htmlObjects=$(html);
			var length=htmlObjects.length;
			var afterChildren=[];
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
	

	
})(jQuery);



