package com.gam.nocr.ems.web.listreader.processor;

import com.gam.commons.listreader.ListReaderException;
import com.gam.commons.listreader.ParameterProvider;
import com.gam.nocr.ems.util.EmsUtil;
import gampooya.tools.vlp.ListException;
import gampooya.tools.vlp.ValueListHandler;
import gampooya.tools.vlp.ValueListProvider;

import java.util.HashMap;

/**
 * The list reader processor class for business log grid
 *
 * @author Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public class BizLogProcessor extends EMSVLPListProcessor {
    protected ValueListHandler prepareVLH(ParameterProvider paramProvider) throws ListReaderException {
        HashMap parameters = new HashMap();
        StringBuilder parts = new StringBuilder();
        ValueListProvider vlp = getValueListProvider();
        String orderBy = getOrderBy(paramProvider);

        //check grid filters and add related parts and parameters
        String action = paramProvider.getFilter("actionNameStr");
        String actor = paramProvider.getFilter("actor");
        String adData = paramProvider.getFilter("additionalData");
        String fromDate = paramProvider.getFilter("fromDate");
        String toDate = paramProvider.getFilter("toDate");
        String entityID = paramProvider.getFilter("entityID");
        String entityName = paramProvider.getFilter("entityNameStr");

        if (action != null && action.trim().length() != 0) {
            parameters.put("action", action);
            parts.append(",action");
        }
        if (actor != null && actor.trim().length() != 0) {
            parameters.put("actor", "%" + actor + "%");
            parts.append(",actor");
        }
        if (adData != null && adData.trim().length() != 0) {
            parameters.put("adData", "%" + adData + "%");
            parts.append(",adData");
        }
        if (entityID != null && entityID.trim().length() != 0) {
            parameters.put("entityID", entityID);
            parts.append(",entityID");
        }
        if (entityName != null && entityName.trim().length() != 0) {
            parameters.put("entityName", entityName);
            parts.append(",entityName");
        }
        if (EmsUtil.checkFromAndToDate(fromDate, toDate)) {
            parameters.put("fromDate", EmsUtil.completeFromDate(fromDate));
            parameters.put("toDate", EmsUtil.completeToDate(toDate));
            parts.append(",date");
        }

        ValueListHandler vlh = null;
        try {
            vlh = vlp.loadList("bizLogList",
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
