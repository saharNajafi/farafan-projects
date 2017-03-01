/**
 * Created with IntelliJ IDEA. User: Mousavi Date: 6/24/12 Time: 5:00 PM To
 * change this template use File | Settings | File Templates.
 **/
Ext.define('Ems.view.messages.View.CueMessages',
				{
	                id:'CueMessagesView',
	            	extend : 'Ext.form.Panel',
					alias : 'widget.messageneweditCueMessagesView',
					title : 'گیرنده ها',
					overflow:'auto',
					autoScroll : true,
					contentStyle : function() {
						return (Tools.user.StyleObject);
					},
					layout: {
				        type: 'vbox'
				    },
					 margin: 10,
					 height : 100,
//					layout : 'column',
					initComponent : function() {

						var me = this;

						me.defaults = {
//							columnWidth : 1 / 1
						};

						me.callParent(arguments);
					}
				});
