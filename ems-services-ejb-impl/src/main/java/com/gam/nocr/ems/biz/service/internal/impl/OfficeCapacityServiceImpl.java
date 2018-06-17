package com.gam.nocr.ems.biz.service.internal.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.BizLoggable;
import com.gam.commons.core.biz.service.Permissions;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.commons.profile.ConfigurationFileHandler;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.dao.EnrollmentOfficeDAO;
import com.gam.nocr.ems.data.dao.OfficeCapacityDAO;
import com.gam.nocr.ems.data.domain.EnrollmentOfficeTO;
import com.gam.nocr.ems.data.domain.OfficeCapacityTO;
import com.gam.nocr.ems.data.domain.vol.OfficeCapacityVTO;
import com.gam.nocr.ems.data.enums.ShiftEnum;
import com.gam.nocr.ems.util.CalendarUtil;

import javax.annotation.Resource;
import javax.ejb.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.gam.nocr.ems.config.EMSLogicalNames.DAO_ENROLLMENT_OFFICE;
import static com.gam.nocr.ems.config.EMSLogicalNames.getDaoJNDIName;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 1/8/18.
 */
@Stateless(name = "OfficeCapacityService")
@Local(OfficeCapacityServiceLocal.class)
@Remote(OfficeCapacityServiceRemote.class)
public class OfficeCapacityServiceImpl extends EMSAbstractService implements
        OfficeCapacityServiceLocal, OfficeCapacityServiceRemote {
    @Resource
    SessionContext sessionContext;

    @Override
    public Long save(OfficeCapacityVTO officeCapacityVTO) throws BaseException {

        OfficeCapacityTO officeCapacityTO = null;
        int endDate;
        try {
            List<OfficeCapacityTO> officeCapacityTOList;
//        checkOfficeCapacity(officeCapacityVTO);
            String toDateWithSlash = convertGregorianToPersian(officeCapacityVTO.getStartDate());
            int toDateWithoutSlash = Integer.valueOf(toDateWithSlash.replace("/", ""));
            Date date;
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Boolean previuosAdded = false;
            date = format.parse(officeCapacityVTO.getStartDate());
            if (date.before(new Date()))
                throw new ServiceException(BizExceptionCode.OC_006, BizExceptionCode.OC_006_MSG);
            if(officeCapacityVTO.getEnrollmentOfficeId() == null)
                throw new ServiceException(BizExceptionCode.OC_003, BizExceptionCode.OC_003_MSG);
            officeCapacityTOList = getOfficeCapacityDAO().findByEnrollmentOfficeId(officeCapacityVTO.getEnrollmentOfficeId());
            Integer previousDay = Integer.valueOf(convertGregorianToPersian(
                            getPreviousDay(officeCapacityVTO.getStartDate())).replace("/", ""));
            if (officeCapacityTOList != null) {
                for (OfficeCapacityTO officeCapacity : officeCapacityTOList) {
                    if (officeCapacity.getStartDate() == toDateWithoutSlash)
                        throw new ServiceException(BizExceptionCode.OC_007, BizExceptionCode.OC_007_MSG);
                    int currentIndex = officeCapacityTOList.indexOf(officeCapacity);
                    if(currentIndex == 0 && toDateWithoutSlash < officeCapacity.getStartDate()){
                        endDate = Integer.valueOf(convertGregorianToPersian(getPreviousDay(
                                convertPersianToGregorian(String.valueOf(
                                        officeCapacity.getStartDate())
                                ))).replace("/", ""));
                        officeCapacityTO = createOfficeCapacity(officeCapacityVTO, endDate);
                        previuosAdded = true;
                    }
                    if (currentIndex == officeCapacityTOList.size() - 1
                            && toDateWithoutSlash > officeCapacity.getStartDate()) {
                        officeCapacity.setEndDate(previousDay);
                        getOfficeCapacityDAO().update(officeCapacity);
                        endDate = Integer.parseInt(ConfigurationFileHandler.getInstance().getProperty(
                                "officeCapacity-endDate").toString());
                        officeCapacityTO = createOfficeCapacity(officeCapacityVTO, endDate);

                    } else {
                        if(!previuosAdded) {
                            if (currentIndex > 0) {
                                int previousIndex = currentIndex - 1;
                                OfficeCapacityTO previousOfficeCapacity;
                                    previousOfficeCapacity = officeCapacityTOList.get(previousIndex);
                                if (toDateWithoutSlash > previousOfficeCapacity.getStartDate()
                                        && toDateWithoutSlash < officeCapacity.getStartDate()) {
                                    previousOfficeCapacity.setEndDate(previousDay);
                                    getOfficeCapacityDAO().update(previousOfficeCapacity);
                                    endDate = Integer.valueOf(convertGregorianToPersian(getPreviousDay(
                                            convertPersianToGregorian(String.valueOf(
                                                    officeCapacity.getStartDate())
                                            ))).replace("/", ""));
                                    officeCapacityTO = createOfficeCapacity(officeCapacityVTO, endDate);
                                    previuosAdded = true;
                                }
                            }
                        }
                    }
                }
            } else {
                 endDate = Integer.parseInt(ConfigurationFileHandler.getInstance().getProperty(
                        "officeCapacity-endDate").toString());
                officeCapacityTO = createOfficeCapacity(officeCapacityVTO, endDate);
            }

        } catch (ParseException e) {
            throw new ServiceException(BizExceptionCode.OC_010, BizExceptionCode.OC_010_MSG, e);
        }
        return officeCapacityTO != null ? officeCapacityTO.getId() : null;
    }

    @Override
    public Long update(OfficeCapacityVTO officeCapacityVTO) throws BaseException {
        OfficeCapacityTO officeCapacityTO = null;
        try {
            officeCapacityTO = getOfficeCapacityDAO().find(OfficeCapacityTO.class, officeCapacityVTO.getId());
            if (officeCapacityTO == null)
                throw new ServiceException(BizExceptionCode.OC_002, BizExceptionCode.OC_002_MSG, new Long[]{officeCapacityVTO.getId()});
            officeCapacityTO.setCapacity(Short.parseShort(officeCapacityVTO.getCapacity()));
            officeCapacityTO = getOfficeCapacityDAO().update(officeCapacityTO);
        } catch (ServiceException e) {
            sessionContext.setRollbackOnly();
            throw new ServiceException(BizExceptionCode.OC_011, BizExceptionCode.OC_011_MSG, e);
        }
        return officeCapacityTO != null ? officeCapacityTO.getId() : null;
    }

    @Override
    public OfficeCapacityVTO load(Long officeCapacityId) throws BaseException {
        OfficeCapacityTO officeCapacityTO;
        OfficeCapacityVTO officeCapacityVTO = new OfficeCapacityVTO();
        try {
            if (officeCapacityId == null)
                throw new ServiceException(BizExceptionCode.OC_001, BizExceptionCode.OC_001_MSG);
            officeCapacityTO = getOfficeCapacityDAO().find(OfficeCapacityTO.class, officeCapacityId);
            if (officeCapacityTO == null)
                throw new ServiceException(BizExceptionCode.OC_005,
                        BizExceptionCode.OC_005_MSG, new Long[]{officeCapacityId});
            officeCapacityVTO.setCapacity(String.valueOf(officeCapacityTO.getCapacity()));
            officeCapacityVTO.setStartDate(String.valueOf(officeCapacityTO.getStartDate()));
            officeCapacityVTO.setEndDate(String.valueOf(officeCapacityTO.getEndDate()));
            officeCapacityVTO.setEnrollmentOfficeId(officeCapacityTO.getEnrollmentOffice().getId());
            officeCapacityVTO.setShiftNo(String.valueOf(officeCapacityTO.getShiftNo()));
            officeCapacityVTO.setWorkingHoursFrom(officeCapacityTO.getWorkingHoursFrom());
            officeCapacityVTO.setWorkingHoursTo(officeCapacityTO.getWorkingHoursTo());
        }catch (ServiceException e){
            throw new ServiceException(BizExceptionCode.OC_012, BizExceptionCode.OC_012_MSG, e);
        }
        return officeCapacityVTO;
    }


    private String convertPersianToGregorian(String date) {
        String DELIMITER = "/";
        String year = date.substring(0, 4);
        String month = date.substring(4, 6);
        String day = date.substring(6, 8);
        String startDateWithSlash = year + DELIMITER + month + DELIMITER + day;
        return CalendarUtil.convertPersianToGregorian(startDateWithSlash).concat("T00:00:00");
    }

    private String getPreviousDay(String endDateWithSlash) throws BaseException {
        if(endDateWithSlash.contains("/")){
            endDateWithSlash = endDateWithSlash.replace("/", "-");
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
        String previousDay = "";
        try {
            previousDay = dateFormat.format(
                    dateFormat.parse(endDateWithSlash).getTime() - MILLIS_IN_DAY);
        } catch (ParseException e) {
            throw new ServiceException(BizExceptionCode.OC_008, BizExceptionCode.OC_008_MSG, e);
        }
        return  previousDay;
    }

    private String convertGregorianToPersian(String toDate) {
        String DELIMITER = "/";
        String year = toDate.substring(0, 4);
        String month = toDate.substring(5, 7);
        String day = toDate.substring(8, 10);
        String startDateWithSlash = day + DELIMITER + month + DELIMITER + year;
        return CalendarUtil.convertGregorianToPersian(startDateWithSlash);

    }

    private OfficeCapacityTO createOfficeCapacity(OfficeCapacityVTO officeCapacityVTO, int endDate) throws BaseException  {
        OfficeCapacityTO capacityTO = new OfficeCapacityTO();
        EnrollmentOfficeTO enrollmentOfficeTO;
        String shiftNo = null;
        try {
            String startDateWithSlash = convertGregorianToPersian(officeCapacityVTO.getStartDate());
            capacityTO.setStartDate(Integer.valueOf(startDateWithSlash.replace("/", "")));
            capacityTO.setEndDate(endDate);
            capacityTO.setWorkingHoursFrom(officeCapacityVTO.getWorkingHoursFrom());
            capacityTO.setWorkingHoursTo(officeCapacityVTO.getWorkingHoursTo());
            capacityTO.setCapacity(Short.parseShort(officeCapacityVTO.getCapacity()));
            enrollmentOfficeTO = getEnrollmentOfficeDAO().findEnrollmentOfficeById(
                            officeCapacityVTO.getEnrollmentOfficeId());
            if (enrollmentOfficeTO != null)
                capacityTO.setEnrollmentOffice(enrollmentOfficeTO);
            if( officeCapacityVTO.getShiftNo().equals("1"))
                shiftNo = "0";
            else if(officeCapacityVTO.getShiftNo().equals("2"))
                shiftNo = "1";
            capacityTO.setShiftNo(ShiftEnum.getShift(shiftNo));
            capacityTO = getOfficeCapacityDAO().create(capacityTO);
        } catch (ServiceException e) {
            throw new ServiceException(BizExceptionCode.OC_009, BizExceptionCode.OC_009_MSG, e);
        }
        return capacityTO;
    }

    private void checkOfficeCapacity(OfficeCapacityVTO officeCapacity) throws BaseException {

    }

    private OfficeCapacityDAO getOfficeCapacityDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(EMSLogicalNames
                    .getDaoJNDIName(EMSLogicalNames.DAO_OFFICE_CAPACITY));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.EOS_086,
                    BizExceptionCode.GLB_001_MSG, e,
                    EMSLogicalNames.DAO_OFFICE_CAPACITY.split(","));
        }
    }

    private EnrollmentOfficeDAO getEnrollmentOfficeDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(
                    getDaoJNDIName(DAO_ENROLLMENT_OFFICE));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.CRE_006,
                    BizExceptionCode.GLB_001_MSG, e,
                    DAO_ENROLLMENT_OFFICE.split(","));
        }
    }

}
