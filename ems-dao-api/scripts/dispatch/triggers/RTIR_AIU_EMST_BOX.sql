create or replace
TRIGGER "ICT_NOCR_EMS_ANB".RTIR_AIU_EMST_BOX
  after update of box_state on EMST_BOX
  for each row
  -- Author  : Vahidi
  -- Created : 11/06/12
  -- Purpose : Insert into emst_dispatch_info
declare
--  STR VARCHAR2(4000);
  v_depid number;
  PRAGMA AUTONOMOUS_TRANSACTION;
begin
  commit;
  if :old.box_state != 'SHIPPED' and :new.box_state = 'SHIPPED' then
    select max(b.dep_id) depid
    into v_depid
    from (select a.dep_id , rownum rn
          from (select p.dep_id,level lv
                from emst_department p
                start with p.dep_id in (select r.crq_enroll_office_id
                                        from emst_box b, emst_batch t, emst_card c, emst_card_request r
                                        where b.box_id = t.bat_box_id
                                        and t.bat_id= c.crd_batch_id
                                        and c.crd_id = r.crq_card_id
                                        and b.box_id = :new.box_id)
                connect by prior p.dep_parent_dep_id = p.dep_id
                group by p.dep_id,level
                order by level desc) a
         ) b
    where b.rn = 2;

    INSERT INTO emst_dispatch_info D (dpi_container_id, dpi_container_type, dpi_sender_dep_id, dpi_send_date, dpi_receiver_dep_id,
                                      dpi_receive_date, dpi_lost_date, dpi_hidden, dpi_parent_id )
    VALUES (:NEW.BOX_ID,'BOX',1,SYSDATE,v_depid,SYSDATE,NULL,'F',NULL);

  elsif :old.box_state != 'MISSED' and :new.box_state = 'MISSED' then
    UPDATE EMST_BATCH BCH
    SET BCH.BAT_STATE = 'MISSED'
    WHERE BCH.BAT_BOX_ID = :NEW.BOX_ID;
  end if;

  commit;
--exception
 -- when others then
 --   Raise_application_error(-20001, sqlerrm);
end RTIR_AIU_EMST_BOX;