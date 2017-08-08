package com.gam.nocr.ems.web.ws;


import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Internal;
import com.gam.nocr.ems.biz.delegator.CardRequestDelegator;
import com.gam.nocr.ems.config.WebExceptionCode;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.ws.WebFault;

/**
 * Created by Sahar Najafi on 4/19/17.
 */



@WebFault(faultBean = "com.gam.nocr.ems.web.ws.InternalException")
@WebService(serviceName = "CardRequestStateWS", portName = "CardRequestStatePort", targetNamespace = "http://portalws.ws.web.portal.nocr.farafan.com/")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@Internal
public class CardRequestStateWS {

    private CardRequestDelegator cardRequestDelegator = new CardRequestDelegator();

    @WebMethod
    public String cardRequestState(
            @WebParam(name = "NationalId", targetNamespace = "") @XmlElement(required = true, nillable = false) String nationalId,
            @WebParam(name = "Mobile", targetNamespace = "") @XmlElement(required = true, nillable = false) String mobile
            ) throws InternalException {
        try {
            return cardRequestDelegator.findCardRequestStateByNationalIdAndMobile(nationalId, mobile);
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode(), e.getArgs()), e);
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RQW_003_MSG, new EMSWebServiceFault(WebExceptionCode.RQW_003), e);
        }
    }

    @WebMethod
    public String checkBirthCertificateSerial(
            @WebParam(name = "NationalId", targetNamespace = "") @XmlElement(required = true, nillable = false) String nationalID,
            @WebParam(name = "BirthCertificateSeries", targetNamespace = "") @XmlElement(required = true, nillable = false) String birthCertificateSeries
    ) throws InternalException
    {
        try {
            return cardRequestDelegator.findCardRequestStateByNationalIdAndBirthCertificateSeries(nationalID, birthCertificateSeries);
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode(), e.getArgs()), e);
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RQW_003_MSG, new EMSWebServiceFault(WebExceptionCode.RQW_003), e);
        }

    }
}
