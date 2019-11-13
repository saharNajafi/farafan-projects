package com.gam.nocr.ems.biz.delegator;

import java.util.List;
import java.util.Map;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.Delegator;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.SearchResult;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.service.PersonManagementService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.domain.DepartmentTO;
import com.gam.nocr.ems.data.domain.vol.PersonVTO;
import com.gam.nocr.ems.data.enums.ReplicaReason;
import com.gam.nocr.ems.data.enums.TokenType;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class PersonDelegator implements Delegator {

    private PersonManagementService getService(UserProfileTO userProfileTO) throws BaseException {
        PersonManagementService personManagementService;
        try {
            personManagementService = (PersonManagementService) ServiceFactoryProvider.getServiceFactory().getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_PERSON), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(BizExceptionCode.PDL_001, BizExceptionCode.GLB_002_MSG, e, EMSLogicalNames.SRV_PERSON.split(","));
        }
        personManagementService.setUserProfileTO(userProfileTO);
        return personManagementService;
    }

    public void delegate(UserProfileTO userProfileTO, PersonVTO to) throws BaseException {
        boolean result = getService(userProfileTO).delegate(to);

        if (result)
            getService(userProfileTO).update(to);
        else {
            to.setId(null);
            getService(userProfileTO).save(to);
        }
    }

    public Long save(UserProfileTO userProfileTO, PersonVTO to) throws BaseException {
        return getService(userProfileTO).save(to);
    }

//	public Long saveRequestedPerson(UserProfileTO userProfileTO, PersonVTO to) throws BaseException {
//		return getService(userProfileTO).saveRequestedPerson(to);
//	}

//	public Long update(UserProfileTO userProfileTO, PersonVTO to) throws BaseException {
//		return getService(userProfileTO).update(to);
//	}

    public PersonVTO load(UserProfileTO userProfileTO, Long personId) throws BaseException {
        return getService(userProfileTO).load(personId);
    }

    public boolean remove(UserProfileTO userProfileTO, String personIds) throws BaseException {
        return getService(userProfileTO).remove(personIds);
    }

    public void changeStatus(UserProfileTO userProfileTO, Long personId) throws BaseException {
        getService(userProfileTO).changeStatus(personId);
    }

    public void rejectPerson(UserProfileTO userProfileTO, String personIds) throws BaseException {
        getService(userProfileTO).rejectPerson(personIds);
    }

    public String fetchRolePermissionList(UserProfileTO userProfileTO, String roleId) throws BaseException {
        return getService(userProfileTO).fetchRolePermissionList(roleId);
    }

    public Long issueToken(UserProfileTO userProfileTO, Long personId, TokenType type) throws BaseException {
        return getService(userProfileTO).issueToken(personId, type);
    }

    public Long reissueToken(UserProfileTO userProfileTO, Long tokenId) throws BaseException {
        return getService(userProfileTO).reissueToken(tokenId);
    }
    
    //Adldoost
    public Long renewalToken(UserProfileTO userProfileTO, Long tokenId) throws BaseException {
        return getService(userProfileTO).renewalToken(tokenId);
    }
    
    //Adldoost
    public void removeRenewalTokenRequest(UserProfileTO userProfileTO, Long tokenId) throws BaseException {
        getService(userProfileTO).deleteRenewalTokenRequest(tokenId);
    }

    public Long replicateToken(UserProfileTO userProfileTO, Long personId, TokenType tokenType, ReplicaReason reason) throws BaseException {
        return getService(userProfileTO).replicateToken(personId, tokenType, reason);
    }

    public void revokeToken(UserProfileTO userProfileTO, Long tokenId, ReplicaReason reason, String comment) throws BaseException {
        getService(userProfileTO).revokeToken(tokenId, reason, comment);
    }

    public void deliverToken(UserProfileTO userProfileTO, Long tokenId) throws BaseException {
        getService(userProfileTO).deliverToken(tokenId);
    }

    public String informAcceptableTypes(UserProfileTO userProfileTO, Long personId) throws BaseException {
        return getService(userProfileTO).informAcceptableTypes(personId);
    }

    public String checkDeliveredTokens(UserProfileTO userProfileTO, Long personId) throws BaseException {
        return getService(userProfileTO).checkDeliveredTokens(personId);
    }

    public SearchResult fetchRoles(UserProfileTO userProfileTO, String searchString, int from, int to, String orderBy) throws BaseException {
        return getService(userProfileTO).fetchRoles(searchString, from, to, orderBy);
    }

    public SearchResult fetchPermissions(UserProfileTO userProfileTO, String searchString, int from, int to, String orderBy) throws BaseException {
        return getService(userProfileTO).fetchPermissions(searchString, from, to, orderBy);
    }

    public SearchResult fetchPersons(UserProfileTO userProfileTO, String searchString, int from, int to, String orderBy) throws BaseException {
        return getService(userProfileTO).fetchPersons(searchString, from, to, orderBy);
    }

    public DepartmentTO loadDepartmentByPersonId(UserProfileTO userProfileTO, Long personId) throws BaseException {
        return getService(userProfileTO).loadDepartmentByPersonId(personId);
    }

    /**
     * The method deleteToken is used to delete an instance of type {@link com.gam.nocr.ems.data.domain.PersonTokenTO}
     *
     * @param userProfileTO is an instance of type {@link UserProfileTO}
     * @param tokenId       is an instance of {@link Long}
     */
    public void deleteToken(UserProfileTO userProfileTO,
                            Long tokenId) throws BaseException {
        getService(userProfileTO).deleteToken(tokenId);
    }
    public SearchResult fetchPersons(UserProfileTO userProfile,
			String searchString, int from, int to, String orderBy,
			Map additionalParams) throws BaseException  {
		return getService(userProfile).fetchPersonsByDepartmentId(searchString, from, to,
				orderBy,additionalParams);
	}
    
    public List<Long> findListDepartmentsByPersonId(Long personId) throws BaseException
    {
    	return getService(null).findListDepartmentsByPersonId(personId);
    }

	public List<Long> getAllPersonIds() throws BaseException {
		return getService(null).getAllPersonIds();
		
	}
    
    
}
