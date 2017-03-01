package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.DocumentTO;
import com.gam.nocr.ems.data.domain.DocumentTypeTO;

import java.util.List;

/**
 * @author Haeri (haeri@gamelectronics.com)
 */
public interface DocumentDAO extends EmsBaseDAO<DocumentTO> {

    public List<DocumentTO> findByRequestIdAndType(Long requestId, DocumentTypeTO bioType) throws BaseException;

    public Integer removeByRequestIdAndType(Long requestId, List<DocumentTypeTO> documentTypeTOs) throws BaseException;

}
