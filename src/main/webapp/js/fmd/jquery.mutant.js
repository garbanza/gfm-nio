(function ( $ ) {
    $.fn.mutant=function(options){
    	if(options=='unbind'||options=='unobserve'||options=='reobserve'){
    		var _reg=$.fn.mutant._reg;
    		return this.each(function(){
    			for(var i=0;i<_reg.length;i++){
        			if(_reg[i].id===this){
        				if(options=='unbind'){
        					_reg[i].settings.observer.disconnect();
        					_reg.splice(i,1);
        				}
        				else if(options=='unobserve'){
        					_reg[i].settings.observer.disconnect();
        				}
        				else if(options=='reobserve'){
        					_reg[i].settings.observer.observe(this,_reg[i].settings.observe);
        				}
        				break;
        			}
        		}
    		});
    	}
    	return this.each(function(){
    		var settings = $.extend(true,{ 
    			observe:{
    				childList:true,
    				attributes:true,
        			characterData:true,
        			subtree:true,
        			attributeOldValue:false,
        			characterDataOldValue:false,
        			attributeFilter:[]
    			},
    			handle:function(mutations){
    				console.log(mutations);
    			}
    		}, options);
    		settings.observer = new MutationObserver(function( mutations ) {
    			settings.handle(mutations);
    			/*
    			mutations.forEach(function( mutation ) {
    				var newNodes = mutation.addedNodes; // DOM NodeList
    				if( newNodes !== null ) { // If there are new nodes added
    					var $nodes = $( newNodes ); // jQuery set
    					$nodes.each(function() {
    						var $node = $( this );
    						if( $node.hasClass( "message" ) ) {
    							// do something
    						};
    					});
    				}
    			});*/    
    		});
    		// Pass in the target node, as well as the observer options
    		settings.observer.observe(this, settings.observe);
    		
    	});
    };
    $.fn.mutant._reg=[];
}(jQuery));