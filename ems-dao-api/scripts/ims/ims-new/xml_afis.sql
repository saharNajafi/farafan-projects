
CREATE SEQUENCE "SEQ_EMST_XMLAFIS" MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 5 NOORDER NOCYCLE ;

CREATE TABLE EMST_XMLAFIS 
(
  	XAF_ID NUMBER(19, 0) NOT NULL 
, 	XAF_DATA CLOB NOT NULL 
,	XAF_REQUEST_ID NUMBER(19, 0) NOT NULL
, 	XAF_CREATE_DATE TIMESTAMP(6) NOT NULL
, 	XAF_ERRMSG CLOB NOT NULL 
,	CONSTRAINT PK_XAF_ID PRIMARY KEY 
  (
    	XAF_ID
  )
); 

