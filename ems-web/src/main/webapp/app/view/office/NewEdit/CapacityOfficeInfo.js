/**
 * Created with IntelliJ IDEA.
 * User: Navid
 * Date: 05/21/2018
 * Time: 10:35 AM
 */
Ext.define('Ems.view.office.NewEdit.CapacityOfficeInfo', {
    extend: 'Gam.form.FieldSet',
    alias: 'widget.officeneweditcapacityofficeinfo',

    id: 'idOfficeNewEditCapacityOfficeInfo',

    title: 'اطلاعات  ',
    height: 255,
    margin: 10,
    frame: false,
    layout: 'column',
    initComponent: function () {

        var me = this;

        me.defaults = {
            columnWidth: 1 / 2,
            labelWidth: 150
        };

        me.callParent(arguments);
    },

    // getReadOnlyFields: function () {
    //     return [
    //         {
    //             fieldLabel: 'کد پستی',
    //             anchor: '-10',
    //             id: 'postCode'
    //         },
    //         {
    //             fieldLabel: 'آدرس کامل دفتر',
    //             anchor: '-10',
    //             id: 'address'
    //         }
    //     ];
    // },

    getEditableFields: function () {
        return [
            {
                xtype: 'displayfield',
                fieldLabel: 'کد دفتر',
                itemId: 'code'
            },
            {
                xtype: 'displayfield',
                fieldLabel: 'نام دفتر',
                itemId: 'name'
            },
            {
                xtype: 'displayfield',
                fieldLabel: 'فکس',
                itemId: 'fax'
            },
            {
                xtype: 'displayfield',
                fieldLabel: 'تلفن ثابت',
                itemId: 'phone'
            },
            {
                xtype: 'displayfield',
                fieldLabel: 'جایگاه سازمانی مافوق',
                itemId: 'parentName'
            },
            {
                xtype: 'displayfield',
                fieldLabel: 'شهر/روستا',
                itemId: 'locName'
            },
            {
                xtype: 'displayfield',
                fieldLabel: 'متراژ',
                itemId: 'area'
            },
            {
                xtype: 'displayfield',
                fieldLabel: 'رتبه',
                itemId: 'rate'
            },
            {
                xtype: 'displayfield',
                fieldLabel: 'شروع ساعت کاری',
                itemId: 'workingHoursStart'
              //  renderer: Ext.bind(this.formatTimeValue, this)
            },
            {
                xtype: 'displayfield',
                fieldLabel: 'پایان ساعت کاری',
                itemId: 'workingHoursFinish'
                //renderer: Ext.bind(this.formatTimeValue, this)
            },
            {
                xtype: 'displayfield',
                fieldLabel: 'نوع دفتر',
                itemId: 'khosusiType',
                renderer: function (value) {
                    if (value == "1") {
                        return "دفتر پیشخوان";
                    } else if (value == "0") {
                        return "اداره ثبت احوال";
                    } else if (value == "2") {
                        return "دفتر پست";
                    }
                }
            },
            {
                xtype: 'displayfield',
                fieldLabel: 'وضعیت تحویل کارت دفتر پیشخوان',
                itemId: 'officeDeliver',
                renderer: function (value) {
                    if (value == "1") {
                        return "قابلیت تحویل دارد";
                    } else if (value == "0") {
                        return "قابلیت تحویل ندارد";
                    }
                }
            },
            {
                xtype: 'displayfield',
                fieldLabel: 'دفتر معین',
                itemId: 'superiorOfficeId',
                renderer: function (value) {
                    if (!value) {
                        return "ندارد";
                    } else {
                        return value;
                    }
                }
            }
        ];
    }
});

