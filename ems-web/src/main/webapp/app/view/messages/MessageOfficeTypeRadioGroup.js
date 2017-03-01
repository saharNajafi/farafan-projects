
Ext.define('Ems.view.messages.MessageOfficeTypeRadioGroup', {
	extend : 'ICT.form.FieldSet',
    alias: 'widget.messageofficetyperadiogroup',

//    columns : 2,
    layout:'column',
//    border:false,
    initComponent : function() {

    	var me = this;

    	me.defaults = {
    			columnWidth : 1 / 3
    	};

    	me.callParent(arguments);
    },
    getEditableFields : function() {
    	return [
                {
                	xtype: 'checkboxfield',
    				boxLabel: 'كل سازمانها',
    				boxLabelAlign: 'after',
//    				labelWidth:'10',
    				id: EmsObjectName.messages.isAll,
    				name: EmsObjectName.messages.isAll,
    				disabled: false,
    				listeners: {

		        		change: Ext.bind(function ($event) {
		        			if($event.checked){
		        				Ext.getCmp(EmsObjectName.messages_ID.provinceName).setDisabled(true);
		        				Ext.getCmp('provinceMultiSelectds').getStore().removeAll();
		        				
		        				Ext.getCmp(EmsObjectName.messages_ID.departmentName).setDisabled(true);
		        				Ext.getCmp('departmentMultiSelectds').getStore().removeAll();
		        				
		        				Ext.getCmp(EmsObjectName.messages_ID.personName).setDisabled(true);
		        				Ext.getCmp('personMultiSelectds').getStore().removeAll();
		        				
		        				Ext.getCmp(EmsObjectName.messages.isAllOffice).setValue(true);

		        			}
		        			else{ 
		        				Ext.getCmp(EmsObjectName.messages_ID.provinceName).setDisabled(false);
		        				Ext.getCmp(EmsObjectName.messages_ID.departmentName).setDisabled(false);
		        				Ext.getCmp(EmsObjectName.messages_ID.personName).setDisabled(false);
		        				
		        				Ext.getCmp(EmsObjectName.messages.isOffice).setValue(false);
		        				Ext.getCmp(EmsObjectName.messages.isNocr).setValue(false);
		        				Ext.getCmp(EmsObjectName.messages.isManager).setValue(false);
		        			}
		        		}, this)
		        	
    				}
                },
                {
                	xtype: 'radiogroup',
            		id: '_idMessageProvinceForm',
            		border: false,
            		bodyBorder: false,
            		fieldLabel: 'نوع دفتر',
            		columns : 1,
            		items:[
            		       {boxLabel: 'همه', name: 'officeType', inputValue: 'ALLOFFICE', id: EmsObjectName.messages.isAllOffice,labelWidth:'5',checked:true},
            		       {boxLabel: 'خصوصی', name: 'officeType', inputValue: 'OFFICE', id: EmsObjectName.messages.isOffice,labelWidth:'5',},
            		       {boxLabel: 'دولتی', name: 'officeType', inputValue: 'NOCR', id: EmsObjectName.messages.isNocr,labelWidth:'5',}
            		       ]
                	
                },
                {
		        	xtype: 'checkboxfield',
		        	boxLabel: '  مدیر',
//		        	labelWidth:'5',
		        	boxLabelAlign: 'after',
		        	id: EmsObjectName.messages.isManager,
		        	name:EmsObjectName.messages.isManager,
		        	disabled: false
		        },
        
    	        ]}

 

    /**
     * Converts the enrollment office type property to an appropriate format to set on radio group
     *
     * @param value     The value in String, representing type of enrollment office
     */
//    setValue: function (value) {
//        if (value == "NOCR") {
//            this.callParent([
//                {officeType: "NOCR"}
//            ]);
//        } else if (value == "OFFICE") {
//            this.callParent([
//                {officeType: "OFFICE"}
//            ]);
//        }
//    }
});

