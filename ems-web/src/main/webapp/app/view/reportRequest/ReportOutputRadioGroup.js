/**
 * This class would be used as a wrapper to display report output types (pdf, excel, etc) on report parameters dialogue
 *
 * User: Moghaddam
 * Date: 5/20/13
 * Time: 11:26 AM
 *
 * Report output type radio group
 */
Ext.define('Ems.view.reportRequest.ReportOutputRadioGroup', {
    extend: 'Ext.form.RadioGroup',
    alias: 'widget.reportoutputradiogroup',

    forceSelection: true,

    items: [
        {
            boxLabel: EmsObjectName.reportRequest.pdfLabel,
            name: 'reportOutputType',
            inputValue: 'PDF',
            id: EmsObjectName.reportRequest.pdf,
            labelWidth: 30,
            labelAlign: 'left',
            checked: true
        },
        {
            boxLabel: EmsObjectName.reportRequest.xlsLabel,
            name: 'reportOutputType',
            inputValue: 'XLS',
            id: EmsObjectName.reportRequest.xls,
            labelAlign: 'left',
            labelWidth: 30
        }
    ]
//    ,
//
//    /**
//     * Converts the enrollment office type property to an appropriate format to set on radio group
//     *
//     * @param value     The value in String, representing type of enrollment office
//     */
//    setValue: function(value){
//        if(value == "NOCR"){
//            this.callParent([{officeType: "NOCR"}]);
//        } else if(value == "OFFICE"){
//            this.callParent([{officeType: "OFFICE"}]);
//        }
//    }
});

