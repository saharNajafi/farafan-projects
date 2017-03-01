		//khodayari
Ext.define('Ems.view.helpFileList.Grid', {
			extend: 'Gam.grid.Crud',
			alias: 'widget.helpfilelistgrid',
			stateId: 'wHelpFileListGrid',
			title: 'مدیریت راهنمای کاربران CCOS',
			multiSelect : false,
		
			requires : ['Ems.store.HelpFileListStore'
			             ,'Ems.view.helpFileList.Dialog'],
		
			store : {type : 'helpfileliststore'},
		
			actions: ['gam.add->helpfilelistdialog', 'gam.delete' ],
			tbar: ['gam.add', 'gam.delete',{
				iconCls: 'girdAction-infoabout-icon',
				text: 'توضیحات',
				action: 'about',
				stateful: true,
				stateId: this.stateId + 'about'
			}],
		
			initComponent : function() {
		
				this.columns = this.getGridColumns();
				this.callParent(arguments);
			},
			actionColumnItems : [
             {
            	 getClass: function (value, metaData, record, rowIndex, colIndex, store) {
            		 return 'report-download-icon';
            	 },
            	 tooltip: 'دریافت فایل',
            	 action: 'downloadHelp'
              }
             ],
			getGridColumns: function () {
				return [
				{
					//xtype: 'gridcolumn',
					dataIndex : EmsObjectName.helpFileList.title,
					id : EmsObjectName.helpFileList.title,
					text : 'عنوان',
	                width: 100
				},
				{
					dataIndex : EmsObjectName.helpFileList.creator,
					id : EmsObjectName.helpFileList.creator,
					text : 'ایجاد کننده',
					width: 100
				},
				
				{
					xtype : 'gam.datecolumn',
					dataIndex : EmsObjectName.helpFileList.createDateHelp,
					id : EmsObjectName.helpFileList.createDateHelp,
					text : 'تاریخ ویرایش',
					width: 200,
					format : Ext.Date.defaultDateTimeFormat
				}
		];
	}
		
});
