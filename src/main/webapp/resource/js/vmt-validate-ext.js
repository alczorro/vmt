jQuery.validator.addMethod("coreMailStart",function(value,element,params){
	return coreMailStartFunc(value);
},'必须以数字,小写字母,下划线开头');
jQuery.validator.addMethod("coreMailUserName",function(value,element,params){
	return coreMailUserName(value);
},'只允许输入数字,小写字母,下划线,中划线,点');
function coreMailStartFunc(value){
	var regix=/^[0-9a-z\_]{1}.*$/;
	return regix.test(value);
}
function coreMailUserName(value){
	var regix=/^[0-9a-z\_]{1}[0-9a-z\_\.\-]{0,19}$/;
	return regix.test(value);
}
function coreMailEmailFunc(value){
	var regix=/^[0-9a-z\_]{1}[0-9a-z\_\.\-]{0,19}@[a-z0-9A-Z\.\_\-]{2,30}$/;
	return regix.test(value);
}
/**
 * 不允许输入敏感字符
 * */


jQuery.validator.addMethod("specialLetter",  //addMethod第1个参数:方法名称
		function(value, element, params) {     //addMethod第2个参数:验证方法，参数（被验证元素的值，被验证元素，参数）
	return hasSpecial(value);
 },'请不要输入特殊字符');
jQuery.validator.addMethod("noZh",  //addMethod第1个参数:方法名称
		function(value, element, params) {     //addMethod第2个参数:验证方法，参数（被验证元素的值，被验证元素，参数）
	return !hasZh(value);
 },'请不要输入中文');
jQuery.validator.addMethod("passwordNotEquals",  //addMethod第1个参数:方法名称
function(value, element, params) {     //addMethod第2个参数:验证方法，参数（被验证元素的值，被验证元素，参数）
			if(value==''||params.notEquals()==''){
				return true;
			}
			return value!=params.notEquals();
 },'密码不能与用户名相同');

jQuery.validator.addMethod("passwordAllSmall",  //addMethod第1个参数:方法名称
		function(value, element, params) {     //addMethod第2个参数:验证方法，参数（被验证元素的值，被验证元素，参数）
		if(params==null){
			return true;
		}
	    return !passWordAllSmall(value)&&params;
 },'密码不能全为小写字母');

jQuery.validator.addMethod("nodeIdOK",  //addMethod第1个参数:方法名称
		function(value, element, params) {     //addMethod第2个参数:验证方法，参数（被验证元素的值，被验证元素，参数）
	    return nodeIdOK(value);
 },'请不要输入特殊字符');


jQuery.validator.addMethod("passwordAllNum",  //addMethod第1个参数:方法名称
		function(value, element, params) {     //addMethod第2个参数:验证方法，参数（被验证元素的值，被验证元素，参数）
		if(params==null){
			return true;
		}
	    return !passWordAllNum(value)&&params;
 },'密码不能全为数字');

jQuery.validator.addMethod("passwordAllBig",  //addMethod第1个参数:方法名称
		function(value, element, params) {     //addMethod第2个参数:验证方法，参数（被验证元素的值，被验证元素，参数）
		if(params==null){
			return true;
		}
	    return !passWordAllBig(value)&&params;
 },'密码不能全为大写字母');

jQuery.validator.addMethod("passwordHasSpace",  //addMethod第1个参数:方法名称
		function(value, element, params) {     //addMethod第2个参数:验证方法，参数（被验证元素的值，被验证元素，参数）
		if(params==null){
			return true;
		}
	    return !passWordHasSpace(value)&&params;
 },'密码不能有空格');

jQuery.validator.addMethod("numOrLetter",  //addMethod第1个参数:方法名称
		function(value, element, params) {     //addMethod第2个参数:验证方法，参数（被验证元素的值，被验证元素，参数）
		if(params==null){
			return true;
		}
	    return numOrLetter(value)&&params;
 },'只允许输入英文和数字');

jQuery.validator.addMethod("isYYYYMMDD",  //addMethod第1个参数:方法名称
		function(value, element, params) {     //addMethod第2个参数:验证方法，参数（被验证元素的值，被验证元素，参数）
		if(params==null){
			return true;
		}
	    return isYyyyMMddDate(value);
 },'日期格式不合法（yyyy-MM-dd）');


jQuery.validator.addMethod("doMySelf",  //addMethod第1个参数:方法名称
		function(value, element, params) {     //addMethod第2个参数:验证方法，参数（被验证元素的值，被验证元素，参数）
	return params.call(value);
 },'Do it Your Self');


/**
 * 验证用户是否验证太弱了
 * @param value 密码
 * @param notEquals 不应该与此相同
 * */
function isPassWeak(value,notEquals){
	return passWordAllSmall(value)||passWordAllNum(value)||passWordAllBig(value)||notEquals==value||regixHasSpace.test(value);
}
/**
 * 验证密码是否都是小写字母组成
 * @param value
 * @return boolean
 * */
function passWordAllSmall(value){
	var regixAllSmall=/^[a-z]+$/;
	return regixAllSmall.test(value);
}
function numOrLetter(value){
	var regix=/^[a-z0-9A-Z]+$/;
	return regix.test(value);
	
}
/**
 * 验证密码是否都是数字组成
 * @param value
 * @return boolean
 * */
function passWordAllNum(value){
	var regixAllNum=/^[0-9]+$/;
	return regixAllNum.test(value);
}

/**
 * 验证密码是否都是大写字母组成
 * @param value
 * @return boolean
 * */
function passWordAllBig(value){
	var regixAllBig=/^[A-Z]+$/;
	return regixAllBig.test(value);
}
/**
 * 验证密码是否有空格
 * */
function passWordHasSpace(value){
	var regixAllBig=/ /;
	return regixAllBig.test(value);
}

function hasSpecial(value){
	var regix=/^\s*[A-Za-z0-9\-]+\s*$/;
	return regix.test(value);
}
function hasZh(value){
	var regix=/[^\x00-\xff]/ig;
	return regix.test(value);
}


function isYyyyMMddDate(value){
        if(!value){
        	return true;
        }
	     var date = value;
         var result = date.match(/^(\d{4})(-)(\d{2})\2(\d{2})$/);
         if (result == null){
        	 return false;
         }
         var d = new Date(result[1], result[3] - 1, result[4]);
         return (d.getFullYear() == result[1] && (d.getMonth() + 1) == result[3] && d.getDate() == result[4]);
}


function nodeIdOK(value){
	var regix=/^[a-zA-Z0-9\-\_\.\!\~\*\'()\u4e00-\u9fa5]{1,1023}$/;
	return regix.test(value);
}
function groupNameOK(value){
	return value!=null&&value.match(/[:\\\/<>*?|"]/)==null;
}
