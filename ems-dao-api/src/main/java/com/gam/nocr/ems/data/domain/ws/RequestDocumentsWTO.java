package com.gam.nocr.ems.data.domain.ws;

import java.io.Serializable;

public class RequestDocumentsWTO implements Serializable {

    private Long requestId;
    private DocumentWTO[] documentWTO;

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public DocumentWTO[] getDocumentWTO() {
        return documentWTO;
    }

    public void setDocumentWTO(DocumentWTO[] documentWTO) {
        this.documentWTO = documentWTO;
    }
}
