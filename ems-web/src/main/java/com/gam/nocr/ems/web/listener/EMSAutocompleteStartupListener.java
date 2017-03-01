package com.gam.nocr.ems.web.listener;

import com.gam.commons.autocomplete.AutocompleteStartupListener;

/**
 * The autocomplete startup listener which specifies the address of project's autocomplete configuration file by
 * overriding {@link com.gam.commons.autocomplete.AutocompleteStartupListener#getConfigURLString()}
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class EMSAutocompleteStartupListener extends AutocompleteStartupListener {

    /**
     * Returns the address of autocomplete configuration file in project
     *
     * @return the address of autocomplete configuration file in project
     */
    @Override
    protected String getConfigURLString() {
        return "config/autocomplete/ems-autocomplete-config.xml";
    }
}
