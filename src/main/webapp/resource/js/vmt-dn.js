var DN= function(dn){
	this.dnArray=dn.split(",");
	this.dn=dn;
	var self=this;
	this.dept=function(index){
		var result='';
		for(var i=index-1;i>=0;i--){
			result+=this.suffix(i)+","; 
		}
		return result.substring(0, result.length-1);
	};
	this.suffix=function(index){
		return self.dnArray[self.dnArray.length-index-1];
	};
	this.prefix=function(index){
		return self.dnArray[index];
	};
	this.getValue=function(attribute){
		return $.trim(attribute.split("=")[1]);
	};
	this.getValues=function(split){
		var result='';
		for(var i=this.dnArray.length-2;i>0;i--){
			result+=(this.getValue(this.dnArray[i])+(i>1?split:''));
		}
		return result;
	};
	this.parent=function(){
		return dn.substr(dn.indexOf(',')+1);
	};
};