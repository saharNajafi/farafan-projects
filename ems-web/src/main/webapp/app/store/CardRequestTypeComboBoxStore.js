/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/25/12
 * Time: 5:07 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.store.CardRequestTypeComboBoxStore', {
    extend: 'Ext.data.Store',
    alias: 'store.cardrequesttypecomboboxstore',
    model: 'Ems.model.ComboBoxSimpleModel',
    data: [
        {
            id: 1,
            code: 'FIRST_CARD',
            name: 'درخواست جدید'
        },
        {
            id: 2,
            code: 'EXTEND',
            name: 'تمدید'
        },
        {
            id: 3,
            code: 'REPLICA',
            name: 'المثنی'
        },
        {
            id: 4,
            code: 'REPLACE',
            name: 'تعویض'
        },
        {
            id: 5,
            code: 'UNSUCCESSFUL_DELIVERY',
            name: 'تحویل ناموفق'
        }
    ]
});

