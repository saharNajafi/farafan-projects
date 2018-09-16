package com.gam.nocr.ems.data.dao.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.commons.core.data.domain.SearchResult;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.config.EMSValueListProvider;
import com.gam.nocr.ems.data.domain.DepartmentTO;
import com.gam.nocr.ems.data.domain.PersonTO;
import com.gam.nocr.ems.data.enums.BooleanType;
import com.gam.nocr.ems.data.enums.PersonRequestStatus;
import com.gam.nocr.ems.util.EmsUtil;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import gampooya.tools.vlp.ListException;
import gampooya.tools.vlp.ValueListHandler;
import org.displaytag.exception.ListHandlerException;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p> TODO -- Explain this class </p>
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
@Stateless(name = "PersonDAO")
@Local(PersonDAOLocal.class)
@Remote(PersonDAORemote.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PersonDAOImpl extends EmsBaseDAOImpl<PersonTO> implements PersonDAOLocal, PersonDAORemote {

    private static final String UNIQUE_KEY_PER_NATIONAL_ID = "AK_PER_NATIONAL_ID";
    private static final String UNIQUE_KEY_PER_USER_ID = "AK_PER_USER_ID";
    private static final String UNIQUE_KEY_PER_USER_NAME = "AK_PER_USER_NAME";
    private static final String FOREIGN_KEY_PER_USER_NAME = "FK_PER_TKN_PERSON_ID";
    private static final String FOREIGN_KEY_PERSON_PROFILE_PER_ID = "FK_PERSON_PROFILE_PER_ID";
    private static final String FOREIGN_KEY_ENROLL_OFC_PER_ID = "FK_ENROLL_OFC_PER_ID";

    @Override
    @PersistenceContext(unitName = "EmsOraclePU")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    /**
     * The create method, handles all the save operations for all the classes which are extended from EntityTO
     *
     * @param personTO - the object of type EntityTO to create
     * @return the object of type EntityTo
     */
    @Override
    public PersonTO create(PersonTO personTO) throws BaseException {
        try {
            PersonTO to = super.create(personTO);
            em.flush();
            return to;
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains(UNIQUE_KEY_PER_NATIONAL_ID))
                throw new DAOException(DataExceptionCode.PDI_002, DataExceptionCode.PDI_002_MSG, e);
            if (err.contains(UNIQUE_KEY_PER_USER_ID))
                throw new DAOException(DataExceptionCode.PDI_003, DataExceptionCode.PDI_003_MSG, e);
            if (err.contains(UNIQUE_KEY_PER_USER_NAME))
                throw new DAOException(DataExceptionCode.PDI_004, DataExceptionCode.PDI_004_MSG, e);
            if (err.contains(FOREIGN_KEY_ENROLL_OFC_PER_ID))
                throw new DAOException(DataExceptionCode.PDI_025, DataExceptionCode.PDI_025_MSG, e);
            else
                throw new DAOException(DataExceptionCode.PDI_005, DataExceptionCode.PDI_005_MSG, e);
        }
    }

    /**
     * The Update method, handles the update operations for all the classes which are extended from EntityTO.
     *
     * @param personTO - the object of type EntityTO to create
     * @return the object which of type EntityTO, or null if the object is not found
     */
    @Override
    public PersonTO update(PersonTO personTO) throws BaseException {
        try {
//			try{
//				utx.begin();
//			}catch (Exception ex){}
            PersonTO to = super.update(personTO);
            em.flush();
            return to;
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains(UNIQUE_KEY_PER_NATIONAL_ID))
                throw new DAOException(DataExceptionCode.PDI_021, DataExceptionCode.PDI_002_MSG, e);
            if (err.contains(UNIQUE_KEY_PER_USER_ID))
                throw new DAOException(DataExceptionCode.PDI_022, DataExceptionCode.PDI_003_MSG, e);
            if (err.contains(UNIQUE_KEY_PER_USER_NAME))
                throw new DAOException(DataExceptionCode.PDI_023, DataExceptionCode.PDI_004_MSG, e);
            if (err.contains(FOREIGN_KEY_ENROLL_OFC_PER_ID))
                throw new DAOException(DataExceptionCode.PDI_026, DataExceptionCode.PDI_025_MSG, e);
            else
                throw new DAOException(DataExceptionCode.PDI_024, DataExceptionCode.PDI_005_MSG, e);
        }
    }

    /**
     * The find method, handles the find operation on the classes which are extended from EntityTO.
     *
     * @param type - the class type of an instance of the class
     * @param id   - the id number of an instance of the class
     * @return the object which of type EntityTO, or null if the object is not found
     */
    @Override
    public PersonTO find(Class type, Object id) throws BaseException {
        PersonTO person = null;
        try {
            person = super.find(type, id);
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.PDI_009, DataExceptionCode.GLB_003_MSG, e);
        }
        if (person != null)
            return person;
        else
            throw new DAOException(DataExceptionCode.PDI_007, DataExceptionCode.PDI_007_MSG);
    }

    /**
     * The Delete method, handles the delete operation for all the classes which are extended from EntityTO.
     *
     * @param personTO - the object of type EntityTO to create
     */
    @Override
    public void delete(PersonTO personTO) throws BaseException {
        try {
            super.delete(personTO);
            em.flush();
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains(FOREIGN_KEY_PER_USER_NAME))
                throw new DAOException(DataExceptionCode.PDI_006, DataExceptionCode.PDI_006_MSG, e);
            if (err.contains(FOREIGN_KEY_PERSON_PROFILE_PER_ID))
                throw new DAOException(DataExceptionCode.PDI_010, DataExceptionCode.PDI_010_MSG, e);
            if (err.contains(FOREIGN_KEY_ENROLL_OFC_PER_ID))
                throw new DAOException(DataExceptionCode.PDI_017, DataExceptionCode.PDI_017_MSG, e);
            else
                throw new DAOException(DataExceptionCode.PDI_008, DataExceptionCode.GLB_007_MSG, e);
        }
    }

    @Override
    public boolean removePersons(String personIds) throws BaseException {
        int personToDeleteCount = personIds.split(",").length;

        List<Long> ids = new ArrayList<Long>();
        for (String id : personIds.split(","))
            ids.add(Long.parseLong(id.trim()));

        int personDeletedCount = 0;
        try {
            personDeletedCount = em.createQuery(" DELETE FROM PersonTO EP " +
                    " where EP.id in (:ids) ")
                    .setParameter("ids", ids)
                    .executeUpdate();
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains(FOREIGN_KEY_PER_USER_NAME))
                throw new DAOException(DataExceptionCode.PDI_011, DataExceptionCode.PDI_006_MSG, e);
            if (err.contains(FOREIGN_KEY_PERSON_PROFILE_PER_ID))
                throw new DAOException(DataExceptionCode.PDI_012, DataExceptionCode.PDI_010_MSG, e);
            if (err.contains(FOREIGN_KEY_ENROLL_OFC_PER_ID))
                throw new DAOException(DataExceptionCode.PDI_018, DataExceptionCode.PDI_017_MSG, e);
            else
                throw new DAOException(DataExceptionCode.PDI_013, DataExceptionCode.GLB_007_MSG, e);
        }
        return personToDeleteCount == personDeletedCount;
    }


    @Override
    public boolean changeStatus(PersonTO personTO) throws BaseException {
        if (personTO.getStatus().equals(BooleanType.T)) {
            personTO.setStatus(BooleanType.F);
            PersonTO mergedTO = em.merge(personTO);
            if (mergedTO.getStatus().equals(BooleanType.F))
                return true;
            else
                throw new DAOException(DataExceptionCode.PDI_001, DataExceptionCode.PDI_001_MSG);
        } else if (personTO.getStatus().equals(BooleanType.F)) {
            personTO.setStatus(BooleanType.T);
            PersonTO mergedTO = em.merge(personTO);
            if (mergedTO.getStatus().equals(BooleanType.T))
                return true;
            else
                throw new DAOException(DataExceptionCode.PDI_001, DataExceptionCode.PDI_001_MSG);
        }

        return false;
    }

    @Override
    public boolean changeStatus(Long personId, BooleanType state) throws BaseException {
        try {
            em.createQuery(" update PersonTO pt " +
                    " set pt.status = :state " +
                    " where pt.id = :personId ")
                    .setParameter("state", state)
                    .setParameter("personId", personId)
                    .executeUpdate();
            em.flush();
            return true;
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.PDI_016, DataExceptionCode.GLB_006_MSG, e);
        }
    }

    @Override
    public void updatePersonRequestState(List<Long> personIds, PersonRequestStatus requestStatus) throws BaseException {
        try {
            em.createQuery(" update PersonTO pt " +
                    " set pt.requestStatus = :requestStatus " +
                    " where pt.id in (:personIds) ")
                    .setParameter("requestStatus", requestStatus)
                    .setParameter("personIds", personIds)
                    .executeUpdate();
            em.flush();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.PDI_020, DataExceptionCode.GLB_006_MSG, e);
        }
    }

    @Override
    public PersonTO findByNationalId(String nationalId) throws BaseException {
        List<PersonTO> personTOList;
        try {
            personTOList = em.createQuery(" select pt from PersonTO pt " +
                    " where pt.nid = :nid ", PersonTO.class)
                    .setParameter("nid", nationalId)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.PDI_014, DataExceptionCode.GLB_005_MSG, e);
        }
        if (personTOList.size() != 0)
            return personTOList.get(0);
        else
            return null;
    }

    @Override
    public Integer findUserIdByPersonId(Long personId) throws BaseException {
        List<Integer> userIdList;
        try {
            userIdList = em.createQuery(" select ep.userId from PersonTO ep " +
                    " where ep.id = :personId ", Integer.class)
                    .setParameter("personId", personId)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.PDI_015, DataExceptionCode.GLB_005_MSG, e);
        }

        if (userIdList.size() != 0)
            return userIdList.get(0);
        else
            return null;
    }

    @Override
    public DepartmentTO findDepartmentByPersonId(Long personId) throws BaseException {
        List<DepartmentTO> departmentTOList;
        try {
            departmentTOList = em.createQuery(" select ep.department from PersonTO ep " +
                    " where ep.id = :personId ", DepartmentTO.class)
                    .setParameter("personId", personId)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.PDI_019, DataExceptionCode.GLB_005_MSG, e);
        }

        if (departmentTOList != null && departmentTOList.size() != 0)
            return departmentTOList.get(0);
        else
            return null;
    }
    
    @Override
    public List<Long> findListDepartmentsByPersonId(Long personId) throws BaseException {
    	List<Long> resultList = new ArrayList<Long>();
        try {
        	StringBuffer queryBuffer = new StringBuffer();

    		queryBuffer.append(
    				"select dp.dep_id from emst_department dp connect by prior dp.dep_id=dp.dep_parent_dep_id start with dp.dep_id"
    				+ " in (select pr.per_dep_id from emst_person pr where pr.per_id=:perid union select e.eof_id from emst_enrollment_office e connect by prior e.eof_id=e.eof_superior_office start with e.eof_id"
    				+ " in (select p.per_dep_id from emst_person p where p.per_id=:perid ))");
    		Query query = em.createNativeQuery(queryBuffer.toString()).setParameter("perid", personId);
    		List<?> result = query.getResultList();
    		if (result != null){
    			for (Object object : result) {
					resultList.add(((BigDecimal)object).longValue());
				}
    		}
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.PDI_038, DataExceptionCode.GLB_005_MSG, e);
        }

        return resultList;
    }

    @Override
    public Boolean checkIsAdmin(Long personId) throws BaseException {
        List<PersonTO> personTOList;
        try {
            personTOList = em.createQuery(" select ep from PersonTO ep, EnrollmentOfficeTO eo " +
                    " where ep.id = eo.manager.id " +
                    " and ep.id = :personId", PersonTO.class)
                    .setParameter("personId", personId)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.PDI_027, DataExceptionCode.GLB_005_MSG, e);
        }

        return EmsUtil.checkListSize(personTOList);
    }

    /**
     * The method findByUsername is used to find an instance of type {@link com.gam.nocr.ems.data.domain.PersonTO} via an appropriate username
     *
     * @param username is an instance of type {@link String}
     * @return an instance of type {@link com.gam.nocr.ems.data.domain.PersonTO}
     * @throws com.gam.commons.core.BaseException
     */
    @Override
    public PersonTO findByUsername(String username) throws BaseException {
        try {
            List<PersonTO> personTOList = em.createQuery(" " +
                    "SELECT PRS FROM PersonTO PRS " +
                    "WHERE PRS.userName = :USERNAME ", PersonTO.class)
                    .setParameter("USERNAME", username)
                    .getResultList();
            if (EmsUtil.checkListSize(personTOList)) {
                return personTOList.get(0);
            }
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.PDI_031, DataExceptionCode.GLB_005_MSG, e);
        }

        return null;
    }

	@Override
	public List<PersonTO> fetchPersonByDepartmentId(Long departmentId)
			throws BaseException {

		List<PersonTO> result = null;
		try {
			result = em
					.createQuery(
							"from PersonTO pe where pe.department.id =:departmentId",
							PersonTO.class)
					.setParameter("departmentId", departmentId).getResultList();

		} catch (Exception e) {
			throw new DAOException(DataExceptionCode.PDI_032,
					DataExceptionCode.GLB_005_MSG, e);
		}

		return result;
	}

	@Override
	public SearchResult fetchPersonByLocationId(Long locationId)
			throws BaseException {
		HashMap param = new HashMap();
		param.put("perName", "%" + "" + "%");
		param.put("locationId", locationId);
		try {
			ValueListHandler vlh = EMSValueListProvider.getProvider().loadList(
					"personByLocationIdAC", "main".split(","),
					"count".split(","), param, null, null);
			List list = vlh.getList(true);
			return new SearchResult(vlh.size(), list);
		} catch (ListException e) {
			throw new DAOException(DataExceptionCode.PDI_033,
					DataExceptionCode.GLB_005_MSG, e);
		} catch (ListHandlerException e) {
			throw new DAOException(DataExceptionCode.PDI_034,
					DataExceptionCode.GLB_005_MSG, e);
		} catch (Exception e) {
			throw new DAOException(DataExceptionCode.PDI_035,
					DataExceptionCode.GLB_005_MSG, e);
		}
	}
	
	@Override
	public List<PersonTO> getAll() throws BaseException {
		List<PersonTO> result = null;
		try {
			result = em
					.createQuery(
							"from PersonTO pe",
							PersonTO.class).getResultList();

		} catch (Exception e) {
			throw new DAOException(DataExceptionCode.PDI_036,
					DataExceptionCode.GLB_005_MSG, e);
		}

		return result;
	}
	
	
	@Override
	public List<Long> getAllPersonIds() throws BaseException {
		List<Long> ids = null;
		try {
			ids = em.createQuery("select pe.id from PersonTO pe",Long.class).getResultList();
			if(EmsUtil.checkListSize(ids))
				return ids;		

		} catch (Exception e) {
			throw new DAOException(DataExceptionCode.PDI_040,
					DataExceptionCode.GLB_005_MSG, e);
		}
		return ids;

	}

	@Override
	public PersonTO findByPersonId(Long personId) throws BaseException {
		List<PersonTO> personTOList;
        try {
            personTOList = em.createQuery(" select pt from PersonTO pt " +
                    " where pt.id = :pid ", PersonTO.class)
                    .setParameter("pid", personId)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.PDI_037, DataExceptionCode.GLB_005_MSG, e);
        }
        if (personTOList.size() != 0)
            return personTOList.get(0);
        else
            return null;
	}
	
	
	//****************** Anbari : caching the personID
		private static String PERSONID_CACHE_NAME = "cachePersonID";
	    private NamedCache cachePersonID = CacheFactory.getCache(PERSONID_CACHE_NAME);	
		@Override
	    public Long findPersonIdByUsername(String username) throws BaseException {
			Long personID = null;
	        try {
	        	
	        	if(EmsUtil.checkString(username))
	        	{
	        		 personID = (Long) cachePersonID.get(username);
	        		 if(personID == null)
	        		 {
	        			 List<Long> personTOList = em.createQuery(" " +
	        	                    "SELECT PRS.id FROM PersonTO PRS " +
	        	                    "WHERE PRS.userName = :USERNAME ", Long.class)
	        	                    .setParameter("USERNAME", username)
	        	                    .getResultList();
	        	            if (EmsUtil.checkListSize(personTOList)) {
	        	                personID = personTOList.get(0);
	        	                cachePersonID.put(username, personID);
	        	            }        			 
	        		 }        		 
	        		
	        	}
	        	
	            
	        } catch (Exception e) {
	            throw new DAOException(DataExceptionCode.PDI_039, DataExceptionCode.GLB_005_MSG, e);
	        }

	        return personID;
	    }
		//**********************

}
