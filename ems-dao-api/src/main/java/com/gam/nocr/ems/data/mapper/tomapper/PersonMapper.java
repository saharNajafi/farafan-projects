package com.gam.nocr.ems.data.mapper.tomapper;

import gampooya.tools.util.Base64;
import gampooya.tools.vlp.ListException;
import gampooya.tools.vlp.ValueListHandler;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.displaytag.exception.ListHandlerException;
import org.slf4j.Logger;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.nocr.ems.config.EMSValueListProvider;
import com.gam.nocr.ems.config.MapperExceptionCode;
import com.gam.nocr.ems.data.domain.DepartmentTO;
import com.gam.nocr.ems.data.domain.PersonTO;
import com.gam.nocr.ems.data.domain.vol.PermissionVTO;
import com.gam.nocr.ems.data.domain.vol.PersonInfoVTO;
import com.gam.nocr.ems.data.domain.vol.PersonVTO;
import com.gam.nocr.ems.data.domain.vol.RoleVTO;
import com.gam.nocr.ems.data.domain.vol.SchedulingVTO;
import com.gam.nocr.ems.data.domain.vol.UserInfoVTO;
import com.gam.nocr.ems.data.domain.vol.ValidIPVTO;
import com.gam.nocr.ems.data.domain.ws.PersonWTO;
import com.gam.nocr.ems.data.enums.BooleanType;
import com.gam.nocr.ems.data.enums.PersonRequestStatus;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class PersonMapper {

    private static final Logger logger = BaseLog.getLogger(PersonMapper.class);

    public static PersonInfoVTO convert(PersonVTO person) throws BaseException {
        PersonTO newPersonTO = new PersonTO();
        UserInfoVTO userInfoVTO = new UserInfoVTO();

        if (!EmsUtil.checkString(person.getRequestStatusString())) {
            if (person.getId() != null) {
                newPersonTO = fetchPerson(String.valueOf(person.getId()));

                if (!newPersonTO.getUserName().equals(person.getUserName()))
                    throw new BaseException(MapperExceptionCode.PRM_011, MapperExceptionCode.PRM_011_MSG);

                newPersonTO.setId(person.getId());
                newPersonTO.setRequestStatus(PersonRequestStatus.valueOf(newPersonTO.getRequestStatusString()));
                userInfoVTO.setUserId(newPersonTO.getUserId());
                if (BooleanType.T.toString().equalsIgnoreCase(person.getlStatus())) {
                    newPersonTO.setStatus(BooleanType.T);
                    userInfoVTO.setEnabled(true);
                } else {
                    newPersonTO.setStatus(BooleanType.F);
                    userInfoVTO.setEnabled(false);
                }
            } else {
                newPersonTO.setUserId(null);
                newPersonTO.setStatus(BooleanType.F);
                newPersonTO.setRequestStatus(PersonRequestStatus.APPROVED);
            }

            newPersonTO.setFirstName(person.getFirstName());

            newPersonTO.setLastName(person.getLastName());

            newPersonTO.setNid(person.getNid());

            newPersonTO.setUserName(person.getUserName());
            userInfoVTO.setUsername(person.getUserName());

            if (!EmsUtil.checkString(person.getDepartmentId()))
                throw new BaseException(MapperExceptionCode.PRM_003, MapperExceptionCode.PRM_003_MSG);

            try {
                newPersonTO.setDepartment(new DepartmentTO(Long.parseLong(person.getDepartmentId())));
            } catch (NumberFormatException e) {
                throw new BaseException(MapperExceptionCode.PRM_004, MapperExceptionCode.PRM_004_MSG, e);
            }

            newPersonTO.setLastLogin(new Timestamp(new Date().getTime()));
            newPersonTO.setBirthCertNum(person.getBirthCertNum());
            newPersonTO.setBirthCertSeries(person.getBirthCertSeries());
            newPersonTO.setFatherName(person.getFatherName());
            newPersonTO.setEmail(person.getEmail());

            if ((EmsUtil.checkString(person.getPassword())) && (EmsUtil.checkString(person.getcPassword()))) {
                if (person.getPassword().equals(person.getcPassword())) {
                    try {
                        userInfoVTO.setPassword(Base64.encode(EmsUtil.MD5Digest(person.getUserName().toLowerCase()
                                + person.getPassword()), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        throw new BaseException(MapperExceptionCode.PRM_005, MapperExceptionCode.PRM_005_MSG, e);
                    }
                } else {
                    throw new BaseException(MapperExceptionCode.PRM_006, MapperExceptionCode.PRM_006_MSG);
                }
            } else {
                if (person.getId() == null) {
                    throw new BaseException(MapperExceptionCode.PRM_007, MapperExceptionCode.PRM_007_MSG);
                }
            }

            if (!EmsUtil.checkString(person.getRoles()))
                throw new BaseException(MapperExceptionCode.PRM_008, MapperExceptionCode.PRM_008_MSG);

            List<RoleVTO> roleVTOs = new ArrayList<RoleVTO>();
            if (EmsUtil.checkString(person.getRoles())) {
                String[] roles = person.getRoles().split(",");
                for (String role : roles) {
                    try {
                        RoleVTO roleVTO = new RoleVTO();
                        roleVTO.setId(Long.valueOf(role));
                        roleVTOs.add(roleVTO);
                    } catch (NumberFormatException e) {
                        logger.error(e.getMessage(), e);
                    }
                }
                userInfoVTO.setRoles(roleVTOs);
            } else
                userInfoVTO.setRoles(new ArrayList<RoleVTO>());

            List<PermissionVTO> permissionVTOs = new ArrayList<PermissionVTO>();
            if (EmsUtil.checkString(person.getPermissions())) {
                String[] permissions = person.getPermissions().split(",");
                for (String permission : permissions) {
                    try {
                        PermissionVTO permissionVTO = new PermissionVTO();
                        permissionVTO.setId(Long.valueOf(permission));
                        permissionVTOs.add(permissionVTO);
                    } catch (NumberFormatException e) {
                        logger.error(e.getMessage(), e);
                    }
                }
                userInfoVTO.setPermissions(permissionVTOs);
            } else
                userInfoVTO.setPermissions(new ArrayList<PermissionVTO>());

            if (!EmsUtil.checkListSize(roleVTOs))
                throw new BaseException(MapperExceptionCode.PRM_009, MapperExceptionCode.PRM_009_MSG);

            //todo -should be read from config
            List<ValidIPVTO> validIPVTOs = new ArrayList<ValidIPVTO>();
            ValidIPVTO validIPVTO = new ValidIPVTO();
            validIPVTO.setId(22L);
            validIPVTOs.add(validIPVTO);
            userInfoVTO.setValidIps(validIPVTOs);

            SchedulingVTO schedulingVTO = new SchedulingVTO();
            schedulingVTO.setId(1L);
            userInfoVTO.setScheduling(schedulingVTO);
            //todo -should be read from config
        } else {
            newPersonTO.setFirstName(person.getFirstName());
            newPersonTO.setLastName(person.getLastName());
            newPersonTO.setNid(person.getNid());
            newPersonTO.setFatherName(person.getFatherName());
            newPersonTO.setBirthCertNum(person.getBirthCertNum());
            newPersonTO.setBirthCertSeries(person.getBirthCertSeries());
            newPersonTO.setEmail(person.getEmail());
            newPersonTO.setRequestStatus(PersonRequestStatus.REQUESTED);
            newPersonTO.setStatus(BooleanType.F);
            if (EmsUtil.checkString(person.getDepartmentId())) {
                try {
                    newPersonTO.setDepartment(new DepartmentTO(Long.valueOf(person.getDepartmentId())));
                } catch (NumberFormatException e) {
                    throw new BaseException(MapperExceptionCode.PRM_010, MapperExceptionCode.PRM_004_MSG, e);
                }
            }
        }

        return new PersonInfoVTO(newPersonTO, userInfoVTO);
    }

    public static PersonVTO convert(PersonTO personTO, UserInfoVTO userInfoVTO) throws BaseException {
        PersonVTO personVTO = new PersonVTO();

        personVTO.setId(personTO.getId());
        personVTO.setFirstName(personTO.getFirstName());
        personVTO.setLastName(personTO.getLastName());
        personVTO.setNid(personTO.getNid());
        personVTO.setUserName(personTO.getUserName());
//		personVTO.setLastLogin(personTO.getLastLogin());
        personVTO.setBirthCertNum(personTO.getBirthCertNum());
        personVTO.setBirthCertSeries(personTO.getBirthCertSeries());
        personVTO.setFatherName(personTO.getFatherName());
        personVTO.setMobilePhone(personTO.getMobilePhone());
        personVTO.setPhone(personTO.getPhone());
        if (personTO.getDepartment() != null)
            personVTO.setDepartmentId(personTO.getDepartment().getId().toString());
        personVTO.setDepartmentName(personTO.getDepartmentName());
        personVTO.setEmail(personTO.getEmail());

        if (userInfoVTO != null) {
            List<RoleVTO> roleVTOs = userInfoVTO.getRoles();
            String roles = "";
            for (RoleVTO roleVTO : roleVTOs)
                roles = "," + roleVTO.getId() + roles;

            if (roleVTOs.size() > 0) {
                personVTO.setRoles(roles.substring(1));
                personVTO.setRoleList(roleVTOs);
            } else {
                personVTO.setRoles("");
                personVTO.setRoleList(new ArrayList<RoleVTO>());
            }

            List<PermissionVTO> permissionVTOs = userInfoVTO.getPermissions();
            String permissions = "";
            for (PermissionVTO permissionVTO : permissionVTOs)
                permissions = "," + permissionVTO.getId() + permissions;

            if (permissionVTOs.size() > 0) {
                personVTO.setPermissions(permissions.substring(1));
                personVTO.setPermissionList(permissionVTOs);
            } else {
                personVTO.setPermissions("");
                personVTO.setPermissionList(new ArrayList<PermissionVTO>());
            }
        }

        return personVTO;
    }

    public static PersonVTO convert(PersonWTO personWTO) throws BaseException {
        PersonVTO personVTO = new PersonVTO();

        personVTO.setFirstName(personWTO.getFirstName());
        personVTO.setLastName(personWTO.getLastName());
        personVTO.setNid(personWTO.getNid());
        personVTO.setFatherName(personWTO.getFatherName());
        personVTO.setBirthCertNum(personWTO.getBirthCertNum());
        personVTO.setBirthCertSeries(personWTO.getBirthCertSeries());
        personVTO.setEmail(personWTO.getEmail());
        personVTO.setRequestStatusString(PersonRequestStatus.REQUESTED.name());

        return personVTO;
    }

    private static PersonTO fetchPerson(String personId) throws BaseException {
        HashMap param = new HashMap();
        param.put("personId", personId);
        String parts = ",perId";
        try {
            ValueListHandler vlh = EMSValueListProvider.getProvider().loadList("userList", ("main" + parts).split(","), ("count" + parts).split(","), param, null, null);
            return (PersonTO) vlh.getList(true).get(0);
        } catch (ListException e) {
            throw new BaseException(MapperExceptionCode.PRM_001, MapperExceptionCode.PRM_001_MSG, e);
        } catch (ListHandlerException e) {
            throw new BaseException(MapperExceptionCode.PRM_002, MapperExceptionCode.PRM_002_MSG, e);
        }
    }
}