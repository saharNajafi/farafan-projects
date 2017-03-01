package com.gam.nocr.ems.web.action;

import gampooya.tools.security.BusinessSecurityException;

import java.util.List;

import org.slf4j.Logger;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.web.struts2.extJsController.ActionException;
import com.gam.commons.core.web.struts2.extJsController.ListControllerImpl;
import com.gam.nocr.ems.biz.delegator.CardRequestDelegator;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.vol.CardDispatchInfoVTO;
/**
 * this is a class used in lost card cartable in 3S
 * @author ganjyar
 *
 */
public class LostCardAction extends ListControllerImpl<CardDispatchInfoVTO> {

	private static final Logger logger = BaseLog
			.getLogger(LostCardAction.class);

	/**
	 * the id of the card that must confirm its losing
	 */
	private String cardId;

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	@Override
	public void setRecords(List<CardDispatchInfoVTO> records) {
		this.records = records;
	}

	/**
	 * this method is used in confirm lost card which announced in CCos. At the
	 * first the admin of 3s must confirm it then a job will notify CMS about
	 * them
	 * 
	 * @author Gangyar
	 * @return
	 */
	public String doConfirmLostCard() throws BaseException {

		try {

			new CardRequestDelegator().doConfirmLostCard(getUserProfile(),
					Long.valueOf(cardId));
			logger.info("Confirming lost card with id : " + getCardId());
			return SUCCESS_RESULT;
		} catch (BusinessSecurityException e) {
			throw new ActionException(WebExceptionCode.LCA_001,
					WebExceptionCode.GLB_001_MSG, e);
		} catch (Exception e) {
			throw new ActionException(WebExceptionCode.LCA_002,
					WebExceptionCode.GLB_003_MSG, e);
		}

	}

}
