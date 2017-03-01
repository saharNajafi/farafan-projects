package com.gam.nocr.ems.web.action;

import gampooya.tools.security.BusinessSecurityException;

import java.util.ArrayList;
import java.util.List;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.web.struts2.extJsController.ActionException;
import com.gam.commons.core.web.struts2.extJsController.ListControllerImpl;
import com.gam.nocr.ems.biz.delegator.HelpDelegator;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.AboutTO;
import com.gam.nocr.ems.data.domain.vol.AboutVTO;

public class AboutAction extends ListControllerImpl<AboutVTO> {

	@Override
	public void setRecords(List<AboutVTO> records) {
		this.records = records;
	}

	public String save() throws BaseException {
		try {
			HelpDelegator helpDelegator = new HelpDelegator();
			for (AboutVTO to : records) {
				if (to.getId() == null) {
					AboutTO aboutTO = new AboutTO();
					aboutTO.setContent(to.getContent());

					helpDelegator.saveAbout(getUserProfile(), aboutTO);
				} else {
					AboutTO aboutTO = new AboutTO();
					aboutTO.setContent(to.getContent());
					aboutTO.setId(to.getId());
					helpDelegator.updateAbout(getUserProfile(), aboutTO);
				}
			}
			return SUCCESS_RESULT;
		} catch (BusinessSecurityException e) {
			throw new ActionException(WebExceptionCode.ABA_001,
					WebExceptionCode.GLB_001_MSG, e);
		}
	}

	public String load() throws BaseException {
		try {
			HelpDelegator ratingDelegator = new HelpDelegator();
			AboutTO aboutTO = null;

			aboutTO = ratingDelegator.getAbout(getUserProfile());

			AboutVTO aboutVTO = new AboutVTO();

			aboutVTO.setId(aboutTO.getId());
			aboutVTO.setContent(aboutTO.getContent());
			aboutVTO.setCreateDateAbout(aboutTO.getCreateDate());
			List<AboutVTO> abouts = new ArrayList<AboutVTO>();
			abouts.add(aboutVTO);
			setRecords(abouts);

			return SUCCESS_RESULT;
		} catch (BusinessSecurityException e) {
			throw new ActionException(WebExceptionCode.ABA_002,
					WebExceptionCode.GLB_001_MSG, e);
		} 
	}
}
