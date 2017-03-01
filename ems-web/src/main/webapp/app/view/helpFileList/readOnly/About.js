			//khodayari
			Ext.define('Ems.view.helpFileList.readOnly.About',
					{
				id:'readOnlyAbout',
				extend : 'ICT.form.FieldSet',
				alias : 'widget.readOnlyHelpFileAbout',
				title : 'درباره',
//				width:'100%',
				contentStyle : function() {
					return (Tools.user.StyleObject);
				},
				requires : [],
				initComponent : function() {
			
					var me = this;
					me.callParent(arguments);
				},
			isReadOnly : function(){
				return true;
			},
		getReadOnlyFields : function() {
		return [
			{
				fieldLabel : 'تاریخ',
				allowBlank : true,
				xtype:'displayfield',
				labelWidth:50,
				id :  EmsObjectName.about.createDateAbout,
				itemId:  EmsObjectName.about.createDateAbout,
				name : EmsObjectName.about.createDateAbout,
				maxLength : 32,
				width:480
			},
			{
				xtype : 'textareafield',
		        grow   : true,
				id :  EmsObjectName.about.content,
				itemId:  EmsObjectName.about.content,
				name : EmsObjectName.about.content,
				width:480,
				height:230,
				readOnly : true
			}
			];
				}
});
