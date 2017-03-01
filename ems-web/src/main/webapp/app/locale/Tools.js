/**
 * Created by IntelliJ IDEA.
 * User: Mousavi
 * Date: 5/21/12
 * Time: 2:11 PM
 * To change this template use File | Settings | File Templates.
 */
function Tools() {

    this.CountDispatch = null;

    this.user = {
        StyleObject: null,
        accesses: null,
        roles: null,
        Editing: 0

    };

    this.UserRequestToken = {
        personId: null,
        selectedRowIndex: null,
        thisWindow: null,
        selectionManagementTokenRowId: null,
        RegisterRequestToken: 0

    };


    this.docType = {
        containerNewWindow: 0,
        containerEditWindow: 0
    }


    this.defineUserAndDepartment = {
        logout: 0,
        changPassWord: 0,
        register: 0
    }

    this.NavigationContorller = {
        me: null
    }


}

Tools.prototype.trim = function (str) {
    if (str && Ext.isString(str))
        return str.replace(/^\s+|\s+$/g, "");
}


//Json to Object
Tools.prototype.toObject = function (jsonString) {

    return (eval('(' + jsonString + ')'));

}

//Object to Json
Tools.prototype.toJson = function (valueForm, InputRecord) {

    var jsonText = "[";

    if (InputRecord === true) {
        jsonText += Tools.toJsonRecord(valueForm);
    } else {
        jsonText += valueForm;
    }

    return jsonText + "]"
}

Tools.prototype.isNullStore = function (store) {
    // return false;
}

Tools.prototype.getJsonGrid = function (store, objectName) {
    var jsonText = "[";

    Ext.each(store.data.items, function (item) {
        if (jsonText.length >= 2)
            jsonText += ',';
        jsonText += Tools.toJsonRecord(item.data);

    });

    jsonText += "]";

    return "{\'" + objectName + "\' : " + jsonText + "}";
}

Tools.prototype.getJsonFrom = function (form) {

    var valueForm = form.getForm().getValues();

    return Tools.toJsonRecord(valueForm);
}

Tools.prototype.editValuesForm = function (form) {
    form = form.getForm();
    var valueForm = form.getValues(),
        currentRec = form.getRecord(),
        key;

    currentRec.beginEdit();

    for (key in valueForm)
        currentRec.set(key, valueForm[key])

    currentRec.endEdit();

    currentRec.commit();

    return currentRec;
}

Tools.prototype.changeRecordInGrid = function (grid, newReceod) {
    var id = null,
        dataStore = grid.getStore().data.items,
        key;

    try {
        //get record(of local)
        id = newReceod.get('id');

        Ext.each(dataStore, function (item) {
            if (item.get('id') === id)
                for (key in item.data)
                    item.set(key, newReceod.data[key]);
        });


    } catch (e) {
        try {
            //get array record(of server)
            id = newReceod.id;
            Ext.each(dataStore, function (item) {
                if (item.get('id') === id)
                    for (key in item.data)
                        item.set(key, newReceod[key]);
            });
        } catch (e2) {
            alert(e.message);
        }
    }
}


Tools.prototype.checkDataInStore = function (store, key, value) {

    var data = store.data.items,
        checking = false;

    Ext.each(data, function (item) {
        if (parseInt(item.get(key), 10) === parseInt(value, 10))
            checking = true;
    });
    return checking;
}

Tools.prototype.addToFirstRowGrid = function (grid, newRecord) {

}


Tools.prototype.getValue = function (valueForm, key) {

    try {

        var value = (valueForm.get(key));
        if (value != undefined) {
            return value;
        } else {
            return null;
        }

    } catch (e) {
        alert(e.message);
    }
}

//system
Tools.prototype.toJsonRecord = function (value) {

    var jsonText = "{",
        key;

    for (key in value) {

        if (jsonText.length <= 2)
            jsonText += "'" + key + "':'" + value[key] + "'";
        else
            jsonText += ",'" + key + "':'" + value[key] + "'";
    }

    return jsonText + "}"

}

Tools.prototype.MaskUnMask = function (windows) {
    Ext.getBody().mask();
    windows.on('close', function () {
        Ext.getBody().unmask();
    });
}

Tools.prototype.errorMessageServer = function (messageInfo) {

    Ext.Msg.show({
        title: 'هشدار',
        msg: Ext.String.format(eval(messageInfo.message), messageInfo.arguments[0]),
//        width: 300,
        buttons: Ext.Msg.OK,
        icon: Ext.Msg.ERROR//messageInfo.icon//
    });
    return false;
}

Tools.prototype.errorMessageClient = function (textShow) {
    Ext.Msg.show({
        title: 'اخطار',
        msg: textShow + '',
        buttons: Ext.Msg.OK,
        icon: 'Msg-Warning-icon'
    });

}

Tools.prototype.successMessage = function (textShow) {
    Ext.Msg.show({
        title: 'موفقیت',
        msg: textShow + '',
        buttons: Ext.Msg.OK,
        iconCls: 'x-notification-success'
    });

}

Tools.prototype.errorFailure = function () {
    this.errorMessageClient(Ems.ErrorCode.client.EMS_C_001);
//    Ext.Msg.show({
//        title:'اخطار',
//        msg: ,
//        buttons:Ext.Msg.OK,
//        icon:'Msg-Warning-icon'
//    });

}

Tools.prototype.messageBoxConfirm = function (textShow, fn) {
    Ext.MessageBox.buttonText = {
        ok: "تایید",
        cancel: "بازگشت",
        yes: "بله",
        no: "خیر"
    };
    Ext.MessageBox.confirm('توجه!', textShow, function (btn) {
        if (btn == 'yes') {
            fn();
        }
    });

    // return false;
}


Tools.prototype.setElementDir = function (view, dirText) {
    try {
        view.getEl().dom.children[0].children[0].firstChild.firstElementChild.control.dir = dirText;
    } catch (e) {
        try {
            //IE
            view.el.dom.children[0].children[view.id + '-inputRow'].children[view.id + '-bodyEl'].firstChild.dir = dirText;
        } catch (e) {
        }
    }
}


Tools.prototype.regexFarsiAlpha = function () {
    // return (/^[\u0622\u0627\u0628\u067e\u062A\u062b\u062c\u0686\u062d\u062e\u062f\u0630\u0631\u0632\u0698\u0633\u0634\u0635\u0636\u0637\u0638\u0630\u063a\u0641\u0642\u0643\u06af\u0644\u0645\u0646\u0648\u0647\u0649\u064A\s]+$/);   \u0626
    return (/^[\u0622\u0627\u0628\u067e\u062A\u062b\u062c\u0686\u062d\u062e\u062f\u0630\u0631\u0632\u0698\u0633\u0634\u0635\u0636\u0637\u0638\u0630\u063a\u0641\u0642\u0643\u06af\u0644\u0645\u0646\u0648\u0647\u0649\u064A\ufeef\ufef0\ufef1\ufef2\ufef3\ufef4\ufecb\ufecc\ufec9\u0649\u0639\u06cc\ufed9\ufeda\ufedb\ufedc\ufedb\ufb8e\ufb8f\ufb90\ufb91\u0643\u06a9\u0626\u0625\u0624\u0623\u0621\s]+$/);
}

Tools.prototype.regexFarsiAlphaAndNumber = function () {
    // return (/^[\u0622\u0627\u0628\u067e\u062A\u062b\u062c\u0686\u062d\u062e\u062f\u0630\u0631\u0632\u0698\u0633\u0634\u0635\u0636\u0637\u0638\u0630\u063a\u0641\u0642\u0643\u06af\u0644\u0645\u0646\u0648\u0647\u0649\u064A\s]+$/);
    return (/^[0-9\u0622\u0627\u0628\u067e\u062A\u062b\u062c\u0686\u062d\u062e\u062f\u0630\u0631\u0632\u0698\u0633\u0634\u0635\u0636\u0637\u0638\u0630\u063a\u0641\u0642\u0643\u06af\u0644\u0645\u0646\u0648\u0647\u0649\u064A\ufeef\ufef0\ufef1\ufef2\ufef3\ufef4\ufecb\ufecc\ufec9\u0626\u0649\u0639\u06cc\ufed9\ufeda\ufedb\ufedc\ufedb\ufb8e\ufb8f\ufb90\ufb91\u0643\u06a9\u0626\u0625\u0624\u0623\u0621\s]+$/);
}

Tools.prototype.regexPassword = function () {
    return (/^[a-zA-Z0-9\u0021-\u002f\u0040\u007e\005f\u005e\u02c6\s]+$/);
}


Tools.prototype.regexCellPhone = function () {
    //regexText:'این فیلد باید یک شماره 11 رقمی در قالب 09xxxxxxxxx باشد' ,
    return (/^[0]+[9]+[0-9]+[0-9]+[0-9]+[0-9]+[0-9]+[0-9]+[0-9]+[0-9]+[0-9]$/);
}

Tools.prototype.regexEmail = function () {
    return (/^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/);
}

Tools.prototype.deleteAllCookies = function () {
    var cookies = document.cookie.split(";");

    for (var i = 0; i < cookies.length; i++) {
        var cookie = cookies[i];
        var eqPos = cookie.indexOf("=");
        var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
        document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
    }
}


Tools.prototype.keyboardShortcuts = function () {
    /**
     * http://www.openjs.com/scripts/events/keyboard_shortcuts/
     * Version : 2.01.B
     * By Binny V A
     * License : BSD
     */
    shortcut = {
        'all_shortcuts': {},//All the shortcuts are stored in this array
        'add': function (shortcut_combination, callback, opt) {
            //Provide a set of default options
            var default_options = {
                'type': 'keydown',
                'propagate': false,
                'disable_in_input': false,
                'target': document,
                'keycode': false
            }
            if (!opt) opt = default_options;
            else {
                for (var dfo in default_options) {
                    if (typeof opt[dfo] == 'undefined') opt[dfo] = default_options[dfo];
                }
            }

            var ele = opt.target;
            if (typeof opt.target == 'string') ele = document.getElementById(opt.target);
            var ths = this;
            shortcut_combination = shortcut_combination.toLowerCase();

            //The function to be called at keypress
            var func = function (e) {
                e = e || window.event;

                if (opt['disable_in_input']) { //Don't enable shortcut keys in Input, Textarea fields
                    var element;
                    if (e.target) element = e.target;
                    else if (e.srcElement) element = e.srcElement;
                    if (element.nodeType == 3) element = element.parentNode;

                    if (element.tagName == 'INPUT' || element.tagName == 'TEXTAREA') return;
                }

                //Find Which key is pressed
                if (e.keyCode) code = e.keyCode;
                else if (e.which) code = e.which;
                var character = String.fromCharCode(code).toLowerCase();

                if (code == 188) character = ","; //If the user presses , when the type is onkeydown
                if (code == 190) character = "."; //If the user presses , when the type is onkeydown

                var keys = shortcut_combination.split("+");
                //Key Pressed - counts the number of valid keypresses - if it is same as the number of keys, the shortcut function is invoked
                var kp = 0;

                //Work around for stupid Shift key bug created by using lowercase - as a result the shift+num combination was broken
                var shift_nums = {
                    "`": "~",
                    "1": "!",
                    "2": "@",
                    "3": "#",
                    "4": "$",
                    "5": "%",
                    "6": "^",
                    "7": "&",
                    "8": "*",
                    "9": "(",
                    "0": ")",
                    "-": "_",
                    "=": "+",
                    ";": ":",
                    "'": "\"",
                    ",": "<",
                    ".": ">",
                    "/": "?",
                    "\\": "|"
                }
                //Special Keys - and their codes
                var special_keys = {
                    'esc': 27,
                    'escape': 27,
                    'tab': 9,
                    'space': 32,
                    'return': 13,
                    'enter': 13,
                    'backspace': 8,

                    'scrolllock': 145,
                    'scroll_lock': 145,
                    'scroll': 145,
                    'capslock': 20,
                    'caps_lock': 20,
                    'caps': 20,
                    'numlock': 144,
                    'num_lock': 144,
                    'num': 144,

                    'pause': 19,
                    'break': 19,

                    'insert': 45,
                    'home': 36,
                    'delete': 46,
                    'end': 35,

                    'pageup': 33,
                    'page_up': 33,
                    'pu': 33,

                    'pagedown': 34,
                    'page_down': 34,
                    'pd': 34,

                    'left': 37,
                    'up': 38,
                    'right': 39,
                    'down': 40,

                    'f1': 112,
                    'f2': 113,
                    'f3': 114,
                    'f4': 115,
                    'f5': 116,
                    'f6': 117,
                    'f7': 118,
                    'f8': 119,
                    'f9': 120,
                    'f10': 121,
                    'f11': 122,
                    'f12': 123
                }

                var modifiers = {
                    shift: { wanted: false, pressed: false},
                    ctrl: { wanted: false, pressed: false},
                    alt: { wanted: false, pressed: false},
                    meta: { wanted: false, pressed: false}	//Meta is Mac specific
                };

                if (e.ctrlKey)    modifiers.ctrl.pressed = true;
                if (e.shiftKey)    modifiers.shift.pressed = true;
                if (e.altKey)    modifiers.alt.pressed = true;
                if (e.metaKey)   modifiers.meta.pressed = true;

                for (var i = 0; k = keys[i], i < keys.length; i++) {
                    //Modifiers
                    if (k == 'ctrl' || k == 'control') {
                        kp++;
                        modifiers.ctrl.wanted = true;

                    } else if (k == 'shift') {
                        kp++;
                        modifiers.shift.wanted = true;

                    } else if (k == 'alt') {
                        kp++;
                        modifiers.alt.wanted = true;
                    } else if (k == 'meta') {
                        kp++;
                        modifiers.meta.wanted = true;
                    } else if (k.length > 1) { //If it is a special key
                        if (special_keys[k] == code) kp++;

                    } else if (opt['keycode']) {
                        if (opt['keycode'] == code) kp++;

                    } else { //The special keys did not match
                        if (character == k) kp++;
                        else {
                            if (shift_nums[character] && e.shiftKey) { //Stupid Shift key bug created by using lowercase
                                character = shift_nums[character];
                                if (character == k) kp++;
                            }
                        }
                    }
                }

                if (kp == keys.length &&
                    modifiers.ctrl.pressed == modifiers.ctrl.wanted &&
                    modifiers.shift.pressed == modifiers.shift.wanted &&
                    modifiers.alt.pressed == modifiers.alt.wanted &&
                    modifiers.meta.pressed == modifiers.meta.wanted) {
                    callback(e);

                    if (!opt['propagate']) { //Stop the event
                        //e.cancelBubble is supported by IE - this will kill the bubbling process.
                        e.cancelBubble = true;
                        e.returnValue = false;

                        //e.stopPropagation works in Firefox.
                        if (e.stopPropagation) {
                            e.stopPropagation();
                            e.preventDefault();
                        }
                        return false;
                    }
                }
            }
            this.all_shortcuts[shortcut_combination] = {
                'callback': func,
                'target': ele,
                'event': opt['type']
            };
            //Attach the function with the event
            if (ele.addEventListener) ele.addEventListener(opt['type'], func, false);
            else if (ele.attachEvent) ele.attachEvent('on' + opt['type'], func);
            else ele['on' + opt['type']] = func;
        },

        //Remove the shortcut - just specify the shortcut and I will remove the binding
        'remove': function (shortcut_combination) {
            shortcut_combination = shortcut_combination.toLowerCase();
            var binding = this.all_shortcuts[shortcut_combination];
            delete(this.all_shortcuts[shortcut_combination])
            if (!binding) return;
            var type = binding['event'];
            var ele = binding['target'];
            var callback = binding['callback'];

            if (ele.detachEvent) ele.detachEvent('on' + type, callback);
            else if (ele.removeEventListener) ele.removeEventListener(type, callback, false);
            else ele['on' + type] = false;
        }
    }
}


var Tools = new Tools();
Tools.keyboardShortcuts();

//Gam.app.controller.FormBasedCrud


/*
 4376
 format: function(date, format)
 {

 if(jUtilDate.formatFunctions[format] == null)
 {
 jUtilDate.createFormat(format);
 }
 var result = jUtilDate.formatFunctions[format].call(date)+'';

 if(format==="Y/m/d-H:m:s"){

 date = date+'';
 var MarkDate = date.indexOf(":"),
 DateTime_t,
 MarkResult = result.indexOf("-");

 if(MarkDate>-1){
 DateTime_t = date.substring(MarkDate-2,MarkDate+6);
 }

 if(MarkResult>-1){
 result=result.substring(0,MarkResult+1)+DateTime_t;
 }
 }
 return result + '';
 },
 */






