-------------------------------------------------------------------------------
----------------------------------- MESSAGE -----------------------------------
-------------------------------------------------------------------------------

CREATE SEQUENCE "SEQ_EMST_MESSAGE" MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 5 NOORDER NOCYCLE ;

CREATE TABLE EMST_MESSAGE 
(
  MESS_ID NUMBER(19, 0) NOT NULL 
, MESS_TITLE VARCHAR2(200 BYTE) 
, MESS_CONTENT CLOB 
, MESS_PERSON_ID NUMBER(19, 0) NOT NULL
, MESS_PRIORTY VARCHAR2(20 CHAR) 
, MESS_CREATE_DATE TIMESTAMP(6) 
, CONSTRAINT PK_MESS_ID PRIMARY KEY 
  (
    MESS_ID 
  )
); 

ALTER TABLE EMST_MESSAGE
ADD CONSTRAINT CKC_MESS_PRIORTY CHECK 
(MESS_PRIORTY IN ('IMPORTANT','NOT_IMPORTANT'))
ENABLE;


ALTER TABLE EMST_MESSAGE
ADD CONSTRAINT FK_MESS_PERSON_ID FOREIGN KEY
(
  MESS_PERSON_ID 
)
REFERENCES EMST_PERSON
(
  PER_ID 
)
-------------------------------------------------------------------------------
----------------------------------- MESSAGE DEST ------------------------------
-------------------------------------------------------------------------------

CREATE SEQUENCE "SEQ_EMST_MESS_DEST" MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 5 NOORDER NOCYCLE ;

CREATE TABLE EMST_MESS_DEST 
(
  MEDE_ID NUMBER(19, 0) NOT NULL 
, MEDE_MESSAGE NUMBER(19, 0) 
, MEDE_LOCATION NUMBER(19, 0) 
, MEDE_DEPARTMENT NUMBER(19, 0) 
, MEDE_OFFICE NUMBER(19, 0) 
, MEDE_NOCR NUMBER(19, 0) 
, MEDE_PERSON NUMBER(19, 0) 
, MEDE_MESSAGE_TYPE VARCHAR2(20 CHAR) 
, CONSTRAINT PK_MEDE_ID PRIMARY KEY 
  (
    MEDE_ID 
  )
); 


ALTER TABLE EMST_MESS_DEST
ADD CONSTRAINT FK_MESSAGE FOREIGN KEY
(
  MEDE_MESSAGE 
)
REFERENCES EMST_MESSAGE
(
  MESS_ID 
)
ENABLE;

ALTER TABLE EMST_MESS_DEST
ADD CONSTRAINT FK_MESSAGE_DEP FOREIGN KEY
(
  MEDE_DEPARTMENT 
)
REFERENCES EMST_DEPARTMENT
(
  DEP_ID 
)
ENABLE;

ALTER TABLE EMST_MESS_DEST
ADD CONSTRAINT FK_MESSAGE_LOC FOREIGN KEY
(
  MEDE_LOCATION 
)
REFERENCES EMST_LOCATION
(
  LOC_ID 
)
ENABLE;

ALTER TABLE EMST_MESS_DEST
ADD CONSTRAINT FK_MESSAGE_NOCR FOREIGN KEY
(
  MEDE_NOCR 
)
REFERENCES EMST_DEPARTMENT
(
  DEP_ID 
)
ENABLE;

ALTER TABLE EMST_MESS_DEST
ADD CONSTRAINT FK_MESSAGE_OFFICE FOREIGN KEY
(
  MEDE_OFFICE 
)
REFERENCES EMST_DEPARTMENT
(
  DEP_ID 
)
ENABLE;
ALTER TABLE EMST_MESS_DEST
ADD CONSTRAINT FK_MESSAGE_PERSON FOREIGN KEY
(
  MEDE_PERSON 
)
REFERENCES EMST_PERSON
(
  PER_ID 
)
ENABLE;
-------------------------------------------------------------------------------
----------------------------------- MESSAGE PERSON ----------------------------
-------------------------------------------------------------------------------
CREATE SEQUENCE "SEQ_EMST_MESSAGE_PERSON" MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 5 NOORDER NOCYCLE ;

CREATE TABLE EMST_MESSAGE_PERSON 
(
  MESSPER_ID NUMBER(19, 0) NOT NULL 
, MESSPER_PERSON_ID NUMBER(19, 0) 
, MESSPER_MESSAGE_ID NUMBER(19, 0) 
, MESSPER_READ_STATE VARCHAR2(20 CHAR) 
, CONSTRAINT PK_MESSPER_ID PRIMARY KEY 
  (
    MESSPER_ID 
  )
); 

ALTER TABLE EMST_MESSAGE_PERSON
ADD CONSTRAINT CKC_MESSPER_READ_STATE CHECK 
(MESSPER_READ_STATE IN ('R','U'))
ENABLE;



