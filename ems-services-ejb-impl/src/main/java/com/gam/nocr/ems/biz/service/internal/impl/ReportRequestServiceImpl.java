package com.gam.nocr.ems.biz.service.internal.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.AbstractService;
import com.gam.commons.core.biz.service.BizLoggable;
import com.gam.commons.core.biz.service.Permissions;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.biz.service.factory.ServiceFactory;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.nocr.ems.biz.service.ReportManagementService;
import com.gam.nocr.ems.biz.service.ReportRequestService;
import com.gam.nocr.ems.biz.service.UserManagementService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.EMSValueListProvider;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.dao.PersonDAO;
import com.gam.nocr.ems.data.dao.ReportDAO;
import com.gam.nocr.ems.data.dao.ReportFileDAO;
import com.gam.nocr.ems.data.dao.ReportRequestDAO;
import com.gam.nocr.ems.data.domain.PersonTO;
import com.gam.nocr.ems.data.domain.ReportFileTO;
import com.gam.nocr.ems.data.domain.ReportRequestTO;
import com.gam.nocr.ems.data.domain.ReportTO;
import com.gam.nocr.ems.data.domain.vol.ReportRequestVTO;
import com.gam.nocr.ems.data.enums.ReportFileType;
import com.gam.nocr.ems.data.enums.ReportOutputType;
import com.gam.nocr.ems.data.enums.ReportRequestState;
import com.gam.nocr.ems.data.enums.SystemId;
import com.gam.nocr.ems.util.EmsUtil;
import gampooya.tools.vlp.ValueListHandler;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.jms.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static com.gam.nocr.ems.config.EMSLogicalNames.*;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
@Stateless(name = "ReportRequestService")
@Local(ReportRequestServiceLocal.class)
@Remote(ReportRequestServiceRemote.class)
public class ReportRequestServiceImpl extends AbstractService implements ReportRequestServiceLocal, ReportRequestServiceRemote {

    private static final Logger logger = BaseLog.getLogger(ReportRequestServiceImpl.class);
    private static final Logger reportJobLogger = BaseLog.getLogger("processRequestedReports");
    private static final Logger reportLogger = BaseLog.getLogger("reportJMS");
    
    private static final String DEFAULT_REPORTING_JNDI_NAME  = "jdbc/GamNocrEmsOracleDS";

    @Resource(name = "jms/EMSJmsModuleConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(name = "queue/EMSJmsModuleReportRequestQueue")
    private Destination destination;

    /**
     * Getter for DAOs
     */

    /**
     * getReportDAO
     *
     * @return an instance of type {@link com.gam.nocr.ems.data.dao.ReportDAO}
     * @throws BaseException
     */
    private ReportDAO getReportDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(getDaoJNDIName(DAO_REPORT));
        } catch (DAOFactoryException e) {
            throw new ServiceException(
                    BizExceptionCode.RRS_003,
                    BizExceptionCode.GLB_001_MSG,
                    e,
                    new String[]{EMSLogicalNames.DAO_REPORT});
        }
    }

    /**
     * getPersonDAO
     *
     * @return an instance of type {@link com.gam.nocr.ems.data.dao.PersonDAO}
     * @throws BaseException
     */
    private PersonDAO getPersonDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(getDaoJNDIName(DAO_PERSON));
        } catch (DAOFactoryException e) {
            throw new ServiceException(
                    BizExceptionCode.RRS_027,
                    BizExceptionCode.GLB_001_MSG,
                    e,
                    new String[]{EMSLogicalNames.DAO_PERSON});
        }
    }

    /**
     * getReportRequestDAO
     *
     * @return an instance of type {@link com.gam.nocr.ems.data.dao.ReportRequestDAO}
     * @throws BaseException
     */
    private ReportRequestDAO getReportRequestDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(getDaoJNDIName(DAO_REPORT_REQUEST));
        } catch (DAOFactoryException e) {
            throw new ServiceException(
                    BizExceptionCode.RRS_005,
                    BizExceptionCode.GLB_001_MSG,
                    e,
                    new String[]{EMSLogicalNames.DAO_REPORT_REQUEST});
        }
    }

    /**
     * getReportFileDAO
     *
     * @return an instance of type {@link com.gam.nocr.ems.data.dao.ReportFileDAO}
     * @throws BaseException
     */
    private ReportFileDAO getReportFileDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(getDaoJNDIName(DAO_REPORT_FILE));
        } catch (DAOFactoryException e) {
            throw new ServiceException(
                    BizExceptionCode.RRS_013,
                    BizExceptionCode.GLB_001_MSG,
                    e,
                    new String[]{EMSLogicalNames.DAO_REPORT_FILE});
        }
    }

    /**
     * Getter for Services
     */

    /**
     * getUserManagementService
     *
     * @return an instance of type {@link UserManagementService}
     * @throws BaseException
     */
    private UserManagementService getUserManagementService() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider.getServiceFactory();
        UserManagementService userManagementService;
        try {
            userManagementService = serviceFactory.getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_USER), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(
                    BizExceptionCode.RRS_016,
                    BizExceptionCode.GLB_002_MSG,
                    e,
                    EMSLogicalNames.SRV_USER.split(","));
        }
        userManagementService.setUserProfileTO(getUserProfileTO());
        return userManagementService;
    }

    /**
     * getJRResultSetDataSource
     *
     * @param query
     * @param dataSourceJNDIName
     * @return
     * @throws BaseException
     */
    private JRResultSetDataSource getJRResultSetDataSource(String query,
                                                           String dataSourceJNDIName) throws BaseException {
        Connection connection = EmsUtil.getConnectionByDataSource(dataSourceJNDIName);
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            connection.close();
            return new JRResultSetDataSource(resultSet);
        } catch (SQLException e) {
            throw new ServiceException(BizExceptionCode.RRS_009, BizExceptionCode.RRS_009_MSG, e);
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.RRS_010, BizExceptionCode.GLB_008_MSG, e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                logger.error(BizExceptionCode.GLB_ERR_MSG, e.getMessage(), e);
            }
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                logger.error(BizExceptionCode.GLB_ERR_MSG, e.getMessage(), e);
            }
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error(BizExceptionCode.GLB_ERR_MSG, e.getMessage(), e);
            }
        }
    }

    /**
     * getReportManagementService
     *
     * @return an instance of type {@link ReportManagementService}
     * @throws BaseException
     */
    private ReportManagementService getReportManagementService() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider.getServiceFactory();
        ReportManagementService reportManagementService;
        try {
            reportManagementService = serviceFactory.getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_REPORT_MANAGEMENT), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(
                    BizExceptionCode.RRS_008,
                    BizExceptionCode.GLB_002_MSG,
                    e,
                    EMSLogicalNames.SRV_REPORT_MANAGEMENT.split(","));
        }
        reportManagementService.setUserProfileTO(getUserProfileTO());
        return reportManagementService;
    }

    /**
     * The method validateReportRequest is used to check the correctness of mandatory fields
     *
     * @param reportRequestVTO is an instance of type {@link ReportRequestVTO}
     * @throws BaseException
     */
    private void validateReportRequest(ReportRequestVTO reportRequestVTO) throws BaseException {
        if (reportRequestVTO == null) {
            throw new ServiceException(BizExceptionCode.RRS_001, BizExceptionCode.GLB_018_MSG);
        }
        if (reportRequestVTO.getReportId() == null) {
            throw new ServiceException(BizExceptionCode.RRS_002, BizExceptionCode.RRS_002_MSG);
        }


    }

    /**
     * The method getEmbeddedReport is used to get the object of type {@link ReportTO}, which assigned to the report request
     *
     * @param reportId is an instance of type {@link Long}, which represents a specified instance of type {@link ReportTO}
     * @return an instance of type {@link ReportTO}
     * @throws BaseException
     */
    private ReportTO getEmbeddedReport(Long reportId) throws BaseException {
        ReportTO reportTO = getReportDAO().find(ReportTO.class, reportId);
        if (reportTO == null) {
            throw new ServiceException(BizExceptionCode.RRS_004, BizExceptionCode.RRS_004_MSG);
        }
        return reportTO;
    }

    /**
     * the method generateResult is used to generate a byte array, which represents the ultimate report
     *
     * @param reportRequestType    is an instance of {@link com.gam.nocr.ems.data.enums.ReportOutputType}
     * @param byteArrayInputStream is an instance of {@link java.io.ByteArrayInputStream}
     * @param params               is a map of type {@link java.util.Map<String, Object>}
     * @param resultSetDataSource  is an instance of type {@link net.sf.jasperreports.engine.JRResultSetDataSource}
     * @param embeddedQueryFlag
     * @return a byte[], which carries the ultimate report or null
     * @throws BaseException
     */
    private byte[] generateResult(ReportOutputType reportRequestType,
                                  ByteArrayInputStream byteArrayInputStream,
                                  Map<String, Object> params,
                                  JRResultSetDataSource resultSetDataSource, boolean embeddedQueryFlag) throws BaseException {
        reportLogger.debug("Generating report in " + reportRequestType + " format started");

        byte[] result = null;
        try {
            if (embeddedQueryFlag) {
                switch (reportRequestType) {
                    case PDF:
                        result = JasperRunManager.runReportToPdf(byteArrayInputStream, params);
                        break;
                    case XLS:
                        JasperPrint jasperPrint = JasperFillManager.fillReport(byteArrayInputStream, params);
                        ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
                        JRXlsExporter exporter = new JRXlsExporter();
                        exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
                        exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, xlsReport);
                        exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
                        exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
                        exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
                        exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
                        exporter.exportReport();
                        result = xlsReport.toByteArray();
                        break;
                }

            } else if (resultSetDataSource != null) {
                switch (reportRequestType) {

                    case PDF:
                        result = JasperRunManager.runReportToPdf(byteArrayInputStream, params, resultSetDataSource);
                        break;
                    case XLS:
                        JasperPrint jasperPrint = JasperFillManager.fillReport(byteArrayInputStream, params, resultSetDataSource);
                        ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
                        JRXlsExporter exporter = new JRXlsExporter();
                        exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
                        exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, xlsReport);
                        exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
                        exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
                        exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
                        exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
                        exporter.exportReport();
                        result = xlsReport.toByteArray();
                        break;
                }

            } else {
                switch (reportRequestType) {
                    case PDF:
                        result = JasperRunManager.runReportToPdf(byteArrayInputStream, params, new JREmptyDataSource());
//						JasperPrint jasperPrint = JasperFillManager.fillReport(byteArrayInputStream, params, new JREmptyDataSource());
//						result = JasperExportManager.exportReportToPdf(jasperPrint);
                        break;
                    case XLS:
                        JasperPrint jasperPrint = JasperFillManager.fillReport(byteArrayInputStream, params, new JREmptyDataSource());
                        JRXlsExporter exporterXLS = new JRXlsExporter();
                        exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
                        exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
                        exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
                        exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
                        exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
                        exporterXLS.exportReport();
                        ByteArrayOutputStream os = (ByteArrayOutputStream) exporterXLS.getParameter(JRExporterParameter.OUTPUT_STREAM);
                        result = os.toByteArray();
                        break;
                }
            }
        } catch (JRException e) {
            reportLogger.error("An error occurred while generating report in " + reportRequestType + " format", e);
            throw new ServiceException(BizExceptionCode.RRS_012, BizExceptionCode.RRS_012_MSG, e);
        }

        reportLogger.debug("Generating report in " + reportRequestType + " format finished");

        return result;
    }

    /**
     * The method fetchListName is used to fetch the name of list, which has to be used for vlp query
     *
     * @param reportTO is an instance of {@link ReportTO}
     * @return an instance of type {@link String}, which represents the listName of the VLP queries or null
     * @throws BaseException
     */
    private String fetchListName(ReportTO reportTO) throws BaseException {
        Map<Object, Object> variableMap = getReportManagementService().getVariables(reportTO);
        for (Object key : variableMap.keySet()) {
            try {
                if (key.toString().toLowerCase().contains("list")) {
                    return (String) variableMap.get(key);
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                // Do nothing
            }
        }
        return null;
    }

    /**
     * The method checkEmbeddedQuery is used to check, whether any queries embedded in the report or not.
     *
     * @param reportTO is an instance of {@link ReportTO}
     * @return true or false
     * @throws BaseException
     */
    private boolean checkEmbeddedQuery(ReportTO reportTO) throws BaseException {
        Object[] queryChunks = getReportManagementService().getQueryString(reportTO);
        return EmsUtil.checkArraySize(queryChunks);
    }

    /**
     * The method putInJMSQueue is used to put a message into the queue to be processed
     *
     * @param reportRequestId is an instance of type {@link Long}, which identifies an instance of type {@link ReportRequestTO}
     * @throws BaseException
     */
    private void putInJMSQueue(Long reportRequestId) throws BaseException {
        if (reportRequestId == null) {
            throw new ServiceException(BizExceptionCode.RRS_021, BizExceptionCode.RRS_018_MSG);
        }

        javax.jms.Connection connection = null;
        Session jmsSession = null;
        MessageProducer messageProducer = null;

        try {
            connection = connectionFactory.createConnection();
            jmsSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            messageProducer = jmsSession.createProducer(destination);
            messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
            ObjectMessage objectMessage = jmsSession.createObjectMessage();
            objectMessage.setObject(reportRequestId);
            messageProducer.send(objectMessage);
        } catch (JMSException e) {
            throw new ServiceException(BizExceptionCode.RRS_019, BizExceptionCode.GLB_022_MSG, e);
        } finally {
            try {
                if (connection != null)
                    connection.close();
                if (messageProducer != null)
                    messageProducer.close();
                if (jmsSession != null)
                    jmsSession.close();
            } catch (JMSException e) {
                throw new ServiceException(BizExceptionCode.RRS_020, BizExceptionCode.GLB_023_MSG, e);
            }
        }
    }

    @Override
    @Permissions(value = "ems_addReportRequest")
    @BizLoggable(logAction = "INSERT", logEntityName = "REPORT_REQUEST")
    public Long save(ReportRequestVTO reportRequestVTO) throws BaseException {
        try {
            validateReportRequest(reportRequestVTO);
            ReportTO reportTO = getEmbeddedReport(Long.parseLong(reportRequestVTO.getReportId()));

            // Checking appropriate permission, which belongs to the specified report
            if (!getUserManagementService().checkUserPermission(getUserManagementService().getUserProfileTO().getUserName(), reportTO.getPermission())) {
                throw new ServiceException(BizExceptionCode.RRS_028, BizExceptionCode.RRS_028_MSG);
            }

            //  Instantiating a ReportRequestTO using given parameters
            ReportRequestTO reportRequestTO = new ReportRequestTO();
            reportRequestTO.setRequestDate(new Date());
            reportRequestTO.setState(ReportRequestState.REQUESTED);
            reportRequestTO.setType(ReportOutputType.valueOf(reportRequestVTO.getRequestOutputType()));
            reportRequestTO.setReportTO(reportTO);
            Long personID = getPersonDAO().findPersonIdByUsername(userProfileTO.getUserName());
            reportRequestTO.setPerson(new PersonTO(personID));
            if (reportRequestVTO.getJobScheduleDate() != null) {
                reportRequestTO.setJobScheduleDate(reportRequestVTO.getJobScheduleDate());
            }

            // Attaching parameters
            List<JSONObject> jsonObjects = new ArrayList<JSONObject>();

            // 1. Attaching the username parameter to the json array of the report parameters

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", "username");
            jsonObject.put("value", getUserProfileTO().getUserName());
            jsonObjects.add(jsonObject);

            // 2. Attaching user full name as a parameter to the json array of the report parameters
            PersonTO personTO = getPersonDAO().findByUsername(getUserProfileTO().getUserName());
            StringBuilder userFullName = new StringBuilder();
            jsonObject = new JSONObject();
            jsonObject.put("name", "userFullName");
            jsonObject.put("value", userFullName.append(personTO.getFirstName()).append(" ").append(personTO.getLastName()));
            jsonObjects.add(jsonObject);

            //  Removing any parameter which has no value attribute. So the default value specified at jrxml file will
            //  be used
            JSONArray params = new JSONArray(reportRequestVTO.getParameters());
            JSONArray finalParamList = new JSONArray();
            for (int i = 0; i < params.length(); i++) {
                JSONObject repParam = (JSONObject) params.get(i);
                if (EmsUtil.checkString(repParam.getString("value"))) {
                    finalParamList.put(repParam);
                }
            }

            String parameters = EmsUtil.attachToJSONArray(finalParamList, jsonObjects).toString();
            reportRequestTO.setParameters(parameters);

            // Persisting the object of type ReportRequestTO
            ReportRequestTO savedReportRequestTO = getReportRequestDAO().create(reportRequestTO);

            // Putting the object into the JMS queue
            if (savedReportRequestTO.getJobScheduleDate() == null) {
                reportRequestTO.setState(ReportRequestState.IN_PROGRESSED);
                putInJMSQueue(savedReportRequestTO.getId());
            }
            return savedReportRequestTO.getId();
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.RRS_017, BizExceptionCode.GLB_008_MSG);
        }
    }

    @Override
    @Permissions(value = "ems_deleteReportRequest")
    @BizLoggable(logAction = "DELETE", logEntityName = "REPORT_REQUEST")
    public void remove(Long[] reportRequestId) throws BaseException {

        if (!EmsUtil.checkArraySize(reportRequestId)) {
            throw new ServiceException(BizExceptionCode.RRS_024, BizExceptionCode.RRS_024_MSG);
        }

        ReportRequestDAO reportRequestDAO = getReportRequestDAO();

        List<ReportRequestTO> deletingReportsList = new ArrayList<ReportRequestTO>(reportRequestId.length);
        for (Long reportID : reportRequestId) {
            ReportRequestTO rrTO = reportRequestDAO.find(ReportRequestTO.class, reportID);
            if (rrTO.getState() == ReportRequestState.IN_PROGRESSED) {
                throw new ServiceException(BizExceptionCode.RRS_023, BizExceptionCode.RRS_023_MSG);
            }
            deletingReportsList.add(rrTO);
        }

        try {
            reportRequestDAO.delete(reportRequestId);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(BizExceptionCode.RRS_025, BizExceptionCode.RRS_025_MSG);
        }
    }

    @Override
    public Long getRequestedReportByJobCount() throws BaseException {
        return getReportRequestDAO().getRequestsToGenerateReportByJobCount();
    }

    /**
     * The method generateRequestedReportsByJob is used to generate the report, which belong to appropriate report
     * requests, via a specified job
     *
     * @throws com.gam.commons.core.BaseException
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.NEVER)
    public void generateRequestedReportsByJob(Integer from, Integer to) throws BaseException {
        List<ReportRequestTO> reportRequests = getReportRequestDAO().findRequestsToGenerateReportByJob(from, to);

        if (EmsUtil.checkListSize(reportRequests)) {
            for (ReportRequestTO reportRequest : reportRequests) {
                try {
                    updateState(reportRequest, ReportRequestState.IN_PROGRESSED);
                    reportJobLogger.info("State of the report request " + reportRequest.getId() + " changed from '" + reportRequest.getState() + "' to 'IN_PROGRESSED'");
                    putInJMSQueue(reportRequest.getId());
                    reportJobLogger.info("Report request " + reportRequest.getId() + " queued in JMS to be generated");
                } catch (Exception e) {
                    throw new ServiceException(BizExceptionCode.RRS_029, BizExceptionCode.GLB_ERR_MSG, e);
                }
            }
        }
    }

    /**
     * The method loadGeneratedReport is used to load the report belonging to an appropriate request, which is in the
     * state of ReportRequestState.Processed
     *
     * @param reportRequestId is an instance of type {@link Long}, which represents the id of a specified instance of type
     *                        {@link com.gam.nocr.ems.data.domain.ReportRequestTO}
     * @return an instance of type {@link com.gam.nocr.ems.data.domain.ReportRequestTO}
     * @throws com.gam.commons.core.BaseException
     */
    @Override
    public ReportRequestTO loadGeneratedReport(Long reportRequestId) throws BaseException {
        ReportRequestTO reportRequestTO = getReportRequestDAO().loadGeneratedReport(reportRequestId);
        if (reportRequestTO == null) {
            throw new ServiceException(BizExceptionCode.RRS_011, BizExceptionCode.RRS_011_MSG);
        }
        return reportRequestTO;
    }

    /**
     * This method is used for testing the process of generating report
     *
     * @param reportRequestTO
     * @throws com.gam.commons.core.BaseException
     */
    @Override
    public void generateRequestedReportTest(ReportRequestTO reportRequestTO) throws BaseException {
//        generateRequestedReport(reportRequestTO);
    }

    /**
     * The method generateRequestedReport is used to generate the requested report
     *
     * @param reportRequestId is an instance of type {@link ReportRequestTO}
     * @return an instance of type {@link ReportRequestTO}
     * @throws BaseException
     */
    @Override
    @javax.ejb.TransactionAttribute(TransactionAttributeType.NEVER)
    public ReportRequestTO generateRequestedReport(Long reportRequestId) throws BaseException {
        reportLogger.info("Generating report started with report request id " + reportRequestId);

        ReportRequestTO reportRequestTO = getReportRequestDAO().findRequestWithAllAttributes(reportRequestId);
        if (reportRequestTO != null) {
            Connection masterReportConnection = null;
            Connection subReportConnection = null;
            try {
            	
            	String reportingJndiName = String.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_REPORTING_JNDI_NAME,DEFAULT_REPORTING_JNDI_NAME));
            	
                Long reportId = reportRequestTO.getReportTO().getId();
                reportLogger.debug("The report request belongs to report " + reportRequestTO.getReportTO().getName());
                List<ReportFileTO> reportFileTOs = getReportFileDAO().findReportFilesByReportIdAndOutputType(reportId, reportRequestTO.getType());

                reportLogger.debug("Report definition files loaded. Number of report files : " + reportFileTOs.size());
//                List<ReportFileTO> reportFileTOs = reportRequestTO.getReportTO().getReportFiles();

                // Finding Master report along with it's sub reports
                ReportFileTO masterReportFile = null;
                List<ReportFileTO> subReportFiles = new ArrayList<ReportFileTO>();
                for (ReportFileTO reportFileTO : reportFileTOs) {
                    if (ReportFileType.MASTER_REPORT.equals(reportFileTO.getType())) {
                        masterReportFile = reportFileTO;
                    } else {
                        subReportFiles.add(reportFileTO);
                    }
                }

                // Creating an instance of type InputStream by using masterReportFileTO
                ByteArrayInputStream byteArrayInputStreamForMasterReportFile = new ByteArrayInputStream(masterReportFile.getData());

                reportLogger.debug("Parameters for report request id '" + reportRequestId + "' are " + reportRequestTO.getParameters());

                // Gathering report parameters along with values
                Map params = new HashMap<String, Object>();
                boolean bindParamsFlag = false;
                if (EmsUtil.checkString(reportRequestTO.getParameters())) {
                    params = EmsUtil.convertJsonToMap(reportRequestTO.getParameters());
                    bindParamsFlag = true;
                }

                params.put("imagesPath", "/reports/images");
                params.put("templatesPath", "/reports/templates");

                // Setting connection attribute of the report(Used in reports and subReports, which are consisted of embedded query string)
                masterReportConnection = EmsUtil.getConnectionByDataSource(reportingJndiName);
                subReportConnection = EmsUtil.getConnectionByDataSource(reportingJndiName);
                params.put(JRParameter.REPORT_CONNECTION, masterReportConnection);
                params.put("subReportConnection", subReportConnection);

                //	Adding sub report as a parameter of the master report
                try {
                    reportLogger.debug("Adding sub reports to the parameters list of master report");
                    for (ReportFileTO subReportFile : subReportFiles) {
                        ByteArrayInputStream byteArrayInputStreamForSubReport = new ByteArrayInputStream(subReportFile.getData());
                        JasperReport jasperSubReport = (JasperReport) JRLoader.loadObject(byteArrayInputStreamForSubReport);
                        params.put(subReportFile.getCaption(), jasperSubReport);

                    }
                } catch (Exception e) {
                    logger.error(BizExceptionCode.GLB_ERR_MSG, e);
                    reportLogger.error("An error occurred while adding subreports to the parameters list of master report", e);
                }

                // Check embedded query
                boolean embeddedQueryFlag = false;
                if (checkEmbeddedQuery(reportRequestTO.getReportTO())) {
                    embeddedQueryFlag = true;
                }
                JRResultSetDataSource resultSetDataSource = null;
                if (!embeddedQueryFlag) {
                    // Fetching the VLP query by using report variables
                    String queryName = fetchListName(reportRequestTO.getReportTO());
                    String query = null;
                    if (EmsUtil.checkString(queryName)) {
                        ValueListHandler vlh = EMSValueListProvider.getProvider().loadList(queryName, "main".split(","), "count".split(","), params, null, null);
                        query = vlh.getMainQueryString(bindParamsFlag);
                        // Creating JRResultSetDataSource
                        resultSetDataSource = getJRResultSetDataSource(query, reportingJndiName);
                    }
                }

                // Generating the ultimate report
                byte[] result = generateResult(reportRequestTO.getType(), byteArrayInputStreamForMasterReportFile, params, resultSetDataSource, embeddedQueryFlag);

                reportLogger.debug("Report result generated. Updating report request state to 'PROCESSED'");
                reportRequestTO.setResult(result);
                reportRequestTO.setState(ReportRequestState.PROCESSED);
                reportRequestTO.setGenerateDate(new Date());
            } catch (Exception e) {
                logger.error(BizExceptionCode.GLB_ERR_MSG, e);
                reportLogger.error("An error occurred in generating report result. Incrementing retry counter of the report request and updating its state to 'ERROR'", e);
                if (reportRequestTO.getJobScheduleDate() != null) {
                    reportRequestTO.setTryCounter(reportRequestTO.getTryCounter() + 1);
                }
                reportRequestTO.setState(ReportRequestState.ERROR);
                reportRequestTO.setGenerateDate(new Date());
            } finally {
                // Closing the master report and sub report passed connections
                try {
                    if (masterReportConnection != null) {
                        masterReportConnection.close();
                    }
                } catch (SQLException e) {
                    logger.error(BizExceptionCode.GLB_ERR_MSG, e.getMessage(), e);
                }
                try {
                    if (subReportConnection != null) {
                        subReportConnection.close();
                    }
                } catch (SQLException e) {
                    logger.error(BizExceptionCode.GLB_ERR_MSG, e.getMessage(), e);
                }
            }
        } else {
            reportLogger.warn("No report request found by identifier " + reportRequestId);
        }

        reportLogger.info("Generating report finished for report request id " + reportRequestId);
        return reportRequestTO;
    }

    /**
     * The methods updateReportRequest is used to update an instance of type {@link com.gam.nocr.ems.data.domain.ReportRequestTO}
     *
     * @param reportRequestTO is an instance of type {@link com.gam.nocr.ems.data.domain.ReportRequestTO}
     * @throws com.gam.commons.core.BaseException
     */
    @Override
    public void updateReportRequest(ReportRequestTO reportRequestTO) throws BaseException {
        if (reportRequestTO == null) {
            throw new ServiceException(BizExceptionCode.RRS_022, BizExceptionCode.RRS_022_MSG);
        }
        getReportRequestDAO().update(reportRequestTO);
    }

    /**
     * The method updateState is used to update the state of an instance of type {@link com.gam.nocr.ems.data.domain.ReportRequestTO}
     *
     * @param reportRequestTO    is an instance of type {@link com.gam.nocr.ems.data.domain.ReportRequestTO}
     * @param reportRequestState is an instance of type {@link ReportRequestState}
     * @throws com.gam.commons.core.BaseException
     */
    @Override
    @javax.ejb.TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void updateState(ReportRequestTO reportRequestTO,
                            ReportRequestState reportRequestState) throws BaseException {
        reportRequestTO.setState(reportRequestState);
        reportRequestTO.setGenerateRetryDate(new Date());
        getReportRequestDAO().update(reportRequestTO);
    }

    @Override
    public void validateReport(Long reportId,
                               String userName,
                               SystemId systemId,
                               JSONObject receivedParam) throws BaseException {
        String metadata = getReportManagementService().getMetaData(reportId, userProfileTO.getUserName(), systemId);
        HashMap<String, String> result = new HashMap<String, String>();

        try {
            JSONArray reportParamArray = new JSONArray(metadata);

            for (int j = 0; j < reportParamArray.length(); j++) {
                JSONObject jo = (JSONObject) reportParamArray.get(j);
                if (jo.get("name").equals("nocrVALIDATOR"))
                    result = EmsUtil.executeJS(String.valueOf(jo.get("value")), receivedParam);
            }
        } catch (JSONException e) {
            throw new ServiceException(BizExceptionCode.RRS_032, BizExceptionCode.GLB_008_MSG, e);
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.RRS_033, BizExceptionCode.GLB_008_MSG, e);
        }

        if (EmsUtil.checkString(result.get("success"))) {
            if ("undefined".equals(result.get("success"))
                    || "undefined".equals(result.get("message"))) {
                throw new ServiceException(BizExceptionCode.RRS_034, BizExceptionCode.GLB_008_MSG);
            } else if ("false".equals(result.get("success"))) {
                if ("undefined".equals(result.get("message")))
                    throw new ServiceException(BizExceptionCode.RRS_035, BizExceptionCode.GLB_008_MSG);
                else
                    throw new ServiceException(result.get("message"), BizExceptionCode.GLB_008_MSG);
            }
        }
    }

}
