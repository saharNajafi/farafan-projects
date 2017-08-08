package com.gam.nocr.ems.biz.service.external.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.xml.namespace.QName;

import com.gam.nocr.ems.biz.service.external.client.gaas.*;
import org.hsqldb.lib.HashMap;
import org.slf4j.Logger;

import servicePortUtil.ServicePorts;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.AbstractService;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.commons.profile.ProfileManager;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.ProfileHelper;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.domain.vol.PermissionVTO;
import com.gam.nocr.ems.data.domain.vol.PermissionVTOWrapper;
import com.gam.nocr.ems.data.domain.vol.RoleVTO;
import com.gam.nocr.ems.data.domain.vol.RoleVTOWrapper;
import com.gam.nocr.ems.data.domain.vol.SchedulingVTO;
import com.gam.nocr.ems.data.domain.vol.UserInfoVTO;
import com.gam.nocr.ems.data.domain.vol.ValidIPVTO;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 * @author Soheil Toodeh Fallah (fallah@gamelectronics.com)
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */

@Stateless(name = "GAASService")
@Local(GAASServiceLocal.class)
@Remote(GAASServiceRemote.class)
public class GAASServiceImpl extends AbstractService implements GAASServiceLocal, GAASServiceRemote {


    private static final Logger logger = BaseLog.getLogger(GAASServiceImpl.class);
    private static final Logger gaasLogger = BaseLog.getLogger("GaasLogger");
    private static final Logger threadLocalLogger = BaseLog.getLogger("threadLocal");

    /**
     * Gaas Exception Codes
     */

    //  GAAS SERVICE ERROR  (GSE)
    private static final Integer GSE200 = 200;
    private static final Integer GSE201 = 201;
    private static final Integer GSE202 = 202;
    private static final Integer GSE205 = 205;
    private static final Integer GSE208 = 208;

    //GENERAL ERROR
    private static final Integer GSE203 = 100;

    //Token
    private static final Integer TOKEN_INVALID_SERVICE_FAULT_CODE = 310;
    private static final Integer TOKEN_NOT_EXIST_SERVICE_FAULT_CODE = 311;

    //APPLICATION SERVER ERROR
    private static final Integer GENERAL_APPLICATION_SERVER_CODE = 800;

    private static final Integer APPLICATION_SERVER_NAMINGEXCEPTION_CODE = 801;

    //DATA ERROR
    private static final Integer GENERAL_SQLEXCEPTION_CODE = 900;
    private static final Integer SQLEXCEPTION_GETCONNECTION_CODE = 901;
    private static final Integer SQLEXCEPTION_CLOSE_PREPARESSTATEMENT_OR_RESULTSET_CODE = 902;
    private static final Integer SQLEXCEPTION_UPDATE_USERLOGIN_CODE = 903;
    private static final Integer SQLEXCEPTION_lOAD_USERINFO_CODE = 904;
    private static final Integer SQLEXCEPTION_CLOSECONNECTION_CODE = 905;

    /**
     * Default values for web services
     */
    private static final String DEFAULT_GAAS_WSDL_URL = "http://localhost:7001/gaasws/GAASWebService?WSDL";
    private static final String DEFAULT_GAAS_NAMESPACE = "http://gaas.gam.com/webservice";
    private static final String DEFAULT_CAS_WSDL_URL = "http://localhost:7001/gaasws/CASWebService?WSDL";

    HashMap errorCodeMap = new HashMap();
    HashMap vtoMap = new HashMap();

    GAASWebService gaasWebService = null;
    GAASWebServiceInterface service = null;


    /**
     * Private methods
     */

    /**
     * The method getService is used to get WebServices from GAAS sub system
     *
     * @return an instance of type {@link GAASWebServiceInterface}
     * @throws BaseException if cannot get the service
     */
    private GAASWebServiceInterface getGaasService() throws BaseException {
        try {
            ProfileManager pm = ProfileHelper.getProfileManager();
            String wsdlUrl = (String) pm.getProfile(ProfileKeyName.KEY_GAAS_ENDPOINT, true, null, null);
            String namespace = (String) pm.getProfile(ProfileKeyName.KEY_GAAS_NAMESPACE, true, null, null);
            if (wsdlUrl == null)
                wsdlUrl = DEFAULT_GAAS_WSDL_URL;
            if (namespace == null)
                namespace = DEFAULT_GAAS_NAMESPACE;
            String serviceName = "GAASWebService";
            logger.debug("Gaas wsdl url: " + wsdlUrl);
            gaasLogger.debug("Gaas wsdl url: " + wsdlUrl);
            GAASWebServiceInterface gaasPort = ServicePorts.getGassPort();
            if(gaasPort == null){
            	threadLocalLogger.debug("*********************** new GAASWebServiceInterface in GAAS getGaasService()");
            	gaasPort  = new GAASWebService(new URL(wsdlUrl), new QName(namespace, serviceName)).getGAASWebServiceImpl();
            	ServicePorts.setGaasPort(gaasPort);
            }
            else
            {
            	threadLocalLogger.debug("************************* using GAASWebServiceInterface from threadLocal");
            }
            return gaasPort;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.GSI_062, e.getMessage(), e);
        }
    }

    private CASWebServiceInterface getCasService() throws BaseException {
        try {
            ProfileManager pm = ProfileHelper.getProfileManager();
            String wsdlUrl = (String) pm.getProfile(ProfileKeyName.KEY_CAS_ENDPOINT, true, null, null);
            String namespace = (String) pm.getProfile(ProfileKeyName.KEY_GAAS_NAMESPACE, true, null, null);
            if (wsdlUrl == null)
                wsdlUrl = DEFAULT_CAS_WSDL_URL;
            if (namespace == null)
                namespace = DEFAULT_GAAS_NAMESPACE;
            String serviceName = "CASWebService";
            logger.info("Cas wsdl url: " + wsdlUrl);
            gaasLogger.info("Cas wsdl url: " + wsdlUrl);
            return new CASWebService(new URL(wsdlUrl), new QName(namespace, serviceName)).getCASWebServiceImpl();
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.GSI_090, e.getMessage(), e);
        }
    }

    private User convertToUser(UserInfoVTO userInfoTO) throws BaseException {
        try {
            User user = new User();
            user.setId(userInfoTO.getUserId());
            user.setUsername(userInfoTO.getUsername());
            user.setPassword(userInfoTO.getPassword());
            user.setEnabled(userInfoTO.isEnabled());
            user.setPersonId((userInfoTO.getPersonId() == null) ? null : userInfoTO.getPersonId().intValue());
//		TODO : read from config
            Schedule schedule = new Schedule();
            schedule.setId(1);
            user.setSchedule(schedule);

            List<Role> roles = new ArrayList<Role>();
            for (RoleVTO roleVTO : userInfoTO.getRoles()) {
                Role role = new Role();
                role.setId(roleVTO.getId().intValue());
                roles.add(role);
            }
            user.getRole().addAll(roles);

            List<Access> accesses = new ArrayList<Access>();
            for (PermissionVTO permissionVTO : userInfoTO.getPermissions()) {
                Access access = new Access();
                access.setId(permissionVTO.getId().intValue());
                accesses.add(access);
            }
            user.getAccess().addAll(accesses);

            List<LoginIP> loginIPs = new ArrayList<LoginIP>();
            for (ValidIPVTO validIPVTO : userInfoTO.getValidIps()) {
                LoginIP loginIP = new LoginIP();
                loginIP.setId(validIPVTO.getId().intValue());
                loginIPs.add(loginIP);
            }
            user.getLoginIP().addAll(loginIPs);
            return user;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.GSI_083, e.getMessage(), e);
        }
    }

    private Role convertToRole(RoleVTO roleVTO) {
        Role role = new Role();
        role.setId((roleVTO.getId() == null) ? null : roleVTO.getId().intValue());
        role.setRoleName(roleVTO.getName());
        return role;
    }

    private List<Role> convertToRoleList(List<RoleVTO> list) {
        List<Role> roleList = new ArrayList<Role>();
        if (list != null && list.size() > 0) {
            for (RoleVTO roleVTO : list) {
                Role role = convertToRole(roleVTO);
                roleList.add(role);
            }
        }
        return roleList;
    }

    private Access convertToAccess(PermissionVTO permissionVTO) throws BaseException {
        try {
            Access access = new Access();
            access.setId((permissionVTO.getId() == null) ? null : permissionVTO.getId().intValue());
            access.setAccessName(permissionVTO.getName());
            return access;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.GSI_087, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    private List<Access> convertToAccessList(List<PermissionVTO> list) throws BaseException {
        List<Access> accessList = new ArrayList<Access>();
        if (list != null && list.size() > 0) {
            for (PermissionVTO permissionVTO : list) {
                Access access = convertToAccess(permissionVTO);
                accessList.add(access);
            }
        }
        return accessList;
    }

    private UserInfoVTO convertToUserInfoVTO(User user) throws BaseException {
        try {
            UserInfoVTO userInfoVTO = new UserInfoVTO();
            userInfoVTO.setUserId(user.getId());
            userInfoVTO.setEnabled(user.isEnabled());
            userInfoVTO.setPersonId((user.getPersonId() == null) ? null : user.getPersonId().longValue());
            userInfoVTO.setUsername(user.getUsername());
            if (user.getSchedule() != null)
                userInfoVTO.setScheduling(convertToScheduleVTO(user.getSchedule()));
            userInfoVTO.setPermissions(convertToPermissionVTOList(user.getAccess()));
            userInfoVTO.setRoles(convertToRoleVTOList(user.getRole()));
            userInfoVTO.setValidIps(convertToValidVTOList(user.getLoginIP()));
            return userInfoVTO;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.GSI_086, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    private RoleVTO convertToRoleVTO(Role role) throws BaseException {
        try {
            RoleVTO roleVTO = new RoleVTO();
            roleVTO.setId((role.getId() == null) ? null : role.getId().longValue());
            roleVTO.setName(role.getRoleName());
            return roleVTO;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.GSI_084, BizExceptionCode.GLB_008_MSG, e);
        }
    }
    /**
     * the method returns all accesses with English name not by persian name
     * @author ganjyar
     */
    private PermissionVTO convertToPermissionVTOWithName(Access access) throws BaseException {
        try {
            PermissionVTO permissionVTO = new PermissionVTO();
            permissionVTO.setId((access.getId() == null) ? null : access.getId().longValue());
            permissionVTO.setName(access.getAccessName());
            return permissionVTO;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.GSI_085, BizExceptionCode.GLB_008_MSG, e);
        }
    }
    private PermissionVTO convertToPermissionVTO(Access access) throws BaseException {
        try {
            PermissionVTO permissionVTO = new PermissionVTO();
            permissionVTO.setId((access.getId() == null) ? null : access.getId().longValue());
            permissionVTO.setName(access.getAccessComment());
            return permissionVTO;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.GSI_085, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    private SchedulingVTO convertToScheduleVTO(Schedule schedule) throws BaseException {
        try {
            SchedulingVTO scheduleVTO = new SchedulingVTO();
            scheduleVTO.setId((schedule.getId() == null) ? null : schedule.getId().longValue());
            scheduleVTO.setName(schedule.getScheduleName());
            return scheduleVTO;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.GSI_088, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    private ValidIPVTO convertToValidIPVTO(LoginIP ip) throws BaseException {
        try {
            ValidIPVTO validIP = new ValidIPVTO();
            validIP.setId((ip.getId() == null) ? null : ip.getId().longValue());
            validIP.setName(ip.getRangeName());
            return validIP;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.GSI_089, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    private List<RoleVTO> convertToRoleVTOList(List<Role> list) throws BaseException {
        List<RoleVTO> roleVTOList = new ArrayList<RoleVTO>();
        if (list != null && list.size() > 0) {
            for (Role role : list) {
                RoleVTO roleVTO = convertToRoleVTO(role);
                roleVTOList.add(roleVTO);
            }
        }
        return roleVTOList;
    }
    /**
     * @author ganjyar
     */
    private List<PermissionVTO> convertToPermissionVTOListWithName(List<Access> list) throws BaseException {
        List<PermissionVTO> permissionVTOList = new ArrayList<PermissionVTO>();
        if (list != null && list.size() > 0) {
            for (Access access : list) {
                PermissionVTO permissionVTO = convertToPermissionVTOWithName(access);
                permissionVTOList.add(permissionVTO);
            }
        }
        return permissionVTOList;
    }
    
    private List<PermissionVTO> convertToPermissionVTOList(List<Access> list) throws BaseException {
        List<PermissionVTO> permissionVTOList = new ArrayList<PermissionVTO>();
        if (list != null && list.size() > 0) {
            for (Access access : list) {
                PermissionVTO permissionVTO = convertToPermissionVTO(access);
                permissionVTOList.add(permissionVTO);
            }
        }
        return permissionVTOList;
    }

    private List<PermissionVTO> convertToAccessVTOList(List<Access> list) throws BaseException {
        List<PermissionVTO> permissionVTOList = new ArrayList<PermissionVTO>();
        if (list != null && list.size() > 0) {
            for (Access access : list) {
                PermissionVTO permissionVTO = convertToPermissionVTO(access);
                permissionVTOList.add(permissionVTO);
            }
        }
        return permissionVTOList;
    }

    private List<SchedulingVTO> convertToScheduleVTOList(List<Schedule> list) throws BaseException {
        List<SchedulingVTO> scheduleVTOList = new ArrayList<SchedulingVTO>();
        if (list != null && list.size() > 0) {
            for (Schedule schedule : list) {
                SchedulingVTO schedulingVTO = convertToScheduleVTO(schedule);
                scheduleVTOList.add(schedulingVTO);
            }
        }
        return scheduleVTOList;
    }

    private List<ValidIPVTO> convertToValidVTOList(List<LoginIP> list) throws BaseException {
        List<ValidIPVTO> validIPVTOList = new ArrayList<ValidIPVTO>();
        if (list != null && list.size() > 0) {
            for (LoginIP loginIP : list) {
                ValidIPVTO validIPVTO = convertToValidIPVTO(loginIP);
                validIPVTOList.add(validIPVTO);
            }
        }
        return validIPVTOList;
    }

    public int save(UserInfoVTO userInfoTO) throws BaseException {
        logger.info("GAASServiceImpl : The method 'save' is started...");
        gaasLogger.info("GAASServiceImpl : The method 'save' is started...");
        User user = convertToUser(userInfoTO);
        Integer userId;
        try {
            logger.info("==============================================");
            gaasLogger.info("==============================================");
            logger.info("================= before invoke getGaasService().addUser ===================");
            gaasLogger.info("================= before invoke getGaasService().addUser ===================");
            logger.info("=============== user name = " + user.getUsername() + "====================");
            gaasLogger.info("=============== user name = " + user.getUsername() + "====================");
            logger.info("==============================================");
            gaasLogger.info("==============================================");
            userId = getGaasService().addUser(user);
            logger.info("==============================================");
            gaasLogger.info("==============================================");
            logger.info("================= after getGaasService().addUser ===================");
            gaasLogger.info("================= after getGaasService().addUser ===================");
            logger.info("==============================================");
            gaasLogger.info("==============================================");
        } catch (GAASWebServiceFaultException e) {
            String errorMessage = e.getFaultInfo().getMessage();
            Integer errorCode = e.getFaultInfo().getCode();
            if (GSE200.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_001,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE201.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_002,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE202.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_003,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE203.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_004,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            }
            ServiceException serviceException = new ServiceException(
                    BizExceptionCode.GSI_063,
                    errorMessage,
                    e,
                    EMSLogicalNames.SRV_GAAS.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
            gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
            throw serviceException;
        }
        logger.info("GAASServiceImpl : The method 'save' is finished.");
        gaasLogger.info("GAASServiceImpl : The method 'save' is finished.");
        return userId;
    }

    @Override
    public int update(UserInfoVTO userInfoTO) throws BaseException {
        logger.info("GAASServiceImpl : The method 'update' is started.");
        gaasLogger.info("GAASServiceImpl : The method 'update' is started.");

        User user = convertToUser(userInfoTO);
        try {
            getGaasService().updateUser(user);
        } catch (GAASWebServiceFaultException e) {
            String errorMessage = e.getFaultInfo().getMessage();
            Integer errorCode = e.getFaultInfo().getCode();
            if (GSE200.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_079,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE201.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_080,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE202.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_081,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE203.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_082,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            }
            ServiceException serviceException = new ServiceException(
                    BizExceptionCode.GSI_064,
                    errorMessage,
                    e,
                    EMSLogicalNames.SRV_GAAS.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
            gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
            throw serviceException;
        }
        logger.info("GAASServiceImpl : The method 'update' is finished.");
        gaasLogger.info("GAASServiceImpl : The method 'update' is finished.");
        return userInfoTO.getUserId();
    }

    public void changePassword(Integer userId, String oldpass, String newpass) throws BaseException {
        logger.info("GAASServiceImpl : The method 'changePassword' is started...");
        gaasLogger.info("GAASServiceImpl : The method 'changePassword' is started...");
        try {
            getGaasService().changePassword(userId, oldpass, newpass);
        } catch (GAASWebServiceFaultException e) {
            String errorMessage = e.getFaultInfo().getMessage();
            Integer errorCode = e.getFaultInfo().getCode();
            if (GSE200.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_005,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE201.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_006,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE202.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_007,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE203.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_008,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE208.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_096,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(errorMessage, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(errorMessage, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            }
            ServiceException serviceException = new ServiceException(
                    BizExceptionCode.GSI_065,
                    errorMessage,
                    e,
                    EMSLogicalNames.SRV_GAAS.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
            gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
            throw serviceException;
        }
        logger.info("GAASServiceImpl : The method 'changePassword' is finished.");
        gaasLogger.info("GAASServiceImpl : The method 'changePassword' is finished.");
    }

    public void changeState(Integer userId, Boolean state) throws BaseException {
        logger.info("GAASServiceImpl : The method 'changeState' is started...");
        gaasLogger.info("GAASServiceImpl : The method 'changeState' is started...");
        try {
            GAASWebServiceInterface gaasService = getGaasService();
            if (state) {
                User user = gaasService.getUser(userId);
                if (user != null) {
                    user.setEnabled(state);
                    gaasService.updateUser(user);
                } else {
                    gaasService.disableUser(userId);
                }
            }
        } catch (GAASWebServiceFaultException e) {
            String errorMessage = e.getFaultInfo().getMessage();
            Integer errorCode = e.getFaultInfo().getCode();
            if (GSE200.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_009,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE201.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_010,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE202.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_011,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE203.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_012,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            }
            ServiceException serviceException = new ServiceException(
                    BizExceptionCode.GSI_066,
                    errorMessage,
                    e,
                    EMSLogicalNames.SRV_GAAS.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
            gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
            throw serviceException;
        }
        logger.info("GAASServiceImpl : The method 'changeState' is finished.");
        gaasLogger.info("GAASServiceImpl : The method 'changeState' is finished.");
    }

    public void remove(Integer userId) throws BaseException {
        logger.info("GAASServiceImpl : The method 'remove' is started...");
        gaasLogger.info("GAASServiceImpl : The method 'remove' is started...");
        try {
            getGaasService().deleteUser(userId);
        } catch (GAASWebServiceFaultException e) {
            String errorMessage = e.getFaultInfo().getMessage();
            Integer errorCode = e.getFaultInfo().getCode();
            logger.error(BizExceptionCode.GLB_ERR_MSG, e);
            if (GSE200.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_013,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE201.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_014,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE202.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_015,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE203.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_016,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE205.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_061,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            }
            ServiceException serviceException = new ServiceException(
                    BizExceptionCode.GSI_067,
                    errorMessage,
                    e,
                    EMSLogicalNames.SRV_GAAS.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
            gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
            throw serviceException;
        }
        logger.info("GAASServiceImpl : The method 'remove' is finished.");
        gaasLogger.info("GAASServiceImpl : The method 'remove' is finished.");
    }

    public List<RoleVTO> getRoles(Integer userId, String searchString, int from, int to) throws BaseException {
        logger.info("GAASServiceImpl : The method 'getRoles' is started...");
        gaasLogger.info("GAASServiceImpl : The method 'getRoles' is started...");
        List<RoleVTO> roleVTOList;
        try {
            List<Role> roleList = getGaasService().getRoles(userId, searchString, from, to);
            roleVTOList = convertToRoleVTOList(roleList);
        } catch (GAASWebServiceFaultException e) {
            String errorMessage = e.getFaultInfo().getMessage();
            Integer errorCode = e.getFaultInfo().getCode();
            if (GSE200.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_017,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE201.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_018,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE202.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_019,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE203.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_020,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            }
            ServiceException serviceException = new ServiceException(
                    BizExceptionCode.GSI_068,
                    errorMessage,
                    e,
                    EMSLogicalNames.SRV_GAAS.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
            gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
            throw serviceException;
        }
        logger.info("GAASServiceImpl : The method 'getRoles' is finished.");
        gaasLogger.info("GAASServiceImpl : The method 'getRoles' is finished.");
        return roleVTOList;
    }

    
    public RoleVTOWrapper getRolesWithCount(Integer userId, String searchString, int from, int to) throws BaseException {
        logger.info("GAASServiceImpl : The method 'getRoles' is started...");
        gaasLogger.info("GAASServiceImpl : The method 'getRoles' is started...");
        List<RoleVTO> roleVTOList;
        Integer count ;
        try {
            RoleWrapper roleWrapper = getGaasService().getRolesWithCount(userId, searchString, from, to);
            roleVTOList = convertToRoleVTOList(roleWrapper.getList());
            count = roleWrapper.getCount();
        } catch (GAASWebServiceFaultException e) {
            String errorMessage = e.getFaultInfo().getMessage();
            Integer errorCode = e.getFaultInfo().getCode();
            if (GSE200.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_017,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE201.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_018,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE202.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_019,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE203.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_020,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            }
            ServiceException serviceException = new ServiceException(
                    BizExceptionCode.GSI_068,
                    errorMessage,
                    e,
                    EMSLogicalNames.SRV_GAAS.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
            gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
            throw serviceException;
        }
        logger.info("GAASServiceImpl : The method 'getRoles' is finished.");
        gaasLogger.info("GAASServiceImpl : The method 'getRoles' is finished.");
        return new RoleVTOWrapper(roleVTOList, count);
    }


    public List<PermissionVTO> getAllPermission(String searchString, int from, int to) throws BaseException {
        logger.info("GAASServiceImpl : The method 'getAllPermission' is started...");
        gaasLogger.info("GAASServiceImpl : The method 'getAllPermission' is started...");
        List<PermissionVTO> permissionVTOList = null;
        try {
            List<Access> accessList = getGaasService().getAllUserPermission(null, searchString, from, to);
            permissionVTOList = convertToPermissionVTOList(accessList);
        } catch (GAASWebServiceFaultException e) {
            String errorMessage = e.getFaultInfo().getMessage();
            Integer errorCode = e.getFaultInfo().getCode();
            if (GSE200.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_021,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE201.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_022,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE202.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_023,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE203.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_024,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            }
            ServiceException serviceException = new ServiceException(
                    BizExceptionCode.GSI_069,
                    errorMessage,
                    e,
                    EMSLogicalNames.SRV_GAAS.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
            gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
            throw serviceException;
        }
        logger.info("GAASServiceImpl : The method 'getAllPermission' is finished.");
        gaasLogger.info("GAASServiceImpl : The method 'getAllPermission' is finished.");
        return permissionVTOList;
    }

    public List<PermissionVTO> getDirectUserPermission(Integer userId, String searchString, int from, int to) throws BaseException {
        logger.info("GAASServiceImpl : The method 'getDirectUserPermission' is started...");
        gaasLogger.info("GAASServiceImpl : The method 'getDirectUserPermission' is started...");
        List<PermissionVTO> permissionVTOList = null;
        try {
            List<Access> accessList = getGaasService().getAllUserPermission(userId, searchString, from, to);
            permissionVTOList = convertToPermissionVTOList(accessList);
        } catch (GAASWebServiceFaultException e) {
            String errorMessage = e.getFaultInfo().getMessage();
            Integer errorCode = e.getFaultInfo().getCode();
            if (GSE200.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_025,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE201.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_026,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE202.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_027,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE203.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_028,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            }
            ServiceException serviceException = new ServiceException(
                    BizExceptionCode.GSI_070,
                    errorMessage,
                    e,
                    EMSLogicalNames.SRV_GAAS.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
            gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
            throw serviceException;
        }
        logger.info("GAASServiceImpl : The method 'getDirectUserPermission' is finished.");
        gaasLogger.info("GAASServiceImpl : The method 'getDirectUserPermission' is finished.");
        return permissionVTOList;
    }
    /**
     * @author ganjyar
     */
    public List<PermissionVTO> getAllUserAccess(Integer userId, String searchString, int from, int to) throws BaseException {
        logger.info("GAASServiceImpl : The method 'getAllUserPermission' is started...");
        gaasLogger.info("GAASServiceImpl : The method 'getAllUserPermission' is started...");
        List<PermissionVTO> permissionVTOList;
        try {
            List<Access> accessList = getGaasService().getAllUserPermission(userId, searchString, from, to);
            permissionVTOList = convertToPermissionVTOListWithName(accessList);
        } catch (GAASWebServiceFaultException e) {
            String errorMessage = e.getFaultInfo().getMessage();
            Integer errorCode = e.getFaultInfo().getCode();
            if (GSE200.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_029,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE201.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_030,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE202.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_031,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE203.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_032,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            }
            ServiceException serviceException = new ServiceException(
                    BizExceptionCode.GSI_071,
                    errorMessage,
                    e,
                    EMSLogicalNames.SRV_GAAS.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
            gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
            throw serviceException;
        }
        logger.info("GAASServiceImpl : The method 'getAllUserPermission' is finished.");
        gaasLogger.info("GAASServiceImpl : The method 'getAllUserPermission' is finished.");
        return permissionVTOList;
    }
    public List<PermissionVTO> getAllUserPermission(Integer userId, String searchString, int from, int to) throws BaseException {
        logger.info("GAASServiceImpl : The method 'getAllUserPermission' is started...");
        gaasLogger.info("GAASServiceImpl : The method 'getAllUserPermission' is started...");
        List<PermissionVTO> permissionVTOList;
        try {
            List<Access> accessList = getGaasService().getAllUserPermission(userId, searchString, from, to);
            permissionVTOList = convertToPermissionVTOList(accessList);
        } catch (GAASWebServiceFaultException e) {
            String errorMessage = e.getFaultInfo().getMessage();
            Integer errorCode = e.getFaultInfo().getCode();
            if (GSE200.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_029,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE201.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_030,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE202.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_031,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE203.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_032,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            }
            ServiceException serviceException = new ServiceException(
                    BizExceptionCode.GSI_071,
                    errorMessage,
                    e,
                    EMSLogicalNames.SRV_GAAS.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
            gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
            throw serviceException;
        }
        logger.info("GAASServiceImpl : The method 'getAllUserPermission' is finished.");
        gaasLogger.info("GAASServiceImpl : The method 'getAllUserPermission' is finished.");
        return permissionVTOList;
    }

    
    public PermissionVTOWrapper getAllUserPermissionWithCount(Integer userId, String searchString, int from, int to) throws BaseException {
        logger.info("GAASServiceImpl : The method 'getAllUserPermission' is started...");
        gaasLogger.info("GAASServiceImpl : The method 'getAllUserPermission' is started...");
        List<PermissionVTO> permissionVTOList;
        Integer count;
        try {
            AccessWrapper accessWrapper = getGaasService().getAllUserPermissionWithCount(userId, searchString, from, to);
            permissionVTOList = convertToPermissionVTOList(accessWrapper.getList());
            count = accessWrapper.getCount();
        } catch (GAASWebServiceFaultException e) {
            String errorMessage = e.getFaultInfo().getMessage();
            Integer errorCode = e.getFaultInfo().getCode();
            if (GSE200.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_029,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE201.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_030,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE202.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_031,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE203.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_032,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            }
            ServiceException serviceException = new ServiceException(
                    BizExceptionCode.GSI_071,
                    errorMessage,
                    e,
                    EMSLogicalNames.SRV_GAAS.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
            gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
            throw serviceException;
        }
        logger.info("GAASServiceImpl : The method 'getAllUserPermission' is finished.");
        gaasLogger.info("GAASServiceImpl : The method 'getAllUserPermission' is finished.");
        return new PermissionVTOWrapper(permissionVTOList, count);
    }

    public List<PermissionVTO> getRolePermission(Integer roleId, int from, int to) throws BaseException {
        logger.info("GAASServiceImpl : The method 'getRolePermission' is started...");
        gaasLogger.info("GAASServiceImpl : The method 'getRolePermission' is started...");
        List<PermissionVTO> permissionVTOList;
        try {
            List<Access> accessList = getGaasService().getRolePermission(roleId);
            permissionVTOList = convertToPermissionVTOList(accessList);
        } catch (GAASWebServiceFaultException e) {
            String errorMessage = e.getFaultInfo().getMessage();
            Integer errorCode = e.getFaultInfo().getCode();
            if (GSE200.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_033,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE201.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_034,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE202.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_035,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE203.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_036,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            }
            ServiceException serviceException = new ServiceException(
                    BizExceptionCode.GSI_072,
                    errorMessage,
                    e,
                    EMSLogicalNames.SRV_GAAS.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
            gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
            throw serviceException;
        }
        logger.info("GAASServiceImpl : The method 'getRolePermission' is finished.");
        gaasLogger.info("GAASServiceImpl : The method 'getRolePermission' is finished.");
        return permissionVTOList;
    }

    public List<SchedulingVTO> getScheduling(Integer userId, String searchString, int from, int to) throws BaseException {
        logger.info("GAASServiceImpl : The method 'getScheduling' is started...");
        gaasLogger.info("GAASServiceImpl : The method 'getScheduling' is started...");
        List<SchedulingVTO> schedulingVTOList;
        try {
            List<Schedule> scheduleList = getGaasService().getSchedules(userId, searchString, from, to);
            schedulingVTOList = convertToScheduleVTOList(scheduleList);
        } catch (GAASWebServiceFaultException e) {
            String errorMessage = e.getFaultInfo().getMessage();
            Integer errorCode = e.getFaultInfo().getCode();
            if (GSE200.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_037,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE201.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_038,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE202.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_039,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE203.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_040,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            }
            ServiceException serviceException = new ServiceException(
                    BizExceptionCode.GSI_073,
                    errorMessage,
                    e,
                    EMSLogicalNames.SRV_GAAS.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
            gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
            throw serviceException;
        }
        logger.info("GAASServiceImpl : The method 'getScheduling' is finished.");
        gaasLogger.info("GAASServiceImpl : The method 'getScheduling' is finished.");
        return schedulingVTOList;
    }

    public List<ValidIPVTO> getValidIPs(Integer userId, String searchString, int from, int to) throws BaseException {
        logger.info("GAASServiceImpl : The method 'getValidIPs' is started...");
        gaasLogger.info("GAASServiceImpl : The method 'getValidIPs' is started...");
        List<ValidIPVTO> validIPVTOList;
        try {
            List<LoginIP> loginIPList = getGaasService().getValidLoginIPs(userId, searchString, from, to);
            validIPVTOList = convertToValidVTOList(loginIPList);
        } catch (GAASWebServiceFaultException e) {
            String errorMessage = e.getFaultInfo().getMessage();
            Integer errorCode = e.getFaultInfo().getCode();
            if (GSE200.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_041,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE201.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_042,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE202.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_043,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE203.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_044,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            }
            ServiceException serviceException = new ServiceException(
                    BizExceptionCode.GSI_074,
                    errorMessage,
                    e,
                    EMSLogicalNames.SRV_GAAS.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
            gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
            throw serviceException;
        }
        logger.info("GAASServiceImpl : The method 'getValidIPs' is finished.");
        gaasLogger.info("GAASServiceImpl : The method 'getValidIPs' is finished.");
        return validIPVTOList;
    }

    public Boolean hasTokenPermission(Integer userId) throws BaseException {
        logger.info("GAASServiceImpl : The method 'hasTokenPermission' is started...");
        gaasLogger.info("GAASServiceImpl : The method 'hasTokenPermission' is started...");
        Boolean returnValue;
        try {
            returnValue = getGaasService().hasTokenPermission(userId);
        } catch (GAASWebServiceFaultException e) {
            String errorMessage = e.getFaultInfo().getMessage();
            Integer errorCode = e.getFaultInfo().getCode();
            if (GSE200.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_045,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE201.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_046,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE202.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_047,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE203.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_048,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            }
            ServiceException serviceException = new ServiceException(
                    BizExceptionCode.GSI_075,
                    errorMessage,
                    e,
                    EMSLogicalNames.SRV_GAAS.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
            gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
            throw serviceException;

        }

        logger.info("GAASServiceImpl : The method 'hasTokenPermission' is finished.");
        gaasLogger.info("GAASServiceImpl : The method 'hasTokenPermission' is finished.");
        return returnValue;
    }

    public Boolean validateTicket(String username, String ticket) throws BaseException {
        logger.info("CASServiceImpl : The method 'validateTicket' is started...");
        gaasLogger.info("CASServiceImpl : The method 'validateTicket' is started...");
        return validateTicketInCache(username, ticket);
//        try {
//            return getCasService().validateTicket(username, ticket);
//        } catch (CASWebServiceFaultException e) {
//            String errorMessage = e.getFaultInfo().getMessage();
//            Integer errorCode = e.getFaultInfo().getCode();
//            if (GSE200.equals(errorCode)) {
//                ServiceException serviceException = new ServiceException(
//                        BizExceptionCode.GSI_091,
//                        errorMessage,
//                        e,
//                        EMSLogicalNames.SRV_CAS.split(","));
//                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CAS.split(","));
//                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CAS.split(","));
//                throw serviceException;
//            } else if (GSE201.equals(errorCode)) {
//                ServiceException serviceException = new ServiceException(
//                        BizExceptionCode.GSI_092,
//                        errorMessage,
//                        e,
//                        EMSLogicalNames.SRV_CAS.split(","));
//                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CAS.split(","));
//                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CAS.split(","));
//                throw serviceException;
//            } else if (GSE202.equals(errorCode)) {
//                ServiceException serviceException = new ServiceException(
//                        BizExceptionCode.GSI_093,
//                        errorMessage,
//                        e,
//                        EMSLogicalNames.SRV_CAS.split(","));
//                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CAS.split(","));
//                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CAS.split(","));
//                throw serviceException;
//            } else if (GSE203.equals(errorCode)) {
//                ServiceException serviceException = new ServiceException(
//                        BizExceptionCode.GSI_094,
//                        errorMessage,
//                        e,
//                        EMSLogicalNames.SRV_CAS.split(","));
//                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CAS.split(","));
//                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CAS.split(","));
//                throw serviceException;
//            } else {
//                ServiceException serviceException = new ServiceException(
//                        BizExceptionCode.GSI_095,
//                        errorMessage,
//                        e,
//                        EMSLogicalNames.SRV_CAS.split(","));
//                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CAS.split(","));
//                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_CAS.split(","));
//                throw serviceException;
//            }
//        }
    }
    
    private static String TICKET_CACHE_NAME = "ticketCache";
    private static long TICKET_EXPIRY_TIME = 20 * 60 * 1000;
    private NamedCache ticketCache = CacheFactory.getCache(TICKET_CACHE_NAME);

    public boolean validateTicketInCache(String username, String ticket) {
        boolean valid = false;
        username = username.toLowerCase();
        if (ticket != null) {
            valid = ticket.equals(ticketCache.get(username));
            logger.info("username: " + username+" ticket:"+ticket+" tiket in cach:"+ticketCache.get(username)+" valid:"+valid);
            gaasLogger.info("username: " + username+" ticket:"+ticket+" tiket in cach:"+ticketCache.get(username)+" valid:"+valid);
            if (valid) {
                ticketCache.remove(username);
                ticketCache.put(username, ticket, TICKET_EXPIRY_TIME);
            }
        }
        return valid;
    }

    @Override
    public void enableUser(Integer userId) throws BaseException {
        logger.info("GAASServiceImpl : The method 'enableUser' is started...");
        gaasLogger.info("GAASServiceImpl : The method 'enableUser' is started...");
        try {
            getGaasService().enableUser(userId);
        } catch (GAASWebServiceFaultException e) {
            String errorMessage = e.getFaultInfo().getMessage();
            Integer errorCode = e.getFaultInfo().getCode();
            if (GSE200.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_049,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE201.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_050,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE202.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_051,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE203.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_052,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            }
            ServiceException serviceException = new ServiceException(
                    BizExceptionCode.GSI_076,
                    errorMessage,
                    e,
                    EMSLogicalNames.SRV_GAAS.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
            gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
            throw serviceException;
        }
        logger.info("GAASServiceImpl : The method 'enableUser' is finished.");
        gaasLogger.info("GAASServiceImpl : The method 'enableUser' is finished.");
    }

    @Override
    public void disableUser(Integer userId) throws BaseException {
        logger.info("GAASServiceImpl : The method 'disableUser' is started...");
        gaasLogger.info("GAASServiceImpl : The method 'disableUser' is started...");
        try {
            getGaasService().disableUser(userId);
        } catch (GAASWebServiceFaultException e) {
            String errorMessage = e.getFaultInfo().getMessage();
            Integer errorCode = e.getFaultInfo().getCode();
            if (GSE200.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_053,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE201.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_054,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE202.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_055,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE203.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_056,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            }
            ServiceException serviceException = new ServiceException(
                    BizExceptionCode.GSI_077,
                    errorMessage,
                    e,
                    EMSLogicalNames.SRV_GAAS.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
            gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
            throw serviceException;
        }
        logger.info("GAASServiceImpl : The method 'disableUser' is finished.");
        gaasLogger.info("GAASServiceImpl : The method 'disableUser' is finished.");
    }

    @Override
    public UserInfoVTO getUser(Integer userId) throws BaseException {
        logger.info("GAASServiceImpl : The method 'getUser' is started...");
        gaasLogger.info("GAASServiceImpl : The method 'getUser' is started...");
        User user;
        UserInfoVTO userInfoVTO = new UserInfoVTO();
        try {
            user = getGaasService().getUser(userId);
        } catch (GAASWebServiceFaultException e) {
            String errorMessage = e.getFaultInfo().getMessage();
            Integer errorCode = e.getFaultInfo().getCode();
            if (GSE200.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_057,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE201.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_058,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE202.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_059,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            } else if (GSE203.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.GSI_060,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_GAAS.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
                throw serviceException;
            }
            ServiceException serviceException = new ServiceException(
                    BizExceptionCode.GSI_078,
                    errorMessage,
                    e,
                    EMSLogicalNames.SRV_GAAS.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
            gaasLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_GAAS.split(","));
            throw serviceException;
        }

        if (user != null) {
            userInfoVTO = convertToUserInfoVTO(user);
        }
        logger.info("GAASServiceImpl : The method 'getUser' is finished.");
        gaasLogger.info("GAASServiceImpl : The method 'getUser' is finished.");
        return userInfoVTO;
    }
    
    
    private static String PERSONID_CACHE_NAME = "ticketPersonID";
    private NamedCache ticketPersonID = CacheFactory.getCache(PERSONID_CACHE_NAME);

    //Anbari
//	@Override
//	public Long getPersonID(UserProfileTO userProfileTO) throws BaseException {
//		
//		Long personID = null;
//        personID = (Long) ticketPersonID.get(userProfileTO.getUserName());
//        if(personID == null)
//        {
//        	personID = userProfileTO.getPersonID();        	 
//        	ticketPersonID.put(userProfileTO.getUserName(), personID);
//        }        
//		return personID;
//	}
}
