
create table "EMST_BIOMETRIC_INFO"(
"BII_ID" number(19) not null,
"BII_TYPE" number(1),
"BII_MINUTIA_TYPE" number(1),
"BII_NID" VARCHAR2(10 CHAR),
"BII_CITIZEN_ID" number(19) not null,
"BII_FING_QUALITY" number(1),
CONSTRAINT "EMST_BIOMETRIC_INFO_PK" primary key ("BII_ID"),
CONSTRAINT "FK_BII_CITIZEN_ID" FOREIGN KEY ("BII_CITIZEN_ID") REFERENCES EMST_CITIZEN ("CTZ_ID")  ON DELETE CASCADE ENABLE
)
SEGMENT CREATION IMMEDIATE PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING STORAGE
(
INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT
)
TABLESPACE "TS_NOCR_EMS";


CREATE SEQUENCE  SEQ_EMS_BIOMETRIC_INFO  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 36 CACHE 5 NOORDER  NOCYCLE ;



select 'insert into emst_biometric_info(bii_id,bii_type,bii_minutia_type,bii_nid) values(' || SEQ_EMS_BIOMETRIC_INFO.nextval || ',' 
|| (case tedad when 1 then 0 else 1 end)|| ',' || (case tedad when 1 then 0 when 2 then 1 else 2 end) || ',''' || nid || ''');'
from
(
select c.CTZ_NATIONAL_ID as nid, count(b.bim_id) as tedad
from EMST_CITIZEN c
inner join emst_biometric b on b.bim_citizen_info_id = c.CTZ_ID
where (b.bim_type ='FING_ALL' or b.bim_type ='FING_MIN_1' or b.bim_type = 'FING_MIN_2')
group by c.CTZ_NATIONAL_ID);




