
package est;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TransferInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TransferInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="data" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="errCodes" type="{http://nims.manipardaz.com/}errorInfo" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="errMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="extraDesc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="jaalDesc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransferInfo", propOrder = {
    "data",
    "errCodes",
    "errMessage",
    "extraDesc",
    "jaalDesc"
})
public class TransferInfo {

    protected byte[] data;
    @XmlElement(nillable = true)
    protected List<ErrorInfo> errCodes;
    protected String errMessage;
    protected String extraDesc;
    protected String jaalDesc;

    /**
     * Gets the value of the data property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Sets the value of the data property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setData(byte[] value) {
        this.data = value;
    }

    /**
     * Gets the value of the errCodes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the errCodes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getErrCodes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ErrorInfo }
     * 
     * 
     */
    public List<ErrorInfo> getErrCodes() {
        if (errCodes == null) {
            errCodes = new ArrayList<ErrorInfo>();
        }
        return this.errCodes;
    }

    /**
     * Gets the value of the errMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrMessage() {
        return errMessage;
    }

    /**
     * Sets the value of the errMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrMessage(String value) {
        this.errMessage = value;
    }

    /**
     * Gets the value of the extraDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtraDesc() {
        return extraDesc;
    }

    /**
     * Sets the value of the extraDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtraDesc(String value) {
        this.extraDesc = value;
    }

    /**
     * Gets the value of the jaalDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJaalDesc() {
        return jaalDesc;
    }

    /**
     * Sets the value of the jaalDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJaalDesc(String value) {
        this.jaalDesc = value;
    }

}
