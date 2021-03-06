
package com.gam.nocr.ems.biz.service.external.client.portal;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.3-b02-
 * Generated source version: 2.1
 * 
 */
@WebFault(name = "ExternalInterfaceException", targetNamespace = "http://portalws.ws.web.portal.nocr.gam.com/")
public class ExternalInterfaceException_Exception
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private ExternalInterfaceException faultInfo;

    /**
     * 
     * @param message
     * @param faultInfo
     */
    public ExternalInterfaceException_Exception(String message, ExternalInterfaceException faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param message
     * @param faultInfo
     * @param cause
     */
    public ExternalInterfaceException_Exception(String message, ExternalInterfaceException faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: com.gam.nocr.ems.biz.service.external.client.portal.ExternalInterfaceException
     */
    public ExternalInterfaceException getFaultInfo() {
        return faultInfo;
    }

}
