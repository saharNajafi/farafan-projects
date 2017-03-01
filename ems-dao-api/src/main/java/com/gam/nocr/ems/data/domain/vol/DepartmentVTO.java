package com.gam.nocr.ems.data.domain.vol;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.util.EmsUtil;
import flexjson.JSON;

import java.io.Serializable;

/**
 * @author Haeri (haeri@gamelectronics.com)
 */
public class DepartmentVTO extends ExtEntityTO implements Serializable {

    private String code;
    private String name;
    private String address;
    private String postalCode;
    private String dn;
    private String parentDN;

    private String sendType;
    private String parentName;
    private Long parentId;
    private String locName;
    private Long locId;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getDn() {
        return dn;
    }

    public void setDn(String dn) {
        this.dn = dn;
    }

    public String getParentDN() {
        return parentDN;
    }

    public void setParentDN(String parentDN) {
        this.parentDN = parentDN;
    }

    @JSON(include = false)
    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    @JSON(include = false)
    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getLocName() {
        return locName;
    }

    public void setLocName(String locName) {
        this.locName = locName;
    }

    public Long getLocId() {
        return locId;
    }

    public void setLocId(Long locId) {
        this.locId = locId;
    }

    @Override
    public String toString() {
        return EmsUtil.toJSON(this);
    }
}
