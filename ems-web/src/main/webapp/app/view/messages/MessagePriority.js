Ext.define('Ems.view.messages.MessagePriority', {
    extend: 'Ext.form.ComboBox',
    alias: 'widget.messagepriority',

//  fieldLabel: '',
  initComponent: function () {
      this.store = Ext.create('Ext.data.ArrayStore', {
          fields: [
              'name',
              'value'
          ],
          data: [
                 ['مهم', '1'],
                 ['غیر مهم','0']
             ]
      });
      this.callParent(arguments);
  },
//  margin: 5,
//  labelWidth: 70,
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
