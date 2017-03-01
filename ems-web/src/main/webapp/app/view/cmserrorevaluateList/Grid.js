/**
 * Created with IntelliJ IDEA. User: Mousavi Date: 7/25/12 Time: 12:06 PM To
 * change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.cmserrorevaluateList.Grid', {

	extend : 'Gam.grid.Crud',

	alias : 'widget.cmserrorevaluatelistgrid',

	stateId : 'wCmsErrorEvaluateListGrid',

	title : 'مدیدیت خطاهای صدور',

	multiSelect : false,

	requires : [ 'Ems.store.CmsErrorEvaluateListStore' ],

	store : {
		type : 'cmserrorevaluateliststore'
	},

	actionColumnItems : [
			{
				/*getClass : function(value, metaData, record, rowIndex,
						colIndex, store) {
					return 'grid-repeal-request-action-icon';
				},*/
				tooltip : 'ابطال درخواست',
				action : 'repeal',
				getClass: function (value, metadata, record) {
                    return(EmsObjectName.cmsErrorEvaluateList.errorRepealedAccess ?'grid-repeal-request-action-icon' : 'x-hide-display');
                }
			},

			{
				tooltip : 'آماده برای صدور مجدد',
				action : 'cMSRetry',
					getClass : function(value, metaData, record, rowIndex,
							colIndex, store) {
						return(EmsObjectName.cmsErrorEvaluateList.errorRetryAccess ? 'grid-undo-repeal-request-action-icon': 'x-hide-display');
					},
			},

			{
				tooltip : 'پاک کردن تصاویر متقاضی',
				action : 'deleteImage',
				getClass : function(value, metaData, record, rowIndex,
						colIndex, store) {
					 return(EmsObjectName.cmsErrorEvaluateList.errorDeleteImageAccess ?'grid-undo-person-token-action-icon' : 'x-hide-display');
				}
			},

	],

	initComponent : function() {
		this.columns = this.getItemsGridForm();
		this.callParent(arguments);
	},

	getItemsGridForm : function() {
		return ([

		{
			dataIndex : EmsObjectName.cmsErrorEvaluateList.cardRequestId,
			id : EmsObjectName.cmsErrorEvaluateList.cardRequestId,
			text : 'شناسه درخواست',
			filterable : true,
			filter : false,
		},

		{
			dataIndex : EmsObjectName.cmsErrorEvaluateList.citizenFirstName,
			id : EmsObjectName.cmsErrorEvaluateList.citizenFirstName,
			text : 'نام',
			filterable : true,
			filter : true,
		},

		{
			dataIndex : EmsObjectName.cmsErrorEvaluateList.citizenSurname,
			id : EmsObjectName.cmsErrorEvaluateList.citizenSurname,
			text : 'نام خانوادگی',
			filterable : true,
			filter : true,
		},

		{
			dataIndex : EmsObjectName.cmsErrorEvaluateList.citizenNId,
			id : EmsObjectName.cmsErrorEvaluateList.citizenNId,
			text : 'کد ملی',
			filterable : true,
			filter : true,
		},

		{
			dataIndex : EmsObjectName.cmsErrorEvaluateList.officeName,
			id : EmsObjectName.cmsErrorEvaluateList.officeName,
			text : 'دفتر',
			filterable : true,
			filter : false,
		},		

		{
			xtype: 'gam.datecolumn',
			dataIndex : EmsObjectName.cmsErrorEvaluateList.backDate,
			id : EmsObjectName.cmsErrorEvaluateList.backDate,
			text : 'تاریخ برگشت',
			filterable : true,
			filter: {
        		  xtype: 'container',
        		  layout: {
        			  type: 'hbox',
        			  align: 'middle'
        		  },
        		  defaults: {
        			  labelWidth: 10,
        			  xtype: 'datefield',
        			  flex: 1
        		  }, items: [
        		             {
        		            	 fieldLabel: 'از',
        		            	 name: 'fromBackDate'
        		             },
        		             {
        		            	 fieldLabel: 'تا',
        		            	 name: 'toBackDate'
        		             }
        		             ]
        	  }
		},

		{
			dataIndex : EmsObjectName.cmsErrorEvaluateList.result,
			id : EmsObjectName.cmsErrorEvaluateList.result,
			text : 'نتیجه',
			filterable : true,
			filter : false,
		},
		]);
	}
});
