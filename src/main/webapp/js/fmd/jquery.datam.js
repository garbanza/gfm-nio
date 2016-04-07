(function ( $ ) {
	var dataRWXNS='default';
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
	$.fn.datam=function(attr,value){
		if(typeOf(attr)=='object'){
			$().mudata('$data'+attr.data,attr.nspace);
			dataRWXNS=attr.nspace;
			return this;
		}
		var prefix="";
		var att=attr;
		if(attr.match(/^[\+|\-|\:]/)){
			prefix=attr.match(/^[\+|\-|\:]/)[0];
			att=attr.slice(1);
		}
		return this[dataRWXNS](prefix+"data-"+dataRWXNS+(att!=""?("."+att):''),value);
		
	};
}(jQuery));