/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 6/6/12
 * Time: 10:41 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.dispatch.parent.Grid', {
    extend: 'Gam.grid.Crud',

    requires: ['Ems.view.dispatch.parent.DispatchStatusCombobox'],

    stateId: 'parentDispatchGrid',

    initComponent: function () {
        this.columns = this.getColumns();
        this.callParent(arguments);
    }, getColumns: function () {/**/
    }, getMySendDateBox: function () { /**/
    }, getBatchNumber: function () {/**/
    }, ActionColumnDispatch: function () {
        return [
            this.getReceived(),
            this.getNotReceived(),
            this.getBackToInitialState(),
            this.getSend(),
            this.getLost(),
            this.getFind(),
            this.getUndoSend(),
            this.getSetBoxDetail()
        ];
    }, getReceived: function () {
        return({
            //icon:'resources/themes/images/dispath/itemReceived.png',// Receipt.png,
            tooltip: 'دریافت',
            action: 'received',
            style: 'margin-left:5px',
            stateful: true,
            stateId: 'wDispatchReceived',
            getClass: function (value, metadata, record) {
                var statusBox = record.get(EmsObjectName.dispatchGrid.statusBox),
                    typeReceiver = record.get(EmsObjectName.dispatchGrid.typeReceiver),
                    typeSend = record.get(EmsObjectName.dispatchGrid.typeSend);
                return (statusBox === 0 && typeReceiver == typeSend) ? 'girdAction-itemReceived-icon' : 'x-hide-display';
            }
        });
    }, getNotReceived: function () {
        return({
            // icon:'resources/themes/images/dispath/itemNotReceived.png',//NotReceipt.png,
            tooltip: 'عدم دریافت',
            action: 'notReceived',
            style: 'margin-left:5px',
            stateful: true,
            stateId: 'wDispatchNotReceived',
            getClass: function (value, metadata, record) {
                var statusBox = record.get(EmsObjectName.dispatchGrid.statusBox),
                    typeReceiver = record.get(EmsObjectName.dispatchGrid.typeReceiver),
                    typeSend = record.get(EmsObjectName.dispatchGrid.typeSend);
                return (statusBox === 0 && typeReceiver == typeSend) ? 'girdAction-itemNotReceived-icon' : 'x-hide-display';
            }
        });
    }, getBackToInitialState: function () {
        return({
            icon: 'resources/themes/images/dispath/backToInitialState.png',//BackToRecipt.png,
            tooltip: 'بازگشت به حالت آماده دریافت',
            action: 'backToInitialState',
            style: 'margin-left:5px',
            stateful: true,
            stateId: 'wDispatchBackToInitialState',
            getClass: function (value, metadata, record) {
                var statusBox = record.get(EmsObjectName.dispatchGrid.statusBox),
                    typeReceiver = record.get(EmsObjectName.dispatchGrid.typeReceiver),
                    typeSend = record.get(EmsObjectName.dispatchGrid.typeSend);
                return ((statusBox === 1 || statusBox === 2) && typeReceiver == typeSend ) ? 'girdAction-backToInitialState-icon' : 'x-hide-display';
            }
        });
    }, getLost: function () {
        return({
            // icon:'resources/themes/images/dispath/itemLost.png',//Loss.png,
            tooltip: 'گم شدن',
            action: 'lost',
            style: 'margin-left:5px',
            stateful: true,
            stateId: 'wDispatchLost',
            getClass: function (value, metadata, record) {
                var statusBox = record.get(EmsObjectName.dispatchGrid.statusBox),
                    typeReceiver = record.get(EmsObjectName.dispatchGrid.typeReceiver),
                    typeSend = record.get(EmsObjectName.dispatchGrid.typeSend);
                return (statusBox == 1 && typeReceiver == typeSend ) ? 'girdAction-itemLost-icon' : 'x-hide-display';
            }
        });
    }, getFind: function () {
        return({
            //icon:'resources/themes/images/dispath/itemFound.png',//find.png,
            tooltip: 'پیدا شدن',
            action: 'found',
            style: 'margin-left:5px',
            stateful: true,
            stateId: 'wDispatchFound',
            getClass: function (value, metadata, record) {
                var statusBox = record.get(EmsObjectName.dispatchGrid.statusBox),
                    typeReceiver = record.get(EmsObjectName.dispatchGrid.typeReceiver),
                    typeSend = record.get(EmsObjectName.dispatchGrid.typeSend);
                return (statusBox == 4 && typeReceiver == typeSend ) ? 'girdAction-itemFound-icon' : 'x-hide-display';
            }
        });
    }, getUndoSend: function () {
        return({
            // icon:'resources/themes/images/dispath/undoSend.png',//BackToSend.png,
            tooltip: 'بازگشت به حالت دریافت شده',
            action: 'undoSend',
            style: 'margin-left:5px',
            stateful: true,
            stateId: 'wDispatchUndoSent',
            getClass: function (value, metadata, record) {
                var statusBox = record.get(EmsObjectName.dispatchGrid.statusBox),
                    typeReceiver = record.get(EmsObjectName.dispatchGrid.typeReceiver),
                    typeSend = record.get(EmsObjectName.dispatchGrid.typeSend);
                lostDate = record.get(EmsObjectName.dispatchGrid.lostDateBox);
                return (statusBox === 3 && typeReceiver == typeSend && lostDate == null) ? 'girdAction-undoSend-icon' : 'x-hide-display';
            }
        });
    }, getSend: function () {
        return({
            //icon:'resources/themes/images/dispath/itemSend.png',//Send.png,
            tooltip: 'ارسال',
            action: 'send',
            style: 'margin-left:5px',
            stateful: true,
            stateId: 'wDispatchSent',
            getClass: function (value, metadata, record) {
                var statusBox = record.get(EmsObjectName.dispatchGrid.statusBox),
                    typeReceiver = record.get(EmsObjectName.dispatchGrid.typeReceiver),
                    typeSend = record.get(EmsObjectName.dispatchGrid.typeSend);

                return ((statusBox === 1 ) && typeReceiver == typeSend) ? 'girdAction-itemSend-icon' : 'x-hide-display';
            }
        });
    }, getSetBoxDetail: function () {
        return ({
            // icon:'resources/themes/images/dispath/setBoxDetail.png',
            tooltip: 'تعیین وضعیت دسته ها',
            action: 'setBoxDetail',
            style: 'margin-left:5px',
            stateful: true,
            stateId: 'wDispatchSetBoxDetail',
            getClass: function (value, metadata, record) {
                var statusBox = record.get(EmsObjectName.dispatchGrid.statusBox),
                    typeReceiver = record.get(EmsObjectName.dispatchGrid.typeReceiver),
                    typeSend = record.get(EmsObjectName.dispatchGrid.typeSend);

                return ( typeReceiver != typeSend) ? 'girdAction-setBoxDetail-icon' : 'x-hide-display';
            }
        });
    }

});
