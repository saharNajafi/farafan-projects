---------------------------EMST_REPORT-----------------------------------

------007
Insert into EMST_REPORT (RPT_ID,RPT_NAME,RPT_ACTIVATION_FLAG,RPT_CREATE_DATE,RPT_PERMISSION,RPT_SCOPE) 
values (1000,'گزارش آماری ثبت نام کننده گان به تفکیک جنسیت، دین و گروه سنی','T',to_timestamp('23-JUN-15 04.15.16.071000000 PM','DD-MON-RR HH.MI.SS.FF AM'),'ems_rpt_citizen_info','EMS');


-----008
Insert into EMST_REPORT (RPT_ID,RPT_NAME,RPT_ACTIVATION_FLAG,RPT_CREATE_DATE,RPT_PERMISSION,RPT_SCOPE) 
values (2000,'گزارش آماری دفاتر ثبت نام فعال و غیر فعال به تفکیک استان','T',to_timestamp('23-JUN-15 04.15.16.071000000 PM','DD-MON-RR HH.MI.SS.FF AM'),'ems_rpt_enrollment_activate','EMS');

-----008-2
Insert into EMST_REPORT (RPT_ID,RPT_NAME,RPT_ACTIVATION_FLAG,RPT_CREATE_DATE,RPT_PERMISSION,RPT_SCOPE) 
values (3000,'گزارش آماری دفاتر ثبت نام فعال و غیر فعال به تفکیک ادارات','T',to_timestamp('23-JUN-15 04.15.16.071000000 PM','DD-MON-RR HH.MI.SS.FF AM'),'ems_rpt_enrollment_state','EMS');


-----010
Insert into EMST_REPORT (RPT_ID,RPT_NAME,RPT_ACTIVATION_FLAG,RPT_CREATE_DATE,RPT_PERMISSION,RPT_SCOPE) 
values (4000,'گزارش آماری درخواست های باطل شده به تفکیک استان','T',to_timestamp('23-JUN-15 04.15.16.071000000 PM','DD-MON-RR HH.MI.SS.FF AM'),'ems_rpt_repeal_request','EMS');



-----010-2
Insert into EMST_REPORT (RPT_ID,RPT_NAME,RPT_ACTIVATION_FLAG,RPT_CREATE_DATE,RPT_PERMISSION,RPT_SCOPE) 
values (5000,'گزارش آماری درخواست های باطل شده به تفکیک ادارات','T',to_timestamp('23-JUN-15 04.15.16.071000000 PM','DD-MON-RR HH.MI.SS.FF AM'),'ems_rpt_repeal_request2','EMS');



-----011
Insert into EMST_REPORT (RPT_ID,RPT_NAME,RPT_ACTIVATION_FLAG,RPT_CREATE_DATE,RPT_PERMISSION,RPT_SCOPE) 
values (6000,'گزارش آماری کارت های باطل شده به تفکیک استان','T',to_timestamp('23-JUN-15 04.15.16.071000000 PM','DD-MON-RR HH.MI.SS.FF AM'),'ems_rpt_repeal_card','EMS');



-----011-2
Insert into EMST_REPORT (RPT_ID,RPT_NAME,RPT_ACTIVATION_FLAG,RPT_CREATE_DATE,RPT_PERMISSION,RPT_SCOPE) 
values (7000,'گزارش آماری کارت های باطل شده به تفکیک ادارات','T',to_timestamp('23-JUN-15 04.15.16.071000000 PM','DD-MON-RR HH.MI.SS.FF AM'),'ems_rpt_repeal_card2','EMS');



-----012
Insert into EMST_REPORT (RPT_ID,RPT_NAME,RPT_ACTIVATION_FLAG,RPT_CREATE_DATE,RPT_PERMISSION,RPT_SCOPE) 
values (8000,'گزارش آماری کارت های گم شده و دریافت نشده به تفکیک استان','T',to_timestamp('23-JUN-15 04.15.16.071000000 PM','DD-MON-RR HH.MI.SS.FF AM'),'ems_rpt_missed_card','EMS');



-----012-2
Insert into EMST_REPORT (RPT_ID,RPT_NAME,RPT_ACTIVATION_FLAG,RPT_CREATE_DATE,RPT_PERMISSION,RPT_SCOPE) 
values (9000,'گزارش آماری کارت های گم شده و دریافت نشده به تفکیک ادارات','T',to_timestamp('23-JUN-15 04.15.16.071000000 PM','DD-MON-RR HH.MI.SS.FF AM'),'ems_rpt_missed_card2','EMS');



-----013
Insert into EMST_REPORT (RPT_ID,RPT_NAME,RPT_ACTIVATION_FLAG,RPT_CREATE_DATE,RPT_PERMISSION,RPT_SCOPE) 
values (10000,'گزارش آماری علت درخواست به تفکیک استان','T',to_timestamp('23-JUN-15 04.15.16.071000000 PM','DD-MON-RR HH.MI.SS.FF AM'),'ems_rpt_request_reason','EMS');



-----013-2
Insert into EMST_REPORT (RPT_ID,RPT_NAME,RPT_ACTIVATION_FLAG,RPT_CREATE_DATE,RPT_PERMISSION,RPT_SCOPE) 
values (11000,'گزارش آماری علت درخواست به تفکیک ادارات','T',to_timestamp('23-JUN-15 04.15.16.071000000 PM','DD-MON-RR HH.MI.SS.FF AM'),'ems_rpt_request_reason2','EMS');



-----014
Insert into EMST_REPORT (RPT_ID,RPT_NAME,RPT_ACTIVATION_FLAG,RPT_CREATE_DATE,RPT_PERMISSION,RPT_SCOPE) 
values (12000,'گزارش آماری وضعیت آثار انگشتان درخواست‌های ثبت‌نام شده به تفکیک استان','T',to_timestamp('23-JUN-15 04.15.16.071000000 PM','DD-MON-RR HH.MI.SS.FF AM'),'ems_rpt_Minutia_request','EMS');



-----014-2
Insert into EMST_REPORT (RPT_ID,RPT_NAME,RPT_ACTIVATION_FLAG,RPT_CREATE_DATE,RPT_PERMISSION,RPT_SCOPE) 
values (13000,'گزارش آماری وضعیت آثار انگشتان درخواست‌های ثبت‌نام شده به تفکیک ادارات','T',to_timestamp('23-JUN-15 04.15.16.071000000 PM','DD-MON-RR HH.MI.SS.FF AM'),'ems_rpt_Minutia_request2','EMS');



-----015
Insert into EMST_REPORT (RPT_ID,RPT_NAME,RPT_ACTIVATION_FLAG,RPT_CREATE_DATE,RPT_PERMISSION,RPT_SCOPE) 
values (14000,'گزارش آماری وضعیت آثار انگشتان کارت‌های تحویل شده  به تفکیک استان','T',to_timestamp('23-JUN-15 04.15.16.071000000 PM','DD-MON-RR HH.MI.SS.FF AM'),'ems_rpt_Minutia_issued','EMS');



-----015-2
Insert into EMST_REPORT (RPT_ID,RPT_NAME,RPT_ACTIVATION_FLAG,RPT_CREATE_DATE,RPT_PERMISSION,RPT_SCOPE) 
values (15000,'گزارش آماری وضعیت آثار انگشتان کارت‌های تحویل شده  به تفکیک ادارات','T',to_timestamp('23-JUN-15 04.15.16.071000000 PM','DD-MON-RR HH.MI.SS.FF AM'),'ems_rpt_Minutia_issued2','EMS');