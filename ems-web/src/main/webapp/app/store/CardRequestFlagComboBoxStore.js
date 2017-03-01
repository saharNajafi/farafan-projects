/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
Ext.define('Ems.store.CardRequestFlagComboBoxStore', {
    extend: 'Ext.data.Store',
    alias: 'store.cardrequestflagcomboboxstore',
    model: 'Ems.model.ComboBoxSimpleModel',
    data: [
        {
            id: 1,
            code: 'true',
            name: 'دارد'
        },
        {
            id: 2,
            code: 'false',
            name: 'ندارد'
        }
    ]
});

