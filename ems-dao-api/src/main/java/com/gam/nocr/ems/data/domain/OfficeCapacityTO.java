/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gam.nocr.ems.data.domain;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.data.enums.ShiftEnum;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author safiary
 */
@Entity
@Table(name = "EMST_OFFICE_CAPACITY")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_OFFICE_CAPACITY", allocationSize = 1)
@NamedQueries({
        @NamedQuery(
                name = "officeCapacityTO.findByEnrollmentOfficeIdAndShiftNo",
                query = "SELECT oc FROM OfficeCapacityTO oc" +
                        " where oc.enrollmentOffice.id=:enrollmentOfficeId" +
                        " and oc.shiftNo=:shiftNo" +
                        " order by oc.startDate ASC "),
        @NamedQuery(
                name = "OfficeCapacityTO.findByEnrollmentOfficeId",
                query = "SELECT oc FROM OfficeCapacityTO oc where oc.enrollmentOffice.id=:eofId"),
        @NamedQuery(
                name = "OfficeCapacityTO.listOfficeCapacityByDate",
                query = "select oc from OfficeCapacityTO oc where oc.endDate >= :startDate and oc.startDate <= :endDate"
        )
  })
public class OfficeCapacityTO extends ExtEntityTO {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    private int startDate;
    private int endDate;
    private ShiftEnum shiftNo;
    private short capacity;
    private Float workingHoursFrom;
    private Float workingHoursTo;
    private EnrollmentOfficeTO enrollmentOffice;

    public OfficeCapacityTO() {
    }

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "EOC_ID")
    public Long getId() {
        return super.getId();
    }
    @Basic(optional = false)
    @NotNull
    @Column(name = "EOC_START_DATE")
    public int getStartDate() {
        return startDate;
    }

    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }

    @Basic(optional = false)
    @NotNull
    @Column(name = "EOC_END_DATE")
    public int getEndDate() {
        return endDate;
    }

    public void setEndDate(int endDate) {
        this.endDate = endDate;
    }

    @Basic(optional = false)
    @NotNull
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "EOC_SHIFT_NO")
    public ShiftEnum getShiftNo() {
        return shiftNo;
    }

    public void setShiftNo(ShiftEnum shiftNo) {
        this.shiftNo = shiftNo;
    }

    @Basic(optional = false)
    @NotNull
    @Column(name = "EOC_CAPACITY")
    public short getCapacity() {
        return capacity;
    }

    public void setCapacity(short capacity) {
        this.capacity = capacity;
    }

    @Column(name = "EOC_WORKING_HOURS_FROM")
    public Float getWorkingHoursFrom() {
        return workingHoursFrom;
    }

    public void setWorkingHoursFrom(Float workingHoursFrom) {
        this.workingHoursFrom = workingHoursFrom;
    }

    @Column(name = "EOC_WORKING_HOURS_TO")
    public Float getWorkingHoursTo() {
        return workingHoursTo;
    }

    public void setWorkingHoursTo(Float workingHoursTo) {
        this.workingHoursTo = workingHoursTo;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EOC_OFFICE_ID")
    public EnrollmentOfficeTO getEnrollmentOffice() {
        return enrollmentOffice;
    }

    public void setEnrollmentOffice(EnrollmentOfficeTO enrollmentOffice) {
        this.enrollmentOffice = enrollmentOffice;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (super.getId() != null ? super.getId().hashCode() : 0);
        return hash;
    }

//    @Override
//    public boolean equals(Object object) {
//        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof OfficeCapacityTO)) {
//            return false;
//        }
//        OfficeCapacityTO other = (OfficeCapacityTO) object;
//        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
//            return false;
//        }
//        return true;
//    }

    @Override
    public String toString() {
        return "com.gam.nocr.ems.data.domain.OfficeCapacityTO[ id=" + super.getId() + " ]";
    }


}
