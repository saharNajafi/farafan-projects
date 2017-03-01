/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/25/12
 * Time: 5:16 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.store.CardStateComboBoxStore', {
    extend: 'Ext.data.Store',
    alias: 'store.cardstatecomboboxstore',
    model: 'Ems.model.ComboBoxSimpleModel',
    data: [
        {
            id: 1,
            code: 'SHIPPED',
            name: 'در حال توزیع'
        },
        {
            id: 2,
            code: 'RECEIVED',
            name: 'دریافت شده'
        },
        {
            id: 3,
            code: 'MISSED',
            name: 'گم شده'
        },
        {
            id: 4,
            code: 'DELIVERED',
            name: 'تحویل شده'
        },
        {
            id: 5,
            code: 'REVOKED',
            name: 'ابطال شده'
        },
        {
            id: 6,
            code: 'LOST',
            name: 'مفقود شده'
        }
    ]
});
