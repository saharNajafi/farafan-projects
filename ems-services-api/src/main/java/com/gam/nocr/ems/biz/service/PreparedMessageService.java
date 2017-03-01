package com.gam.nocr.ems.biz.service;

/**
 * hossein
 */

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.nocr.ems.data.domain.PreparedMessageTO;

public interface PreparedMessageService extends Service {

	public void save(PreparedMessageTO to) throws BaseException;

	public void delete(String id) throws BaseException;

	public PreparedMessageTO loadById(String id)throws BaseException;
	
	public PreparedMessageTO downloadById(String id)throws BaseException;

}