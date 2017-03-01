/**
 * The model representing a report result item
 */
Ext.define('Ems.model.ReportResultModel', {
    extend: 'Gam.data.model.Model',
    fields: [
        EmsObjectName.reportResponse.g_title,
        EmsObjectName.reportResponse.g_count
    ]
});
