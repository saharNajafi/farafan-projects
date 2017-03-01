/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/18/12
 * Time: 3:48 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.model.ReportRequestModel', {
    extend: 'Gam.data.model.Model',
    fields: [
        EmsObjectName.reportRequest.name,
        EmsObjectName.reportRequest.comment
    ]
});
