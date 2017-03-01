/**
 *
 */
package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.nocr.ems.data.domain.BusinessLogTO;
import com.gam.nocr.ems.data.enums.BusinessLogAction;
import com.gam.nocr.ems.data.enums.BusinessLogEntity;

/**
 * @author Sina Golesorkhi
 */
public interface BusinessLogService extends Service {

    /**
     * @param to
     * @throws BaseException
     */
    public void insertLog(BusinessLogTO to) throws BaseException;

    public void createBusinessLog(BusinessLogAction logAction,
                                  BusinessLogEntity logEntityName,
                                  String logActor,
                                  String additionalData,
                                  Boolean exceptionFlag) throws BaseException;

    /**
     * @param id
     * @return
     * @throws BaseException
     */
    public boolean verify(String id) throws BaseException;
}
