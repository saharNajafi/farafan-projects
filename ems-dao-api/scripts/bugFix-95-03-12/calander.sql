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