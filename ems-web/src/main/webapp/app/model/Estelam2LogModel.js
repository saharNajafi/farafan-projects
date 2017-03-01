/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/25/12
 * Time: 11:56 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.model.Estelam2LogModel', {
    extend: 'Gam.data.model.Model',
    fields: [
        EmsObjectName.estelam2FalseList.id
        , {name: EmsObjectName.estelam2FalseList.createDate, type: 'date'}
        , EmsObjectName.estelam2FalseList.perName
        , EmsObjectName.estelam2FalseList.action
    ]
});
