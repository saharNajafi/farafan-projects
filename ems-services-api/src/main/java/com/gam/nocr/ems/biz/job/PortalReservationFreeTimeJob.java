package com.gam.nocr.ems.biz.job;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.nocr.ems.biz.delegator.EnrollmentOfficeDelegator;
import com.gam.nocr.ems.biz.delegator.PortalManagementDelegator;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.util.EmsUtil;
import com.gam.nocr.ems.util.StringUtils;
import org.quartz.*;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class PortalReservationFreeTimeJob extends BaseEmsJob implements InterruptableJob {

    private static final Logger jobLOGGER = BaseLog
            .getLogger("portalReservationFreeTime");

    private static final String DEFAULT_NUMBER_OF_EOF_TO_UPDATE = "5";

    private boolean isJobInterrupted = false;
    private JobKey jobKey = null;

    @Override
    public void execute(JobExecutionContext jobExecutionContext)
            throws JobExecutionException {
//        startLogging(jobLOGGER);
//        jobKey = jobExecutionContext.getJobDetail().getKey();
//
//        Integer from = 0;
//        Integer numberOfEOFToUpdate = Integer.valueOf(EmsUtil
//                .getProfileValue(
//                        ProfileKeyName.KEY_NUMBER_OF_EOF_TO_UPDATE,
//                        DEFAULT_NUMBER_OF_EOF_TO_UPDATE));
//        Integer to = numberOfEOFToUpdate;
//
//        try {
//            EnrollmentOfficeDelegator enrollmentOfficeDelegator = new EnrollmentOfficeDelegator();
//            List<Long> eofIds = enrollmentOfficeDelegator.getEnrollmentOfficeListIds();
//
//
//            info("Total number of EOF for updating free Reservation Time " + eofIds.size());
//
//            if (eofIds.size() > 0) {
//                List<List<Long>> batches = new ArrayList<List<Long>>();
//
//                int divison = eofIds.size() / numberOfEOFToUpdate;
//                int remainder = eofIds.size() % numberOfEOFToUpdate;
//
//                for (int i = 0; i < divison; i++) {
//                    List<Long> batch = eofIds.subList(from, to);
//                    batches.add(batch);
//                    from = from + numberOfEOFToUpdate;
//                    to = to + numberOfEOFToUpdate;
//                }
//
//                if (remainder > 0) {
//                    List<Long> batch = eofIds.subList(divison
//                            * numberOfEOFToUpdate, eofIds.size());
//                    batches.add(batch);
//                }
//
//                info("Updating EOF Free Reservation Time in bunch of "
//                        + numberOfEOFToUpdate);
//
//                info("Total number of batch for EOF Free Reservation Time "
//                        + batches.size());
//
//                info("Start to Delete Previous EOF Free Date ... ");
//
//
//                Date dateForDelete = EmsUtil.getDateAtMidnight(incrementDateUtil(new Date(), 1));
//                PortalManagementDelegator portalManagementDelegator = new PortalManagementDelegator();
////                int deleteActionState = portalManagementDelegator.deleteReservationDateFromOfficeRSVFreeTime(dateForDelete.getTime());
//
//
//                int batchNum = 1;
//                for (List<Long> batch : batches) {
//                    if (!isJobInterrupted) {
//                        try {
//                            info("Going to update batch " + batchNum);
//                            // if(deleteActionState == 1)
//                            portalManagementDelegator
//                                    .notifyPortalRezervationFreeTime(batch,
//                                            dateForDelete.getTime());
//                            info("batch " + batchNum
//                                    + " updated successfully");
//                        } catch (Exception e) {
//                            String excepStr = StringUtils
//                                    .getStringFromException(e);
//                            error("An error occurred while updating batch "
//                                    + batchNum + " : " + excepStr);
//                        }
//                    } else {
//                        info("Job execution interrupted. No further processing will be done");
//                        break;
//                    }
//                    batchNum++;
//                }
//            } else {
//                info("No need to update reruest states");
//            }
//
//        } catch (BaseException e) {
//            error("An error occurred in updating state of some requests in portal - "
//                    + e.getExceptionCode() + " : " + e.getMessage());
//        }
//
//        endLogging();
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        error("calling interrupt: jobKey ==> " + jobKey);
        isJobInterrupted = true;
    }

    private Date incrementDateUtil(Date curentDate, Integer increment) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(curentDate);
        cal.add(Calendar.DATE, increment);
        return cal.getTime();
    }
}
