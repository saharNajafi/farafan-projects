/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/31/12
 * Time: 2:49 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.model.SystemProfileListModel', {
    extend: 'Gam.data.model.Model',
    fields: [
        EmsObjectName.systemProfileList.keyName ,
        EmsObjectName.systemProfileList.parentKeyName ,
        EmsObjectName.systemProfileList.caption ,
        EmsObjectName.systemProfileList.value ,
        EmsObjectName.systemProfileList.childCount

    ]
});
