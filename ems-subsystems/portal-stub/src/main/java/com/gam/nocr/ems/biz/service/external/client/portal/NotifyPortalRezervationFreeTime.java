
package com.gam.nocr.ems.biz.service.external.client.portal;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for notifyPortalRezervationFreeTime complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="notifyPortalRezervationFreeTime">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="EofIds" type="{http://www.w3.org/2001/XMLSchema}long" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="DateForDelete" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "notifyPortalRezervationFreeTime", propOrder = {
    "eofIds",
    "dateForDelete"
})
public class NotifyPortalRezervationFreeTime {

    @XmlElement(name = "EofIds", type = Long.class)
    protected List<Long> eofIds;
    @XmlElement(name = "DateForDelete")
    protected long dateForDelete;

    /**
     * Gets the value of the eofIds property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the eofIds property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEofIds().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Long }
     * 
     * 
     */
    public List<Long> getEofIds() {
        if (eofIds == null) {
            eofIds = new ArrayList<Long>();
        }
        return this.eofIds;
    }

    /**
     * Gets the value of the dateForDelete property.
     * 
     */
    public long getDateForDelete() {
        return dateForDelete;
    }

    /**
     * Sets the value of the dateForDelete property.
     * 
     */
    public void setDateForDelete(long value) {
        this.dateForDelete = value;
    }

}
