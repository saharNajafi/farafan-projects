change constraint with name =  CKC_BIM_TYPE in EMST_BIOMETRIC table and add two type FING_NORMAL_1 and FING_NORMAL_2;

INSERT INTO "COMM_DEV_EMS"."INFT_PROFILE_KEY" (PRF_ID, PRF_PARENT_ID, PRF_SYS_ID, PRF_NAME, PRF_PRIORITY_CHAIN, PRF_STATUS)
VALUES ('2190', '3', '1', 'nocr.ems.profile.fingNormal2SizeKB', 'P,D,S', 'H');
INSERT INTO "COMM_DEV_EMS"."INFT_SYSTEM_PROFILE" (SPF_ID, SPF_PRF_ID, SPF_CLOB) VALUES ('2190', '2190', '2');


INSERT INTO "COMM_DEV_EMS"."INFT_PROFILE_KEY" (PRF_ID, PRF_PARENT_ID, PRF_SYS_ID, PRF_NAME, PRF_PRIORITY_CHAIN, PRF_STATUS)
VALUES ('2191', '3', '1', 'nocr.ems.profile.fingNormal1SizeKB', 'P,D,S', 'H');
INSERT INTO "COMM_DEV_EMS"."INFT_SYSTEM_PROFILE" (SPF_ID, SPF_PRF_ID, SPF_CLOB) VALUES ('2191', '2191', '2');


the featureExtractid and featureextractversion for all office setting must be filling.