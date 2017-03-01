package com.gam.nocr.ems.web.listreader.wrapper;

import com.gam.commons.core.BaseLog;
import com.gam.commons.listreader.Wrapper;
import com.gam.nocr.ems.data.domain.vol.CardRequestVTO;
import com.gam.nocr.ems.data.enums.CardRequestState;
import com.gam.nocr.ems.data.enums.CardRequestType;
import com.gam.nocr.ems.data.enums.CardState;
import org.slf4j.Logger;

import java.util.ResourceBundle;

/**
 * The wrapper used for card request grid in 3S. It replaces the enumeration values (e.g. state, type, etc.) with their
 * corresponding Farsi labels (read from a property file). As this list is going to be used both in ExtJS grid and Excel
 * output and there is no clint side rendering feature in excel, we need to replace enumeration values with a label in
 * server side
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class CardRequestWrapper extends Wrapper {

    ResourceBundle labels = null;
    private static Logger logger = BaseLog.getLogger(CardRequestWrapper.class);

    public CardRequestWrapper() {

        try {
            labels = ResourceBundle.getBundle("grids");
        } catch (Exception e) {
            String message = "Unable to get values from SearchResultTO for process";
            logger.error(message, e);
        }
    }

    /**
     * Replaces the value of 'cardRequestState' property of result items with their corresponding Farsi label
     *
     * @return A Farsi label for the value of 'cardRequestState' property of result items
     */
    public String getCardRequestState() {
        CardRequestVTO cardRequestVTO = (CardRequestVTO) getCurrentRow();

        if (cardRequestVTO.getCardRequestState() != null) {
            if (CardRequestState.RECEIVED_BY_EMS.name().equals(cardRequestVTO.getCardRequestState()))
                return this.labels.getString("cardRequest.cardRequestState.RECEIVED_BY_EMS");
            else if (CardRequestState.PENDING_IMS.name().equals(cardRequestVTO.getCardRequestState()))
                return this.labels.getString("cardRequest.cardRequestState.PENDING_IMS");
            else if (CardRequestState.VERIFIED_IMS.name().equals(cardRequestVTO.getCardRequestState()))
                return this.labels.getString("cardRequest.cardRequestState.VERIFIED_IMS");
            else if (CardRequestState.NOT_VERIFIED_BY_IMS.name().equals(cardRequestVTO.getCardRequestState()))
                return this.labels.getString("cardRequest.cardRequestState.NOT_VERIFIED_BY_IMS");
            else if (CardRequestState.RESERVED.name().equals(cardRequestVTO.getCardRequestState()))
                return this.labels.getString("cardRequest.cardRequestState.RESERVED");
            else if (CardRequestState.REFERRED_TO_CCOS.name().equals(cardRequestVTO.getCardRequestState()))
                return this.labels.getString("cardRequest.cardRequestState.REFERRED_TO_CCOS");
            else if (CardRequestState.DOCUMENT_AUTHENTICATED.name().equals(cardRequestVTO.getCardRequestState()))
                return this.labels.getString("cardRequest.cardRequestState.DOCUMENT_AUTHENTICATED");
            else if (CardRequestState.APPROVED_BY_MES.name().equals(cardRequestVTO.getCardRequestState()))
                return this.labels.getString("cardRequest.cardRequestState.APPROVED_BY_MES");
            else if (CardRequestState.APPROVED.name().equals(cardRequestVTO.getCardRequestState()))
                return this.labels.getString("cardRequest.cardRequestState.APPROVED");
            else if (CardRequestState.SENT_TO_AFIS.name().equals(cardRequestVTO.getCardRequestState()))
                return this.labels.getString("cardRequest.cardRequestState.SENT_TO_AFIS");
            else if (CardRequestState.APPROVED_BY_AFIS.name().equals(cardRequestVTO.getCardRequestState()))
                return this.labels.getString("cardRequest.cardRequestState.APPROVED_BY_AFIS");
            else if (CardRequestState.PENDING_ISSUANCE.name().equals(cardRequestVTO.getCardRequestState()))
                return this.labels.getString("cardRequest.cardRequestState.PENDING_ISSUANCE");
            else if (CardRequestState.ISSUED.name().equals(cardRequestVTO.getCardRequestState()))
                return this.labels.getString("cardRequest.cardRequestState.ISSUED");
            else if (CardRequestState.READY_TO_DELIVER.name().equals(cardRequestVTO.getCardRequestState()))
                return this.labels.getString("cardRequest.cardRequestState.READY_TO_DELIVER");
            else if (CardRequestState.PENDING_TO_DELIVER_BY_CMS.name().equals(cardRequestVTO.getCardRequestState()))
                return this.labels.getString("cardRequest.cardRequestState.PENDING_TO_DELIVER_BY_CMS");
            else if (CardRequestState.DELIVERED.name().equals(cardRequestVTO.getCardRequestState()))
                return this.labels.getString("cardRequest.cardRequestState.DELIVERED");
            else if (CardRequestState.UNSUCCESSFUL_DELIVERY.name().equals(cardRequestVTO.getCardRequestState()))
                return this.labels.getString("cardRequest.cardRequestState.UNSUCCESSFUL_DELIVERY");
            else if (CardRequestState.UNSUCCESSFUL_DELIVERY_BECAUSE_OF_DAMAGE.name().equals(cardRequestVTO.getCardRequestState()))
                return this.labels.getString("cardRequest.cardRequestState.UNSUCCESSFUL_DELIVERY_BECAUSE_OF_DAMAGE");
            else if (CardRequestState.UNSUCCESSFUL_DELIVERY_BECAUSE_OF_BIOMETRIC.name().equals(cardRequestVTO.getCardRequestState()))
                return this.labels.getString("cardRequest.cardRequestState.UNSUCCESSFUL_DELIVERY_BECAUSE_OF_BIOMETRIC");
            else if (CardRequestState.NOT_DELIVERED.name().equals(cardRequestVTO.getCardRequestState()))
                return this.labels.getString("cardRequest.cardRequestState.NOT_DELIVERED");
            else if (CardRequestState.STOPPED.name().equals(cardRequestVTO.getCardRequestState()))
                return this.labels.getString("cardRequest.cardRequestState.STOPPED");
            else if (CardRequestState.CMS_ERROR.name().equals(cardRequestVTO.getCardRequestState()))
                return this.labels.getString("cardRequest.cardRequestState.CMS_ERROR");
            else if (CardRequestState.CMS_PRODUCTION_ERROR.name().equals(cardRequestVTO.getCardRequestState()))
                return this.labels.getString("cardRequest.cardRequestState.CMS_PRODUCTION_ERROR");
            else if (CardRequestState.IMS_ERROR.name().equals(cardRequestVTO.getCardRequestState()))
                return this.labels.getString("cardRequest.cardRequestState.IMS_ERROR");
            else
                return cardRequestVTO.getCardRequestState();
        } else
            return null;
    }

    /**
     * Replaces the value of 'cardType' property of result items with their corresponding Farsi label
     *
     * @return A Farsi label for the value of 'cardType' property of result items
     */
    public String getCardType() {
        CardRequestVTO cardRequestVTO = (CardRequestVTO) getCurrentRow();

        if (cardRequestVTO.getCardType() != null) {
            if (CardRequestType.FIRST_CARD.name().equals(cardRequestVTO.getCardType()))
                return this.labels.getString("cardRequest.cardRequestType.FIRST_CARD");
            else if (CardRequestType.EXTEND.name().equals(cardRequestVTO.getCardType()))
                return this.labels.getString("cardRequest.cardRequestType.EXTEND");
            else if (CardRequestType.REPLICA.name().equals(cardRequestVTO.getCardType()))
                return this.labels.getString("cardRequest.cardRequestType.REPLICA");
            else if (CardRequestType.REPLACE.name().equals(cardRequestVTO.getCardType()))
                return this.labels.getString("cardRequest.cardRequestType.REPLACE");
            else if (CardRequestType.UNSUCCESSFUL_DELIVERY.name().equals(cardRequestVTO.getCardType()))
                return this.labels.getString("cardRequest.cardRequestType.UNSUCCESSFUL_DELIVERY");
            else
                return cardRequestVTO.getCardType();
        } else
            return null;
    }


    /**
     * Replaces the value of 'cardState' property of result items with their corresponding Farsi label
     *
     * @return A Farsi label for the value of 'cardState' property of result items
     */
    public String getCardState() {
        CardRequestVTO cardRequestVTO = (CardRequestVTO) getCurrentRow();

        if (cardRequestVTO.getCardState() != null) {
            if (CardState.SHIPPED.name().equals(cardRequestVTO.getCardState()))
                return this.labels.getString("cardRequest.cardState.SHIPPED");
            else if (CardState.RECEIVED.name().equals(cardRequestVTO.getCardState()))
                return this.labels.getString("cardRequest.cardState.RECEIVED");
            else if (CardState.MISSED.name().equals(cardRequestVTO.getCardState()))
                return this.labels.getString("cardRequest.cardState.MISSED");
            else if (CardState.DELIVERED.name().equals(cardRequestVTO.getCardState()))
                return this.labels.getString("cardRequest.cardState.DELIVERED");
            else if (CardState.REVOKED.name().equals(cardRequestVTO.getCardState()))
                return this.labels.getString("cardRequest.cardState.REVOKED");
            else if (CardState.LOST.name().equals(cardRequestVTO.getCardState()))
                return this.labels.getString("cardRequest.cardState.LOST");
            else
                return cardRequestVTO.getCardState();
        } else
            return null;
    }
}