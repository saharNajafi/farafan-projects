/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/27/12
 * Time: 3:55 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.store.JobListStore', {
    extend: 'Gam.data.store.grid.Grid',

    alias: 'store.jobliststore',

    listName: 'jobList',

    model: 'Ems.model.JobListModel',

    baseUrl: 'extJsController/jobs'

});
