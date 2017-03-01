package com.gam.nocr.ems.data.dao.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.DataException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.ReligionTO;
import com.gam.nocr.ems.util.EmsUtil;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jalilian
 * Date: 8/21/13
 * Time: 10:12 AM
 */
@Stateless(name = "ReligionDAO")
@Local(ReligionDAOLocal.class)
@Remote(ReligionDAORemote.class)
public class ReligionDAOImpl extends EmsBaseDAOImpl<ReligionTO> implements ReligionDAOLocal, ReligionDAORemote {

    @Override
    @PersistenceContext(unitName = "EmsOraclePU")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    /**
     * The method findByName is used to find a specified religion by name
     *
     * @param name is an instance of type {@link String}, which represents a specified religion
     * @return an instance of type {@link com.gam.nocr.ems.data.domain.ReligionTO} or null
     * @throws com.gam.commons.core.BaseException
     *
     */
    @Override
    public ReligionTO findByName(String name) throws BaseException {
        try {
            List<ReligionTO> religionTOs = em.createQuery("SELECT RLG " +
                    "FROM ReligionTO RLG " +
                    "WHERE RLG.name LIKE :RELIGION_NAME ", ReligionTO.class)
                    .setParameter("RELIGION_NAME", "%" + name + "%")
                    .getResultList();
            if (EmsUtil.checkListSize(religionTOs)) {
                return religionTOs.get(0);
            }
            return null;
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.RLG_001, DataExceptionCode.GLB_005_MSG, e);
        }
    }
}
