Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (1000,3,1,'nocr.ems.profile.smsBodyDocumentAuthenticate','S,D,P','H',null,null,null,null);


Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (1001,3,1,'nocr.ems.profile.smsBodyRepealedFirstCard','S,D,P','H',null,null,null,null);

Insert into INFT_PROFILE_KEY (PRF_ID,PRF_PARENT_ID,PRF_SYS_ID,PRF_NAME,PRF_PRIORITY_CHAIN,PRF_STATUS,PRF_ICON_CLASS,PRF_PER_ACCESS,PRF_DEP_ACCESS,PRF_SYS_ACCESS) values (1002,3,1,'nocr.ems.profile.smsBodyRepealedOthers','S,D,P','H',null,null,null,null);

Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values (1000,1000,null,'متقاضی گرامی {fname} {lname}، تصویرچهره اخذ شده شما دچار مشکل شده است. لطفا برای اخذ تصویر چهره خود به  دفتر پیش خوان قبلی مراجعه کنید. سازمان ثبت احوال کشور');
Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values (1001,1001,null,'متقاضی گرامی {fname} {lname}، درخواست کارت هوشمند ملی شما ابطال شده است. لطفا برای ویرایش اطلاعات خود به وبگاه ثبت نام کارت هوشمند ملی مراجعه کنید. سازمان ثبت احوال کشور');
Insert into INFT_SYSTEM_PROFILE (SPF_ID,SPF_PRF_ID,SPF_DATE,SPF_CLOB) values (1002,1002,null,'متقاضی گرامی {fname} {lname}، درخواست کارت هوشمند ملی شما ابطال شده است. لطفا برای تکمیل ثبت نام خود به  دفتر پیش خوان قبلی مراجعه کنید. سازمان ثبت احوال کشور');

commit;




 