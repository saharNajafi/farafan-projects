package com.gam.nocr.ems.web.listener;

import com.tangosol.net.CacheFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * General listener for EMS. Its main use is to shutdown the cache factory
 *
 * @author Haeri (haeri@gamelectronics.com)
 */
public class EMSShutdownListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        // do nothing
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        CacheFactory.shutdown();
    }
}
