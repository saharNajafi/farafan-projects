package com.gam.nocr.ems.web.action;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.web.struts2.extJsController.ActionException;
import com.gam.commons.core.web.struts2.extJsController.ListControllerImpl;
import com.gam.nocr.ems.biz.delegator.PersonDelegator;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.vol.PersonVTO;
import com.gam.nocr.ems.data.enums.ReplicaReason;
import com.gam.nocr.ems.data.enums.TokenType;
import com.gam.nocr.ems.util.EmsUtil;
import gampooya.tools.security.BusinessSecurityException;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * All AJAX interactions of person management part in 3S is handled by this class
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class PersonAction extends ListControllerImpl<PersonVTO> {

    private static final Logger logger = BaseLog.getLogger(PersonAction.class);

    /**
     * Identifier of a person that would be used by operations like 'save', 'load', etc.
     */
    private String personId;

    /**
     * Identifier of a person token that would be used by operations like 'reissuePersonToken', 'deliverPersonToken',
     * etc
     */
    private String tokenId;

    /**
     * Type of a person token that would be used by operations like 'issuePersonToken'
     */
    private String tokenType;

    /**
     * Reason of a person token issuance request that would be used by operations like 'issuePersonToken'
     */
    private String reason;

    /**
     * Used as the result of informAcceptableTypes method which specifies what token types requests can be made for
     * given user
     */
    private String validTokenTypes;

    /**
     * Check if user already has a delivered token
     */
    private String hasActiveToken;

    /**
     * The description text used when revoking a token
     */
    private String description;

    /**
     * Used by fetchRolePermissionList as the input parameter to fetch its permissions list
     */
    private String roleId;

    /**
     * The result of fetchRolePermissionList will be stored in this property to be retrieved by client
     */
    private String permissionList;

    /**
     * Must be implemented in order to set records by json populator
     *
     * @param records
     */
    @Override
    public void setRecords(List<PersonVTO> records) {
        this.records = records;
    }

    /**
     * Saves a person information
     *
     * @return {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String save() throws BaseException {
        try {
            PersonDelegator personDelegator = new PersonDelegator();
            for (PersonVTO to : records) {
                if (to.getId() == null && to.getUserName() == null)
                    personDelegator.save(getUserProfile(), to);
                else
                    personDelegator.delegate(getUserProfile(), to);
            }
            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.PEA_001, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    /**
     * Loads a person information base on its identifier specified as 'ids'
     *
     * @return {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String load() throws BaseException {
        try {
            PersonDelegator personDelegator = new PersonDelegator();
            PersonVTO person;

            if (ids != null)
                person = personDelegator.load(getUserProfile(), Long.parseLong(ids));
            else
                throw new ActionException(WebExceptionCode.PEA_005, WebExceptionCode.PEA_005_MSG);

            List<PersonVTO> personRecords = new ArrayList<PersonVTO>();
            personRecords.add(person);
            setRecords(personRecords);

            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.PEA_002, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    /**
     * Deletes a person information base on its identifier specified as 'ids'
     *
     * @return {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String delete() throws BaseException {
        try {
            PersonDelegator personDelegator = new PersonDelegator();

            personDelegator.remove(getUserProfile(), ids);

            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.PEA_003, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    /**
     * Reverses the status of current user. If he/she is already an active user, makes it de-active and vice versa.
     *
     * @return {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String changeStatus() throws BaseException {
        try {
            PersonDelegator personDelegator = new PersonDelegator();
            for (String id : ids.split(",")) {
                id = id.trim();
                personDelegator.changeStatus(getUserProfile(), Long.parseLong(id));
            }

            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.PEA_004, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    /**
     * Called by 3S when a user registration request (from CCOS) is rejected by an administrator in 3S
     *
     * @return {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String rejectPerson() throws BaseException {
        try {
            if (!EmsUtil.checkString(ids))
                throw new ActionException(WebExceptionCode.PEA_009, WebExceptionCode.PEA_009_MSG);

            new PersonDelegator().rejectPerson(getUserProfile(), ids);

            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.PEA_010, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    /**
     * Given a role identifier and returns the list of all permissions it has
     *
     * @return {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String fetchRolePermissionList() throws BaseException {
        try {
            setPermissionList(new PersonDelegator().fetchRolePermissionList(getUserProfile(), getRoleId()));

            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.PEA_011, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    // ================= Token Management ===============

    /**
     * Given a token request data (e.g. type) and registers a new token request (first token or replicate)
     *
     * @return {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String issuePersonToken() throws BaseException {
        try {
            PersonDelegator personDelegator = new PersonDelegator();

            if ("N".equals(getReason())) {
                //  This is a first token request
                TokenType type = null;

                if ("A".equals(getTokenType()))
                    type = TokenType.AUTHENTICATION;
                if ("S".equals(getTokenType()))
                    type = TokenType.SIGNATURE;
                if ("E".equals(getTokenType()))
                    type = TokenType.ENCRYPTION;

                personDelegator.issueToken(getUserProfile(), Long.parseLong(getPersonId()), type);

                return SUCCESS_RESULT;

            } else {
                //  This is a replicate token request
                TokenType type = null;
                ReplicaReason reason = null;

                if ("A".equals(getTokenType()))
                    type = TokenType.AUTHENTICATION;
                if ("S".equals(getTokenType()))
                    type = TokenType.SIGNATURE;
                if ("E".equals(getTokenType()))
                    type = TokenType.ENCRYPTION;

                if ("R".equals(getReason()))
                    reason = ReplicaReason.REPLICA;
                if ("D".equals(getReason()))
                    reason = ReplicaReason.DAMAGE;

                personDelegator.replicateToken(getUserProfile(), Long.parseLong(getPersonId()), type, reason);

                return SUCCESS_RESULT;
            }
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.PEA_007, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    /**
     * Given a token identifier and registers a reissue request for it (issuing the same token type)
     *
     * @return {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String reissuePersonToken() throws BaseException {
        try {
            PersonDelegator personDelegator = new PersonDelegator();

            personDelegator.reissueToken(getUserProfile(), Long.parseLong(getTokenId()));

            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.PEA_008, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    /**
     * Registers the delivery of person token to its owner
     *
     * @return {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String deliverPersonToken() throws BaseException {
        try {
            PersonDelegator personDelegator = new PersonDelegator();

            personDelegator.deliverToken(getUserProfile(), Long.parseLong(getTokenId()));

            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.PEA_012, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    /**
     * Revokes given person token
     *
     * @return {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String revokePersonToken() throws BaseException {
        try {
            PersonDelegator personDelegator = new PersonDelegator();

            ReplicaReason reason = null;
            String comment = this.description;

            try {
                if (getReason().contains("L")) {
                    reason = ReplicaReason.REPLICA;
                    comment = reason + "-" + comment;

                } else if (getReason().contains("D")) {
                    reason = ReplicaReason.DAMAGE;
                    comment = reason + "-" + comment;

                } else {
                    reason = ReplicaReason.UNSPECIFIED;

                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                reason = ReplicaReason.UNSPECIFIED;
            }

            if (comment == null || comment.trim().length() == 0)
                comment = "Not Specified.";

            personDelegator.revokeToken(getUserProfile(), Long.parseLong(getTokenId()), reason, comment);

            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.PEA_013, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    /**
     * Returns valid token types that given person can requests. It would be used in 3S GUI in order to enable/disable
     * token type radio buttons
     *
     * @return {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String informAcceptableTypes() throws BaseException {
        try {
            PersonDelegator personDelegator = new PersonDelegator();

            String acceptableTypes = personDelegator.informAcceptableTypes(getUserProfile(), Long.parseLong(getPersonId()));

            setValidTokenTypes(acceptableTypes);

            String deliveredTokens = personDelegator.checkDeliveredTokens(getUserProfile(), Long.parseLong(getPersonId()));
            setHasActiveToken(deliveredTokens);

            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.PEA_006, WebExceptionCode.GLB_001_MSG, e);
        }
    }

    /**
     * Removes a token request (if it's not already in progress or has not been processed)
     *
     * @return {@link com.gam.commons.core.web.struts2.extJsController.BaseController#SUCCESS_RESULT}
     * @throws BaseException
     */
    public String deleteToken() throws BaseException {
        PersonDelegator personDelegator = new PersonDelegator();
        try {
            personDelegator.deleteToken(getUserProfile(), Long.parseLong(tokenId));
            return SUCCESS_RESULT;
        } catch (BusinessSecurityException e) {
            throw new ActionException(WebExceptionCode.PEA_015, WebExceptionCode.GLB_001_MSG, e);
        }
    }
    // ================= Token Management ===============

    // ============== Accessor Methods =============

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getValidTokenTypes() {
        return validTokenTypes;
    }

    public void setValidTokenTypes(String validTokenTypes) {
        this.validTokenTypes = validTokenTypes;
    }

    public void setHasActiveToken(String hasActiveToken) {
        this.hasActiveToken = hasActiveToken;
    }

    public String getHasActiveToken() {
        return hasActiveToken;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(String permissionList) {
        this.permissionList = permissionList;
    }
}