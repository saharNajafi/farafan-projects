create or replace TRIGGER RTIR_BIU_EMST_CARD_REQ
  before insert or update of crq_state, CRQ_ENROLL_OFFICE_ID on EMST_CARD_REQUEST
  for each row
  -- Author  : Vahidi
  -- Created : 04/07/12
  -- Purpose : UPDATE LAST MODIFY DATE
declare
begin
  if :new.crq_state != 'ISSUED' then
    :new.crq_last_modified_date := sysdate;    
  end if;
  
  if :new.CRQ_PORTAL_REQUEST_ID is not null and :new.crq_state != 'STOPPED' and :new.crq_state != 'RESERVED' then
    :new.CRQ_SYNC_FLAG := 1;
  end if;
  
  end RTIR_BIU_EMST_CARD_REQ;