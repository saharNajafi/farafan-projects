package com.gam.nocr.ems.web.listreader.processor;

import com.gam.commons.core.BaseLog;
import com.gam.commons.listreader.ListReaderException;
import com.gam.commons.listreader.ParameterProvider;
import com.gam.nocr.ems.util.EmsUtil;
import gampooya.tools.security.SecurityContextService;
import gampooya.tools.vlp.ListException;
import gampooya.tools.vlp.ValueListHandler;
import gampooya.tools.vlp.ValueListProvider;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * The list reader processor class for box details popup grid
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class DetailDispatchProcessor extends EMSVLPListProcessor {

    private static final Logger logger = BaseLog.getLogger(DetailDispatchProcessor.class);

    protected ValueListHandler prepareVLH(ParameterProvider paramProvider) throws ListReaderException {
        HttpServletRequest request = paramProvider.getRequest();
        SecurityContextService scs = new SecurityContextService(request);
        Integer perId;
        try {
            perId = scs.getCurrentPersonId();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            perId = 0;
        }
        HashMap parameters = new HashMap();
        parameters.put("perid", perId);
        String parId = paramProvider.getParameter("parId");
        if (parId == null || parId.trim().length() == 0)
            parId = "0";
        parameters.put("parid", parId);

        StringBuilder parts = new StringBuilder();

        String cmsId = paramProvider.getFilter("cmsId");
        String itemCount = paramProvider.getFilter("itemCount");
        String fromDispatchSentDate = paramProvider.getParameter("fromDispatchSentDate");
        String toDispatchSentDate = paramProvider.getParameter("toDispatchSentDate");
        String nextReceiverName = paramProvider.getFilter("nextReceiverName");
        String fromSentDate = paramProvider.getFilter("fromSentDate");
        String toSendDate = paramProvider.getFilter("toSendDate");
        String fromReceiveDate = paramProvider.getFilter("fromReceiveDate");
        String toReceiveDate = paramProvider.getFilter("toReceiveDate");
        String fromMySendDate = paramProvider.getFilter("fromMySendDate");
        String toMySendDate = paramProvider.getFilter("toMySendDate");

        String status = paramProvider.getFilter("status");

        if (cmsId != null) {
            parameters.put("cmsId", "%" + cmsId + "%");
            parts.append(",cmsId");
        }
        if (itemCount != null) {
            parameters.put("itemCount", itemCount);
            parts.append(",itemCount");
        }
        if (EmsUtil.checkFromAndToDate(fromDispatchSentDate, toDispatchSentDate)) {
            parameters.put("fromDispatchSentDate", EmsUtil.completeFromDate(fromDispatchSentDate));
            parameters.put("toDispatchSentDate", EmsUtil.completeToDate(toDispatchSentDate));
            parts.append(",sendDate");
        }
        if (checkString(nextReceiverName)) {
            parameters.put("nextReceiverName", "%" + nextReceiverName + "%");
            parts.append(",receiver");
        }
        if (EmsUtil.checkFromAndToDate(fromSentDate, toSendDate)) {
            parameters.put("fromSentDate", EmsUtil.completeFromDate(fromSentDate));
            parameters.put("toSendDate", EmsUtil.completeToDate(toSendDate));
            parts.append(",dispatchSentDate");
        }
        if (EmsUtil.checkFromAndToDate(fromReceiveDate, toReceiveDate)) {
            parameters.put("fromReceiveDate", EmsUtil.completeFromDate(fromReceiveDate));
            parameters.put("toReceiveDate", EmsUtil.completeToDate(toReceiveDate));
            parts.append(",dispatchReceiveDate");
        }
        if (EmsUtil.checkFromAndToDate(fromMySendDate, toMySendDate)) {
            parameters.put("fromMySendDate", EmsUtil.completeFromDate(fromMySendDate));
            parameters.put("toMySendDate", EmsUtil.completeToDate(toMySendDate));
            parts.append(",mySendDate");
        }
        if (EmsUtil.checkString(status)) {
            parameters.put("statusId", status);
            parts.append(",status");
        }
        //  Retrieve a VLP implementation of the underlying project
        ValueListProvider vlp = getValueListProvider();
        //  Determe requested ordering of the result
        String orderBy = getOrderBy(paramProvider);
        //  Create an instance of VLH base on given parameters
        ValueListHandler vlh = null;
        try {
            vlh = vlp.loadList(paramProvider.getListName(),
                    ("main" + parts).split(","),
                    ("count" + parts).split(","),
                    parameters,
                    orderBy,
                    null);
        } catch (ListException e) {
            throw new ListReaderException("Unable to prepare a VLH to fetch list named '" + paramProvider.getListName() + "'", e);
        }
        return vlh;
    }

    private boolean checkString(String param) {
        return param != null && param.trim().length() != 0;
    }
}
