package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.data.domain.*;
import com.gam.nocr.ems.data.domain.ws.DispatchInfoWTO;
import com.gam.nocr.ems.data.enums.BatchState;
import com.gam.nocr.ems.data.enums.BoxState;
import com.gam.nocr.ems.sharedobjects.GeneralCriteria;

import java.util.HashMap;
import java.util.List;

/**
 * <p> TODO -- Explain this class </p>
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public interface DispatchDAO extends EmsBaseDAO<CardContainerTO> {

    public void itemLost(Long perId, String ids, String detailIds, String cardIds) throws BaseException;

    public void itemFound(Long perId, String ids, String detailIds, String cardIds) throws BaseException;

    public void itemReceived(Long perId, String ids, String detailIds, String cardIds) throws BaseException;

    public void itemNotReceived(Long perId, String ids, String detailIds, String cardIds) throws BaseException;

    public void itemSent(Long perId, String ids, String detailIds) throws BaseException;

    public void undoSend(Long perId, String ids, String detailIds) throws BaseException;

    public void backToInitialState(Long perId, String ids, String detailIds, String cardsIds) throws BaseException;

    public void updateBatches(BoxTO boxTO, List<String> batchIds) throws BaseException;

    /**
     * The method updateBatchesState is use to update the state of the batches in spite of their ids
     *
     * @param batchIdList a list of type Long which represents the ids for the specified batches
     * @param batchState  an enum value of type {@link BatchState}
     */
    public void updateBatchesState(List<Long> batchIdList,
                                   BatchState batchState) throws BaseException;

    /**
     * The method updateBoxesState is use to update the state of the batches in spite of their ids
     *
     * @param boxIdList a list of type Long which represents the ids for the specified boxes
     * @param boxState  an enum value of type {@link BoxState}
     */
    public void updateBoxesState(List<Long> boxIdList,
                                 BoxState boxState) throws BaseException;

    /**
     * The method findReceivedBatches is used to find a specified instances of type {@link
     * BatchTO} in order to understand which one of them were received
     *
     * @return a list of type {@link BatchTO}
     */
    public List<BatchTO> findReceivedBatches(Integer from, Integer to) throws BaseException;

    public Long findReceivedBatchesCount() throws BaseException;

    /**
     * The method findMissedBatches is used to find a specified instances of type {@link
     * BatchTO} in order to understand which one of them were missed
     *
     * @return a list of type {@link BatchTO}
     */
    public List<BatchTO> findMissedBatches(Integer from, Integer to) throws BaseException;

    public Long findMissedBatchesCount() throws BaseException;

    /**
     * The method findMissedBoxes is used to find a specified instances of type {@link
     * BoxTO} in order to understand which one of them were missed
     *
     * @return a list of type {@link BoxTO}
     */
    public List<BoxTO> findMissedBoxes(Integer from, Integer to) throws BaseException;

    public Long findMissedBoxesCount() throws BaseException;

    /**
     * The method findMissedCards is used to find a specified instances of type {@link
     * CardTO} in order to understand which one of them were missed
     *
     * @return a list of type {@link CardTO}
     */
    public List<CardTO> findMissedCards(Integer from, Integer to) throws BaseException;

    public Long findMissedCardsCount() throws BaseException;

    public BatchTO findBatchByCmsId(String cmsBatchId) throws BaseException;

    /**
     * The method findBoxByCmsId is used to find an instance of type {@link BoxTO} by using CMS box id
     *
     * @param cmsBoxId is a string value which represents the CMS box Id
     * @return a instance of type {@link BoxTO} or null
     * @throws BaseException
     */
    public BoxTO findBoxByCmsId(String cmsBoxId) throws BaseException;

    public void replaceDispatchInfoReceiverDepId(Long enrollmentOfficeId, Long superiorEnrollmentOfficeId) throws BaseException;

    public List<DispatchInfoTO> findByContainerId(Long containerId) throws BaseException;
    
    public void notifyCardMissed(List<CardTO> cards) throws BaseException;
    public void replaceDispatchInfoReceiverIdBySuperriorOfficeId(Long eofId,Long supperiorOfficeId) throws BaseException;;
    /**
     * @author ganjyar
     * @param from
     * @param to
     * @return
     * @throws BaseException
     */
    public List<BatchTO> findMissedBatchesConfirmed(Integer from, Integer to) throws BaseException;

    /**
     * @author ganjyar
     * @return
     * @throws BaseException
     */
    public Long findMissedBatchesCountConfirmed() throws BaseException;
    
    public List<CardTO> findMissedCardsConfirmed(Integer from, Integer to) throws BaseException;
    public Long findMissedCardsCountConfirmed() throws BaseException;

	public Long isValidToSendDeliverySms(Long batchId) throws BaseException;

	//Madanipour
	public List<DispatchInfoWTO> fetchBatchDispatchList(GeneralCriteria criteria,UserProfileTO userProfileTO)throws BaseException;

	//Madanipour
	public Integer countBatchDispatchList(HashMap parameters, UserProfileTO userProfileTO) throws BaseException;
}
