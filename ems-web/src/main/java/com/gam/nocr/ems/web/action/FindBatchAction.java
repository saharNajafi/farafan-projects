package com.gam.nocr.ems.web.action;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.web.struts2.extJsController.ActionException;
import com.gam.commons.core.web.struts2.extJsController.ListControllerImpl;
import com.gam.nocr.ems.biz.delegator.CardRequestDelegator;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.vol.BatchDispatchInfoVTO;
import gampooya.tools.security.BusinessSecurityException;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * this is a class used to find batch in 3S
 *
 * @author a.amiri
 */
public class FindBatchAction extends ListControllerImpl<BatchDispatchInfoVTO> {
    private static final Logger logger = BaseLog.getLogger(FindBatchAction.class);


    private Long cardRequestId;

    public Long getCardRequestId() {
        return cardRequestId;
    }

    public void setCardRequestId(Long cardRequestId) {
        this.cardRequestId = cardRequestId;
    }

    @Override
    public void setRecords(List<BatchDispatchInfoVTO> records) {
        this.records = records;
    }

    /**
     * this method is used in find batch Id by request Id
     *
     * @return
     * @throws BaseException
     * @author a.amiri
     */
    public String findBatchIdByCardRequestId() throws BaseException {
        try {
            String cmsBatch = new CardRequestDelegator().findCmsBatchByRequestId(
                    getUserProfile(), cardRequestId);
            BatchDispatchInfoVTO batchDispatchInfoVTO = new BatchDispatchInfoVTO();
            batchDispatchInfoVTO.setCmsID(cmsBatch);
            List<BatchDispatchInfoVTO> batchDispatchInfoVTOS = new ArrayList<BatchDispatchInfoVTO>();
            batchDispatchInfoVTOS.add(batchDispatchInfoVTO);
            setRecords(batchDispatchInfoVTOS);
            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.CRA_007,
                    WebExceptionCode.GLB_001_MSG, e);
        } catch (Exception e) {
            throw new ActionException(WebExceptionCode.CRA_008,
                    WebExceptionCode.GLB_003_MSG, e);
        }

    }

}
