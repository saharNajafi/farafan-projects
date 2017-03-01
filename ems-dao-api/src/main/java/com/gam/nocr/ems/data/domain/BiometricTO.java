package com.gam.nocr.ems.data.domain;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.data.enums.BiometricType;
import com.gam.nocr.ems.util.EmsUtil;
import com.gam.nocr.ems.util.JSONable;
import flexjson.JSON;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Haeri (haeri@gamelectronics.com)
 */
@Entity
@Table(name = "EMST_BIOMETRIC")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_BIOMETRIC", allocationSize = 1)
public class BiometricTO extends ExtEntityTO implements JSONable {

    private CitizenInfoTO citizenInfo;
    private byte[] data;
    private BiometricType type;
    private String metaData;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "BIM_ID")
    public Long getId() {
        return super.getId();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BIM_CITIZEN_INFO_ID")
    @JSON(include = false)
    public CitizenInfoTO getCitizenInfo() {
        return citizenInfo;
    }

    public void setCitizenInfo(CitizenInfoTO citizenInfo) {
        this.citizenInfo = citizenInfo;
    }

    @Lob
    @Column(name = "BIM_DATA", nullable = false)
    @JSON(include = false)
    public byte[] getData() {
        return data == null ? data : data.clone();
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "BIM_TYPE", length = 20, nullable = false)
    public BiometricType getType() {
        return type;
    }

    public void setType(BiometricType type) {
        this.type = type;
    }

    @Column(name = "BIM_META_DATA", length = 300)
    public String getMetaData() {
        return metaData;
    }

    public void setMetaData(String metaData) {
        this.metaData = metaData;
    }

    public static Map<String, String> getMetaData(String metaData) {

        if (metaData == null) {
            return null;
        }
        Map<String, String> map = new HashMap<String, String>();
        String[] arrayMetaData = metaData.split(",");
        for (int i = 0; i < arrayMetaData.length; i++) {
            if (arrayMetaData[i].contains("FingerIndex=")) {
                String[] strArray = arrayMetaData[i].split("=");
                map.put(strArray[0], strArray[1]);
            }
        }

        return map;
    }

    @Override
    public String toString() {
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
            jsonObject += "," + "\"" + "citizenInfoId:" + "\"";
        } else {
            jsonObject += "," + "citizenInfoId:" + citizenInfo.getId();
        }
        jsonObject += "}";
        return jsonObject;
    }
}