
package com.gam.nocr.ems.biz.service.external.client.emks.lds;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.3-b02-
 * Generated source version: 2.1
 * 
 */
@WebFault(name = "EMKSException", targetNamespace = "http://schemas.datacontract.org/2004/07/EMKS_WCFService")
public class IServiceEMKSGetNIDCardMoCKeysEMKSExceptionFaultFaultMessage
    extends java.lang.Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private EMKSException faultInfo;

    /**
     * 
     * @param message
     * @param faultInfo
     */
    public IServiceEMKSGetNIDCardMoCKeysEMKSExceptionFaultFaultMessage(String message, EMKSException faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param message
     * @param faultInfo
     * @param cause
     */
    public IServiceEMKSGetNIDCardMoCKeysEMKSExceptionFaultFaultMessage(String message, EMKSException faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: com.gam.nocr.ems.biz.service.external.client.emks.lds.EMKSException
     */
    public EMKSException getFaultInfo() {
        return faultInfo;
    }

}