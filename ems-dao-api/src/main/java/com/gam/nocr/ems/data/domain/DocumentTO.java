package com.gam.nocr.ems.data.domain;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.util.EmsUtil;
import com.gam.nocr.ems.util.JSONable;
import flexjson.JSON;

import javax.persistence.*;

/**
 * @author: Haeri (haeri@gamelectronics.com)
 */
@Entity
@Table(name = "EMST_DOCUMENT")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_DOCUMENT", allocationSize = 1)
public class DocumentTO extends ExtEntityTO implements JSONable {

    private CitizenInfoTO citizenInfo;
    private byte[] data;
    private DocumentTypeTO type;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "DOC_ID")
    public Long getId() {
        return super.getId();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOC_CITIZEN_INFO_ID", nullable = false)
    @JSON(include = false)
    public CitizenInfoTO getCitizenInfo() {
        return citizenInfo;
    }

    public void setCitizenInfo(CitizenInfoTO citizenInfo) {
        this.citizenInfo = citizenInfo;
    }

    @Lob
    @Column(name = "DOC_DATA", nullable = false)
    @JSON(include = false)
    public byte[] getData() {
        return data == null ? data : data.clone();
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @ManyToOne
    @JoinColumn(name = "DOC_TYPE_ID", nullable = false)
    public DocumentTypeTO getType() {
        return type;
    }

    public void setType(DocumentTypeTO type) {
        this.type = type;
    }

    @Override
    public String toString() {
//		String string = "DocumentTO : {" +
//				"id = " + getId() +
//				", type = " + type;
//
//		if(citizenInfo ==  null) {
//			string += ", citizenInfo = " + citizenInfo;
//		} else {
//			string += ", citizenInfoId = " + citizenInfo.getId() +
//					", citizenInfoBirthCertSeries = " + citizenInfo.getBirthCertificateSeries() +
//					", citizenInfoFirstNameEn = " + citizenInfo.getFirstNameEnglish() +
//					", citizenInfoSurnameEn = " + citizenInfo.getSurnameEnglish();
//		}
//
//		string += " }";
//		return string;
        return toJSON();
    }

    /**
     * The method toJSON is used to convert an object to an instance of type {@link String}
     *
     * @return an instance of type {@link String}
     */
    @Override
    public String toJSON() {
        String jsonObject = EmsUtil.toJSON(this);
        jsonObject = jsonObject.substring(0, jsonObject.length() - 1);
        if (citizenInfo == null) {
            jsonObject += ", citizenInfoId:" + citizenInfo;
        } else {
            jsonObject += ", citizenInfoId:" + citizenInfo.getId();
        }
        jsonObject += "}";
        return jsonObject;
    }
}
