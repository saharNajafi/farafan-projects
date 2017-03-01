package com.gam.nocr.ems.biz.service.internal.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.gam.nocr.ems.data.dao.HelpDAO;
import com.gam.nocr.ems.data.dao.PersonDAO;
import com.gam.nocr.ems.data.domain.AboutTO;
import com.gam.nocr.ems.data.domain.HelpTO;
import com.gam.nocr.ems.data.domain.PersonTO;
import com.gam.nocr.ems.data.domain.vol.HelpVTO;

@Stateless(name = "HelpService")
@Local(HelpServiceLocal.class)
@Remote(HelpServiceRemote.class)
public class HelpServiceImpl extends EMSAbstractService implements
		HelpServiceLocal, HelpServiceRemote {

	private HelpDAO getHelpDAO() throws BaseException {
		try {
			return DAOFactoryProvider.getDAOFactory().getDAO(
					EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_HELP));
		} catch (DAOFactoryException e) {
			throw new ServiceException(BizExceptionCode.HES_001,
					BizExceptionCode.GLB_001_MSG, e,
					EMSLogicalNames.DAO_HELP.split(","));
		}
	}

	private PersonDAO getPersonDAO() throws BaseException {
		try {
			return DAOFactoryProvider.getDAOFactory().getDAO(
					EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_PERSON));
		} catch (DAOFactoryException e) {
			throw new ServiceException(BizExceptionCode.HES_002,
					BizExceptionCode.GLB_001_MSG, e,
					EMSLogicalNames.DAO_PERSON.split(","));
		}
	}

	@Override
	@Permissions(value = "ems_addHelp || ems_editHelp")
	public Long save(HelpVTO to) throws BaseException {

		try {
			if (to == null)
				throw new ServiceException(BizExceptionCode.HES_003,
						BizExceptionCode.HES_003_MSG);

			PersonTO person = getPersonDAO().findByUsername(
					getUserProfileTO().getUserName());

			HelpTO helpTO = new HelpTO();
			helpTO.setHelpFile(to.getHelpFile());

			helpTO.setCreator(person);
			helpTO.setCreateDate(new Date());
			helpTO.setTitle(to.getTitle());
			helpTO.setContentType(to.getContentType());
			helpTO.setDescription(to.getDescription());
			return getHelpDAO().create(helpTO).getId();
		} catch (BaseException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(BizExceptionCode.HES_004,
					BizExceptionCode.GLB_008_MSG, e);
		}

	}

	@Override
	@Permissions(value = "ems_editHelp")
	public Long update(HelpTO to) throws BaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Permissions(value = "ems_viewHelp")
	public AboutTO load(Long HelpId) throws BaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Permissions(value = "ems_viewHelp")
	public HelpTO downloadHelpFile(long fileId) throws BaseException {
		try {

			HelpTO helpTO = getHelpDAO().find(HelpTO.class, fileId);

			return helpTO;

		} catch (BaseException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(BizExceptionCode.HES_005,
					BizExceptionCode.GLB_008_MSG, e);
		}
	}

	@Override
	@Permissions(value = "ems_removeHelp")
	public boolean remove(String helpIds) throws BaseException {
		try {
			if (helpIds == null || helpIds.trim().length() == 0)
				throw new ServiceException(BizExceptionCode.HES_006,
						BizExceptionCode.HES_006_MSG);
			String[] ids = helpIds.split(",");
			List<Long> idsList = new ArrayList<Long>();
			try {
				for (String id : ids) {
					idsList.add(Long.parseLong(id));
				}
			} catch (NumberFormatException e) {
				throw new ServiceException(BizExceptionCode.HES_007,
						BizExceptionCode.HES_007_MSG);
			}
			return getHelpDAO().deleteHelps(idsList);
		} catch (BaseException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(BizExceptionCode.HES_008,
					BizExceptionCode.GLB_008_MSG, e);
		}
	}

}
