/**
 * Created by Gam Electronics Co. User: Haeri Date: 2/20/12 Time: 11:11 AM
 */
Ext.define('Ems.locale.Resource', {
    singleton: true,
    alternateClassName: 'EmsResource',
    alias: 'localeprovider.Ems',

    general: {
        buttons: {
            printSelectedItems: 'چاپ موارد انتخابی',
            printAllItems: 'چاپ همه موارد',
            deliver: 'تحویل شده',
            fromDateComplete: 'از تاریخ',
            toDateComplete: 'تا تاریخ',
            search: 'جستجو',
            clear: 'پاک کن',
            reject: 'عودتی',
            registerExtraData: 'ثبت اطلاعات تکمیلی'
        }
    },

// registration: {
// title: 'ثبت نام',
// // fields
// // Step 1
// step1: {
// fldName:'نام',
// fldNameEnglish:'نام(انگليسی)',
// fldNamePrev:'نام قبلی',
// fldSurname:'نام خانوادگی',
// fldSurnameEnglish:'نام خانوادگی(انگليسی)',
// fldSurnamePrev:'نام خانوادگی قبلی',
// fldSex:'جنسيت',
// fldSexFemale:'مونث',
// fldSexMale:'مذکر',
// fldDeen:'دين',
// fldTarikhTavallod:'تاريخ تولد',
// fldTarikhTavallodMiladi:'تاريخ تولد ميلادی',
// fldTarikhTavallodGhamari:'تاريخ تولد قمری',
// fldTarikhTavallodPrev:'تاريخ تولد قبلی',
// fldShomareMelli:'شماره ملی',
// fldShomareShenasname:'شماره شناسنامه',
// fldTozihatShenasname:'توضيحات شناسنامه',
// fldShomareSerialShenasname:'شماره سريال شناسنامه',
// fldEmail:'آدرس پست الکترونيک',
// fldMahalTavallodOstan:'استان',
// fldMahalTavallodShahrestan:'شهرستان',
// fldMahalTavallodDehestan:'دهستان',
// fldMahalTavallodBakhsh:'بخش',
// fldMahalTavallodShahrRoosta:'شهر-روستا',
// fldMahalTavallodEnglish:'محل تولد(انگليسی)',
// fldMahalSodoorShenasnameOstan:'استان',
// fldMahalSodoorShenasnameShahr:'شهر',
// fldNamePedar:'نام پدر',
// fldNamePedarEnglish:'نام پدر(انگليسی)',
// fldShomareMelliPedar:'شماره ملی پدر',
// fldNameMadar:'نام مادر',
// fldShomareMelliMadar:'شماره ملی مادر'
// }
// },

// booking: {
// title: 'رزرو'
// },
//
// forget: {
// title: 'فراموشی کد رهگيری'
// },
//
// rahgiri: {
// title: 'رهگيری'
// }


    // ///////////////////////////
    dispatching: {
        DispatchStatus: {
            s0: 'آماده دريافت', s1: 'دريافت شده', s2: 'دريافت نشده', s3: 'ارسال شده', s4: 'گم شده', s5: 'دریافت شده در دفتر'
        }
    }, DocType: {
        service: {
            s1: 'ثبت نام جدید',
            s2: 'تمدید',
            s3: 'المثنی',
            s4: 'صدور مجدد به دلیل خرابی',
            s5: 'صدور مجدد به دلیل تغییرات هویتی'
        }
    }, cardRequestList: {
        CardRequestType: {
            FIRST_CARD: 'درخواست جدید',
            UNSUCCESSFUL_DELIVERY_FOR_FIRST_CARD: 'درخواست جدید',
            EXTEND: 'تمدید',
            UNSUCCESSFUL_DELIVERY_FOR_EXTEND: 'تمدید',
            REPLICA: 'المثنی',
            UNSUCCESSFUL_DELIVERY_FOR_REPLICA: 'المثنی',
            REPLACE: 'تعویض',
            UNSUCCESSFUL_DELIVERY_FOR_REPLACE: 'تعویض',
            UNSUCCESSFUL_DELIVERY: 'تحویل ناموفق'
        },
        CardRequestState: {

// INACTIVE: 'غیرفعال',

// REGISTERED: 'ثبت نام شده',
// PENDING_FOR_EMS: 'منتظر ارسال به سیستم ثبت نام',
            RECEIVED_BY_EMS: 'آماده برای استعلام',
// SEND_TO_EMS: 'ثبت شده در سیستم ثبت نام',

            PENDING_IMS: 'در دست استعلام',
            VERIFIED_IMS: 'تایید شده در مرحله استعلام',
            NOT_VERIFIED_BY_IMS: 'رد شده در مرحله استعلام',

            RESERVED: 'نوبت دهی شده',
// STOPPED_NOT_REFER_TO_CCOS: 'عدم مراجعه به دفتر پیشخوان',

// READY_TO_RESERVE: 'آماده نوبت دهی',

            REFERRED_TO_CCOS: 'مراجعه به دفتر',
            DOCUMENT_AUTHENTICATED: 'در حال تکمیل ثبت نام',
// READY_TO_LIMITED_RESERVE: 'آماده نوبت دهی محدود',

// LIMITED_RESERVED: 'نوبت دهی شده محدود',
            APPROVED: 'تایید شده توسط مدیر',

            SENT_TO_AFIS: 'ارسال شده به سیستم EQC',
            APPROVED_BY_AFIS: 'تایید شده توسط سیستم EQC',
            PENDING_ISSUANCE: 'در دست صدور',

            ISSUED: 'صادر شده',

            READY_TO_DELIVER: 'آماده تحویل',

            PENDING_TO_DELIVER_BY_CMS: 'منتظر اعلام تحویل کارت به CMS',
            DELIVERED: ' تحویل شده',

            UNSUCCESSFUL_DELIVERY: 'تحویل ناموفق',
            UNSUCCESSFUL_DELIVERY_BECAUSE_OF_DAMAGE: 'تحویل ناموفق بدلیل خرابی',
            UNSUCCESSFUL_DELIVERY_BECAUSE_OF_BIOMETRIC: 'تحویل ناموفق بدلیل تغییر اطلاعات بیومتریک',

            NOT_DELIVERED: 'تحویل نشده',

// REVOKED_BY_AFIS: 'باطل شده توسط سیستم AFIS',
            STOPPED: 'متوقف شده',
            REPEALED: 'باطل شده',
            CMS_ERROR: 'خطا در ارسال درخواست به سیستم CMS',
            CMS_PRODUCTION_ERROR: 'خطا در تولید کارت در CMS',
            IMS_ERROR: 'خطا در برقراری ارتباط با IMS',
            APPROVED_BY_MES: 'ثبت شده توسط سیستم MES'

        },

        CardState: {

            SHIPPED: 'در حال توزیع',

            RECEIVED: 'دریافت شده',

            MISSED: 'گم شده',

            DELIVERED: 'تحویل شده',

            REVOKED: 'ابطال شده',

            LOST: 'مفقود شده'

        }
    }, Entity: {
        PERSON: 'کاربر',
        PERSON_TOKEN: 'توکن کاربران',
        DEPARTMENT: 'جایگاه سازمانی',
        ENROLLMENT_OFFICE: 'دفتر پیشخوان',
        NETWORK_TOKEN: 'توکن دفتر',
        RATING: 'رتبه بندی',
        CARD: 'کارت',
        BOX: 'جعبه',
        BATCH: 'دسته',
        REQUEST: 'درخواست ثبت نام',
        CITIZEN: 'شهروند',
        WORKSTATION: 'ایستگاه کاری',
        BIOMETRIC: 'بیومتریک',
        DOCUMENT: 'سند',
        SERVICE_DOC_TYPE: 'سند سرویس',
        DISPATCH: 'توزیع',
        BLACK_LIST: 'لیست سیاه',

        DOC_TYPE: 'نوع سند',
        ENCRYPTED_CARD_REQUEST: 'درخواست رمزگذاری شده',
// RESERVATION: 'نوبت دهی',
        SYSTEM_PROFILE: 'تنظیمات سیستمی',

        IMS: 'سامانه ثبت احوال',
        PORTAL: 'سامانه پورتال',
        PKI: 'سامانه مدیریت گواهی'



    }, Action: {
// LOGIN:'ورود به سامانه',
// LOGOUT:'خروج از سامانه',
        INSERT: 'جدید',
        UPDATE: 'ویرایش',
        DELETE: 'حذف',
        LOAD: 'مشاهده',
// SAVE: 'ذخیره سازی',
        REMOVE: 'حذف',
// FIND: 'جستجو',
        APPROVE: 'تایید',
        REJECT: 'عدم تایید',
        CHANGE_STATE: 'تغییر وضعیت',
        ISSUE: 'صدور',
        REISSUE: 'صدور مجدد',
        REPLICATE: 'المثنی',
        REVOKE: 'ابطال',
        DELIVER: 'تحویل',
// LOAD_ROLES:'دسترسی به فهرست نقشها',
// LOAD_PERMISSIONS:'دسترسی به فهرست مجوزها',
        LOST: 'گم شدن',
        FOUND: 'پیدا شدن',
        RECEIVED: 'دریافت',
        NOT_RECEIVED: 'عدم دریافت',
        SENT: 'ارسال',
        UNDO: 'بازگشت',
// INVOKE_EXTERNAL_SERVICE: 'فراخوانی سرویس خارجی',
// EXECUTE_JOB: 'اجرای جاب',
        AFTER_DELIVERY_CHANGE_PIN: 'تغییر پین',
        AFTER_DELIVERY_REPEAL_SIGNATURE: 'ابطال حق امضا',
        AFTER_DELIVERY_RESUME: 'اعلام پیدا شدن کارت',
        AFTER_DELIVERY_REVOKE: 'ابطال کارت',
        AFTER_DELIVERY_SUSPEND: 'اعلام گم شدن کارت',
        AFTER_DELIVERY_UNBLOCK: 'آن بلاک کردن کارت',
        AFTER_DELIVERY_UPDATE_DYNAMIC_DATA: 'بروزرسانی اطلاعات پویا',


        UNSUCCESSFUL_DELIVER: 'تحویل ناموفق',//
        RESET: 'از سرگیری مجدد',//

        PRODUCTION_ERROR: 'خطا در تولید', //
        CHANGE_PASSWORD: 'تغییر واژه عبور', //
        AUTHENTICATE: 'اعتبار دادن', //
        NOTIFY_MISSED: 'اعلام از دست رفتن', //
        NOTIFY_RECEIVED: 'اعلام دریافت', //
        NOTIFY_ADDED: 'اعلام افزودن',
        NOTIFY_REMOVED: 'اعلام حذف',
        NOTIFY_MODIFIED: 'اعلام تغییر',
        NOTIFY_HANDOUT: ' اعلام تحویل کارت', //
        BATCH_ENQUIRY_REQUEST: 'درخواست استعلام بسته', //
        BATCH_ENQUIRY_RESPONSE: 'پاسخ استعلام بسته', //
        LOAD_UPDATED_CITIZENS: 'بارگذاری شهروندان تغییر یافته',//
        UPDATE_CITIZENS_INFO: 'به روز رسانی اطلاعات شهروند', //
        LOAD_FROM_PORTAL: 'دریافت پیش ثبت نام ها',
        FETCH_RESERVATION_INFO: 'دریافت اطلاعات نوبت دهی',
        UPDATE_REQUEST_STATES: 'به روز رسانی وضعیت درخواست ها',
        UPDATE_CCOS_AND_IMS_VERIFIED_MES_REQUESTS: 'بروزرسانی درخواست های ثبت  نام شده در دفاتر پیشخوان و دفاتر سیار (مورد تایید IMS)',
        UPDATE_NOT_VERIFIED_MES_REQUESTS: 'بروز رسانی درخواست های ثبت نام شده در دفاتر سیار (عدم تایید IMS)',
        UPDATE_PROVINCE_LIST: 'بروز رسانی فهرست استان ها',
        PROCESS_UNSUCCESSFUL_DELIVERY: 'پردازش تحویل ناموفق',//
// VERIFY_PENDING_TOKENS: 'درخواست دریافت نتیجه صدور توکن',//
// VERIFY_READY_TOKENS: 'درخواست صدور توکن',//
        PERSON_TOKEN_REQUEST: 'درخواست صدور توکن کاربر',
        PERSON_TOKEN_RESPONSE: 'نتیجه درخواست صدور توکن کاربر',
        NETWORK_TOKEN_REQUEST: 'درخواست صدور توکن دفتر',
        NETWORK_TOKEN_RESPONSE: 'نتیجه درخواست صدور توکن دفتر',

        /**
		 * IMS
		 */
        SEND_REQUEST_FOR_OFFLINE_ENQUIRY: 'مشاهده جزییات درخواست استعلام',
        RECEIVE_REQUEST_FROM_OFFLINE_ENQUIRY: 'مشاهده جزییات پاسخ استعلام دسته ای ',
        FETCH_CITIZEN_INFO_FROM_IMS: 'مشاهده اطلاعات دریافت شده از سامانه ثبت احوال',
        SEND_REQUEST_FOR_ONLINE_ENQUIRY: 'مشاهده جزییات استعلام انفرادی',
        GET_PINS:'دریافت پین از emks'
        	

    }

// ,workStation:{
// StatusText:{
// sN:'جدید',
// sA:'تایید',
// sR:'عدم تایید'
// }
// }

    , userFormField: {
        firstName: 'نام', lastName: 'نام خانوادگی', nationalCode: 'کد ملی', certificateId: 'شماره شناسنامه', certificateSerial: 'شماره سریال شناسنامه', fatherName: 'نام پدر', userName: 'نام کاربری', password: 'گذر واژه', repeatPassword: 'تکرار گذرواژه', OrganizationalStatus: 'جایگاه سازمانی', lastLoginDate: 'آخرین ورود'
        // ,status:'status'
        , userActive: 'userActive', userRole: 'userRole', userAccess: 'userAccess'
    }, jobList: {
        Status: {
            NORMAL: 'نرمال',
            PAUSED: 'متوقف شده',
// COMPLETE: 'تکمیل شده',
            COMPLETE: 'در حال اجرا',
            ERROR: 'خطا',
            BLOCKED: 'در حال اجرا',
            NONE: '-'

        }
    }, reportResult: {
        receivedByEms: "تعداد پیش ثبت نام های آماده برای استعلام",
        reservesCount: "تعداد پیش ثبت نام های نوبت دهی شده",
        overdueReserves: "تعداد پیش ثبت نام های که زمان نوبت دهی آنها گذشته است",
        pendingIms: "تعداد پیش ثبت نام هایی که برای استعلام ارسال شده اند",
        verifiedIms: "تعداد پیش ثبت نام هایی که نتیجه استعلام آنها مثبت بوده است",
        notVerifiedByIms: "تعداد پیش ثبت نام هایی که در استعلام مردود شده اند",
        approved: "تعداد ثبت نام های تایید شده توسط مدیر دفتر",
        todayReserves: "تعداد پیش ثبت نام های نوبت دهی شده برای امروز",
        readyToApprove: "تعداد ثبت نام های آماده برای تایید مدیر دفتر",
        issuanceProcess: "تعداد ثبت نام های در دست صدور",
        referredToCCOS: "تعداد ثبت نام های آماده احراز اصالت مدارک",
        documentAuthenticated: "تعداد ثبت نام های در دست تکمیل"
    },
    
    
    estelamFailureType:{
    	NOT_FOUND: 'شهروندی با مشخصات مورد نظر یافت نشد',
    	NOT_GENDER_EQUAL: 'عدم تطابق جنسیت شهروند با مقدار دریافتی از IMS', 
    	INVAILD_CERTIFICATE_ID:'معتبر نبودن شماره شناسنامه',
    	INVAILD_BIRTH_DATE: 'معتبر نبودن تاریخ تولد'
    	,INVAILD_CERTIFICATE_SERIAL: 'معتبر نبودن سریال شناسنامه'
    	,INVAILD_NAME: 'معتبر نبودن نام'
    	,INVAILD_LAST_NAME: 'معتبر نبودن نام خانوادگی'
    	,INVAILD_FATHER_NAME: 'معتبر نبودن نام پدر'
    	,INVAILD_GENDER: 'معتبر نبودن جنیست'
    	,UNKNOWN_IMS_MESSAGE: 'خطای ناشناخته از IMS'
    }

});
