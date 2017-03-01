package com.gam.nocr.ems.data.mapper.tomapper;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.config.MapperExceptionCode;
import com.gam.nocr.ems.data.domain.DocumentTO;
import com.gam.nocr.ems.data.domain.DocumentTypeTO;
import com.gam.nocr.ems.data.domain.ws.DocumentWTO;

/**
 * @author Haeri (haeri@gamelectronics.com)
 */
public class DocumentMapper {

    private DocumentMapper() {
    }

    public static DocumentTO convert(DocumentWTO wto) throws BaseException {
        if (wto == null) {
            throw new BaseException(MapperExceptionCode.DOM_001, MapperExceptionCode.DOM_001_MSG);
        }
        DocumentTO to = new DocumentTO();
        to.setId(wto.getId());
        if (wto.getDocumentTypeId() != null) {
            to.setType(new DocumentTypeTO(wto.getDocumentTypeId()));
        }
        to.setData(wto.getData());
        return to;
    }

    public static DocumentWTO convert(DocumentTO to) throws BaseException {
        if (to == null) {
            throw new BaseException(MapperExceptionCode.DOM_002, MapperExceptionCode.DOM_002_MSG);
        }
        DocumentWTO wto = new DocumentWTO();
        wto.setId(to.getId());
        if (to.getType() != null) {
            wto.setDocumentTypeId(to.getType().getId());
            wto.setDocumentTypeName(to.getType().getName());
        }
        wto.setData(to.getData());
        return wto;
    }
}
