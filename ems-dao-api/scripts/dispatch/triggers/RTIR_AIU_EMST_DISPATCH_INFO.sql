create or replace
TRIGGER "ICT_NOCR_EMS_ANB".RTIR_AIU_EMST_DISPATCH_INFO AFTER INSERT OR UPDATE ON "EMST_DISPATCH_INFO" REFERENCING OLD AS "OLD" NEW AS "NEW" FOR EACH ROW
declare
--  V_CONTAINER_ID NUMBER;
	v_supDepId number;
  PRAGMA AUTONOMOUS_TRANSACTION;
begin
  commit;
  
  
  
  
  if inserting then
    FOR REC IN (select ch.dep_id DEPID,p.dep_dispatch_send_type DEPTYPE,ch.dep_dispatch_send_type CH_DEPTYPE
                from emst_department p,emst_department ch
                where ch.dep_parent_dep_id = p.dep_id
                and p.dep_id = :new.dpi_receiver_dep_id
                and ch.dep_id in (select d.dep_id
                                  from emst_department d
                                  start with d.dep_id in (select r.crq_enroll_office_id
                                                          from emst_box b, emst_batch t, emst_card c, emst_card_request r
                                                          where b.box_id = t.bat_box_id
                                                          and t.bat_id= c.crd_batch_id
                                                          and c.crd_id = r.crq_card_id
                                                          and b.box_id = :new.dpi_container_id)
                                  connect by prior d.dep_parent_dep_id = d.dep_id) ) LOOP
				select
							(CASE
								WHEN t.EOF_TYPE = 'OFFICE' then t.EOF_SUPERIOR_OFFICE
								when t.EOF_TYPE = 'NOCR' then t.EOF_ID
								ELSE t.EOF_ID
							end
							) eof_id into v_supDepId
						from
							emst_enrollment_office t
							where t.EOF_ID = REC.DEPID;

        IF :NEW.DPI_CONTAINER_TYPE = 'BOX' AND REC.DEPTYPE='BATCH' THEN               
          FOR REC_CNTNR IN (SELECT MAX(bt.
          bat_id) CNTNR_ID
                            FROM EMST_BATCH BT,EMST_CARD CD,EMST_CARD_REQUEST RQ
                            WHERE BT.BAT_ID = CD.CRD_BATCH_ID
                            AND CD.CRD_ID = RQ.CRQ_CARD_ID
                            AND RQ.CRQ_ENROLL_OFFICE_ID IN (select d.dep_id
                                                            from emst_department d
                                                            start with d.dep_id = REC.DEPID
                                                            connect by prior d.dep_id=d.dep_parent_dep_id)
                            AND BT.BAT_BOX_ID = :new.dpi_container_id
                            GROUP BY bt.bat_id) LOOP
                            
                            
            

            INSERT INTO emst_dispatch_info D (dpi_container_id, dpi_container_type, dpi_sender_dep_id, dpi_send_date, dpi_receiver_dep_id,
                                              dpi_receive_date, dpi_lost_date, dpi_hidden, dpi_parent_id,dpi_detail_receive_date)
            VALUES (REC_CNTNR.CNTNR_ID,REC.DEPTYPE,:new.dpi_receiver_dep_id,SYSDATE,v_supDepId,NULL,NULL,'F',:new.dpi_id,SYSDATE);                  
            
            
                  COMMIT;     
            
          END LOOP;
        ELSE
        --  V_CONTAINER_ID := :NEW.DPI_CONTAINER_ID;
        
       
        
        
          INSERT INTO emst_dispatch_info D (dpi_container_id, dpi_container_type, dpi_sender_dep_id, dpi_send_date, dpi_receiver_dep_id,
                                            dpi_receive_date, dpi_lost_date, dpi_hidden, dpi_parent_id,dpi_detail_receive_date )
          VALUES (:NEW.DPI_CONTAINER_ID,REC.DEPTYPE,:new.dpi_receiver_dep_id,SYSDATE,REC.DEPID,NULL,NULL,'F',:new.dpi_id,SYSDATE);                
          
          
          COMMIT;
        END IF;
        
         
               
--insert into a values ('dpi_container_id='||V_CONTAINER_ID||' , dpi_container_type='||REC.DEPTYPE||', dpi_sender_dep_id='||
--:new.dpi_receiver_dep_id||' , dpi_send_date=NULL '||', dpi_receiver_dep_id='||REC.DEPID||' dpi_parent_id='||:new.dpi_id) ;
        COMMIT;
    END LOOP;
  --elsif updating then
  end if;
  
 
  
  commit;
--exception
  --when others then
  --  Raise_application_error(-20001, sqlerrm);
end RTIR_AIU_EMST_DISPATCH_INFO;