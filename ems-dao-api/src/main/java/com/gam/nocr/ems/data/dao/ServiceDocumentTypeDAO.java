package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.ServiceDocumentTypeTO;

import java.util.List;

/**
 * @author <a href="mailto:haeri@gamelectronics.com.com">Nooshin Haeri</a>
 */
public interface ServiceDocumentTypeDAO extends EmsBaseDAO<ServiceDocumentTypeTO> {

    public List<ServiceDocumentTypeTO> findByServiceId(Integer serviceId) throws BaseException;

    public List<ServiceDocumentTypeTO> findByDocumentTypeId(Long docTypeId) throws BaseException;

    public void deleteByDocType(List<Long> docTypeIds) throws BaseException;

}
