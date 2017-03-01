 alter table emst_card_request drop constraint "CKC_CRQ_ORIGIN";
 alter table emst_card_request add constraint "CKC_CRQ_ORIGIN" CHECK (CRQ_ORIGIN  IN ('M', 'C', 'P', 'V')) ENABLE;