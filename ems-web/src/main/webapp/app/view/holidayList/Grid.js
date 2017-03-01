/**
 * Created with IntelliJ IDEA. User: Mousavi Date: 7/25/12 Time: 12:06 PM To
 * change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.holidayList.Grid', {

	extend : 'Gam.grid.RowEditingCrud',
	alias : 'widget.holidaylistgrid',

	stateId : 'wHolidayListGrid',

	title : 'مدیریت تعطیلات',

	multiSelect : true,

	requires : [ 'Ems.store.HolidayListStore' ],

	store : {
		type : 'holidayliststore'
	},

	actions : [ 'gam.add', 'gam.delete' ],
//	actionColumnItems : [ 'edit' ],
	contextMenu : [ 'gam.add', 'gam.delete' ],
	tbar : [ 'gam.add', 'gam.delete' ],

	initComponent : function() {
		this.columns = this.getItemsGridForm();
		this.callParent(arguments);
	},

	getItemsGridForm : function() {
		return ([  {
			xtype : 'gam.datecolumn',
			format: 'Y/m/d',
			dataIndex : EmsObjectName.holidayList.holiday,
			id: EmsObjectName.holidayList.holiday,
			format : Ext.Date.defaultDateTimeFormat,
			text : 'تاریخ تعطیلی',
			editor : {
				allowBlank : false,
				xtype: 'datefield',
			}
		}
		
		]);
	}
});
