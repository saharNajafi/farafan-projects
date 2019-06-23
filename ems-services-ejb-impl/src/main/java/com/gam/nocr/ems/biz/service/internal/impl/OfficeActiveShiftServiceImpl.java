package com.gam.nocr.ems.biz.service.internal.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.dao.OfficeActiveShiftDAO;
import com.gam.nocr.ems.data.dao.ReservationDAO;
import com.gam.nocr.ems.data.domain.EnrollmentOfficeTO;
import com.gam.nocr.ems.data.domain.OfficeActiveShiftTO;
import com.gam.nocr.ems.data.domain.OfficeCapacityTO;
import com.gam.nocr.ems.data.enums.ShiftEnum;
import com.gam.nocr.ems.util.CalendarUtil;
import org.slf4j.Logger;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 1/8/18.
 */
@Stateless(name = "OfficeActiveShiftService")
@Local(OfficeActiveShiftServiceLocal.class)
@Remote(OfficeActiveShiftServiceRemote.class)
public class OfficeActiveShiftServiceImpl extends EMSAbstractService
        implements OfficeActiveShiftServiceLocal, OfficeActiveShiftServiceRemote {

    private static final Logger logger = BaseLog
            .getLogger(OfficeActiveShiftServiceImpl.class);

    @Override
    public OfficeActiveShiftTO OfficeActiveShiftByOfficeIdAndRsvDate(Long officeId, ShiftEnum shiftEnum, int myDate) throws BaseException {
        return getActiveShiftDAO().OfficeActiveShiftByOfficeIdAndRsvDate(officeId, shiftEnum, myDate);
    }


    public OfficeActiveShiftTO findByEnrollmentOfficeId(Long enrollmentOfficeId, int activeDate, ShiftEnum shiftNo) throws BaseException {
        OfficeActiveShiftTO activeShiftTO = null;
        try {
            activeShiftTO = getActiveShiftDAO().findByEnrollmentOfficeId(enrollmentOfficeId, activeDate, shiftNo);
        } catch (BaseException e) {
            logger.error("findByEnrollmentOfficeId failed: ", e);
        }
        return activeShiftTO;
    }

    public void editActiveShiftRemainCapacity(Long activeShiftId, int remainCapacity) throws BaseException {
        getActiveShiftDAO().editActiveShiftRemainCapacity(activeShiftId, remainCapacity);
    }

    @Override
    public void activeShiftSaveOrUpdate(OfficeCapacityTO officeCapacityTO, OfficeActiveShiftTO officeActiveShiftTO, EnrollmentOfficeTO enrollmentOfficeTO, Date fromDate) throws BaseException {
        if (officeActiveShiftTO != null) {
            Integer newCapacity = Integer.valueOf(officeCapacityTO.getCapacity());
            Integer remainingCapacity = Integer.valueOf(officeActiveShiftTO.getRemainCapacity());
            if (newCapacity == 0) {
                officeActiveShiftTO.setRemainCapacity(0);
            } else if (remainingCapacity - newCapacity > 0) {
                officeActiveShiftTO.setRemainCapacity(newCapacity);
            } else if (remainingCapacity - newCapacity < 0) {
                Integer enrollmentOfficeReservedCount = getReservationDAO().findByEnrolAndReserveDateAndShift
                        (enrollmentOfficeTO.getId(), fromDate, officeActiveShiftTO.getShiftNo());
                officeActiveShiftTO.setRemainCapacity((newCapacity - enrollmentOfficeReservedCount));
            }
            officeActiveShiftTO.setShiftNo(officeCapacityTO.getShiftNo());
            officeActiveShiftTO.setOfficeCapacity(officeCapacityTO);
            Boolean activeDaysBaseOnShiftNo = checkValidActiveDate(officeCapacityTO.getShiftNo(), enrollmentOfficeTO, fromDate);
            if (!activeDaysBaseOnShiftNo) {
                officeActiveShiftTO.setRemainCapacity(0);
            }
            getActiveShiftDAO().update(officeActiveShiftTO);
        } else {
            Boolean activeDaysBaseOnShiftNo = checkValidActiveDate(officeCapacityTO.getShiftNo(), enrollmentOfficeTO, fromDate);
            if (activeDaysBaseOnShiftNo) {
                OfficeActiveShiftTO officeActiveShiftNew = new OfficeActiveShiftTO();
                officeActiveShiftNew.setEnrollmentOffice(enrollmentOfficeTO);
                officeActiveShiftNew.setShiftNo(officeCapacityTO.getShiftNo());
                officeActiveShiftNew.setOfficeCapacity(officeCapacityTO);
                officeActiveShiftNew.setActiveDate(Integer.valueOf(CalendarUtil.getDate(fromDate, new Locale("fa")).replace("/","")));
                Integer newCapacity = Integer.valueOf(officeCapacityTO.getCapacity());
                officeActiveShiftNew.setRemainCapacity(newCapacity);
                getActiveShiftDAO().create(officeActiveShiftNew);
            }
        }

    }

    public OfficeActiveShiftTO findActiveShiftByOfficeCapacity(Long officeCapacity) throws BaseException{
        DateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        int activeDate = Integer.parseInt(CalendarUtil.convertGregorianToPersian(date.format(
                new Date())).replaceAll("/", ""));
       return getActiveShiftDAO().findActiveShiftByOfficeCapacityAndActiveDate(officeCapacity, activeDate);
    }

    public Boolean checkValidActiveDate(ShiftEnum shiftEnum, EnrollmentOfficeTO enrollmentOfficeTO, Date fromDate) {

        Boolean activeFlag = Boolean.TRUE;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fromDate);

        // because calender starts from 1. 5 is thusday and 6 is friday.
        int indexOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        switch (shiftEnum) {
            case MORNING:
                switch (indexOfWeek) {
                    case 5:
                        if (!enrollmentOfficeTO.getThursdayMorningActive())
                            activeFlag = Boolean.FALSE;
                        break;
                    case 6:
                        if (!enrollmentOfficeTO.getFridayMorningActive())
                            activeFlag = Boolean.FALSE;
                        break;
                }
                break;
            case EVENING:
                switch (indexOfWeek) {
                    case 5:
                        if (!enrollmentOfficeTO.getThursdayEveningActive())
                            activeFlag = Boolean.FALSE;
                        break;
                    case 6:
                        if (!enrollmentOfficeTO.getFridayEveningActive())
                            activeFlag = Boolean.FALSE;
                        break;
                }
                break;
            default:
                break;
        }
        return activeFlag;
    }

    private OfficeActiveShiftDAO getActiveShiftDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(EMSLogicalNames
                    .getDaoJNDIName(EMSLogicalNames.DAO_OFFICE_ACTIVE_SHIFT));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.OASH_001,
                    BizExceptionCode.GLB_001_MSG, e,
                    EMSLogicalNames.DAO_OFFICE_ACTIVE_SHIFT.split(","));
        }
    }

    /**
     * getReservationDAO
     *
     * @return an instance of type ReservationDAO
     * @throws {@link BaseException}
     */
    private ReservationDAO getReservationDAO() throws BaseException {
        ReservationDAO reservationDAO;
        try {
            reservationDAO = DAOFactoryProvider.getDAOFactory().getDAO(EMSLogicalNames
                    .getDaoJNDIName(EMSLogicalNames.DAO_RESERVATION));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.PTL_006,
                    BizExceptionCode.GLB_001_MSG, e,
                    EMSLogicalNames.DAO_RESERVATION.split(","));
        }
        return reservationDAO;
    }

}
