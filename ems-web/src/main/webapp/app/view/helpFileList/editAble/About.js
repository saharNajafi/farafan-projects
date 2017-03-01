			//khodayari
			Ext.define('Ems.view.helpFileList.editAble.About',
					{
				id:'editAbleAbout',
				extend : 'ICT.form.FieldSet',
				alias : 'widget.editAbleHelpFileAbout',
				title : 'درباره',
//				width:'100%',
				contentStyle : function() {
					return (Tools.user.StyleObject);
				},
				requires : [],
			//	layout : 'column',
				initComponent : function() {
			
					var me = this;
			
					//me.defaults = {
								//columnWidth : 1 / 2
					//};
			
					me.callParent(arguments);
				},
			
		getEditableFields : function() {
			return [
				
				{
				xtype : 'textareafield',
		        grow   : true,
				id :  EmsObjectName.about.content,
				itemId:  EmsObjectName.about.content,
				name : EmsObjectName.about.content,
				width:480,
				height:230
			}
			];
				}
});
