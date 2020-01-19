package com.gam.nocr.ems.biz.service.internal.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.BizLoggable;
import com.gam.commons.core.biz.service.Permissions;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.biz.service.factory.ServiceFactory;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.dao.factory.DAOFactory;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.service.*;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.dao.CertificateDAO;
import com.gam.nocr.ems.data.dao.EnrollmentOfficeDAO;
import com.gam.nocr.ems.data.dao.PersonDAO;
import com.gam.nocr.ems.data.dao.PersonTokenDAO;
import com.gam.nocr.ems.data.domain.*;
import com.gam.nocr.ems.data.domain.vol.PersonTokenVTO;
import com.gam.nocr.ems.data.enums.*;
import com.gam.nocr.ems.data.mapper.tomapper.PersonTokenMapper;
import com.gam.nocr.ems.util.EmsUtil;
import org.slf4j.Logger;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.gam.nocr.ems.config.EMSLogicalNames.getExternalServiceJNDIName;
import static com.gam.nocr.ems.config.EMSLogicalNames.getServiceJNDIName;
//Adldoost
//import com.gam.nocr.ems.data.dao.NetworkTokenDAO;
//Adldoost
//import com.gam.nocr.ems.data.domain.NetworkTokenTO;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */

@Stateless(name = "TokenManagementService")
@Local(TokenManagementServiceLocal.class)
@Remote(TokenManagementServiceRemote.class)
public class TokenManagementServiceImpl extends EMSAbstractService implements
        TokenManagementServiceLocal, TokenManagementServiceRemote {

    private static final Logger logger = BaseLog
            .getLogger(TokenManagementServiceImpl.class);

    /**
     * =============== Private Methods ===============
     */

    /**
     * The method doRevocation is used to act as an inner helping method for the
     * process 'token revocation'.
     */
    private void doRevocation(TokenTO tokenTO, TokenType tokenType,
                              String comment) throws BaseException {
        CertificateTO certificateTO = findCertificateByUsage(CertificateUsage.PKI_SIGN);
        if (tokenTO.getAKI() == null
                || tokenTO.getCertificateSerialNumber() == null) {
            tokenTO = getPKIService().searchTokenRequest(tokenTO, tokenType,
                    certificateTO);
        }
        getPKIService().revokeTokenRequest(tokenTO, tokenType, certificateTO,
                comment);
    }

    // Adldoost
    private void doActivation(TokenTO tokenTO, TokenType tokenType)
            throws BaseException {
        CertificateTO certificateTO = findCertificateByUsage(CertificateUsage.PKI_SIGN);
        if (tokenTO.getAKI() == null
                || tokenTO.getCertificateSerialNumber() == null) {
            tokenTO = getPKIService().searchTokenRequest(tokenTO, tokenType,
                    certificateTO);
        }
        getPKIService().activatePersonToken((PersonTokenTO) tokenTO,
                certificateTO);
    }

    /**
     * =============== Getter for DAOs ===============
     */

    /**
     * getPersonTokenDAO
     *
     * @return an instance of type PersonTokenDAO
     */
    private PersonTokenDAO getPersonTokenDAO() throws BaseException {
        DAOFactory factory = DAOFactoryProvider.getDAOFactory();
        PersonTokenDAO personTokenDAO = null;
        try {
            personTokenDAO = (PersonTokenDAO) factory.getDAO(EMSLogicalNames
                    .getDaoJNDIName(EMSLogicalNames.DAO_PERSON_TOKEN));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.TMS_001,
                    BizExceptionCode.GLB_001_MSG, e,
                    EMSLogicalNames.DAO_PERSON_TOKEN.split(","));
        }
        return personTokenDAO;
    }

    /**
     * getNetworkTokenDAO
     *
     * @return an instance of type NetworkTokenDAO
     */
    // Commented By Adldoost
    // private NetworkTokenDAO getNetworkTokenDAO() throws BaseException {
    // DAOFactory factory = DAOFactoryProvider.getDAOFactory();
    // NetworkTokenDAO networkTokenDAO = null;
    // try {
    // networkTokenDAO = (NetworkTokenDAO)
    // factory.getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_NETWORK_TOKEN));
    // } catch (DAOFactoryException e) {
    // throw new ServiceException(
    // BizExceptionCode.TMS_002,
    // BizExceptionCode.GLB_001_MSG,
    // e,
    // EMSLogicalNames.DAO_NETWORK_TOKEN.split(","));
    // }
    // return networkTokenDAO;
    // }

    /**
     * getPersonDAO
     *
     * @return an instance of type NetworkTokenDAO
     */
    private PersonDAO getPersonDAO() throws BaseException {
        DAOFactory factory = DAOFactoryProvider.getDAOFactory();
        PersonDAO personDAO = null;
        try {
            personDAO = (PersonDAO) factory.getDAO(EMSLogicalNames
                    .getDaoJNDIName(EMSLogicalNames.DAO_PERSON));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.TMS_003,
                    BizExceptionCode.GLB_001_MSG, e,
                    EMSLogicalNames.DAO_PERSON.split(","));
        }
        return personDAO;
    }

    /**
     * getEnrollmentOfficeDAO
     *
     * @return an instance of type EnrollmentOfficeDAO
     */
    private EnrollmentOfficeDAO getEnrollmentOfficeDAO() throws BaseException {
        DAOFactory factory = DAOFactoryProvider.getDAOFactory();
        EnrollmentOfficeDAO enrollmentOfficeDAO = null;
        try {
            enrollmentOfficeDAO = (EnrollmentOfficeDAO) factory
                    .getDAO(EMSLogicalNames
                            .getDaoJNDIName(EMSLogicalNames.DAO_ENROLLMENT_OFFICE));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.TMS_004,
                    BizExceptionCode.GLB_001_MSG, e,
                    EMSLogicalNames.DAO_ENROLLMENT_OFFICE.split(","));
        }
        return enrollmentOfficeDAO;
    }

    /**
     * getCertificateDAO
     *
     * @return an instance of type CertificateDAO
     */
    private CertificateDAO getCertificateDAO() throws BaseException {
        DAOFactory factory = DAOFactoryProvider.getDAOFactory();
        CertificateDAO certificateDAO = null;
        try {
            certificateDAO = (CertificateDAO) factory.getDAO(EMSLogicalNames
                    .getDaoJNDIName(EMSLogicalNames.DAO_CERTIFICATE));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.TMS_006,
                    BizExceptionCode.GLB_001_MSG, e,
                    EMSLogicalNames.DAO_CERTIFICATE.split(","));
        }
        return certificateDAO;
    }

    /**
     * =================== Getter for services ===================
     */

    /**
     * getPKIService
     *
     * @return an instance of type PKIService
     */
    private PKIService getPKIService() throws BaseException {
        ServiceFactory factory = ServiceFactoryProvider.getServiceFactory();
        PKIService pkiService = null;
        try {
            pkiService = (PKIService) factory.getService(EMSLogicalNames
                    .getExternalServiceJNDIName(EMSLogicalNames.SRV_PKI), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(BizExceptionCode.TMS_005,
                    BizExceptionCode.GLB_001_MSG, e,
                    EMSLogicalNames.SRV_PKI.split(","));
        }
        pkiService.setUserProfileTO(getUserProfileTO());
        return pkiService;
    }

    /**
     * getGaasService
     *
     * @return an instance of type
     * {@link com.gam.nocr.ems.biz.service.GAASService}
     */
    private GAASService getGaasService() throws BaseException {
        GAASService gaasService = null;
        try {
            gaasService = ServiceFactoryProvider
                    .getServiceFactory()
                    .getService(
                            getExternalServiceJNDIName(EMSLogicalNames.SRV_GAAS), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(BizExceptionCode.TMS_029,
                    BizExceptionCode.GLB_002_MSG, e,
                    new String[]{EMSLogicalNames.SRV_GAAS});
        }
        gaasService.setUserProfileTO(getUserProfileTO());
        return gaasService;
    }

    private BusinessLogService getBusinessLogService() throws BaseException {
        BusinessLogService businessLogService;
        try {
            businessLogService = ServiceFactoryProvider
                    .getServiceFactory()
                    .getService(
                            getServiceJNDIName(EMSLogicalNames.SRV_BUSINESS_LOG), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(BizExceptionCode.TMS_052,
                    BizExceptionCode.GLB_002_MSG, e,
                    new String[]{EMSLogicalNames.SRV_BUSINESS_LOG});
        }
        businessLogService.setUserProfileTO(getUserProfileTO());
        return businessLogService;
    }

    /**
     * The method findAndRevokeDeliveredPersonTokenWithSameType is used to find
     * out a delivered token which the type is as same as the type of new token
     */
    private void findAndRevokeDeliveredPersonTokenWithSameType(
            PersonTokenTO personTokenTO) throws BaseException {
        PersonTokenTO tempPersonTokenTO = null;
        tempPersonTokenTO = findDeliveredPersonTokenWithSameType(personTokenTO);

        logger.error(DataExceptionCode.PTI_007_MSG);
        if (tempPersonTokenTO != null) {
            revokePersonToken(tempPersonTokenTO.getId(),
                    ReplicaReason.UNSPECIFIED, "");
            tempPersonTokenTO.setState(TokenState.REVOKED);
            getPersonTokenDAO().update(tempPersonTokenTO);
        }
    }

    /**
     * The method findAndRevokeDeliveredNetworkTokenWithSameType is used to find
     * out a delivered token which the type is as same as the type of new token
     */
    // Commented By Adldoost
    // private void
    // findAndRevokeDeliveredNetworkTokenWithSameType(NetworkTokenTO
    // networkTokenTO) throws BaseException {
    //
    // NetworkTokenTO tempNetworkTokenTO = null;
    // try {
    // tempNetworkTokenTO =
    // findDeliveredNetworkTokenWithSameType(networkTokenTO);
    // } catch (DataException e) {
    // logger.error(e.getMessage(), e);
    // if (DataExceptionCode.NTI_005.equals(e.getExceptionCode())) {
    // // TODO : In this circumstances do nothing
    // }
    // }
    // if (tempNetworkTokenTO != null) {
    // revokeNetworkToken(tempNetworkTokenTO.getId(), ReplicaReason.UNSPECIFIED,
    // "");
    // tempNetworkTokenTO.setState(TokenState.REVOKED);
    // getNetworkTokenDAO().update(tempNetworkTokenTO);
    // }
    //
    // }
    private PersonTokenTO findDeliveredPersonTokenWithSameType(
            PersonTokenTO personTokenTO) throws BaseException {
        return getPersonTokenDAO().findDeliveredByPersonIdAndType(
                personTokenTO.getPerson().getId(), personTokenTO.getType());
    }

    private List<PersonTokenTO> findRnewnalPersonTokenRequestForSamePerson(
            PersonTokenTO personTokenTO) throws BaseException {
        List<TokenState> stateList = new ArrayList<TokenState>();
        stateList.add(TokenState.PENDING_TO_RENEWAL_ISSUE);
        stateList.add(TokenState.READY_TO_RENEWAL_DELIVER);
        return getPersonTokenDAO().findByPersonIdAndTypeAndState(
                personTokenTO.getPerson().getId(), TokenType.SIGNATURE,
                stateList);
    }

    // Adldoost
    public PersonTokenTO findDeliveredPersonTokenByTypeAndPerson(
            PersonTO personTO, TokenType tokenType) throws BaseException {
        return getPersonTokenDAO().findDeliveredByPersonIdAndType(
                personTO.getId(), tokenType);
    }

    // Commented By Adldoost
    // private NetworkTokenTO
    // findDeliveredNetworkTokenWithSameType(NetworkTokenTO networkTokenTO)
    // throws BaseException {
    // return
    // getNetworkTokenDAO().findDeliveredByEnrollmentOfficeIdAndType(networkTokenTO.getEnrollmentOffice());
    // }

    /**
     * The method revokeAndUpdatePersonTokenTO is used to revoke the
     * personTokenTO and update the database
     */

    private void revokeAndUpdatePersonTokenTO(PersonTokenTO personTokenTO,
                                              String comment) throws BaseException {
        if (personTokenTO.getRequestID() != null) {

            //Edited by Madanipour to allow change token state to revoked in case of that token is revoked or expired in PKI
            try {

                doRevocation(personTokenTO, personTokenTO.getType(), comment);
            } catch (BaseException e) {
                if ("EMS_S_PKI_00400090".equals(e.getExceptionCode()) || "EMS_S_PKI_00400110".equals(e.getExceptionCode())) {

                    personTokenTO.setState(TokenState.REVOKED);
                    getPersonTokenDAO().update(personTokenTO);

                } else {

                    throw e;
                }
            }
            personTokenTO.setState(TokenState.REVOKED);
            getPersonTokenDAO().update(personTokenTO);

        } else {
            getPersonTokenDAO().delete(personTokenTO);
        }
    }

    /**
     * The method revokeAndUpdateNetworkTokenTO is used to revoke the
     * networkTokenTO and update the database
     */
    // Commented By Adldoost
    // private void revokeAndUpdateNetworkTokenTO(NetworkTokenTO networkTokenTO,
    // String comment) throws BaseException {
    // if (networkTokenTO.getRequestID() != null) {
    // doRevocation(networkTokenTO, TokenType.NETWORK, comment);
    // networkTokenTO.setState(TokenState.REVOKED);
    // getNetworkTokenDAO().update(networkTokenTO);
    // } else {
    // getNetworkTokenDAO().delete(networkTokenTO);
    // }
    // }
    public void createBusinessLog(BusinessLogAction logAction,
                                  BusinessLogEntity logEntityName, String logActor,
                                  String additionalData, Boolean exceptionFlag) throws BaseException {
        BusinessLogTO businessLogTO = new BusinessLogTO();
        businessLogTO.setEntityID(" ");
        businessLogTO.setAction(logAction);
        businessLogTO.setEntityName(logEntityName);
        businessLogTO.setActor(logActor);
        businessLogTO.setAdditionalData(additionalData);
        businessLogTO.setDate(new Timestamp(new Date().getTime()));
        if (exceptionFlag) {
            businessLogTO.setActionAttitude(BusinessLogActionAttitude.F);
        } else {
            businessLogTO.setActionAttitude(BusinessLogActionAttitude.T);
        }
        getBusinessLogService().insertLog(businessLogTO);

    }

    /**
     * The method issueFirstPersonToken is used to save an object of type ${link
     * PersonTokenTO} as the first token
     *
     * @param entityId  is the id of an instance of type
     *                  {@link com.gam.nocr.ems.data.domain.PersonTO}
     * @param tokenType ia an enumeration of type
     *                  {@link com.gam.nocr.ems.data.enums.TokenType}, which
     *                  demonstrates the type of Token
     * @return A number of type long which represents the identification of the
     * object of type {@link com.gam.nocr.ems.data.domain.PersonTokenTO}
     * which was recorded on database
     */
    @Override
    @BizLoggable(logAction = "INSERT", logEntityName = "PERSON_TOKEN")
    public long issueFirstPersonToken(long entityId, TokenType tokenType)
            throws BaseException {
        logger.info("The process 'issueFirstPersonToken' is started...");
        PersonTokenTO personTokenTO = new PersonTokenTO();
        PersonTO personTO = getPersonDAO().find(PersonTO.class, entityId);
        if (personTO == null) {
            throw new ServiceException(BizExceptionCode.TMS_010,
                    BizExceptionCode.TMS_010_MSG);
        }
        personTokenTO.setPerson(personTO);
        personTokenTO.setState(TokenState.READY_TO_ISSUE);
        personTokenTO.setPerson(personTO);
        personTokenTO.setType(tokenType);
        personTokenTO.setRequestDate(new Date());

        personTokenTO.setPtReason(TokenReason.FIRST_TOKEN);
        personTokenTO = getPersonTokenDAO().create(personTokenTO);

        logger.info("The process 'issueFirstPersonToken' is Finished.");

        return personTokenTO.getId();
    }

    /**
     * The method issueFirstNetworkToken is used to save an object of type
     * ${link NetworkTokenTO} as the first token
     *
     * @param entityId
     *            is the id of an instance of type
     *            {@link com.gam.nocr.ems.data.domain.EnrollmentOfficeTO}
     * @param tokenType
     *            ia an enumeration of type
     *            {@link com.gam.nocr.ems.data.enums.TokenType}, which
     *            demonstrates the type of Token
     * @return A number of type long which represents the identification of the
     *         object of type
     *         {@link com.gam.nocr.ems.data.domain.NetworkTokenTO} which was
     *         recorded on database
     */
    // Commented By Adldoost
    // @Override
    // @BizLoggable(logAction = "INSERT", logEntityName = "NETWORK_TOKEN")
    // public long issueFirstNetworkToken(long entityId,
    // TokenType tokenType) throws BaseException {
    // logger.info("The process 'issueFirstNetworkToken' is started...");
    // EnrollmentOfficeTO enrollmentOfficeTO =
    // getEnrollmentOfficeDAO().find(EnrollmentOfficeTO.class, entityId);
    // if (enrollmentOfficeTO == null) {
    // throw new ServiceException(BizExceptionCode.TMS_011,
    // BizExceptionCode.TMS_011_MSG);
    // }
    // if (EnrollmentOfficeType.NOCR.equals(enrollmentOfficeTO.getType())) {
    // throw new ServiceException(BizExceptionCode.TMS_057,
    // BizExceptionCode.TMS_057_MSG);
    // }
    // NetworkTokenTO networkTokenTO = new NetworkTokenTO();
    // networkTokenTO.setEnrollmentOffice(enrollmentOfficeTO);
    // networkTokenTO.setState(TokenState.READY_TO_ISSUE);
    // networkTokenTO.setRequestDate(new Date());
    // networkTokenTO = getNetworkTokenDAO().create(networkTokenTO);
    // logger.info("The process 'issueFirstNetworkToken' is Finished.");
    // return networkTokenTO.getId();
    // }

    /**
     * @param tokenId represents the id of previous token
     * @return A number of type long which represents the identification of the
     * object of type {@link com.gam.nocr.ems.data.domain.PersonTokenTO}
     * which was recorded on database
     */
    @Override
    @BizLoggable(logAction = "REISSUE", logEntityName = "PERSON_TOKEN")
    public Long reIssuePersonTokenRequest(Long tokenId) throws BaseException {
        revokePersonToken(tokenId, ReplicaReason.UNSPECIFIED, "");

        PersonTokenTO oldPersonTokenTO = getPersonTokenDAO().find(
                PersonTokenTO.class, tokenId);
        if (oldPersonTokenTO == null) {
            throw new ServiceException(BizExceptionCode.TMS_019,
                    BizExceptionCode.TMS_018_MSG);
        }
        // TODO : if the config service return true then
        // TODO : Find a valid range of time for reissue request
        PersonTokenTO newPersonTokenTO = new PersonTokenTO();
        newPersonTokenTO.setPerson(oldPersonTokenTO.getPerson());
        newPersonTokenTO.setType(oldPersonTokenTO.getType());
        newPersonTokenTO.setState(TokenState.READY_TO_ISSUE);
        newPersonTokenTO.setRequestDate(new Date());
        newPersonTokenTO.setPtReason(TokenReason.REPLICA);
        newPersonTokenTO = getPersonTokenDAO().create(newPersonTokenTO);

        return newPersonTokenTO.getId();
    }

    // Adldoost
    @Override
    // @BizLoggable(logAction = "RENEWAL", logEntityName = "PERSON_TOKEN")
    public Long renewalPersonTokenRequest(Long tokenId) throws BaseException {
        PersonTokenTO oldPersonTokenTO = getPersonTokenDAO().find(
                PersonTokenTO.class, tokenId);
        if (oldPersonTokenTO == null) {
            throw new ServiceException(BizExceptionCode.TMS_059,
                    BizExceptionCode.TMS_018_MSG);
        }
        // TODO : if the config service return true then
        // TODO : Find a valid range of time for reissue request
        PersonTokenTO newPersonTokenTO = new PersonTokenTO();
        newPersonTokenTO.setPerson(oldPersonTokenTO.getPerson());
        newPersonTokenTO.setType(oldPersonTokenTO.getType());
        newPersonTokenTO.setState(TokenState.PENDING_FOR_EMS);
        newPersonTokenTO.setRequestDate(new Date());
        newPersonTokenTO.setPtReason(TokenReason.REPLICA);
        newPersonTokenTO = getPersonTokenDAO().create(newPersonTokenTO);

        return newPersonTokenTO.getId();
    }

    /**
     * @param tokenId
     *            represents the id of previous token
     * @return A number of type long which represents the identification of the
     *         object of type
     *         {@link com.gam.nocr.ems.data.domain.NetworkTokenTO} which was
     *         recorded on database
     */
    // Commented By Adldoost
    // @Override
    // @BizLoggable(logAction = "REISSUE", logEntityName = "NETWORK_TOKEN")
    // public Long reIssueNetworkTokenRequest(Long tokenId) throws BaseException
    // {
    // revokeNetworkToken(tokenId, ReplicaReason.UNSPECIFIED, "");
    // NetworkTokenTO oldNetworkTokenTO =
    // getNetworkTokenDAO().find(NetworkTokenTO.class, tokenId);
    // if (oldNetworkTokenTO == null) {
    // throw new ServiceException(BizExceptionCode.TMS_020,
    // BizExceptionCode.TMS_018_MSG);
    // }
    // // TODO : if the config service return true then
    // // TODO : Find a valid range of time for reissue request
    // if
    // (EnrollmentOfficeType.OFFICE.equals(oldNetworkTokenTO.getEnrollmentOffice().getType()))
    // {
    // NetworkTokenTO newNetworkTokenTO = new NetworkTokenTO();
    // newNetworkTokenTO.setEnrollmentOffice(oldNetworkTokenTO.getEnrollmentOffice());
    // newNetworkTokenTO.setState(TokenState.READY_TO_ISSUE);
    // newNetworkTokenTO.setRequestDate(new Date());
    // newNetworkTokenTO = getNetworkTokenDAO().create(newNetworkTokenTO);
    // return newNetworkTokenTO.getId();
    // } else {
    // logger.info(BizExceptionCode.TMS_057_MSG);
    // return null;
    // }
    // }

    /**
     * The method replicatePersonToken is used to handle the request of
     * replicating for a token for a specified person.
     *
     * @param entityId  is the id of an instance of type
     *                  {@link com.gam.nocr.ems.data.domain.PersonTO}
     * @param tokenType represents the id of previous token
     * @param reason    represents the reason of replication which is an enum type of
     *                  {@link com.gam.nocr.ems.data.enums.ReplicaReason}
     * @return A number of type long which represents the identification of the
     * object of type {@link com.gam.nocr.ems.data.domain.PersonTokenTO}
     * which was recorded on database
     */
    @Override
    @BizLoggable(logAction = "REPLICATE", logEntityName = "PERSON_TOKEN")
    public Long replicatePersonToken(Long entityId, TokenType tokenType,
                                     ReplicaReason reason) throws BaseException {
        PersonTokenTO oldPersonTokenTO = getPersonTokenDAO()
                .findByPersonIdAndTypeWhereNotRevoked(entityId, tokenType);
        if (oldPersonTokenTO == null) {
            throw new ServiceException(BizExceptionCode.TMS_021,
                    BizExceptionCode.TMS_021_MSG);
        }

        if (oldPersonTokenTO.getState() != TokenState.REVOKED)
            revokeAndUpdatePersonTokenTO(oldPersonTokenTO, "");

        PersonTokenTO newPersonTokenTO = new PersonTokenTO();
        newPersonTokenTO.setPerson(oldPersonTokenTO.getPerson());
        newPersonTokenTO.setType(oldPersonTokenTO.getType());
        newPersonTokenTO.setState(TokenState.READY_TO_ISSUE);
        newPersonTokenTO.setRequestDate(new Date());
        if (reason == ReplicaReason.DAMAGE) {
            newPersonTokenTO.setPtReason(TokenReason.REPLACED);
        } else {
            newPersonTokenTO.setPtReason(TokenReason.REPLICA);
        }
        newPersonTokenTO = getPersonTokenDAO().create(newPersonTokenTO);
        return newPersonTokenTO.getId();
    }

    /**
     * The method replicateNetworkToken is used to handle the request of
     * replicating for a token of type NETWORK.
     *
     * @param entityId
     *            is the id of an instance of type
     *            {@link com.gam.nocr.ems.data.domain.EnrollmentOfficeTO}
     * @param tokenType
     *            represents the type of previous token
     * @param reason
     *            represents the reason of replication which is an enum type of
     *            {@link com.gam.nocr.ems.data.enums.ReplicaReason}
     * @return A number of type long which represents the identification of the
     *         object of type
     *         {@link com.gam.nocr.ems.data.domain.NetworkTokenTO} which was
     *         recorded on database
     */

    // Commented By Adldoost
    // @Override
    // @BizLoggable(logAction = "REPLICATE", logEntityName = "NETWORK_TOKEN")
    // public Long replicateNetworkToken(Long entityId,
    // TokenType tokenType,
    // ReplicaReason reason) throws BaseException {
    // NetworkTokenTO oldNetworkTokenTO =
    // getNetworkTokenDAO().findByEnrollmentIdAndTypeWhereNotRevoked(entityId,
    // tokenType);
    // if (oldNetworkTokenTO == null) {
    // throw new ServiceException(BizExceptionCode.TMS_022,
    // BizExceptionCode.TMS_022_MSG);
    // }
    //
    // revokeAndUpdateNetworkTokenTO(oldNetworkTokenTO, "");
    //
    // if
    // (EnrollmentOfficeType.OFFICE.equals(oldNetworkTokenTO.getEnrollmentOffice().getType()))
    // {
    // NetworkTokenTO newNetworkTokenTO = new NetworkTokenTO();
    // newNetworkTokenTO.setEnrollmentOffice(oldNetworkTokenTO.getEnrollmentOffice());
    // newNetworkTokenTO.setState(TokenState.READY_TO_ISSUE);
    // newNetworkTokenTO.setRequestDate(new Date());
    // newNetworkTokenTO = getNetworkTokenDAO().create(newNetworkTokenTO);
    // return newNetworkTokenTO.getId();
    // } else {
    // logger.info(BizExceptionCode.TMS_057_MSG);
    // return null;
    // }
    // }

    /**
     * The method revokePersonToken is used to handle the request of revocation
     * for any types of Person token.
     *
     * @param tokenId represents the id of the token which should be revoked
     * @param reason  represents the reason of revocation which is an instance of
     *                type {@link String}
     * @param comment
     */
    @Override
    @BizLoggable(logAction = "REVOKE", logEntityName = "PERSON_TOKEN")
    public void revokePersonToken(long tokenId, ReplicaReason reason,
                                  String comment) throws BaseException {
        // TODO : this load should be refactored, it is redundant if call from
        // replica and deliver
        PersonTokenTO oldPersonTokenTO = getPersonTokenDAO().find(
                PersonTokenTO.class, tokenId);

        if (oldPersonTokenTO != null) {
            revokeAndUpdatePersonTokenTO(oldPersonTokenTO, comment);
        } else
            throw new ServiceException(BizExceptionCode.TMS_018,
                    BizExceptionCode.TMS_018_MSG);
    }

    /**
     * The method revokePersonToken is used to handle the request of revocation
     * for any types of Person token.
     *
     * @param tokenId
     *            represents the id of the token which should be revoked
     * @param reason
     *            represents the reason of revocation which is an instance of
     *            type {@link String}
     */
    // Commented By Adldoost
    // @Override
    // @BizLoggable(logAction = "REVOKE", logEntityName = "NETWORK_TOKEN")
    // public void revokeNetworkToken(long tokenId,
    // ReplicaReason reason,
    // String comment) throws BaseException {
    // // TODO : this load should be refactored, it is redundant if call from
    // replica and deliver
    // NetworkTokenTO oldNetworkTokenTO =
    // getNetworkTokenDAO().find(NetworkTokenTO.class, tokenId);
    // if (oldNetworkTokenTO != null) {
    // revokeAndUpdateNetworkTokenTO(oldNetworkTokenTO, comment);
    // } else {
    // throw new ServiceException(BizExceptionCode.TMS_023,
    // BizExceptionCode.TMS_018_MSG);
    // }
    // }

    /**
     * The method deliverPersonToken is used to handle the request of delivering
     * for changing the state of the person token.
     *
     * @param tokenId represents the id of the specified token
     */
    @Override
    @BizLoggable(logAction = "DELIVER", logEntityName = "PERSON_TOKEN")
    public void deliverPersonToken(Long tokenId) throws BaseException {
        PersonTokenTO personTokenTO = getPersonTokenDAO().find(
                PersonTokenTO.class, tokenId);
        if (personTokenTO == null) {
            throw new ServiceException(BizExceptionCode.TMS_024,
                    BizExceptionCode.TMS_018_MSG);
        }
        PersonTO personTO = personTokenTO.getPerson();
        if (personTO == null) {
            throw new ServiceException(BizExceptionCode.TMS_030,
                    BizExceptionCode.TMS_030_MSG,
                    new Long[]{personTokenTO.getId()});
        }

        if (TokenState.READY_TO_DELIVER.equals(personTokenTO.getState())) {

            if (TokenType.AUTHENTICATION.equals(personTokenTO.getType())) {
                try {
                    getGaasService().enableUser(personTO.getUserId());
                } catch (BaseException e) {
                    logger.warn(BizExceptionCode.TMS_031_MSG,
                            new Long[]{personTokenTO.getPerson().getId()}, e);
                }
            }

            findAndRevokeDeliveredPersonTokenWithSameType(personTokenTO);
            doActivation(personTokenTO, personTokenTO.getType());
            personTokenTO.setState(TokenState.DELIVERED);
            getPersonTokenDAO().update(personTokenTO);

        } else {
            throw new ServiceException(BizExceptionCode.TMS_017,
                    BizExceptionCode.TMS_017_MSG);
        }
    }

    /**
     * The method deliverNetworkToken is used to handle the request of
     * delivering for changing the state of the token of type NETWORK.
     *
     * @param tokenId
     *            represents the id of the specified token
     */
    // Commented By Adldoost
    // @Override
    // @BizLoggable(logAction = "DELIVER", logEntityName = "NETWORK_TOKEN")
    // public void deliverNetworkToken(Long tokenId) throws BaseException {
    // NetworkTokenTO networkTokenTO =
    // getNetworkTokenDAO().find(NetworkTokenTO.class, tokenId);
    // if (networkTokenTO == null) {
    // throw new ServiceException(BizExceptionCode.TMS_025,
    // BizExceptionCode.TMS_018_MSG);
    // }
    //
    // EnrollmentOfficeTO enrollmentOfficeTO =
    // networkTokenTO.getEnrollmentOffice();
    // if (enrollmentOfficeTO == null) {
    // throw new ServiceException(BizExceptionCode.TMS_032,
    // BizExceptionCode.TMS_032_MSG, new Long[]{networkTokenTO.getId()});
    // }
    //
    // if (TokenState.READY_TO_DELIVER.equals(networkTokenTO.getState())) {
    // findAndRevokeDeliveredNetworkTokenWithSameType(networkTokenTO);
    // networkTokenTO.setState(TokenState.DELIVERED);
    // getNetworkTokenDAO().update(networkTokenTO);
    // } else {
    // throw new ServiceException(BizExceptionCode.TMS_026,
    // BizExceptionCode.TMS_017_MSG);
    // }
    // }

    /**
     * <br>
     * The method verifyReadyToIssueTokens do these work :</br> <br>
     * 1. Find the tokens with the state of 'READY_TO_ISSUE'</br> <br>
     * 2. send token issuance request to the sub system 'PKI'</br> <br>
     * 3. Change the state of tokens to 'PENDING_TO_ISSUE'</br>
     */
    // @Override
    // public void verifyReadyToIssueTokens() throws BaseException {
    // processOnTokensBeforeIssueRequest(TokenState.READY_TO_ISSUE);
    // }

    // /**
    // * <br>The method verifyPendingToIssueTokens do these work :</br>
    // * <br>1. calling the search method of the sub system 'PKI'. If the
    // specified token is created, then</br>
    // * <br>2. Change the state of tokens from 'PENDING_TO_ISSUE' to the state
    // 'READY_TO_DELIVER'</br>
    // */
    // @Override
    // public void verifyPendingToIssueTokens() throws BaseException {
    // processOnTokensAfterPKIResponse(TokenState.PENDING_TO_ISSUE);
    // }

    /**
     * The method deleteToken is used to delete an instance of type
     * {@link com.gam.nocr.ems.data.domain.TokenTO}
     *
     * @param tokenId     is an instance of type {@link Long}, which represents the id
     *                    of a specified token
     * @param tokenOrigin is an enumeration of type {@link TokenOrigin}
     * @throws com.gam.commons.core.BaseException
     */
    @Override
    public void deleteToken(Long tokenId, TokenOrigin tokenOrigin)
            throws BaseException {
        try {
            if (tokenId == null) {
                throw new ServiceException(BizExceptionCode.TMS_039,
                        BizExceptionCode.TMS_039_MSG);
            }

            if (tokenOrigin == null) {
                throw new ServiceException(BizExceptionCode.TMS_040,
                        BizExceptionCode.TMS_040_MSG);
            }

            switch (tokenOrigin) {
                case PERSON:
                    PersonTokenDAO personTokenDAO = getPersonTokenDAO();
                    PersonTokenTO desiredPersonTokenTO = personTokenDAO.find(
                            PersonTokenTO.class, tokenId);
                    if (desiredPersonTokenTO == null) {
                        throw new ServiceException(BizExceptionCode.TMS_041,
                                BizExceptionCode.TMS_018_MSG);
                    } else if (TokenState.READY_TO_ISSUE
                            .equals(desiredPersonTokenTO.getState())
                            || TokenState.PENDING_FOR_EMS
                            .equals(desiredPersonTokenTO.getState())) {
                        personTokenDAO.delete(desiredPersonTokenTO);
                    } else {
                        throw new ServiceException(BizExceptionCode.TMS_042,
                                BizExceptionCode.TMS_042_MSG);
                    }
                    break;
                // Commented By Adldoost
                // case NETWORK:
                // NetworkTokenTO desiredNetworkTokenTO = new NetworkTokenTO();
                // desiredNetworkTokenTO.setId(tokenId);
                // getNetworkTokenDAO().delete(desiredNetworkTokenTO);
                // break;
            }
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.TMS_045,
                    BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    public Long getCountByState(TokenType tokenType, TokenState tokenState)
            throws BaseException {
        if (tokenType == null) {
            return getPersonTokenDAO().getCountByState(tokenState);
        }
        // Commented By Adldoost
        // else if (TokenType.NETWORK.equals(tokenType)) {
        // return getNetworkTokenDAO().getCountByState(tokenState);
        // }
        return null;
    }

    @Override
    public List<Long> getIdsByState(TokenType tokenType, TokenState tokenState)
            throws BaseException {
        if (tokenType == null) {
            return getPersonTokenDAO().getIdsByState(tokenState);
        }
        // Commented By Adldoost
        // else if (TokenType.NETWORK.equals(tokenType)) {
        // return getNetworkTokenDAO().getIdsByState(tokenState);
        // }
        return null;
    }

    @Override
    public Long ProcessPersonTokenRequest(Integer from,
                                          CertificateTO certificateTO) throws BaseException {
        try {
            List<PersonTokenTO> personTokenTOList = getPersonTokenDAO()
                    .findByState(TokenState.READY_TO_ISSUE, from, 1);
            // todo: refactor
            if (EmsUtil.checkListSize(personTokenTOList)) {
                PersonTokenTO personTokenTO = personTokenTOList.get(0);
                if (findDeliveredPersonTokenWithSameType(personTokenTO) != null) {
                    throw new ServiceException(BizExceptionCode.TMS_047,
                            BizExceptionCode.TMS_047_MSG);
                }
                try {
                    PersonTokenTO tempTokenTO = getPKIService()
                            .issuePersonTokenRequest(personTokenTO,
                                    certificateTO);
                    personTokenTO.setRequestID(tempTokenTO.getRequestID());
                    personTokenTO.setState(TokenState.PENDING_TO_ISSUE);
                    return personTokenTO.getId();
                } catch (BaseException e) {
                    if (BizExceptionCode.PKI_011.equals(e.getExceptionCode())) {
                        logger.error(e.getExceptionCode(), e.getMessage(), e);
                        personTokenTO.setState(TokenState.PKI_ERROR);
                    } else {
                        throw e;
                    }
                } catch (Exception e) {
                    throw new ServiceException(BizExceptionCode.TMS_048,
                            BizExceptionCode.GLB_008_MSG, e);
                }
            }
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.TMS_046,
                    e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Long ProcessPersonTokenRenewalRequest(Integer from,
                                                 CertificateTO certificateTO) throws BaseException {
        try {
            String AUTO_ACCEPT_RENEWAL_REQUEST = "1";

            List<PersonTokenTO> personTokenTOList = getPersonTokenDAO()
                    .findByState(TokenState.READY_TO_RENEWAL_ISSUE, from, 1);
            int autoAccept = Integer.valueOf(EmsUtil.getProfileValue(
                    ProfileKeyName.Auto_Accept_Renewal_Request,
                    AUTO_ACCEPT_RENEWAL_REQUEST));
            if (autoAccept == 1) {
                List<PersonTokenTO> pendingList = getPersonTokenDAO()
                        .findByState(TokenState.PENDING_FOR_EMS, from, 1);
                if (EmsUtil.checkListSize(pendingList))
                    personTokenTOList.addAll(pendingList);
            }
            // todo: refactor
            if (EmsUtil.checkListSize(personTokenTOList)) {
                PersonTokenTO personTokenTO = personTokenTOList.get(0);
                if (findRnewnalPersonTokenRequestForSamePerson(personTokenTO)
                        .size() != 0) {
                    throw new ServiceException(BizExceptionCode.TMS_060,
                            BizExceptionCode.TMS_060_MSG);
                }
                try {
                    PersonTokenTO oldToken = getPersonTokenDAO()
                            .findByPersonIdAndType(personTokenTO.getPerson(),
                                    TokenType.SIGNATURE);
                    if (oldToken == null) {
                        throw new ServiceException(BizExceptionCode.TMS_063,
                                BizExceptionCode.TMS_063_MSG);
                    }
                    // PersonTokenTO searchResultToken = (PersonTokenTO)
                    // getPKIService()
                    // .searchTokenRequest(oldToken, TokenType.SIGNATURE,
                    // certificateTO);
                    PersonTokenTO tempTokenTO = getPKIService()
                            .renewalPersonTokenRequest(oldToken, certificateTO);
                    personTokenTO.setRequestID(tempTokenTO.getRequestID());
                    personTokenTO.setState(TokenState.PENDING_TO_RENEWAL_ISSUE);
                    return personTokenTO.getId();
                } catch (BaseException e) {
                    if (BizExceptionCode.PKI_011.equals(e.getExceptionCode())) {
                        logger.error(e.getExceptionCode(), e.getMessage(), e);
                        personTokenTO.setState(TokenState.PKI_ERROR);
                    } else {
                        throw e;
                    }
                } catch (Exception e) {
                    throw new ServiceException(BizExceptionCode.TMS_061,
                            BizExceptionCode.GLB_008_MSG, e);
                }
            }
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.TMS_062,
                    e.getMessage(), e);
        }
        return null;
    }

    // Commented By Adldoost
    // @Override
    // public Long ProcessNetworkTokenRequest(Integer from, CertificateTO
    // certificateTO) throws BaseException {
    // try {
    // List<NetworkTokenTO> networkTokenTOList =
    // getNetworkTokenDAO().findByState(TokenState.READY_TO_ISSUE, from, 1);
    // if (EmsUtil.checkListSize(networkTokenTOList)) {
    // NetworkTokenTO networkTokenTO = networkTokenTOList.get(0);
    // if (findDeliveredNetworkTokenWithSameType(networkTokenTO) != null) {
    // throw new ServiceException(BizExceptionCode.TMS_050,
    // BizExceptionCode.TMS_050_MSG);
    // }
    // try {
    // NetworkTokenTO tempTokenTO =
    // getPKIService().issueNetworkTokenRequest(networkTokenTO, certificateTO);
    // networkTokenTO.setRequestID(tempTokenTO.getRequestID());
    // networkTokenTO.setState(TokenState.PENDING_TO_ISSUE);
    // return networkTokenTO.getId();
    // } catch (BaseException e) {
    // if (BizExceptionCode.PKI_010.equals(e.getExceptionCode())) {
    // logger.error(e.getExceptionCode(), e.getMessage(), e);
    // networkTokenTO.setState(TokenState.PKI_ERROR);
    // } else {
    // throw e;
    // }
    // } catch (Exception e) {
    // throw new ServiceException(BizExceptionCode.TMS_051, e.getMessage(), e);
    // }
    // }
    // } catch (BaseException e) {
    // throw e;
    // } catch (Exception e) {
    // throw new ServiceException(BizExceptionCode.TMS_049, e.getMessage(), e);
    // }
    // return null;
    // }

    @Override
    public Long processPersonTokenResponse(Long personTokenId,
                                           CertificateTO certificateTO) throws BaseException {
        try {
            // List<PersonTokenTO> personTokenTOList =
            // getPersonTokenDAO().findByState(TokenState.PENDING_TO_ISSUE,
            // from, 1);
            PersonTokenTO personTokenTO = getPersonTokenDAO().findById(
                    personTokenId);

            TokenState state = personTokenTO.getState();

            // if (EmsUtil.checkListSize(personTokenTOList)) {
            // PersonTokenTO personTokenTO = personTokenTOList.get(0);
            if (personTokenTO != null) {
                try {
                    TokenTO tempTokenTO = getPKIService().searchTokenRequest(
                            personTokenTO, personTokenTO.getType(),
                            certificateTO);
                    if (TokenState.PROCESSED.equals(tempTokenTO.getState())) {
                        personTokenTO.setAKI(tempTokenTO.getAKI());
                        personTokenTO.setCertificateSerialNumber(tempTokenTO
                                .getCertificateSerialNumber());
                        personTokenTO.setIssuanceDate(new Date());

                        // Adldoost
                        if (TokenState.PENDING_TO_RENEWAL_ISSUE == state) {
                            personTokenTO
                                    .setState(TokenState.READY_TO_RENEWAL_DELIVER);
//							MessageVTO message = new MessageVTO();
//							message.setMsgPriority("1");
//							message.setMsgContent("توکن جدید شما آماده تحویل است");
//							message.setMsgSubject("تحویل توکن تمدیدی");
//							List<MessageDestinationVTO> destination = new ArrayList<MessageDestinationVTO>();
//							MessageDestinationVTO messageDestinationVTO = new MessageDestinationVTO();
//							messageDestinationVTO.setPersonId(personTokenTO
//									.getPerson().getId().toString());
//							messageDestinationVTO.setMessageType("شخصی");
//							destination.add(messageDestinationVTO);
//							UserProfileTO userProfile = new UserProfileTO();
//							userProfile.setUserName("system");
//							getMessageService(userProfile).save(message,
//									destination);
                        } else {
                            personTokenTO.setState(TokenState.READY_TO_DELIVER);
                        }

                    }
                } catch (BaseException e) {
                    throw e;
                } catch (Exception e) {
                    throw new ServiceException(BizExceptionCode.TMS_053,
                            e.getMessage(), e);
                }
            }
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.TMS_054,
                    e.getMessage(), e);
        }
        return null;
    }

    // Commented By Adldoost
    // @Override
    // public Long processNetworkTokenResponse(Long networkTokenId,
    // CertificateTO certificateTO) throws BaseException {
    // try {
    //
    // NetworkTokenTO networkTokenTO =
    // getNetworkTokenDAO().findById(networkTokenId);
    //
    // // List<NetworkTokenTO> networkTokenTOList =
    // getNetworkTokenDAO().findByState(TokenState.PENDING_TO_ISSUE, from, 1);
    // // if (EmsUtil.checkListSize(networkTokenTOList)) {
    // // NetworkTokenTO networkTokenTO = networkTokenTOList.get(0);
    // if (networkTokenTO != null){
    // try {
    // TokenTO tempTokenTO = getPKIService().searchTokenRequest(networkTokenTO,
    // TokenType.NETWORK, certificateTO);
    // if (TokenState.PROCESSED.equals(tempTokenTO.getState())) {
    // networkTokenTO.setAKI(tempTokenTO.getAKI());
    // networkTokenTO.setCertificateSerialNumber(tempTokenTO.getCertificateSerialNumber());
    // networkTokenTO.setIssuanceDate(new Date());
    // networkTokenTO.setState(TokenState.READY_TO_DELIVER);
    // }
    // } catch (BaseException e) {
    // throw e;
    // } catch (Exception e) {
    // throw new ServiceException(BizExceptionCode.TMS_055, e.getMessage(), e);
    // }
    // }
    // } catch (BaseException e) {
    // throw e;
    // } catch (Exception e) {
    // throw new ServiceException(BizExceptionCode.TMS_056, e.getMessage(), e);
    // }
    // return null;
    // }

    @Override
    public CertificateTO findCertificateByUsage(
            CertificateUsage certificateUsage) throws BaseException {
        return getCertificateDAO().findCertificateByUsage(certificateUsage);
    }

    // Adldoost
    @Override
    public PersonTokenTO findDeliveredPersonTokenByTypeAndPerson(UserProfileTO userProfileTO,
                                                                 TokenType tokenType) throws BaseException {
        Long personID = getPersonDAO().findPersonIdByUsername(userProfileTO.getUserName());
        return getPersonTokenDAO().findDeliveredByPersonIdAndType(personID,
                tokenType);
    }


    // Adldoost
    @Override
    public PersonTokenTO findPersonTokenById(Long id) throws BaseException {
        return getPersonTokenDAO().findById(id);
    }

    // Adldoost
    @Override
    public List<PersonTokenTO> findPersonTokenRenewalRequest(UserProfileTO userProfileTO)
            throws BaseException {

        Long personID = null;
        List<TokenState> states = new ArrayList<TokenState>();

        // states.add(TokenState.DELIVERED);
        // states.add(TokenState.PKI_ERROR);
        // states.add(TokenState.REVOKED);

        states.add(TokenState.READY_TO_ISSUE);
        states.add(TokenState.PENDING_TO_ISSUE);
        states.add(TokenState.READY_TO_DELIVER);

        states.add(TokenState.PENDING_FOR_EMS);
        states.add(TokenState.EMS_REJECT);
        states.add(TokenState.READY_TO_RENEWAL_ISSUE);
        states.add(TokenState.PENDING_TO_RENEWAL_ISSUE);
        states.add(TokenState.READY_TO_RENEWAL_DELIVER);
        states.add(TokenState.PICKUP_IN_PROGRESS);
        states.add(TokenState.SUSPENDED);
        states.add(TokenState.WAITING_FOR_PICKUP);
        states.add(TokenState.PROCESSED);

        personID = getPersonDAO().findPersonIdByUsername(userProfileTO.getUserName());

        List<PersonTokenTO> tokenList = getPersonTokenDAO()
                .findByPersonIdAndTypeAndState(personID, TokenType.SIGNATURE,
                        states);
        return tokenList;
    }

    // Adldoost
    @Override
    public List<PersonTokenVTO> findPendingToEMSAndRejectByEMS()
            throws BaseException {
        List<TokenState> states = new ArrayList<TokenState>();
        states.add(TokenState.PENDING_FOR_EMS);
        states.add(TokenState.EMS_REJECT);
        List<PersonTokenTO> tokenList = getPersonTokenDAO().findByState(states);
        List<PersonTokenVTO> tokenVTOList = new ArrayList<PersonTokenVTO>();
        for (PersonTokenTO token : tokenList) {
            PersonTokenVTO tokenVTO = new PersonTokenVTO();
            PersonTokenMapper.convert(token);
            tokenVTOList.add(tokenVTO);
        }
        return tokenVTOList;

    }

    // Adldoost
    @Override
    public void approveRenewalRequest(String ids) throws BaseException {
        getPersonTokenDAO().approveRewnewalRequest(ids);
    }

    // Adldoost
    @Override
    public void rejectRenewalRequest(String ids) throws BaseException {
        getPersonTokenDAO().rejectRewnewalRequest(ids);
    }

    // Adldoost
    // edited by Madanipour
    @Override
    public void deliverRenewalRequest(String ids) throws BaseException {
        PersonTokenTO personTokenTO = getPersonTokenDAO().findById(
                Long.parseLong(ids));
        PersonTokenTO oldPersonTokenTO = findDeliveredPersonTokenByTypeAndPerson(
                personTokenTO.getPerson(), TokenType.SIGNATURE);

        if (oldPersonTokenTO != null
                && oldPersonTokenTO.getState() != TokenState.REVOKED)
            revokePersonToken(oldPersonTokenTO.getId(), ReplicaReason.RENEW,
                    null);
        String[] sid = ids.split(",");

        getPersonTokenDAO().deliverRewnewalRequest(sid[0]);
    }

    // Adldoost
    @Override
    @Permissions(value = "ems_activateToken")
    public void activateRenewalRequest(String ids) throws BaseException {
        try {
            CertificateTO certificateTO = findCertificateByUsage(CertificateUsage.PKI_SIGN);
            String[] array = ids.split(",");
            if (!EmsUtil.checkArraySize(array))
                throw new ServiceException(BizExceptionCode.TMS_064,
                        "ids array is null");
            for (String id : array) {
                long personTokenId;

                personTokenId = Long.parseLong(array[0]);


                PersonTokenTO token = getPersonTokenDAO().findById(personTokenId);
                if (token.getRequestID() == null) {
                    throw new ServiceException(BizExceptionCode.TMS_066,
                            "token request id is null");
                }
                doActivation(token, TokenType.SIGNATURE);
                getPersonTokenDAO().activateRenewalRequest(id);
            }
        } catch (BaseException e) {
            throw e;
        } catch (Exception ex) {
            throw new ServiceException(BizExceptionCode.TMS_065,
                    ex.getMessage(), ex);
        }
    }

    private MessageService getMessageService(UserProfileTO userProfileTO)
            throws BaseException {
        MessageService messageService = null;
        try {
            messageService = (MessageService) ServiceFactoryProvider
                    .getServiceFactory()
                    .getService(
                            EMSLogicalNames
                                    .getServiceJNDIName(EMSLogicalNames.SRV_MESSAGE), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(BizExceptionCode.MSD_002,
                    BizExceptionCode.GLB_028_MSG, e,
                    EMSLogicalNames.SRV_MESSAGE.split(","));
        }
        messageService.setUserProfileTO(userProfileTO);
        return messageService;
    }


}
