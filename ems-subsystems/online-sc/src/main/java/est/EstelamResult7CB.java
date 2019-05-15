
package est;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EstelamResult7CB complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EstelamResult7CB">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nin" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="family" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="fatherName" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="shenasnameSeri" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="shenasnameNo" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="shenasnameSerial" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="birthDate" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="gender" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="deathStatus" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="officeCode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="officeName" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="exceptionMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="deathDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="zipcode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="zipcode_Desc" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="address_Desc" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="specialFeild" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="birthplace" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="shenasnameIssueDate" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="shenasnameIssuePlace" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="cardExpireDate" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="cardSeri" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cardSrno" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EstelamResult7CB", propOrder = {
    "nin",
    "name",
    "family",
    "fatherName",
    "shenasnameSeri",
    "shenasnameNo",
    "shenasnameSerial",
    "birthDate",
    "gender",
    "deathStatus",
    "officeCode",
    "officeName",
    "exceptionMessage",
    "deathDate",
    "zipcode",
    "zipcodeDesc",
    "addressDesc",
    "specialFeild",
    "birthplace",
    "shenasnameIssueDate",
    "shenasnameIssuePlace",
    "cardExpireDate",
    "cardSeri",
    "cardSrno"
})
public class EstelamResult7CB {

    protected long nin;
    protected byte[] name;
    protected byte[] family;
    protected byte[] fatherName;
    protected byte[] shenasnameSeri;
    protected int shenasnameNo;
    protected int shenasnameSerial;
    protected int birthDate;
    protected int gender;
    protected int deathStatus;
    protected int officeCode;
    protected byte[] officeName;
    protected String exceptionMessage;
    protected String deathDate;
    protected String zipcode;
    @XmlElement(name = "zipcode_Desc")
    protected byte[] zipcodeDesc;
    @XmlElement(name = "address_Desc")
    protected byte[] addressDesc;
    protected byte[] specialFeild;
    protected byte[] birthplace;
    protected int shenasnameIssueDate;
    protected byte[] shenasnameIssuePlace;
    protected int cardExpireDate;
    protected String cardSeri;
    protected String cardSrno;

    /**
     * Gets the value of the nin property.
     * 
     */
    public long getNin() {
        return nin;
    }

    /**
     * Sets the value of the nin property.
     * 
     */
    public void setNin(long value) {
        this.nin = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setName(byte[] value) {
        this.name = value;
    }

    /**
     * Gets the value of the family property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getFamily() {
        return family;
    }

    /**
     * Sets the value of the family property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setFamily(byte[] value) {
        this.family = value;
    }

    /**
     * Gets the value of the fatherName property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getFatherName() {
        return fatherName;
    }

    /**
     * Sets the value of the fatherName property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setFatherName(byte[] value) {
        this.fatherName = value;
    }

    /**
     * Gets the value of the shenasnameSeri property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getShenasnameSeri() {
        return shenasnameSeri;
    }

    /**
     * Sets the value of the shenasnameSeri property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setShenasnameSeri(byte[] value) {
        this.shenasnameSeri = value;
    }

    /**
     * Gets the value of the shenasnameNo property.
     * 
     */
    public int getShenasnameNo() {
        return shenasnameNo;
    }

    /**
     * Sets the value of the shenasnameNo property.
     * 
     */
    public void setShenasnameNo(int value) {
        this.shenasnameNo = value;
    }

    /**
     * Gets the value of the shenasnameSerial property.
     * 
     */
    public int getShenasnameSerial() {
        return shenasnameSerial;
    }

    /**
     * Sets the value of the shenasnameSerial property.
     * 
     */
    public void setShenasnameSerial(int value) {
        this.shenasnameSerial = value;
    }

    /**
     * Gets the value of the birthDate property.
     * 
     */
    public int getBirthDate() {
        return birthDate;
    }

    /**
     * Sets the value of the birthDate property.
     * 
     */
    public void setBirthDate(int value) {
        this.birthDate = value;
    }

    /**
     * Gets the value of the gender property.
     * 
     */
    public int getGender() {
        return gender;
    }

    /**
     * Sets the value of the gender property.
     * 
     */
    public void setGender(int value) {
        this.gender = value;
    }

    /**
     * Gets the value of the deathStatus property.
     * 
     */
    public int getDeathStatus() {
        return deathStatus;
    }

    /**
     * Sets the value of the deathStatus property.
     * 
     */
    public void setDeathStatus(int value) {
        this.deathStatus = value;
    }

    /**
     * Gets the value of the officeCode property.
     * 
     */
    public int getOfficeCode() {
        return officeCode;
    }

    /**
     * Sets the value of the officeCode property.
     * 
     */
    public void setOfficeCode(int value) {
        this.officeCode = value;
    }

    /**
     * Gets the value of the officeName property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getOfficeName() {
        return officeName;
    }

    /**
     * Sets the value of the officeName property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setOfficeName(byte[] value) {
        this.officeName = value;
    }

    /**
     * Gets the value of the exceptionMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExceptionMessage() {
        return exceptionMessage;
    }

    /**
     * Sets the value of the exceptionMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExceptionMessage(String value) {
        this.exceptionMessage = value;
    }

    /**
     * Gets the value of the deathDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeathDate() {
        return deathDate;
    }

    /**
     * Sets the value of the deathDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeathDate(String value) {
        this.deathDate = value;
    }

    /**
     * Gets the value of the zipcode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZipcode() {
        return zipcode;
    }

    /**
     * Sets the value of the zipcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZipcode(String value) {
        this.zipcode = value;
    }

    /**
     * Gets the value of the zipcodeDesc property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getZipcodeDesc() {
        return zipcodeDesc;
    }

    /**
     * Sets the value of the zipcodeDesc property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setZipcodeDesc(byte[] value) {
        this.zipcodeDesc = value;
    }

    /**
     * Gets the value of the addressDesc property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getAddressDesc() {
        return addressDesc;
    }

    /**
     * Sets the value of the addressDesc property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setAddressDesc(byte[] value) {
        this.addressDesc = value;
    }

    /**
     * Gets the value of the specialFeild property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getSpecialFeild() {
        return specialFeild;
    }

    /**
     * Sets the value of the specialFeild property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setSpecialFeild(byte[] value) {
        this.specialFeild = value;
    }

    /**
     * Gets the value of the birthplace property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getBirthplace() {
        return birthplace;
    }

    /**
     * Sets the value of the birthplace property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setBirthplace(byte[] value) {
        this.birthplace = value;
    }

    /**
     * Gets the value of the shenasnameIssueDate property.
     * 
     */
    public int getShenasnameIssueDate() {
        return shenasnameIssueDate;
    }

    /**
     * Sets the value of the shenasnameIssueDate property.
     * 
     */
    public void setShenasnameIssueDate(int value) {
        this.shenasnameIssueDate = value;
    }

    /**
     * Gets the value of the shenasnameIssuePlace property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getShenasnameIssuePlace() {
        return shenasnameIssuePlace;
    }

    /**
     * Sets the value of the shenasnameIssuePlace property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setShenasnameIssuePlace(byte[] value) {
        this.shenasnameIssuePlace = value;
    }

    /**
     * Gets the value of the cardExpireDate property.
     * 
     */
    public int getCardExpireDate() {
        return cardExpireDate;
    }

    /**
     * Sets the value of the cardExpireDate property.
     * 
     */
    public void setCardExpireDate(int value) {
        this.cardExpireDate = value;
    }

    /**
     * Gets the value of the cardSeri property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCardSeri() {
        return cardSeri;
    }

    /**
     * Sets the value of the cardSeri property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCardSeri(String value) {
        this.cardSeri = value;
    }

    /**
     * Gets the value of the cardSrno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCardSrno() {
        return cardSrno;
    }

    /**
     * Sets the value of the cardSrno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCardSrno(String value) {
        this.cardSrno = value;
    }

}
