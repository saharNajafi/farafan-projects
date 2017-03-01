Ext.define('Ems.view.user.RequestToken.tokenStateCombo', {
    extend: 'Ext.form.ComboBox',

    alias: 'widget.tokenstatecombo',

//    fieldLabel: '',
    initComponent: function () {
        this.store = Ext.create('Ext.data.ArrayStore', {
            fields: [
                'name',
                'value'
            ],
            data: [
                ['آماده صدور', 'READY_TO_ISSUE'],
                ['در دست صدور', 'PENDING_TO_ISSUE'],
                ['آماده تحویل', 'READY_TO_DELIVER'],
                ['تحویل شد', 'DELIVERED'],
                ['ابطال شده', 'REVOKED'],
                ['منتظر تایید EMS', 'PENDING_FOR_EMS'],
                ['آماده صدور (توکن تمدیدی)', 'READY_TO_RENEWAL_ISSUE'],
                ['در دست صدور (توکن تمدیدی)', 'PENDING_TO_RENEWAL_ISSUE'],
                ['آماده تحویل (توکن تمدیدی)', 'READY_TO_RENEWAL_DELIVER'],
                ['تایید نشده', 'EMS_REJECT'],
                ['آماده فعالسازی', 'SUSPENDED'],
                
            ]
        });
        this.callParent(arguments);
    },
    margin: 5,
    labelWidth: 70,
    queryMode: 'local',
    displayField: 'name',
    valueField: 'value',
    listConfig: {
        getInnerTpl: function () {
            var tpl = '<div>{name}</div>';
            return tpl;
        }
    }
});
