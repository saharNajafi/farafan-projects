package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.CitizenInfoTO;

import java.util.HashMap;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 * @author: Haeri (haeri@gamelectronics.com)
 */
public interface CitizenInfoDAO extends EmsBaseDAO<CitizenInfoTO> {

    /**
     * The method updateCitizenInfoByRequestIdOfHistory is used to update a list of type {@link
     * com.gam.nocr.ems.data.domain.CitizenInfoTO} in spite of History requestId list
     *
     * @param citizenInfoHashMap is a {@link java.util.HashMap} of{@link java.util.HashMap <CitizenInfoTO, String>} which
     *                           carries the necessary attributes which are required for update
     */
    void updateCitizenInfoByRequestIdOfHistory(HashMap<CitizenInfoTO, String> citizenInfoHashMap) throws BaseException;

    /**
     * The method updateCitizenInfoByRequestHistory is used to update a list of type {@link CitizenInfoTO} in spite of
     * History requestId list
     *
     * @param citizenInfoHashMap is a {@link HashMap} of{@link HashMap<CitizenInfoTO, Long>} which carries the
     *                           necessary attributes which are required for update
     */
    void updateCitizenInfoByRequestHistory(HashMap<CitizenInfoTO, Long> citizenInfoHashMap) throws BaseException;
    
	CitizenInfoTO getCitizenInfoById(Long id) throws BaseException;
	
	String getBirthCertIssuePlaceByNid(String substring) throws BaseException;

}
