package com.gam.nocr.ems.web.autocomplete;

import com.gam.commons.autocomplete.AutocompleteHandler;
import com.gam.commons.core.BaseLog;
import com.gam.commons.profile.ProfileException;
import com.gam.commons.profile.ProfileManager;
import com.gam.nocr.ems.config.ProfileHelper;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.config.WebExceptionCode;
import org.slf4j.Logger;

/**
 * The base class for all autocomplete handlers of the projcet
 *
 * @author Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public abstract class BaseAutocompleteHandler extends AutocompleteHandler {

    private static final Logger logger = BaseLog.getLogger(BaseAutocompleteHandler.class);

    /**
     * Returns a singleton instance of project's {@link com.gam.commons.profile.ProfileManager} to fetch configurations
     *
     * @return a singleton instance of project's {@link com.gam.commons.profile.ProfileManager} to fetch configurations
     */
    @Override
    protected ProfileManager getProfileManager() {
        try {
            return ProfileHelper.getProfileManager();
        } catch (ProfileException e) {
            logger.error(WebExceptionCode.GLB_ERR_MSG, e);
            return null;
        }
    }

    /**
     * Returns the prefix of profile keys used to persist autocomplete configurations
     *
     * @return  the prefix of profile keys used to persist autocomplete configurations
     */
    @Override
    protected String getAutocompleteParentProfileKeyName() {
        return ProfileKeyName.AUTO_COMPLETE_ROOT + ".";//"ems.profile.autocomplete.";
    }
}
