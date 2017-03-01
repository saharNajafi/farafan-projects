
package com.gam.nocr.ems.biz.service.external.client.portal;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.3-b02-
 * Generated source version: 2.1
 * 
 */
@WebService(name = "ReservationWS", targetNamespace = "http://portalws.ws.web.portal.nocr.gam.com/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface ReservationWS {


    /**
     * 
     * @param reservationIds
     * @return
     *     returns java.util.List<com.gam.nocr.ems.biz.service.external.client.portal.ReservationWTO>
     * @throws ExternalInterfaceException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "transferReservation", targetNamespace = "http://portalws.ws.web.portal.nocr.gam.com/", className = "com.gam.nocr.ems.biz.service.external.client.portal.TransferReservation")
    @ResponseWrapper(localName = "transferReservationResponse", targetNamespace = "http://portalws.ws.web.portal.nocr.gam.com/", className = "com.gam.nocr.ems.biz.service.external.client.portal.TransferReservationResponse")
    public List<ReservationWTO> transferReservation(
        @WebParam(name = "reservationIds", targetNamespace = "")
        List<Long> reservationIds)
        throws ExternalInterfaceException_Exception
    ;

    /**
     * 
     * @return
     *     returns java.util.List<java.lang.Long>
     * @throws ExternalInterfaceException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "fetchReservationIds", targetNamespace = "http://portalws.ws.web.portal.nocr.gam.com/", className = "com.gam.nocr.ems.biz.service.external.client.portal.FetchReservationIds")
    @ResponseWrapper(localName = "fetchReservationIdsResponse", targetNamespace = "http://portalws.ws.web.portal.nocr.gam.com/", className = "com.gam.nocr.ems.biz.service.external.client.portal.FetchReservationIdsResponse")
    public List<Long> fetchReservationIds()
        throws ExternalInterfaceException_Exception
    ;

    /**
     * 
     * @param reservationIds
     * @return
     *     returns java.lang.Boolean
     * @throws ExternalInterfaceException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "receivedByEMS", targetNamespace = "http://portalws.ws.web.portal.nocr.gam.com/", className = "com.gam.nocr.ems.biz.service.external.client.portal.ReceivedByEMS")
    @ResponseWrapper(localName = "receivedByEMSResponse", targetNamespace = "http://portalws.ws.web.portal.nocr.gam.com/", className = "com.gam.nocr.ems.biz.service.external.client.portal.ReceivedByEMSResponse")
    public Boolean receivedByEMS(
        @WebParam(name = "reservationIds", targetNamespace = "")
        List<Long> reservationIds)
        throws ExternalInterfaceException_Exception
    ;

}
