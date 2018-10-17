package com.gam.nocr.ems.data.dao.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.FeatureExtractVersionsTO;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 10/16/18.
 */
@Stateless(name = "FeatureExtractVersionsDAO")
@Local(FeatureExtractVersionsDAOLocal.class)
@Remote(FeatureExtractVersionsDAORemote.class)
public class FeatureExtractVersionsDAOImpl
        extends EmsBaseDAOImpl<FeatureExtractVersionsTO> implements FeatureExtractVersionsDAOLocal, FeatureExtractVersionsDAORemote{
    @Override
    public FeatureExtractVersionsTO findById(Long fevId) throws BaseException{
        try {
            return (FeatureExtractVersionsTO) em.createNamedQuery("FeatureExtractVersionsTO.findById")
                    .setParameter("id", fevId)
                    .getSingleResult();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.FEV_001,
                    DataExceptionCode.FEV_001_MSG, e);
        }
    }
}
