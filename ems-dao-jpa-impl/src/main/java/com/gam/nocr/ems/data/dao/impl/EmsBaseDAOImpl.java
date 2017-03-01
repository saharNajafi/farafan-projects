package com.gam.nocr.ems.data.dao.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.data.dao.EmsBaseDAO;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

/**
 * The EmsBaseDAOImpl class implements the behaviours which are
 * defined in BaseDAO interface, and all the other DAO classes
 * use it for performing crud operations.
 *
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 * @author <a href="mailto:saadat@gamelectronics.com.com">Alireza Saadat</a>
 */

@Stateless(name = "EmsBaseDAO")
@Local(EmsBaseDAO.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class EmsBaseDAOImpl<T extends ExtEntityTO> implements EmsBaseDAO<T> {

    protected EntityManager em;

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public EntityManager getEm() {
        return em;
    }

    /**
     * The create method, handles all the save operations for all the
     * classes which are extended from EntityTO
     *
     * @param t - the object of type EntityTO to create
     * @return the object of type EntityTo
     */
    public T create(T t) throws BaseException {
        if (t.getId() == null)
            em.persist(t);
        else
            em.merge(t);

        em.flush();
        return t;
    }

    /**
     * The find method, handles the find operation on the classes
     * which are extended from EntityTO.
     *
     * @param type - the class type of an instance of the class
     * @param id   - the id number of an instance of the class
     * @return the object which of type EntityTO, or null if the object is not found
     */
    public T find(Class type, Object id) throws BaseException {
        return (T) em.find(type, id);
    }

    /**
     * The Update method, handles the update operations for all the classes
     * which are extended from EntityTO.
     *
     * @param t - the object of type EntityTO to create
     * @return the object which of type EntityTO, or null if the object is not found
     */
    public T update(T t) throws BaseException {
        em.merge(t);
        em.flush();
        return t;
    }

    /**
     * The Delete method, handles the delete operation for all the classes
     * which are extended from EntityTO.
     *
     * @param t - the object of type EntityTO to create
     */
    public void delete(T t) throws BaseException {
        t = em.merge(t);
        em.remove(t);
        em.flush();
    }

    /**
     * @param queryName
     * @return
     */
    public List<T> findWithNamedQuery(String queryName) throws BaseException {
        return em.createNamedQuery(queryName).getResultList();
    }

    /**
     * @param queryName
     * @param resultLimit
     * @return
     */
    public List<T> findWithNamedQuery(String queryName, int resultLimit) throws BaseException {
        return em.createNamedQuery(queryName).setMaxResults(resultLimit).getResultList();
    }

    /**
     * @param namedQueryName
     * @param parameters
     * @return
     */
    public List<T> findWithNamedQuery(String namedQueryName, Map<String, Object> parameters) throws BaseException {
        return findWithNamedQuery(namedQueryName, parameters, 0);
    }

    /**
     * @param namedQueryName
     * @param parameters
     * @param resultLimit
     * @return
     */
    public List<T> findWithNamedQuery(String namedQueryName, Map<String, Object> parameters, int resultLimit) throws BaseException {
        Query query = this.em.createNamedQuery(namedQueryName);
        if (resultLimit > 0) {
            query.setMaxResults(resultLimit);
        }
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query.getResultList();
    }

    /**
     * @param sql
     * @param type
     * @return
     */
    @SuppressWarnings("UnChecked")
    public List<T> findWithNativeQuery(String sql, Class<T> type) throws BaseException {
        return em.createNativeQuery(sql, type).getResultList();
    }
}
