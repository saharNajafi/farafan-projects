package com.gam.nocr.ems.web.listreader.processor;

import com.gam.commons.listreader.ListReaderException;
import com.gam.commons.listreader.ParameterProvider;
import gampooya.tools.vlp.ListException;
import gampooya.tools.vlp.ValueListHandler;
import gampooya.tools.vlp.ValueListProvider;

import java.util.Map;

/**
 * The list reader processor class for reports definition grid
 *
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public class ReportManagementProcessor extends EMSVLPListProcessor {

    /**
     * Given a parameters required for running underlying query and returns a prepared ValueListHandler object to be
     * used for running queries
     *
     * @param paramProvider An object encapsulating any parameter required for preparing ValueLisHandler object
     * @return Array of part names to be used on count query
     * @throws com.gam.commons.listreader.ListReaderException If any unpredicted condition occurs or any invalid data
     *                                                        passed as parameters, such an exception should be thrown
     *                                                        and halts loading requested list
     */
    @Override
    protected ValueListHandler prepareVLH(ParameterProvider paramProvider) throws ListReaderException {

        String parts = "";
        Map parameters = getQueryParrameters(paramProvider);
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
