function StringUtils(){
	this.deleteCharAt = function (str,i){
    var resultStr = str.substr(0,i-1) +str.substr(i+1);
	return resultStr;
}
}