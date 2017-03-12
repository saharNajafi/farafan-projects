package com.gam.nocr.ems.biz.service;

import java.util.List;
import java.util.Map;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.domain.SearchResult;
import com.gam.nocr.ems.data.domain.DepartmentTO;
import com.gam.nocr.ems.data.domain.vol.PersonVTO;

/**
 * <p> TODO -- Explain this class </p>
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public interface PersonManagementService extends TokenService {

    public Long save(PersonVTO to) throws BaseException;

    public Long update(PersonVTO to) throws BaseException;

    public PersonVTO load(Long personId) throws BaseException;

    public boolean remove(String personIds) throws BaseException;

    public void changeStatus(Long personId) throws BaseException;

    public void rejectPerson(String personIds) throws BaseException;

    public String fetchRolePermissionList(String roleId) throws BaseException;

    public String informAcceptableTypes(Long personId) throws BaseException;

    public DepartmentTO loadDepartmentByPersonId(Long personId) throws BaseException;

    public SearchResult fetchRoles(String searchString, int from, int to, String orderBy) throws BaseException;

    public SearchResult fetchPermissions(String searchString, int from, int to, String orderBy) throws BaseException;

    public SearchResult fetchPersons(String searchString, int from, int to, String orderBy) throws BaseException;

    public boolean delegate(PersonVTO to) throws BaseException;

	public SearchResult fetchPersonsByDepartmentId(String searchString,
			int from, int to, String orderBy, Map additionalParams)  throws BaseException;
	
	public List<Long> findListDepartmentsByPersonId(Long personId) throws BaseException;;
	
	// Anbari
	public Long findPersonIdByUsername(String username) throws BaseException;

	//Anbari
	public List<Long> getAllPersonIds() throws BaseException;

	// Anbari
	//public void populateUserPermisionCache() throws BaseException;

	// Anbari
	//public List<String> getPermissionByUserName(String userName) throws BaseException;
}