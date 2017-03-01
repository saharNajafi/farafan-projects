package com.gam.nocr.ems.web.ws;

import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.WebFault;

import org.slf4j.Logger;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.Internal;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.commons.security.SecurityException;
import com.gam.nocr.ems.biz.delegator.PersonDelegator;
import com.gam.nocr.ems.biz.delegator.TokenManagementDelegator;
import com.gam.nocr.ems.biz.service.PersonManagementService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.PersonTokenTO;
import com.gam.nocr.ems.data.domain.ws.SecurityContextWTO;
import com.gam.nocr.ems.data.enums.TokenState;
import com.gam.nocr.ems.data.enums.TokenType;

/**
 * Collection of services related to the card (e.g. delivering, after delivery service, etc.) are implemented in this
 * web service
 *
 * @author Hamid Adldoost (Adldoust@ipreez.ir)
 */

@WebFault
        (
                faultBean = "com.gam.nocr.ems.web.ws.InternalException"
        )
@WebService
        (
                serviceName = "TokenWS",
                portName = "TokenWSPort",
                targetNamespace = "http://ws.web.ems.nocr.gam.com/"
        )
@SOAPBinding
        (
                style = SOAPBinding.Style.DOCUMENT,
                use = SOAPBinding.Use.LITERAL,
                parameterStyle = SOAPBinding.ParameterStyle.WRAPPED
        )
@Internal
public class TokenWS extends EMSWS{

	private static final Logger logger = BaseLog.getLogger(TokenWS.class);
	

	private TokenManagementDelegator tokenDelegator = new TokenManagementDelegator();
	private PersonDelegator personDelegator = new PersonDelegator();
	
	@WebMethod
	public void saveRenewalRequest(
			@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO) throws InternalException {
		UserProfileTO userProfile = super.validateRequest(securityContextWTO);
		Long personID = null;
		personID = getPersoID(userProfile.getUserName());
		try {
				logger.info("Saving request for ");
				PersonTokenTO token = tokenDelegator
						.findPersonTokenByPersonAndTokenType(userProfile,
								TokenType.SIGNATURE);
				if (token == null) {
					throw new InternalException(WebExceptionCode.TKW_004_MSG
							+ personID,
							new EMSWebServiceFault(WebExceptionCode.TKW_004));
				}
				long existingTokenId = token.getId();
				personDelegator.renewalToken(userProfile, existingTokenId);
			}catch (BaseException e) {

			if (e instanceof SecurityException) {
				SecurityException ex = (SecurityException) e;
				String exceptionCode = ex.getExceptionCode();
				if (exceptionCode.contains("AUT_004"))
					throw new InternalException(WebExceptionCode.TKW_006_MSG,
							new EMSWebServiceFault(WebExceptionCode.TKW_006), e);
			}
			throw new InternalException(e.getMessage(), new EMSWebServiceFault(
					e.getExceptionCode(), e.getArgs()));
		} catch (Exception e) {
			if (e instanceof InternalException)
				throw (InternalException) e;
			throw new InternalException(WebExceptionCode.TKW_001_MSG
					+ personID, new EMSWebServiceFault(
					WebExceptionCode.TKW_001), e);
		}

	}
	
	@WebMethod
    public void removeRenewalRequest(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,@WebParam(name = "requestId") Long id) throws InternalException {
        UserProfileTO userProfile = super.validateRequest(securityContextWTO);
        Long personID = null;
		personID = getPersoID(userProfile.getUserName());
        try
        {
        	logger.info("removing request for token id : {}", id);
        	PersonTokenTO token = tokenDelegator.findPersonTokenById(userProfile, id);
        	if(token.getState() == TokenState.PENDING_FOR_EMS && token.getType() == TokenType.SIGNATURE && token.getPerson().getId() == personID)
        		personDelegator.removeRenewalTokenRequest(userProfile, token.getId());
        	else
        		throw new InternalException(WebExceptionCode.TKW_003_MSG +personID + token.getState() + " " + token.getTokenType().toString() + " " + token.getPerson().getId().toString(), new EMSWebServiceFault(WebExceptionCode.TKW_003));
        }
        catch(BaseException e)
        {
        	throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode(), e.getArgs()));
        }
        catch(Exception e)
        {
        	throw new InternalException(WebExceptionCode.TKW_002_MSG + personID, new EMSWebServiceFault(WebExceptionCode.TKW_002), e);
        }
	}
	
	@WebMethod
    public boolean hasTokenRenewalRequest(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO) throws InternalException {
        UserProfileTO userProfile = super.validateRequest(securityContextWTO);
        Long personID = null;
		personID = getPersoID(userProfile.getUserName());
        try
        {
        	logger.info("checking person has renewal request or not");
        	List<PersonTokenTO> tokenList = tokenDelegator.findPersonTokenRenewalRequest(userProfile);
        	if(tokenList == null || tokenList.isEmpty())
        		return false;
        	else if (tokenList.size() == 1)
        		return true;
        	else
        		throw new InternalException(WebExceptionCode.TKW_005_MSG+ personID, new EMSWebServiceFault(WebExceptionCode.TKW_005) );
        }
        catch(BaseException e)
        {
        	throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode(), e.getArgs()));
        }
        catch(Exception e)
        {
        	if(e instanceof InternalException)
        		throw (InternalException)e;
        	throw new InternalException(WebExceptionCode.TKW_002_MSG + personID, new EMSWebServiceFault(WebExceptionCode.TKW_002), e);
        }
	}
	
	
	private Long getPersoID(String username)
	{
		Long personID = null;
  		try {
			personID = getPersonService().findPersonIdByUsername(username);
		} catch (BaseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
  		return personID;
		
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
