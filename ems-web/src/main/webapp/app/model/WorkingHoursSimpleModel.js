/**
 * Created by IntelliJ IDEA.
 * User: E.Z.Moghaddam
 * Date: 5/01/13
 * Time: 10:51 AM
 */

/**
 * The working hours combo box model should have a special model class, because its code property is of type 'float'
 * in spite of ComboboxSimpleModel class which has a string property named code. The type mismatch causes the setRecord
 * method not to fill this field properly
 */
Ext.define("Ems.model.WorkingHoursSimpleModel", {
    extend: 'Gam.data.model.comboBox.Simple',
    fields: [
        {type: 'float', name: EmsObjectName.comboBox.code},
        {type: 'string', name: EmsObjectName.comboBox.name}
    ]
});
