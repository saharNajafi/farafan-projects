1- EMS (CMS_PRODUCTION_ERROR) add 'DOCUMENT_AUTHENTICATED' ,'APPROVED_BY_AFIS','REPEALED'
when  :old.crq_state ='CMS_PRODUCTION_ERROR' and :new.crq_state   in  ('CMS_PRODUCTION_ERROR','DOCUMENT_AUTHENTICATED' , 'APPROVED_BY_AFIS','REPEALED')  then  return ;


2-Portal(PENDING_ISSUANCE) add 'REPEALED'
 when  :old.CRQ_CARD_REQUEST_STATUS ='PENDING_ISSUANCE' and :new.CRQ_CARD_REQUEST_STATUS   in  ('DOCUMENT_AUTHENTICATED','APPROVED','SENT_TO_AFIS','APPROVED_BY_AFIS'
																															,'REVOKED_BY_AFIS','PENDING_ISSUANCE','ISSUED','READY_TO_DELIVER','PENDING_TO_DELIVER_BY_CMS','DELIVERED'
																															,'UNSUCCESSFUL_DELIVERY_BECAUSE_OF_DAMAGE','UNSUCCESSFUL_DELIVERY_BECAUSE_OF_BIOMETRIC','NOT_DELIVERED'
																															,'STOPPED','CMS_ERROR','CMS_PRODUCTION_ERROR','IMS_ERROR','REPEALED')  then  return ;
3-Portal(CMS_PRODUCTION_ERROR) add 'REPEALED'
   when  :old.CRQ_CARD_REQUEST_STATUS ='CMS_PRODUCTION_ERROR' and :new.CRQ_CARD_REQUEST_STATUS   in  ('CMS_PRODUCTION_ERROR', 'DOCUMENT_AUTHENTICATED','APPROVED','SENT_TO_AFIS','APPROVED_BY_AFIS'
																															,'REVOKED_BY_AFIS','PENDING_ISSUANCE','ISSUED','READY_TO_DELIVER','PENDING_TO_DELIVER_BY_CMS','DELIVERED'
																															,'UNSUCCESSFUL_DELIVERY_BECAUSE_OF_DAMAGE','UNSUCCESSFUL_DELIVERY_BECAUSE_OF_BIOMETRIC','NOT_DELIVERED'
																															,'STOPPED','CMS_ERROR','CMS_PRODUCTION_ERROR','REPEALED')  then  return ;
																															
