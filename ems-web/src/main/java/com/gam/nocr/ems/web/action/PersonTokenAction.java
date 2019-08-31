package com.gam.nocr.ems.web.action;

import gampooya.tools.security.BusinessSecurityException;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.web.struts2.extJsController.ActionException;
import com.gam.commons.core.web.struts2.extJsController.ListControllerImpl;
import com.gam.commons.security.SecurityException;
import com.gam.nocr.ems.biz.delegator.TokenManagementDelegator;
import com.gam.nocr.ems.biz.delegator.UserDelegator;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.PersonTokenTO;
import com.gam.nocr.ems.data.domain.vol.PersonTokenVTO;
import com.gam.nocr.ems.data.enums.TokenState;
import com.gam.nocr.ems.data.enums.TokenType;
import com.gam.nocr.ems.util.EmsUtil;

//Adldoost
public class PersonTokenAction extends ListControllerImpl<PersonTokenVTO> {

	private static final Logger logger = BaseLog
			.getLogger(PersonTokenAction.class);

	private String tokenState;

	private String tokenType;

	private String tokenReason;

	private String requestID;

	private Date requestDate;

	private Date issuanceDate;

	@Override
	public void setRecords(List<PersonTokenVTO> records) {

		this.records = records;

	}

	// public String load() throws BaseException {
	// try {
	// TokenManagementDelegator tokenDelegator = new TokenManagementDelegator();
	// List<PersonTokenVTO> personTokenList;
	// personTokenList =
	// tokenDelegator.findPendingToEMSAndRejectByEMS(getUserProfile());
	//
	// setRecords(personTokenList);
	// setRecords(personTokenList);
	//
	// return SUCCESS_RESULT;
	// } catch (BusinessSecurityException e) {
	// throw new ActionException(WebExceptionCode.PTA_001,
	// WebExceptionCode.GLB_003_MSG, e);
	// }
	// }

	public String approve() throws BaseException {
		try {
			if (!EmsUtil.checkString(ids)) {
				throw new ActionException(WebExceptionCode.PTA_002,
						WebExceptionCode.PTA_002_MSG);
			}
			TokenManagementDelegator tokenDelegator = new TokenManagementDelegator();
			tokenDelegator.approveRewnewalRequest(getUserProfile(), ids);

			return SUCCESS_RESULT;
		} catch (BusinessSecurityException e) {
			throw new ActionException(WebExceptionCode.PTA_003,
					WebExceptionCode.GLB_003_MSG, e);
		}
	}

	public String reject() throws BaseException {
		try {
			if (!EmsUtil.checkString(ids)) {
				throw new ActionException(WebExceptionCode.PTA_004,
						WebExceptionCode.PTA_002_MSG);
			}
			TokenManagementDelegator tokenDelegator = new TokenManagementDelegator();
			tokenDelegator.rejectRewnewalRequest(getUserProfile(), ids);

			return SUCCESS_RESULT;
		} catch (BusinessSecurityException e) {
			throw new ActionException(WebExceptionCode.PTA_005,
					WebExceptionCode.GLB_003_MSG, e);
		}
	}

	// edited by Madanipour
	public String deliver() throws BaseException {
		try {
			if (!EmsUtil.checkString(ids)) {
				throw new ActionException(WebExceptionCode.PTA_006,
						WebExceptionCode.PTA_002_MSG);
			}
			TokenManagementDelegator tokenDelegator = new TokenManagementDelegator();

			// at the first we should revoke the person old token

			// **
			// PersonTokenTO oldToken = tokenDelegator
			// .findPersonTokenByPersonAndTokenType(getUserProfile(),
			// TokenType.SIGNATURE);
			// if (oldToken.getState() != TokenState.REVOKED)
			// tokenDelegator.revokeRewnewalRequest(getUserProfile(),
			// oldToken.getId());
			// **

			// TokenManagementDelegator tokenDelegator = new
			// TokenManagementDelegator();

			tokenDelegator.deliverRewnewalRequest(getUserProfile(), ids);

			return SUCCESS_RESULT;
		} catch (Exception e) {
			throw new ActionException(WebExceptionCode.PTA_007,
					WebExceptionCode.GLB_003_MSG, e);
		}
	}

	// edited by Madanipour
	public String activate() throws BaseException {
		try {
			if (!EmsUtil.checkString(ids)) {
				throw new ActionException(WebExceptionCode.PTA_008,
						WebExceptionCode.PTA_002_MSG);
			}

			TokenManagementDelegator tokenDelegator = new TokenManagementDelegator();

			// old token revoked in deliver method
			// PersonTokenTO oldToken = tokenDelegator
			// .findPersonTokenByPersonAndTokenType(getUserProfile(),
			// TokenType.SIGNATURE);
			// if (oldToken.getState() != TokenState.REVOKED)
			// tokenDelegator.revokeRewnewalRequest(getUserProfile(),
			// oldToken.getId());

			tokenDelegator.activateRewnewalRequest(getUserProfile(), ids);
			return SUCCESS_RESULT;
		} catch (BusinessSecurityException e) {
			throw new ActionException(WebExceptionCode.PTA_009,
					WebExceptionCode.GLB_003_MSG, e);

		}
	}

	public String getTokenState() {
		return tokenState;
	}

	public void setTokenState(String tokenState) {
		this.tokenState = tokenState;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public String getRequestID() {
		return requestID;
	}

	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public Date getIssuanceDate() {
		return issuanceDate;
	}

	public void setIssuanceDate(Date issuanceDate) {
		this.issuanceDate = issuanceDate;
	}

	public String getTokenReason() {
		return tokenReason;
	}

	public void setTokenReason(String tokenReason) {
		this.tokenReason = tokenReason;
	}
}
