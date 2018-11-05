package com.gam.nocr.ems.data.dao.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.FeatureExtractIdsTO;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 10/16/18.
 */
@Stateless(name = "FeatureExtractNormalDAO")
@Local(FeatureExtractIdsDAOLocal.class)
@Remote(FeatureExtractIdsDAORemote.class)
public class FeatureExtractIdsDAOImpl extends EmsBaseDAOImpl<FeatureExtractIdsTO>
        implements FeatureExtractIdsDAOLocal, FeatureExtractIdsDAORemote {

    @Override
    @PersistenceContext(unitName = "EmsOraclePU")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Override
    public FeatureExtractIdsTO findById(Long fenId) throws BaseException{
        List<FeatureExtractIdsTO> featureExtractIdsTOList;
        try {
            featureExtractIdsTOList = em.createNamedQuery("FeatureExtractIdsTO.findById")
                    .setParameter("id", fenId)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.FEN_001,
                    DataExceptionCode.FEN_001_MSG, e);
        }
        return featureExtractIdsTOList.size() != 0 ? featureExtractIdsTOList.get(0) : null;
    }
}