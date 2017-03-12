--alter table emst_citizen add CTZ_PURGE_BIO number(1,0) default 0;

alter table emst_citizen add CTZ_PURGE_BIO number(1,0);


alter table emst_citizen add CTZ_PURGE_BIO_DATE TIMESTAMP(6);


--alter table emst_citizen add CONSTRAINT CHECK_PURGE_BIO check(CTZ_PURGE_BIO in (0 , 1));

create index IDX_PURGE_FLAG on EMST_CITIZEN (CTZ_PURGE_BIO);

update emst_citizen set ctz_purge_bio = 0 where ctz_purge_bio is null;

-- ALTER TABLE emst_citizen drop column CTZ_PURGE_BIO;
-- ALTER TABLE emst_citizen drop column CTZ_PURGE_BIO_DATE;
-- ALTER TABLE emst_citizen drop CONSTRAINT CHECK_PURGE_BIO;
-- cachePermissions -- > name of cache permission 