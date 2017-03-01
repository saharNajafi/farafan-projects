/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 6/9/12
 * Time: 11:44 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.model.userMultiSelectModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'id'},
        {name: 'name'}
    ],
    idProperty: 'id'
});

