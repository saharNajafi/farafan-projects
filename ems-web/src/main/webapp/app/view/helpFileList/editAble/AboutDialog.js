//khodayari
Ext.define('Ems.view.helpFileList.editAble.AboutDialog', {
		 extend: 'ICT.Panel',
	    alias: 'widget.editAbleaboutdialog',

	    requires: ['Ems.view.helpFileList.editAble.About'],

//	    width: 505,

	    initComponent: function () {
//	        this.height = 305;

	        this.callParent();
	    },
	    constructor: function (){
	    	this.callParent(arguments);
	    },

	    getItems: function(){
	        return [
	            {
	                xtype: 'editAbleHelpFileAbout'
	            }
	        ];
	    }
	});
