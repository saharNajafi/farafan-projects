
package com.gam.nocr.ems.biz.service.external.client.emks.lds;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CardKeysAndPINs complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CardKeysAndPINs">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ASD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ASD_ENC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ASD_MAC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NMoC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Sign" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CardKeysAndPINs", namespace = "http://schemas.datacontract.org/2004/07/EMKS_WCFService", propOrder = {
    "asd",
    "asdenc",
    "asdmac",
    "id",
    "nMoC",
    "sm",
    "sign"
})
public class CardKeysAndPINs {

    @XmlElementRef(name = "ASD", namespace = "http://schemas.datacontract.org/2004/07/EMKS_WCFService", type = JAXBElement.class)
    protected JAXBElement<String> asd;
    @XmlElementRef(name = "ASD_ENC", namespace = "http://schemas.datacontract.org/2004/07/EMKS_WCFService", type = JAXBElement.class)
    protected JAXBElement<String> asdenc;
    @XmlElementRef(name = "ASD_MAC", namespace = "http://schemas.datacontract.org/2004/07/EMKS_WCFService", type = JAXBElement.class)
    protected JAXBElement<String> asdmac;
    @XmlElementRef(name = "ID", namespace = "http://schemas.datacontract.org/2004/07/EMKS_WCFService", type = JAXBElement.class)
    protected JAXBElement<String> id;
    @XmlElementRef(name = "NMoC", namespace = "http://schemas.datacontract.org/2004/07/EMKS_WCFService", type = JAXBElement.class)
    protected JAXBElement<String> nMoC;
    @XmlElementRef(name = "SM", namespace = "http://schemas.datacontract.org/2004/07/EMKS_WCFService", type = JAXBElement.class)
    protected JAXBElement<String> sm;
    @XmlElementRef(name = "Sign", namespace = "http://schemas.datacontract.org/2004/07/EMKS_WCFService", type = JAXBElement.class)
    protected JAXBElement<String> sign;

    /**
     * Gets the value of the asd property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getASD() {
        return asd;
    }

    /**
     * Sets the value of the asd property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setASD(JAXBElement<String> value) {
        this.asd = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the asdenc property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getASDENC() {
        return asdenc;
    }

    /**
     * Sets the value of the asdenc property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setASDENC(JAXBElement<String> value) {
        this.asdenc = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the asdmac property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getASDMAC() {
        return asdmac;
    }

    /**
     * Sets the value of the asdmac property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setASDMAC(JAXBElement<String> value) {
        this.asdmac = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getID() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setID(JAXBElement<String> value) {
        this.id = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the nMoC property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getNMoC() {
        return nMoC;
    }

    /**
     * Sets the value of the nMoC property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setNMoC(JAXBElement<String> value) {
        this.nMoC = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the sm property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSM() {
        return sm;
    }

    /**
     * Sets the value of the sm property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSM(JAXBElement<String> value) {
        this.sm = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the sign property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSign() {
        return sign;
    }

    /**
     * Sets the value of the sign property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSign(JAXBElement<String> value) {
        this.sign = ((JAXBElement<String> ) value);
    }

}
