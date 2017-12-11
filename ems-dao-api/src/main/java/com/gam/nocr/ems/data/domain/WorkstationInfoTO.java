/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gam.nocr.ems.data.domain;

import com.gam.commons.core.data.domain.ExtEntityTO;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author sahar
 */
@Entity
@Table(name = "EMST_WORKSTATION_INFO")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_WORKSTATION_INFO", allocationSize = 1)
@NamedQueries({
         @NamedQuery(
        name = "WorkstationInfoTO.findByWorkstationId",
        query = "SELECT e FROM WorkstationInfoTO e" +
                " WHERE e.workstation.id =:workstationId")
})
public class WorkstationInfoTO extends ExtEntityTO {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation

    private Long id;
    private String macAddressList;
    private String ipAddressList;
    private String cpuType;
    private Integer ramCapacity;
    private String osVersion;
    private String computerName;
    private String username;
    private String gateway;
    private Short hasDotnetFramwork45;
    private Short is64bitOs;
    private WorkstationTO workstation;
    private short gatherSate;

    public WorkstationInfoTO() {
    }

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "WSI_ID")
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "WSI_MAC_ADDRESS_LIST")
    public String getMacAddressList() {
        return macAddressList;
    }

    public void setMacAddressList(String macAddressList) {
        this.macAddressList = macAddressList;
    }

    @Column(name = "WSI_IP_ADDRESS_LIST")
    public String getIpAddressList() {
        return ipAddressList;
    }

    public void setIpAddressList(String ipAddressList) {
        this.ipAddressList = ipAddressList;
    }

    @Column(name = "WSI_CPU_TYPE")
    public String getCpuType() {
        return cpuType;
    }

    public void setCpuType(String cpuType) {
        this.cpuType = cpuType;
    }

    @Column(name = "WSI_RAM_CAPACITY")
    public Integer getRamCapacity() {
        return ramCapacity;
    }

    public void setRamCapacity(Integer ramCapacity) {
        this.ramCapacity = ramCapacity;
    }

    @Column(name = "WSI_OS_VERSION")
    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    @Column(name = "WSI_COMPUTER_NAME")
    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    @Column(name = "WSI_USERNAME")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "WSI_GATEWAY")
    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    @Column(name = "WSI_HAS_DOTNET_FRAMWORK45")
    public Short getHasDotnetFramwork45() {
        return hasDotnetFramwork45;
    }

    public void setHasDotnetFramwork45(Short hasDotnetFramwork45) {
        this.hasDotnetFramwork45 = hasDotnetFramwork45;
    }

    @Column(name = "WSI_IS_64BIT_OS")
    public Short getIs64bitOs() {
        return is64bitOs;
    }

    public void setIs64bitOs(Short is64bitOs) {
        this.is64bitOs = is64bitOs;
    }

    @OneToOne
    @JoinColumn(name = "WSI_WORKSTATION_ID")
    public WorkstationTO getWorkstation() {
        return workstation;
    }

    public void setWorkstation(WorkstationTO workstation) {
        this.workstation = workstation;
    }

    @Basic(optional = false)
    @Column(name = "WSI_GATHER_SATE")
    public short getGatherSate() {
        return gatherSate;
    }

    public void setGatherSate(short gatherSate) {
        this.gatherSate = gatherSate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WorkstationInfoTO)) {
            return false;
        }
        WorkstationInfoTO other = (WorkstationInfoTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gam.nocr.ems.data.domain.WorkstationInfoTO[ wsiId=" + id + " ]";
    }
    
}
