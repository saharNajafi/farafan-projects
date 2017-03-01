/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 6/3/12
 * Time: 1:01 PM
 */

Ext.define('Ems.view.messages.View.Dialog', {
	 extend: 'ICT.Panel',
	 border : false,
    alias: 'widget.messagesdialogView',

    requires: [
        'Ems.view.messages.View.MessageInfo'
        , 'Ems.view.messages.View.CueMessages'
    ],

    width: '100%',

    initComponent: function () {
//        this.height = 400 ;

        this.callParent();
    },
    constructor: function (){
    	this.callParent(arguments);
    },

    getItems: function(){
        return [
            {
                xtype: 'messageneweditmessageInfoView'
            },
            {
                xtype: 'messageneweditCueMessagesView'
            }
        ];
    }
});
