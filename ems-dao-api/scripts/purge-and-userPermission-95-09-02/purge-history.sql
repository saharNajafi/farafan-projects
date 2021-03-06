CREATE SEQUENCE "SEQ_EMST_PURGE_HISTORY" MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 5 NOORDER NOCYCLE ;

CREATE TABLE EMST_PURGE_HISTORY 
(
  PUH_ID NUMBER(19, 0) NOT NULL
, PUH_CITIZEN_ID NUMBER(19, 0) NOT NULL 
, PUH_NATIONAL_ID VARCHAR2(10 CHAR) NOT NULL 
, PUH_PURGEBIO_DATE TIMESTAMP(6)
, PUH_METADATA CLOB
, PUH_STATE VARCHAR2(1 CHAR)
, CONSTRAINT PK_PUH_ID PRIMARY KEY 
  (
    PUH_ID 
  )
, CONSTRAINT FK_PURGE_CITIZEN_ID FOREIGN KEY
	(
  		PUH_CITIZEN_ID 
	)
  REFERENCES EMST_CITIZEN
	(
  		CTZ_ID 
	)

); 