package com.gam.nocr.ems.web.servlets;

import com.gam.commons.core.biz.service.factory.ServiceFactory;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.nocr.ems.biz.service.AfterDeliveryService;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.util.EmsUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author <a href="mailto:haeri@gamelectronics.com.com">Nooshin Haeri</a>
 */
@WebServlet(name = "CardServlet", urlPatterns = "/CardServlet")
public class CardServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            testDeliver(out);
        } finally {
            out.close();
        }
    }

    private AfterDeliveryService getAfterDeliveryService() {
        ServiceFactory sf = ServiceFactoryProvider.getServiceFactory();
        try {
            return sf.getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_AFTER_DELIVERY), null);
        } catch (ServiceFactoryException e) {
            throw new RuntimeException("Error while trying to lookup RegistrationService", e);
        }
    }

    private void testDeliver(PrintWriter out) {
        out.println("Testing CardService.deliver(long requestId, String message, byte[] messageDigest) ...<br/>");

        AfterDeliveryService afterDeliveryService = getAfterDeliveryService();
        long requestId = 1250;
//        afterDeliveryService.deliver(requestId, "Card has been delivered", new byte[]{3, 3, 3, 3, 3});

        out.println("CardService.deliver(long requestId, String message, byte[] messageDigest) successful ...<br/>");
    }
}
