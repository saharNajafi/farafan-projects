package com.gam.nocr.ems.data.domain.vol;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.util.EmsUtil;

import flexjson.JSON;

/**
 * <p> TODO -- Explain this class </p>
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class EnrollmentOfficeVTO extends ExtEntityTO {

    private String name;
    private String address;
    private String zip;
    private String phone;
    private String fax;
    private String code;
    private String codeNum;
    private Long parentId;
    private String parentName;
    private Long locId;
    private String locName;
    private Long area;
    private Long rateId;
    private String rate;
    private boolean ssl;

    private String managerMobile;
    private String managerPhone;
    private Long managerId;
    private String managerName;

    private Long substitutionOfficeId;
    private String substitutionOfficeName;

    private Long tokenId;
    private String tokenState;

    private Float workingHoursStart;
    private Float workingHoursFinish;

    private String officeType;
    private String officeDeliver;
    private Long superiorOfficeId;
    private String superiorOfficeName;
    private String fqdn;
    private String khosusiType;
    private String provinceName;
    private String calenderType;
    private String fingerScanSingleMode;
    private String uploadPhoto;
    private String reIssueRequest;
    private String disabilityMode;
    private String elderlyMode;
    private String nMocGeneration;
    private String amputationAnnouncment;
    private String useScannerUI;
    private String allowEditBackground;
    private Long depPhoneNumber;
    private Boolean thursdayMorningActive;
    private Boolean thursdayEveningActive;
    private Boolean fridayMorningActive;
    private Boolean fridayEveningActive;
    private Boolean singleStageOnly;
    private Boolean hasStair;
    private Boolean hasElevator;
    private Boolean hasPortabilityEquipment;
    private Boolean isActive;
    private Boolean isPostNeeded;
    private String postDestinationCode;
    private String allowAmputatedFinger;
    private String allowChangeFinger;
    private String featureExtractName;
    private String featureExtractVersion;
    private Long ostId;
    private String officeSettingType;
    private Long feiId;
    private Long fevId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JSON(include = false)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @JSON(include = false)
    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    @JSON(include = false)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @JSON(include = false)
    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeNum() {
        return codeNum;
    }

    public void setCodeNum(String codeNum) {
        this.codeNum = codeNum;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Long getLocId() {
        return locId;
    }

    public void setLocId(Long locId) {
        this.locId = locId;
    }

    public String getLocName() {
        return locName;
    }

    public void setLocName(String locName) {
        this.locName = locName;
    }

    @JSON(include = false)
    public Long getArea() {
        return area;
    }

    public void setArea(Long area) {
        this.area = area;
    }

    @JSON(include = false)
    public Long getRateId() {
        return rateId;
    }

    public void setRateId(Long rateId) {
        this.rateId = rateId;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public boolean getSsl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    @JSON(include = false)
    public String getManagerMobile() {
        return managerMobile;
    }

    public void setManagerMobile(String managerMobile) {
        this.managerMobile = managerMobile;
    }

    @JSON(include = false)
    public String getManagerPhone() {
        return managerPhone;
    }

    public void setManagerPhone(String managerPhone) {
        this.managerPhone = managerPhone;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    @JSON(include = false)
    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    @JSON(include = false)
    public Long getTokenId() {
        return tokenId;
    }

    public void setTokenId(Long tokenId) {
        this.tokenId = tokenId;
    }

    @JSON(include = false)
    public String getTokenState() {
        return tokenState;
    }

    public void setTokenState(String tokenState) {
        this.tokenState = tokenState;
    }

    @JSON(include = false)
    public Long getSubstitutionOfficeId() {
        return substitutionOfficeId;
    }

    public void setSubstitutionOfficeId(Long substitutionOfficeId) {
        this.substitutionOfficeId = substitutionOfficeId;
    }

    @JSON(include = false)
    public String getSubstitutionOfficeName() {
        return substitutionOfficeName;
    }

    public void setSubstitutionOfficeName(String substitutionOfficeName) {
        this.substitutionOfficeName = substitutionOfficeName;
    }

    @JSON(include = false)
    public Float getWorkingHoursStart() {
        return workingHoursStart;
    }

    public void setWorkingHoursStart(Float workingHoursStart) {
        this.workingHoursStart = workingHoursStart;
    }

    @JSON(include = false)
    public Float getWorkingHoursFinish() {
        return workingHoursFinish;
    }

    public void setWorkingHoursFinish(Float workingHoursFinish) {
        this.workingHoursFinish = workingHoursFinish;
    }

    @JSON(include = false)
    public String getOfficeType() {
        return officeType;
    }

    public void setOfficeType(String officeType) {
        this.officeType = officeType;
    }

    @JSON(include = false)
    public Long getSuperiorOfficeId() {
        return superiorOfficeId;
    }

    public void setSuperiorOfficeId(Long superiorOfficeId) {
        this.superiorOfficeId = superiorOfficeId;
    }

    @JSON(include = false)
    public String getSuperiorOfficeName() {
        return superiorOfficeName;
    }

    public void setSuperiorOfficeName(String superiorOfficeName) {
        this.superiorOfficeName = superiorOfficeName;
    }

    @JSON(include = false)
    public String getFqdn() {
        return fqdn;
    }

    public void setFqdn(String fqdn) {
        this.fqdn = fqdn;
    }

    public String toString() {
        return EmsUtil.toJSON(this);
    }   

	@JSON(include = false)
	public String getOfficeDeliver() {
		return officeDeliver;
	}

	public void setOfficeDeliver(String officeDeliver) {
		this.officeDeliver = officeDeliver;
	}

	public String getKhosusiType() {
		return khosusiType;
	}

	public void setKhosusiType(String khosusiType) {
		this.khosusiType = khosusiType;
	}


	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCalenderType() {
		return calenderType;
	}

	public void setCalenderType(String calenderType) {
		this.calenderType = calenderType;
	}

	public String getOfficeSettingType() {
		return officeSettingType;
	}

	public void setOfficeSettingType(String officeSettingType) {
		this.officeSettingType = officeSettingType;
	}

	public String getFingerScanSingleMode() {
		return fingerScanSingleMode;
	}

	public void setFingerScanSingleMode(String fingerScanSingleMode) {
		this.fingerScanSingleMode = fingerScanSingleMode;
	}

	public String getUploadPhoto() {
		return uploadPhoto;
	}

	public void setUploadPhoto(String uploadPhoto) {
		this.uploadPhoto = uploadPhoto;
	}

	public String getReIssueRequest() {
		return reIssueRequest;
	}

	public void setReIssueRequest(String reIssueRequest) {
		this.reIssueRequest = reIssueRequest;
	}

	public String getDisabilityMode() {
		return disabilityMode;
	}

	public void setDisabilityMode(String disabilityMode) {
		this.disabilityMode = disabilityMode;
	}

	public String getElderlyMode() {
		return elderlyMode;
	}

	public void setElderlyMode(String elderlyMode) {
		this.elderlyMode = elderlyMode;
	}

	public String getnMocGeneration() {
		return nMocGeneration;
	}

	public void setnMocGeneration(String nMocGeneration) {
		this.nMocGeneration = nMocGeneration;
	}

	public String getAmputationAnnouncment() {
		return amputationAnnouncment;
	}

	public void setAmputationAnnouncment(String amputationAnnouncment) {
		this.amputationAnnouncment = amputationAnnouncment;
	}

	public String getUseScannerUI() {
		return useScannerUI;
	}

	public void setUseScannerUI(String useScannerUI) {
		this.useScannerUI = useScannerUI;
	}

	public String getAllowEditBackground() {
		return allowEditBackground;
	}

	public void setAllowEditBackground(String allowEditBackground) {
		this.allowEditBackground = allowEditBackground;
	}

    public Long getDepPhoneNumber() {
        return depPhoneNumber;
    }

    public void setDepPhoneNumber(Long depPhoneNumber) {
        this.depPhoneNumber = depPhoneNumber;
    }

    public Boolean getThursdayMorningActive() {
        return thursdayMorningActive;
    }


    public void setThursdayMorningActive(Boolean thursdayMorningActive) {
        this.thursdayMorningActive = thursdayMorningActive;
    }

    public Boolean getThursdayEveningActive() {
        return thursdayEveningActive;
    }

    public void setThursdayEveningActive(Boolean thursdayEveningActive) {
        this.thursdayEveningActive = thursdayEveningActive;
    }

    public Boolean getFridayMorningActive() {
        return fridayMorningActive;
    }

    public void setFridayMorningActive(Boolean fridayMorningActive) {
        this.fridayMorningActive = fridayMorningActive;
    }

    public Boolean getFridayEveningActive() {
        return fridayEveningActive;
    }

    public void setFridayEveningActive(Boolean fridayEveningActive) {
        this.fridayEveningActive = fridayEveningActive;
    }

    public Boolean getSingleStageOnly() {
        return singleStageOnly;
    }

    public void setSingleStageOnly(Boolean singleStageOnly) {
        this.singleStageOnly = singleStageOnly;
    }

    public Boolean getHasStair() {
        return hasStair;
    }

    public void setHasStair(Boolean hasStair) {
        this.hasStair = hasStair;
    }

    public Boolean getHasElevator() {
        return hasElevator;
    }

    public void setHasElevator(Boolean hasElevator) {
        this.hasElevator = hasElevator;
    }

    public Boolean getHasPortabilityEquipment() {
        return hasPortabilityEquipment;
    }

    public void setHasPortabilityEquipment(Boolean hasPortabilityEquipment) {
        this.hasPortabilityEquipment = hasPortabilityEquipment;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getPostNeeded() {
        return isPostNeeded;
    }

    public void setPostNeeded(Boolean postNeeded) {
        isPostNeeded = postNeeded;
    }

    public String getPostDestinationCode() {
        return postDestinationCode;
    }

    public void setPostDestinationCode(String postDestinationCode) {
        this.postDestinationCode = postDestinationCode;
    }

    public String getAllowAmputatedFinger() {
        return allowAmputatedFinger;
    }

    public void setAllowAmputatedFinger(String allowAmputatedFinger) {
        this.allowAmputatedFinger = allowAmputatedFinger;
    }

    public String getAllowChangeFinger() {
        return allowChangeFinger;
    }

    public void setAllowChangeFinger(String allowChangeFinger) {
        this.allowChangeFinger = allowChangeFinger;
    }

    public String getFeatureExtractName() {
        return featureExtractName;
    }

    public void setFeatureExtractName(String featureExtractName) {
        this.featureExtractName = featureExtractName;
    }

    public String getFeatureExtractVersion() {
        return featureExtractVersion;
    }

    public void setFeatureExtractVersion(String featureExtractVersion) {
        this.featureExtractVersion = featureExtractVersion;
    }

    public Long getOstId() {
        return ostId;
    }

    public void setOstId(Long ostId) {
        this.ostId = ostId;
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
