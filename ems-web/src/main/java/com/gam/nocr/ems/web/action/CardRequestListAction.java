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
import com.gam.nocr.ems.data.domain.vol.CardRequestVTO;
import com.gam.nocr.ems.data.enums.CardRequestedAction;
import com.gam.nocr.ems.data.enums.SystemId;

/**
 * Main action class to handle all requests from card requests list
 * 
 * @author <a href="mailto:moghaddam@gamelectronics.com">Ehsan Zaery
 *         Moghaddam</a>
 */
public class CardRequestListAction extends ListControllerImpl<CardRequestVTO> {

	private static final Logger logger = BaseLog
			.getLogger(CardRequestListAction.class);

	/**
	 * Identifier of a card request. When an service is requested for a card
	 * request (e.g. repealing a card request), the identifier card request will
	 * be deserialized into this property
	 */
	private String cardRequestId;
	private boolean hasAccessToChangePriority;

	private CardRequestVTO data;

	public boolean isHasAccessToChangePriority() {
		return hasAccessToChangePriority;
	}

	public void setHasAccessToChangePriority(boolean hasAccessToChangePriority) {
		this.hasAccessToChangePriority = hasAccessToChangePriority;
	}

	/**
	 * In repealing a card request (or undoing it), the type of action requested
	 * by user (repeal, undo, accept, etc.) is passed from client as this
	 * property
	 */
	private String cardRequestAction;

	private String priority;

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	@Override
	public void setRecords(List<CardRequestVTO> records) {
		this.records = records;
	}

	/**
	 * Handles a repealing action requested by the client
	 * 
	 * @return {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
	 * @throws BaseException
	 */
	public String repealCardRequest() throws BaseException {
		try {
			new CardRequestDelegator().doCardRequestRepealAction(
					getUserProfile(), Long.valueOf(getCardRequestId()),
					CardRequestedAction.valueOf(getCardRequestAction()),
					SystemId.EMS);
			logger.info("Repealing request with id : " + getCardRequestId());
			logger.info("Repeal request action : " + getCardRequestAction());
			return SUCCESS_RESULT;
		} catch (BusinessSecurityException e) {
			throw new ActionException(WebExceptionCode.CRA_001,
					WebExceptionCode.GLB_001_MSG, e);
		} catch (Exception e) {
			throw new ActionException(WebExceptionCode.CRA_002,
					WebExceptionCode.GLB_003_MSG, e);
		}
	}

	public String getCardRequestId() {
		return cardRequestId;
	}

	public void setCardRequestId(String cardRequestId) {
		this.cardRequestId = cardRequestId;
	}

	public String getCardRequestAction() {
		return cardRequestAction;
	}

	public void setCardRequestAction(String cardRequestAction) {
		this.cardRequestAction = cardRequestAction;
	}

	/**
	 * this method is used in change priority process .this method finds a card
	 * request by id
	 * 
	 * @return
	 * @throws BaseException
	 * @author ganjyar
	 */
	public String findCardRequestById() throws BaseException {
		try {
			CardRequestVTO cr = new CardRequestDelegator().findCardRequestById(
					getUserProfile(), cardRequestId);
			ArrayList<CardRequestVTO> cardRequestList = new ArrayList<CardRequestVTO>();
			cardRequestList.add(cr);
			setRecords(cardRequestList);

			return SUCCESS_RESULT;
		} catch (BusinessSecurityException e) {
			throw new ActionException(WebExceptionCode.CRA_007,
					WebExceptionCode.GLB_001_MSG, e);
		} catch (Exception e) {
			throw new ActionException(WebExceptionCode.CRA_008,
					WebExceptionCode.GLB_003_MSG, e);
		}

	}

	/**
	 * this method is used in change priority process. the given priority must
	 * be between 0 or 99
	 * 
	 * @return
	 * @throws BaseException
	 * @author ganjyar
	 */
	public String updateCardRequestPriority() throws BaseException {
		try {

			new CardRequestDelegator().updateCardRequestPriority(
					getUserProfile(), cardRequestId, priority);

			return SUCCESS_RESULT;
		} catch (BusinessSecurityException e) {
			throw new ActionException(WebExceptionCode.CRA_009,
					WebExceptionCode.GLB_001_MSG, e);
		} catch (Exception e) {
			throw new ActionException(WebExceptionCode.CRA_010,
					WebExceptionCode.GLB_003_MSG, e);
		}

	}

	/**
	 * this method is used to check change priority access
	 * 
	 * @return
	 * @throws BaseException
	 * @author ganjyar
	 */
	public String hasChangePriorityAccess() throws BaseException {
		try {
			hasAccessToChangePriority = new CardRequestDelegator()
			.hasChangePriorityAccess(getUserProfile());

			return SUCCESS_RESULT;
		} catch (BusinessSecurityException e) {
			throw new ActionException(WebExceptionCode.CRA_011,
					WebExceptionCode.GLB_001_MSG, e);
		} catch (Exception e) {
			throw new ActionException(WebExceptionCode.CRA_012,
					WebExceptionCode.GLB_003_MSG, e);
		}

	}

	//hossein 8feature start
	public String loadById() throws BaseException {
		try {
			logger.info("Card request id : " + getCardRequestId() + "\n"
					+ "Requested action : " + getCardRequestAction());
			if (cardRequestId != null) {
				data = new CardRequestDelegator().viewCardRequestInfo(
						getUserProfile(), Long.parseLong(getCardRequestId()));
				return SUCCESS_RESULT;
			} else {
				throw new ActionException(WebExceptionCode.CRA_013,
						WebExceptionCode.CRA_013_MSG);
			}
		} catch (BusinessSecurityException e) {
			throw new ActionException(WebExceptionCode.CRA_014,
					WebExceptionCode.GLB_001_MSG, e);
		}

	}

	public CardRequestVTO getData() {
		return data;
	}

	public void setData(CardRequestVTO data) {
		this.data = data;
	}
	//hossein 8feature end


}
