/**
 * As the value of 'officeType' field is String, and the 'setValue' method of RadioGroup needs a parameter of type
 * Object, this class would be used as a wrapper to convert the provided String value to an appropriate object
 * format to be used on form
 *
 * User: Moghaddam
 * Date: 4/13/13
 * Time: 2:35 PM
 * NOCR Office type selection radio group
 */
Ext.define('Ems.view.office.NewEdit.OfficeTypeRadioGroup', {
    extend: 'Ext.form.RadioGroup',
    alias: 'widget.officetyperadiogroup',

//    allowBlank: false,

    items : [ {
		boxLabel : 'اداره ثبت احوال',
		name : 'khosusiType',
		inputValue : 'NOCR',
		id : EmsObjectName.officeNewEdit.officeTypeNOCR
	}, {
		boxLabel : 'دفتر پیشخوان',
		name : 'khosusiType',
		inputValue : 'OFFICE',
		id : EmsObjectName.officeNewEdit.officeTypeOffice
	}, {
		boxLabel : 'دفتر پست',
		name : 'khosusiType',
		inputValue : 'POST',
		id : EmsObjectName.officeNewEdit.officeTypePost
	} ],

    /**
     * Converts the enrollment office type property to an appropriate format to set on radio group
     *
     * @param value     The value in String, representing type of enrollment office
     */
	//change by khodayari
    setValue: function (value) {
    	if (value == "0") {
			this.callParent([ {
				khosusiType : "NOCR"
			} ]);
		} else if (value == "1") {
			this.callParent([ {
				khosusiType : "OFFICE"
			} ]);
		} else if (value == "2") {
			this.callParent([ {
				khosusiType : "POST"
			} ]);
		}
		else if (value == "NOCR") {
			this.callParent([ {
				khosusiType : "NOCR"
			} ]);
		} else if (value == "OFFICE") {
			this.callParent([ {
				khosusiType : "OFFICE"
			} ]);
		} else if (value == "POST") {
			this.callParent([ {
				khosusiType : "POST"
			} ]);
		}
    	EmsObjectName._oldOfficeValues.setKhosusiType = value;
    }
});

