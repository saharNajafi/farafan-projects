package com.gam.nocr.ems.data.domain.ws;

import com.gam.nocr.ems.util.EmsUtil;
import flexjson.JSON;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public class AfterDeliveryServiceWTO {
    private int type;
    private byte[] data;

    public AfterDeliveryServiceWTO() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @JSON(include = false)
    public byte[] getData() {
        return data == null ? data : data.clone();
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return EmsUtil.toJSON(this);
    }
}
