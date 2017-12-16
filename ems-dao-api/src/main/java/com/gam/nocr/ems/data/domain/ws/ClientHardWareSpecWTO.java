package com.gam.nocr.ems.data.domain.ws;

import com.gam.commons.core.data.domain.ExtEntityTO;

import java.util.List;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 11/15/17.
 */
public class ClientHardWareSpecWTO extends ExtEntityTO{
    private List<String> macAddressList;
    private String cpuType;
    private Integer ramCapacity;

    public List<String> getMacAddressList() {
        return macAddressList;
    }

    public void setMacAddressList(List<String> macAddressList) {
        this.macAddressList = macAddressList;
    }

    public String getCpuType() {
        return cpuType;
    }

    public void setCpuType(String cpuType) {
        this.cpuType = cpuType;
    }

    public Integer getRamCapacity() {
        return ramCapacity;
    }

    public void setRamCapacity(Integer ramCapacity) {
        this.ramCapacity = ramCapacity;
    }
}
