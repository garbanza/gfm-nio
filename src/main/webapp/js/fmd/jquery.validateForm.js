(function ( $ ) {
    $.fn.validateForm=function(options){
    	return this.each(function(){
    		var settings = $.extend(true,{
    	       	notEmpty:function(val){
    	       		return val?true:false;
    	       	},
    	       	numeric:function(val){
    	       		return !isNaN(parseFloat(val)) && isFinite(val);
    	       	},
    		}, options);
    		settings.tests={};
    		settings.error={};
    		for(var key in settings.input){
    			settings.tests[key]=false;
    			settings.error[key]=settings.input[key].msg;
    		}
    		console.log("this");
    		console.log(this);
    		var this_=this;
    		$(this).find("[app-id~='form-input']").each(function(){
      			$(this).keyup(function(){
    				//console.log(id+" changed "+$(this).val());
    				//console.log($(this_).val());
    				
    				var accepted=true;
    				var notAcceptedMessage="";
    				for(var key in settings.input){
    					if(! settings[settings.input[key].test]
    						($(this_).find('#'+key).val(),
    						settings.input[key].args)){
        				//	settings.tests[id]=true;
        					//settings.error[id]="";
        				//}
        				//else{
        					//settings.tests[id]=false;
        					//settings.error[id]=settings.input[id].msg;
        					notAcceptedMessage+=settings.input[key].msg+". ";
        					accepted=false;
        				}
    					/*if(!settings.tests[key]){
    						accepted=false;
    						notAcceptedMessage+=settings.error[key]+". ";
    					}*/
    				}
    				if(!accepted){
    					$(settings.todo).empty().append(notAcceptedMessage)
    					.css('color','red');
    					$(this_).find("[app-id~='form-submit']")
    					$(settings.submit).attr('disabled',true);
    				}
    				else{
    					$(settings.todo).empty().append(settings.submitMessage)
    					.css('color','green');
    					$(settings.submit).removeAttr('disabled');
    				}
    			});
    		});
    	});
    }
}( jQuery ));