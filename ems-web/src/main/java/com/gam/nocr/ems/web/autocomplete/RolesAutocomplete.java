package com.gam.nocr.ems.web.autocomplete;

import com.gam.commons.autocomplete.AutocompleteTO;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.data.domain.SearchResult;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.delegator.PersonDelegator;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.EMSAutocompleteTO;
import org.slf4j.Logger;

import java.util.Map;

/**
 * Handler for roles autocomplete used in 3S forms like person edit form. It calls GAAS web services to fetch the
 * results
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class RolesAutocomplete extends BaseAutocompleteHandler {

    private static final Logger logger = BaseLog.getLogger(RolesAutocomplete.class);

    @Override
    protected String getAutocompleteProfileKeyName() {
        return "roles";
    }

    @Override
    protected AutocompleteTO getNewAutocompleteTO() {
        return new EMSAutocompleteTO();
    }


    @Override
    public SearchResult getSearchResult(String searchString, int from, int to, Integer depId, Integer perId, String orderBy, Map additionalParams) {
        try {
            UserProfileTO userProfile = new UserProfileTO();
            if (perId != null)
                userProfile.setPersonID(perId);
            if (depId != null)
                userProfile.setDepID(depId);

            PersonDelegator personDelegator = new PersonDelegator();

            logger.info("++++++++++++++++++++++++++++++++++++++");
            logger.info("++++++++++++++++++++++++++++++++++++++");
            return personDelegator.fetchRoles(userProfile, searchString, from, to, orderBy);

        } catch (Throwable t) {
            logger.error(WebExceptionCode.GLB_ERR_MSG, t);
        }
        return null;
    }
}
