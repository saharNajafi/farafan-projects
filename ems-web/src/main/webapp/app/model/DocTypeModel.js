/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/7/12
 * Time: 3:53 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.model.DocTypeModel', {
    extend: 'Gam.data.model.Model',
    fields: [
        EmsObjectName.docType.service,
        EmsObjectName.docType.name
    ]
});
