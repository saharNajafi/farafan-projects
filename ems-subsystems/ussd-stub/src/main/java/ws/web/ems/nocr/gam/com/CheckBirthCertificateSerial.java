
package ws.web.ems.nocr.gam.com;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for checkBirthCertificateSerial complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="checkBirthCertificateSerial">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NationalId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="BirthCertificateSeries" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CitizenBirthDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "checkBirthCertificateSerial", propOrder = {
    "nationalId",
    "birthCertificateSeries",
    "citizenBirthDate"
})
public class CheckBirthCertificateSerial {

    @XmlElement(name = "NationalId", required = true)
    protected String nationalId;
    @XmlElement(name = "BirthCertificateSeries", required = true)
    protected String birthCertificateSeries;
    @XmlElement(name = "CitizenBirthDate", required = true)
    protected String citizenBirthDate;

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
     * Gets the value of the birthCertificateSeries property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBirthCertificateSeries() {
        return birthCertificateSeries;
    }

    /**
     * Sets the value of the birthCertificateSeries property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBirthCertificateSeries(String value) {
        this.birthCertificateSeries = value;
    }

    /**
     * Gets the value of the citizenBirthDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCitizenBirthDate() {
        return citizenBirthDate;
    }

    /**
     * Sets the value of the citizenBirthDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCitizenBirthDate(String value) {
        this.citizenBirthDate = value;
    }

}
