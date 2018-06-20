(function($){
	var ID=-1,Z=1000;
	var registry=[];
	var globals={
			unblockOnClick:false,
			unblockOnKeyPress:false,
			unblockOnAnyKey:false,
			appendTo:'body',
			focusOn:'body',
			klass:'black-on-white'
			
	};
	$.blockUI=function(opts){
		console.log("reg init:");console.log(registry);
		console.log("opts"); console.log(opts);
		if(opts.unBlockUI){
			var focusOn;
			if(opts.id){
				var index=-1;
				for(var i=0;i<registry.length;i++){
					if(registry[i].id==opts.id){index=i;break;}
				}
				if(index>-1){
					console.log("delete "+index);
					var index2=index==0?1:index;
					focusOn=registry[index].focusOn;
					$("#"+opts.id).remove();
					registry.splice(index,index2);
				}
				else throw new Error("No blocking element found with id "+opts.id);
			}
			else{
				console.log("delete last");
				if(opts.afterUnblock)opts.afterUnblock();
				var last=registry.length-1;
				focusOn=registry[last].focusOn;
				$(registry[last].node).remove();
				registry.splice(last,last+1);
			}
			$(focusOn).focus();
			return;
		}
		if(opts.changeContent){
			var index=-1;
			var id=opts.node.attr('id');
			for(var i=0;i<registry.length;i++){
				if(registry[i].id==id){index=i;break;}
			}
			if(index>-1){
				Object.assign(registry[index],opts)
			}
			else throw new Error("No blocking element found with id "+id);
			$("#"+id).empty().append(
					'<div id="jquery-block-content" class="jquery-block-content" style="position:static;">' +
					opts.content +
    	          '</div>');
			$.blockUI.listen(opts.node,registry[index]);
			return opts.node;
		}
		var configs = Object.assign({},globals,opts);
		//console.log("init configs");
		//console.log(configs);
		//if(!configs.index)configs.index=ID++;
		ID+=1;
		configs.id=ID+randomString(1,1,30,30,"aA0");
		configs.z=++Z;Z+=1;
		configs.focusOn=document.activeElement;
		$(
    	        '<div id="'+configs.id+'" tabindex="100" class="' + configs.klass + '" style="position:fixed; z-index:' + configs.z + '; top:0px; right:0px; left:0px; height: 100%; background-color: rgba(0,0,0,.8);">' +
    	          '<div id="jquery-block-content" class="jquery-block-content" style="position:static;">' +
    	            configs.content +
    	          '</div>' +
    	        '</div>'
    	      ).appendTo(configs.appendTo).focus();
		var $overlay = $('#'+configs.id);
		$.blockUI.listen($overlay,configs);
		

		configs.node=$overlay;
		//console.log("reg before cat:");console.log(registry);
		//if(registry.length<1)
		registry=registry.concat([configs]);
		//console.log("reg after cat:");console.log(registry);
		//console.log(registry);
		return $overlay;
	}
	$.unblockUI=function(opts){
		if(opts){
			if(typeOf(opts)=="string"){
				console.log("string id:"+opts);
				$.blockUI({id:opts, unBlockUI:true});
				return;
			}
			else if(typeOf(opts)=="object"){
				console.log("opts");
				console.log(opts);
				console.log("object id:"+opts.attr('id'));
				$.blockUI({id:opts.attr('id'), unBlockUI:true});
				//$.blockUI(opts);
				return;
			}
			
		}
		else $.blockUI({unBlockUI:true});
	}
	$.blockUI.reg=function(){return registry;}
	$.blockUI.listen=function($overlay,configs){
		if(configs.unblockOnClick){
			$overlay.click(function(e){
				$.unblockUI($overlay);
				$(configs.focusOn).focus();
			});
		}
		if(configs.unblockOnKeyPress){
			$overlay.keypress(function(e){
				$.unblockUI($overlay);
				$(configs.focusOn).focus();
			});
		}
		if(configs.unblockOnAnyKey){
			//console.log("anykey");
			$overlay.bind('click keypress',function(e){
				$.unblockUI($overlay);
				$(configs.focusOn).focus();
			});
		}
	}
})(jQuery);