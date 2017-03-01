package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.nocr.ems.data.domain.AboutTO;
import com.gam.nocr.ems.data.domain.HelpTO;
import com.gam.nocr.ems.data.domain.vol.HelpVTO;

public interface HelpService extends Service {

	public Long save(HelpVTO to) throws BaseException;

	public Long update(HelpTO to) throws BaseException;

	public AboutTO load(Long HelpId) throws BaseException;

	public HelpTO downloadHelpFile(long fileId) throws BaseException;

	public boolean remove(String ids) throws BaseException;


}
