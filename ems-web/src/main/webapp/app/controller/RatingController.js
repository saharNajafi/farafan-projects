/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/3/12
 * Time: 2:10 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.controller.RatingController', {
    extend: 'Gam.app.controller.RowEditorBasedGrid',//'Gam.app.controller.LocalDialogBasedGrid',

    statics: {
        statefulComponents: [
            'wRatingGrid',
            'wRatingGridAdd',
            'wRatingGridDelete',
            'wRatingGridEdit'

        ]
    },

    ns: 'extJsController/rating',

    views: ['rating.Grid'],

    initViewType: 'ratinggrid',

    constructor: function (config) {

        this.callParent(arguments);
    }

});
