
package com.gam.nocr.ems.biz.service.external.client.gaas;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for access complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="access">
 *   &lt;complexContent>
 *     &lt;extension base="{http://gaas.gam.com/webservice/entity}entity">
 *       &lt;sequence>
 *         &lt;element name="accessName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="accessComment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="accessEnabled" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "access", namespace = "http://gaas.gam.com/webservice/entity", propOrder = {
    "accessName",
    "accessComment",
    "accessEnabled"
})
public class Access
    extends Entity
{

    protected String accessName;
    protected String accessComment;
    protected String accessEnabled;

    /**
     * Gets the value of the accessName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccessName() {
        return accessName;
    }

    /**
     * Sets the value of the accessName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccessName(String value) {
        this.accessName = value;
    }

    /**
     * Gets the value of the accessComment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccessComment() {
        return accessComment;
    }

    /**
     * Sets the value of the accessComment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccessComment(String value) {
        this.accessComment = value;
    }

    /**
     * Gets the value of the accessEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccessEnabled() {
        return accessEnabled;
    }

    /**
     * Sets the value of the accessEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccessEnabled(String value) {
        this.accessEnabled = value;
    }

}
