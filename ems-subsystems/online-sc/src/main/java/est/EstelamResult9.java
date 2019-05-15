
package est;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for estelamResult9 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="estelamResult9">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="birthDate" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="birthplace" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="deathStatus" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="exceptionMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="family" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="fatherName" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="motherName" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="nin" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="shenasnameIssuePlace" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="shenasnameNo" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "estelamResult9", propOrder = {
    "birthDate",
    "birthplace",
    "deathStatus",
    "exceptionMessage",
    "family",
    "fatherName",
    "motherName",
    "name",
    "nin",
    "shenasnameIssuePlace",
    "shenasnameNo"
})
public class EstelamResult9 {

    protected int birthDate;
    protected byte[] birthplace;
    protected int deathStatus;
    protected String exceptionMessage;
    protected byte[] family;
    protected byte[] fatherName;
    protected byte[] motherName;
    protected byte[] name;
    protected long nin;
    protected byte[] shenasnameIssuePlace;
    protected int shenasnameNo;

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
     * Gets the value of the motherName property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getMotherName() {
        return motherName;
    }

    /**
     * Sets the value of the motherName property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setMotherName(byte[] value) {
        this.motherName = value;
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

}
