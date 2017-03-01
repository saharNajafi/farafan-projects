alter table emst_card_request disable all triggers;
delete from emst_card_request;
delete from emst_card;
delete from emst_card_request_history;
delete from emst_dispatch_info;
delete from emst_batch;
delete from emst_box;
delete from emst_citizen_info;
delete from emst_citizen;
delete from emst_child;
delete from emst_spouse;
delete from emst_biometric;
delete from emst_document;
delete from emst_reservation;
alter table emst_card_request enable all triggers;
commit;


delete from prtl_card_request;
delete from prtl_citizen_info;
delete from prtl_citizen;
delete from prtl_child;
delete from prtl_spouse;
delete from PRTL_RESERVATION_HIS;
delete from PRTL_PAYMENT;
delete from prtl_reservation;
commit;