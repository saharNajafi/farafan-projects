//package com.gam.nocr.ems.web.servlets;
//
//import com.gam.commons.core.BaseLog;
//import com.gam.commons.core.data.dao.factory.DAOFactory;
//import com.gam.commons.core.data.dao.factory.DAOFactoryException;
//import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
//import com.gam.nocr.ems.config.EMSLogicalNames;
//import com.gam.nocr.ems.config.WebExceptionCode;
//import com.gam.nocr.ems.data.dao.CitizenDAO;
//import org.slf4j.Logger;
//
//import javax.naming.Context;
//import javax.naming.InitialContext;
//import javax.naming.NamingException;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.Hashtable;
//
///**
// * @author Saeed Jalilian (jalilian@gamelectronics.com)
// */
//
//@WebServlet(name = "TestServlet", urlPatterns = "/TestServlet")
//public class TestServlet extends HttpServlet {
//
//    static Logger logger = BaseLog.getLogger(TestServlet.class);
//
//    protected void service(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        response.setContentType("text/html;charset=UTF-8");
//        PrintWriter out = response.getWriter();
//        try {
//            Hashtable<String, String> env = new Hashtable<String, String>();
//            env.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
//            env.put(Context.SECURITY_PRINCIPAL, "weblogic");
//            env.put(Context.SECURITY_CREDENTIALS, "welcome1");
//            env.put(Context.PROVIDER_URL, "t3://localhost:7001");
//
//            Context ic = new InitialContext(env);
//            DAOFactory daoFactory = (DAOFactory) DAOFactoryProvider.getDAOFactory();
//
//            CitizenDAO citizenDAO = null;
//            citizenDAO = daoFactory.getDAO(EMSLogicalNames.getDaoJNDIName("CitizenDAO"));
//            logger.info("EJB DAO From DAOFactory : " + citizenDAO);
//
//        } catch (NamingException e) {
//            logger.error(WebExceptionCode.GLB_ERR_MSG, e);
//        } catch (DAOFactoryException e) {
//            logger.error(WebExceptionCode.GLB_ERR_MSG, e);
//        } finally {
//            out.close();
//        }
//    }
//
//}
