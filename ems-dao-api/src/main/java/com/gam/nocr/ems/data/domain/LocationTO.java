package com.gam.nocr.ems.data.domain;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.data.enums.LocationType;
import com.gam.nocr.ems.util.JSONable;
import flexjson.JSON;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
@Entity
@Table(name = "EMST_LOCATION")
public class LocationTO extends ExtEntityTO implements JSONable {

    private LocationTO province;
    private LocationTO county;
    private LocationTO township;
    private LocationTO district;
    private String name;
    private String type;
    private String state;
    private Boolean modified;
    private ProvinceCodeTO provinceCodeTO;

    public LocationTO() {
    }

    public LocationTO(Long id) {
        this.setId(id);
    }

    public LocationTO(Long id, LocationTO province, LocationTO county, LocationTO township, LocationTO district,
                      String name, LocationType type, String state, Boolean modified) {
        this.setId(id);
        this.province = province;
        this.county = county;
        this.township = township;
        this.district = district;
        this.name = name;
        this.type = LocationType.toNocrString(type);
        this.state = state;
        this.modified = modified;
    }

    @Id
    @Column(name = "LOC_ID")
    public Long getId() {
        return super.getId();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LOC_PROVINCE_ID")
    @JSON(include = false)
    public LocationTO getProvince() {
        return province;
    }

    public void setProvince(LocationTO provinceId) {
        this.province = provinceId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LOC_COUNTY_ID")
    @JSON(include = false)
    public LocationTO getCounty() {
        return county;
    }

    public void setCounty(LocationTO countyId) {
        this.county = countyId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LOC_TOWNSHIP_ID")
    @JSON(include = false)
    public LocationTO getTownship() {
        return township;
    }

    public void setTownship(LocationTO townshipId) {
        this.township = townshipId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LOC_DISTRICT_ID")
    @JSON(include = false)
    public LocationTO getDistrict() {
        return district;
    }

    public void setDistrict(LocationTO districtId) {
        this.district = districtId;
    }

    @Column(name = "LOC_NAME", length = 80, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "LOC_TYPE")
    @JSON(include = false)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "LOC_STATE", nullable = false)
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Column(name = "LOC_MODIFIED")
    @JSON(include = false)
    public Boolean getModified() {
        return modified;
    }

    public void setModified(Boolean modified) {
        this.modified = modified;
    }

    @NotNull
    @JoinColumn(name = "LOC_PROVINCE_ID", referencedColumnName = "PRO_GEO_ID",insertable = false,updatable = false)
    @ManyToOne(optional = false)
    public ProvinceCodeTO getProvinceCodeTO() {
        return provinceCodeTO;
    }

    public void setProvinceCodeTO(ProvinceCodeTO provinceCodeTO) {
        this.provinceCodeTO = provinceCodeTO;
    }

    /**
     * The method toJSON is used to convert an object to an instance of type {@link String} to build a JSON Object
     *
     * @return an instance of type {@link String}
     */
    @Override
    public String toJSON() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
