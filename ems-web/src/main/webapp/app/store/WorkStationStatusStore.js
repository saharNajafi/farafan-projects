/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/11/12
 * Time: 5:43 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.store.WorkStationStatusStore', {
    extend: 'Ext.data.Store',
    alias: 'store.workstationstatusstore',
    model: 'Ems.model.ComboBoxSimpleModel',

    data: [
        {
            id: 1,
            code: 'N',
            name: 'جدید'
        },
        {
            id: 2,
            code: 'A',
            name: 'تایید شد'
        },
        {
            id: 3,
            code: 'R',
            name: 'عدم تایید'
        }
    ]

});
