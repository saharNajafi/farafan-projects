package com.gam.nocr.ems.biz.service.internal.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.biz.service.factory.ServiceFactory;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.dao.factory.DAOFactory;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.nocr.ems.biz.service.CardRequestService;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.biz.service.ims.CitizenInfoService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.dao.CitizenDAO;
import com.gam.nocr.ems.data.dao.CitizenInfoDAO;
import com.gam.nocr.ems.data.domain.CitizenInfoTO;
import com.gam.nocr.ems.data.domain.CitizenTO;
import com.gam.nocr.ems.data.domain.vol.BirthCertIssPlaceVTO;
import com.gam.nocr.ems.util.EmsUtil;
import gampooya.tools.date.DateFormatException;
import gampooya.tools.date.DateUtil;
import org.nocr.NIN;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import java.util.ArrayList;
import java.util.List;

import static com.gam.nocr.ems.config.EMSLogicalNames.DAO_CITIZEN;
import static com.gam.nocr.ems.config.EMSLogicalNames.DAO_CITIZEN_INFO;
import static com.gam.nocr.ems.config.EMSLogicalNames.getDaoJNDIName;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 8/8/18.
 */
@Stateless(name = "CitizenService")
@Local(CitizenServiceLocal.class)
@Remote(CitizenServiceRemote.class)
public class CitizenServiceImpl extends EMSAbstractService
implements CitizenServiceLocal, CitizenServiceRemote{

    public CitizenTO findByNationalId(String nid) throws BaseException {
        try {
            return getCitizenDAO().findByNationalId(nid);
        } catch (BaseException e) {
            throw e;
        }
    }

    private CitizenDAO getCitizenDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(
                    getDaoJNDIName(DAO_CITIZEN));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.CRE_038,
                    BizExceptionCode.GLB_001_MSG, e);
        }
    }

    public CitizenTO addCitizen(CitizenTO entity) throws BaseException {

        checkCitizenValid(entity);
        getCitizenInfoService().checkCitizenInfoValid(entity.getCitizenInfo());
        List<BirthCertIssPlaceVTO> birthCertIssPlaceVTOList =
                getCitizenInfoService().fetchBirthCertIssPlace(entity.getNationalID());
        if (EmsUtil.checkListSize(birthCertIssPlaceVTOList))
            entity.getCitizenInfo().setBirthCertificateIssuancePlace(
                    birthCertIssPlaceVTOList.get(0).getDepName());
        else
            throw new ServiceException(
                    BizExceptionCode.RSI_107, BizExceptionCode.RSI_107_MSG +
                    entity.getNationalID());
        CitizenTO newCitizen = getCitizenDAO().findByNationalId(entity.getNationalID());
        if (newCitizen != null) {
            entity.setId(newCitizen.getId());
            entity.getCitizenInfo().setId(newCitizen.getId());
        }
        newCitizen = getCitizenDAO().create(entity);
        return newCitizen;
    }

    private void checkCitizenValid(CitizenTO ctz) throws BaseException {
        if (ctz == null) {
            throw new ServiceException(BizExceptionCode.RSI_011,
                    BizExceptionCode.RSI_011_MSG);
        }
        if (!EmsUtil.checkString(ctz.getFirstNamePersian())) {
            throw new ServiceException(BizExceptionCode.RSI_008,
                    BizExceptionCode.RSI_008_MSG);
        }
        if (ctz.getFirstNamePersian().length() > 42) {
            throw new ServiceException(BizExceptionCode.RSI_015,
                    BizExceptionCode.RSI_015_MSG);
        }
        if (!EmsUtil.checkString(ctz.getSurnamePersian())) {
            throw new ServiceException(BizExceptionCode.RSI_009,
                    BizExceptionCode.RSI_009_MSG);
        }
        if (ctz.getSurnamePersian().length() > 42) {
            throw new ServiceException(BizExceptionCode.RSI_016,
                    BizExceptionCode.RSI_016_MSG);
        }
        if (ctz.getNationalID() == null) {
            throw new ServiceException(BizExceptionCode.RSI_010,
                    BizExceptionCode.RSI_010_MSG);
        }
        try {
            if (!NIN.validate(Long.valueOf(ctz.getNationalID()))) {
                throw new ServiceException(BizExceptionCode.RSI_017,
                        BizExceptionCode.RSI_017_MSG);
            }
        } catch (NumberFormatException e) {
            throw new ServiceException(BizExceptionCode.RSI_123,
                    BizExceptionCode.RSI_017_MSG);
        }
    }


    private CitizenInfoService getCitizenInfoService() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider
                .getServiceFactory();
        CitizenInfoService citizenInfoService;
        try {
            citizenInfoService = serviceFactory.getService(EMSLogicalNames
                    .getServiceJNDIName(EMSLogicalNames.SRV_CITIZEN_INFO), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(BizExceptionCode.PTL_005,
                    BizExceptionCode.GLB_002_MSG, e,
                    EMSLogicalNames.SRV_CITIZEN_INFO.split(","));
        }
        citizenInfoService.setUserProfileTO(getUserProfileTO());
        return citizenInfoService;
    }

}
