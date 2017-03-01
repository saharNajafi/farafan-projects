package com.gam.nocr.ems.data.domain.vol;

import com.gam.nocr.ems.util.EmsUtil;
import flexjson.JSON;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public class CardApplicationInfoVTO {

    private String description;
    private String id;
    private String name;
    private String reason;
    private int status;

    public CardApplicationInfoVTO(
            String description,
            String id,
            String name,
            String reason,
            int status) {
        this.description = description;
        this.id = id;
        this.name = name;
        this.reason = reason;
        this.status = status;
    }

    @JSON(include = false)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JSON(include = false)
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String toString() {
        return EmsUtil.toJSON(this);
    }
}
