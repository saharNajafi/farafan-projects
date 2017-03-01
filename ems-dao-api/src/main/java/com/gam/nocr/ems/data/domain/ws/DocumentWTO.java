package com.gam.nocr.ems.data.domain.ws;

import com.gam.nocr.ems.util.EmsUtil;
import flexjson.JSON;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public class DocumentWTO {
    private Long id;
    private byte[] data;
    private Long documentTypeId;
    private String documentTypeName;

    public DocumentWTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(Long documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    @JSON(include = false)
    public byte[] getData() {
        return data == null ? data : data.clone();
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getDocumentTypeName() {
        return documentTypeName;
    }

    public void setDocumentTypeName(String documentTypeName) {
        this.documentTypeName = documentTypeName;
    }

    @Override
    public String toString() {
        return EmsUtil.toJSON(this);
    }
}
