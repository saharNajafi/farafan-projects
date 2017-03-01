package com.gam.nocr.ems.data.domain;

import com.gam.commons.core.data.domain.ExtEntityTO;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * @author: Haeri (haeri@gamelectronics.com)
 */
@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "EMST_MARITAL_STATUS")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_MARITAL_STATUS", allocationSize = 1)
public class MaritalStatusTO extends ExtEntityTO {

    private String name;

    public MaritalStatusTO() {
    }

    public MaritalStatusTO(Long id) {
        this.setId(id);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "MST_ID")
    public Long getId() {
        return super.getId();
    }

    @Column(name = "MST_NAME", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MaritalStatusTO : {" +
                "id = " + getId() +
                ", name = " + name +
                " }";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof MaritalStatusTO)) {
            return false;
        }
        MaritalStatusTO other = (MaritalStatusTO) obj;
        if (this.getId() == null ||
                other.getId() == null) {
            return false;
        }
        return this.getId().equals(other.getId());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }
}
