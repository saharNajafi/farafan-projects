
package com.gam.nocr.ems.biz.service.external.client.portal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for enrollmentOfficeWTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="enrollmentOfficeWTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="calenderType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="enrollmentOfficeAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="enrollmentOfficeArea" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="enrollmentOfficeCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="enrollmentOfficeFax" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="enrollmentOfficeName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="enrollmentOfficePhone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="enrollmentOfficePostalCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="enrollmentOfficeStatus" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="enrollmentOfficeType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="enrollmentOfficeWorkingHourFrom" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *         &lt;element name="enrollmentOfficeWorkingHourTo" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="khosusiType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="locationId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="ratingInfoId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "enrollmentOfficeWTO", propOrder = {
    "calenderType",
    "enrollmentOfficeAddress",
    "enrollmentOfficeArea",
    "enrollmentOfficeCode",
    "enrollmentOfficeFax",
    "enrollmentOfficeName",
    "enrollmentOfficePhone",
    "enrollmentOfficePostalCode",
    "enrollmentOfficeStatus",
    "enrollmentOfficeType",
    "enrollmentOfficeWorkingHourFrom",
    "enrollmentOfficeWorkingHourTo",
    "id",
    "khosusiType",
    "locationId",
    "ratingInfoId"
})
public class EnrollmentOfficeWTO {

    protected String calenderType;
    protected String enrollmentOfficeAddress;
    protected Integer enrollmentOfficeArea;
    protected String enrollmentOfficeCode;
    protected String enrollmentOfficeFax;
    protected String enrollmentOfficeName;
    protected String enrollmentOfficePhone;
    protected String enrollmentOfficePostalCode;
    protected Boolean enrollmentOfficeStatus;
    protected String enrollmentOfficeType;
    protected Float enrollmentOfficeWorkingHourFrom;
    protected Float enrollmentOfficeWorkingHourTo;
    protected Long id;
    protected String khosusiType;
    protected Long locationId;
    protected Long ratingInfoId;

    /**
     * Gets the value of the calenderType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCalenderType() {
        return calenderType;
    }

    /**
     * Sets the value of the calenderType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCalenderType(String value) {
        this.calenderType = value;
    }

    /**
     * Gets the value of the enrollmentOfficeAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnrollmentOfficeAddress() {
        return enrollmentOfficeAddress;
    }

    /**
     * Sets the value of the enrollmentOfficeAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnrollmentOfficeAddress(String value) {
        this.enrollmentOfficeAddress = value;
    }

    /**
     * Gets the value of the enrollmentOfficeArea property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getEnrollmentOfficeArea() {
        return enrollmentOfficeArea;
    }

    /**
     * Sets the value of the enrollmentOfficeArea property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setEnrollmentOfficeArea(Integer value) {
        this.enrollmentOfficeArea = value;
    }

    /**
     * Gets the value of the enrollmentOfficeCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnrollmentOfficeCode() {
        return enrollmentOfficeCode;
    }

    /**
     * Sets the value of the enrollmentOfficeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnrollmentOfficeCode(String value) {
        this.enrollmentOfficeCode = value;
    }

    /**
     * Gets the value of the enrollmentOfficeFax property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnrollmentOfficeFax() {
        return enrollmentOfficeFax;
    }

    /**
     * Sets the value of the enrollmentOfficeFax property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnrollmentOfficeFax(String value) {
        this.enrollmentOfficeFax = value;
    }

    /**
     * Gets the value of the enrollmentOfficeName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnrollmentOfficeName() {
        return enrollmentOfficeName;
    }

    /**
     * Sets the value of the enrollmentOfficeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnrollmentOfficeName(String value) {
        this.enrollmentOfficeName = value;
    }

    /**
     * Gets the value of the enrollmentOfficePhone property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnrollmentOfficePhone() {
        return enrollmentOfficePhone;
    }

    /**
     * Sets the value of the enrollmentOfficePhone property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnrollmentOfficePhone(String value) {
        this.enrollmentOfficePhone = value;
    }

    /**
     * Gets the value of the enrollmentOfficePostalCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnrollmentOfficePostalCode() {
        return enrollmentOfficePostalCode;
    }

    /**
     * Sets the value of the enrollmentOfficePostalCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnrollmentOfficePostalCode(String value) {
        this.enrollmentOfficePostalCode = value;
    }

    /**
     * Gets the value of the enrollmentOfficeStatus property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEnrollmentOfficeStatus() {
        return enrollmentOfficeStatus;
    }

    /**
     * Sets the value of the enrollmentOfficeStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEnrollmentOfficeStatus(Boolean value) {
        this.enrollmentOfficeStatus = value;
    }

    /**
     * Gets the value of the enrollmentOfficeType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnrollmentOfficeType() {
        return enrollmentOfficeType;
    }

    /**
     * Sets the value of the enrollmentOfficeType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnrollmentOfficeType(String value) {
        this.enrollmentOfficeType = value;
    }

    /**
     * Gets the value of the enrollmentOfficeWorkingHourFrom property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getEnrollmentOfficeWorkingHourFrom() {
        return enrollmentOfficeWorkingHourFrom;
    }

    /**
     * Sets the value of the enrollmentOfficeWorkingHourFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setEnrollmentOfficeWorkingHourFrom(Float value) {
        this.enrollmentOfficeWorkingHourFrom = value;
    }

    /**
     * Gets the value of the enrollmentOfficeWorkingHourTo property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getEnrollmentOfficeWorkingHourTo() {
        return enrollmentOfficeWorkingHourTo;
    }

    /**
     * Sets the value of the enrollmentOfficeWorkingHourTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setEnrollmentOfficeWorkingHourTo(Float value) {
        this.enrollmentOfficeWorkingHourTo = value;
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
     * Gets the value of the khosusiType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKhosusiType() {
        return khosusiType;
    }

    /**
     * Sets the value of the khosusiType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKhosusiType(String value) {
        this.khosusiType = value;
    }

    /**
     * Gets the value of the locationId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getLocationId() {
        return locationId;
    }

    /**
     * Sets the value of the locationId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setLocationId(Long value) {
        this.locationId = value;
    }

    /**
     * Gets the value of the ratingInfoId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getRatingInfoId() {
        return ratingInfoId;
    }

    /**
     * Sets the value of the ratingInfoId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setRatingInfoId(Long value) {
        this.ratingInfoId = value;
    }

}
