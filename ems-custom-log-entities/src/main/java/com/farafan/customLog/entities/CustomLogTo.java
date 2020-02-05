package com.farafan.customLog.entities;

import com.farafan.customLog.enums.CustomLogAction;
import com.farafan.customLog.enums.CustomLogEntity;
import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.util.EmsUtil;
import flexjson.JSON;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author: Mazaher Namjoofar
 */

@Entity
@Table(name = "EMST_CUSTOME_LOG")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_CUSTOME_LOG", allocationSize = 1)
public class CustomLogTo extends ExtEntityTO {

    private Timestamp date;
    private String actor;
    private CustomLogAction action;
    private CustomLogEntity entityName;
    private String entityID;
    private String editedValue;
    private String additionalData;
    private Boolean isActionSuccess;


    public CustomLogTo() {
    }

    public CustomLogTo(Timestamp date, String actor, CustomLogAction action, CustomLogEntity entityName, String entityID, Boolean isActionSuccess, String additionalData, String editedValue) {
        this.date = date;
        this.actor = actor;
        this.action = action;
        this.entityName = entityName;
        this.entityID = entityID;
        this.isActionSuccess = isActionSuccess;
        this.additionalData = additionalData;
        this.editedValue = editedValue;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "CTL_ID")
    public Long getId() {
        return super.getId();
    }

    @Column(name = "CTL_DATE", nullable = false)
    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Column(name = "CTL_ACTOR", length = 255, nullable = false)
    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "CTL_ACTION", length = 255, nullable = false)
    public CustomLogAction getAction() {
        return action;
    }

    public void setAction(CustomLogAction action) {
        this.action = action;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "CTL_ENTITY_NAME", length = 255, nullable = false)
    public CustomLogEntity getEntityName() {
        return entityName;
    }

    public void setEntityName(CustomLogEntity entityName) {
        this.entityName = entityName;
    }

    @Column(name = "CTL_ENTITY_ID", length = 255, nullable = false)
    public String getEntityID() {
        return entityID;
    }

    public void setEntityID(String entityID) {
        this.entityID = entityID;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "CTL_EDITED_VALUE")
    public String getEditedValue() {
        return editedValue;
    }

    public void setEditedValue(String editedValue) {
        this.editedValue = editedValue;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "CTL_ADDITIONAL_DATA")
    @JSON(include = false)
    public String getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(String additionalData) {
        this.additionalData = additionalData;
    }

    @Column(name = "CTL_IS_ACTION_SUCCESS")
    @JSON(include = false)
    public Boolean getActionSuccess() {
        return isActionSuccess;
    }

    public void setActionSuccess(Boolean actionSuccess) {
        this.isActionSuccess = actionSuccess;
    }

    @Override
    public String toString() {
        return EmsUtil.toJSON(this);
    }
}
