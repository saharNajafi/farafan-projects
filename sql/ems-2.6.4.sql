---------------------------------------------------------------------------------
--------------------------------add column------------------------------------------

ALTER TABLE GAM_MAIN_EMS.EMST_CITIZEN_INFO
ADD  CZI_FACE_DISABILITY_STATUS NUMBER(1,0);


---------------------------------------------------------------------------------
---------------------------------------------------------------------------------

INSERT INTO gam_main_ems.EMST_DOC_TYPE (DCT_ID, DCT_NAME) VALUES (52, 'تصویر چهره  استخراج شده از شناسنامه');
INSERT INTO gam_main_ems.EMST_DOC_TYPE (DCT_ID, DCT_NAME) VALUES (53, 'تصویر شماره سریال شناسنامه');

---------------------------------------------------------------------------------
---------------------------------------------------------------------------------

INSERT INTO gam_main_ems.EMST_FEATURE_EXTRACT_IDS (FEI_ID, FEI_FEATURE_EXTRACT_ID, FEI_FEATURE_EXTRACT_NAME,FEI_FEATURE_EXTRACT_TYPE)
VALUES (gam_main_ems.SEQ_EMST_FEATURE_EXTRACT_IDS.nextval, '0', 'صفر', 'ISOCC');

INSERT INTO gam_main_ems.EMST_FEATURE_EXTRACT_IDS (FEI_ID, FEI_FEATURE_EXTRACT_ID, FEI_FEATURE_EXTRACT_NAME,FEI_FEATURE_EXTRACT_TYPE)
VALUES (gam_main_ems.SEQ_EMST_FEATURE_EXTRACT_IDS.nextval, '1', 'یک', 'ISOCC');



---------------------------------------------------------------------------------
-------------------------------add profile key-----------------------------------
---------------------------------------------------------------------------------
DECLARE
  seq_val NUMBER;
BEGIN

  select gam_main_ems.seq_inft_profile_key.nextval into seq_val from dual;
INSERT INTO gam_main_ems.INFT_PROFILE_KEY (PRF_ID, PRF_PARENT_ID, PRF_SYS_ID, PRF_NAME, PRF_PRIORITY_CHAIN, PRF_STATUS, PRF_VALUES, PRF_ICON_CLASS, PRF_PER_ACCESS, PRF_DEP_ACCESS, PRF_SYS_ACCESS)
VALUES (seq_val, 69, 1, 'nocr.ems.profile.ccos.ISO19794NormalFormatMaxSizeBytes', 'P,D,S', 'H', NULL, NULL, NULL, NULL, NULL);

INSERT INTO gam_main_ems.INFT_SYSTEM_PROFILE (SPF_ID, SPF_PRF_ID, SPF_CLOB, SPF_BLOB, SPF_DATE)
VALUES (seq_val, seq_val, '1500', NULL, NULL);
-- -------------------------------------------------

  select gam_main_ems.seq_inft_profile_key.nextval into seq_val from dual;
INSERT INTO gam_main_ems.INFT_PROFILE_KEY (PRF_ID, PRF_PARENT_ID, PRF_SYS_ID, PRF_NAME, PRF_PRIORITY_CHAIN, PRF_STATUS, PRF_VALUES, PRF_ICON_CLASS, PRF_PER_ACCESS, PRF_DEP_ACCESS, PRF_SYS_ACCESS)
VALUES (seq_val, 69, 1, 'nocr.ems.profile.ccos.finger.singleprint.MOCEngineEnhancementNFIQ2QualityThreshold', 'P,D,S', 'H', NULL, NULL, NULL, NULL, NULL);

INSERT INTO gam_main_ems.INFT_SYSTEM_PROFILE (SPF_ID, SPF_PRF_ID, SPF_CLOB, SPF_BLOB, SPF_DATE)
VALUES (seq_val, seq_val, '30', NULL, NULL);
-------------------------------------------------

  select gam_main_ems.seq_inft_profile_key.nextval into seq_val from dual;
INSERT INTO gam_main_ems.INFT_PROFILE_KEY (PRF_ID, PRF_PARENT_ID, PRF_SYS_ID, PRF_NAME, PRF_PRIORITY_CHAIN, PRF_STATUS, PRF_VALUES, PRF_ICON_CLASS, PRF_PER_ACCESS, PRF_DEP_ACCESS, PRF_SYS_ACCESS)
VALUES (seq_val, 3, 1, 'nocr.ems.profile.ccos.document.FaceImageCompressionMaxSizeLimitBytes', 'S,D,P', 'H', NULL, NULL, NULL, NULL, NULL);

INSERT INTO gam_main_ems.INFT_SYSTEM_PROFILE (SPF_ID, SPF_PRF_ID, SPF_CLOB, SPF_BLOB, SPF_DATE)
VALUES (seq_val, seq_val, '25600', NULL, NULL);
-------------------------------------------------

  select gam_main_ems.seq_inft_profile_key.nextval into seq_val from dual;
INSERT INTO gam_main_ems.INFT_PROFILE_KEY (PRF_ID, PRF_PARENT_ID, PRF_SYS_ID, PRF_NAME, PRF_PRIORITY_CHAIN, PRF_STATUS, PRF_VALUES, PRF_ICON_CLASS, PRF_PER_ACCESS, PRF_DEP_ACCESS, PRF_SYS_ACCESS)
VALUES (seq_val, 3, 1, 'nocr.ems.profile.ccos.document.SerialNumberImageCompressionMaxSizeLimitBytes', 'S,D,P', 'H', NULL, NULL, NULL, NULL, NULL);

INSERT INTO gam_main_ems.INFT_SYSTEM_PROFILE (SPF_ID, SPF_PRF_ID, SPF_CLOB, SPF_BLOB, SPF_DATE)
VALUES (seq_val, seq_val, '10240', NULL, NULL);


Rem
Rem NAME
Rem
Rem FUNCTION
Rem  CRS_1.7.2 , EMS_2.6.4
Rem
Rem NOTES
Rem
Rem
Rem MODIFIED
Rem     شرکت فرافن- 98/04/01-
Rem
-------------------------------------------------------------------------------------------
----------------------------RUN IN GAM_MAIN_EMS--------------------------------------------
-------------------------------------------------------------------------------------------
CREATE SEQUENCE GAM_MAIN_EMS.SEQ_CARD_REQUEST_TRACKING_ID INCREMENT BY 1 START WITH 300000000 MAXVALUE 599999999 MINVALUE 300000000 CACHE 1000 NOORDER  NOCYCLE;