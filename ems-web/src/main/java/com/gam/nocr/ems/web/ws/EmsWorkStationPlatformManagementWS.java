package com.gam.nocr.ems.web.ws;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Internal;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.delegator.WorkstationInfoDelegator;
import com.gam.nocr.ems.biz.delegator.WorkstationPluginsDelegator;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.WorkstationInfoTO;
import com.gam.nocr.ems.data.domain.WorkstationPluginsTO;
import com.gam.nocr.ems.data.domain.ws.*;
import com.gam.nocr.ems.util.Utils;

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

    @WebMethod
    public boolean isWorkstationInfoRequired(
            @WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
            @WebParam(name = "WorkstationCode", targetNamespace = "")
            @XmlElement(required = true, nillable = false) String workstationCode
    ) throws InternalException, BaseException {
        UserProfileTO userProfileTO = super.validateRequest(securityContextWTO);
        return workstationInfoDelegator.isReliableVerInquiryRequired(userProfileTO, workstationCode);
    }

    @WebMethod
    public void registerWorkstationInfo(
            @WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
            @WebParam(name = "WorkstationCode", targetNamespace = "")
            @XmlElement(required = true, nillable = false) String workstationCode,
            @WebParam(name = "ClientWorkstationInfo", targetNamespace = "")
            @XmlElement(required = true, nillable = false) ClientWorkstationInfo clientWorkstationInfo) throws InternalException {
        UserProfileTO userProfileTO = super.validateRequest(securityContextWTO);
        String verCode = null;
        try {
            WorkstationInfoTO workstationInfoTO = convertToWorkstationInfo(clientWorkstationInfo);
            verCode = workstationInfoDelegator.getReliableVerByPlatform(
                    userProfileTO, workstationCode, workstationInfoTO);
        } catch (BaseException e) {
            e.printStackTrace();
        }
        //return verCode;
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
        String verCode = null;
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
            verCode = workstationPluginsDelegator.getReliableVerByPlugin(
                    userProfileTO, workstationCode, workstationPluginsList);
        } catch (BaseException e) {
            throw new InternalException(e.getExceptionCode(), new EMSWebServiceFault(e.getMessage()), e);
        }
        return;
    }


    @WebMethod
    public List<String> getCompatibleClientVerList(
            @WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO
    ) throws InternalException {
        UserProfileTO userProfileTO = super.validateRequest(securityContextWTO);
        List<String> verCode = null;
        try {
            verCode = workstationInfoDelegator.getCompatibleClientVerList(userProfileTO);
        } catch (BaseException e) {
            e.printStackTrace();
        }
        return verCode;
    }

    private WorkstationInfoTO convertToWorkstationInfo(ClientWorkstationInfo clientWorkstationInfo) throws InternalException {
        WorkstationInfoTO workstationInfoTO = new WorkstationInfoTO();

        //1-cpu info==================================================================================================================
        if (clientWorkstationInfo.getCpuInfo() == null || clientWorkstationInfo.getCpuInfo().trim().isEmpty()) {
            throw new InternalException(WebExceptionCode.EMSWorkstationPMService0018, new EMSWebServiceFault(WebExceptionCode.WST_018));
        } else {
            workstationInfoTO.setCpuInfo(clientWorkstationInfo.getCpuInfo());
        }

        //2-.Operation system version====================================================================================================
        if (clientWorkstationInfo.getOsVersion() == null || clientWorkstationInfo.getOsVersion().trim().isEmpty()) {
            throw new InternalException(WebExceptionCode.EMSWorkstationPMService0012, new EMSWebServiceFault(WebExceptionCode.WST_012));
        } else {
            workstationInfoTO.setOsVersion(clientWorkstationInfo.getOsVersion());
        }

        //3-.net framework============================================================================================================
        if (clientWorkstationInfo.getHasDotnetFramwork45() == null) {
            throw new InternalException(WebExceptionCode.EMSWorkstationPMService0019, new EMSWebServiceFault(WebExceptionCode.WST_019));
        } else {
            workstationInfoTO.setHasDotnetFramwork45(Short.parseShort(String.valueOf(((clientWorkstationInfo.getHasDotnetFramwork45()) ? 1 : 0))));
        }

        //4-memory capacity===================================================
        if (clientWorkstationInfo.getMemoryCapacity() == null || clientWorkstationInfo.getMemoryCapacity().trim().isEmpty()) {
            throw new InternalException(WebExceptionCode.EMSWorkstationPMService0016, new EMSWebServiceFault(WebExceptionCode.WST_016));
        } else {
            workstationInfoTO.setMemoryCapacity(clientWorkstationInfo.getMemoryCapacity().trim());
        }

        //5-ccos version======================================
        if (clientWorkstationInfo.getCcosVersion() == null || clientWorkstationInfo.getCcosVersion().trim().isEmpty()) {
            throw new InternalException(WebExceptionCode.EMSWorkstationPMService0017, new EMSWebServiceFault(WebExceptionCode.WST_017));
        } else {
            workstationInfoTO.setCcosVersion(clientWorkstationInfo.getCcosVersion().trim());
        }

        //5-ip addresses=============================================================================================================
        if (clientWorkstationInfo.getIpAddress() == null || !Utils.isIPValid(clientWorkstationInfo.getIpAddress())) {
            throw new InternalException(WebExceptionCode.EMSWorkstationPMService0008, new EMSWebServiceFault(WebExceptionCode.WST_008));
        } else {
            workstationInfoTO.setIpAddress(String.valueOf(clientWorkstationInfo.getIpAddress()));
        }

        //6-username==================================================================================================================
        if (clientWorkstationInfo.getUsername() == null) {
            throw new InternalException(WebExceptionCode.EMSWorkstationPMService0013, new EMSWebServiceFault(WebExceptionCode.WST_013));
        } else {
            workstationInfoTO.setUsername(clientWorkstationInfo.getUsername());
        }

        //7-data as json==============================================================================================================
        //it could be null!
        workstationInfoTO.setAdditionalInfoAsJson(clientWorkstationInfo.getAdditionalInfoAsJson());

        //8-last modified date========================================================================================================
        workstationInfoTO.setLastModifiedDate(new Date());

        //9-gather state==============================================================================================================
        workstationInfoTO.setGatherState((short) 0);

        return workstationInfoTO;
    }
}

