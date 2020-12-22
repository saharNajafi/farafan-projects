
package est;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CardDeliverInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CardDeliverInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cardState" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *         &lt;element name="cardbatchId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="carddeliveredDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cardlostDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cardreceivedDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cardshipmentDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="estelamId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="officeCode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="personNin" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="smartCardIssuanceDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="smartCardSeriSerial" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CardDeliverInfo", propOrder = {
    "cardState",
    "cardbatchId",
    "carddeliveredDate",
    "cardlostDate",
    "cardreceivedDate",
    "cardshipmentDate",
    "estelamId",
    "officeCode",
    "personNin",
    "smartCardIssuanceDate",
    "smartCardSeriSerial"
})
public class CardDeliverInfo {

    protected byte cardState;
    protected long cardbatchId;
    protected String carddeliveredDate;
    protected String cardlostDate;
    protected String cardreceivedDate;
    protected String cardshipmentDate;
    protected long estelamId;
    protected int officeCode;
    protected long personNin;
    protected String smartCardIssuanceDate;
    protected String smartCardSeriSerial;

    /**
     * Gets the value of the cardState property.
     * 
     */
    public byte getCardState() {
        return cardState;
    }

    /**
     * Sets the value of the cardState property.
     * 
     */
    public void setCardState(byte value) {
        this.cardState = value;
    }

    /**
     * Gets the value of the cardbatchId property.
     * 
     */
    public long getCardbatchId() {
        return cardbatchId;
    }

    /**
     * Sets the value of the cardbatchId property.
     * 
     */
    public void setCardbatchId(long value) {
        this.cardbatchId = value;
    }

    /**
     * Gets the value of the carddeliveredDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCarddeliveredDate() {
        return carddeliveredDate;
    }

    /**
     * Sets the value of the carddeliveredDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCarddeliveredDate(String value) {
        this.carddeliveredDate = value;
    }

    /**
     * Gets the value of the cardlostDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCardlostDate() {
        return cardlostDate;
    }

    /**
     * Sets the value of the cardlostDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCardlostDate(String value) {
        this.cardlostDate = value;
    }

    /**
     * Gets the value of the cardreceivedDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCardreceivedDate() {
        return cardreceivedDate;
    }

    /**
     * Sets the value of the cardreceivedDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCardreceivedDate(String value) {
        this.cardreceivedDate = value;
    }

    /**
     * Gets the value of the cardshipmentDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCardshipmentDate() {
        return cardshipmentDate;
    }

    /**
     * Sets the value of the cardshipmentDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCardshipmentDate(String value) {
        this.cardshipmentDate = value;
    }

    /**
     * Gets the value of the estelamId property.
     * 
     */
    public long getEstelamId() {
        return estelamId;
    }

    /**
     * Sets the value of the estelamId property.
     * 
     */
    public void setEstelamId(long value) {
        this.estelamId = value;
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
     * Gets the value of the smartCardIssuanceDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSmartCardIssuanceDate() {
        return smartCardIssuanceDate;
    }

    /**
     * Sets the value of the smartCardIssuanceDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSmartCardIssuanceDate(String value) {
        this.smartCardIssuanceDate = value;
    }

    /**
     * Gets the value of the smartCardSeriSerial property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSmartCardSeriSerial() {
        return smartCardSeriSerial;
    }

    /**
     * Sets the value of the smartCardSeriSerial property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSmartCardSeriSerial(String value) {
        this.smartCardSeriSerial = value;
    }

}
