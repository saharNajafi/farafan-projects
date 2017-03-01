package com.gam.nocr.ems.web.listreader.processor;

import com.gam.commons.listreader.ListReaderException;
import com.gam.commons.listreader.ParameterProvider;
import gampooya.tools.vlp.ListException;
import gampooya.tools.vlp.ValueListHandler;
import gampooya.tools.vlp.ValueListProvider;

import java.util.HashMap;

/**
 * The list reader processor class for system configurations grid
 *
 * @author Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public class SystemProfileProcessor extends EMSVLPListProcessor {
    protected ValueListHandler prepareVLH(ParameterProvider paramProvider) throws ListReaderException {
        HashMap parameters = new HashMap();
        StringBuilder parts = new StringBuilder();
        ValueListProvider vlp = getValueListProvider();
        String orderBy = getOrderBy(paramProvider);

        //check grid filters and add related parts and parameters
        String keyName = paramProvider.getFilter("keyName");
        String caption = paramProvider.getFilter("caption");

        if (keyName != null && keyName.trim().length() != 0) {
            parameters.put("keyName", "%" + keyName + "%");
            parts.append(",keyName");
        }
        if (caption != null && caption.trim().length() != 0) {
            parameters.put("caption", "%" + caption + "%");
            parts.append(",caption");
        }

        ValueListHandler vlh = null;
        try {
            vlh = vlp.loadList("systemProfileList",
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
