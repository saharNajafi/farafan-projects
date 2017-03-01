package com.gam.nocr.ems.data.domain.vol;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public class ReportFileVTO {

    private String caption;
    private String type;
    private byte[] data;

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getData() {
        return data == null ? data : data.clone();
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
