package com.gam.nocr.ems.biz.service.internal.impl;

import static com.gam.nocr.ems.config.EMSLogicalNames.DAO_CARD_REQUEST;
import static com.gam.nocr.ems.config.EMSLogicalNames.getDaoJNDIName;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Schedule;
import javax.ejb.Stateless;

import org.slf4j.Logger;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.commons.core.data.dao.factory.DAOFactory;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.dao.BiometricDAO;
import com.gam.nocr.ems.data.dao.CardRequestDAO;
import com.gam.nocr.ems.data.dao.CardRequestHistoryDAO;
import com.gam.nocr.ems.data.dao.CitizenDAO;
import com.gam.nocr.ems.data.dao.DocumentDAO;
import com.gam.nocr.ems.data.dao.PurgeStatusDAO;
import com.gam.nocr.ems.data.dao.impl.BiometricDAOLocal;
import com.gam.nocr.ems.data.dao.impl.CardRequestDAOImpl;
import com.gam.nocr.ems.data.dao.impl.CardRequestDAOLocal;
import com.gam.nocr.ems.data.dao.impl.PurgeStatusDAOLocal;
import com.gam.nocr.ems.data.domain.BiometricTO;
import com.gam.nocr.ems.data.domain.CardRequestTO;
import com.gam.nocr.ems.data.domain.DocumentTO;
import com.gam.nocr.ems.data.domain.DocumentTypeTO;
import com.gam.nocr.ems.data.domain.PurgeStatusTO;
import com.gam.nocr.ems.data.enums.BiometricType;
import com.gam.nocr.ems.data.enums.CardRequestHistoryAction;
import com.gam.nocr.ems.data.enums.SystemId;
import com.gam.nocr.ems.util.EmsUtil;

@Stateless(name = "PurgeService")
@Local(PurgeServiceLocal.class)
@Remote(PurgeServiceRemote.class)
public class PurgeServiceImpl extends EMSAbstractService implements PurgeServiceLocal, PurgeServiceRemote {

	private static final String DEFAULT_PURGE_ACTIVATION = "0";
	
	private static final String DEFAULT_PURGE_AFTER_DELIVERY_DAYS = "365";
	
	private static final String DEFAULT_PURGE_COUNT_PER_CYCLE = "1000";
	
	private static final Logger logger = BaseLog
			.getLogger(CardRequestDAOImpl.class);
	
	private PurgeStatusDAO getPurgeStatusDAO() throws BaseException {
		DAOFactory factory = DAOFactoryProvider.getDAOFactory();
		PurgeStatusDAO purgeStatusDAO = null;
		try {
			purgeStatusDAO = factory.getDAO(EMSLogicalNames
					.getDaoJNDIName(EMSLogicalNames.DAO_PURGE_STATUS));
		} catch (DAOFactoryException e) {
			throw new ServiceException(BizExceptionCode.PGS_004,
					BizExceptionCode.GLB_001_MSG, e,
					EMSLogicalNames.DAO_PURGE_STATUS.split(","));
		}
		return purgeStatusDAO;
	}
	
	private CardRequestDAO getCardRequestDAO() throws BaseException {
		try {
			return DAOFactoryProvider.getDAOFactory().getDAO(
					getDaoJNDIName(DAO_CARD_REQUEST));
		} catch (DAOFactoryException e) {
			throw new ServiceException(BizExceptionCode.PGS_005,
					BizExceptionCode.GLB_001_MSG, e,
					EMSLogicalNames.DAO_CARD_REQUEST.split(","));
		}
	}
	
	private CitizenDAO getCitizenDAO() throws BaseException {
		try {
			return DAOFactoryProvider.getDAOFactory().getDAO(
					getDaoJNDIName(EMSLogicalNames.DAO_CITIZEN));
		} catch (DAOFactoryException e) {
			throw new ServiceException(BizExceptionCode.PGS_007,
					BizExceptionCode.GLB_001_MSG, e,
					EMSLogicalNames.DAO_CITIZEN.split(","));
		}
	}
	
	private BiometricDAO getBiometricDAO() throws BaseException {
		try {
			return DAOFactoryProvider.getDAOFactory().getDAO(
					getDaoJNDIName(EMSLogicalNames.DAO_BIOMETRIC));
		} catch (DAOFactoryException e) {
			throw new ServiceException(BizExceptionCode.PGS_006,
					BizExceptionCode.GLB_001_MSG, e,
					EMSLogicalNames.DAO_BIOMETRIC.split(","));
		}
	}
	
	private DocumentDAO getDocumentDAO() throws BaseException {
		try {
			return DAOFactoryProvider.getDAOFactory().getDAO(
					getDaoJNDIName(EMSLogicalNames.DAO_DOCUMENT));
		} catch (DAOFactoryException e) {
			throw new ServiceException(BizExceptionCode.PGS_008,
					BizExceptionCode.GLB_001_MSG, e,
					EMSLogicalNames.DAO_DOCUMENT.split(","));
		}
	}
	
	private CardRequestHistoryDAO getCardRequestHistoryDAO() throws BaseException {
		try {
			return DAOFactoryProvider.getDAOFactory().getDAO(
					getDaoJNDIName(EMSLogicalNames.DAO_CARD_REQUEST_HISTORY));
		} catch (DAOFactoryException e) {
			throw new ServiceException(BizExceptionCode.PGS_009,
					BizExceptionCode.GLB_001_MSG, e,
					EMSLogicalNames.DAO_CARD_REQUEST_HISTORY.split(","));
		}
	}
	
	
//	@Schedule(hour="*", minute="*/30", persistent=false)
	@Override
	public void doPurgeNextCycle() {
		
		Integer isActive = Integer.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_PURGE_ACTIVATION,
                DEFAULT_PURGE_ACTIVATION));
		
		Integer purgeAfterDeliveryDays = Integer.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_PURGE_AFTER_DELIVERY_DAYS,
				DEFAULT_PURGE_AFTER_DELIVERY_DAYS));
		
		Integer purgeCountPerCycle = Integer.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_PURGE_COUNT_PER_CYCLE, DEFAULT_PURGE_COUNT_PER_CYCLE));
		
		if(isActive == 0)
		{
			return;
		}

		PurgeStatusTO purgeStatus  = null;
		PurgeStatusTO newPurgeStatus = new PurgeStatusTO();
		List<CardRequestTO> crqList = null;
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, (-1) * purgeAfterDeliveryDays);
		try {
			purgeStatus = getPurgeStatusDAO().findLastPurgeStatus();
		} catch (Exception e) {

//			throw new ServiceException(BizExceptionCode.PGS_001, e.getMessage());
			e.printStackTrace();
			logger.error(BizExceptionCode.PGS_001 + " : " + e.getMessage());
		}
		if(purgeStatus != null)
		{
			try {
				crqList = getCardRequestDAO().findNextBatchDeliveredCRQFromIdBeforeDate(purgeStatus.getLastPurgesCRQId(),cal, purgeCountPerCycle);
			} catch (BaseException e) {

//				throw new ServiceException(BizExceptionCode.PGS_002, e.getMessage());
				logger.error(BizExceptionCode.PGS_002 + " : " + e.getMessage());
				e.printStackTrace();
			}
		}
		else
		{
			try {
				crqList = getCardRequestDAO().findNextBatchDeliveredCRQFromIdBeforeDate(0, cal, purgeCountPerCycle);
			} catch (BaseException e) {

//				throw new ServiceException(BizExceptionCode.PGS_002, e.getMessage());
				logger.error(BizExceptionCode.PGS_003 + " : " + e.getMessage());
				e.printStackTrace();
			}
		}
		long crqId = 0;
		byte[] bytes = new byte[1];
		for(CardRequestTO crq : crqList)
		{
			isActive = Integer.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_PURGE_ACTIVATION,
	                DEFAULT_PURGE_ACTIVATION));
			//checks purge job is still active
			if(isActive == 0)
				break;
			//if citizen is in reissue cycle, its biometric data maybe in change, so should not be purged.
			try {
				List<CardRequestTO> undeliveredCrqList = getCitizenDAO().checkCitizenHasAnyUnDeliveredRequest(crq.getCitizen());
				if(undeliveredCrqList != null && !undeliveredCrqList.isEmpty())
					continue;
			} catch (BaseException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
				logger.error(BizExceptionCode.PGS_004 + " : " + e1.getMessage());
				e1.printStackTrace();
			}
						
			List<BiometricTO> bioList = crq.getCitizen().getCitizenInfo().getBiometrics();
			for(BiometricTO bio : bioList)
			{
				if(bio.getType() == BiometricType.FACE_CHIP)
				{
					bio.setData(bytes);
				}
				else if(bio.getType() == BiometricType.FACE_IMS)
				{
					bio.setData(bytes);
				}
				else if(bio.getType() == BiometricType.FACE_LASER)
				{
					bio.setData(bytes);
				}
				else if(bio.getType() == BiometricType.FACE_MLI)
				{
					bio.setData(bytes);
				}
				else if(bio.getType() == BiometricType.FING_ALL)
				{
					bio.setData(bytes);
				}
				else if(bio.getType() == BiometricType.FING_CANDIDATE)
				{
					bio.setData(bytes);
				}
				else if(bio.getType() == BiometricType.FING_MIN_1)
				{
					bio.setData(bytes);
				}
				else if(bio.getType() == BiometricType.FING_MIN_2)
				{
					bio.setData(bytes);
				}
				else if(bio.getType() == BiometricType.VIP_IMAGE)
				{
					bio.setData(bytes);
				}
				try {
					getBiometricDAO().update(bio);
					getCardRequestDAO().refreshCRQ(crq);
				} catch (BaseException e) {
//					throw new ServiceException(BizExceptionCode.PGS_003, e.getMessage());
					logger.error(BizExceptionCode.PGS_005 + " : " + e.getMessage());
					e.printStackTrace();
				}
			}
			
			List<DocumentTO> documentList = crq.getCitizen().getCitizenInfo().getDocuments();
			for(DocumentTO doc : documentList)
			{
				doc.setData(bytes);
				try {
					getDocumentDAO().update(doc);
					getCardRequestDAO().refreshCRQ(crq);
				} catch (BaseException e) {
//					throw new ServiceException(BizExceptionCode.PGS_004, e.getMessage());
					logger.error(BizExceptionCode.PGS_006 + " : " + e.getMessage());
					e.printStackTrace();
				}
			}
			crqId = crq.getId();
			try {
				getCardRequestHistoryDAO().create(crq, "Purge Schedule run",
				        SystemId.IMS, null, CardRequestHistoryAction.BIO_DUC_PURGED, null);
			} catch (BaseException e) {
				logger.error(BizExceptionCode.PGS_010 + " : " + e.getMessage());
				e.printStackTrace();
			}
		}
		newPurgeStatus.setLastPurgeDate(new Date());
		if(crqId == 0 && purgeStatus!= null)
		{
			crqId = purgeStatus.getLastPurgesCRQId();
		}
		newPurgeStatus.setLastPurgesCRQId(crqId);
		try {
			getPurgeStatusDAO().create(newPurgeStatus);
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			logger.error(BizExceptionCode.PGS_007 + " : " + e.getMessage());
			e.printStackTrace();
		}
		
	}

}
