package com.gam.nocr.ems.web.listreader.processor;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.commons.listreader.ListReaderException;
import com.gam.commons.listreader.ParameterProvider;
import com.gam.nocr.ems.biz.service.PersonManagementService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.WebExceptionCode;
import gampooya.tools.security.SecurityContextService;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * The list reader processor class for report results grid (in 3S)
 *
 * @author <a href="mailto:moghaddam@gamelectronics.com>Ehsan Zaery Moghaddam</a>
 */
public class ReportResultsProcessor extends EMSVLPListProcessor {

    private static final Logger logger = BaseLog.getLogger(ReportResultsProcessor.class);
	

    @Override
    protected String[] getCountQueryParts(ParameterProvider paramProvider) throws ListReaderException {
        return getQueryParts("count", paramProvider);
    }

    @Override
    protected String[] getMainQueryParts(ParameterProvider paramProvider) throws ListReaderException {
        return getQueryParts("main", paramProvider);
    }

    /**
     * Generates an appropriate query parts base on given query type
     *
     * @param queryType             Type of query ('count' or 'main')
     * @param parameterProvider     Container of all request parameter
     * @return                      List of query parts
     * @throws ListReaderException
     */
    private String[] getQueryParts(String queryType, ParameterProvider parameterProvider) throws ListReaderException{
        String[] finalResult = {queryType};
        try
        {
            //  The personID is the only parameter which is required to run the query. So retrieve the current user's
            //  person id and store it in parameterProvider to be used as query parameter
            Integer perId;
            Long personID = null;

            UserProfileTO uto = parameterProvider.getUserProfileTO();
            if (uto != null)
            {
            	try {
					personID = getPersonService().findPersonIdByUsername(uto.getUserName());
				} catch (BaseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                perId = Integer.valueOf("" + personID);
            }
            else {
                HttpServletRequest request = parameterProvider.getRequest();
                SecurityContextService scs = new SecurityContextService(request);
                try {
                    perId = scs.getCurrentPersonId();
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    perId = 0;
                }
            }

            parameterProvider.setAttribute("personID", perId);
        }
        catch(Exception e)
        {
            logger.error(WebExceptionCode.GLB_ERR_MSG, e);
        }

        return finalResult;
    }
    
    //Anbari
    private PersonManagementService getPersonService() throws BaseException {
        PersonManagementService personManagementService;
        try {
            personManagementService = (PersonManagementService) ServiceFactoryProvider.getServiceFactory().getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_PERSON),null);
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(BizExceptionCode.PDL_001, BizExceptionCode.GLB_002_MSG, e, EMSLogicalNames.SRV_PERSON.split(","));
        }
        return personManagementService;
    }
}
