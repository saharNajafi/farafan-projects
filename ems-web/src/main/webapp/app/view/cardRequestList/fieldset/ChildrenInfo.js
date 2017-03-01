/**
 * Created by moghaddam on 6/25/14.
 *
 * The fieldset object containing information of citizen spouses (like name, NID, etc.)
 */
Ext.define('Ems.view.cardRequestList.fieldset.ChildrenInfo', {
    extend: 'Gam.form.FieldSet',
    alias: 'widget.cardrequestlistfieldsetchildreninfo',
    name: "citizenParentsInfo",

    id: 'childrenInfoFieldSetID',

    title: 'اطلاعات فرزندان',

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
                store: Ext.data.StoreManager.lookup('childrenStore'),
                columns: [
                    {
                        text: 'نام',
                        dataIndex: EmsObjectName.cardRequestList.childFirstName,
                        sortable: false,
                        hideable: false
                    },
                    {
                        text: 'شماره ملی',
                        dataIndex: EmsObjectName.cardRequestList.childNID,
                        sortable: false,
                        hideable: false},
                    {
                        text: 'تاریخ تولد',
                        dataIndex: EmsObjectName.cardRequestList.childBirthDate,
                        xtype: 'gam.datecolumn',
                        sortable: false,
                        hideable: false
                    },
                    {
                        text: 'جنسیت',
                        dataIndex: EmsObjectName.cardRequestList.childGender,
                        sortable: false,
                        hideable: false,
                        renderer: function(value) {
                            if(value == 'M'){
                                return "مرد";
                            } else if (value == 'F'){
                                return "زن";
                            }
                        }
                    }
                ],
                width: '100%',
                height: 130
            })
        ]
    }
});
