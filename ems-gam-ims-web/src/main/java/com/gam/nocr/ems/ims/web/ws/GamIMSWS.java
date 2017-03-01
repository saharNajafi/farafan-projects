package com.gam.nocr.ems.ims.web.ws;


import est.EstelamResult;
import est.PersonInfo;
import gampooya.tools.BaseException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.WebFault;

import org.slf4j.Logger;

import web.info.TransferInfo;

import com.gam.commons.core.BaseLog;
import com.gam.commons.core.data.dao.factory.DAOFactory;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.dao.IMSBatchEnquiryDAO;
import com.gam.nocr.ems.data.dao.IMSCitizenInfoDAO;
import com.gam.nocr.ems.data.dao.IMSUpdateDAO;
import com.gam.nocr.ems.data.domain.IMSBatchEnquiryTO;
import com.gam.nocr.ems.data.domain.IMSCitizenInfoTO;
import com.gam.nocr.ems.data.domain.IMSUpdateTO;
import com.gam.nocr.ems.data.domain.vol.TransferInfoVTO;
import com.gam.nocr.ems.data.mapper.xmlmapper.XMLMapperProvider;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */

@WebFault(faultBean = "com.gam.nocr.ems.web.ws.InternalException")
@WebService(serviceName = "GamIMSWS", portName = "GamIMSWSPort", targetNamespace = "http://ws.web.ims.ems.nocr.gam.com/")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public class GamIMSWS {

	private static final Logger logger = BaseLog.getLogger(GamIMSWS.class);

	/**
	 * getIMSUpdateDAO
	 */
	private IMSUpdateDAO getIMSUpdateDAO() throws com.gam.commons.core.BaseException {
		DAOFactory daoFactory = DAOFactoryProvider.getDAOFactory();
		IMSUpdateDAO imsUpdateDAO = null;
		try {
			imsUpdateDAO = (IMSUpdateDAO) daoFactory.getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_IMS_UPDATE));
		} catch (DAOFactoryException e) {
            logger.error(e.getMessage(), e);
		}
		return imsUpdateDAO;
	}

	/**
	 * getIMSCitizenInfoDAO
	 */
	private IMSCitizenInfoDAO getCitizenInfoDAO() throws com.gam.commons.core.BaseException {
		DAOFactory daoFactory = DAOFactoryProvider.getDAOFactory();
		IMSCitizenInfoDAO imsCitizenInfoDAO = null;
		try {
			imsCitizenInfoDAO = (IMSCitizenInfoDAO) daoFactory.getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_IMS_CITIZEN_INFO));
		} catch (DAOFactoryException e) {
			logger.error(e.getMessage(), e);
		}
		return imsCitizenInfoDAO;
	}

	/**
	 * getIMSBatchEnquiryDAO
	 */
	private IMSBatchEnquiryDAO getBatchEnquiryDAO() throws com.gam.commons.core.BaseException {
		DAOFactory daoFactory = DAOFactoryProvider.getDAOFactory();
		IMSBatchEnquiryDAO imsBatchEnquiryDAO = null;
		try {
			imsBatchEnquiryDAO = (IMSBatchEnquiryDAO) daoFactory.getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_IMS_BATCH_ENQUIRY));
		} catch (DAOFactoryException e) {
			logger.error(e.getMessage(), e);
		}
		return imsBatchEnquiryDAO;
	}

	private TransferInfo convertToTransferInfo(TransferInfoVTO transferInfoVTO) throws com.gam.commons.core.BaseException {
		try {
			TransferInfo transferInfo = new TransferInfo();
			transferInfo.setFilename(transferInfoVTO.getRequestId());
			transferInfo.setData(transferInfoVTO.getData());
			transferInfo.setIndex(0);
			return transferInfo;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@WebMethod
	public String upload(
			@WebParam(name = "username") String username,
			@WebParam(name = "password") String password,
			@WebParam(name = "transferInfo") TransferInfo transferInfo) throws BaseException {
		if (transferInfo == null) {
			logger.info("The sent instance of type TransferInfo was null.");
		} else {
			String okValue = "OK";
			String notOkValue = "Not Ok";
			try {
				String strData = EmsUtil.convertByteArrayToString(transferInfo.getData());
				String[] strDataSplitByEnter = strData.split("\n");
				String enquiryInfo = "";
				for (String eachRow : strDataSplitByEnter) {
					String eachRowId = eachRow.split(",")[0];
					enquiryInfo += eachRowId + "," + okValue + "\n";
				}
				if (EmsUtil.checkString(enquiryInfo)) {
					IMSBatchEnquiryTO imsBatchEnquiryTO = new IMSBatchEnquiryTO();
					imsBatchEnquiryTO.setEnquiryInfo(enquiryInfo);
					imsBatchEnquiryTO.setReplyFlag(false);
					getBatchEnquiryDAO().create(imsBatchEnquiryTO);
				}
			} catch (com.gam.commons.core.BaseException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return "The request has been received successfully.";
	}

	@WebMethod
	public TransferInfo download(
			@WebParam(name = "username") String username,
			@WebParam(name = "password") String password) throws BaseException {
		try {
			IMSBatchEnquiryTO imsBatchEnquiryTO = getBatchEnquiryDAO().findByReplyFlag(false);
			imsBatchEnquiryTO.setReplyFlag(true);
			getBatchEnquiryDAO().update(imsBatchEnquiryTO);
			TransferInfo transferInfo = new TransferInfo();
			transferInfo.setData(imsBatchEnquiryTO.getEnquiryInfo().getBytes());
			return transferInfo;
		} catch (com.gam.commons.core.BaseException e) {
            logger.error(e.getMessage(), e);
			return null;
		}
	}

	@WebMethod
	public TransferInfo updateCitizenInfo(
			@WebParam(name = "username") String username,
			@WebParam(name = "password") String password,
			@WebParam(name = "transferInfo") TransferInfo transferInfo) {

		try {
			String xmlData = EmsUtil.convertByteArrayToString(transferInfo.getData());
			logger.info("\n\nThe received xml request for update citizenInfo is: \n" + xmlData + "\n\n");
			XMLMapperProvider xmlMapperProvider = new XMLMapperProvider();
			IMSUpdateTO imsUpdateTO = (IMSUpdateTO) xmlMapperProvider.readXML(xmlData, new IMSUpdateTO());
			getIMSUpdateDAO().create(imsUpdateTO);

		} catch (com.gam.commons.core.BaseException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return transferInfo;
	}

	@WebMethod
	public TransferInfo getUpdatedCitizensResult(
			@WebParam(name = "username") String username,
			@WebParam(name = "password") String password,
			@WebParam(name = "imsRequestId") String imsRequestId) {

		TransferInfo transferInfo = null;
		try {
			IMSUpdateTO imsUpdateTO = getIMSUpdateDAO().findByRequestId(imsRequestId);
			imsUpdateTO.setState(IMSUpdateTO.State.PROCESSED);
			getIMSUpdateDAO().update(imsUpdateTO);
			if (imsUpdateTO != null) {
				XMLMapperProvider xmlMapperProvider = new XMLMapperProvider();
				byte[] byteArray = xmlMapperProvider.writeXML(imsUpdateTO, null);
				transferInfo = new TransferInfo();
				transferInfo.setFilename(imsRequestId);
				transferInfo.setData(byteArray);
			}
		} catch (com.gam.commons.core.BaseException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return transferInfo;
	}

	@WebMethod
	public boolean setCitizenCardRequested(
			@WebParam(name = "username") String username,
			@WebParam(name = "password") String password,
			@WebParam(name = "nationalId") String nationalId) {
		return true;
	}

	@WebMethod
	public boolean setCitizenCardDelivered(
			@WebParam(name = "username") String username,
			@WebParam(name = "password") String password,
			@WebParam(name = "nationalId") String nationalId) {
		return true;
	}

	@WebMethod
	public TransferInfo fetchCitizenInfo(
			@WebParam(name = "username") String username,
			@WebParam(name = "password") String password,
			@WebParam(name = "nationalId") String nationalId) {
		TransferInfo transferInfo = null;
		try {
			IMSUpdateTO imsUpdateTO = getIMSUpdateDAO().findByNationalId(nationalId);
			transferInfo = new TransferInfo();
			transferInfo.setFilename(imsUpdateTO.getRequestId());
			transferInfo.setData(imsUpdateTO.getCitizenData());
		} catch (com.gam.commons.core.BaseException e) {
			logger.error(e.getMessage(), e);
		} catch(Exception e){
			logger.error(e.getMessage(), e);
		}

		return transferInfo;
	}

	@WebMethod
	public EstelamResult[] getEstelam2(
			@WebParam(name = "username") String username,
			@WebParam(name = "password") String password,
			@WebParam(name = "keyhanUsername") String keyhanUsername,
			@WebParam(name = "keyhanSerial") String keyhanSerial,
			@WebParam(name = "personInfo") PersonInfo personInfo) {

		EstelamResult[] estelamResults = null;
		try {
			IMSCitizenInfoTO imsCitizenInfoTO = getCitizenInfoDAO().findByNationalId(personInfo.getNin());
			if (imsCitizenInfoTO != null) {
				estelamResults = new EstelamResult[1];
				estelamResults[0] = new EstelamResult();
				estelamResults[0].setBirthDate(imsCitizenInfoTO.getBirthDate());
				estelamResults[0].setGender(imsCitizenInfoTO.getGender());
				estelamResults[0].setShenasnameNo(imsCitizenInfoTO.getBirthCertificateId());
				estelamResults[0].setShenasnameSerial(imsCitizenInfoTO.getBirthCertificateSerial());
				estelamResults[0].setNin(imsCitizenInfoTO.getNationalId());
				estelamResults[0].setName(imsCitizenInfoTO.getFirstName().getBytes("UTF-8"));
				estelamResults[0].setFamily(imsCitizenInfoTO.getSurName().getBytes("UTF-8"));
				estelamResults[0].setFatherName(imsCitizenInfoTO.getFatherName().getBytes(("UTF-8")));
			}
		} catch (com.gam.commons.core.BaseException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return estelamResults;
	}

}
