/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 10/22/12
 * Time: 10:32 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.apply(Ext.form.field.VTypes, {
    serialCertificateText: 'مقدار این فیلد معتبر نمی باشد',
    serialCertificate: function (value, field) {
        //
        var regexIdentitySerial = /^\d{6}$/i;
        var val = parseInt(value, 10);
        var regexVal = regexIdentitySerial.test(value)
        //
        var minNumber = 100001 ,
            maxNumber = 999999;

        if (((minNumber < val) && (val < maxNumber)) && regexVal) {
            return true;
        } else {
            return false;
        }
    }

}, {
    numericText: 'مقدار این فیلد معتبر نمی باشد',
    // numericMask:'',
    numericRe: /\d/i,
    numeric: function (value, field) {
        var regexIdentitySerial = /\d$/i;
        var val = parseInt(value, 10);
        if (regexIdentitySerial.test(value)) {
            return true
        } else {
            return false;
        }

    }
});
