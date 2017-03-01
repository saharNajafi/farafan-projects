alter table emst_card_request add CRQ_REQUESTED_SMS_STATUS number(3,0);

Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (4000,3,1,'nocr.ems.profile.smsBodyReadyToDeliverCard','S,D,P','H',null,null,null,null);

Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values (4000,4000,null,'متقاضی گرامی {fname} {lname} کارت هوشمند ملی شما با شماره دسته {bnum} آماده تحویل است. لطفا برای دریافت آن به {dname} به آدرس {depadd} مراجعه فرمایید.');


Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (4001,3,1,'nocr.ems.profile.smsBodyReservedRequest','S,D,P','H',null,null,null,null);

Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values (4001,4001,null,'متقاضی گرامی {fname} {lname} لطفاً برای تکمیل ثبت نام خود در تاریخ {rdate} به دفتر ثبت نامی خود «{dname}» مراجعه فرمایید.');


Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (4002,3,1,'nocr.ems.profile.sendReserveSmsTimeInterval','S,D,P','H',null,null,null,null);

Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values (4002,4002,null,'1');



Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (4003,3,1,'nocr.ems.profile.numberOfRequestForSendReservedDateRmindingSmsFetchLimit','S,D,P','H',null,null,null,null);

Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values (4003,4003,null,'1000');



Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (4004,3,1,'nocr.ems.profile.deleteFromMsgtTimeInterval','S,D,P','H',null,null,null,null);

Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values (4004,4004,null,'2');


Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (4005,3,1,'nocr.ems.profile.deleteFromMsgtEnable','S,D,P','H',null,null,null,null);

Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values (4005,4005,null,'T');


/*Black list table*/
  CREATE TABLE "EMST_BLACK_LIST_LOCATIONS"
  (
    "BLL_ID"      NUMBER(19,0) NOT NULL ENABLE,
    CONSTRAINT "PRTL_BL_LOCATION_PK" PRIMARY KEY ("BLL_ID") USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS NOCOMPRESS LOGGING TABLESPACE "TS_NOCR_EMS" ENABLE
  )