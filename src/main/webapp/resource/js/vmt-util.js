window.vmt={};
$.fn.extend({ 
	enter: function (callBack) {
	    $(this).keydown(function(event){
	    	if(event.keyCode=='13'){
	    		callBack.apply(event.currentTarget,arguments);
	    		event.preventDefault();
				event.stopPropagation();
	    	}
	    });
	},
	formToJson:function(){  
	    var inputs=$(this).find("input,textarea,select");  
	    var o = {};  
	    $.each(inputs,function(i,n){  
	        switch(n.nodeName.toUpperCase()){  
	            case "INPUT":  
	                if($(n).is(":checkbox")){  
	                    if($(n).is(":checked")){  
	                        o[n.name]=true;  
	                    }else{  
	                        o[n.name]=false;  
	                    }  
	                }else if($(n).is(":radio")){  
	                    if($(n).is(":checked")){  
	                        o[n.name]=n.value;  
	                    }  
	                }else{  
	                    o[n.name]=n.value;   
	                }  

	                break;  
	            case "TEXTAREA":  
	                o[n.name]=$(n).text();  
	                break;  
	            case "SELECT":  
	                o[n.name]=n.value;  
	                break;  
	        }  
	    });  
	    return o;  
	}  
});
/**全选控件
 * @param allId 全选的id
 * @param for出来的item的class
 * */
vmt.checkAllBox=function(allId,itemClass){
	var $all=$('#'+allId);
	$all.live('click',function(){
		var checked=$all.attr("checked");
		$('.'+itemClass).each(function(i,n){
			if(checked){
				$(n).attr("checked","checked");
			}else{
				$(n).removeAttr("checked");
			}
		});
	});
	$('.'+itemClass).live('click',function(){
		var allSelect=true;
		$('.'+itemClass).each(function(i,n){
			if($(n).attr("checked")){
				allSelect&=true;
			}else{
				allSelect&=false;	
			}
		});
		if(allSelect){
			$all.attr("checked","checked");
		}else{
			$all.removeAttr("checked");
		}
	});
	
};

function getTodayStr(){
	var dat=new Date();
	var mon=(dat.getMonth()+1);
	var day=dat.getDate();
	
	var today=dat.getYear()+1900+'-'+(mon<10?'0':'')+mon+'-'+(day<10?'0':'')+day;
	return today;
}


var Util={
		emailRegix:/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)*([a-zA-Z0-9])+$/,
		isEmail:function(email){
			return this.emailRegix.test(email);
		},
	    getEmailName:function(email){
	    	return email.substr(0,email.indexOf("@"));
	    }
};
Date.prototype.formatVmtDate = function(format){ 
	var o = { 
	"M+" : this.getMonth()+1, //month 
	"d+" : this.getDate(), //day 
	"h+" : this.getHours(), //hour 
	"m+" : this.getMinutes(), //minute 
	"s+" : this.getSeconds(), //second 
	"q+" : Math.floor((this.getMonth()+3)/3), //quarter 
	"S" : this.getMilliseconds() //millisecond 
	} ;

	if(/(y+)/.test(format)) { 
	format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
	} 

	for(var k in o) { 
	if(new RegExp("("+ k +")").test(format)) { 
	format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length)); 
	} 
	} 
	return format; 
	} ;

var Map=function(){
	{
		this.data={};
		this.count=0;
		this.put=function(key,value){
			if(!this.isExists(key)){
				this.count++;
			}
			this.data[key]=value;
		};
		this.remove=function(key){
			if(!this.isExists(key)){
				return;
			}
			if(this.count!=0){
				this.count--;	
			}
			this.data[key]=null;
		};
		this.get=function(key){
			return this.data[key];
		};
		this.removeAll=function(){
			this.data={};
			this.count=0;
		};
		this.isExists=function(key){
			return this.get(key)!=null;
		};
		this.asArray=function(){
			var result=[];
			for(var key in this.data){
				if(this.isExists(key)){
					result.push(this.data[key]);
				}
			}
			return result;
		};
		this.isExistsAttrEquals=function(attrName,attrValue){
			var result=false;
			$(this.asArray()).each(function(i,n){
				if(n[attrName]==attrValue){
					result=true;
					return false;
				}
			});
			return result;
		};
		
}
	
};