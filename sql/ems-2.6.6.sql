DECLARE
  seq_val NUMBER;
BEGIN

 select GAM_MAIN_EMS.seq_inft_profile_key.nextval into seq_val from dual;
INSERT INTO GAM_MAIN_EMS.INFT_PROFILE_KEY (PRF_ID, PRF_PARENT_ID, PRF_SYS_ID, PRF_NAME, PRF_PRIORITY_CHAIN, PRF_STATUS, PRF_VALUES, PRF_ICON_CLASS, PRF_PER_ACCESS, PRF_DEP_ACCESS, PRF_SYS_ACCESS)
VALUES (seq_val, 3, 1, 'nocr.ems.profile.workstationCheckPeriod', 'P,D,S', 'H', NULL, NULL, NULL, NULL, NULL);

INSERT INTO GAM_MAIN_EMS.INFT_SYSTEM_PROFILE (SPF_ID, SPF_PRF_ID, SPF_CLOB, SPF_BLOB, SPF_DATE)
VALUES (seq_val, seq_val, '40', NULL, NULL);

commit;

END ;

drop table GAM_MAIN_EMS.EMST_WORKSTATION_INFO purge ;

  CREATE TABLE GAM_MAIN_EMS."EMST_WORKSTATION_INFO"
   (    "WSI_ID" NUMBER(19,0) NOT NULL ENABLE,
    "WSI_ADDITIONAL_INFO_AS_JSON" VARCHAR2(255 CHAR),
    "WSI_CCOS_VERSION" VARCHAR2(255 CHAR),
    "WSI_CPU_INFO" VARCHAR2(255 CHAR),
    "WSI_GATHER_STATE" NUMBER(5,0) NOT NULL ENABLE,
    "WSI_HAS_DOTNET_FRAMWORK45" NUMBER(5,0),
    "WSI_IP_ADDRESS" VARCHAR2(255 CHAR),
    "WSI_LAST_MODIFIED_DATE" TIMESTAMP (6),
    "WSI_MEMORY_CAPACITY" VARCHAR2(255 CHAR),
    "WSI_OS_VERSION" VARCHAR2(255 CHAR),
    "WSI_USERNAME" VARCHAR2(255 CHAR),
    "WSI_WORKSTATION_ID" NUMBER(19,0))
  TABLESPACE "TS_NOCR_EMSAPP" ;


alter table GAM_MAIN_EMS.EMST_WORKSTATION_INFO  add constraint EMST_WORKSTATION_INFO_PK primary key ( WSI_ID )
;
 alter table GAM_MAIN_EMS.EMST_WORKSTATION_INFO  add CONSTRAINT "WORKSTATION_ID_FK" FOREIGN KEY ("WSI_WORKSTATION_ID")
  REFERENCES GAM_MAIN_EMS."EMST_WORKSTATION" ("WST_ID") ENABLE;

  CREATE INDEX "GAM_MAIN_EMS"."WORKSTATION_ID_UN" ON "GAM_MAIN_EMS"."EMST_WORKSTATION_INFO" ("WSI_WORKSTATION_ID")
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS
  STORAGE( INITIAL 65536 NEXT 1048576 MINEXTENTS 1
  FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TS_NOCR_EMS_INDEX" ;

