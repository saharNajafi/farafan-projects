package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.BatchTO;
import com.gam.nocr.ems.data.domain.vol.BatchDispatchInfoVTO;
import com.gam.nocr.ems.sharedobjects.GeneralCriteria;

import java.util.List;

/**
 * @author <a href="mailto:haeri@gamelectronics.com.com">Nooshin Haeri</a>
 */
public interface BatchDAO extends EmsBaseDAO<BatchTO> {

    public List<BatchTO> findAll() throws BaseException;

    public Integer countBatchLostTemp(GeneralCriteria criteria) throws BaseException;

    public List<BatchDispatchInfoVTO> fetchBatchLostTempList(
            GeneralCriteria criteria) throws BaseException;

    public String findCmsIdByRequestId(Long requestId) throws BaseException;
}
