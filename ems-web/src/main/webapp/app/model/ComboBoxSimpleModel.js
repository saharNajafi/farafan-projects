/**
 * Created by IntelliJ IDEA.
 * User: Mousavi
 * Date: 5/16/12
 * Time: 8:22 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define("Ems.model.ComboBoxSimpleModel", {
    extend: 'Gam.data.model.comboBox.Simple',//  "Ext.data.Model",   //
    fields: [
        {type: 'string', name: EmsObjectName.comboBox.code},
        {type: 'string', name: EmsObjectName.comboBox.name}
    ]
});