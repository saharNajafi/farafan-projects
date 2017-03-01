package com.gam.nocr.ems.data.dao.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.nocr.ems.data.domain.IMSBatchEnquiryTO;
import org.slf4j.Logger;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */

@Stateless(name = "IMSBatchEnquiryDAO")
@Local(IMSBatchEnquiryDAOLocal.class)
@Remote(IMSBatchEnquiryDAORemote.class)
public class IMSBatchEnquiryDAOImpl extends EmsBaseDAOImpl<IMSBatchEnquiryTO> implements IMSBatchEnquiryDAOLocal, IMSBatchEnquiryDAORemote {

    private static final Logger logger = BaseLog.getLogger(IMSBatchEnquiryDAOImpl.class);

	@Override
	@PersistenceContext(unitName = "EmsOraclePU")
	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Override
	public IMSBatchEnquiryTO findByReplyFlag(boolean replyFlag) throws BaseException {
		try {
			List<IMSBatchEnquiryTO> imsBatchEnquiryTOList = em.createQuery("" +
					"SELECT IBE " +
					"FROM IMSBatchEnquiryTO IBE " +
					"WHERE IBE.id IN (SELECT MIN(IBE2.id) FROM IMSBatchEnquiryTO IBE2 WHERE IBE2.replyFlag = :REPLY_FLAG )", IMSBatchEnquiryTO.class)
					.setParameter("REPLY_FLAG", replyFlag).getResultList();
			if(imsBatchEnquiryTOList != null){
				return imsBatchEnquiryTOList.get(0);
			}else{
				return null;
			}
		} catch (Exception e) {
            logger.error(e.getMessage(), e);
			return null;
		}
	}
}
