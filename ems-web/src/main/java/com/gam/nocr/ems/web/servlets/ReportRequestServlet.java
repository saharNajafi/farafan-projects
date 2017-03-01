package com.gam.nocr.ems.web.servlets;

import com.gam.commons.core.BaseLog;
import com.gam.commons.core.data.dao.factory.DAOFactory;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.nocr.ems.biz.delegator.ReportRequestDelegator;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.dao.IMSBatchEnquiryDAO;
import com.gam.nocr.ems.data.domain.IMSBatchEnquiryTO;
import com.gam.nocr.ems.data.domain.ReportFileTO;
import com.gam.nocr.ems.data.domain.ReportRequestTO;
import com.gam.nocr.ems.data.domain.ReportTO;
import com.gam.nocr.ems.data.enums.BooleanType;
import com.gam.nocr.ems.data.enums.ReportFileType;
import com.gam.nocr.ems.data.enums.ReportOutputType;
import com.gam.nocr.ems.util.EmsUtil;
import gampooya.tools.BaseException;
import org.slf4j.Logger;
import web.info.TransferInfo;

import javax.jws.WebParam;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */

@WebServlet(name = "ReportRequestServlet", urlPatterns = "/ReportRequestServlet")
public class ReportRequestServlet extends HttpServlet {

    private static final Logger logger = BaseLog.getLogger(ReportRequestServlet.class);

    private byte[] getByteArray(InputStream inputStream) throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];

        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        return buffer.toByteArray();
    }

    //	generateRequestedReports
    private ReportRequestTO getReportRequestTO(Map<String, String> sentItems) {
        ReportRequestTO reportRequestTO = new ReportRequestTO();
        List<ReportFileTO> reportFileTOs = new ArrayList<ReportFileTO>();
        ReportTO reportTO = new ReportTO();
        reportTO.setComment("This is a sample report");
        reportTO.setActivateFlag(BooleanType.T);
        reportTO.setCreateDate(new Date());
        reportTO.setName(sentItems.get("name"));

        ReportFileTO reportFileTO = new ReportFileTO();
        ReportFileTO subReportFileTO1 = new ReportFileTO();
        ReportFileTO subReportFileTO2 = new ReportFileTO();

        reportRequestTO.setGenerateDate(new Date());
        reportRequestTO.setType(ReportOutputType.valueOf(sentItems.get("output")));
        if (EmsUtil.checkString(sentItems.get("params"))) {
            reportRequestTO.setParameters(sentItems.get("params"));
        }

        try {
            reportFileTO.setData(getByteArray(new FileInputStream(sentItems.get("path"))));
            reportFileTO.setType(ReportFileType.MASTER_REPORT);
            reportFileTO.setReportTO(reportTO);
            reportFileTOs.add(reportFileTO);

            if (EmsUtil.checkString(sentItems.get("subReportPath1"))) {
                subReportFileTO1.setData(getByteArray(new FileInputStream(sentItems.get("subReportPath1"))));
                subReportFileTO1.setType(ReportFileType.SUB_REPORT);
                subReportFileTO1.setCaption(sentItems.get("subReportCaption1"));
                subReportFileTO1.setReportTO(reportTO);
                reportFileTOs.add(subReportFileTO1);
            }
            if (EmsUtil.checkString(sentItems.get("subReportPath2"))) {
                subReportFileTO2.setData(getByteArray(new FileInputStream(sentItems.get("subReportPath2"))));
                subReportFileTO2.setType(ReportFileType.SUB_REPORT);
                subReportFileTO2.setCaption(sentItems.get("subReportCaption2"));
                subReportFileTO2.setReportTO(reportTO);
                reportFileTOs.add(subReportFileTO2);
            }
        } catch (Exception e) {
            logger.error(WebExceptionCode.GLB_ERR_MSG, e);
        }
        reportTO.getReportFiles().addAll(reportFileTOs);

        reportRequestTO.setReportTO(reportTO);
        return reportRequestTO;
    }

    private IMSBatchEnquiryDAO getBatchEnquiryDAO() throws com.gam.commons.core.BaseException {
        DAOFactory daoFactory = DAOFactoryProvider.getDAOFactory();
        IMSBatchEnquiryDAO imsBatchEnquiryDAO = null;
        try {
            imsBatchEnquiryDAO = (IMSBatchEnquiryDAO) daoFactory.getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_IMS_BATCH_ENQUIRY));
        } catch (DAOFactoryException e) {
            logger.error(e.getMessage(), e);
        }
        return imsBatchEnquiryDAO;
    }

    private TransferInfo download(
            @WebParam(name = "username") String username,
            @WebParam(name = "password") String password) throws BaseException {
        try {
            IMSBatchEnquiryTO imsBatchEnquiryTO = getBatchEnquiryDAO().findByReplyFlag(false);
            imsBatchEnquiryTO.setReplyFlag(true);
            getBatchEnquiryDAO().update(imsBatchEnquiryTO);
            TransferInfo transferInfo = new TransferInfo();
            transferInfo.setData(imsBatchEnquiryTO.getEnquiryInfo().getBytes());
            return transferInfo;
        } catch (com.gam.commons.core.BaseException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        try {
//            TransferInfo transferInfo = download("", "");
//            TransferInfoVTO transferInfoVTO = new TransferInfoVTO(transferInfo.getFilename(), transferInfo.getIndex(), transferInfo.getErrMsg(), transferInfo.getData());
//            new IMSDelegator().updateRequestsByBatchEnquiryResult(transferInfoVTO);

			Map<String, String> sentItems = new HashMap<String, String>();
			sentItems.put("name", request.getParameter("name"));
			sentItems.put("path", request.getParameter("path"));
			sentItems.put("subReportPath1", request.getParameter("subReportPath1"));
			sentItems.put("subReportCaption1", request.getParameter("subReportCaption1"));
			sentItems.put("subReportPath2", request.getParameter("subReportPath2"));
			sentItems.put("subReportCaption2", request.getParameter("subReportCaption2"));
			sentItems.put("params", request.getParameter("params"));
			sentItems.put("output", request.getParameter("output"));
			ReportRequestTO reportRequestTO = getReportRequestTO(sentItems);
			ReportRequestDelegator reportRequestDelegator = new ReportRequestDelegator();

			reportRequestDelegator.generateRequestedReportTest(reportRequestTO);

			response.setHeader("Content-Disposition", "attachment; filename=\"" + reportRequestTO.getReportTO().getName() + "\"");
			response.setContentType(ReportOutputType.getContentType(reportRequestTO.getType()));
			response.setContentLength(reportRequestTO.getResult().length);
			ServletOutputStream outputStream = response.getOutputStream();
			outputStream.write(reportRequestTO.getResult());
			outputStream.flush();
			outputStream.close();

        } catch (Exception e) {
            logger.error(WebExceptionCode.GLB_ERR_MSG, e);
        }
    }

}
