//khodayari
Ext.define('Ems.view.helpFileList.readOnly.AboutDialog', {
		 extend: 'ICT.Panel',
	    alias: 'widget.readOnlyaboutdialog',

	    requires: ['Ems.view.helpFileList.readOnly.About'],

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
	                xtype: 'readOnlyHelpFileAbout'
	            }
	        ];
	    }
	});
