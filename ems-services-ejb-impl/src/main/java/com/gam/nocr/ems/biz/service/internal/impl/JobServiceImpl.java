package com.gam.nocr.ems.biz.service.internal.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.scheduler.SchedulerService;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.config.BizExceptionCode;
import org.quartz.*;
import org.slf4j.Logger;

import javax.ejb.*;
import java.util.List;

import static org.quartz.CronScheduleBuilder.cronSchedule;

/**
 * @author <a href="mailto:haeri@gamelectronics.com.com">Nooshin Haeri</a>
 */
@Stateless(name = "JobService")
@Local(JobServiceLocal.class)
@Remote(JobServiceRemote.class)
public class JobServiceImpl extends EMSAbstractService implements JobServiceLocal, JobServiceRemote {

    private static final Logger logger = BaseLog.getLogger(JobServiceImpl.class);

    @Override
    @TransactionAttribute(TransactionAttributeType.NEVER)
    public void pause(SchedulerService schedulerService, String jobKey) throws BaseException {
        logger.info("+++++++++++++ Trying to pause job " + jobKey);
        if (jobKey == null || jobKey.trim().length() == 0) {
            throw new ServiceException(BizExceptionCode.JBS_001, BizExceptionCode.JBS_001_MSG);
        }
        if (schedulerService == null) {
            throw new ServiceException(BizExceptionCode.JBS_002, BizExceptionCode.JBS_002_MSG);
        }
        if (schedulerService.getScheduler()==null){
            throw new ServiceException(BizExceptionCode.JBS_026, BizExceptionCode.JBS_026_MSG);
        }
        List<JobDetail> jobDetails = schedulerService.getJobsList(schedulerService.getScheduler());
        for (JobDetail job : jobDetails) {
            if (job.getKey().getName().equals(jobKey)) {
                try {
                    List<Trigger> triggers = (List<Trigger>) schedulerService.getScheduler().getTriggersOfJob(job.getKey());

                    for (Trigger trigger : triggers) {
                        if (trigger instanceof CronTrigger) {
                            schedulerService.pauseTrigger(schedulerService.getScheduler(), trigger.getKey());
                        }
                    }
                } catch (Exception e) {
                    logger.info("+++++++++++++ Exception thrown when trying to pause job");
                    throw new ServiceException(BizExceptionCode.JBS_009, BizExceptionCode.JBS_009_MSG, e);
                }
                logger.info("+++++++++++++ Job paused");
                return;
            }
        }
        logger.info("+++++++++++++ Job not found in job list");
        throw new ServiceException(BizExceptionCode.JBS_010, BizExceptionCode.JBS_010_MSG);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NEVER)
    public void resume(SchedulerService schedulerService, String jobKey) throws BaseException {
        logger.info("+++++++++++++ Trying to resume job " + jobKey);
        if (jobKey == null || jobKey.trim().length() == 0) {
            throw new ServiceException(BizExceptionCode.JBS_003, BizExceptionCode.JBS_003_MSG);
        }
        if (schedulerService == null) {
            throw new ServiceException(BizExceptionCode.JBS_004, BizExceptionCode.JBS_004_MSG);
        }
        if (schedulerService.getScheduler()==null){
            throw new ServiceException(BizExceptionCode.JBS_026, BizExceptionCode.JBS_026_MSG);
        }
        List<JobDetail> jobDetails = schedulerService.getJobsList(schedulerService.getScheduler());
        for (JobDetail job : jobDetails) {
            if (job.getKey().getName().equals(jobKey)) {
                try {
                    List<Trigger> triggers = (List<Trigger>) schedulerService.getScheduler().getTriggersOfJob(job.getKey());

                    for (Trigger trigger : triggers) {
                        if (trigger instanceof CronTrigger) {
                            schedulerService.resumeTrigger(schedulerService.getScheduler(), trigger.getKey());
                        }
                    }
                } catch (Exception e) {
                    logger.info("+++++++++++++ Exception thrown when trying to resume job");
                    throw new ServiceException(BizExceptionCode.JBS_011, BizExceptionCode.JBS_011_MSG, e);
                }
                logger.info("+++++++++++++ Job resumed");
                return;
            }
        }
        logger.info("+++++++++++++ Job not found in job list");
        throw new ServiceException(BizExceptionCode.JBS_012, BizExceptionCode.JBS_012_MSG);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NEVER)
    public void run(SchedulerService schedulerService, String jobKey) throws BaseException {
        logger.info("+++++++++++++ Trying to run job " + jobKey);
        if (jobKey == null || jobKey.trim().length() == 0) {
            throw new ServiceException(BizExceptionCode.JBS_005, BizExceptionCode.JBS_005_MSG);
        }
        if (schedulerService == null) {
            throw new ServiceException(BizExceptionCode.JBS_006, BizExceptionCode.JBS_006_MSG);
        }
        if (schedulerService.getScheduler()==null){
            throw new ServiceException(BizExceptionCode.JBS_026, BizExceptionCode.JBS_026_MSG);
        }
        List<JobDetail> jobDetails = schedulerService.getJobsList(schedulerService.getScheduler());
        for (JobDetail job : jobDetails) {
            if (job.getKey().getName().equals(jobKey)) {
                List<JobExecutionContext> contexts;
                try {
                    contexts = schedulerService.getScheduler().getCurrentlyExecutingJobs();
                    if (contexts != null) {
                        for (JobExecutionContext context : contexts) {
                            if (job.getKey().getName().equals(context.getJobDetail().getKey().getName())) {
                                throw new ServiceException(BizExceptionCode.JBS_021, BizExceptionCode.JBS_021_MSG);
                            }
                        }
                    }
                    schedulerService.getScheduler().triggerJob(job.getKey());
                } catch (SchedulerException e) {
                    logger.info("+++++++++++++ Exception thrown when trying to run job");
                    throw new ServiceException(BizExceptionCode.JBS_013, BizExceptionCode.JBS_013_MSG, e);
                }
                logger.info("+++++++++++++ Job running");
                return;
            }
        }
        logger.info("+++++++++++++ Job not found in job list");
        throw new ServiceException(BizExceptionCode.JBS_014, BizExceptionCode.JBS_014_MSG);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NEVER)
    public void update(SchedulerService schedulerService, String jobKey, String cron) throws BaseException {
        logger.info("+++++++++++++ Trying to update cron for job " + jobKey);
        if (jobKey == null || jobKey.trim().length() == 0) {
            throw new ServiceException(BizExceptionCode.JBS_007, BizExceptionCode.JBS_007_MSG);
        }
        if (schedulerService == null) {
            throw new ServiceException(BizExceptionCode.JBS_008, BizExceptionCode.JBS_008_MSG);
        }
        if (schedulerService.getScheduler()==null){
            throw new ServiceException(BizExceptionCode.JBS_026, BizExceptionCode.JBS_026_MSG);
        }
        List<JobDetail> jobDetails = schedulerService.getJobsList(schedulerService.getScheduler());
        JobKey key = null;
        for (JobDetail jobDetail : jobDetails) {
            if (jobDetail.getKey().getName().equals(jobKey)) {
                logger.info("+++++++++++++ Job found for key name " + jobKey);
                key = jobDetail.getKey();
                break;
            }
        }
        if (key == null) {
            logger.info("+++++++++++++ Job not found for key name " + jobKey);
            throw new ServiceException(BizExceptionCode.JBS_015, BizExceptionCode.JBS_015_MSG);
        }

        List<Trigger> triggers;
        Scheduler scheduler = schedulerService.getScheduler();
        try {
            triggers = (List<Trigger>) scheduler.getTriggersOfJob(key);
        } catch (SchedulerException e) {
            logger.info("+++++++++++++ Exception when trying to load triggers for job key " + jobKey);
            throw new ServiceException(BizExceptionCode.JBS_016, BizExceptionCode.JBS_016_MSG, e, new String[]{jobKey});
        }

        if (triggers == null || triggers.size() == 0) {
            logger.info("+++++++++++++ No trigger found for job key " + jobKey);
            throw new ServiceException(BizExceptionCode.JBS_017, BizExceptionCode.JBS_017_MSG, new String[]{jobKey});
        }

        if (triggers.size() > 1) {
            logger.info("+++++++++++++ Multiple triggers found for job key " + jobKey);
            throw new ServiceException(BizExceptionCode.JBS_018, BizExceptionCode.JBS_018_MSG, new String[]{jobKey});
        }

        Trigger oldTrigger = triggers.get(0);

        // obtain a builder that would produce the trigger
        TriggerBuilder tb = oldTrigger.getTriggerBuilder();
        Trigger newTrigger = null;

        try {
            // update the schedule associated with the builder, and build the new trigger
            // (other builder methods could be called, to change the trigger in any desired way)
            newTrigger = tb.withSchedule(cronSchedule(cron)).build();
        } catch (Exception e) {
            logger.info("+++++++++++++ Illegal cron expression format for job key " + jobKey);
            throw new ServiceException(BizExceptionCode.JBS_019, BizExceptionCode.JBS_019_MSG, e, new String[]{jobKey});
        }
        try {
            scheduler.rescheduleJob(oldTrigger.getKey(), newTrigger);
        } catch (SchedulerException e) {
            logger.info("+++++++++++++ Problem when trying to reschedule job " + jobKey);
            throw new ServiceException(BizExceptionCode.JBS_020, BizExceptionCode.JBS_020_MSG, e, new String[]{jobKey});
        }

        logger.info("+++++++++++++ Job cron updated");
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NEVER)
    public void interrupt(SchedulerService schedulerService, String jobKey) throws BaseException {
        logger.info("+++++++++++++ Trying to interrupt job " + jobKey);
        if (jobKey == null || jobKey.trim().length() == 0) {
            throw new ServiceException(BizExceptionCode.JBS_022, BizExceptionCode.JBS_005_MSG);
        }
        if (schedulerService == null) {
            throw new ServiceException(BizExceptionCode.JBS_023, BizExceptionCode.JBS_006_MSG);
        }
        if (schedulerService.getScheduler()==null){
            throw new ServiceException(BizExceptionCode.JBS_026, BizExceptionCode.JBS_026_MSG);
        }
        List<JobDetail> jobDetails = schedulerService.getJobsList(schedulerService.getScheduler());
        for (JobDetail job : jobDetails) {
            if (job.getKey().getName().equals(jobKey)) {
                try {
                    schedulerService.getScheduler().interrupt(job.getKey());
                } catch (SchedulerException e) {
                    logger.info("+++++++++++++ Exception thrown when trying to interrupt job");
                    throw new ServiceException(BizExceptionCode.JBS_024, BizExceptionCode.JBS_024_MSG, e);
                }
                logger.info("+++++++++++++ Job interrupted");
                return;
            }
        }
        logger.info("+++++++++++++ Job not found in job list");
        throw new ServiceException(BizExceptionCode.JBS_025, BizExceptionCode.JBS_014_MSG);
    }
}
