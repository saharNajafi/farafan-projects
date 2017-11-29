package com.gam.nocr.ems.web.ws;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Internal;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.delegator.WorkstationDelegator;
import com.gam.nocr.ems.biz.delegator.WorkstationInfoDelegator;
import com.gam.nocr.ems.biz.delegator.WorkstationPluginsDelegator;
import com.gam.nocr.ems.data.domain.vol.ClientHardWareSpecVTO;
import com.gam.nocr.ems.data.domain.vol.ClientNetworkConfigsVTO;
import com.gam.nocr.ems.data.domain.vol.ClientSoftWareSpecVTO;
import com.gam.nocr.ems.data.domain.vol.PluginInfoVTO;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.ws.WebFault;
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
public class EmsWorkStationPlatformManagementWS {
    private WorkstationInfoDelegator workstationInfoDelegator = new WorkstationInfoDelegator();
    private WorkstationPluginsDelegator workstationPluginsDelegator = new WorkstationPluginsDelegator();

    @WebMethod
    public Boolean isReliableVerInquiryRequired(
            @WebParam(name = "WorkstationCode", targetNamespace = "")
            @XmlElement(required = true, nillable = false) String workstationCode
    ) throws InternalException {
        UserProfileTO userProfileTO = null;
        Boolean result = false;
        try {
             result = workstationInfoDelegator.isReliableVerInquiryRequired(userProfileTO, workstationCode);
        } catch (BaseException e) {
            e.printStackTrace();
        }
    return result;
    }

    @WebMethod
    public String getReliableVerByPlatform(
            @WebParam(name = "WorkstationCode", targetNamespace = "")
            @XmlElement(required = true, nillable = false) String workstationCode,
            @WebParam(name = "ClientHardWareSpec", targetNamespace = "")
            @XmlElement(required = true, nillable = false) ClientHardWareSpecVTO clientHardWareSpec,
            @WebParam(name = "ClientNetworkConfig", targetNamespace = "")
            @XmlElement(required = true, nillable = false) ClientNetworkConfigsVTO clientNetworkConfig,
            @WebParam(name = "ClientSoftWareSpec", targetNamespace = "")
            @XmlElement(required = true, nillable = false) ClientSoftWareSpecVTO clientSoftWareSpec
    ) throws InternalException {
        UserProfileTO userProfileTO = null;
        String verCode = null;
        try {
            verCode = workstationInfoDelegator.getReliableVerByPlatform(
                    userProfileTO, workstationCode, clientHardWareSpec, clientNetworkConfig, clientSoftWareSpec);
        } catch (BaseException e) {
            e.printStackTrace();
        }
        return verCode;
    }

    @WebMethod
    public String getReliableVerByPlugin(
            @WebParam(name = "WorkstationCode", targetNamespace = "")
            @XmlElement(required = true, nillable = false) String workstationCode,
            @WebParam(name = "PluginInfo", targetNamespace = "")
            @XmlElement(required = true, nillable = false) List<PluginInfoVTO> pluginInfoList
    ) throws InternalException {
        UserProfileTO userProfileTO = null;
        String verCode = null;
        try {
            verCode = workstationPluginsDelegator.getReliableVerByPlugin(userProfileTO, workstationCode, pluginInfoList);
        } catch (BaseException e) {
            e.printStackTrace();
        }
        return verCode;
    }

    @WebMethod
    public List<String> getCompatibleClientVerList() throws InternalException {
        UserProfileTO userProfileTO = null;
        List<String> verCode = null;
        try {
            verCode = workstationInfoDelegator.getCompatibleClientVerList(userProfileTO);
        } catch (BaseException e) {
            e.printStackTrace();
        }
        return verCode;
    }

}
