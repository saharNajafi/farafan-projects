package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.CardTO;
import com.gam.nocr.ems.data.domain.vol.CardDispatchInfoVTO;
import com.gam.nocr.ems.data.enums.CardState;
import com.gam.nocr.ems.sharedobjects.GeneralCriteria;

import java.util.Date;
import java.util.List;

/**
 * <p> TODO -- Explain this class </p>
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public interface CardDAO extends EmsBaseDAO<CardTO> {


    /**
     * The method updateCardsState is use to update the state of the cards in spite of their ids
     *
     * @param cardIdList a list of type Long which represents the ids for the specified cards
     * @param cardState  an enum value of type {@link CardState}
     */
	void updateCardsState(List<Long> cardIdList,
                          CardState cardState) throws BaseException;

    /**
     * The method updateCardsStateByCRN is use to update the state of the cards in spite of the crn number of their cards
     *
     * @param crnList   a list of type String which represents the crn number for the specified cards
     * @param cardState an enum value of type {@link CardState}
     */
    void updateCardsStateByCRN(List<String> crnList,
                               CardState cardState) throws BaseException;

    void updateCardsStateByCmsId(List<String> batchIds) throws BaseException;

    /**
     * The method findCRNByRequestId is used to find a desired crn in spite of the given requestId.
     *
     * @param requestId a number of type {@link Long} which represents the a specified request in {@link
     *                  com.gam.nocr.ems.data.domain.CardRequestTO}
     * @return an object of type {@link String} which represents the crn or null
     * @throws com.gam.commons.core.BaseException
     */
    String findCRNByRequestId(Long requestId) throws BaseException;

    List<CardTO> findByBatchId(Long batchId) throws BaseException;

	void updateDeliverDate(Long requestId, Date date)  throws BaseException;
	/**
	 * 
	 * @author ganjyar
	 */
	List<CardDispatchInfoVTO> fetchCardLostTempList(GeneralCriteria criteria)
			throws BaseException;

	/**
	 * 
	 * @author ganjyar
	 */
	Integer countCardLostTemp(GeneralCriteria criteria) throws BaseException;

	/**
	 * 
	 * @author ganjyar
	 */
	void updateLostConfirmBytBatchConfirm(Long batchId) throws BaseException;

	/**
	 *
	 * @author Namjoofar
	 */
	void unconfirmAllCardsOfBatch(Long batchId) throws BaseException;

    Integer countCardLostDate(Long batchId) throws BaseException;
}
