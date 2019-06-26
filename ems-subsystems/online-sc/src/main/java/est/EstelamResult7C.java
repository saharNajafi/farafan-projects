
package est;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for estelamResult7C complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="estelamResult7C">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="estelamResult7CB" type="{http://est}EstelamResult7CB" minOccurs="0"/>
 *         &lt;element name="imageResult" type="{http://est}ImageResult_3F" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "estelamResult7C", propOrder = {
    "estelamResult7CB",
    "imageResult",
    "message"
})
public class EstelamResult7C {

    protected EstelamResult7CB estelamResult7CB;
    @XmlElement(nillable = true)
    protected List<ImageResult3F> imageResult;
    @XmlElement(nillable = true)
    protected List<String> message;

    /**
     * Gets the value of the estelamResult7CB property.
     * 
     * @return
     *     possible object is
     *     {@link EstelamResult7CB }
     *     
     */
    public EstelamResult7CB getEstelamResult7CB() {
        return estelamResult7CB;
    }

    /**
     * Sets the value of the estelamResult7CB property.
     * 
     * @param value
     *     allowed object is
     *     {@link EstelamResult7CB }
     *     
     */
    public void setEstelamResult7CB(EstelamResult7CB value) {
        this.estelamResult7CB = value;
    }

    /**
     * Gets the value of the imageResult property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the imageResult property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getImageResult().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ImageResult3F }
     * 
     * 
     */
    public List<ImageResult3F> getImageResult() {
        if (imageResult == null) {
            imageResult = new ArrayList<ImageResult3F>();
        }
        return this.imageResult;
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
