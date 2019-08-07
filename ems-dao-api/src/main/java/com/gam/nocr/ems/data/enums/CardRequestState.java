package com.gam.nocr.ems.data.enums;

/**
 * @author Haeri (haeri@gamelectronics.com)
 */
public enum CardRequestState {

	INACTIVE, // NOT USED
	REGISTERED,  // NOT USED
	SEND_TO_EMS, // NOT USED
	PENDING_FOR_EMS,  // NOT USED
	RECEIVED_BY_EMS, // NOT USED
	PENDING_IMS, //NOT USED
	VERIFIED_IMS,  // NOT USED
	NOT_VERIFIED_BY_IMS, //NOT USED
	RESERVED,
	STOPPED_NOT_REFER_TO_CCOS, // NOT USED
	READY_TO_RESERVE, // NOT USED
	REFERRED_TO_CCOS,
	NOT_REFERRED_TO_CCOS, // NOT USED
	READY_TO_LIMITED_RESERVE, // NOT USED
	LIMITED_RESERVED, // NOT USED
	STOPPED_NOT_FOLLOW_LIMITED_RESERVE,  // NOT USED
	DOCUMENT_AUTHENTICATED,
	SCANNED_DOCUMENTS, // NOT USED
	SCANNED_FACE, // NOT USED
	SCANNED_FINGER, // NOT USED
	APPROVED_BY_MES,  // NOT USED
	APPROVED,
	SENT_TO_AFIS,
	REVOKED_BY_AFIS, // NOT USED
	APPROVED_BY_AFIS,
	PENDING_ISSUANCE,
	ISSUED,
	READY_TO_DELIVER,
	PENDING_TO_DELIVER_BY_CMS,
	DELIVERED,
	UNSUCCESSFUL_DELIVERY, // NOT USED
	UNSUCCESSFUL_DELIVERY_BECAUSE_OF_DAMAGE,
	UNSUCCESSFUL_DELIVERY_BECAUSE_OF_BIOMETRIC,
	NOT_DELIVERED, // NOT USED
	STOPPED,
	REPEALED,
	CMS_ERROR,
	CMS_PRODUCTION_ERROR,
	IMS_ERROR,
	SCANNED_DOC_FACE, // NOT USED
	SCANNED_DOC_FINGER, // NOT USED
	SCANNED_FACE_FINGER, // NOT USED
	READY_TO_APPROVE; // NOT USED

	public static Long toLong(CardRequestState state) {
		if (state == null) {
			return null;
		}

		switch (state) {
		case RESERVED:
			return 1L;
		case REFERRED_TO_CCOS:
			return 2L;
		case DOCUMENT_AUTHENTICATED:
			return 3L;
		case SCANNED_DOCUMENTS:
			return 4L;
		case SCANNED_FACE:
			return 5L;
		case SCANNED_FINGER:
			return 6L;
		case SCANNED_DOC_FACE:
			return 7L;
		case SCANNED_DOC_FINGER:
			return 8L;
		case SCANNED_FACE_FINGER:
			return 9L;
		case READY_TO_APPROVE:
			return 10L;
		case APPROVED:
			return 11L;
		case REGISTERED:
			return 12L;
		case NOT_REFERRED_TO_CCOS:
			return 13L;
		case PENDING_FOR_EMS:
			return 14L;
		case NOT_VERIFIED_BY_IMS:
			return 15L;
		case RECEIVED_BY_EMS:
			return 16L;
		}
		return null;
	}

	public static CardRequestState toState(Long state) {
		if (state == null) {
			return null;
		}

		switch (state.intValue()) {
		case 1:
			return RESERVED;
		case 2:
			return REFERRED_TO_CCOS;
		case 3:
			return DOCUMENT_AUTHENTICATED;
		case 4:
			return SCANNED_DOCUMENTS;
		case 5:
			return SCANNED_FACE;
		case 6:
			return SCANNED_FINGER;
		case 7:
			return SCANNED_DOC_FACE;
		case 8:
			return SCANNED_DOC_FINGER;
		case 9:
			return SCANNED_FACE_FINGER;
		case 10:
			return READY_TO_APPROVE;
		case 11:
			return APPROVED;
		case 12:
			return REGISTERED;
		case 13:
			return NOT_REFERRED_TO_CCOS;
		case 14:
			return PENDING_FOR_EMS;
		case 15:
			return NOT_VERIFIED_BY_IMS;
		case 16:
			return RECEIVED_BY_EMS;
		}
		return null;
	}

	// Adldoost
	public static boolean checkStateChangeValidation(
			CardRequestState fromState, CardRequestState toState) {
		switch (fromState) {

		case REGISTERED:
		case PENDING_FOR_EMS:
		case PENDING_IMS:
			break;

		case RECEIVED_BY_EMS:
			switch (toState) {
			case RECEIVED_BY_EMS:
			case PENDING_IMS:
			case REPEALED:
				break;
			default:
				return false;
			}
			break;

		// TODO : PENDING_IMS AND VERIFIED_IMS NEED TO GET HANDELED
		// case PENDING_IMS :
		// switch(toState)
		// {
		// case APPROVED_BY_MES :
		// case PENDING_IMS:
		// case NOT_VERIFIED_BY_IMS:
		// case APPROVED:
		// break;
		// default:
		// return false;
		// }
		// break;
			
		case VERIFIED_IMS:
			switch (toState) {
			case VERIFIED_IMS:
			case REPEALED:
			case RESERVED:
				break;
			default:
				return false;
			}
			break;
			
		case NOT_VERIFIED_BY_IMS:
			switch (toState) {
			case NOT_VERIFIED_BY_IMS:
			case STOPPED:
			case RECEIVED_BY_EMS:
			case REPEALED:
			case RESERVED:
				break;
			default:
				return false;
			}
			break;
		case RESERVED:
			switch (toState) {
			case RESERVED:
			case REFERRED_TO_CCOS:
			case STOPPED:
			case REPEALED:
				break;
			default:
				return false;
			}
			break;
		case REFERRED_TO_CCOS:
			switch (toState) {
			case REFERRED_TO_CCOS:
			case DOCUMENT_AUTHENTICATED:
			case REPEALED:
				break;
			default:
				return false;
			}
			break;
		case DOCUMENT_AUTHENTICATED:
			switch (toState) {
			case DOCUMENT_AUTHENTICATED:
			case APPROVED:
			case REPEALED:
			case REFERRED_TO_CCOS:
				break;
			default:
				return false;
			}
			break;
		case APPROVED:
			switch (toState) {
			case APPROVED:
			case SENT_TO_AFIS:
			case REPEALED:
			case REFERRED_TO_CCOS:
				break;
			default:
				return false;
			}
			break;
		case SENT_TO_AFIS:
			switch (toState) {
			case SENT_TO_AFIS:
			case PENDING_IMS:
			case APPROVED_BY_AFIS:
			case IMS_ERROR:
				break;
			default:
				return false;
			}
			break;
		case APPROVED_BY_AFIS:
			switch (toState) {
			case APPROVED_BY_AFIS:
			case PENDING_ISSUANCE:
			case CMS_ERROR:
				break;
			default:
				return false;
			}
			break;
		case REVOKED_BY_AFIS:
			switch (toState) {
			case REVOKED_BY_AFIS:
				break;
			default:
				return false;
			}
			break;
		case PENDING_ISSUANCE:
			switch (toState) {
			case PENDING_ISSUANCE:
			case ISSUED:
			case CMS_PRODUCTION_ERROR:
			case STOPPED:
			case APPROVED_BY_AFIS:
				break;
			default:
				return false;
			}
			break;
		case ISSUED:
			switch (toState) {
			case ISSUED:
			case READY_TO_DELIVER:
			case APPROVED_BY_AFIS:
				break;
			default:
				return false;
			}
			break;
		case READY_TO_DELIVER:
			switch (toState) {
			case ISSUED:
			case READY_TO_DELIVER:
			case PENDING_TO_DELIVER_BY_CMS:
			case UNSUCCESSFUL_DELIVERY_BECAUSE_OF_DAMAGE:
			case UNSUCCESSFUL_DELIVERY_BECAUSE_OF_BIOMETRIC:
			case NOT_DELIVERED:
			case STOPPED:
			case APPROVED_BY_AFIS:
				break;
			default:
				return false;
			}
			break;
		case PENDING_TO_DELIVER_BY_CMS:
			switch (toState) {
			case PENDING_TO_DELIVER_BY_CMS:
			case DELIVERED:
				break;
			default:
				return false;
			}
			break;
		case DELIVERED:
			switch (toState) {
			case DELIVERED:
				break;
			default:
				return false;
			}
			break;
		case UNSUCCESSFUL_DELIVERY_BECAUSE_OF_DAMAGE:
			switch (toState) {
			case UNSUCCESSFUL_DELIVERY_BECAUSE_OF_DAMAGE:
			case APPROVED_BY_AFIS:
				break;
			default:
				return false;
			}
			break;
		case UNSUCCESSFUL_DELIVERY_BECAUSE_OF_BIOMETRIC:
			switch (toState) {
			case UNSUCCESSFUL_DELIVERY_BECAUSE_OF_BIOMETRIC:
			case DOCUMENT_AUTHENTICATED:
				break;
			default:
				return false;
			}
			break;
		case NOT_DELIVERED:
			switch (toState) {
			case NOT_DELIVERED:
				break;
			default:
				return false;
			}
			break;
		case STOPPED:
			switch (toState) {
			case STOPPED:
			case DOCUMENT_AUTHENTICATED:
				break;
			default:
				return false;
			}
			break;
		case REPEALED:
			switch (toState) {
			case RECEIVED_BY_EMS:
			case REPEALED:
			case STOPPED:
			case RESERVED:
				break;
			default:
				return false;
			}
			break;
		case CMS_ERROR:
			switch (toState) {
			case CMS_ERROR:
			case APPROVED_BY_AFIS:
				break;
			default:
				return false;
			}
			break;
		case CMS_PRODUCTION_ERROR:
			switch (toState) {
			case CMS_PRODUCTION_ERROR:
			case DOCUMENT_AUTHENTICATED:
			case APPROVED_BY_AFIS:
			case REPEALED:
				break;
			default:
				return false;
			}
			break;
		case IMS_ERROR:
			switch (toState) {
			case IMS_ERROR:
				break;
			default:
				return false;
			}
			break;
		case APPROVED_BY_MES:
			switch (toState) {
			case APPROVED_BY_MES:
			case PENDING_IMS:
			case NOT_VERIFIED_BY_IMS:
			case APPROVED:
				break;
			default:
				return false;
			}
			break;
		default:
			return false;

		}
		return true;

	}
}
