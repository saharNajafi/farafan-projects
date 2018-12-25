package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.nocr.ems.data.domain.CardRequestHistoryTO;
import com.gam.nocr.ems.data.domain.CardRequestTO;
import com.gam.nocr.ems.data.enums.CardRequestHistoryAction;
import com.gam.nocr.ems.data.enums.SystemId;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 8/11/18.
 */
public interface CardRequestHistoryService extends Service {
    CardRequestHistoryTO create(CardRequestTO cardRequestTO,
                                String result,
                                SystemId systemId,
                                String requestId,
                                CardRequestHistoryAction cardRequestHistoryAction,
                                String actor) throws BaseException;

    boolean findByCardRequestAndCrhAction(Long id, CardRequestHistoryAction cardRequestHistoryAction) throws BaseException;
}
