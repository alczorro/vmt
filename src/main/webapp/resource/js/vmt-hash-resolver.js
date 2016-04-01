$(document).ready(function(){
	var hash=window.location.hash;
	doCommand(hash);
	function doCommand(hash){
			if(hash==''){
				hash=request('hash');
			}
			if(hash){
				hash=hash.replace('#','');
				if(vmt.doHash&&typeof vmt.doHash=='function'){
					vmt.doHash(hash);
					return;
				}
				switch(hash){
					case 'createGroup':{
						$('#createGroupBtn').click();
					}
			}
		}
	}
	function request(paras) {  
        var url = location.href;  
        var paraString = url.substring(url.indexOf("?") + 1, url.length).split("&");  
        var paraObj = {} ;
        for (var i = 0;i<paraString.length; i++) {
          var j = paraString[i];
            paraObj[j.substring(0, j.indexOf("=")).toLowerCase()] = j.substring(j.indexOf("=") + 1, j.length);  
        }  
        var returnValue = paraObj[paras.toLowerCase()];  
        if (typeof (returnValue) == "undefined") {  
            return "";  
        } else {  
            return returnValue;  
        }  
    }  
});