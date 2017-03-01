alter trigger RTIR_AIU_EMST_BATCH disable;
alter trigger RTIR_AIU_EMST_BOX disable;
alter trigger RTIR_AIU_EMST_CARD disable;
alter trigger RTIR_AIU_EMST_CARD_LOST disable;
alter trigger RTIR_AIU_EMST_CARD_RCV disable;
alter trigger RTIR_AIU_EMST_DIS_INF_LOST disable;
alter trigger RTIR_AIU_EMST_DIS_INF_RCV disable;
alter trigger RTIR_AIU_EMST_DISPATCH_INFO disable;
alter trigger RTIR_BI_EMST_DISPATCH_INFO disable;
create or replace
TRIGGER "ICT_NOCR_EMS_SAB".RTIR_BIU_EMST_CARD_REQ
  before insert or update of crq_state, CRQ_ENROLL_OFFICE_ID on EMST_CARD_REQUEST
  for each row
declare
begin
  if :new.crq_state != 'ISSUED' then
    :new.crq_last_modified_date := sysdate;  
  end if;

  
end RTIR_AU_EMST_CARD_REQ;