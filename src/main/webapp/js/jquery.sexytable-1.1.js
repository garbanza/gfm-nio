	(function($) {

		jQuery.fn.equals = function(selector) {
			return $(this).get(0)==$(selector).get(0);
		};
		
	})(jQuery);

	(function($) {
		
		jQuery.fn.sexytable = function(options) {
			// global options
			var gopts = $.extend({}, jQuery.fn.sexytable.defaults, options);
			if(gopts.addRow){
				return $(this).each(function(){
					addRow(this,gopts.row, gopts.index,gopts.class_);
				});
			}

			if(gopts.removeRow){
				return $(this).each(function(){
					removeRow(this,gopts.index);
				});
			}
			if(gopts.editedRow){
				return $(this).each(function(){
					editedRow(this,gopts.index);
				});
			}
			if(gopts.disabledRow){
				return $(this).each(function(){
					disabledRow(this,gopts.index);
				});
			}
			if(gopts.enabledRow){
				return $(this).each(function(){
					enabledRow(this,gopts.index);
				});
			}
			if(gopts.render){
				return $(this).each(function(){
					render(this);
				});
			}
	
			
			
			// for each matched element
			return $(this).each(function(){
				//this particular options
				var topts = $.meta ? $.extend({}, gopts, $(this).data()) : gopts;
				topts.rndid=0;
				registerTopts(this,topts);
				addRow(this,topts.row,topts.index,topts.class_);
			});


		};

		addRow=function(e,row,index,class_){
			topts=getRegisteredTopts(e);
			var rndID="tableingRNDID"+String((new Date()).getTime()).replace(/\D/gi,'')+(topts.rndid++);
			eLength=$(e).children().length;
			if(index>=eLength||index=='last'){
				$(e).append("<div class='"+class_+"' id='"+rndID+"'></div>");
			}
			else if(index=='first'){
				$(e).children(":eq(1)").before("<div class='"+class_+"' id='"+rndID+"'></div>");
			}
			else{
				$(e).children(":eq("+index+")").before("<div class='"+class_+"' id='"+rndID+"'></div>");
			}
			var left=[];
			
			var maxH=0;
			for(var i=0;i<row.length;i++){
				left[i]=i==0?0:left[i-1]+row[i-1].width;
				$(e).find('#'+rndID).append("<div class='"+(i%2==0?'celleven':'cellodd')+"' id='"+rndID+i+"' width='"+row[i].width+"%'>"+row[i].content+"</div>");
				$(e).find('#'+rndID+i).css({position:'absolute',left: left[i]+'%',top:0,width: row[i].width+'%',overflow: 'hidden'});
				var h=$('#'+rndID+i).height();
				if(h>maxH)maxH=h;
			}
			
			$('#'+rndID).css({position:'absolute',width:'100%',height: maxH});
			render(e);
		};

		removeRow=function(e,index){
			topts=getRegisteredTopts(e);
			if(index>=0){
				$(e).children(":eq("+index+")").remove();
			}
			else{
				$(index,e).remove();
			}
		}
		
		editedRow=function(e,index){
			topts=getRegisteredTopts(e);
			if(index>=0){
				$(e).children(":eq("+index+")").addClass('editedrow');
			}
			else{
				$(index,e).addClass('editedrow');
			}
		}
		
		disabledRow=function(e,index){
			topts=getRegisteredTopts(e);
			if(index>=0){
				$(e).children(":eq("+index+")").addClass('disabledrow');
			}
			else{
				$(index,e).addClass('disabledrow');
			}
		}
		
		enabledRow=function(e,index){
			topts=getRegisteredTopts(e);
			if(index>=0){
				$(e).children(":eq("+index+")").removeClass('disabledrow');
			}
			else{
				$(index,e).removeClass('disabledrow');
			}
		}

		render=function(e){
			var H=0;
			topts=getRegisteredTopts(e);
			$(e).children().each(function(){
				if(topts.animate==0)$(this).css({top:H});
				else $(this).animate({top:H},topts.animate);
				H+=$(this).height();
				//alert('H: '+H+" $(this).height(): "+$(this).height());
			});
			$(e).css('height',H);
		}

		jQuery.fn.sexytable.defaults = {
				row:[],//[{content:"field test 1",width:'50%'},{content:"field test 2",width:'50%'}],
				addRow:false,
				index:0,
				class_:"tableinghead",
				render:false,
				removable:false,
				editedRow:false,
				disabled:false,
				enabled:true,
				animate:50
		};

		var registeredTopts = new Array(0);
		
		registerTopts = function(element,topts){
			registeredTopts.push({'e':element,'topts':topts});
		};

		getRegisteredTopts=function(e){
			for(var i=0;i<registeredTopts.length;i++){
				if($(e).equals(registeredTopts[i].e)){
					return registeredTopts[i].topts;
				}
			}
		};
	
	})(jQuery);