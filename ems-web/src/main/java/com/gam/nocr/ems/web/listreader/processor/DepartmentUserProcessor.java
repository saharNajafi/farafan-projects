package com.gam.nocr.ems.web.listreader.processor;

import com.gam.commons.listreader.ListReaderException;
import com.gam.commons.listreader.ParameterProvider;
import gampooya.tools.vlp.ListException;
import gampooya.tools.vlp.ValueListHandler;
import gampooya.tools.vlp.ValueListProvider;

import java.util.HashMap;

/**
 * The list reader processor class for department users grid in popup
 *
 * @author Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public class DepartmentUserProcessor extends EMSVLPListProcessor {
    protected ValueListHandler prepareVLH(ParameterProvider paramProvider) throws ListReaderException {
        HashMap parameters = new HashMap();
        ValueListProvider vlp = getValueListProvider();
        String orderBy = getOrderBy(paramProvider);
        parameters.put("depId", paramProvider.getParameter("depId"));
        ValueListHandler vlh = null;
        try {
            vlh = vlp.loadList("departmentUserList",
                    "main".split(","),
                    "count".split(","),
                    parameters,
                    orderBy,
                    null);
        } catch (ListException e) {
            throw new ListReaderException("Unable to prepare a VLH to fetch list named '" + paramProvider.getListName() + "'", e);
        }
        return vlh;
    }
}