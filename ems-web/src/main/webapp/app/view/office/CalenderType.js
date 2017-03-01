Ext.define('Ems.view.office.CalenderType', {
    extend: 'Ext.form.ComboBox',
    alias: 'widget.calendertype',

//  fieldLabel: '',
  initComponent: function () {
      this.store = Ext.create('Ext.data.ArrayStore', {
          fields: [
              'name',
              'value'
          ],
          data: [
                 ['جمعه و پنجشنبه تعطیل','0'],
                 ['پنجشنبه باز و جمعه تعطیل','1'],
                 ['جمعه و پنجشنبه باز', '2'],
                 ['جمعه باز و پنجشنبه تعطیل', '3']
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
