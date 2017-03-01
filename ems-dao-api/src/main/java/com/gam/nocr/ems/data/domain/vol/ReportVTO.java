package com.gam.nocr.ems.data.domain.vol;

import com.gam.commons.core.data.domain.ExtEntityTO;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public class ReportVTO extends ExtEntityTO{
    private String name;
    private String comment;
    private String createDate;
    private String permission;
    private String activationFlag;
    private String scope;
//    private String metaData;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getActivationFlag() {
        return activationFlag;
    }

    public void setActivationFlag(String activationFlag) {
        this.activationFlag = activationFlag;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    //    public String getMetaData() {
//        return metaData;
//    }
//
//    public void setMetaData(String metaData) {
//        this.metaData = metaData;
//    }
}
