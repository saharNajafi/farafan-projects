CREATE TABLE "COMM_DEV_EMS"."EMST_FEATURE_EXTRACT_IDS"
   (	"FEI_ID" NUMBER(19,0) NOT NULL ENABLE,
	"FEI_FEATURE_EXTRACT_NAME" VARCHAR2(15 BYTE),
	"FEI_FEATURE_EXTRACT_TYPE" VARCHAR2(255 CHAR) NOT NULL ENABLE,
	"FEI_FEATURE_EXTRACT_ID" VARCHAR2(4 BYTE),
	 PRIMARY KEY ("FEI_ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TS_NOCR_EMS"  ENABLE
   ) SEGMENT CREATION IMMEDIATE
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TS_NOCR_EMS" ;

CREATE SEQUENCE  "COMM_DEV_EMS"."SEQ_EMST_FEATURE_EXTRACT_IDS"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;


CREATE TABLE "COMM_DEV_EMS"."OST_FEI"
   (	"OST_ID" NUMBER(19,0) NOT NULL ENABLE,
	"FEI_ID" NUMBER(19,0) NOT NULL ENABLE,
	 PRIMARY KEY ("OST_ID", "FEI_ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TS_NOCR_EMS"  ENABLE,
	 CONSTRAINT "FKE57A799B15A5FCFA" FOREIGN KEY ("OST_ID")
	  REFERENCES "COMM_DEV_EMS"."EMST_OFFICE_SETTING" ("OST_ID") ENABLE,
	 CONSTRAINT "FKE57A799BC69C0199" FOREIGN KEY ("FEI_ID")
	  REFERENCES "COMM_DEV_EMS"."EMST_FEATURE_EXTRACT_IDS" ("FEI_ID") ENABLE
   ) SEGMENT CREATION IMMEDIATE
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TS_NOCR_EMS" ;



INSERT INTO "COMM_DEV_EMS"."EMST_FEATURE_EXTRACT_IDS" (
FEI_ID, FEI_FEATURE_EXTRACT_NAME, FEI_FEATURE_EXTRACT_TYPE, FEI_FEATURE_EXTRACT_ID)
VALUES('1',	'متیران', 'NORMAL', '02');

INSERT INTO "COMM_DEV_EMS"."EMST_FEATURE_EXTRACT_IDS" (
FEI_ID, FEI_FEATURE_EXTRACT_NAME, FEI_FEATURE_EXTRACT_TYPE, FEI_FEATURE_EXTRACT_ID)
VALUES('2',	'بهین پژوهش', 'NORMAL', '01');

INSERT INTO "COMM_DEV_EMS"."EMST_FEATURE_EXTRACT_IDS" (
FEI_ID, FEI_FEATURE_EXTRACT_NAME, FEI_FEATURE_EXTRACT_TYPE, FEI_FEATURE_EXTRACT_ID)
VALUES('3',	'Inov', 'NORMAL', '00');


INSERT INTO "COMM_DEV_EMS"."EMST_FEATURE_EXTRACT_IDS" (
FEI_ID, FEI_FEATURE_EXTRACT_NAME, FEI_FEATURE_EXTRACT_TYPE, FEI_FEATURE_EXTRACT_ID)
VALUES('4',	'متیران', 'CC', '02');

INSERT INTO "COMM_DEV_EMS"."EMST_FEATURE_EXTRACT_IDS" (
FEI_ID, FEI_FEATURE_EXTRACT_NAME, FEI_FEATURE_EXTRACT_TYPE, FEI_FEATURE_EXTRACT_ID)
VALUES('5',	'بهین پژوهش', 'CC', '01');

INSERT INTO "COMM_DEV_EMS"."EMST_FEATURE_EXTRACT_IDS" (
FEI_ID, FEI_FEATURE_EXTRACT_NAME, FEI_FEATURE_EXTRACT_TYPE, FEI_FEATURE_EXTRACT_ID)
VALUES('6',	'Inov', 'CC', '00');