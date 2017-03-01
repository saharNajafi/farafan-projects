
package com.gam.nocr.ems.biz.service.external.client.portal;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for updateRatingInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="updateRatingInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ratingInfoWTOList" type="{http://portalws.ws.web.portal.nocr.gam.com/}ratingInfoWTO" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateRatingInfo", propOrder = {
    "ratingInfoWTOList"
})
public class UpdateRatingInfo {

    protected List<RatingInfoWTO> ratingInfoWTOList;

    /**
     * Gets the value of the ratingInfoWTOList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ratingInfoWTOList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRatingInfoWTOList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RatingInfoWTO }
     * 
     * 
     */
    public List<RatingInfoWTO> getRatingInfoWTOList() {
        if (ratingInfoWTOList == null) {
            ratingInfoWTOList = new ArrayList<RatingInfoWTO>();
        }
        return this.ratingInfoWTOList;
    }

}
