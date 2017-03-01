package com.gam.nocr.ems.web.action;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.commons.core.web.struts2.extJsController.ActionException;
import com.gam.commons.core.web.struts2.extJsController.ListControllerImpl;
import com.gam.nocr.ems.biz.delegator.DispatchingDelegator;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.vol.DispatchInfoVTO;
import gampooya.tools.security.BusinessSecurityException;
import org.slf4j.Logger;

import java.util.List;

/**
 * Manages all AJAX requests of dispatching section
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class DispatchingAction extends ListControllerImpl<DispatchInfoVTO> {

    private static final Logger dispachLogger = BaseLog.getLogger("dispatch");

    /**
     * Identifier of items that should be processed by an operation like lost, find, send, etc.
     */
    private String detailIds = null;

    public String getDetailIds() {
        return detailIds;
    }

    public void setDetailIds(String detailIds) {
        this.detailIds = detailIds;
    }

    @Override
    public void setRecords(List<DispatchInfoVTO> records) {
        this.records = records;
    }

    /**
     * Registers the lost of the batch specified as 'detailedIds'
     *
     * @return {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String itemLost() throws BaseException {

        try {
            DispatchingDelegator dispatchingDelegator = new DispatchingDelegator();
            UserProfileTO up = getUserProfile();
            dispachLogger.info("The batch {} declared to be lost by user {}", detailIds, up.getUserName());
            dispatchingDelegator.itemLost(up, ids, detailIds, null);

            return SUCCESS_RESULT;

        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.DPA_001, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    /**
     * Registers a lost batch specified as 'detailedIds' as found
     *
     * @return {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String itemFound() throws BaseException {
        try {
            DispatchingDelegator dispatchingDelegator = new DispatchingDelegator();
            UserProfileTO up = getUserProfile();
            dispachLogger.info("The batch {} declared to be found by user {}", detailIds, up.getUserName());
            dispatchingDelegator.itemFound(up, ids, detailIds, null);

            return SUCCESS_RESULT;

        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.DPA_002, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    /**
     * Registers the receive of the batch specified as 'detailedIds'
     *
     * @return {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String itemReceived() throws BaseException {
        try {
            DispatchingDelegator dispatchingDelegator = new DispatchingDelegator();
            UserProfileTO up = getUserProfile();
            dispachLogger.info("The batch {} declared to be received by user {}", detailIds, up.getUserName());
            dispatchingDelegator.itemReceived(up, ids, detailIds, null);

            return SUCCESS_RESULT;

        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.DPA_003, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    /**
     * Registers the batch specified as 'detailedIds' as not received
     *
     * @return {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String itemNotReceived() throws BaseException {
        try {
            DispatchingDelegator dispatchingDelegator = new DispatchingDelegator();
            UserProfileTO up = getUserProfile();
            dispachLogger.info("The batch {} declared not received by user {}", detailIds, up.getUserName());
            dispatchingDelegator.itemNotReceived(up, ids, detailIds, null);

            return SUCCESS_RESULT;

        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.DPA_004, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    /**
     * Registers the batch specified as 'detailedIds' as sent
     *
     * @return {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String itemSent() throws BaseException {
        try {
            DispatchingDelegator dispatchingDelegator = new DispatchingDelegator();
            UserProfileTO up = getUserProfile();
            dispachLogger.info("The batch {} declared to be sent by user : {}", detailIds, up.getUserName());
            dispatchingDelegator.itemSent(up, ids, detailIds);

            return SUCCESS_RESULT;

        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.DPA_005, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    /**
     * Registers the undo operation of a batch specified as 'detailedIds'
     *
     * @return {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String undoSend() throws BaseException {
        try {
            DispatchingDelegator dispatchingDelegator = new DispatchingDelegator();
            UserProfileTO up = getUserProfile();
            dispachLogger.info("The batch {} declared to undo-sent by user {}", detailIds, up.getUserName());
            dispatchingDelegator.undoSend(up, ids, detailIds);

            return SUCCESS_RESULT;

        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.DPA_006, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    /**
     * Returns a received batch specified as 'detailedIds' to its original state (ready to receive)
     *
     * @return {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String backToInitialState() throws BaseException {
        try {
            DispatchingDelegator dispatchingDelegator = new DispatchingDelegator();
            UserProfileTO up = getUserProfile();
            dispachLogger.info("The batch {} declared to be backed to initial state by user {}", detailIds, up.getUserName());
            dispatchingDelegator.backToInitialState(up, ids, detailIds, null);

            return SUCCESS_RESULT;

        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.DPA_007, WebExceptionCode.GLB_001_MSG, e);
        }
    }
}
