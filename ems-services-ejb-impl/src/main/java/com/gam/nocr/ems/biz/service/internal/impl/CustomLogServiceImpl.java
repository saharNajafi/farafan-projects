package com.gam.nocr.ems.biz.service.internal.impl;

import com.farafan.customLog.entities.CustomLogTo;
import com.farafan.customLog.enums.CustomLogAction;
import com.farafan.customLog.enums.CustomLogEntity;
import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.dao.CustomLogDAO;
import org.slf4j.Logger;

import javax.ejb.*;
import java.sql.Timestamp;
import java.util.Date;

@Stateless(name = "CustomLogService")
@Local(CustomLogServiceLocal.class)
@Remote(CustomLogServiceRemote.class)
public class CustomLogServiceImpl extends EMSAbstractService implements CustomLogServiceRemote, CustomLogServiceLocal {

    private static final Logger logger = BaseLog.getLogger("CustomLogger");

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void insertLog(CustomLogTo to) throws BaseException {
        try {
            try {
                getCustomLogDAO().insertLog(to);
            } catch (DAOFactoryException e) {
                logger.error("inserting error in custom logger", e);
                throw new ServiceException(
                        BizExceptionCode.CST_001,
                        BizExceptionCode.GLB_001_MSG,
                        e,
                        new String[]{EMSLogicalNames.DAO_CUSTOMLOG}
                );
            } catch (Exception ex) {
                logger.error("inserting error in custom logger", ex);
                if (ex instanceof BaseException)
                    throw (BaseException) ex;
                else
                    throw new ServiceException(BizExceptionCode.CST_002, BizExceptionCode.GLB_001_MSG, ex);
            }
        } catch (BaseException e) {
            logger.error("inserting error in custom logger", e);
            throw e;
        } catch (Exception e) {
            logger.error("inserting error in custom logger", e);
            throw new ServiceException(BizExceptionCode.CST_003, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    /**
     * @return
     * @throws DAOFactoryException
     */
    private CustomLogDAO getCustomLogDAO() throws DAOFactoryException {
        return DAOFactoryProvider.getDAOFactory().getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_CUSTOMLOG));
    }
}
