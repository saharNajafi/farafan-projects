package com.gam.nocr.ems.data.dao;

import java.util.List;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.AboutTO;

public interface AboutDAO extends EmsBaseDAO<AboutTO> {

	public AboutTO getAbout() throws BaseException;
}
