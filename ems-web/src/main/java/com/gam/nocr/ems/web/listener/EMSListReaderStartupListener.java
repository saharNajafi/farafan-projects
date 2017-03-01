package com.gam.nocr.ems.web.listener;

import com.gam.commons.listreader.ListReaderStartupListener;

/**
 * The list reader startup listener which specifies the address of project's list reader configuration file by
 * overriding {@link com.gam.commons.listreader.ListReaderStartupListener#getConfigURLString()} ()}
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class EMSListReaderStartupListener extends ListReaderStartupListener {

    public void contextInitialized(javax.servlet.ServletContextEvent servletContextEvent) {
        super.contextInitialized(servletContextEvent);
        servletContextEvent.getServletContext().setInitParameter("javax.xml.parsers.DocumentBuilderFactory", "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
    }

    /**
     * Returns the address of list reader configuration file in project
     *
     * @return the address of list reader configuration file in project
     */
    @Override
    protected String getConfigURLString() {
        return "com/gam/nocr/ems/web/listreader/ems-listreader-config.xml";
    }
}