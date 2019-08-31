/**
 * Created by IntelliJ IDEA.
 * User: Mousavi
 * Date: 5/2/12
 * Time: 6:13 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.model.userRequestTokenGridModel', {
    extend: 'Gam.data.model.Model',

    // fields:["id","tokenType","tokenState"]

    fields: [
        {
            name: EmsObjectName.userRequestToken.status    // 1->Dar Daste Sodor  , 2->Amade Eghdam  ,  3->Tahvil Shodeh
        },
        {
            name: EmsObjectName.userRequestToken.code       //0->Ehraz   , 1->Emza  ,  2->Ramznegari
        }
        ,
        {
            name: EmsObjectName.userRequestToken.deleteToken
        }
        ,
        {
            name: EmsObjectName.userRequestToken.reason
        }
    ]
});
