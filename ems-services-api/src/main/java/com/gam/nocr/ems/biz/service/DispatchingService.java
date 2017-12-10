package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.data.domain.CardTO;
import com.gam.nocr.ems.data.domain.ws.DispatchInfoWTO;
import com.gam.nocr.ems.sharedobjects.GeneralCriteria;

import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 * @author: Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public interface DispatchingService extends Service {

	public String batchProduction(String batchId, List<CardTO> cards) throws BaseException;

	public String batchProduction(String batchId, List<CardTO> cards,String postalTrackingCode) throws BaseException;

	public void updateBatchPostalTrackingCode(String batchId,String postalTrackingCode) throws BaseException;

	public String boxShipped(String boxId, List<String> batchIds) throws BaseException;

	public String cardProductionError(String requestID, String errorCode, String description) throws BaseException;

	public void itemLost(String ids, String detailIds, String cardIds) throws BaseException;

	public void itemFound(String ids, String detailIds, String cardIds) throws BaseException;

	public void itemReceived(String ids, String detailIds, String cardIds) throws BaseException;

	public void itemNotReceived(String ids, String detailIds, String cardIds) throws BaseException;

	public void itemSent(String ids, String detailIds) throws BaseException;

	public void undoSend(String ids, String detailIds) throws BaseException;

	public void backToInitialState(String ids, String detailIds, String cardIds) throws BaseException;

	public List<DispatchInfoWTO> fetchBatchDispatchList(GeneralCriteria criteria,UserProfileTO userProfileTO) throws BaseException;

	public Integer countBatchDispatchList(HashMap parameters,UserProfileTO userProfileTO)throws BaseException;
}
