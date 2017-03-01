package com.gam.nocr.ems.web.listreader.processor;

import com.gam.commons.listreader.ListReaderException;
import com.gam.commons.listreader.ParameterProvider;
import gampooya.tools.vlp.ListException;
import gampooya.tools.vlp.ValueListHandler;
import gampooya.tools.vlp.ValueListProvider;

import java.util.HashMap;

/**
 * The list reader processor class for document types grid
 *
 * @author Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public class DocTypeProcessor extends EMSVLPListProcessor {
    protected ValueListHandler prepareVLH(ParameterProvider paramProvider) throws ListReaderException {
        HashMap parameters = new HashMap();
        String parts = "";
        ValueListProvider vlp = getValueListProvider();
        String orderBy = getOrderBy(paramProvider);

        //check grid filters and add related parts and parameters
        String name = paramProvider.getFilter("name");


        if (name != null && name.trim().length() != 0) {
            parameters.put("name", "%" + name + "%");
            parts += ",docType";
        }

        ValueListHandler vlh = null;
        try {
            vlh = vlp.loadList("docTypeList",
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
