package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.nocr.ems.data.domain.vol.DocTypeVTO;

/**
 * @author: Haeri (haeri@gamelectronics.com)
 */
public interface DocTypeService extends Service {

    public Long save(DocTypeVTO vto) throws BaseException;

    public Long update(DocTypeVTO vto) throws BaseException;

    public DocTypeVTO load(Long docTypeId) throws BaseException;

    public boolean remove(String docTypeIds) throws BaseException;
}
