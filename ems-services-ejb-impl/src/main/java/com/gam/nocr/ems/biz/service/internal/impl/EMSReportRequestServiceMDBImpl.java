package com.gam.nocr.ems.biz.service.internal.impl;


import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.biz.service.factory.ServiceFactory;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.nocr.ems.biz.service.ReportRequestService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.dao.ReportDAO;
import com.gam.nocr.ems.data.domain.ReportRequestTO;
import com.gam.nocr.ems.util.EmsUtil;

import org.slf4j.Logger;

import javax.ejb.MessageDriven;
import javax.ejb.TransactionAttributeType;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import static com.gam.nocr.ems.config.EMSLogicalNames.DAO_REPORT;
import static com.gam.nocr.ems.config.EMSLogicalNames.getDaoJNDIName;


/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */

@MessageDriven(mappedName = "queue/EMSJmsModuleReportRequestQueue")
public class EMSReportRequestServiceMDBImpl implements MessageListener {

    private static final Logger logger = BaseLog.getLogger(EMSReportRequestServiceMDBImpl.class);
    private static final Logger reportLogger = BaseLog.getLogger("reportJMS");

    private ReportDAO getReportDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(getDaoJNDIName(DAO_REPORT));
        } catch (DAOFactoryException e) {
            throw new ServiceException(
                    BizExceptionCode.RMG_002,
                    BizExceptionCode.GLB_001_MSG,
                    e,
                    new String[]{EMSLogicalNames.DAO_REPORT});
        }
    }

    /**
     * getReportRequestService
     *
     * @return an instance of type {@link ReportRequestService}
     * @throws com.gam.commons.core.BaseException
     *
     */
    private ReportRequestService getReportRequestService() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider.getServiceFactory();
        ReportRequestService reportRequestService;
        try {
            reportRequestService = serviceFactory.getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_REPORT_REQUEST), null);
        } catch (ServiceFactoryException e) {
            throw new ServiceException(
                    BizExceptionCode.RRM_001,
                    BizExceptionCode.GLB_002_MSG,
                    e,
                    EMSLogicalNames.SRV_REPORT_REQUEST.split(","));
        }
        return reportRequestService;
    }

    @Override
    @javax.ejb.TransactionAttribute(TransactionAttributeType.NEVER)
    public void onMessage(Message message) {
        ObjectMessage objectMessage = (ObjectMessage) message;
        Long reportRequestId = null;
        try {
            reportLogger.info("Processing a report request started");
            reportRequestId = (Long) objectMessage.getObject();
            ReportRequestTO reportRequestTO = getReportRequestService().generateRequestedReport(reportRequestId);
            getReportRequestService().updateReportRequest(reportRequestTO);
            reportLogger.info("Report request " + reportRequestId + " updated in database");
        } catch (JMSException e) {
            logger.error(BizExceptionCode.GLB_ERR_MSG, e);
            reportLogger.error("An error occurred while processing report request " + reportRequestId, e);
        } catch (BaseException e) {
            logger.error(BizExceptionCode.GLB_ERR_MSG, e);
            reportLogger.error("An error occurred while processing report request " + reportRequestId, e);
        } catch (Exception e) {
            logger.error(BizExceptionCode.GLB_ERR_MSG, e);
            reportLogger.error("An error occurred while processing report request " + reportRequestId, e);
        }
    }

}
