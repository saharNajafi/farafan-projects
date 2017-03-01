/**
 * Created by moghaddam on 6/17/14.
 *
 * The fieldset object containing card request information (like request type, state, etc.)
 */
Ext.define('Ems.view.cardRequestList.fieldset.CardRequestInfo', {
    extend: 'Gam.form.FieldSet',
    alias: 'widget.cardrequestlistfieldsetcardrequestinfo',

    id: 'cardRequestInfoFieldSetID',

    title: 'اطلاعات درخواست',

    layout: 'column',

    initComponent: function () {

        this.defaults = {
            columnWidth: 1 / 3,
            labelWidth: 150
        };

        this.callParent(arguments);
    },

    getReadOnlyFields: function () {
        return this.getItems();
    },

    getEditableFields: function () {
        return this.getItems();
    },

    getItems: function () {
        return[
            {
                fieldLabel: 'نوع کارت',
                name: EmsObjectName.cardRequestList.cardType,
                renderer: function (value) {
                    if (value && typeof value === 'string') {
                        var resualt = eval("EmsResource.cardRequestList.CardRequestType." + value);
                        return resualt != null ? resualt : value;
                    }
                }
            },
            {
                fieldLabel: 'نحوه ثبت نام',
                name: EmsObjectName.cardRequestList.requestOrigin,
                renderer: function (value) {
                    var result = "";
                    if (value == "P") {
                        result = "پورتال";
                    } else if (value == "C") {
                        result = "دفتر پیشخوان";
                    } else if (value == "M") {
                        result = "ثبت نام سیار";
                    }

                    return result;
                }
            },
            {
                fieldLabel: 'وضعیت درخواست',
                name: EmsObjectName.cardRequestList.cardRequestState,
                renderer: function(value){
                    if (value && typeof value === 'string') {
                        var resualt = eval("EmsResource.cardRequestList.CardRequestState." + value);
                        return resualt != null ? resualt : value
                    }
                }
            },
            {
                fieldLabel: 'دفتر ثبت نام',
                name: EmsObjectName.cardRequestList.enrollmentOfficeName
            },
            {
                fieldLabel: 'دفتر پیشخوان تحویل کارت',
                name: EmsObjectName.cardRequestList.deliveredOfficeName
            },
            {
                fieldLabel: 'وضعیت کارت',
                name: EmsObjectName.cardRequestList.cardState,
                renderer: function (value) {
                    if (value && typeof value === 'string') {
                        return eval("EmsResource.cardRequestList.CardState." + value);
                    }

                    return value;
                }
            },
            {
                fieldLabel: 'تاریخ پیش ثبت نام',
                name: EmsObjectName.cardRequestList.portalEnrolledDate,
                renderer: Gam.util.Format.dateRenderer('Y/m/d')
            },
            {
                fieldLabel: 'تاریخ نوبت گیری',
                name: EmsObjectName.cardRequestList.reservationDate,
                renderer: Gam.util.Format.dateRenderer('Y/m/d')
            },
            {
                fieldLabel: 'تاریخ تکمیل پیش ثبت نام',
                name: EmsObjectName.cardRequestList.enrolledDate,
                renderer: Gam.util.Format.dateRenderer('Y/m/d')
            },
            {
                fieldLabel: 'کد رهگیری',
                name: EmsObjectName.cardRequestList.trackingId
            }
        ]
    }
});
