package com.gam.nocr.ems.data.domain.vol;

import com.gam.nocr.ems.util.EmsUtil;

import java.util.Date;

/**
 * Created by hossein on 12/21/2015.
 */
public class SpouseVTO{

    private String firstName;
    private String nid;
    private String sureName;
    private Date marriageDate;
    private Long maritalStatusId;
    private Date deathDivorceDate;

   public  SpouseVTO(){

   }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getSureName() {
        return sureName;
    }

    public void setSureName(String sureName) {
        this.sureName = sureName;
    }

    public Date getMarriageDate() {
        return marriageDate;
    }

    public void setMarriageDate(Date marriageDate) {
        this.marriageDate = marriageDate;
    }

    public Long getMaritalStatusId() {
        return maritalStatusId;
    }

    public void setMaritalStatusId(Long maritalStatusId) {
        this.maritalStatusId = maritalStatusId;
    }

    public Date getDeathDivorceDate() {
        return deathDivorceDate;
    }

    public void setDeathDivorceDate(Date deathDivorceDate) {
        this.deathDivorceDate = deathDivorceDate;
    }

    @Override
    public String toString() {
        return EmsUtil.toJSON(this);
    }
}
