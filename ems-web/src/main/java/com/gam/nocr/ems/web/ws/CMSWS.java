package com.gam.nocr.ems.web.ws;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.profile.ProfileException;
import com.gam.commons.profile.ProfileManager;
import com.gam.nocr.ems.biz.delegator.DispatchingDelegator;
import com.gam.nocr.ems.config.ProfileHelper;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.domain.CardTO;
import com.gam.nocr.ems.data.domain.ws.CardInfo;
import com.gam.nocr.ems.data.enums.CardState;
import com.gam.nocr.ems.util.EmsUtil;
import org.slf4j.Logger;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.WebFault;
import javax.xml.ws.WebServiceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of contracted services that should be provided by EMS to CMS
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */

@WebFault
        (
                faultBean = "com.gam.nocr.ems.web.ws.ExternalInterfaceException"
        )
@WebService
        (
                serviceName = "CardProductionNotificationService",
                portName = "CardProductionNotificationPort",
                targetNamespace = "http://ws.matiran.gam.com/"
        )
@SOAPBinding
        (
                style = SOAPBinding.Style.DOCUMENT,
                use = SOAPBinding.Use.LITERAL,
                parameterStyle = SOAPBinding.ParameterStyle.WRAPPED
        )

public class CMSWS extends WSSecurity {

    @Resource
    WebServiceContext webServiceContext;

    private static final Logger logger = BaseLog.getLogger(CMSWS.class);

    /**
     * An instance of logger that would be used just to log cms related dispatching events
     */
    private static final Logger dispatchLogger = BaseLog.getLogger("cmsdispatch");

    /**
     * The prefix that would be used to report error messages to CMS in shipment services
     */
    private static String PROSHIP_ERROR_CODE_PREFIX = "EMS-SHPT";
    private static String PROSHIP_ERROR_CODE_PREFIX_NEW = "Exc.EMSCardProductionService";

    /**
     * The prefix that would be used to report error messages to CMS in card error notification service
     */
    private static String CARDERR_ERROR_CODE_PREFIX = "EMS-CARD";

    private DispatchingDelegator dispatchingDelegator = new DispatchingDelegator();

    /**
     * When a batch is ready in CMS, this service would be called by CMS to notify EMS about its properties and contents
     * <br/> for more information see EMS-CMS external interface document
     *
     * @param batchId       Identifier of the batch in CMS
     * @param producedCards Collection of {@link com.gam.nocr.ems.data.domain.ws.CardInfo} objects each containing a
     *                      card information
     * @throws ExternalInterfaceException
     */
   /* @WebMethod
    public void batchProduction(@WebParam(name = "batchId") String batchId,
                                @WebParam(name = "producedCards") List<CardInfo> producedCards) throws ExternalInterfaceException {
        dispatchLogger.info("'batchProduction' web service called by CMS with batchId : {} and productedCards : {}", batchId, producedCards);

        //  Make sure the caller is the CMS (not anyone else)
        try {
            super.authenticate(webServiceContext);
        } catch (BaseException e) {
            logger.error("EMS-INTL-000001 - EMS internal error", e);
            dispatchLogger.error("Authentication failed for 'batchProduction' web services", e);
            throw new ExternalInterfaceException("EMS-INTL-000001", "Unauthorized SSL connection!");
        }

        List<CardTO> cardTOs = new ArrayList<CardTO>();

        *//*
          Due to some inconsistencies between the implementation and the service contract at the project starting days,
          the prefix value used to report errors to CMS is defined as a configurable item in project configuration. So
          here we need to load the prefix from configurations first
        *//*
        String prefix = EmsUtil.getProfileValue(ProfileKeyName.CMS_PROSHIP_ERROR_CODE_PREFIX, PROSHIP_ERROR_CODE_PREFIX);

        if (prefix != null)
            PROSHIP_ERROR_CODE_PREFIX = prefix;

        //  Validating input parameters
        if (!EmsUtil.checkString(batchId)) {
            logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – the parameter 'batchID' is invalid");
            dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – the value of '{}' for parameter 'batchID' is invalid", batchId);
            throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000001",
                    "Invalid parameter – the parameter 'batchID' is invalid");
        }
        if (!EmsUtil.checkListSize(producedCards)) {
            logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – the parameter 'producedCards' is invalid");
            dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – the value of '{}' for parameter 'producedCards' is invalid", producedCards);
            throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000001",
                    "Invalid parameter – the parameter 'producedCards' is invalid");
        }
        for (CardInfo cardInfo : producedCards) {
            if (!EmsUtil.checkString(cardInfo.getRequestId())) {
                logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – the parameter 'requestID' is invalid");
                dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – the value of '{}' for parameter 'requestID' is invalid", cardInfo.getRequestId());
                throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000001",
                        "Invalid parameter – the parameter 'requestID' is invalid");
            }
            if (!EmsUtil.checkString(cardInfo.getCrn())) {
                logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – the parameter 'crn' is invalid");
                dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – the value of '{}' for parameter 'crn' is invalid", cardInfo.getCrn());
                throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000001",
                        "Invalid parameter – the parameter 'crn' is invalid");
            }
            if (cardInfo.getIssuanceDate() == null) {
                logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter - the parameter 'issuanceDate' is invalid");
                dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter - the value of '{}' for parameter 'issuanceDate' is invalid", cardInfo.getIssuanceDate());
                throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000001",
                        "Invalid parameter – the parameter 'issuanceDate' is invalid");
            }
            if (cardInfo.getShipmentDate() == null) {
                logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – the parameter 'shipmentDate' is invalid");
                dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – the value of '{}' for parameter 'shipmentDate' is invalid", cardInfo.getShipmentDate());
                throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000001",
                        "Invalid parameter – the parameter 'shipmentDate' is invalid");
            }
            if (!EmsUtil.checkString(cardInfo.getKeySetVersion())) {
                logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – the parameter 'keySetVersion' is invalid");
                dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – the value of '{}' for parameter 'keySetVersion' is invalid", cardInfo.getKeySetVersion());
                throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000001",
                        "Invalid parameter – the parameter 'keySetVersion' is invalid");
            }
            if (!EmsUtil.checkMaxFieldLength(batchId, 40)) {
                logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000003 - Max Field length – request '" + batchId + "',  parameter ‘batchId’ too big");
                dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000003 - Max Field length – request '" + batchId + "',  parameter ‘batchId’ too big");
                throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000003",
                        "Max Field length – request '" + cardInfo.getRequestId() + "',  parameter ‘batchId’ too big");
            }
            if (!EmsUtil.checkMaxFieldLength(cardInfo.getRequestId(), 32)) {
                logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000003 - Max Field length – request '" + cardInfo.getRequestId() + "',  parameter ‘requestId’ too big");
                dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000003 - Max Field length – request '" + cardInfo.getRequestId() + "',  parameter ‘requestId’ too big");
                throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000003",
                        "Max Field length – request '" + cardInfo.getRequestId() + "',  parameter ‘requestId’ too big");
            }
            if (!EmsUtil.checkMaxFieldLength(cardInfo.getCrn(), 15)) {
                logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000003 - Max Field length – request '" + cardInfo.getRequestId() + "',  parameter ‘crn’ too big");
                dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000003 - Max Field length – the value of '{}' for 'crn' field of request '" + cardInfo.getRequestId() + "' is too big", cardInfo.getCrn());
                throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000003",
                        "Max Field length – request '" + cardInfo.getRequestId() + "',  parameter ‘crn’ too big");
            }
//			if (!EmsUtil.checkMaxFieldLength(cardInfo.getIssuanceDate(), 20)) {
//				throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000003",
//						"Max Field length – request ‘" + cardInfo.getRequestId() + "’,  parameter ‘issuanceDate’ too big");
//			}
//			if (!EmsUtil.checkMaxFieldLength(cardInfo.getShipmentDate(), 20)) {
//				throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000003",
//						"Max Field length – request ‘" + cardInfo.getRequestId() + "’,  parameter ‘shipmentDate’ too big");
//			}
            if (!EmsUtil.checkMaxFieldLength(cardInfo.getKeySetVersion(), 100)) {
                logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000003 - Max Field length – request '" + cardInfo.getRequestId() + "',  parameter ‘keySetVersion’ too big");
                dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000003 - Max Field length – the value of '{}' for 'keySetVersion' field of request '" + cardInfo.getRequestId() + "' is too big", cardInfo.getKeySetVersion());
                throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000003",
                        "Max Field length – request '" + cardInfo.getRequestId() + "',  parameter ‘keySetVersion’ too big");
            }

            CardTO cardTO = new CardTO();
            cardTO.setCrn(cardInfo.getCrn());
            cardTO.setState(CardState.READY);
            cardTO.setCmsRequestId(cardInfo.getRequestId());
            cardTO.setIssuanceDate(cardInfo.getIssuanceDate());
            cardTO.setShipmentDate(cardInfo.getShipmentDate());
            cardTOs.add(cardTO);
        }
        try {
            dispatchingDelegator.batchProduction(null, batchId, cardTOs);
        } catch (Exception e) {
            if (e instanceof BaseException) {
                *//**
                 * Wrap exceptions into an instance of {@link com.gam.nocr.ems.web.ws.ExternalInterfaceException} base
                 * on what that has been contracted by Gemalto
                 *//*
                if (("EMS_S_DPI_010").equals(((BaseException) e).getExceptionCode())) {
                    logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – t he parameter 'batchID' is invalid", e);
                    dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – the parameter 'batchID' is invalid", e);
                    throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000001", "Invalid parameter – the parameter 'batchID' is invalid");
                }
                if (("EMS_S_DPI_011").equals(((BaseException) e).getExceptionCode())) {
                    logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – the parameter 'producedCards' is invalid", e);
                    dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – the parameter 'producedCards' is invalid", e);
                    throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000001", "Invalid parameter – the parameter 'producedCards' is invalid");
                }
                if (("EMS_S_DPI_016").equals(((BaseException) e).getExceptionCode())) {
                    logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 " + e.getMessage(), e);
                    dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 " + e.getMessage(), e);
                    throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000001", e.getMessage());
                }
                if (("EMS_S_DPI_017").equals(((BaseException) e).getExceptionCode())) {
                    logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000002 " + e.getMessage(), e);
                    dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000002 " + e.getMessage(), e);
                    throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000002", e.getMessage());
                }
                if (("EMS_S_DPI_021").equals(((BaseException) e).getExceptionCode())) {
                    logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000007 " + e.getMessage(), e);
                    dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000007 " + e.getMessage(), e);
                    throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000007", e.getMessage());
                }
                if (("EMS_S_DPI_022").equals(((BaseException) e).getExceptionCode())) {
                    logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000006 " + e.getMessage(), e);
                    dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000006 " + e.getMessage(), e);
                    throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000006", e.getMessage());
                } else {
                    logger.error("EMS-INTL-000001 - EMS internal error", e);
                    dispatchLogger.error("EMS-INTL-000001 - EMS internal error", e);
                    throw new ExternalInterfaceException("EMS-INTL-000001", "EMS internal error");
                }
            } else {
                *//**
                 * Wrap any runtime exception into an instance of {@link com.gam.nocr.ems.web.ws.ExternalInterfaceException}
                 * with a dedicated error code
                 *//*
                logger.error("EMS-INTL-000001 - EMS internal error", e);
                dispatchLogger.error("EMS-INTL-000001 - EMS internal error", e);
                throw new ExternalInterfaceException("EMS-INTL-000001", "EMS internal error");
            }
        }
    }*/


    /**
     * When a batch is ready in CMS, this service would be called by CMS to notify EMS about its properties and contents
     * <br/> for more information see EMS-CMS external interface document
     *
     * @param batchId            Identifier of the batch in CMS
     * @param producedCards      Collection of {@link com.gam.nocr.ems.data.domain.ws.CardInfo} objects each containing a
     *                           card information
     * @param postalTrackingCode postal Tracking code send from post algoritm in cms project
     * @throws ExternalInterfaceException
     */

    @WebMethod
    public void batchProduction(@WebParam(name = "batchId") String batchId,
                                                @WebParam(name = "producedCards") List<CardInfo> producedCards,
                                                @WebParam(name = "postalTrackingCode") String postalTrackingCode) throws ExternalInterfaceException {

        dispatchLogger.info("'batchProduction' web service called by CMS with batchId : {} and productedCards : {} and postalTrackingCode : {} ", new Object[]{batchId, producedCards, postalTrackingCode});

        //  Make sure the caller is the CMS (not anyone else)
        try {
            super.authenticate(webServiceContext);
        } catch (BaseException e) {
            logger.error("EMS-INTL-000001 - EMS internal error", e);
            dispatchLogger.error("Authentication failed for 'batchProduction' web services", e);
            throw new ExternalInterfaceException("EMS-INTL-000001", "Unauthorized SSL connection!");
        }

        List<CardTO> cardTOs = new ArrayList<CardTO>();

        /*
          Due to some inconsistencies between the implementation and the service contract at the project starting days,
          the prefix value used to report errors to CMS is defined as a configurable item in project configuration. So
          here we need to load the prefix from configurations first
        */
        String prefix = EmsUtil.getProfileValue(ProfileKeyName.CMS_PROSHIP_ERROR_CODE_PREFIX, PROSHIP_ERROR_CODE_PREFIX);

        if (prefix != null)
            PROSHIP_ERROR_CODE_PREFIX = prefix;

        //  Validating input parameters
        if (!EmsUtil.checkString(batchId)) {
            logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – the parameter 'batchID' is invalid");
            dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – the value of '{}' for parameter 'batchID' is invalid", batchId);
            throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000001",
                    "Invalid parameter – the parameter 'batchID' is invalid");
        }

        if (!EmsUtil.checkListSize(producedCards)) {
            logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – the parameter 'producedCards' is invalid");
            dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – the value of '{}' for parameter 'producedCards' is invalid", producedCards);
            throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000001",
                    "Invalid parameter – the parameter 'producedCards' is invalid");
        }

        if (!EmsUtil.checkString(postalTrackingCode)) {
            logger.error(PROSHIP_ERROR_CODE_PREFIX_NEW + "-000001 - Postal tracking code is empty");
            dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX_NEW + "-000001 Postal tracking code is empty");
            throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX_NEW + "-000001",
                    "Postal tracking code is empty");
        }
        if (!EmsUtil.checkMaxFieldLength(postalTrackingCode, 30)) {
            logger.error(PROSHIP_ERROR_CODE_PREFIX_NEW + "-000002 Too long postal tracking code");
            dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX_NEW + "-000002 Too long postal tracking code");
            throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX_NEW + "-000002", "Too long postal tracking code");
        }

        if (!EmsUtil.checkRegex(postalTrackingCode, EmsUtil.numberConstraint)) {
            logger.error(PROSHIP_ERROR_CODE_PREFIX_NEW + "-000003 Postal tracking contains invalid char");
            dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX_NEW + "-000003 Postal tracking contains invalid char");
            throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX_NEW + "-000003", "Postal tracking contains invalid char");
        }

        for (CardInfo cardInfo : producedCards) {
            if (!EmsUtil.checkString(cardInfo.getRequestId())) {
                logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – the parameter 'requestID' is invalid");
                dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – the value of '{}' for parameter 'requestID' is invalid", cardInfo.getRequestId());
                throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000001",
                        "Invalid parameter – the parameter 'requestID' is invalid");
            }
            if (!EmsUtil.checkString(cardInfo.getCrn())) {
                logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – the parameter 'crn' is invalid");
                dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – the value of '{}' for parameter 'crn' is invalid", cardInfo.getCrn());
                throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000001",
                        "Invalid parameter – the parameter 'crn' is invalid");
            }
            if (cardInfo.getIssuanceDate() == null) {
                logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter - the parameter 'issuanceDate' is invalid");
                dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter - the value of '{}' for parameter 'issuanceDate' is invalid", cardInfo.getIssuanceDate());
                throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000001",
                        "Invalid parameter – the parameter 'issuanceDate' is invalid");
            }
            if (cardInfo.getShipmentDate() == null) {
                logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – the parameter 'shipmentDate' is invalid");
                dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – the value of '{}' for parameter 'shipmentDate' is invalid", cardInfo.getShipmentDate());
                throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000001",
                        "Invalid parameter – the parameter 'shipmentDate' is invalid");
            }
            if (!EmsUtil.checkString(cardInfo.getKeySetVersion())) {
                logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – the parameter 'keySetVersion' is invalid");
                dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – the value of '{}' for parameter 'keySetVersion' is invalid", cardInfo.getKeySetVersion());
                throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000001",
                        "Invalid parameter – the parameter 'keySetVersion' is invalid");
            }
            if (!EmsUtil.checkMaxFieldLength(batchId, 40)) {
                logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000003 - Max Field length – request '" + batchId + "',  parameter ‘batchId’ too big");
                dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000003 - Max Field length – request '" + batchId + "',  parameter ‘batchId’ too big");
                throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000003",
                        "Max Field length – request '" + cardInfo.getRequestId() + "',  parameter ‘batchId’ too big");
            }
            if (!EmsUtil.checkMaxFieldLength(cardInfo.getRequestId(), 32)) {
                logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000003 - Max Field length – request '" + cardInfo.getRequestId() + "',  parameter ‘requestId’ too big");
                dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000003 - Max Field length – request '" + cardInfo.getRequestId() + "',  parameter ‘requestId’ too big");
                throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000003",
                        "Max Field length – request '" + cardInfo.getRequestId() + "',  parameter ‘requestId’ too big");
            }
            if (!EmsUtil.checkMaxFieldLength(cardInfo.getCrn(), 15)) {
                logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000003 - Max Field length – request '" + cardInfo.getRequestId() + "',  parameter ‘crn’ too big");
                dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000003 - Max Field length – the value of '{}' for 'crn' field of request '" + cardInfo.getRequestId() + "' is too big", cardInfo.getCrn());
                throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000003",
                        "Max Field length – request '" + cardInfo.getRequestId() + "',  parameter ‘crn’ too big");
            }
//
            if (!EmsUtil.checkMaxFieldLength(cardInfo.getKeySetVersion(), 100)) {
                logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000003 - Max Field length – request '" + cardInfo.getRequestId() + "',  parameter ‘keySetVersion’ too big");
                dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000003 - Max Field length – the value of '{}' for 'keySetVersion' field of request '" + cardInfo.getRequestId() + "' is too big", cardInfo.getKeySetVersion());
                throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000003",
                        "Max Field length – request '" + cardInfo.getRequestId() + "',  parameter ‘keySetVersion’ too big");
            }

            CardTO cardTO = new CardTO();
            cardTO.setCrn(cardInfo.getCrn());
            cardTO.setState(CardState.READY);
            cardTO.setCmsRequestId(cardInfo.getRequestId());
            cardTO.setIssuanceDate(cardInfo.getIssuanceDate());
            cardTO.setShipmentDate(cardInfo.getShipmentDate());
            cardTOs.add(cardTO);
        }
        try {
            dispatchingDelegator.batchProduction(null, batchId, cardTOs, postalTrackingCode);
        } catch (Exception e) {
            if (e instanceof BaseException) {
                /**
                 * Wrap exceptions into an instance of {@link com.gam.nocr.ems.web.ws.ExternalInterfaceException} base
                 * on what that has been contracted by Gemalto
                 */
                if (("EMS_S_DPI_010").equals(((BaseException) e).getExceptionCode())) {
                    logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – t he parameter 'batchID' is invalid", e);
                    dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – the parameter 'batchID' is invalid", e);
                    throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000001", "Invalid parameter – the parameter 'batchID' is invalid");
                }
                if (("EMS_S_DPI_011").equals(((BaseException) e).getExceptionCode())) {
                    logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – the parameter 'producedCards' is invalid", e);
                    dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – the parameter 'producedCards' is invalid", e);
                    throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000001", "Invalid parameter – the parameter 'producedCards' is invalid");
                }
                if (("EMS_S_DPI_016").equals(((BaseException) e).getExceptionCode())) {
                    logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 " + e.getMessage(), e);
                    dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 " + e.getMessage(), e);
                    throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000001", e.getMessage());
                }
                if (("EMS_S_DPI_017").equals(((BaseException) e).getExceptionCode())) {
                    logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000002 " + e.getMessage(), e);
                    dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000002 " + e.getMessage(), e);
                    throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000002", e.getMessage());
                }
                if (("EMS_S_DPI_021").equals(((BaseException) e).getExceptionCode())) {
                    logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000007 " + e.getMessage(), e);
                    dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000007 " + e.getMessage(), e);
                    throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000007", e.getMessage());
                }
                if (("EMS_S_DPI_022").equals(((BaseException) e).getExceptionCode())) {
                    logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000006 " + e.getMessage(), e);
                    dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000006 " + e.getMessage(), e);
                    throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000006", e.getMessage());
                }
                if (("EMS_S_DPI_064").equals(((BaseException) e).getExceptionCode())) {
                    logger.error(PROSHIP_ERROR_CODE_PREFIX_NEW + "-000005 " + e.getMessage(), e);
                    dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX_NEW + "-000005 " + e.getMessage(), e);
                    throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX_NEW + "-000005", e.getMessage());
                } else {
                    logger.error("EMS-INTL-000001 - EMS internal error", e);
                    dispatchLogger.error("EMS-INTL-000001 - EMS internal error", e);
                    throw new ExternalInterfaceException("EMS-INTL-000001", "EMS internal error");
                }
            } else {
                /**
                 * Wrap any runtime exception into an instance of {@link com.gam.nocr.ems.web.ws.ExternalInterfaceException}
                 * with a dedicated error code
                 */
                logger.error("EMS-INTL-000001 - EMS internal error", e);
                dispatchLogger.error("EMS-INTL-000001 - EMS internal error", e);
                throw new ExternalInterfaceException("EMS-INTL-000001", "EMS internal error");
            }
        }
    }


    public void updatePostalTrackingCode(@WebParam(name = "batchId") String batchId,
                                              @WebParam(name = "postalTrackingCode") String postalTrackingCode) throws ExternalInterfaceException {

        dispatchLogger.info("'updateBatchPostalTrackingCode' web service called by CMS with batchId : {} and postalTrackingCode : {} ", batchId, postalTrackingCode);

        //  Make sure the caller is the CMS (not anyone else)
        try {
            super.authenticate(webServiceContext);
        } catch (BaseException e) {
            logger.error("EMS-INTL-000001 - EMS internal error", e);
            dispatchLogger.error("Authentication failed for 'batchProduction' web services", e);
            throw new ExternalInterfaceException("EMS-INTL-000001", "Unauthorized SSL connection!");
        }

      /*
          Due to some inconsistencies between the implementation and the service contract at the project starting days,
          the prefix value used to report errors to CMS is defined as a configurable item in project configuration. So
          here we need to load the prefix from configurations first
        */
        String prefix = EmsUtil.getProfileValue(ProfileKeyName.CMS_PROSHIP_ERROR_CODE_PREFIX, PROSHIP_ERROR_CODE_PREFIX);

        if (prefix != null)
            PROSHIP_ERROR_CODE_PREFIX = prefix;

        //  Validating input parameters
        if (!EmsUtil.checkString(batchId)) {
            logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – the parameter 'batchID' is invalid");
            dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – the value of '{}' for parameter 'batchID' is invalid", batchId);
            throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000001",
                    "Invalid parameter – the parameter 'batchID' is invalid");
        }

        if (!EmsUtil.checkString(postalTrackingCode)) {
            logger.error(PROSHIP_ERROR_CODE_PREFIX_NEW + "-000001 - Postal tracking code is empty");
            dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX_NEW + "-000001 Postal tracking code is empty");
            throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX_NEW + "-000001",
                    "Postal tracking code is empty");
        }
        if (!EmsUtil.checkMaxFieldLength(postalTrackingCode, 30)) {
            logger.error(PROSHIP_ERROR_CODE_PREFIX_NEW + "-000002 Too long postal tracking code");
            dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX_NEW + "-000002 Too long postal tracking code");
            throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX_NEW + "-000002", "Too long postal tracking code");
        }

        if (!EmsUtil.checkRegex(postalTrackingCode, EmsUtil.numberConstraint)) {
            logger.error(PROSHIP_ERROR_CODE_PREFIX_NEW + "-000003 Postal tracking contains invalid char");
            dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX_NEW + "-000003 Postal tracking contains invalid char");
            throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX_NEW + "-000003", "Postal tracking contains invalid char");
        }

        try {
            dispatchingDelegator.updateBatchPostalTrackingCode(null, batchId, postalTrackingCode);
        } catch (BaseException e) {
            if (e instanceof BaseException) {
                /**
                 * Wrap exceptions into an instance of {@link com.gam.nocr.ems.web.ws.ExternalInterfaceException} base
                 * on what that has been contracted by Gemalto
                 */
                if (("EMS_S_DPI_062").equals(((BaseException) e).getExceptionCode())) {
                    logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – t he parameter 'batchID' is invalid", e);
                    dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – the parameter 'batchID' is invalid", e);
                    throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000001", "Invalid parameter – the parameter 'batchID' is invalid");
                }

                if (("EMS_S_DPI_063").equals(((BaseException) e).getExceptionCode())) {
                    logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – t he parameter 'batchID' is invalid", e);
                    dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – the parameter 'batchID' is invalid", e);
                    throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000001", "Invalid parameter – the parameter 'batchID' is invalid");
                }

                if (("EMS_S_DPI_064").equals(((BaseException) e).getExceptionCode())) {
                    logger.error(PROSHIP_ERROR_CODE_PREFIX_NEW + "-000005 " + e.getMessage(), e);
                    dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX_NEW + "-000005 " + e.getMessage(), e);
                    throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX_NEW + "-000005", e.getMessage());
                }

                if (("EMS_S_DSI_065").equals(((BaseException) e).getExceptionCode())) {
                    logger.error(PROSHIP_ERROR_CODE_PREFIX_NEW + "-000007 " + e.getMessage(), e);
                    dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX_NEW + "-000007 " + e.getMessage(), e);
                    throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX_NEW + "-000007", e.getMessage());
                }
                if (("EMS_S_DSI_066").equals(((BaseException) e).getExceptionCode())) {
                    logger.error(PROSHIP_ERROR_CODE_PREFIX_NEW + "-000006 " + e.getMessage(), e);
                    dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX_NEW + "-000006 " + e.getMessage(), e);
                    throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX_NEW + "-000006", e.getMessage());
                }else {
                    logger.error("EMS-INTL-000001 - EMS internal error", e);
                    dispatchLogger.error("EMS-INTL-000001 - EMS internal error", e);
                    throw new ExternalInterfaceException("EMS-INTL-000001", "EMS internal error");
                }
            } else {
                /**
                 * Wrap any runtime exception into an instance of {@link com.gam.nocr.ems.web.ws.ExternalInterfaceException}
                 * with a dedicated error code
                 */
                logger.error("EMS-INTL-000001 - EMS internal error", e);
                dispatchLogger.error("EMS-INTL-000001 - EMS internal error", e);
                throw new ExternalInterfaceException("EMS-INTL-000001", "EMS internal error");
            }
        }


    }


    /**
     * When a box is ready in CMS, it calls this service from the EMS to notify it about start of box shipment process.
     * <br/> for more information see EMS-CMS external interface document
     *
     * @param boxId    Identifier of the box in CMS
     * @param batchIds Collection of batch identifiers (CMS identifier) included in the givne box
     * @throws ExternalInterfaceException
     */

    public void boxShipped(@WebParam(name = "boxId") String boxId,
                           @WebParam(name = "batchIds") List<String> batchIds) throws ExternalInterfaceException {
        dispatchLogger.info("'boxShipped' web service called by CMS with boxId : {} and batchIds : {}", boxId, batchIds);

        //  Make sure the caller is the CMS (not anyone else)
        try {
            super.authenticate(webServiceContext);
        } catch (BaseException e) {
            logger.info("EMS-INTL-000001 - EMS internal error", e);
            throw new ExternalInterfaceException("EMS-INTL-000001", "unAuthorized SSL connection!");
        }

        /*
          Due to some inconsistencies between the implementation and the service contract at the project starting days,
          the prefix value used to report errors to CMS is defined as a configurable item in project configuration. So
          here we need to load the prefix from configurations first
        */
        ProfileManager pm;
        try {
            pm = ProfileHelper.getProfileManager();
            String prefix = (String) pm.getProfile(ProfileKeyName.CMS_PROSHIP_ERROR_CODE_PREFIX, true, null, null);

            if (prefix != null)
                PROSHIP_ERROR_CODE_PREFIX = prefix;

        } catch (ProfileException e) {
            logger.warn("Unable to read profile key named : " + ProfileKeyName.CMS_PROSHIP_ERROR_CODE_PREFIX + " from database");
        }

        //  Validating input parameters
        if (!EmsUtil.checkString(boxId)) {
            logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – the parameter ‘boxID’ is invalid");
            dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – The value of '{}' for the parameter ‘boxID’ is invalid", boxId);
            throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000001",
                    "Invalid parameter – the parameter ‘boxID’ is invalid");
        }
        if (!EmsUtil.checkMaxFieldLength(boxId, 32)) {
            logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – the parameter ‘boxID’ is invalid");
            dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – The value of '{}' for the parameter ‘boxID’ is too long", boxId);
            throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000001",
                    "Invalid parameter – the parameter ‘boxID’ is invalid");
        }
        if (!EmsUtil.checkListSize(batchIds)) {
            logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – the parameter ‘batchIds’ is invalid");
            dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – The length of '{}' for 'batchIds' is invalid", batchIds.size());
            throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000001",
                    "Invalid parameter – the parameter ‘batchIds’ is invalid");
        }
        for (String batchId : batchIds) {
            if (!EmsUtil.checkString(batchId)) {
                logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000009  - Invalid batch ID – the batch ID ‘" + batchId + "’ is invalid.");
                dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000009  - Invalid parameter – The value of '{}' for the batch ID is invalid", batchId);
                throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000009",
                        "Invalid batch ID – the batch ID ‘" + batchId + "’ is invalid.");
            }
            if (!EmsUtil.checkMaxFieldLength(batchId, 24)) {
                logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000008 - Max Field length – batch ID ‘" + batchId + "’,  parameter ‘" + batchId + "’ is too big");
                dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000008 - Max Field length – batch ID ‘{}' for box '{}' is too big", batchId, boxId);
                throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000008",
                        "Max Field length – batch ID ‘" + batchId + "’,  parameter ‘" + batchId + "’ is too big");
            }
        }

        try {
            dispatchingDelegator.boxShipped(null, boxId, batchIds);
        } catch (Exception e) {
            if (e instanceof BaseException) {
                /**
                 * Wrap exceptions into an instance of {@link com.gam.nocr.ems.web.ws.ExternalInterfaceException} base
                 * on what that has been contracted by Gemalto
                 */
                if (("EMS_S_DPI_023").equals(((BaseException) e).getExceptionCode())) {
                    logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000014 - " + e.getMessage(), e);
                    dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000014 - " + e.getMessage(), e);
                    throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000014", e.getMessage());
                }
//				if (("EMS_S_DPI_024").equals(((BaseException) e).getExceptionCode()))
//					throw new ExternalInterfaceException("EMS-SHPT-000013", e.getMessage());
                if (("EMS_S_DPI_025").equals(((BaseException) e).getExceptionCode())) {
                    logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000011 - " + e.getMessage(), e);
                    dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000011 - " + e.getMessage(), e);
                    throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000011", e.getMessage());
                }
                if (("EMS_S_DPI_026").equals(((BaseException) e).getExceptionCode())) {
                    logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000012 - " + e.getMessage(), e);
                    dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000012 - " + e.getMessage(), e);
                    throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000012", e.getMessage());
                }
                if (("EMS_S_DPI_012").equals(((BaseException) e).getExceptionCode())) {
                    logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – the parameter ‘boxId’ is invalid", e);
                    dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – the parameter ‘boxId’ is invalid", e);
                    throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000001", "Invalid parameter – the parameter ‘boxId’ is invalid");
                }
                if (("EMS_S_DPI_013").equals(((BaseException) e).getExceptionCode())) {
                    logger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – the parameter ‘batchIds’ is invalid", e);
                    dispatchLogger.error(PROSHIP_ERROR_CODE_PREFIX + "-000001 - Invalid parameter – the parameter ‘batchIds’ is invalid", e);
                    throw new ExternalInterfaceException(PROSHIP_ERROR_CODE_PREFIX + "-000001", "Invalid parameter – the parameter ‘batchIds’ is invalid");
                } else {
                    logger.error("EMS-INTL-000001 - EMS internal error", e);
                    dispatchLogger.error("EMS-INTL-000001 - EMS internal error", e);
                    throw new ExternalInterfaceException("EMS-INTL-000001", "EMS internal error");
                }
            } else {
                /**
                 * Wrap any runtime exception into an instance of {@link com.gam.nocr.ems.web.ws.ExternalInterfaceException}
                 * with a dedicated error code
                 */
                logger.error("EMS-INTL-000001 - EMS internal error", e);
                dispatchLogger.error("EMS-INTL-000001 - EMS internal error", e);
                throw new ExternalInterfaceException("EMS-INTL-000001", "EMS internal error");
            }
        }
    }

    /**
     * In case of any error in issuing a card, the CMS notifies EMS about the situation by calling this service
     * <br/> for more information see EMS-CMS external interface document
     *
     * @param requestID   The identifier of the request that has been sent for issue
     * @param errorCode   The error code specifying the exact problem in CMS side
     * @param description A description about the error
     * @throws ExternalInterfaceException
     */
    public void cardProductionError(@WebParam(name = "requestID") String requestID,
                                    @WebParam(name = "errorCode") String errorCode,
                                    @WebParam(name = "description") String description) throws ExternalInterfaceException {

        dispatchLogger.info("'cardProductionError' web service called by CMS with requestID : '{}' and errorCode : '{}' and description : '{}'", new Object[]{requestID, errorCode, description});

        //  Make sure the caller is the CMS (not anyone else)
        try {
            super.authenticate(webServiceContext);
        } catch (BaseException e) {
            logger.info("EMS-INTL-000001 - EMS internal error", e);
            throw new ExternalInterfaceException("EMS-INTL-000001", "unAuthorized SSL connection!");
        }

        /*
          Due to some inconsistencies between the implementation and the service contract at the project starting days,
          the prefix value used to report errors to CMS is defined as a configurable item in project configuration. So
          here we need to load the prefix from configurations first
        */
        ProfileManager pm;
        try {
            pm = ProfileHelper.getProfileManager();
            String prefix = (String) pm.getProfile(ProfileKeyName.CMS_CARDERR_ERROR_CODE_PREFIX, true, null, null);

            if (prefix != null)
                CARDERR_ERROR_CODE_PREFIX = prefix;

        } catch (ProfileException e) {
            throw new ExternalInterfaceException("EMS-INTL-000001", "EMS internal error");
        }

        //  Validating input parameters
        if (!EmsUtil.checkString(requestID)) {
            logger.error(CARDERR_ERROR_CODE_PREFIX + "-000001 : Invalid parameter – the parameter ‘requestID’ is invalid");
            dispatchLogger.error(CARDERR_ERROR_CODE_PREFIX + "-000001 : Invalid parameter – The value of '{}' for the parameter ‘requestID’ is invalid", requestID);
            throw new ExternalInterfaceException(CARDERR_ERROR_CODE_PREFIX + "-000001",
                    "Invalid parameter – the parameter ‘requestID’ is invalid");
        }
        if (!EmsUtil.checkString(errorCode)) {
            logger.error(CARDERR_ERROR_CODE_PREFIX + "-000001 : Invalid parameter – the parameter ‘errorCode’ is invalid");
            dispatchLogger.error(CARDERR_ERROR_CODE_PREFIX + "-000001 : Invalid parameter – The value of '{}' for the parameter ‘errorCode’ is invalid", errorCode);
            throw new ExternalInterfaceException(CARDERR_ERROR_CODE_PREFIX + "-000001",
                    "Invalid parameter – the parameter ‘errorCode’ is invalid");
        }
        if (!EmsUtil.checkString(description)) {
            logger.error(CARDERR_ERROR_CODE_PREFIX + "-000001 : Invalid parameter – the parameter ‘description’ is invalid");
            dispatchLogger.error(CARDERR_ERROR_CODE_PREFIX + "-000001 : Invalid parameter – The value of '{}' for the parameter ‘description’ is invalid", description);
            throw new ExternalInterfaceException(CARDERR_ERROR_CODE_PREFIX + "-000001",
                    "Invalid parameter – the parameter ‘description’ is invalid");
        }
        if (!EmsUtil.checkMaxFieldLength(requestID, 32)) {
            logger.error(CARDERR_ERROR_CODE_PREFIX + "-000001 : Invalid parameter – the parameter ‘requestID’ is invalid");
            dispatchLogger.error(CARDERR_ERROR_CODE_PREFIX + "-000001 : Invalid parameter – The value of '{}' for the parameter ‘requestID’ is too long", requestID);
            throw new ExternalInterfaceException(CARDERR_ERROR_CODE_PREFIX + "-000001",
                    "Invalid parameter – the parameter ‘requestID’ is invalid");
        }
        if (!EmsUtil.checkMaxFieldLength(errorCode, 10)) {
            logger.error(CARDERR_ERROR_CODE_PREFIX + "-000001 : Invalid parameter – the parameter ‘errorCode’ is invalid");
            dispatchLogger.error(CARDERR_ERROR_CODE_PREFIX + "-000001 : Invalid parameter – The value of '{}' for the parameter ‘errorCode’ is invalid", errorCode);
            throw new ExternalInterfaceException(CARDERR_ERROR_CODE_PREFIX + "-000001",
                    "Invalid parameter – the parameter ‘errorCode’ is invalid");
        }
        if (!EmsUtil.checkMaxFieldLength(description, 255)) {
            logger.error(CARDERR_ERROR_CODE_PREFIX + "-000001 : Invalid parameter – the parameter ‘description’ is invalid");
            dispatchLogger.error(CARDERR_ERROR_CODE_PREFIX + "-000001 : Invalid parameter – The value of '{}' for the parameter ‘description’ is invalid", description);
            throw new ExternalInterfaceException(CARDERR_ERROR_CODE_PREFIX + "-000001",
                    "Invalid parameter – the parameter ‘description’ is invalid");
        }
//        if (!("DP01".equals(errorCode) || "PM01".equals(errorCode))) {
//            logger.info(CARDERR_ERROR_CODE_PREFIX + "-000004 : Unknown error code – error code ‘" + errorCode + "’ is unknown");
//            throw new ExternalInterfaceException(CARDERR_ERROR_CODE_PREFIX + "-000004",
//                    "Unknown error code – error code ‘" + errorCode + "’ is unknown");
//        }

        try {
            dispatchingDelegator.cardProductionError(null, requestID, errorCode, description);
        } catch (Exception e) {
            if (e instanceof BaseException) {
                /**
                 * Wrap exceptions into an instance of {@link com.gam.nocr.ems.web.ws.ExternalInterfaceException} base
                 * on what that has been contracted by Gemalto
                 */
                if (("EMS_S_DPI_027").equals(((BaseException) e).getExceptionCode())) {
                    logger.error(CARDERR_ERROR_CODE_PREFIX + "-000002" + e.getMessage(), e);
                    dispatchLogger.error(CARDERR_ERROR_CODE_PREFIX + "-000002" + e.getMessage(), e);
                    throw new ExternalInterfaceException(CARDERR_ERROR_CODE_PREFIX + "-000002", e.getMessage());
                }

                if (("EMS_S_DPI_056").equals(((BaseException) e).getExceptionCode())) {
                    logger.error(CARDERR_ERROR_CODE_PREFIX + "-000002" + e.getMessage(), e);
                    dispatchLogger.error(CARDERR_ERROR_CODE_PREFIX + "-000002" + e.getMessage(), requestID, e);
                    throw new ExternalInterfaceException(CARDERR_ERROR_CODE_PREFIX + "-000002",
                            "Invalid request ID – the request ID " + requestID + " is invalid");
                }
                if (("EMS_S_DPI_028").equals(((BaseException) e).getExceptionCode())) {
                    logger.error(CARDERR_ERROR_CODE_PREFIX + "-000003" + e.getMessage(), e);
                    dispatchLogger.error(CARDERR_ERROR_CODE_PREFIX + "-000003" + e.getMessage(), e);
                    throw new ExternalInterfaceException(CARDERR_ERROR_CODE_PREFIX + "-000003", e.getMessage());
                } else {
                    logger.error("EMS-INTL-000001 : EMS internal error", e);
                    dispatchLogger.error("EMS-INTL-000001 : EMS internal error", e);
                    throw new ExternalInterfaceException("EMS-INTL-000001", "EMS internal error");
                }
            } else {
                /**
                 * Wrap any runtime exception into an instance of {@link com.gam.nocr.ems.web.ws.ExternalInterfaceException}
                 * with a dedicated error code
                 */
                logger.error("EMS-INTL-000001 : EMS internal error", e);
                dispatchLogger.error("EMS-INTL-000001 : EMS internal error", e);
                throw new ExternalInterfaceException("EMS-INTL-000001", "EMS internal error");
            }
        }
    }
}