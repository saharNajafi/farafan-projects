package com.gam.nocr.ems.web.ws;

import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.Internal;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.commons.stateprovider.StateProviderTO;
import com.gam.nocr.ems.biz.delegator.StateProviderDelegator;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.ws.SecurityContextWTO;
import com.gam.nocr.ems.data.domain.ws.StateProviderParameterWTO;
import com.gam.nocr.ems.data.domain.ws.StateProviderWTO;
import org.slf4j.Logger;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.WebFault;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides all configuration information required by CCOS in different business processes
 *
 * @author Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
@WebFault
        (
                faultBean = "com.gam.nocr.ems.web.ws.InternalException"
        )
@WebService
        (
                serviceName = "StateProviderWS",
                portName = "StateProviderWSPort",
                targetNamespace = "http://ws.web.ems.nocr.gam.com/"
        )
@SOAPBinding
        (
                style = SOAPBinding.Style.DOCUMENT,
                use = SOAPBinding.Use.LITERAL,
                parameterStyle = SOAPBinding.ParameterStyle.WRAPPED
        )
@Internal
public class StateProviderWS extends EMSWS {

    private static final Logger configLogger = BaseLog.getLogger("ccosStateProvider");

    /**
     * Given a list of configuration names and returns their corresponding values
     *
     * @param securityContextWTO The login and session information of the user
     * @param parameterWTO       Name of the configuration item to fetch its value
     * @return Collection of name-value pairs each representing a configuration item requested by caller
     * @throws InternalException
     */
    @WebMethod
    public StateProviderWTO[] get(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
                                  @WebParam(name = "stateProviderParameterWTO") StateProviderParameterWTO parameterWTO) throws InternalException {
        UserProfileTO up = super.validateRequest(securityContextWTO);

        if (parameterWTO == null) {
            configLogger.error("Unable to fetch config value. No parameterWTO object specified as input parameter in a request by user {} from workstation {}", up.getUserName(), securityContextWTO.getWorkstationID());
            EMSWebServiceFault EMSWebServiceFault = new EMSWebServiceFault(WebExceptionCode.SPW_002);
            throw new InternalException(WebExceptionCode.SPW_002_MSG, EMSWebServiceFault);
        }

        StateProviderWTO[] wtos = parameterWTO.getStateProviderWTOs();
        if (wtos == null || wtos.length == 0) {
            configLogger.warn("List of requested configuration is null or empty. So returning null as result to the request of user {} from workstation {}", up.getUserName(), securityContextWTO.getWorkstationID());
            return null;
        }

        int len = wtos.length;
        configLogger.info("Total number of configuration items requested by user {} from workstation {} is " + len, up.getUserName(), securityContextWTO.getWorkstationID());
        configLogger.debug("Following configurations are requested by user {} from workstation {}", up.getUserName(), securityContextWTO.getWorkstationID());
        ArrayList<String> stateIds = new ArrayList<String>();
        for (int i = 0; i < len; i++) {
            configLogger.debug(wtos[i].getStateId());
            stateIds.add(wtos[i].getStateId());
        }
        try {
            StateProviderDelegator dlg = new StateProviderDelegator();
            configLogger.debug("Preparing list of configuration for user {} from workstation {}", up.getUserName(), securityContextWTO.getWorkstationID());
            List<StateProviderTO> result = dlg.getState(up, parameterWTO.getModuleName(), stateIds);
            if (result == null) {
                configLogger.warn("No result returned from database. Returning null as a response to the request specified by user {} from workstation {}", up.getUserName(), securityContextWTO.getWorkstationID());
                return null;
            }
            int i = 0;
            StateProviderWTO[] resultWTOs = new StateProviderWTO[result.size()];
            configLogger.debug("The value of requested configurations are as below:");
            for (StateProviderTO to : result) {
                configLogger.debug(to.getStateId() + " : " + to.getValue());
                resultWTOs[i++] = new StateProviderWTO(to);
            }
            configLogger.debug("Preparing list of configuration for user {} from workstation {} finished", up.getUserName(), securityContextWTO.getWorkstationID());
            return resultWTOs;
        } catch (Exception ex) {
            configLogger.error("Unable to retrieve configurations requested by user {} from workstation {}", up.getUserName(), securityContextWTO.getWorkstationID());
            EMSWebServiceFault EMSWebServiceFault = new EMSWebServiceFault(WebExceptionCode.SPW_003);
            throw new InternalException(WebExceptionCode.SPW_003_MSG, EMSWebServiceFault, ex);
        }
    }

    /**
     * Stores a configuration item specified by CCOS in system configuration
     *
     * @param securityContextWTO The login and session information of the user
     * @param parameterWTO       Name and value of a configuration item to store in system
     * @throws InternalException
     * @deprecated
     */
    @WebMethod
    public void save(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
                     @WebParam(name = "stateProviderParameterWTO") StateProviderParameterWTO parameterWTO) throws InternalException {
        UserProfileTO up = super.validateRequest(securityContextWTO);
        if (parameterWTO == null) {
            EMSWebServiceFault EMSWebServiceFault = new EMSWebServiceFault(WebExceptionCode.SPW_005);
            throw new InternalException(WebExceptionCode.SPW_005_MSG, EMSWebServiceFault);
        }
        StateProviderWTO[] wtos = parameterWTO.getStateProviderWTOs();
        if (wtos == null || wtos.length == 0)
            return;
        int len = wtos.length;
        ArrayList<StateProviderTO> states = new ArrayList<StateProviderTO>();
        for (int i = 0; i < len; i++) {
            StateProviderTO to = new StateProviderTO();
            to.setStateId(wtos[i].getStateId());
            to.setValue(wtos[i].getValue());
            states.add(to);
        }
        try {
            StateProviderDelegator dlg = new StateProviderDelegator();
            dlg.setState(up, parameterWTO.getModuleName(), states);
        } catch (Exception ex) {
            EMSWebServiceFault EMSWebServiceFault = new EMSWebServiceFault(WebExceptionCode.SPW_006);
            throw new InternalException(WebExceptionCode.SPW_006_MSG, EMSWebServiceFault);
        }
    }
}
