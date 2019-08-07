package com.gam.nocr.ems.data.domain.ws;

public class ClientWorkstationInfo {

    private String cpuInfo;
    private String osVersion;
    private Boolean hasDotnetFramwork45;
    private String memoryCapacity;
    private String ccosVersion;
    private String ipAddress;
    private String username;
    private String additionalInfoAsJson;

    //region getter and setter
    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMemoryCapacity() {
        return memoryCapacity;
    }

    public void setMemoryCapacity(String memoryCapacity) {
        this.memoryCapacity = memoryCapacity;
    }

    public String getCcosVersion() {
        return ccosVersion;
    }

    public void setCcosVersion(String ccosVersion) {
        this.ccosVersion = ccosVersion;
    }

    public String getCpuInfo() {
        return cpuInfo;
    }

    public Boolean getHasDotnetFramwork45() {
        return hasDotnetFramwork45;
    }

    public void setHasDotnetFramwork45(Boolean hasDotnetFramwork45) {
        this.hasDotnetFramwork45 = hasDotnetFramwork45;
    }

    public void setCpuInfo(String cpuInfo) {
        this.cpuInfo = cpuInfo;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAdditionalInfoAsJson() {
        return additionalInfoAsJson;
    }

    public void setAdditionalInfoAsJson(String additionalInfoAsJson) {
        this.additionalInfoAsJson = additionalInfoAsJson;
    }

    //endregion
}
