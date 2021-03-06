  ---------------------------------------------------------------------------------
  
  CREATE TABLE "PRE_MESS_LOC"
  (
    "PRE_MESS_ID"      NUMBER(19,0) NOT NULL ENABLE,
    "LOC_ID"      NUMBER(19,0) NOT NULL ENABLE,
    
  
    CONSTRAINT "EMST_MESS_PRE_LOC_FK" FOREIGN KEY ("PRE_MESS_ID") REFERENCES "EMST_PREPARED_MESSAGE" ("PRE_MESS_ID") ENABLE,
    CONSTRAINT "EMST_MESS_LOC_FK" FOREIGN KEY ("LOC_ID") REFERENCES "EMST_LOCATION" ("LOC_ID") ENABLE,
    CONSTRAINT "EMST_PRE_MESS_LOC_PK" PRIMARY KEY ("PRE_MESS_ID" , "LOC_ID") USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS NOCOMPRESS LOGGING TABLESPACE "TS_NOCR_EMS" ENABLE
  )
	
  SEGMENT CREATION DEFERRED PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING TABLESPACE "TS_NOCR_EMS" ;
  ---------------------------------------------------------------------------
  
  