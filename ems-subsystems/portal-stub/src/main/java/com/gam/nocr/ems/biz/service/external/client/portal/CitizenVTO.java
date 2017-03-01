
package com.gam.nocr.ems.biz.service.external.client.portal;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for citizenVTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="citizenVTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="address" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="birthCertId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="birthCertPlace" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="birthCertPrvId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="birthCertPrvName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="birthCertSerial" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="birthDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="birthDateHijri" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cellNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="children" type="{http://portalws.ws.web.portal.nocr.gam.com/}childVTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="editCounter" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="email" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="enrolledDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="fatherBirthCertId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fatherBirthCertSerial" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fatherBirthDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fatherFatherName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fatherFirstNameEN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fatherFirstNameFA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fatherNationalId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fatherSureName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="firstNameEN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="firstNameFA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="gender" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="isPaid" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="lastModifiedDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="livingCityId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="livingCityName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="livingPrvId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="livingPrvName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="metadata" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="motherBirthCertId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="motherBirthCertSerial" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="motherBirthDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="motherFatherName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="motherFirstNameEN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="motherFirstNameFA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="motherNationalId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="motherSureName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nationalId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="religionId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="requestId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="spouses" type="{http://portalws.ws.web.portal.nocr.gam.com/}spouseVTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="state" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="sureNameEN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sureNameFA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="trackingId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "citizenVTO", propOrder = {
    "address",
    "birthCertId",
    "birthCertPlace",
    "birthCertPrvId",
    "birthCertPrvName",
    "birthCertSerial",
    "birthDate",
    "birthDateHijri",
    "cellNo",
    "children",
    "editCounter",
    "email",
    "enrolledDate",
    "fatherBirthCertId",
    "fatherBirthCertSerial",
    "fatherBirthDate",
    "fatherFatherName",
    "fatherFirstNameEN",
    "fatherFirstNameFA",
    "fatherNationalId",
    "fatherSureName",
    "firstNameEN",
    "firstNameFA",
    "gender",
    "id",
    "isPaid",
    "lastModifiedDate",
    "livingCityId",
    "livingCityName",
    "livingPrvId",
    "livingPrvName",
    "metadata",
    "motherBirthCertId",
    "motherBirthCertSerial",
    "motherBirthDate",
    "motherFatherName",
    "motherFirstNameEN",
    "motherFirstNameFA",
    "motherNationalId",
    "motherSureName",
    "nationalId",
    "religionId",
    "requestId",
    "spouses",
    "state",
    "sureNameEN",
    "sureNameFA",
    "trackingId",
    "type"
})
public class CitizenVTO {

    protected String address;
    protected String birthCertId;
    protected String birthCertPlace;
    protected Long birthCertPrvId;
    protected String birthCertPrvName;
    protected String birthCertSerial;
    protected String birthDate;
    protected String birthDateHijri;
    protected String cellNo;
    protected List<ChildVTO> children;
    protected Integer editCounter;
    protected String email;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar enrolledDate;
    protected String fatherBirthCertId;
    protected String fatherBirthCertSerial;
    protected String fatherBirthDate;
    protected String fatherFatherName;
    protected String fatherFirstNameEN;
    protected String fatherFirstNameFA;
    protected String fatherNationalId;
    protected String fatherSureName;
    protected String firstNameEN;
    protected String firstNameFA;
    protected String gender;
    protected Long id;
    protected Integer isPaid;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar lastModifiedDate;
    protected Long livingCityId;
    protected String livingCityName;
    protected Long livingPrvId;
    protected String livingPrvName;
    @XmlElement(nillable = true)
    protected List<String> metadata;
    protected String motherBirthCertId;
    protected String motherBirthCertSerial;
    protected String motherBirthDate;
    protected String motherFatherName;
    protected String motherFirstNameEN;
    protected String motherFirstNameFA;
    protected String motherNationalId;
    protected String motherSureName;
    protected String nationalId;
    protected Long religionId;
    protected Long requestId;
    protected List<SpouseVTO> spouses;
    protected Long state;
    protected String sureNameEN;
    protected String sureNameFA;
    protected String trackingId;
    protected Long type;

    /**
     * Gets the value of the address property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddress(String value) {
        this.address = value;
    }

    /**
     * Gets the value of the birthCertId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBirthCertId() {
        return birthCertId;
    }

    /**
     * Sets the value of the birthCertId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBirthCertId(String value) {
        this.birthCertId = value;
    }

    /**
     * Gets the value of the birthCertPlace property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBirthCertPlace() {
        return birthCertPlace;
    }

    /**
     * Sets the value of the birthCertPlace property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBirthCertPlace(String value) {
        this.birthCertPlace = value;
    }

    /**
     * Gets the value of the birthCertPrvId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getBirthCertPrvId() {
        return birthCertPrvId;
    }

    /**
     * Sets the value of the birthCertPrvId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setBirthCertPrvId(Long value) {
        this.birthCertPrvId = value;
    }

    /**
     * Gets the value of the birthCertPrvName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBirthCertPrvName() {
        return birthCertPrvName;
    }

    /**
     * Sets the value of the birthCertPrvName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBirthCertPrvName(String value) {
        this.birthCertPrvName = value;
    }

    /**
     * Gets the value of the birthCertSerial property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBirthCertSerial() {
        return birthCertSerial;
    }

    /**
     * Sets the value of the birthCertSerial property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBirthCertSerial(String value) {
        this.birthCertSerial = value;
    }

    /**
     * Gets the value of the birthDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBirthDate() {
        return birthDate;
    }

    /**
     * Sets the value of the birthDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBirthDate(String value) {
        this.birthDate = value;
    }

    /**
     * Gets the value of the birthDateHijri property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBirthDateHijri() {
        return birthDateHijri;
    }

    /**
     * Sets the value of the birthDateHijri property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBirthDateHijri(String value) {
        this.birthDateHijri = value;
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
     * Gets the value of the children property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the children property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getChildren().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ChildVTO }
     * 
     * 
     */
    public List<ChildVTO> getChildren() {
        if (children == null) {
            children = new ArrayList<ChildVTO>();
        }
        return this.children;
    }

    /**
     * Gets the value of the editCounter property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getEditCounter() {
        return editCounter;
    }

    /**
     * Sets the value of the editCounter property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setEditCounter(Integer value) {
        this.editCounter = value;
    }

    /**
     * Gets the value of the email property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmail(String value) {
        this.email = value;
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
     * Gets the value of the fatherBirthCertId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFatherBirthCertId() {
        return fatherBirthCertId;
    }

    /**
     * Sets the value of the fatherBirthCertId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFatherBirthCertId(String value) {
        this.fatherBirthCertId = value;
    }

    /**
     * Gets the value of the fatherBirthCertSerial property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFatherBirthCertSerial() {
        return fatherBirthCertSerial;
    }

    /**
     * Sets the value of the fatherBirthCertSerial property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFatherBirthCertSerial(String value) {
        this.fatherBirthCertSerial = value;
    }

    /**
     * Gets the value of the fatherBirthDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFatherBirthDate() {
        return fatherBirthDate;
    }

    /**
     * Sets the value of the fatherBirthDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFatherBirthDate(String value) {
        this.fatherBirthDate = value;
    }

    /**
     * Gets the value of the fatherFatherName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFatherFatherName() {
        return fatherFatherName;
    }

    /**
     * Sets the value of the fatherFatherName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFatherFatherName(String value) {
        this.fatherFatherName = value;
    }

    /**
     * Gets the value of the fatherFirstNameEN property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFatherFirstNameEN() {
        return fatherFirstNameEN;
    }

    /**
     * Sets the value of the fatherFirstNameEN property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFatherFirstNameEN(String value) {
        this.fatherFirstNameEN = value;
    }

    /**
     * Gets the value of the fatherFirstNameFA property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFatherFirstNameFA() {
        return fatherFirstNameFA;
    }

    /**
     * Sets the value of the fatherFirstNameFA property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFatherFirstNameFA(String value) {
        this.fatherFirstNameFA = value;
    }

    /**
     * Gets the value of the fatherNationalId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFatherNationalId() {
        return fatherNationalId;
    }

    /**
     * Sets the value of the fatherNationalId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFatherNationalId(String value) {
        this.fatherNationalId = value;
    }

    /**
     * Gets the value of the fatherSureName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFatherSureName() {
        return fatherSureName;
    }

    /**
     * Sets the value of the fatherSureName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFatherSureName(String value) {
        this.fatherSureName = value;
    }

    /**
     * Gets the value of the firstNameEN property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirstNameEN() {
        return firstNameEN;
    }

    /**
     * Sets the value of the firstNameEN property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirstNameEN(String value) {
        this.firstNameEN = value;
    }

    /**
     * Gets the value of the firstNameFA property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirstNameFA() {
        return firstNameFA;
    }

    /**
     * Sets the value of the firstNameFA property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirstNameFA(String value) {
        this.firstNameFA = value;
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
     * Gets the value of the isPaid property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIsPaid() {
        return isPaid;
    }

    /**
     * Sets the value of the isPaid property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIsPaid(Integer value) {
        this.isPaid = value;
    }

    /**
     * Gets the value of the lastModifiedDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * Sets the value of the lastModifiedDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setLastModifiedDate(XMLGregorianCalendar value) {
        this.lastModifiedDate = value;
    }

    /**
     * Gets the value of the livingCityId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getLivingCityId() {
        return livingCityId;
    }

    /**
     * Sets the value of the livingCityId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setLivingCityId(Long value) {
        this.livingCityId = value;
    }

    /**
     * Gets the value of the livingCityName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLivingCityName() {
        return livingCityName;
    }

    /**
     * Sets the value of the livingCityName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLivingCityName(String value) {
        this.livingCityName = value;
    }

    /**
     * Gets the value of the livingPrvId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getLivingPrvId() {
        return livingPrvId;
    }

    /**
     * Sets the value of the livingPrvId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setLivingPrvId(Long value) {
        this.livingPrvId = value;
    }

    /**
     * Gets the value of the livingPrvName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLivingPrvName() {
        return livingPrvName;
    }

    /**
     * Sets the value of the livingPrvName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLivingPrvName(String value) {
        this.livingPrvName = value;
    }

    /**
     * Gets the value of the metadata property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the metadata property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMetadata().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getMetadata() {
        if (metadata == null) {
            metadata = new ArrayList<String>();
        }
        return this.metadata;
    }

    /**
     * Gets the value of the motherBirthCertId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMotherBirthCertId() {
        return motherBirthCertId;
    }

    /**
     * Sets the value of the motherBirthCertId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMotherBirthCertId(String value) {
        this.motherBirthCertId = value;
    }

    /**
     * Gets the value of the motherBirthCertSerial property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMotherBirthCertSerial() {
        return motherBirthCertSerial;
    }

    /**
     * Sets the value of the motherBirthCertSerial property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMotherBirthCertSerial(String value) {
        this.motherBirthCertSerial = value;
    }

    /**
     * Gets the value of the motherBirthDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMotherBirthDate() {
        return motherBirthDate;
    }

    /**
     * Sets the value of the motherBirthDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMotherBirthDate(String value) {
        this.motherBirthDate = value;
    }

    /**
     * Gets the value of the motherFatherName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMotherFatherName() {
        return motherFatherName;
    }

    /**
     * Sets the value of the motherFatherName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMotherFatherName(String value) {
        this.motherFatherName = value;
    }

    /**
     * Gets the value of the motherFirstNameEN property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMotherFirstNameEN() {
        return motherFirstNameEN;
    }

    /**
     * Sets the value of the motherFirstNameEN property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMotherFirstNameEN(String value) {
        this.motherFirstNameEN = value;
    }

    /**
     * Gets the value of the motherFirstNameFA property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMotherFirstNameFA() {
        return motherFirstNameFA;
    }

    /**
     * Sets the value of the motherFirstNameFA property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMotherFirstNameFA(String value) {
        this.motherFirstNameFA = value;
    }

    /**
     * Gets the value of the motherNationalId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMotherNationalId() {
        return motherNationalId;
    }

    /**
     * Sets the value of the motherNationalId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMotherNationalId(String value) {
        this.motherNationalId = value;
    }

    /**
     * Gets the value of the motherSureName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMotherSureName() {
        return motherSureName;
    }

    /**
     * Sets the value of the motherSureName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMotherSureName(String value) {
        this.motherSureName = value;
    }

    /**
     * Gets the value of the nationalId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNationalId() {
        return nationalId;
    }

    /**
     * Sets the value of the nationalId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNationalId(String value) {
        this.nationalId = value;
    }

    /**
     * Gets the value of the religionId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getReligionId() {
        return religionId;
    }

    /**
     * Sets the value of the religionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setReligionId(Long value) {
        this.religionId = value;
    }

    /**
     * Gets the value of the requestId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getRequestId() {
        return requestId;
    }

    /**
     * Sets the value of the requestId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setRequestId(Long value) {
        this.requestId = value;
    }

    /**
     * Gets the value of the spouses property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the spouses property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSpouses().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SpouseVTO }
     * 
     * 
     */
    public List<SpouseVTO> getSpouses() {
        if (spouses == null) {
            spouses = new ArrayList<SpouseVTO>();
        }
        return this.spouses;
    }

    /**
     * Gets the value of the state property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getState() {
        return state;
    }

    /**
     * Sets the value of the state property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setState(Long value) {
        this.state = value;
    }

    /**
     * Gets the value of the sureNameEN property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSureNameEN() {
        return sureNameEN;
    }

    /**
     * Sets the value of the sureNameEN property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSureNameEN(String value) {
        this.sureNameEN = value;
    }

    /**
     * Gets the value of the sureNameFA property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSureNameFA() {
        return sureNameFA;
    }

    /**
     * Sets the value of the sureNameFA property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSureNameFA(String value) {
        this.sureNameFA = value;
    }

    /**
     * Gets the value of the trackingId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTrackingId() {
        return trackingId;
    }

    /**
     * Sets the value of the trackingId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTrackingId(String value) {
        this.trackingId = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setType(Long value) {
        this.type = value;
    }

}
