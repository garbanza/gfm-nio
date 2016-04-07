(function($) {
	jQuery.fn.editable = function(options) {
		return this.each(function(){
			$(this).bind("click focusin",function(){
				$('#txtarea').blur();
				var content=$(this).text();
				var this_=this;
				//alert(content);
				var pos=$(this).position();
				var top=pos.top;
				var left=pos.left;
				var height=$(this).height();
				var width=$(this).width();
				$('body').after("<textarea tabindex=0"+(parseFloat($(this).attr('tabindex'))+0)+" id='txtarea' style='opacity:0; position:absolute; top:"+top+"px left:"+left+"px; height:0px; width:"+width+"px';>"+content+"</textarea>")
				$('#txtarea').bind('keyup focusin',function(e){
					var code = (e.keyCode ? e.keyCode : e.which);
					if(code==27)$(this).blur();
					var length=$(this_).text().length;
					//var char_=String.fromCharCode(code);
					var start=$(this).caret().start;
					var str=$('#txtarea').val().substring(0,start)+"_"+(start==length?"":$('#txtarea').val().substring(start));
					$(this_).text(str);
				}).focus(function(){
					var length=$(this_).text().length;
					$(this).caret({start:length,end:length});
				}).focus();
				$('#txtarea').blur(function(){
					$(this_).text( $('#txtarea').val()!=''?$('#txtarea').val():'null');
					$(this).remove();
				});

			});
			
		});
	};
})(jQuery);