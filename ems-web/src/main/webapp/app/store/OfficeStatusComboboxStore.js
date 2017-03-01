/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/11/12
 * Time: 4:39 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.store.OfficeStatusComboboxStore', {
    extend: 'Ext.data.Store',
    alias: 'store.officestatuscomboboxstore',
    model: 'Ems.model.ComboBoxSimpleModel',

    data: [
        {
            id: 1,
            code: 'READY_TO_ISSUE',
            name: 'آماده صدور'
        },
        {
            id: 2,
            code: 'PENDING_TO_ISSUE',
            name: 'در دست صدور'
        },
        {
            id: 3,
            code: 'READY_TO_DELIVER',
            name: 'آماده تحویل'
        },
        {
            id: 4,
            code: 'DELIVERED',
            name: 'تحویل شد'
        }
    ]

});
