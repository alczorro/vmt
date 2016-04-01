var VmtDialog =function(baseUrl){
	this.baseUrl=baseUrl;
	var self=this;
	$(".vmt-dialog").live("click",function(){
		self.loadDialog.apply(this);
	});
	this.loadDialog=function(){
		if('false'==$(this).attr("default")){
			return;
		};
		var tmplId=$(this).attr("tmplId");
		var init=$(this).attr("init");
		loadTmpl(tmplId);
		$('#'+tmplId).modal("show");
		if(init!=null){
			eval(init);
		}
	};
	var loadTmpl=function(tmplId){
		var $mod=$('#'+tmplId);
		$.ajax({
			 url:self.baseUrl+"/resource/template/"+tmplId+".html?_="+Math.random(),
			 async:false,
			 type:'post',
			 dataType:'html',
			 success:function(xml){
				if($mod!=null){
					$mod.remove();
				}
				$('body').append(
						xml.replace(/{{id}}/gm,tmplId)
							  .replace(/{{baseUrl}}/gm,self.baseUrl));
				$mod=$('#'+tmplId);
				$mod.on('hidden',function(){
					$mod.remove();
				});
			 }
		});
	};
	
};