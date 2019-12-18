/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gam.nocr.ems.data.domain;

import com.gam.commons.core.data.domain.ExtEntityTO;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
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
    private String cpuInfo;
    private String osVersion;
    private String hasDotnetFramwork45;
    private String memoryCapacity;
    private String ccosVersion;
    private String ipAddress;
    private String username;
    private String additionalInfoAsJson;
    private Boolean gatherState;
    private Date lastModifiedDate;
    private WorkstationTO workstation;

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

    @NotNull
    @Column(name = "WSI_IP_ADDRESS")
    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @NotNull
    @Column(name = "WSI_CPU_INFO")
    public String getCpuInfo() {
        return cpuInfo;
    }

    public void setCpuInfo(String cpuInfo) {
        this.cpuInfo = cpuInfo;
    }

    @NotNull
    @Column(name = "WSI_CCOS_VERSION")
    public String getCcosVersion() {
        return ccosVersion;
    }

    public void setCcosVersion(String ccosVersion) {
        this.ccosVersion = ccosVersion;
    }

    @NotNull
    @Column(name = "WSI_OS_VERSION")
    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    @NotNull
    @Column(name = "WSI_MEMORY_CAPACITY")
    public String getMemoryCapacity() {
        return memoryCapacity;
    }

    public void setMemoryCapacity(String memoryCapacity) {
        this.memoryCapacity = memoryCapacity;
    }

    @NotNull
    @Column(name = "WSI_USERNAME")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NotNull
    @Column(name = "WSI_HAS_DOTNET_FRAMWORK45")
    public String getHasDotnetFramwork45() {
        return hasDotnetFramwork45;
    }

    public void setHasDotnetFramwork45(String hasDotnetFramwork45) {
        this.hasDotnetFramwork45 = hasDotnetFramwork45;
    }

    @OneToOne
    @JoinColumn(name = "WSI_WORKSTATION_ID",nullable = false)
    public WorkstationTO getWorkstation() {
        return workstation;
    }

    public void setWorkstation(WorkstationTO workstation) {
        this.workstation = workstation;
    }

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "WSI_LAST_MODIFIED_DATE")
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @NotNull
    @Basic(optional = false)
    @Column(name = "WSI_GATHER_STATE")
    public Boolean getGatherState() {
        return gatherState;
    }

    public void setGatherState(Boolean gatherState) {
        this.gatherState = gatherState;
    }

    @NotNull
    @Lob
    @Column(name = "WSI_ADDITIONAL_INFO_AS_JSON")
    public String getAdditionalInfoAsJson() {
        return additionalInfoAsJson;
    }

    public void setAdditionalInfoAsJson(String additionalInfoAsJson) {
        this.additionalInfoAsJson = additionalInfoAsJson;
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
