
package com.gam.nocr.ems.biz.service.external.client.emks.lds;

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
@WebService(name = "IService_EMKS", targetNamespace = "http://tempuri.org/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface IServiceEMKS {


    /**
     * 
     * @param requestForCardKeysOrPins
     * @param cardInfo
     * @return
     *     returns com.gam.nocr.ems.biz.service.external.client.emks.lds.CardKeysAndPINs
     * @throws IServiceEMKSGetNIDCardKeysOrPINsEMKSExceptionFaultFaultMessage
     */
    @WebMethod(operationName = "GetNIDCardKeysOrPINs", action = "http://tempuri.org/IService_EMKS/GetNIDCardKeysOrPINs")
    @WebResult(name = "GetNIDCardKeysOrPINsResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "GetNIDCardKeysOrPINs", targetNamespace = "http://tempuri.org/", className = "com.gam.nocr.ems.biz.service.external.client.emks.lds.GetNIDCardKeysOrPINs")
    @ResponseWrapper(localName = "GetNIDCardKeysOrPINsResponse", targetNamespace = "http://tempuri.org/", className = "com.gam.nocr.ems.biz.service.external.client.emks.lds.GetNIDCardKeysOrPINsResponse")
    public CardKeysAndPINs getNIDCardKeysOrPINs(
        @WebParam(name = "CardInfo", targetNamespace = "http://tempuri.org/")
        String cardInfo,
        @WebParam(name = "requestForCardKeysOrPins", targetNamespace = "http://tempuri.org/")
        RequestForCardKeysOrPINs requestForCardKeysOrPins)
        throws IServiceEMKSGetNIDCardKeysOrPINsEMKSExceptionFaultFaultMessage
    ;

    /**
     * 
     * @param cardInfo
     * @return
     *     returns com.gam.nocr.ems.biz.service.external.client.emks.lds.CardMoCKeys
     * @throws IServiceEMKSGetNIDCardMoCKeysEMKSExceptionFaultFaultMessage
     */
    @WebMethod(operationName = "GetNIDCardMoCKeys", action = "http://tempuri.org/IService_EMKS/GetNIDCardMoCKeys")
    @WebResult(name = "GetNIDCardMoCKeysResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "GetNIDCardMoCKeys", targetNamespace = "http://tempuri.org/", className = "com.gam.nocr.ems.biz.service.external.client.emks.lds.GetNIDCardMoCKeys")
    @ResponseWrapper(localName = "GetNIDCardMoCKeysResponse", targetNamespace = "http://tempuri.org/", className = "com.gam.nocr.ems.biz.service.external.client.emks.lds.GetNIDCardMoCKeysResponse")
    public CardMoCKeys getNIDCardMoCKeys(
        @WebParam(name = "CardInfo", targetNamespace = "http://tempuri.org/")
        String cardInfo)
        throws IServiceEMKSGetNIDCardMoCKeysEMKSExceptionFaultFaultMessage
    ;

    /**
     * 
     * @param rndData
     * @return
     *     returns java.lang.String
     * @throws IServiceEMKSGetSignatureEMKSExceptionFaultFaultMessage
     */
    @WebMethod(operationName = "GetSignature", action = "http://tempuri.org/IService_EMKS/GetSignature")
    @WebResult(name = "GetSignatureResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "GetSignature", targetNamespace = "http://tempuri.org/", className = "com.gam.nocr.ems.biz.service.external.client.emks.lds.GetSignature")
    @ResponseWrapper(localName = "GetSignatureResponse", targetNamespace = "http://tempuri.org/", className = "com.gam.nocr.ems.biz.service.external.client.emks.lds.GetSignatureResponse")
    public String getSignature(
        @WebParam(name = "RndData", targetNamespace = "http://tempuri.org/")
        String rndData)
        throws IServiceEMKSGetSignatureEMKSExceptionFaultFaultMessage
    ;

}
