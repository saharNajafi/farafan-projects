package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.PhotoVipTO;

public interface PhotoVipDAO extends EmsBaseDAO<PhotoVipTO> {

	PhotoVipTO getPhotoVip(Long cardRquestId) throws BaseException;
}
