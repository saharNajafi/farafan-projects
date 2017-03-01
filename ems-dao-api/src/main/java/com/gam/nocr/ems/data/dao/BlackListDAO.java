package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.BlackListTO;

import java.util.List;

/**
 * @author: Haeri (haeri@gamelectronics.com)
 */
public interface BlackListDAO extends EmsBaseDAO<BlackListTO> {

    public List<BlackListTO> findAll() throws BaseException;

    public boolean deleteBlackListItems(List<Long> blackListIds) throws BaseException;

    public List<BlackListTO> findByNid(String nid) throws BaseException;

}
