package com.gam.nocr.ems.web.action;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.web.struts2.extJsController.ActionException;
import com.gam.commons.core.web.struts2.extJsController.ListControllerImpl;
import com.gam.nocr.ems.biz.delegator.CardRequestDelegator;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.vol.CardRequestReceiptVTO;
import com.gam.nocr.ems.data.domain.vol.CardRequestVTO;
import com.gam.nocr.ems.data.enums.CardRequestedAction;
import com.gam.nocr.ems.data.enums.SystemId;
import gampooya.tools.security.BusinessSecurityException;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main action class to handle all requests from card requests list
 *
 * @author <a href="mailto:moghaddam@gamelectronics.com">Ehsan Zaery
 *         Moghaddam</a>
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

    private CardRequestVTO data;

    public boolean isHasAccessToChangePriority() {
        return hasAccessToChangePriority;
    }

    public void setHasAccessToChangePriority(boolean hasAccessToChangePriority) {
        this.hasAccessToChangePriority = hasAccessToChangePriority;
    }

    private InputStream inputStream;
    private String downloadFileName;
    private String contentType;

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getDownloadFileName() {
        return downloadFileName;
    }

    public void setDownloadFileName(String downloadFileName) {
        this.downloadFileName = downloadFileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
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

            new CardRequestDelegator().updateCardRequestPriority(
                    getUserProfile(), cardRequestId, priority);

            return SUCCESS_RESULT;
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
    public String hasChangePriorityAccess() throws BaseException {
        try {
            hasAccessToChangePriority = new CardRequestDelegator()
                    .hasChangePriorityAccess(getUserProfile());

            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.CRA_011,
                    WebExceptionCode.GLB_001_MSG, e);
        } catch (Exception e) {
            throw new ActionException(WebExceptionCode.CRA_012,
                    WebExceptionCode.GLB_003_MSG, e);
        }

    }

    public String hasPrintRegistrationReceipt() throws BaseException {
        try {
            hasPrintRegistrationReceipt =
                    new CardRequestDelegator().hasPrintRegistrationReceipt(getUserProfile());
            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.CRA_015,
                    WebExceptionCode.GLB_001_MSG, e);
        } catch (Exception e) {
            throw new ActionException(WebExceptionCode.CRA_016,
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

    public String print() throws BaseException {
        /*try {
            if (cardRequestId != null) {
                new CardRequestDelegator().print(
                        getUserProfile()
                        , Long.parseLong(getCardRequestId()));
                return SUCCESS_RESULT;
            } else {
                throw new ActionException(WebExceptionCode.CRA_019,
                        WebExceptionCode.CRA_013_MSG);
            }
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.CRA_018,
                    WebExceptionCode.GLB_001_MSG, e);
        }*/

        CardRequestReceiptVTO cardRequestReceiptVTO;
        try {
            if (cardRequestId != null) {
                cardRequestReceiptVTO =
                        new CardRequestDelegator().printRegistrationReceipt(
                                getUserProfile()
                                , Long.parseLong(getCardRequestId()));

            } else {
                throw new ActionException(WebExceptionCode.CRA_017,
                        WebExceptionCode.CRA_013_MSG);
            }
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.CRA_018,
                    WebExceptionCode.GLB_001_MSG, e);
        }
        try {

            if (cardRequestReceiptVTO != null) {

                HttpServletResponse response = ServletActionContext.getResponse();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition", "attachment;filename*='UTF-8'" + URLEncoder.encode(cardRequestReceiptVTO.getCitizenFirstName() + "" + cardRequestReceiptVTO.getCitizenSurname(), "utf-8") + ".pdf");
                response.addHeader("Cache-Control", "no-cache");
                String sourceFileName = "jasper/reciept.jasper";
                ClassLoader classloader = Thread.currentThread().getContextClassLoader();
                InputStream reportStream = classloader.getResourceAsStream(sourceFileName);
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
                JasperReport jasReport = (JasperReport) JRLoader.loadObject(reportStream);
                JasperExportManager.exportReportToPdfStream(JasperFillManager.fillReport(jasReport, parameters, new JREmptyDataSource()), byteArrayOutputStream);
                //byte[] bytes = JasperExportManager.exportReportToPdf(JasperFillManager.fillReport(jasReport, parameters, new JREmptyDataSource()));
                //setInputStream(new ByteArrayInputStream(bytes));
                //setContentType("application/pdf");
                //setDownloadFileName("hasan");
                response.setContentLength(byteArrayOutputStream.size());
                ServletOutputStream servletOutputStream = response.getOutputStream();
                byteArrayOutputStream.writeTo(servletOutputStream);
                byteArrayOutputStream.flush();
                servletOutputStream.flush();
                servletOutputStream.close();
                byteArrayOutputStream.close();
                reportStream.close();
                return SUCCESS_RESULT;

            } else {
                throw new ActionException(WebExceptionCode.CRA_019,
                        WebExceptionCode.CRA_013_MSG);
            }
        } /*catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.CRA_018,
                    WebExceptionCode.GLB_001_MSG, e);
        }*/ catch (JRException e) {
            throw new ActionException(WebExceptionCode.CRA_018,
                    WebExceptionCode.GLB_001_MSG, e);
        } catch (IOException ex) {
            throw new ActionException(WebExceptionCode.CRA_018,
                    WebExceptionCode.GLB_001_MSG, ex);
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
}
