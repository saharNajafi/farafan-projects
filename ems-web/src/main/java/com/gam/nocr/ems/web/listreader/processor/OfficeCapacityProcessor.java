package com.gam.nocr.ems.web.listreader.processor;

import com.gam.commons.core.BaseLog;
import com.gam.commons.listreader.ListReaderException;
import com.gam.commons.listreader.ParameterProvider;
import gampooya.tools.vlp.ListException;
import gampooya.tools.vlp.ValueListHandler;
import gampooya.tools.vlp.ValueListProvider;
import org.slf4j.Logger;

import java.util.HashMap;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 5/26/18.
 */
public class OfficeCapacityProcessor extends EMSVLPListProcessor {

    private static final Logger logger = BaseLog.getLogger(OfficeCapacityProcessor.class);

    protected ValueListHandler prepareVLH(ParameterProvider paramProvider) throws ListReaderException {
        HashMap parameters = new HashMap();
        StringBuilder parts = new StringBuilder();
        ValueListProvider vlp = getValueListProvider();

        ValueListHandler vlh;
        try {
            vlh = vlp.loadList("officeCapacityList",
                    ("main" + parts).split(","),
                    ("count" + parts).split(","),
                    parameters,null, null);
        } catch (ListException e) {
            throw new ListReaderException("Unable to prepare a VLH to fetch list named '" + paramProvider.getListName() + "'", e);
        }
        return vlh;
    }
}
