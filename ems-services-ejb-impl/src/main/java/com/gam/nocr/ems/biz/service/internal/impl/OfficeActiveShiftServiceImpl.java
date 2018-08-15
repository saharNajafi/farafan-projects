package com.gam.nocr.ems.biz.service.internal.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.dao.OfficeActiveShiftDAO;
import com.gam.nocr.ems.data.domain.OfficeActiveShiftTO;
import com.gam.nocr.ems.data.enums.ShiftEnum;
import com.gam.nocr.ems.util.CalendarUtil;
import org.slf4j.Logger;

import javax.ejb.*;
import javax.persistence.CacheStoreMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 1/8/18.
 */
@Stateless(name = "OfficeActiveShiftService")
@Local(OfficeActiveShiftServiceLocal.class)
@Remote(OfficeActiveShiftServiceRemote.class)
public class OfficeActiveShiftServiceImpl extends EMSAbstractService
implements OfficeActiveShiftServiceLocal, OfficeActiveShiftServiceRemote{

    private static final Logger logger = BaseLog
            .getLogger(OfficeActiveShiftServiceImpl.class);

    public OfficeActiveShiftTO findActiveShiftByEofId(Long enrollmentOfficeId) throws BaseException {
        int activeDate;
        ShiftEnum shiftNo;
        OfficeActiveShiftTO activeShiftTO = null;
        try {
            DateFormat date = new SimpleDateFormat("dd/MM/yyyy");
            activeDate = Integer.parseInt(CalendarUtil.convertGregorianToPersian(date.format(
                    new Date())).replaceAll("/", ""));
            SimpleDateFormat hours = new SimpleDateFormat("HH");
            shiftNo = ShiftEnum.getShift(String.valueOf(hours.parse(hours.format(
                    new Date())).before(hours.parse("12:00")) ? 0 : 1));
            activeShiftTO = findByEnrollmentOfficeId(enrollmentOfficeId, activeDate, shiftNo);
        } catch (ParseException e) {

        } catch (BaseException e) {
            throw e;
        }
        return activeShiftTO;
    }

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

    public void editActiveShiftRemainCapacity(Long activeShiftId, int remainCapacity) throws BaseException{
        getActiveShiftDAO().editActiveShiftRemainCapacity(activeShiftId, remainCapacity);
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

}
