package com.gam.nocr.ems.data.dao.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.ProvinceCodeTO;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;

/**
 * @author Mazaher Namjoofar
 */

@Stateless(name = "ProvinceCodeDAO")
@Local(ProvinceCodeDAOLocal.class)
@Remote(ProvinceCodeDAORemote.class)
public class ProvinceCodeDAOImpl extends EmsBaseDAOImpl<ProvinceCodeTO> implements ProvinceCodeDAOLocal, ProvinceCodeDAORemote {

    @Override
    public ProvinceCodeTO findByLocationId(Class clazz, Object locationId) throws BaseException {
        try {
            return super.find(clazz, locationId);
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.PCD_001, DataExceptionCode.GLB_006_MSG, e);
        }
    }

}
