package com.gam.nocr.ems.biz.service.external.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.xml.namespace.QName;

import org.slf4j.Logger;

import servicePortUtil.ServicePorts;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.profile.ProfileManager;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
//import com.gam.nocr.ems.biz.service.external.client.portal.ExternalInterfaceException_Exception;
//import com.gam.nocr.ems.biz.service.external.client.portal.RegistrationWS;
//import com.gam.nocr.ems.biz.service.external.client.portal.RegistrationWS_Service;
//import com.gam.nocr.ems.biz.service.external.client.portal.ReservationWS;
//import com.gam.nocr.ems.biz.service.external.client.portal.ReservationWS_Service;
//import com.gam.nocr.ems.biz.service.external.client.portal.ReservationWTO;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.ProfileHelper;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.domain.CardRequestTO;
import com.gam.nocr.ems.data.domain.EnrollmentOfficeTO;
import com.gam.nocr.ems.data.domain.ReservationTO;
import com.gam.nocr.ems.data.mapper.tomapper.CardRequestMapper;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */

@Stateless(name = "PortalReservationService")
@Local(PortalReservationServiceLocal.class)
@Remote(PortalReservationServiceRemote.class)
public class PortalReservationServiceImpl extends EMSAbstractService implements PortalReservationServiceLocal, PortalReservationServiceRemote {

    private static final String DEFAULT_WSDL_URL = "http://localhost:7001/portal-web/services/ReservationWS?wsdl";
    private static final String DEFAULT_NAMESPACE = "http://portalws.ws.web.portal.nocr.gam.com/";

    private static final String PRT_S_RVI_020 = "PRT_S_RVI_020";

    private static final Logger logger = BaseLog.getLogger(CMSServiceImpl.class);
    private static final Logger portalLogger = BaseLog.getLogger("PortalLogger");
    private static final Logger threadLocalLogger = BaseLog.getLogger("threadLocal");

//    ReservationWS service = null;

    /**
     * The method getService is used to get WebServices from Portal sub system
     *
     * @return an instance of type {@link ReservationWS}
     * @throws com.gam.commons.core.BaseException
     *          if cannot get the service
     */
//    private ReservationWS getService() throws BaseException {
//        try {
//            ProfileManager pm = ProfileHelper.getProfileManager();
//
//            String wsdlUrl = (String) pm.getProfile(ProfileKeyName.KEY_PORTAL_RESERVATION_ENDPOINT, true, null, null);
//            String namespace = (String) pm.getProfile(ProfileKeyName.KEY_PORTAL_NAMESPACE, true, null, null);
//            if (wsdlUrl == null)
//                wsdlUrl = DEFAULT_WSDL_URL;
//            if (namespace == null)
//                namespace = DEFAULT_NAMESPACE;
//            String serviceName = "ReservationWS";
//            logger.debug("Portal Reservation wsdl url: " + wsdlUrl);
//            portalLogger.debug("Portal Reservation wsdl url: " + wsdlUrl);
//            //Commented for ThreadLocal
//            //ReservationWS port = new ReservationWS_Service(new URL(wsdlUrl), new QName(namespace, serviceName)).getReservationWSPort();
//            ReservationWS port = ServicePorts.getPortalReservationPort();
//			if (port == null) {
//				threadLocalLogger.debug("**************************** new PortalReservation in Portal getService()");
//				port = new ReservationWS_Service(new URL(wsdlUrl), new QName(namespace, serviceName)).getReservationWSPort();
//				ServicePorts.setPortalReservationPort(port);
//			} else {
//				threadLocalLogger.debug("***************************** using PortalReservation from ThradLocal");
//			}
//            EmsUtil.setJAXWSWebserviceProperties(port, wsdlUrl);
//            return port;
//        } catch (Exception e) {
//            throw new ServiceException(BizExceptionCode.PRR_001, e.getMessage(), e);
//        }
//    }

    /**
     * The method convertToReservationTOList is used to convert a list of type {@link ReservationWTO} to a list of type
     * {@link ReservationTO}
     *
     * @param reservationWTOList a list of type {@link ReservationWTO}
     * @return a list of type {@link ReservationTO}
     */
//    private List<ReservationTO> convertToReservationTOList(List<ReservationWTO> reservationWTOList) throws BaseException {
//        try {
//            List<ReservationTO> reservationTOList = new ArrayList<ReservationTO>();
//			for (ReservationWTO reservationWTO : reservationWTOList) {
//                ReservationTO reservationTO = new ReservationTO();
//
//                CardRequestTO cardRequestTO = CardRequestMapper.convert(reservationWTO.getCitizen());
//                cardRequestTO.setPortalRequestId(reservationWTO.getCardRequestId());
//                reservationTO.setCardRequest(cardRequestTO);
//                //Anbari:Payment*********************************
//                reservationTO.setPaid(reservationWTO.isPaid());
//                reservationTO.setPaidDate(reservationWTO.getPaidDate() == null ? null : reservationWTO.getPaidDate().toGregorianCalendar().getTime());
//                //***********************************************
//                reservationTO.setEnrollmentOffice(new EnrollmentOfficeTO(reservationWTO.getEnrollmentOfficeId()));
//                reservationTO.setDate(reservationWTO.getReservationDate().toGregorianCalendar().getTime());
//                reservationTO.setPortalReservationId(reservationWTO.getId());
//
//                reservationTOList.add(reservationTO);
//            }
//            return reservationTOList;
//        } catch (Exception e) {
//            portalLogger.error(BizExceptionCode.PRR_004, e.getMessage(), e);
//            throw new ServiceException(BizExceptionCode.PRR_004, e.getMessage(), e);
//        }
//    }

//    @Override
//    public List<Long> fetchReservationIds() throws BaseException {
//        try {
//            return getService().fetchReservationIds();
//        } catch (ExternalInterfaceException_Exception e) {
//            String errorMessage = e.getFaultInfo().getMessage();
//
//            ServiceException serviceException = new ServiceException(
//                    BizExceptionCode.PRR_005,
//                    errorMessage,
//                    e,
//                    EMSLogicalNames.SRV_PORTAL_RESERVATION.split(","));
//            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_PORTAL_RESERVATION.split(","));
//            portalLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_PORTAL_RESERVATION.split(","));
//            throw serviceException;
//        }
//    }

    /**
     * The method doActivityForReservations is used to transfer the reservations which were done on the sub system
     * 'Profile'
     *
     * @return a list of type {@link com.gam.nocr.ems.data.domain.ReservationTO}
     * @throws com.gam.commons.core.BaseException
     *
     */
//    @Override
//    public List<ReservationTO> transferReservations(List<Long> reservationIds) throws BaseException {
//        List<ReservationTO> reservationTOList = new ArrayList<ReservationTO>();
//        try {
//            List<ReservationWTO> reservationWTOList = getService().transferReservation(reservationIds);
//            if (reservationWTOList != null && !reservationWTOList.isEmpty()) {
//                reservationTOList = convertToReservationTOList(reservationWTOList);
//            }
//        } catch (ExternalInterfaceException_Exception e) {
//            String errorMessage = e.getFaultInfo().getMessage();
//
//            ServiceException serviceException = new ServiceException(
//                    BizExceptionCode.PRR_002,
//                    errorMessage,
//                    e,
//                    EMSLogicalNames.SRV_PORTAL_RESERVATION.split(","));
//            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_PORTAL_RESERVATION.split(","));
//            portalLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_PORTAL_RESERVATION.split(","));
//            throw serviceException;
//        }
//        return reservationTOList;
//    }

    /**
     * The method receivedByEMS is used to inform the sub system 'Portal' about the reservations which are saved by EMS.
     *
     * @param portalReservationIds a list of type {@link Long} which represents reservations which are save by EMS
     * @return true or false
     * @throws com.gam.commons.core.BaseException
     *
     */
//    @Override
//    public Boolean receivedByEMS(List<Long> portalReservationIds) throws BaseException {
//        Boolean returnValue;
//        try {
//            returnValue = getService().receivedByEMS(portalReservationIds);
//        } catch (ExternalInterfaceException_Exception e) {
//            String errorMessage = e.getFaultInfo().getMessage();
//
//            ServiceException serviceException = new ServiceException(
//                    BizExceptionCode.PRR_003,
//                    errorMessage,
//                    e,
//                    EMSLogicalNames.SRV_PORTAL_RESERVATION.split(","));
//            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_PORTAL_RESERVATION.split(","));
//            portalLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_PORTAL_RESERVATION.split(","));
//            throw serviceException;
//        }
//        return returnValue;  //To change body of implemented methods use File | Settings | File Templates.
//    }
}
