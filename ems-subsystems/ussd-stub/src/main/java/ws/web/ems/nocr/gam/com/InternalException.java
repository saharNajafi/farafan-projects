
package ws.web.ems.nocr.gam.com;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b14002
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "EMSWebServiceFault", targetNamespace = "http://ws.web.ems.nocr.gam.com/")
public class InternalException
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private InternalInterfaceException faultInfo;

    /**
     * 
     * @param message
     * @param faultInfo
     */
    public InternalException(String message, InternalInterfaceException faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param message
     * @param faultInfo
     * @param cause
     */
    public InternalException(String message, InternalInterfaceException faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: ws.web.ems.nocr.gam.com.InternalInterfaceException
     */
    public InternalInterfaceException getFaultInfo() {
        return faultInfo;
    }

}
