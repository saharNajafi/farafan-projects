package com.gam.nocr.ems.data.mapper.tomapper;

import gampooya.tools.date.DateFormatException;
import gampooya.tools.date.DateUtil;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.config.MapperExceptionCode;
import com.gam.nocr.ems.data.domain.ReportTO;
import com.gam.nocr.ems.data.domain.vol.ReportVTO;
import com.gam.nocr.ems.data.enums.BooleanType;

/**
 * Created by IntelliJ IDEA.
 * User: jalilian
 * Date: 5/8/13
 * Time: 5:42 PM
 */
public class ReportMapper {
    private ReportMapper() {
    }

    public static ReportTO convert(ReportVTO reportVTO) throws BaseException {
        ReportTO reportTO = new ReportTO();
        reportTO.setId(reportVTO.getId());
        reportTO.setName(reportVTO.getName());
        try {
            reportTO.setCreateDate(DateUtil.convert(reportVTO.getCreateDate(), DateUtil.JALALI));
        } catch (DateFormatException e) {
            throw new BaseException(MapperExceptionCode.RPM_001, MapperExceptionCode.GLB_001_MSG, e);
        }
        reportTO.setComment(reportVTO.getComment());
        reportTO.setPermission(reportVTO.getPermission());
        reportTO.setActivateFlag(BooleanType.valueOf(reportVTO.getActivationFlag()));
        return reportTO;
    }

    public static ReportVTO convert(ReportTO reportTO) throws BaseException {
        ReportVTO reportVTO = new ReportVTO();
        reportVTO.setId(reportTO.getId());
        reportVTO.setName(reportTO.getName());
        reportVTO.setCreateDate(DateUtil.convert(reportTO.getCreateDate(), DateUtil.JALALI));
        reportVTO.setComment(reportTO.getComment());
        reportVTO.setPermission(reportVTO.getPermission());
        reportVTO.setActivationFlag(reportTO.getActivateFlag().name());
        return reportVTO;
    }


}
