package com.gam.nocr.ems.web.ws;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Internal;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.delegator.WorkstationInfoDelegator;
import com.gam.nocr.ems.biz.delegator.WorkstationPluginsDelegator;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.WorkstationInfoTO;
import com.gam.nocr.ems.data.domain.ws.*;
import com.gam.nocr.ems.util.EmsUtil;
import com.gam.nocr.ems.util.Utils;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.ws.WebFault;
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

    private WorkstationInfoTO convertToWorkstationInfo(ClientWorkstationInfo clientWorkstationInfo) throws InternalException {
        WorkstationInfoTO workstationInfoTO = new WorkstationInfoTO();

        //1-cpu data==================================================================================================================
        workstationInfoTO.setCpuType(clientWorkstationInfo.getCpuType());

        //2-.Operation system data====================================================================================================
        if (clientWorkstationInfo.getOsVersion() == null)
            throw new InternalException(
                    WebExceptionCode.EMSWorkstationPMService0012, new EMSWebServiceFault(WebExceptionCode.WST_012));
        workstationInfoTO.setOsVersion(clientWorkstationInfo.getOsVersion());

        //3-.net framework============================================================================================================
        if (clientWorkstationInfo.getHasDotnetFramwork45() != null)
            workstationInfoTO.setHasDotnetFramwork45(Short.parseShort(
                    String.valueOf(((clientWorkstationInfo.getHasDotnetFramwork45()) ? 1 : 0))));

        //4-is 64 bit=================================================================================================================
        if (clientWorkstationInfo.getIs64bitOs() != null)
            workstationInfoTO.setIs64bitOs(Short.parseShort(String.valueOf(((clientWorkstationInfo.getIs64bitOs()) ? 1 : 0))));

        //5-ip addresses=============================================================================================================
        for (String ipAddress : clientWorkstationInfo.getIpAddressList()) {
            if (!Utils.isIPValid(ipAddress))
                throw new InternalException(WebExceptionCode.EMSWorkstationPMService0008, new EMSWebServiceFault(WebExceptionCode.WST_008));
        }
        workstationInfoTO.setIpAddressList(String.valueOf(clientWorkstationInfo.getIpAddressList()));

        //6-username==================================================================================================================
        if (clientWorkstationInfo.getUsername() == null)
            throw new InternalException(WebExceptionCode.EMSWorkstationPMService0013, new EMSWebServiceFault(WebExceptionCode.WST_013));
        workstationInfoTO.setUsername(clientWorkstationInfo.getUsername());

        //7-data as json==============================================================================================================
        if (clientWorkstationInfo.getDataAsJson() != null && !clientWorkstationInfo.getDataAsJson().trim().isEmpty()) {
            if (EmsUtil.isJSONValid(clientWorkstationInfo.getDataAsJson())) {
                workstationInfoTO.setDataAsJson(clientWorkstationInfo.getDataAsJson());
            } else {
                throw new InternalException(WebExceptionCode.EMSWorkstationPMService0015, new EMSWebServiceFault(WebExceptionCode.WST_015));
            }
        }

        //8-last modified date========================================================================================================
        workstationInfoTO.setLastModifiedDate(new Date());

        //9-gather state==============================================================================================================
        workstationInfoTO.setGatherState((short) 0);

        return workstationInfoTO;
    }
}

