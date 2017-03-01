
package com.gam.nocr.ems.biz.service.external.client.emks;

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
 *         &lt;element name="GetNIDCardPINsResult" type="{http://schemas.datacontract.org/2004/07/EMKS_WCFService}CardPINs" minOccurs="0"/>
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
    "getNIDCardPINsResult"
})
@XmlRootElement(name = "GetNIDCardPINsResponse")
public class GetNIDCardPINsResponse {

    @XmlElementRef(name = "GetNIDCardPINsResult", namespace = "http://tempuri.org/", type = JAXBElement.class)
    protected JAXBElement<CardPINs> getNIDCardPINsResult;

    /**
     * Gets the value of the getNIDCardPINsResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link CardPINs }{@code >}
     *     
     */
    public JAXBElement<CardPINs> getGetNIDCardPINsResult() {
        return getNIDCardPINsResult;
    }

    /**
     * Sets the value of the getNIDCardPINsResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link CardPINs }{@code >}
     *     
     */
    public void setGetNIDCardPINsResult(JAXBElement<CardPINs> value) {
        this.getNIDCardPINsResult = ((JAXBElement<CardPINs> ) value);
    }

}
