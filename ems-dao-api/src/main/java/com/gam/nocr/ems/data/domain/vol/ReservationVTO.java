package com.gam.nocr.ems.data.domain.vol;

import java.util.Date;

/**
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class ReservationVTO {
    private Long cardRequestTOIdForUpdateState;
    private Long cardRequestTOIdForUpdateReEnrolledDate;
    private Long portalReserveTOId;
    private Date reservationDate;

    public Long getCardRequestTOIdForUpdateState() {
        return cardRequestTOIdForUpdateState;
    }

    public void setCardRequestTOIdForUpdateState(Long cardRequestTOIdForUpdateState) {
        this.cardRequestTOIdForUpdateState = cardRequestTOIdForUpdateState;
    }

    public Long getCardRequestTOIdForUpdateReEnrolledDate() {
        return cardRequestTOIdForUpdateReEnrolledDate;
    }

    public void setCardRequestTOIdForUpdateReEnrolledDate(Long cardRequestTOIdForUpdateReEnrolledDate) {
        this.cardRequestTOIdForUpdateReEnrolledDate = cardRequestTOIdForUpdateReEnrolledDate;
    }

    public Long getPortalReserveTOId() {
        return portalReserveTOId;
    }

    public void setPortalReserveTOId(Long portalReserveTOId) {
        this.portalReserveTOId = portalReserveTOId;
    }

    public Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }
}
