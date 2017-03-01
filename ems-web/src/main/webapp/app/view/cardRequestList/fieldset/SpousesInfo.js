/**
 * Created by moghaddam on 6/22/14.
 *
 * The fieldset object containing information of citizen spouses (like name, NID, etc.)
 */
Ext.define('Ems.view.cardRequestList.fieldset.SpousesInfo', {
    extend: 'Gam.form.FieldSet',
    alias: 'widget.cardrequestlistfieldsetspousesinfo',
    name: "citizenParentsInfo",

    id: 'spousesInfoFieldSetID',

    title: 'اطلاعات همسر',

    layout: 'fit',

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
        return [
            Ext.create('Ext.grid.Panel', {
                store: Ext.data.StoreManager.lookup('spousesStore'),
                columns: [
                    {
                        text: 'نام',
                        dataIndex: EmsObjectName.cardRequestList.spouseFirstName,
                        sortable: false,
                        hideable: false
                    },
                    {
                        text: 'نام خانوادگی',
                        dataIndex: EmsObjectName.cardRequestList.spouseSureName,
                        sortable: false,
                        hideable: false
                    },
                    {
                        text: 'شماره ملی',
                        dataIndex: EmsObjectName.cardRequestList.spouseNID,
                        sortable: false,
                        hideable: false
                    },
                    {
                        text: 'وضعیت ازدواج',
                        dataIndex: EmsObjectName.cardRequestList.spouseMaritalStatus,
                        sortable: false,
                        hideable: false,
                        renderer: function(value){
                            if(value == 1){
                                return "پایدار";
                            } else if (value == 2){
                                return "متوفی";
                            } else if (value == 3){
                                return "مطلقه";
                            }
                        }
                    },
                    {
                        text: 'تاریخ ازدواج',
                        dataIndex: EmsObjectName.cardRequestList.spouseMarriageDate,
                        xtype: 'gam.datecolumn',
                        sortable: false,
                        hideable: false
                    },
                    {
                        text: 'تاریخ طلاق/فوت',
                        dataIndex: EmsObjectName.cardRequestList.spouseDeathDivorceDate,
                        xtype: 'gam.datecolumn',
                        sortable: false,
                        hideable: false
                    }
                ],
                width: '100%',
                height: 100
            })
        ]
    }
});
