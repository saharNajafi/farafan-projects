create or replace
procedure fill_outgoing_refer_sms(i_card_request_id in number,
                                              i_message           in varchar2) is
  v_czi_mobile      VARCHAR2(20);
  v_first_name_fa   VARCHAR2(42 CHAR);
  v_surname_fa      VARCHAR2(42 CHAR);

  v_resultstr    varchar2(4000);
  v_message_body VARCHAR2(4000 CHAR) := i_message;

  v_error varchar2(4000);
begin
  select czi.czi_mobile,
         ctz.ctz_first_name_fa,
         ctz.ctz_surname_fa
    into v_czi_mobile, v_first_name_fa, v_surname_fa
    from emst_card_request crq, emst_citizen ctz, emst_citizen_info czi
   where crq.crq_citizen_id = ctz.ctz_id
     and ctz.ctz_id = czi.czi_id
     and crq.crq_id = i_card_request_id;

  v_message_body := replace(v_message_body, '{fname}', v_first_name_fa);
  v_message_body := replace(v_message_body, '{lname}', v_surname_fa);  

  if v_czi_mobile is not null then
    insert into msgt_outgoing_sms
      (OSM_ID,
       OSM_REQUEST_DATE,
       OSM_CELL_NO,
       OSM_MESSAGE_BODY,
       OSM_RETRY_COUNT,
       OSM_PRIORITY,
       OSM_RETRY_LIMIT,OSM_TYPE)
    values
      (seq_outging_sms.nextval,
       current_date,
       v_czi_mobile,
       v_message_body,
       0,
       1,
       10,
       1);
    --commit;
    v_resultstr := 'success to insert sms request with portal request id : ' ||
                   i_card_request_id;
  else    
    if v_czi_mobile is null then
      v_resultstr := 'fail to insert sms request with portal request id : ' ||
                     i_card_request_id ||
                     ' SQLERRM : cell number is null';
    end if;
  end if;

  insert into db_log
    (dbl_id, dbl_name, dbl_type, dbl_status, dbl_comment, dbl_date)
  values
    (seq_db_log.nextval,
     'fill_outgoing_sms',
     'PRC',
     'SUCCESS',
     v_resultstr,
     current_date);

EXCEPTION
  WHEN OTHERS THEN
    v_error := SQLERRM;

    insert into db_log
      (dbl_id, dbl_name, dbl_type, dbl_status, dbl_comment, dbl_date)
    values
      (seq_db_log.nextval,
       'fill_outgoing_sms',
       'PRC',
       'ERROR',
       'fail to insert sms request with portal request id : ' ||
       i_card_request_id || ' SQLERRM : ' || v_error,
       current_date);

    commit;
end fill_outgoing_refer_sms;