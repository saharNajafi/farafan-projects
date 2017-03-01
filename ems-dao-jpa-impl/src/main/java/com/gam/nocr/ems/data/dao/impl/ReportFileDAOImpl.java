package com.gam.nocr.ems.data.dao.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.ReportFileTO;
import com.gam.nocr.ems.data.enums.ReportOutputType;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
@Stateless(name = "ReportFileDAO")
@Local(ReportFileDAOLocal.class)
@Remote(ReportFileDAORemote.class)
public class ReportFileDAOImpl extends EmsBaseDAOImpl<ReportFileTO> implements ReportFileDAOLocal, ReportFileDAORemote {

    @Override
    @PersistenceContext(unitName = "EmsOraclePU")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    /**
     * The method findReportFilesByReportId is used to find a list of type {@link com.gam.nocr.ems.data.domain.ReportFileTO},
     * regarding to the parameter of reportId
     *
     * @param reportId is an instance of type {@link Long}, which represents the id belongs to an appropriate instance of
     *                 type {@link com.gam.nocr.ems.data.domain.ReportTO}
     * @return a list of type {@link com.gam.nocr.ems.data.domain.ReportFileTO}
     * @throws com.gam.commons.core.BaseException
     *
     */
    @Override
    public List<ReportFileTO> findReportFilesByReportId(Long reportId) throws BaseException {
        try {
            return em.createQuery("" +
                    "SELECT RPF " +
                    "FROM ReportFileTO RPF " +
                    "WHERE RPF.reportTO.id = :REPORT_ID", ReportFileTO.class)
                    .setParameter("REPORT_ID", reportId)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.RPF_001, DataExceptionCode.GLB_005_MSG, e);
        }
    }

    /**
     * The method findReportFilesByReportIdAndOutputType is used to fetch a list of type {@link com.gam.nocr.ems.data.domain.ReportFileTO},
     * regards to its reportId and requestType
     *
     * @param reportId is an instance of type {@link Long}, which represents an appropriate report
     * @param type     is an instance of type {@link com.gam.nocr.ems.data.enums.ReportOutputType}
     * @return a list of type {@link com.gam.nocr.ems.data.domain.ReportFileTO}
     * @throws com.gam.commons.core.BaseException
     *
     */
    @Override
    public List<ReportFileTO> findReportFilesByReportIdAndOutputType(Long reportId,
                                                                     ReportOutputType type) throws BaseException {
        try {
            return em.createQuery("" +
                    "SELECT RPF " +
                    "FROM ReportFileTO RPF " +
                    "WHERE RPF.reportTO.id = :REPORT_ID " +
                    "AND RPF.outputType = :REPORT_OUTPUT_TYPE ", ReportFileTO.class)
                    .setParameter("REPORT_ID", reportId)
                    .setParameter("REPORT_OUTPUT_TYPE", type)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.RPF_002, DataExceptionCode.GLB_005_MSG, e);
        }
    }
}
