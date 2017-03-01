package com.gam.nocr.ems.data.dao.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.ReportFileTO;
import com.gam.nocr.ems.data.domain.ReportTO;
import com.gam.nocr.ems.util.EmsUtil;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
@Stateless(name = "ReportDAO")
@Local(ReportDAOLocal.class)
@Remote(ReportDAORemote.class)
public class ReportDAOImpl extends EmsBaseDAOImpl<ReportTO> implements ReportDAOLocal, ReportDAORemote {

    @Override
    @PersistenceContext(unitName = "EmsOraclePU")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    /**
     * The method findByAllAttributes is used to find an instance of type {@link com.gam.nocr.ems.data.domain.ReportTO} with all it's attributes
     *
     * @param id is an instance of type {@link Long}, which represents an instance of type {@link com.gam.nocr.ems.data.domain.ReportTO}
     * @return an instance of type {@link com.gam.nocr.ems.data.domain.ReportTO} or null
     * @throws com.gam.commons.core.BaseException
     *
     */
    @Override
    public ReportTO findByAllAttributes(Long id) throws BaseException {

        try {
            List<ReportTO> reportTOs = em.createQuery(" " +
                    "SELECT RPT " +
                    "FROM ReportTO RPT " +
                    "WHERE " +
                    "RPT.id = :REPORT_ID ", ReportTO.class)
                    .setParameter("REPORT_ID", id)
                    .getResultList();

            if (EmsUtil.checkListSize(reportTOs)) {
                ReportTO reportTO = reportTOs.get(0);
                reportTO.getReportFiles().size();
                return reportTO;
            }
            return null;
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.RPT_001, DataExceptionCode.GLB_005_MSG, e);
        }
    }
}
