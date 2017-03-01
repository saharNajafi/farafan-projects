package com.gam.nocr.ems.web.ws;

import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.Internal;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.commons.listreader.GeneralListService;
import com.gam.commons.listreader.Parameter;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.ws.ListFilterWTO;
import com.gam.nocr.ems.data.domain.ws.ListParameterWTO;
import com.gam.nocr.ems.data.domain.ws.SecurityContextWTO;
import org.slf4j.Logger;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.WebFault;
import java.util.HashMap;

/**
 * The CCOS calls the only method in this web service in order to fetch a list of items. It could be a list of users,
 * card requests, workstations, etc. Actually it delegates all request to the
 * {@link com.gam.commons.listreader.GeneralListService} class to fetch the requested list (in JSON) to be displayed
 * in CCOS grids
 *
 * @author Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */

@WebFault
        (
                faultBean = "com.gam.nocr.ems.web.ws.InternalException"
        )
@WebService
        (
                serviceName = "ListWS",
                portName = "ListWSPort",
                targetNamespace = "http://ws.web.ems.nocr.gam.com/"
        )
@SOAPBinding
        (
                style = SOAPBinding.Style.DOCUMENT,
                use = SOAPBinding.Use.LITERAL,
                parameterStyle = SOAPBinding.ParameterStyle.WRAPPED
        )
@Internal
public class ListServiceWS extends EMSWS {

    private static final Logger logger = BaseLog.getLogger(ListServiceWS.class);

    /**
     * Given a list name and required parameters, fetches the list contents and sends them back to the caller as a JSON
     * string
     *
     * @param securityContextWTO The login and session information of the user
     * @param listName      Name of a list configured in List Reader
     * @param parameters    List of name-value parameters that may be required in fetching the result
     *                      (e.g. userID, depName, etc.)
     * @param filters       List of name-value items that should be used as a filter to limit the results. It's actually
     *                      the collection of search strings specified by user
     * @param sort          Name of a field in result list that the final result should be ordered by it
     * @return              JSON representation of list items
     * @throws InternalException
     */
    @WebMethod
    public String fetchList(
            @WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
            @WebParam(name = "listName") String listName,
            @WebParam(name = "parameters") ListParameterWTO[] parameters,
            @WebParam(name = "filters") ListFilterWTO[] filters,
            @WebParam(name = "sort") String sort) throws InternalException {

        UserProfileTO up = super.validateRequest(securityContextWTO);

        logger.info("Fetch list called from CCOS by following parameters: ");
        logger.info("  listName: " + listName);

        Parameter[] _parameters = null;
        if (parameters != null) {
            _parameters = new Parameter[parameters.length];
            int i = 0;
            logger.info("  parameters:");
            for (ListParameterWTO wto : parameters){
                logger.info(wto.getKey() + " - " + wto.getValue());
                _parameters[i++] = new Parameter(wto.getKey(), wto.getValue());
            }
        }
        HashMap<String, Object> _filters = null;
        if (filters != null) {
            logger.info("  filters:");
            _filters = new HashMap<String, Object>();
            for (ListFilterWTO wto : filters){
                logger.info(wto.getKey() + " - " + wto.getValue());
                _filters.put(wto.getKey(), wto.getValue());
            }
        }
        GeneralListService service = null;
        try {
            service = new GeneralListService(up, listName, _parameters, _filters, sort);
        } catch (Exception ex) {
            EMSWebServiceFault EMSWebServiceFault = new EMSWebServiceFault(WebExceptionCode.LSW_002);
            throw new InternalException(WebExceptionCode.LSW_002_MSG, EMSWebServiceFault, ex);
        }
        try {
            String result = service.execute();

            return result;
        } catch (Exception ex) {
			EMSWebServiceFault EMSWebServiceFault;
			String exceptionMsg = "\n" + ex.getMessage();
			if (ex.getCause() != null)
				exceptionMsg += "\n" + ex.getCause().getMessage();

			if(exceptionMsg.toLowerCase().contains("permission")){
				EMSWebServiceFault = new EMSWebServiceFault(WebExceptionCode.LSW_004);
				throw new InternalException(WebExceptionCode.LSW_004_MSG + exceptionMsg, EMSWebServiceFault, ex);

			}else{
				EMSWebServiceFault = new EMSWebServiceFault(WebExceptionCode.LSW_003);
				throw new InternalException(WebExceptionCode.LSW_003_MSG + exceptionMsg, EMSWebServiceFault, ex);
			}
        }
    }
}
