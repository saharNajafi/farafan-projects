/**
 * Created with IntelliJ IDEA. User: Mousavi Date: 6/24/12 Time: 5:00 PM To
 * change this template use File | Settings | File Templates.
 **/
Ext.define('Ems.view.messages.View.MessageBody',
		{
	id:'MessageBodyView',
	extend : 'ICT.form.FieldSet',
	alias : 'widget.messageneweditmessageBodyView',


	contentStyle : function() {
		return (Tools.user.StyleObject);
	},
	initComponent : function() {

		var me = this;

		me.callParent(arguments);
	},
//	layout : 'column',
	border:false,
	isReadOnly : function(){
		return true;
	},
	getReadOnlyFields : function() {
		return [
		        {
		        	fieldLabel : 'عنوان',
		        	readOnly :true,
		        	xtype:'displayfield',
		        	labelWidth:30,
		        	id :  EmsObjectName.messages_ID.title,
		        	itemId:  EmsObjectName.messages.title,
		        	name : EmsObjectName.messages.title,
		        	maxLength : 32,
//		        	width:850,
		        	bodyStyle : this.contentStyle(),
		        	style : this.contentStyle()
		        },
		        {
		        	xtype: 'htmleditor',
		        	readOnly : true,
		        	id :  EmsObjectName.messages_ID.content,
		        	itemId:  EmsObjectName.messages.content,
		        	name : EmsObjectName.messages.content,
		        	width:800,
		        	height:200,
		        	listeners : {
		        		initialize : function(editor) {
		        			editor.focus();
		        			editor.getToolbar().hide(false);
		        		}
		        	}
		        }
		        ];
	}
		});
