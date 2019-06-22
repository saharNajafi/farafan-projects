package com.gam.nocr.ems.util;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.ServiceException;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.struts2.ServletActionContext;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by safiary on 6/22/19.
 */
public class JasperUtil {

    static final String ROOT_DIRECTORY = "jasper" + File.separator;

    /*public static void generatePDFWithDataSource(String jrxmlFileNameByItsPath, String pdfFileName, Map parameterMap, List datasource) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment;filename*='UTF-8'" + URLEncoder.encode(pdfFileName, "utf-8") + ".pdf");
            response.addHeader("Cache-Control", "no-cache");
            ClassLoader loader = JasperUtil.class.getClassLoader();
            InputStream reportStream = loader.getResourceAsStream(ROOT_DIRECTORY + jrxmlFileNameByItsPath);
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datasource);
            JasperExportManager.exportReportToPdfStream(JasperFillManager.fillReport(JasperCompileManager.compileReport(reportStream), parameterMap, dataSource), byteArrayOutputStream);
            response.setContentLength(byteArrayOutputStream.size());
            ServletOutputStream servletOutputStream = response.getOutputStream();
            byteArrayOutputStream.writeTo(servletOutputStream);
            byteArrayOutputStream.flush();
            servletOutputStream.flush();
            servletOutputStream.close();
            byteArrayOutputStream.close();
            reportStream.close();
            facesContext.responseComplete();
        } catch (JRException e) {
            e.printStackTrace();
        } catch (IOException iox) {
            iox.printStackTrace();
        }
    }*/

    public static void generatePDFWithOutDataSource(String jrxmlFileNameByItsPath, String pdfFileName, Map parameterMap) throws BaseException {
        try {
            HttpServletResponse response = ServletActionContext.getResponse();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment;filename*='UTF-8'" + URLEncoder.encode(pdfFileName, "utf-8") + ".pdf");
            response.addHeader("Cache-Control", "no-cache");
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream reportStream = classloader.getResourceAsStream(ROOT_DIRECTORY + jrxmlFileNameByItsPath);
            JasperReport jasReport = (JasperReport) JRLoader.loadObject(reportStream);
            JasperExportManager.exportReportToPdfStream(JasperFillManager.fillReport(jasReport, parameterMap, new JREmptyDataSource()), byteArrayOutputStream);
            response.setContentLength(byteArrayOutputStream.size());
            ServletOutputStream servletOutputStream = response.getOutputStream();
            byteArrayOutputStream.writeTo(servletOutputStream);
            byteArrayOutputStream.flush();
            servletOutputStream.flush();
            servletOutputStream.close();
            byteArrayOutputStream.close();
            reportStream.close();
        } catch (JRException e) {
            throw new ServiceException("Exception ", e);
        } catch (IOException e) {
            throw new ServiceException("Exception ", e);
        }
    }

}
