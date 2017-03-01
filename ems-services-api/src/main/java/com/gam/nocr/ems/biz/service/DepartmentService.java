package com.gam.nocr.ems.biz.service;

import java.util.Map;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.commons.core.data.domain.SearchResult;
import com.gam.nocr.ems.data.domain.vol.DepartmentVTO;

/**
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 * @author: Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public interface DepartmentService extends Service {

    public Long save(DepartmentVTO to) throws BaseException;

    public Long update(DepartmentVTO to) throws BaseException;

    public DepartmentVTO load(Long departmentId) throws BaseException;

    public boolean remove(String departmentIds) throws BaseException;

    public SearchResult fetchDepartments(String searchString, int from, int to, String orderBy) throws BaseException;
    
    public SearchResult fetchNOCR(String searchString, int from, int to, String orderBy) throws BaseException;

    public SearchResult fetchPersonDepartments(String searchString, int from, int to, String orderBy) throws BaseException;

    public SearchResult fetchNonEnrollmentDepartments(String searchString, int from, int to, String orderBy) throws BaseException;
    
    public SearchResult fetchProvinces(String searchString, int from, int to, String orderBy) throws BaseException;

	public SearchResult fetchAnyKindDepartments(String searchString, int from, int to,
			String orderBy, Map additionalParams) throws BaseException;
}
