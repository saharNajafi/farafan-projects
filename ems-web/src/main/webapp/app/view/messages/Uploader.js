//khodayari
Ext.define('Ems.view.messages.Uploader', {
    extend: 'Ext.form.Panel',
    alias: 'widget.messageFileUploader',
    id: 'idmessagefileuploader',
    border: false,
    bodyPadding: 10,
    layout: 'column',
	columnWidth: 1/2,
    title: '',
    items: [{
//    	name:'EmsObjectName.messages.title',
        xtype: 'filefield',
        name: 'hfile',
        fieldLabel: 'فایل',
        labelWidth: 60,
        width: 600,
        msgTarget: 'side',
        allowBlank: false,
        anchor: '100%',
        accept : [],

        buttonText: 'فایل مورد نظر را انتخاب نمایید',
        onChange: function(value) {
//        	var ns = 'extJsController/messages/uploadAttachedFile';
//        	var form = this.up('form').getForm();
//        	var dataForm = form.getValues();
//    	    var data = Ext.decode(Tools.toJson(dataForm, true));
////    	    var grid =  Ext.getCmp('messagefilelistgrid');
//    
//            if(form.isValid()){
////            	Gam.Msg.showWaitMsg();
////                form.submit({
////                    url: ns+'?title='+data[0].title/*+'&desc='+data[0].desc*/,
////                    method: 'POST',
////                    clientValidation: false,
////                    success: function(fp, o) {
////                    	 Gam.Msg.hideWaitMsg();
//////                    	  grid.getStore().load();
//////                    	 messageBody.setData(Ext.create('Ems.model.MessagesModel',
//////     							{
//////     						id:rec[0].id,
//////     						title : rec[0].title,
//////     						content  : unescape(rec[0].content)
//////     							}),messageBody);
////                    	 
////                    }, failure: function (fp, o) {
////                    	 Tools.errorMessageServer(o.response.responseJSON.messageInfo);
////      	          }
////            });
//        }
//             
        }

    },
    {
    	xtype:'hidden',
    	height :20,
    	width:20,
    },
    {
        text: 'بارگذاری',
        action:'upload',
        xtype:'button',
        id: 'idBtnSavemessageFile',
        iconCls: 'windows-Save-icon',
//        style  : 'margin-left: 22px',
        handler: function() {
        	var ns = 'extJsController/messages/uploadAttachedFile';
        	var form = this.up('form').getForm();
        	var dataForm = form.getValues();
    	    var data = Ext.decode(Tools.toJson(dataForm, true));
    
            if(form.isValid()){
            	Gam.Msg.showWaitMsg();
                form.submit({
                    url: ns+'?title='+data[0].title/*+'&desc='+data[0].desc*/,
                    method: 'POST',
                    clientValidation: false,
                    success: function(fp, o) {
                    	 Gam.Msg.hideWaitMsg();

                    }, failure: function (fp, o) {
                    	 Tools.errorMessageServer(o.response.responseJSON.messageInfo);
      	          }
            });
        }
        }
    }
//    {
//    	name:EmsObjectName.messages.title,
//    	xtype:'textfield',
//	    anchor: '100%',
//	    labelWidth: 60,
////    	fieldLabel: 'نام فایل',
//    	allowBlank : true
//    }
    ],
    
//    buttons: [{
//        text: 'بارگذاری',
//        action:'upload',
//        id: 'idBtnSavemessageFile',
//        iconCls: 'windows-Save-icon',
////        style  : 'margin-left: 22px',
//        handler: function() {
//        	var ns = 'extJsController/messages/uploadAttachedFile';
//        	var form = this.up('form').getForm();
//        	var dataForm = form.getValues();
//    	    var data = Ext.decode(Tools.toJson(dataForm, true));
//    
//            if(form.isValid()){
//            	Gam.Msg.showWaitMsg();
//                form.submit({
//                    url: ns+'?title='+data[0].title/*+'&desc='+data[0].desc*/,
//                    method: 'POST',
//                    clientValidation: false,
//                    success: function(fp, o) {
//                    	 Gam.Msg.hideWaitMsg();
//
//                    }, failure: function (fp, o) {
//                    	 Tools.errorMessageServer(o.response.responseJSON.messageInfo);
//      	          }
//            });
//        }
//        }
//    }]
    
});