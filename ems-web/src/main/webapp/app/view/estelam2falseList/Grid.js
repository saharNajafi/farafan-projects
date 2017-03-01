/**
 * Created with IntelliJ IDEA. User: Mousavi Date: 7/25/12 Time: 12:06 PM To
 * change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.estelam2falseList.Grid', {

	extend : 'Gam.grid.Crud',

	alias : 'widget.estelam2falselistgrid',

	stateId : 'wEstelam2FalseListGrid',

	title : 'مدیریت استعلام های منفی',

	multiSelect : false,

	requires : [ 'Ems.store.Estelam2FalseListStore' ,
	             'Ems.view.cardRequestList.combo.CardRequestStateComboBox',],

	store : {
		type : 'estelam2falseliststore'
	},

	actionColumnItems : [
			{
				getClass : function(value, metaData, record, rowIndex,
						colIndex, store) {
					return 'grid-view-action-icon';
				},
				tooltip : 'مشاهده تاریخچه',
				action : 'estelam2Log'
			},

	],

	initComponent : function() {
		this.columns = this.getItemsGridForm();
		this.callParent(arguments);
	},

	getItemsGridForm : function() {
		return ([ {
			dataIndex : EmsObjectName.estelam2FalseList.citizenFirstName,
			id : EmsObjectName.estelam2FalseList.citizenFirstName,
			text : 'نام',
		},

		{
			dataIndex : EmsObjectName.estelam2FalseList.citizenSurname,
			id : EmsObjectName.estelam2FalseList.citizenSurname,
			text : 'نام خانوادگی',
		},

		{
			dataIndex : EmsObjectName.estelam2FalseList.citizenNId,
			id : EmsObjectName.estelam2FalseList.citizenNId,
			text : 'کد ملی',
			filterable : true,
			filter : true,
		},

		{
			dataIndex : EmsObjectName.estelam2FalseList.birthDateSolar,
			id : EmsObjectName.estelam2FalseList.birthDateSolar,
			text : 'تاریخ تولد',
		},

		{
			dataIndex : EmsObjectName.estelam2FalseList.cardRequestState,
			id : EmsObjectName.estelam2FalseList.cardRequestState,
			text : 'وضعیت درخواست',

			renderer: function (value) {
                 if (value && typeof value === 'string') {
                     var resualt = eval("EmsResource.cardRequestList.CardRequestState." + value);
                     return resualt != null ? resualt : value;
                 }
             },
             filterable: true,
             filter: {
                 xtype: 'cardrequeststatecombobox'
             }
		},

		{
			dataIndex : EmsObjectName.estelam2FalseList.trackingId,
			id : EmsObjectName.estelam2FalseList.trackingId,
			text : 'کد رهگیری',
			filterable : true,
			filter : true,
		},

		{
			xtype : 'gam.datecolumn',
			dataIndex : EmsObjectName.estelam2FalseList.reservationDate,
			id : EmsObjectName.estelam2FalseList.reservationDate,
			format : Ext.Date.defaultDateTimeFormat,
			text : 'تاریخ رزرو',
		},
		
		
		{
			dataIndex : EmsObjectName.estelam2FalseList.officeName,
			id : EmsObjectName.estelam2FalseList.officeName,
			text : 'دفتر پیشخوان',
		},

		]);
	}
});
