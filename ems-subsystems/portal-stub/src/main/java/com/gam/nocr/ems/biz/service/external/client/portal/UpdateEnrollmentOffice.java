
package com.gam.nocr.ems.biz.service.external.client.portal;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for updateEnrollmentOffice complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="updateEnrollmentOffice">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="enrollmentOfficeWTOList" type="{http://portalws.ws.web.portal.nocr.gam.com/}enrollmentOfficeWTO" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateEnrollmentOffice", propOrder = {
    "enrollmentOfficeWTOList"
})
public class UpdateEnrollmentOffice {

    protected List<EnrollmentOfficeWTO> enrollmentOfficeWTOList;

    /**
     * Gets the value of the enrollmentOfficeWTOList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the enrollmentOfficeWTOList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEnrollmentOfficeWTOList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EnrollmentOfficeWTO }
     * 
     * 
     */
    public List<EnrollmentOfficeWTO> getEnrollmentOfficeWTOList() {
        if (enrollmentOfficeWTOList == null) {
            enrollmentOfficeWTOList = new ArrayList<EnrollmentOfficeWTO>();
        }
        return this.enrollmentOfficeWTOList;
    }

}
