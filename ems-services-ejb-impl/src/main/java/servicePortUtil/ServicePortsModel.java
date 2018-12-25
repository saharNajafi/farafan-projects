package servicePortUtil;

import com.gam.nocr.ems.biz.service.external.client.cms.DocumentRequestWSLocal;
import com.gam.nocr.ems.biz.service.external.client.emks.lds.IServiceEMKS;
import com.gam.nocr.ems.biz.service.external.client.gaas.GAASWebServiceInterface;
import com.gam.nocr.ems.biz.service.external.client.nocrSms.SmsDelegate;
import com.gam.nocr.ems.biz.service.external.client.portal.BasicInfoWS;
import com.gam.nocr.ems.biz.service.external.client.portal.RegistrationWS;
import com.gam.nocr.ems.biz.service.external.client.portal.ReservationWS;
import com.gam.nocr.ems.biz.service.external.client.ussd.CardRequestStateWS;
import est.EstelamPort;
import est.ImsService;

public class ServicePortsModel {

    private DocumentRequestWSLocal documentRequestWSLocal;
    private GAASWebServiceInterface gaasWebServiceInterface;
    private ImsService imsService;
    private EstelamPort estelamService;
//    private BasicInfoWS portalBasicInfo;
//    private RegistrationWS portalRegistration;
//    private ReservationWS portalReservation;
    private IServiceEMKS emksService;
    private SmsDelegate smsDelegate;
    private CardRequestStateWS cardRequestStateWS;

    public DocumentRequestWSLocal getDocumentRequestWSLocal() {
        return documentRequestWSLocal;
    }

    public void setDocumentRequestWSLocal(
            DocumentRequestWSLocal documentRequestWSLocal) {
        this.documentRequestWSLocal = documentRequestWSLocal;
    }

    public GAASWebServiceInterface getGaasWebServiceInterface() {
        return gaasWebServiceInterface;
    }

    public void setGaasWebServiceInterface(
            GAASWebServiceInterface gaasWebServiceInterface) {
        this.gaasWebServiceInterface = gaasWebServiceInterface;
    }

    public ImsService getImsService() {
        return imsService;
    }

    public void setImsService(ImsService imsService) {
        this.imsService = imsService;
    }

    public EstelamPort getEstelamService() {
        return estelamService;
    }

    public void setEstelamService(EstelamPort estelamService) {
        this.estelamService = estelamService;
    }

//    public BasicInfoWS getPortalBasicInfo() {
//        return portalBasicInfo;
//    }

//    public void setPortalBasicInfo(BasicInfoWS portalBasicInfo) {
//        this.portalBasicInfo = portalBasicInfo;
//    }

//    public RegistrationWS getPortalRegistration() {
//        return portalRegistration;
//    }

//    public void setPortalRegistration(RegistrationWS portalRegistration) {
//        this.portalRegistration = portalRegistration;
//    }

//    public ReservationWS getPortalReservation() {
//        return portalReservation;
//    }
//
//    public void setPortalReservation(ReservationWS portalReservation) {
//        this.portalReservation = portalReservation;
//    }

    public IServiceEMKS getEmksService() {
        return emksService;
    }

    public void setEmksService(IServiceEMKS emksService) {
        this.emksService = emksService;
    }

    public SmsDelegate getSmsDelegate() {
        return smsDelegate;
    }

    public void setSmsDelegate(SmsDelegate smsDelegate) {
        this.smsDelegate = smsDelegate;
    }

    public CardRequestStateWS getCardRequestStateWS() {
        return cardRequestStateWS;
    }

    public void setCardRequestStateWS(CardRequestStateWS cardRequestStateWS) {
        this.cardRequestStateWS = cardRequestStateWS;
    }
}
