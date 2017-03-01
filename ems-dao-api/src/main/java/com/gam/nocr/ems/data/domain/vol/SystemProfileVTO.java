package com.gam.nocr.ems.data.domain.vol;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.util.EmsUtil;
import flexjson.JSON;

/**
 * @author: Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public class SystemProfileVTO extends ExtEntityTO {
    private String keyName;
    private String parentKeyName;
    private String caption;
    private String value;
    private String childCount;

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    @JSON(include = false)
    public String getParentKeyName() {
        return parentKeyName;
    }

    public void setParentKeyName(String parentKeyName) {
        this.parentKeyName = parentKeyName;
    }

    @JSON(include = false)
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @JSON(include = false)
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @JSON(include = false)
    public String getChildCount() {
        return childCount;
    }

    public void setChildCount(String childCount) {
        this.childCount = childCount;
    }

    @Override
    public String toString() {
        return EmsUtil.toJSON(this);
    }
}
