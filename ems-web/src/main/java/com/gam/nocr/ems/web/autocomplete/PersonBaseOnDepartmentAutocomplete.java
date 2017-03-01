package com.gam.nocr.ems.web.autocomplete;

import java.util.Map;

import org.slf4j.Logger;

import com.gam.commons.autocomplete.AutocompleteTO;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.data.domain.SearchResult;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.delegator.PersonDelegator;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.EMSAutocompleteTO;

public class PersonBaseOnDepartmentAutocomplete extends BaseAutocompleteHandler {

	private static final Logger logger = BaseLog
			.getLogger(PersonBaseOnDepartmentAutocomplete.class);

	@Override
	protected String getAutocompleteProfileKeyName() {
		return "personBaseOnDepartments";
	}

	@Override
	protected AutocompleteTO getNewAutocompleteTO() {
		return new EMSAutocompleteTO();
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

			PersonDelegator personDelegator = new PersonDelegator();

			return personDelegator.fetchPersons(userProfile, searchString,
					from, to, orderBy, additionalParams);

		} catch (Throwable t) {
			logger.error(WebExceptionCode.GLB_ERR_MSG, t);
		}
		return null;
	}
}