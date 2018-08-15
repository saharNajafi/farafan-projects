package com.gam.nocr.ems.data.domain;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.data.enums.BooleanType;
import com.gam.nocr.ems.data.enums.ShiftEnum;
import com.gam.nocr.ems.util.EmsUtil;
import com.gam.nocr.ems.util.JSONable;
import flexjson.JSON;

import javax.persistence.*;
import java.util.Date;

/**
 * @author: Haeri (haeri@gamelectronics.com)
 */
@Entity
@Table(name = "EMST_RESERVATION")
@NamedQueries({
        @NamedQuery(
                name = "ReservationTO.findReservationByCardRequestId",
                query = " select rat " +
                        " from ReservationTO rat" +
                        " where rat.cardRequest.id =:cardRequestId"
        ),
        @NamedQuery(
                name = "ReservationTO.fetchReservationByPortalReservationId",
                query = " select res " +
                        "from ReservationTO res " +
                        " where res.portalReservationId =:portalReservationId " +
                        "order by res.id asc"
        ),
        @NamedQuery(
                name = "ReservationTO.findReservationByCrqId",
                query = " select res " +
                        "from ReservationTO res " +
                        " where res.cardRequest.id =:carqId " +
                        "order by res.id asc"
        ),
        @NamedQuery(
                name = "ReservationTO.findByEnrolAndReserveDateAndShift",
                query = " select count (res.id) " +
                        "from ReservationTO res " +
                        " where res.enrollmentOffice.id =:enrollmentOfficeId " +
                        "and  res.date=:reservationDate " +
                        "and res.shiftNo =:shiftNo "
        )
})
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_RESERVATION", allocationSize = 1)
public class ReservationTO extends ExtEntityTO implements JSONable {

    private CardRequestTO cardRequest;
    private EnrollmentOfficeTO enrollmentOffice;
    private Date date;
    private BooleanType attended;
    private Long portalReservationId;
    private boolean isPaid;
    private Date paidDate;
    private ShiftEnum shiftNo;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "RSV_ID")
    public Long getId() {
        return super.getId();
    }

    @ManyToOne
    @JoinColumn(name = "RSV_CARD_REQUEST_ID", nullable = false)
    @JSON(include = false)
    public CardRequestTO getCardRequest() {
        return cardRequest;
    }

    public void setCardRequest(CardRequestTO cardRequest) {
        this.cardRequest = cardRequest;
    }

    @OneToOne
    @JoinColumn(name = "RSV_ENROLLMENT_ID", nullable = false)
    @JSON(include = false)
    public EnrollmentOfficeTO getEnrollmentOffice() {
        return enrollmentOffice;
    }

    public void setEnrollmentOffice(EnrollmentOfficeTO enrollmentOffice) {
        this.enrollmentOffice = enrollmentOffice;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RSV_DATE")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "RSV_ATTENDED")
    public BooleanType getAttended() {
        return attended;
    }

    public void setAttended(BooleanType attended) {
        this.attended = attended;
    }

    @Column(name = "RSV_PORTAL_RESERVATION_ID")
    public Long getPortalReservationId() {
        return portalReservationId;
    }

    public void setPortalReservationId(Long portalReservationId) {
        this.portalReservationId = portalReservationId;
    }

    @Override
    public String toString() {
        return toJSON();
    }

    @Transient
    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    @Transient
    public Date getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(Date paidDate) {
        this.paidDate = paidDate;
    }

    @Basic(optional = false)
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "RSV_SHIFT_NO")
    public ShiftEnum getShiftNo() {
        return shiftNo;
    }

    public void setShiftNo(ShiftEnum shiftNo) {
        this.shiftNo = shiftNo;
    }

    /**
     * The method toJSON is used to convert an object to an instance of type {@link String}
     *
     * @return an instance of type {@link String}
     */
    @Override
    public String toJSON() {
        String jsonObject = EmsUtil.toJSON(this);
        jsonObject = jsonObject.substring(0, jsonObject.length() - 1);
        if (cardRequest == null) {
            jsonObject += ", cardRequestId:" + cardRequest;
        } else {
            jsonObject += ", cardRequestId:" + cardRequest.getId();
        }
        if (enrollmentOffice == null) {
            jsonObject += ", enrollmentOfficeId:" + enrollmentOffice;
        } else {
            jsonObject += ", enrollmentOfficeId:" + enrollmentOffice.getId();
        }
        jsonObject += "}";

        return jsonObject;
    }
}
