package com.gam.nocr.ems.data.dao;

import java.util.List;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.HelpTO;

public interface HelpDAO extends EmsBaseDAO<HelpTO> {

	boolean deleteHelps(List<Long> idsList) throws BaseException;


}
