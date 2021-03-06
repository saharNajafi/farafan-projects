/**
 * Created by IntelliJ IDEA.
 * User: Mousavi
 * Date: 5/9/12
 * Time: 12:49 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.user.view.access', {
    extend: 'Ext.view.View',

    alias: 'widget.userviewaccess',

    requires: ['Ems.store.userAccessGridStore'],

    store: {type: 'useraccessmultiselectstore'},//'useraccessgridsotre'},

    initComponent: function () {
        //this.config=config || {};
        this.callParent(arguments);
    },


    //tpl : Ext.create('Ext.XTemplate', '<div class="tooltip"><div>name</div></div>', {compiled: true}),

    tpl: new Ext.XTemplate(
        '<tpl for=".">',
        '<ol>',
        '<li>{name}</li>',
        '</ol><br/>',
        '</tpl>'
    ),
    //itemSelector: 'div.thumb-wrap',

    emptyText: 'مجاز به دسترسی نمی باشد'

});

