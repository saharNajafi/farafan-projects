/**
 * Created by IntelliJ IDEA.
 * User: Mousavi
 * Date: 5/16/12
 * Time: 2:30 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.store.userRequestTokenReasonCancelTokenStore', {
    extend: 'Ext.data.Store',
    alias: 'store.userrequesttokenreasoncanceltokenstore',
    require: ['Ems.model.ComboBoxSimpleModel'],
    model: 'Ems.model.ComboBoxSimpleModel',

    data: [
        {
            id: 1,
            code: 'L',
            name: 'گم شدن توکن'
        },
        {
            id: 2,
            code: 'D',
            name: 'خرابی  توکن'
        },
        {
            id: 3,
            code: 'E',
            name: 'سایر موارد'
        }
    ]
});
