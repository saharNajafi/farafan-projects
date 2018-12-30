package servicePortUtil;

import com.gam.nocr.ems.biz.service.external.client.cms.DocumentRequestWSLocal;
import com.gam.nocr.ems.biz.service.external.client.emks.lds.IServiceEMKS;
import com.gam.nocr.ems.biz.service.external.client.gaas.GAASWebServiceInterface;
import com.gam.nocr.ems.biz.service.external.client.nocrSms.SmsDelegate;
import com.gam.nocr.ems.biz.service.external.client.portal.BasicInfoWS;
import com.gam.nocr.ems.biz.service.external.client.portal.RegistrationWS;
import com.gam.nocr.ems.biz.service.external.client.portal.ReservationWS;

import est.EstelamPort;
import est.ImsService;

public class ServicePorts {

    static ThreadLocal<ServicePortsModel> threadLocal = new ThreadLocal<ServicePortsModel>();

    // GAAS
    public static GAASWebServiceInterface getGassPort() {
        if (threadLocal.get() != null)
            return threadLocal.get().getGaasWebServiceInterface();
        return null;
    }

    public static void setGaasPort(GAASWebServiceInterface dr) {
        if (threadLocal.get() == null) {
            threadLocal.set(new ServicePortsModel());
        }
        threadLocal.get().setGaasWebServiceInterface(dr);
    }

    // CMS
    public static DocumentRequestWSLocal getDocumentCMSPort() {
        if (threadLocal.get() != null)
            return threadLocal.get().getDocumentRequestWSLocal();
        return null;
    }

    public static void setDocumentCMSPort(DocumentRequestWSLocal dr) {
        if (threadLocal.get() == null) {
            threadLocal.set(new ServicePortsModel());
        }
        threadLocal.get().setDocumentRequestWSLocal(dr);
    }

    // AFIS
    public static ImsService getImsPort() {
        if (threadLocal.get() != null)
            return threadLocal.get().getImsService();
        return null;
    }

    public static void setImsPort(ImsService dr) {
        if (threadLocal.get() == null) {
            threadLocal.set(new ServicePortsModel());
        }
        threadLocal.get().setImsService(dr);
    }

    // Estelam
    public static EstelamPort getEstelamPort() {
        if (threadLocal.get() != null)
            return threadLocal.get().getEstelamService();
        return null;
    }

    public static void setEstelamPort(EstelamPort dr) {
        if (threadLocal.get() == null) {
            threadLocal.set(new ServicePortsModel());
        }
        threadLocal.get().setEstelamService(dr);
    }

    // PortalBasicInfo
    public static BasicInfoWS getPortalBasicInfoPort() {
        if (threadLocal.get() != null)
            return threadLocal.get().getPortalBasicInfo();
        return null;
    }

    public static void setPortalBasicInfoPort(BasicInfoWS dr) {
        if (threadLocal.get() == null) {
            threadLocal.set(new ServicePortsModel());
        }
        threadLocal.get().setPortalBasicInfo(dr);
    }

    // PortalRegistration
    public static RegistrationWS getPortalRegistrationPort() {
        if (threadLocal.get() != null)
            return threadLocal.get().getPortalRegistration();
        return null;
    }

    public static void setPortalRegistrationPort(RegistrationWS dr) {
        if (threadLocal.get() == null) {
            threadLocal.set(new ServicePortsModel());
        }
        threadLocal.get().setPortalRegistration(dr);
    }

    // PortalReservation
    public static ReservationWS getPortalReservationPort() {
        if (threadLocal.get() != null)
            return threadLocal.get().getPortalReservation();
        return null;
    }

    public static void setPortalReservationPort(ReservationWS dr) {
        if (threadLocal.get() == null) {
            threadLocal.set(new ServicePortsModel());
        }
        threadLocal.get().setPortalReservation(dr);
    }

    // EMKS
    public static IServiceEMKS getEmksPort() {
        if (threadLocal.get() != null)
            return threadLocal.get().getEmksService();
        return null;
    }

    public static void setEmksPort(IServiceEMKS dr) {
        if (threadLocal.get() == null) {
            threadLocal.set(new ServicePortsModel());
        }
        threadLocal.get().setEmksService(dr);
    }

    // SMS
    public static SmsDelegate getSMSPort() {
        if (threadLocal.get() != null)
            return threadLocal.get().getSmsDelegate();
        return null;
    }

    public static void setSMSPort(SmsDelegate dr) {
        if (threadLocal.get() == null) {
            threadLocal.set(new ServicePortsModel());
        }
        threadLocal.get().setSmsDelegate(dr);
    }


    /*// CardRequestState
    public static CardRequestStateWS getCardRequestStatePort() {
        if (threadLocal.get() != null)
            return threadLocal.get().getCardRequestStateWS();
        return null;
    }

    public static void setCardRequestStatePort(CardRequestStateWS crq) {
        if (threadLocal.get() == null) {
            threadLocal.set(new ServicePortsModel());
        }
        threadLocal.get().setCardRequestStateWS(crq);
    }*/
}
