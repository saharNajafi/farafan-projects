--------------------------------------------------------
---------------- PURGE BIO AND DOCS --------------------
--------------------------------------------------------

Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (2142,3,1,'nocr.ems.profile.purgeBioFetchLimit','S,D,P','H',null,null,null,null);
Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values (2142,2142,null,'1');


Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (5002,3,1,'nocr.ems.profile.intervalPurgeDown','S,D,P','H',null,null,null,null);
Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values (5503,5503,null,'1394/01/01');

Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (5002,3,1,'nocr.ems.profile.intervalPurgeUp','S,D,P','H',null,null,null,null);
Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values (5504,5504,null,'1394/02/01');

--------------------------------------------------------
---------------- PURGE HISTORY --------------------
--------------------------------------------------------
Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (2144,3,1,'nocr.ems.profile.savePurgeHistory','S,D,P','H',null,null,null,null);
Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values (2144,2144,null,'1');



-------------- User Permission Access ProfileKey

Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (2145,3,1,'nocr.ems.profile.gaasJndiName','S,D,P','H',null,null,null,null);
Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values (2145,2145,null,'jdbc/GamNocrEmsOracleDS');

commit;




--- ***************************************  

-- Add Transition sent_to_affis to refer_to_ccos for reject (AFIS return code )


