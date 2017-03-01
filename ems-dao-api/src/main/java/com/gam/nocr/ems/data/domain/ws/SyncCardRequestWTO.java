package com.gam.nocr.ems.data.domain.ws;

import com.gam.nocr.ems.data.enums.CardRequestState;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * @author <a href="mailto:saadat@gamelectronics.com.com">Alireza Saadat</a>
 */
public class SyncCardRequestWTO {

	private Long id;
	private String cardRequestState;
	private Long cardEnrollmentOfficeId;
	private String cardRequestMetadata;
	private Long originalCardRequestOfficeId;

	public SyncCardRequestWTO() {
	}

	public SyncCardRequestWTO(Long id, String cardRequestState,
			Long cardEnrollmentOfficeId, String cardRequestMetadata) {
		this.id = id;
		this.cardRequestState = cardRequestState;
		this.cardEnrollmentOfficeId = cardEnrollmentOfficeId;
		this.cardRequestMetadata = cardRequestMetadata;
	}

	public SyncCardRequestWTO(Long portalRequestId, CardRequestState state,
			Long cardEnrollmentOfficeId, String cardRequestMetadata,
			Long originalCardRequestOfficeId) {
		this.id = portalRequestId;
		this.cardRequestState = state.name();
		this.cardEnrollmentOfficeId = cardEnrollmentOfficeId;
		this.cardRequestMetadata = cardRequestMetadata;
		this.originalCardRequestOfficeId = originalCardRequestOfficeId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCardRequestState() {
		return cardRequestState;
	}

	public void setCardRequestState(String cardRequestState) {
		this.cardRequestState = cardRequestState;
	}

	public Long getCardEnrollmentOfficeId() {
		return cardEnrollmentOfficeId;
	}

	public void setCardEnrollmentOfficeId(Long cardEnrollmentOfficeId) {
		this.cardEnrollmentOfficeId = cardEnrollmentOfficeId;
	}

	public String getCardRequestMetadata() {
		return cardRequestMetadata;
	}

	public void setCardRequestMetadata(String cardRequestMetadata) {
		this.cardRequestMetadata = cardRequestMetadata;
	}

	public Long getOriginalCardRequestOfficeId() {
		return originalCardRequestOfficeId;
	}

	public void setOriginalCardRequestOfficeId(Long originalCardRequestOfficeId) {
		this.originalCardRequestOfficeId = originalCardRequestOfficeId;
	}

	@Override
	public String toString() {
		return EmsUtil.toJSON(this);
	}
}
