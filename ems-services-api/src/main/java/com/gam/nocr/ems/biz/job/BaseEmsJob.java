package com.gam.nocr.ems.biz.job;

import com.gam.commons.core.BaseException;
import org.slf4j.Logger;

import java.util.Date;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 * Created by Saeid on 11/7/2017.
 */
public class BaseEmsJob {
    private long startTime;
    private Logger logger;

    public void info(String message) {
        logger.info(message);
    }

    public void info(String format, Object arg) {
        logger.info(format, arg);
    }

    public void debug(String message) {
        logger.debug(message);
    }

    public void error(String message) {
        Exception ex = null;
        logger.error(message, ex);
    }

    public void error(String message, Exception ex) {
        logger.error(message, ex);
    }

    public void error(String format, Object arg1, Object arg2) {
        logger.error(format, arg1, arg2);
    }

    public void warn(String message) {
        logger.warn(message);
    }

    public void logException(BaseException e) {
        error(e.getExceptionCode() + " : " + e.getMessage(), e);
    }

    public void logGeneralException(Exception e) {
        error(e.getMessage(), e);
    }

    public void startLogging(Logger logger) {
        logger.warn("------->> " + getClass().getName() + " started at " + new Date() + " <<------");
        startTime = System.nanoTime();
        this.logger = logger;
    }

    public void endLogging() {
        long endTime = System.nanoTime();
        logger.warn("------->> " + getClass().getName()
                + " finished at " + new Date() + " -  Duration(" + ((endTime - startTime) / 1000000l) + " ms) <<------");
    }

}
