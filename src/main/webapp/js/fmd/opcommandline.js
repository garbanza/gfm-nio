/** COMMAND LINE FUNCTION*/
opcommandline=function(input, event){

	this.value=input.val().replace(/^\s\s*/, '').replace(/\s\s*$/, '');		//value
	if(this.value==""){
		return;
	}
	var splited=this.value.split(" ");
	this.command=splited[0];
	this.kind=false;
	this.pargs=[];
	this.argssize=0;
	this.getFromDB=false;
	
	this.args=this.value.match(/[^\s"']+|"([^"]*)"|'([^']*)'/g).splice(1);
	this.argssize=this.args.length;
	
	if(this.command=='usuario'){
		if(event.which==13 && args.legnth>0){
			editShopman();
		}
	}
	else if(this.command=='alta'){
		if(event.which==13){
			registerProduct();
		}
	}
	else if(this.command=='alta'){
		this.kind="requestshopman";
		if(event.swhich==13);
	}
};
editShopman=function(){
	
};
registerProduct=function(){
	var form=$('#form').empty().inATable([
	                                      {
	                                    	  cantidad:"cantidad",
	                                    	  codigo:"codigo",
	                                    	  marca:"marca",
	                                    	  descripcion:"descripcion",
	                                    	  unidad:"unidad",
	                                    	  precio:"$ compra",
	                                    	  ganancia:"% gaancia",
	                                    	  proveedor:"proveedor",
	                                    	  referencia:"referencia"
	                                      },
	                                      {
	                                    	  cantidad:"<input id='cantidad' size='5'>",
	                                    	  codigo:"<input id='codigo' size='15'>",
	                                    	  marca:"<input id='marca' size='10'>",
	                                    	  descripcion:"<input id='descripcion' size='50'>",
	                                    	  unidad:"<input id='unidad' size='5'>",
	                                    	  precio:"<input id='compra' size='6'>",
	                                    	  ganancia:"<input id='gancia' size='3'>",
	                                    	  proveedor:"<input id='proveedor' size='15'>",
	                                    	  referencia:"<input id='referencia' size='10'>"
	                                      }

	                                      ]);
	form.oldCode='';
	form.index=-1;
	form.find('#cantidad').focus();
	form.find('#codigo').keyup(function(event){
		form.caret=form.find('#codigo').caret().start;
		//event.preventDefault();

		var args=form.find('#codigo').val().replace(/^\s\s*/, '').replace(/\s\s*$/, '');
		console.log(args);
		console.log(event);
		if(event.which==40){
			var idx1=form.index;
			form.index=(form.index+1)>=form.data.length?0:form.index+1;
			var idx2=form.index;
			form.menu.find('tr').eq(idx1+1).css('font-weight','normal');
			form.menu.find('tr').eq(idx2+1).css('font-weight','bold');
			console.log(idx1+' down '+idx2);
			console.log(form.data.length);
			console.log(form.data);
			event.preventDefault();
			return;
		}
		else if(event.which==38){
			var idx1=form.index;
			form.index=(form.index-1)<0?form.data.length-1:form.index-1;
			var idx2=form.index;
			form.menu.find('tr').eq(idx1+1).css('font-weight','normal');
			form.menu.find('tr').eq(idx2+1).css('font-weight','bold');
			console.log(idx1+' up '+idx2);
			event.preventDefault();
			return;
		}
		if(event.which==13){
			console.log(form.index);
			var i=form.index;
			if(i>=0){
				var d=form.data[i];

				form.find('#codigo').val(d.code);
				form.find('#marca').val(d.mark);
				form.find('#descripcion').val(d.description);
				form.find('#unidad').val(d.unit);
				form.find('#compra').focus();
				$('#menu').empty();

			}
			return;
		}
		if(form.oldCode!=args){
			console.log(form.oldCode+"->"+(args));
			form.oldCode=args;

		}
		else return;
		REQUEST_NUMBER++;
		$('#menu').empty();
		if(!(args.length>0))return;
		$.ajax({
			url: CONTEXT_PATH+"/port",
			dataType: "json",
			type:'POST',
			data: {
				search: encodeURIComponent(args),
				requestNumber: REQUEST_NUMBER,
				clientReference: CLIENT_REFERENCE,
				commandkind: 'product',
				consummerType: 1,
				token : TOKEN
			},
			success: function(data) {
				if(REQUEST_NUMBER!=data.requestNumber){
					//console.log("distintos id's");
					return;
				}
				form.data=data.products;
				if(form.data.length==0){
					return;
				}
				form.dats=[{codigo:"codigo", descripcion:"descripcion", marca:"marca"}];
				var p=data.products;
				for(var i=0;i<p.length;i++){
					var row={code:p[i].code,description:p[i].description,mark:p[i].mark};
					form.dats.push(row);
				}
				//console.log(dats);
				form.menu=$('#menu').inATable(form.dats);
				form.index=p.length>0?0:-1;
				form.menu.find('tr').eq(1).css('font-weight','bold');
			}
		});
	});
};
