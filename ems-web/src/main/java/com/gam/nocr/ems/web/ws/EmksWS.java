package com.gam.nocr.ems.web.ws;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.Internal;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.delegator.EmksDelegator;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.ws.EMKSCardMoCKeysWTO;
import com.gam.nocr.ems.data.domain.ws.EMKSDataResultWTO;
import com.gam.nocr.ems.data.domain.ws.EMKSDataWTO;
import com.gam.nocr.ems.data.domain.ws.SecurityContextWTO;
import org.slf4j.Logger;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.WebFault;

/**
 * Collection of services related to EMKS (e.g. pin management)
 */
@WebFault(faultBean = "com.gam.nocr.ems.web.ws.InternalException")
@WebService(serviceName = "EmksWS", portName = "EmksWSPort", targetNamespace = "http://ws.web.ems.nocr.gam.com/")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@Internal
public class EmksWS extends EMSWS {
    private static final Logger emksLogger = BaseLog.getLogger("EmksLogger");
    static final Logger logger = BaseLog.getLogger(EmksWS.class);

    /**
     * get pin while deliver card to citizen in ccos
     *
     * @param securityContextWTO The login and session information of the user
     * @throws InternalException
     */
    @WebMethod
    public EMKSDataResultWTO getNIDCardPINs(
            @WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
            @WebParam(name = "emksDataWTO") EMKSDataWTO emksDataWTO,
            @WebParam(name = "requestID") long requestID)
            throws InternalException {
        logger.info("\n========================= GET CARD PINS FROM EMKS =========================\n");
        emksLogger
                .info("\n========================= GET CARD PINS FROM EMKS =========================\n");
        logger.info("\n========================================================================\n");
        emksLogger
                .info("\n========================================================================\n");
        UserProfileTO up = super.validateRequest(securityContextWTO);

        EmksDelegator emksDelegator = new EmksDelegator();
        //Anbari
        //super.isNocrOffice(up);
        super.isNocrOfficeOrDeliveryOffice(up);

        try {
            logger.info("\n request id : " + requestID + "\n");
            emksLogger.info("\n request id : " + requestID + "\n");

            return emksDelegator.getNIDCardPINs(up, emksDataWTO, requestID);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
            emksLogger.error(e.getMessage(), e);
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(
                    e.getExceptionCode()), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            emksLogger.error(e.getMessage(), e);
            throw new InternalException(WebExceptionCode.EKW_001,
                    new EMSWebServiceFault(WebExceptionCode.EKW_001_MSG), e);
        }
    }


    @WebMethod
    public String getSigniture(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
                               @WebParam(name = "requestStr") String requestStr) throws InternalException {
        logger.info("\n========================= GET CARD SIGNATURE FROM EMKS =========================\n");
        emksLogger
                .warn("\n========================= GET CARD SIGNATURE FROM EMKS =========================\n");
        logger.info("\n========================================================================\n");
        emksLogger
                .warn("\n========================================================================\n");
        UserProfileTO up = super.validateRequest(securityContextWTO);

        EmksDelegator emksDelegator = new EmksDelegator();
        //Anbari
        //super.isNocrOffice(up);
        super.isNocrOfficeOrDeliveryOffice(up);

        try {
            logger.info("\n request id : " + requestStr + "\n");
            emksLogger.info("\n request id : " + requestStr + "\n");
//			if(!EmsUtil.checkString(requestStr))
//				throw new InternalException(WebExceptionCode.RSW_065_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_078));


            return emksDelegator.getSigniture(up, requestStr);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
            emksLogger.error(e.getMessage(), e);
            throw new InternalException(WebExceptionCode.EKW_002_MSG,
                    new EMSWebServiceFault(WebExceptionCode.EKW_002), e);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            emksLogger.error(e.getMessage(), e);
            throw new InternalException(WebExceptionCode.EKW_003_MSG,
                    new EMSWebServiceFault(WebExceptionCode.EKW_003), e);
        }

    }

    /**
     * get MOCs Keys and Default Pins while deliver card to citizen in ccos
     *
     * @param securityContextWTO The login and session information of the user
     * @throws InternalException
     */
    @WebMethod
    public EMKSCardMoCKeysWTO GetNIDCardMoCKeys(
            @WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
            @WebParam(name = "emksDataWTO") EMKSDataWTO emksDataWTO,
            @WebParam(name = "requestID") long requestID)
            throws InternalException {
        emksLogger
                .info("\n========================= GET CARD MOCs FROM EMKS =========================\n");
        emksLogger
                .info("\n========================================================================\n");
        UserProfileTO up = super.validateRequest(securityContextWTO);

        EmksDelegator emksDelegator = new EmksDelegator();
        //Anbari
        //super.isNocrOffice(up);
        super.isNocrOfficeOrDeliveryOffice(up);

        try {
            emksLogger.info("\n request id : " + requestID + "\n");
            return emksDelegator.getNIDCardMoCKeys(up, emksDataWTO, requestID);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
            emksLogger.error(e.getMessage(), e);
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(
                    e.getExceptionCode()), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            emksLogger.error(e.getMessage(), e);
            throw new InternalException(WebExceptionCode.EKW_004,
                    new EMSWebServiceFault(WebExceptionCode.EKW_004_MSG), e);
        }
    }

}
