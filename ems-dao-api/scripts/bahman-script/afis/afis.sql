insert into inft_profile_key pr(pr.prf_id,pr.prf_parent_id,pr.prf_sys_id,pr.prf_name,pr.prf_priority_chain,pr.prf_status,pr.prf_values,pr.prf_icon_class,pr.prf_per_access,pr.prf_dep_access,pr.prf_sys_access) values(2017,3,1,'nocr.ems.profile.numberOfRequestForUpdateAFISFetchLimit','P,D,S','H','',null,null,null,null );
insert into inft_system_profile sypro(sypro.spf_id,sypro.spf_prf_id, sypro.spf_clob,sypro.spf_blob,sypro.spf_date) values(2017,2017,2000,null,null);
commit;
