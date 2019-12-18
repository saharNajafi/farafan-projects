package com.gam.nocr.ems.web.ws;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.Internal;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.delegator.WorkstationInfoDelegator;
import com.gam.nocr.ems.biz.delegator.WorkstationPluginsDelegator;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.WorkstationInfoTO;
import com.gam.nocr.ems.data.domain.WorkstationPluginsTO;
import com.gam.nocr.ems.data.domain.ws.*;
import com.gam.nocr.ems.util.EmsUtil;
import com.gam.nocr.ems.util.Utils;
import org.slf4j.Logger;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.ws.WebFault;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 11/15/17.
 */
@WebFault(faultBean = "com.gam.nocr.ems.web.ws.InternalException")
@WebService(serviceName = "EmsWorkStationPlatformManagementWS",
        portName = "EmsWorkStationPlatformManagementPort", targetNamespace = "http://ws.web.ems.nocr.gam.com/")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL,
        parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@Internal
public class EmsWorkStationPlatformManagementWS extends EMSWS {

    private WorkstationInfoDelegator workstationInfoDelegator = new WorkstationInfoDelegator();
    private WorkstationPluginsDelegator workstationPluginsDelegator = new WorkstationPluginsDelegator();
    private static final Logger workstationInfoLogger = BaseLog.getLogger("WorkstationInfoLogger");
    private static final String INVALID_PARAM = "unknown";

    @WebMethod
    public boolean isWorkstationInfoRequired(
            @WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
            @WebParam(name = "WorkstationCode", targetNamespace = "")
            @XmlElement(required = true, nillable = false) String workstationCode
    ) throws InternalException {
        try {
            UserProfileTO userProfileTO = super.validateRequest(securityContextWTO);
            return workstationInfoDelegator.workstationInfoRequired(userProfileTO, workstationCode);
        } catch (BaseException e) {
            workstationInfoLogger.error(e.getMessage() + ":" + e.getMessage(), e);
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(
                    e.getExceptionCode()), e);
        } catch (Exception e) {
            workstationInfoLogger.error("Unhandled Exception In Calling Web service :", e);
            throw new InternalException(
                    WebExceptionCode.WST_009_MSG, new EMSWebServiceFault(WebExceptionCode.WST_009), e);
        }
    }

    @WebMethod
    public void registerWorkstationInfo(
            @WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
            @WebParam(name = "WorkstationCode", targetNamespace = "")
            @XmlElement(required = true, nillable = false) String workstationCode,
            @WebParam(name = "WorkstationInfoWTO", targetNamespace = "")
            @XmlElement(required = true, nillable = false) WorkstationInfoWTO workstationInfoWTO) throws InternalException {
        UserProfileTO userProfileTO = super.validateRequest(securityContextWTO);
        try {
            WorkstationInfoTO workstationInfoTO = convertToWorkstationInfo(workstationInfoWTO);
            workstationInfoDelegator.registerWorkstationInfo(
                    userProfileTO, workstationCode, workstationInfoTO);
        } catch (BaseException e) {
            workstationInfoLogger.error(e.getExceptionCode() + ":" + e.getMessage(), e);
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(
                    e.getExceptionCode()), e);
        } catch (Exception e) {
            workstationInfoLogger.error("Unhandled Exception In Calling Web service :", e);
            throw new InternalException(
                    WebExceptionCode.WST_010_MSG, new EMSWebServiceFault(WebExceptionCode.WST_010), e);
        }
    }

    @WebMethod
    public void registerWorkstationPlugins(
            @WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
            @WebParam(name = "WorkstationCode", targetNamespace = "")
            @XmlElement(required = true, nillable = false) String workstationCode,
            @WebParam(name = "PluginInfo", targetNamespace = "")
            @XmlElement(required = true, nillable = false) List<PluginInfoWTO> pluginInfoList
    ) throws InternalException {
        UserProfileTO userProfileTO = super.validateRequest(securityContextWTO);
        List<WorkstationPluginsTO> workstationPluginsList = new ArrayList<WorkstationPluginsTO>();
        try {
            if (pluginInfoList.size() > 0) {
                for (PluginInfoWTO pluginInfo : pluginInfoList) {
                    WorkstationPluginsTO workstationPlugins = new WorkstationPluginsTO();
                    workstationPlugins.setPluginName(pluginInfo.getPluginName());
                    workstationPlugins.setState(Short.valueOf(pluginInfo.getState()));
                    workstationPluginsList.add(workstationPlugins);
                }
            }
            workstationPluginsDelegator.registerWorkstationPlugins(
                    userProfileTO, workstationCode, workstationPluginsList);
        } catch (BaseException e) {
            workstationInfoLogger.error(e.getExceptionCode() + ":" + e.getMessage(), e);
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(
                    e.getExceptionCode()), e);
        } catch (Exception e) {
            workstationInfoLogger.error("Unhandled Exception In Calling Web service :", e);
            throw new InternalException(
                    WebExceptionCode.WST_011_MSG, new EMSWebServiceFault(WebExceptionCode.WST_011), e);
        }
    }

    private WorkstationInfoTO convertToWorkstationInfo(WorkstationInfoWTO workstationInfoWTO) throws InternalException {
        WorkstationInfoTO workstationInfoTO = new WorkstationInfoTO();

        //1-cpu info==================================================================================================================
        if (!EmsUtil.checkString(workstationInfoWTO.getCpuInfo())) {
            //throw new InternalException(WebExceptionCode.WST_018_MSG, new EMSWebServiceFault(WebExceptionCode.WST_018));
            workstationInfoTO.setCpuInfo(INVALID_PARAM);
        } else {
            workstationInfoTO.setCpuInfo(workstationInfoWTO.getCpuInfo());
        }

        //2-.Operation system version====================================================================================================
        if (!EmsUtil.checkString(workstationInfoWTO.getOsVersion())) {
            //throw new InternalException(WebExceptionCode.WST_012_MSG, new EMSWebServiceFault(WebExceptionCode.WST_012));
            workstationInfoTO.setOsVersion(INVALID_PARAM);
        } else {
            workstationInfoTO.setOsVersion(workstationInfoWTO.getOsVersion());
        }

        //3-.net framework============================================================================================================
        if (workstationInfoWTO.getHasDotnetFramwork45() == null) {
            //throw new InternalException(WebExceptionCode.WST_019_MSG, new EMSWebServiceFault(WebExceptionCode.WST_019));
            workstationInfoTO.setHasDotnetFramwork45(INVALID_PARAM);
        } else {
            workstationInfoTO.setHasDotnetFramwork45(String.valueOf(workstationInfoWTO.getHasDotnetFramwork45()));
        }

        //4-memory capacity===================================================
        if (!EmsUtil.checkString(workstationInfoWTO.getMemoryCapacity())) {
            //throw new InternalException(WebExceptionCode.WST_016_MSG, new EMSWebServiceFault(WebExceptionCode.WST_016));
            workstationInfoTO.setMemoryCapacity(INVALID_PARAM);
        } else {
            workstationInfoTO.setMemoryCapacity(workstationInfoWTO.getMemoryCapacity().trim());
        }

        //5-ccos version======================================
        if (!EmsUtil.checkString(workstationInfoWTO.getCcosVersion())) {
            //throw new InternalException(WebExceptionCode.WST_017_MSG, new EMSWebServiceFault(WebExceptionCode.WST_017));
            workstationInfoTO.setCcosVersion(INVALID_PARAM);
        } else {
            workstationInfoTO.setCcosVersion(workstationInfoWTO.getCcosVersion().trim());
        }

        //5-ip addresses=============================================================================================================
        if (!EmsUtil.checkString(workstationInfoWTO.getIpAddress()) || !Utils.isIPValid(workstationInfoWTO.getIpAddress())) {
            //throw new InternalException(WebExceptionCode.WST_008_MSG, new EMSWebServiceFault(WebExceptionCode.WST_008));
            workstationInfoTO.setIpAddress(INVALID_PARAM);
        } else {
            workstationInfoTO.setIpAddress(String.valueOf(workstationInfoWTO.getIpAddress()));
        }

        //6-username==================================================================================================================
        if (!EmsUtil.checkString(workstationInfoWTO.getUsername())) {
            //throw new InternalException(WebExceptionCode.WST_013_MSG, new EMSWebServiceFault(WebExceptionCode.WST_013));
            workstationInfoTO.setUsername(INVALID_PARAM);
        } else {
            workstationInfoTO.setUsername(workstationInfoWTO.getUsername());
        }

        //7-data as json==============================================================================================================
        //it could be null!
        workstationInfoTO.setAdditionalInfoAsJson(workstationInfoWTO.getAdditionalInfoAsJson());

        //8-last modified date========================================================================================================
        workstationInfoTO.setLastModifiedDate(new Date());

        //9-gather state==============================================================================================================
        workstationInfoTO.setGatherState(false);

        return workstationInfoTO;
    }
}

