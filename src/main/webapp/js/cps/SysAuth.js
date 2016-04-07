(function($){$.capsule(

	{
		type:'CapsuleCore',
		info:{
			author:'raynmune at gmail dot com',
			license:'Creative Commons',
			version:'1.0'
		},

		name:'cps.SysAuth',

		defaults:{
			color:'rgba(217,237,255,.8)',
			message:"introduce password de autenticación : ",
			id:function(){
				return "cps.SysAuth"+$.capsule.randomString(1,1,15,15,'aA0');
			}
		},
		html:function(){
			return 	"" +
				"<div id='$(id)' " +
						"style='background-color:$(color);" +
						"position:absolute;top:0;left:0;" +
						"height:100%;width:100%;z-index:999'>"+
					"<label style='background-color:white'>$(message)</label>"+
					"<input style='background-color:white' type='password'>" +
					"<button>cancel</button>"+
				"</div>";
		},
		after:function(data,els){
			$(els).hide();
		},
		//params - opts.cancel, opts.failed, opts.destroy, opts.todo
		input:function(select_, opts){
			var html=this.getHtml(opts);
			console.log(html);
			var select=$(html).appendTo(select_);
			//$(select).show();
			var input=$(select).children("input");
			var button=$(select).children("button");
			input.focus().bind('keydown',function(event){
				if(event.which==13){
					var success=opts.todo(input.val());
					if(success){
						$(select).remove();
					}
					else {
						alert(opts.failed);
						input.val("");
					}
				}
				else if(event.which==27){
					var yes=confirm(opts.cancel);
					if(yes){
						input.unbind('keydown');
						$(select).remove();
					}
				}
			});
			button.bind('click',function(event){
				$(select).remove();
			});
			console.log(select);
			console.log(opts);
		}
	}



                        
);})(jQuery);