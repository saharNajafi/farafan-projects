
/**
 * Reserved Sms
 */
Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS)
values (2102,3,1,'nocr.ems.profile.numberOfProcessReservedSmsFetchLimit','S,D,P','H',null,null,null,null);

Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values (2102,2102,null,1000);


/**
 * Delivery Sms
 */
Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS)
values (2103,3,1,'nocr.ems.profile.numberOfProcessDeliverySmsFetchLimit','S,D,P','H',null,null,null,null);

Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values (2103,2103,null,1000);


/**
 * ReferToCcos sms
 */
Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS)
values (2104,3,1,'nocr.ems.profile.numberOfProcessReferToCcosSmsFetchLimit','S,D,P','H',null,null,null,null);

Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values (2104,2104,null,1000);



