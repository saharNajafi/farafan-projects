/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/8/12
 * Time: 4:26 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.store.bizLogActionStore', {
    extend: 'Ext.data.Store', //extend: 'Gam.data.store.Autocomplete',
    alias: 'store.bizlogactionstore',
    model: 'Ems.model.ComboBoxSimpleModel',

    data: [
        {
            id: 1,
            code: 'INSERT',
            name: 'جدید'
        },
        {
            id: 2,
            code: 'UPDATE',
            name: 'ویرایش'
        },
        {
            id: 3,
            code: 'DELETE',
            name: 'حذف'
        },
        {
            id: 4,
            code: 'LOAD',
            name: 'مشاهده'
        },

        {
            id: 5,
            code: 'REMOVE',
            name: 'حذف'
        },

        {
            id: 6,
            code: 'APPROVE',
            name: 'تایید'
        },
        {
            id: 7,
            code: 'REJECT',
            name: 'عدم تایید'
        },
        {
            id: 8,
            code: 'CHANGE_STATE',
            name: 'تغییر وضعیت'
        },
        {
            id: 9,
            code: 'ISSUE',
            name: 'صدور'
        },
        {
            id: 10,
            code: 'REISSUE',
            name: 'صدور مجدد'
        },
        {
            id: 11,
            code: 'REPLICATE',
            name: 'المثنی'
        },
        {
            id: 12,
            code: 'REVOKE',
            name: 'ابطال'
        },
        {
            id: 13,
            code: 'DELIVER',
            name: 'تحویل'
        },

        {
            id: 14,
            code: 'LOST',
            name: 'گم شدن'
        },
        {
            id: 15,
            code: 'FOUND',
            name: 'پیدا شدن'
        },
        {
            id: 16,
            code: 'RECEIVED',
            name: 'دریافت'
        },
        {
            id: 17,
            code: 'NOT_RECEIVED',
            name: 'عدم دریافت'
        },
        {
            id: 18,
            code: 'SENT',
            name: 'ارسال'
        },
        {
            id: 19,
            code: 'UNDO',
            name: 'بازگشت'
        },
        {
            id: 20,
            code: 'INVOKE_EXTERNAL_SERVICE',
            name: 'فراخوانی سرویس خارجی'
        },
        {
            id: 21,
            code: 'EXECUTE_JOB',
            name: 'اجرای جاب'
        },
        {
            id: 22,
            code: 'AFTER_DELIVERY_CHANGE_PIN',
            name: 'تغییر پین'
        },
        {
            id: 23,
            code: 'AFTER_DELIVERY_REPEAL_SIGNATURE',
            name: 'ابطال حق امضا'
        },
        {
            id: 24,
            code: 'AFTER_DELIVERY_RESUME',
            name: 'اعلام پیدا شدن کارت'
        },
        {
            id: 25,
            code: 'AFTER_DELIVERY_REVOKE',
            name: 'ابطال کارت'
        },
        {
            id: 26,
            code: 'AFTER_DELIVERY_SUSPEND',
            name: 'اعلام گم شدن کارت'
        },
        {
            id: 27,
            code: 'AFTER_DELIVERY_UNBLOCK',
            name: 'آن بلاک کردن کارت'
        },
        {
            id: 28,
            code: 'AFTER_DELIVERY_UPDATE_DYNAMIC_DATA ',
            name: 'بروزرسانی اطلاعات پویا'
        },
        {
            id: 29,
            code: 'AFTER_DELIVERY_SUSPEND',
            name: 'اعلام گم شدن کارت'
        },
        {
            id: 30,
            code: 'UNSUCCESSFUL_DELIVER',
            name: 'تحویل ناموفق'
        },
        {
            id: 31,
            code: 'RESET',
            name: 'از سرگیری مجدد'
        },
        {
            id: 32,
            code: 'PRODUCTION_ERROR',
            name: 'خطا در تولید'
        },
        {
            id: 33,
            code: 'CHANGE_PASSWORD',
            name: 'تغییر واژه عبور'
        },
        {
            id: 34,
            code: 'AUTHENTICATE',
            name: 'اعتبار دادن'
        },
        {
            id: 35,
            code: 'NOTIFY_MISSED',
            name: 'اعلام از دست رفتن'
        },
        {
            id: 36,
            code: 'NOTIFY_RECEIVED',
            name: 'اعلام دریافت'
        },

        {
            id: 37,
            code: 'NOTIFY_ADDED',
            name: 'اعلام افزون'
        },
        {
            id: 38,
            code: 'NOTIFY_REMOVED',
            name: 'اعلام حذف'
        },
        {
            id: 39,
            code: 'NOTIFY_MODIFIED',
            name: 'اعلام تغییر'
        },
        {
            id: 40,
            code: 'NOTIFY_HANDOUT',
            name: ' اعلام تحویل کارت'
        },
        {
            id: 41,
            code: 'BATCH_ENQUIRY_REQUEST',
            name: 'درخواست استعلام بسته'
        },
        {
            id: 42,
            code: 'BATCH_ENQUIRY_RESPONSE',
            name: 'پاسخ استعلام بسته'
        },
        {
            id: 43,
            code: 'LOAD_UPDATED_CITIZENS',
            name: 'بارگذاری شهروندان تغییر یافته'
        },
        {
            id: 44,
            code: 'UPDATE_CITIZENS_INFO',
            name: 'به روز رسانی اطلاعات شهرون'
        },
        {
            id: 45,
            code: 'LOAD_FROM_PORTAL',
            name: 'دریافت پیش ثبت نام ها'
        },
        {
            id: 45,
            code: 'FETCH_RESERVATION_INFO',
            name: 'دریافت اطلاعات نوبت دهی'
        },
        {
            id: 46,
            code: 'UPDATE_REQUEST_STATES',
            name: 'به روز رسانی وضعیت درخواست ها'
        },
        {
            id: 47,
            code: 'UPDATE_CCOS_AND_IMS_VERIFIED_MES_REQUESTS',
            name: 'بروزرسانی درخواست های ثبت  نام شده در دفاتر پیشخوان و دفاتر سیار (مورد تایید IMS)'
        },
        {
            id: 47,
            code: 'UPDATE_NOT_VERIFIED_MES_REQUESTS',
            name: 'بروز رسانی درخواست های ثبت نام شده در دفاتر سیار (عدم تایید IMS)'
        },
        {
            id: 47,
            code: 'UPDATE_PROVINCE_LIST',
            name: 'بروز رسانی فهرست استان ها'
        },
        {
            id: 48,
            code: 'PROCESS_UNSUCCESSFUL_DELIVERY',
            name: 'پردازش تحویل ناموفق'
        },

        {
            id: 49,
            code: 'PERSON_TOKEN_REQUEST',
            name: 'درخواست صدور توکن کاربر'
        },
        {
            id: 50,
            code: 'PERSON_TOKEN_RESPONSE',
            name: 'نتیجه درخواست صدور توکن کاربر'
        },
        {
            id: 51,
            code: 'NETWORK_TOKEN_REQUEST',
            name: 'درخواست صدور توکن دفتر'
        },
        {
            id: 52,
            code: 'NETWORK_TOKEN_RESPONSE',
            name: 'نتیجه درخواست صدور توکن دفتر'
        },
        {
            id: 53,
            code: 'SEND_REQUEST_FOR_OFFLINE_ENQUIRY',
            name: 'مشاهده جزییات درخواست استعلام'
        },
        {
            id: 54,
            code: 'RECEIVE_REQUEST_FROM_OFFLINE_ENQUIRY',
            name: 'مشاهده جزییات پاسخ استعلام دسته ای '
        },
        {
            id: 55,
            code: 'FETCH_CITIZEN_INFO_FROM_IMS',
            name: 'مشاهده اطلاعات دریافت شده از سامانه ثبت احوال'
        }
        ,
        {
            id: 56,
            code: 'SEND_REQUEST_FOR_ONLINE_ENQUIRY',
            name: 'مشاهده جزییات استعلام انفرادی'
        },
        {
            id: 57,
            code: 'GET_PINS',
            name: 'دریافت پین از emks'
        }



    /**
     * Emitted items
     */
        //        {
        //            id:5,
        //            code:'SAVE',
        //            name:'ذخیره سازی'
        //        },

        //        {
        //            id:7,
        //            code:'FIND',
        //            name:'جستجو'
        //        },

        //        {
        //            id:16,
        //            code:'LOAD_ROLES',
        //            name:'دسترسی به فهرست نقشها'
        //        },
        //        {
        //            id:17,
        //            code:'LOAD_PERMISSIONS',
        //            name:'دسترسی به فهرست مجوزها'
        //        },

        //        {
        //            id: 50,
        //            code: 'LOAD_FROM_PORTAL',
        //            name: 'بارگذاری از پرتال'
        //        },

        //        {
        //            id: 41,
        //            code: 'VERIFY_READY_TOKENS',
        //            name: 'درخواست صدور توکن'
        //        },
        //        {
        //            id: 54,
        //            code: 'VERIFY_PENDING_TOKENS',
        //            name: 'درخواست دریافت نتیجه صدور توکن'
        //        },

    ]

});
