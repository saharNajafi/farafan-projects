//khodayari
Ext.define('Ems.view.helpFileList.Uploader', {
    extend: 'Ext.form.Panel',
    alias: 'widget.helpFileUploader',
    id: 'idhelpfileuploader',
    border: false,
    bodyPadding: 10,
    
    title: '',
    items: [{
//    	id:EmsObjectName.helpFileList._helpFile,
    	name:EmsObjectName.helpFileList.helpFile,
        xtype: 'filefield',
        name: 'hfile',
        fieldLabel: 'فایل',
        labelWidth: 60,
        msgTarget: 'side',
        allowBlank: false,
        anchor: '100%',
        // Array of acceptable file extensions
        // overridden when decalred with a string
        // of extensions minus the period.
        accept        : ['pdf','docx','txt','xlsx','doc'],
//        listeners     : {
//            validitychange : function(me) {
//            	if(me.getValue().length == 0)
//            		return;
//            	
//                // This gets the part of the file name after the last period
//                var indexofPeriod = me.getValue().lastIndexOf("."),
//                    uploadedExtension = me.getValue().substr(indexofPeriod + 1, me.getValue().length - indexofPeriod);
//        
//                // See if the extension is in the
//                      //array of acceptable file extensions
//                if (!Ext.Array.contains(this.accept, uploadedExtension)){
//                    // Add the tooltip below to
//                      // the red exclamation point on the form field
//                    me.setActiveError('لطفا فایل با فرمت :  ' + this.accept.join() + ' آپلود نمایید!');
//                  // Let the user know why the field is red and blank!
//                    Ext.MessageBox.show({
//                        title   : 'خطای فایل ورودی',
//                        msg   : 'لطفا فایل با فرمت :  ' + this.accept.join() + ' آپلود نمایید!',
//                        buttons : Ext.Msg.OK,
//                        icon  : Ext.Msg.ERROR
//                    });
//                 	Ext.getCmp('idhelpfilelistdialog').close();
//                    // Set the raw value to null so that the extjs form submit
//                    // isValid() method will stop submission.
//                    me.setRawValue(null);
//                }
//            }
//        },

        buttonText: 'فایل مورد نظر را انتخاب نمایید',
        onChange: function(value) {
        	                   
        }

    },
    {
    	name:EmsObjectName.helpFileList.title,
    	xtype:'textfield',
	    anchor: '100%',
	    labelWidth: 60,
    	fieldLabel: 'نام فایل',
    	allowBlank : false
    }/*,
    {
    	name:EmsObjectName.helpFileList.desc,
    	xtype:'textfield',
	    anchor: '100%',
	    labelWidth: 60,
    	fieldLabel: 'توضیحات',
    	allowBlank : true
    }*/],
    buttons: [{
        text: 'ارسال',
        action:'upload',
        id: 'idBtnSaveHelpFile',
        iconCls: 'windows-Save-icon',
//        style  : 'margin-left: 22px',
        handler: function() {
        	var form = this.up('form').getForm();
        	var dataForm = form.getValues();
    	    var data = Ext.decode(Tools.toJson(dataForm, true));
    	    var grid =  Ext.getCmp('helpfilelistgrid');
    
            if(form.isValid()){
            	Gam.Msg.showWaitMsg();
                form.submit({
                    url: 'extJsController/helpfilelist/save?title='+data[0].title/*+'&desc='+data[0].desc*/,
                    method: 'POST',
                    clientValidation: false,
                    success: function(fp, o) {
                    	
                    	 Gam.Msg.hideWaitMsg();
                    	 Ext.getCmp('idhelpfilelistdialog').close();
                    	  grid.getStore().load();

                    }, failure: function (fp, o) {
                    	
                    	 Tools.errorMessageServer(o.response.responseJSON.messageInfo);
      	          }
            });
        }
        }
    }]
    
});