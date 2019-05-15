
package est;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for estelamResult9C complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="estelamResult9C">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="estelamResult9" type="{http://est}estelamResult9" minOccurs="0"/>
 *         &lt;element name="imageResult" type="{http://est}ImageResult_3F" minOccurs="0"/>
 *         &lt;element name="message" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="trackingInfo" type="{http://est}trackingInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "estelamResult9C", propOrder = {
    "estelamResult9",
    "imageResult",
    "message",
    "trackingInfo"
})
public class EstelamResult9C {

    protected EstelamResult9 estelamResult9;
    protected ImageResult3F imageResult;
    @XmlElement(nillable = true)
    protected List<String> message;
    protected TrackingInfo trackingInfo;

    /**
     * Gets the value of the estelamResult9 property.
     * 
     * @return
     *     possible object is
     *     {@link EstelamResult9 }
     *     
     */
    public EstelamResult9 getEstelamResult9() {
        return estelamResult9;
    }

    /**
     * Sets the value of the estelamResult9 property.
     * 
     * @param value
     *     allowed object is
     *     {@link EstelamResult9 }
     *     
     */
    public void setEstelamResult9(EstelamResult9 value) {
        this.estelamResult9 = value;
    }

    /**
     * Gets the value of the imageResult property.
     * 
     * @return
     *     possible object is
     *     {@link ImageResult3F }
     *     
     */
    public ImageResult3F getImageResult() {
        return imageResult;
    }

    /**
     * Sets the value of the imageResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ImageResult3F }
     *     
     */
    public void setImageResult(ImageResult3F value) {
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

    /**
     * Gets the value of the trackingInfo property.
     * 
     * @return
     *     possible object is
     *     {@link TrackingInfo }
     *     
     */
    public TrackingInfo getTrackingInfo() {
        return trackingInfo;
    }

    /**
     * Sets the value of the trackingInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link TrackingInfo }
     *     
     */
    public void setTrackingInfo(TrackingInfo value) {
        this.trackingInfo = value;
    }

}
