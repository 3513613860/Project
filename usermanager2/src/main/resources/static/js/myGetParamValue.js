function getParamValue(urlkey) {
    var param = location.search;  //?k1=vl&k2=v2
    param = param.substring(1);   //k1=v1&k2=v2
    var paramArr = param.split("&"); //k1=vl   k2=v2
    for(var i = 0; i < paramArr.length; i++){
        var item = paramArr[i].split("=");
        if(item[0] == urlkey){
            return item[1];
        }
    }
    return null;
}