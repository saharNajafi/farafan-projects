package com.gam.nocr.ems.data.dao.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.FeatureExtractIdsTO;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 10/16/18.
 */
@Stateless(name = "FeatureExtractIdsDAO")
@Local(FeatureExtractIdsDAOLocal.class)
@Remote(FeatureExtractIdsDAORemote.class)
public class FeatureExtractIdsDAOImpl extends EmsBaseDAOImpl<FeatureExtractIdsTO>
        implements FeatureExtractIdsDAOLocal, FeatureExtractIdsDAORemote{
    @Override
    public FeatureExtractIdsTO findById(Long feiId) throws BaseException{
        try {
            return (FeatureExtractIdsTO) em.createNamedQuery("FeatureExtractIdsTO.findById")
                    .setParameter("id", feiId)
                    .getSingleResult();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.FEI_001,
                    DataExceptionCode.FEI_001_MSG, e);
        }
    }
}