create table emst_biometric_info(
bii_id number(19) not null,
bii_type number(1),
bii_minutia_type number(1),
bii_nid VARCHAR2(10 CHAR),
BII_CITIZEN_ID number(19) not null,
BII_FING_QUALITY number(1),
primary key (bii_id),
CONSTRAINT "FK_BII_CITIZEN_ID" FOREIGN KEY ("BII_CITIZEN_ID") REFERENCES EMST_CITIZEN ("CTZ_ID")  ON DELETE CASCADE ENABLE,
CONSTRAINT "CKC_FING_QUALITY" CHECK (BII_FING_QUALITY IN ('0', '1', '2', '3')) ENABLE);


CREATE SEQUENCE SEQ_EMS_BIOMETRIC_INFO  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 5 NOORDER  NOCYCLE ;


insert into emst_biometric_info(bii_id,bii_type,bii_minutia_type,bii_nid,bii_citizen_id,bii_fing_quality)
select 
 SEQ_EMS_BIOMETRIC_INFO.nextval as id, 
 (case tedad when 1 then 0 else 1 end) as type, 
 (case tedad when 1 then 0 when 2 then 1 else 2 end) as min_type, 
 (select ctz_national_id from emst_citizen where ctz_id = citID) as nid ,
 citID,
 0 as fingQulty
from
(
select b.bim_citizen_info_id as citID, count(b.bim_id) as tedad
from emst_biometric b
where (b.bim_type ='FING_ALL' or b.bim_type ='FING_MIN_1' or b.bim_type = 'FING_MIN_2')
group by b.bim_citizen_info_id);