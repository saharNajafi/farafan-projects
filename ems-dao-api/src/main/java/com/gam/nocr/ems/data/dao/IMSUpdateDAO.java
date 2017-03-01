package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.IMSUpdateTO;

import java.util.List;

/**
 * The interface IMSUpdateDAO is a temporary interface which is used for testing the IMS stub and will be omitted on
 * future
 *
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public interface IMSUpdateDAO extends EmsBaseDAO<IMSUpdateTO> {

    List<IMSUpdateTO> findAll() throws BaseException;

    List<IMSUpdateTO> findAllByState(IMSUpdateTO.State state) throws BaseException;

    IMSUpdateTO findByNationalId(String nationalId);

	IMSUpdateTO findByRequestId(String requestId) throws BaseException;
}
