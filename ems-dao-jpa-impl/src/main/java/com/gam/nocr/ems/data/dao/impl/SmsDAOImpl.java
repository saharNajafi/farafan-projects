package com.gam.nocr.ems.data.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.OutgoingSMSTO;
import com.gam.nocr.ems.util.EmsUtil;
import com.tangosol.dev.assembler.New;

/**
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
@Stateless(name = "SmsDAO")
@Local(SmsDAOLocal.class)
@Remote(SmsDAORemote.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class SmsDAOImpl extends EmsBaseDAOImpl<OutgoingSMSTO> implements SmsDAOLocal, SmsDAORemote {

    @Override
    @PersistenceContext(unitName = "EmsOraclePU")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    /**
     * The Update method, handles the update operations for all the classes
     * which are extended from EntityTO.
     *
     * @param outgoingSMSTO - the object of type EntityTO to create
     * @return the object which of type EntityTO, or null if the object is not found
     */
    @Override
    public OutgoingSMSTO update(OutgoingSMSTO outgoingSMSTO) throws BaseException {
        try {
            OutgoingSMSTO to = super.update(outgoingSMSTO);
            em.flush();
            return to;
        } catch (Exception e) {
//            String err = e.getMessage();
//            if (e.getCause() != null) {
//                if (e.getCause().getCause() != null)
//                    err = e.getCause().getCause().getMessage();
//                else
//                    err = e.getCause().getMessage();
//            }
//            if (err.contains(FOREIGN_KEY_ENROLL_OFC_PER_ID))
//                throw new DAOException(DataExceptionCode.PDI_026, DataExceptionCode.PDI_025_MSG, e);
//            else
            throw new DAOException(DataExceptionCode.SDI_001, DataExceptionCode.GLB_006_MSG, e);
        }
    }

	@Override
	public void deleteOldRecordsFromMsgt(Integer timeInterval, Integer smsType) throws DAOException {
		
		try {

			if (timeInterval > 0) {
				em.createQuery(
						"DELETE from OutgoingSMSTO msg where msg.sentDate is not null and msg.sentDate <= :specifiedTime")
						.setParameter("specifiedTime",
								EmsUtil.differDay(new Date(), -timeInterval))
						.executeUpdate();
				em.flush();
			} else if (timeInterval == 0) {
				em.createQuery(
						"DELETE from OutgoingSMSTO msg where msg.sentDate is not null and msg.sentDate = :specifiedTime ")
						.setParameter("specifiedTime", EmsUtil.getToday())
						.executeUpdate();
				em.flush();
			}

		} catch (Exception e) {
			throw new DAOException(DataExceptionCode.SDI_002,
					DataExceptionCode.GLB_006_MSG, e);
		}
		
	}

	@Override
	public List<Long> fetchMessagesId(Integer sendSmsType, Integer fetchLimit) throws DAOException {
		try {
			
			Calendar cal = Calendar.getInstance();
	        cal.setTime(new Date());
	        cal.add(Calendar.MINUTE, -2);
	        Date twoMinAgo = cal.getTime();
	        
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder
					.append(" SELECT MOS.OSM_ID FROM MSGT_OUTGOING_SMS MOS ");
			stringBuilder.append("WHERE ");
			stringBuilder.append("MOS.OSM_SENT_DATE IS NULL ");
			stringBuilder.append("AND MOS.OSM_TYPE = :sendSmsType ");
			stringBuilder.append("AND MOS.OSM_CELL_NO <> '0' ");
			stringBuilder.append("AND LENGTH(MOS.OSM_CELL_NO) >= 11 ");
			stringBuilder.append("AND MOS.OSM_REQUEST_DATE < :twoMinAgo ");
			stringBuilder.append("AND MOS.OSM_RETRY_LIMIT > 0 ");
			stringBuilder.append("AND MOS.OSM_RETRY_LIMIT > MOS.OSM_RETRY_COUNT ");
			stringBuilder.append("ORDER BY MOS.OSM_ID ASC ");
			List<BigDecimal> results = em
					.createNativeQuery(stringBuilder.toString())
					.setParameter("twoMinAgo", twoMinAgo )
					.setParameter("sendSmsType", sendSmsType)
					.setMaxResults(fetchLimit).getResultList();
			
			if (EmsUtil.checkListSize(results)) {
				List<Long> resultList = new ArrayList<Long>();
				for (BigDecimal id : results) {
					resultList.add(id.longValue());
				}

				return resultList;
			}
			return null;

		} catch (Exception e) {
			throw new DAOException(DataExceptionCode.SDI_003,
					DataExceptionCode.GLB_005_MSG, e);
		}

	}
}
