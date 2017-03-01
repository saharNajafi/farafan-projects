package com.gam.nocr.ems.config;

import com.gam.commons.profile.ProfileException;
import com.gam.commons.profile.ProfileManager;

/**
 * The utility class to create an instance of {@link com.gam.commons.profile.ProfileManager} to access profile values in
 * EMS
 *
 * @author Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public class ProfileHelper {

    /**
     * The only instance of the {@link com.gam.commons.profile.ProfileManager} that would be used as a singleton in EMS
     */
    private static ProfileManager pm = null;

    private static String systemName = null;
    private static String systemNameAbr = null;

    /**
     * Initializes internal properties that would be necessary in creating {@link com.gam.commons.profile.ProfileManager}
     */
    private static void initSystemName() {
        if (systemName == null || systemName.trim().length() == 0) {
            systemName = "ems";
        }
        if (systemNameAbr == null || systemNameAbr.trim().length() == 0) {
            systemNameAbr = "ems";
        }
    }

    private ProfileHelper() {
    }

    /**
     * Returns the singleton instance of the {@link com.gam.commons.profile.ProfileManager}
     *
     * @return the singleton instance of the {@link com.gam.commons.profile.ProfileManager}
     * @throws ProfileException
     */
    public static ProfileManager getProfileManager() throws ProfileException {
        if (pm == null) {
            initSystemName();
            pm = new ProfileManager(systemName, "jdbc/GamNocrEmsOracleDS");
        }
        return pm;
    }
}
