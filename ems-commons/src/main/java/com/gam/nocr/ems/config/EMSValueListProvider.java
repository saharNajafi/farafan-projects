package com.gam.nocr.ems.config;

import gampooya.tools.vlp.ExtValueListProvider;

import java.net.URL;

import org.slf4j.Logger;

import com.gam.commons.core.BaseLog;

/**
 * An implementation of {@link gampooya.tools.vlp.ValueListProvider} that specifies the exact address of VLP
 * configuration file
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class EMSValueListProvider extends ExtValueListProvider {

    private static final Logger _log = BaseLog.getLogger(EMSValueListProvider.class);

    /**
     * The singleton instance of the {@link gampooya.tools.vlp.ValueListProvider} for EMS
     */
    private static ExtValueListProvider vlp;

    static {
        vlp = new EMSValueListProvider(true);
    }

    private EMSValueListProvider() {
        super();
    }

    private EMSValueListProvider(boolean showCompleteLog) {
        super(showCompleteLog);
    }

    /**
     * Returns the singleton instance of {@link gampooya.tools.vlp.ValueListProvider} of the EMS
     *
     * @return the singleton instance of {@link gampooya.tools.vlp.ValueListProvider}
     */
    public static ExtValueListProvider getProvider() {
        return vlp;
    }

    /**
     * Returns the fully qualified address of the VLPConfig.xml file in project. This method would be called by the
     * parent class in order to setup value list provider of the project
     *
     * @return The URL address of the VLPConfig.xml file of the project
     */
    @Override
    protected URL getListConfiguration() {
        URL url = null;
        _log.info("getListConfiguration invoked from EMSValueListProvider");
        try {
            url = Thread.currentThread().getContextClassLoader().getResource("com/gam/nocr/ems/config/VLPConfig.xml");
            if (url == null) {
                url = this.getClass().getClassLoader().getResource("com/gam/nocr/ems/config/VLPConfig.xml");
            }

            if (url == null) {
                _log.info("finding VLPConfig for myListReader: URL=null ");
            } else {
                _log.info("finding VLPConfig for myListReader: " + url.toString());
            }
        } catch (Exception ex) {
            _log.error("finding VLPConfig for myListReader failed!:" + ex.getMessage());
        }
        return url;
    }
}