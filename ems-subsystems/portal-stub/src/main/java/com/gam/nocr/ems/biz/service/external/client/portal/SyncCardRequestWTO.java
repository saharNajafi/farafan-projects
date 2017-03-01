
package com.gam.nocr.ems.biz.service.external.client.portal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for syncCardRequestWTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="syncCardRequestWTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cardEnrollmentOfficeId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="cardRequestMetadata" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cardRequestState" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="originalCardRequestOfficeId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "syncCardRequestWTO", propOrder = {
    "cardEnrollmentOfficeId",
    "cardRequestMetadata",
    "cardRequestState",
    "id",
    "originalCardRequestOfficeId"
})
public class SyncCardRequestWTO {

    protected Long cardEnrollmentOfficeId;
    protected String cardRequestMetadata;
    protected String cardRequestState;
    protected Long id;
    protected Long originalCardRequestOfficeId;

    /**
     * Gets the value of the cardEnrollmentOfficeId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCardEnrollmentOfficeId() {
        return cardEnrollmentOfficeId;
    }

    /**
     * Sets the value of the cardEnrollmentOfficeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCardEnrollmentOfficeId(Long value) {
        this.cardEnrollmentOfficeId = value;
    }

    /**
     * Gets the value of the cardRequestMetadata property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCardRequestMetadata() {
        return cardRequestMetadata;
    }

    /**
     * Sets the value of the cardRequestMetadata property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCardRequestMetadata(String value) {
        this.cardRequestMetadata = value;
    }

    /**
     * Gets the value of the cardRequestState property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCardRequestState() {
        return cardRequestState;
    }

    /**
     * Sets the value of the cardRequestState property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCardRequestState(String value) {
        this.cardRequestState = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setId(Long value) {
        this.id = value;
    }

    /**
     * Gets the value of the originalCardRequestOfficeId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getOriginalCardRequestOfficeId() {
        return originalCardRequestOfficeId;
    }

    /**
     * Sets the value of the originalCardRequestOfficeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setOriginalCardRequestOfficeId(Long value) {
        this.originalCardRequestOfficeId = value;
    }

}
