package com.gam.nocr.ems.data.domain.ws;

import com.gam.nocr.ems.util.EmsUtil;
import flexjson.JSON;

/**
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class PersonWTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String nid;
    private String fatherName;
    private String birthCertNum;
    private String birthCertSeries;
    private String email;
    private String requestState;

    public PersonWTO() {
    }

    public PersonWTO(Long id, String firstName, String lastName, String nid, String fatherName, String birthCertNum, String birthCertSeries, String email, String requestState) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nid = nid;
        this.fatherName = fatherName;
        this.birthCertNum = birthCertNum;
        this.birthCertSeries = birthCertSeries;
        this.email = email;
        this.requestState = requestState;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getBirthCertNum() {
        return birthCertNum;
    }

    public void setBirthCertNum(String birthCertNum) {
        this.birthCertNum = birthCertNum;
    }

    @JSON(include = false)
    public String getBirthCertSeries() {
        return birthCertSeries;
    }

    public void setBirthCertSeries(String birthCertSeries) {
        this.birthCertSeries = birthCertSeries;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRequestState() {
        return requestState;
    }

    public void setRequestState(String requestState) {
        this.requestState = requestState;
    }

    @Override
    public String toString() {
        return EmsUtil.toJSON(this);
    }
}
