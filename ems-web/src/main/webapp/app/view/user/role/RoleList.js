Ext.define('Ems.view.user.role.RoleList', {
    extend: 'Ext.form.field.ComboBox',//'Gam.form.field.comboBox.Local',//
    alias: 'widget.userrolerolelist',
    id: 'cmbUserRolesChoice',
    displayField: EmsObjectName.comboBox.name,
    valueField: EmsObjectName.comboBox.code,
    store: {type: 'userrolesotre'},

    grow: true,
    queryMode: 'local',

    listConfig: {
        getInnerTpl: function () {
            return '<div>{name}</div>'
        }
    }
});