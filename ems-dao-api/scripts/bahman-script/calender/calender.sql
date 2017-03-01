--------------------------------------------------------
--  ADD CALENDAR TYPE TO ENROLLMENT OFFICE IN EMS DB   
--------------------------------------------------------
alter table emst_enrollment_office add EOF_CALENDER_TYPE NUMBER(10,0) default 0;
ALTER TABLE EMST_ENROLLMENT_OFFICE
ADD CONSTRAINT CKC_EOF_CALENDER_TYPE CHECK 
(EOF_CALENDER_TYPE IN (0, 1,2))
ENABLE;
--------------------------------------------------------
--  ADD CALENDAR TYPE TO ENROLLMENT OFFICE IN PORTAL DB   
--------------------------------------------------------
alter table prtl_enrollment_office add EOF_CALENDER_TYPE NUMBER(10,0) default 0;
ALTER TABLE prtl_enrollment_office
ADD CONSTRAINT CKC_EOF_CALENDER_TYPE CHECK 
(EOF_CALENDER_TYPE IN (0, 1,2))
ENABLE;
--------------------------------------------------------
--  ADD ACTIVE TYPE TO FREE TIME IN RESERVATION TABLE IN PORTAL DB   
--------------------------------------------------------
alter TABLE PRTL_OFFICE_RSV_FREETIME add ORF_ACTIVE VARCHAR2(1 BYTE) default 'T';
ALTER TABLE PRTL_OFFICE_RSV_FREETIME
ADD CONSTRAINT CKC_ORF_ACTIVE CHECK 
(ORF_ACTIVE IN ('T', 'F'))
ENABLE;
--------------------------------------------------------
--  change calender type in ems and portal -------------   
--------------------------------------------------------
ALTER TABLE EMST_ENROLLMENT_OFFICE
drop CONSTRAINT CKC_EOF_CALENDER_TYPE;

ALTER TABLE EMST_ENROLLMENT_OFFICE
ADD CONSTRAINT CKC_EOF_CALENDER_TYPE CHECK 
(EOF_CALENDER_TYPE IN (0, 1,2,3))
ENABLE;

ALTER TABLE prtl_enrollment_office
drop CONSTRAINT CKC_EOF_CALENDER_TYPE;

ALTER TABLE prtl_enrollment_office
ADD CONSTRAINT CKC_EOF_CALENDER_TYPE CHECK 
(EOF_CALENDER_TYPE IN (0, 1,2,3))
ENABLE;
