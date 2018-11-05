package com.gam.nocr.ems.data.domain.vol;


import com.gam.commons.core.data.domain.ExtEntityTO;


/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 10/15/18.
 */
public class OfficeSettingVTO extends ExtEntityTO {

    private Long feiId;
    private Long osdId;
    private String featureExtractName;
    private String featureExtractType;

    public Long getOsdId() {
        return osdId;
    }

    public void setOsdId(Long osdId) {
        this.osdId = osdId;
    }

    public String getFeatureExtractName() {
        return featureExtractName;
    }

    public void setFeatureExtractName(String featureExtractName) {
        this.featureExtractName = featureExtractName;
    }

    public String getFeatureExtractType() {
        return featureExtractType;
    }

    public void setFeatureExtractType(String featureExtractType) {
        this.featureExtractType = featureExtractType;
    }

    public Long getFeiId() {
        return feiId;
    }

    public void setFeiId(Long feiId) {
        this.feiId = feiId;
    }
}
