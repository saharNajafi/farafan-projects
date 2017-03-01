// this file is
function ajaxRequestGet(actionUrl, data, success, fail) {
	$.ajax({
        type: "GET",
        url: actionUrl,
        data: convertObjectToJSON(data),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (response) {
        	// if success do something
        },
        failure: function (msg) {
        	// if failure do something
        },
        always: function (msg) {
        },
        error: function (xhr, error, msg) {
        	// if error do something
        }

    });
}

function convertObjectToJSON(obj) {
	if (!obj)
        return "{}";
    var newObj = new Object();
    var allProperties = Object.keys(obj);
    for (var iProperty = 0; iProperty < allProperties.length; iProperty++) {
        var key = allProperties[iProperty];
        newObj[key] = obj[key];
    }
    return JSON.stringify(newObj);
}

function test(ver){
	alert(ver);
}

//function convertMiladiToJalali(date){
//	
//	var meDate = new Date(Date.parse(date));
//	var jalaiDate = Ext.JalaliDate.dateFormat(meDate,Ext.Date.defaultFormat);
//	return jalaiDate;
//}