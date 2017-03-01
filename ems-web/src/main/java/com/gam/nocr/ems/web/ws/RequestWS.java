package com.gam.nocr.ems.web.ws;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.Internal;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.delegator.CardRequestDelegator;
import com.gam.nocr.ems.biz.delegator.CompleteRegistrationDelegator;
import com.gam.nocr.ems.biz.delegator.RegistrationDelegator;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.ws.SecurityContextWTO;
import com.gam.nocr.ems.data.domain.ws.TrackingNumberWTO;
import com.gam.nocr.ems.data.enums.CardRequestedAction;
import com.gam.nocr.ems.data.enums.SystemId;
import org.slf4j.Logger;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.WebFault;
import java.util.Date;

/**
 * Card request operations (not related to updating data) like transfer request, repeal request, etc. are implemented in
 * this web service
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
@WebFault(faultBean = "com.gam.nocr.ems.web.ws.InternalException")
@WebService(serviceName = "RequestWS", portName = "RequestWSPort", targetNamespace = "http://ws.web.ems.nocr.gam.com/")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@Internal
public class RequestWS extends EMSWS {

    static final Logger logger = BaseLog.getLogger(RequestWS.class);

    private CardRequestDelegator cardRequestDelegator = new CardRequestDelegator();
    private CompleteRegistrationDelegator completeRegistrationDelegator = new CompleteRegistrationDelegator();
    private RegistrationDelegator registrationDelegator = new RegistrationDelegator();

    /**
     * Registers a repeal card request or its undo request
     *
     * @param securityContextWTO  The login and session information of the user
     * @param cardRequestId       Identifier of the {@link com.gam.nocr.ems.data.domain.CardRequestTO} to register
     *                            a repeal request
     * @param cardRequestedAction Type of action requested by CCOS. It should be string representation of one of
     *                            values defined in {@link com.gam.nocr.ems.data.enums.CardRequestedAction} starting
     *                            with REPEAL
     * @throws InternalException
     */
    @WebMethod
    public void doCardRequestRepealAction(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
                                          @WebParam(name = "cardRequestId") Long cardRequestId,
                                          @WebParam(name = "cardRequestedAction") String cardRequestedAction) throws InternalException {
        UserProfileTO userProfileTO = super.validateRequest(securityContextWTO);

        try {
            cardRequestDelegator.doCardRequestRepealAction(userProfileTO,
                    cardRequestId, CardRequestedAction.valueOf(cardRequestedAction), SystemId.CCOS);
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode(), e.getArgs()), e);
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RQW_003_MSG, new EMSWebServiceFault(WebExceptionCode.RQW_003), e);
        }
    }

    /**
     * Transfers a card request from CCOS office to NOCR office and vice versa. Undoing the transfer is only available
     * if no operation has been done on transferred card request by NOCR office
     *
     * @param securityContextWTO The login and session information of the user
     * @param cardRequestId      Identifier of the {@link com.gam.nocr.ems.data.domain.CardRequestTO} to transfer
     *                           (or undo it)
     * @param transferAction     Requested operation to register (e.g. transfer, undo, etc.). It should be string
     *                           representation of one of values defined in
     *                           {@link com.gam.nocr.ems.data.enums.CardRequestedAction} starting with TRANSFER
     * @throws InternalException
     */
    @WebMethod
    public void doCardRequestTransferAction(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
                                            @WebParam(name = "cardRequestId") Long cardRequestId,
                                            @WebParam(name = "transferAction") String transferAction) throws InternalException {
        UserProfileTO userProfileTO = super.validateRequest(securityContextWTO);

        try {
            cardRequestDelegator.transferCardRequestToNocr(userProfileTO,
                    cardRequestId, CardRequestedAction.valueOf(transferAction));
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode(), e.getArgs()), e);
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RQW_004_MSG, new EMSWebServiceFault(WebExceptionCode.RQW_004), e);
        }
    }

    /**
     * Generates a unique archiving identifier for given card request in given enrollment office to be printed on top of
     * registration form.
     *
     * @param securityContextWTO The login and session information of the user
     * @param cardRequestId      Identifier of the {@link com.gam.nocr.ems.data.domain.CardRequestTO} to generate an
     *                           archiving identifier for it
     * @param enrollmentOfficeId Identifier of the enrollment office that the request belongs to
     * @return Generated archiving identifier
     * @throws InternalException
     */
    @WebMethod
    public String requestArchiveId(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
                                   @WebParam(name = "cardRequestId") Long cardRequestId,
                                   @WebParam(name = "enrollmentOfficeId") Long enrollmentOfficeId) throws InternalException {
        UserProfileTO userProfileTO = super.validateRequest(securityContextWTO);

        try {
            return completeRegistrationDelegator.requestArchiveId(userProfileTO, cardRequestId, enrollmentOfficeId);
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode(), e.getArgs()), e);
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RQW_001_MSG, new EMSWebServiceFault(WebExceptionCode.RQW_001), e);
        }
    }

    /**
     * In order to access the tracking number for a card request (in cases that a citizen forgot his/her tracking
     * number), this service would be called by CCOS to fetch the tracking number
     *
     * @param securityContextWTO The login and session information of the user
     * @param nationalId         NID of the citizen
     * @param birthDate          Birth date of the citizen
     * @return Tracking number of the citizen if given NID and birth date match
     * @throws InternalException
     */
    @WebMethod
    public TrackingNumberWTO retrieveTrackingNumber(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
                                         @WebParam(name = "nationalId") String nationalId,
                                         @WebParam(name = "birthDate") Date birthDate) throws InternalException {

        UserProfileTO up = super.validateRequest(securityContextWTO);

        try {
            return registrationDelegator.retrieveTrackingNumber(up, nationalId, birthDate);
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode(), e.getArgs()), e);
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RQW_002_MSG, new EMSWebServiceFault(WebExceptionCode.RQW_002), e);
        }
    }
}
