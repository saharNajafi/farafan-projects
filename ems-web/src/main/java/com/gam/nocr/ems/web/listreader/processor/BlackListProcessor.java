package com.gam.nocr.ems.web.listreader.processor;

import com.gam.commons.listreader.ListReaderException;
import com.gam.commons.listreader.ParameterProvider;
import gampooya.tools.vlp.ListException;
import gampooya.tools.vlp.ValueListHandler;
import gampooya.tools.vlp.ValueListProvider;

import java.util.HashMap;

/**
 * The list reader processor class for black list grid
 *
 * @author Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public class BlackListProcessor extends EMSVLPListProcessor {
    protected ValueListHandler prepareVLH(ParameterProvider paramProvider) throws ListReaderException {
        HashMap parameters = new HashMap();
        StringBuilder parts = new StringBuilder();
        ValueListProvider vlp = getValueListProvider();
        String orderBy = getOrderBy(paramProvider);

        //check grid filters and add related parts and parameters
        String firstName = paramProvider.getFilter("firstName");
        String surname = paramProvider.getFilter("surname");
        String nationalId = paramProvider.getFilter("nationalId");

        if (firstName != null && firstName.trim().length() != 0) {
            parameters.put("firstName", "%" + firstName + "%");
            parts.append(",firstName");
        }
        if (surname != null && surname.trim().length() != 0) {
            parameters.put("surname", "%" + surname + "%");
            parts.append(",surname");
        }
        if (nationalId != null && nationalId.trim().length() != 0) {
            parameters.put("nationalId", "%" + nationalId + "%");
            parts.append(",nationalId");
        }


        ValueListHandler vlh = null;
        try {
            vlh = vlp.loadList("blackList",
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
