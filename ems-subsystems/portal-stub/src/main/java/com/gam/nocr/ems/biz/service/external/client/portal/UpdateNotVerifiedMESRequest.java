
package com.gam.nocr.ems.biz.service.external.client.portal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for updateNotVerifiedMESRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="updateNotVerifiedMESRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="citizenWTO" type="{http://portalws.ws.web.portal.nocr.gam.com/}citizenWTO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateNotVerifiedMESRequest", propOrder = {
    "citizenWTO"
})
public class UpdateNotVerifiedMESRequest {

    protected CitizenWTO citizenWTO;

    /**
     * Gets the value of the citizenWTO property.
     * 
     * @return
     *     possible object is
     *     {@link CitizenWTO }
     *     
     */
    public CitizenWTO getCitizenWTO() {
        return citizenWTO;
    }

    /**
     * Sets the value of the citizenWTO property.
     * 
     * @param value
     *     allowed object is
     *     {@link CitizenWTO }
     *     
     */
    public void setCitizenWTO(CitizenWTO value) {
        this.citizenWTO = value;
    }

}
