package com.gam.nocr.ems.biz.service.internal.impl;

import static com.gam.nocr.ems.config.EMSLogicalNames.DAO_ENROLLMENT_OFFICE;
import static com.gam.nocr.ems.config.EMSLogicalNames.DAO_PERSON;
import static com.gam.nocr.ems.config.EMSLogicalNames.DAO_PERSON_TOKEN;
import static com.gam.nocr.ems.config.EMSLogicalNames.SRV_GAAS;
import static com.gam.nocr.ems.config.EMSLogicalNames.getDaoJNDIName;
import static com.gam.nocr.ems.config.EMSLogicalNames.getExternalServiceJNDIName;
import gampooya.tools.vlp.ListException;
import gampooya.tools.vlp.ValueListHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.transaction.UserTransaction;

import org.apache.commons.validator.EmailValidator;
import org.displaytag.exception.ListHandlerException;
import org.nocr.NIN;
import org.slf4j.Logger;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.BizLoggable;
import com.gam.commons.core.biz.service.Permissions;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.commons.core.data.domain.SearchResult;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.biz.service.GAASService;
import com.gam.nocr.ems.biz.service.PersonManagementService;
import com.gam.nocr.ems.biz.service.TokenManagementService;
import com.gam.nocr.ems.biz.service.UserManagementService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.EMSValueListProvider;
import com.gam.nocr.ems.data.dao.EnrollmentOfficeDAO;
import com.gam.nocr.ems.data.dao.PersonDAO;
import com.gam.nocr.ems.data.dao.PersonTokenDAO;
import com.gam.nocr.ems.data.domain.DepartmentTO;
import com.gam.nocr.ems.data.domain.EMSAutocompleteTO;
import com.gam.nocr.ems.data.domain.EnrollmentOfficeTO;
import com.gam.nocr.ems.data.domain.PersonTO;
import com.gam.nocr.ems.data.domain.PersonTokenTO;
import com.gam.nocr.ems.data.domain.vol.PermissionVTO;
import com.gam.nocr.ems.data.domain.vol.PermissionVTOWrapper;
import com.gam.nocr.ems.data.domain.vol.PersonInfoVTO;
import com.gam.nocr.ems.data.domain.vol.PersonVTO;
import com.gam.nocr.ems.data.domain.vol.RoleVTO;
import com.gam.nocr.ems.data.domain.vol.RoleVTOWrapper;
import com.gam.nocr.ems.data.domain.vol.UserInfoVTO;
import com.gam.nocr.ems.data.domain.ws.PermissionsWTO;
import com.gam.nocr.ems.data.enums.BooleanType;
import com.gam.nocr.ems.data.enums.PersonRequestStatus;
import com.gam.nocr.ems.data.enums.ReplicaReason;
import com.gam.nocr.ems.data.enums.TokenOrigin;
import com.gam.nocr.ems.data.enums.TokenState;
import com.gam.nocr.ems.data.enums.TokenType;
import com.gam.nocr.ems.data.mapper.tomapper.PersonMapper;
import com.gam.nocr.ems.util.EmsUtil;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

/**
 * <p> TODO -- Explain this class </p>
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
@Stateless(name = "PersonManagementService")
@Local(PersonManagementServiceLocal.class)
@Remote(PersonManagementServiceRemote.class)
public class PersonManagementServiceImpl extends EMSAbstractService implements PersonManagementServiceLocal, PersonManagementServiceRemote {

    private static final Logger logger = BaseLog.getLogger(PersonManagementServiceImpl.class);

    @Resource
    UserTransaction utx;

    @Resource
    SessionContext sessionContext;

    @Override
    @Permissions(value = "ems_editPerson || ems_addPerson")
    @BizLoggable(logAction = "INSERT", logEntityName = "PERSON")
    @TransactionAttribute(TransactionAttributeType.NEVER)
    public Long save(PersonVTO to) throws BaseException {
        if (to == null)
            throw new ServiceException(BizExceptionCode.PSI_002, BizExceptionCode.PSI_002_MSG);

        PersonInfoVTO personInfoVTO = PersonMapper.convert(to);

        validatePerson(personInfoVTO.getPersonTO(), null);

        try {
            try {
                utx.begin();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            PersonTO personTO = savePerson(personInfoVTO.getPersonTO());
            utx.commit();

            if (PersonRequestStatus.APPROVED.equals(personTO.getRequestStatus())) {
                GAASService gaasService = getGaasService();

                personInfoVTO.getUserInfoVTO().setPersonId(personTO.getId());

                int person_userId = gaasService.save(personInfoVTO.getUserInfoVTO());

                if (gaasService.hasTokenPermission(person_userId)) {
                    getTokenManagementService().issueFirstPersonToken(personTO.getId(), TokenType.AUTHENTICATION);
                    gaasService.disableUser(person_userId);
                    personTO.setStatus(BooleanType.F);
                } else {
                    gaasService.enableUser(person_userId);
                    personTO.setStatus(BooleanType.T);
                }

                if (person_userId != 0) {
                    utx.begin();

                    PersonDAO personDAO = getPersonDAO();

                    personTO.setUserId(person_userId);
                    personDAO.update(personTO);
                    to.setlStatus(personTO.getStatus().toString());
                    utx.commit();
                } else {
                    throw new ServiceException(BizExceptionCode.PSI_007, BizExceptionCode.PSI_007_MSG);
                }
            }

            return personTO.getId();
        } catch (Exception e) {
            try {
                utx.rollback();
            } catch (Exception ex) {
                logger.error(e.getMessage(), e);
            }
            if (e instanceof BaseException)
                throw (BaseException) e;
            else
                throw new ServiceException(BizExceptionCode.PSI_028, BizExceptionCode.PSI_028_MSG);
        }
    }

    private PersonTO savePerson(PersonTO to) throws BaseException {
        try {
            if (to == null)
                throw new ServiceException(BizExceptionCode.PSI_045, BizExceptionCode.PSI_002_MSG);

            PersonDAO personDAO = getPersonDAO();

            PersonTO loadedPerson = personDAO.findByNationalId(to.getNid());

            PersonTO persistedPerson;

            if (loadedPerson == null) {
                persistedPerson = personDAO.create(to);
            } else {
                if (loadedPerson.getUserId() == null && !PersonRequestStatus.REJECTED.equals(loadedPerson.getRequestStatus())) {
                    personDAO.delete(loadedPerson);
                    persistedPerson = personDAO.create(to);
                } else {
                    throw new ServiceException(BizExceptionCode.PSI_046, BizExceptionCode.PSI_046_MSG);
                }
            }
            return persistedPerson;
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.PSI_008, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    @Permissions(value = "ems_editPerson")
    @BizLoggable(logAction = "UPDATE", logEntityName = "PERSON")
    @TransactionAttribute(TransactionAttributeType.NEVER)
    public Long update(PersonVTO to) throws BaseException {
        try {
            if (to == null)
                throw new ServiceException(BizExceptionCode.PSI_002, BizExceptionCode.PSI_002_MSG);

            PersonDAO personDAO = getPersonDAO();

            PersonTO oldPerson = personDAO.find(PersonTO.class, to.getId());

            PersonInfoVTO personInfoVTO = PersonMapper.convert(to);

            validatePerson(personInfoVTO.getPersonTO(), oldPerson);

            PersonTO personTO = personDAO.update(personInfoVTO.getPersonTO());

            GAASService gaasService = getGaasService();

            boolean hadTokenPermission = gaasService.hasTokenPermission(personTO.getUserId());

            personInfoVTO.getUserInfoVTO().setPersonId(personTO.getId());
            gaasService.update(personInfoVTO.getUserInfoVTO());

            NamedCache permissionCache = CacheFactory.getCache("repl.timelim.lru.gam.tools.permissionCache");
            permissionCache.remove(personTO.getUserName());

            if (personTO.getId() != null) {
                NamedCache userNameCache = CacheFactory.getCache("repl.timelim.lru.gam.tools.userNameCache");
                userNameCache.remove(personTO.getId() + "");
            }

            Boolean hasTokenPermission = gaasService.hasTokenPermission(personTO.getUserId());

            Integer userId = personDAO.findUserIdByPersonId(personTO.getId());

            if (!hadTokenPermission && hasTokenPermission) {
                getTokenManagementService().issueFirstPersonToken(personTO.getId(), TokenType.AUTHENTICATION);
                gaasService.disableUser(userId);
                personTO.setStatus(BooleanType.F);
                personDAO.update(personTO);
            }
            if (hadTokenPermission && !hasTokenPermission) {
                PersonTokenTO personTokenTO = getPersonTokenDAO().findDeliveredByPersonIdAndType(personTO.getId(), TokenType.AUTHENTICATION);
                if (personTokenTO != null) {
                    getTokenManagementService().revokePersonToken(personTokenTO.getId(), ReplicaReason.UNSPECIFIED, "");
                    gaasService.enableUser(userId);
                    personTO.setStatus(BooleanType.T);
                    personDAO.update(personTO);
                }
            }
            //********** Anbari -userPerm-commented
            //getUserManagementService().updatePermissionCache(personTO.getId());

            return personTO.getId();
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.PSI_004, BizExceptionCode.GLB_008_MSG, e);
        }
    }
    
    
 // Anbari
 	private PersonManagementService getPersonService() throws BaseException {
 		PersonManagementService personManagementService;
 		try {
 			personManagementService = (PersonManagementService) ServiceFactoryProvider
 					.getServiceFactory()
 					.getService(
 							EMSLogicalNames
 									.getServiceJNDIName(EMSLogicalNames.SRV_PERSON),
 							null);
 		} catch (ServiceFactoryException e) {
 			throw new DelegatorException(BizExceptionCode.PDL_001,
 					BizExceptionCode.GLB_002_MSG, e,
 					EMSLogicalNames.SRV_PERSON.split(","));
 		}
 		return personManagementService;
 	}

    @Override
    @Permissions(value = "ems_viewPerson")
    @BizLoggable(logAction = "LOAD", logEntityName = "PERSON")
    public PersonVTO load(Long personId) throws BaseException {
        try {
            if (personId == 0)
                throw new ServiceException(BizExceptionCode.PSI_003, BizExceptionCode.PSI_003_MSG);

            PersonDAO personDAO = getPersonDAO();
            PersonTO personTO = personDAO.find(PersonTO.class, personId);
            if (personTO.getDepartment() != null)
                personTO.setDepartmentName(personTO.getDepartment().getName());

            UserInfoVTO gaasUser = null;
            if (personTO.getRequestStatus().equals(PersonRequestStatus.APPROVED)) {
                GAASService service = getGaasService();
                gaasUser = service.getUser(personTO.getUserId());
            }
            PersonVTO personVTO = PersonMapper.convert(personTO, gaasUser);

            if (personDAO.checkIsAdmin(personId))
                personVTO.setAdmin("T");
            else
                personVTO.setAdmin("F");

            return personVTO;
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.PSI_010, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    @Permissions(value = "ems_removePerson")
    @BizLoggable(logAction = "DELETE", logEntityName = "PERSON")
    public boolean remove(String personIds) throws BaseException {
        try {
            if (personIds == null || personIds.trim().length() == 0)
                throw new ServiceException(BizExceptionCode.PSI_012, BizExceptionCode.PSI_012_MSG);

            PersonDAO personDAO = getPersonDAO();
//			Integer userId = personDAO.findUserIdByPersonId(Long.parseLong(personIds));
            PersonTO personTO = personDAO.find(PersonTO.class, Long.parseLong(personIds));

            Integer userId = null;
            if (personTO != null && personTO.getUserId() != null) {
                if ("gaasadmin".equals(personTO.getUserName()))
                    throw new ServiceException(BizExceptionCode.PSI_072, BizExceptionCode.PSI_072_MSG);

                userId = personTO.getUserId();
            }

            GAASService gaasService = getGaasService();
            if (userId != null) {
                gaasService.disableUser(userId);
                personDAO.changeStatus(Long.parseLong(personIds), BooleanType.F);
            } else {
                if (personTO.getRequestStatus().equals(PersonRequestStatus.APPROVED))
                    throw new ServiceException(BizExceptionCode.PSI_058, BizExceptionCode.PSI_058_MSG + personIds);
            }

            List<PersonTokenTO> personTokenTOList = getPersonTokenDAO().findNotRevokedByPersonId(Long.parseLong(personIds));

            if (EmsUtil.checkListSize(personTokenTOList))
                throw new ServiceException(BizExceptionCode.PSI_071, BizExceptionCode.PSI_071_MSG);

//			if (personTokenTOList.size() != 0) {
//				int counter = 0;
//				for (PersonTokenTO personTokenTO : personTokenTOList) {
//					try {
//						getTokenManagementService().revokePersonToken(personTokenTO.getId(), ReplicaReason.UNSPECIFIED, "");
//						counter++;
//					} catch (BaseException e) {
//						if (counter > 0)
//							throw new ServiceException(BizExceptionCode.PSI_057, BizExceptionCode.PSI_057_MSG + personTokenTO.getId());
//						else {
//							gaasService.enableUser(userId);
//							throw new ServiceException(BizExceptionCode.PSI_056, BizExceptionCode.PSI_056_MSG);
//						}
//					}
//				}
//			}

            getPersonDAO().removePersons(personIds);
            return true;
        } catch (Exception e) {
            if (e instanceof BaseException) {
                if (BizExceptionCode.PSI_057.equals(((BaseException) e).getExceptionCode())) {
                    throw (BaseException) e;
                } else {
                    sessionContext.setRollbackOnly();
                    throw (BaseException) e;
                }
            } else {
                sessionContext.setRollbackOnly();
                throw new ServiceException(BizExceptionCode.PSI_055, BizExceptionCode.PSI_055_MSG, e);
            }
        }
    }

    @Override
    @Permissions(value = "ems_changePersonStatus")
    @BizLoggable(logAction = "CHANGE_STATE", logEntityName = "PERSON")
    public void changeStatus(Long personId) throws BaseException {
        try {
            PersonDAO personDAO = getPersonDAO();
            PersonTO personTO = personDAO.find(PersonTO.class, personId);

            List<PersonTokenTO> personTokenTOList = getPersonTokenDAO().findNotRevokedOrDeliveredByPersonIdAndType(personTO.getId(), TokenType.AUTHENTICATION);

            if (personTO.getStatus().equals(BooleanType.T)) {
                getGaasService().disableUser(personTO.getUserId());
                personDAO.changeStatus(personTO);
            } else if (personTO.getStatus().equals(BooleanType.F)) {
                if (personTokenTOList.size() == 0) {
                    getGaasService().enableUser(personTO.getUserId());
                    personDAO.changeStatus(personTO);
                } else {
                    //	If user has any authentication token in PENDING_FOR_ISSUE or READY_TO_DELIVER or READY_TO_ISSUE state,
                    throw new ServiceException(BizExceptionCode.PSI_043, BizExceptionCode.PSI_043_MSG);
                }
            }
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.PSI_011, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    public void rejectPerson(String personIds) throws BaseException {
        try {
            PersonDAO personDAO = getPersonDAO();

            if (!EmsUtil.checkString(personIds))
                throw new ServiceException(BizExceptionCode.PSI_064, BizExceptionCode.PSI_064_MSG);

            List<Long> ids = new ArrayList<Long>();
            try {
                for (String id : personIds.split(","))
                    ids.add(Long.parseLong(id.trim()));
            } catch (NumberFormatException e) {
                throw new ServiceException(BizExceptionCode.PSI_066, BizExceptionCode.PSI_064_MSG, e);
            }

            personDAO.updatePersonRequestState(ids, PersonRequestStatus.REJECTED);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.PSI_009, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    public String fetchRolePermissionList(String roleId) throws BaseException {
        try {
            if (!EmsUtil.checkString(roleId))
                throw new ServiceException(BizExceptionCode.PSI_073, BizExceptionCode.PSI_073_MSG);

            GAASService gaasService = getGaasService();

            List<PermissionVTO> permissionVTOList = gaasService.getRolePermission(Integer.parseInt(roleId), 0, 0);

            StringBuilder permissionList = new StringBuilder();
            if (EmsUtil.checkListSize(permissionVTOList)) {
                for (PermissionVTO permissionVTO : permissionVTOList) {
                    permissionList.append(permissionVTO.getName()).append("\n");
                }
            }

            return permissionList.toString();
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.PSI_074, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    public String informAcceptableTypes(Long personId) throws BaseException {
        try {
            List<PersonTokenTO> personTokenTOList = getPersonTokenDAO().findByPersonId(personId);

            if (EmsUtil.checkListSize(personTokenTOList)) {
                String acceptableTypes = ",A,S,E";
                List<TokenState> tokenStates = new ArrayList<TokenState>();
                tokenStates.add(TokenState.DELIVERED);
                tokenStates.add(TokenState.REVOKED);

                for (PersonTokenTO personTokenTO : personTokenTOList) {
                    if (!tokenStates.contains(personTokenTO.getState())) {
                        String sub = String.valueOf(personTokenTO.getType().toString().charAt(0));
                        acceptableTypes = acceptableTypes.replaceAll("," + sub, "");
                    }
                }
                return ((acceptableTypes.trim().length() > 0) ? acceptableTypes.substring(1) : "");

            }
            return "A,S,E";
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.PSI_053, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    public DepartmentTO loadDepartmentByPersonId(Long personId) throws BaseException {
        try {
            if (personId == null)
                throw new ServiceException(BizExceptionCode.PSI_059, BizExceptionCode.PSI_059_MSG);

            DepartmentTO departmentTO = getPersonDAO().findDepartmentByPersonId(personId);
            if (departmentTO != null)
                return departmentTO;
            else
                throw new ServiceException(BizExceptionCode.PSI_060, BizExceptionCode.PSI_060_MSG);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.PSI_063, BizExceptionCode.GLB_008_MSG, e);
        }
    }


    // ================== Token Service methods ==================
    @Override
    @Permissions(value = "ems_issuePersonToken")
    public Long issueToken(Long personId, TokenType type) throws BaseException {
        try {
            if (personId == null)
                throw new ServiceException(BizExceptionCode.PSI_023, BizExceptionCode.PSI_023_MSG);
            if (type == null)
                throw new ServiceException(BizExceptionCode.PSI_024, BizExceptionCode.PSI_024_MSG);

            Long tokenId = getTokenManagementService().issueFirstPersonToken(personId, type);

            PersonDAO personDAO = getPersonDAO();

            Integer userId = personDAO.findUserIdByPersonId(personId);
            if (TokenType.AUTHENTICATION.equals(type)) {
                getGaasService().disableUser(userId);
                personDAO.changeStatus(personId, BooleanType.F);
            }

            return tokenId;
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.PSI_025, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    @Permissions(value = "ems_issuePersonToken")
    public Long reissueToken(Long tokenId) throws BaseException {
        try {
            if (tokenId == null)
                throw new ServiceException(BizExceptionCode.PSI_026, BizExceptionCode.PSI_026_MSG);

            return getTokenManagementService().reIssuePersonTokenRequest(tokenId);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.PSI_030, BizExceptionCode.GLB_008_MSG, e);
        }
    }
    
    //Adldoost
    @Override
//    @Permissions(value = "ems_renewPersonToken")
    public Long renewalToken(Long tokenId) throws BaseException {
        try {
            if (tokenId == null)
                throw new ServiceException(BizExceptionCode.PSI_081, BizExceptionCode.PSI_026_MSG);

            return getTokenManagementService().renewalPersonTokenRequest(tokenId);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.PSI_082, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    @Permissions(value = "ems_issuePersonToken")
    public Long replicateToken(Long personId, TokenType tokenType, ReplicaReason reason) throws BaseException {
        try {
            if (personId == null)
                throw new ServiceException(BizExceptionCode.PSI_027, BizExceptionCode.PSI_023_MSG);
            if (reason == null)
                throw new ServiceException(BizExceptionCode.PSI_029, BizExceptionCode.PSI_029_MSG);

            return getTokenManagementService().replicatePersonToken(personId, tokenType, reason);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.PSI_065, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    @Permissions(value = "ems_revokePersonToken")
    public void revokeToken(Long tokenId, ReplicaReason reason, String comment) throws BaseException {
        try {
            if (tokenId == null)
                throw new ServiceException(BizExceptionCode.PSI_032, BizExceptionCode.PSI_026_MSG);

            getTokenManagementService().revokePersonToken(tokenId, reason, comment);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.PSI_067, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    @Permissions(value = "ems_deliverPersonToken")
    public void deliverToken(Long tokenId) throws BaseException {
        try {
            if (tokenId == null)
                throw new ServiceException(BizExceptionCode.PSI_031, BizExceptionCode.PSI_026_MSG);

            getTokenManagementService().deliverPersonToken(tokenId);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.PSI_068, BizExceptionCode.GLB_008_MSG, e);
        }
    }
    // =================== Token Service methods ==================

    
    
    
//    @Override
//    @Permissions(value = "ems_viewPerson")
//    public SearchResult fetchRoles(String searchString, int from, int to, String orderBy) throws BaseException {
//        try {
//            PersonTO personTO = getPersonDAO().find(PersonTO.class, getUserProfileTO().getPersonID());
//            List<RoleVTO> roleVTOs = getGaasService().getRoles(personTO.getUserId(), "%" + searchString + "%", from, to);
//            List<EMSAutocompleteTO> autocompleteTOs = new ArrayList<EMSAutocompleteTO>();
//
//            for (RoleVTO roleVTO : roleVTOs) {
//                EMSAutocompleteTO autocompleteTO = new EMSAutocompleteTO();
//                autocompleteTO.setAcType("ROLE");
//                autocompleteTO.setAcId(roleVTO.getId());
//                autocompleteTO.setAcName(roleVTO.getName());
//                autocompleteTOs.add(autocompleteTO);
//            }
//
//            return new SearchResult(roleVTOs.size(), autocompleteTOs);
//        } catch (BaseException e) {
//            throw e;
//        } catch (Exception e) {
//            throw new ServiceException(BizExceptionCode.PSI_068, BizExceptionCode.GLB_008_MSG, e);
//        }
//    }
    
    @Override
    @Permissions(value = "ems_viewPerson")
    public SearchResult fetchRoles(String searchString, int from, int to, String orderBy) throws BaseException {
        try {
        	Long personID = getPersonDAO().findPersonIdByUsername(getUserProfileTO().getUserName());
            PersonTO personTO = getPersonDAO().find(PersonTO.class, personID);
            RoleVTOWrapper roleWrapper = getGaasService().getRolesWithCount(personTO.getUserId(), "%" + searchString + "%", from, to);
            
            List<RoleVTO> roleVTOs = roleWrapper.getRoles();
            Integer count = roleWrapper.getCount();
            
            List<EMSAutocompleteTO> autocompleteTOs = new ArrayList<EMSAutocompleteTO>();

            for (RoleVTO roleVTO : roleVTOs) {
                EMSAutocompleteTO autocompleteTO = new EMSAutocompleteTO();
                autocompleteTO.setAcType("ROLE");
                autocompleteTO.setAcId(roleVTO.getId());
                autocompleteTO.setAcName(roleVTO.getName());
                autocompleteTOs.add(autocompleteTO);
            }

            return new SearchResult(count, autocompleteTOs);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.PSI_068, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    
//    @Override
//    @Permissions(value = "ems_viewPerson")
//    public SearchResult fetchPermissions(String searchString, int from, int to, String orderBy) throws BaseException {
//        try {
//            PersonTO personTO = getPersonDAO().find(PersonTO.class, getUserProfileTO().getPersonID());
//            List<PermissionVTO> permissionVTOs = getGaasService().getAllUserPermission(personTO.getUserId(), "%" + searchString + "%", from, to);
//            List<EMSAutocompleteTO> autocompleteTOs = new ArrayList<EMSAutocompleteTO>();
//
//            for (PermissionVTO permissionVTO : permissionVTOs) {
//                EMSAutocompleteTO autocompleteTO = new EMSAutocompleteTO();
//                autocompleteTO.setAcType("PERM");
//                autocompleteTO.setAcId(permissionVTO.getId());
//                autocompleteTO.setAcName(permissionVTO.getName());
//                autocompleteTOs.add(autocompleteTO);
//            }
//            
//            List<PermissionVTO> allPermission = getGaasService().getAllUserPermission(personTO.getUserId(), "%" + searchString + "%", 0, Integer.MAX_VALUE);
//            
//            return new SearchResult(allPermission.size(), autocompleteTOs);
//        } catch (BaseException e) {
//            throw e;
//        } catch (Exception e) {
//            throw new ServiceException(BizExceptionCode.PSI_069, BizExceptionCode.GLB_008_MSG, e);
//        }
//    }
    
    @Override
    @Permissions(value = "ems_viewPerson")
    public SearchResult fetchPermissions(String searchString, int from, int to, String orderBy) throws BaseException {
        try {
        	Long personID = getPersonDAO().findPersonIdByUsername(getUserProfileTO().getUserName());
            PersonTO personTO = getPersonDAO().find(PersonTO.class,personID);
            PermissionVTOWrapper permissionVTOWrapper = getGaasService().getAllUserPermissionWithCount(personTO.getUserId(), "%" + searchString + "%", from, to);
            
            List<PermissionVTO> permissionVTOs = permissionVTOWrapper.getPermissions();
            Integer permissionCount = permissionVTOWrapper.getCount();
            
            List<EMSAutocompleteTO> autocompleteTOs = new ArrayList<EMSAutocompleteTO>();

            for (PermissionVTO permissionVTO : permissionVTOs) {
                EMSAutocompleteTO autocompleteTO = new EMSAutocompleteTO();
                autocompleteTO.setAcType("PERM");
                autocompleteTO.setAcId(permissionVTO.getId());
                autocompleteTO.setAcName(permissionVTO.getName());
                autocompleteTOs.add(autocompleteTO);
            }
            
//            List<PermissionVTO> allPermission = getGaasService().getAllUserPermission(personTO.getUserId(), "%" + searchString + "%", 0, Integer.MAX_VALUE);
            
            return new SearchResult(permissionCount, autocompleteTOs);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.PSI_069, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    private PersonDAO getPersonDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(getDaoJNDIName(DAO_PERSON));
        } catch (DAOFactoryException e) {
            throw new ServiceException(
                    BizExceptionCode.PSI_001,
                    BizExceptionCode.GLB_001_MSG,
                    e,
                    new String[]{EMSLogicalNames.DAO_PERSON});
        }
    }

    private PersonTokenDAO getPersonTokenDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(getDaoJNDIName(DAO_PERSON_TOKEN));
        } catch (DAOFactoryException e) {
            throw new ServiceException(
                    BizExceptionCode.PSI_039,
                    BizExceptionCode.GLB_001_MSG,
                    e,
                    new String[]{EMSLogicalNames.DAO_PERSON_TOKEN});
        }
    }

    private EnrollmentOfficeDAO getEnrollmentOfficeDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(getDaoJNDIName(DAO_ENROLLMENT_OFFICE));
        } catch (DAOFactoryException e) {
            throw new ServiceException(
                    BizExceptionCode.PSI_019,
                    BizExceptionCode.GLB_001_MSG,
                    e,
                    new String[]{EMSLogicalNames.DAO_ENROLLMENT_OFFICE});
        }
    }

    private GAASService getGaasService() throws BaseException {
        GAASService gaasService = null;
        try {
            gaasService = ServiceFactoryProvider.getServiceFactory().getService(getExternalServiceJNDIName(SRV_GAAS), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(
                    BizExceptionCode.PSI_020,
                    BizExceptionCode.GLB_002_MSG,
                    e,
                    new String[]{EMSLogicalNames.SRV_GAAS});
        }
        gaasService.setUserProfileTO(getUserProfileTO());
        return gaasService;
    }

    private TokenManagementService getTokenManagementService() throws BaseException {
        TokenManagementService tokenManagementService = null;
        try {
            tokenManagementService = (TokenManagementService) ServiceFactoryProvider.getServiceFactory().getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_TOKEN_MANAGEMENT), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(
                    BizExceptionCode.PSI_005,
                    BizExceptionCode.GLB_002_MSG,
                    e,
                    EMSLogicalNames.SRV_TOKEN_MANAGEMENT.split(","));
        }
        tokenManagementService.setUserProfileTO(getUserProfileTO());
        return tokenManagementService;
    }

    private void validatePerson(PersonTO newPersonTO, PersonTO oldPersonTO) throws BaseException {
        // First Name
        if (isNullOrEmptyString(newPersonTO.getFirstName()))
            throw new ServiceException(BizExceptionCode.PSI_013, BizExceptionCode.PSI_013_MSG);
        if (isValidLength(newPersonTO.getFirstName(), 32, false))
            throw new ServiceException(BizExceptionCode.PSI_033, "First Name" + BizExceptionCode.GLB_004_MSG + "below 255 character");
        if (!EmsUtil.checkRegex(newPersonTO.getFirstName(), EmsUtil.persianAlphaConstraint))
            throw new ServiceException(BizExceptionCode.PSI_044, BizExceptionCode.PSI_044_MSG);
        // Last Name
        if (isNullOrEmptyString(newPersonTO.getLastName()))
            throw new ServiceException(BizExceptionCode.PSI_014, BizExceptionCode.PSI_014_MSG);
        if (isValidLength(newPersonTO.getLastName(), 32, false))
            throw new ServiceException(BizExceptionCode.PSI_034, "Last Name" + BizExceptionCode.GLB_004_MSG + "below 255 character");
        if (!EmsUtil.checkRegex(newPersonTO.getLastName(), EmsUtil.persianAlphaConstraint))
            throw new ServiceException(BizExceptionCode.PSI_047, BizExceptionCode.PSI_047_MSG);
        // National ID
        if (isNullOrEmptyString(newPersonTO.getNid()))
            throw new ServiceException(BizExceptionCode.PSI_015, BizExceptionCode.PSI_015_MSG);
        if (!isValidLength(newPersonTO.getNid(), 10, true)) {
            try {
                if (!NIN.validate(Long.valueOf(newPersonTO.getNid())))
                    throw new ServiceException(BizExceptionCode.PSI_076, BizExceptionCode.PSI_076_MSG);
            } catch (NumberFormatException e) {
                throw new ServiceException(BizExceptionCode.PSI_076, BizExceptionCode.PSI_076_MSG);
            }
        } else {
            throw new ServiceException(BizExceptionCode.PSI_016, "National ID" + BizExceptionCode.GLB_005_MSG + "10 digit");
        }
        // Father Name
        if (isNullOrEmptyString(newPersonTO.getFatherName()))
            throw new ServiceException(BizExceptionCode.PSI_022, BizExceptionCode.PSI_022_MSG);
        if (isValidLength(newPersonTO.getFatherName(), 42, false))
            throw new ServiceException(BizExceptionCode.PSI_037, "Father Name" + BizExceptionCode.GLB_004_MSG + "255 character");
        if (!EmsUtil.checkRegex(newPersonTO.getFatherName(), EmsUtil.persianAlphaConstraint))
            throw new ServiceException(BizExceptionCode.PSI_051, BizExceptionCode.PSI_051_MSG);
        // Birth Certificate Number
        if (isNullOrEmptyString(newPersonTO.getBirthCertNum()))
            throw new ServiceException(BizExceptionCode.PSI_006, BizExceptionCode.PSI_006_MSG);
        if (isValidLength(newPersonTO.getBirthCertNum(), 10, false))
            throw new ServiceException(BizExceptionCode.PSI_035, "Birth Certificate Number" + BizExceptionCode.GLB_004_MSG + "10 character");
        if (!EmsUtil.checkRegex(newPersonTO.getBirthCertNum(), EmsUtil.numberConstraint))
            throw new ServiceException(BizExceptionCode.PSI_049, BizExceptionCode.PSI_049_MSG);
        // Birth Certificate Series
        if (isNullOrEmptyString(newPersonTO.getBirthCertSeries()))
            throw new ServiceException(BizExceptionCode.PSI_021, BizExceptionCode.PSI_021_MSG);
        if (isValidLength(newPersonTO.getBirthCertSeries(), 6, false))
            throw new ServiceException(BizExceptionCode.PSI_036, "Birth Certificate Series" + BizExceptionCode.GLB_004_MSG + "10 character");
        if (!EmsUtil.checkRegex(newPersonTO.getBirthCertSeries(), EmsUtil.birthCertSerialConstraint)
                || newPersonTO.getBirthCertSeries().equals("100000"))
            throw new ServiceException(BizExceptionCode.PSI_054, BizExceptionCode.PSI_054_MSG);
        // Email
        if (isNullOrEmptyString(newPersonTO.getEmail()))
            throw new ServiceException(BizExceptionCode.PSI_040, BizExceptionCode.PSI_040_MSG);
        if (isValidLength(newPersonTO.getEmail(), 255, false))
            throw new ServiceException(BizExceptionCode.PSI_041, "Email" + BizExceptionCode.GLB_004_MSG + "255 character");
        if (!EmailValidator.getInstance().isValid(newPersonTO.getEmail()))
            throw new ServiceException(BizExceptionCode.PSI_042, BizExceptionCode.PSI_042_MSG);

        if (PersonRequestStatus.APPROVED.equals(newPersonTO.getRequestStatus())) {
            // User Name
            if (isNullOrEmptyString(newPersonTO.getUserName()))
                throw new ServiceException(BizExceptionCode.PSI_017, BizExceptionCode.PSI_017_MSG);
            if (isValidLength(newPersonTO.getUserName(), 20, false))
                throw new ServiceException(BizExceptionCode.PSI_038, "User Name" + BizExceptionCode.GLB_004_MSG + "20 character");
//			if (!EmsUtil.checkRegex(newPersonTO.getUserName(), EmsUtil.latinAlphaConstraint))
//				throw new ServiceException(BizExceptionCode.PSI_052, BizExceptionCode.PSI_052_MSG);
            // Department
            if (newPersonTO.getDepartment().getId() == null)
                throw new ServiceException(BizExceptionCode.PSI_018, BizExceptionCode.PSI_018_MSG);
            else {
                if (oldPersonTO != null && oldPersonTO.getDepartment() != null) {
                    if (!oldPersonTO.getDepartment().getId().equals(newPersonTO.getDepartment().getId())) {
                        EnrollmentOfficeDAO enrollmentOfficeDAO = getEnrollmentOfficeDAO();

                        EnrollmentOfficeTO enrollmentOfficeTO = enrollmentOfficeDAO.
                                fetchOfficeByIdAndManagerId(oldPersonTO.getDepartment().getId(), oldPersonTO.getId());

                        if (enrollmentOfficeTO != null) {
                        	
                        	throw new ServiceException(BizExceptionCode.PSI_075, BizExceptionCode.PSI_075_MSG);
                        	
//                            if (EnrollmentOfficeType.NOCR.equals(enrollmentOfficeTO.getType())) {
//                                throw new ServiceException(BizExceptionCode.PSI_080, BizExceptionCode.PSI_080_MSG);
//                            } else {
//                                List<TokenState> tokenStates = new ArrayList<TokenState>();
//                                tokenStates.add(TokenState.READY_TO_ISSUE);
//                                tokenStates.add(TokenState.PENDING_TO_ISSUE);
//                                tokenStates.add(TokenState.READY_TO_DELIVER);
//                                tokenStates.add(TokenState.DELIVERED);
//                                tokenStates.add(TokenState.PKI_ERROR);
//                                tokenStates.add(TokenState.PROCESSED);
//
////Edited By Adldoost
////                                EnrollmentOfficeTO to = getEnrollmentOfficeDAO().
////                                        fetchOfficeByIdAndTokenState(oldPersonTO.getDepartment().getId(), tokenStates);
////
//                                EnrollmentOfficeTO to = getEnrollmentOfficeDAO().find(EnrollmentOfficeTO.class, oldPersonTO.getDepartment().getId());
// ////////////////////                               
//                                if (to == null) {
//                                    enrollmentOfficeTO.setManager(null);
//                                    enrollmentOfficeDAO.update(enrollmentOfficeTO);
//                                } else {
//                                    throw new ServiceException(BizExceptionCode.PSI_075, BizExceptionCode.PSI_075_MSG);
//                                }
//                        }
                        }
                    }
                }
            }
        }
    }

    private boolean isNullOrEmptyString(String param) {
        return param == null || param.trim().length() == 0;
    }

    private boolean isValidLength(String str, Integer length, boolean fixed) {
        if (fixed)
            return (!isNullOrEmptyString(str) && str.length() != length);
        else
            return (!isNullOrEmptyString(str) && str.length() > length);
    }

    @Override
    @Permissions(value = "ems_viewUserList")
    public SearchResult fetchPersons(String searchString, int from, int to, String orderBy) throws BaseException {
        HashMap param = new HashMap();
        param.put("perName", "%" + searchString + "%");
        param.put("perNID", "%" + searchString + "%");
        try {
            ValueListHandler vlh = EMSValueListProvider.getProvider().loadList("personAC", "main".split(","), "count".split(","), param, orderBy, null);
            List list = vlh.getList(from, to, true);
            return new SearchResult(vlh.size(), list);
        } catch (ListException e) {
            throw new ServiceException(BizExceptionCode.PSI_061, BizExceptionCode.GLB_006_MSG, e);
        } catch (ListHandlerException e) {
            throw new ServiceException(BizExceptionCode.PSI_062, BizExceptionCode.GLB_007_MSG, e);
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.PSI_070, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    public boolean delegate(PersonVTO to) throws BaseException {
        Integer userId = getPersonDAO().findUserIdByPersonId(to.getId());

        return userId != null;
    }

    @Override
    public void deleteToken(Long tokenId) throws BaseException {
        getTokenManagementService().deleteToken(tokenId, TokenOrigin.PERSON);
    }
    
    @Override
    @Permissions(value = "ems_deleteRenewalTokenRequest")
    public void deleteRenewalTokenRequest(Long tokenId) throws BaseException {
        getTokenManagementService().deleteToken(tokenId, TokenOrigin.PERSON);
    }

	
	@Override
	public SearchResult fetchPersonsByDepartmentId(String searchString, int from, int to,
			String orderBy, Map additionalParams) throws BaseException {
		HashMap param = new HashMap();
		param.put("perName", "%" + searchString + "%");
		param.put("departmentId", additionalParams.get("departmentId"));
		
		try {
			ValueListHandler vlh = EMSValueListProvider.getProvider().loadList(
					"personByDepartmentIdAC", "main".split(","), "count".split(","), param,
					orderBy, null);
			List list = vlh.getList(from, to, true);
			return new SearchResult(vlh.size(), list);
		} catch (ListException e) {
			throw new ServiceException(BizExceptionCode.PSI_061,
					BizExceptionCode.GLB_006_MSG, e);
		} catch (ListHandlerException e) {
			throw new ServiceException(BizExceptionCode.PSI_062,
					BizExceptionCode.GLB_007_MSG, e);
		} catch (Exception e) {
			throw new ServiceException(BizExceptionCode.PSI_070,
					BizExceptionCode.GLB_008_MSG, e);
		}
	}

	@Override
	public List<Long> findListDepartmentsByPersonId(Long personId) throws BaseException {
		List<Long> ids = getPersonDAO().findListDepartmentsByPersonId(personId);
		return ids;
	}
	
	// Anbari
	@Override
	public Long findPersonIdByUsername(String username) throws BaseException {
		return getPersonDAO().findPersonIdByUsername(username);
	}
	
	
    //Anbari
    private UserManagementService getUserManagementService() throws BaseException {
        UserManagementService userManagementService;
        try {
            userManagementService = ServiceFactoryProvider.getServiceFactory().getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_USER), null);
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(BizExceptionCode.RMG_016, BizExceptionCode.GLB_002_MSG, e, EMSLogicalNames.SRV_USER.split(","));
        }
        return userManagementService;
    }

	@Override
	public List<Long> getAllPersonIds() throws BaseException {
		return getPersonDAO().getAllPersonIds();
	}
	
	//*********************Anbari : caching the all user permissions	
//		private static String PERMISSION_CACHE_NAME = "cachePermissions";
//	    private NamedCache cachePermissions = CacheFactory.getCache(PERMISSION_CACHE_NAME);	
//	    @Override
//		public void populateUserPermisionCache() throws BaseException {
//
//	    	cachePermissions.clear();
//			List<PersonTO> personList = getPersonDAO().getAll();
//			for (PersonTO person : personList) {
//				List<PermissionVTO> allUserPermission = getGaasService().getAllUserAccess(person.getUserId(), "", 0,Integer.MAX_VALUE);
//				PermissionsWTO wto = new PermissionsWTO();
//				List<String> permissionList = new ArrayList<String>();
//				for (PermissionVTO permission : allUserPermission) {
//					permissionList.add(permission.getName());				
//				}
//				
//				if (EmsUtil.checkListSize(permissionList)) {
//					wto.setName(permissionList);
//					cachePermissions.put(person.getId(), wto);
//				}
//			}
//
//		}
//	    //*******************Anbari : get Permission from cache
//	    @Override
//	    public List<String> getPermissionByUserName(String username) throws BaseException
//	    {
//	    	
//	    	Long personId = getPersonDAO().findPersonIdByUsername(username);
//	    	if(personId != null)
//	    	{
//	    		PermissionsWTO permissionsWTO = (PermissionsWTO) cachePermissions.get(personId);
//	    		if(permissionsWTO != null)
//	    			return permissionsWTO.getName();
//	    	}
//			return new ArrayList<String>();
//	    	
//	    }
//		
}
