package com.gam.nocr.ems.biz.delegator;

import java.util.Map;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.Delegator;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactory;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.SearchResult;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.service.DepartmentService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.domain.vol.DepartmentVTO;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * @author: Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public class DepartmentDelegator implements Delegator {

    private DepartmentService getService(UserProfileTO userProfileTO) throws BaseException {
        ServiceFactory factory = ServiceFactoryProvider.getServiceFactory();
        DepartmentService departmentService = null;
        try {
            departmentService = (DepartmentService) factory.getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_DEPARTMENT), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(BizExceptionCode.DDL_001, BizExceptionCode.GLB_002_MSG, e, EMSLogicalNames.SRV_DEPARTMENT.split(","));
        }
        departmentService.setUserProfileTO(userProfileTO);
        return departmentService;
    }

    public long save(UserProfileTO userProfileTO, DepartmentVTO to) throws BaseException {
        return getService(userProfileTO).save(to);
    }

    public long update(UserProfileTO userProfileTO, DepartmentVTO to) throws BaseException {
        return getService(userProfileTO).update(to);
    }

    public DepartmentVTO load(UserProfileTO userProfileTO, long departmentId) throws BaseException {
        return getService(userProfileTO).load(departmentId);
    }

    public boolean remove(UserProfileTO userProfileTO, String departmentIds) throws BaseException {
        return getService(userProfileTO).remove(departmentIds);
    }

    public SearchResult fetchDepartments(UserProfileTO userProfileTO, String searchString, int from, int to, String orderBy) throws BaseException {
        return getService(userProfileTO).fetchDepartments(searchString, from, to, orderBy);
    }

    public SearchResult fetchPersonDepartments(UserProfileTO userProfileTO, String searchString, int from, int to, String orderBy) throws BaseException {
        return getService(userProfileTO).fetchPersonDepartments(searchString, from, to, orderBy);
    }

    public SearchResult fetchNonEnrollmentDepartments(UserProfileTO userProfileTO, String searchString, int from, int to, String orderBy) throws BaseException {
        return getService(userProfileTO).fetchNonEnrollmentDepartments(searchString, from, to, orderBy);
    }
    
    public SearchResult fetchProvinces(UserProfileTO userProfileTO, String searchString, int from, int to, String orderBy) throws BaseException {
        return getService(userProfileTO).fetchProvinces(searchString, from, to, orderBy);
    }
    
    public SearchResult fetchAnyKindDepartments(
			UserProfileTO userProfileTO, String searchString, int from, int to,
			String orderBy, Map additionalParams) throws BaseException {
		return getService(userProfileTO).fetchAnyKindDepartments(searchString, from,
				to, orderBy, additionalParams);
	}
}
