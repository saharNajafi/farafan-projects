
package com.gam.nocr.ems.biz.service.external.client.portal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for deleteReservationDateFromOfficeRSVFreeTime complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="deleteReservationDateFromOfficeRSVFreeTime">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
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
@XmlType(name = "deleteReservationDateFromOfficeRSVFreeTime", propOrder = {
    "dateForDelete"
})
public class DeleteReservationDateFromOfficeRSVFreeTime {

    @XmlElement(name = "DateForDelete")
    protected long dateForDelete;

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
