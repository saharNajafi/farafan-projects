package com.gam.nocr.ems.biz.delegator;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.Delegator;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.service.AboutService;
import com.gam.nocr.ems.biz.service.HelpService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.domain.AboutTO;
import com.gam.nocr.ems.data.domain.HelpTO;
import com.gam.nocr.ems.data.domain.vol.HelpVTO;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * @author: Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public class HelpDelegator implements Delegator {

	private AboutService getAboutService(UserProfileTO userProfileTO)
			throws BaseException {
		AboutService aboutService;
		try {
			aboutService = (AboutService) ServiceFactoryProvider
					.getServiceFactory()
					.getService(
							EMSLogicalNames
									.getServiceJNDIName(EMSLogicalNames.SRV_ABOUT), EmsUtil.getUserInfo(userProfileTO));
		} catch (ServiceFactoryException e) {
			throw new DelegatorException(BizExceptionCode.HDL_001,
					BizExceptionCode.GLB_002_MSG, e,
					EMSLogicalNames.SRV_ABOUT.split(","));
		}
		aboutService.setUserProfileTO(userProfileTO);
		return aboutService;
	}

	private HelpService getHelpService(UserProfileTO userProfileTO)
			throws BaseException {
		HelpService helpService;
		try {
			helpService = (HelpService) ServiceFactoryProvider
					.getServiceFactory()
					.getService(
							EMSLogicalNames
									.getServiceJNDIName(EMSLogicalNames.SRV_HELP), EmsUtil.getUserInfo(userProfileTO));
		} catch (ServiceFactoryException e) {
			throw new DelegatorException(BizExceptionCode.HDL_001,
					BizExceptionCode.GLB_002_MSG, e,
					EMSLogicalNames.SRV_HELP.split(","));
		}
		helpService.setUserProfileTO(userProfileTO);
		return helpService;
	}

	public AboutTO getAbout(UserProfileTO userProfileTO) throws BaseException {
		return getAboutService(userProfileTO).getLastAbout();
	}

	public long save(UserProfileTO userProfile, HelpVTO to) throws BaseException {
		return getHelpService(userProfile).save(to);
	}

	public long saveAbout(UserProfileTO userProfile, AboutTO to)
			throws BaseException {
		return getAboutService(userProfile).save(to);

	}

	public void updateAbout(UserProfileTO userProfile, AboutTO to)
			throws BaseException {
		getAboutService(userProfile).update(to);
	}

	public AboutTO load(UserProfileTO userProfile, long parseLong)
			throws BaseException {
		return getAboutService(userProfile).getLastAbout();
	}

	public HelpTO downloadHelpFile(UserProfileTO up, long fileId) throws BaseException  {
		return getHelpService(up).downloadHelpFile(fileId);
	}

	public boolean remove(UserProfileTO userProfile, String ids) throws BaseException {
		  return getHelpService(userProfile).remove(ids);
		
	}

}
