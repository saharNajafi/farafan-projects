package com.gam.nocr.ems.web.autocomplete;

import com.gam.commons.autocomplete.AutocompleteTO;
import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.data.domain.SearchResult;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.delegator.DepartmentDelegator;
import com.gam.nocr.ems.data.domain.EMSAutocompleteTO;
import org.slf4j.Logger;

import java.util.Map;

/**
 * Handler for department autocomplete used in 3S forms like Person edit form
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class DepartmentAutocomplete extends BaseAutocompleteHandler {

    private static final Logger logger = BaseLog.getLogger(DepartmentAutocomplete.class);

    private DepartmentDelegator departmentDelegator = new DepartmentDelegator();

    @Override
    protected String getAutocompleteProfileKeyName() {
        return "department";
    }

    @Override
    protected AutocompleteTO getNewAutocompleteTO() {
        return new EMSAutocompleteTO();
    }

    /**
     * Fetches the list of departments having given searchString in their title
     *
     * @param searchString     The search term specified by user in autocomplete textbox
     * @param from             The start index to fetch the result from (used by client side for paging)
     * @param to               The end index to fetch the result (used by client side for paging)
     * @param depId            The department of current user that may be used in underlying query
     * @param perId            The identifier of current user that may be used in underlying query
     * @param orderBy          The ordering of result as requested by client
     * @param additionalParams A Map of additional parameters that may be needed in underlying query to filter results
     * @return List of items resulted from given query parameters
     */
    @Override
    public SearchResult getSearchResult(String searchString, int from, int to, Integer depId, Integer perId, String orderBy, Map additionalParams) {
        UserProfileTO userProfile = new UserProfileTO();
        if (perId != null)
            userProfile.setPersonID(perId);
        if (depId != null)
            userProfile.setDepID(depId);

        try {
            return departmentDelegator.fetchDepartments(userProfile, searchString, from, to, orderBy);
        } catch (BaseException e) {
            logger.info("", e);
            return null;
        } catch (Exception e) {
            logger.info("", e);
            return null;
        }
    }
}
