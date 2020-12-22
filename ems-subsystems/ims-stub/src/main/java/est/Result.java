
package est;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Result complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Result">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dataXML" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="meesage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Result", propOrder = {
    "dataXML",
    "meesage"
})
public class Result {

    protected String dataXML;
    protected String meesage;

    /**
     * Gets the value of the dataXML property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataXML() {
        return dataXML;
    }

    /**
     * Sets the value of the dataXML property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataXML(String value) {
        this.dataXML = value;
    }

    /**
     * Gets the value of the meesage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMeesage() {
        return meesage;
    }

    /**
     * Sets the value of the meesage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMeesage(String value) {
        this.meesage = value;
    }

}
