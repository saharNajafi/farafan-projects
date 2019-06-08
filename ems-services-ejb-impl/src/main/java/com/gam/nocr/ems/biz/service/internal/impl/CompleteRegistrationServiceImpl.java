package com.gam.nocr.ems.biz.service.internal.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.BizLoggable;
import com.gam.commons.core.biz.service.Permissions;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.biz.service.factory.ServiceFactory;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.nocr.ems.biz.service.CardRequestHistoryService;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.biz.service.RegistrationService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.dao.CardRequestDAO;
import com.gam.nocr.ems.data.dao.EnrollmentOfficeDAO;
import com.gam.nocr.ems.data.domain.BiometricTO;
import com.gam.nocr.ems.data.domain.CardRequestTO;
import com.gam.nocr.ems.data.domain.DocumentTO;
import com.gam.nocr.ems.data.domain.EnrollmentOfficeTO;
import com.gam.nocr.ems.data.enums.CardRequestHistoryAction;
import com.gam.nocr.ems.data.enums.SystemId;
import com.gam.nocr.ems.util.EmsUtil;
import gampooya.tools.date.DateUtil;
import org.slf4j.Logger;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.Date;

import static com.gam.nocr.ems.config.EMSLogicalNames.*;

/**
 * @author Haeri (haeri@gamelectronics.com)
 */
@Stateless(name = "CompleteRegistrationService")
@Local(CompleteRegistrationServiceLocal.class)
@Remote(CompleteRegistrationServiceRemote.class)
public class CompleteRegistrationServiceImpl extends EMSAbstractService implements CompleteRegistrationServiceLocal, CompleteRegistrationServiceRemote {

    private static final Logger logger = BaseLog.getLogger(CompleteRegistrationServiceImpl.class);

    @Resource
    SessionContext sessionContext;

    @Override
    @Permissions(value = "ems_transferMES")
    @BizLoggable(logAction = "INSERT", logEntityName = "REQUEST")
    public void register(CardRequestTO requestTO, ArrayList<BiometricTO> fingers
            , ArrayList<BiometricTO> faces
            , ArrayList<DocumentTO> documents, byte[] signature, String featureExtractorIdNormal,String featureExtractorIdCC) throws BaseException {
        RegistrationService registrationService = getRegistrationService();
        registrationService.setUserProfileTO(getUserProfileTO());
        long requestId = registrationService.saveFromMES(requestTO);
//        registrationService.addFingerDataFromMES(requestId, fingers);
        registrationService.addFingerDataFromMES(requestId, fingers, featureExtractorIdNormal,featureExtractorIdCC);
        registrationService.addFaceDataFromMES(requestId, faces);
        registrationService.addDocumentFromMES(requestId, documents);
//		registrationService.authenticateDocumentFromMES(requestId);
        registrationService.approveRequestFromMES(requestId, signature , getUserProfileTO());
    }

    @Override
    public String requestArchiveId(Long cardRequestId, Long enrollmentOfficeId) throws BaseException {
        if (cardRequestId == null)
            throw new ServiceException(BizExceptionCode.CRS_004, BizExceptionCode.CRS_004_MSG);
        if (enrollmentOfficeId == null)
            throw new ServiceException(BizExceptionCode.CRS_005, BizExceptionCode.CRS_005_MSG);

        EnrollmentOfficeTO enrollmentOfficeTO = getEnrollmentOfficeDAO().find(EnrollmentOfficeTO.class, enrollmentOfficeId);
        String archiveId;
        String archiveIdCounter;

        if (enrollmentOfficeTO == null)
            throw new ServiceException(BizExceptionCode.CRS_006, BizExceptionCode.CRS_006_MSG);

        if (enrollmentOfficeTO.getArchiveIdRecycleDate() == null
                || !EmsUtil.getDateAtMidnight(enrollmentOfficeTO.getArchiveIdRecycleDate())
                .equals(EmsUtil.getDateAtMidnight(new Date()))) {
            enrollmentOfficeTO.setArchiveIdCounter("0");
            enrollmentOfficeTO.setArchiveIdRecycleDate(EmsUtil.getDateAtMidnight(new Date()));
            getEnrollmentOfficeDAO().update(enrollmentOfficeTO);
            archiveIdCounter = "0";
        } else {
            archiveIdCounter = String.valueOf((Long.valueOf(enrollmentOfficeTO.getArchiveIdCounter()) + 1));
            enrollmentOfficeTO.setArchiveIdCounter(archiveIdCounter);
            getEnrollmentOfficeDAO().update(enrollmentOfficeTO);
        }

        archiveId = generateArchiveId(archiveIdCounter, enrollmentOfficeTO.getCode());
        getCardRequestDAO().updateArchiveId(cardRequestId, archiveId);
        getCardRequestHistoryService().create(
                new CardRequestTO(cardRequestId),
                "Registration receipt is printed"
                , SystemId.CCOS
                , cardRequestId.toString()
                , null
                , getUserProfileTO().getUserName());

        return archiveId;
    }

    private String generateArchiveId(String archiveIdCounter, String enrollmentOfficeCode) {
        if (enrollmentOfficeCode.length() > 5)
            enrollmentOfficeCode = enrollmentOfficeCode.substring(0, 5);
        else {
            try {
                enrollmentOfficeCode = EmsUtil.createRequestIdForMessage(enrollmentOfficeCode, 5, "", "0");
            } catch (BaseException e) {
                logger.error(BizExceptionCode.GLB_ERR_MSG, e);
            }
        }

        if (archiveIdCounter.length() > 3)
            archiveIdCounter = archiveIdCounter.substring(0, 3);
        else {
            try {
                archiveIdCounter = EmsUtil.createRequestIdForMessage(archiveIdCounter, 3, "", "0");
            } catch (BaseException e) {
                logger.error(BizExceptionCode.GLB_ERR_MSG, e);
            }
        }


        String jalaliDate = DateUtil.convert(new Date(), DateUtil.JALALI);

        String jalaliYear = jalaliDate.split("/")[0].substring(2, 4);
        String jalaliMonth = jalaliDate.split("/")[1];
        String jalaliDay = jalaliDate.split("/")[2];

        return enrollmentOfficeCode + jalaliYear + jalaliMonth + jalaliDay + archiveIdCounter;
    }

    private RegistrationService getRegistrationService() throws BaseException {
        RegistrationService registrationService;
        try {
            registrationService = ServiceFactoryProvider.getServiceFactory().getService(getServiceJNDIName(SRV_REGISTRATION), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(BizExceptionCode.CRS_001, BizExceptionCode.GLB_002_MSG, e, SRV_REGISTRATION.split(","));
        }
        registrationService.setUserProfileTO(getUserProfileTO());
        return registrationService;
    }

    private CardRequestDAO getCardRequestDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(getDaoJNDIName(DAO_CARD_REQUEST));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.CRS_002, BizExceptionCode.GLB_001_MSG, e,
                    DAO_CARD_REQUEST.split(","));
        }
    }

    private EnrollmentOfficeDAO getEnrollmentOfficeDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(getDaoJNDIName(DAO_ENROLLMENT_OFFICE));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.CRS_003, BizExceptionCode.GLB_001_MSG, e,
                    DAO_ENROLLMENT_OFFICE.split(","));
        }
    }

    private CardRequestHistoryService getCardRequestHistoryService() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider
                .getServiceFactory();
        CardRequestHistoryService cardRequestHistoryService;
        try {
            cardRequestHistoryService = serviceFactory.getService(EMSLogicalNames
                    .getServiceJNDIName(EMSLogicalNames.SRV_CARD_REQUEST_HISTORY), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(BizExceptionCode.CRS_007,
                    BizExceptionCode.GLB_002_MSG, e,
                    EMSLogicalNames.SRV_CARD_REQUEST_HISTORY.split(","));
        }
        cardRequestHistoryService.setUserProfileTO(getUserProfileTO());
        return cardRequestHistoryService;
    }
}
