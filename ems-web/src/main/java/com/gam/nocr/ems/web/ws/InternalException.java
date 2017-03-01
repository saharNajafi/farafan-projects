package com.gam.nocr.ems.web.ws;

import javax.xml.ws.WebFault;

/**
 * An implementation of {@link com.gam.nocr.ems.web.ws.WebServiceFaultException} that is declared as the only exception
 * thrown by all web services exposed to CCOS
 *
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */

@WebFault(name = "EMSWebServiceFault", targetNamespace = "http://ws.web.ems.nocr.gam.com/")
public class InternalException extends WebServiceFaultException {

    public InternalException(String message, InternalInterfaceException faultInfo) {
        super(message, faultInfo);
    }

    public InternalException(String message, InternalInterfaceException faultInfo, Throwable cause) {
        super(message, faultInfo, cause);
    }

    @Override
    public InternalInterfaceException getFaultInfo() {
        return (InternalInterfaceException) super.getFaultInfo();
    }


}
