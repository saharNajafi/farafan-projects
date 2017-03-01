
package com.gam.nocr.ems.biz.service.external.client.gaas;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for role complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="role">
 *   &lt;complexContent>
 *     &lt;extension base="{http://gaas.gam.com/webservice/entity}entity">
 *       &lt;sequence>
 *         &lt;element name="roleName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="roleComment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="roleEnabled" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "role", namespace = "http://gaas.gam.com/webservice/entity", propOrder = {
    "roleName",
    "roleComment",
    "roleEnabled"
})
public class Role
    extends Entity
{

    protected String roleName;
    protected String roleComment;
    protected String roleEnabled;

    /**
     * Gets the value of the roleName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * Sets the value of the roleName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRoleName(String value) {
        this.roleName = value;
    }

    /**
     * Gets the value of the roleComment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRoleComment() {
        return roleComment;
    }

    /**
     * Sets the value of the roleComment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRoleComment(String value) {
        this.roleComment = value;
    }

    /**
     * Gets the value of the roleEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRoleEnabled() {
        return roleEnabled;
    }

    /**
     * Sets the value of the roleEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRoleEnabled(String value) {
        this.roleEnabled = value;
    }

}
