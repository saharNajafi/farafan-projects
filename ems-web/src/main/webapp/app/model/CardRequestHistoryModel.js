/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/25/12
 * Time: 11:56 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.model.CardRequestHistoryModel', {
    extend: 'Gam.data.model.Model',
    fields: [
        EmsObjectName.cardRequestList.result
        , {name: EmsObjectName.cardRequestList.date, type: 'date'}
        , EmsObjectName.cardRequestList.systemId
        , EmsObjectName.cardRequestList.cmsRequestId
        , EmsObjectName.cardRequestList.action
        , EmsObjectName.cardRequestList.actor
    ]
});
