/**
 * Created with IntelliJ IDEA.
 * User: a.amiri
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.model.BatchListModel', {
    extend: 'Gam.data.model.Model',
    fields: [
        EmsObjectName.BatchList.batchId
        , EmsObjectName.BatchList.cmsID
        , EmsObjectName.BatchList.departmentName
        , EmsObjectName.BatchList.isConfirm
        , {name: EmsObjectName.BatchList.batchLostDate, type: 'date'}

    ]
});
