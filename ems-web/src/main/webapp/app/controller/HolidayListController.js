/**
 * Created with IntelliJ IDEA. User: Mousavi Date: 7/25/12 Time: 12:25 PM To
 * change this template use File | Settings | File Templates.
 */
Ext.define('Ems.controller.HolidayListController', {
	extend : 'Gam.app.controller.RowEditorBasedGrid',

	statics : {
		statefulComponents : [ 'wHolidayListGrid', ]
	},

	ns : 'extJsController/holidaylist',
	views : [ 'holidayList.Grid' ],

	initViewType : 'holidaylistgrid',

	stores : [ 'HolidayListStore' ],

	constructor : function(config) {

		this.callParent(arguments);
	}

});
