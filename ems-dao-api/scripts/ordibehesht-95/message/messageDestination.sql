  ---------------------------------------------------------------------------------
  
  CREATE TABLE "EMST_MESS_DEST"
  (
    "MEDE_ID"      NUMBER(19,0) NOT NULL ENABLE,
    "MEDE_MESSAGE_ID"      NUMBER(19,0) NOT NULL ENABLE,
    "MEDE_DELETE_LIST" CLOB,
    "MEDE_SEEN_LIST" CLOB,
    "MEDE_MESSAGE_TYPE" NUMBER(1,0),
    "MEDE_DESTINATION_ID" NUMBER(19,0),

    CONSTRAINT "EMST_DES_MESSAGE_FK" FOREIGN KEY ("MEDE_MESSAGE_ID") REFERENCES "EMST_MESSAGE" ("MESS_ID") ENABLE,
    CONSTRAINT "EMST_MESS_DEST_PK" PRIMARY KEY ("MEDE_ID") USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS NOCOMPRESS LOGGING TABLESPACE "TS_NOCR_EMS" ENABLE
  )
	
  SEGMENT CREATION DEFERRED PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING TABLESPACE "TS_NOCR_EMS" ;
  ---------------------------------------------------------------------------
  CREATE SEQUENCE "SEQ_EMST_MESS_DEST" MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 401 CACHE 20 NOORDER NOCYCLE ;

  
  ---------------------------------------------------------------------------  
  
  