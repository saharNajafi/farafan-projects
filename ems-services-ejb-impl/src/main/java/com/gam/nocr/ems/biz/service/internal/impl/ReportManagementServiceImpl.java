package com.gam.nocr.ems.biz.service.internal.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.AbstractService;
import com.gam.commons.core.biz.service.BizLoggable;
import com.gam.commons.core.biz.service.Permissions;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.commons.profile.ProfileManager;
import com.gam.nocr.ems.biz.service.PersonManagementService;
import com.gam.nocr.ems.biz.service.UserManagementService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.ProfileHelper;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.dao.ReportDAO;
import com.gam.nocr.ems.data.dao.ReportRequestDAO;
import com.gam.nocr.ems.data.domain.ReportFileTO;
import com.gam.nocr.ems.data.domain.ReportRequestTO;
import com.gam.nocr.ems.data.domain.ReportTO;
import com.gam.nocr.ems.data.domain.vol.ReportVTO;
import com.gam.nocr.ems.data.enums.BooleanType;
import com.gam.nocr.ems.data.enums.ReportFileType;
import com.gam.nocr.ems.data.enums.ReportRequestState;
import com.gam.nocr.ems.data.enums.SystemId;
import com.gam.nocr.ems.data.mapper.tomapper.ReportMapper;
import com.gam.nocr.ems.util.EmsUtil;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.json.JSONArray;
import org.slf4j.Logger;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gam.nocr.ems.config.EMSLogicalNames.*;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */

@Stateless(name = "ReportManagementService")
@Local(ReportManagementServiceLocal.class)
@Remote(ReportManagementServiceRemote.class)
public class ReportManagementServiceImpl extends AbstractService implements ReportManagementServiceLocal, ReportManagementServiceRemote {

    private static final String PARAM_NAME = "name";
    private static final String PARAM_DESCRIPTION = "description";
    private static final String PARAM_VALUE = "value";
    private static final String PARAM_TYPE = "type";
    private static final String PARAM_LABEL = "label";
    private static final String PARAM_PROPERTIES = "properties";

    private static final Logger logger = BaseLog.getLogger(ReportManagementServiceImpl.class);

    /**
     * Getters for DAOs
     */

    /**
     * getReportDAO
     *
     * @return an instance of type {@link ReportDAO}
     * @throws BaseException
     */
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
     * getReportRequestDAO
     *
     * @return an instance of type {@link ReportRequestDAO}
     * @throws BaseException
     */
    private ReportRequestDAO getReportRequestDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(getDaoJNDIName(DAO_REPORT_REQUEST));
        } catch (DAOFactoryException e) {
            throw new ServiceException(
                    BizExceptionCode.RMG_023,
                    BizExceptionCode.GLB_001_MSG,
                    e,
                    new String[]{EMSLogicalNames.DAO_REPORT_REQUEST});
        }
    }

    /**
     * Getters for services
     */

    /**
     * getUserManagementService
     *
     * @return an instance of type {@link PersonManagementService}
     * @throws BaseException
     */
    private UserManagementService getUserManagementService() throws BaseException {
        UserManagementService userManagementService;
        try {
            userManagementService = ServiceFactoryProvider.getServiceFactory().getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_USER), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(BizExceptionCode.RMG_016, BizExceptionCode.GLB_002_MSG, e, EMSLogicalNames.SRV_USER.split(","));
        }
        userManagementService.setUserProfileTO(userProfileTO);
        return userManagementService;
    }

    /**
     * The method validateReport is used to validate the necessary attributes of the new/update request.
     *
     * @param reportVTO is an instance of type {@link ReportVTO}, which carries the required fields of report for saving or
     *                  updating operations
     * @throws BaseException
     */
    private void validateReport(ReportVTO reportVTO) throws BaseException {
        if (reportVTO == null) {
            throw new ServiceException(BizExceptionCode.RMG_001, BizExceptionCode.GLB_018_MSG);
        }

        if (!EmsUtil.checkString(reportVTO.getName())) {
            throw new ServiceException(BizExceptionCode.RMG_019, BizExceptionCode.RMG_019_MSG);
        }
        if (!EmsUtil.checkString(reportVTO.getPermission())) {
            throw new ServiceException(BizExceptionCode.RMG_020, BizExceptionCode.RMG_020_MSG);
        }
    }

    /**
     * The method validateReportPermission is used to validate a specified permission, which belongs to the case report.
     *
     * @param username   is an instance of type {@link String}
     * @param permission is an instance of type {@link String}, which represents a specified permission
     * @return true or false
     * @throws BaseException
     */
    private boolean validateReportPermission(String username,
                                             String permission) throws BaseException {
        try {
            return getUserManagementService().checkUserPermission(username, permission);
        } catch (Exception e) {
            logger.error(BizExceptionCode.GLB_ERR_MSG, e);
        }
//        TODO: Revise the method again to find out the reason of the exception: "Error in ValueListHandler when Loading list of permissions"
        return true;
    }

    /**
     * The method isActive is used to figure out whether the selected report is active or not
     *
     * @param reportTO is an instance of type {@link ReportTO}
     * @return true or false
     */
//    private Boolean isActive(ReportTO reportTO) {
//        return reportTO.getActivateFlag();
//    }

    /**
     * The method loadJasperReport is used to load a report as an instance of type {@link JasperReport}
     *
     * @param reportTO is an instance of {@link ReportTO}
     * @return an instance of type{@link JasperReport}
     * @throws BaseException
     */
    private JasperReport loadJasperReport(ReportTO reportTO) throws BaseException {
        List<ReportFileTO> reportFileTOs = reportTO.getReportFiles();
        for (ReportFileTO reportFileTO : reportFileTOs) {
            if (ReportFileType.MASTER_REPORT.equals(reportFileTO.getType())) {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(reportFileTO.getData());
                try {
                    return (JasperReport) JRLoader.loadObject(byteArrayInputStream);
                } catch (JRException e) {
                    throw new ServiceException(BizExceptionCode.RMG_009, BizExceptionCode.RMG_009_MSG);
                }
            }
        }
        throw new ServiceException(BizExceptionCode.RMG_010, BizExceptionCode.RMG_010_MSG);
    }

    /**
     * The method convertToMap is used to convert an array of type {@link JRParameter} to a map
     *
     * @param jrParameters An array of type {@link JRParameter}
     * @return An instance of type {@link Map<Object, Object>}
     * @throws BaseException
     */
    private JSONArray convertToMap(JRParameter[] jrParameters) throws BaseException {
        if (!EmsUtil.checkArraySize(jrParameters)) {
            throw new ServiceException(BizExceptionCode.RMG_013, BizExceptionCode.RMG_013_MSG);
        }
        String userDefinedAttributesPrefix = getUserDefinedReportAttributesPrefix();

        JSONArray jsonArray = new JSONArray();

        for (JRParameter jrParameter : jrParameters) {
            if (jrParameter.getName().contains(userDefinedAttributesPrefix)) {
                Map<Object, Object> attributesMap = new HashMap<Object, Object>();
                attributesMap.put(PARAM_NAME, jrParameter.getName());
                attributesMap.put(PARAM_DESCRIPTION, jrParameter.getDescription());
                attributesMap.put(PARAM_VALUE, (jrParameter.getDefaultValueExpression() == null) ? null : jrParameter.getDefaultValueExpression().getText());
                attributesMap.put(PARAM_TYPE, jrParameter.getValueClassName());

                //  Converting parameter properties from JRPropertiesMap to java.util.Map (The JRPropertiesMap doesn't
                //  convert to a valid JSON string)
                Map<String, String> properties = new HashMap<String, String>();
                JRPropertiesMap propertiesMap = jrParameter.getPropertiesMap();
                if ((propertiesMap != null) && (propertiesMap.hasProperties())) {
                    String[] propertyNames = propertiesMap.getPropertyNames();
                    for (String propertyName : propertyNames) {
                        String value = propertiesMap.getProperty(propertyName);
                        properties.put(propertyName, value);
                    }
                }
                attributesMap.put(PARAM_PROPERTIES, properties);

                jsonArray.put(attributesMap);
            }
        }
        return jsonArray;
    }

    /**
     * The method convertToMapForCCOS is used to convert an array of type {@link JRParameter} to a map
     *
     * @param jrParameters An array of type {@link JRParameter}
     * @return An instance of type {@link Map<Object, Object>}
     * @throws BaseException
     */
    private JSONArray convertToMapForCCOS(JRParameter[] jrParameters) throws BaseException {
        if (!EmsUtil.checkArraySize(jrParameters)) {
            throw new ServiceException(BizExceptionCode.RMG_025, BizExceptionCode.RMG_013_MSG);
        }
        String userDefinedAttributesPrefix = getUserDefinedReportAttributesPrefix();

        JSONArray jsonArray = new JSONArray();

        for (JRParameter jrParameter : jrParameters) {
            Map<Object, Object> attributesMap = new HashMap<Object, Object>();
            String paramName = jrParameter.getName();
            if (EmsUtil.checkString(paramName) && paramName.contains(userDefinedAttributesPrefix)) {
                JRPropertiesMap propertiesMap = jrParameter.getPropertiesMap();
                if ((propertiesMap != null) && (propertiesMap.hasProperties())) {
                    String uiType = propertiesMap.getProperty("ui");
                    String type = propertiesMap.getProperty("type");
                    if (EmsUtil.checkString(uiType) && (uiType.equals("DATE") || uiType.equals("TEXT"))) {
                        String label = propertiesMap.getProperty("label");
                        attributesMap.put(PARAM_NAME, paramName);
                        attributesMap.put(PARAM_VALUE, jrParameter.getDefaultValueExpression() != null ? jrParameter.getDefaultValueExpression() : "");
                        attributesMap.put(PARAM_DESCRIPTION, jrParameter.getDescription() != null ? jrParameter.getDescription() : "");
                        attributesMap.put(PARAM_LABEL, label != null ? label : "");
                        attributesMap.put(PARAM_TYPE, uiType);
                    }
                    if (EmsUtil.checkString(type) && type.equals("FUNCTION")) {
                        attributesMap.put(PARAM_NAME, jrParameter.getName());
                        attributesMap.put(PARAM_VALUE, propertiesMap.getProperty("default"));
                        attributesMap.put(PARAM_DESCRIPTION, jrParameter.getDescription());
                        attributesMap.put(PARAM_TYPE, jrParameter.getValueClassName());
                    }
                }
                if (attributesMap.size() > 0)
                    jsonArray.put(attributesMap);
            }
        }
        return jsonArray;
    }

    /**
     * The method getUserDefinedReportAttributesPrefix is used to get the pre defined prefix for user defined attributes of the report
     *
     * @return an instance of type {@link String}, which represents a specified prefix
     * @throws BaseException
     */
    private String getUserDefinedReportAttributesPrefix() throws BaseException {
        try {
            ProfileManager profileManager = ProfileHelper.getProfileManager();
            String returnConfig = (String) profileManager.getProfile(ProfileKeyName.KEY_REPORT_USER_DEFINED_ATTRIBUTES, true, null, null);
            if (!EmsUtil.checkString(returnConfig)) {
                returnConfig = "nocr";
            }
            return returnConfig;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.RMG_012, BizExceptionCode.RMG_012_MSG, e);
        }
    }

    @Override
    @Permissions(value = "ems_addReport")
    @BizLoggable(logAction = "INSERT", logEntityName = "REPORT_MANAGEMENT")
    public Long save(ReportVTO reportVTO) throws BaseException {
        try {
            validateReport(reportVTO);
            ReportTO reportTO = ReportMapper.convert(reportVTO);
            ReportTO savedReportTo = getReportDAO().create(reportTO);
            return savedReportTo.getId();
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.RMG_021, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    @Permissions(value = "ems_updateReport")
    @BizLoggable(logAction = "UPDATE", logEntityName = "REPORT_MANAGEMENT")
    public Long update(ReportVTO reportVTO) throws BaseException {
        try {
            validateReport(reportVTO);
            ReportTO reportTO = ReportMapper.convert(reportVTO);
            ReportTO updatedReportTo = getReportDAO().update(reportTO);
            return updatedReportTo.getId();
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.RMG_005, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    @Permissions(value = "ems_loadReport")
    @BizLoggable(logAction = "LOAD", logEntityName = "REPORT_MANAGEMENT")
    public ReportVTO load(Long reportId) throws BaseException {
        if (reportId == null || reportId == 0) {
            throw new ServiceException(BizExceptionCode.RMG_004, BizExceptionCode.RMG_004_MSG);
        }
        try {
            ReportTO reportTO = getReportDAO().find(ReportTO.class, reportId);
            return ReportMapper.convert(reportTO);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.RMG_006, BizExceptionCode.GLB_008_MSG, e);
        }
    }


    @Override
    @Permissions(value = "ems_deleteReport")
    @BizLoggable(logAction = "DELETE", logEntityName = "REPORT_MANAGEMENT")
    public boolean remove(String reportId) throws BaseException {
        if (!EmsUtil.checkString(reportId)) {
            throw new ServiceException(BizExceptionCode.RMG_007, BizExceptionCode.RMG_007_MSG);
        }
        List<ReportRequestState> noDeletePermissionStateList = new ArrayList<ReportRequestState>();
        noDeletePermissionStateList.add(ReportRequestState.REQUESTED);
        noDeletePermissionStateList.add(ReportRequestState.IN_PROGRESSED);
        List<ReportRequestTO> reportRequestTOs = getReportRequestDAO().findByReportIdAndStates(Long.parseLong(reportId), noDeletePermissionStateList);
        if (EmsUtil.checkListSize(reportRequestTOs)) {
            throw new ServiceException(BizExceptionCode.RMG_024, BizExceptionCode.RMG_024_MSG);
        }
        getReportDAO().delete(reportRequestTOs.get(0).getReportTO());
        return true;
    }


    /**
     * The method getVariables is used to prepare the variables, which belong to an appropriate report
     *
     * @param reportTO is an instance of type {@link ReportTO}
     * @return an instance of type {@link Map<Object, Object>}, which represents the variables and their values
     * @throws BaseException
     */
    @Override
    public Map<Object, Object> getVariables(ReportTO reportTO) throws BaseException {
        /**
         * Report Variables
         */
        Map<Object, Object> variablesMap = new HashMap<Object, Object>();
        JasperReport jasperReport = loadJasperReport(reportTO);
        JRVariable[] jrVariables = jasperReport.getVariables();
        for (JRVariable jrVariable : jrVariables) {
//			TODO: Optimize the way of putting data into this map, in coming future
            try {
                variablesMap.put(jrVariable.getName(), jrVariable.getExpression().getChunks()[0].getText().split("\"")[1]);
            } catch (Exception e) {
                logger.error(BizExceptionCode.GLB_ERR_MSG, e);
                //Do nothing
            }
        }
        return variablesMap;
    }

    /**
     * The method getParameters is used to prepare the parameters, which belong to an appropriate report
     *
     * @param reportTO is an instance of type {@link ReportTO}
     * @return an instance of type {@link String}, which represents the parameters in a JSON String
     * @throws BaseException
     */
    public String getParameters(ReportTO reportTO, SystemId systemId) throws BaseException {
        try {
            JasperReport jasperReport = loadJasperReport(reportTO);
            JRParameter[] jrParameters = jasperReport.getParameters();

            if (systemId == null)
                return convertToMap(jrParameters).toString();
            else
                return convertToMapForCCOS(jrParameters).toString();
        } catch (BaseException e) {
            logger.error(e.getExceptionCode(), e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error(BizExceptionCode.RMG_011, BizExceptionCode.GLB_008_MSG, e);
            throw new ServiceException(BizExceptionCode.RMG_011, BizExceptionCode.GLB_008_MSG);
        }
    }

    /**
     * The method getMetaData is used to prepare the metadata, such as parameters or ..., which belong to an appropriate report
     *
     * @param reportId is an instance of type {@link Long}, which represents the id of a specified instance of type {@link ReportTO}
     * @param username is an instance of type {@link String}
     * @return an instance of type {@link String}, which represents the parameters in a JSON String
     * @throws BaseException
     */
    public String getMetaData(Long reportId,
                              String username,
                              SystemId systemId) throws BaseException {
        if (reportId == null) {
            throw new ServiceException(BizExceptionCode.RMG_014, BizExceptionCode.RMG_004_MSG);
        }
        if (!EmsUtil.checkString(username)) {
            throw new ServiceException(BizExceptionCode.RMG_018, BizExceptionCode.RMG_018_MSG);
        }
        ReportTO reportTO = getReportDAO().findByAllAttributes(reportId);
        if (reportTO == null) {
            throw new ServiceException(BizExceptionCode.RMG_015, BizExceptionCode.RMG_015_MSG, new String[]{reportId.toString()});
        }
//        if (!validateReportPermission(username, reportTO.getPermission())) {
//            throw new ServiceException(BizExceptionCode.RMG_017, BizExceptionCode.RMG_017_MSG);
//        }
        return getParameters(reportTO, systemId);
    }

    /**
     * The method getQueryString is used to extract the query string attribute from the report
     *
     * @param reportTO is an instance of type {@link com.gam.nocr.ems.data.domain.ReportTO}
     * @return an array of type {@link Object}, which represents the query and parameters belong to that query
     * @throws com.gam.commons.core.BaseException
     */
    @Override
    public Object[] getQueryString(ReportTO reportTO) throws BaseException {
        JasperReport jasperReport = loadJasperReport(reportTO);
        JRQuery jrQuery = jasperReport.getQuery();
        if (jrQuery != null && EmsUtil.checkArraySize(jrQuery.getChunks())) {
            return jrQuery.getChunks();
        }
        return null;
    }

    /**
     * Changes the state of given report. It would be used to enable/disable a report
     *
     * @param reportId       Identifier of the record to change its state
     * @param newReportState New state of the report
     */
    @Override
    @Permissions(value = "ems_changeReportStatus")
    @BizLoggable(logAction = "CHANGE_STATE", logEntityName = "REPORT_MANAGEMENT")
    public void changeReportState(Long reportId, BooleanType newReportState) throws BaseException {

        try {
            ReportTO reportTO = getReportDAO().find(ReportTO.class, reportId);
            reportTO.setActivateFlag(newReportState);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.RMG_022, BizExceptionCode.GLB_008_MSG, e);
        }
    }
}
