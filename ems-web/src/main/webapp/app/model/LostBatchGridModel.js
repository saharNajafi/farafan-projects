/**
 * Created by IntelliJ IDEA.
 * User: Mousavi
 * Date: 4/28/12
 * Time: 9:18 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.model.LostBatchGridModel', {
    extend: 'Gam.data.model.Model',

    fields: [
        {name: EmsObjectName.lostBatch.batchId, type: 'string'},
        {name: EmsObjectName.lostBatch.cmsID, type: 'string'},
        {name: EmsObjectName.lostBatch.departmentName, type: 'string'},
        {name: EmsObjectName.lostBatch.batchLostDate, type: 'date'},
        {name: EmsObjectName.lostBatch.isConfirm, type: 'string'}
    ]
});

