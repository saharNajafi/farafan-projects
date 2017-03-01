package com.gam.nocr.ems.data.dao.impl;

import com.gam.nocr.ems.data.dao.EmsBaseDAO;
import com.gam.nocr.ems.data.dao.ReportDAO;
import com.gam.nocr.ems.data.domain.ReportTO;

import javax.ejb.Remote;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */

@Remote
public interface ReportDAORemote extends ReportDAO {
}
