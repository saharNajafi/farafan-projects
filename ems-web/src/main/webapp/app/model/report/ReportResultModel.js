/**
 * The model representing a report result item
 */
Ext.define('Ems.model.report.ReportResultModel', {
    extend: 'Gam.data.model.Model',
    fields: [
        EmsObjectName.reportResult.reportName,
        {name: EmsObjectName.reportResult.requestDate, type: 'date'},
        {name: EmsObjectName.reportResult.generateDate, type: 'date'},
        {name: EmsObjectName.reportResult.jobScheduleDate, type: 'date'},
        EmsObjectName.reportResult.requestState,
        EmsObjectName.reportResult.requestOutputType
    ]
});
