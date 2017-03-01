	
	 //khodayar
	Ext.define('Ems.controller.HelpFileListController', {
	    extend: 'Gam.app.controller.LocalDialogBasedGrid',
	
	
	    ns:'extJsController/helpfilelist',
	    nsDownload : 'extJsController/downloadfile',
	    ns_about:'extJsController/about',
	    statics: {
	        statefulComponents: [
	            'wHelpFileListGrid',
	        ]
	    },
	    views: ['helpFileList.Grid'],
	
	//    refs: [
	//           {
	//               ref: 'estelam2LogGrid',
	//               selectors: 'estelam2loggrid'
	//           }
	//       ],
	    constructor: function (config) {
	   	 this.me = this;
	       this.callParent(arguments);
	   }, 
	    init: function () { //
	    	var me = this;
	    	var record = new Object();
	        this.control({
	            "[action=about]": {
	                click: function (btn) {
	                    me.doAbout(btn);
	                }
	            }
	        });
	        this.control({
	           
	//        	 "[action=btnSaveHelpFile]": {  'click': this.getSaveHelpFile, scope: this}  
	        	"[action=btnAdd]": {'click': this.getSaveAbout, scope: this},
	        	"[action=btnEdit]": {'click': this.getEditAbout, scope: this},
	        	"[action=btnCancelEdit]":{'click' : this.getCancelEdit , scope : this}
	        });
	        this.callParent(arguments);
	    },
	
		initViewType : 'helpfilelistgrid',
	
	    stores: ['HelpFileListStore','Ems.store.AboutStore'],
	
	    getSaveHelpFile: function (btn) {
	    	var me = this;
	    	var dataForm = new Object();
	    	dataForm.title = Ext.getCmp(EmsObjectName.helpFileList.title).getValue();
	    	dataForm.helpFile = helpFileContent;
	    	
		    var data = Ext.decode(Tools.toJson(dataForm, true));
	    	 Ext.Ajax.request({
	             url: me.ns + '/save', jsonData: {
	                 records: data
	             },
	             success: function (response) {
	            	
	                 var obj = Ext.decode(response.responseText);
	                 Gam.Msg.hideWaitMsg();
	                 if (obj.success) {
	                //     win.close();
	                 //    grid.getStore().load();
	                 } else {
	                     Gam.Msg.hideWaitMsg();
	                     Tools.errorMessageServer(obj.messageInfo);
	                 }
	             },
	             failure: function (resp) {
	                 Gam.Msg.showWaitMsg();
	                 Tools.errorFailure();
	             }
	         });
	    },
	    getSaveAbout : function(btn){
	    	Gam.Msg.showWaitMsg();
	    	var me = this;
	    	var dataForm = new Object();
	    	dataForm.content = escape(Ext.getCmp(EmsObjectName.about.content).getValue());
	  
	    	if(me.getRecord())
	    		dataForm.id = me.getRecord().id;
		    var data = Ext.decode(Tools.toJson(dataForm, true));
		
	    	 Ext.Ajax.request({
	             url: me.ns_about + '/save', jsonData: {
	                 records: data
	             },
	             success: function (response) {
	                 var obj = Ext.decode(response.responseText);
	                 Gam.Msg.hideWaitMsg();
	                 if (obj.success) {
	                	 btn.up('window').close();
	                 } else {
	                     Gam.Msg.hideWaitMsg();
	                     Tools.errorMessageServer(obj.messageInfo);
	                 }
	             },
	             failure: function (resp) {
	                 Gam.Msg.showWaitMsg();
	                 Tools.errorFailure();
	             }
	         });
	    },
	    getCancelEdit : function(btn){
	    	me = this;
	    	btn.up('window').close();
	    	
	    	var win , panel ,aboutInfo;
	    	win = Ext.create('Ems.view.helpFileList.readOnly.AboutWindow');
			 panel =  win.down('readOnlyaboutdialog');
			 aboutInfo = panel.down('readOnlyHelpFileAbout');
			 aboutInfo.setData(Ext.create('Ems.model.AboutModel',{
		    	   id:me.getRecord().id,
		    	   createDateAbout:Gam.util.Format.date(me.getRecord().createDateAbout),
		           content:unescape(me.getRecord().content)
		    	}),aboutInfo);
			 	
			 Tools.MaskUnMask(win);
		     win.show();
	    },
	    getEditAbout : function(btn){
	    	 me = this;
	    	 btn.up('window').close();
	    	 me.loadForm('edit', me.getRecord());
	    },
	    loadForm : function (mode , rec){
	    	 me = this;
	     	var win , panel ,aboutInfo;
	     	switch (mode) {
	 		case 'view':
	 		{			
	 			 win = Ext.create('Ems.view.helpFileList.readOnly.AboutWindow');
	 			 panel =  win.down('readOnlyaboutdialog');
	 			 aboutInfo = panel.down('readOnlyHelpFileAbout');
	 			 aboutInfo.setData(Ext.create('Ems.model.AboutModel',{
	    	    	   id:rec.id,
	    	    	   createDateAbout:Gam.util.Format.date(rec.createDateAbout),
	    	           content:unescape(rec.content)
	    	    	}),aboutInfo);
	 			break;
	 			
	 		}
	 		case 'add':{
	 			 win = Ext.create('Ems.view.helpFileList.editAble.AboutWindow');
				 panel =  win.down('editAbleaboutdialog');
				 aboutInfo = panel.down('editAbleHelpFileAbout');
	 	         break;
	 		    	
	 		}
	 		case 'edit':{
	 			 win = Ext.create('Ems.view.helpFileList.editAble.AboutWindow');
				 panel =  win.down('editAbleaboutdialog');
				 aboutInfo = panel.down('editAbleHelpFileAbout');
				 aboutInfo.setData(Ext.create('Ems.model.AboutModel',{
	   	    	   id:rec.id,
	   	    	   createDateAbout:Gam.util.Format.date(rec.createDateAbout),
	   	           content:unescape(rec.content)
	   	    	}),aboutInfo);
				break;
				
			}
	 		default:
	 			break;
	 		}
	     	Tools.MaskUnMask(win);
	     	win.show();
	    },
	    doAbout: function (btn) {
	    	me = this;
	        Ext.Ajax.request({
	        	url: me.ns_about+'/load',
	    		success: function (response) {
	            var obj = Ext.decode(response.responseText);
	            var rec = new Object();
	     if(obj.messageInfo.message != "Ems.ErrorCode.security.EMS_S_AUT_004"){
	    	 if ( obj.records != null) {
	    		 rec = obj.records;
	    		 me.loadForm('view', rec[0]);
	    		 me.setRecord(rec[0]);
	    	 } else {
	    		 me.loadForm('add', rec);
	    		 Tools.errorMessageServer(obj.messageInfo);
	    	 }
	    	 
	     }
	        },
	        failure: function (resp) {
	            Gam.Msg.showWaitMsg();
	            Tools.errorFailure();
	        }
	        });
	    },
	    setRecord : function(rec){
	    	me = this;
	    	me.record = rec;
	    },
	    getRecord : function(){
	    	me = this;
	    	if(me.record)
	    		return me.record;
	    	return null;
	    },
	    doDownloadHelp : function(grid, rowIndex){
	    	me = this;
	    	console.log(grid.getStore().getAt(rowIndex).get("id"));
	    	try {
	            if (Ext.get('downloadIframe')) {
	                Ext.destroy(Ext.get('downloadIframe'));
	            }
	        }
	        catch (e) {
	            console.info(e);
	        }
	
	        Ext.DomHelper.append(document.body, {
	            tag: 'iframe',
	            id: 'downloadIframe',
	            frameBorder: 0,
	            width: 0,
	            height: 0,
	            css: 'display:none;visibility:hidden;height:0px;',
	            src: me.nsDownload+'/downloadHelpFile?id=' +
	                grid.getStore().getAt(rowIndex).get("id")
	        });
	    }
	});
	
	
