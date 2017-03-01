/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/7/12
 * Time: 5:30 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.model.BlackListModel', {
    extend: 'Gam.data.model.Model',
    fields: [
        EmsObjectName.blackList.firstName,
        EmsObjectName.blackList.lastName,
        EmsObjectName.blackList.nid
    ]
});
