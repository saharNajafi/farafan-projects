package com.gam.nocr.ems.data.domain.ws;

import com.gam.nocr.ems.util.EmsUtil;
import flexjson.JSON;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public class BiometricWTO {
    private Long id;
    private byte[] data;
    private String type;
    private ItemWTO[] metaData;
    private byte[] symmetricKey;
    private byte[] initialVector;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JSON(include = false)
    public byte[] getData() {
        return data == null ? data : data.clone();
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JSON(include = false)
    public ItemWTO[] getMetaData() {
        return metaData == null ? metaData : metaData.clone();
    }

    public void setMetaData(ItemWTO[] metaData) {
        this.metaData = metaData;
    }

    /**
     * @return the symmetricKey
     */
    @JSON(include = false)
    public byte[] getSymmetricKey() {
        return symmetricKey == null ? symmetricKey : symmetricKey.clone();
    }

    /**
     * @param symmetricKey symmetricKey attribute, which will be used to decrypt 'data' attribute.
     */
    public void setSymmetricKey(byte[] symmetricKey) {
        this.symmetricKey = symmetricKey;
    }

    @Override
    public String toString() {
        return EmsUtil.toJSON(this);
    }

    /**
     * @return the initialVector
     */
    public byte[] getInitialVector() {
        return initialVector == null ? initialVector : initialVector.clone();
    }

    /**
     * @param initialVector the initialVector to set
     */
    public void setInitialVector(byte[] initialVector) {
        this.initialVector = initialVector;
    }
}
