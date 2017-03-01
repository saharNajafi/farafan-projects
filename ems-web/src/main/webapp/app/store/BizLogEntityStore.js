/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/8/12
 * Time: 4:31 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.store.BizLogEntityStore', {
    extend: 'Ext.data.Store', //extend: 'Gam.data.store.Autocomplete',
    alias: 'store.bizlogentitystore',
    model: 'Ems.model.ComboBoxSimpleModel',

    data: [
        {
            id: 1,
            code: 'PERSON',
            name: 'کاربر'
        },
        {
            id: 2,
            code: 'PERSON_TOKEN',
            name: 'توکن کاربران'
        },
        {
            id: 3,
            code: 'DEPARTMENT',
            name: 'جایگاه سازمانی'
        },
        {
            id: 4,
            code: 'ENROLLMENT_OFFICE',
            name: 'دفتر پیشخوان'
        },
        {
            id: 5,
            code: 'NETWORK_TOKEN',
            name: 'توکن دفتر'
        },
        {
            id: 6,
            code: 'RATING',
            name: 'رتبه بندی'
        },
        {
            id: 7,
            code: 'CARD',
            name: 'کارت'
        },
        {
            id: 8,
            code: 'BOX',
            name: 'جعبه'
        },
        {
            id: 9,
            code: 'BATCH',
            name: 'دسته'
        },
        {
            id: 10,
            code: 'REQUEST',
            name: 'درخواست ثبت نام'
        },
        {
            id: 11,
            code: 'CITIZEN',
            name: 'شهروند'
        },
        {
            id: 12,
            code: 'WORKSTATION',
            name: 'ایستگاه کاری'
        },
        {
            id: 13,
            code: 'BIOMETRIC',
            name: 'بیومتریک'
        },
        {
            id: 14,
            code: 'DOCUMENT',
            name: 'سند'
        },
        {
            id: 15,
            code: 'SERVICE_DOC_TYPE',
            name: ' سند سرویس'
        },
        {
            id: 16,
            code: 'DISPATCH',
            name: 'توزیع'
        },
        {
            id: 17,
            code: 'BLACK_LIST',
            name: ' لیست سیاه'
        },
        {
            id: 18,
            code: 'DOC_TYPE',
            name: 'نوع سند'
        },
        {
            id: 19,
            code: 'ENCRYPTED_CARD_REQUEST',
            name: 'درخواست رمزگذاری شده'
        },
//        {
//            id:20,
//            code:'RESERVATION',
//            name:'نوبت دهی'
//        },
        {
            id: 21,
            code: 'SYSTEM_PROFILE',
            name: 'تنظیمات سیستمی'
        },
        {
            id: 22,
            code: 'IMS',
            name: 'سامانه ثبت احوال'
        },
        {
            id: 23,
            code: 'PORTAL',
            name: 'سامانه پورتال'
        },
        {
            id: 24,
            code: 'PKI',
            name: 'سامانه مدیریت گواهی'
        }
    ]

});
