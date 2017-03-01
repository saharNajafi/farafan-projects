package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.IMSBatchEnquiryTO;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public interface IMSBatchEnquiryDAO extends EmsBaseDAO<IMSBatchEnquiryTO> {

	 IMSBatchEnquiryTO findByReplyFlag(boolean replyFlag) throws BaseException;
}
