package com.gam.nocr.ems.data.domain;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.data.enums.*;
import com.gam.nocr.ems.util.EmsUtil;
import com.gam.nocr.ems.util.JSONable;
import flexjson.JSON;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Haeri (haeri@gamelectronics.com)
 */
@Entity
@Table(name = "EMST_CARD_REQUEST")
@NamedQueries({
        @NamedQuery(
                name = "CardRequestTO.findCardRequestStateByNationalIdAndMobile",
                query = " select crq " +
                        " from CitizenTO ctz, CardRequestTO crq, CitizenInfoTO czi" +
                        " where crq.citizen.nationalID =:nationalId" +
                        " and crq.citizen.citizenInfo.mobile =:mobile" +
                        " and crq.citizen.id = ctz.id" +
                        " and czi.citizen.id = ctz.id" +
                        " order by crq.id desc"),
        @NamedQuery(
                name = "CardRequestTO.findCardRequestStateByNationalIdAndBirthCertificateSeries",
                query = " select crq " +
                        " from CardRequestTO crq, CitizenInfoTO czi" +
                        " where crq.citizen.nationalID =:nationalId" +
                        " and crq.citizen.citizenInfo.birthCertificateSeries =:birthCertificateSeries" +
                        " and crq.citizen.id = czi.id" +
                        " order by crq.id desc"),
        @NamedQuery(
                name = "CardRequestTO.findCardRequestStateByTrackingId",
                query = " select crq " +
                        " from CardRequestTO crq" +
                        " where crq.trackingID=:trackingId" +
                        " order by crq.id desc"),
        @NamedQuery(
                name = "CardRequestTO.findCardRequestStateByNationalId",
                query = " select crq " +
                        " from CardRequestTO crq" +
                        " where crq.citizen.nationalID =:nationalId" +
                        " order by crq.id desc "),
        @NamedQuery(
                name = "CardRequestTO.findByCRSRequestId",
                query = "select crq" +
                        " from CardRequestTO crq " +
                        " where crq.portalRequestId=:portalRequestId " +
                        " order by crq.id desc"
        ),
        @NamedQuery(
                name = "CardRequestTO.findLastRequestByNationalId",
                query = "select crq from CardRequestTO crq  where crq.id " +
                        "= (select MAX(crqq.id) from CardRequestTO crqq" +
                        " where crqq.citizen.nationalID=:nationalId)"
        ),
        @NamedQuery(
                name = "CardRequestTO.fetchCardRequest",
                query = "select crq " +
                        " from CardRequestTO crq " +
                        " where crq.id =:cardRequestId "
        ),
        @NamedQuery(
                name = "CardRequestTO.findByCitizen",
                query = "select crq " +
                        "from CardRequestTO crq " +
                        " where crq.citizen.id =:citizenId " +
                        "order by crq.id desc"
        ),
        @NamedQuery(
                name = "CardRequestTO.updateCardRequestsState",
                query = "UPDATE CardRequestTO crs " +
                        "SET crs.state =:CARD_REQUEST_STATE " +
                        "WHERE crs.id IN (:ID_LIST)"
        ),
        @NamedQuery(
                name = "CardRequestTO.readyEstelam2Flag",
                query = "UPDATE CardRequestTO crs " +
                        "SET crs.estelam2Flag =:READY, crs.requestedSmsStatus =:status " +
                        "WHERE crs.id IN (:ID_LIST)"
        ),
        @NamedQuery(
                name = "CardRequestTO.updateCardRequestOfficeId",
                query = "UPDATE CardRequestTO crs " +
                        "SET crs.enrollmentOffice.id =:enrollmentOfficeId " +
                        "WHERE crs.id = :cardRequestId " +
                        "and crs.originalCardRequestOfficeId is null"
        ),
        @NamedQuery(
                name = "CardRequestTO.updateReEnrolledDateByCardRequestId",
                query = "UPDATE CardRequestTO crq " +
                        "SET crq.reEnrolledDate =:reEnrolledDate " +
                        "WHERE crq.id IN (:cardRequestId)"
        ),
        @NamedQuery(
                name = "CardRequestTO.findRegistrationPaymentId",
                query = "select crq " +
                        "from CardRequestTO crq " +
                        "where crq.id =:requestId " +
                        "order by crq.id desc "
        )


})

@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_CARD_REQUEST", allocationSize = 1)
public class CardRequestTO extends ExtEntityTO implements Serializable, JSONable {

    private EnrollmentOfficeTO enrollmentOffice;
    private CitizenTO citizen;
    private Date enrolledDate;
    private Date reEnrolledDate;
    private Date portalEnrolledDate;
    private Date portalLastModifiedDate;
    private byte[] signedReceipt;
    private String receiptText;
    private byte[] officerSign;
    private CardRequestState state;
    private CardRequestAuthenticity authenticity;
    private String trackingID;
    private CardTO card;
    private CardRequestType type;
    private String reason;
    private Integer priority;
    private String metadata;
    private CardRequestOrigin origin;
    private String archiveId;
    private CardRequestedAction requestedAction;
    private Long originalCardRequestOfficeId;
    private Date reservationDate;
    private Date lockDate;
    private Long deliveredOfficeId;
    private String cmsRequestId;
    /**
     * Using at portal interchanging
     */
    private Long portalRequestId;
    private Date lastSyncDate;
    private Date lastModifiedDate;
    private Estelam2FlagType estelam2Flag;
    private boolean isPaid = false;
    private Date paidDate;
    private Integer requestedSmsStatus = 0;
    private List<ReservationTO> reservations = new ArrayList<ReservationTO>(0);

    private RegistrationPaymentTO registrationPaymentTO;

    public CardRequestTO() {
    }

    public CardRequestTO(Long id) {
        this.setId(id);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "CRQ_ID")
    public Long getId() {
        return super.getId();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CRQ_ENROLL_OFFICE_ID")
    @JSON(include = false)
    public EnrollmentOfficeTO getEnrollmentOffice() {
        return enrollmentOffice;
    }

    public void setEnrollmentOffice(EnrollmentOfficeTO enrollmentOffice) {
        this.enrollmentOffice = enrollmentOffice;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "CRQ_CITIZEN_ID", nullable = false)
    @JSON(include = false)
    public CitizenTO getCitizen() {
        return citizen;
    }

    public void setCitizen(CitizenTO citizen) {
        this.citizen = citizen;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CRQ_ENROLLED_DATE")
    @JSON(include = false)
    public Date getEnrolledDate() {
        return enrolledDate;
    }

    public void setEnrolledDate(Date enrolledDate) {
        this.enrolledDate = enrolledDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CRQ_REENROLLED_DATE")
    public Date getReEnrolledDate() {
        return reEnrolledDate;
    }

    public void setReEnrolledDate(Date reEnrolledDate) {
        this.reEnrolledDate = reEnrolledDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CRQ_PORTAL_ENROLLED_DATE")
    public Date getPortalEnrolledDate() {
        return portalEnrolledDate;
    }

    public void setPortalEnrolledDate(Date portalEnrolledDate) {
        this.portalEnrolledDate = portalEnrolledDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CRQ_PORTAL_LAST_MODIFIED_DATE")
    public Date getPortalLastModifiedDate() {
        return portalLastModifiedDate;
    }

    public void setPortalLastModifiedDate(Date portalLastModifiedDate) {
        this.portalLastModifiedDate = portalLastModifiedDate;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "CRQ_SIGNED_RECEIPT")
    @JSON(include = false)
    public byte[] getSignedReceipt() {
        return signedReceipt == null ? signedReceipt : signedReceipt.clone();
    }

    public void setSignedReceipt(byte[] signedReceipt) {
        this.signedReceipt = signedReceipt;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "CRQ_RECEIPT_TEXT")
    @JSON(include = false)
    public String getReceiptText() {
        return receiptText;
    }

    public void setReceiptText(String receiptText) {
        this.receiptText = receiptText;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "CRQ_OFFICER_SIGN")
    @JSON(include = false)
    public byte[] getOfficerSign() {
        return officerSign == null ? officerSign : officerSign.clone();
    }

    public void setOfficerSign(byte[] officerSign) {
        this.officerSign = officerSign;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "CRQ_STATE", nullable = false)
    public CardRequestState getState() {
        return state;
    }

    public void setState(CardRequestState state) {
        this.state = state;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "CRQ_AUTHENTICITY")
    @JSON(include = false)
    public CardRequestAuthenticity getAuthenticity() {
        return authenticity;
    }

    public void setAuthenticity(CardRequestAuthenticity authenticity) {
        this.authenticity = authenticity;
    }

    @Size(min = 10, max = 10)
    @Column(name = "CRQ_TRACKING_ID")
    public String getTrackingID() {
        return trackingID;
    }

    public void setTrackingID(String trackingID) {
        this.trackingID = trackingID;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CRQ_CARD_ID")
    @JSON(include = false)
    public CardTO getCard() {
        return card;
    }

    public void setCard(CardTO card) {
        this.card = card;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "CRQ_TYPE", nullable = false)
    public CardRequestType getType() {
        return type;
    }

    public void setType(CardRequestType type) {
        this.type = type;
    }

    @OneToMany(mappedBy = "cardRequest")
    public List<ReservationTO> getReservations() {
        return reservations;
    }

    public void setReservations(List<ReservationTO> reservations) {
        this.reservations = reservations;
    }

    @Column(name = "CRQ_REASON")
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Column(name = "CRQ_PORTAL_REQUEST_ID")
    public Long getPortalRequestId() {
        return portalRequestId;
    }

    public void setPortalRequestId(Long portalRequestId) {
        this.portalRequestId = portalRequestId;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CRQ_LAST_SYNC_DATE")
    @JSON(include = false)
    public Date getLastSyncDate() {
        return lastSyncDate;
    }

    public void setLastSyncDate(Date lastSyncDate) {
        this.lastSyncDate = lastSyncDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CRQ_LAST_MODIFIED_DATE")
    @JSON(include = false)
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Column(name = "CRQ_PRIORITY")
    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Lob
    @Column(name = "CRQ_METADATA")
    @JSON(include = false)
    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "CRQ_ORIGIN", columnDefinition = "NVARCHAR2(1)")
    public CardRequestOrigin getOrigin() {
        return origin;
    }

    public void setOrigin(CardRequestOrigin origin) {
        this.origin = origin;
    }

    @Column(name = "CRQ_ARCHIVE_ID")
    public String getArchiveId() {
        return archiveId;
    }

    public void setArchiveId(String archiveId) {
        this.archiveId = archiveId;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "CRQ_REQUESTED_ACTION")
    public CardRequestedAction getRequestedAction() {
        return requestedAction;
    }

    public void setRequestedAction(CardRequestedAction requestedAction) {
        this.requestedAction = requestedAction;
    }

    @Column(name = "CRQ_ORIGINAL_ENROLL_OFFICE_ID")
    public Long getOriginalCardRequestOfficeId() {
        return originalCardRequestOfficeId;
    }

    public void setOriginalCardRequestOfficeId(Long originalCardRequestOfficeId) {
        this.originalCardRequestOfficeId = originalCardRequestOfficeId;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CRQ_RSV_DATE")
    @JSON(include = false)
    public Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CRQ_LOCK_DATE")
    @JSON(include = false)
    public Date getLockDate() {
        return lockDate;
    }

    public void setLockDate(Date lockDate) {
        this.lockDate = lockDate;
    }

    @Column(name = "CRQ_DELIVERED_OFFICE_ID")
    public Long getDeliveredOfficeId() {
        return deliveredOfficeId;
    }

    public void setDeliveredOfficeId(Long deliveredOfficeId) {
        this.deliveredOfficeId = deliveredOfficeId;
    }


    @Column(name = "CRQ_CMS_REQ_ID", columnDefinition = "CHAR(16 BYTE)")
    public String getCmsRequestId() {
        return cmsRequestId;
    }

    public void setCmsRequestId(String cmsRequestId) {
        this.cmsRequestId = cmsRequestId;
    }


    @Override
    public String toString() {
        return toJSON();
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "CRQ_ESTELAM2_FLAG", length = 1)
    public Estelam2FlagType getEstelam2Flag() {
        return estelam2Flag;
    }

    public void setEstelam2Flag(Estelam2FlagType estelam2Flag) {
        this.estelam2Flag = estelam2Flag;
    }

    @Column(name = "CRQ_REQUESTED_SMS_STATUS")
    public Integer getRequestedSmsStatus() {
        return requestedSmsStatus;
    }

    public void setRequestedSmsStatus(Integer requestedSmsStatus) {
        this.requestedSmsStatus = requestedSmsStatus;
    }

    /**
     * The method toJSON is used to convert an object to an instance of type {@link String}
     *
     * @return an instance of type {@link String}
     */


    @Column(name = "CRQ_PAID_STATE")
    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    @Column(name = "CRQ_PAID_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @JSON(include = false)
    public Date getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(Date paidDate) {
        this.paidDate = paidDate;
    }


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CRQ_PAYMENT_ID")
    public RegistrationPaymentTO getRegistrationPaymentTO() {
        return registrationPaymentTO;
    }

    public void setRegistrationPaymentTO(RegistrationPaymentTO registrationPaymentTO) {
        this.registrationPaymentTO = registrationPaymentTO;
    }

    @Override
    public String toJSON() {
        String jsonObject = EmsUtil.toJSON(this);
        jsonObject = jsonObject.substring(0, jsonObject.length() - 1);

        if (enrollmentOffice == null) {
            jsonObject += "," + "\"enrollmentOfficeId\":" + enrollmentOffice;
        } else {
            jsonObject += "," + "\"enrollmentOfficeId\":" + enrollmentOffice.getId();
        }

        if (citizen == null) {
            jsonObject += "," + "\"citizenId\":" + citizen;
        } else {
            jsonObject += "," + "\"citizenId\":" + citizen.getId();
        }

        if (card == null) {
            jsonObject += "," + "\"cardId\":" + card;
        } else {
            jsonObject += "," + "\"cardId\":" + card.getId();
        }

        jsonObject += "}";
        return jsonObject;
    }

}
