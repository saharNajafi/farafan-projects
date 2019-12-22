package com.gam.nocr.ems.data.dao.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.DataException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.RegistrationPaymentTO;
import org.apache.commons.lang3.StringUtils;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 12/12/17.
 */
@Stateless(name = "RegistrationPaymentDAO")
@Local(RegistrationPaymentDAOLocal.class)
@Remote(RegistrationPaymentDAORemote.class)
public class RegistrationPaymentDAOImpl extends EmsBaseDAOImpl<RegistrationPaymentTO>
        implements RegistrationPaymentDAOLocal, RegistrationPaymentDAORemote {

    @Override
    @PersistenceContext(unitName = "EmsOraclePU")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Override
    public RegistrationPaymentTO create(RegistrationPaymentTO registrationPaymentTO) throws BaseException {
        try {
            RegistrationPaymentTO to = super.create(registrationPaymentTO);
            em.flush();
            return to;
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.RGP_002, DataExceptionCode.RGP_002_MSG, e);
        }
    }

    public RegistrationPaymentTO findByCitizenId(Long citizenId) throws BaseException {
        List<RegistrationPaymentTO> registrationPaymentTO;
        try {
            registrationPaymentTO = em.createNamedQuery("RegistrationPayment.findByCitizenId")
                    .setParameter("citizenId", citizenId)
                    .getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.RGP_001,
                    DataExceptionCode.RGP_001_MSG, e);
        }
        return !registrationPaymentTO.isEmpty() ? registrationPaymentTO.get(0) : null;
    }

    @Override
    public RegistrationPaymentTO findLastCardRequestPaymentByNationalId(String nationalId) throws DataException {
        List<RegistrationPaymentTO> registrationPaymentTO;
        try {
            nationalId = StringUtils.leftPad(String.valueOf(nationalId), 10, "0");
            registrationPaymentTO = em.createNamedQuery("RegistrationPayment.findLastCardRequestPaymentByNationalId")
                    .setParameter("nationalId", nationalId)
                    .getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.RGP_003,
                    DataExceptionCode.RGP_003_MSG, e);
        }
        return !registrationPaymentTO.isEmpty() ? registrationPaymentTO.get(0) : null;
    }

    @Override
    public String nextValueOfRegistrationPaymentCode() throws BaseException {
        try {
            return em.createNativeQuery("SELECT SEQ_EMS_REG_PAYMENT_CODE.NEXTVAL FROM DUAL").getSingleResult().toString();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.RGP_004,
                    DataExceptionCode.RGP_004_MSG, e);
        }
    }


}
