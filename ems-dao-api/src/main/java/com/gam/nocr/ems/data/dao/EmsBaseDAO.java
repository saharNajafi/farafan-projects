package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.dao.BaseDAO;
import com.gam.commons.core.data.domain.ExtEntityTO;

import javax.ejb.Local;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

/**
 * The EmsBaseDAO interface, defines the base crud behaviours, and
 * is implemented by all the classes which handle crud operations.
 *
 * @author : Saeed Jalilian (jalilian@gamelectronics.com)
 */
@Local
public interface EmsBaseDAO<T extends ExtEntityTO> extends BaseDAO {

    public EntityManager getEm();

    public void setEm(EntityManager em);

    /**
     * The create method, handles all the save operations for all the
     * classes which are extended from EntityTO
     *
     * @param t - the object of type EntityTO to create
     * @return the object of type EntityTo
     */
    public T create(T t) throws BaseException;

    /**
     * The find method, handles the find operation on the classes
     * which are extended from EntityTO.
     *
     * @param type - the class type of an instance of the class
     * @param id   - the id number of an instance of the class
     * @return the object which of type EntityTO, or null if the object is not found
     */
    public T find(Class type, Object id) throws BaseException;

    /**
     * The Update method, handles the update operations for all the classes
     * which are extended from EntityTO.
     *
     * @param t - the object of type EntityTO to create
     * @return the object which of type EntityTO, or null if the object is not found
     */
    public T update(T t) throws BaseException;

    /**
     * The Delete method, handles the delete operation for all the classes
     * which are extended from EntityTO.
     *
     * @param t - the object of type EntityTO to create
     */
    public void delete(T t) throws BaseException;

    /**
     * @param queryName
     * @return
     */
    public List<T> findWithNamedQuery(String queryName) throws BaseException;

    /**
     * @param queryName
     * @param resultLimit
     * @return
     */
    public List<T> findWithNamedQuery(String queryName,
                                      int resultLimit) throws BaseException;

    /**
     * @param namedQueryName
     * @param parameters
     * @return
     */
    public List<T> findWithNamedQuery(String namedQueryName,
                                      Map<String, Object> parameters) throws BaseException;

    /**
     * @param namedQueryName
     * @param parameters
     * @param resultLimit
     * @return
     */
    public List<T> findWithNamedQuery(String namedQueryName,
                                      Map<String, Object> parameters,
                                      int resultLimit) throws BaseException;

    /**
     * @param sql
     * @param type
     * @return
     */
    public List<T> findWithNativeQuery(String sql,
                                       Class<T> type) throws BaseException;

}
