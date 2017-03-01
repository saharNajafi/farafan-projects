package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.ReportTO;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public interface ReportDAO extends EmsBaseDAO<ReportTO> {

    /**
     * The method findByAllAttributes is used to find an instance of type {@link ReportTO} with all it's attributes
     *
     * @param id is an instance of type {@link Long}, which represents an instance of type {@link ReportTO}
     * @return an instance of type {@link ReportTO} or null
     * @throws BaseException
     */
    ReportTO findByAllAttributes(Long id) throws BaseException;

}
