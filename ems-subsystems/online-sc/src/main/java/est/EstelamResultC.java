
package est;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EstelamResultC complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EstelamResultC">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="estelamResult3" type="{http://est}EstelamResult3" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="imageResult" type="{http://est}ImageResult" minOccurs="0"/>
 *         &lt;element name="message" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EstelamResultC", propOrder = {
    "estelamResult3",
    "imageResult",
    "message"
})
public class EstelamResultC {

    @XmlElement(nillable = true)
    protected List<EstelamResult3> estelamResult3;
    protected ImageResult imageResult;
    @XmlElement(nillable = true)
    protected List<String> message;

    /**
     * Gets the value of the estelamResult3 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the estelamResult3 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEstelamResult3().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EstelamResult3 }
     * 
     * 
     */
    public List<EstelamResult3> getEstelamResult3() {
        if (estelamResult3 == null) {
            estelamResult3 = new ArrayList<EstelamResult3>();
        }
        return this.estelamResult3;
    }

    /**
     * Gets the value of the imageResult property.
     * 
     * @return
     *     possible object is
     *     {@link ImageResult }
     *     
     */
    public ImageResult getImageResult() {
        return imageResult;
    }

    /**
     * Sets the value of the imageResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ImageResult }
     *     
     */
    public void setImageResult(ImageResult value) {
        this.imageResult = value;
    }

    /**
     * Gets the value of the message property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the message property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMessage().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getMessage() {
        if (message == null) {
            message = new ArrayList<String>();
        }
        return this.message;
    }

}
