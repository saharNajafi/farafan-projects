package com.gam.nocr.ems.web.listreader.processor;

import com.gam.commons.listreader.ListReaderException;
import com.gam.commons.listreader.ParameterProvider;
import com.gam.nocr.ems.util.EmsUtil;
import gampooya.tools.vlp.ListException;
import gampooya.tools.vlp.ValueListHandler;
import gampooya.tools.vlp.ValueListProvider;

import java.util.HashMap;

/**
 * The list reader processor class for card request history grid
 *
 * @author <a href="mailto:saadat@gamelectronics.com.com">Alireza Saadat</a>
 */
public class EmsCardRequestHistoryProcessor extends EMSVLPListProcessor {

    protected ValueListHandler prepareVLH(ParameterProvider paramProvider) throws ListReaderException {
        HashMap parameters = new HashMap();
        StringBuilder parts = new StringBuilder();
        //  Retrieve a VLP implementation of the underlying project
        ValueListProvider vlp = getValueListProvider();
        //  Determine requested ordering of the result
        String orderBy = getOrderBy(paramProvider) != null ? getOrderBy(paramProvider) : "date asc";
        //  Create an instance of VLH base on given parameters
        ValueListHandler vlh;

        String cardRequestId = paramProvider.getParameter("cardRequestId");
        parameters.put("cardRequestId", cardRequestId);

        String result = paramProvider.getFilter("result");
        String fromDate = paramProvider.getFilter("fromDate");
        String toDate = paramProvider.getFilter("toDate");
        String systemId = paramProvider.getFilter("systemId");
        String cmsRequestId = paramProvider.getFilter("cmsRequestId");

        if (EmsUtil.checkString(result)) {
            parameters.put("result", "%" + result + "%");
            parts.append(",result");
        }
        if (EmsUtil.checkFromAndToDate(fromDate, toDate)) {
            parameters.put("fromDate", EmsUtil.completeFromDate(fromDate));
            parameters.put("toDate", EmsUtil.completeToDate(toDate));
            parts.append(",enrolledDate");
        }
        if (EmsUtil.checkString(systemId)) {
            parameters.put("systemId", "%" + systemId + "%");
            parts.append(",systemId");
        }
        if (EmsUtil.checkString(cmsRequestId)) {
            parameters.put("cmsRequestId", "%" + cmsRequestId + "%");
            parts.append(",cmsRequestId");
        }

        try {
            vlh = vlp.loadList("emsCardRequestHistoryList",
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
}