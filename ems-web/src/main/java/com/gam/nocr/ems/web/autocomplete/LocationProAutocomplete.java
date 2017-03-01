package com.gam.nocr.ems.web.autocomplete;

import java.util.Map;

import org.slf4j.Logger;

import com.gam.commons.autocomplete.AutocompleteTO;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.data.domain.SearchResult;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.delegator.LocationDelegator;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.EMSAutocompleteTO;

public class LocationProAutocomplete extends BaseAutocompleteHandler {

	private static final Logger logger = BaseLog
			.getLogger(LocationProAutocomplete.class);

	@Override
	protected String getAutocompleteProfileKeyName() {
		return "locationPro";
	}

	@Override
	protected AutocompleteTO getNewAutocompleteTO() {
		return new EMSAutocompleteTO();
	}

	@Override
	public SearchResult getSearchResult(String searchString, int from, int to,
			Integer depId, Integer perId, String orderBy) {
		return null;
	}

	@Override
	public SearchResult getSearchResult(String searchString, int from, int to,
			Integer depId, Integer perId, String orderBy, Map additionalParams) {
		try {
			UserProfileTO userProfile = new UserProfileTO();
			if (perId != null)
				userProfile.setPersonID(perId);
			if (depId != null)
				userProfile.setDepID(depId);
			LocationDelegator locationDelegator = new LocationDelegator();

			return locationDelegator.getAllProvinces(userProfile, searchString,
					from, to, orderBy, additionalParams);

		} catch (Throwable t) {
			logger.error(WebExceptionCode.GLB_ERR_MSG, t);
		}
		return null;
	}
}
