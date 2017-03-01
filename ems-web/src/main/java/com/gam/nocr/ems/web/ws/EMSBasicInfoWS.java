package com.gam.nocr.ems.web.ws;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.delegator.PersonDelegator;
import com.gam.nocr.ems.biz.delegator.UserDelegator;
import com.gam.nocr.ems.biz.delegator.WorkstationDelegator;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.EnrollmentOfficeTO;
import com.gam.nocr.ems.data.domain.WorkstationTO;
import com.gam.nocr.ems.data.domain.vol.PersonVTO;
import com.gam.nocr.ems.data.domain.vol.UserVTO;
import com.gam.nocr.ems.data.domain.ws.PersonWTO;
import com.gam.nocr.ems.data.domain.ws.SecurityContextWTO;
import com.gam.nocr.ems.data.domain.ws.UserWTO;
import com.gam.nocr.ems.data.domain.ws.WorkstationWTO;
import com.gam.nocr.ems.data.mapper.tomapper.PersonMapper;
import com.gam.nocr.ems.data.mapper.tomapper.UserMapper;
import com.gam.nocr.ems.data.mapper.tomapper.WorkstationMapper;
import org.slf4j.Logger;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.WebFault;

/**
 * Services related to defining basic information (like workstations, users, etc.) are collected in this class to be
 * exposed as a web service to CCOS
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
@WebFault(faultBean = "com.gam.nocr.ems.web.ws.ExternalInterfaceException")
@WebService(serviceName = "EMSBasicInfoWS", portName = "EMSBasicInfoWSPort", targetNamespace = "http://basicinfows.ws.web.portal.nocr.gam.com/")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public class EMSBasicInfoWS extends EMSWS {

    private static final Logger logger = BaseLog.getLogger(EMSBasicInfoWS.class);

    private WorkstationDelegator workstationDelegator;
    private PersonDelegator personDelegator;
    private UserDelegator userDelegator;

    public EMSBasicInfoWS() {
        workstationDelegator = new WorkstationDelegator();
        personDelegator = new PersonDelegator();
        userDelegator = new UserDelegator();
    }

    /**
     * Registers a new workstation data in database. This would be called by CCOS when a manager adds a new workstation
     * to his/her enrollment office and wants to register it in workstation list of the system. The new workstation
     * would be in 'NEW' state till a 3S administrator accepts it (or maybe reject it)
     *
     * @param securityContextWTO The login and session information of the user
     * @param workstationWTO     Requested workstation details (e.g. code, activation code, etc.)
     * @throws InternalException
     */
    @WebMethod
    public void requestWorkstation(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
                                   @WebParam(name = "workstationWTO") WorkstationWTO workstationWTO) throws InternalException {
        if (workstationWTO == null)
            throw new InternalException(WebExceptionCode.BIW_001_MSG, new EMSWebServiceFault(WebExceptionCode.BIW_001));

        WorkstationTO workstationTO;

        try {
            workstationTO = WorkstationMapper.convert(workstationWTO);
        } catch (BaseException e) {
            logger.error(e.getExceptionCode() + ":" + e.getMessage(), e);
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
        } catch (Exception e) {
            logger.error(WebExceptionCode.BIW_002 + ":" + WebExceptionCode.BIW_002_MSG, e);
            throw new InternalException(WebExceptionCode.BIW_002_MSG, new EMSWebServiceFault(WebExceptionCode.BIW_002), e);
        }

        UserProfileTO userProfileTO = super.validateRequest(securityContextWTO);

        EnrollmentOfficeTO enrollmentOfficeTO = new EnrollmentOfficeTO();
        enrollmentOfficeTO.setId(userProfileTO.getDepID());
        workstationTO.setEnrollmentOffice(enrollmentOfficeTO);

        try {
            workstationDelegator.save(userProfileTO, workstationTO);
        } catch (BaseException e) {
            logger.error(e.getExceptionCode() + ":" + e.getMessage(), e);
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
        } catch (Exception e) {
            logger.error(WebExceptionCode.BIW_003 + ":" + WebExceptionCode.BIW_003_MSG, e);
            throw new InternalException(WebExceptionCode.BIW_003_MSG, new EMSWebServiceFault(WebExceptionCode.BIW_003), e);
        }
    }

    /**
     * Registers a new user data in database. This would be called by CCOS when a manager wants to adds a new user to
     * his/her enrollment office and wants to register him/her in users list of the system. The new user  would be in
     * 'REQUESTED' state till a 3S administrator accepts it (or maybe reject it)
     *
     * @param securityContextWTO The login and session information of the user
     * @param personWTO          Requested user's details (e.g. nid, name, etc.)
     * @throws InternalException
     */
    @WebMethod
    public void requestPerson(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
                              @WebParam(name = "personWTO") PersonWTO personWTO) throws InternalException {
        if (personWTO == null)
            throw new InternalException(WebExceptionCode.BIW_004_MSG, new EMSWebServiceFault(WebExceptionCode.BIW_004));

        PersonVTO personVTO;

        try {
            personVTO = PersonMapper.convert(personWTO);
        } catch (BaseException e) {
            logger.error(e.getExceptionCode() + ":" + e.getMessage(), e);
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
        } catch (Exception e) {
            logger.error(WebExceptionCode.BIW_005 + ":" + WebExceptionCode.BIW_005_MSG, e);
            throw new InternalException(WebExceptionCode.BIW_005_MSG, new EMSWebServiceFault(WebExceptionCode.BIW_005), e);
        }

        UserProfileTO userProfileTO = super.validateRequest(securityContextWTO);

        personVTO.setDepartmentId(String.valueOf(userProfileTO.getDepID()));

        try {
            personDelegator.save(userProfileTO, personVTO);
        } catch (BaseException e) {
            logger.error(e.getExceptionCode() + ":" + e.getMessage(), e);
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
        } catch (Exception e) {
            logger.error(WebExceptionCode.BIW_006 + ":" + WebExceptionCode.BIW_006_MSG, e);
            throw new InternalException(WebExceptionCode.BIW_006_MSG, new EMSWebServiceFault(WebExceptionCode.BIW_006), e);
        }
    }

    /**
     * Returns detailed information of current user (first name, last name, department) as a string
     * @param securityContextWTO The login and session information of the user
     * @return  Detailed information of current user (first name, last name, department) as a string
     * @throws InternalException
     */
    @WebMethod
    public String fetchUserInfo(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO) throws InternalException {
        UserProfileTO userProfileTO = super.validateRequest(securityContextWTO);

        try {
            return userDelegator.fetchUserInfo(userProfileTO);
        } catch (BaseException e) {
            logger.error(e.getExceptionCode() + ":" + e.getMessage(), e);
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
        } catch (Exception e) {
            logger.error(WebExceptionCode.BIW_010 + ":" + WebExceptionCode.BIW_010_MSG, e);
            throw new InternalException(WebExceptionCode.BIW_010_MSG, new EMSWebServiceFault(WebExceptionCode.BIW_010), e);
        }
    }
}
