package com.gam.nocr.ems.data.domain.vol;


import com.gam.commons.core.data.domain.ExtEntityTO;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 10/15/18.
 */
public class OfficeSettingVTO extends ExtEntityTO {

    private Long id;
    private String featureExtractorID;
    private String featureExtractorVersion;
    private Long feiId;
    private Long fevId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFeatureExtractorID() {
        return featureExtractorID;
    }

    public void setFeatureExtractorID(String featureExtractorID) {
        this.featureExtractorID = featureExtractorID;
    }

    public String getFeatureExtractorVersion() {
        return featureExtractorVersion;
    }

    public void setFeatureExtractorVersion(String featureExtractorVersion) {
        this.featureExtractorVersion = featureExtractorVersion;
    }

    public Long getFeiId() {
        return feiId;
    }

    public void setFeiId(Long feiId) {
        this.feiId = feiId;
    }

    public Long getFevId() {
        return fevId;
    }

    public void setFevId(Long fevId) {
        this.fevId = fevId;
    }
}
