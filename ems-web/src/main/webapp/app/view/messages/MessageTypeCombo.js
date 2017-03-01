Ext.define('Ems.view.messages.MessageTypeCombo', {
    extend: 'Ext.form.ComboBox',

    alias: 'widget.messagetypecombo',

//    fieldLabel: '',
    initComponent: function () {
        this.store = Ext.create('Ext.data.ArrayStore', {
            fields: [
                'name',
                'value'
            ],
            data: [
                 ['همه', 'ALL'],
                ['استان', 'PROVINCE'],
                ['اداره','DEPARTMENT'],
                ['دفتر معین', 'NOCR'],
                ['دفتر خصوصی', 'OFFICE'],
                ['شخصی', 'PRIVATE']
            ]
        });
        this.callParent(arguments);
    },
//    margin: 5,
//    labelWidth: 70,
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
