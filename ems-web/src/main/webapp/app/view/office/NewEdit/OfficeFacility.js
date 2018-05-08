Ext.define('Ems.view.office.NewEdit.OfficeFacility', {
    extend: 'Gam.form.FieldSet',
    alias: 'widget.officeneweditofficeFacility',
    id: 'idofficeneweditofficeFacility',
    margin: 10,
    initComponent: function () {

        var me = this;
        me.callParent(arguments);
    },
    getReadOnlyFields: function () {
        return [
            {
                fieldLabel: 'تلفن مکان یابی',
                id: EmsObjectName.officeNewEdit.officeDepPhoneNumber
            },
            {
                xtype: 'checkboxgroup',
                columns: 4,
                items: [
             {
                 readOnly: true,
                 boxLabel: 'پنجشنبه صبح دایر',
                    id: EmsObjectName.officeNewEdit.thursdayMorningActive
                },
                    {
                        readOnly: true,
                        boxLabel: 'پنجشنبه عصر دایر',
                        id: EmsObjectName.officeNewEdit.thursdayEveningActive
                    },
                    {
                        readOnly: true,
                        boxLabel: 'جمعه صبح دایر',
                        id: EmsObjectName.officeNewEdit.fridayMorningActive
                    },
                    {
                        readOnly: true,
                        boxLabel: 'جمعه عصر دایر',
                        id: EmsObjectName.officeNewEdit.fridayEveningActive
                    }
                    ]},

            {
                xtype: 'checkboxgroup',
                columns: 4,
                items: [
                    {
                        readOnly: true,
                        boxLabel: 'قابل مشاهده در CRS',
                id: EmsObjectName.officeNewEdit.singleStageOnly
            },
            {
                readOnly: true,
                boxLabel: 'پله',
                id: EmsObjectName.officeNewEdit.hasStair
            },
            {
                readOnly: true,
                boxLabel: 'آسانسور',
                id: EmsObjectName.officeNewEdit.hasElevator
            },
            {
                readOnly: true,
                boxLabel: 'امکان ثبت نام سیار',
                id: EmsObjectName.officeNewEdit.hasPortabilityEquipment
            }
            ]}
                    

        ]
    },
    getEditableFields: function () {
        return [
            {
                xtype: 'textfield',
                fieldLabel: 'تلفن مکان یابی',
                anchor: '50%',
                id: EmsObjectName.officeNewEdit.officeDepPhoneNumber,
                name: EmsObjectName.officeNewEdit.officeDepPhoneNumber,
                maskRe: /\d/i,
                allowBlank: false,
                maxLength: 8,
                enforceMaxLength: 8
            },
            {
                xtype: 'checkboxgroup',
                columns: 4,
                allowBlank: true,
                items: [{
                    boxLabel: 'پنجشنبه صبح دایر',
                    id: EmsObjectName.officeNewEdit.thursdayMorningActive,
                    name: EmsObjectName.officeNewEdit.thursdayMorningActive
                }, {
                    boxLabel: 'پنجشنبه عصر دایر',
                    id: EmsObjectName.officeNewEdit.thursdayEveningActive,
                    name: EmsObjectName.officeNewEdit.thursdayEveningActive
                }, {
                    boxLabel: 'جمعه صبح دایر',
                    id: EmsObjectName.officeNewEdit.fridayMorningActive,
                    name: EmsObjectName.officeNewEdit.fridayMorningActive
                },{
                    boxLabel: 'جمعه عصر دایر',
                    id: EmsObjectName.officeNewEdit.fridayEveningActive,
                    name: EmsObjectName.officeNewEdit.fridayEveningActive
                }]
            },
            {
                xtype: 'checkboxgroup',
                columns: 4,
                allowBlank: true,
                items: [
                    {
                        boxLabel: 'قابل مشاهده در CRS',
                        id: EmsObjectName.officeNewEdit.singleStageOnly,
                        name: EmsObjectName.officeNewEdit.singleStageOnly
                    },
                    {
                        boxLabel: 'پله دارد',
                        id: EmsObjectName.officeNewEdit.hasStair,
                        name: EmsObjectName.officeNewEdit.hasStair
                    },
                    { boxLabel: 'آسانسور دارد',
                        id: EmsObjectName.officeNewEdit.hasElevator,
                        name: EmsObjectName.officeNewEdit.hasElevator
                    },
                    { boxLabel: 'امکان ثبت نام سیار دارد',
                        id: EmsObjectName.officeNewEdit.hasPortabilityEquipment,
                        name: EmsObjectName.officeNewEdit.hasPortabilityEquipment
                    }]
            }

        ];
    }
});