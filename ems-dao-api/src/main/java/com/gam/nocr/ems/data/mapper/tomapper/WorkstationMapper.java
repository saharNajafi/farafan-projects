package com.gam.nocr.ems.data.mapper.tomapper;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.WorkstationTO;
import com.gam.nocr.ems.data.domain.ws.WorkstationWTO;
import com.gam.nocr.ems.data.enums.WorkstationState;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class WorkstationMapper {

    public static WorkstationTO convert(WorkstationWTO workstationWTO) throws BaseException {
        WorkstationTO workstationTO = new WorkstationTO();

        if (EmsUtil.checkString(workstationWTO.getCode()))
            workstationTO.setCode(workstationWTO.getCode());
        if (EmsUtil.checkString(workstationWTO.getActivationCode()))
            workstationTO.setActivationCode(workstationWTO.getActivationCode());
        if (workstationWTO.getEnrollmentOfficeId() != null)
            workstationTO.setEnrollmentOfficeId(workstationWTO.getEnrollmentOfficeId());
        workstationTO.setState(WorkstationState.N);

        return workstationTO;
    }
}
