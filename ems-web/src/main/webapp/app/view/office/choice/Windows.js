Ext.define('Ems.view.office.choice.Windows', {
    extend: 'Ext.window.Window',

    alias: 'widget.officechoicewindows',
    /* requires:['Ems.view.office.choice.Form'],*/
    requires: [
        'Ems.view.user.RequestToken.deleteToken.cmdReasonCancelToken',
        'Ems.view.office.OfficeName.AutoComplete'
    ],

    //title: 'انتخاب دفتر پیشخوان جایگزین',
//    title: 'ابطال توکن',
//    height: 215,
    width: 450,
    layout: 'fit',
    inProgressRequests: false,

    constructor: function (config) {
        this.callParent(arguments);
    },

    initComponent: function () {
        this.items = this.OnRegisterFormToken();
        this.dockedItems = [
            {
                xtype: 'toolbar',
                style: 'direction:ltr; border:0px;',
                bodyStyle: 'direction:ltr; border:0px;',
                dock: 'bottom',
                width: 160,
                items: [
                    {
                        action: 'btnOfficeRegisterDeliver',
                        id: 'btnOfficeRegisterDeliver',
                        text: 'ثبت',
                        xtype: 'button',
                        width: 70,
                        iconCls: 'windows-Save-icon'/* ,
                     margins: '5 0 0 0'*/
                        //style:'margin-right:10px; '
                    },
                    {
                        width: 70,
                        text: 'انصراف',
                        iconCls: 'windows-Cancel-icon',
                        // style:'margin-right:5px; ',
                        handler: function () {
                            this.up('window').close();
                        }
                    }
                ]

            }
        ];
        this.callParent(arguments);
    },
    OnRegisterFormToken: function () {
        return ({
            layout: 'column',
            border: false,
            bodyBorder: false,
            items: [this.OnRegisterDeleteFormToken_c1()]
        });
    },
    OnRegisterDeleteFormToken_c1: function () {
        var me = this;
        return({
            xtype: 'form',
            columnWidth: 1,
            bodyBorder: false,
            border: false,
            defaults: {
                margin: 10,
                labelWidth: 120
            },
            items: [
                {
                    xtype: 'officenameautocomplete',
                    fieldLabel: 'دفتر پیشخوان جایگزین',
                    id: EmsObjectName.userRequestTokenDeleteTokenForm.replacedEnrollmentOfficeId,
                    name: EmsObjectName.userRequestTokenDeleteTokenForm.replacedEnrollmentOfficeId,
                    hiddenName: 'replacedEnrollmentOfficeIdssssss',
//                    disabled: !this.inProgressRequests,
                    allowBlank: false,
                    listeners: {
                        focus: function (autocomplete, e) {
                            var params = {'superDepartmentID': me.parentId, 'officeType': me.officeType, 'officeId': me.officeID};

//                            if (me.officeType != 'NOCR') {
//                                Ext.apply({'officeId': me.officeID }, params);
//                            }
                            this.getStore().readParams.additionalParams = Ext.JSON.encode(params);
                        }
                    }
                },
//                {
//                    xtype: 'userrequesttokencmdreasoncanceltoken',
//                    id: EmsObjectName.userRequestTokenDeleteTokenForm.reasonDelete,
//                    name: EmsObjectName.userRequestTokenDeleteTokenForm.reasonDelete,
//                    fieldLabel: 'دلیل ابطال',
//                    allowBlank: false
//                },
//                {
//                    xtype: 'textarea',
//                    //labelWidth:75,
//                    fieldLabel: 'توضیحات',
//                    anchor: '-5',
//                    name: EmsObjectName.userRequestTokenDeleteTokenForm.descriptionDeleteToken,
//                    id: EmsObjectName.userRequestTokenDeleteTokenForm.descriptionDeleteToken
//                }
            ]
        });
    }
    /*OnItemsChoic:function(){

     var officeNam_tt=new Ext.form.ComboBox({
     fieldLabel:'نام دفتر',
     padding:5,
     anchor:'-10',
     store: new Ext.data.SimpleStore({
     fields: ['value', 'name'],
     data: [['1', 'دفتر 1'], ['2', 'دفتر 2'], ['3', 'دفتر 3']]
     }),
     name: 'officeName_t',
     hiddenName: 'province_id',
     hiddenValue : '1',
     displayField: 'name',
     valueField: 'value',
     mode: 'local'
     });



     return officeNam_tt;

     } ,*/

});//.show();
