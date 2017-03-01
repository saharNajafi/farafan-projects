package com.gam.nocr.ems.web.ws;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.Internal;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.delegator.CardDelegator;
import com.gam.nocr.ems.biz.delegator.CardManagementDelegator;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.ws.ImsCitizenInfoRequestWTO;
import com.gam.nocr.ems.data.domain.ws.ImsCitizenInfoResponseWTO;
import com.gam.nocr.ems.data.domain.ws.SecurityContextWTO;
import com.gam.nocr.ems.data.enums.AfterDeliveryRequestType;
import com.gam.nocr.ems.data.enums.UnSuccessfulDeliveryRequestReason;
import org.slf4j.Logger;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.WebFault;

/**
 * Collection of services related to the card (e.g. delivering, after delivery service, etc.) are implemented in this
 * web service
 *
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */

@WebFault
        (
                faultBean = "com.gam.nocr.ems.web.ws.InternalException"
        )
@WebService
        (
                serviceName = "CardWS",
                portName = "CardWSPort",
                targetNamespace = "http://ws.web.ems.nocr.gam.com/"
        )
@SOAPBinding
        (
                style = SOAPBinding.Style.DOCUMENT,
                use = SOAPBinding.Use.LITERAL,
                parameterStyle = SOAPBinding.ParameterStyle.WRAPPED
        )
@Internal
public class CardWS extends EMSWS {

    private static final Logger logger = BaseLog.getLogger(CardWS.class);

    private CardManagementDelegator cardManagementDelegator = new CardManagementDelegator();

    /**
     * Given an identifier of a request and ensures that its corresponding CRN value is the same as given CRN parameter.
     * It's only use is in the delivery process of the card. When user tries to deliver the card to the citizen, in the
     * last step, a call to this service ensures that the physical card that is handing out to the citizen is the exact
     * card that has been issued for him/her.<br/>
     * This will prevent it to deliver an invalid card (a card that has been issued for the same citizen but is already
     * revoked or missed) to the citizen
     *
     * @param securityContextWTO The login and session information of the user
     * @param requestID          The identifier of the {@link com.gam.nocr.ems.data.domain.CardRequestTO} object that
     *                           its CRN value should be checked
     * @param crn                The CRN value of the physical card that is already in card reader and is going to be
     *                           handed out to the citizen
     * @return The result of comparison. True, if the given CRN is the same as given request CRN
     * value and False in other cases.
     * @throws InternalException
     */
    @WebMethod
    public Boolean checkCRNValidation(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
                                      @WebParam(name = "requestID") Long requestID,
                                      @WebParam(name = "crn") String crn) throws InternalException {
        UserProfileTO up = super.validateRequest(securityContextWTO);
        try {
            return cardManagementDelegator.checkCRNValidation(up, requestID, crn);
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()));
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.CRW_001_MSG + requestID, new EMSWebServiceFault(WebExceptionCode.CRW_001), e);
        }
    }

    /**
     * After delivering a card to the citizen, its delivery will be notified to the EMS by calling this service.
     *
     * @param securityContextWTO The login and session information of the user
     * @param requestID          The identifier of the {@link com.gam.nocr.ems.web.action.CardRequestListAction}
     *                           object that has been delivered
     * @param message            As an important step in card activation process, the user has to sign a message
     *                           using his/her smart card. This step is a key step to physically activate the smart
     *                           card and is taken by CCOS (through CFS). This parameter is the exact message that
     *                           has been signed by citizen in this step
     * @param messageDigest      The signed message
     * @throws InternalException
     */
    @WebMethod
    public void deliver(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
                        @WebParam(name = "requestID") long requestID,
                        @WebParam(name = "message") String message,
                        @WebParam(name = "messageDigest") byte[] messageDigest) throws InternalException {
        UserProfileTO up = super.validateRequest(securityContextWTO);
        //Anbari
        //super.isNocrOffice(up);
        super.isNocrOfficeOrDeliveryOffice(up);
        try {
            cardManagementDelegator.deliver(up, requestID, message, messageDigest);
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()));
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.CRW_001_MSG + requestID, new EMSWebServiceFault(WebExceptionCode.CRW_001), e);
        }
    }

    /**
     * Returns the message that should be signed by citizen in card delivery process in order to make his/her card
     * physically active.
     *
     * @param securityContextWTO The login and session information of the user
     * @param requestId          The identifier of a {@link com.gam.nocr.ems.data.domain.CardRequestTO} object to
     *                           generate a message base on its citizen information (name, nid, etc.)
     * @return The message to be signed by citizen smart card in card activation step in deliver
     * process
     * @throws InternalException
     * @see com.gam.nocr.ems.web.ws.CardWS#deliver(com.gam.nocr.ems.data.domain.ws.SecurityContextWTO, long, String, byte[])
     */
    @WebMethod
    public String retrieveDeliverMessage(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
                                         @WebParam(name = "requestId") Long requestId) throws InternalException {
        UserProfileTO up = super.validateRequest(securityContextWTO);
        //Anbari
        //super.isNocrOffice(up);
        super.isNocrOfficeOrDeliveryOffice(up);
        try {
            return cardManagementDelegator.retrieveDeliverMessage(up, requestId);
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()));
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.CRW_008_MSG, new EMSWebServiceFault(WebExceptionCode.CRW_008), e);
        }
    }

    /**
     * In order to used to inform the EMS about the unsuccessful delivery of a card request and its reason, this method
     * would be called by CCOS
     *
     * @param securityContextWTO The login and session information of the user
     * @param requestID          The identifier of the {@link com.gam.nocr.ems.data.domain.CardRequestTO} object that
     *                           has not been delivered successfully
     * @param reason             The reason of unsuccessful delivery. It should be the string representation of one
     *                           of values specified in {@link com.gam.nocr.ems.data.enums.UnSuccessfulDeliveryRequestReason}
     * @throws InternalException
     */
    @WebMethod
    public void notifyUnsuccessfulDelivery(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
                                           @WebParam(name = "requestID") Long requestID,
                                           @WebParam(name = "reason") String reason) throws InternalException {

        UserProfileTO userProfileTO = super.validateRequest(securityContextWTO);
        //Anbari
        //super.isNocrOffice(userProfileTO);
        super.isNocrOfficeOrDeliveryOffice(userProfileTO);

        UnSuccessfulDeliveryRequestReason validReason = null;
        try {
            validReason = UnSuccessfulDeliveryRequestReason.valueOf(reason);
        } catch (Exception e) {
            throw new InternalException("Invalid Reason! - the reason '" + reason + "' is not a valid reason in the request with id " + requestID,
                    new EMSWebServiceFault(WebExceptionCode.CRW_005), e);
        }

        try {
            cardManagementDelegator.notifyUnsuccessfulDelivery(userProfileTO, requestID, validReason);
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode(), e.getArgs()));
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.CRW_002_MSG + requestID, new EMSWebServiceFault(WebExceptionCode.CRW_002), e);
        }
    }

    /**
     * Notifies the EMS about an after delivery services. Some after delivery services are just an offline service that
     * change something in physical card (e.g. change pin). In such situations, this service would be called by CCOS
     * just to inform the EMS about accomplishment of the activity to save a log in BizLog for future reports or
     * occasional investigations.<br/>
     * In cases like suspending a card, or revoking it, the actual operation is to call a service from the CMS. In such
     * scenarios the EMS has to call the CMS services and inform the CCOS about the operation results.
     *
     * @param securityContextWTO The login and session information of the user
     * @param requestID          The identifier of the {@link com.gam.nocr.ems.web.action.CardRequestListAction}
     *                           object that the after delivery service is requested for it
     * @param requestType        The type of after delivery service. Its value should be the string representation of
     *                           one of values defined in {@link com.gam.nocr.ems.data.enums.AfterDeliveryRequestType}
     * @param complementaryData  Any complementary data necessary to finish the operation. It's not mandatory in all
     *                           requestTypes. For more information see
     *                           {@link com.gam.nocr.ems.biz.service.internal.impl.AfterDeliveryServiceImpl#doAfterDelivery(long, com.gam.nocr.ems.data.enums.AfterDeliveryRequestType, String)}
     * @throws InternalException
     */
    @WebMethod
    public void doAfterDeliveryService(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
                                       @WebParam(name = "requestID") long requestID,
                                       @WebParam(name = "requestType") String requestType,
                                       @WebParam(name = "complementaryData") String complementaryData) throws InternalException {

        UserProfileTO up = super.validateRequest(securityContextWTO);
        //Anbari
        //super.isNocrOffice(up);
        super.isNocrOfficeOrDeliveryOffice(up);
        
        try {
            new CardDelegator().doAfterDelivery(up, requestID, AfterDeliveryRequestType.valueOf(requestType), complementaryData);
        } catch (BaseException e) {
            String errorMessage = e.getMessage();
            String errorCode = e.getExceptionCode();
            logger.error(errorMessage, errorCode, e);
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()));
        } catch (Exception e) {
            throw new InternalException("Unable to execute after delivery service of type " + requestType + " for request id " + requestID,
                    new EMSWebServiceFault(WebExceptionCode.CRW_004), e);
        }
    }

    /**
     * Given a citizen certificate object and validates it using OCSP protocol. As there is no such services in project
     * this service is not used
     *
     * @param certificate        The certificate object to check its validity
     * @param securityContextWTO The login and session information of the user
     * @throws InternalException
     */
    @WebMethod
    public void doOCSPVerification(@WebParam(name = "certificate") byte[] certificate, @WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO) throws InternalException {
        UserProfileTO userProfileTO = super.validateRequest(securityContextWTO);
        try {
            new CardDelegator().doOCSPVerification(certificate, userProfileTO);
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode(), e.getArgs()));
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.CRW_007_MSG, new EMSWebServiceFault(WebExceptionCode.CRW_007), e);
        }
    }
    /**
     * this method is called in card delivering process and use getEstelam3 to verification citizen
     * @author ganjyar
     */
    @WebMethod
    public ImsCitizenInfoResponseWTO doImsVerificationInDelivery(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
    										@WebParam(name = "imsCitizenInfoRequestWTO") ImsCitizenInfoRequestWTO imsCitizenInfoRequestWTO) throws InternalException {
        UserProfileTO userProfileTO = super.validateRequest(securityContextWTO);
        try {
        	ImsCitizenInfoResponseWTO imsCitizenInfoResponse = new CardDelegator().doImsVerificationInDelivery(userProfileTO,imsCitizenInfoRequestWTO);
            return imsCitizenInfoResponse;
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode(), e.getArgs()));
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.CRW_009_MSG, new EMSWebServiceFault(WebExceptionCode.CRW_009), e);
        }
    }
}
