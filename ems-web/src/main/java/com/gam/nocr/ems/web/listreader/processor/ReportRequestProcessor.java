package com.gam.nocr.ems.web.listreader.processor;

import com.gam.commons.listreader.ListReaderException;
import com.gam.commons.listreader.ParameterProvider;
import com.gam.nocr.ems.data.enums.ReportScope;
import com.gam.nocr.ems.util.EmsUtil;
import gampooya.tools.vlp.ListException;
import gampooya.tools.vlp.ValueListHandler;
import gampooya.tools.vlp.ValueListProvider;

import java.util.Map;

/**
 * The list reader processor class for report requests grid
 *
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public class ReportRequestProcessor extends EMSVLPListProcessor {

    private String getPart(String scope) {
        StringBuilder parts = new StringBuilder();
        if (!EmsUtil.checkString(scope) ||
                ReportScope.CCOS.name().equals(scope)) {
            parts.append(",ccos");
        } else if (ReportScope.EMS.name().equals(scope)) {
            parts.append(",ems");
        } else if (ReportScope.ALL.name().equals(scope)) {
            parts.append(",all");
        }
        return parts.toString();
    }

    @Override
    protected ValueListHandler prepareVLH(ParameterProvider paramProvider) throws ListReaderException {
        Map parameters = getQueryParrameters(paramProvider);
        String scope = paramProvider.getFilter("scope");
        String parts = getPart(scope);

        String orderBy = getOrderBy(paramProvider);

        ValueListProvider vlp = getValueListProvider();
        ValueListHandler vlh;
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
