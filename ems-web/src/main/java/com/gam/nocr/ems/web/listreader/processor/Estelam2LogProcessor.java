package com.gam.nocr.ems.web.listreader.processor;

import gampooya.tools.vlp.ListException;
import gampooya.tools.vlp.ValueListHandler;
import gampooya.tools.vlp.ValueListProvider;

import java.util.Map;

import org.slf4j.Logger;

import com.gam.commons.core.BaseLog;
import com.gam.commons.listreader.ListReaderException;
import com.gam.commons.listreader.ParameterProvider;

/**
 * The list reader processor class for card request grid
 * 
 * @author Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public class Estelam2LogProcessor extends EMSVLPListProcessor {

	private static final Logger logger = BaseLog
			.getLogger(Estelam2LogProcessor.class);

	protected ValueListHandler prepareVLH(ParameterProvider paramProvider)
			throws ListReaderException {

		StringBuilder part = new StringBuilder();
		// Retrieve a list of parameters sent by client
		Map parameters = getQueryParrameters(paramProvider);

		ValueListProvider vlp = getValueListProvider();
		String orderBy = getOrderBy(paramProvider);
		ValueListHandler vlh;
		try {
			vlh = vlp.loadList("estelam2LogList", ("main" + part).split(","),
					("count" + part).split(","), parameters, orderBy, null);
		} catch (ListException e) {
			throw new ListReaderException(
					"Unable to prepare a VLH to fetch list named '"
							+ paramProvider.getListName() + "'", e);
		}
		return vlh;
	}
}
