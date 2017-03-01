//package com.gam.nocr.ems.web.servlets;
//
//import com.gam.commons.core.BaseException;
//import com.gam.commons.core.BaseLog;
//import com.gam.commons.core.data.dao.factory.DAOFactoryException;
//import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
//import com.gam.nocr.ems.config.EMSLogicalNames;
//import com.gam.nocr.ems.config.WebExceptionCode;
//import com.gam.nocr.ems.data.dao.BatchDAO;
//import com.gam.nocr.ems.data.dao.CardRequestHistoryDAO;
//import com.gam.nocr.ems.data.domain.BatchTO;
//import com.gam.nocr.ems.data.domain.CardRequestHistoryTO;
//import com.gam.nocr.ems.data.domain.ws.CardInfo;
//import com.gam.nocr.ems.data.enums.SystemId;
//import com.gam.nocr.ems.web.ws.CMSWS;
//import gampooya.tools.date.DateUtil;
//import org.slf4j.Logger;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.List;
//
///**
//* @author: Haeri (haeri@gamelectronics.com)
//*/
//@WebServlet(name = "CMSServlet", urlPatterns = "/CMSServlet")
//public class CMSServlet extends HttpServlet {
//
//    private static final Logger logger = BaseLog.getLogger(CMSServlet.class);
//
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        processRequest(request, response);
//    }
//
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        processRequest(request, response);
//    }
//
//    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        PrintWriter out = response.getWriter();
//        try {
//            if (request.getParameter("method") == null) {
//                out.println("No method assigned to servlet");
//            } else if (request.getParameter("method").equals("loadRequestIds")) {
//                loadRequestIds(out);
//            } else if (request.getParameter("method").equals("loadBatchIds")) {
//                loadBatchIds(out);
//            } else if (request.getParameter("method").equals("submitBatch")) {
//                submitBatch(request, out);
//            } else if (request.getParameter("method").equals("submitBox")) {
//                submitBox(request, out);
//            } else {
//                out.println("Method '" + request.getParameter("method") + "' not found");
//            }
//
//        } catch (BaseException e) {
//            logger.error(WebExceptionCode.GLB_ERR_MSG, e);
//        } finally {
//            out.close();
//        }
//    }
//
//    private void loadRequestIds(PrintWriter out) throws BaseException {
//        List<CardRequestHistoryTO> results = getCardRequestHistoryDAO().findBySystemId(SystemId.CMS);
//        out.println("<option>  </option>");
//        for (CardRequestHistoryTO to : results) {
//            out.println("<option>" + to.getRequestID() + "</option>");
//        }
//    }
//
//    private void loadBatchIds(PrintWriter out) throws BaseException {
//        List<BatchTO> batches = getBatchDAO().findAll();
//        out.println("<option>  </option>");
//        for (BatchTO to : batches) {
//            out.println("<option>" + to.getCmsID() + "</option>");
//        }
//    }
//
//    private void submitBatch(HttpServletRequest request, PrintWriter out) {
//        try {
//            String batchId = request.getParameter("batId");
//            List<CardInfo> cards = new ArrayList<CardInfo>();
//            for (int i = 0; i < 10; i++) {
//                if (request.getParameter("reqId" + i) == null) {
//                    break;
//                }
//                CardInfo crd = new CardInfo();
//                crd.setRequestId(request.getParameter("reqId" + i));
//                String[] args = request.getParameter("cardInfo" + i).split(",");
//                if (args.length > 0) {
//                    crd.setCrn(args[0]);
//                }
//                if (args.length > 1) {
//                    crd.setIssuanceDate(new Timestamp(DateUtil.convert(args[1], DateUtil.GREGORIAN).getTime()));
//                }
//                if (args.length > 2) {
//                    crd.setShipmentDate(new Timestamp(DateUtil.convert(args[2], DateUtil.GREGORIAN).getTime()));
//                }
//                if (args.length > 3) {
//                    crd.setKeySetVersion(args[3]);
//                }
//                cards.add(crd);
//            }
//            try {
//                new CMSWS().batchProduction(batchId, cards);
//            } catch (Exception e) {
//                logger.error(WebExceptionCode.GLB_ERR_MSG, e);
//                return;
//            }
//        } catch (Exception e) {
//            logger.error(WebExceptionCode.GLB_ERR_MSG, e);
//            return;
//        }
//        out.println("Batch submitted successfully");
//    }
//
//    private void submitBox(HttpServletRequest request, PrintWriter out) {
//        try {
//            String boxId = request.getParameter("boxId");
//            List<String> batchIds = new ArrayList<String>();
//            for (int i = 0; i < 10; i++) {
//                if (request.getParameter("batId" + i) == null) {
//                    break;
//                }
//                batchIds.add(request.getParameter("batId" + i));
//            }
//            try {
//                new CMSWS().boxShipped(boxId, batchIds);
//            } catch (Exception e) {
//                logger.error(WebExceptionCode.GLB_ERR_MSG, e);
//                return;
//            }
//        } catch (Exception e) {
//            logger.error(WebExceptionCode.GLB_ERR_MSG, e);
//            return;
//        }
//        out.println("Box submitted successfully");
//    }
//
//    private CardRequestHistoryDAO getCardRequestHistoryDAO() {
//        CardRequestHistoryDAO cardRequestHistoryDAO = null;
//        try {
//            return cardRequestHistoryDAO = DAOFactoryProvider.getDAOFactory().getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_CARD_REQUEST_HISTORY));
//        } catch (DAOFactoryException e) {
//            logger.error(WebExceptionCode.GLB_ERR_MSG, e);
//        }
//        return cardRequestHistoryDAO;
//    }
//
//    private BatchDAO getBatchDAO() {
//        BatchDAO batchDAO = null;
//        try {
//            return batchDAO = DAOFactoryProvider.getDAOFactory().getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_BATCH));
//        } catch (DAOFactoryException e) {
//            logger.error(WebExceptionCode.GLB_ERR_MSG, e);
//        }
//        return batchDAO;
//    }
//
//
//}
