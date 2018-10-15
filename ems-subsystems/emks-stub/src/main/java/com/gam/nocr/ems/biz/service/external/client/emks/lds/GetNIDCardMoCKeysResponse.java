
package com.gam.nocr.ems.biz.service.external.client.emks.lds;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetNIDCardMoCKeysResult" type="{http://schemas.datacontract.org/2004/07/EMKS_WCFService}CardMoCKeys" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "getNIDCardMoCKeysResult"
})
@XmlRootElement(name = "GetNIDCardMoCKeysResponse")
public class GetNIDCardMoCKeysResponse {

    @XmlElementRef(name = "GetNIDCardMoCKeysResult", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<CardMoCKeys> getNIDCardMoCKeysResult;

    /**
     * Gets the value of the getNIDCardMoCKeysResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link CardMoCKeys }{@code >}
     *     
     */
    public JAXBElement<CardMoCKeys> getGetNIDCardMoCKeysResult() {
        return getNIDCardMoCKeysResult;
    }

    /**
     * Sets the value of the getNIDCardMoCKeysResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link CardMoCKeys }{@code >}
     *     
     */
    public void setGetNIDCardMoCKeysResult(JAXBElement<CardMoCKeys> value) {
        this.getNIDCardMoCKeysResult = value;
    }

}
