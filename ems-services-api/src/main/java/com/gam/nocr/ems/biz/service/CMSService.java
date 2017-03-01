package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.nocr.ems.data.domain.CardRequestTO;
import com.gam.nocr.ems.data.domain.CertificateTO;
import com.gam.nocr.ems.data.domain.EnrollmentOfficeTO;
import com.gam.nocr.ems.data.domain.vol.CardApplicationInfoVTO;
import com.gam.nocr.ems.data.domain.vol.CardInfoVTO;

import java.util.List;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public interface CMSService extends Service {
    // TODO : =============================================================================
    // TODO : Do not remove this part which is commented- it must be completed by Jalilian
    // TODO : =============================================================================


    /**
     * The issueCard method is used for sending the card issuance request to CMS subsystem.
     *
     * @param cardRequestTO is the object which encapsulates the attributes which are needed for the process 'issue card'
     * @param requestId     is an object of type String which shows the request id of a specified card issuance request
     * @param certificateTO is an object of type {@link com.gam.nocr.ems.data.domain.CertificateTO}, which carries the
     *                      required field for signing request
     */
    public void issueCard(CardRequestTO cardRequestTO,
                          String requestId, CertificateTO certificateTO) throws BaseException;

    /**
     * The batchReceipt method is used to assure the sub system 'CMS' which the batch has completely received
     * by sub system 'EMS'
     *
     * @param batchId represents the batchId of the card
     */
    public void batchReceipt(String batchId) throws BaseException;

    /**
     * The batchMissed method is used to inform the sub system 'CMS' from missing the batch by sub system 'EMS'
     *
     * @param batchId represents the batchId of the card
     * @param reason  is a description about why the batch has missed
     */
    public void batchMissed(String batchId,
                            String reason) throws BaseException;

    /**
     * The boxMissed method is used to inform the sub system 'CMS' from missing the box by sub system 'EMS'
     *
     * @param boxId  represents the boxId of the card
     * @param reason is a description about why the box has missed
     */
    public void boxMissed(String boxId,
                          String reason) throws BaseException;

    /**
     * The cardMissed method is used to inform the sub system 'CMS' from missing the card by sub system 'EMS'
     *
     * @param crn    represents the serial number of the card
     * @param reason is a description about why the card has missed
     */
    public void cardMissed(String crn,
                           String reason) throws BaseException;

    /**
     * The cardHandedOut method is used to inform the sub system 'CMS' from handing out the card by sub system 'EMS'
     *
     * @param crn represents the serial number of the card
     */
    public void cardHandedOut(String crn) throws BaseException;

    /**
     * The expireCard method is used to send the request to the sub system 'CMS' to expire the card
     *
     * @param crn    represents the serial number of the card
     * @param reason is a description about the card expiration time
     */
    public void expireCard(String crn,
                           String reason) throws BaseException;

    /**
     * The suspendCard method is used to send the request to the sub system 'CMS' to suspend the card
     *
     * @param crn    represents the serial number of the card
     * @param reason is a description about the card suspension
     */
    public void suspendCard(String crn,
                            String reason) throws BaseException;

    /**
     * The resumeCard method is used to send the request to the sub system 'CMS' to resume the card
     *
     * @param crn    represents the serial number of the card
     * @param reason is a description about the card resumption
     */
    public void resumeCard(String crn,
                           String reason) throws BaseException;


    /**
     * The revokeCard method is used to send the request to the sub system 'CMS' to revoke the card
     *
     * @param crn    represents the serial number of the card
     * @param reason is a description about the card revocation
     */
    public void revokeCard(String crn,
                           String reason) throws BaseException;

    /**
     * The destroyCard method is used to send the request to the sub system 'CMS' to destroy the card
     *
     * @param crn    represents the serial number of the card
     * @param reason is a description about the card destroying
     */
    public void destroyCard(String crn,
                            String reason) throws BaseException;

    /**
     * The method getCitizenCards is used to be gained the information about all cards of the citizen.
     *
     * @param nationalId represents the citizen nationalId
     * @return a list of volatile object of type {@link CardInfoVTO}
     */
    public List<CardInfoVTO> getCitizenCards(String nationalId) throws BaseException;

    /**
     * The method getCitizenCardsByProduct is used to be gained the information about all of the citizen cards for a
     * specified product.
     *
     * @param nationalId represents the citizen nationalId
     * @param productId  represents the productId of the card
     * @return a list of volatile object of type {@link CardInfoVTO}
     */
    public List<CardInfoVTO> getCitizenCardsByProduct(String nationalId,
                                                      String productId) throws BaseException;

    /**
     * The method getCurrentCitizenCards is used to be gained the information about all cards of the citizen which are
     * active.
     *
     * @param nationalId represents the citizen nationalId
     * @return a list of volatile object of type {@link CardInfoVTO}
     */
    public List<CardInfoVTO> getCurrentCitizenCards(String nationalId) throws BaseException;

    /**
     * The method getCurrentCitizenCardsByProduct is used to be gained the information about the current card of the
     * citizen for a specified product.
     *
     * @param nationalId represents the citizen nationalId
     * @param productId  represents the productId of the card
     * @return an instance of volatile object of type {@link CardInfoVTO}
     */
    public CardInfoVTO getCurrentCitizenCardByProduct(String nationalId,
                                                      String productId) throws BaseException;

    /**
     * The method getCardInfo is used to be gained the information about the current active card of the
     * citizen.
     *
     * @param crn represents the serial number of the card
     * @return an object of type {@link CardInfoVTO}
     */
    public CardInfoVTO getCardInfo(String crn) throws BaseException;

    /**
     * The method getCardApplications is used to be gained the applications of the specified card.
     *
     * @param crn represents the serial number of the card
     * @return an object of type {@link CardApplicationInfoVTO}
     */
    public List<CardApplicationInfoVTO> getCardApplications(String crn) throws BaseException;

    /**
     * The method updateCardApplicationStatus is used to send the update request on an specified
     * embedded application of the card.
     *
     * @param crn                    represents the serial number of the card
     * @param cardApplicationInfoVTO is a volatile object that encapsulates the necessary attributes which are needed in
     *                               sending the application update request to the sub system 'CMS'
     */
    public void updateCardApplicationStatus(String crn,
                                            CardApplicationInfoVTO cardApplicationInfoVTO) throws BaseException;

    /**
     * The method addEnrollmentOffice is used to send a request to add new enrollment office.
     *
     * @param enrollmentOfficeTO is used to encapsulate the necessary attributes which are needed in sending the
     *                           request of adding new enrollment office to the sub system 'CMS'
     * @param enableStatus       1 for enabled and 2 for disabled
     */
    public void addEnrollmentOffice(EnrollmentOfficeTO enrollmentOfficeTO,
                                    int enableStatus) throws BaseException;

    /**
     * The method updateEnrollmentOffices is used to send a request to update a list of type {@link EnrollmentOfficeTO}.
     *
     * @param enrollmentOfficeTOList is used to encapsulate the necessary attributes which are needed in sending the
     *                               request of updating new enrollment office to the sub system 'CMS'
     * @param enableStatus           1 for enabled and 2 for disabled
     */
    public void updateEnrollmentOffices(List<EnrollmentOfficeTO> enrollmentOfficeTOList,
                                        int enableStatus) throws BaseException;
}
