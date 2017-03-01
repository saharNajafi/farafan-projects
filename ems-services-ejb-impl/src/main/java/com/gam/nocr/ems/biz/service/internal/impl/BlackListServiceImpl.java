package com.gam.nocr.ems.biz.service.internal.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.BizLoggable;
import com.gam.commons.core.biz.service.Permissions;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.dao.BlackListDAO;
import com.gam.nocr.ems.data.domain.BlackListTO;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Haeri (haeri@gamelectronics.com)
 */
@Stateless(name = "BlackListService")
@Local(BlackListServiceLocal.class)
@Remote(BlackListServiceRemote.class)
public class BlackListServiceImpl extends EMSAbstractService implements BlackListServiceLocal, BlackListServiceRemote {

    private void checkBlackListItem(BlackListTO to) throws BaseException {
        if (to == null)
            throw new ServiceException(BizExceptionCode.BLS_001, BizExceptionCode.BLS_001_MSG);
        if (to.getFirstName() == null || to.getFirstName().trim().equals(""))
            throw new ServiceException(BizExceptionCode.BLS_002, BizExceptionCode.BLS_002_MSG);
        if (to.getSurname() == null || to.getSurname().trim().equals(""))
            throw new ServiceException(BizExceptionCode.BLS_003, BizExceptionCode.BLS_003_MSG);
        if (to.getNationalId() == null || to.getNationalId().trim().equals(""))
            throw new ServiceException(BizExceptionCode.BLS_004, BizExceptionCode.BLS_004_MSG);
        if (to.getFirstName().length() > 84)
            throw new ServiceException(BizExceptionCode.BLS_005, BizExceptionCode.BLS_005_MSG);
        if (to.getSurname().length() > 84)
            throw new ServiceException(BizExceptionCode.BLS_006, BizExceptionCode.BLS_006_MSG);
        if (to.getNationalId().length() != 10)
            throw new ServiceException(BizExceptionCode.BLS_007, BizExceptionCode.BLS_007_MSG);
    }

    @Override
    @Permissions(value = "ems_editBlackList || ems_addBlackList")
    @BizLoggable(logAction = "INSERT", logEntityName = "BLACK_LIST")
    public Long save(BlackListTO to) throws BaseException {
        try {
            checkBlackListItem(to);
            return getBlackListDAO().create(to).getId();
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.BLS_012, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    @Permissions(value = "ems_editBlackList")
    @BizLoggable(logAction = "UPDATE", logEntityName = "BLACK_LIST")
    public Long update(BlackListTO to) throws BaseException {
        try {
            checkBlackListItem(to);
            BlackListDAO blackListDAO = getBlackListDAO();
            BlackListTO item = blackListDAO.find(BlackListTO.class, to.getId());
            if (item == null)
                throw new ServiceException(BizExceptionCode.BLS_009, BizExceptionCode.BLS_009_MSG, new Long[]{to.getId()});
            blackListDAO.update(to);
            return to.getId();
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.BLS_013, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    @Permissions(value = "ems_viewBlackList")
    @BizLoggable(logAction = "LOAD", logEntityName = "BLACK_LIST")
    public BlackListTO load(Long blackListId) throws BaseException {
        try {
            if (blackListId == null)
                throw new ServiceException(BizExceptionCode.BLS_010, BizExceptionCode.BLS_010_MSG);
            return getBlackListDAO().find(BlackListTO.class, blackListId);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.BLS_014, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    @Permissions(value = "ems_removeBlackList")
    @BizLoggable(logAction = "DELETE", logEntityName = "BLACK_LIST")
    public boolean remove(String blackListIds) throws BaseException {
        try {
            if (blackListIds == null || blackListIds.trim().length() == 0)
                throw new ServiceException(BizExceptionCode.BLS_011, BizExceptionCode.BLS_011_MSG);
            String[] ids = blackListIds.split(",");
            List<Long> idsList = new ArrayList<Long>();
            try {
                for (String id : ids) {
                    idsList.add(Long.parseLong(id));
                }
            } catch (NumberFormatException e) {
                throw new ServiceException(BizExceptionCode.RMS_014, BizExceptionCode.RMS_014_MSG, e);
            }
            return getBlackListDAO().deleteBlackListItems(idsList);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.BLS_015, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    private BlackListDAO getBlackListDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_BLACK_LIST));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.BLS_008, BizExceptionCode.GLB_001_MSG, e, EMSLogicalNames.DAO_BLACK_LIST.split(","));
        }
    }
}
