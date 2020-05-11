package com.gam.nocr.ems.data.domain;

import com.gam.commons.core.data.domain.ExtEntityTO;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author Mazaher Namjoofar
 */
@Cacheable
@Entity
@Table(name = "EMST_CBI_PROVINCE_LOCATION_MAP")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMST_PROVINCE_CODE")
public class ProvinceCodeTO extends ExtEntityTO {

    private Long geoId;
    private String provinceName;
    private Integer provinceCode;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "PRO_CODE_ID")
    public Long getId() {
        return super.getId();
    }

    @Id
    @NotNull
    @Column(name = "PRO_GEO_ID")
    public Long getGeoId() {
        return geoId;
    }

    public void setGeoId(Long geoId) {
        this.geoId = geoId;
    }

    @Column(name = "PRO_PROVINCE_NAME", length = 255)
    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    @Column(name = "PRO_PROVINCE_CODE")
    public Integer getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(Integer provinceCode) {
        this.provinceCode = provinceCode;
    }
}
