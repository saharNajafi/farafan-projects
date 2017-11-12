package com.gam.nocr.ems.biz.job;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.nocr.ems.biz.delegator.CardRequestDelegator;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.util.EmsUtil;
import org.quartz.*;
import org.slf4j.Logger;

import java.util.List;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class PurgeBiometricAndDocumentJob extends BaseEmsJob implements InterruptableJob {

    private static final Logger purgeBioAndDocsLogger = BaseLog
            .getLogger("PurgeBiometricAndDocumentLogger");
    private static final String DEFAULT_CITIZEN_TO_PURGE_BIO_FETCH_LIMIT = "1";
    private static final String DEFAULT_SAVE_PURGE_HISTORY = "true";

    private boolean isJobInterrupted = false;
    private JobKey jobKey = null;

    @Override
    public void execute(JobExecutionContext jobExecutionContext)
            throws JobExecutionException {
        startLogging(purgeBioAndDocsLogger);
        CardRequestDelegator cardRequestDelegator = new CardRequestDelegator();
        try {
            jobKey = jobExecutionContext.getJobDetail().getKey();
            Integer fetchLimit = Integer.valueOf(EmsUtil.getProfileValue(
                    ProfileKeyName.
                            KEY_NUMBER_OF_CITIZEN_TO_PURGE_BIO_FETCH_LIMIT, DEFAULT_CITIZEN_TO_PURGE_BIO_FETCH_LIMIT));

            String savePurgeHistory = String.valueOf(EmsUtil.getProfileValue(
                    ProfileKeyName.KEY_SAVE_PURGE_HISTORY, DEFAULT_SAVE_PURGE_HISTORY));

            List<Long> citizenIds = cardRequestDelegator.getCitizenIdsForPurgeBioAndDocs(fetchLimit);
            int requestCount = citizenIds.size();
            info("Citizen List Size For Purge Biometrics and Docs :" + requestCount);

            for (Long id : citizenIds) {
                if (!isJobInterrupted) {
                    try {
                        cardRequestDelegator.purgeBiometricsAndDocuments(id, savePurgeHistory);
                    } catch (Exception e) {
                        error(e.getMessage(), e);
                    }
                } else {
                    break;
                }
            }


        } catch (BaseException e) {
            logException(e);
        }
        endLogging();
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        error("calling interrupt: jobKey ==> " + jobKey);
        isJobInterrupted = true;
    }
}
