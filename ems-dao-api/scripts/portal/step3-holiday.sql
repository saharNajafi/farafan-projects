--------------------------------------------------------
--  File created - Saturday-April-18-2015   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table EMST_HOLIDAY
--------------------------------------------------------

  CREATE TABLE "EMST_HOLIDAY" 
   (	"HOL_ID" NUMBER(19,0), 
		"HOL_HOLIDAY" TIMESTAMP (6), 
		"HOL_FLAG" NUMBER(9,0)
   ) ;
   
ALTER TABLE "EMST_HOLIDAY" ADD CONSTRAINT "PK_HOL_ID" PRIMARY KEY ("HOL_ID");

ALTER TABLE "EMST_HOLIDAY" ADD CONSTRAINT "PK_HOL_HOLIDAY_DATE" UNIQUE ("HOL_HOLIDAY");

CREATE SEQUENCE "SEQ_EMS_HOLIDAY" MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 16 CACHE 5 NOORDER NOCYCLE ;