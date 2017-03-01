package com.gam.nocr.ems.biz.service.internal.impl;

import static com.gam.nocr.ems.config.EMSLogicalNames.DAO_PERSON;
import static com.gam.nocr.ems.config.EMSLogicalNames.getDaoJNDIName;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.dao.DepartmentDAO;
import com.gam.nocr.ems.data.dao.EnrollmentOfficeDAO;
import com.gam.nocr.ems.data.dao.MessageDAO;
import com.gam.nocr.ems.data.dao.MessageDestinationDAO;
import com.gam.nocr.ems.data.dao.PersonDAO;
import com.gam.nocr.ems.data.dao.PreparedMessageDAO;
import com.gam.nocr.ems.data.domain.DepartmentTO;
import com.gam.nocr.ems.data.domain.EnrollmentOfficeTO;
import com.gam.nocr.ems.data.domain.LocationTO;
import com.gam.nocr.ems.data.domain.MessageDestinationTO;
import com.gam.nocr.ems.data.domain.MessageTO;
import com.gam.nocr.ems.data.domain.PersonTO;
import com.gam.nocr.ems.data.domain.PreparedMessageTO;
import com.gam.nocr.ems.data.domain.ws.MessageWTO;
import com.gam.nocr.ems.data.enums.DestMessageType;
import com.gam.nocr.ems.data.enums.EnrollmentOfficeType;
import com.gam.nocr.ems.data.enums.PreparedMessageState;

@Stateless(name = "MessageService")
@Local(MessageServiceLocal.class)
@Remote(MessageServiceRemote.class)
public class MessageServiceImpl extends EMSAbstractService implements
		MessageServiceLocal, MessageServiceRemote {

	private MessageDAO getMessageDAO() throws BaseException {
		try {
			return DAOFactoryProvider.getDAOFactory()
					.getDAO(EMSLogicalNames
							.getDaoJNDIName(EMSLogicalNames.DAO_MESSAGE));
		} catch (DAOFactoryException e) {
			throw new DelegatorException(BizExceptionCode.MSS_006,
					BizExceptionCode.GLB_001_MSG, e,
					new String[] { EMSLogicalNames.DAO_MESSAGE });
		}
	}

	private PreparedMessageDAO getPreparedMessageDAO() throws BaseException {
		try {
			return DAOFactoryProvider
					.getDAOFactory()
					.getDAO(EMSLogicalNames
							.getDaoJNDIName(EMSLogicalNames.DAO_PREPARED_MESSAGE));
		} catch (DAOFactoryException e) {
			throw new DelegatorException(BizExceptionCode.MSS_015,
					BizExceptionCode.GLB_001_MSG, e,
					new String[] { EMSLogicalNames.DAO_MESSAGE });
		}
	}

	private MessageDestinationDAO getMessageDestinationDAO()
			throws BaseException {
		try {
			return DAOFactoryProvider
					.getDAOFactory()
					.getDAO(EMSLogicalNames
							.getDaoJNDIName(EMSLogicalNames.DAO_MESSAGE_DESTINATION));
		} catch (DAOFactoryException e) {
			throw new DelegatorException(BizExceptionCode.MSS_016,
					BizExceptionCode.GLB_001_MSG, e,
					new String[] { EMSLogicalNames.DAO_MESSAGE });
		}
	}

	private EnrollmentOfficeDAO getEnrollmentOfficeDAO() throws BaseException {
		try {
			return DAOFactoryProvider
					.getDAOFactory()
					.getDAO(EMSLogicalNames
							.getDaoJNDIName(EMSLogicalNames.DAO_ENROLLMENT_OFFICE));
		} catch (DAOFactoryException e) {
			throw new ServiceException(BizExceptionCode.EOS_001,
					BizExceptionCode.GLB_001_MSG, e,
					EMSLogicalNames.DAO_ENROLLMENT_OFFICE.split(","));
		}
	}

	private PersonDAO getPersonDAO() throws BaseException {
		try {
			return DAOFactoryProvider.getDAOFactory().getDAO(
					getDaoJNDIName(DAO_PERSON));
		} catch (DAOFactoryException e) {
			throw new ServiceException(BizExceptionCode.PSI_001,
					BizExceptionCode.GLB_001_MSG, e,
					new String[] { EMSLogicalNames.DAO_PERSON });
		}
	}

	private DepartmentDAO getDepartmentDAO() throws BaseException {
		try {
			return DAOFactoryProvider.getDAOFactory().getDAO(
					EMSLogicalNames
							.getDaoJNDIName(EMSLogicalNames.DAO_DEPARTMENT));
		} catch (DAOFactoryException e) {
			throw new ServiceException(BizExceptionCode.DSI_001,
					BizExceptionCode.GLB_001_MSG, e,
					new String[] { EMSLogicalNames.DAO_DEPARTMENT });
		}
	}

	@Override
	public List<MessageDestinationTO> getMessageByCriteriagetMessageByCriteria(
			UserProfileTO userProfileTO, MessageWTO wto) throws BaseException {
		
		Long personID = getPersonDAO().findPersonIdByUsername(getUserProfileTO().getUserName());
		if (wto.getMsgType() != null) {
			if ("1".equals(wto.getMsgType())) {
				return getMessageDAO().getMessageForAll(
						personID, wto.getMsgDate(),
						wto.getPageSize(), wto.getPageNo());
			} else if ("2".equals(wto.getMsgType())) {

				DepartmentTO fetchDepartment = getDepartmentDAO()
						.fetchDepartment(userProfileTO.getDepID());

				if (fetchDepartment == null)
					return null;

				Long provinceId = fetchDepartment.getLocation().getProvince()
						.getId();

				return getMessageDAO().getMessageByProvinceId(
						personID, provinceId,
						wto.getMsgDate(), wto.getPageSize(), wto.getPageNo());

			} else if ("3".equals(wto.getMsgType())) {
				return getMessageDAO().getMessageByOfficeId(
						personID, userProfileTO.getDepID(),
						wto.getMsgDate(), wto.getPageSize(), wto.getPageNo());
			} else if ("4".equals(wto.getMsgType())) {
				return getMessageDAO().getMessageByPersonId(
						personID, wto.getMsgDate(),
						wto.getPageSize(), wto.getPageNo());
			}
		}

		return null;
	}

	@Override
	public List<Long> fetchReadyToProcessMessage() throws BaseException {
		return getMessageDAO().fetchReadyToProcessMessage();
	}

	@Override
	public void processMessage(Long id) throws BaseException {

		PreparedMessageTO msg = getPreparedMessageDAO().find(
				PreparedMessageTO.class, id);
		if (msg != null) {
			// ALL(1)
			if (msg.getIsAll() != false && msg.getIsNocr() == true
					&& msg.getIsOffice() == true
					&& msg.getOffices().size() == 0
					&& msg.getProvinces().size() == 0
					&& msg.getIsManager() == false
					&& msg.getPersons().size() == 0) {
				saveAll(msg);

			}
			// ALL Manager(2)
			else if (msg.getIsNocr() == true
					&& msg.getIsOffice() == true
					&& msg.getOffices().size() == 0
					&& msg.getProvinces().size() == 0
					&& msg.getIsManager() == true
					&& msg.getPersons().size() == 0) {
				saveAllManager(msg);

			}
			// All NOCR Manager(3)
			else if (msg.getIsNocr() == true
					&& msg.getIsOffice() == false
					&& msg.getOffices().size() == 0
					&& msg.getProvinces().size() == 0
					&& msg.getIsManager() == true
					&& msg.getPersons().size() == 0) {
				saveAllOfficeOrNocrManager(msg, EnrollmentOfficeType.NOCR);

			}
			// ALL OFFICE Manager(4)
			else if (msg.getIsNocr() == false
					&& msg.getIsOffice() == true
					&& msg.getOffices().size() == 0
					&& msg.getProvinces().size() == 0
					&& msg.getIsManager() == true
					&& msg.getPersons().size() == 0) {
				saveAllOfficeOrNocrManager(msg, EnrollmentOfficeType.OFFICE);

			}
			// All NOCR(5)
			else if (msg.getIsNocr() == true
					&& msg.getIsOffice() == false
					&& msg.getOffices().size() == 0
					&& msg.getProvinces().size() == 0
					&& msg.getIsManager() == false
					&& msg.getPersons().size() == 0) {
				saveAllNOCROrOffice(msg, EnrollmentOfficeType.NOCR);

			}
			// All OFFICE(6)
			else if (msg.getIsNocr() == false
					&& msg.getIsOffice() == true
					&& msg.getOffices().size() == 0
					&& msg.getProvinces().size() == 0
					&& msg.getIsManager() == false
					&& msg.getPersons().size() == 0) {
				saveAllNOCROrOffice(msg, EnrollmentOfficeType.OFFICE);

			}
			// Province is just one or multi and All NOCR(7)
			else if (msg.getIsAll() == false && msg.getIsNocr() == true
					&& msg.getIsOffice() == false
					&& msg.getOffices().size() == 0
					&& msg.getProvinces().size() > 0
					&& msg.getIsManager() == false
					&& msg.getPersons().size() == 0) {
				saveProvinceAllNOCRorOFFICE(msg, msg.getProvinces(),
						EnrollmentOfficeType.NOCR, false);

			}
			// Province is just one or multi and OFFICE(8)
			else if (msg.getIsAll() == false && msg.getIsNocr() == false
					&& msg.getIsOffice() == true 
					&& msg.getOffices().size() == 0
					&& msg.getProvinces().size() > 0
					&& msg.getIsManager() == false
					&& msg.getPersons().size() == 0) {
				saveProvinceAllNOCRorOFFICE(msg, msg.getProvinces(),
						EnrollmentOfficeType.OFFICE, false);

			}
			// Province is just one or multi and Manager(9)
			else if (msg.getIsAll() == false  
					&& msg.getIsNocr() == true
					&& msg.getIsOffice() == true 
					&& msg.getOffices().size() == 0
					&& msg.getProvinces().size() > 0
					&& msg.getIsManager() == true
					&& msg.getPersons().size() == 0) {
				saveProvinceAllManager(msg, msg.getProvinces());

			}
			// Province is just one or multi and OFFICE and Manager(10)
			else if (msg.getIsAll() == false && msg.getIsNocr() == false
					&& msg.getIsOffice() == true 
					&& msg.getOffices().size() == 0
					&& msg.getProvinces().size() > 0
					&& msg.getIsManager() == true
					&& msg.getPersons().size() == 0) {

				saveProvinceAllNOCRorOFFICE(msg, msg.getProvinces(),
						EnrollmentOfficeType.OFFICE, true);

			}
			// Province is just one or multi and NOCR and Manager(11)
			else if (msg.getIsAll() == false && msg.getIsNocr() == true
					&& msg.getIsOffice() == false
					&& msg.getOffices().size() == 0
					&& msg.getProvinces().size() > 0
					&& msg.getIsManager() == true
					&& msg.getPersons().size() == 0) {
				saveProvinceAllNOCRorOFFICE(msg, msg.getProvinces(),
						EnrollmentOfficeType.NOCR, true);

			}
			// Multi province(12)
			else if (msg.getIsAll() == false 
					&& msg.getOffices().size() == 0
					&& msg.getProvinces().size() > 0
					&& msg.getIsManager() == false
					&& msg.getPersons().size() == 0) {
				saveMultiProvince(msg, msg.getProvinces());

			}
			// province is just one and office is one or multi and Manager is
			// selected(13)
			else if (msg.getIsAll() == false
					&& msg.getOffices().size() > 0
					&& msg.getProvinces().size() == 1
					&& msg.getIsManager() == true
					&& msg.getPersons().size() == 0) {
				saveOfficeForAllManager(msg, msg.getOffices());

			}
			// OFFICE is one or multi (14)
			else if (msg.getIsAll() == false  && msg.getOffices().size() > 0
					&& msg.getProvinces().size() == 1
					&& msg.getPersons().size() == 0 
					&& msg.getIsManager() == false) {
				saveOneOfficeOrMoreAndManagerIsNotSelected(msg, msg.getOffices());

			}
			// Office is just one and person is one or more(15)
			else if (msg.getIsAll() == false 
					&& msg.getOffices().size() == 1
					&& msg.getProvinces().size() == 1
					&& msg.getPersons().size() > 0
					&& msg.getIsManager() == false) {
				saveOfficeMultiPerson(msg, msg.getPersons());

			}

		}

	}

	private void saveOneOfficeOrMoreAndManagerIsNotSelected(PreparedMessageTO msg,
			List<EnrollmentOfficeTO> enrollmentOfficeList) throws BaseException {
		if (enrollmentOfficeList != null && enrollmentOfficeList.size() > 0) {
			MessageTO messageTO = createMessageTO(msg);
			for (EnrollmentOfficeTO enrollmentOfficeTO : enrollmentOfficeList) {

				MessageDestinationTO messageDestinationTO = new MessageDestinationTO();
				messageDestinationTO.setMessage(messageTO);
				messageDestinationTO.setDestinationType(DestMessageType.OFFICE);
				messageDestinationTO.setDestinationId(enrollmentOfficeTO
						.getId());
				getMessageDestinationDAO().create(messageDestinationTO);

			}
			msg.setPreparedState(PreparedMessageState.SENT);

		}

	}

	private void saveOfficeForAllManager(PreparedMessageTO msg,
			List<EnrollmentOfficeTO> enrollmentOfficeList) throws BaseException {

		if (enrollmentOfficeList != null && enrollmentOfficeList.size() > 0) {
			MessageTO messageTO = createMessageTO(msg);
			for (EnrollmentOfficeTO enrollmentOfficeTO : enrollmentOfficeList) {
				PersonTO person = enrollmentOfficeTO.getManager();
				if (person != null) {
					Long personId = person.getId();
					MessageDestinationTO messageDestinationTO = new MessageDestinationTO();
					messageDestinationTO.setMessage(messageTO);
					messageDestinationTO
							.setDestinationType(DestMessageType.PRIVATE);
					messageDestinationTO.setDestinationId(personId);
					getMessageDestinationDAO().create(messageDestinationTO);
				}

			}
			msg.setPreparedState(PreparedMessageState.SENT);

		}

	}

	private void saveOfficeMultiPerson(PreparedMessageTO msg,
			List<PersonTO> personList) throws BaseException {

		if (personList != null && personList.size() > 0) {
			MessageTO messageTO = createMessageTO(msg);
			for (PersonTO personTO : personList) {
				Long personId = personTO.getId();
				MessageDestinationTO messageDestinationTO = new MessageDestinationTO();
				messageDestinationTO.setMessage(messageTO);
				messageDestinationTO
						.setDestinationType(DestMessageType.PRIVATE);
				messageDestinationTO.setDestinationId(personId);
				getMessageDestinationDAO().create(messageDestinationTO);

			}
			msg.setPreparedState(PreparedMessageState.SENT);
		}

	}

	private void saveMultiProvince(PreparedMessageTO msg,
			List<LocationTO> provinces) throws BaseException {
		if (provinces != null && provinces.size() > 0) {
			MessageTO messageTO = createMessageTO(msg);
			for (LocationTO loc : provinces) {

				MessageDestinationTO messageDestinationTO = new MessageDestinationTO();
				messageDestinationTO.setMessage(messageTO);
				messageDestinationTO
						.setDestinationType(DestMessageType.PROVINCE);
				messageDestinationTO.setDestinationId(loc.getId());
				getMessageDestinationDAO().create(messageDestinationTO);

			}
			msg.setPreparedState(PreparedMessageState.SENT);

		}

	}

	private void saveProvinceAllManager(PreparedMessageTO msg,
			List<LocationTO> provinceList) throws BaseException {
		
		for (LocationTO locationTO : provinceList) {
			
		List<Long> eofListId = getEnrollmentOfficeDAO()
				.getEnrollmentOfficeListIdsByProvince(locationTO.getId());
		MessageTO messageTO = createMessageTO(msg);
		for (Long eofId : eofListId) {
			EnrollmentOfficeTO enrollmentOfficeTO = getEnrollmentOfficeDAO()
					.find(EnrollmentOfficeTO.class, eofId);
			if (enrollmentOfficeTO != null) {
				PersonTO person = enrollmentOfficeTO.getManager();
				if (person != null) {
					Long personId = person.getId();
					MessageDestinationTO messageDestinationTO = new MessageDestinationTO();
					messageDestinationTO.setMessage(messageTO);
					messageDestinationTO
							.setDestinationType(DestMessageType.PRIVATE);
					messageDestinationTO.setDestinationId(personId);
					getMessageDestinationDAO().create(messageDestinationTO);
				}
			}

		}
		
		}
		msg.setPreparedState(PreparedMessageState.SENT);

	}

	private void saveProvinceAllNOCRorOFFICE(PreparedMessageTO msg,
			List<LocationTO> provinceList, EnrollmentOfficeType eofType, Boolean isManager)
					throws BaseException {

		MessageTO messageTO = createMessageTO(msg);

		for (LocationTO locationTO : provinceList) {

			if (isManager) {

				List<Long> eofListId = getEnrollmentOfficeDAO()
						.getEnrollmentOfficeListIdsByProvinceAndType(locationTO.getId(),
								eofType);

				for (Long eofId : eofListId) {
					EnrollmentOfficeTO enrollmentOfficeTO = getEnrollmentOfficeDAO()
							.find(EnrollmentOfficeTO.class, eofId);
					if (enrollmentOfficeTO != null) {
						PersonTO person = enrollmentOfficeTO.getManager();
						if (person != null) {
							Long personId = person.getId();
							MessageDestinationTO messageDestinationTO = new MessageDestinationTO();
							messageDestinationTO.setMessage(messageTO);
							messageDestinationTO
							.setDestinationType(DestMessageType.PRIVATE);
							messageDestinationTO.setDestinationId(personId);
							getMessageDestinationDAO().create(messageDestinationTO);
						} 
					}
				}
			}
			else {
				MessageDestinationTO messageDestinationTO = new MessageDestinationTO();
				messageDestinationTO.setMessage(messageTO);
				messageDestinationTO
				.setDestinationType(DestMessageType.PROVINCE);
				messageDestinationTO.setDestinationId(locationTO.getId());
				getMessageDestinationDAO().create(messageDestinationTO);

			}
		}
		msg.setPreparedState(PreparedMessageState.SENT);


	}

	private void saveAllNOCROrOffice(PreparedMessageTO msg,
			EnrollmentOfficeType eofType) throws BaseException {
		List<Long> eofListId = getEnrollmentOfficeDAO()
				.getEnrollmentOfficeListIdsByEOFType(eofType);
		MessageTO messageTO = createMessageTO(msg);
		for (Long eofId : eofListId) {
			EnrollmentOfficeTO enrollmentOfficeTO = getEnrollmentOfficeDAO()
					.find(EnrollmentOfficeTO.class, eofId);
			if (enrollmentOfficeTO != null) {
				MessageDestinationTO messageDestinationTO = new MessageDestinationTO();
				messageDestinationTO.setMessage(messageTO);
				messageDestinationTO.setDestinationType(DestMessageType.OFFICE);
				messageDestinationTO.setDestinationId(enrollmentOfficeTO
						.getId());
				getMessageDestinationDAO().create(messageDestinationTO);
			}

		}
		msg.setPreparedState(PreparedMessageState.SENT);

	}

	private void saveAllOfficeOrNocrManager(PreparedMessageTO msg,
			EnrollmentOfficeType eofType) throws BaseException {
		List<Long> eofListId = getEnrollmentOfficeDAO()
				.getEnrollmentOfficeListIdsByEOFType(eofType);
		MessageTO messageTO = createMessageTO(msg);
		for (Long eofId : eofListId) {
			EnrollmentOfficeTO enrollmentOfficeTO = getEnrollmentOfficeDAO()
					.find(EnrollmentOfficeTO.class, eofId);
			if (enrollmentOfficeTO != null) {
				PersonTO person = enrollmentOfficeTO.getManager();
				if (person != null) {
					Long personId = person.getId();
					MessageDestinationTO messageDestinationTO = new MessageDestinationTO();
					messageDestinationTO.setMessage(messageTO);
					messageDestinationTO
							.setDestinationType(DestMessageType.PRIVATE);
					messageDestinationTO.setDestinationId(personId);
					getMessageDestinationDAO().create(messageDestinationTO);
				}
			}

		}
		msg.setPreparedState(PreparedMessageState.SENT);

	}

	// Insert for all NOCR and OFFICE
	private void saveAllManager(PreparedMessageTO msg) throws BaseException {

		List<Long> eofListId = getEnrollmentOfficeDAO()
				.getEnrollmentOfficeListIds();
		MessageTO messageTO = createMessageTO(msg);
		for (Long eofId : eofListId) {
			EnrollmentOfficeTO enrollmentOfficeTO = getEnrollmentOfficeDAO()
					.find(EnrollmentOfficeTO.class, eofId);
			if (enrollmentOfficeTO != null) {
				PersonTO person = enrollmentOfficeTO.getManager();
				if (person != null) {
					Long personId = person.getId();
					MessageDestinationTO messageDestinationTO = new MessageDestinationTO();
					messageDestinationTO.setMessage(messageTO);
					messageDestinationTO
							.setDestinationType(DestMessageType.PRIVATE);
					messageDestinationTO.setDestinationId(personId);
					getMessageDestinationDAO().create(messageDestinationTO);
				}
			}

		}
		msg.setPreparedState(PreparedMessageState.SENT);
	}

	private void saveAll(PreparedMessageTO msg) throws BaseException {
		MessageTO messageTO = createMessageTO(msg);
		MessageDestinationTO messageDestinationTO = new MessageDestinationTO();
		messageDestinationTO.setMessage(messageTO);
		messageDestinationTO.setDestinationType(DestMessageType.ALL);
		getMessageDestinationDAO().create(messageDestinationTO);
		msg.setPreparedState(PreparedMessageState.SENT);

	}

	private MessageTO createMessageTO(PreparedMessageTO msg)
			throws BaseException {
		MessageTO messageTO = new MessageTO();
		messageTO.setTitle(msg.getTitle());
		messageTO.setContent(msg.getContent());
		messageTO.setSenderUsername(msg.getSenderUsername());
		messageTO.setAttachFile(msg.getAttachFile());
		messageTO.setHasFile(msg.getHasFile());
		messageTO.setHasFile(msg.getHasFile());
		messageTO.setFileName(msg.getFileName());
		messageTO.setFileType(msg.getFileType());
		messageTO.setCreateDate(msg.getCreateDate());
		MessageTO savedMessage = getMessageDAO().create(messageTO);
		return savedMessage;
	}

	@Override
	public MessageDestinationTO getMessageDestinationById(Long msgId , Long personId)
			throws BaseException {
		MessageDestinationTO messageDestinationTO = getMessageDestinationDAO()
				.find(MessageDestinationTO.class, msgId);
		if (messageDestinationTO != null){
			if (personId!=null && messageDestinationTO.getSeenList().indexOf(
					"@" + personId + "@") == -1) {
				String seenList = messageDestinationTO.getSeenList();
				seenList+="@" + personId + "@";
				messageDestinationTO.setSeenList(seenList);
				getMessageDestinationDAO().update(messageDestinationTO);	
			}
			
			return messageDestinationTO;
			}
		return null;
	}

	@Override
	public void deleteMessageDestinationByPesonId(Long msgId, Long personId)
			throws BaseException {
		MessageDestinationTO messageDestinationTO = getMessageDestinationDAO()
				.find(MessageDestinationTO.class, msgId);
		if (messageDestinationTO != null) {
			String deleteList = messageDestinationTO.getDeleteList();
			deleteList += "@" + personId + "@";
			messageDestinationTO.setDeleteList(deleteList);
			getMessageDestinationDAO().update(messageDestinationTO);
		}

	}

}
