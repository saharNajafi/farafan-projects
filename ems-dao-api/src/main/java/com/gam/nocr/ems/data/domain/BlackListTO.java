package com.gam.nocr.ems.data.domain;


import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.util.EmsUtil;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * @author: Haeri (haeri@gamelectronics.com)
 */
@Entity
@Table(name = "EMST_BLACK_LIST")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_BLACK_LIST", allocationSize = 1)
public class BlackListTO extends ExtEntityTO {

    private String firstName;
    private String surname;
    private String nationalId;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "BLK_ID")
    public Long getId() {
        return super.getId();
    }

    @Column(name = "BLK_FIRST_NAME", length = 84, nullable = false)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "BLK_SURNAME", length = 84, nullable = false)
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Size(min = 10, max = 10)
    @Column(name = "BLK_NATIONAL_ID", nullable = false)
    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    @Override
    public String toString() {
        return EmsUtil.toJSON(this);

    }
}
