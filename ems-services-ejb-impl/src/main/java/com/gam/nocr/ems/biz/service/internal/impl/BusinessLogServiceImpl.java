package com.gam.nocr.ems.biz.service.internal.impl;

import java.sql.Timestamp;
import java.util.Date;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import org.slf4j.Logger;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.dao.BusinessLogDAO;
import com.gam.nocr.ems.data.domain.BusinessLogTO;
import com.gam.nocr.ems.data.enums.BusinessLogAction;
import com.gam.nocr.ems.data.enums.BusinessLogActionAttitude;
import com.gam.nocr.ems.data.enums.BusinessLogEntity;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * Session Bean implementation class BusinessLogServiceImpl
 */
@Stateless(name = "BusinessLogService")
@Local(BusinessLogServiceLocal.class)
@Remote(BusinessLogServiceRemote.class)
public class BusinessLogServiceImpl extends EMSAbstractService implements BusinessLogServiceRemote, BusinessLogServiceLocal {

	  private static final Logger logger = BaseLog.getLogger("BusinessLogger");

//    @Override
//    public void insertLog(BusinessLogTO to) throws BaseException {
//        try {
//            try {
//                getBusinessLogDAO().insertLog(to);
//            } catch (DAOFactoryException e) {
//                throw new ServiceException(BizExceptionCode.BSI_001, BizExceptionCode.GLB_001_MSG, e, new String[]{EMSLogicalNames.DAO_BUSINESSLOG});
//            } catch (Exception ex) {
//                if (ex instanceof BaseException)
//                    throw (BaseException) ex;
//                else
//                    throw new ServiceException(BizExceptionCode.BSI_002, BizExceptionCode.GLB_001_MSG, ex);
//            }
//        } catch (BaseException e) {
//            throw e;
//        } catch (Exception e) {
//            throw new ServiceException(BizExceptionCode.BSI_003, BizExceptionCode.GLB_008_MSG, e);
//        }
//    }
	
	@Override
	public void insertLog(BusinessLogTO to) throws BaseException {
		try {
			String method = EmsUtil.getProfileValue(ProfileKeyName.KEY_BIZLOG_INSERT_METHOD, "file");
			if ("file".equalsIgnoreCase(method)) {
				try {
					logger.info(EmsUtil.toJSON(to));
				} catch (Exception e) {
					logger.error("INSERTING BIZLOG IN FILE ENCOURTED ERROR", e);
				}
			} else if ("db".equalsIgnoreCase(method)) {
				try {
					getBusinessLogDAO().insertLog(to);
				} catch (DAOFactoryException e) {
					throw new ServiceException(BizExceptionCode.BSI_001, BizExceptionCode.GLB_001_MSG, e,
							new String[] { EMSLogicalNames.DAO_BUSINESSLOG });
				} catch (Exception ex) {
					if (ex instanceof BaseException)
						throw (BaseException) ex;
					else
						throw new ServiceException(BizExceptionCode.BSI_002, BizExceptionCode.GLB_001_MSG, ex);
				}
			} else {
				// do nothing
				return;
			}
		} catch (BaseException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(BizExceptionCode.BSI_003, BizExceptionCode.GLB_008_MSG, e);
		}
	}

    @Override
    public void createBusinessLog(BusinessLogAction logAction,
                                   BusinessLogEntity logEntityName,
                                   String logActor,
                                   String additionalData,
                                   Boolean exceptionFlag) throws BaseException {
        BusinessLogTO businessLogTO = new BusinessLogTO();
        businessLogTO.setEntityID(" ");
        businessLogTO.setAction(logAction);
        businessLogTO.setEntityName(logEntityName);
        businessLogTO.setActor(logActor);
        businessLogTO.setAdditionalData(additionalData);
        businessLogTO.setDate(new Timestamp(new Date().getTime()));
        if (exceptionFlag) {
            businessLogTO.setActionAttitude(BusinessLogActionAttitude.F);
        } else {
            businessLogTO.setActionAttitude(BusinessLogActionAttitude.T);
        }
        insertLog(businessLogTO);

    }

    @Override
    public boolean verify(String id) throws BaseException {
        boolean result = false;
        try {
            result = getBusinessLogDAO().verify(id);
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.BSI_004, BizExceptionCode.GLB_001_MSG, e, new String[]{EMSLogicalNames.DAO_BUSINESSLOG});
        }
        return result;
    }

    /**
     * @return
     * @throws DAOFactoryException
     */
    private BusinessLogDAO getBusinessLogDAO() throws DAOFactoryException {
        return DAOFactoryProvider.getDAOFactory().getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_BUSINESSLOG));
    }
}
