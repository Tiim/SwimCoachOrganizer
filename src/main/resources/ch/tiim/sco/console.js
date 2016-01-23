load("nashorn:mozilla_compat.js");


importPackage(
    java.util,
    java.time,
    java.io
);


var ch = Packages.ch
var sco = ch.tiim.sco
var model = sco.database.model

var func = function(obj) {
    return Packages.ch.tiim.sco.util.JSHelper.getFuctions(obj);
}