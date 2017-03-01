package com.gam.nocr.ems.web.ws;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.Internal;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.delegator.ReportManagementDelegator;
import com.gam.nocr.ems.biz.delegator.ReportRequestDelegator;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.ReportRequestTO;
import com.gam.nocr.ems.data.domain.vol.ReportRequestVTO;
import com.gam.nocr.ems.data.domain.ws.ReportRequestWTO;
import com.gam.nocr.ems.data.domain.ws.SecurityContextWTO;
import com.gam.nocr.ems.data.enums.SystemId;
import com.gam.nocr.ems.data.mapper.tomapper.ReportRequestMapper;
import com.gam.nocr.ems.util.EmsUtil;
import gampooya.tools.date.DateUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.WebFault;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Encapsulates services required by CCOS to manage reports (request report, delete report, download result, etc.)
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
@WebFault(faultBean = "com.gam.nocr.ems.web.ws.InternalException")
@WebService(serviceName = "ReportWS", portName = "ReportWSPort", targetNamespace = "http://ws.web.ems.nocr.gam.com/")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@Internal
public class ReportWS extends EMSWS {

    static final Logger logger = BaseLog.getLogger(ReportWS.class);

    private ReportRequestDelegator reportRequestDelegator = new ReportRequestDelegator();

    /**
     * Given an identifier of a {@link com.gam.nocr.ems.data.domain.ReportTO} and returns its metadata (as mentioned in
     * report implementation document) containing parameter list, their labels, etc. This service would be used by CCOS
     * to generate the report parameter window
     *
     * @param securityContextWTO The login and session information of the user
     * @param reportId           Identifier of the report definition to fetch its parameters and other metadata
     * @return                   Report metadata in JSON format
     * @throws InternalException
     */
    @WebMethod
    public String fetchReportMetaData(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
                                      @WebParam(name = "reportId") Long reportId) throws InternalException {
        UserProfileTO userProfileTO = super.validateRequest(securityContextWTO);

        try {
            return new ReportManagementDelegator().getMetaData(userProfileTO, reportId, SystemId.CCOS);
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode(), e.getArgs()), e);
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RPW_001_MSG, new EMSWebServiceFault(WebExceptionCode.RPW_001), e);
        }
    }

    /**
     * When a user requests a new report, the CCOS notifies EMS about report request by calling this service.
     *
     * @param securityContextWTO The login and session information of the user
     * @param reportRequestWTO   Detailed information about user's request (requested report, parameters, etc.)
     * @return                   The identifier of registered report request
     * @throws InternalException
     */
    @WebMethod
    public Long submitReportRequest(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
                                    @WebParam(name = "reportRequestWTO") ReportRequestWTO reportRequestWTO) throws InternalException {
        UserProfileTO userProfileTO = super.validateRequest(securityContextWTO);

        try {
            Map<Object, Object> map = new HashMap<Object, Object>();
            ReportRequestVTO reportRequestVTO = ReportRequestMapper.convert(reportRequestWTO);

            // Adding depId and depName to the parameters, which sent by CCOS
            JSONArray jsonArray = new JSONArray(reportRequestVTO.getParameters());

            // Converting the date format, which sent via CCOS
            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject jo = (JSONObject) jsonArray.get(j);
                if (jo.get("type").equals("DATE")) {
                    try {
                        Object name = jo.get("name");
                        if (!EmsUtil.checkString((String) jo.get("value"))) {
                            if ("nocrFromDate".equals(name)) {
                                // set to the date, which the project started
                                jo.put("value", "2013/08/24");
                            } else if ("nocrToDate".equals(name)) {
                                // set to the current date
                                jo.put("value", DateUtil.convert(new Date(), DateUtil.GREGORIAN));
                            }
                        } else {
                            String OLD_FORMAT = "mm/dd/yyyy";
                            String NEW_FORMAT = "yyyy/mm/dd";
                            SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
                            Date d = sdf.parse((String) jo.get("value"));
                            sdf.applyPattern(NEW_FORMAT);
                            jo.put("value", sdf.format(d));
                        }
                    } catch (ParseException e1) {
                        logger.error(WebExceptionCode.GLB_ERR_MSG, e1);
                    }
                }
                map.put(jo.get("name"), jo.get("value"));
            }

            JSONObject receivedParam = EmsUtil.toJSONObject(map);

            reportRequestDelegator.validateReport(userProfileTO,
                    Long.valueOf(reportRequestVTO.getReportId()), SystemId.CCOS, receivedParam);

            JSONObject DepIdJsonObject = new JSONObject();
            DepIdJsonObject.put("name", "nocrDep");
            DepIdJsonObject.put("value", userProfileTO.getDepID());
            JSONObject depNameJsonObject = new JSONObject();
            depNameJsonObject.put("name", "nocrDepName");
            depNameJsonObject.put("value", userProfileTO.getDepName());
            jsonArray.put(DepIdJsonObject);
            jsonArray.put(depNameJsonObject);

            reportRequestVTO.setParameters(jsonArray.toString());
            return reportRequestDelegator.save(userProfileTO, reportRequestVTO);
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode(), e.getArgs()), e);
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RPW_002_MSG, new EMSWebServiceFault(WebExceptionCode.RPW_002), e);
        }
    }

    /**
     * Removes a report request from user's report list. When user tries to delete an old report request from its
     * reports list, a call to this service would be made by CCOS to remove given report request
     *
     * @param securityContextWTO The login and session information of the user
     * @param reportRequestId    Identifier of a report request to remove
     * @return                   Always true (not used)
     * @throws InternalException
     */
    @WebMethod
    public Boolean removeReport(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
                                @WebParam(name = "reportRequestId") Long reportRequestId) throws InternalException {
        UserProfileTO userProfileTO = super.validateRequest(securityContextWTO);

        try {
            reportRequestDelegator.remove(userProfileTO, new Long[]{reportRequestId});
            return true;
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode(), e.getArgs()), e);
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RPW_003_MSG, new EMSWebServiceFault(WebExceptionCode.RPW_003), e);
        }
    }

    /**
     * Given a report request identifier, loads its result (PDF or Excel) from database and returns it back to the
     * caller. It's mainly used by CCOS when the result is ready and user wants to retrieve it
     *
     * @param securityContextWTO The login and session information of the user
     * @param reportRequestId    Identifier of a report request to download
     * @return                   Result file of the report request
     * @throws InternalException
     */
    @WebMethod
    public ReportRequestWTO loadGeneratedReport(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
                                                @WebParam(name = "reportRequestId") Long reportRequestId) throws InternalException {
        UserProfileTO userProfileTO = super.validateRequest(securityContextWTO);

        try {
            ReportRequestTO reportRequestTO = reportRequestDelegator.loadGeneratedReport(userProfileTO, reportRequestId);
            return ReportRequestMapper.convert(reportRequestTO);
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode(), e.getArgs()), e);
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RPW_004_MSG, new EMSWebServiceFault(WebExceptionCode.RPW_004), e);
        }
    }
}
