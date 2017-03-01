/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/7/12
 * Time: 9:44 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.model.OfficeUserListModel', {
    extend: 'Gam.data.model.Model',
    fields: [
        {name: EmsObjectName.suerList.firstName},
        {name: EmsObjectName.suerList.lastName},
        {name: EmsObjectName.suerList.nid},
        {name: EmsObjectName.suerList.username}

        // {name:'address', type:'string'}


    ]
});
