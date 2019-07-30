package com.gam.nocr.ems.data.domain.ws;

import java.util.List;

public class ClientWorkstationInfo {

    private List<String> ipAddressList;
    private String cpuType;
    private String osVersion;
    private String username;
    private Boolean hasDotnetFramwork45;
    private Boolean is64bitOs;
    private String dataAsJson;

    //region getter and setter

    public List<String> getIpAddressList() {
        return ipAddressList;
    }

    public void setIpAddressList(List<String> ipAddressList) {
        this.ipAddressList = ipAddressList;
    }

    public String getCpuType() {
        return cpuType;
    }

    public Boolean getHasDotnetFramwork45() {
        return hasDotnetFramwork45;
    }

    public void setHasDotnetFramwork45(Boolean hasDotnetFramwork45) {
        this.hasDotnetFramwork45 = hasDotnetFramwork45;
    }

    public Boolean getIs64bitOs() {
        return is64bitOs;
    }

    public void setIs64bitOs(Boolean is64bitOs) {
        this.is64bitOs = is64bitOs;
    }

    public void setCpuType(String cpuType) {
        this.cpuType = cpuType;
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

    public String getDataAsJson() {
        return dataAsJson;
    }

    public void setDataAsJson(String dataAsJson) {
        this.dataAsJson = dataAsJson;
    }
    //endregion
}
