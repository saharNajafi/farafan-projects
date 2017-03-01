package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.nocr.ems.data.enums.AfterDeliveryRequestType;

/**
 * @author: Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public interface AfterDeliveryService extends Service {

    public void doAfterDelivery(long requestId, AfterDeliveryRequestType requestType, String complementaryData) throws BaseException;

    /**
     * @param certificate
     * @throws ServiceException
     * @author Sina Golesorkhi
     */
    public void doOCSPVerification(byte[] certificate) throws ServiceException;

}
