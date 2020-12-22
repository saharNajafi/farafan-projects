
package est;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CardRevokedInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CardRevokedInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cardrevokedDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="officeCode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="personNin" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="revokedReason" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CardRevokedInfo", propOrder = {
    "cardrevokedDate",
    "officeCode",
    "personNin",
    "revokedReason"
})
public class CardRevokedInfo {

    protected String cardrevokedDate;
    protected int officeCode;
    protected long personNin;
    protected int revokedReason;

    /**
     * Gets the value of the cardrevokedDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCardrevokedDate() {
        return cardrevokedDate;
    }

    /**
     * Sets the value of the cardrevokedDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCardrevokedDate(String value) {
        this.cardrevokedDate = value;
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
     * Gets the value of the personNin property.
     * 
     */
    public long getPersonNin() {
        return personNin;
    }

    /**
     * Sets the value of the personNin property.
     * 
     */
    public void setPersonNin(long value) {
        this.personNin = value;
    }

    /**
     * Gets the value of the revokedReason property.
     * 
     */
    public int getRevokedReason() {
        return revokedReason;
    }

    /**
     * Sets the value of the revokedReason property.
     * 
     */
    public void setRevokedReason(int value) {
        this.revokedReason = value;
    }

}
