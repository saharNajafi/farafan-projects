/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/25/12
 * Time: 11:52 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.model.CmsErrorEvaluateListModel', {
    extend: 'Gam.data.model.Model',
    fields: [
        EmsObjectName.cmsErrorEvaluateList.citizenFirstName
        , EmsObjectName.cmsErrorEvaluateList.citizenSurname
        , EmsObjectName.cmsErrorEvaluateList.citizenNId
        , EmsObjectName.cmsErrorEvaluateList.result
        , EmsObjectName.cmsErrorEvaluateList.cardRequestId
        , EmsObjectName.cmsErrorEvaluateList.citizenId
        , EmsObjectName.cmsErrorEvaluateList.cmsRequestId
        , EmsObjectName.cmsErrorEvaluateList.officeName        
        , EmsObjectName.cmsErrorEvaluateList.backDate
    ]
});
