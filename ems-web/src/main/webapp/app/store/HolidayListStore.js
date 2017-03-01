/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/25/12
 * Time: 12:00 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.store.HolidayListStore', {
    extend: 'Gam.data.store.grid.Grid',

    alias: 'store.holidayliststore',

    id: 'idHolidayListStore',

    listName: 'holidayList',

    model: 'Ems.model.HolidayListModel',

    baseUrl: 'extJsController/holidaylist'

});

