package com.gam.nocr.ems.web.listreader.processor;

import gampooya.tools.vlp.ListException;
import gampooya.tools.vlp.ValueListHandler;
import gampooya.tools.vlp.ValueListProvider;

import java.util.HashMap;

import com.gam.commons.listreader.ListReaderException;
import com.gam.commons.listreader.ParameterProvider;

public class HelpProcessor extends EMSVLPListProcessor {
	protected ValueListHandler prepareVLH(ParameterProvider paramProvider)
			throws ListReaderException {
		HashMap parameters = new HashMap();
		String parts = "";
		ValueListProvider vlp = getValueListProvider();
		String orderBy = getOrderBy(paramProvider);

		ValueListHandler vlh = null;
		try {
			vlh = vlp.loadList("emsHelpFilesList", ("main" + parts).split(","),
					("count" + parts).split(","), parameters, orderBy, null);
		} catch (ListException e) {
			throw new ListReaderException(
					"Unable to prepare a VLH to fetch list named '"
							+ paramProvider.getListName() + "'", e);
		}
		return vlh;
	}
}
