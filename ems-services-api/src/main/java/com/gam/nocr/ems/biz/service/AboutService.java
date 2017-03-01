package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.nocr.ems.data.domain.AboutTO;

public interface AboutService extends Service {

	public Long save(AboutTO to) throws BaseException;

	public Long update(AboutTO to) throws BaseException;

	public AboutTO load(Long aboutId) throws BaseException;

	public AboutTO getLastAbout() throws BaseException;

}
