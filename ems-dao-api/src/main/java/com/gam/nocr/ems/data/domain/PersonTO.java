package com.gam.nocr.ems.data.domain;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.data.enums.BooleanType;
import com.gam.nocr.ems.data.enums.PersonRequestStatus;
import com.gam.nocr.ems.util.EmsUtil;
import com.gam.nocr.ems.util.JSONable;
import flexjson.JSON;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * <p> TODO -- Explain this class </p>
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
@Entity
@Table(name = "EMST_PERSON")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_PERSON", allocationSize = 1)
public class PersonTO extends ExtEntityTO implements JSONable {

    private String firstName;
    private String lastName;
    private String nid;
    private String userName;
    private Integer userId;
    private Timestamp lastLogin;
    private String birthCertNum;
    private String birthCertSeries;
    private String fatherName;
    private String mobilePhone;
    private String phone;
    private BooleanType status;
    private PersonRequestStatus requestStatus;
    private String email;
    private DepartmentTO department;

    private Long departmentId;
    private String departmentName;
    private String lStatus;
    private String requestStatusString;
    private String admin;
    private String provinceName;

    private List<ReportRequestTO> reportRequestTOs = new ArrayList<ReportRequestTO>(0);

    public PersonTO() {
    }

    public PersonTO(Long id) {
        setId(id);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "PER_ID")
    @JSON(include = false)
    public Long getId() {
        return super.getId();
    }

    @Column(name = "PER_FIRST_NAME", length = 255, nullable = false)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "PER_LAST_NAME", length = 255, nullable = false)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Size(min = 10, max = 10)
    @Column(name = "PER_NID", nullable = false)
    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    @Column(name = "PER_USERNAME", length = 255, nullable = false)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "PER_USERID")
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Column(name = "PER_LAST_LOGIN")
    @JSON(include = false)
    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }


    @Column(name = "PER_BIRTH_CERT_NO", length = 255)
    @JSON(include = false)
    public String getBirthCertNum() {
        return birthCertNum;
    }

    public void setBirthCertNum(String birthCertNum) {
        this.birthCertNum = birthCertNum;
    }

    @Column(name = "PER_BIRTH_CERT_SERIES", length = 255)
    @JSON(include = false)
    public String getBirthCertSeries() {
        return birthCertSeries;
    }

    public void setBirthCertSeries(String birthCertSeries) {
        this.birthCertSeries = birthCertSeries;
    }

    @Column(name = "PER_FATHER_NAME", length = 255)
    @JSON(include = false)
    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PER_DEP_ID")
    @JSON(include = false)
    public DepartmentTO getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentTO department) {
        this.department = department;
    }

    @Column(name = "PER_MOBILE_PHONE", length = 11)
    @JSON(include = false)
    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    @Column(name = "PER_PHONE", length = 20)
    @JSON(include = false)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "PER_STATUS", length = 1, nullable = false)
    public BooleanType getStatus() {
        return status;
    }

    public void setStatus(BooleanType status) {
        this.status = status;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "PER_REQUEST_STATE", nullable = false)
    @JSON(include = false)
    public PersonRequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(PersonRequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    /**
     * @return the email
     */
    @Column(name = "PER_EMAIL", length = 255)
    @JSON(include = false)
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    @Transient
    @JSON(include = false)
    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    @Transient
    @JSON(include = false)
    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Transient
    @JSON(include = false)
    public String getlStatus() {
        return lStatus;
    }

    public void setlStatus(String lStatus) {
        this.lStatus = lStatus;
    }

    @Transient
    @JSON(include = false)
    public String getRequestStatusString() {
        return requestStatusString;
    }

    public void setRequestStatusString(String requestStatusString) {
        this.requestStatusString = requestStatusString;
    }

    @Transient
    @JSON(include = false)
    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    @OneToMany(mappedBy = "person", cascade = {CascadeType.PERSIST})
    public List<ReportRequestTO> getReportRequestTOs() {
        return reportRequestTOs;
    }

    public void setReportRequestTOs(List<ReportRequestTO> reportRequestTOs) {
        this.reportRequestTOs = reportRequestTOs;
    }
    
    @Transient
    @JSON(include = false)
    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }


    @Override
    public String toString() {
        return toJSON();
    }

    /**
     * The method toJSON is used to convert an object to an instance of type {@link String}
     *
     * @return an instance of type {@link String}
     */
    @Override
    public String toJSON() {
        String jsonObject = EmsUtil.toJSON(this);
        jsonObject = jsonObject.substring(0, jsonObject.length() - 1);
        if (department == null) {
            jsonObject += ", departmentId:" + department;
        } else {
            jsonObject += ", departmentId:" + department.getId();
        }
        jsonObject += "}";
        return jsonObject;
    }
}