package com.gam.nocr.ems.web.action;

import com.gam.nocr.ems.data.enums.OfficeCardRequestStates;
import gampooya.tools.security.BusinessSecurityException;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.web.struts2.extJsController.ActionException;
import com.gam.commons.core.web.struts2.extJsController.ListControllerImpl;
import com.gam.nocr.ems.biz.delegator.CardRequestDelegator;
import com.gam.nocr.ems.biz.delegator.EnrollmentOfficeDelegator;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.vol.AccessProductionVTO;
import com.gam.nocr.ems.data.domain.vol.EnrollmentOfficeVTO;
import com.gam.nocr.ems.data.enums.OfficeSettingType;
import com.gam.nocr.ems.util.EmsUtil;

/**
 *  Manages all AJAX interactions related to enrollment office section
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class EnrollmentOfficeAction extends ListControllerImpl<EnrollmentOfficeVTO> {

    private static final Logger logger = BaseLog.getLogger(EnrollmentOfficeAction.class);

    /**
     * Identifier of the enrollment office that would be used by operations like 'save', 'load', etc.
     */
    private String enrollmentId;

    /**
     * Identifier of the substitution enrollment office that would be used in operations like revokeNetworkToken
     */
    private String substitutionOfficeId;

    /**
     * The reason of network token revoke (e.g. REPLICA, REPLACE, etc.)
     */
    private String reason;

    /**
     * A flag indicating whether an enrollment office has any in progress card requests or not. It's mainly used in
     * revoking network token to select a substitution office
     */
    private OfficeCardRequestStates inProgressRequestsFlag;
   	
   	private String officeSettingType;

	private Boolean accessViewAndChangeOfficeSetting;




    /**
     * Must be implemented in order to set records by json populator
     *
     * @param records
     */
    @Override
    public void setRecords(List<EnrollmentOfficeVTO> records) {
        this.records = records;
    }

    /**
     * Saves an enrollment office
     *
     * @return {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String save() throws BaseException {
        try {
            EnrollmentOfficeDelegator enrollmentOfficeDelegator = new EnrollmentOfficeDelegator();
            for (EnrollmentOfficeVTO to : records) {
            	
            	if (to.getKhosusiType().equals("NOCR") || to.getKhosusiType().equals("0")) {
					to.setOfficeType("NOCR");
					
				} else if (to.getKhosusiType().equals("OFFICE")
						|| to.getKhosusiType().equals("POST") || 
						to.getKhosusiType().equals("1") || to.getKhosusiType().equals("2")) {
					to.setOfficeType("OFFICE");
					
				}
            	
                if (to.getId() == null)
                    enrollmentOfficeDelegator.save(getUserProfile(), to);
                else
                    enrollmentOfficeDelegator.update(getUserProfile(), to);
            }
            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.EOA_001, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    /**
     *  Loads an enrollment office information base on its identifier specified as 'ids'
     *
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String load() throws BaseException {
        try {
            EnrollmentOfficeDelegator enrollmentOfficeDelegator = new EnrollmentOfficeDelegator();
            EnrollmentOfficeVTO enrollmentOfficeVTO;

            if (ids != null)
                enrollmentOfficeVTO = enrollmentOfficeDelegator.load(getUserProfile(), Long.parseLong(ids));
            else
                throw new ActionException(WebExceptionCode.EOA_004, WebExceptionCode.EOA_004_MSG);

            List<EnrollmentOfficeVTO> officeTOs = new ArrayList<EnrollmentOfficeVTO>();
            officeTOs.add(enrollmentOfficeVTO);
            setRecords(officeTOs);

            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.EOA_002, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    /**
     *  Deletes an enrollment office information base on its identifier specified as 'ids'
     *
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
//Commented By Adldoost
//    public String delete() throws BaseException {
//    	
//        try {
//            new EnrollmentOfficeDelegator().remove(getUserProfile(), ids);
//        	
//
//            return SUCCESS_RESULT;
//        } catch (BusinessSecurityException e) {
//            throw new ActionException(WebExceptionCode.EOA_003, WebExceptionCode.GLB_001_MSG, e);
//        }
//    }
    //Adldoost
    public String substituteAndDelete()throws BaseException
    {
    	Long enrollmentOfficeId;
    	
    	try {
            enrollmentOfficeId = Long.parseLong(getEnrollmentId());
        } catch (Exception e) {
            throw new ActionException(WebExceptionCode.EOA_011, WebExceptionCode.EOA_011_MSG, e, new String[]{"enrollmentOfficeId"});
        }
   	
        Long superiorId = null;
        try {
        	if(getSubstitutionOfficeId() == null || getSubstitutionOfficeId().isEmpty())
        		superiorId = null;
        	else if (EmsUtil.checkString(getSubstitutionOfficeId())) {
                superiorId = Long.parseLong(getSubstitutionOfficeId());
        	}
                new EnrollmentOfficeDelegator().substituteAndDelete(getUserProfile(), enrollmentOfficeId, superiorId);
            
        }
        catch (BaseException exp) {
        	if(exp.getExceptionCode() == BizExceptionCode.EOS_071)
        		throw new ActionException(WebExceptionCode.EOA_015, WebExceptionCode.EOA_015_MSG, exp, new String[]{"substitutionOfficeId"});
        	else
        		throw new ActionException(WebExceptionCode.EOA_012, WebExceptionCode.EOA_011_MSG, exp, new String[]{"substitutionOfficeId"});
        }catch (Exception e) {
            throw new ActionException(WebExceptionCode.EOA_012, WebExceptionCode.EOA_011_MSG, e, new String[]{"substitutionOfficeId"});
        }
        return SUCCESS_RESULT;
    }

    /**
     * This method is used to save the request of first token issuance
     *
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
//Commented By Adldoost
//    public String issueFirstNetworkToken() throws BaseException {
//        try {
//            EnrollmentOfficeDelegator enrollmentOfficeDelegator = new EnrollmentOfficeDelegator();
//
//            TokenType type = TokenType.NETWORK;
//            enrollmentOfficeDelegator.issueToken(getUserProfile(), Long.parseLong(getEnrollmentId()), type);
//            return SUCCESS_RESULT;
//
//        } catch (BusinessSecurityException e) {
//            throw new ActionException(WebExceptionCode.EOA_005, WebExceptionCode.GLB_001_MSG, e);
//        }
//    }

    /**
     * This method is used to save the request of replica token issuance
     *
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
//Commented By Adldoost
//    public String issueReplicaNetworkToken() throws BaseException {
//        try {
//            EnrollmentOfficeDelegator enrollmentOfficeDelegator = new EnrollmentOfficeDelegator();
//            TokenType type = TokenType.NETWORK;
//            ReplicaReason reason = ReplicaReason.REPLICA;
//            enrollmentOfficeDelegator.replicateToken(getUserProfile(), Long.parseLong(getEnrollmentId()), type, reason);
//            return SUCCESS_RESULT;
//        } catch (BusinessSecurityException e) {
//            throw new ActionException(WebExceptionCode.EOA_009, WebExceptionCode.GLB_001_MSG, e);
//        }
//    }

    /**
     * This method is used to save the request of replace token issuance
     *
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
//Commented By Adldoost
//    public String issueNetworkTokenForDamagedToken() throws BaseException {
//        try {
//            EnrollmentOfficeDelegator enrollmentOfficeDelegator = new EnrollmentOfficeDelegator();
//            TokenType type = TokenType.NETWORK;
//            ReplicaReason reason = ReplicaReason.DAMAGE;
//            enrollmentOfficeDelegator.replicateToken(getUserProfile(), Long.parseLong(getEnrollmentId()), type, reason);
//            return SUCCESS_RESULT;
//        } catch (BusinessSecurityException e) {
//            throw new ActionException(WebExceptionCode.EOA_010, WebExceptionCode.GLB_001_MSG, e);
//        }
//    }

    /**
     * This method is used to save the request of reissuing a network token
     *
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
//Commented By Adldoost
//    public String reissueNetworkToken() throws BaseException {
//        try {
//            EnrollmentOfficeDelegator enrollmentOfficeDelegator = new EnrollmentOfficeDelegator();
//
//            enrollmentOfficeDelegator.reissueToken(getUserProfile(), Long.parseLong(getEnrollmentId()));
//
//            return SUCCESS_RESULT;
//        } catch (BusinessSecurityException e) {
//            throw new ActionException(WebExceptionCode.EOA_006, WebExceptionCode.GLB_001_MSG, e);
//        }
//    }

    /**
     * Registers the delivery of a network token
     *
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
//Commented by Adldoost
//    public String deliverNetworkToken() throws BaseException {
//        try {
//            EnrollmentOfficeDelegator enrollmentOfficeDelegator = new EnrollmentOfficeDelegator();
//
//            enrollmentOfficeDelegator.deliverToken(getUserProfile(), Long.parseLong(getEnrollmentId()));
//
//            return SUCCESS_RESULT;
//        } catch (BusinessSecurityException e) {
//            throw new ActionException(WebExceptionCode.EOA_007, WebExceptionCode.GLB_001_MSG, e);
//        }
//    }

    /**
     * Registers a revoke network token request
     *
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
//    public String revokeNetworkToken() throws BaseException {
//
//        EnrollmentOfficeDelegator enrollmentOfficeDelegator = new EnrollmentOfficeDelegator();
//
//        ReplicaReason reason;
//        String comment;
//        Long enrollmentOfficeId;
//
//        try {
//            if (getReason().contains("L"))
//                reason = ReplicaReason.REPLICA;
//            else if (getReason().contains("D"))
//                reason = ReplicaReason.DAMAGE;
//            else
//                reason = ReplicaReason.UNSPECIFIED;
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//            reason = ReplicaReason.UNSPECIFIED;
//        }
//
//        try {
//            comment = getReason().split("-")[1];
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//            comment = "Not Specified.";
//        }
//        comment = reason + "\n" + comment;
//
//        try {
//            enrollmentOfficeId = Long.parseLong(getEnrollmentId());
//        } catch (Exception e) {
//            throw new ActionException(WebExceptionCode.EOA_011, WebExceptionCode.EOA_011_MSG, e, new String[]{"enrollmentOfficeId"});
//        }
//
//        Long superiorId = null;
//        try {
//            if (EmsUtil.checkString(getSubstitutionOfficeId())) {
//                superiorId = Long.parseLong(getSubstitutionOfficeId());
//            }
//        } catch (Exception e) {
//            throw new ActionException(WebExceptionCode.EOA_012, WebExceptionCode.EOA_011_MSG, e, new String[]{"substitutionOfficeId"});
//        }
//CommentedByAdldoost
//        try {
//            enrollmentOfficeDelegator.revokeAndSubstitute(getUserProfile(), enrollmentOfficeId, superiorId, reason, comment);
//            
//        } catch (BusinessSecurityException e) {
//            throw new ActionException(WebExceptionCode.EOA_008, WebExceptionCode.GLB_001_MSG, e);
//        }
//        return SUCCESS_RESULT;
//    }

    /**
     * Given an enrollment office identifier and checks the number of in progress card requests for it. It would be used
     * when revoking a network token in order to decide the need to specify a substitution enrollment office or not
     *
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String checkInProgressRequests() throws BaseException {
        EnrollmentOfficeDelegator enrollmentOfficeDelegator = new EnrollmentOfficeDelegator();
        try {
            inProgressRequestsFlag = enrollmentOfficeDelegator.checkInProgressRequests(getUserProfile(), Long.parseLong(getEnrollmentId()));
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.EOA_014, WebExceptionCode.GLB_001_MSG, e);
        }
        return SUCCESS_RESULT;
    }

    /**
     * Deletes a network token request for given enrollment office identifier
     *
     * @return  {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
//Commented By Adldoost
//    public String deleteToken() throws BaseException {
//        EnrollmentOfficeDelegator enrollmentOfficeDelegator = new EnrollmentOfficeDelegator();
//        try {
//            enrollmentOfficeDelegator.deleteToken(getUserProfile(), Long.parseLong(enrollmentId));
//            return SUCCESS_RESULT;
//        } catch (BusinessSecurityException e) {
//            throw new ActionException(WebExceptionCode.EOA_013, WebExceptionCode.GLB_001_MSG, e);
//        }
//    }
    
    
    /**
     * @author Madanipour
     * @return
     */
    public String chageOfficeSetting() throws BaseException {
		try {
			
			EnrollmentOfficeDelegator enrollmentOfficeDelegator = new EnrollmentOfficeDelegator();
			enrollmentOfficeDelegator.chageOfficeSetting(getUserProfile(), Long.parseLong(enrollmentId), officeSettingType);
			return SUCCESS_RESULT;
		} catch (BusinessSecurityException e) {
			throw new ActionException(WebExceptionCode.EOA_015,
					WebExceptionCode.GLB_001_MSG, e);
		}
	}
    
	/**
	 * @author Madanipour
	 */
	public String doAccessViewAndChangeOfficeSetting() throws BaseException {
		CardRequestDelegator cardRequestDelegator = new CardRequestDelegator();
		try {
			EnrollmentOfficeDelegator enrollmentOfficeDelegator = new EnrollmentOfficeDelegator();
			accessViewAndChangeOfficeSetting  = enrollmentOfficeDelegator.getAccessViewAndChangeOfficeSetting(getUserProfile());
			
			return SUCCESS_RESULT;
		} catch (BusinessSecurityException e) {
			throw new ActionException(WebExceptionCode.CRA_006,
					WebExceptionCode.GLB_001_MSG, e);
		}

	}

        

    public String getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(String enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public String getSubstitutionOfficeId() {
        return substitutionOfficeId;
    }

    public void setSubstitutionOfficeId(String substitutionOfficeId) {
        this.substitutionOfficeId = substitutionOfficeId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public OfficeCardRequestStates getInProgressRequestsFlag() {
        return inProgressRequestsFlag;
    }

    public void setInProgressRequestsFlag(OfficeCardRequestStates inProgressRequestsFlag) {
        this.inProgressRequestsFlag = inProgressRequestsFlag;
    }
    
 	public String getOfficeSettingType() {
 		return officeSettingType;
 	}

 	public void setOfficeSettingType(String officeSettingType) {
 		this.officeSettingType = officeSettingType;
 	}

	public Boolean getAccessViewAndChangeOfficeSetting() {
		return accessViewAndChangeOfficeSetting;
	}

	public void setAccessViewAndChangeOfficeSetting(
			Boolean accessViewAndChangeOfficeSetting) {
		this.accessViewAndChangeOfficeSetting = accessViewAndChangeOfficeSetting;
	}


 	
 	
     
}
