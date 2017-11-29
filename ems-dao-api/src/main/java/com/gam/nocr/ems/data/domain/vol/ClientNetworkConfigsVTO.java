package com.gam.nocr.ems.data.domain.vol;

import com.gam.commons.core.data.domain.ExtEntityTO;

import java.util.List;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 11/15/17.
 */
public class ClientNetworkConfigsVTO extends ExtEntityTO{
    private List<String> ipAddressList;
    private String computerName;
    private String userName;
    private String gateway;

    public List<String> getIpAddressList() {
        return ipAddressList;
    }

    public void setIpAddressList(List<String> ipAddressList) {
        this.ipAddressList = ipAddressList;
    }

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }
}
