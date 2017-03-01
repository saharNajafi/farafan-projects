package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.BiometricTO;
import com.gam.nocr.ems.data.enums.BiometricType;

import java.util.List;

/**
 * @author: Haeri (haeri@gamelectronics.com)
 */
public interface BiometricDAO extends EmsBaseDAO<BiometricTO> {

    public List<BiometricTO> findByRequestIdAndType(long requestId, BiometricType bioType) throws BaseException;

    public int removeByRequestIdAndType(long requestId, BiometricType biometricType) throws BaseException;

    /**
     * The method removeFingersInfoByCitizenId is used to remove all the biometric data of type finger which belongs to a
     * specified citizen
     *
     * @param citizenId an instance of type {@link Long} which represents the id of a specified citizen
     * @return 1 if success, otherwise 0
     * @throws BaseException
     */
    public int removeFaceInfoByRequestId(long requestId) throws BaseException;
    
    int removeFingersInfoByCitizenId(Long citizenId) throws BaseException;

    int removeFaceInfoByCitizenId(Long citizenId) throws BaseException;

}
