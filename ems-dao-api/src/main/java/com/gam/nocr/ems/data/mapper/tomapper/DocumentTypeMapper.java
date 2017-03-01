package com.gam.nocr.ems.data.mapper.tomapper;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.DocumentTypeTO;
import com.gam.nocr.ems.data.domain.ws.DocumentTypeWTO;

/**
 * @author <a href="mailto:haeri@gamelectronics.com.com">Nooshin Haeri</a>
 */
public class DocumentTypeMapper {

    public static DocumentTypeTO convert(DocumentTypeWTO wto) throws BaseException {
        DocumentTypeTO to = new DocumentTypeTO();
        to.setId(wto.getDocTypeId());
        to.setName(wto.getName());
        return to;
    }

    public static DocumentTypeWTO convert(DocumentTypeTO to) throws BaseException {
        DocumentTypeWTO wto = new DocumentTypeWTO();
        wto.setDocTypeId(to.getId());
        wto.setName(to.getName());
        return wto;
    }
}
