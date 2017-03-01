package com.gam.nocr.ems.web.listener;

import com.gam.commons.scheduler.SchedulerService;
import com.gam.commons.scheduler.impl.SchedulerServiceImpl;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Initializes the quartz scheduler service on system startup and shuts it down while system is going to stop
 *
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */

public class EMSSchedulerStartupListener implements ServletContextListener {

    SchedulerService schedulerService = new SchedulerServiceImpl();

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //todo: this is for testing jobs using jobs.jsp, it's not used in production mode and can be removed
        servletContextEvent.getServletContext().setAttribute("scheduler", schedulerService);
        schedulerService.initialize();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        schedulerService.tearDown(schedulerService.getScheduler());
    }
}
