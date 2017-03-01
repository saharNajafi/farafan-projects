package com.gam.nocr.ems.data.domain.vol;

import com.gam.nocr.ems.util.EmsUtil;

import java.util.Date;

/**
 * Created by Jafari on 6/21/2014.
 */
public class ChildVTO{

    private String firstName;
    private String gender;
    private String nid;
    private Date birthDate;

    public ChildVTO(){

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return EmsUtil.toJSON(this);
    }
}
