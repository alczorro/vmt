var KeyMultiPress=function(option){
			var ctrlPressed=false;
			var shiftPressed=false;
			var shiftFirstEmail=-1;
			var shiftSecondEmail=-1;
			var self=this;
			var singleClick=function(){
				if(option.getCount()==1){
					self.clearAll();
				}
				if(!option.isSelected($(this))){
					option.addClass($(this));
				}
			};
			$(option.selector).die('click').live("click",function(){
				if(!option.multiable){
					singleClick.apply(this);
				}else if(shiftPressed){
					if(shiftFirstEmail==-1){
						shiftFirstEmail=$(this).attr("id");
						option.addClass($(this));
					}else{
						shiftSecondEmail=$(this).attr("id");
						var index=0;
						//clearAll();
						$(option.selector).each(function(i,n){
							
							if(shiftFirstEmail==shiftSecondEmail){
								return false;
							}
							if(shiftFirstEmail==$(n).attr("id")||shiftSecondEmail==$(n).attr("id")){
									index++;	
							}
							if(index>0){
								if(!option.isSelected($(this))){
									option.addClass($(n));
								};
							}
							if(index==2){
								return false;
							}
						});
						shiftFirstEmail=-1;
					}
				}else{
					shiftFirstEmail=$(this).attr("id");
					if(!ctrlPressed){
						singleClick.apply(this);
					}else{
						if(!option.isSelected($(this))){
							option.addClass($(this));
						}else{
							option.removeClass($(this));
						}
					}
				}
				option.complete();
			});
			this.clearAll=function(){
				$(option.selector).each(function(i,n){
					option.removeClass($(n));
				});
			};
			this.remove=function(id){
				option.removeClass($('#'+id));
			};
			$(document).keydown(function(event){
				switch(event.keyCode){
					//ctrl
					case 17:{
						ctrlPressed=true;
						break;
					}
					//shift
					case 16:{
						shiftPressed=true;
						break;
					}
				}
			});
			$(document).keyup(function(event){
				switch(event.keyCode){
					//ctrl
					case 17:{
						ctrlPressed=false;break;
					}
					//shift
					case 16:{
						shiftPressed=false;
						break;
					}
				}
			});
};