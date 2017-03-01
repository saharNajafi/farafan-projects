/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/8/12
 * Time: 5:04 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.model.BizModel', {
    extend: 'Gam.data.model.Model',
    fields: [
        {name: 'date', type: 'date'},
        'actor',
        'actionNameStr',
        'entityNameStr',
        'entityID',
        'additionalData',
        'status',

        'verify'
//        { name: EmsObjectName.bizLog.date, type: 'date' },
//        EmsObjectName.bizLog.actor,
//        EmsObjectName.bizLog.actionNameStv,
//        EmsObjectName.bizLog.entityNameStv,
//        EmsObjectName.bizLog.entityID,
//        EmsObjectName.bizLog.additionalData,
//        EmsObjectName.bizLog.status
    ]
});
