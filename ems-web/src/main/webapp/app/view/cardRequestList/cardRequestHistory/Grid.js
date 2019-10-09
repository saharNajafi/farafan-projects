/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/25/12
 * Time: 6:11 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.cardRequestList.cardRequestHistory.Grid', {
    extend: 'Gam.grid.Crud',
    alias: 'widget.cardrequesthistorygrid',
    requires: ['Ems.store.CardRequestHistoryStore'],
    store: {type: 'cardrequesthistorystore'},
    border: false,
    columns: [
        {
            dataIndex: EmsObjectName.cardRequestList.action,
            width: 250,
            text: 'عملیات',
            renderer: function (value, metaData, record) {
                var requestedAction = value;
                var result = record.get(EmsObjectName.cardRequestList.result);
                switch (value) {
                    case 'TRANSFER_FROM_PORTAL':
                        requestedAction = 'انتقال درخواست از سامانه پورتال';
                        break;
                    case 'IMS_SEND_ERROR':
                        requestedAction = 'خطا در ارسال به IMS';
                        break;
                    case 'IMS_RECEIVE_ERROR':
                        requestedAction = 'خطا در دریافت از IMS';
                        break;
                    case 'PENDING_IMS':
                        requestedAction = 'ارسال برای استعلام';
                        break;
                    case 'VERIFIED_IMS':
                        requestedAction = 'تایید استعلام';
                        break;
                    case 'NOT_VERIFIED_BY_IMS':
                        requestedAction = 'عدم تایید استعلام';
                        break;
                    case 'TRANSFER_RESERVE':
                        requestedAction = 'انتقال نوبت از سامانه پورتال';
                        break;
                    case 'COMPLETE_REGISTRATION':
                        requestedAction = 'تکمیل ثبت نام در دفتر پیشخوان';
                        break;
                    case 'AUTHENTICATE_DOCUMENT':
                        requestedAction = 'تایید مدارک (احراز اصالت مدارک)';
                        break;
                    case 'DOCUMENT_SCAN':
                        requestedAction = 'اسکن مدارک';
                        break;
                    case 'FINGER_SCAN':
                        requestedAction = 'اسکن اثر انگشت';
                        break;
                    case 'FACE_SCAN':
                        requestedAction = 'اسکن تصویر چهره';
                        break;
                    case 'MANAGER_APPROVAL':
                        requestedAction = 'تایید مدیر';
                        break;
                    case 'SENT_TO_AFIS':
                        requestedAction = 'ارسال برای EQC';
                        break;
                    case 'AFIS_SEND_ERROR':
                        requestedAction = 'خطا در ارسال به EQC';
                        break;
                    case 'AFIS_RECEIVE_ERROR':
                        requestedAction = 'خطا در دریافت از EQC';
                        break;
                    case 'APPROVED_BY_AFIS':
                        requestedAction = 'تایید شده توسط EQC';
                        break;
                    case 'PENDING_ISSUANCE':
                        requestedAction = 'ارسال به مدیریت کارت';
                        break;
                    case 'CMS_ERROR':
                        requestedAction = 'توقف به دلیل خطا در ارسال به مدیریت کارت';
                        break;
                    case 'ISSUE_CARD_ERROR_WITH_UNLIMITED_RETRY':
                        requestedAction = 'خطا در ارسال به مدیریت کارت';
                        break;
                    case 'BATCH_PRODUCTION':
                        requestedAction = 'اعلام تولید بسته حاوی کارت توسط سامانه مدیریت کارت';
                        break;
                    case 'BOX_SHIPMENT':
                        requestedAction = 'اعلام ارسال جعبه حاوی کارت از سامانه مدیریت کارت';
                        break;
                    case 'PRODUCTION_ERROR':
                        requestedAction = 'اعلام خطا در تولید کارت توسط سامانه مدیریت کارت';
                        break;
                    case 'BATCH_RECEIVED':
                        requestedAction = 'اعلام دریافت جعبه حاوی کارت به سامانه مدیریت کارت';
                        break;
                    case 'DELIVERED_TO_CITIZEN':
                        requestedAction = 'تحویل کارت به شهروند';
                        break;
                    case 'NOTIFY_CARD_DELIVER':
                        requestedAction = 'اعلام تحویل کارت شهروند به سامانه مدیریت کارت';
                        break;
                    case 'NOTIFY_CARD_MISSED':
                        requestedAction = 'اعلام گم شدن کارت به سامانه مدیریت کارت';
                        break;
                    case 'REPEAL_CARD_REQUEST':
                        if (result == 'REPEALING')
                            requestedAction = 'ثبت درخواست ابطال';
                        else if (result == 'REPEAL_UNDO')
                            requestedAction = 'بازگشت درخواست ابطال';
                        else if (result == 'REPEAL_ACCEPTED')
                            requestedAction = 'تایید درخواست ابطال';
                        else
                            requestedAction = 'ابطال درخواست';
                        break;
                    case 'TRANSFER_TO_SUPERIOR_OFFICE':
                        requestedAction = 'انتقال درخواست به دفتر معین مربوطه';
                        break;
                    case 'UNDO_TRANSFER_FROM_SUPERIOR_OFFICE':
                        requestedAction = 'بازگشت انتقال درخواست از دفتر معین مربوطه';
                        break;
                    case 'UNSUCCESSFUL_DELIVERY_BECAUSE_OF_IDENTITY_CHANGE':
                        requestedAction = 'تحویل ناموفق به دلیل تغییر اطلاعات هویتی';
                        break;
                    case 'UNSUCCESSFUL_DELIVERY_BECAUSE_OF_IS_FORBIDDEN':
                        requestedAction = 'تحویل ناموفق به دلیل ممنوعیت';
                        break;
                    case 'UNSUCCESSFUL_DELIVERY_BECAUSE_OF_IS_DIED':
                        requestedAction = 'تحویل ناموفق به دلیل فوت';
                        break;
                    case 'UNSUCCESSFUL_DELIVERY_BECAUSE_OF_DAMAGE':
                        requestedAction = 'تحویل ناموفق به دلیل خرابی';
                        break;
                    case 'UNSUCCESSFUL_DELIVERY_BECAUSE_OF_BIOMETRIC':
                        requestedAction = 'تحویل ناموفق بدلیل تغییر اطلاعات بیومتریک';
                        break;
                    case 'IMS_IMAGE_NOT_FOUND':
                        requestedAction = 'تصویر در پایگاه داده استعلام یافت نشد';
                        break;
                    case 'IMS_ESTELAM_RETRY':
                        requestedAction = 'تلاش مجدد برای استعلام گیری';
                        break;
                    case 'AFIS_DOCUMENT_ERROR':
                        requestedAction = 'خطای سند در دریافت اطلاعات شهروند از EQC';
                        break;
                    case 'AFIS_IMAGE_ERROR':
                        requestedAction = 'خطای تصویر در دریافت اطلاعات شهروند از EQC';
                        break;
                    case 'AFIS_FINGER_ERROR':
                        requestedAction = 'خطای اثر انگشت در دریافت اطلاعات شهروند از EQC';
                        break;
                    case 'AFIS_IMAGES_UPDATED':
                        requestedAction = 'اصلاح تصویر چهره توسط سامانه EQC';
                        break;
                    case 'AFIS_REJECT':
                        requestedAction = 'رد شده از سمت EQC';
                        break;                   
                    case 'REJECT_FACE_INFO_BY_MANAGER':
                        requestedAction = 'عدم تایید اطلاعات تصویر چهره توسط مدیر';
                        break;
                    case 'REJECT_DOC_INFO_BY_MANAGER':
                        requestedAction = 'عدم تایید اطلاعات مدارک توسط مدیر';
                        break;
                    case 'AFIS_DELIVERED_ERROR':
                        requestedAction = 'خطا در اعلام تحویل به IMS';
                        break;
                    case 'UNSUCCESSFUL_DELIVERY_BECAUSE_OF_CFINGER':
                        requestedAction = 'عدم تطابق انگشتان قطع شده';
                        break;
                    case 'UNSUCCESSFUL_DELIVERY_BECAUSE_OF_FINGER_IMAGE':
                        requestedAction = 'عدم تطابق انگشتان قطع شده و تصویر چهره';
                        break;
                    case 'UNSUCCESSFUL_DELIVERY_BECAUSE_OF_IMAGE':
                        requestedAction = 'عدم تطابق تصویر چهره';
                        break;
                    case 'UNSUCCESSFUL_DELIVERY_BECAUSE_OF_FINGER':
                        requestedAction = 'عدم تطابق انگشتان';
                        break;

                }

                return requestedAction;
            }
        },
        {
            xtype: 'gam.datecolumn',
            dataIndex: EmsObjectName.cardRequestList.date,
            format: Ext.Date.defaultDateTimeFormat,
            text: 'تاریخ'
        },
        {
            dataIndex: EmsObjectName.cardRequestList.actor,
            text: 'کاربر'
        },
        {
            dataIndex: EmsObjectName.cardRequestList.systemId,
            text: 'کد سیستمی',
            width: 150,
            renderer: function (value) {
                switch (value) {
                    case "CCOS":
                        return 'نرم افزار دفتر پیشخوان';
                    case "CMS":
                        return 'سامانه مدیریت کارت';
                    case "IMS":
                        return 'سامانه مدیریت هویت';
                    case "PORTAL":
                        return 'سامانه پورتال';
                    case "PKI":
                        return 'سامانه زیرساخت کلید عمومی';
                    case "JOB":
                        return 'اجرای کار (Job)';
                    case "EMS":
                        return 'سامانه پشتیبانی خدمات';

                }
            }
        },
        {
            dataIndex: EmsObjectName.cardRequestList.cmsRequestId,
            width: 200,
            text: 'کد درخواست'
        },
        {
            dataIndex: EmsObjectName.cardRequestList.result,
            width: 600,
            text: 'نتیجه'
        }
    ],
    initComponent: function () {
        this.callParent(arguments);
    }

});
