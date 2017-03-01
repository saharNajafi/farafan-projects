/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/25/12
 * Time: 4:47 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.store.CardRequestStateComboBoxStore', {
    extend: 'Ext.data.Store',
    alias: 'store.cardrequeststatecomboboxstore',
    model: 'Ems.model.ComboBoxSimpleModel',

    data: [
//        {
//            id:1,
//            code:'INACTIVE',
//            name:'غیرفعال'
//        },
//        {
//            id:2,
//            code:'REGISTERED',
//            name:'ثبت نام شده'
//        },
//        {   id:3,
//            code:'PENDING_FOR_EMS',
//            name:'منتظر ارسال به سیستم ثبت نام'
//        },
        {
            id: 4,
            code: 'RECEIVED_BY_EMS',
            name: 'آماده برای استعلام'
        },
//        {
//            id:5,
//            code:'SEND_TO_EMS' ,
//            name:'ثبت شده در سیستم ثبت نام'
//        },
        {
            id: 6,
            code: 'PENDING_IMS',
            name: 'در دست استعلام'
        },
        {
            id: 7,
            code: 'VERIFIED_IMS',
            name: 'تایید شده در مرحله استعلام'
        },
        {
            id: 8,
            code: 'NOT_VERIFIED_BY_IMS',
            name: 'رد شده در مرحله استعلام'
        },
        {
            id: 9,
            code: 'RESERVED',
            name: 'نوبت دهی شده'
        },
//        {
//            id:10,
//            code:'STOPPED_NOT_REFER_TO_CCOS',
//            name:'عدم مراجعه به دفتر پیشخوان'
//        },
//        {
//            id:11,
//            code:'READY_TO_RESERVE',
//            name:'آماده رزرو'
//        },
        {
            id: 12,
            code: 'REFERRED_TO_CCOS',
            name: 'مراجعه به دفتر'
        },
        {
            id: 13,
            code: 'DOCUMENT_AUTHENTICATED',
            name: 'در حال تکمیل ثبت نام'
        },
//        {
//            id:14,
//            code:'READY_TO_LIMITED_RESERVE',
//            name:'آماده رزرو محدود'
//        },
//        {
//            id:15,
//            code:'LIMITED_RESERVED',
//            name:'رزرو محدود'
//        },
        {
            id: 16,
            code: 'APPROVED',
            name: 'تایید شده توسط مدیر'
        },
        {
            id: 17,
            code: 'SENT_TO_AFIS',
            name: 'ارسال شده به سیستم EQC'
        },
        {
            id: 18,
            code: 'APPROVED_BY_AFIS',
            name: 'تایید شده توسط سیستم EQC'
        },
        {
            id: 19,
            code: 'PENDING_ISSUANCE',
            name: 'در دست صدور'
        },
        {
            id: 20,
            code: 'ISSUED',
            name: 'صادر شده'
        },
        {
            id: 21,
            code: 'READY_TO_DELIVER',
            name: 'آماده تحویل'
        },
        {
            id: 22,
            code: 'PENDING_TO_DELIVER_BY_CMS',
            name: 'منتظر اعلام تحویل کارت به CMS'
        },
        {
            id: 23,
            code: 'DELIVERED',
            name: ' تحویل شده'
        },
        {
            id: 24,
            code: 'UNSUCCESSFUL_DELIVERY',
            name: 'تحویل ناموفق'
        },
        {
            id: 25,
            code: 'UNSUCCESSFUL_DELIVERY_BECAUSE_OF_DAMAGE',
            name: 'تحویل ناموفق بدلیل خرابی'
        },
        {
            id: 26,
            code: 'UNSUCCESSFUL_DELIVERY_BECAUSE_OF_BIOMETRIC',
            name: 'تحویل ناموفق بدلیل تغییر اطلاعات بیومتریک'
        },
        {
            id: 27,
            code: 'NOT_DELIVERED',
            name: 'تحویل نشده'
        },
//        {
//            id:28,
//            code:'REVOKED_BY_AFIS',
//            name:'باطل شده توسط سیستم AFIS'
//        },
        {
            id: 29,
            code: 'STOPPED',
            name: 'متوقف شده'
        },
        {
            id: 30,
            code: 'CMS_ERROR',
            name: 'خطا در ارسال درخواست به سیستم CMS'
        },
        {
            id: 31,
            code: 'CMS_PRODUCTION_ERROR',
            name: 'خطا در تولید کارت در CMS'
        },
        {
            id: 32,
            code: 'IMS_ERROR',
            name: 'خطا در برقراری ارتباط با IMS'
        },
        {
            id: 33,
            code: 'APPROVED_BY_MES',
            name: 'ثبت شده توسط سیستم MES'
        },
        {
            id: 34,
            code: 'REPEALED',
            name: 'باطل شده'
        }
    ]
});
