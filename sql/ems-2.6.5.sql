---------------------------------------------------------------------------------
--------------------------------add column---------------------------------------

ALTER TABLE GAM_MAIN_EMS.EMST_ENROLLMENT_OFFICE
ADD EOF_IS_DELETED NUMBER(1,0) DEFAULT 0 NOT NULL ENABLE;

---------------------------------------------------------------------------------
--------------------------------add row------------------------------------------

DECLARE
    seq_val NUMBER;
BEGIN
    select GAM_MAIN_EMS.seq_inft_profile_key.nextval into seq_val from dual;
    INSERT INTO GAM_MAIN_EMS.INFT_PROFILE_KEY (PRF_ID, PRF_PARENT_ID, PRF_SYS_ID, PRF_NAME, PRF_PRIORITY_CHAIN, PRF_STATUS, PRF_VALUES, PRF_ICON_CLASS, PRF_PER_ACCESS, PRF_DEP_ACCESS, PRF_SYS_ACCESS)
    VALUES (seq_val, 3, 1, 'nocr.ems.profile.criticalCardRequestStates', 'P,D,S', 'H', NULL, NULL, NULL, NULL, NULL);
    INSERT INTO GAM_MAIN_EMS.INFT_SYSTEM_PROFILE (SPF_ID, SPF_PRF_ID, SPF_CLOB, SPF_BLOB, SPF_DATE)
    VALUES (seq_val, seq_val, null, NULL, NULL);
    commit;
END ;

