package com.gam.nocr.ems.data.domain;

import com.gam.commons.core.data.domain.ExtEntityTO;

import javax.persistence.*;

/**
 * @author Mazaher Namjoofar
 */
@Entity
@Table(name = "CBI_PROVINCE_LOCATION_MAP")
public class ProvinceCodeTO extends ExtEntityTO {

    private String provinceName;
    private Integer provinceCode;

    @Id
    @Column(name = "EMS_LOC_ID")
    public Long getId() {
        return super.getId();
    }

    @Column(name = "CBI_PROVINCE_NAME", length = 255)
    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    @Column(name = "CBI_PROVINCE_CODE")
    public Integer getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(Integer provinceCode) {
        this.provinceCode = provinceCode;
    }
}
