/**
 * A Model representing a report definition
 * User: E.Z.Moghaddam
 * Date: 7/18/12
 * Time: 3:48 PM
 * Representation of a report on EMS system
 */
Ext.define('Ems.model.ReportModel', {
    extend: 'Gam.data.model.Model',
    fields: [
        EmsObjectName.manageReports.name,
        EmsObjectName.manageReports.comment,
        EmsObjectName.manageReports.permission,
        EmsObjectName.manageReports.activationFlag
    ]
});
