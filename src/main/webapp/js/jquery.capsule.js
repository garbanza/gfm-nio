(function($) {

	function Capsule(core,options,alias){
		
		for(var key in Capsule){
			this[key]=Capsule[key];
		}
		Capsule.prototype.gen=function(v){//new RegExp("<[(.*?)\\[>","g")
			var html=this.html(v);
			var arrayFormat=[];
			var regex;
			//console.log(this);
			html.search("]>")!=-1?(
				regex=new RegExp("\\<\\[(.*?)\\]\\>","g"),arrayFormat[0]="<[",arrayFormat[1]="]>"
			):(
				html.search("\\[>")!=-1?(
					regex=new RegExp("\\<\\[(.*?)\\[\\>","g"),arrayFormat[0]="<[>",arrayFormat[1]="</[>"
				):(
					html.search("\\array>")!=-1?(
						regex=new RegExp("\\<array(.*?)array\\>","g"),arrayFormat[0]="<array>",arrayFormat[1]="</array>"
					):(
						regex=null
					)
				)
			);
			if(typeOf(v)=="array"){
				console.log('V');
				console.log(v);
				if(regex==null){
					for(var i=0;i<v.length;i++){
						html=this.make(v[i],html);
					}
				}
				else{
					var arrays=html.match(regex);
					var rndVarName=[];
					//console.log(html);
					for(var i=0;i<arrays.length;i++){
						rndVarName[i]='$('+this.uniqueId(this.randomString(1,15,'aA0'))+')';
						html=html.replace(arrays[i],rndVarName[i]);
						//console.log(html);	
					}
					html=this.make(v[0], html);
					//console.log(html);
					for(var i=0;i<arrays.length;i++){
						var evaluated="";
						for(var j=0;j<v.length;j++){
							evaluated+=this.make(v[j],arrays[i].replace(arrayFormat[0],'').replace(arrayFormat[1],''));
						}
						html=html.replace(rndVarName[i],evaluated);
						//console.log('eval '+html);
					}
				}
			}
			else if(typeOf(v)=="object"){
				html=this.make(v,html.replace(arrayFormat[0],'').replace(arrayFormat[1],''));
			}
			//this.style();
			//console.log('GEN: ' +html);
			return html;
		};
		Capsule.prototype.getHtml=function(v){
			return this.gen(v);
		};
		Capsule.prototype.make=function(v,html){
			var ret=html;
			for (var key in this.defaults)
				ret=v.hasOwnProperty(key)?
						ret.replace(new RegExp("\\$\\("+key+"\\)","g"),v[key]):
							ret.replace(new RegExp("\\$\\("+key+"\\)","g"),this.defaults[key]);
			for (var key in v)
				ret=ret.replace(new RegExp("\\$\\("+key+"\\)","g"),v[key]);
			return ret;
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
						styles+='-webkit-'+keyj+':'+css[keyi][keyj]+';\n';
						styles+='-moz-'+keyj+':'+css[keyi][keyj]+';\n';
						styles+='-o-'+keyj+':'+css[keyi][keyj]+';\n';
					}
					styles+='}\n';
				}
			}
			else if(typeOf(css)=='array'){
				for(var i=0;i<css.length;i++){
					for(var keyi in css[i]){
						styles+=keyi+'{\n';
						for(var keyj in css[i][keyi]){
							styles+=keyj+':'+css[i][keyi][keyj]+';\n';
							styles+='-webkit-'+keyj+':'+css[keyi][keyj]+';\n';
							styles+='-moz-'+keyj+':'+css[keyi][keyj]+';\n';
							styles+='-o-'+keyj+':'+css[keyi][keyj]+';\n';
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
			var defaultsDefined=0;
			for(var key in data){
				if(!this.defaults.hasOwnProperty(key)){
					matchesAll=false;
					dataDismatches++;
				}
				else {
					dataMatches++;
					this.defaults[key]!=''?defaultsDefined++:'';
				}
			}
			for(var key in this.defaults){
				if(!data.hasOwnProperty(key)){
					optDismatches++;
					
				}
				else optMatches++;
			}
			var hasName=!(this.name.indexOf("__unnamed")==0);
			var result={id:this.id, name:this.name, dataMatches:dataMatches,
					dataDismatches:dataDismatches,optDismatches:optDismatches,matchesAll:matchesAll,
					defaultsDefined:defaultsDefined,hasName:hasName};
			//console.log($.toJSON(result)+' for '+$.toJSON(data)+" -> "+$.toJSON(this.defaults));
			return result;
		};
		
		Capsule.prototype.capFunction=function(data){
			if(typeOf(data)=="array"){
				v=[];
				for(var i=0;i<data.length;i++){
					for(var key in this.defaults){
						if(data[i][key])v.push(data[i][key]);
						else v.push(this.defaults[key]);
					}
				}
			}
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
		if(typeOf(core)=="string"){
			if(core.trim().charAt(0)=='<'){
				var coreObj={};
				coreObj.html=function(){return core;};
				
				if(typeOf(options)=="string"){
					//coreObj.name=options?options:'unnamed'+this.id;
					setOptions(coreObj, core);
					coreObj.name=options!=''?options:'';
					//console.log(coreObj.name+ " "+core);
				}
				else if(typeOf(options) == "object"){
					if(options.hasOwnProperty('name'))coreObj.name=options.name!=''?options.name:'';
					coreObj.defaults=options.defaults;
				}
				else if(options==null){
					setOptions(coreObj, core);
				}
				var capsule=new Capsule(coreObj,null,alias);
				if(capsule.hasOwnProperty('css'))capsule.style();
				return capsule;
			}
			else {
				var coresfs=coresfromServer(core);
				if(coresfs==null)return;
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
				coreObj=new Capsule(core[i],null,core[i].hasOwnProperty('alias')?core[i].alias:null);
				capsules.push(coreObj);
			}
			return capsules;
		}
		else{
			this.id=Capsule.count++;
			for(var key in core){
				this[key]=core[key];
				//console.log(core.name+' adding key '+key);
			}
			if(!core.hasOwnProperty('name')){
				if(options!=null)this.name=options!=''?options:'__unnamed'+this.id;
				else {
					//this.name=core.name!=''?core.name:'__unnamed'+this.id;
					this.name='__unnamed'+this.id;
					//console.log(this.name+" "+core.name);
				}
			}
			else {
				this.name=core.name!=''?core.name:'__unnamed'+this.id;
				//this.name='__unnamed'+this.id;
				//console.log(this.name+" "+core.name);
			}
			var theName=this.name;
			if(!core.hasOwnProperty('defaults')){
				var coreObj={};
				setOptions(coreObj, core.html());
				this.defaults=coreObj.defaults;
			}
			for(var i=0;i<Capsule.capsules.length;i++){
				if(Capsule.capsules[i].name==theName){
					//console.log("cap ya en uso .>"+theName);
					return null;
				}
			}
			Capsule.capsules.push(this);
			var thethis=this;
			if(this.hasOwnProperty('css'))this.style();
			//var sp=theName.split('.');
			//TODO fix this mess down
			//if(sp.length>1){
				//treefun(jQuery.fn,sp,'append',this,false,false);
				/*treefun(jQuery.fn,sp,function(data,feed){
					var this_=this;
					var thethis_=thethis;
					Capsule.__tool(this_,data,'append',thethis_,feed);
					return this;
				});
				var fieldName=sp[sp.length-1];
				var up=fieldName[0]==fieldName[0].toUpperCase();
				var FieldName=fieldName.charAt(0).toUpperCase()+fieldName.slice(1);
				sp[sp.length-1]=(up?'Pre':'pre')+FieldName;
				treefun(node,fields,method,capsule,feed,getChilds,i)
				treefun(jQuery.fn,sp,function(data,feed,method,capsule){
					var this_=this;
					var thethis_=thethis;
					Capsule.__tool(this_,data,'prepend',thethis_,feed);
					return this;
				});
				sp[sp.length-1]=(up?FieldName+'In':fieldName+'In');
				treefun(jQuery.fn,sp,function(data,feed){
					var this_=this;
					var thethis_=thethis;
					return Capsule.__tool(this_,data,'append',thethis_,feed);
				});
				
				sp[sp.length-1]=(up?'Pre'+FieldName+'In':'pre'+FieldName+'In');
				treefun(jQuery.fn,sp,function(data,feed){
					var this_=this;
					var thethis_=thethis;
					return Capsule.__tool(this_,data,'prepend',thethis_,feed);
				});
				sp[sp.length-1]=(up?'Feed':'feed')+FieldName;
				treefun(jQuery.fn,sp,function(data,feed){
					var this_=this;
					var thethis_=thethis;
					return Capsule.__tool(this_,data,'append',thethis_,feed);
				});
				sp[sp.length-1]=(up?'PreFeed':'preFeed')+FieldName;
				treefun(jQuery.fn,sp,function(data,feed){
					var this_=this;
					var thethis_=thethis;
					return Capsule.__tool(this_,data,'prepend',thethis_,feed);
				});*/
					
			//}
			//TODO this 'if' is at test stage
			if(alias==null){
				var tname=this.name;
				if(tname.search(/\./)>=0){
					var tnameSplit=tname.split(".");
					var _alias=tnameSplit[0];
					for(var i=1;i<tnameSplit.length;i++){
						_alias+=tnameSplit[i].charAt(0).toUpperCase()+tnameSplit[i].slice(1);
					}
					alias=_alias;
				}
				else alias=this.name;//.replace(/\./g,'_');
			}
			this.alias=alias;
			var toPushCapsule=true;
			var capsuleIndex;
			for(var i=0;i<Capsule.capsules.length;i++){
				if(Capsule.capsules[i].alias==alias){
					toPushCapsule=false;
					capsuleIndex=i;
					break;
				}
			}
			//if(toPushCapsule)Capsule.capsules.push(this);
			//else return Capsule.capsules[capsuleIndex];
			if(alias!=null){
				var aliasCap=alias.charAt(0).toUpperCase()+alias.slice(1);
				if(jQuery.fn.hasOwnProperty(alias)){
					jQuery.fn[alias]['names'].push(theName);
				}
				else{
					
					jQuery.fn[alias]=function(data,feed) {
						var this_=this;
						//console.log("capsule is:");
						//console.log(jQuery.fn[alias]['names']);
						Capsule.__tool(this_,data,'append',thethis,feed);
						return this;
					};
					jQuery.fn[alias]['names']=[];
					jQuery.fn[alias]['names'].push(theName);

					jQuery.fn['pre'+aliasCap] = function(data,feed) {
						var this_=this;
						Capsule.__tool(this_,data,'prepend',thethis,feed);//jQuery.fn[alias]['names'],feed);
						return this;
					};
					jQuery.fn['in'+aliasCap] = function(data,feed) {
						var this_=this;
						return Capsule.__tool(this_,data,'append',thethis,feed);//,jQuery.fn[alias]['names'],feed);
					};
					jQuery.fn['preIn'+aliasCap] = function(data,feed) {
						var this_=this;
						return Capsule.__tool(this_,data,'prepend',thethis,feed);//,jQuery.fn[alias]['names'],feed);
					};
					//TODO no feed no this
					jQuery.fn['feed'+aliasCap]=function(method,data){

						return thethis[method](this, data);
						
						//return this.capsule(data,jQuery.fn[aliasCap]['names'],true);
					};
					/*
					jQuery.fn['feedpre'+aliasCap]=function(data){
						return this.precapsule(data,jQuery.fn[aliasCap]['names'],true);
					};
					*/
				}
			}
			return this;
		}
	};
	
	/*function treefun(node,fields,method,capsule,feed,getChilds,i){
		i==null?i=0:i++;
		if(i==fields.length-1){
			node[fields[i]]=function(data){
				console.log('----- feed='+feed+" getChilds="+getChilds);
				console.log(this.parent);
				console.log(capsule);
				console.log('-------------------------------');
				if(getChilds)return Capsule.__tool(this,data,method,capsule,feed);
				else return this.parent;
			}
			return;
		}
		if(i==0){
			node.hasOwnProperty(fields[i])?'':node[fields[i]]={},node[fields[i]].parent=function(){return this;
			treefun(node[fields[i]],fields,method,capsule,feed,getChilds,i);
		}
		else {
			node.hasOwnProperty(fields[i])?'':node[fields[i]]={},node[fields[i]].parent=this.parent;
			treefun(node[fields[i]],fields,method,capsule,feed,getChilds,i);
		}
	}*/

	
	function coresfromServer(str){
		//TODO improbe this pattern - URL validator
		var pattern = /(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/;
		var url=null;
		if(pattern.test(str)){
			if(Capsule.capsulesFromURL==null)Capsule.capsulesFromURL=[];
			
			for(var i=0;i<Capsule.capsulesFromURL.length;i++){
				if(Capsule.capsulesFromURL[i]==str){
					//console.log("url ya en uso "+str);
					return null;
				}
			}
			Capsule.capsulesFromURL.push(str);
			url=str;
		}
		else {
			for(var i=0;i<Capsule.capsules.length;i++){
				if(Capsule.capsules[i].name==str){
					//console.log("cap ya en uso "+str);
					return null;
				}
			}
		}
		if(url==null){
			var cps;
			if(str.match(/.js$/)==".js"){
				cps=str.replace(/.js$/,"").replace(/\./g,"/")+".js";
			}
			else {
				cps=str.replace(/\./g,"/")+".js";
			}
			var host=($.capsule.host?$.capsule.host:location.host);
			var slash="";
			if(host.match(/\/$/)!="/")slash="/";
			
			url=host+slash+
				"js/"+cps;
		}
		//console.log(url);
		var cores=null;
		//console.log(url);
		$.ajax({
			  url: url,
			  async:false
		}).done(function(data){
			var crs="cores="+data;
			eval(crs);
			//console.log(crs);
		}).fail(function(){
			cores=null;
		});
		
		return cores;
	};
	
	Capsule.getMoreAccurate=function(data,name){
		//console.log("more accurate for:");
		//console.log(data);
		//console.log(name);
		var theData=typeOf(data)=='array'?data[0]:data;
		var capsules=[];
		if(name!=null){
			//console.log('processing '+name+': '+$.toJSON(data));
			for(var i=0;i<Capsule.capsules.length;i++){
				if(typeOf(name)=='array'){
					for(var j=0;j<name.length;j++){
						if(Capsule.capsules[i].name==name[j])capsules.push(Capsule.capsules[i]);
					}
				}
				else {if(Capsule.capsules[i].name==name)capsules.push(Capsule.capsules[i]);}
			}
		}
		else capsules=Capsule.capsules;
		//console.log('data is: '+$.toJSON(data));
		var capsule;
		var compatibility=[];
		var matchesAll=[];
		for(var i=0;i<capsules.length;i++){
			var cmpt=capsules[i].compatibility(theData);
			compatibility.push(cmpt);
			if(cmpt.matchesAll)matchesAll.push(cmpt);
		}
		if(matchesAll.length>0){
			//console.log("matches all:"+matchesAll.length);
			//console.log(matchesAll);
			if(matchesAll.length==1)capsule=Capsule.get(matchesAll[0].id);
			else{
				var minor=Infinity;
				var cpstmp=null;
				for(var i=0;i<matchesAll.length;i++){
					if(matchesAll[i].optDismatches<minor){
						cpstmp=matchesAll[i];
						minor=matchesAll[i].optDismatches;
					}
					else if(matchesAll[i].optDismatches==minor){
						if(matchesAll[i].defaultsDefined>cpstmp.defaultsDefined){
							cpstmp=matchesAll[i];
						}
						else if(matchesAll[i].defaultsDefined==cpstmp.defaultsDefined){
							if(matchesAll[i].hasName){
								cpstmp=matchesAll[i];
							}
							else if(!matchesAll[i].hasName){
								if(!cpstmp.hasName)cpstmp=matchesAll[i];
							}
						}
					}
				}
				capsule=Capsule.get(cpstmp.id);
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
								else if(compatibility[i].optDismatches==cmpt.optDismatches){
									if(compatibility[i].defaultsDefined>cmpt.defaultsDefined){
										cmpt=compatibility[i];
									}
									else if(compatibility[i].defaultsDefined==cmpt.defaultsDefined){
										if(compatibility[i].hasName){
											cmpt=compatibility[i];
										}
										else if(!compatibility[i].hasName){
											if(!cmpt.hasName)cmpt=compatibility[i];
										}
									}
								}
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
			if(cmpt==null){
				//console.log("no capsule found");
				throw new Error("no capsules matching your request for:\n\tdata='"+$.toJSON(theData)+"'\n\tname='"+$.toJSON(name)+"'");
			}
			//else console.log("capsule found");
			if(cmpt.dataMatches==0)capsule=null;
			else capsule=Capsule.get(cmpt.id);
			
		}
		//console.log('returning capsule: '+capsule.name);
		return capsule;
	};
	Capsule.count=0;
	Capsule.capsules=[];
	Capsule.id_=0;
	Capsule.uniqueId=function(prefix){return prefix?(prefix+(++this.id_)):(++this.id_);},
	Capsule.randomString=function(minWords,maxWords, minLength, maxLength, kind){
	    var text = '';
	    var possible='';
	    var low = 'abcdefghijklmnopqrstuvwxyz';
	    var upp = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
	    var num = '0123456789';
	    if(minLength==null){
	    	kind='a';
	    	minLength=maxWords;
	    	maxLength=minLength;
	    	maxWords=minWords;
	    }
	    if(maxLength==null){
	    	kind=minLength;
	    	minLength=maxWords;
	    	maxLength=minLength;
	    	maxWords=minWords;
	    }
	    if(kind==null)kind='a';
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
		var capsule=null;
		if(Capsule.isNumber(name)){
			for(var i=0;i<Capsule.capsules.length;i++)
				if(Capsule.capsules[i].id==name){
					capsule=Capsule.capsules[i];
					break;
				}
		}
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

	Capsule.typeOf=function(value) {
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
	
	var typeOf=Capsule.typeOf;
	
	$.capsule=function(cores,options,alias){
		new Capsule(cores,options,alias);
	};
	for(var key in Capsule){
		$.capsule[key]=Capsule[key];
	}
	
	jQuery.fn.capsule = function(data,name,feed) {
		var this_=this;
		Capsule.__tool(this_,data,'append',name,feed);
		return this;
	};
	jQuery.fn.preCapsule = function(data,name,feed) {
		var this_=this;
		Capsule.__tool(this_,data,'prepend',name,feed);
		return this;
	};
	jQuery.fn.inCapsule = function(data,name,feed) {
		var this_=this;
		return Capsule.__tool(this_,data,'append',name,feed);
	};
	jQuery.fn.preInCapsule = function(data,name,feed) {
		var this_=this;
		return Capsule.__tool(this_,data,'prepend',name,feed);
	};
	/*
	jQuery.fn.feedcapsule=function(data,name){
		return this.capsule(data,name,true);
	};
	
	jQuery.fn.feedprecapsule=function(data,name){
		return this.precapsule(data,name,true);
	};
	*/
	//TODO has que el nombre del core sea usado en $().coreName(), $().precoreName()
	Capsule.__tool=function(this_, data, method,name,feed){
		feed=feed==null?false:feed;
		//console.log('data is: '+$.toJSON(data));
		if(data==null)data={};
		var capsule=null;
		//console.log("tool for name="+name);
		if(typeOf(name)=="string"||typeOf(name)=="array")capsule=Capsule.getMoreAccurate(typeOf(data)=="array"?data[0]:data,name==null?null:name);
		else if(name==null)capsule=Capsule.getMoreAccurate(typeOf(data)=="array"?data[0]:data,null);
		else if(typeOf(name)=="object"){
			capsule=name;
			//console.log(capsule);
		}
		//console.log(":"+name+" is");
		//console.log(this_);
		var resultset=[];
		//console.log(this_);
		this_.each(function(){
			var children=[];
			
			var afterChildren=[];
			var html=null;
			
			if(feed){
				var chld=capsule.feed(data,method);
				for(var i=0;i<chld.length;i++){
					children.push(chld[i]);
					capsule.hasOwnProperty('after')?afterChildren.push(chld[i]):'';
				}
				//capsule.hasOwnProperty('after')?afterProcess={capsule:capsule, children:afterChildren, data:data}:'';
			}
			else{
				capsule.hasOwnProperty('init')?capsule.init(data):'';
				html=capsule.hasOwnProperty('html')?capsule.gen(data):null;
			}
			
			if(html!=null){
				var htmlObjects=$(html);
				var length=htmlObjects.length;
				
				for(var j=0;j<length;j++){
						children.push(htmlObjects[j]);
						capsule.hasOwnProperty('after')?afterChildren.push(htmlObjects[j]):'';
				}
				//capsule.hasOwnProperty('after')?afterProcess={capsule:capsule, children:afterChildren, data:data}:'';
			
			}
			if(method=='append'&&!feed)
				$(this).append(children);
			else if(method=='prepend'&&!feed)
				$(this).prepend(children);

			for(var i=0;i<children.length;i++){
				resultset.push(children[i]);
			}
			capsule.hasOwnProperty('after')?capsule.after(data,afterChildren):'';
		});
		return $(resultset);
	};
	

	
})(jQuery);



