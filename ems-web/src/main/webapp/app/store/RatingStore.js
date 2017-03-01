/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/4/12
 * Time: 5:15 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.store.RatingStore', {
    extend: 'Gam.data.store.grid.Grid',

    alias: 'store.ratingstore',

    model: 'Ems.model.RatingModel',

    baseUrl: 'extJsController/user',

    listName: 'ratingInfoList'

});
