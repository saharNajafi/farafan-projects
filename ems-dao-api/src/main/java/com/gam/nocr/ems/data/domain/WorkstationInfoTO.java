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
@NamedQueries({
        @NamedQuery(
                name = "WorkstationInfoTO.findAll",
                query = "SELECT e FROM WorkstationInfoTO e")
        , @NamedQuery(
        name = "WorkstationInfoTO.findById",
        query = "SELECT e FROM WorkstationInfoTO e WHERE e.id =:id")
        , @NamedQuery(
        name = "WorkstationInfoTO.findByWorkstationId",
        query = "SELECT e FROM WorkstationInfoTO e WHERE e.workstationTO.id =:workstationId")
        , @NamedQuery(
        name = "WorkstationInfoTO.findByMacAddressList",
        query = "SELECT e FROM WorkstationInfoTO e WHERE e.macAddressList = :wsiMacAddressList")
        , @NamedQuery(
        name = "WorkstationInfoTO.findByIpAddressList",
        query = "SELECT e FROM WorkstationInfoTO e WHERE e.ipAddressList = :wsiIpAddressList")
        , @NamedQuery(
        name = "WorkstationInfoTO.findByCpuType",
        query = "SELECT e FROM WorkstationInfoTO e WHERE e.cpuType = :wsiCpuType")
        , @NamedQuery(
        name = "WorkstationInfoTO.findByRamCapacity",
        query = "SELECT e FROM WorkstationInfoTO e WHERE e.ramCapacity = :wsiRamCapacity")
        , @NamedQuery(
        name = "WorkstationInfoTO.findByOsVersion",
        query = "SELECT e FROM WorkstationInfoTO e WHERE e.osVersion = :wsiOsVersion")
        , @NamedQuery(
        name = "WorkstationInfoTO.findByComputerName",
        query = "SELECT e FROM WorkstationInfoTO e WHERE e.computerName = :wsiComputerName")
        , @NamedQuery(
        name = "WorkstationInfoTO.findByUsername",
        query = "SELECT e FROM WorkstationInfoTO e WHERE e.username = :wsiUsername")
        , @NamedQuery(
        name = "WorkstationInfoTO.findByGateway",
        query = "SELECT e FROM WorkstationInfoTO e WHERE e.gateway = :wsiGateway")
        , @NamedQuery(
        name = "WorkstationInfoTO.findByHasDotnetFramwork45",
        query = "SELECT e FROM WorkstationInfoTO e WHERE e.hasDotnetFramwork45 = :wsiHasDotnetFramwork45")
        , @NamedQuery(
        name = "WorkstationInfoTO.findByIs64bitOs",
        query = "SELECT e FROM WorkstationInfoTO e WHERE e.is64bitOs = :wsiIs64bitOs")
})
public class WorkstationInfoTO extends ExtEntityTO {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "WSI_ID")
    private Long id;
    @Column(name = "WSI_MAC_ADDRESS_LIST")
    private String macAddressList;
    @Column(name = "WSI_IP_ADDRESS_LIST")
    private String ipAddressList;
    @Column(name = "WSI_CPU_TYPE")
    private String cpuType;
    @Column(name = "WSI_RAM_CAPACITY")
    private Integer ramCapacity;
    @Column(name = "WSI_OS_VERSION")
    private String osVersion;
    @Column(name = "WSI_COMPUTER_NAME")
    private String computerName;
    @Column(name = "WSI_USERNAME")
    private String username;
    @Column(name = "WSI_GATEWAY")
    private String gateway;
    @Column(name = "WSI_HAS_DOTNET_FRAMWORK45")
    private Short hasDotnetFramwork45;
    @Column(name = "WSI_IS_64BIT_OS")
    private Short is64bitOs;
    @MapsId
    @JoinColumn(name = "WSI_WORKSTATION_ID", referencedColumnName = "WST_ID")
    @OneToOne
    private WorkstationTO workstationTO;
    @Basic(optional = false)
    @Column(name = "GATHER_SATE")
    private short gatherSate;
    public WorkstationInfoTO() {
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getMacAddressList() {
        return macAddressList;
    }

    public void setMacAddressList(String macAddressList) {
        this.macAddressList = macAddressList;
    }

    public String getIpAddressList() {
        return ipAddressList;
    }

    public void setIpAddressList(String ipAddressList) {
        this.ipAddressList = ipAddressList;
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

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public Short getHasDotnetFramwork45() {
        return hasDotnetFramwork45;
    }

    public void setHasDotnetFramwork45(Short hasDotnetFramwork45) {
        this.hasDotnetFramwork45 = hasDotnetFramwork45;
    }

    public Short getIs64bitOs() {
        return is64bitOs;
    }

    public void setIs64bitOs(Short is64bitOs) {
        this.is64bitOs = is64bitOs;
    }

    public WorkstationTO getWorkstationTO() {
        return workstationTO;
    }

    public void setWorkstationTO(WorkstationTO workstationTO) {
        this.workstationTO = workstationTO;
    }

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
