/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 6/3/12
 * Time: 1:01 PM
 */

Ext.define('Ems.view.messages.Dialog', {
	 extend: 'ICT.Panel',
    alias: 'widget.messagesdialog',

    requires: [
        'Ems.view.messages.NewEdit.MessageInfo'
        , 'Ems.view.messages.NewEdit.CueMessages'
    ],

    width: '100%',

    initComponent: function () {
        this.height = 520;

        this.callParent();
    },

    getItems: function(){
        return [
            {xtype: 'messageneweditmessageInfo'},
//            {xtype: 'messageneweditCueMessages'},
            {
            	xtype: 'container',
            	layout: 'column',
        		columnWidth: 1/3,
                margin: 5,
                border: false,
                items: [
                        {
            	        	xtype: 'provincemultiselect',
            	        	height: 80,
            	        	width:250,
            	        	style: 'margin:5px; height:100px;',
            	        	hiddenName: 'provinceMultiHiddenName'

            	        },
            	        {
            	        	xtype: 'departmentmultiselect',
            	        	height: 80,
            	        	width:250,
            	        	style: 'margin:5px; height:100px;',
            	        	hiddenName: 'departmentMultiHiddenName'

            	        },
            	        {
            	        	xtype: 'personmultiselect',
            	        	height: 80,
            	        	width:250,
            	        	style: 'margin:5px; height:100px;',
            	        	hiddenName: 'personMultiHiddenName'

            	        }]
            },
          
	        {xtype:'messageneweditmessageBody'},
	        {xtype: 'messageFileUploader'}
        ];
    }
});
