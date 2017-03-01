/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/4/12
 * Time: 5:14 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.rating.Grid', {
    extend: 'Gam.grid.RowEditingCrud',

    alias: 'widget.ratinggrid',

    stateId: 'wRatingGrid',

    title: 'مدیریت رتبه',

    requires: [ 'Ems.store.RatingStore'  ],

    store: {type: 'ratingstore'},

    actions: ['gam.add', 'gam.delete' ],
    actionColumnItems: [ 'edit' ],
    contextMenu: [ 'gam.add', 'gam.delete' ],
    tbar: ['gam.add', 'gam.delete' ],

    initComponent: function () {
        this.columns = this.getItemsRatingGridForm();
        this.callParent(arguments);
    },
    getItemsRatingGridForm: function () {
        return([
            {
                xtype: 'gridcolumn',
                width: 150,
                text: 'رتبه',
                dataIndex: EmsObjectName.Rating.clazz,
                id: EmsObjectName.Rating.clazz,
                filterable: true,
                filter: true,
                editor: {
                    allowBlank: false,
                    xtype: 'textfield'
                }
            },
            {
                xtype: 'gridcolumn',
                text: 'ظرفیت',
                align: 'center',
                dataIndex: EmsObjectName.Rating.size,
                id: EmsObjectName.Rating.size,
                filterable: true,
                filter: true,
                editor: {
                    allowBlank: false,
                    xtype: 'textfield',
                    maskRe: /\d/i,
                    maxLength: 3,
                    enforceMaxLength: 3
                }
            }
        ]);
    }

});


