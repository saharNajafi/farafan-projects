package com.gam.nocr.ems.biz.job;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.biz.delegator.EnrollmentOfficeDelegator;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.domain.EnrollmentOfficeTO;
import com.gam.nocr.ems.data.domain.OfficeCapacityTO;
import com.gam.nocr.ems.util.CalendarUtil;
import com.gam.nocr.ems.util.EmsUtil;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.NamingException;
import java.util.*;
import java.util.Calendar;
import java.util.concurrent.*;

/**
 * Created by Saeid Rastak (saeid.rastak@gmail.com) on 4/10/2018.
 */
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class CreateActiveShiftsJob extends BaseEmsJob implements InterruptableJob {

    private static final Logger LOG = LoggerFactory.getLogger("CreateActiveShifts");

    private static boolean isJobInterrupted = false;
    private static JobKey jobKey = null;

    EnrollmentOfficeDelegator enrollmentOfficeDelegator;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        isJobInterrupted = false;
        startLogging(LOG);
        jobKey = context.getJobDetail().getKey();
        try {
            try {
                info("Start creating active shifts in EMS...");
                Integer from = 0;
                Integer numberOfEOFToUpdate = Integer.valueOf(EmsUtil.
                        getProfileValue(ProfileKeyName.KEY_NUMBER_OF_EOF_TO_UPDATE, "5"));
                Integer to = numberOfEOFToUpdate;

                final Date dateForCreateActiveShift = EmsUtil.getDateAtMidnight(new Date());

                List<EnrollmentOfficeTO> enrollmentOfficeList = null;
                try {
                    enrollmentOfficeList = getEnrollmentOfficeDelegator().getEnrollmentOfficeList();
                } catch (BaseException e) {
                    throw e;
                }

                Integer maximumDurationToReserve = Integer.valueOf(EmsUtil.
                        getProfileValue(ProfileKeyName.ACTIVESHIFT_MAXIMUM_DURATION_TO_RESERVE, "60"));

                final Date toDate = EmsUtil.getDateAtMidnight(incrementDateUtil(
                        new Date(), Integer.valueOf(maximumDurationToReserve) + 1));

                if (enrollmentOfficeList.size() > 0) {
                    List<List<EnrollmentOfficeTO>> batches = new ArrayList<List<EnrollmentOfficeTO>>();

                    int divison = enrollmentOfficeList.size() / numberOfEOFToUpdate;
                    int remainder = enrollmentOfficeList.size() % numberOfEOFToUpdate;

                    for (int i = 0; i < divison; i++) {
                        List<EnrollmentOfficeTO> batch = enrollmentOfficeList.subList(from, to);
                        batches.add(batch);
                        from = from + numberOfEOFToUpdate;
                        to = to + numberOfEOFToUpdate;
                    }

                    if (remainder > 0) {
                        List<EnrollmentOfficeTO> batch = enrollmentOfficeList.subList(from, enrollmentOfficeList.size());
                        batches.add(batch);
                    }

                    final Map<Long, List<OfficeCapacityTO>> officeCapacityMap = new HashMap<Long, List<OfficeCapacityTO>>();
                    int endDate = Integer.valueOf(CalendarUtil.getDateWithoutSlash(toDate, new Locale("fa"), "YYYYMMDD"));
                    int startDate = Integer.valueOf(CalendarUtil.getDateWithoutSlash(new Date(), new Locale("fa"), "YYYYMMDD"));
                    List<OfficeCapacityTO> officeCapacityTOs = getEnrollmentOfficeDelegator().listOfficeCapacityByDate(startDate, endDate);
                    for (OfficeCapacityTO officeCapacityTO : officeCapacityTOs) {
                        if (officeCapacityMap.containsKey(officeCapacityTO.getEnrollmentOffice().getId())) {
                            officeCapacityMap.get(officeCapacityTO.getEnrollmentOffice().getId()).add(officeCapacityTO);
                        } else {
                            List<OfficeCapacityTO> capacityTOList = new ArrayList<OfficeCapacityTO>();
                            capacityTOList.add(officeCapacityTO);
                            officeCapacityMap.put(officeCapacityTO.getEnrollmentOffice().getId(), capacityTOList);
                        }
                    }

                    ExecutorService executorService = Executors.newFixedThreadPool(15);
                    CompletionService executorCompletionService = new ExecutorCompletionService<Integer>(executorService);
                    List<Future> futures = new ArrayList<Future>();


                    for (final List<EnrollmentOfficeTO> batch : batches) {
                        futures.add(executorCompletionService.submit(new Callable<Integer>() {
                            @Override
                            public Integer call() {
                                for (EnrollmentOfficeTO enrollmentOfficeTO : batch) {
                                    Date fromDate = dateForCreateActiveShift;

                                    while (fromDate.before(toDate)) {
                                        try {
                                            getEnrollmentOfficeDelegator().updateActiveShiftForEnrollmentOfficeAndDate(enrollmentOfficeTO, fromDate, officeCapacityMap);
                                        } catch (Exception e) {
                                            error(
                                                    "Exception occured while updating enrollmentOffice reservation for eofId = "
                                                            + enrollmentOfficeTO.getId()
                                                            + " and Date = " + fromDate
                                                            + " , message = " + e.getMessage(), e);
                                            if (e instanceof BaseException) {
                                                logException((BaseException) e);
                                            } else {
                                                logGeneralException(e);
                                            }

                                        } finally {
                                            fromDate = incrementDateUtil(fromDate, 1);
                                        }
                                    }

                                }
                                return 1;
                            }
                        }));
                    }

                    try {
                        for (int i = 0; i < futures.size(); i++) {
                            try {
                                futures.get(i).get();
                            } catch (InterruptedException e) {
                                error("InterruptedException in createActiveShift", e);
                            } catch (ExecutionException e) {
                                error("ExecutionException in createActiveShift", e);
                            }
                            // Some processing here
                        }
                    } finally {
                        for (Future future : futures) {
                            future.cancel(true);
                        }
                        System.out.println("attempt to shutdown executor");
                        executorService.shutdownNow();
                    }
                }
            } catch (Exception ex) {
                throw new BaseException(BizExceptionCode.CHG_006, BizExceptionCode.CHG_005_MSG, ex);
            }
            /*if (!result) {
                throw new BaseException(BizExceptionCode.CHG_007, BizExceptionCode.CHG_005_MSG);
            }*/
            info("Creating active shifts in EMS finished successfully.");
        } catch (Exception e) {
            if (e instanceof BaseException) {
                logException((BaseException) e);
            } else {
                error("Error occurred in creating active shifts job: ", e);
            }
        }
        endLogging();
    }


    @Override
    public void interrupt() throws UnableToInterruptJobException {
        System.out.println("calling interrupt: " + jobKey);
        isJobInterrupted = true;
    }

    public EnrollmentOfficeDelegator getEnrollmentOfficeDelegator() throws NamingException {
        if (enrollmentOfficeDelegator == null) {
            enrollmentOfficeDelegator = new EnrollmentOfficeDelegator();
        }
        return enrollmentOfficeDelegator;
    }

    private Date incrementDateUtil(Date curentDate, Integer increment) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(curentDate);
        cal.add(Calendar.DATE, increment);
        return cal.getTime();
    }


}
