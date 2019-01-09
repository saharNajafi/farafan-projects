
package est;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PersonInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PersonInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="birthDate" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="bookNo" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="bookRow" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="dateHasPostfix" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="family" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="familyHasPerfix" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="familyHasPostfix" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="fatherName" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="gender" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="nameHasPerfix" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="nameHasPostfix" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="nin" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="officeCode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="officeName" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="shenasnameNo" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="shenasnameSeri" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="shenasnameSerial" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PersonInfo", propOrder = {
    "birthDate",
    "bookNo",
    "bookRow",
    "dateHasPostfix",
    "family",
    "familyHasPerfix",
    "familyHasPostfix",
    "fatherName",
    "gender",
    "name",
    "nameHasPerfix",
    "nameHasPostfix",
    "nin",
    "officeCode",
    "officeName",
    "shenasnameNo",
    "shenasnameSeri",
    "shenasnameSerial"
})
public class PersonInfo {

    protected int birthDate;
    protected int bookNo;
    protected int bookRow;
    protected int dateHasPostfix;
    protected byte[] family;
    protected int familyHasPerfix;
    protected int familyHasPostfix;
    protected byte[] fatherName;
    protected int gender;
    protected byte[] name;
    protected int nameHasPerfix;
    protected int nameHasPostfix;
    protected long nin;
    protected int officeCode;
    protected byte[] officeName;
    protected int shenasnameNo;
    protected byte[] shenasnameSeri;
    protected int shenasnameSerial;

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
     * Gets the value of the bookNo property.
     * 
     */
    public int getBookNo() {
        return bookNo;
    }

    /**
     * Sets the value of the bookNo property.
     * 
     */
    public void setBookNo(int value) {
        this.bookNo = value;
    }

    /**
     * Gets the value of the bookRow property.
     * 
     */
    public int getBookRow() {
        return bookRow;
    }

    /**
     * Sets the value of the bookRow property.
     * 
     */
    public void setBookRow(int value) {
        this.bookRow = value;
    }

    /**
     * Gets the value of the dateHasPostfix property.
     * 
     */
    public int getDateHasPostfix() {
        return dateHasPostfix;
    }

    /**
     * Sets the value of the dateHasPostfix property.
     * 
     */
    public void setDateHasPostfix(int value) {
        this.dateHasPostfix = value;
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
     * Gets the value of the familyHasPerfix property.
     * 
     */
    public int getFamilyHasPerfix() {
        return familyHasPerfix;
    }

    /**
     * Sets the value of the familyHasPerfix property.
     * 
     */
    public void setFamilyHasPerfix(int value) {
        this.familyHasPerfix = value;
    }

    /**
     * Gets the value of the familyHasPostfix property.
     * 
     */
    public int getFamilyHasPostfix() {
        return familyHasPostfix;
    }

    /**
     * Sets the value of the familyHasPostfix property.
     * 
     */
    public void setFamilyHasPostfix(int value) {
        this.familyHasPostfix = value;
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
     * Gets the value of the nameHasPerfix property.
     * 
     */
    public int getNameHasPerfix() {
        return nameHasPerfix;
    }

    /**
     * Sets the value of the nameHasPerfix property.
     * 
     */
    public void setNameHasPerfix(int value) {
        this.nameHasPerfix = value;
    }

    /**
     * Gets the value of the nameHasPostfix property.
     * 
     */
    public int getNameHasPostfix() {
        return nameHasPostfix;
    }

    /**
     * Sets the value of the nameHasPostfix property.
     * 
     */
    public void setNameHasPostfix(int value) {
        this.nameHasPostfix = value;
    }

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

}
