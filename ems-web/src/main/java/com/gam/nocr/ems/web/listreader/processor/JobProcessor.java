package com.gam.nocr.ems.web.listreader.processor;

import com.gam.commons.core.BaseLog;
import com.gam.commons.listreader.ListReaderException;
import com.gam.commons.listreader.ListResult;
import com.gam.commons.listreader.ParameterProvider;
import com.gam.commons.listreader.processor.ListProcessor;
import com.gam.commons.scheduler.SchedulerService;
import com.gam.nocr.ems.data.domain.vol.JobVTO;
import org.quartz.*;
import org.slf4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The list reader processor class for jobs grid
 *
 * @author Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public class JobProcessor implements ListProcessor {
    private static final Logger logger = BaseLog.getLogger(JobProcessor.class);

    @Override
    public void prepareList(ParameterProvider paramProvider) throws ListReaderException {

    }

    /**
     * As this grid is not going to be filled through database query, there is no need to implement this method
     *
     * @param paramProvider Encapsulates all parameters required to generate the result list
     * @return  Collection of transfer object that should be serialized to be send to the client
     * @throws ListReaderException
     */
    @Override
    public Collection executeMainQuery(ParameterProvider paramProvider) throws ListReaderException {
        return null;
    }

    /**
     /**
     * As this grid is not going to be filled through database query, there is no need to implement this method
     *
     * @param paramProvider Encapsulates all parameters required to generate the result list
     * @return  Collection of transfer object that should be serialized to be send to the client
     * @throws ListReaderException
     */
    @Override
    public int executeCountQuery(ParameterProvider paramProvider) throws ListReaderException {
        return 0;
    }

    /**
     * Prepares a list of system jobs (using Quartz API) to be displayed in 3S GUI.
     *
     * @param paramProvider Encapsulates all parameters required to generate the result list
     * @return  The result object containing list of jobs to send to the caller
     * @throws ListReaderException
     */
    @Override
    public ListResult fetchList(ParameterProvider paramProvider) throws ListReaderException {
        HttpServletRequest request = paramProvider.getRequest();
        if (request != null) {
            ServletContext servletContext = request.getSession(true).getServletContext();
            Object obj = servletContext.getAttribute("scheduler");
            if (obj == null) {
                return null;
            }
            SchedulerService schedulerService = (SchedulerService) obj;
            Scheduler scheduler = schedulerService.getScheduler();
            List<JobDetail> jobDetails = schedulerService.getJobsList(scheduler);
            if (jobDetails == null)
                return null;

            List<JobVTO> jobVTOs = new ArrayList<JobVTO>();
            List<Trigger> triggers;
            Long i = 0l;
            for (JobDetail detail : jobDetails) {
                JobVTO vto = new JobVTO();
                vto.setId(++i);
                vto.setName(detail.getKey().getName());
                vto.setDescription(detail.getDescription());
                try {
                    triggers = (List<Trigger>) scheduler.getTriggersOfJob(detail.getKey());
                    if (triggers == null || triggers.size() == 0) {
                        logger.info("No triggers were found for job " + detail.getKey().getName());
                    } else {
                        for (Trigger trigger : triggers) {
                            if (triggers.size() > 1) {
                                if (trigger instanceof SimpleTrigger) {
                                    vto.setState(schedulerService.getScheduler().getTriggerState(trigger.getKey()).name());
                                }
                            } else {
                                vto.setState(schedulerService.getScheduler().getTriggerState(trigger.getKey()).name());
                            }

                            if (trigger instanceof CronTrigger) {
                                vto.setCronState(schedulerService.getScheduler().getTriggerState(trigger.getKey()).name());
                                vto.setPreviousFireTime(schedulerService.getScheduler().getTrigger(trigger.getKey()).getPreviousFireTime());
                                vto.setNextFireTime(schedulerService.getScheduler().getTrigger(trigger.getKey()).getNextFireTime());
                                vto.setCron(((CronTrigger) trigger).getCronExpression());
                            } else if (trigger instanceof SimpleTrigger) {
                                vto.setSimpleState(schedulerService.getScheduler().getTriggerState(trigger.getKey()).name());
                            }
                        }
                    }
                } catch (SchedulerException e) {
                    logger.info("Exception when using triggers of job " + detail.getKey().getName());
                }
                jobVTOs.add(vto);
            }
            return new ListResult(paramProvider.getListName(), jobVTOs.size(), jobVTOs);
        }
        return null;
    }
}
