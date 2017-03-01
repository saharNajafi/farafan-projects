/**
 * Created by IntelliJ IDEA.
 * User: Mousavi
 * Date: 5/16/12
 * Time: 8:32 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.store.userRequestTokenReasonIssuanceStore', {
    extend: 'Ext.data.Store', //extend: 'Gam.data.store.Autocomplete',
    alias: 'store.userrequesttokenreasonissuancestore',
    model: 'Ems.model.ComboBoxSimpleModel',

    data: [
        {
            id: 1,
            code: 'N',
            name: 'صدوراولیه'
        },
        {
            id: 2,
            code: 'D',
            name: 'صدور مجدد به دلیل خرابی'
        },
        {
            id: 3,
            code: 'R',
            name: 'صدورالمثنی'
        }
    ]

});
