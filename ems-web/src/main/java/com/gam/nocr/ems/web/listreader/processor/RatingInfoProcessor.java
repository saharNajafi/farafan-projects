package com.gam.nocr.ems.web.listreader.processor;

import com.gam.commons.listreader.ListReaderException;
import com.gam.commons.listreader.ParameterProvider;
import com.gam.nocr.ems.util.EmsUtil;
import gampooya.tools.vlp.ListException;
import gampooya.tools.vlp.ValueListHandler;
import gampooya.tools.vlp.ValueListProvider;

import java.util.HashMap;

/**
 * The list reader processor class for rating info grid
 *
 * @author Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public class RatingInfoProcessor extends EMSVLPListProcessor {
    protected ValueListHandler prepareVLH(ParameterProvider paramProvider) throws ListReaderException {
        HashMap parameters = new HashMap();
        StringBuilder parts = new StringBuilder();
        ValueListProvider vlp = getValueListProvider();
        String orderBy = getOrderBy(paramProvider);

        String clazz = null;
        String size = null;

        if (paramProvider.containsFilter("clazz"))
            clazz = paramProvider.getFilter("clazz");
        if (paramProvider.containsFilter("size"))
            size = paramProvider.getFilter("size");

        if (EmsUtil.checkString(clazz)) {
            parameters.put("clazz", "%" + clazz + "%");
            parts.append(",clazz");
        }
        if (EmsUtil.checkString(size)) {
            parameters.put("size", size);
            parts.append(",size");
        }

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
