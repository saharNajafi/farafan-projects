/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/27/12
 * Time: 3:56 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.model.JobListModel', {
    extend: 'Gam.data.model.Model',
    fields: [
        EmsObjectName.job.name
        , EmsObjectName.job.description
        , EmsObjectName.job.cron
        , EmsObjectName.job.state
        , EmsObjectName.job.cronState
        , EmsObjectName.job.simpleState
        , {name: EmsObjectName.job.previousFireTime, type: 'date'}
        , {name: EmsObjectName.job.nextFireTime, type: 'date'}
    ]
});
