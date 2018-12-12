
package com.gam.nocr.ems.biz.service.external.client.emks.lds;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CardMoCKeys complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CardMoCKeys">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MoC_ENC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MoC_MAC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CardMoCKeys", namespace = "http://schemas.datacontract.org/2004/07/EMKS_WCFService", propOrder = {
    "moCENC",
    "moCMAC"
})
public class CardMoCKeys {

    @XmlElementRef(name = "MoC_ENC", namespace = "http://schemas.datacontract.org/2004/07/EMKS_WCFService", type = JAXBElement.class)
    protected JAXBElement<String> moCENC;
    @XmlElementRef(name = "MoC_MAC", namespace = "http://schemas.datacontract.org/2004/07/EMKS_WCFService", type = JAXBElement.class)
    protected JAXBElement<String> moCMAC;

    /**
     * Gets the value of the moCENC property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMoCENC() {
        return moCENC;
    }

    /**
     * Sets the value of the moCENC property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMoCENC(JAXBElement<String> value) {
        this.moCENC = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the moCMAC property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMoCMAC() {
        return moCMAC;
    }

    /**
     * Sets the value of the moCMAC property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMoCMAC(JAXBElement<String> value) {
        this.moCMAC = ((JAXBElement<String> ) value);
    }

}
