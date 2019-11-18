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
//@Table(name = "EMST_CUSTOME_LOG", schema = "EmsOraclePUSecond")
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

    private String actionNameStr;
    private String entityNameStr;
    private String status;
    private Boolean isActionSuccess;

    public CustomLogTo() {
    }

    public CustomLogTo(Timestamp date, String actor, CustomLogAction action, CustomLogEntity entityName, String entityID, String additionalData) {
        this.date = date;
        this.actor = actor;
        this.action = action;
        this.entityName = entityName;
        this.entityID = entityID;
        this.additionalData = additionalData;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "CUSTOM_ID")
    public Long getId() {
        return super.getId();
    }

    @Column(name = "CUSTOM_DATE", nullable = false)
    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Column(name = "CUSTOM_ACTOR", length = 255, nullable = false)
    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "CUSTOM_ACTION", length = 255, nullable = false)
    public CustomLogAction getAction() {
        return action;
    }

    public void setAction(CustomLogAction action) {
        this.action = action;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "CUSTOM_ENTITY_NAME", length = 255, nullable = false)
    public CustomLogEntity getEntityName() {
        return entityName;
    }

    public void setEntityName(CustomLogEntity entityName) {
        this.entityName = entityName;
    }

    @Column(name = "CUSTOM_ENTITY_ID", length = 255, nullable = false)
    public String getEntityID() {
        return entityID;
    }

    public void setEntityID(String entityID) {
        this.entityID = entityID;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "CUSTOM_EDITED_VALUE")
    public String getEditedValue() {
        return editedValue;
    }

    public void setEditedValue(String editedValue) {
        this.editedValue = editedValue;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "CUSTOM_ADDITIONAL_DATA")
    @JSON(include = false)
    public String getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(String additionalData) {
        this.additionalData = additionalData;
    }

    @Transient
    @JSON(include = false)
    public String getActionNameStr() {
        return actionNameStr;
    }

    public void setActionNameStr(String actionNameStr) {
        this.actionNameStr = actionNameStr;
    }

    @Transient
    @JSON(include = false)
    public String getEntityNameStr() {
        return entityNameStr;
    }

    public void setEntityNameStr(String entityNameStr) {
        this.entityNameStr = entityNameStr;
    }

    @Transient
    @JSON(include = false)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "CUSTOM_IS_ACTION_SUCCESS")
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
