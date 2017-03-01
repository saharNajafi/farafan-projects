package com.gam.nocr.ems.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.slf4j.Logger;

import com.gam.commons.core.BaseLog;


public abstract class CcosRequestStateBundle {

    private static Logger logger = BaseLog.getLogger(CcosRequestStateBundle.class);
    private static final String CCOS_BASE_NAME = "ccos-request-state";
    private static ResourceBundle bundle = null;

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
