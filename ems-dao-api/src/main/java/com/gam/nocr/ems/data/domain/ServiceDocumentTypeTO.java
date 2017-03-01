package com.gam.nocr.ems.data.domain;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.util.EmsUtil;
import com.gam.nocr.ems.util.JSONable;
import flexjson.JSON;

import javax.persistence.*;

/**
 * @author <a href="mailto:haeri@gamelectronics.com.com">Nooshin Haeri</a>
 */
@Entity
@Table(name = "EMST_SERVICE_DOCUMENT_TYPE")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_SERVICE_DOC_TYPE", allocationSize = 1)
public class ServiceDocumentTypeTO extends ExtEntityTO implements JSONable {

    private DocumentTypeTO documentType;
    private Integer serviceId;

    public ServiceDocumentTypeTO() {
    }

    public ServiceDocumentTypeTO(DocumentTypeTO documentType, Integer serviceId) {
        this.documentType = documentType;
        this.serviceId = serviceId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "SDT_ID")
    public Long getId() {
        return super.getId();
    }

    @ManyToOne
    @JoinColumn(name = "SDT_DOC_TYPE_ID", nullable = false)
    @JSON(include = false)
    public DocumentTypeTO getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentTypeTO documentType) {
        this.documentType = documentType;
    }

    @Column(name = "SDT_SERVICE_ID", nullable = false)
    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
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
        if (documentType == null) {
            jsonObject += ", documentTypeId:" + documentType;
        } else {
            jsonObject += ", documentTypeId:" + documentType.getId();
        }
        jsonObject += "}";

        return jsonObject;
    }
}
