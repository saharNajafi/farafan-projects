package com.gam.nocr.ems.data.domain.ws;

/**
 * @author: Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public class ItemWTO {
    private String key;
    private String value;

    public ItemWTO() {
    }

    public ItemWTO(String key, String value) {
        this.key = key;
        this.value = value;
    }

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
}
