
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
 *         &lt;element name="GetNIDCardKeysAndPINsResult" type="{http://schemas.datacontract.org/2004/07/EMKS_WCFService}CardKeysAndPINs" minOccurs="0"/>
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
    "getNIDCardKeysAndPINsResult"
})
@XmlRootElement(name = "GetNIDCardKeysAndPINsResponse")
public class GetNIDCardKeysAndPINsResponse {

    @XmlElementRef(name = "GetNIDCardKeysAndPINsResult", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<CardKeysAndPINs> getNIDCardKeysAndPINsResult;

    /**
     * Gets the value of the getNIDCardKeysAndPINsResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link CardKeysAndPINs }{@code >}
     *     
     */
    public JAXBElement<CardKeysAndPINs> getGetNIDCardKeysAndPINsResult() {
        return getNIDCardKeysAndPINsResult;
    }

    /**
     * Sets the value of the getNIDCardKeysAndPINsResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link CardKeysAndPINs }{@code >}
     *     
     */
    public void setGetNIDCardKeysAndPINsResult(JAXBElement<CardKeysAndPINs> value) {
        this.getNIDCardKeysAndPINsResult = value;
    }

}
