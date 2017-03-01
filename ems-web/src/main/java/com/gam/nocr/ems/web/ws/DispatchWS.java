package com.gam.nocr.ems.web.ws;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.Internal;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.delegator.DispatchingDelegator;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.ws.SecurityContextWTO;
import org.slf4j.Logger;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.WebFault;

/**
 * All the operations related to the dispatching process in CCOS offices are handled through services exposed by this
 * web service
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */

@WebService(
        serviceName = "DispatchWS",
        portName = "DispatchWSPort",
        targetNamespace = "http://dispatchws.ws.web.ems.nocr.gam.com/"
)
@SOAPBinding(
        style = SOAPBinding.Style.DOCUMENT,
        use = SOAPBinding.Use.LITERAL,
        parameterStyle = SOAPBinding.ParameterStyle.WRAPPED
)
@WebFault(
        faultBean = "com.gam.nocr.ems.web.ws.ExternalInterfaceException"
)
@Internal
public class DispatchWS extends EMSWS {

    /**
     * An instance of logger that would be used just to log events related to the dispatching process
     */
    private static final Logger dispatchLogger = BaseLog.getLogger("dispatch");

    private DispatchingDelegator dispatchingDelegator = new DispatchingDelegator();

    /**
     * When a card is missed in a NOCR office (before delivering to citizen, this service would be called by CCOS
     *
     * @param securityContextWTO The login and session information of the user
     * @param cardIds            Identifier of {@link com.gam.nocr.ems.data.domain.CardTO} objects that have been lost
     * @throws InternalException
     */
    @WebMethod
    public void itemLost(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
                         @WebParam(name = "cardIds") String cardIds) throws InternalException {
        UserProfileTO up = super.validateRequest(securityContextWTO);
        //Anbari
        //super.isNocrOffice(up);
        super.isNocrOfficeOrDeliveryOffice(up);
        try {
            dispatchLogger.info("The card {} declared to be lost by user {}", cardIds, up.getUserName());
            dispatchingDelegator.itemLost(up, null, null, cardIds);
        } catch (BaseException e) {
            dispatchLogger.error(e.getMessage(), e);
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
        } catch (Exception e) {
            dispatchLogger.error("Unable to declare card {" + cardIds + "} as lost by user {" + up.getUserName() + "}", e);
            throw new InternalException(WebExceptionCode.DPW_001_MSG + cardIds, new EMSWebServiceFault(WebExceptionCode.DPW_001), e);
        }
    }

    /**
     * When a missed card is found in a NOCR office (before notifying CMS about its missing) this service would be
     * called by CCOS
     *
     * @param securityContextWTO The login and session information of the user
     * @param cardIds            Identifier of {@link com.gam.nocr.ems.data.domain.CardTO} objects that have been fount
     * @throws InternalException
     */
    @WebMethod
    public void itemFound(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
                          @WebParam(name = "cardIds") String cardIds) throws InternalException {
        UserProfileTO up = super.validateRequest(securityContextWTO);
        //Anbari
        //super.isNocrOffice(up);
        super.isNocrOfficeOrDeliveryOffice(up);
        try {
            dispatchLogger.info("The card {} declared to be found by user {}", cardIds, up.getUserName());
            dispatchingDelegator.itemFound(up, null, null, cardIds);
        } catch (BaseException e) {
            dispatchLogger.error(e.getMessage(), e);
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
        } catch (Exception e) {
            dispatchLogger.error("Unable to declare card {" + cardIds + "} as found by user {" + up.getUserName() + "}", e);
            throw new InternalException(WebExceptionCode.DPW_002_MSG + cardIds, new EMSWebServiceFault(WebExceptionCode.DPW_002), e);
        }
    }

    /**
     * When CCOS user declares a card (in a batch) as received, this service would be called by CCOS
     *
     * @param securityContextWTO The login and session information of the user
     * @param cardIds            Identifier of {@link com.gam.nocr.ems.data.domain.CardTO} objects that have been received
     * @throws InternalException
     */
    @WebMethod
    public void itemReceived(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
                             @WebParam(name = "cardIds") String cardIds) throws InternalException {
        UserProfileTO up = super.validateRequest(securityContextWTO);
        //Anbari
        //super.isNocrOffice(up);
        super.isNocrOfficeOrDeliveryOffice(up);
        try {
            dispatchLogger.info("The card {} declared to be received by user {}", cardIds, up.getUserName());
            dispatchingDelegator.itemReceived(up, null, null, cardIds);
        } catch (BaseException e) {
            dispatchLogger.error(e.getMessage(), e);
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
        } catch (Exception e) {
            dispatchLogger.error("Unable to declare card {" + cardIds + "} as received by user {" + up.getUserName() + "}", e);
            throw new InternalException(WebExceptionCode.DPW_003_MSG + cardIds, new EMSWebServiceFault(WebExceptionCode.DPW_003), e);
        }
    }

    /**
     * When CCOS user declares a card (in a batch) as not received (e.g. the card doesn't exist in the batch pack) this
     * service would be called by CCOS
     *
     * @param securityContextWTO The login and session information of the user
     * @param cardIds            Identifier of {@link com.gam.nocr.ems.data.domain.CardTO} objects that have not been received
     * @throws InternalException
     */
    @WebMethod
    public void itemNotReceived(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
                                @WebParam(name = "cardIds") String cardIds) throws InternalException {
        UserProfileTO up = super.validateRequest(securityContextWTO);
        //Anbari
        //super.isNocrOffice(up);
        super.isNocrOfficeOrDeliveryOffice(up);
        try {
            dispatchLogger.info("The card {} declared not received by user {}", cardIds, up.getUserName());
            dispatchingDelegator.itemNotReceived(up, null, null, cardIds);
        } catch (BaseException e) {
            dispatchLogger.error(e.getMessage(), e);
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
        } catch (Exception e) {
            dispatchLogger.error("Unable to declare card {" + cardIds + "} as not received by user {" + up.getUserName() + "}", e);
            throw new InternalException(WebExceptionCode.DPW_004_MSG + cardIds, new EMSWebServiceFault(WebExceptionCode.DPW_004), e);
        }
    }

    /**
     * When CCOS user wants to undo the card receive operation to return it to the initial state (ready to receive) this
     * service would be called by CCOS
     *
     * @param securityContextWTO The login and session information of the user
     * @param cardIds            Identifier of {@link com.gam.nocr.ems.data.domain.CardTO} objects that their state should be
     *                           changed to "ready to receive"
     * @throws InternalException
     */
    @WebMethod
    public void backToInitialState(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
                                   @WebParam(name = "cardIds") String cardIds) throws InternalException {
        UserProfileTO up = super.validateRequest(securityContextWTO);
        //Anbari
        //super.isNocrOffice(up);
        super.isNocrOfficeOrDeliveryOffice(up);
        try {
            dispatchLogger.info("The card {} declared to be backed to initial state by user {}", cardIds, up.getUserName());
            dispatchingDelegator.backToInitialState(up, null, null, cardIds);
        } catch (BaseException e) {
            dispatchLogger.error(e.getMessage(), e);
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
        } catch (Exception e) {
            dispatchLogger.error("Unable to declare card {" + cardIds + "} back to initial state by user {" + up.getUserName() + "}", e);
            throw new InternalException(WebExceptionCode.DPW_005_MSG + cardIds, new EMSWebServiceFault(WebExceptionCode.DPW_005), e);
        }
    }
}
