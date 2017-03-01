package com.gam.nocr.ems.data.domain;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.util.EmsUtil;

import javax.persistence.*;

/**
 * <p> TODO -- Explain this class </p>
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
@Entity
@Table(name = "EMST_RATING_INFO")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_RATING_INFO", allocationSize = 1)
public class RatingInfoTO extends ExtEntityTO {

    private String clazz;
    private Long size;
    private Boolean modified;

    public RatingInfoTO() {
    }

    public RatingInfoTO(Long id) {
        this.setId(id);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "RAT_ID")
    public Long getId() {
        return super.getId();
    }

    @Column(name = "RAT_CLASS", nullable = false, unique = true)
    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    @Column(name = "RAT_SIZE", nullable = false)
    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    @Column(name = "RAT_MODIFIED", nullable = false)
    public Boolean getModified() {
        return modified;
    }

    public void setModified(Boolean modified) {
        this.modified = modified;
    }

    @Override
    public String toString() {
        return EmsUtil.toJSON(this);
    }
}
