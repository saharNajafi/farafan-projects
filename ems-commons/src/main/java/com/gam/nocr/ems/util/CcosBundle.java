package com.gam.nocr.ems.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.slf4j.Logger;

import com.gam.commons.core.BaseLog;

/**
 * The utility class to fetch CCOS error messages from underlying property file. All messages that would be displayed
 * for an error message are defined in a property file (named ccos-messages.properties).
 *
 * @author: Haeri (haeri@gamelectronics.com)
 */
public abstract class CcosBundle {

    private static Logger logger = BaseLog.getLogger(CcosBundle.class);

    /**
     * Name of the property file containing CCOS error messages
     */
    private static final String CCOS_BASE_NAME = "ccos-messages";

    /**
     * Contents of property file will be cached in this object
     */
    private static ResourceBundle bundle = null;

    /**
     * Given a name of error code and returns corresponding error message from underlying property file
     *
     * @param key Name of the error to lookup its message (e.g. EMS_D_PDI_001)
     * @return The message that should be displayed to CCOS user
     */
    public static String getMessage(String key) {
        String message = "";
        if (bundle == null) {
            try {
                bundle = ResourceBundle.getBundle(CCOS_BASE_NAME);
            } catch (Exception e) {
                logger.info("CCOS resource bundle not found. Any error messages sent to the client will not be shown.");
            }
        }
        if (bundle != null) {
            try {
                message = bundle.getString(key);
            } catch (NullPointerException e) {
                logger.info("Ccos resource bundle key is null");
            } catch (MissingResourceException e) {
                logger.info("No object found in Ccos resource bundle for key: " + key);
            } catch (Exception e) {
                logger.info("Unable to load value in Ccos resource bundle for key: " + key);
            }
        }
        return message;
    }

}
