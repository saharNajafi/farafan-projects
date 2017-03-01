/**
 *
 */
package com.gam.nocr.ems.data.domain;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.data.enums.BusinessLogAction;
import com.gam.nocr.ems.data.enums.BusinessLogActionAttitude;
import com.gam.nocr.ems.data.enums.BusinessLogEntity;
import com.gam.nocr.ems.util.EmsUtil;
import flexjson.JSON;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author Sina Golesorkhi
 */
@Entity
@Table(name = "EMST_BIZLOG")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_BIZLOG", allocationSize = 1)
public class BusinessLogTO extends ExtEntityTO {

    private Timestamp date;
    private String actor;
    private BusinessLogAction action;
    private BusinessLogEntity entityName;
    private String entityID;
    private String editedValue;
    private String additionalData;
    private byte[] digest;

    private String actionNameStr;
    private String entityNameStr;
    private String status;
    private CertificateTO certificateTO;
    private BusinessLogActionAttitude actionAttitude;

    public BusinessLogTO() {
    }

    public BusinessLogTO(Timestamp date, String actor, BusinessLogAction action, BusinessLogEntity entityName, String entityID, String additionalData) {
        this.date = date;
        this.actor = actor;
        this.action = action;
        this.entityName = entityName;
        this.entityID = entityID;
        this.additionalData = additionalData;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "BZL_ID")
    public Long getId() {
        return super.getId();
    }

    @Column(name = "BZL_DATE", nullable = false)
    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Column(name = "BZL_ACTOR", length = 255, nullable = false)
    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "BZL_ACTION", length = 255, nullable = false)
    public BusinessLogAction getAction() {
        return action;
    }

    public void setAction(BusinessLogAction action) {
        this.action = action;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "BZL_ENTITY_NAME", length = 255, nullable = false)
    public BusinessLogEntity getEntityName() {
        return entityName;
    }

    public void setEntityName(BusinessLogEntity entityName) {
        this.entityName = entityName;
    }

    @Column(name = "BZL_ENTITY_ID", length = 255, nullable = false)
    public String getEntityID() {
        return entityID;
    }

    public void setEntityID(String entityID) {
        this.entityID = entityID;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "BZL_EDITED_VALUE")
    public String getEditedValue() {
        return editedValue;
    }

    public void setEditedValue(String editedValue) {
        this.editedValue = editedValue;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "BZL_ADDITIONAL_DATA")
    @JSON(include = false)
    public String getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(String additionalData) {
        this.additionalData = additionalData;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "BZL_DIGEST", nullable = false)
    @JSON(include = false)
    public byte[] getDigest() {
        return digest == null ? digest : digest.clone();
    }

    public void setDigest(byte[] digest) {
        this.digest = digest;
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

    /**
     * @return the certificateTO
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "BZL_CRT_ID", nullable = true)
    @JSON(include = false)
    public CertificateTO getCertificateTO() {
        return certificateTO;
    }

    /**
     * @param certificateTO the certificateTO to set
     */
    public void setCertificateTO(CertificateTO certificateTO) {
        this.certificateTO = certificateTO;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "BZL_ACTION_ATTITUDE")
    @JSON(include = false)
    public BusinessLogActionAttitude getActionAttitude() {
        return actionAttitude;
    }

    public void setActionAttitude(BusinessLogActionAttitude actionAttitude) {
        this.actionAttitude = actionAttitude;
    }

    @Override
    public String toString() {
        return EmsUtil.toJSON(this);
    }
}
