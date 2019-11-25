package com.gam.nocr.ems.web.action;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.web.struts2.extJsController.ActionException;
import com.gam.commons.core.web.struts2.extJsController.ListControllerImpl;
import com.gam.nocr.ems.biz.delegator.CardRequestDelegator;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.CardRequestTO;
import com.gam.nocr.ems.data.domain.vol.CardRequestReceiptVTO;
import com.gam.nocr.ems.data.domain.vol.CardRequestVTO;
import com.gam.nocr.ems.data.enums.CardRequestedAction;
import com.gam.nocr.ems.data.enums.SystemId;
import com.gam.nocr.ems.util.CcosBundle;
import com.gam.nocr.ems.util.EmsUtil;
import com.gam.nocr.ems.util.JasperUtil;
import gampooya.tools.security.BusinessSecurityException;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main action class to handle all requests from card requests list
 *
 * @author <a href="mailto:moghaddam@gamelectronics.com">Ehsan Zaery
 * Moghaddam</a>
 */
public class CardRequestListAction extends ListControllerImpl<CardRequestVTO> {

    private static final Logger logger = BaseLog
            .getLogger(CardRequestListAction.class);

    /**
     * Identifier of a card request. When an service is requested for a card
     * request (e.g. repealing a card request), the identifier card request will
     * be deserialized into this property
     */
    private String cardRequestId;
    private boolean hasAccessToChangePriority;
    private boolean hasPrintRegistrationReceipt;
    private boolean hasAccessToReceiveBatchId;

    private CardRequestVTO data;

    public boolean isHasAccessToChangePriority() {
        return hasAccessToChangePriority;
    }

    public void setHasAccessToChangePriority(boolean hasAccessToChangePriority) {
        this.hasAccessToChangePriority = hasAccessToChangePriority;
    }

    /**
     * In repealing a card request (or undoing it), the type of action requested
     * by user (repeal, undo, accept, etc.) is passed from client as this
     * property
     */
    private String cardRequestAction;

    private String priority;

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    @Override
    public void setRecords(List<CardRequestVTO> records) {
        this.records = records;
    }

    /**
     * Handles a repealing action requested by the client
     *
     * @return {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String repealCardRequest() throws BaseException {
        try {
            new CardRequestDelegator().doCardRequestRepealAction(
                    getUserProfile(), Long.valueOf(getCardRequestId()),
                    CardRequestedAction.valueOf(getCardRequestAction()),
                    SystemId.EMS);
            logger.info("Repealing request with id : " + getCardRequestId());
            logger.info("Repeal request action : " + getCardRequestAction());
            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.CRA_001,
                    WebExceptionCode.GLB_001_MSG, e);
        } catch (Exception e) {
            throw new ActionException(WebExceptionCode.CRA_002,
                    WebExceptionCode.GLB_003_MSG, e);
        }
    }

    public String getCardRequestId() {
        return cardRequestId;
    }

    public void setCardRequestId(String cardRequestId) {
        this.cardRequestId = cardRequestId;
    }

    public String getCardRequestAction() {
        return cardRequestAction;
    }

    public void setCardRequestAction(String cardRequestAction) {
        this.cardRequestAction = cardRequestAction;
    }

    /**
     * this method is used in change priority process .this method finds a card
     * request by id
     *
     * @return
     * @throws BaseException
     * @author ganjyar
     */
    public String findCardRequestById() throws BaseException {
        try {
            CardRequestVTO cr = new CardRequestDelegator().findCardRequestById(
                    getUserProfile(), cardRequestId);
            ArrayList<CardRequestVTO> cardRequestList = new ArrayList<CardRequestVTO>();
            cardRequestList.add(cr);
            setRecords(cardRequestList);

            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.CRA_007,
                    WebExceptionCode.GLB_001_MSG, e);
        } catch (Exception e) {
            throw new ActionException(WebExceptionCode.CRA_008,
                    WebExceptionCode.GLB_003_MSG, e);
        }

    }

    /**
     * this method is used in change priority process. the given priority must
     * be between 0 or 99
     *
     * @return
     * @throws BaseException
     * @author ganjyar
     */
    public String updateCardRequestPriority() throws BaseException {
        try {
            if (EmsUtil.checkString(priority)
                    && EmsUtil.checkString(cardRequestId)) {
                CardRequestTO cardRequestTO = new CardRequestDelegator().loadById(Long.valueOf(cardRequestId));
                new CardRequestDelegator().updateCardRequestPriority(
                        getUserProfile(),
                        cardRequestTO,
                        cardRequestTO.getId(),
                        cardRequestTO.getPriority(),//old priority
                        priority//new priority
                );
                return SUCCESS_RESULT;
            } else {
                throw new ServiceException(WebExceptionCode.CRA_023,
                        WebExceptionCode.CRA_023_MSG);
            }
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.CRA_009,
                    WebExceptionCode.GLB_001_MSG, e);
        } catch (Exception e) {
            throw new ActionException(WebExceptionCode.CRA_010,
                    WebExceptionCode.GLB_003_MSG, e);
        }

    }

    /**
     * this method is used to check change priority access
     *
     * @return
     * @throws BaseException
     * @author ganjyar
     */
    public String checkCardRequestListAccesses() throws BaseException {
        try {
            hasAccessToChangePriority = new CardRequestDelegator()
                    .hasChangePriorityAccess(getUserProfile());

            hasPrintRegistrationReceipt =
                    new CardRequestDelegator().hasPrintRegistrationReceipt(getUserProfile());
            hasAccessToReceiveBatchId = new CardRequestDelegator()
                    .hasReceiveBatchIdAccess(getUserProfile());
            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.CRA_011,
                    WebExceptionCode.GLB_001_MSG, e);
        } catch (Exception e) {
            throw new ActionException(WebExceptionCode.CRA_012,
                    WebExceptionCode.GLB_003_MSG, e);
        }

    }

    //hossein 8feature start
    public String loadById() throws BaseException {
        try {
            logger.info("Card request id : " + getCardRequestId() + "\n"
                    + "Requested action : " + getCardRequestAction());
            if (cardRequestId != null) {
                data = new CardRequestDelegator().viewCardRequestInfo(
                        getUserProfile(), Long.parseLong(getCardRequestId()));
                return SUCCESS_RESULT;
            } else {
                throw new ActionException(WebExceptionCode.CRA_013,
                        WebExceptionCode.CRA_013_MSG);
            }
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.CRA_014,
                    WebExceptionCode.GLB_001_MSG, e);
        }

    }

    /**
     * this method is used to check if we can print this card request or not?
     *
     * @return String
     * @throws BaseException
     * @author amiri
     */
    public String canPrintRegistration() throws BaseException {
        try {
            new CardRequestDelegator().printRegistrationReceipt(
                    getUserProfile()
                    , Long.parseLong(getCardRequestId()));
            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.PEA_004, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    public void print() throws BaseException {

        CardRequestReceiptVTO cardRequestReceiptVTO = new CardRequestReceiptVTO();
        String sourceFileName;
        if (cardRequestId != null) {
            try {
                cardRequestReceiptVTO =
                        new CardRequestDelegator().printRegistrationReceipt(
                                getUserProfile()
                                , Long.parseLong(getCardRequestId()));
            } catch (BaseException e) {
                Map parameters = new HashMap();
                parameters.put("warningMessage", CcosBundle.getMessage(e.getExceptionCode()));
                sourceFileName = "warninmessage.jasper";
                JasperUtil.generatePDFWithOutDataSource(sourceFileName, "ERROR", parameters);
                return;

            } catch (BusinessSecurityException e) {
                throw new ActionException(WebExceptionCode.CRA_018,
                        WebExceptionCode.GLB_001_MSG, e);
            }
        } else {
            throw new ActionException(WebExceptionCode.CRA_017,
                    WebExceptionCode.CRA_013_MSG);
        }

        try {
            new CardRequestDelegator().createHistoryOfReceipt(
                    getUserProfile()
                    , Long.parseLong(getCardRequestId()));

            sourceFileName = "reciept.jasper";
            Map parameters = new HashMap();
            parameters.put("firstName", cardRequestReceiptVTO.getCitizenFirstName());
            parameters.put("lastName", cardRequestReceiptVTO.getCitizenSurname());
            parameters.put("fatherName", cardRequestReceiptVTO.getFatherName());
            parameters.put("nationalId", cardRequestReceiptVTO.getNationalID());
            parameters.put("certificateId", cardRequestReceiptVTO.getBirthCertificateId());
            parameters.put("birthDate", cardRequestReceiptVTO.getBirthDateSolar());
            parameters.put("enrollDate", cardRequestReceiptVTO.getEnrolledDate());
            parameters.put("trackingId", cardRequestReceiptVTO.getTrackingID());
            parameters.put("printDate", cardRequestReceiptVTO.getReceiptDate());
            parameters.put("userName", cardRequestReceiptVTO.getUserFirstName() + " " + cardRequestReceiptVTO.getUserLastName());
            parameters.put("enrollmentName", cardRequestReceiptVTO.getEnrollmentName());
            JasperUtil.generatePDFWithOutDataSource(sourceFileName, cardRequestReceiptVTO.getNationalID(), parameters);
            return;

        } catch (BaseException e) {
            throw new ActionException(WebExceptionCode.CRA_020,
                    WebExceptionCode.GLB_003_MSG, e);
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.CRA_019,
                    WebExceptionCode.GLB_001_MSG, e);
        }
    }

    public CardRequestVTO getData() {
        return data;
    }

    public void setData(CardRequestVTO data) {
        this.data = data;
    }
    //hossein 8feature end

    public boolean isHasPrintRegistrationReceipt() {
        return hasPrintRegistrationReceipt;
    }

    public void setHasPrintRegistrationReceipt(boolean hasPrintRegistrationReceipt) {
        this.hasPrintRegistrationReceipt = hasPrintRegistrationReceipt;
    }

    public boolean isHasAccessToReceiveBatchId() {
        return hasAccessToReceiveBatchId;
    }

    public void setHasAccessToReceiveBatchId(boolean hasAccessToReceiveBatchId) {
        this.hasAccessToReceiveBatchId = hasAccessToReceiveBatchId;
    }
}
