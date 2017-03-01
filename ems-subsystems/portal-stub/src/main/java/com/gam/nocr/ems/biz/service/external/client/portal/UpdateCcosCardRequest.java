
package com.gam.nocr.ems.biz.service.external.client.portal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for updateCcosCardRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="updateCcosCardRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cardRequestWTO" type="{http://portalws.ws.web.portal.nocr.gam.com/}cardRequestWTO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateCcosCardRequest", propOrder = {
    "cardRequestWTO"
})
public class UpdateCcosCardRequest {

    protected CardRequestWTO cardRequestWTO;

    /**
     * Gets the value of the cardRequestWTO property.
     * 
     * @return
     *     possible object is
     *     {@link CardRequestWTO }
     *     
     */
    public CardRequestWTO getCardRequestWTO() {
        return cardRequestWTO;
    }

    /**
     * Sets the value of the cardRequestWTO property.
     * 
     * @param value
     *     allowed object is
     *     {@link CardRequestWTO }
     *     
     */
    public void setCardRequestWTO(CardRequestWTO value) {
        this.cardRequestWTO = value;
    }

}
