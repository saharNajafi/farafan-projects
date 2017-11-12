package com.gam.nocr.ems.biz.job;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.nocr.ems.biz.delegator.PortalManagementDelegator;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.util.EmsUtil;
import org.quartz.*;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Transfers reservation information from portal
 *
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class PortalReservationsJob extends BaseEmsJob implements InterruptableJob {

    private static final Logger jobLogger = BaseLog.getLogger("PortalReservationsJob");

    private static final String DEFAULT_NUMBER_OF_PORTAL_RESERVATION_TO_LOAD = "10";

    private boolean isJobInterrupted = false;
    private JobKey jobKey = null;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        startLogging(jobLogger);
        jobKey = jobExecutionContext.getJobDetail().getKey();

        try {
            PortalManagementDelegator portalManagementDelegator = new PortalManagementDelegator();

            //  Fetch a list of portal reservations that are ready to be transferred to EMS
            List<Long> portalReservationIds = portalManagementDelegator.fetchReservationIds();

            if (EmsUtil.checkListSize(portalReservationIds)) {
                Integer reservationCount = Integer.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_NUMBER_OF_PORTAL_RESERVATION_TO_LOAD,
                        DEFAULT_NUMBER_OF_PORTAL_RESERVATION_TO_LOAD));

                Integer loopCount = portalReservationIds.size() / reservationCount;
                Integer modular = portalReservationIds.size() % reservationCount;

                List<List<Long>> list = new ArrayList<List<Long>>();
                for (int i = 0; i < loopCount; i++) {
                    if (!isJobInterrupted) {
                        list.add(portalReservationIds.subList(i * reservationCount, (i * reservationCount) + reservationCount));
                    } else {
                        break;
                    }
                }

                if (modular > 0 && !isJobInterrupted)
                    list.add(portalReservationIds.subList(reservationCount * loopCount, (reservationCount * loopCount) + modular));

                for (List<Long> longList : list) {
                    if (!isJobInterrupted) {
                        try {
                            portalManagementDelegator.transferReservationsToEMSAndDoEstelam2(longList);
                        } catch (BaseException e) {
                            logException(e);
                        }
                    } else {
                        break;
                    }
                }

            }
        } catch (BaseException e) {
            logException(e);
        } catch (Exception e) {
            logGenerakException(e);
        }
        endLogging();
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        error("calling interrupt: jobKey ==> " + jobKey);
        isJobInterrupted = true;
    }
}
