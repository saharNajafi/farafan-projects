package com.gam.nocr.ems.data.domain;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.util.EmsUtil;

import javax.persistence.*;

/**
 * @author: Haeri (haeri@gamelectronics.com)
 */
@Entity
@Table(name = "EMST_ENCRYPTED_CARD_REQUEST")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_EMST_ENCR_CARD_REQUEST")
public class EncryptedCardRequestTO extends ExtEntityTO {

    private byte[] citizenInfo;
    private byte[] spouses;
    private byte[] children;
    private byte[] fingers;
    private byte[] faces;
    private byte[] documents;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "ECR_ID")
    public Long getId() {
        return super.getId();
    }

    @Lob
    @Column(name = "ECR_CITIZEN_INFO", nullable = false)
    public byte[] getCitizenInfo() {
        return citizenInfo == null ? citizenInfo : citizenInfo.clone();
    }

    public void setCitizenInfo(byte[] citizenInfo) {
        this.citizenInfo = citizenInfo;
    }

    @Lob
    @Column(name = "ECR_SPOUSES")
    public byte[] getSpouses() {
        return spouses == null ? spouses : spouses.clone();
    }

    public void setSpouses(byte[] spouses) {
        this.spouses = spouses;
    }

    @Lob
    @Column(name = "ECR_CHILDREN")
    public byte[] getChildren() {
        return children == null ? children : children.clone();
    }

    public void setChildren(byte[] children) {
        this.children = children;
    }

    @Lob
    @Column(name = "ECR_FINGERS")
    public byte[] getFingers() {
        return fingers == null ? fingers : fingers.clone();
    }

    public void setFingers(byte[] fingers) {
        this.fingers = fingers;
    }

    @Lob
    @Column(name = "ECR_FACES", nullable = false)
    public byte[] getFaces() {
        return faces == null ? faces : faces.clone();
    }

    public void setFaces(byte[] faces) {
        this.faces = faces;
    }

    @Lob
    @Column(name = "ECR_DOCUMENTS", nullable = false)
    public byte[] getDocuments() {
        return documents == null ? documents : documents.clone();
    }

    public void setDocuments(byte[] documents) {
        this.documents = documents;
    }

    @Override
    public String toString() {
        return EmsUtil.toJSON(this);
    }
}
