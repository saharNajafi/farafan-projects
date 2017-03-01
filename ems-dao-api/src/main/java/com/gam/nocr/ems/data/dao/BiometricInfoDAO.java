package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.BiometricInfoTO;

/**
 * 
 * @author haghshenas
 *
 */
public interface BiometricInfoDAO extends EmsBaseDAO<BiometricInfoTO> {

	public Long checkBiometricInfo(Long requestId) throws BaseException;
	
	public int removeBiometricInfo(Long citizenId) throws BaseException;
	
//	public int removeBiometricInfoByCitizenId(String nid) throws BaseException;
	
	public BiometricInfoTO findByNid(String nid) throws BaseException;

	
}
