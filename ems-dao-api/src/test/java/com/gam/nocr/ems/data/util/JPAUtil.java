package com.gam.nocr.ems.data.util;

import com.gam.commons.core.BaseLog;
import com.gam.nocr.ems.config.DataExceptionCode;
import org.omg.CORBA.SystemException;
import org.slf4j.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.UserTransaction;

/**
 * Basic J2EE JPAUtil class, handles EntityManagerFactory, EntityManager and
 * EntityTransaction
 * Uses a static initializer for the initial EntityManagerFactory creation
 * and holds EntityManager and EntityTransaction in thread local variables.
 *
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */

public class JPAUtil {

    private static final Logger logger = BaseLog.getLogger(JPAUtil.class);

	private static String persistenceUnitName;
	private static EntityManagerFactory entityManagerFactory;
	private static final ThreadLocal threadEntityManager = new ThreadLocal();
	private static final ThreadLocal threadEntityTransaction = new ThreadLocal();


	/**
	 * Create the initial EntityManagerFactory with a configuration which
	 * its name is passed to this method as the input parameter.
	 */
	public static void setEntityManagerFactory(String persistenceUnitName) {
		try {
			if(entityManagerFactory == null) {
				entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
			}
		} catch (Throwable ex) {
            logger.error(DataExceptionCode.GLB_ERR_MSG, ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	/**
	 * Returns the EntityManagerFactory used for this static class.
	 *
	 * @return EntityManagerFactory
	 */
	public static EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}

	/**
	 * Retrieves the current EntityManager local to the thread.
	 * If no Session is open, opens a new EntityManager for the running thread.
	 *
	 * @return EntityManager
	 */
	public static EntityManager getEntityManager() {
		EntityManager em = (EntityManager) threadEntityManager.get();
		if (em == null) {
			em = getEntityManagerFactory().createEntityManager();
			threadEntityManager.set(em);
		}
		return em;
	}

	/**
	 * Closes the EntityManager local to the thread.
	 */
	public static void closeEntityManager() {
		EntityManager em = (EntityManager) threadEntityManager.get();
		threadEntityManager.set(null);
		if ((null != em) && em.isOpen()) {
			em.close();
		}
	}

	/**
	 * Start a new database entity transaction.
	 */
	public static void beginTransaction() throws NamingException, SystemException, NotSupportedException, javax.transaction.SystemException, NotSupportedException {
		UserTransaction transaction = (UserTransaction) threadEntityTransaction.get();
		if (transaction == null) {
			transaction = (UserTransaction) new InitialContext().lookup("javax.transaction.UserTransaction");
			transaction.begin();
			threadEntityTransaction.set(transaction);
		}
	}

	/**
	 * Commit the database entity transaction.
	 */
	public static void commitTransaction() throws SystemException, javax.transaction.RollbackException, HeuristicRollbackException, HeuristicMixedException, javax.transaction.SystemException {
		UserTransaction transaction = (UserTransaction) threadEntityTransaction.get();
		if (transaction != null) {
			transaction.commit();
			threadEntityTransaction.set(null);
		}


	}

	/**
	 * Rollback the database entity transaction.
	 */
	public static void rollbackEntityTransaction() {
		EntityTransaction et = (EntityTransaction) threadEntityTransaction.get();
		try {
			threadEntityTransaction.set(null);
			if ((null != et) && et.isActive()) {
				et.rollback();
			}
		} finally {
			closeEntityManager();
		}
	}

	/**
	 * Given a query file name (located in resources/dbtestscripts folder) and tries to run its content
	 *
	 * @param queryFileName Name of initial sql query file name
	 */
	public static void runInitialQuery(String queryFileName) {
		String query = "";    //	TODO: Read from 'queryFileName'
		getEntityManager().createQuery(query).executeUpdate();
	}
}