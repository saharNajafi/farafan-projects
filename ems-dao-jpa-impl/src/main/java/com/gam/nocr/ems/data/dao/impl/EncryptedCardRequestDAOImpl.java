package com.gam.nocr.ems.data.dao.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.EncryptedCardRequestTO;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author: Haeri (haeri@gamelectronics.com)
 */
@Stateless(name = "EncryptedCardRequestDAO")
@Local(EncryptedCardRequestDAOLocal.class)
public class EncryptedCardRequestDAOImpl extends EmsBaseDAOImpl<EncryptedCardRequestTO> implements EncryptedCardRequestDAOLocal, EncryptedCardRequestDAORemote {

    @Override
    @PersistenceContext(unitName = "EmsOraclePU")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    /**
     * The create method, handles all the save operations for all the classes which are extended from EntityTO
     *
     * @param encryptedCardRequestTO - the object of type EntityTO to create
     * @return the object of type EntityTo
     */
    @Override
    public EncryptedCardRequestTO create(EncryptedCardRequestTO encryptedCardRequestTO) throws BaseException {
        try {
            EncryptedCardRequestTO to = super.create(encryptedCardRequestTO);
            em.flush();
            return to;
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.ECI_001, DataExceptionCode.GLB_004_MSG, e);
        }
    }
}
