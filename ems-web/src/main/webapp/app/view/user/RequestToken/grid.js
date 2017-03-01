/**
 * Created by IntelliJ IDEA.
 * User: Mousavi
 * Date: 5/2/12
 * Time: 6:11 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.user.RequestToken.grid', {
    extend: 'Gam.grid.Crud',

    //stateId:'UserRequestTokenGrid',

    alias: 'widget.userrequesttoken',

    requires: [
        'Ems.store.userRequestTokenGridStore',
        'Ems.view.user.RequestToken.tokenStateCombo',
        'Ems.view.user.RequestToken.tokenTypeCombo'
    ],


    id: 'iduserrequesttoken',

    title: 'لیست توکن های تخصیص داده شده',

    height: 210,

    store: {type: 'userrequesttokengridstore'},

    actionColumnItems: [
        {
            //icon:'resources/themes/images/TokenIcon/tahvil.gif',
            tooltip: 'تحویل توکن',
            action: 'readyToDeliver',
            getClass: function (value, metadata, record) {
                var result = record.get(EmsObjectName.userRequestToken.status);
                return(result === Tools.trim("READY_TO_DELIVER") ) ? 'girdAction-tahvil-icon' : 'x-hide-display';
            }
        },
        {
            //icon:'resources/themes/images/TokenIcon/tamdid.png',
            tooltip: 'تمدید توکن',
            action: 'delivered',
            getClass: function (value, metadata, record) {
                var result = record.get(EmsObjectName.userRequestToken.status);
                return(result === Tools.trim("DELIVERED") ) ? 'girdAction-tamdid-icon' : 'x-hide-display';
            }
        },
        {
            // icon:'resources/themes/images/TokenIcon/deleteToken.png',
            tooltip: 'ابطال توکن',
            action: 'revoke',
            getClass: function (value, metadata, record) {
                var result = Tools.trim(record.get(EmsObjectName.userRequestToken.status));
                return((result === Tools.trim("DELIVERED")) || (result === Tools.trim("READY_TO_DELIVER"))) ? 'girdAction-deleteToken-icon' : 'x-hide-display';
            }
        },
        {
            // icon:'resources/themes/images/TokenIcon/deleteToken.png',
            tooltip: 'حذف درخواست صدور توکن',
            action: 'delete',
            getClass: function (value, metadata, record) {
                var result = Tools.trim(record.get(EmsObjectName.userRequestToken.status));
                if (result == 'READY_TO_ISSUE') {
                    return 'grid-undo-person-token-action-icon';
                }

                return 'grid-action-hidden';
            }
        }
    ],
    initComponent: function () {

        this.columns = this.getColumnUserRequestToken();
        this.callParent(arguments);

    }, getColumnUserRequestToken: function () {
        return([
            {
                xtype: 'gridcolumn',
                width: 150,
                text: 'نوع توکن',
                dataIndex: EmsObjectName.userRequestToken.code,
                filter: {
                    xtype: 'tokentypecombo'
                },
                renderer: function (value) {
                    if (value && typeof value === 'string') {
                        switch (value) {
                            case "AUTHENTICATION":
                                return 'احراز هویت';
                                break;
                            case "SIGNATURE":
                                return 'امضا';
                                break;
                            case "ENCRYPTION":
                                return 'رمزنگاری';
                                break;
                            default :
                                break;
                        }
                    }
                }
            },
            {
                xtype: 'gridcolumn',
                width: 150,
                text: 'وضعیت',
                filter: {
                    xtype: 'tokenstatecombo'
                },
                dataIndex: EmsObjectName.userRequestToken.status,
                renderer: function (value) {
                    if (value && typeof value === 'string') {
                        switch (value) {
                            case "READY_TO_ISSUE":
                                return 'آماده صدور';
                                break;
                            case "PENDING_TO_ISSUE":
                                return 'در دست صدور';
                                break;
                            case "READY_TO_DELIVER":
                                return 'آماده تحویل';
                                break;
                            case "DELIVERED":
                                return 'تحویل شد';
                                break;
                            case "REVOKED":
                                return 'ابطال شده';
                                break;
                            case "PENDING_FOR_EMS":
                                return 'منتظر تایید EMS';
                                break;
                            case "READY_TO_RENEWAL_ISSUE":
                                return 'آماده صدور';
                                break;
                            case "PENDING_TO_RENEWAL_ISSUE":
                                return 'در دست صدور';
                                break;
                            case "READY_TO_RENEWAL_DELIVER":
                                return 'آماده تحویل';
                                break;
                            case "EMS_REJECT":
                                return 'نایید نشده';
                                break;
                            case "SUSPENDED":
                                return 'آماده فعالسازی';
                                break;
                            default :
                                break;
                        }
                    }
                }
            }
        ]);
    }
});

