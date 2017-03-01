/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 2/6/12
 * Time: 3:21 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Scp.state.Provider
 * @extends Gam.state.RemoteProvider
 */

Ext.define('Ems.state.Provider', {

    extend: 'Gam.state.RemoteProvider',

    name: 'Ems',

    stateful: true,

    throttled: true,

    initComponent: function () {
        var me;
        this.callParent(arguments);
    },

    setDetail: function (stateId, value, lgth) {

        var pos, row;

        if ((pos = this.store.find('stateId', stateId)) > -1) {
            row = this.store.getAt(pos);
            row.set('value', this.encodeValue(value));
        }
        else {

            this.store.add({
                stateId: stateId,
                value: this.encodeValue(value)
            });
        }

        me = this;

    },

    saveing: function () {
        var store = me.store;
        store.sync();

        Ext.each(store.data.items, function (item) {
            this.fireEvent('statechange', me, item.data.stateId, item.data.value);
        });
        //view.items.items[store.length-1].stateful= false;
    },
    onLoadModuleNavigation: function (moduleName, statefulComponents) {
        /*
         var getmodelclassname=this.getModuleClassName(moduleName + 'Controller', 'controller');
         var controllerClass =  Ext.ClassManager.get(getmodelclassname);
         */

        if (this.stateful) {

            Ext.state.Manager.getProvider();
            this.load(moduleName, statefulComponents);

        }

    },

    getModuleClassName: function (name, type) {
        var namespace = Ext.Loader.getPrefix(name);

        if (namespace.length > 0 && namespace !== name) {
            return name;
        }

        return this.name + '.' + type + '.' + name;
    }
});