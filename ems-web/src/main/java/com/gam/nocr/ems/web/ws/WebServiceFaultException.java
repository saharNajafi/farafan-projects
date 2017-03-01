package com.gam.nocr.ems.web.ws;

import javax.xml.ws.WebFault;

/**
 * Base class of internal web service exceptions (exposed to CCOS)
 *
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */

@WebFault(name = "EMSWebServiceFault", targetNamespace = "http://ws.web.ems.nocr.gam.com/")
public class WebServiceFaultException extends Exception {
    private InternalInterfaceException faultInfo;

    public WebServiceFaultException(String message, InternalInterfaceException faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    public WebServiceFaultException(String message, InternalInterfaceException faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    public InternalInterfaceException getFaultInfo() {
        return faultInfo;
    }
}
