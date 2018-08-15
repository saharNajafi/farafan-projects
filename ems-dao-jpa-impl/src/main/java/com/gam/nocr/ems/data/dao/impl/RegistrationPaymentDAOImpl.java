package com.gam.nocr.ems.data.dao.impl;
import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.DataException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.RegistrationPaymentTO;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.util.List;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 12/12/17.
 */
@Stateless(name = "RegistrationPaymentDAO")
@Local(RegistrationPaymentDAOLocal.class)
@Remote(RegistrationPaymentDAORemote.class)
public class RegistrationPaymentDAOImpl extends EmsBaseDAOImpl<RegistrationPaymentTO>
implements  RegistrationPaymentDAOLocal, RegistrationPaymentDAORemote{

    public RegistrationPaymentTO findByCitizenId(Long citizenId) throws BaseException {
        List<RegistrationPaymentTO> registrationPaymentTO;
        try {
            registrationPaymentTO =  em.createNamedQuery("RegistrationPayment.findByCitizenId")
                    .setParameter("citizenId", citizenId)
                    .getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.RGP_001,
                    DataExceptionCode.RGP_001_MSG, e);
        }
        return registrationPaymentTO.size() !=0 ? registrationPaymentTO.get(0) : null;
    }


}
