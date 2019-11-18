alter table EMST_PERSON_TOKEN
    add "PTK_REASON" VARCHAR2(255 CHAR);
COMMENT ON COLUMN "EMST_PERSON_TOKEN"."PTK_REASON" IS 'دلیل صدور';
commit ;


ALTER TABLE COMM_DEV_EMS.EMST_REGISTRATION_PAYMENT
  ADD (RPY_TERMINAl_ID  VARCHAR2(50 CHAR));

ALTER TABLE COMM_DEV_EMS.EMST_REGISTRATION_PAYMENT
  ADD (RPY_MERCHANT_ID  VARCHAR2(50 CHAR));





alter table GAM_MAIN_EMS.emst_registration_payment
  add (rpy_payment_type number(1));

alter table gam_main_ems.emst_registration_payment
  add constraint ck_rpy_payment_type check
    (rpy_payment_type in (1,2)) enable;

comment on column gam_main_ems.emst_registration_payment.rpy_payment_type is '1=IPG 2=PCPOS نوع پرداخت';