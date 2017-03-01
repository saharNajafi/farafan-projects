delete from inft_system_profile se where se.spf_prf_id =(select ke.prf_id from inft_profile_key ke where ke.prf_name='nocr.ems.profile.state.ccos.canImportFaceFromFile');
delete from inft_system_profile se where se.spf_prf_id =(select ke.prf_id from inft_profile_key ke where ke.prf_name='nocrsddf.ems.profile.state.ccos.canImportFaceFromScan');
delete from inft_system_profile se where se.spf_prf_id =(select ke.prf_id from inft_profile_key ke where ke.prf_name='nocr.ems.profile.state.ccos.face.allowEditBackground');
delete from inft_system_profile se where se.spf_prf_id =(select ke.prf_id from inft_profile_key ke where ke.prf_name='nocr.ems.profile.state.ccos.face.icaoTestsEnabled');
delete from inft_system_profile se where se.spf_prf_id =(select ke.prf_id from inft_profile_key ke where ke.prf_name='nocr.ems.profile.state.ccos.finger.tenprint.allowUnusableFingerStatus');
-------------------------


delete from inft_profile_key ke where ke.prf_name= 'nocr.ems.profile.state.ccos.canImportFaceFromFile';
delete from inft_profile_key ke where ke.prf_name= 'nocrsddf.ems.profile.state.ccos.canImportFaceFromScan';
delete from inft_profile_key ke where ke.prf_name= 'nocr.ems.profile.state.ccos.face.allowEditBackground';
delete from inft_profile_key ke where ke.prf_name= 'nocr.ems.profile.state.ccos.face.icaoTestsEnabled';
delete from inft_profile_key ke where ke.prf_name= 'nocr.ems.profile.state.ccos.finger.tenprint.allowUnusableFingerStatus';
commit;
--------------------------
--29,30,32 have exceptions
Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (2105,3,1,'nocr.ems.profile.state.ccos.document.areaRectangleNotSelectedColor','S,D,P','H',null,null,null,null);
Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (2106,3,1,'nocr.ems.profile.state.ccos.document.bitDepth','S,D,P','H',null,null,null,null);
Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (2107,3,1,'nocr.ems.profile.state.ccos.document.compressionMaxSizeLimitBytes','S,D,P','H',null,null,null,null);
Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (2108,3,1,'nocr.ems.profile.state.ccos.document.compressionMinSizeLimitBytes','S,D,P','H',null,null,null,null);
Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (2109,3,1,'nocr.ems.profile.state.ccos.document.compressionMode','S,D,P','H',null,null,null,null);
Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (2110,3,1,'nocr.ems.profile.state.ccos.document.compressionQuality','S,D,P','H',null,null,null,null);
Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (2111,3,1,'nocr.ems.profile.state.ccos.document.imageFormat','S,D,P','H',null,null,null,null);
Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (2112,3,1,'nocr.ems.profile.state.ccos.document.NewStyleCertificateChildrenHeight','S,D,P','H',null,null,null,null);
Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (2113,3,1,'nocr.ems.profile.state.ccos.document.NewStyleCertificateChildrenWidth','S,D,P','H',null,null,null,null);
Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (2114,3,1,'nocr.ems.profile.state.ccos.document.NewStyleCertificateFirstpageHeight','S,D,P','H',null,null,null,null);
Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (2115,3,1,'nocr.ems.profile.state.ccos.document.NewStyleCertificateFirstpageWidth','S,D,P','H',null,null,null,null);
Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (2116,3,1,'nocr.ems.profile.state.ccos.document.NewStyleCertificateSpouseHeight','S,D,P','H',null,null,null,null);
Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (2117,3,1,'nocr.ems.profile.state.ccos.document.NewStyleCertificateSpouseWidth','S,D,P','H',null,null,null,null);
Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (2118,3,1,'nocr.ems.profile.state.ccos.document.OldStyleCertificateChildrenHeight','S,D,P','H',null,null,null,null);
Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (2119,3,1,'nocr.ems.profile.state.ccos.document.OldStyleCertificateChildrenWidth','S,D,P','H',null,null,null,null);
Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (2120,3,1,'nocr.ems.profile.state.ccos.document.OldStyleCertificateFirstpageHeight','S,D,P','H',null,null,null,null);
Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (2121,3,1,'nocr.ems.profile.state.ccos.document.OldStyleCertificateFirstpageWidth','S,D,P','H',null,null,null,null);
Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (2122,3,1,'nocr.ems.profile.state.ccos.document.OldStyleCertificateSpouseHeight','S,D,P','H',null,null,null,null);
Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (2123,3,1,'nocr.ems.profile.state.ccos.document.OldStyleCertificateSpouseWidth','S,D,P','H',null,null,null,null);
Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (2124,3,1,'nocr.ems.profile.state.ccos.document.scanResolution','S,D,P','H',null,null,null,null);
Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (2125,3,1,'nocr.ems.profile.state.ccos.document.soundNotificationsEnabled','S,D,P','H',null,null,null,null);
Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (2126,3,1,'nocr.ems.profile.state.ccos.document.validateOutput','S,D,P','H',null,null,null,null);
Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (2127,3,1,'nocr.ems.profile.state.ccos.document.windowBackgroundColor','S,D,P','H',null,null,null,null);
Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (2128,3,1,'nocr.ems.profile.state.ccos.face.displayCursorLocation','S,D,P','H',null,null,null,null);
Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (2129,3,1,'nocr.ems.profile.state.ccos.FaceEnrollment.Settings.LimitEyesPositionChange','S,D,P','H',null,null,null,null);
Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (2130,3,1,'nocr.ems.profile.state.ccos.finger.automaticCaptureRetryCountForElderly','S,D,P','H',null,null,null,null);
Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (2131,3,1,'nocr.ems.profile.state.ccos.finger.elderlyAgeThreshold','S,D,P','H',null,null,null,null);
Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (2132,3,1,'nocr.ems.profile.state.ccos.finger.tenprint.minCaptureRetryCountForLowQualityFingersForElderly','S,D,P','H',null,null,null,null);
Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (2133,3,1,'nocr.ems.profile.state.ccos.finger.tenprint.minimumGoodQualityFingerCount','S,D,P','H',null,null,null,null);
Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (2134,3,1,'nocr.ems.profile.state.ccos.finger.tenprint.minimumTotalFingerCount','S,D,P','H',null,null,null,null);
Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (2135,3,1,'nocr.ems.profile.state.ccos.finger.tenprint.preserveLowQualityFingerImages','S,D,P','H',null,null,null,null);
Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (2136,3,1,'nocr.ems.profile.state.ccos.finger.tenprint.saveUnsegmentedFingerImages','S,D,P','H',null,null,null,null);
Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (2137,3,1,'nocr.ems.profile.state.ccos.finger.tenprint.allowReplaceWithLowerQualityFingers','S,D,P','H',null,null,null,null);
--------------------------
-- system profile keys
--29,30,32
Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values  (2105,2105,null,'Red');
Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values  (2106,2106,null,'Colored24bpp');
Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values  (2107,2107,null,'153600');
Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values  (2108,2108,null,'51200');
Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values  (2109,2109,null,'SizeLimit');
Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values  (2110,2110,null,'100');
Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values  (2111,2111,null,'Jpeg');
Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values  (2112,2112,null,'100');
Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values  (2113,2113,null,'141');
Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values  (2114,2114,null,'121');
Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values  (2115,2115,null,'141');
Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values  (2116,2116,null,'190');
Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values  (2117,2117,null,'141');
Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values  (2118,2118,null,'77');
Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values  (2119,2119,null,'210');
Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values  (2120,2120,null,'147');
Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values  (2121,2121,null,'110');
Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values  (2122,2122,null,'77');
Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values  (2123,2123,null,'210');
Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values  (2124,2124,null,'100');
Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values  (2125,2125,null,'TRUE');
Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values  (2126,2126,null,'FALSE');
Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values  (2127,2127,null,'LightBlue');
Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values  (2128,2128,null,'FALSE');
Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values  (2129,2129,null,'FALSE');
Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values  (2130,2130,null,'1');
Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values  (2131,2131,null,'200');
Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values  (2132,2132,null,'1');
Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values  (2133,2133,null,'0');
Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values  (2134,2134,null,'0');
Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values  (2135,2135,null,'TRUE');
Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values  (2136,2136,null,'TRUE');
Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values  (2137,2137,null,'FALSE');
commit;
