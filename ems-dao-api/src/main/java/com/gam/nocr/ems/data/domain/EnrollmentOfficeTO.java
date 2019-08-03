package com.gam.nocr.ems.data.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gam.nocr.ems.data.enums.EnrollmentOfficeDeliverStatus;
import com.gam.nocr.ems.data.enums.EnrollmentOfficeStatus;
import com.gam.nocr.ems.data.enums.EnrollmentOfficeType;
import com.gam.nocr.ems.data.enums.OfficeCalenderType;
import com.gam.nocr.ems.data.enums.OfficeType;
import com.gam.nocr.ems.util.JSONable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author Haeri (haeri@gamelectronics.com)
 */
@Entity
@Table(name = "EMST_ENROLLMENT_OFFICE")
@NamedQueries({
        @NamedQuery(
                name = "EnrollmentOfficeTO.findEnrollmentOfficeById",
                query = " select eof" +
                        " from EnrollmentOfficeTO eof" +
                        " where eof.id =:eofId and deleted = false "
        )
})
@PrimaryKeyJoinColumn(name = "EOF_ID", referencedColumnName = "DEP_ID")
public class EnrollmentOfficeTO extends DepartmentTO implements JSONable {

    private String phone;
    private PersonTO manager;
    private String fax;
    private Integer area;
    private RatingInfoTO ratingInfo;
    private Date archiveIdRecycleDate;
    private String archiveIdCounter;
    private EnrollmentOfficeType type;
    private Float workingHoursFrom;
    private Float workingHoursTo;
    private EnrollmentOfficeTO superiorOffice;
    private EnrollmentOfficeStatus status;
    private EnrollmentOfficeDeliverStatus deliver;
    private OfficeType khosusiType;
    private OfficeCalenderType calenderType;
    private String postDestinationCode;
    private Boolean isPostNeeded = true;
    private Boolean hasStair;
    private Boolean hasElevator;
    private Boolean hasPortabilityEquipment;
    private Boolean isActive;
    private List<OfficeCapacityTO> officeCapacityTO;
    private Boolean thursdayMorningActive = false;
    private Boolean thursdayEveningActive = false;
    private Boolean fridayMorningActive = false;
    private Boolean fridayEveningActive = false;
    private Boolean singleStageOnly;
    private Boolean deleted;



    public EnrollmentOfficeTO() {
    }

    public EnrollmentOfficeTO(Long id) {
        this.setId(id);
    }

    @Column(name = "EOF_PHONE", length = 20)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EOF_MANAGER_ID")
    public PersonTO getManager() {
        return manager;
    }

    public void setManager(PersonTO manager) {
        this.manager = manager;
    }

    @Column(name = "EOF_FAX", length = 20)
    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    @Column(name = "EOF_AREA")
    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EOF_RAT_ID", nullable = false)
    public RatingInfoTO getRatingInfo() {
        return ratingInfo;
    }

    public void setRatingInfo(RatingInfoTO ratingInfo) {
        this.ratingInfo = ratingInfo;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EOF_ARCHIVE_ID_RECYCLE_DATE")
    public Date getArchiveIdRecycleDate() {
        return archiveIdRecycleDate;
    }

    public void setArchiveIdRecycleDate(Date archiveIdRecycleDate) {
        this.archiveIdRecycleDate = archiveIdRecycleDate;
    }

    @Column(name = "EOF_ARCHIVE_ID_COUNTER")
    public String getArchiveIdCounter() {
        return archiveIdCounter;
    }

    public void setArchiveIdCounter(String archiveIdCounter) {
        this.archiveIdCounter = archiveIdCounter;
    }

    @Column(name = "EOF_IS_DELETED",columnDefinition = "NUMBER(1) DEFAULT 0")
    @NotNull
    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "EOF_TYPE")
    public EnrollmentOfficeType getType() {
        return type;
    }

    public void setType(EnrollmentOfficeType type) {
        this.type = type;
    }

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "EOF_DELIVER_TYPE")
    public EnrollmentOfficeDeliverStatus getDeliver() {
        return deliver;
    }

    public void setDeliver(EnrollmentOfficeDeliverStatus deliver) {
        this.deliver = deliver;
    }


    @Column(name = "EOF_WORKING_HOURS_FROM")
    public Float getWorkingHoursFrom() {
        return workingHoursFrom;
    }

    public void setWorkingHoursFrom(Float workingHoursFrom) {
        this.workingHoursFrom = workingHoursFrom;
    }

    @Column(name = "EOF_WORKING_HOURS_TO")
    public Float getWorkingHoursTo() {
        return workingHoursTo;
    }

    public void setWorkingHoursTo(Float workingHoursTo) {
        this.workingHoursTo = workingHoursTo;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EOF_SUPERIOR_OFFICE")
    public EnrollmentOfficeTO getSuperiorOffice() {
        return superiorOffice;
    }

    public void setSuperiorOffice(EnrollmentOfficeTO superiorOffice) {
        this.superiorOffice = superiorOffice;
    }

    @Transient
    public EnrollmentOfficeStatus getStatus() {
        return status;
    }

    public void setStatus(EnrollmentOfficeStatus status) {
        this.status = status;
    }

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "EOF_KHOSUSI_TYPE")
    public OfficeType getKhosusiType() {
        return khosusiType;
    }

    public void setKhosusiType(OfficeType khosusiType) {
        this.khosusiType = khosusiType;
    }

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "EOF_CALENDER_TYPE")
    public OfficeCalenderType getCalenderType() {
        return calenderType;
    }

    public void setCalenderType(OfficeCalenderType calenderType) {
        this.calenderType = calenderType;
    }

    @Column(name = "EOF_POST_DESTINATION_CODE", length = 10)
    public String getPostDestinationCode() {
        return postDestinationCode;
    }

    public void setPostDestinationCode(String postDestinationCode) {
        this.postDestinationCode = postDestinationCode;
    }

    @Column(name = "EOF_IS_POST_NEEDED")
    public Boolean getPostNeeded() {
        return isPostNeeded;
    }

    public void setPostNeeded(Boolean postNeeded) {
        isPostNeeded = postNeeded;
    }

    @Column(name = "EOF_HAS_STAIR", nullable = false)
    public Boolean getHasStair() {
        return hasStair;
    }

    public void setHasStair(Boolean hasStair) {
        this.hasStair = hasStair;
    }

    @Column(name = "EOF_HAS_ELEVATOR", nullable = false)
    public Boolean getHasElevator() {
        return hasElevator;
    }

    public void setHasElevator(Boolean hasElevator) {
        this.hasElevator = hasElevator;
    }

    @Column(name = "EOF_HAS_PORTABILITY_EQUIPMENT", nullable = false)
    public Boolean getHasPortabilityEquipment() {
        return hasPortabilityEquipment;
    }

    public void setHasPortabilityEquipment(Boolean hasPortabilityEquipment) {
        this.hasPortabilityEquipment = hasPortabilityEquipment;
    }

    @Column(name = "EOF_IS_ACTIVE")
    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @OneToMany(mappedBy = "enrollmentOffice")
    @JsonIgnore
    public List<OfficeCapacityTO> getOfficeCapacityTO() {
        return officeCapacityTO;
    }

    public void setOfficeCapacityTO(List<OfficeCapacityTO> officeCapacityTO) {
        this.officeCapacityTO = officeCapacityTO;
    }

    @Basic(optional = false)
    @NotNull
    @Column(name = "THURSDAY_MORNING_ACTIVE", columnDefinition = "NUMBER(1) DEFAULT 0")
    public Boolean getThursdayMorningActive() {
        return thursdayMorningActive;
    }

    public void setThursdayMorningActive(Boolean thursdayMorningActive) {
        this.thursdayMorningActive = thursdayMorningActive;
    }

    @Basic(optional = false)
    @NotNull
    @Column(name = "THURSDAY_EVENING_ACTIVE", columnDefinition = "NUMBER(1) DEFAULT 0")
    public Boolean getThursdayEveningActive() {
        return thursdayEveningActive;
    }

    public void setThursdayEveningActive(Boolean thursdayEveningActive) {
        this.thursdayEveningActive = thursdayEveningActive;
    }

    @Basic(optional = false)
    @NotNull
    @Column(name = "FRIDAY_MORNING_ACTIVE", columnDefinition = "NUMBER(1) DEFAULT 0")
    public Boolean getFridayMorningActive() {
        return fridayMorningActive;
    }

    public void setFridayMorningActive(Boolean fridayMorningActive) {
        this.fridayMorningActive = fridayMorningActive;
    }

    @Basic(optional = false)
    @NotNull
    @Column(name = "FRIDAY_EVENING_ACTIVE", columnDefinition = "NUMBER(1) DEFAULT 0")
    public Boolean getFridayEveningActive() {
        return fridayEveningActive;
    }

    public void setFridayEveningActive(Boolean fridayEveningActive) {
        this.fridayEveningActive = fridayEveningActive;
    }

    @Basic(optional = false)
    @Column(name = "SINGLE_STAGE_ONLY")
    public Boolean getSingleStageOnly() {
        return singleStageOnly;
    }

    public void setSingleStageOnly(Boolean singleStageOnly) {
        this.singleStageOnly = singleStageOnly;
    }


    @Override
    public String toString() {
        StringBuilder returnValue = new StringBuilder();
        returnValue.append(returnValue.append("EnrollmentOfficeTO : { ")).append("id = ").append(getId()).append(", area = ").append(area).append(", ").append(", name = ").append(getName()).append(", ");
        if (status == null) {
            returnValue.append("status = ").append(status).append(", ");
        } else {
            returnValue.append("status = ").append(status.name()).append(", ");
        }
        returnValue.append(" }");
        return returnValue.toString();
    }


}
