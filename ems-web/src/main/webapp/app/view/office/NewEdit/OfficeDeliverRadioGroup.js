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
Ext.define('Ems.view.office.NewEdit.OfficeDeliverRadioGroup', {
    extend: 'Ext.form.RadioGroup',
    alias: 'widget.officedeliverradiogroup',
    			   

//    allowBlank: false,

    items: [
        {boxLabel: 'قابلیت تحویل دارد', name: 'officeDeliver', inputValue: '1', id: EmsObjectName.officeNewEdit.officeDeliverEnable},
        {boxLabel: 'قابلیت تحویل ندارد', name: 'officeDeliver', inputValue: '0', id: EmsObjectName.officeNewEdit.officeDeliverDiable}
    ],

    /**
     * Converts the enrollment office type property to an appropriate format to set on radio group
     *
     * @param value     The value in String, representing type of enrollment office
     */
    setValue: function (value) {
        if (value == "1") {
            this.callParent([
                {officeDeliver: "1"}
            ]);
        } else if (value == "0") {
            this.callParent([
                {officeDeliver: "0"}
            ]);
        }
    }
});

