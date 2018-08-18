package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
//import com.gam.nocr.ems.data.domain.ws.EMKSCardMoCKeysWTO;
import com.gam.nocr.ems.data.domain.ws.EMKSDataResultWTO;
import com.gam.nocr.ems.data.domain.ws.EMKSDataWTO;

/**
 * @author <a href="mailto:saadat@gamelectronics.com.com">Alireza Saadat</a>
 */
public interface EmksService extends Service {

	public EMKSDataResultWTO getNIDCardPINs(EMKSDataWTO emksDataWTO, Long requestID)
			throws BaseException;

/*	public EMKSCardMoCKeysWTO getNIDCardMoCKeys(EMKSDataWTO emksDataWTO, Long requestID)
			throws BaseException;*/

	public String getSigniture(String str) throws BaseException;

}
