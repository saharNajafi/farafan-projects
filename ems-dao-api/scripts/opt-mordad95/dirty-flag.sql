ALTER TABLE EMST_CARD_REQUEST 
ADD (CRQ_SYNC_FLAG NUMBER(1) DEFAULT 0 NOT NULL);


CREATE INDEX IX_CRQ_SYNC_FLAG ON EMST_CARD_REQUEST (CRQ_SYNC_FLAG) 
TABLESPACE TS_NOCR_EMS_INDEX;

---------------------------------trigger-------------------------------


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
  
  if :new.CRQ_PORTAL_REQUEST_ID is not null then
    :new.CRQ_SYNC_FLAG := 1;
  end if;
  
end RTIR_AU_EMST_CARD_REQ;


------------------------------initial update----------------------------

alter table EMST_CARD_REQUEST disable all triggers;

update /*+ NOLOGGING PARALLEL(4) */ EMST_CARD_REQUEST set CRQ_SYNC_FLAG = 1 where CRQ_ID in
(SELECT  CRQ.CRQ_ID FROM EMST_CARD_REQUEST CRQ WHERE 
(CRQ.CRQ_LAST_SYNC_DATE is NULL OR CRQ.CRQ_LAST_MODIFIED_DATE > CRQ.CRQ_LAST_SYNC_DATE) 
AND CRQ.CRQ_PORTAL_REQUEST_ID is NOT NULL);

alter table EMST_CARD_REQUEST enable all triggers;