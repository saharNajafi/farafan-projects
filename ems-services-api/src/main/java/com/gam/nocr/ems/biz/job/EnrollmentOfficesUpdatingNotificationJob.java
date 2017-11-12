package com.gam.nocr.ems.biz.job;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.nocr.ems.biz.delegator.EnrollmentOfficeDelegator;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.data.domain.EnrollmentOfficeTO;
import org.quartz.*;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Notifies CMS and Portal about a change in enrollment offices (e.g. add, delete or update)
 *
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class EnrollmentOfficesUpdatingNotificationJob  extends BaseEmsJob implements InterruptableJob {
    private static final Logger jobLogger = BaseLog.getLogger("enrollmentOfficesUpdatingNotification");

    private boolean isJobInterrupted = false;
    private JobKey jobKey = null;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        startLogging(jobLogger);
        jobKey = jobExecutionContext.getJobDetail().getKey();

        EnrollmentOfficeDelegator enrollmentOfficeDelegator = new EnrollmentOfficeDelegator();

        try {
            Map<String, List<EnrollmentOfficeTO>> enrollmentOffices = enrollmentOfficeDelegator.fetchModifiedEnrollmentOffices();

            //  List of enrollment offices updated successfully/unsuccessfully in other systems so they can be used in
            //  second part of the method to update the last sync date of items
            List<Long> newOfficeSuccess = new ArrayList<Long>();
            List<Long> newOfficeFailure = new ArrayList<Long>();
            List<Long> editedOfficeSuccess = new ArrayList<Long>();
            List<Long> editedOfficeFailure = new ArrayList<Long>();
            List<Long> deletedOfficeSuccess = new ArrayList<Long>();
            List<Long> deletedOfficeFailure = new ArrayList<Long>();


            for (EnrollmentOfficeTO newEnrollmentOffice : enrollmentOffices.get("newEnrollmentOffice")) {
                if (!isJobInterrupted) {
                    try {
                        newOfficeSuccess.add(enrollmentOfficeDelegator.
                                notifySubSystemsAboutEnrollmentOffices(newEnrollmentOffice, "NEW"));
                        debug("successfully updated new enrollment office in CMS and CCOS with id : " + newEnrollmentOffice.getId());
                    } catch (BaseException e) {
                        error("updating enrollment office information encounter error with id : " + newEnrollmentOffice.getId(),null);
                        logException(e);
                        newOfficeFailure.add(newEnrollmentOffice.getId());
                    }
                } else {
                    break;
                }
            }
            for (EnrollmentOfficeTO modifiedEnrollmentOffice : enrollmentOffices.get("modifiedEnrollmentOffice")) {
                if (!isJobInterrupted) {
                    try {
                        editedOfficeSuccess.add(enrollmentOfficeDelegator.
                                notifySubSystemsAboutEnrollmentOffices(modifiedEnrollmentOffice, "EDIT"));

                        debug("successfully updated modified enrollment office in CMS and CCOS with id : " + modifiedEnrollmentOffice.getId());
                    } catch (BaseException e) {
                        error("updating enrollment office information encounter error with id : " + modifiedEnrollmentOffice.getId());
                       logException(e);
                        editedOfficeFailure.add(modifiedEnrollmentOffice.getId());
                    }
                } else {
                    break;
                }
            }
            for (EnrollmentOfficeTO deletedEnrollmentOffice : enrollmentOffices.get("deletedEnrollmentOffice")) {
                if (!isJobInterrupted) {
                    try {
                        deletedOfficeSuccess.add(enrollmentOfficeDelegator.
                                notifySubSystemsAboutEnrollmentOffices(deletedEnrollmentOffice, "DELETE"));

                        debug("successfully updated deleted enrollment office in CMS and CCOS with id : " + deletedEnrollmentOffice.getId());
                    } catch (BaseException e) {
                        error("updating enrollment office information encounter error with id : " + deletedEnrollmentOffice.getId());
                        logException(e);
                        deletedOfficeFailure.add(deletedEnrollmentOffice.getId());
                    }
                } else {
                    break;
                }
            }

            Map<String, List<Long>> updatedEnrollmentOffice = new HashMap<String, List<Long>>();
            updatedEnrollmentOffice.put("newOfficeSuccess", newOfficeSuccess);
            updatedEnrollmentOffice.put("newOfficeFailure", newOfficeFailure);
            updatedEnrollmentOffice.put("editedOfficeSuccess", editedOfficeSuccess);
            updatedEnrollmentOffice.put("editedOfficeFailure", editedOfficeFailure);
            updatedEnrollmentOffice.put("deletedOfficeSuccess", deletedOfficeSuccess);
            updatedEnrollmentOffice.put("deletedOfficeFailure", deletedOfficeFailure);

            if (!isJobInterrupted)
                enrollmentOfficeDelegator.updateSyncDateByCurrentDate(updatedEnrollmentOffice);


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
