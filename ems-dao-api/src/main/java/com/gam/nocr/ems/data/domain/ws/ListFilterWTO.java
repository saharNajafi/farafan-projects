package com.gam.nocr.ems.data.domain.ws;

import java.io.Serializable;

/**
 * @author: Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public class ListFilterWTO implements Serializable {
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String value;
}
