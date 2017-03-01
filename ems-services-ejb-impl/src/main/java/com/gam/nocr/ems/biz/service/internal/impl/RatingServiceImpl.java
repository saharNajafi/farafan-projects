package com.gam.nocr.ems.biz.service.internal.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.BizLoggable;
import com.gam.commons.core.biz.service.Permissions;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.commons.core.data.domain.SearchResult;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.biz.service.PortalBaseInfoService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.EMSValueListProvider;
import com.gam.nocr.ems.data.dao.RatingInfoDAO;
import com.gam.nocr.ems.data.domain.RatingInfoTO;
import com.gam.nocr.ems.util.EmsUtil;

import gampooya.tools.vlp.ListException;
import gampooya.tools.vlp.ValueListHandler;
import org.displaytag.exception.ListHandlerException;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
@Stateless(name = "RatingService")
@Local(RatingServiceLocal.class)
@Remote(RatingServiceRemote.class)

public class RatingServiceImpl extends EMSAbstractService implements RatingServiceLocal, RatingServiceRemote {//todo: set permission annotations

    private RatingInfoDAO getRatingInfoDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_RATING_INFO));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.RMS_006, BizExceptionCode.GLB_001_MSG, e, EMSLogicalNames.DAO_RATING_INFO.split(","));
        }
    }

    /**
     * ===================
     * Getter for Services
     * ===================
     */

    /**
     * getPortalBaseInfoService
     *
     * @return an instance of type {@link PortalBaseInfoService}
     * @throws {@link BaseException}
     */
    private PortalBaseInfoService getPortalBaseInfoService() throws BaseException {
        try {
            return ServiceFactoryProvider.getServiceFactory().getService(EMSLogicalNames.getExternalServiceJNDIName(EMSLogicalNames.SRV_PORTAL_BASE_INFO), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(BizExceptionCode.RMS_020, BizExceptionCode.GLB_002_MSG, e, EMSLogicalNames.SRV_PORTAL_BASE_INFO.split(","));
        }
    }

    @Override
    @Permissions(value = "ems_editRating || ems_addRating")
    @BizLoggable(logAction = "INSERT", logEntityName = "RATING")
    public Long save(RatingInfoTO to) throws BaseException {
        try {
            if (to == null)
                throw new ServiceException(BizExceptionCode.RMS_003, BizExceptionCode.RMS_003_MSG);
            if (to.getClazz() == null || to.getClazz().trim().equals(""))
                throw new ServiceException(BizExceptionCode.RMS_004, BizExceptionCode.RMS_004_MSG);
            if (to.getSize() == null)
                throw new ServiceException(BizExceptionCode.RMS_005, BizExceptionCode.RMS_005_MSG);

            to.setModified(true);
            return getRatingInfoDAO().create(to).getId();
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.RMS_015, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    @Permissions(value = "ems_editRating")
    @BizLoggable(logAction = "UPDATE", logEntityName = "RATING")
    public Long update(RatingInfoTO to) throws BaseException {
        try {
            if (to == null)
                throw new ServiceException(BizExceptionCode.RMS_007, BizExceptionCode.RMS_007_MSG);
            if (to.getClazz() == null || to.getClazz().trim().equals(""))
                throw new ServiceException(BizExceptionCode.RMS_009, BizExceptionCode.RMS_009_MSG);
            if (to.getSize() == null)
                throw new ServiceException(BizExceptionCode.RMS_010, BizExceptionCode.RMS_010_MSG);

            RatingInfoTO rating = getRatingInfoDAO().find(RatingInfoTO.class, to.getId());

            if (rating == null)
                throw new ServiceException(BizExceptionCode.RMS_008, BizExceptionCode.RMS_008_MSG, new Long[]{to.getId()});

            to.setModified(true);
            getRatingInfoDAO().update(to);

            return rating.getId();
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.RMS_016, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    @Permissions(value = "ems_viewRating")
    @BizLoggable(logAction = "LOAD", logEntityName = "RATING")
    public RatingInfoTO load(Long ratingInfoId) throws BaseException {
        try {
            if (ratingInfoId == null)
                throw new ServiceException(BizExceptionCode.RMS_011, BizExceptionCode.RMS_011_MSG);
            return getRatingInfoDAO().find(RatingInfoTO.class, ratingInfoId);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.RMS_017, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    @Permissions(value = "ems_removeRating")
    @BizLoggable(logAction = "DELETE", logEntityName = "RATING")
    public boolean remove(String ratingInfoIds) throws BaseException {
        try {
            if (ratingInfoIds == null || ratingInfoIds.trim().length() == 0)
                throw new ServiceException(BizExceptionCode.RMS_012, BizExceptionCode.RMS_012_MSG);
            String[] ids = ratingInfoIds.split(",");
            List<Long> idsList = new ArrayList<Long>();
            try {
                for (String id : ids) {
                    idsList.add(Long.parseLong(id));
                }
            } catch (NumberFormatException e) {
                throw new ServiceException(BizExceptionCode.RMS_014, BizExceptionCode.RMS_014_MSG);
            }
            return getRatingInfoDAO().deleteRatingInfos(idsList);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.RMS_018, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    public SearchResult fetchRatingList(String searchString, int from, int to, String orderBy) throws BaseException {
        try {
            HashMap param = new HashMap();
            String part = "";
            if (searchString != null && searchString.trim().length() != 0) {
                param.put("clsName", "%" + searchString + "%");
                part = ",search";
            }
            try {
                ValueListHandler vlh = EMSValueListProvider.getProvider().loadList("ratingAC", ("main" + part).split(","), ("count" + part).split(","), param, orderBy, null);
                List list = vlh.getList(from, to, true);
                return new SearchResult(vlh.size(), list);
            } catch (ListException e) {
                throw new ServiceException(BizExceptionCode.RMS_001, BizExceptionCode.GLB_006_MSG, e);
            } catch (ListHandlerException e) {
                throw new ServiceException(BizExceptionCode.RMS_002, BizExceptionCode.GLB_007_MSG, e);
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.RMS_019, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    /**
     * The method notifyPortalAboutModifiedRatingInfo is used to notify the subsystem 'Portal' about the new or the
     * modified instances of type {@link RatingInfoTO}
     *
     * @throws com.gam.commons.core.BaseException
     *
     */
    @Override
    public void notifyPortalAboutModifiedRatingInfo() throws BaseException {
        List<RatingInfoTO> ratingInfoTOList = getRatingInfoDAO().findModifiedRatingInfo();
        if (ratingInfoTOList != null && !ratingInfoTOList.isEmpty()) {
            getPortalBaseInfoService().updateRatingInfo(ratingInfoTOList);
            List<Long> ratingInfoIdList = new ArrayList<Long>();
            for (RatingInfoTO ratingInfoTO : ratingInfoTOList) {
                ratingInfoIdList.add(ratingInfoTO.getId());
            }
            getRatingInfoDAO().updateModifiedFields(ratingInfoIdList, Boolean.FALSE);
        }
    }
}
