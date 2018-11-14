package com.gam.nocr.ems.data.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.util.EmsUtil;
import com.gam.nocr.ems.util.JSONable;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "EMST_OFFICE_SETTING")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_OFFICE_SETTING", allocationSize = 1)
@NamedQueries({
        @NamedQuery(name = "OfficeSettingTO.findById"
        , query = "select ost" +
                " from OfficeSettingTO ost" +
                " where ost.id=:id")
})
public class OfficeSettingTO extends ExtEntityTO implements Serializable,
        JSONable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private EnrollmentOfficeTO enrollmentOffice;
    private Boolean nonConfirmingIcaoImage = Boolean.FALSE;
    private Boolean uploadPhoto = Boolean.FALSE;
    private Boolean fingerScanSingleMode = Boolean.FALSE;
    private Boolean disabilityMode = Boolean.FALSE;
    private Boolean reissueRequest = Boolean.FALSE;
    private Boolean elderlyMode = Boolean.FALSE;
    private Boolean nMocGeneration = Boolean.FALSE;
    private Boolean amputationAnnouncment = Boolean.FALSE;
    private Boolean useScannerUI = Boolean.FALSE;
    private Boolean allowEditBackground = Boolean.FALSE;
    private Boolean allowAmputatedFingerStatusForElderly = Boolean.FALSE;
    private Boolean allowChangeFingerStatusDuringCaptureForElderly = Boolean.TRUE;
    private Set<FeatureExtractIdsTO> featureExtractIdsTO;


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "OST_ID")
    public Long getId() {
        return super.getId();
    }

    @ManyToOne
    @JoinColumn(name = "OST_ENROLL_OFFICE_ID")
    public EnrollmentOfficeTO getEnrollmentOffice() {
        return enrollmentOffice;
    }

    public void setEnrollmentOffice(EnrollmentOfficeTO enrollmentOffice) {
        this.enrollmentOffice = enrollmentOffice;
    }

    @Column(name = "OST_UPLOAD_PHOTO")
    public Boolean getUploadPhoto() {
        return uploadPhoto;
    }

    public void setUploadPhoto(Boolean uploadPhoto) {
        this.uploadPhoto = uploadPhoto;
    }

    @Column(name = "OST_FINGER_SCAN_SINGLE_MODE")
    public Boolean getFingerScanSingleMode() {
        return fingerScanSingleMode;
    }

    public void setFingerScanSingleMode(Boolean fingerScanSingleMode) {
        this.fingerScanSingleMode = fingerScanSingleMode;
    }

    @Column(name = "OST_DISABILITY_MODE")
    public Boolean getDisabilityMode() {
        return disabilityMode;
    }

    public void setDisabilityMode(Boolean disabilityMode) {
        this.disabilityMode = disabilityMode;
    }

    @Column(name = "OST_REISSUE_REQUEST")
    public Boolean getReissueRequest() {
        return reissueRequest;
    }

    public void setReissueRequest(Boolean reissueRequest) {
        this.reissueRequest = reissueRequest;
    }

    @Column(name = "OST_ELDERLY_MODE")
    public Boolean getElderlyMode() {
        return elderlyMode;
    }

    public void setElderlyMode(Boolean elderyMode) {
        this.elderlyMode = elderyMode;
    }

    @Column(name = "OST_NMOC_GENERATION")
    public Boolean getnMocGeneration() {
        return nMocGeneration;
    }

    public void setnMocGeneration(Boolean nMocGeneration) {
        this.nMocGeneration = nMocGeneration;
    }

    @Column(name = "OST_AMPUTATION_ANNOUNCMENT")
    public Boolean getAmputationAnnouncment() {
        return amputationAnnouncment;
    }

    public void setAmputationAnnouncment(Boolean amputationAnnouncment) {
        this.amputationAnnouncment = amputationAnnouncment;
    }

    @Column(name = "OST_NONCONFIRMING_ICAO_IMAGE")
    public Boolean getNonConfirmingIcaoImage() {
        return nonConfirmingIcaoImage;
    }

    public void setNonConfirmingIcaoImage(Boolean nonConfirmingIcaoImage) {
        this.nonConfirmingIcaoImage = nonConfirmingIcaoImage;
    }

    @Column(name = "OST_USE_SCANNER_UI")
    public Boolean getUseScannerUI() {
        return useScannerUI;
    }

    public void setUseScannerUI(Boolean useScannerUI) {
        this.useScannerUI = useScannerUI;
    }

    @Column(name = "OST_ALLOW_EDIT_BACKGROUND")
    public Boolean getAllowEditBackground() {
        return allowEditBackground;
    }

    public void setAllowEditBackground(Boolean allowEditBackground) {
        this.allowEditBackground = allowEditBackground;
    }

    @Column(name = "OST_ALW_AMPT_FING_STA_ELD")
    public Boolean getAllowAmputatedFingerStatusForElderly() {
        return allowAmputatedFingerStatusForElderly;
    }

    public void setAllowAmputatedFingerStatusForElderly(Boolean allowAmputatedFingerStatusForElderly) {
        this.allowAmputatedFingerStatusForElderly = allowAmputatedFingerStatusForElderly;
    }

    @Column(name = "OST_ALW_CHG_FING_DUR_CAP_ELD")
    public Boolean getAllowChangeFingerStatusDuringCaptureForElderly() {
        return allowChangeFingerStatusDuringCaptureForElderly;
    }

    public void setAllowChangeFingerStatusDuringCaptureForElderly(Boolean allowChangeFingerStatusDuringCaptureForElderly) {
        this.allowChangeFingerStatusDuringCaptureForElderly = allowChangeFingerStatusDuringCaptureForElderly;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "OFFICE_SETTING_FEATURE_EXTRACT"
            ,joinColumns = @JoinColumn(name = "OST_ID"
            , referencedColumnName = "OST_ID")
            , inverseJoinColumns = @JoinColumn(name = "FEI_ID"
            , referencedColumnName = "FEI_ID"))
    public Set<FeatureExtractIdsTO> getFeatureExtractIdsTO() {
        return featureExtractIdsTO;
    }

    public void setFeatureExtractIdsTO(Set<FeatureExtractIdsTO> featureExtractIdsTO) {
        this.featureExtractIdsTO = featureExtractIdsTO;
    }



    @Override
    public String toJSON() {
        String jsonObject = EmsUtil.toJSON(this);
        jsonObject = jsonObject.substring(0, jsonObject.length() - 1);

        return jsonObject;
    }

}
