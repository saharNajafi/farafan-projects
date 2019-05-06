
package com.gam.nocr.ems.biz.service.external.client.bpi;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for sadadResponseAsJson complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="sadadResponseAsJson">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="amount" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="retrivalRefNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="systemTraceNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="terminalId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sadadResponseAsJson", propOrder = {
    "amount",
    "retrivalRefNo",
    "systemTraceNo",
    "terminalId"
})
public class SadadResponseAsJson {

    protected Long amount;
    protected String retrivalRefNo;
    protected String systemTraceNo;
    protected String terminalId;

    /**
     * Gets the value of the amount property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getAmount() {
        return amount;
    }

    /**
     * Sets the value of the amount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setAmount(Long value) {
        this.amount = value;
    }

    /**
     * Gets the value of the retrivalRefNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRetrivalRefNo() {
        return retrivalRefNo;
    }

    /**
     * Sets the value of the retrivalRefNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRetrivalRefNo(String value) {
        this.retrivalRefNo = value;
    }

    /**
     * Gets the value of the systemTraceNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSystemTraceNo() {
        return systemTraceNo;
    }

    /**
     * Sets the value of the systemTraceNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSystemTraceNo(String value) {
        this.systemTraceNo = value;
    }

    /**
     * Gets the value of the terminalId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTerminalId() {
        return terminalId;
    }

    /**
     * Sets the value of the terminalId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTerminalId(String value) {
        this.terminalId = value;
    }

}
