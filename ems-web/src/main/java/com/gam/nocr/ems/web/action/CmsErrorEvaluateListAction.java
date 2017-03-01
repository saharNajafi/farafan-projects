package com.gam.nocr.ems.web.action;

import gampooya.tools.security.BusinessSecurityException;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.web.struts2.extJsController.ActionException;
import com.gam.commons.core.web.struts2.extJsController.ListControllerImpl;
import com.gam.nocr.ems.biz.delegator.CardRequestDelegator;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.vol.AccessProductionVTO;
import com.gam.nocr.ems.data.domain.vol.CmsErrorEvaluateVTO;
import com.gam.nocr.ems.data.enums.CardRequestState;

/**
 * Main action class to handle all requests from card requests list
 * 
 * @author <a href="mailto:moghaddam@gamelectronics.com">Ehsan Zaery
 *         Moghaddam</a>
 */
public class CmsErrorEvaluateListAction extends
		ListControllerImpl<CmsErrorEvaluateVTO> {

	private static final Logger logger = BaseLog
			.getLogger(CmsErrorEvaluateListAction.class);

	private CmsErrorEvaluateVTO record;
	private AccessProductionVTO accessProduction;
	public void setAccessProduction(AccessProductionVTO accessProduction) {
		this.accessProduction = accessProduction;
	}
	public AccessProductionVTO getAccessProduction() {
		return accessProduction;
	}
	public CmsErrorEvaluateVTO getRecord() {
		return record;
	}

	public void setRecord(CmsErrorEvaluateVTO record) {
		this.record = record;
	}

	@Override
	public void setRecords(List<CmsErrorEvaluateVTO> records) {
	}

	// Anbari
	public String doRepealAction() throws BaseException {

				CardRequestDelegator cardRequestDelegator = new CardRequestDelegator();
				try {
					List<Long> cardRequestIds = new ArrayList<Long>();
					cardRequestIds.add(record.getCardRequestId());
					cardRequestDelegator.doRepealCardAction(getUserProfile(),
							cardRequestIds);
					return SUCCESS_RESULT;
				} catch (BusinessSecurityException e) {
					throw new ActionException(WebExceptionCode.CRA_003, WebExceptionCode.GLB_001_MSG, e);
				}

	}

	// Anbari
	public String doDeleteImageAction() throws BaseException {
				CardRequestDelegator cardRequestDelegator = new CardRequestDelegator();
				try {
					List<Long> cardRequestIds = new ArrayList<Long>();
					cardRequestIds.add(record.getCardRequestId());
					cardRequestDelegator.doImageDeleteAction(getUserProfile(),
							cardRequestIds, record.getCitizenId(),
							CardRequestState.DOCUMENT_AUTHENTICATED);
					return SUCCESS_RESULT;
				} catch (BusinessSecurityException e) {
					throw new ActionException(WebExceptionCode.CRA_004, WebExceptionCode.GLB_001_MSG, e);
				}

	}

	// Anbari
	public String doCMSRetryAction() throws BaseException {
				CardRequestDelegator cardRequestDelegator = new CardRequestDelegator();
				try {
					List<Long> cardRequestIds = new ArrayList<Long>();
					cardRequestIds.add(record.getCardRequestId());
					cardRequestDelegator.doCMSRetryAction(getUserProfile(),
							cardRequestIds, CardRequestState.APPROVED_BY_AFIS);
					return SUCCESS_RESULT;
				} catch (BusinessSecurityException e) {
					throw new ActionException(WebExceptionCode.CRA_005, WebExceptionCode.GLB_001_MSG, e);
				}

	}
	// ganjyar
		public String doAccessProduction() throws BaseException {
			CardRequestDelegator cardRequestDelegator = new CardRequestDelegator();
			try {
				accessProduction = cardRequestDelegator.getAccessProduction(getUserProfile());
				
				return SUCCESS_RESULT;
			} catch (BusinessSecurityException e) {
				throw new ActionException(WebExceptionCode.CRA_006,
						WebExceptionCode.GLB_001_MSG, e);
			}

		}

}
