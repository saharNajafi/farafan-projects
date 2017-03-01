package com.gam.nocr.ems.biz.service.internal.impl;

import java.util.Date;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Permissions;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.dao.AboutDAO;
import com.gam.nocr.ems.data.dao.PersonDAO;
import com.gam.nocr.ems.data.domain.AboutTO;
import com.gam.nocr.ems.data.domain.PersonTO;
import com.gam.nocr.ems.util.EmsUtil;

@Stateless(name = "AboutService")
@Local(AboutServiceLocal.class)
@Remote(AboutServiceRemote.class)
public class AboutServiceImpl extends EMSAbstractService implements
		AboutServiceLocal, AboutServiceRemote {

	private AboutDAO getAboutDAO() throws BaseException {
		try {
			return DAOFactoryProvider.getDAOFactory().getDAO(
					EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_ABOUT));
		} catch (DAOFactoryException e) {
			throw new ServiceException(BizExceptionCode.ABS_001,
					BizExceptionCode.GLB_001_MSG, e,
					EMSLogicalNames.DAO_ABOUT.split(","));
		}
	}

	private PersonDAO getPersonDAO() throws BaseException {
		try {
			return DAOFactoryProvider.getDAOFactory().getDAO(
					EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_PERSON));
		} catch (DAOFactoryException e) {
			throw new ServiceException(BizExceptionCode.ABS_002,
					BizExceptionCode.GLB_001_MSG, e,
					EMSLogicalNames.DAO_ABOUT.split(","));
		}
	}

	@Override
	@Permissions(value = "ems_editAbout || ems_addAbout")
	public Long save(AboutTO to) throws BaseException {
		try {
			if (to == null)
				throw new ServiceException(BizExceptionCode.ABS_003,
						BizExceptionCode.ABS_003_MSG);

			if (!EmsUtil.checkString(to.getContent()))
				throw new ServiceException(BizExceptionCode.ABS_004,
						BizExceptionCode.ABS_004_MSG);

			PersonTO person = getPersonDAO().findByUsername(
					userProfileTO.getUserName());

			to.setCreator(person);
			to.setCreateDate(new Date());
			return getAboutDAO().create(to).getId();
		} catch (BaseException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(BizExceptionCode.ABS_005,
					BizExceptionCode.GLB_008_MSG, e);
		}
	}

	@Override
	@Permissions(value = "ems_editAbout")
	public Long update(AboutTO to) throws BaseException {
		try {
			if (to == null)
				throw new ServiceException(BizExceptionCode.ABS_006,
						BizExceptionCode.ABS_003_MSG);
			if (!EmsUtil.checkString(to.getContent()))
				throw new ServiceException(BizExceptionCode.ABS_007,
						BizExceptionCode.ABS_004_MSG);

			AboutTO about = getAboutDAO().find(AboutTO.class, to.getId());

			if (about == null)
				throw new ServiceException(BizExceptionCode.ABS_008,
						BizExceptionCode.ABS_008_MSG, new Long[] { to.getId() });

			about.setContent(to.getContent());
			about.setCreateDate(new Date());
			getAboutDAO().update(about);

			return about.getId();
		} catch (BaseException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(BizExceptionCode.ABS_009,
					BizExceptionCode.GLB_008_MSG, e);
		}
	}

	@Override
	@Permissions(value = "ems_viewAbout")
	public AboutTO load(Long aboutId) throws BaseException {
		try {
			if (aboutId == null)
				throw new ServiceException(BizExceptionCode.ABS_010,
						BizExceptionCode.ABS_010_MSG);
			return getAboutDAO().find(AboutTO.class, aboutId);
		} catch (BaseException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(BizExceptionCode.ABS_011,
					BizExceptionCode.GLB_008_MSG, e);
		}
	}

	@Override
	@Permissions(value = "ems_viewAbout")
	public AboutTO getLastAbout() throws BaseException {
		try {

			AboutTO aboutTO = getAboutDAO().getAbout();

			if (aboutTO == null)
				throw new ServiceException(BizExceptionCode.ABS_012,
						BizExceptionCode.ABS_012_MSG);

			return aboutTO;

		} catch (BaseException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(BizExceptionCode.ABS_013,
					BizExceptionCode.GLB_008_MSG, e);
		}

	}

}
