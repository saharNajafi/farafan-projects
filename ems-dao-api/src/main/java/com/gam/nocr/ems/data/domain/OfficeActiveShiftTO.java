/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gam.nocr.ems.data.domain;



import com.fasterxml.jackson.annotation.JsonIdentityInfo;
//import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.data.enums.ShiftEnum;
//import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author safiary
 */
@Entity
@Table(name = "EMST_OFFICE_ACTIVE_SHIFT")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_OFFICE_ACTIVE", allocationSize = 1)
//@DynamicUpdate
@NamedQueries({
        @NamedQuery(
                name = "OfficeActiveShiftTO.findByEnrollmentOfficeId",
                query = "select oac from OfficeActiveShiftTO oac" +
                        " where oac.enrollmentOffice.id=:eofId" +
                        " and oac.activeDate=:activeDate" +
                        " and oac.shiftNo=:shiftNo"),
        @NamedQuery(
                name = "OfficeActiveShiftTO.findById",
                query = "select oac from OfficeActiveShiftTO oac" +
                        " where oac.id=:id"),
        @NamedQuery(
                name = "OfficeActiveShiftTO.listByDate",
                query = "select oac from OfficeActiveShiftTO oac where oac.activeDate =:activeShiftDate" +
                        " and oac.enrollmentOffice.singleStageOnly=false "
        )
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class OfficeActiveShiftTO extends ExtEntityTO {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    private int activeDate;
    private ShiftEnum shiftNo;
    private Integer remainCapacity;
    private OfficeCapacityTO officeCapacity;
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
//    @JsonIdentityReference(alwaysAsId = true)
    private EnrollmentOfficeTO enrollmentOffice;

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "EOA_ID")
    @Override
    public Long getId() {
        return super.getId();
    }

    @Basic(optional = false)
    @Column(name = "EOA_ACTIVE_DATE")
    public int getActiveDate() {
        return activeDate;
    }

    public void setActiveDate(int activeDate) {
        this.activeDate = activeDate;
    }

    @Basic(optional = false)
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "EOA_SHIFT_NO")
    public ShiftEnum getShiftNo() {
        return shiftNo;
    }

    public void setShiftNo(ShiftEnum shiftNo) {
        this.shiftNo = shiftNo;
    }

    @Basic(optional = false)
    @Column(name = "EOA_REMIAN_CAPACITY")
    public Integer getRemainCapacity() {
        return remainCapacity;
    }

    public void setRemainCapacity(Integer remainCapacity) {
        this.remainCapacity = remainCapacity;
    }

    @ManyToOne
    @JoinColumn(name = "EOA_OFFICE_ID")
    public EnrollmentOfficeTO getEnrollmentOffice() {
        return enrollmentOffice;
    }

    public void setEnrollmentOffice(EnrollmentOfficeTO enrollmentOffice) {
        this.enrollmentOffice = enrollmentOffice;
    }

    @ManyToOne
    @JoinColumn(name = "EOA_OC_ID")
    public OfficeCapacityTO getOfficeCapacity() {
        return officeCapacity;
    }

    public void setOfficeCapacity(OfficeCapacityTO officeCapacity) {
        this.officeCapacity = officeCapacity;
    }

    public OfficeActiveShiftTO() {
    }

    public OfficeActiveShiftTO(Long id) {
        this.setId(id);
    }

    public OfficeActiveShiftTO(Long id, int activeDate, ShiftEnum shiftNo, Integer remainCapacity, EnrollmentOfficeTO enrollmentOffice) {
        this.setId(id);
        this.activeDate = activeDate;
        this.shiftNo = shiftNo;
        this.remainCapacity = remainCapacity;
        this.enrollmentOffice = enrollmentOffice;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OfficeActiveShiftTO)) {
            return false;
        }
        OfficeActiveShiftTO other = (OfficeActiveShiftTO) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.farafan.crsProvider.domain.OfficeActiveShiftTO[ id=" + getId() + " ]";
    }

}
