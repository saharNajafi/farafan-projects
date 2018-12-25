
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
 *         &lt;element name="ASd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ENC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MAC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NMoC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PIN_SM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SMd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "aSd",
    "enc",
    "id",
    "mac",
    "nMoC",
    "pinsm",
    "sMd",
    "sign"
})
public class CardKeysAndPINs {

    @XmlElementRef(name = "ASd", namespace = "http://schemas.datacontract.org/2004/07/EMKS_WCFService", type = JAXBElement.class)
    protected JAXBElement<String> aSd;
    @XmlElementRef(name = "ENC", namespace = "http://schemas.datacontract.org/2004/07/EMKS_WCFService", type = JAXBElement.class)
    protected JAXBElement<String> enc;
    @XmlElementRef(name = "ID", namespace = "http://schemas.datacontract.org/2004/07/EMKS_WCFService", type = JAXBElement.class)
    protected JAXBElement<String> id;
    @XmlElementRef(name = "MAC", namespace = "http://schemas.datacontract.org/2004/07/EMKS_WCFService", type = JAXBElement.class)
    protected JAXBElement<String> mac;
    @XmlElementRef(name = "NMoC", namespace = "http://schemas.datacontract.org/2004/07/EMKS_WCFService", type = JAXBElement.class)
    protected JAXBElement<String> nMoC;
    @XmlElementRef(name = "PIN_SM", namespace = "http://schemas.datacontract.org/2004/07/EMKS_WCFService", type = JAXBElement.class)
    protected JAXBElement<String> pinsm;
    @XmlElementRef(name = "SMd", namespace = "http://schemas.datacontract.org/2004/07/EMKS_WCFService", type = JAXBElement.class)
    protected JAXBElement<String> sMd;
    @XmlElementRef(name = "Sign", namespace = "http://schemas.datacontract.org/2004/07/EMKS_WCFService", type = JAXBElement.class)
    protected JAXBElement<String> sign;

    /**
     * Gets the value of the aSd property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getASd() {
        return aSd;
    }

    /**
     * Sets the value of the aSd property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setASd(JAXBElement<String> value) {
        this.aSd = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the enc property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getENC() {
        return enc;
    }

    /**
     * Sets the value of the enc property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setENC(JAXBElement<String> value) {
        this.enc = ((JAXBElement<String> ) value);
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
     * Gets the value of the mac property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMAC() {
        return mac;
    }

    /**
     * Sets the value of the mac property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMAC(JAXBElement<String> value) {
        this.mac = ((JAXBElement<String> ) value);
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
     * Gets the value of the pinsm property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPINSM() {
        return pinsm;
    }

    /**
     * Sets the value of the pinsm property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPINSM(JAXBElement<String> value) {
        this.pinsm = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the sMd property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSMd() {
        return sMd;
    }

    /**
     * Sets the value of the sMd property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSMd(JAXBElement<String> value) {
        this.sMd = ((JAXBElement<String> ) value);
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
