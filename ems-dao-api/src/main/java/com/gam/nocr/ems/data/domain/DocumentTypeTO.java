package com.gam.nocr.ems.data.domain;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.util.EmsUtil;

import javax.persistence.*;

/**
 * @author: Haeri (haeri@gamelectronics.com)
 */
@Entity
@Table(name = "EMST_DOC_TYPE")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_DOC_TYPE")
public class DocumentTypeTO extends ExtEntityTO {

    private String name;
    private String services;

    public DocumentTypeTO() {
    }

    public DocumentTypeTO(Long id) {
        this.setId(id);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "DCT_ID")
    public Long getId() {
        return super.getId();
    }

    @Column(name = "DCT_NAME", nullable = false, length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Transient
    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    @Override
    public String toString() {
        return EmsUtil.toJSON(this);
    }
}
