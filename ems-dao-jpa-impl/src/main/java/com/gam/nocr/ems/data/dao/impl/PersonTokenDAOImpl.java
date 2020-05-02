package com.gam.nocr.ems.data.dao.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.data.DataException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.PersonTO;
import com.gam.nocr.ems.data.domain.PersonTokenTO;
import com.gam.nocr.ems.data.enums.TokenState;
import com.gam.nocr.ems.data.enums.TokenType;
import com.gam.nocr.ems.util.EmsUtil;
import org.slf4j.Logger;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */

@Stateless(name = "PersonTokenDAO")
@Local(PersonTokenDAOLocal.class)
@Remote(PersonTokenDAORemote.class)
public class PersonTokenDAOImpl extends EmsBaseDAOImpl<PersonTokenTO> implements PersonTokenDAOLocal, PersonTokenDAORemote {

    private static final Logger logger = BaseLog.getLogger(PersonTokenDAOImpl.class);

    private boolean checkList(List<PersonTokenTO> personTokenTOList) {
        boolean returnValue = false;
        if (personTokenTOList != null && personTokenTOList.size() > 0) {
            returnValue = true;
        }
        return returnValue;
    }

    @Override
    @PersistenceContext(unitName = "EmsOraclePU")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Override
    public PersonTokenTO create(PersonTokenTO personTokenTO) throws BaseException {
        PersonTokenTO pto = null;
        try {
            pto = super.create(personTokenTO);
            em.flush();
            return pto;
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains("AK_PER_TKN_PER_STATE_TYPE"))
                throw new DAOException(DataExceptionCode.PTI_011, DataExceptionCode.PTI_011_MSG, e);
            else
                throw new DataException(DataExceptionCode.PTI_001, DataExceptionCode.GLB_004_MSG, e);
        }
    }

    @Override
    public PersonTokenTO find(Class type, Object id) throws BaseException {
        PersonTokenTO pto = null;
        try {
            pto = super.find(type, id);
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.PTI_002, DataExceptionCode.GLB_005_MSG, e);
        }

        return pto;
    }

    @Override
    public PersonTokenTO update(PersonTokenTO personTokenTO) throws BaseException {
        PersonTokenTO pto = null;
        try {
            pto = super.update(personTokenTO);
            em.flush();
            return pto;
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains("AK_PER_TKN_PER_STATE_TYPE"))
                throw new DAOException(DataExceptionCode.PTI_014, DataExceptionCode.PTI_011_MSG, e);
            else
                throw new DataException(DataExceptionCode.PTI_003, DataExceptionCode.GLB_006_MSG, e);
        }
    }

    @Override
    public void delete(PersonTokenTO personTokenTO) throws BaseException {
        try {
            super.delete(personTokenTO);
            em.flush();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.PTI_004, DataExceptionCode.GLB_007_MSG, e);
        }
    }

    /**
     * The method findByState is used to find a list of type {@link com.gam.nocr.ems.data.domain.PersonTokenTO} with a
     * specified tokenState.
     *
     * @param tokenState is an enumeration of type {@link com.gam.nocr.ems.data.enums.TokenState}
     * @return a list of type {@link com.gam.nocr.ems.data.domain.PersonTokenTO} or null if there is nothing to be found
     */
    @Override
    public List<PersonTokenTO> findByState(TokenState tokenState) throws BaseException {
        List<PersonTokenTO> personTokenTOList = null;
        try {
            personTokenTOList = em.createQuery("" +
                    "SELECT PTT FROM PersonTokenTO PTT " +
                    "WHERE PTT.state = :token_state", PersonTokenTO.class)
                    .setParameter("token_state", tokenState)
                    .getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.PTI_009, DataExceptionCode.GLB_005_MSG, e);
        }
        return personTokenTOList;

    }
    
    //Adldoost
    @Override
    public List<PersonTokenTO> findByState(List<TokenState> tokenStateList) throws BaseException {
    	
    	List<PersonTokenTO> personTokenTOList = null;
        try {
            personTokenTOList = em.createQuery("" +
                    "SELECT PTT FROM PersonTokenTO PTT " +
                    "WHERE PTT.state in (:token_state)", PersonTokenTO.class)
                    .setParameter("token_state", tokenStateList)
                    .getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.PTI_022, DataExceptionCode.GLB_005_MSG, e);
        }
        return personTokenTOList;
    }
    
    //Adldoost
    @Override
    public void approveRewnewalRequest(String ids) throws BaseException
    {
    	if(!EmsUtil.checkString(ids))
    		throw new BaseException(DataExceptionCode.PTI_023, DataExceptionCode.PTI_023_MSG);
    	try
    	{
	    	String[] sids = ids.split(","); 
	    	List<Long> lids = new ArrayList<Long>();
	    	for(String id : sids)
	    	{
	    		Long lid = Long.parseLong(id);
	    		lids.add(lid);
	    		PersonTokenTO token = findById(lid);
	    		if(token.getState() == TokenState.PENDING_FOR_EMS || token.getState() == TokenState.EMS_REJECT)
	    		{
	    			token.setState(TokenState.READY_TO_RENEWAL_ISSUE);
	    			create(token);
	    		}
	    		else
	    		{
	    			throw new BaseException(DataExceptionCode.PTI_024, DataExceptionCode.PTI_024_MSG, null, new Object[]{token.getState()});
	    		}
	    	}
    	}
    	catch(Exception e)
    	{
    		throw new BaseException(DataExceptionCode.PTI_025, DataExceptionCode.GLB_003_MSG, e);
    	}

    }
    
    //Adldoost
    @Override
    public void rejectRewnewalRequest(String ids) throws BaseException
    {
    	if(!EmsUtil.checkString(ids))
    		throw new BaseException(DataExceptionCode.PTI_026, DataExceptionCode.PTI_023_MSG);
    	try
    	{
	    	String[] sids = ids.split(","); 
	    	List<Long> lids = new ArrayList<Long>();
	    	for(String id : sids)
	    	{
	    		Long lid = Long.parseLong(id);
	    		lids.add(lid);
	    		PersonTokenTO token = findById(lid);
	    		if(token.getState() == TokenState.PENDING_FOR_EMS)
	    		{
	    			token.setState(TokenState.EMS_REJECT);
	    			create(token);
	    		}
	    		else
	    		{
	    			throw new BaseException(DataExceptionCode.PTI_027, DataExceptionCode.PTI_027_MSG, null, new Object[]{token.getState()});
	    		}
	    	}
    	}
    	catch(Exception e)
    	{
    		throw new BaseException(DataExceptionCode.PTI_028, DataExceptionCode.GLB_003_MSG, e);
    	}
    }
    
    //Adldoost
    @Override
	public void deliverRewnewalRequest(String ids) throws BaseException {
		if (!EmsUtil.checkString(ids))
			throw new BaseException(DataExceptionCode.PTI_029,
					DataExceptionCode.PTI_023_MSG);
		try {
			// String[] sids = ids.split(",");
			// List<Long> lids = new ArrayList<Long>();
			// for(String id : sids)
			// {
			Long lid = Long.parseLong(ids);
			// lids.add(lid);
			PersonTokenTO token = findById(lid);
			if (token.getState() == TokenState.READY_TO_RENEWAL_DELIVER) {
				token.setState(TokenState.SUSPENDED);
				token.setDeliverDate(new Date());
				create(token);
			} else {
				throw new BaseException(DataExceptionCode.PTI_030,
						DataExceptionCode.PTI_027_MSG, null,
						new Object[] { token.getState() });
			}
			// }
		} catch (Exception e) {
			throw new BaseException(DataExceptionCode.PTI_031,
					DataExceptionCode.GLB_003_MSG, e);
		}
	}
    
    //Adldoost
    @Override
    public void activateRenewalRequest(String ids) throws BaseException
    {
    	if(!EmsUtil.checkString(ids))
    		throw new BaseException(DataExceptionCode.PTI_035, DataExceptionCode.PTI_023_MSG);
    	try
    	{
	    	String[] sids = ids.split(","); 
	    	List<Long> lids = new ArrayList<Long>();
	    	for(String id : sids)
	    	{
	    		Long lid = Long.parseLong(id);
	    		lids.add(lid);
	    		PersonTokenTO token = findById(lid);
	    		if(token.getState() == TokenState.SUSPENDED)
	    		{
	    			token.setState(TokenState.DELIVERED);
                    token.setDeliverDate(new Date());
	    			create(token);
	    		}
	    		else
	    		{
	    			throw new BaseException(DataExceptionCode.PTI_036, DataExceptionCode.PTI_027_MSG, null, new Object[]{token.getState()});
	    		}
	    	}
    	}
    	catch(Exception e)
    	{
    		throw new BaseException(DataExceptionCode.PTI_037, DataExceptionCode.GLB_003_MSG, e);
    	}
    }

    @Override
    public List<PersonTokenTO> findByState(TokenState tokenState, Integer from, Integer to) throws BaseException {
        List<PersonTokenTO> personTokenTOList = null;
        try {
            personTokenTOList = em.createQuery("" +
                    "SELECT PTT FROM PersonTokenTO PTT " +
                    "WHERE PTT.state = :token_state", PersonTokenTO.class)
                    .setParameter("token_state", tokenState)
                    .setFirstResult(from)
                    .setMaxResults(to)
                    .getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.PTI_019, DataExceptionCode.GLB_005_MSG, e);
        }
        return personTokenTOList;

    }

    @Override
    public Long findCountByState(TokenState tokenState, Integer from, Integer to) throws BaseException {
        List<Long> countList;
        try {
            countList = em.createQuery("" +
                    "SELECT COUNT(PTT.id) FROM PersonTokenTO PTT " +
                    "WHERE PTT.state = :token_state", Long.class)
                    .setParameter("token_state", tokenState)
                    .getResultList();
            if (EmsUtil.checkListSize(countList)) {
                return countList.get(0);
            }
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.PTI_018, DataExceptionCode.GLB_005_MSG, e);
        }
        return null;
    }

    @Override
    public Long getCountByState(TokenState tokenState) throws BaseException {
        List<Long> personTokenCountList;
        try {
            personTokenCountList = em.createQuery("" +
                    "SELECT COUNT(PTT.id) FROM PersonTokenTO PTT " +
                    "WHERE PTT.state = :token_state", Long.class)
                    .setParameter("token_state", tokenState)
                    .getResultList();
            if (EmsUtil.checkListSize(personTokenCountList)) {
                return personTokenCountList.get(0);
            }
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.PTI_009, DataExceptionCode.GLB_005_MSG, e);
        }
        return null;
    }
    
    @Override
    public List<Long> getIdsByState(TokenState tokenState) throws BaseException {
        List<Long> personTokenIdsList;
        try {
        	personTokenIdsList = em.createQuery("" +
                    "SELECT PTT.id FROM PersonTokenTO PTT " +
                    "WHERE PTT.state = :token_state", Long.class)
                    .setParameter("token_state", tokenState)
                    .getResultList();
        	return personTokenIdsList;
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.PTI_009, DataExceptionCode.GLB_005_MSG, e);
        }
    }

    @Override
    public PersonTokenTO findByPersonIdAndType(PersonTO personTO, TokenType tokenType) throws BaseException {
        List<PersonTokenTO> personTokenTOs = null;
        try {
            personTokenTOs = em.createQuery("select pt from PersonTokenTO pt" +
                    " where pt.person.id = :personId " +
                    " and pt.type = :tokenType ", PersonTokenTO.class)
                    .setParameter("personId", personTO.getId())
                    .setParameter("tokenType", tokenType)
                    .getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.PTI_012, DataExceptionCode.GLB_005_MSG, e);
        }

        if (checkList(personTokenTOs))
            return personTokenTOs.get(0);
        else
            return null;
    }

    /**
     * The method findDeliveredByPersonIdAndType is used to find an object of type {@link
     * com.gam.nocr.ems.data.domain.PersonTokenTO} in spite of personId and tokenType.
     *
     * @param personId  an object of type {@link com.gam.nocr.ems.data.domain.PersonTO}
     * @param tokenType an enum type of {@link com.gam.nocr.ems.data.enums.TokenType}
     * @return an instance of type ${@link com.gam.nocr.ems.data.domain.PersonTokenTO} or null if there is no ${@link
     * com.gam.nocr.ems.data.domain.PersonTokenTO} which matches to input parameter
     */
    @Override
    public PersonTokenTO findDeliveredByPersonIdAndType(Long personId,
                                                        TokenType tokenType) throws BaseException {
        List<PersonTokenTO> personTokenTOList = null;
        try {
            personTokenTOList = em.createQuery("SELECT PTT FROM PersonTokenTO PTT WHERE " +
                    "PTT.person.id = :personId AND " +
                    "PTT.type = :token_type AND " +
                    "PTT.state = :token_state", PersonTokenTO.class)
                    .setParameter("personId", personId)
                    .setParameter("token_type", tokenType)
                    .setParameter("token_state", TokenState.DELIVERED).getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.PTI_008, DataExceptionCode.GLB_005_MSG, e);
        }
        if (checkList(personTokenTOList)) {
        	if(personTokenTOList.size() == 1)
        		return personTokenTOList.get(0);
        	else
        		throw new DAOException(DataExceptionCode.PTI_020, DataExceptionCode.PTI_020_MSG);
        } else {
            return null;
        }

    }

    //Madanipour
    @Override
    public PersonTokenTO findByPersonIdAndTypeWhereNotRevoked(Long personId, TokenType tokenType) throws BaseException {
        List<PersonTokenTO> personTokenTOList;
		try {
			// commented by Madanipour
			// personTokenTOList =
			// em.createQuery("select pt from PersonTokenTO pt" +
			// " where pt.person.id = :personId " +
			// " and pt.type = :tokenType " +
			// " and pt.state ='" + TokenState.REVOKED + "'",
			// PersonTokenTO.class)
			// .setParameter("personId", personId)
			// .setParameter("tokenType", tokenType)
			// .getResultList();
			personTokenTOList = em
					.createQuery(
							"select pt from PersonTokenTO pt"
									+ " where pt.person.id = :personId "
									+ " and pt.type = :tokenType ",
							PersonTokenTO.class)
					.setParameter("personId", personId)
					.setParameter("tokenType", tokenType).getResultList();
		} catch (Exception e) {
			throw new DataException(DataExceptionCode.PTI_010,
					DataExceptionCode.GLB_005_MSG, e);
		}

		if (checkList(personTokenTOList))
			return personTokenTOList.get(0);
		else
			return null;
    }

    @Override
    public List<PersonTokenTO> findNotRevokedByPersonId(Long personId) throws BaseException {
        try {
            return em.createQuery(" select distinct pt from PersonTokenTO pt " +
                    " where pt.person.id = :personId " +
                    " and pt.state <> :state ", PersonTokenTO.class)
                    .setParameter("personId", personId)
                    .setParameter("state", TokenState.REVOKED)
                    .getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.PTI_015, DataExceptionCode.GLB_005_MSG, e);
        }
    }

    @Override
    public List<PersonTokenTO> findNotRevokedOrDeliveredByPersonIdAndType(Long personId, TokenType tokenType) throws BaseException {
        try {
            return em.createQuery(" select distinct pt from PersonTokenTO pt " +
                    " where pt.person.id = :personId " +
                    " and pt.type = :tokenType " +
                    " and (pt.state <> '" + TokenState.DELIVERED + "'" +
                    " and pt.state <> '" + TokenState.REVOKED + "' )", PersonTokenTO.class)
                    .setParameter("personId", personId)
                    .setParameter("tokenType", tokenType)
                    .getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.PTI_016, DataExceptionCode.GLB_005_MSG, e);
        }
    }

    @Override
    public List<PersonTokenTO> findByPersonId(Long personId) throws BaseException {
        try {
            return em.createQuery(" select pt from PersonTokenTO pt " +
                    " where pt.person.id = :personId", PersonTokenTO.class)
                    .setParameter("personId", personId)
                    .getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.PTI_017, DataExceptionCode.GLB_005_MSG, e);
        }
    }
    
    @Override
    public PersonTokenTO findById(Long personTokenId) throws BaseException {
        try {
            return em.createQuery(" select pt from PersonTokenTO pt " +
                    " where pt.id = :personTokenId", PersonTokenTO.class)
                    .setParameter("personTokenId", personTokenId)
                    .getSingleResult();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.PTI_017, DataExceptionCode.GLB_005_MSG, e);
        }
    }

	@Override
	public PersonTokenTO findByPersonIdAndRequestId(Long personId,
			String requestId) throws BaseException {
		 try {
	            return em.createQuery(" select pt from PersonTokenTO pt " +
	                    " where pt.requestID = :requestId and pt.person.id = :personId", PersonTokenTO.class)
	                    .setParameter("requestId", requestId)
	                    .setParameter("personId", personId)
	                    .getSingleResult();
	        } catch (Exception e) {
	            throw new DataException(DataExceptionCode.PTI_017, DataExceptionCode.GLB_005_MSG, e);
	        }
	}

	@Override
	public List<PersonTokenTO> findByPersonIdAndTypeAndState(Long personId,
			TokenType type, List<TokenState> state) throws BaseException {
		try {
            return em.createQuery(" select pt from PersonTokenTO pt " +
                    " where pt.person.id = :personId and pt.type = :type and pt.state in (:state)", PersonTokenTO.class)
                    .setParameter("personId", personId)
                    .setParameter("type", type)
                    .setParameter("state", state)
                    .getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.PTI_021, DataExceptionCode.GLB_005_MSG, e);
        }
	}
	
	
}
