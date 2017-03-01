package com.gam.nocr.ems.web.listreader.processor;

import com.gam.commons.listreader.ListReaderException;
import com.gam.commons.listreader.ParameterProvider;
import com.gam.nocr.ems.util.EmsUtil;
import gampooya.tools.vlp.ListException;
import gampooya.tools.vlp.ValueListHandler;
import gampooya.tools.vlp.ValueListProvider;

import java.util.HashMap;

/**
 * The list reader processor class for person tokens popup grid
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class PersonTokenProcessor extends EMSVLPListProcessor {
    protected ValueListHandler prepareVLH(ParameterProvider paramProvider) throws ListReaderException {

        StringBuilder parts = new StringBuilder();
        HashMap parameters = new HashMap();

        String tokenType = paramProvider.getFilter("tokenType");
        String tokenState = paramProvider.getFilter("tokenState");

        String perId = paramProvider.getParameter("perId");
        if (perId == null || perId.trim().length() == 0)
            perId = "0";

        parameters.put("perid", perId);

        if (EmsUtil.checkString(tokenType)) {
            parameters.put("tokenType", tokenType);
            parts.append(",tokenType");
        }
        if (EmsUtil.checkString(tokenState)) {
            parameters.put("tokenState", tokenState);
            parts.append(",tokenState");
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
}
