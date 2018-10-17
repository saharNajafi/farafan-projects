package com.gam.nocr.ems.web.autocomplete;

import com.gam.commons.autocomplete.AutocompleteTO;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.data.domain.SearchResult;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.delegator.FeatureExtractIdsDelegator;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.EMSAutocompleteTO;
import org.slf4j.Logger;

import java.util.Map;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 10/13/18.
 */
public class FeatureExtractIdsAutoComplete extends BaseAutocompleteHandler {

    private static final Logger logger = BaseLog.getLogger(FeatureExtractIdsAutoComplete.class);

    @Override
    protected String getAutocompleteProfileKeyName() {
        return "featureExtractIds";
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
            FeatureExtractIdsDelegator featureExtractIdsDelegator = new FeatureExtractIdsDelegator();
            return featureExtractIdsDelegator.fetchFeatureExtractIdList(userProfile, searchString, from, to, orderBy);
        } catch (Throwable t) {
            logger.error(WebExceptionCode.GLB_ERR_MSG, t);
        }
        return null;
    }
}
