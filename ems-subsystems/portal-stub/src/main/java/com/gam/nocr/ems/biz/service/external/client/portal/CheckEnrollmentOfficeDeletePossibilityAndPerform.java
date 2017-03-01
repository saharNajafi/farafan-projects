
package com.gam.nocr.ems.biz.service.external.client.portal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for checkEnrollmentOfficeDeletePossibilityAndPerform complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="checkEnrollmentOfficeDeletePossibilityAndPerform">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="EnrollmentOfficeId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "checkEnrollmentOfficeDeletePossibilityAndPerform", propOrder = {
    "enrollmentOfficeId"
})
public class CheckEnrollmentOfficeDeletePossibilityAndPerform {

    @XmlElement(name = "EnrollmentOfficeId")
    protected long enrollmentOfficeId;

    /**
     * Gets the value of the enrollmentOfficeId property.
     * 
     */
    public long getEnrollmentOfficeId() {
        return enrollmentOfficeId;
    }

    /**
     * Sets the value of the enrollmentOfficeId property.
     * 
     */
    public void setEnrollmentOfficeId(long value) {
        this.enrollmentOfficeId = value;
    }

}
