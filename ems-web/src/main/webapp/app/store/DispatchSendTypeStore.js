/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/9/12
 * Time: 1:10 PM
 * To change this template use File | Settings | File Templates.
 */

Ext.define('Ems.store.DispatchSendTypeStore', {
    extend: 'Ext.data.Store',
    alias: 'store.dispatchsendtypestore',
    model: 'Ems.model.ComboBoxSimpleModel',

    data: [
        {
            id: 1,
            code: 'BOX',
            name: 'جعبه'
        },
        {
            id: 2,
            code: 'BATCH',
            name: 'دسته'
        }
    ]

});
