load("nashorn:mozilla_compat.js");


importPackage(
    java.util,
    java.time,
    java.io,
    java.lang,
    java.nio,
    java.nio.file
);


var ch = Packages.ch
var sco = ch.tiim.sco
var model = sco.database.model
var exp = sco.database.export

var func = function(obj) {
    var str = '';
    Packages.ch.tiim.sco.util.JSHelper.getFuctions(obj).forEach(function(it) {
        str = str.concat(it).concat("\n");
    });
    return str;
}

var obj = function(o) {
    var str = ''
    Packages.ch.tiim.sco.util.JSHelper.objInfo(o).forEach(function(it) {
        str = str.concat(it).concat("\n");
    })
    return str;
}

var path = function(string) {
    return Paths.get(string);
}
