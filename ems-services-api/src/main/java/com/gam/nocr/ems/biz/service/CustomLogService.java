package com.gam.nocr.ems.biz.service;

import com.farafan.customLog.entities.CustomLogTo;
import com.farafan.customLog.enums.CustomLogAction;
import com.farafan.customLog.enums.CustomLogEntity;
import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;

/**
 * @author Mazaher Namjoofar
 */
public interface CustomLogService extends Service {

    /**
     * @param to
     * @throws BaseException
     */
    public void insertLog(CustomLogTo to) throws BaseException;

}
