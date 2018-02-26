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
 * @author sahar
 */
@Entity
@Table(name = "EMST_WORKSTATION_PLUGINS")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_WORKSTATION_PLUGINS", allocationSize = 1)
@NamedQueries({
        @NamedQuery(
                name = "WorkstationPluginsTO.findByWorkstationById",
                query = " SELECT w FROM WorkstationPluginsTO w" +
                        " WHERE w.workstationTO.id =:workstationId"),
        @NamedQuery(name = "WorkstationPluginsTO.findByWorkstationIdAndName",
                query = "select w from WorkstationPluginsTO w" +
                        " where w.pluginName=:pluginName and w.workstationTO=:workstationId")
})
public class WorkstationPluginsTO extends ExtEntityTO {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation

    private Long id;
    private String pluginName;
    private short state;
    private WorkstationTO workstationTO;

    public WorkstationPluginsTO() {
    }


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "WPL_ID")
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "WPL_PLUGIN_NAME")
    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    @Basic(optional = false)
    @Column(name = "WPL_STATE")
    public short getState() {
        return state;
    }

    public void setState(short state) {
        this.state = state;
    }

    @ManyToOne
    @JoinColumn(name = "WPL_WORKSTATION_ID")
    public WorkstationTO getWorkstationTO() {
        return workstationTO;
    }

    public void setWorkstationTO(WorkstationTO workstationTO) {
        this.workstationTO = workstationTO;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (super.getId() != null ? super.getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WorkstationPluginsTO)) {
            return false;
        }
        WorkstationPluginsTO other = (WorkstationPluginsTO) object;
        if ((id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gam.nocr.ems.data.domain.WorkstationPluginsTO[ wplId=" + id + " ]";
    }

}
