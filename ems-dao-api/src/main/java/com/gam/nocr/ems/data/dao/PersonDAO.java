package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.domain.SearchResult;
import com.gam.nocr.ems.data.domain.DepartmentTO;
import com.gam.nocr.ems.data.domain.PersonTO;
import com.gam.nocr.ems.data.enums.BooleanType;
import com.gam.nocr.ems.data.enums.PersonRequestStatus;

import java.util.List;

/**
 * <p> TODO -- Explain this class </p>
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public interface PersonDAO extends EmsBaseDAO<PersonTO> {

    public boolean removePersons(String personIds) throws BaseException;

    public boolean changeStatus(PersonTO personTO) throws BaseException;

    public boolean changeStatus(Long personId, BooleanType state) throws BaseException;

    public void updatePersonRequestState(List<Long> personIds, PersonRequestStatus requestStatus) throws BaseException;

    public PersonTO findByNationalId(String nationalId) throws BaseException;
    
    public PersonTO findByPersonId(Long personId) throws BaseException;

//	public PersonTO createPersonsByNationalId(PersonTO personTO);

//	public PersonTO removePersonsByNationalId(PersonTO personTO);

    public Integer findUserIdByPersonId(Long personId) throws BaseException;
    
    public DepartmentTO findDepartmentByPersonId(Long personId) throws BaseException;

    //Anbari
    List<Long> findListDepartmentsByPersonId(Long personId) throws BaseException;

    public Boolean checkIsAdmin(Long personId) throws BaseException;

    /**
     * The method findByUsername is used to find an instance of type {@link PersonTO} via an appropriate username
     *
     * @param userName is an instance of type {@link String}
     * @return an instance of type {@link PersonTO}
     * @throws BaseException
     */
    PersonTO findByUsername(String userName) throws BaseException;
    
    
    
    public List<PersonTO> fetchPersonByDepartmentId(Long departmentId)
			throws BaseException;


	public SearchResult fetchPersonByLocationId(Long locationId)
			throws BaseException;

	public List<PersonTO> getAll() throws BaseException;
	
	//Anbari
	Long findPersonIdByUsername(String username) throws BaseException;
}
