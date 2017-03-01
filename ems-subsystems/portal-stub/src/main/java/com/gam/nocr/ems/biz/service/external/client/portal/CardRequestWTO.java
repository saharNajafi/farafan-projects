
package com.gam.nocr.ems.biz.service.external.client.portal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for cardRequestWTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cardRequestWTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="birthCertificateId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="birthCertificateIssuancePlace" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="birthCertificateSerial" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="birthDateLunar" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="birthDateSolar" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cardRequestState" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cardRequestType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cellNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="citizenId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="enrolledDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="enrollmentOfficeId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="firstNamePersian" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="gender" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="metadata" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="motherFirstNamePersian" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nationalID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="religion" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="surnamePersian" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="trackingID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cardRequestWTO", propOrder = {
    "birthCertificateId",
    "birthCertificateIssuancePlace",
    "birthCertificateSerial",
    "birthDateLunar",
    "birthDateSolar",
    "cardRequestState",
    "cardRequestType",
    "cellNo",
    "citizenId",
    "enrolledDate",
    "enrollmentOfficeId",
    "firstNamePersian",
    "gender",
    "id",
    "metadata",
    "motherFirstNamePersian",
    "nationalID",
    "religion",
    "surnamePersian",
    "trackingID"
})
public class CardRequestWTO {

    protected String birthCertificateId;
    protected String birthCertificateIssuancePlace;
    protected String birthCertificateSerial;
    protected String birthDateLunar;
    protected String birthDateSolar;
    protected String cardRequestState;
    protected String cardRequestType;
    protected String cellNo;
    protected Long citizenId;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar enrolledDate;
    protected Long enrollmentOfficeId;
    protected String firstNamePersian;
    protected String gender;
    protected Long id;
    protected String metadata;
    protected String motherFirstNamePersian;
    protected String nationalID;
    protected Long religion;
    protected String surnamePersian;
    protected String trackingID;

    /**
     * Gets the value of the birthCertificateId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBirthCertificateId() {
        return birthCertificateId;
    }

    /**
     * Sets the value of the birthCertificateId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBirthCertificateId(String value) {
        this.birthCertificateId = value;
    }

    /**
     * Gets the value of the birthCertificateIssuancePlace property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBirthCertificateIssuancePlace() {
        return birthCertificateIssuancePlace;
    }

    /**
     * Sets the value of the birthCertificateIssuancePlace property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBirthCertificateIssuancePlace(String value) {
        this.birthCertificateIssuancePlace = value;
    }

    /**
     * Gets the value of the birthCertificateSerial property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBirthCertificateSerial() {
        return birthCertificateSerial;
    }

    /**
     * Sets the value of the birthCertificateSerial property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBirthCertificateSerial(String value) {
        this.birthCertificateSerial = value;
    }

    /**
     * Gets the value of the birthDateLunar property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBirthDateLunar() {
        return birthDateLunar;
    }

    /**
     * Sets the value of the birthDateLunar property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBirthDateLunar(String value) {
        this.birthDateLunar = value;
    }

    /**
     * Gets the value of the birthDateSolar property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBirthDateSolar() {
        return birthDateSolar;
    }

    /**
     * Sets the value of the birthDateSolar property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBirthDateSolar(String value) {
        this.birthDateSolar = value;
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
     * Gets the value of the cardRequestType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCardRequestType() {
        return cardRequestType;
    }

    /**
     * Sets the value of the cardRequestType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCardRequestType(String value) {
        this.cardRequestType = value;
    }

    /**
     * Gets the value of the cellNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCellNo() {
        return cellNo;
    }

    /**
     * Sets the value of the cellNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCellNo(String value) {
        this.cellNo = value;
    }

    /**
     * Gets the value of the citizenId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCitizenId() {
        return citizenId;
    }

    /**
     * Sets the value of the citizenId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCitizenId(Long value) {
        this.citizenId = value;
    }

    /**
     * Gets the value of the enrolledDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getEnrolledDate() {
        return enrolledDate;
    }

    /**
     * Sets the value of the enrolledDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEnrolledDate(XMLGregorianCalendar value) {
        this.enrolledDate = value;
    }

    /**
     * Gets the value of the enrollmentOfficeId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getEnrollmentOfficeId() {
        return enrollmentOfficeId;
    }

    /**
     * Sets the value of the enrollmentOfficeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setEnrollmentOfficeId(Long value) {
        this.enrollmentOfficeId = value;
    }

    /**
     * Gets the value of the firstNamePersian property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirstNamePersian() {
        return firstNamePersian;
    }

    /**
     * Sets the value of the firstNamePersian property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirstNamePersian(String value) {
        this.firstNamePersian = value;
    }

    /**
     * Gets the value of the gender property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the value of the gender property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGender(String value) {
        this.gender = value;
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
     * Gets the value of the metadata property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMetadata() {
        return metadata;
    }

    /**
     * Sets the value of the metadata property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMetadata(String value) {
        this.metadata = value;
    }

    /**
     * Gets the value of the motherFirstNamePersian property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMotherFirstNamePersian() {
        return motherFirstNamePersian;
    }

    /**
     * Sets the value of the motherFirstNamePersian property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMotherFirstNamePersian(String value) {
        this.motherFirstNamePersian = value;
    }

    /**
     * Gets the value of the nationalID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNationalID() {
        return nationalID;
    }

    /**
     * Sets the value of the nationalID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNationalID(String value) {
        this.nationalID = value;
    }

    /**
     * Gets the value of the religion property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getReligion() {
        return religion;
    }

    /**
     * Sets the value of the religion property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setReligion(Long value) {
        this.religion = value;
    }

    /**
     * Gets the value of the surnamePersian property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSurnamePersian() {
        return surnamePersian;
    }

    /**
     * Sets the value of the surnamePersian property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSurnamePersian(String value) {
        this.surnamePersian = value;
    }

    /**
     * Gets the value of the trackingID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTrackingID() {
        return trackingID;
    }

    /**
     * Sets the value of the trackingID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTrackingID(String value) {
        this.trackingID = value;
    }

}
