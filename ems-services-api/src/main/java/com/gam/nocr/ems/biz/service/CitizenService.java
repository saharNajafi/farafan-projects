package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.nocr.ems.data.domain.CitizenTO;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 8/8/18.
 */
public interface CitizenService extends Service{
    CitizenTO findByNationalId(String nationalId) throws BaseException;

    CitizenTO addCitizen(CitizenTO citizen) throws BaseException ;
}
