/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/11/12
 * Time: 6:27 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.store.DispatchStatusStoreComboboxStore', {
    extend: 'Ext.data.Store',
    alias: 'store.dispatchstatusstorecomboboxstore',
    model: 'Ems.model.ComboBoxSimpleModel',

    data: [
        {
            id: 1,
            code: '0',
            name: 'آماده دريافت'
        },
        {
            id: 2,
            code: '1',
            name: 'دريافت شده'
        },
        {
            id: 3,
            code: '2',
            name: 'دريافت نشده'
        },
        {
            id: 4,
            code: '3',
            name: 'ارسال شده'
        },
        {
            id: 5,
            code: '4',
            name: 'گم شده'
        },
        {
            id: 6,
            code: '5',
            name: 'دریافت شده در دفتر'
        }
    ]

});
