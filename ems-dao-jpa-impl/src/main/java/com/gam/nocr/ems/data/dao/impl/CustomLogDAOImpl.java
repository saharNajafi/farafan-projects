package com.gam.nocr.ems.data.dao.impl;

import com.farafan.customLog.entities.CustomLogTo;
import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.*;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(name = "CustomLogDAO")
@Local(CustomLogDAOLocal.class)
@Remote(CustomLogDAORemote.class)
public class CustomLogDAOImpl extends EmsBaseDAOImpl<CustomLogTo> implements CustomLogDAORemote, CustomLogDAOLocal {

    @Override
    @PersistenceContext(unitName = "EmsOraclePUSecond")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Override
    public void insertLog(CustomLogTo to) throws BaseException {
        try {
            em.persist(to);
            em.flush();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.CST_001, DataExceptionCode.CST_001_MSG, e);
        }
    }

}
