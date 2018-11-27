package com.gam.nocr.ems.biz.service.internal.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.data.DataException;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.dao.CardRequestHistoryDAO;
import com.gam.nocr.ems.data.domain.CardRequestHistoryTO;
import com.gam.nocr.ems.data.domain.CardRequestTO;
import com.gam.nocr.ems.data.enums.CardRequestHistoryAction;
import com.gam.nocr.ems.data.enums.SystemId;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 12/31/17.
 */
@Stateless(name = "CardRequestHistoryService")
@Local(CardRequestHistoryServiceLocal.class)
@Remote(CardRequestHistoryServiceRemote.class)
public class CardRequestHistoryServiceImpl extends EMSAbstractService
        implements CardRequestHistoryServiceLocal, CardRequestHistoryServiceRemote {

    public CardRequestHistoryTO create(CardRequestTO cardRequestTO,
                                       String result,
                                       SystemId systemId,
                                       String requestId,
                                       CardRequestHistoryAction cardRequestHistoryAction,
                                       String actor) throws BaseException {
        try {
            return getCardRequestHistoryDAO().create(
                    cardRequestTO, result, systemId, requestId, cardRequestHistoryAction, actor);
        } catch (DataException e) {
            throw e;
        }
    }


    public boolean findByCardRequestAndCrhAction(Long cardRequestId, CardRequestHistoryAction cardRequestHistoryAction) throws BaseException {
        try {
            return getCardRequestHistoryDAO().findByCardRequestAndCrhAction(cardRequestId, cardRequestHistoryAction);
        } catch (BaseException e) {
            throw new ServiceException(BizExceptionCode.CRH_S_001, BizExceptionCode.CRH_S_001_MSG, e);
        }
    }

    private CardRequestHistoryDAO getCardRequestHistoryDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory()
                    .getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_CARD_REQUEST_HISTORY));
        } catch (DAOFactoryException e) {
            throw new ServiceException(
                    BizExceptionCode.CMS_048,
                    BizExceptionCode.GLB_001_MSG,
                    e,
                    EMSLogicalNames.DAO_CARD_REQUEST_HISTORY.split(","));
        }
    }
}

