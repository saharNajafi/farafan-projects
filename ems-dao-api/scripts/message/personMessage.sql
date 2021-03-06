  ---------------------------------------------------------------------------------
  
  CREATE TABLE "PRE_MESS_PERSON"
  (
    "PRE_MESS_ID"      NUMBER(19,0) NOT NULL ENABLE,
    "PER_ID"      NUMBER(19,0) NOT NULL ENABLE,
    
  
    CONSTRAINT "EMST_MESS_PRE_PER_FK" FOREIGN KEY ("PRE_MESS_ID") REFERENCES "EMST_PREPARED_MESSAGE" ("PRE_MESS_ID") ENABLE,
    CONSTRAINT "EMST_MESS_PERSON_FK" FOREIGN KEY ("PER_ID") REFERENCES "EMST_PERSON" ("PER_ID") ENABLE,
    CONSTRAINT "EMST_PRE_MESS_PER_PK" PRIMARY KEY ("PRE_MESS_ID" , "PER_ID") USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS NOCOMPRESS LOGGING TABLESPACE "TS_NOCR_EMS" ENABLE
  )
	
  SEGMENT CREATION DEFERRED PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING TABLESPACE "TS_NOCR_EMS" ;
  ---------------------------------------------------------------------------
  
  