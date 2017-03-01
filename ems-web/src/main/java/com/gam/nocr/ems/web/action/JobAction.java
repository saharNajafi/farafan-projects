package com.gam.nocr.ems.web.action;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.web.struts2.extJsController.ActionException;
import com.gam.commons.core.web.struts2.extJsController.ListControllerImpl;
import com.gam.commons.scheduler.SchedulerService;
import com.gam.nocr.ems.biz.delegator.JobDelegator;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.vol.JobVTO;
import gampooya.tools.security.BusinessSecurityException;

import java.util.List;

/**
 * Manages the execution of jobs (e.g. start, stop, suspend, etc.)
 *
 * @author Haeri (haeri@gamelectronics.com)
 */
public class JobAction extends ListControllerImpl<JobVTO> {

    /**
     * The key of a job that would be used by operations like 'pause', 'resume', etc.
     */
    private String jobKey;

    private JobDelegator jobDelegator;

    public JobAction() {
        jobDelegator = new JobDelegator();
    }

    @Override
    public void setRecords(List<JobVTO> records) {
        this.records = records;
    }

    /**
     * Returns the singleton instance of {@link com.gam.commons.scheduler.SchedulerService}
     *
     * @return  The singleton instance of {@link com.gam.commons.scheduler.SchedulerService}
     * @throws BaseException
     */
    private SchedulerService getSchedulerService() throws BaseException {
        if (request == null) {
            throw new ActionException(WebExceptionCode.JBA_002, WebExceptionCode.JBA_002_MSG);
        }
        Object obj = request.getServletContext().getAttribute("scheduler");
        if (obj == null) {
            throw new ActionException(WebExceptionCode.JBA_003, WebExceptionCode.JBA_003_MSG);
        }
        return (SchedulerService) obj;
    }

    /**
     * Pauses the cron trigger of a job specified as 'jobKey'
     *
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String pause() throws BaseException {
        try {
            jobDelegator.pause(getUserProfile(), getSchedulerService(), jobKey);
            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.JBA_001, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    /**
     * Resumes the cron trigger of a job specified as 'jobKey'
     *
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String resume() throws BaseException {
        try {
            jobDelegator.resume(getUserProfile(), getSchedulerService(), jobKey);
            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.JBA_004, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    /**
     * Tries to run a job specified as 'jobKey'
     *
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String run() throws BaseException {
        try {
            jobDelegator.run(getUserProfile(), getSchedulerService(), jobKey);
            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.JBA_005, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    /**
     * Updates the cron expression of a job specified as 'jobKey'
     *
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String save() throws BaseException {
        try {
            for (JobVTO vto : records) {
                jobDelegator.update(getUserProfile(), getSchedulerService(), vto.getName(), vto.getCron());
            }
            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.JBA_006, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    /**
     * Interrupts the current execution of a job specified as 'jobKey'
     *
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String interrupt() throws BaseException {
        try {
            jobDelegator.interrupt(getUserProfile(), getSchedulerService(), jobKey);
            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.JBA_005, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    public String getJobKey() {
        return jobKey;
    }

    public void setJobKey(String jobKey) {
        this.jobKey = jobKey;
    }
}
