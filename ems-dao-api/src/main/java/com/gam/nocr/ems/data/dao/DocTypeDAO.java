package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.DocumentTypeTO;

import java.util.List;

/**
 * @author: Haeri (haeri@gamelectronics.com)
 */
public interface DocTypeDAO extends EmsBaseDAO<DocumentTypeTO> {

    public List<DocumentTypeTO> findAll() throws BaseException;

    public int deleteDocTypes(List<Long> documentTypeIds) throws BaseException;

}
