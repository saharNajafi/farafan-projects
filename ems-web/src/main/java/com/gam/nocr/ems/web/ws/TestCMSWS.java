package com.gam.nocr.ems.web.ws;

import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.nocr.ems.biz.delegator.CardIssuanceRequestDelegator;
import com.gam.nocr.ems.biz.delegator.IMSDelegator;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.domain.CertificateTO;
import com.gam.nocr.ems.data.enums.CertificateUsage;
import com.gam.nocr.ems.util.EmsUtil;
import org.slf4j.Logger;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.WebFault;
import javax.xml.ws.WebServiceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Implementation of contracted services that should be provided by EMS to CMS
 *
 * @author <a href="mailto:saeid.rastak@gmail.com">Saeid Rastak</a>
 */

@WebFault
        (
                faultBean = "com.gam.nocr.ems.web.ws.ExternalInterfaceException"
        )
@WebService
        (
                serviceName = "TestCMSWS",
                portName = "TestCMSCardIssuanceRequestPort",
                targetNamespace = "http://ws.matiran.gam.com/"
        )
@SOAPBinding
        (
                style = SOAPBinding.Style.DOCUMENT,
                use = SOAPBinding.Use.LITERAL,
                parameterStyle = SOAPBinding.ParameterStyle.WRAPPED
        )

public class TestCMSWS extends WSSecurity {

    @Resource
    WebServiceContext webServiceContext;

    private static final Logger logger = BaseLog.getLogger(TestCMSWS.class);


    @WebMethod
    public void sendCardIssuanceRequestToCms(@WebParam(name = "cardRequestIds") List<Long> cardRequestIds) throws ExternalInterfaceException {
        //  Make sure the caller is the CMS (not anyone else)
        System.out.println("Start CARD Request ID For Sending To CMS: " + cardRequestIds);
       /* try {
            super.authenticate(webServiceContext);
        } catch (BaseException e) {
            logger.error("EMS-INTL-000001 - EMS internal error", e);
            throw new ExternalInterfaceException("EMS-INTL-000001", "Unauthorized SSL connection!");
        }*/

        CardIssuanceRequestDelegator cardIssuanceRequestDelegator = new CardIssuanceRequestDelegator();

        String DEFAULT_NUMBER_OF_Issuance_ASYNC_CALL_COUNT = "10";
        String DEFAULT_ASYNC_ENABLE = "1";
        String isAsyncEnable = EmsUtil.getProfileValue(ProfileKeyName.KEY_ASYNC_ENABLE, DEFAULT_ASYNC_ENABLE);

        Integer issuanceAsyncCallCount = Integer.valueOf(EmsUtil
                .getProfileValue(
                        ProfileKeyName.KEY_NUMBER_OF_Issuace_ASYNC_CALL_COUNT,
                        DEFAULT_NUMBER_OF_Issuance_ASYNC_CALL_COUNT));

        logger.info("issuance job started on : " + new Date());
        try {

            List<Long> ids = cardRequestIds;

            CertificateTO certificateTO = cardIssuanceRequestDelegator
                    .findCertificateByUsage(CertificateUsage.CMS_SIGN);
            if (certificateTO == null) {
                // No certificate found to sign card issuance request before
                // sending to CMS
                throw new ServiceException("ERROR : ",
                        "The specified certificate was not found.");
            }

            int asyncCallCount = 0;
            int finishedThreads = 0;
            int errorThreads = 0;

            List<Future<String>> currentSyncResult = new ArrayList<Future<String>>();
            List<Future<String>> succAsyncResult = new ArrayList<Future<String>>();
            List<Future<String>> failAsyncResult = new ArrayList<Future<String>>();

            if (isAsyncEnable.equals("1")) {
                if (ids != null) {
                    for (Long requestId : ids) {
                        try {
                            Future<String> res = cardIssuanceRequestDelegator
                                    .prepareDataAndSendIssuanceRequestByIdAsync(
                                            requestId, certificateTO);
                            if (res != null) {
                                currentSyncResult.add(res);
                                asyncCallCount++;
                            }
                            while ((currentSyncResult.size() >= issuanceAsyncCallCount)) {

                                for (Future<String> f : currentSyncResult) {
                                    if (f.isDone() == true) {
                                        try {
                                            String tmp = f.get();
                                            finishedThreads++;
                                            succAsyncResult.add(f);
                                        } catch (Exception e) {
                                            errorThreads++;
                                            failAsyncResult.add(f);
                                            logger.error(
                                                    BizExceptionCode.ISU_01,
                                                    " exception on calling issuance job thread async : "
                                                            + e.getMessage(),
                                                    e);
                                        }
                                    }
                                }

                                for (Future<String> f : succAsyncResult) {
                                    currentSyncResult.remove(f);
                                }

                                for (Future<String> f : failAsyncResult) {
                                    currentSyncResult.remove(f);
                                }

                                succAsyncResult.clear();
                                failAsyncResult.clear();

                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                }
                            }

                        } catch (Exception e) {
                            // An exception happened while trying to send an
                            // issuance request to the CMS. So ignore the
                            // current request and go to the next one by
                            // increasing the start index to load
                            logger.error("An exception happened while trying to send issuance request to the CMS", e);
                            break;
                        }
                    }

                    // this piece of code prevent java from getting closed
                    // before
                    // ending all of async calls
                    while ((currentSyncResult.size() > 0 && currentSyncResult
                            .size() <= issuanceAsyncCallCount)) {

                        for (Future<String> f : currentSyncResult) {
                            if (f.isDone() == true) {
                                try {
                                    String tmp = f.get();
                                    finishedThreads++;
                                    succAsyncResult.add(f);
                                } catch (Exception e) {
                                    errorThreads++;
                                    failAsyncResult.add(f);
                                    logger.error(BizExceptionCode.ISU_02,
                                            " exception on calling issuance job thread async : "
                                                    + e.getMessage(), e);
                                }
                            }
                        }

                        for (Future<String> f : succAsyncResult) {
                            currentSyncResult.remove(f);
                        }

                        for (Future<String> f : failAsyncResult) {
                            currentSyncResult.remove(f);
                        }

                        succAsyncResult.clear();
                        failAsyncResult.clear();

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                        }
                    }
                }

            }//End if isAsyncEnable
            else {

                if (ids != null) {
                    for (Long requestId : ids) {
                        try {
                            cardIssuanceRequestDelegator
                                    .prepareDataAndSendIssuanceRequestById(
                                            requestId, certificateTO);
                        } catch (Exception e) {
                            // An exception happened while trying to send an
                            // issuance request to the CMS. So ignore the
                            // current request and go to the next one by
                            // increasing the start index to load
                            logger.error("An exception happened while trying to send issuance request to the CMS", e);
                        }
                    }
                }

            }

            logger.info("issuance job finished on : " + new Date()
                    + " and all threads = " + asyncCallCount
                    + " and success threads = " + finishedThreads
                    + " and exception threads = " + errorThreads);
        } catch (Exception e) {
            logger.error("An exception happened while trying to send issuance request to the CMS", e);
        }
        System.out.println("Stop CARD Request ID For Sending To CMS: " + cardRequestIds);
    }

    @WebMethod
    public void sendIMSUpdateCitizensInfo(@WebParam(name = "cardRequestId") Long cardRequestId) throws ExternalInterfaceException {
        if (cardRequestId != null) {
            try {
                IMSDelegator imsDelegator = new IMSDelegator();
                imsDelegator.updateCitizenInfoById(cardRequestId);
            } catch (Exception e) {
                logger.error("Error: " , e);
            }
        }
    }

}