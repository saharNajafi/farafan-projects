/**
 * Created by IntelliJ IDEA.
 * User: Mousavi
 * Date: 4/29/12
 * Time: 4:41 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define("Ems.model.userPostGridModel", {
    extend: "Ext.data.Model",
    fields: [
        {type: 'string', name: 'code'},
        {type: 'string', name: 'name'}
    ]
});
