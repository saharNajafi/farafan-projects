package com.gam.nocr.ems.web.action;

import gampooya.tools.security.BusinessSecurityException;

import java.util.List;

import org.slf4j.Logger;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.web.struts2.extJsController.ActionException;
import com.gam.commons.core.web.struts2.extJsController.ListControllerImpl;
import com.gam.nocr.ems.biz.delegator.HolidayDelegator;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.HolidayTO;

/**
 * Main action class to handle all requests from card requests list
 * 
 * @author <a href="mailto:moghaddam@gamelectronics.com">Ehsan Zaery
 *         Moghaddam</a>
 */
public class HolidayListAction extends ListControllerImpl<HolidayTO> {

	private static final Logger logger = BaseLog
			.getLogger(HolidayListAction.class);

	/**
	 * Identifier of a card request. When an service is requested for a card
	 * request (e.g. repealing a card request), the identifier card request will
	 * be deserialized into this property
	 */
	private String HolidayId;

	/**
	 * In repealing a card request (or undoing it), the type of action requested
	 * by user (repeal, undo, accept, etc.) is passed from client as this
	 * property
	 */
	private String HolidayAction;

	@Override
	public void setRecords(List<HolidayTO> records) {
		this.records = records;
	}

	/**
	 * Handles a repealing action requested by the client
	 * 
	 * @return {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
	 * @throws BaseException
	 */

	public String getHolidayId() {
		return HolidayId;
	}

	public void setHolidayId(String HolidayId) {
		this.HolidayId = HolidayId;
	}

	public String getHolidayAction() {
		return HolidayAction;
	}

	public void setHolidayAction(String HolidayAction) {
		this.HolidayAction = HolidayAction;
	}

	public String save() throws BaseException {
		try {
			HolidayDelegator holidayDelegator = new HolidayDelegator();
			for (HolidayTO to : records) {
				if (to.getId() == null)
					holidayDelegator.save(getUserProfile(), to);
				else
					holidayDelegator.update(getUserProfile(), to);
			}
			return SUCCESS_RESULT;
		} catch (BusinessSecurityException e) {
			throw new ActionException(WebExceptionCode.HLA_001,
					WebExceptionCode.GLB_001_MSG, e);
		}
	}

	public String delete() throws BaseException {
		try {
			HolidayDelegator holidayDelegator = new HolidayDelegator();

			holidayDelegator.remove(getUserProfile(), ids);

			return SUCCESS_RESULT;
		} catch (BusinessSecurityException e) {
			throw new ActionException(WebExceptionCode.HLA_002,
					WebExceptionCode.GLB_001_MSG, e);
		}
	}

}
