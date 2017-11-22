package com.gam.nocr.ems.data.domain;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.data.enums.WorkstationState;
import com.gam.nocr.ems.util.EmsUtil;
import com.gam.nocr.ems.util.JSONable;
import flexjson.JSON;

import javax.persistence.*;

/**
 * @author Haeri (haeri@gamelectronics.com)
 */
@Entity
@Table(name = "EMST_WORKSTATION")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_WORKSTATION", allocationSize = 1)
public class WorkstationTO extends ExtEntityTO implements JSONable {

    private String code;
    private String activationCode;
    private EnrollmentOfficeTO enrollmentOffice;
    private WorkstationState state;

    private String enrollmentOfficeName;
    private Long enrollmentOfficeId;
    private String enrollmentOfficeCode;
    private String lState;
    private String provinceName;
    private WorkstationInfoTO workstationInfoTO;
    private WorkstationPluginsTO workstationPluginsTO;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "WST_ID")
    public Long getId() {
        return super.getId();
    }

    @Column(name = "WST_CODE", length = 255)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "WST_ACTIVATION_CODE", length = 255, nullable = false)
    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WST_ENROLLMENT_ID", nullable = false)
    @JSON(include = false)
    public EnrollmentOfficeTO getEnrollmentOffice() {
        return enrollmentOffice;
    }

    public void setEnrollmentOffice(EnrollmentOfficeTO enrollmentOffice) {
        this.enrollmentOffice = enrollmentOffice;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "WST_STATUS", length = 1, nullable = false)
    public WorkstationState getState() {
        return state;
    }

    public void setState(WorkstationState state) {
        this.state = state;
    }

    @Transient
    @JSON(include = false)
    public String getEnrollmentOfficeName() {
        return enrollmentOfficeName;
    }

    public void setEnrollmentOfficeName(String enrollmentOfficeName) {
        this.enrollmentOfficeName = enrollmentOfficeName;
    }

    @Transient
    @JSON(include = false)
    public Long getEnrollmentOfficeId() {
        return enrollmentOfficeId;
    }

    public void setEnrollmentOfficeId(Long enrollmentOfficeId) {
        this.enrollmentOfficeId = enrollmentOfficeId;
    }

    @Transient
    @JSON(include = false)
    public String getEnrollmentOfficeCode() {
        return enrollmentOfficeCode;
    }

    public void setEnrollmentOfficeCode(String enrollmentOfficeCode) {
        this.enrollmentOfficeCode = enrollmentOfficeCode;
    }

    @Transient
    @JSON(include = false)
    public String getlState() {
        return lState;
    }

    public void setlState(String lState) {
        this.lState = lState;
    }

    @Transient
    @JSON(include = false)
    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    @OneToOne(mappedBy = "workstationTO", cascade = CascadeType.PERSIST)
    public WorkstationInfoTO getWorkstationInfoTO() {
        return workstationInfoTO;
    }

    public void setWorkstationInfoTO(WorkstationInfoTO workstationInfoTO) {
        this.workstationInfoTO = workstationInfoTO;
    }

    @OneToOne(mappedBy = "workstationTO", cascade = CascadeType.PERSIST)
    public WorkstationPluginsTO getWorkstationPluginsTO() {
        return workstationPluginsTO;
    }

    public void setWorkstationPluginsTO(WorkstationPluginsTO workstationPluginsTO) {
        this.workstationPluginsTO = workstationPluginsTO;
    }
    //	public String toString(Long id,
//						   String code,
//						   String activationCode,
//						   WorkstationState state,
//						   String enrollmentOfficeName,
//						   Long enrollmentOfficeId) {
//		return "WorkstationTO :{ " +
//				"ID: " + id +
//				"Code: " + code +
//				"ActivationCode: " + activationCode +
//				"Enrollment Office ID: " + enrollmentOfficeId +
//				"Enrollment Office Name: " + enrollmentOfficeName +
//				"state: " + state;
//	}


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
        if (enrollmentOffice == null) {
            jsonObject += ", enrollmentOfficeId:" + enrollmentOffice;
        } else {
            jsonObject += ", enrollmentOfficeId:" + enrollmentOffice.getId();
        }
        jsonObject += "}";

        return jsonObject;
    }
}