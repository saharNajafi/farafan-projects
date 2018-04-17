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
    public boolean isReliableVerInquiryRequired(
            @WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
            @WebParam(name = "WorkstationCode", targetNamespace = "")
            @XmlElement(required = true, nillable = false) String workstationCode
    ) throws InternalException, BaseException {
        UserProfileTO userProfileTO = super.validateRequest(securityContextWTO);
          return workstationInfoDelegator.isReliableVerInquiryRequired(userProfileTO, workstationCode);
    }

    @WebMethod
    public String getReliableVerByPlatform(
            @WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
            @WebParam(name = "WorkstationCode", targetNamespace = "")
            @XmlElement(required = true, nillable = false) String workstationCode,
            @WebParam(name = "ClientHardWareSpec", targetNamespace = "")
            @XmlElement(required = true, nillable = false) ClientHardWareSpecWTO clientHardWareSpec,
            @WebParam(name = "ClientNetworkConfig", targetNamespace = "")
            @XmlElement(required = true, nillable = false) ClientNetworkConfigsWTO clientNetworkConfig,
            @WebParam(name = "ClientSoftWareSpec", targetNamespace = "")
            @XmlElement(required = true, nillable = false) ClientSoftWareSpecWTO clientSoftWareSpec
    ) throws InternalException {
        UserProfileTO userProfileTO = super.validateRequest(securityContextWTO);
        String verCode = null;
        try {
           WorkstationInfoTO workstationInfoTO =
                  convertToWorkstationInfo(clientHardWareSpec, clientNetworkConfig, clientSoftWareSpec);
            verCode = workstationInfoDelegator.getReliableVerByPlatform(
                    userProfileTO, workstationCode, workstationInfoTO);
        } catch (BaseException e) {
            e.printStackTrace();
        }
        return verCode;
    }

    @WebMethod
    public String getReliableVerByPlugin(
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
            if (pluginInfoList.size() > 0){
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
        return verCode;
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

    private WorkstationInfoTO convertToWorkstationInfo(ClientHardWareSpecWTO clientHardWareSpec,
                                                       ClientNetworkConfigsWTO clientNetworkConfig,
                                                       ClientSoftWareSpecWTO clientSoftWareSpec) throws InternalException {
        WorkstationInfoTO workstationInfoTO = new WorkstationInfoTO();
        for (String macAddress : clientHardWareSpec.getMacAddressList()) {
            if (macAddress.length() == 0 || macAddress == null)
                throw new InternalException(
                        WebExceptionCode.EMSWorkstationPMService0009, new EMSWebServiceFault(WebExceptionCode.WST_009));
            if (macAddress.length() > 17)
                throw new InternalException(
                        WebExceptionCode.EMSWorkstationPMService0011, new EMSWebServiceFault(WebExceptionCode.WST_011));
            if (macAddress.length() < 17)
                throw new InternalException(
                        WebExceptionCode.EMSWorkstationPMService0010, new EMSWebServiceFault(WebExceptionCode.WST_010));
        }
        workstationInfoTO.setMacAddressList(String.valueOf(clientHardWareSpec.getMacAddressList()));
        workstationInfoTO.setCpuType(clientHardWareSpec.getCpuType());
        workstationInfoTO.setRamCapacity(clientHardWareSpec.getRamCapacity());
        if (clientSoftWareSpec.getOsVersion() == null)
        throw new InternalException(
                WebExceptionCode.EMSWorkstationPMService0012, new EMSWebServiceFault(WebExceptionCode.WST_012));
        workstationInfoTO.setOsVersion(clientSoftWareSpec.getOsVersion());
        if (clientSoftWareSpec.getDotNetwork45Installed() != null)
            workstationInfoTO.setHasDotnetFramwork45(Short.parseShort(
                    String.valueOf(((clientSoftWareSpec.getDotNetwork45Installed()) ? 1 : 0))));
        if (clientSoftWareSpec.getIs64BitOs() != null)
            workstationInfoTO.setIs64bitOs(Short.parseShort(
                    String.valueOf(((clientSoftWareSpec.getIs64BitOs()) ? 1 : 0))));
        for (String ipAddress : clientNetworkConfig.getIpAddressList()) {
            if (!Utils.isIPValid(ipAddress))
                throw new InternalException(
                        WebExceptionCode.EMSWorkstationPMService0008, new EMSWebServiceFault(WebExceptionCode.WST_008));
        }
        workstationInfoTO.setIpAddressList(String.valueOf(clientNetworkConfig.getIpAddressList()));
        if (clientNetworkConfig.getComputerName() == null)
            throw new InternalException(
                    WebExceptionCode.EMSWorkstationPMService0014, new EMSWebServiceFault(WebExceptionCode.WST_014));
        workstationInfoTO.setComputerName(clientNetworkConfig.getComputerName());
        if (clientNetworkConfig.getUserName() == null)
            throw new InternalException(
                    WebExceptionCode.EMSWorkstationPMService0013, new EMSWebServiceFault(WebExceptionCode.WST_013));
        workstationInfoTO.setUsername(clientNetworkConfig.getUserName());
        workstationInfoTO.setGateway(clientNetworkConfig.getGateway());
        workstationInfoTO.setGatherState((short) 0);
        return workstationInfoTO;
    }
}
