/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/27/12
 * Time: 3:42 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.jobList.Grid', {

    extend: 'Gam.grid.RowEditingCrud',

    alias: 'widget.jobsgrid',

    stateId: 'wJobsGrid',

    title: 'مدیریت کارها',

    multiSelect: false,

    requires: ['Ems.store.JobListStore'],

    store: {type: 'jobliststore'},

    pagingEnabled: false,

    actionColumnItems: [  'edit' ,
        {
            action: 'pause',
            id: 'pause',
            stateful: true,
            stateId: this.stateId + 'Pause',
            tooltip: 'توقف دائم',
            getClass: function (value, metadata, record) {
                var cronState = record.get(EmsObjectName.job.cronState);
                return ((cronState === "NORMAL") || (cronState === "BLOCKED")) ? 'girdAction-pause-icon' : 'x-hide-display';
            }
        }, {
            action: 'resume',
            id: 'resume',
            stateful: true,
            stateId: this.stateId + 'Resume',
            tooltip: 'از سرگیری مجدد',
            getClass: function (value, metadata, record) {
                var cronState = record.get(EmsObjectName.job.cronState);
                return ((cronState === "PAUSED")) ? 'girdAction-resume-icon' : 'x-hide-display';
            }
        }, {
            action: 'play',
            id: 'play',
            tooltip: 'اجرا',
            stateful: true,
            stateId: this.stateId + 'Play',
            getClass: function (value, metadata, record) {
                var result = record.get(EmsObjectName.job.state);
                return ((result === "NORMAL") || (result === "PAUSED")) ? 'girdAction-playRTL-icon' : 'x-hide-display';
            }
        }, {
            action: 'interrupt',
            id: 'interrupt',
            tooltip: 'توقف اجرا',
            stateful: true,
            stateId: this.stateId + 'Interrupt',
            getClass: function (value, metadata, record) {
                var result = record.get(EmsObjectName.job.state);
                return ((result === "BLOCKED") || (result === "COMPLETE")) ? 'girdAction-interrupt-icon' : 'x-hide-display';
            }
        }],

    initComponent: function () {
        this.columns = this.getItemsGridForm();

        //  Add a simple toolbar containing a refresh button. No paging is required for jobs list
        this.dockedItems = [
            {
                xtype: 'toolbar',
                dock: 'bottom',
                items: [
                    {
                        cls: 'x-btn x-btn-icon x-btn-default-toolbar-small-icon',
                        iconCls: 'x-tbar-loading',
                        handler: Ext.bind(function () {
                            this.getStore().load();
                        }, this)
                    }
                ]
            }
        ];

        this.callParent(arguments);
    },

    getItemsGridForm: function () {
        return([
            {
                dataIndex: EmsObjectName.job.name,
                id: EmsObjectName.job.name,
                text: 'نام',
                width: 230
            },
            {
                dataIndex: EmsObjectName.job.description,
                id: EmsObjectName.job.description,
                text: 'توضیحات',
                tdCls: 'wrap',
                width: 450
            },
            {
                dataIndex: EmsObjectName.job.state,
                id: EmsObjectName.job.state,
                text: 'وضعیت',
                renderer: function (value) {
                    if (value && typeof value === 'string')
                        return EmsResource.jobList.Status[value];
                }

            },
            {
                dataIndex: EmsObjectName.job.cron,
                id: EmsObjectName.job.cron,
                text: 'الگوی اجرا',
                tdCls: 'ltrStyle',
                align: 'left',
                editor: {
                    allowBlank: true,
                    xtype: 'textfield'
                }
            },
            {
                xtype: 'gam.datecolumn',
                dataIndex: EmsObjectName.job.previousFireTime,
                id: EmsObjectName.job.previousFireTime,
                text: 'زمان اجرای قبلی',
                hidden: true,
                format: Ext.Date.defaultDateTimeFormat
            },
            {
                xtype: 'gam.datecolumn',
                dataIndex: EmsObjectName.job.nextFireTime,
                id: EmsObjectName.job.nextFireTime,
                text: 'زمان اجرای بعدی',
                hidden: true,
                format: Ext.Date.defaultDateTimeFormat
            }
        ]);

    }
});
