
package com.gam.nocr.ems.biz.service.external.client.portal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for syncResevationFreeTimeByNewRating complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="syncResevationFreeTimeByNewRating">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="EnrollmentOfficeId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="RatingInfo" type="{http://portalws.ws.web.portal.nocr.gam.com/}ratingInfoWTO" minOccurs="0"/>
 *         &lt;element name="NewCalender" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "syncResevationFreeTimeByNewRating", propOrder = {
    "enrollmentOfficeId",
    "ratingInfo",
    "newCalender"
})
public class SyncResevationFreeTimeByNewRating {

    @XmlElement(name = "EnrollmentOfficeId")
    protected long enrollmentOfficeId;
    @XmlElement(name = "RatingInfo")
    protected RatingInfoWTO ratingInfo;
    @XmlElement(name = "NewCalender")
    protected String newCalender;

    /**
     * Gets the value of the enrollmentOfficeId property.
     * 
     */
    public long getEnrollmentOfficeId() {
        return enrollmentOfficeId;
    }

    /**
     * Sets the value of the enrollmentOfficeId property.
     * 
     */
    public void setEnrollmentOfficeId(long value) {
        this.enrollmentOfficeId = value;
    }

    /**
     * Gets the value of the ratingInfo property.
     * 
     * @return
     *     possible object is
     *     {@link RatingInfoWTO }
     *     
     */
    public RatingInfoWTO getRatingInfo() {
        return ratingInfo;
    }

    /**
     * Sets the value of the ratingInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link RatingInfoWTO }
     *     
     */
    public void setRatingInfo(RatingInfoWTO value) {
        this.ratingInfo = value;
    }

    /**
     * Gets the value of the newCalender property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNewCalender() {
        return newCalender;
    }

    /**
     * Sets the value of the newCalender property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNewCalender(String value) {
        this.newCalender = value;
    }

}
