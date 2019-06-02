package com.gam.nocr.ems.biz.service.internal.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.BizLoggable;
import com.gam.commons.core.biz.service.Permissions;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.dao.EnrollmentOfficeDAO;
import com.gam.nocr.ems.data.dao.OfficeCapacityDAO;
import com.gam.nocr.ems.data.domain.EnrollmentOfficeTO;
import com.gam.nocr.ems.data.domain.OfficeCapacityTO;
import com.gam.nocr.ems.data.domain.vol.OfficeCapacityVTO;
import com.gam.nocr.ems.data.enums.ShiftEnum;
import com.gam.nocr.ems.util.CalendarUtil;
import com.gam.nocr.ems.util.EmsUtil;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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


    private static final String DEFAULT_OFFICE_CAPACITY_END_DATE = "15000101";

    @Override
    public Long save(OfficeCapacityVTO officeCapacityVTO) throws BaseException {

        OfficeCapacityTO officeCapacityTO = null;
        int endDate;
        try {
            List<OfficeCapacityTO> officeCapacityTOList;
            String toDateWithSlash = convertGregorianToPersian(officeCapacityVTO.getStartDate());
            int toDateWithoutSlash = Integer.valueOf(toDateWithSlash.replace("/", ""));
            Date date;
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Boolean previuosAdded = false;
            date = format.parse(officeCapacityVTO.getStartDate());
            if (date.before(new Date()))
                throw new ServiceException(BizExceptionCode.OC_006, BizExceptionCode.OC_006_MSG);
            if (officeCapacityVTO.getEnrollmentOfficeId() == null)
                throw new ServiceException(BizExceptionCode.OC_003, BizExceptionCode.OC_003_MSG);
            officeCapacityTOList =
                    getOfficeCapacityDAO().findByEnrollmentOfficeIdAndShiftNo(
                            officeCapacityVTO.getEnrollmentOfficeId(), ShiftEnum.getShift(officeCapacityVTO.getShiftNo()));
            Integer previousDay = Integer.valueOf(convertGregorianToPersian(
                    getPreviousDay(officeCapacityVTO.getStartDate())).replace("/", ""));
            endDate = Integer.parseInt(EmsUtil.getProfileValue(
                    ProfileKeyName.KEY_OFFICE_CAPACITY_END_DATE, DEFAULT_OFFICE_CAPACITY_END_DATE));

            if (officeCapacityTOList != null) {
                for (OfficeCapacityTO officeCapacity : officeCapacityTOList) {
                    if (officeCapacity.getStartDate() == toDateWithoutSlash)
                        throw new ServiceException(BizExceptionCode.OC_007, BizExceptionCode.OC_007_MSG);
                    int currentIndex = officeCapacityTOList.indexOf(officeCapacity);
                    if (currentIndex == 0 && toDateWithoutSlash < officeCapacity.getStartDate()) {
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
                        officeCapacityTO = createOfficeCapacity(officeCapacityVTO, endDate);
                    } else {
                        if (!previuosAdded) {
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
                officeCapacityTO = createOfficeCapacity(officeCapacityVTO, endDate);
            }

        } catch (BaseException e) {
            throw e;
        } catch (ParseException e) {
            throw new ServiceException(BizExceptionCode.OC_010, BizExceptionCode.OC_010_MSG, e);
        }
        return officeCapacityTO != null ? officeCapacityTO.getId() : null;
    }

    @Override
    public Long update(OfficeCapacityVTO officeCapacityVTO) throws BaseException {
        OfficeCapacityTO officeCapacityTO = null;
        int endDate;
        try {
            List<OfficeCapacityTO> officeCapacityTOList;
            String toDateWithSlash = convertGregorianToPersian(officeCapacityVTO.getStartDate());
            int toDateWithoutSlash = Integer.valueOf(toDateWithSlash.replace("/", ""));
            Date date;
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Boolean previuosAdded = false;
            date = format.parse(officeCapacityVTO.getStartDate());
            if (date.before(new Date()))
                throw new ServiceException(BizExceptionCode.OC_006, BizExceptionCode.OC_006_MSG);
            if (officeCapacityVTO.getEnrollmentOfficeId() == null)
                throw new ServiceException(BizExceptionCode.OC_003, BizExceptionCode.OC_003_MSG);
            officeCapacityTOList =
                    getOfficeCapacityDAO().findByEnrollmentOfficeIdAndShiftNoAndOcId(
                            officeCapacityVTO.getEnrollmentOfficeId()
                            , ShiftEnum.getShift(officeCapacityVTO.getShiftNo())
                            , officeCapacityVTO.getId());
            Integer previousDay = Integer.valueOf(convertGregorianToPersian(
                    getPreviousDay(officeCapacityVTO.getStartDate())).replace("/", ""));
            endDate = Integer.parseInt(EmsUtil.getProfileValue(
                    ProfileKeyName.KEY_OFFICE_CAPACITY_END_DATE, DEFAULT_OFFICE_CAPACITY_END_DATE));

            if (officeCapacityTOList != null) {
                for (OfficeCapacityTO officeCapacity : officeCapacityTOList) {
                    if (officeCapacity.getStartDate() == toDateWithoutSlash)
                        throw new ServiceException(BizExceptionCode.OC_007, BizExceptionCode.OC_007_MSG);
                    int currentIndex = officeCapacityTOList.indexOf(officeCapacity);
                    if (currentIndex == 0 && toDateWithoutSlash < officeCapacity.getStartDate()) {
                        endDate = Integer.valueOf(convertGregorianToPersian(getPreviousDay(
                                convertPersianToGregorian(String.valueOf(
                                        officeCapacity.getStartDate())
                                ))).replace("/", ""));
                        officeCapacityTO = updateOfficeCapacity(officeCapacityVTO, endDate);
                        previuosAdded = true;
                    }
                    if (currentIndex == officeCapacityTOList.size() - 1
                            && toDateWithoutSlash > officeCapacity.getStartDate()) {
                        officeCapacity.setEndDate(previousDay);
                        getOfficeCapacityDAO().update(officeCapacity);
                        officeCapacityTO = updateOfficeCapacity(officeCapacityVTO, endDate);
                    } else {
                        if (!previuosAdded) {
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
                                    officeCapacityTO = updateOfficeCapacity(officeCapacityVTO, endDate);
                                    previuosAdded = true;
                                }
                            }
                        }
                    }
                }
            } else {
                officeCapacityTO = updateOfficeCapacity(officeCapacityVTO, endDate);
            }

        } catch (BaseException e) {
            throw e;
        } catch (ParseException e) {
            throw new ServiceException(BizExceptionCode.OC_017, BizExceptionCode.OC_017_MSG, e);
        }
        return officeCapacityTO != null ? officeCapacityTO.getId() : null;

    }

    private OfficeCapacityTO updateOfficeCapacity(OfficeCapacityVTO officeCapacityVTO, int endDate) throws BaseException {
        OfficeCapacityTO officeCapacityTO = null;
        String startDate;
        Date date;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        try {
            officeCapacityTO = getOfficeCapacityDAO().find(OfficeCapacityTO.class, officeCapacityVTO.getId());
            if (officeCapacityTO == null)
                throw new ServiceException(BizExceptionCode.OC_002, BizExceptionCode.OC_002_MSG, new Long[]{officeCapacityVTO.getId()});
            startDate = convertPersianToGregorian(String.valueOf(officeCapacityTO.getStartDate()));
            date = sdf.parse(startDate.substring(0, 9));
            if (date.before(new Date()))
                throw new ServiceException(BizExceptionCode.OC_013, BizExceptionCode.OC_013_MSG);
            officeCapacityTO.setCapacity(Short.parseShort(officeCapacityVTO.getCapacity()));
            String startDateWithSlash = convertGregorianToPersian(officeCapacityVTO.getStartDate());
            officeCapacityTO.setStartDate(Integer.valueOf(startDateWithSlash.replace("/", "")));
            officeCapacityTO.setEndDate(endDate);
            officeCapacityTO.setWorkingHoursFrom(officeCapacityVTO.getWorkingHoursFrom());
            officeCapacityTO.setWorkingHoursTo(officeCapacityVTO.getWorkingHoursTo());
            officeCapacityTO.setShiftNo(ShiftEnum.getShift(officeCapacityVTO.getShiftNo()));
            officeCapacityTO = getOfficeCapacityDAO().update(officeCapacityTO);
        } catch (BaseException e) {
            throw e;
        } catch (ParseException e) {
            throw new ServiceException(BizExceptionCode.OC_015, BizExceptionCode.OC_015_MSG, e);
        }
        return officeCapacityTO;
    }

    @Override
    @Permissions(value = "ems_removeDepartment")
    @BizLoggable(logAction = "DELETE", logEntityName = "OFFICECAPACITY")
    public boolean remove(String officeCapacityIds) throws BaseException {
        try {
            if (officeCapacityIds == null || officeCapacityIds.trim().length() == 0) {
                throw new ServiceException(BizExceptionCode.DSI_005, BizExceptionCode.DSI_005_MSG);
            } else {
                for (String OfficeCapacityId : officeCapacityIds.split(",")) {
                    if ("1".equals(OfficeCapacityId))
                        throw new ServiceException(BizExceptionCode.DSI_035, BizExceptionCode.DSI_035_MSG);
                }
            }

            return getOfficeCapacityDAO().removeOfficeCapacities(officeCapacityIds);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.DSI_032, BizExceptionCode.GLB_008_MSG, e);
        }
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
        } catch (BaseException e) {
            throw e;
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
        if (endDateWithSlash.contains("/")) {
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
        return previousDay;
    }

    private String convertGregorianToPersian(String toDate) {
        String DELIMITER = "/";
        String year = toDate.substring(0, 4);
        String month = toDate.substring(5, 7);
        String day = toDate.substring(8, 10);
        String startDateWithSlash = day + DELIMITER + month + DELIMITER + year;
        return CalendarUtil.convertGregorianToPersian(startDateWithSlash);

    }

    private OfficeCapacityTO createOfficeCapacity(OfficeCapacityVTO officeCapacityVTO, int endDate) throws BaseException {
        OfficeCapacityTO capacityTO = new OfficeCapacityTO();
        EnrollmentOfficeTO enrollmentOfficeTO;
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
            capacityTO.setShiftNo(ShiftEnum.getShift(officeCapacityVTO.getShiftNo()));
            capacityTO = getOfficeCapacityDAO().create(capacityTO);
        } catch (ServiceException e) {
            throw new ServiceException(BizExceptionCode.OC_009, BizExceptionCode.OC_009_MSG, e);
        }
        return capacityTO;
    }

    public OfficeCapacityTO findByEnrollmentOfficeId(Long enrollmentOfficeId) throws BaseException {
        OfficeCapacityTO officeCapacityTO = null;
        try {
            officeCapacityTO = getOfficeCapacityDAO().findByEnrollmentOfficeId(enrollmentOfficeId);
        } catch (BaseException e) {
            throw e;
        }
        return officeCapacityTO;
    }

    @Override
    public List<OfficeCapacityTO> listOfficeCapacityByDate(int startDate, int endDate) throws BaseException {
        try {
            return getOfficeCapacityDAO().listOfficeCapacityByDate(startDate, endDate);
        } catch (BaseException e) {
            throw e;
        }
    }

    @Override
    public OfficeCapacityTO findByEnrollmentOfficeIdAndDateAndWorkingHour(Long enrollmentOfficeId) throws BaseException {
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat hours = new SimpleDateFormat("HH");
            SimpleDateFormat minutes = new SimpleDateFormat("mm");
            int min;
            int date = Integer.parseInt(CalendarUtil.convertGregorianToPersian(dateFormat.format(
                    new Date())).replaceAll("/", ""));
            min = Integer.parseInt(minutes.format(new Date())) >= 30 ? 5 : 0;
            float hour = Float.parseFloat(hours.format(new Date()) + '.' + min);
            return getOfficeCapacityDAO().findByEnrollmentOfficeIdAndDateAndWorkingHour(enrollmentOfficeId, date, hour);
        } catch (BaseException e) {
            throw e;
        }
    }

    private OfficeCapacityDAO getOfficeCapacityDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(EMSLogicalNames
                    .getDaoJNDIName(EMSLogicalNames.DAO_OFFICE_CAPACITY));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.OC_014,
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
