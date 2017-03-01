package com.gam.nocr.ems.web.action;

import gampooya.tools.security.BusinessSecurityException;

import java.util.List;

import org.slf4j.Logger;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.web.struts2.extJsController.ActionException;
import com.gam.commons.core.web.struts2.extJsController.ListControllerImpl;
import com.gam.nocr.ems.biz.delegator.CardRequestDelegator;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.vol.BatchDispatchInfoVTO;
/**
 * this is a class used in lost batch cartable in 3S
 * @author ganjyar
 *
 */
public class LostBatchAction extends ListControllerImpl<BatchDispatchInfoVTO> {
    private static final Logger logger = BaseLog.getLogger(LostBatchAction.class);

    
    /**
     * the id of the batch that must confirm its losing
     */
    private String batchId;
   
   
    
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	@Override
	public void setRecords(List<BatchDispatchInfoVTO> records) {
		this.records = records;
	}
	/**
	 * this method is used in confirm lost batch which announced in CCos. At the
	 * first the admin of 3s must confirm it then a job will notify CMS about
	 * them
	 * 
	 * @author Gangyar
	 * @return
	 */
	public String doConfirmLostBatch() throws BaseException {

		try {

			new CardRequestDelegator().doConfirmLostBatch(getUserProfile(),
					Long.valueOf(batchId));
			logger.info("Confirming lost batch with id : " + getBatchId());
			return SUCCESS_RESULT;
		} catch (BusinessSecurityException e) {
			throw new ActionException(WebExceptionCode.LBA_001,
					WebExceptionCode.GLB_001_MSG, e);
		} catch (Exception e) {
			throw new ActionException(WebExceptionCode.LBA_002,
					WebExceptionCode.GLB_003_MSG, e);
		}

	}

}
