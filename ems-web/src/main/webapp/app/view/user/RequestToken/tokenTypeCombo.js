Ext.define('Ems.view.user.RequestToken.tokenTypeCombo', {
    extend: 'Ext.form.ComboBox',

    alias: 'widget.tokentypecombo',

//    fieldLabel: '',
    initComponent: function () {
        this.store = Ext.create('Ext.data.ArrayStore', {
            fields: [
                'name',
                'value'
            ],
            data: [
                ['احراز هویت', 'AUTHENTICATION'],
                ['امضا', 'SIGNATURE'],
                ['رمزنگاری', 'ENCRYPTION']
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
