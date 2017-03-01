--------------------------------------------------------------
-------------- دفتری که باید وی آی پی باشد را مشخص می کند ----
--------------------------------------------------------------
insert into inft_profile_key pr(pr.prf_id,pr.prf_parent_id,pr.prf_sys_id,pr.prf_name,pr.prf_priority_chain,pr.prf_status,pr.prf_values,pr.prf_icon_class,pr.prf_per_access,pr.prf_dep_access,pr.prf_sys_access) 
values(2022,3,1,'nocr.ems.profile.vipEnrollmentOffice','P,D,S','H','',null,null,null,null );
insert into inft_system_profile sypro(sypro.spf_id,sypro.spf_prf_id, sypro.spf_clob,sypro.spf_blob,sypro.spf_date) values(2022,2022,'',null,null);
commit;
--------------------------------------------------------------
------------------- PHOTO VIP --------------------------------
--------------------------------------------------------------
CREATE SEQUENCE "SEQ_EMST_PHOTO_VIP" MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 5 NOORDER NOCYCLE ;

CREATE TABLE EMST_PHOTO_VIP 
(
  PHV_ID NUMBER(19, 0) NOT NULL 
, PHV_DATA BLOB NOT NULL 
, PHV_CARD_REQUEST_ID NUMBER(19, 0) NOT NULL 
, PHV_META_DATA VARCHAR2(300 BYTE)  
, CONSTRAINT PK_PHV_ID PRIMARY KEY 
  (
    PHV_ID 
  )
); 

ALTER TABLE EMST_PHOTO_VIP
ADD CONSTRAINT FK_PHV_CARD_REQUEST_ID FOREIGN KEY
(
  PHV_CARD_REQUEST_ID 
)
REFERENCES EMST_CARD_REQUEST
(
  CRQ_ID 
)

--------------------------------------------------------------
------------------- ADD VIP TO ORIGIN ------------------------
--------------------------------------------------------------
ALTER TABLE EMST_CARD_REQUEST
drop CONSTRAINT CKC_CRQ_ORIGIN;

ALTER TABLE EMST_CARD_REQUEST
ADD CONSTRAINT CKC_CRQ_ORIGIN CHECK 
(CRQ_ORIGIN IN ('M', 'C', 'P', 'V'))
ENABLE;

--------------------------------------------------------------
------------------- ABOUT ------------------------------------
--------------------------------------------------------------
CREATE SEQUENCE "SEQ_EMS_ABOUT" MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 5 NOORDER NOCYCLE ;

CREATE TABLE EMST_ABOUT 
(
  ABT_ID NUMBER(19, 0) NOT NULL 
, ABT_CREATE_DATE TIMESTAMP(6) NOT NULL 
, ABT_CONTENT VARCHAR2(300 BYTE)
, ABT_PERSON_ID NUMBER(19, 0) NOT NULL 
, CONSTRAINT PK_ABT_ID PRIMARY KEY 
  (
   ABT_ID 
  )

);

ALTER TABLE EMST_ABOUT
ADD CONSTRAINT FK_ABT_PERSON_ID FOREIGN KEY
(
  ABT_PERSON_ID 
)
REFERENCES EMST_PERSON
(
  PER_ID 
)

--------------------------------------------------------------
------------------- HELP -------------------------------------
--------------------------------------------------------------
CREATE SEQUENCE "SEQ_EMS_HELP" MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 5 NOORDER NOCYCLE ;

CREATE TABLE EMST_HELP 
(
  HLP_ID NUMBER(19, 0) NOT NULL 
, HLP_CREATE_DATE TIMESTAMP(6) NOT NULL 
, HLP_PERSON_ID NUMBER(19, 0) NOT NULL
, HLP_FILE BLOB
, HLP_TITLE VARCHAR2(42 CHAR) NOT NULL 
, HLP_CONTENT_TYPE VARCHAR2(30 CHAR) NOT NULL 
, HLP_DESCRIPTION VARCHAR2(300 CHAR) 
, CONSTRAINT PK_HLP_ID PRIMARY KEY 
  (
   HLP_ID 
  )

);

ALTER TABLE EMST_HELP
ADD CONSTRAINT FK_HLP_PERSON_ID FOREIGN KEY
(
  HLP_PERSON_ID 
)
REFERENCES EMST_PERSON
(
  PER_ID 
)



