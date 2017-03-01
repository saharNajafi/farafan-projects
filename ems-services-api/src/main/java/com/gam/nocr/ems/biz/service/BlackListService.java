package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.nocr.ems.data.domain.BlackListTO;

/**
 * @author: Haeri (haeri@gamelectronics.com)
 */
public interface BlackListService extends Service {

    public Long save(BlackListTO to) throws BaseException;

    public Long update(BlackListTO to) throws BaseException;

    public BlackListTO load(Long blackListId) throws BaseException;

    public boolean remove(String blackListIds) throws BaseException;

}
