package com.gam.nocr.ems.biz.service.internal.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.config.BizExceptionCode;
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

import static com.gam.nocr.ems.config.EMSLogicalNames.*;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 8/8/18.
 */
@Stateless(name = "CitizenInfoService")
@Local(CitizenInfoServiceLocal.class)
@Remote(CitizenInfoServiceRemote.class)
public class CitizenInfoServiceImpl extends EMSAbstractService
implements CitizenInfoServiceLocal, CitizenInfoServiceRemote{

    public void checkCitizenInfoValid(CitizenInfoTO czi) throws BaseException {

        if (czi == null)
            throw new ServiceException(BizExceptionCode.RSI_018,
                    BizExceptionCode.RSI_018_MSG);
        if (!EmsUtil.checkString(czi.getFirstNameEnglish()))
            throw new ServiceException(BizExceptionCode.RSI_021,
                    BizExceptionCode.RSI_021_MSG);
        if (EmsUtil.checkString(czi.getMotherFirstNamePersian())
                && czi.getMotherFirstNamePersian().length() > 42)
            throw new ServiceException(BizExceptionCode.RSI_034,
                    BizExceptionCode.RSI_034_MSG);
        if (EmsUtil.checkString(czi.getMobile())
                && !"NA".equals(czi.getMobile())) {
            if (!EmsUtil.checkRegex(czi.getMobile(), EmsUtil.cellNoConstraint)) {
                throw new ServiceException(BizExceptionCode.RSI_048,
                        BizExceptionCode.RSI_048_MSG);
            }
        }
        if (czi.getMotherBirthDateSolar() == null)
            throw new ServiceException(BizExceptionCode.RSI_022,
                    BizExceptionCode.RSI_022_MSG);
        if (czi.getBirthDateGregorian() == null)
            throw new ServiceException(BizExceptionCode.RSI_070,
                    BizExceptionCode.RSI_070_MSG);
        if (!EmsUtil.checkString(czi.getBirthDateLunar()))
            throw new ServiceException(BizExceptionCode.RSI_071,
                    BizExceptionCode.RSI_071_MSG);
        try {
            DateUtil.convert(czi.getBirthDateLunar(), DateUtil.HIJRI);
        } catch (DateFormatException e) {
            throw new ServiceException(BizExceptionCode.RSI_072,
                    BizExceptionCode.RSI_072_MSG, e);
        }
        if (!EmsUtil.checkString(czi.getBirthDateSolar())) {
            try {
                czi.setBirthDateSolar(DateUtil.convert(
                        czi.getBirthDateGregorian(), DateUtil.JALALI));
            } catch (Exception e) {
                throw new ServiceException(BizExceptionCode.RSI_073,
                        BizExceptionCode.RSI_073_MSG, e);
            }
        }
        if (!EmsUtil.checkString(czi.getBirthDateSolar()))
            throw new ServiceException(BizExceptionCode.RSI_074,
                    BizExceptionCode.RSI_074_MSG);
        if (!EmsUtil.checkString(czi.getSurnameEnglish()))
            throw new ServiceException(BizExceptionCode.RSI_023,
                    BizExceptionCode.RSI_023_MSG);
        if (czi.getSurnameEnglish().length() > 25)
            throw new ServiceException(BizExceptionCode.RSI_037,
                    BizExceptionCode.RSI_037_MSG);
        if (czi.getReligion() == null)
            throw new ServiceException(BizExceptionCode.RSI_027,
                    BizExceptionCode.RSI_027_MSG);
        if (czi.getBirthCertificateSeries() == null)
            throw new ServiceException(BizExceptionCode.RSI_019,
                    BizExceptionCode.RSI_019_MSG);
        if (EmsUtil.checkString(czi.getBirthCertificateSeries())
                && czi.getBirthCertificateSeries().length() != 6)
            throw new ServiceException(BizExceptionCode.RSI_029,
                    BizExceptionCode.RSI_029_MSG);
    }

    public List<BirthCertIssPlaceVTO> fetchBirthCertIssPlace(String nationalId)
            throws BaseException {
        BirthCertIssPlaceVTO birthCertIssPlaceVTO = new BirthCertIssPlaceVTO();
        List<BirthCertIssPlaceVTO> birthCertIssPlaceVTOs = new ArrayList<BirthCertIssPlaceVTO>();

        try {
            String birthCertIssuePlaceName = getCitizenInfoDAO().getBirthCertIssuePlaceByNid(nationalId);
            birthCertIssPlaceVTO.setDepName(birthCertIssuePlaceName);
            birthCertIssPlaceVTOs.add(birthCertIssPlaceVTO);
            return birthCertIssPlaceVTOs;
        } catch (BaseException e) {
            throw new ServiceException(BizExceptionCode.RSI_104, e);
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.RSI_106, BizExceptionCode.RSI_106_MSG, e);
        }
    }

    private CitizenInfoDAO getCitizenInfoDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(
                    getDaoJNDIName(DAO_CITIZEN_INFO));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.RSI_003,
                    BizExceptionCode.GLB_001_MSG, e,
                    DAO_CITIZEN_INFO.split(","));
        }
    }

}
